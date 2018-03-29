package s.ahmadsq.shhi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class adminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button clockButtAdmin;
    private Button notifiButtAdmin;
    private Button lightButtAdmin;
    private Button signoutButtAdmin;
    private Button manageButt;
    private TextView welcomeTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        clockButtAdmin = findViewById(R.id.clockButt2);
        notifiButtAdmin = findViewById(R.id.notificationButt2);
        lightButtAdmin = findViewById(R.id.lightButt2);
        signoutButtAdmin = findViewById(R.id.signoutButt2);
        manageButt = findViewById(R.id.manageButt);
        mAuth = FirebaseAuth.getInstance();
        welcomeTextView = findViewById(R.id.welcomeTextView);
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
        clock();
        notification();
        light();
        manage();
        signOut();



    }

    protected void light() {

        lightButtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent light = new Intent(getApplicationContext(), lightActivity.class);
                startActivity(light);
            }
        });
    }

    protected void notification() {

        notifiButtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent noti = new Intent(getApplicationContext(), notificationActivity.class);
                startActivity(noti);
            }
        });

    }

    protected void clock() {

        clockButtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clock = new Intent(getApplicationContext(), clockAlarmActivity.class);
                startActivity(clock);
            }
        });

    }

    protected void signOut() {

        signoutButtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("arduino");
                Intent signOut = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(signOut);
            }
        });


    }

    protected void manage() {

        manageButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent manage = new Intent(getApplicationContext(),manageActivity.class);
                  startActivity(manage);
            }
        });


    }
}
