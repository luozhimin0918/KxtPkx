package com.library.util.volley;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.library.util.LogUtil;
import com.library.widget.window.ToastView;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Mr'Dai
 * @date 2016/5/17 17:50
 * @Title: MobileLibrary
 * @Package com.dxmobile.library.util
 * @Description: 返回所有数据, 不对state过滤
 */
public class VolleyHttpUtil2 extends VolleyHttpUtil {
    protected int outTime = 8 * 1000;

    public int getOutTime() {
        return outTime;
    }

    public void setOutTime(int outTime) {
        this.outTime = outTime;
    }

    public VolleyHttpUtil2(Context mContext, RequestQueue mQueue) {
        super(mContext, mQueue);
    }

    protected void send(int method, final String url, final Map<String, String> mParams, final VolleyHttpListener
            mVolleyHttpListener) {
        if (onLoadListener != null)
            onLoadListener.onStart();

        LogUtil.e("网络请求:", "连接" + url);
        if (mParams != null) {
            LogUtil.e("网络请求:", "参数" + mParams.toString());
        }

        FastStringRequest stringRequest = new FastStringRequest(method, url, mParams, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response == null || "".equals(response.trim())) {
                        ToastView.makeText2(mContext, "暂无数据,请稍候再试");
                        return;
                    }

                    LogUtil.e("网络请求成功返回:", response);
                    mVolleyHttpListener.onSuccess(response);

                    if (onLoadListener != null)
                        onLoadListener.onSuccess(response);

                } catch (Exception e) {
                    ToastView.makeText2(mContext, "数据请求出错,请稍候再试");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("网络请求:", "错误信息:" + error.getMessage());
                /**
                 * 网络超时
                 */
                if (error instanceof TimeoutError) {
                    ToastView.makeText2(mContext, "当前网络不佳~");
                    mVolleyHttpListener.onError(ERROR_TIME_OUT);
                }

                /**
                 * 网络错误
                 */
                else if (error instanceof NetworkError) {
                    mVolleyHttpListener.onError(ERROR_NOT_NETWORK);
                }

                /**
                 * 网络未连接
                 */
                else if (error instanceof NoConnectionError) {
                    mVolleyHttpListener.onError(ERROR_NOT_CONNECTION);
                }

                /**
                 * 其他错误
                 */
                else {
                    mVolleyHttpListener.onError(ERROR_NOT_NETWORK);
                }

                if (onLoadListener != null)
                    onLoadListener.onError(error.getMessage());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(outTime, 0, 1.0f));
        stringRequest.setTag(httpTag);
        mQueue.add(stringRequest);
    }


    public HashMap<String, String> newParams() {
        return new HashMap<>();
    }

}
