package com.qyf.rpc.utils;

import java.lang.reflect.Field;

public class ReflectUtils {

    public static void getObjByType(Field field, Object bean, Object o) {
        try {
            String val = (String) o;
            if (field.getType() == String.class) {
                field.set(bean, o);
            } else if (field.getType() == Integer.class) {
                field.setInt(bean, (Integer) o);
            } else if (field.getType() == int.class) {
                field.setInt(bean, Integer.parseInt(val));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
