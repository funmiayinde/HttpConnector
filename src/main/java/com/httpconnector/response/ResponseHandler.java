package com.httpconnector.response;

import com.httpconnector.error.NetworkError;

import org.json.JSONException;

/**
 * Created by funmi on 7/1/17.
 */

public class ResponseHandler<T> {

    /**
     * Return successful callback
     * @param data
     * @param response
     * */
    public void onSuccess(T data,Response response) throws JSONException {}

    /**
     * Return error callback if connection was not successful
     * when status code is >= 400
     * @param message
     * @param response
     * */
    public void onError(String message,Response response){}

    /**
     * Return failure callback ,if (offline,
     * @param networkError
     * */
    public void onFailure(NetworkError networkError){}

    /**
     * Notifies when request completed
     * */
    public void onComplete(){}
}
