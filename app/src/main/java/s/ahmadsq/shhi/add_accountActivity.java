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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_accountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    //Intent log = new Intent(getApplicationContext(),adminActivity.class);
                    //startActivity(log);
                }
            }
        };

         final EditText Email = findViewById(R.id.emailAddAccountEditText);
        final EditText Password = findViewById(R.id.passwordAddAccountEditText);
        final CheckBox admin = findViewById(R.id.AdminCheckBox);
        Button addButt = findViewById(R.id.addButt);
        final ProgressBar spinner = findViewById(R.id.progressBar2);
        spinner.setVisibility(View.GONE);


        addButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);

                final String email = Email.getText().toString();
                final String password = Password.getText().toString();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(add_accountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getBaseContext(),"add Account error",Toast.LENGTH_LONG).show();
                            spinner.setVisibility(View.GONE);
                        }else{
                            Toast.makeText(getBaseContext(),"account added",Toast.LENGTH_LONG).show();
                            String user_id = mAuth.getCurrentUser().getUid();
                            if (admin.isChecked()){
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child("admin").child(user_id);
                                current_user_db.setValue(true);
                                spinner.setVisibility(View.GONE);
                            }else {
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("account").child("user").child(user_id);
                                current_user_db.setValue(true);
                                spinner.setVisibility(View.GONE);
                            }
                        }
                    }
                });

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
