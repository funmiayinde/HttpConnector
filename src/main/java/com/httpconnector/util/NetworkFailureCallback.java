package com.httpconnector.util;

import com.httpconnector.callback.ActionCallback;
import com.httpconnector.error.NetworkError;
import com.httpconnector.response.ResponseHandler;

/**
 * Created by funmi on 7/2/17.
 */

public class NetworkFailureCallback implements ActionCallback {
    private ResponseHandler handler;
    private NetworkError networkError;

    public NetworkFailureCallback(ResponseHandler handler, NetworkError networkError){
        this.handler = handler;
        this.networkError = networkError;
    }

    @Override
    public void call() {
        handler.onFailure(networkError);
    }
}
