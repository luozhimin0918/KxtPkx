package com.library.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.library.manager.ActivityManager;
import com.library.util.StatusBarCompat;
import com.library.util.volley.RequestQueueUtil;

import butterknife.ButterKnife;

/**
 * Created by DaiYao on 2016/5/15 0015.
 */
public abstract class LibActivity extends FragmentActivity {
    public final String TAG = this.getClass().getName();

    protected RequestQueue mQueue;
    /**
     * 使用OnDataBinding标注
     * {@link com.library.annotation.OnDataBinding}.
     */
    protected ViewDataBinding viewDataBinding;

    /**
     * onCreate init Layout and init Function Parameter
     * init Layout employ {@link #setBindingView(int)} DataBinding
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().pushOneActivity(this);
    }

    protected void setBindingView(int layoutResID) {
        setBindingView(layoutResID, true);
    }

    protected void setBindingView(int layoutResID, boolean isStatusColor) {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResID);
        mQueue = RequestQueueUtil.newRequestQueue(this);
        ButterKnife.bind(this);

        if (isStatusColor) {
            StatusBarCompat.compat(this, 0XFF009AFF);
        }
    }

    /**
     * Activity销毁 设置Tag的请求不销毁！
     */
    RequestQueue.RequestFilter mRequestFilter = new RequestQueue.RequestFilter() {
        @Override
        public boolean apply(Request<?> request) {
            Object mRequestTag = request.getTag();
            if (mRequestTag == null) {
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        if (mQueue != null)
            mQueue.cancelAll(mRequestFilter);
        ActivityManager.getInstance().popOneActivity(this);
        super.onDestroy();
    }
}
