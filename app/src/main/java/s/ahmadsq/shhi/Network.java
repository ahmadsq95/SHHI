package s.ahmadsq.shhi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ahmad on 3/6/2018.
 */

public class Network {

    /*** Check WI-FI Connection */
    public static final boolean isConnected(Context context) {
        /*** Access WI-FI Premission*/
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
