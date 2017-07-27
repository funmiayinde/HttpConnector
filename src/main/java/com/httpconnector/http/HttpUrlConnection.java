package com.httpconnector.http;

import com.httpconnector.request.Request;
import com.httpconnector.request.UrlConnectionRequest;
import com.httpconnector.serializer.HttpSerializer;
import com.httpconnector.util.Network;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by funmi on 7/1/17.
 */

public class HttpUrlConnection implements MakeRequest {

    private final HttpSerializer serializer;
    private final Network network;


    public HttpUrlConnection(HttpSerializer serializer,Network network) {
        this.serializer = serializer;
        this.network = network;
    }


    @Override
    public Request get(String url) {
       return makeRequest(url,HttpMethod.GET);
    }

    @Override
    public Request post(String url) {
        return makeRequest(url,HttpMethod.POST);
    }

    @Override
    public Request put(String url) {
        return makeRequest(url,HttpMethod.PUT);
    }

    @Override
    public Request patch(String url) {
        return makeRequest(url,HttpMethod.PATCH);
    }

    @Override
    public Request delete(String url) {
        return makeRequest(url,HttpMethod.DELETE);
    }

    @Override
    public Request makeRequest(String url, String method) {
        try{
            return new UrlConnectionRequest(new URL(url),method,serializer,network);
        }catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
    }
}
