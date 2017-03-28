package com.kxt.pkx.index.model;

import com.kxt.pkx.common.utils.ObserverData;
import com.kxt.pkx.index.jsonBean.BaseBean;
import com.kxt.pkx.index.jsonBean.NewsAdBean;
import com.kxt.pkx.index.jsonBean.NewsDataBean;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public interface INewsModel {

    void getAdData(ObserverData<NewsAdBean> observerData, Map<String, String> map, String url);

    void getNewsData(ObserverData<NewsDataBean> observerData, Map<String, String> map, String url);
}
