package s.ahmadsq.shhi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Map;

public class delete_accountActivity extends AppCompatActivity {


    ListView listView ;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter ;
    DatabaseReference dref ;
    EditText emailEditText ;
    EditText passwordEditText ;
    String adminEmail;
    String adminPassword;
    FirebaseAuth mAuth;
    Button deleteButt ;
    FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


            }
        };



        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        listView = findViewById(R.id.listView);
        deleteButt = findViewById(R.id.deleteButt);

        mAuth = FirebaseAuth.getInstance();
        final String admin_id = mAuth.getCurrentUser().getUid();
        DatabaseReference admin_db = FirebaseDatabase.getInstance().getReference().child("account").child(admin_id);

        admin_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminEmail = dataSnapshot.child("email").getValue(String.class);
                adminPassword = dataSnapshot.child("password").getValue(String.class);
                Toast.makeText(getApplicationContext(), adminEmail+adminPassword,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        deleteButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });






        dref = FirebaseDatabase.getInstance().getReference().child("account");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectUserInfo((Map<String,Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void collectUserInfo (Map<String,Object> notification){


        ArrayList<String> userList = new ArrayList<>();
         listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,userList );
        listView.setAdapter(adapter);




        for (Map.Entry<String,Object>  entry : notification.entrySet()){

            Map singleUser = (Map) entry.getValue();
            String username = (String) singleUser.get("username");
            String email = (String) singleUser.get("email");
            String password = (String) singleUser.get("password");
            String emailPassword =username +" Email: "+ email +" Password: "+password;
            userList.add(emailPassword);

            list.add(notification.toString());
            adapter.notifyDataSetChanged();
        }



    }

    private void deleteUser (String email , String password){


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String user_id = mAuth.getCurrentUser().getUid();
                final DatabaseReference user_db = FirebaseDatabase.getInstance().getReference().child("account").child(user_id);
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           user_db.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       Toast.makeText(getApplicationContext()," and account information deleted from database",Toast.LENGTH_SHORT).show();
                                   }else {
                                       Toast.makeText(getApplicationContext()," Error on delete account information from database",Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                           mAuth.signInWithEmailAndPassword(adminEmail,adminPassword);
                           Toast.makeText(getApplicationContext(),"Account deleted",Toast.LENGTH_SHORT).show();
                       }else {
                           mAuth.signInWithEmailAndPassword(adminEmail,adminPassword);
                           Toast.makeText(getApplicationContext(),"Error deleteing user",Toast.LENGTH_SHORT).show();
                       }

                    }
                });
            } else {
                    mAuth.signInWithEmailAndPassword(adminEmail,adminPassword);
                    Toast.makeText(getApplicationContext(), "Email or password is wrong",Toast.LENGTH_SHORT).show();
                }
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