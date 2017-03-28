package com.kxt.pkx.index.model;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.kxt.pkx.PkxApplicaion;
import com.kxt.pkx.common.constant.UrlConstant;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.ObserverData;
import com.kxt.pkx.index.jsonBean.AdConfigBean;
import com.kxt.pkx.index.jsonBean.UpdateBean;
import com.kxt.pkx.index.persenter.WelcomePersenter;
import com.library.util.volley.VolleyHttpListener;
import com.library.util.volley.VolleyHttpUtil2;

import java.util.LinkedHashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class IWelcomeModelImp implements IWelcomeModel {

    private WelcomePersenter persenter;

    public IWelcomeModelImp(WelcomePersenter persenter) {
        this.persenter = persenter;
    }

    @Override
    public void getWscUrl(final ObserverData<String> observerData, Map<String, String> map, String url) {
        RequestQueue requestQueue = persenter.mView.getRequestQueue();
        VolleyHttpUtil2 request = new VolleyHttpUtil2(persenter.getContext(), requestQueue);
        request.doGet(url, map, new VolleyHttpListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    Claims claims = BaseUtils.parseJWT(result, UrlConstant.URL_PRIVATE_KEY);
                    String value = (String) claims.get("data");
                    observerData.onCallback(value);
                } catch (Exception e) {
                    e.printStackTrace();
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
    public void getUpdateMsg(final ObserverData<UpdateBean> observerData, Map<String, String> map, String url) {
        RequestQueue requestQueue = persenter.mView.getRequestQueue();
        VolleyHttpUtil2 request = new VolleyHttpUtil2(persenter.getContext(), requestQueue);
        request.doGet(url, map, new VolleyHttpListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    UpdateBean updateBean = new UpdateBean();
                    UpdateBean.DataBean updateData = new UpdateBean.DataBean();
                    Claims claims = BaseUtils.parseJWT(result, UrlConstant.URL_PRIVATE_KEY);
                    updateBean.setMsg(claims.get("msg").toString());
                    updateBean.setAud(claims.get("aud").toString());
                    updateBean.setStatus(claims.get("status").toString());
                    LinkedHashMap<String, Object> dataClaims = (LinkedHashMap<String, Object>) claims.get("data");
                    updateData.setContent(dataClaims.get("Content").toString());
                    updateData.setSize(dataClaims.get("Size").toString());
                    updateData.setVersion(dataClaims.get("Version").toString());
                    updateData.setDownloadUrl(dataClaims.get("DownloadUrl").toString());
                    updateBean.setData(updateData);
                    if (claims.get("status").toString().equals("1")) {
                        PkxApplicaion.getInstance().setUpdateBean(updateBean);

                    }
                    observerData.onCallback(updateBean);


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
    public void getAdConfig(final ObserverData<AdConfigBean> observerData, Map<String, String> map, String url) {
        RequestQueue requestQueue = persenter.mView.getRequestQueue();
        VolleyHttpUtil2 request = new VolleyHttpUtil2(persenter.getContext(), requestQueue);
        request.doGet(url, map, new VolleyHttpListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    AdConfigBean adConfigBean = new AdConfigBean();
                    AdConfigBean.DataBean adDataBean = new AdConfigBean.DataBean();
                    AdConfigBean.DataBean.AdvertisementBean advertisementBean = new AdConfigBean.DataBean.AdvertisementBean();

                    Claims claims = BaseUtils.parseJWT(result, UrlConstant.URL_PRIVATE_KEY);
                    adConfigBean.setStatus(claims.get("status").toString());
                    adConfigBean.setAud(claims.get("aud").toString());
                    adConfigBean.setMsg(claims.get("msg").toString());
                    Map<String, Object> dataClaims = (Map<String, Object>) claims.get("data");
                    Map<String, Object> adClaims = (Map<String, Object>) dataClaims.get("Advertisement");
                    advertisementBean.setImageUrl(adClaims.get("ImageUrl").toString());
                    advertisementBean.setTitle(adClaims.get("Title").toString());
                    advertisementBean.setType(adClaims.get("Type").toString());
                    advertisementBean.setUrl(adClaims.get("Url").toString());
                    adDataBean.setAdvertisement(advertisementBean);
                    adConfigBean.setData(adDataBean);
                    if (claims.get("status").toString().equals("1")) {
                        PkxApplicaion.getInstance().setAdConfigBean(adConfigBean);
                    }
                    observerData.onCallback(adConfigBean);

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
