package com.library.util.volley.load;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.library.util.volley.VolleyHttpUtil;

/**
 * 带有加载动态的
 */
public class VolleyProgressUtil extends VolleyHttpUtil implements VolleyHttpUtil.OnLoadListener
{
    private static VolleyProgressUtil mVolleyProgressUtil;

    public VolleyProgressUtil(Context mContext, RequestQueue mQueue)
    {
        super(mContext, mQueue);
        setOnLoadListener(this);
    }

    public static VolleyProgressUtil getInstance(Context mContext, RequestQueue mQueue)
    {
        if (mVolleyProgressUtil == null)
        {
            mVolleyProgressUtil = new VolleyProgressUtil(mContext, mQueue);
        }
        return mVolleyProgressUtil;
    }

    private ProgressDialog mRequestProgress;


    @Override
    public void onStart()
    {
        mRequestProgress = ProgressDialog.show(mContext, "提示", "请稍候...");
    }

    @Override
    public void onSuccess(String response)
    {
        mRequestProgress.dismiss();
    }

    @Override
    public void onError(String error)
    {
        mRequestProgress.dismiss();
    }
}
