package me.gavin.util;

import android.util.Log;

import me.gavin.BuildConfig;

/**
 * 日志工具
 *
 * @author gavin.xiong 2016/9/10
 */
public class L {

    private static final String TAG = "gavin";
    private static final String NULL = ">NULL<";
    private static int level = BuildConfig.LOG_DEBUG ? Log.VERBOSE : Log.ERROR;

    public static <T> T v(T t) {
        return v(TAG, t);
    }

    public static <T> T v(String TAG, T t) {
        if (level <= Log.VERBOSE) Log.v(TAG, t == null ? NULL : t.toString());
        return t;
    }

    public static <T> T d(T t) {
        return d(TAG, t);
    }

    public static <T> T d(String TAG, T t) {
        if (level <= Log.DEBUG) Log.d(TAG, t == null ? NULL : t.toString());
        return t;
    }

    public static <T> T i(T t) {
        return i(TAG, t);
    }

    public static <T> T i(String TAG, T t) {
        if (level <= Log.INFO) Log.i(TAG, t == null ? NULL : t.toString());
        return t;
    }

    public static <T> T w(T t) {
        return w(TAG, t);
    }

    public static <T> T w(String TAG, T t) {
        if (level <= Log.WARN) Log.w(TAG, t == null ? NULL : t.toString());
        return t;
    }

    public static <T> T e(T t) {
        return e(TAG, t);
    }

    public static <T> T e(String TAG, T t) {
        if (level <= Log.ERROR) Log.e(TAG, t == null ? NULL : t.toString());
        return t;
    }

}
