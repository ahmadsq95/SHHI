package s.ahmadsq.shhi;

import android.accounts.Account;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Map;

public class delete_accountActivity extends AppCompatActivity {


    ListView listView ;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter ;
    DatabaseReference dref ;

    String adminEmail;
    String adminPassword;
    FirebaseAuth mAuth;


    ProgressBar spinner ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        spinner = findViewById(R.id.progressBar10);
        spinner.setVisibility(View.GONE);





        listView = findViewById(R.id.listView);


        mAuth = FirebaseAuth.getInstance();
        final String admin_id = mAuth.getCurrentUser().getUid();
        DatabaseReference admin_db = FirebaseDatabase.getInstance().getReference().child("account").child(admin_id);

        admin_db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminEmail = dataSnapshot.child("email").getValue(String.class);
                adminPassword = dataSnapshot.child("password").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





             getData();

    }

    public void getData (){

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

        final ArrayList<String> usernameList = new ArrayList<>();
        final ArrayList<String> emailList = new ArrayList<>();
        final ArrayList<String> passwordList = new ArrayList<>();
         listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,usernameList );
        listView.setAdapter(adapter);




        for (Map.Entry<String,Object>  entry : notification.entrySet()){

            Map singleUser = (Map) entry.getValue();
            String username = (String) singleUser.get("username");
            String email = (String) singleUser.get("email");
            String password = (String) singleUser.get("password");


            usernameList.add(username);
            emailList.add(email);
            passwordList.add(password);
            list.add(notification.toString());
            adapter.notifyDataSetChanged();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email = emailList.get(position);
                String password = passwordList.get(position);

                Toast.makeText(getApplicationContext(),email+" "+password,Toast.LENGTH_SHORT).show();
                AlertDialog diaBox = AskOption((String) listView.getItemAtPosition(position), email,password);
                diaBox.show();

            }
        });




    }


    private AlertDialog AskOption(final String user,final String email, final String password) {
               AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                       .setTitle("Delete user")
                       .setMessage("Do you want to Delete "+user+" ?")
                       .setIcon(R.drawable.delete)
                       .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int whichButton) {

                                        deleteUser(email, password);
                                        dialog.dismiss();

                                            }})

                       .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();

                                            }
              })
                             .create();
                return myQuittingDialogBox;

                    }

    private void deleteUser (String email , String password){
        spinner.setVisibility(View.VISIBLE);

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
                           Toast.makeText(getApplicationContext(),"Account deleted",Toast.LENGTH_SHORT).show();
                           Toast.makeText(getApplicationContext(),"Trying to delete user information from database...",Toast.LENGTH_SHORT).show();
                           mAuth.signInWithEmailAndPassword(adminEmail,adminPassword);
                           user_db.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       spinner.setVisibility(View.GONE);
                                       Toast.makeText(getApplicationContext(),"...and account information deleted from database",Toast.LENGTH_SHORT).show();
                                   }else {
                                       spinner.setVisibility(View.GONE);
                                       Toast.makeText(getApplicationContext()," Error on delete account information from database",Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });


                       }else {
                           mAuth.signInWithEmailAndPassword(adminEmail,adminPassword);
                           Toast.makeText(getApplicationContext(),"Error deleteing user",Toast.LENGTH_SHORT).show();

                           spinner.setVisibility(View.GONE);
                       }

                    }
                });
            } else {
                    mAuth.signInWithEmailAndPassword(adminEmail,adminPassword);
                    Toast.makeText(getApplicationContext(), "Email or password is wrong",Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.GONE);
                }
        }

        });

    }

}