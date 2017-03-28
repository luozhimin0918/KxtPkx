package com.library.util.volley.load;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.library.util.volley.VolleyHttpListener;
import com.library.util.volley.VolleyHttpUtil;

import java.util.Map;

/**
 * 带有加载动态的
 */
public class VolleyFullLoadUtil extends VolleyHttpUtil implements VolleyHttpUtil.OnLoadListener, VolleyLoadLayout
        .OnAfreshLoadListener
{
    private static VolleyFullLoadUtil mVolleyFullLoadUtil;

    public static VolleyFullLoadUtil getInstance(Context mContext, RequestQueue mQueue, VolleyLoadLayout mFullLoad)
    {
        if (mVolleyFullLoadUtil == null)
        {
            mVolleyFullLoadUtil = new VolleyFullLoadUtil(mContext, mQueue, mFullLoad);
        }
        return mVolleyFullLoadUtil;
    }

    private boolean isNotData = false;
    private VolleyLoadLayout mFullLoad;
    private VolleyLoadBean mVolleyLoadBean = new VolleyLoadBean();

    public VolleyFullLoadUtil(Context mContext, RequestQueue mQueue, VolleyLoadLayout mFullLoad)
    {
        super(mContext, mQueue);
        this.mFullLoad = mFullLoad;
        setOnLoadListener(this);
        mFullLoad.setOnAfreshLoadListener(this);
    }

    @Override
    public void send(int method, String url, Map<String, String> mParams, VolleyHttpListener mVolleyHttpListener)
    {
        super.send(method, url, mParams, mVolleyHttpListener);
        mVolleyLoadBean.url = url;
        mVolleyLoadBean.params = mParams;
        mVolleyLoadBean.method = Request.Method.POST;
        mVolleyLoadBean.volleyHttpListener = mVolleyHttpListener;
    }

    public void setNotData(boolean notData)
    {
        isNotData = notData;
    }

    @Override
    public void onStart()
    {
        mFullLoad.showLoading();
    }

    @Override
    public void onSuccess(String response)
    {
        if (isNotData)
        {
            mFullLoad.showLoadNotData();
        } else
        {
            mFullLoad.hindFullLoad();
        }
    }

    @Override
    public void onError(String error)
    {
        mFullLoad.showLoadError();
    }

    @Override
    public void OnAfreshLoad()
    {
        send(mVolleyLoadBean.method, mVolleyLoadBean.url, mVolleyLoadBean.params, mVolleyLoadBean
                .volleyHttpListener);
    }
}
