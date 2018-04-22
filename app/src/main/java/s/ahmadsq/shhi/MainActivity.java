package s.ahmadsq.shhi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);
        Button clockAlarmButt = findViewById(R.id.clockButt);
        Button notificationButt = findViewById(R.id.notificationButt);
        Button lightButt = findViewById(R.id.lightButt);
        Button signOutButt = findViewById(R.id.signoutButt);
        final TextView welcomeTextView = findViewById(R.id.welcomeTextView);
      try {
          String user_id = mAuth.getCurrentUser().getUid();
          DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("account").child(user_id);
          dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  String username = dataSnapshot.child("username").getValue(String.class);
                  welcomeTextView.setText(username);
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });

      }catch (NullPointerException e){
          System.out.println(e);
      }
        clock(clockAlarmButt);
        notification(notificationButt);
        light(lightButt);
        signOut(signOutButt);

    }

    protected void light (Button lightButt){

        lightButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent light = new Intent(getApplicationContext(),lightActivity.class);
                startActivity(light);
            }
        });
    }
    protected void notification (Button notificationButt){

        notificationButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent noti = new Intent(getApplicationContext(),notificationActivity.class);
                startActivity(noti);
            }
        });

    }
    protected void clock (Button clockAlarmButt ){

        clockAlarmButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clock = new Intent(getApplicationContext(),clockAlarmActivity.class);
                startActivity(clock);
            }
        });

    }
    protected void signOut (Button signOutButt){

        signOutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("arduino");
                Intent log = new Intent(getApplicationContext(),loginActivity.class);
                startActivity(log);


            }
        });


    }


}

