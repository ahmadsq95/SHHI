package s.ahmadsq.shhi;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class clockAlarmActivity extends AppCompatActivity {


    AlarmManager alarmManager ;
    TimePicker alarm_timpicker ;
    TextView time_update_text ;
    Context context;
    PendingIntent pending_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_alarm);
        this.context = this ;
        // initialize alarm manager
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        // initialize time picker
        alarm_timpicker = findViewById(R.id.timePicker);
        // initialize text update box
        time_update_text = findViewById(R.id.time_update_text);
        // Create an instance of calender
        final Calendar calendar = Calendar.getInstance();
        //  initialize a vibrator

        // initialize intent to the Alarm receiver
        final Intent AlarmReceiver_intent = new Intent(this.context, Alarm_Receiver.class);






        // initialize set button
        Button setButt = findViewById(R.id.setButt);

        // on click listener for set button
        setButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                // setting time that user input to calendar instance
                    calendar.set(Calendar.HOUR_OF_DAY, alarm_timpicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE,alarm_timpicker.getCurrentMinute());

                int hour = alarm_timpicker.getCurrentHour();
                int minute = alarm_timpicker.getCurrentMinute();
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);
                String AM_PM;

                // change 24 Hour mode to 12 Hour mood and get AM amd PM
                if (hour > 12){
                    hour_string = String.valueOf(hour - 12);
                    AM_PM = "PM";
                }else{
                    AM_PM = "AM";
                }

                // add 0 to minute from 10:3 to 10:30
                if (minute < 10) {
                    minute_string = "0" + String.valueOf(minute);

                }
                    set_alarm_text("alarm set to "+hour_string+":"+minute_string+" "+AM_PM);
                // put extra string into AlarmRecevier_intent
                // tells the clock that you pressed the "set" button
                AlarmReceiver_intent.putExtra("extra","alarm on");



                // create a pending intent that delay intent
                // until the specified time
                pending_intent = PendingIntent.getBroadcast(clockAlarmActivity.this,0, AlarmReceiver_intent,PendingIntent.FLAG_UPDATE_CURRENT);

                // set the alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);


            }
        });


            // initialize unset button
        Button unsetButt = findViewById(R.id.unsetButt);
        //  on click listener for unset button

        unsetButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // cancel the alarm
                alarmManager.cancel(pending_intent);
                set_alarm_text("alarm off !");

                // put extra string into AlarmRecevier_intent
                // tells the clock that you pressed the "unset" button
                AlarmReceiver_intent.putExtra("extra","alarm off");
                // stop the alarm when it alarming
                sendBroadcast(AlarmReceiver_intent);
            }
        });

           }

    public void set_alarm_text(String text) {
        time_update_text.setText(text);
    }
}
