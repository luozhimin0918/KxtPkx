package com.kxt.pkx.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Intent intent1 = new Intent();
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//            BSToast.showLong(context, "网络不可以用");
            //改变背景或者 处理网络的全局变量
            intent1.setAction("netWork");
            intent1.putExtra("msg", "unConn");
            context.sendBroadcast(intent1);// 发送广播，通知行情页面刷新
        } else {
            //改变背景或者 处理网络的全局变量

            intent1.setAction("netWork");
            intent1.putExtra("msg", "isConn");
            context.sendBroadcast(intent1);// 发送广播，通知行情页面刷新
        }
    }


}
