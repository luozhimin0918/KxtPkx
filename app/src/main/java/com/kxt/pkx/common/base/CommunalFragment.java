package com.kxt.pkx.common.base;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.library.base.IBaseView;
import com.library.base.LibFragment;

/**
 * Created by DaiYao on 2016/9/11.
 */
public abstract class CommunalFragment extends LibFragment implements IBaseView {
    /**
     * 是否切换到显示界面
     */
    protected boolean isVisibleToUser = false;
    /**
     * 是否准备好可以开始加载数据
     */
    protected int loadStatus = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //不保存状态
    }


    /**
     * 针对于add fragment 中的setUserVisibleHint() 不执行
     * ViewPage 的显示方法用 #setUserVisibleHint(isVisibleToUser)
     */
    public void onVisibleHint(boolean isActivityToUser) {
    }

    @Override
    protected void onInitialize(Bundle savedInstanceState) {
        loadStatus = 1;
        onVisible();
    }

    /**
     * 懒加载逻辑
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            this.isVisibleToUser = true;
            onVisible();
        } else {
            this.isVisibleToUser = false;
        }
    }

    private void onVisible() {
        onVisibleSure();
        if (loadStatus == 0 || loadStatus == 2 || !isVisibleToUser) {
            return;
        }
        onVisibleInit();
        loadStatus = 2;
    }

    public void onVisibleInit() {

    }

    @Override
    public void onDestroyView() {
        Glide.get(getContext()).clearMemory();
        super.onDestroyView();
        loadStatus = 0;
        isVisibleToUser = false;
    }

    public RequestQueue getRequestQueue() {
        CommunalActivity activity = (CommunalActivity) getActivity();
        return activity.getRequestQueue();
    }

    /**
     * 每次重新显示调用
     */
    public void onVisibleSure() {

    }


}
