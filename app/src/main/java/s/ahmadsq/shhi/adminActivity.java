package s.ahmadsq.shhi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class adminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button clockButtAdmin = findViewById(R.id.clockButt2);
        Button notifiButtAdmin = findViewById(R.id.notificationButt2);
        Button lightButtAdmin = findViewById(R.id.lightButt2);
        Button signoutButtAdmin = findViewById(R.id.signoutButt2);
        Button manageButt = findViewById(R.id.manageButt);
        mAuth = FirebaseAuth.getInstance();
        clock(clockButtAdmin);
        notification(notifiButtAdmin);
        light(lightButtAdmin);
        manage(manageButt);
        signOut(signoutButtAdmin);

    }

    protected void light(Button lightButt) {

        lightButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent light = new Intent(getApplicationContext(), lightActivity.class);
                startActivity(light);
            }
        });
    }

    protected void notification(Button notificationButt) {

        notificationButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent noti = new Intent(getApplicationContext(), notificationActivity.class);
                startActivity(noti);
            }
        });

    }

    protected void clock(Button clockAlarmButt) {

        clockAlarmButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clock = new Intent(getApplicationContext(), clockAlarmActivity.class);
                startActivity(clock);
            }
        });

    }

    protected void signOut(Button signOutButt) {

        signOutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("arduino");
                Intent signOut = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(signOut);
            }
        });


    }

    protected void manage(Button manageButt) {

        manageButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent manage = new Intent(getApplicationContext(),manageActivity.class);
                  startActivity(manage);
            }
        });


    }
}
