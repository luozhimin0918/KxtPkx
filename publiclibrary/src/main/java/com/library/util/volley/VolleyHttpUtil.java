package com.library.util.volley;

import android.content.Context;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.library.base.BaseJson;
import com.library.util.LogUtil;
import com.library.widget.window.ToastView;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr'Dai
 * @date 2016/5/17 17:50
 * @Title: MobileLibrary
 * @Package com.dxmobile.library.util
 * @Description:
 */
public class VolleyHttpUtil {
    /**
     * 请求错误-超时
     */
    public final static String ERROR_TIME_OUT = "timeout";
    /**
     * 请求错误-未开起网络
     */
    public final static String ERROR_NOT_NETWORK = "notNetWork";

    public final static String ERROR_NOT_CONNECTION = "notConentionNetWork";

    /**
     * 如果设置了Tag 退出界面清除所有请求不结束
     */
    public interface OnLoadListener {
        void onStart();

        void onSuccess(String response);

        void onError(String error);
    }

    protected int OUT_TIME = 8 * 1000;
    protected Context mContext;
    protected RequestQueue mQueue;
    protected OnLoadListener onLoadListener;

    /**
     * 如果为false 则退出Activity 请求结束
     */
    protected String httpTag;

    public VolleyHttpUtil(Context mContext, RequestQueue mQueue) {
        this.mContext = mContext;
        this.mQueue = mQueue;
    }

    public void setTag(String httpTag) {
        this.httpTag = httpTag;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void doGet(String url, VolleyHttpListener mVolleyHttpListener) {
        doGet(url, null, mVolleyHttpListener);
    }

    public void doGet(String url, Map<String, String> mParams, VolleyHttpListener mVolleyHttpListener) {
        if (mParams != null) {

            String paramsStr = mParams.toString();
            paramsStr = paramsStr.replaceAll(",", "&").replaceAll(" ", "");
            paramsStr = paramsStr.replace("{", "?");
            paramsStr = paramsStr.replace("}", "");

            url += paramsStr;
        }
        send(Request.Method.GET, url, mParams, mVolleyHttpListener);
    }

    public void doPost(String url, VolleyHttpListener mVolleyHttpListener) {
        doPost(url, null, mVolleyHttpListener);
    }

    public void doPost(String url, Map<String, String> mParams, VolleyHttpListener mVolleyHttpListener) {
        send(Request.Method.POST, url, mParams, mVolleyHttpListener);
    }

    protected void send(int method, final String url, final Map<String, String> mParams, final VolleyHttpListener
            mVolleyHttpListener) {

        if (onLoadListener != null)
            onLoadListener.onStart();

        mVolleyHttpListener.onStart();
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

                    BaseJson baseJson = JSONObject.parseObject(response, BaseJson.class);

                    int status = baseJson.getStatus();
                    if (status == 1) //正确情况
                    {
                        String dataInfo = baseJson.getData();

                        LogUtil.e("网络请求成功返回:", dataInfo);
                        mVolleyHttpListener.onSuccess(dataInfo);

                        if (onLoadListener != null)
                            onLoadListener.onSuccess(dataInfo);
                    } else {
                        mVolleyHttpListener.onStatus(status);
                    }
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
                    mVolleyHttpListener.onError(ERROR_NOT_NETWORK);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(OUT_TIME, 0, 1.0f));
        stringRequest.setTag(httpTag);
        mQueue.add(stringRequest);
    }


    public HashMap<String, String> newParams() {
        return new HashMap<>();
    }

}
