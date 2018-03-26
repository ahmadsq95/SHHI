package s.ahmadsq.shhi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        listView = findViewById(R.id.listView);


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
            userList.add((String) singleUser.get("username"));

            // here will add the all data in listView ((NEED TO MODIFY IT))
            list.add(notification.toString());
            adapter.notifyDataSetChanged();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
}
