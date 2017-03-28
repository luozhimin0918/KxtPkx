package com.kxt.pkx.index.model;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.kxt.pkx.common.constant.UrlConstant;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.ObserverData;
import com.kxt.pkx.index.jsonBean.NewsAdBean;
import com.kxt.pkx.index.jsonBean.NewsDataBean;
import com.kxt.pkx.index.persenter.NewsPersenter;
import com.library.util.volley.VolleyHttpListener;
import com.library.util.volley.VolleyHttpUtil2;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class INewsModelImp implements INewsModel {
    private NewsPersenter newsPersenter;

    public INewsModelImp(NewsPersenter newsPersenter) {

        this.newsPersenter = newsPersenter;
    }

    @Override
    public void getAdData(final ObserverData<NewsAdBean> observerData, Map<String, String> map, String url) {
        RequestQueue requestQueue = newsPersenter.mView.getRequestQueue();
        VolleyHttpUtil2 request = new VolleyHttpUtil2(newsPersenter.getContext(), requestQueue);
        request.doGet(url, map, new VolleyHttpListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    NewsAdBean newsAdBean = new NewsAdBean();
                    ArrayList<NewsAdBean.DataBean> dataBeens = new ArrayList<NewsAdBean.DataBean>();
                    Claims claims = BaseUtils.parseJWT(result, UrlConstant.URL_PRIVATE_KEY);
                    newsAdBean.setAud(claims.get("aud").toString());
                    newsAdBean.setStatus(claims.get("status").toString());
                    newsAdBean.setMsg(claims.get("msg").toString());

                    ArrayList<LinkedHashMap<String, Object>> datas = (ArrayList<LinkedHashMap<String, Object>>) claims.get("data");
                    for (int i = 0; i < datas.size(); i++) {
                        NewsAdBean.DataBean dataBean = new NewsAdBean.DataBean();

                        LinkedHashMap<String, Object> dataClaims = datas.get(i);
                        dataBean.setId(dataClaims.get("id").toString());
                        dataBean.setTitle(dataClaims.get("title").toString());
                        dataBean.setUrl(dataClaims.get("url").toString());
                        dataBean.setWeburl(dataClaims.get("weburl").toString());
                        dataBean.setThumb(dataClaims.get("thumb").toString());
                        dataBeens.add(dataBean);
                    }

                    newsAdBean.setData(dataBeens);
                    observerData.onCallback(newsAdBean);
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
    public void getNewsData(final ObserverData<NewsDataBean> observerData, Map<String, String> map, String url) {
        RequestQueue requestQueue = newsPersenter.mView.getRequestQueue();
        VolleyHttpUtil2 request = new VolleyHttpUtil2(newsPersenter.getContext(), requestQueue);
        request.doGet(url, map, new VolleyHttpListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    NewsDataBean newsDataBean = new NewsDataBean();
                    List<NewsDataBean.DataBean> datas = new ArrayList<NewsDataBean.DataBean>();

                    Claims claims = BaseUtils.parseJWT(result, UrlConstant.URL_PRIVATE_KEY);
                    newsDataBean.setAud(claims.get("aud").toString());
                    newsDataBean.setStatus(claims.get("status").toString());
                    newsDataBean.setMsg(claims.get("msg").toString());

                    ArrayList<LinkedHashMap> claimsDatas = (ArrayList<LinkedHashMap>) claims.get("data");
                    for (int i = 0; i < claimsDatas.size(); i++) {
                        NewsDataBean.DataBean dataBean = new NewsDataBean.DataBean();
                        List<String> tags = new ArrayList<String>();

                        dataBean.setId(claimsDatas.get(i).get("id").toString());
                        dataBean.setTitle(claimsDatas.get(i).get("title").toString());
                        dataBean.setUrl(claimsDatas.get(i).get("url").toString());
                        dataBean.setWeburl(claimsDatas.get(i).get("weburl").toString());
                        dataBean.setAddtime(claimsDatas.get(i).get("addtime").toString());
                        dataBean.setThumb(claimsDatas.get(i).get("thumb").toString());
                        dataBean.setDescription(claimsDatas.get(i).get("description").toString());
                        dataBean.setCategory_id(claimsDatas.get(i).get("category_id").toString());
                        dataBean.setDianzan(claimsDatas.get(i).get("dianzan").toString());

                        ArrayList<String> claimsTags = (ArrayList<String>) claimsDatas.get(i).get("tags");
                        for (int a = 0; a < claimsTags.size(); a++) {
                            tags.add(claimsTags.get(a));
                        }
                        dataBean.setTags(tags);
                        datas.add(dataBean);
                    }
                    newsDataBean.setData(datas);
                    observerData.onCallback(newsDataBean);
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
