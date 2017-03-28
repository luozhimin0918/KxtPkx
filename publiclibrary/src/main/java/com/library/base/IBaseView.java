package com.library.base;

import android.content.Context;

import com.android.volley.RequestQueue;

/**
 * Created by DaiYao on 2016/9/13.
 */
public interface IBaseView
{
    Context getContext();

    RequestQueue getRequestQueue();
}
