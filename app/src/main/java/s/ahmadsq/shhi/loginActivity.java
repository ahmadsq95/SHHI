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
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user!=null){
               Intent log = new Intent(getApplicationContext(),adminActivity.class);
               startActivity(log);

            }
            }
        };

        Button loginButt = findViewById(R.id.loginButt);
        final EditText emailEditText = findViewById(R.id.emailEditText);
        final EditText passwordEditText = findViewById(R.id.passwordEditText);
        final ProgressBar spinner;

        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);



        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spinner.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(),passwordEditText.getText().toString()).addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getBaseContext(),"sign in error",Toast.LENGTH_LONG).show();
                            spinner.setVisibility(View.GONE);
                        }
                        else {
                            Intent log = new Intent(getApplicationContext(),adminActivity.class);
                            startActivity(log);

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
