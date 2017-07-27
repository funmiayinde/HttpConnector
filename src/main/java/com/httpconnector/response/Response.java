package com.httpconnector.response;

import java.util.List;
import java.util.Map;

/**
 * Created by funmi on 7/1/17.
 */

public class Response {

    private int statusCode;
    private Map<String,List<String>> headers;

    public Response(int statusCode,Map<String,List<String>> headers){
        this.statusCode = statusCode;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
