package com.kxt.pkx.common.utils;

import com.library.util.volley.VolleyHttpListener;

/**
 * 判断是否需要刷新
 */
public abstract class ObserverData<T> extends VolleyHttpListener
{
    public void onCallback(T data)
    {

    }

    /**
     * 第二个参数表示状态,
     * 如果-1 则数据读取缓存数据
     * 如果1  则数据读取网络数据
     * @param data
     * @param state
     */
    public void onCallback(T data, int state)
    {

    }

    @Override
    public void onSuccess(String result)
    {

    }
}
