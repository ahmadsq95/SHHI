package s.ahmadsq.shhi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
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
    ToggleButton light1ToggleButt;
    ToggleButton light2ToggleButt;
    TextView light1TextView;
    TextView light2TextView;
    String user_id ;
    DatabaseReference user_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

         light1ToggleButt = findViewById(R.id.light1toggleButton);
         light2ToggleButt = findViewById(R.id.light2toggleButton);
         light1TextView = findViewById(R.id.light1textView);
         light2TextView = findViewById(R.id.light2textView);


         lightState();
         checkPrivilege();




         light1ToggleButt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    send_request("light1","on");

                }else if (!isChecked){
                    send_request("light1","off");
                }
            }
        });

        light2ToggleButt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    send_request("light2","on");
                }else if (!isChecked){
                    send_request("light2","off");
                }
            }
        });



    }


    public void checkPrivilege (){


        user_id = mAuth.getCurrentUser().getUid();
        user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id);
        user_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String light1 = dataSnapshot.child("light1").getValue(String.class);
                String light2 = dataSnapshot.child("light2").getValue(String.class);
                if (light1.equals("yes")){
                    light1ToggleButt.setVisibility(View.VISIBLE);
                    light1TextView.setVisibility(View.VISIBLE);
                }else {
                    light1ToggleButt.setVisibility(View.GONE);
                    light1TextView.setVisibility(View.GONE);
                }

                if (light2.equals("yes")){
                    light2ToggleButt.setVisibility(View.VISIBLE);
                    light2TextView.setVisibility(View.VISIBLE);
                }else {
                    light2ToggleButt.setVisibility(View.GONE);
                    light2TextView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

// get the state of light to set the toggle button to on or off
    public void lightState (){
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference lighDb = FirebaseDatabase.getInstance().getReference().child("light");
        lighDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String light1 = dataSnapshot.child("light1").getValue(String.class);
                String light2 = dataSnapshot.child("light2").getValue(String.class);
                if (light1.equals("on")){
                    light1ToggleButt.setChecked(true);
                }else {
                    light1ToggleButt.setChecked(false);
                }
                if (light2.equals("on")){
                    light2ToggleButt.setChecked(true);
                }else{
                    light2ToggleButt.setChecked(false);
                }
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
