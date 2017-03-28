package com.library.util;

import android.util.Log;

import com.library.BuildConfig;

/**
 * Created by DaiYao on 2016/9/12.
 */
public class LogUtil
{
    public static void e(String tag, String msg)
    {
        if (BuildConfig.DEBUG)
            Log.e(tag, msg);
    }

    public static void i(String tag, String msg)
    {
        if (BuildConfig.DEBUG)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (BuildConfig.DEBUG )
            Log.d(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (BuildConfig.DEBUG )
            Log.v(tag, msg);
    }

    public static void w(String tag, String msg)
    {
        if (BuildConfig.DEBUG )
            Log.w(tag, msg);
    }


}
