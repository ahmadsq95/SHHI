package s.ahmadsq.shhi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class loginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ProgressBar spinner;
    Button loginButt;
    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButt = findViewById(R.id.loginButt);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        spinner = findViewById(R.id.progressBar);


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        spinner.setVisibility(View.GONE);
        loginButt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String email = emailEditText.getText().toString();
        if (email.matches(emailPattern)){
            if (passwordEditText.getText().length() >= 6){
                spinner.setVisibility(View.VISIBLE);
                login();
            }else {
                Toast.makeText(getApplicationContext(),"password should be at least 6 digit.",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Enter Valid email address.",Toast.LENGTH_SHORT).show();
        }

    }
});




    }

    public void login (){
        spinner.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        spinner.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(),passwordEditText.getText().toString()).addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    Toast.makeText(getBaseContext(),"sign in error",Toast.LENGTH_LONG).show();
                    spinner.setVisibility(View.GONE);
                }
                else {
                  try{
                      String user_id = mAuth.getCurrentUser().getUid();
                      DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("admin");
                      current_user_db.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                              String admin = dataSnapshot.getValue(String.class);
                              if (admin.equals("yes")) {
                                  spinner.setVisibility(View.GONE);
                                  FirebaseMessaging.getInstance().subscribeToTopic("arduino");
                                  Intent log = new Intent(getApplicationContext(), adminActivity.class);
                                  log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                  startActivity(log);
                              } else {
                                  spinner.setVisibility(View.GONE);
                                  FirebaseMessaging.getInstance().subscribeToTopic("arduino");
                                  Intent log = new Intent(getApplicationContext(), MainActivity.class);
                                  log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                  startActivity(log);
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
            }
        });

    }


}
