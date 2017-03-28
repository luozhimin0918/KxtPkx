package com.library.util.volley;

import android.graphics.Bitmap;

import com.android.volley.VolleyError;

/**
 * @author Mr'Dai
 * @date 2016/5/17 17:25
 * @Title: MobileLibrary
 * @Package com.dxmobile.library
 * @Description:
 */
public abstract class VolleyImageListener {

    public abstract void onSuccess(Bitmap response);

    public void onError(VolleyError error) {

    }
}
