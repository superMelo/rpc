package com.qyf.rpc.utils;

public class StringUtil {

    public static boolean isEmpty(String text){
        if (text == null || "".equals(text)){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String... text){
        boolean empty = false;
        for (String s : text) {
            empty = isEmpty(s);
        }
        return !empty;
    }
}
