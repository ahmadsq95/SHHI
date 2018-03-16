package s.ahmadsq.shhi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.toolbox.StringRequest;

public class lightActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
    }



    //checking if user have the privilege to control the light
    public void check_privilege () {

    }


    // request Arduino to control the light
    public void send_request (){

            }


}
