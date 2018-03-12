package s.ahmadsq.shhi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ahmad on 3/6/2018.
 */

public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("we are on a receiver.", " Yay!");
    }
}
