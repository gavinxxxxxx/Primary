package me.gavin.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.gavin.primary.BuildConfig;

/**
 * 日志（log）工具类
 *
 * @author gavin.xiong 2016/9/10
 */
public class L {

	private static final String TAG = "gavin";
	private static final String NULL = "<null>";
	private static final String HEADER = "";
	private static final String FOOTER = "";
	private static final int V = 1;
	private static final int D = 2;
	private static final int I = 3;
	private static final int W = 4;
	private static final int E = 5;
	private static int Z = BuildConfig.LOG_DEBUG ? V : E;

	public static void initLevel(int level) {
		Z = level;
	}

	public static void v(Object obj) {
		v(TAG, obj);
	}

	public static void v(String TAG, Object obj) {
		if (Z <= V)
			Log.v(TAG, HEADER + null == obj ? NULL : obj + FOOTER);
	}

	public static void d(Object obj) {
		d(TAG, obj);
	}

	public static void d(String TAG, Object obj) {
		if (Z <= D)
			Log.d(TAG, HEADER + null == obj ? NULL : obj + FOOTER);
	}

	public static void i(Object obj) {
		i(TAG, obj);
	}

	public static void i(String TAG, Object obj) {
		if (Z <= I)
			Log.i(TAG, HEADER + null == obj ? NULL : obj + FOOTER);
	}

	public static void w(Object obj) {
		w(TAG, obj);
	}

	public static void w(String TAG, Object obj) {
		if (Z <= W)
			Log.w(TAG, HEADER + null == obj ? NULL : obj + FOOTER);
	}

	public static void e(Object obj) {
		e(TAG, obj);
	}

	public static void e(String TAG, Object obj) {
		if (Z <= E)
			Log.e(TAG, HEADER + null == obj ? NULL : obj + FOOTER);
	}

	public static String getFileLineMethod() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		StringBuffer toStringBuffer = new StringBuffer("[").append(traceElement.getFileName()).append(" | ")
				.append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("]");
		return toStringBuffer.toString();
	}

	public static String getLineMethod() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		StringBuffer toStringBuffer = new StringBuffer("[").append(traceElement.getLineNumber()).append(" | ")
				.append(traceElement.getMethodName()).append("]");
		return toStringBuffer.toString();
	}

	public static String _FILE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
		return traceElement.getFileName();
	}

	public static String _FUNC_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getMethodName();
	}

	public static int _LINE_() {
		StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
		return traceElement.getLineNumber();
	}

	@SuppressLint("SimpleDateFormat")
	public static String _TIME_() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(now);
	}
}
