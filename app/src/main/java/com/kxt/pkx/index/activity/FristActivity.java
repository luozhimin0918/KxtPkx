package com.kxt.pkx.index.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.kxt.pkx.PkxApplicaion;
import com.kxt.pkx.R;
import com.kxt.pkx.common.base.CommunalActivity;
import com.kxt.pkx.common.constant.SpConstant;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.ConnectionChangeReceiver;
import com.kxt.pkx.common.utils.DoubleClickUtils;
import com.kxt.pkx.common.utils.PopupWindowUtils;
import com.kxt.pkx.common.utils.ViewFindUtils;
import com.kxt.pkx.index.adapter.MainPagerAdapter;
import com.kxt.pkx.index.fragment.NewsFragment;
import com.kxt.pkx.index.fragment.PkxFragment;
import com.kxt.pkx.index.persenter.IFristPersenter;
import com.kxt.pkx.index.view.IFristView;
import com.library.manager.ActivityManager;
import com.library.widget.window.ToastView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class FristActivity extends CommunalActivity implements IFristView, View.OnClickListener {


    @BindView(R.id.filter_img)
    RelativeLayout filterIcon;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_top)
    SegmentTabLayout tabTop;

    private View mDecorView;
    private String[] mTitles = {"快讯", "要闻"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private boolean isShow = false;
    private IFristPersenter fristPersenter;
    private ConnectionChangeReceiver myReceiver;
    private MyReceiver receiver;
    private PopupWindowUtils popupWindowUtils;
    private View popView;
    private RelativeLayout popBgRelative;
    private TextView checkedJy, checkedSy, checkedWh, resertBtn, commitBtn;
    private ToggleButton toggleButton;
    private String toggleValue = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBindingView(R.layout.activity_frist);
        fristPersenter = new IFristPersenter();
        fristPersenter.attach(this);
        init();
        initTabs();
        initViewPager();

    }

    public void init() {
        initPopupView();
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
        popBgRelative = (RelativeLayout) popView.findViewById(R.id.pop_bg_relative);
        checkedJy = (TextView) popView.findViewById(R.id.checkbtn_jy);
        checkedSy = (TextView) popView.findViewById(R.id.checkbtn_sy);
        checkedWh = (TextView) popView.findViewById(R.id.checkbtn_wh);
        resertBtn = (TextView) popView.findViewById(R.id.text_resert);
        commitBtn = (TextView) popView.findViewById(R.id.text_commit);
        toggleButton = (ToggleButton) popView.findViewById(R.id.toggle_btn);
        String ImprList = SpConstant.getCheckPreferences().getString(SpConstant.CHECK_DATA_KEY, "");
        if (null != ImprList && !"".equals(ImprList)) {
            initChecked(checkedJy, ImprList.contains("金银"));
            initChecked(checkedSy, ImprList.contains("石油"));
            initChecked(checkedWh, ImprList.contains("外汇"));
        } else {
            initChecked(checkedJy, true);
            initChecked(checkedSy, true);
            initChecked(checkedWh, true);
            SpConstant.getCheckPreferences().edit().putString(SpConstant.CHECK_DATA_KEY, "金银，石油，外汇").commit();
        }
        String toggle = SpConstant.getTogglePreferences().getString(SpConstant.TOGGLE_DATA_KEY, "false");
        if (toggle.equals("true")) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }
        popBgRelative.setOnClickListener(this);
        checkedJy.setOnClickListener(this);
        checkedSy.setOnClickListener(this);
        checkedWh.setOnClickListener(this);
        resertBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleValue = "true";
                } else {
                    toggleValue = "false";
                }
            }
        });
    }

    public void initChecked(TextView view, boolean isChecked) {
        view.setSelected(!isChecked);
        view.setTag(!isChecked);
        setCheckedButton(view, !isChecked);

    }

    public void setCheckedButton(TextView textView, boolean isJY) {
        if (isJY) {
            textView.setSelected(false);
            textView.setTextColor(getResources().getColor(R.color.text_title));
            textView.setTag(false);
        } else {
            textView.setSelected(true);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setTag(true);
        }
    }

    private void initViewPager() {
        mFragments.add(PkxFragment.getInstance());
        mFragments.add(NewsFragment.getInstance());

        mDecorView = getWindow().getDecorView();

        viewPager = ViewFindUtils.find(mDecorView, R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), mTitles, mFragments));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabTop.setCurrentTab(position);
                if (position != 0) {
                    filterIcon.setVisibility(View.INVISIBLE);
                } else {
                    filterIcon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(0);
    }


    private void initTabs() {
        tabTop.setTabData(mTitles);

        tabTop.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position != 0) {
                    filterIcon.setVisibility(View.INVISIBLE);
                } else {
                    filterIcon.setVisibility(View.VISIBLE);
                }
                tabTop.setCurrentTab(position);
                viewPager.setCurrentItem(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
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
    public void onWindowFocusChanged(boolean hasFocus) {
// TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (!isShow) {
                if (BaseUtils.isNetworkConnected(this)) {
                    if (PkxApplicaion.getInstance().getUpdateBean() != null) {
                        fristPersenter.showUpdatePop(filterIcon);
                    } else if (PkxApplicaion.getInstance().getAdConfigBean() != null) {
                        fristPersenter.showAdPop(filterIcon);
                    }
                    isShow = true;

                }

            }


        }
    }

    @OnClick({R.id.filter_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_img:
                popupWindowUtils.dismiss();
                popupWindowUtils.initPopupWindwo(view, popView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                        Color.TRANSPARENT, R.style.popwindow_register_animation, 0,
                        0);
                break;
            case R.id.pop_bg_relative:
                popupWindowUtils.dismiss();
                break;
            case R.id.checkbtn_jy:
                setCheckedButton(checkedJy, (Boolean) checkedJy.getTag());
                break;
            case R.id.checkbtn_sy:
                setCheckedButton(checkedSy, (Boolean) checkedSy.getTag());
                break;
            case R.id.checkbtn_wh:
                setCheckedButton(checkedWh, (Boolean) checkedWh.getTag());
                break;
            case R.id.text_resert:
                setCheckedButton(checkedJy, false);
                setCheckedButton(checkedSy, false);
                setCheckedButton(checkedWh, false);
                SpConstant.getCheckPreferences().edit().putString(SpConstant.CHECK_DATA_KEY, "金银，石油，外汇").commit();
                toggleButton.setChecked(false);
                break;
            case R.id.text_commit:
                String value = "";
                if ((Boolean) checkedJy.getTag()) {
                    value += "金银，";
                }
                if ((Boolean) checkedSy.getTag()) {
                    value += "石油，";
                }
                if ((Boolean) checkedWh.getTag()) {
                    value += "外汇";
                }
                SpConstant.getCheckPreferences().edit().putString(SpConstant.CHECK_DATA_KEY, value).commit();
                SpConstant.getTogglePreferences().edit().putString(SpConstant.TOGGLE_DATA_KEY, toggleValue).commit();
                PkxFragment.getInstance().notifyAdapter();
                PkxFragment.getInstance().chuliData();
                popupWindowUtils.dismiss();

                break;
        }
    }

    @Override
    public void ShowAdPopView() {
        if (PkxApplicaion.getInstance().getAdConfigBean() != null) {
            fristPersenter.showAdPop(filterIcon);
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getExtras().getString("msg");
            if (msg.equals("isConn")) {
                PkxFragment.getInstance().OnAfreshLoad();
                NewsFragment.getInstance().OnAfreshLoad();
            } else {
                ToastView.makeText3(FristActivity.this, "网络连接断开");
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
        if (myReceiver != null) {
            this.unregisterReceiver(myReceiver);
        }
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
        Map<String, String> map = (Map<String, String>) SpConstant.getSelectPreferences().getAll();
        for (String key : map.keySet()) {
            SpConstant.getSelectPreferences().edit().remove(key).commit();
        }
    }
}
