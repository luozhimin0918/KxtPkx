package com.kxt.pkx.index.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.kxt.pkx.R;
import com.kxt.pkx.common.base.CommunalActivity;
import com.kxt.pkx.common.utils.ConnectionChangeReceiver;
import com.kxt.pkx.common.utils.DoubleClickUtils;
import com.kxt.pkx.common.utils.PopupWindowUtils;
import com.kxt.pkx.index.persenter.IMainPersenter;
import com.kxt.pkx.index.view.IMainView;
import com.library.manager.ActivityManager;
import com.library.util.volley.load.PageLoadLayout;
import com.library.widget.window.ToastView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends CommunalActivity implements IMainView, PageLoadLayout.OnAfreshLoadListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.page_load)
    PageLoadLayout pageLoad;
    @BindView(R.id.materialRefreshLayout)
    MaterialRefreshLayout materialRefreshLayout;
    @BindView(R.id.separator_rl_date)
    LinearLayout separatorRlDate;
    @BindView(R.id.main_separator_line_tv_date)
    TextView mainSeparatorLineTvDate;
    @BindView(R.id.filter_icon)
    RelativeLayout filterIcon;

    private IMainPersenter mainPersenter;
    private ConnectionChangeReceiver myReceiver;
    private MyReceiver receiver;
    private PopupWindowUtils popupWindowUtils;
    private View popView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBindingView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainPersenter = new IMainPersenter();
        mainPersenter.attach(this);
        mainPersenter.initViews(recyclerView, materialRefreshLayout, pageLoad, separatorRlDate, mainSeparatorLineTvDate);
        init();

    }

    public void init() {
        initPopupView();
        pageLoad.setOnAfreshLoadListener(this);

        try {
            if (receiver == null) {
                receiver = new MyReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction("netWork");
                registerReceiver(receiver, filter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != receiver) {
                unregisterReceiver(receiver);
            }
        }

        registerReceiver();
    }

    public void initPopupView() {
        popupWindowUtils = new PopupWindowUtils();
        popView = getLayoutInflater().inflate(R.layout.pop_fliter_view, null, false);
    }

    private void registerReceiver() {
        try {
            if (myReceiver == null) {
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                myReceiver = new ConnectionChangeReceiver();
                this.registerReceiver(myReceiver, filter);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (myReceiver != null) {
                this.unregisterReceiver(myReceiver);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (!DoubleClickUtils.isFastDoubleClick(2000)) {
            ToastView.makeText3(this, "再按一次退出");
        } else {
            ActivityManager.getInstance().finishAllActivity();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPersenter.onActDestory();
    }

    @Override
    public void OnAfreshLoad() {

    }


    @OnClick({R.id.filter_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_icon:
                popupWindowUtils.dismiss();
                popupWindowUtils.initPopupWindwo(view, popView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                        Color.TRANSPARENT, R.style.popwindow_register_animation, 0,
                        0);
                break;
        }
    }


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().getString("msg");
            if (msg.equals("isConn")) {
                //
            } else {
            }
        }

    }
}
