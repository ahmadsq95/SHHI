package s.ahmadsq.shhi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class lightActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        ToggleButton light1ToggleButt = findViewById(R.id.light1toggleButton);
        ToggleButton light2ToggleButt = findViewById(R.id.light2toggleButton);


        mAuth = FirebaseAuth.getInstance();
        check_privilege();



    }





    //checking if user have the privilege to control the light
    public void check_privilege () {
        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id);
        current_user_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String light1 = dataSnapshot.child("light1").getValue(String.class);
                String light2 = dataSnapshot.child("light2").getValue(String.class);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    // request Arduino to control the light
    public void send_request (final String lightNo , final String command){

        StringRequest requset = new StringRequest(Request.Method.POST, "http://192.168.1.111", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // here get json object to know if light set on or off , just to know it
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error to connect the server", Toast.LENGTH_LONG).show();

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("light",lightNo);
                map.put("command",command);
                return map;
            }


        };
        Singleton_Queue.getInstance(getBaseContext()).Add(requset);

            }


}
