package com.httpconnector.util;

import android.support.annotation.NonNull;

import java.io.FilterOutputStream;
import java.io.OutputStream;

/**
 * Created by funmi on 7/3/17.
 */

public final class CountingStream extends FilterOutputStream {

    private final static ProgressListener progressListener = null;
    
    
    public CountingStream(OutputStream out) {
        super(out);
    }
    
    public static interface ProgressListener{
        void transferred(long num);
    }
    
    
}
