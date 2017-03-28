package com.kxt.pkx.index.model;

import com.kxt.pkx.common.utils.ObserverData;
import com.kxt.pkx.index.jsonBean.BaseBean;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public interface IMainModel {

    void getServerTime(ObserverData<BaseBean> observerData, Map<String, String> map, String url);

    void getWscUrl(ObserverData<BaseBean> observerData, Map<String, String> map, String url);
}
