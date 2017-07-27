package com.httpconnector.http;

import android.content.Context;
import android.net.ConnectivityManager;

import com.httpconnector.serializer.JsonSerializer;
import com.httpconnector.util.NetworkListener;

/**
 * Created by funmi on 7/1/17.
 */

public class HttpBuilder {
    public static MakeRequest create(Context context){
        return new HttpUrlConnection(new JsonSerializer(),
                new NetworkListener((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)));
    }
}
