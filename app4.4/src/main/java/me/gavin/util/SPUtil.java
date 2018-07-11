package me.gavin.util;

import android.content.Context;

import me.gavin.inject.component.ApplicationComponent;

/**
 * SharedPreferences 数据存储工具类
 *
 * @author gavin.xiong
 */
public class SPUtil {

    private static String PREFERENCE = "PREFERENCE";

    /**
     * 存储字符串数据类型
     */
    public static void saveString(String key, String value) {
        ApplicationComponent
                .Instance
                .get()
                .getApplication()
                .getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    /**
     * 返回String类型数据，默认是空字符串；
     */
    public static String getString(String key) {
        return ApplicationComponent
                .Instance
                .get()
                .getApplication()
                .getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getString(key, "");
    }

    /**
     * 存储boolean数据类型
     */
    public static void saveBoolean(String key, boolean value) {
        ApplicationComponent
                .Instance
                .get()
                .getApplication()
                .getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    /**
     * 返回boolean类型数据，默认是false；
     */
    public static boolean getBoolean(String key) {
        return ApplicationComponent
                .Instance
                .get()
                .getApplication()
                .getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }

}
