package com.httpconnector.exception;

import com.httpconnector.error.NetworkError;

/**
 * Created by funmi on 7/1/17.
 */

public class HttpConnectorAuthException extends HttpConnectorException {


    public HttpConnectorAuthException(String msg, NetworkError networkError) {
        super("Authentication is required", NetworkError.AUTHENTICATION_ERROR);

    }
}
