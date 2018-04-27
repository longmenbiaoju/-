package com.zzs.meizitu.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zzstar
 * @data 2018/2/10
 */

public class SpUtils {
    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("shared_files",
                Context.MODE_PRIVATE);
    }
    public static String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public static void putInt(String key, int data) {
        sharedPreferences.edit().putInt(key, data).apply();
    }

    public static int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public static void putBoolean(String key, boolean data) {
        sharedPreferences.edit().putBoolean(key, data).apply();
    }

    public static boolean getBoolean(String key, boolean defaultData) {
        return sharedPreferences.getBoolean(key, defaultData);
    }

    public static void putFloat(String key, float data) {
        sharedPreferences.edit().putFloat(key, data).apply();
    }

    public static float getFloat(String key, float defaultData) {
        return sharedPreferences.getFloat(key, defaultData);
    }

    public static void putLong(String key, long data) {
        sharedPreferences.edit().putLong(key, data).apply();
    }

    public static float getLong(String key, long defaultData) {
        return sharedPreferences.getLong(key, defaultData);
    }

    public static void clear() {
        sharedPreferences.edit().clear().apply();
    }

}
