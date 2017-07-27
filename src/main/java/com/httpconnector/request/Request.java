package com.httpconnector.request;

import com.httpconnector.callback.CallableCallback;
import com.httpconnector.response.ResponseHandler;

import java.net.Proxy;

/**
 * Created by funmi on 7/1/17.
 * @author funmi
 */

public interface Request {

    /**
     * the object that will be sent to the server
     * @param obj
    * */
    public Request params(Object obj);

    /**
     * Set the key and the value for the header
     * for the specify url
     * @param key
     * @param value
     * */
    public Request header(String key,String value);

    /**
     * Set the MediaType for the Url
     * @param content
     * */
    public Request contentType(String content);

    /**
     * Set time out for the url
     * @param timeout
     * */
    public Request timeout(int timeout);


    /**
     * Set request proxy
     * @param proxy
     * */
    public Request proxy(Proxy proxy);

    /**
     * Set request callback
     * @param handler
     * */
    public Request handler(ResponseHandler handler);

    /**
     * Executing in background
     * */
    public CallableCallback execute();
}
