package com.lanna.android.portfolioschart.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.lanna.android.portfolioschart.BuildConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lanna on 3/11/16.
 *
 */
public class LogUtils {

    private static boolean isDebug = BuildConfig.DEBUG || "staging".equalsIgnoreCase(BuildConfig.FLAVOR);
    public static boolean hasWriteFile = false;//!"production".equalsIgnoreCase(BuildConfig.FLAVOR);
    public static String DEFAULT_TAG = "PChart";

    // Log model interface
    interface ILogModel {
        String toLogString();
    }

    /*
        Support Log File
     */
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static File mLogFile;

    private static File getLogFile() {
        if (mLogFile == null) {
            mLogFile = new File(Environment.getExternalStorageDirectory(), DEFAULT_TAG + "-" + BuildConfig.FLAVOR + "-log.txt");
            if (!mLogFile.exists()) {
                try {
                    mLogFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logDeviceInfo();
        }
        return mLogFile;
    }

    private static synchronized void appendLogFile(String type, String tag, String log, Throwable e) {
        appendLogFile(type, tag, log + "\n" + e.getMessage());
    }
    private static synchronized void appendLogFile(String type, String tag, String log) {
        try {
            final FileWriter fileOut = new FileWriter(getLogFile(), true);
            fileOut.append(sdf.format(new Date()) + " " + type + "/" + tag + ": " + log + NEW_LINE);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logDeviceInfo() {
        String tag = "DeviceInfo";
        i(tag, "Model : " + android.os.Build.MODEL);
        i(tag, "Brand : " + android.os.Build.BRAND);
        i(tag, "Product : " + android.os.Build.PRODUCT);
        i(tag, "Device : " + android.os.Build.DEVICE);
        i(tag, "Codename : " + android.os.Build.VERSION.CODENAME);
        i(tag, "Release : " + android.os.Build.VERSION.RELEASE);
    }
    // end of Support Log File

    /*
        Base log methods
     */
    private static void logD(String tag, String msg) {
        if (isDebug) Log.d(tag, msg);
        if (hasWriteFile) appendLogFile("D", tag, msg);
    }

    public static void logW(String tag, String msg) {
        if (isDebug) Log.w(tag, msg);
        if (hasWriteFile) appendLogFile("W", tag, msg);
    }

    public static void logI(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
        if (hasWriteFile) appendLogFile("I", tag, msg);
    }

    public static void logV(String tag, String msg) {
        if (isDebug) Log.v(tag, msg);
        if (hasWriteFile) appendLogFile("V", tag, msg);
    }

    public static void logE(String tag, String msg) {
        if (isDebug) Log.e(tag, msg);
        if (hasWriteFile) appendLogFile("E", tag, msg);
    }

    private static void logD(String tag, String msg, Throwable e) {
        if (isDebug) Log.d(tag, msg, e);
        if (hasWriteFile) appendLogFile("D", tag, msg, e);
    }

    public static void logW(String tag, String msg, Throwable e) {
        if (isDebug) Log.w(tag, msg, e);
        if (hasWriteFile) appendLogFile("W", tag, msg, e);
    }

    public static void logI(String tag, String msg, Throwable e) {
        if (isDebug) Log.i(tag, msg, e);
        if (hasWriteFile) appendLogFile("I", tag, msg, e);
    }

    public static void logV(String tag, String msg, Throwable e) {
        if (isDebug) Log.v(tag, msg, e);
        if (hasWriteFile) appendLogFile("V", tag, msg, e);
    }

    public static void logE(String tag, String msg, Throwable e) {
        if (isDebug) Log.e(tag, msg, e);
        if (hasWriteFile) appendLogFile("E", tag, msg, e);
    }
    // end of Base log methods

    public static void logLongText(String tag, String msg) {
        if (hasWriteFile) appendLogFile("D", tag, msg);
        if (isDebug) {
            int maxLen = 4000;
            if (msg.length() > maxLen) {
//                logD(tag, "length = " + msg.length());
                int chunkCount = msg.length() / maxLen;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = maxLen * (i + 1);
                    if (max >= msg.length()) {
                        logD(tag, msg.substring(maxLen * i));
                    } else {
                        logD(tag, msg.substring(maxLen * i, max));
                    }
                }
            } else {
                logD(tag, msg);
            }
        }
    }

    // TODO Log(tag, msg)
    public static void d(String tag, String msg) {
        logD(convertTag(tag), getMessageNotNull(msg));
    }

    public static void w(String tag, String msg) {
        logW(convertTag(tag), getMessageNotNull(msg));
    }

    public static void i(String tag, String msg) {
        logI(convertTag(tag), getMessageNotNull(msg));
    }

    public static void v(String tag, String msg) {
        logV(convertTag(tag), getMessageNotNull(msg));
    }

    public static void e(String tag, String msg) {
        logE(convertTag(tag), getMessageNotNull(msg));
    }

    // TODO Log(tag, msg, Throwable)
    public static void d(String tag, String msg, Throwable e) {
        logD(convertTag(tag), getMessageNotNull(msg), e);
    }

    public static void w(String tag, String msg, Throwable e) {
        logW(convertTag(tag), getMessageNotNull(msg), e);
    }

    public static void i(String tag, String msg, Throwable e) {
        logI(convertTag(tag), getMessageNotNull(msg), e);
    }

    public static void v(String tag, String msg, Throwable e) {
        logV(convertTag(tag), getMessageNotNull(msg), e);
    }

    public static void e(String tag, String msg, Throwable e) {
        logE(convertTag(tag), getMessageNotNull(msg), e);
    }

    // Add methods for object
    // TODO Log(Object, msg)
    private static String getClassName(Object o) {
        return o instanceof Class ? ((Class) o).getSimpleName() : o.getClass().getSimpleName();
    }

    private static String getLogClassName(Object o) {
        return "{" + getClassName(o) + "} ";
    }

    public static void e(Object o, String msg) {
        logE(convertTag(getClassName(o)), getMessageNotNull(msg));
    }

    public static void d(Object o, String msg) {
        logD(convertTag(getClassName(o)), getMessageNotNull(msg));
    }

    public static void w(Object o, String msg) {
        logW(convertTag(getClassName(o)), getMessageNotNull(msg));
    }

    public static void i(Object o, String msg) {
        logI(convertTag(getClassName(o)), getMessageNotNull(msg));
    }

    public static void v(Object o, String msg) {
        logV(convertTag(getClassName(o)), getMessageNotNull(msg));
    }

    // TODO Log(Object, msg, Throwable)
    public static void e(Object o, String msg, Throwable e) {
        logE(convertTag(getClassName(o)), getMessageNotNull(msg), e);
    }

    public static void d(Object o, String msg, Throwable e) {
        logD(convertTag(getClassName(o)), getMessageNotNull(msg), e);
    }

    public static void w(Object o, String msg, Throwable e) {
        logW(convertTag(getClassName(o)), getMessageNotNull(msg), e);
    }

    public static void i(Object o, String msg, Throwable e) {
        logI(convertTag(getClassName(o)), getMessageNotNull(msg), e);
    }

    public static void v(Object o, String msg, Throwable e) {
        logV(convertTag(getClassName(o)), getMessageNotNull(msg), e);
    }

    // TODO Log(func, tag, msg) (add 12/25/2012 by lanna)
    public static void d(String func, String tag, String msg) {
        logD(convertTag(func), tag + ": " + getMessageNotNull(msg));
    }

    public static void w(String func, String tag, String msg) {
        logW(convertTag(func), tag + ": " + getMessageNotNull(msg));
    }

    public static void i(String func, String tag, String msg) {
        logI(convertTag(func), tag + ": " + getMessageNotNull(msg));
    }

    public static void v(String func, String tag, String msg) {
        logV(convertTag(func), tag + ": " + getMessageNotNull(msg));
    }

    public static void e(String func, String tag, String msg) {
        logE(convertTag(func), tag + ": " + getMessageNotNull(msg));
    }

    // TODO Log(func, Object, msg) (add 12/25/2012 by lanna)
    public static void e(String func, Object o, String msg) {
        logE(convertTag(func), getLogClassName(o) + getMessageNotNull(msg));
    }

    public static void d(String func, Object o, String msg) {
        logD(convertTag(func), getLogClassName(o) + getMessageNotNull(msg));
    }

    public static void w(String func, Object o, String msg) {
        logW(convertTag(func), getLogClassName(o) + getMessageNotNull(msg));
    }

    public static void i(String func, Object o, String msg) {
        logI(convertTag(func), getLogClassName(o) + getMessageNotNull(msg));
    }

    public static void v(String func, Object o, String msg) {
        logV(convertTag(func), getLogClassName(o) + getMessageNotNull(msg));
    }


    // TODO by pass Log methods

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    // TODO Log(msg) (added 11/29/2013 by lanna)
    public static void v(String msg) {
        v(DEFAULT_TAG, msg);
    }

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    public static void i(String msg) {
        i(DEFAULT_TAG, msg);
    }

    public static void w(String msg) {
        w(DEFAULT_TAG, msg);
    }

    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }

    /*
        Other support
     */

    public static String convertTag(String tag) {
        tag = TextUtils.isEmpty(tag) ? DEFAULT_TAG
//                : !tag.contains(DEFAULT_TAG) ? (DEFAULT_TAG + "-" + tag)
                : tag;
        return tag.length() <= 23 ? tag : tag.substring(0, 23);
    }

    private static String getMessageNotNull(String msg) {
        if (msg == null) {
            msg = "null";
        }
        return msg;
    }

    /*
        Array
     */

    public static <T> void printArrayLog(String tag, List<T> models) {
        logI(tag, "[");
        for (T model : models) {
            logI(convertTag(tag), "" + models.indexOf(model) + ": " + model);
        }
        logI(tag, "]");
    }

    public static String toArrayLogString(Object... logModels) {
        return toLogString(false, logModels);
    }

    public static String toArrayLogStringWithEnter(Object... logModels) {
        return toLogString(true, logModels);
    }

    private static String toLogString(boolean isEnter, Object... logModels) {
        if (logModels == null) {
            return "null";
        }

        Object model;
        int n = logModels.length - 1;
        StringBuilder log = new StringBuilder()
                .append(logModels.length).append(isEnter ? "[\n" : "[");

        for (int i = 0; i <= n; i++) {
            model = logModels[i];
            if (model instanceof ILogModel) {
                log.append(((ILogModel) model).toLogString());
            } else {
                log.append(model.toString());
            }
            if (i != n) { // not last one
                log.append(isEnter ? ";\n" : "; ");
            }
        }

        return log.append(isEnter ? "]\n" : "]").toString();
    }

    /*
        List
     */

    public static String toListLogString(List logModels) {
        return toListLogStrings(logModels, false);
    }

    public static String toListLogStringWithEnter(List logModels) {
        return toListLogStrings(logModels, true);
    }

    private static String toListLogStrings(List logModels, boolean hasEnter) {
        if (logModels == null) {
            return "null";
        }

        Object model;
        int n = logModels.size() - 1;
        StringBuilder log = new StringBuilder(logModels.size()).append(hasEnter ? "[\n" : "[");
        for (int i = 0; i <= n; i++) {
            model = logModels.get(i);
            if (model instanceof ILogModel) {
                log.append(((ILogModel) model).toLogString());
            } else {
                log.append(model.toString());
            }
            if (i != n) { // not last one
                log.append(hasEnter ? ";\n" : "; ");
            }
        }
        log.append(hasEnter ? "\n]" : "]");

        return log.toString();
    }

}
