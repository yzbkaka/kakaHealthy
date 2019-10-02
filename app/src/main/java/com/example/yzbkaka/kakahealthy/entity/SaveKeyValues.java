package com.example.yzbkaka.kakahealthy.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by yzbkaka on 19-10-2.
 */

public class SaveKeyValues {  //使用SharedPreferences存储数据

    public static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void createSharePreferences(Context context) {
        String appName = context.getPackageName();
        Log.e("储存的文件名",appName);
        sharedPreferences = context.getSharedPreferences(appName, Context.MODE_WORLD_WRITEABLE);
        editor = sharedPreferences.edit();
    }


    public static boolean isUnCreate() {  //判断SharedPreferences是否被创建
        boolean result = (sharedPreferences == null) ? true : false;
        if (result) {
            Log.e("提醒", "sharedPreferences未被创建！");
        }
        return result;
    }


    public static boolean putStringValues(String key, String values) {  //存入String类型的值
        if (isUnCreate()) {
            return false;
        }
        editor.putString(key, values);
        return editor.commit();
    }


    public static String getStringValues(String key, String defValue) {  //取出String类型的值
        if (isUnCreate()) {
            return null;
        }
        String string_value = sharedPreferences.getString(key, defValue);
        return string_value;
    }


    public static boolean putIntValues(String key, int values) {  //存入int类型的值
        if (isUnCreate()) {
            return false;
        }
        editor.putInt(key, values);
        return editor.commit();
    }


    public static int getIntValues(String key, int defValue) {  //取出int类型的值
        if (isUnCreate()) {
            return 0;
        }
        int int_value = sharedPreferences.getInt(key, defValue);
        return int_value;
    }


    public static boolean putLongValues(String key, long values) {  //存入long类型的值
        if (isUnCreate()) {
            return false;
        }
        editor.putLong(key, values);
        return editor.commit();
    }


    public static long getLongValues(String key, long defValue) {  //取出long类型的值
        if (isUnCreate()) {
            return 0;
        }
        long long_value = sharedPreferences.getLong(key, defValue);
        return long_value;
    }


    public static boolean putFloatValues(String key, float values) {  //存入float类型的值
        if (isUnCreate()) {
            return false;
        }
        editor.putFloat(key, values);
        return editor.commit();
    }


    public static float getFloatValues(String key, float defValue) {  //取出float类型的值
        if (isUnCreate()) {
            return 0;
        }
        float float_value = sharedPreferences.getFloat(key, defValue);
        return float_value;
    }


    public static boolean putBooleanValues(String key, boolean values) {  //存入boolean类型的值
        if (isUnCreate()) {
            return false;
        }
        editor.putBoolean(key, values);
        return editor.commit();
    }


    public static boolean getFloatValues(String key, boolean defValue) {  //取出boolean类型的值
        if (isUnCreate()) {
            return false;
        }
        boolean boolean_value = sharedPreferences.getBoolean(key, defValue);
        return boolean_value;
    }


    public static boolean deleteAllValues() {  //清空数据
        if (isUnCreate()) {
            return false;
        }
        editor.clear();
        return editor.commit();
    }


    public static boolean removeKeyForValues(String key) {  //删除指定数据
        if (isUnCreate()) {
            return false;
        }
        editor.remove(key);
        return editor.commit();
    }
}
