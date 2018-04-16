package s.ahmadsq.shhi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class notificationActivity extends AppCompatActivity {

    DatabaseReference dref ;
    ListView listView ;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);



        dref = FirebaseDatabase.getInstance().getReference().child("notification");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              collectNotificationInfo((Map<String,Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void collectNotificationInfo (Map<String,Object> notification){

        ArrayList<String> notificationList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, notificationList);
        listView.setAdapter(adapter);
        for (Map.Entry<String,Object> entry : notification.entrySet()){
            Map singleNotification = (Map) entry.getValue();
            notificationList.add((String) singleNotification.get("Type"));
            list.add(notification.toString());
            adapter.notifyDataSetChanged();
        }

    }
}
