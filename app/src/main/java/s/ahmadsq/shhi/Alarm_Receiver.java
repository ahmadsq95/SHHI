package s.ahmadsq.shhi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ahmad on 3/6/2018.
 */

public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        // get extra string from intent
        String extra_string = intent.getExtras().getString("extra");

        Intent service_intent = new Intent(context,ClockAlarmService.class);

        // pass extra string from clockAlarmActivity to RingTonePlaying service
        service_intent.putExtra("extra",extra_string);
         context.startService(service_intent);
    }
}
