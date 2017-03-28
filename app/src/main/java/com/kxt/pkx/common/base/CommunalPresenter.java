package com.kxt.pkx.common.base;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.library.base.IBaseView;

/**
 * Created by DaiYao on 2016/9/11.
 */
public class CommunalPresenter<T extends IBaseView> {
    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }

    public Context getContext() {
        return mView.getContext();
    }

    public RequestQueue getRequestQueue() {
        return mView.getRequestQueue();
    }
}
