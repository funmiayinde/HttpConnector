package com.httpconnector.callback;

import android.os.AsyncTask;

/**
 * Created by funmi on 7/1/17.
 */

public class AsyncTaskCallable implements CallableCallback {

    private AsyncTask asyncTask;

    public AsyncTaskCallable(AsyncTask asyncTask){
        this.asyncTask = asyncTask;
    }

    @Override
    public void onCancel() {
        asyncTask.cancel(false);
    }
}
