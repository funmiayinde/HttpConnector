package com.httpconnector.callback;

/**
 * Created by funmi on 7/1/17.
 */

public interface CallableCallback {
    public static final CallableCallback EMPTY_CALLBACK = new CallableCallback() {
        @Override
        public void onCancel() {

        }
    };

    public void onCancel();
}

