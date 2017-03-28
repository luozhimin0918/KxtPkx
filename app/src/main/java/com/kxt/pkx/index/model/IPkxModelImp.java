package com.kxt.pkx.index.model;

import com.android.volley.RequestQueue;
import com.kxt.pkx.common.constant.UrlConstant;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.ObserverData;
import com.kxt.pkx.index.jsonBean.BaseBean;
import com.kxt.pkx.index.persenter.PkxPersenter;
import com.library.util.volley.VolleyHttpListener;
import com.library.util.volley.VolleyHttpUtil2;

import java.util.Map;

import io.jsonwebtoken.Claims;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class IPkxModelImp implements IPkxModel {

    private PkxPersenter pkxPersenter;

    public IPkxModelImp(PkxPersenter pkxPersenter) {
        this.pkxPersenter = pkxPersenter;

    }

    @Override
    public void getServerTime(final ObserverData<BaseBean> observerData, Map<String, String> map, String url) {
        RequestQueue requestQueue = pkxPersenter.mView.getRequestQueue();
        VolleyHttpUtil2 request = new VolleyHttpUtil2(pkxPersenter.getContext(), requestQueue);
        request.doGet(url, map, new VolleyHttpListener() {
            @Override
            public void onSuccess(String result) {
                Claims claims = null;
                BaseBean baseBean = new BaseBean();
                try {
                    claims = BaseUtils.parseJWT(result, UrlConstant.URL_PRIVATE_KEY);
                    baseBean.setAud(claims.get("aud").toString());
                    baseBean.setStatus(claims.get("status").toString());
                    baseBean.setMsg(claims.get("msg").toString());
                    Map<String, Object> map = (Map<String, Object>) claims.get("data");
                    baseBean.setData(String.valueOf(map.get("time")));
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
        RequestQueue requestQueue = pkxPersenter.mView.getRequestQueue();
        VolleyHttpUtil2 request = new VolleyHttpUtil2(pkxPersenter.getContext(), requestQueue);
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
