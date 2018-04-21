package s.ahmadsq.shhi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
         light1ToggleButt.setVisibility(View.GONE);
         light2ToggleButt = findViewById(R.id.light2toggleButton);
         light2ToggleButt.setVisibility(View.GONE);
         light1TextView = findViewById(R.id.light1textView);
         light1TextView.setVisibility(View.GONE);
         light2TextView = findViewById(R.id.light2textView);
         light2TextView.setVisibility(View.GONE);

        // check light state if light 1 is on set light 1 toggle button on
         lightState();
        // show Button if user have privilege otherwise hide it :)
         checkPrivilege();
         // on click listener for toggle button
        // we don't use on change state listener, because we change the state of the listener
        // and that cause of sending request every time the state is change
         toggleButt();


    }
    public void toggleButt (){


        light1ToggleButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (light1ToggleButt.isChecked()){
                    send_request("LED1","ON");
                }else if (!light1ToggleButt.isChecked()){
                    send_request("LED1","OFF");
                }
            }
        });


        light2ToggleButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (light2ToggleButt.isChecked()){
                    send_request("LED2","ON");
                }else if (!light2ToggleButt.isChecked()){
                    send_request("LED2","OFF");
                }
            }
        });

    }

    // check if user have access to control the light if is set the button visible otherwise set it invisible
    public void checkPrivilege (){
    try {
        user_id = mAuth.getCurrentUser().getUid();
        user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id);
        user_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String light1 = dataSnapshot.child("light1").getValue(String.class);
                String light2 = dataSnapshot.child("light2").getValue(String.class);
                if (light1.equals("yes")) {
                    light1ToggleButt.setVisibility(View.VISIBLE);
                    light1TextView.setVisibility(View.VISIBLE);
                } else {
                    light1ToggleButt.setVisibility(View.GONE);
                    light1TextView.setVisibility(View.GONE);
                }

                if (light2.equals("yes")) {
                    light2ToggleButt.setVisibility(View.VISIBLE);
                    light2TextView.setVisibility(View.VISIBLE);
                } else {
                    light2ToggleButt.setVisibility(View.GONE);
                    light2TextView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }catch (NullPointerException e){
        System.out.println(e);
    }

    }

    // get the state of light to set the toggle button to on or off
    public void lightState (){
        try {
            mAuth = FirebaseAuth.getInstance();
            DatabaseReference lighDb = FirebaseDatabase.getInstance().getReference().child("light");
            lighDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String light1 = dataSnapshot.child("light1").getValue(String.class);
                    String light2 = dataSnapshot.child("light2").getValue(String.class);
                    if (light1.equals("on")) {
                        light1ToggleButt.setChecked(true);
                    } else {
                        light1ToggleButt.setChecked(false);
                    }
                    if (light2.equals("on")) {
                        light2ToggleButt.setChecked(true);
                    } else {
                        light2ToggleButt.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (NullPointerException e){
            System.out.println(e);
        }
    }

    // request Arduino to control the light
    public void send_request (final String lightNo , final String command){

        StringRequest requset = new StringRequest(Request.Method.GET, "http://192.168.8.101/"+lightNo+"="+command, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error to connect the server", Toast.LENGTH_LONG).show();
            }
        });
        Singleton_Queue.getInstance(getBaseContext()).Add(requset);
            }


}
