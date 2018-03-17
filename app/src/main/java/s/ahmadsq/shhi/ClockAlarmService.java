package s.ahmadsq.shhi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ClockAlarmService extends Service {

    MediaPlayer mp ;
    int startId ;
    Vibrator vibrator;
    boolean isRunning ;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get extra string from alarm receiver
        String state = intent.getExtras().getString("extra");







        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }


        // if there no alarming is running , and user pressed "set" button
        // alarming should run
        if (!this.isRunning && startId == 1){

            mp = MediaPlayer.create(this , R.raw.alarm_clock);
            mp.start();
            mp.setLooping(true);
            vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);


               vibrator.vibrate(800000000);

               // turn on house clock light
               requestArduinoLight("clock alarm", "on");

            this.isRunning = true;
            this.startId = 0 ;


            // notification

            // intent that go to clock alarm activity if we pressed the notification
            Intent notification_intent = new Intent (this.getApplicationContext(), clockAlarmActivity.class);
            // pending intent
            PendingIntent notification_pending_intent = PendingIntent.getActivities(this,0, new Intent[]{notification_intent}, 0);
            // make the notification
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.house)
                    .setContentTitle("SHHI")
                    .setContentText("Clack Alarm !")
                    .setAutoCancel(true)
                    .setContentIntent(notification_pending_intent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());


        }
        // if there alarming is running , and user pressed "unset" button
        // alarming should stop
        else if (this.isRunning && startId == 0){

            mp.stop();
            mp.reset();
            vibrator.cancel();
           // turn off house clock light
            requestArduinoLight("clock alarm", "off");
            isRunning = false;
            this.startId = 1 ;
        }

        // these are if the user pressed any button
        // just for bug-proof app :)

        // if there no alarming is running and user pressed "unset"
        // do nothing
        else if (!isRunning && startId == 0){

            this.isRunning = false ;
            this.startId = 0;



        }
        // if there alarming is running and user pressed "set"
        // do nothing
        else if (isRunning && startId == 1){

            this.isRunning = true ;
            this.startId = 1;
        }
        // if there an odd event and catch it


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        this.isRunning = false ;

    }

    public void requestArduinoLight (final String clockLight, final String command){


       StringRequest requset = new StringRequest(Request.Method.POST, "http://192.168.1.111", new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
                // here get json object to know if light set on or off , just to know it
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(),"Error to connect the server", Toast.LENGTH_LONG).show();

           }
       }){
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("light",clockLight);
                map.put("command",command);
                return map;
            }


       };
        Singleton_Queue.getInstance(getBaseContext()).Add(requset);
    }


}

