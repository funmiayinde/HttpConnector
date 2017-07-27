package com.httpconnector.serializer;

/**
 * Created by funmi on 7/1/17.
 */

public interface HttpSerializer {
    public String getContentType();
    public String onSerialize(Object object);
    public Object onDeserialize(String value,Class clazz);
}
