package s.ahmadsq.shhi;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by ahmad on 3/6/2018.
 */

public class Singleton_Queue {
    private RequestQueue queue;

    private static Singleton_Queue singleton;

    private Context context;

    private Singleton_Queue(Context context) {
        this.context = context;
    }

    public static synchronized Singleton_Queue getInstance(Context context) {
        if (singleton == null) {
            singleton = new Singleton_Queue(context);
        }
        return singleton;

    }

    public RequestQueue getRequestQueu() {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }


    /*** To Add  the request into the qeueu */
    public void Add(StringRequest request) {

        getRequestQueu().add(request);

    }

}
