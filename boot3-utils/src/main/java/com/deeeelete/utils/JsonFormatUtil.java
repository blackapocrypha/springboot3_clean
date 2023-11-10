package com.deeeelete.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class JsonFormatUtil {
    private static Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public JsonFormatUtil() {
    }

    private static Gson getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static String objectToString(Object o) {
        return gson.toJson(o);
    }

    public static <T> T stringToObject(String str, Class<T> classOfT) {
        return gson.fromJson(str, classOfT);
    }

    public static <T> List<T> stringToArrayList(String str, Class<T> classOfT) {
        T[] ts = (T[]) getInstance().fromJson(str, TypeToken.getArray(classOfT).getType());
        List<T> list = new ArrayList();
        if (ts != null && ts.length > 0) {
            Object[] var4 = ts;
            int var5 = ts.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                T t = (T) var4[var6];
                list.add(t);
            }
        }

        return ts == null ? null : list;
    }

    private static class SingletonHolder {
        private static final Gson INSTANCE = (new GsonBuilder()).serializeNulls().disableHtmlEscaping().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        private SingletonHolder() {
        }
    }
}
