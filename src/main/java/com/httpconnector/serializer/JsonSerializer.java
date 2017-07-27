package com.httpconnector.serializer;

import com.google.gson.Gson;

/**
 * Created by funmi on 7/1/17.
 */

public class JsonSerializer implements HttpSerializer{

    private Gson gson = new Gson();

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public String onSerialize(Object object) {
        return gson.toJson(object);
    }

    @Override
    public Object onDeserialize(String jsonStr, Class clazz) {
        return gson.fromJson(jsonStr,clazz);
    }
}
