package com.httpconnector.response;

import java.util.List;
import java.util.Map;

/**
 * Created by funmi on 7/1/17.
 */

public class ResponseData extends Response {

    private Object response;

    public ResponseData(Object response,int statusCode, Map<String, List<String>> headers) {
        super(statusCode, headers);
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }
}
