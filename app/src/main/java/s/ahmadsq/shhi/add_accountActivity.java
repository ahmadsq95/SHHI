package s.ahmadsq.shhi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class add_accountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText Email;
    private  EditText Password;
    private EditText username;
    private CheckBox admin;
    private CheckBox light1;
    private CheckBox light2;
    private Button addButt;
    private ProgressBar spinner;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        mAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.emailAddAccountEditText);
        Password = findViewById(R.id.passwordAddAccountEditText);
        username = findViewById(R.id.usernameEditText);
        admin = findViewById(R.id.AdminCheckBox);
        light1 = findViewById(R.id.light1CheckBox);
        light2 = findViewById(R.id.light2CheckBox);
        addButt = findViewById(R.id.addButt);
        spinner = findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);


      // get admin email and password to sign in later after create new account
        String current_admin_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_admin_db = FirebaseDatabase.getInstance().getReference().child("account").child(current_admin_id);
        final String[] adminEmail = new String[1];
        final String[] adminPassword = new String[1];
        current_admin_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminEmail[0] = dataSnapshot.child("email").getValue(String.class);
                adminPassword[0] = dataSnapshot.child("password").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        addButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);

                final String email = Email.getText().toString();
                final String password = Password.getText().toString();


                // create the account
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(add_accountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getBaseContext(),"Account error",Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getBaseContext(),"account added",Toast.LENGTH_LONG).show();
                            String user_id = mAuth.getCurrentUser().getUid();
                           // add user Id to database
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id);
                            current_user_db.setValue(true);
                            // add email to database
                            current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("email");
                            current_user_db.setValue(email);
                           // add username to database
                            current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("username");
                            current_user_db.setValue(username.getText().toString());
                            // add password to database
                            current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("password");
                            current_user_db.setValue(Password.getText().toString());
                                // add admin or user to database
                            if (admin.isChecked()){
                                current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("admin");
                                current_user_db.setValue("yes");
                                spinner.setVisibility(View.GONE);
                            }else {
                                current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("admin");
                                current_user_db.setValue("no");
                                spinner.setVisibility(View.GONE);
                            }
                            // add light1 privilage to database
                            if (light1.isChecked()){
                                current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("light1");
                                current_user_db.setValue("yes");
                            }else{
                                current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("light1");
                                current_user_db.setValue("no");
                            }
                            // add light1 privilage to database
                            if (light2.isChecked()){
                                current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("light2");
                                current_user_db.setValue("yes");
                            }else{
                                current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id).child("light2");
                                current_user_db.setValue("no");
                            }
                        }
                    }
                });




                /*
                *
                *         because Fire base is signing in the new user automatically we have to sign out
                *         this new account and sign in with original account :)
                *
                 */

                // sign out from the new user
                mAuth.signOut();
                //sign in with original user
              //  EditText email1 = findViewById(R.id.emailEditText);
               // EditText password1 = findViewById(R.id.passwordEditText);


                        mAuth.signInWithEmailAndPassword(adminEmail[0],adminPassword[0]);
                            spinner.setVisibility(View.GONE);

            }
        });


    }

}
