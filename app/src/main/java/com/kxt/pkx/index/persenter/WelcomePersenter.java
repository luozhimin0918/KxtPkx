package com.kxt.pkx.index.persenter;

import com.google.gson.Gson;
import com.kxt.pkx.PkxApplicaion;
import com.kxt.pkx.common.base.CommunalPresenter;
import com.kxt.pkx.common.constant.SpConstant;
import com.kxt.pkx.common.constant.UrlConstant;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.ObserverData;
import com.kxt.pkx.index.jsonBean.AdConfigBean;
import com.kxt.pkx.index.jsonBean.ConfigJson;
import com.kxt.pkx.index.jsonBean.UpdateBean;
import com.kxt.pkx.index.model.IWelcomeModelImp;
import com.kxt.pkx.index.view.IWelcomeView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class WelcomePersenter extends CommunalPresenter<IWelcomeView> {

    private IWelcomeModelImp welcomeModelImp;

    public WelcomePersenter() {
        welcomeModelImp = new IWelcomeModelImp(this);
    }


    public void getWscUrl() {
        Map<String, String> map = new HashMap();
        Gson gson = new Gson();
        try {
            map.put("content", BaseUtils.createJWT(UrlConstant.URL_PRIVATE_KEY, gson.toJson(new ConfigJson())));
            welcomeModelImp.getWscUrl(new ObserverData<String>() {
                @Override
                public void onCallback(String data) {
                    super.onCallback(data);
                    if (null != data && !"".equals(data)) {
                        SpConstant.getWscPreferences().edit().putString(SpConstant.WEBSOCKET_URL, data).commit();
                    }

                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                }
            }, map, UrlConstant.WEBSOCKET_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getUpdateMsg();

    }

    /**
     * 获取版本更新信息
     */
    public void getUpdateMsg() {
        Map<String, String> map = new HashMap();
        Gson gson = new Gson();
        try {
            map.put("content", BaseUtils.createJWT(UrlConstant.URL_PRIVATE_KEY, gson.toJson(new ConfigJson())));
            welcomeModelImp.getUpdateMsg(new ObserverData<UpdateBean>() {
                @Override
                public void onCallback(UpdateBean data) {
                    super.onCallback(data);
                    if (null != data && data.getStatus().equals("1")) {
                        if (!"".equals(data.getData().getDownloadUrl())) {
                            //显示更新
                            PkxApplicaion.getInstance().setUpdateBean(data);
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                }
            }, map, UrlConstant.UPDATE_MSG_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取广告信息
     */
    public void getAdConfig() {
        Map<String, String> map = new HashMap();
        Gson gson = new Gson();
        try {
            map.put("content", BaseUtils.createJWT(UrlConstant.URL_PRIVATE_KEY, gson.toJson(new ConfigJson())));
            welcomeModelImp.getAdConfig(new ObserverData<AdConfigBean>() {
                @Override
                public void onCallback(AdConfigBean data) {
                    super.onCallback(data);
                    if (null != data && data.getStatus().equals("1")) {
                        if (data.getData().getAdvertisement().getType().equals("normal")) {
                            mView.showAd(data);
                        } else {
                            mView.toMainActivity();
                        }
                        PkxApplicaion.getInstance().setAdConfigBean(data);
                    } else {
                        mView.toMainActivity();
                    }
                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                    mView.toMainActivity();
                }
            }, map, UrlConstant.AD_CONFIG_URL);
        } catch (Exception e) {
            e.printStackTrace();
            mView.toMainActivity();
        }
        getWscUrl();
    }
}
