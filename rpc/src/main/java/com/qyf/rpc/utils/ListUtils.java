package com.qyf.rpc.utils;

import java.util.List;

public class ListUtils {

    public static boolean emtry(List list){
        if (list == null || list.size() == 0){
            return true;
        }
        return false;
    }

}
