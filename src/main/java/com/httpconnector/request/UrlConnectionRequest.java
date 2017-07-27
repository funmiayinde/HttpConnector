package com.httpconnector.request;

import android.os.AsyncTask;
import android.util.Log;

import com.httpconnector.callback.ActionCallback;
import com.httpconnector.callback.AsyncTaskCallable;
import com.httpconnector.callback.CallableCallback;
import com.httpconnector.error.NetworkError;
import com.httpconnector.exception.HttpConnectorAuthException;
import com.httpconnector.exception.HttpConnectorException;
import com.httpconnector.response.ResponseData;
import com.httpconnector.response.ResponseHandler;
import com.httpconnector.serializer.HttpSerializer;
import com.httpconnector.util.Network;
import com.httpconnector.util.NetworkFailureCallback;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by funmi on 7/1/17.
 */

public class UrlConnectionRequest implements Request {
    private static final String TAG = UrlConnectionRequest.class.getSimpleName();
    private static final int NORMAL_TIMEOUT = 60000;
    private Proxy proxy = Proxy.NO_PROXY;
    private int timeout = NORMAL_TIMEOUT;
    private String contentType;

    private ResponseHandler responseHandler = new ResponseHandler();
    private Map<String, String> headers = new HashMap<>();
    private Class clazzType;
    private Object data;
    private URL url;
    private String method;
    private HttpSerializer serializer;
    private Network network;


    public UrlConnectionRequest(URL url, String method, HttpSerializer serializer, Network network) {
        this.url = url;
        this.method = method;
        this.serializer = serializer;
        this.network = network;
    }


    @Override
    public Request params(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public Request header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    @Override
    public Request contentType(String content) {
        contentType = content;
        return this;
    }

    @Override
    public Request timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public Request proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    @Override
    public Request handler(ResponseHandler handler) {
        this.responseHandler = handler;
        clazzType = findCallbackType(handler);
        return this;
    }

    @Override
    public CallableCallback execute() {
        if (network.isOffline()) {
            responseHandler.onFailure(NetworkError.OFFLINE);
            responseHandler.onComplete();
            return CallableCallback.EMPTY_CALLBACK;
        }

        return new AsyncTaskCallable(new AsyncTask<Void, Void, ActionCallback>() {

            @Override
            protected ActionCallback doInBackground(Void... params) {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection(proxy);
                    initialize(connection);
                    sendData(connection);
                    final ResponseData response = readResponse(connection);
                    return new ActionCallback() {
                        @Override
                        public void call() throws JSONException {
                            if (response.getStatusCode() < 400) {
                                responseHandler.onSuccess(response.getResponse(), response);
                            } else {
                                responseHandler.onError((String) response.getResponse(), response);
                            }
                        }
                    };
                } catch (HttpConnectorException e) {

                    Log.e(TAG, e.getMessage());
                    return new NetworkFailureCallback(responseHandler, e.getNetwork());

                } catch (SocketTimeoutException ex) {

                    Log.e(TAG, ex.getMessage());
                    return new NetworkFailureCallback(responseHandler, NetworkError.TIMEOUT);

                } catch (ProtocolException protocolExp) {

                    Log.wtf(TAG, protocolExp.getMessage());
                    return new NetworkFailureCallback(responseHandler, NetworkError.UNSUPPORTED_METHOD);

                } catch (Throwable e) {

                    Log.wtf(TAG, e.getMessage());
                    return new NetworkFailureCallback(responseHandler, NetworkError.UNKNOWN);
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }

            }

            @Override
            protected void onPostExecute(ActionCallback actionCallback) {
                try {
                    actionCallback.call();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                responseHandler.onComplete();
            }
        }.execute());
    }

    public void initialize(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod(method);
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        setContentType(data, connection);
    }

    public void setContentType(Object obj, HttpURLConnection connection) {
        if (headers.containsKey("Content-Type"))
            return;
        if (contentType != null) {
            connection.setRequestProperty("Content-Type", contentType);
            return;
        }

        if (obj instanceof InputStream)
            connection.setRequestProperty("Content-Type", "application/octet-stream");
        else
            connection.setRequestProperty("Content-Type", serializer.getContentType());
    }

    public Class findCallbackType(ResponseHandler responseHandler) {
        Method[] methods = responseHandler.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("onSuccess")) {
                Class params = method.getParameterTypes()[0];
                if (!params.equals(Object.class)) {
                    return params;
                }
            }
        }
        return Object.class;
    }

    private void copyToStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[64 * 1024];
        int bytes;
        while ((bytes = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytes);
        }
    }

    private void sendData(HttpURLConnection connection) throws IOException {
        if (data == null)
            return;

        connection.setDoOutput(true);
        OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
        try {
            if (data instanceof InputStream) {

                copyToStream((InputStream) data, outputStream);

            } else if (data instanceof String) {
                OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
                Log.d(TAG, "SENT :" + data);
                writer.write((String) data);
                writer.flush();

            } else {
                OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
                String output = serializer.onSerialize(data);
                writer.write(output);
                writer.flush();
            }

        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    private String getString(InputStream inputStream) throws IOException {
        if (inputStream == null)
            return null;

        StringBuilder sb = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        int bytes;
        char[] buffer = new char[64 * 1024];
        while ((bytes = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, bytes);
        }
        return sb.toString();
    }

    private int getStatusCode(HttpURLConnection connection) throws IOException {
        try {
            return connection.getResponseCode();
        } catch (IOException e) {
            if (e.getMessage().equals("Received authentication challenge is null"))
                return 401;
            throw e;
        }
    }

    private ResponseData readResponse(HttpURLConnection connection) throws IOException, HttpConnectorAuthException {
        int statusCode = getStatusCode(connection);
        if (statusCode >= 500) {
            String response = getString(connection.getInputStream());
            Log.e(TAG, response);
            return new ResponseData(response, statusCode, connection.getHeaderFields());
        }

        if (statusCode >= 400) {
            return new ResponseData(getString(connection.getInputStream()), statusCode, connection.getHeaderFields());
        }

        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
        doValidation(connection);

        if (clazzType.equals(Void.class)) {
            return new ResponseData(null, statusCode, connection.getHeaderFields());
        }

        if (clazzType.equals(InputStream.class)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            copyToStream(inputStream, bos);
            return new ResponseData(new ByteArrayInputStream(bos.toByteArray()), statusCode, connection.getHeaderFields());
        }

        if (clazzType.equals(String.class)) {
            return new ResponseData(getString(inputStream), statusCode, connection.getHeaderFields());
        }

        String response = getString(inputStream);
        Log.d(TAG, "RECEIVED :" + response);
        return new ResponseData(serializer.onSerialize(response), statusCode, connection.getHeaderFields());
    }

    private void doValidation(HttpURLConnection connection) throws HttpConnectorAuthException {
        if (!url.getHost().equals(connection.getURL().getHost())) {
            throw new HttpConnectorAuthException("", null);
        }
    }


}
