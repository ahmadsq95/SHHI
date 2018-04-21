package s.ahmadsq.shhi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;


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
        try {
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

        }catch (NullPointerException e){
            System.out.println(e);
        }
        // if there no alarming is running , and user pressed "set" button
        // alarming should run
        if (!this.isRunning && startId == 1){

            mp = MediaPlayer.create(this , R.raw.alarm_clock);
            mp.start();
            mp.setLooping(true);

            vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

            int vab = 300;
            int gap = 300;
            long[] pattern = {
                    0,  // Start immediately
                    vab, gap
            };
       try{
           vibrator.vibrate(pattern,1); // 1 is for repeat like a loop
       }catch (NullPointerException e){
           System.out.println(e);
       }




               // turn on house clock light
               requestArduinoLight("ON");

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
                    .setPriority(PRIORITY_MAX)
                    .setContentIntent(notification_pending_intent);


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
           try {
               notificationManager.notify(0, notificationBuilder.build());
           }catch (NullPointerException e){
               System.out.println(e);
           }




        }
        // if there alarming is running , and user pressed "unset" button
        // alarming should stop
        else if (this.isRunning && startId == 0){

            mp.stop();
            mp.reset();
            vibrator.cancel();
           // turn off house clock light
            requestArduinoLight("OFF");
            isRunning = false;
            this.startId = 1 ;
        }

        /*

        these are if the user pressed any button
        just for bug-proof app :)
        if there no alarming is running and user pressed "unset"
        do nothing

        */

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

    public void requestArduinoLight(final String command){


       StringRequest requset = new StringRequest(Request.Method.GET, "http://192.168.8.101/"+ "LED3" +"="+command, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
                // here get json object to know if light set on or off , just to know it
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(),"Error to connect the server", Toast.LENGTH_LONG).show();

           }
       });
        Singleton_Queue.getInstance(getBaseContext()).Add(requset);
    }

}

