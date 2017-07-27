package com.httpconnector.exception;

/**
 * Created by funmi on 7/1/17.
 */

public class HttpConnectorIllegalArgumentException extends IllegalArgumentException {

    public HttpConnectorIllegalArgumentException(String message){
        super(message);
    }

    public HttpConnectorIllegalArgumentException(String msg, Throwable throwable){
        super(msg,throwable);
    }

    public HttpConnectorIllegalArgumentException(){
        super();
    }
}
