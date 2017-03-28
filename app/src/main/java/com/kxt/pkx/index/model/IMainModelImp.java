package com.kxt.pkx.index.model;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.kxt.pkx.common.constant.SpConstant;
import com.kxt.pkx.common.constant.UrlConstant;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.ObserverData;
import com.kxt.pkx.index.jsonBean.BaseBean;
import com.kxt.pkx.index.persenter.IMainPersenter;
import com.library.util.volley.VolleyHttpListener;
import com.library.util.volley.VolleyHttpUtil2;

import java.util.Map;

import io.jsonwebtoken.Claims;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class IMainModelImp implements IMainModel {

    private IMainPersenter mainPersenter;

    public IMainModelImp(IMainPersenter mainPersenter) {
        this.mainPersenter = mainPersenter;

    }

    @Override
    public void getServerTime(final ObserverData<BaseBean> observerData, Map<String, String> map, String url) {
        RequestQueue requestQueue = mainPersenter.mView.getRequestQueue();
        VolleyHttpUtil2 request = new VolleyHttpUtil2(mainPersenter.getContext(), requestQueue);
        request.doGet(url, map, new VolleyHttpListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean baseBean = new BaseBean();
                    Claims claims = BaseUtils.parseJWT(result, UrlConstant.URL_PRIVATE_KEY);
                    baseBean.setAud(claims.get("aud").toString());
                    baseBean.setStatus(claims.get("status").toString());
                    baseBean.setMsg(claims.get("msg").toString());
                    baseBean.setData(String.valueOf(claims.get("data")));

                    observerData.onCallback(baseBean);
                } catch (Exception e) {
                    e.printStackTrace();
                    observerData.onError(e.getMessage());
                }

            }

            @Override
            public void onError(String error) {
                super.onError(error);
                observerData.onError(error);
            }
        });
    }

    @Override
    public void getWscUrl(final ObserverData<BaseBean> observerData, Map<String, String> map, String url) {
        RequestQueue requestQueue = mainPersenter.mView.getRequestQueue();
        VolleyHttpUtil2 request = new VolleyHttpUtil2(mainPersenter.getContext(), requestQueue);
        request.doGet(url, map, new VolleyHttpListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    Claims claims = BaseUtils.parseJWT(result, UrlConstant.URL_PRIVATE_KEY);
                    BaseBean baseBean = new BaseBean();
                    baseBean.setAud(claims.get("aud").toString());
                    baseBean.setStatus(claims.get("status").toString());
                    baseBean.setMsg(claims.get("msg").toString());
                    baseBean.setData(claims.get("data").toString());
                    observerData.onCallback(baseBean);
                } catch (Exception e) {
                    e.printStackTrace();
                    observerData.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                observerData.onError(error);
            }
        });
    }
}
