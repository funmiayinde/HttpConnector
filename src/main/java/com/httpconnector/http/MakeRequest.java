package com.httpconnector.http;

import com.httpconnector.request.Request;

/**
 * Created by funmi on 7/1/17.
 */

public interface MakeRequest {
    public Request get(String url);
    public Request post(String url);
    public Request put(String url);
    public Request patch(String url);
    public Request delete(String url);
    public Request makeRequest(String url,String method);


}
