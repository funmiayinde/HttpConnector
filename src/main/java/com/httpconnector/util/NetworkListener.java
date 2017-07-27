package com.httpconnector.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by funmi on 7/1/17.
 */

public class NetworkListener implements Network {

    private ConnectivityManager connectivityManager;

    public NetworkListener(ConnectivityManager manager){
        this.connectivityManager  = manager;
    }

    @Override
    public boolean isOnline() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    public boolean isOffline() {
        return !isOnline();
    }
}
