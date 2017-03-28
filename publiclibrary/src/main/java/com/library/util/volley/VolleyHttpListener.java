package com.library.util.volley;

/**
 * @author Mr'Dai
 * @date 2016/5/17 17:25
 * @Title: MobileLibrary
 * @Package com.dxmobile.library
 * @Description:
 */
public abstract class VolleyHttpListener {
    /**
     * 网络请求 启动
     */
    public void onStart() {

    }

    public abstract void onSuccess(String result);

    /**
     * 网络请求错误,常见与网络请求
     */
    public void onError(String error) {

    }

    public void onStatus(int status) {

    }
}
