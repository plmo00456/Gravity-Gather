package com.wooreal.gravitygather.utils;

import com.google.gson.JsonObject;

public class comUtil {
    public static String isNullChk (String a, String b){
        if(a == null || a.equals("")) return b;
        else return a;
    }

    public static String hasIsNullChk (JsonObject jo, String key, String ret){
        String result = null;
        if (jo.has(key) && !jo.get(key).isJsonNull()) {
            result = jo.get(key).getAsString();
        }
        return result == null ? ret : result;
    }
}
