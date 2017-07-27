package com.httpconnector.exception;

import com.httpconnector.error.NetworkError;

/**
 * Created by funmi on 7/1/17.
 */

public class HttpConnectorException extends Exception {

    private NetworkError network;

    public HttpConnectorException(String msg, NetworkError networkError){
        super(msg);
        this.network = networkError;
    }

    public NetworkError getNetwork() {
        return network;
    }
}
