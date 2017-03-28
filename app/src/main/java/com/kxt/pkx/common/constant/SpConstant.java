package com.kxt.pkx.common.constant;

import android.content.Context;
import android.content.SharedPreferences;

import com.kxt.pkx.PkxApplicaion;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class SpConstant {

    public static String WEBSOCKET_URL = "webSocketURL";
    public static String CACHE_DATA_KEY = "cacheDatakey";
    public static String SELECT_DATA_KEY = "selectDatakey";
    public static String TOGGLE_DATA_KEY = "toggleDatakey";
    public static String CHECK_DATA_KEY = "checkDatakey";
    public static String NEWS_AD_KEY = "newsAdKey";
    public static String NEWS_DATA_KEY = "newsDataKey";
    public static String READ_NWES_KEY = "readNewsKey";
    private static SharedPreferences wscPreferences, cPreferences, selectPreferences, togglePreferences,
            checkPreferences, newsAdreferences, newsDataeferences, readPreferences;

    public static SharedPreferences getWscPreferences() {
        if (null == wscPreferences) {
            wscPreferences = PkxApplicaion.getInstance().getSharedPreferences("webSocket", Context.MODE_PRIVATE);
        }
        return wscPreferences;
    }

    public static SharedPreferences getCachePreferences() {
        if (null == cPreferences) {
            cPreferences = PkxApplicaion.getInstance().getSharedPreferences("cacheData", Context.MODE_PRIVATE);
        }
        return cPreferences;
    }

    public static SharedPreferences getSelectPreferences() {
        if (null == selectPreferences) {
            selectPreferences = PkxApplicaion.getInstance().getSharedPreferences("selectData", Context.MODE_PRIVATE);
        }
        return selectPreferences;
    }

    public static SharedPreferences getTogglePreferences() {
        if (null == togglePreferences) {
            togglePreferences = PkxApplicaion.getInstance().getSharedPreferences("toggleData", Context.MODE_PRIVATE);
        }
        return togglePreferences;
    }

    public static SharedPreferences getCheckPreferences() {
        if (null == checkPreferences) {
            checkPreferences = PkxApplicaion.getInstance().getSharedPreferences("checkData", Context.MODE_PRIVATE);
        }
        return checkPreferences;
    }

    public static SharedPreferences getNewsAdPreferences() {
        if (null == newsAdreferences) {
            newsAdreferences = PkxApplicaion.getInstance().getSharedPreferences("newsAd", Context.MODE_PRIVATE);
        }
        return newsAdreferences;
    }

    public static SharedPreferences getNewsDataPreferences() {
        if (null == newsDataeferences) {
            newsDataeferences = PkxApplicaion.getInstance().getSharedPreferences("newsData", Context.MODE_PRIVATE);
        }
        return newsDataeferences;
    }

    public static SharedPreferences getReadPreferences() {
        if (null == readPreferences) {
            readPreferences = PkxApplicaion.getInstance().getSharedPreferences("readItem", Context.MODE_PRIVATE);
        }
        return readPreferences;
    }
}
