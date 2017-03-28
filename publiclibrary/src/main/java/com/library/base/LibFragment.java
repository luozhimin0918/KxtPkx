package com.library.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.library.R;
import com.library.annotation.OnDataBinding;
import com.library.util.LogUtil;
import com.library.util.SystemUtil;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DaiYao on 2016/5/28 0028.
 */
public abstract class LibFragment extends Fragment
{
    protected final String TAG = this.getClass().getName();

    public LinearLayout replaceLayout;
    private LayoutInflater inflater;
    private Unbinder unbinder;

//    private RequestQueue mQueue;
    /**
     * 使用OnDataBinding标注
     * {@link com.library.annotation.OnDataBinding}.
     */
    protected ViewDataBinding viewDataBinding;

    protected abstract void onInitialize(Bundle savedInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.inflater = inflater;
        onInitialize(savedInstanceState);
        return replaceLayout;
    }

    protected void setBindingView(@LayoutRes int layoutResID)
    {
        setBindingView(layoutResID, -1);
    }

    protected void setBindingView(@LayoutRes int layoutResID, int statusColor)
    {
        View contentView = inflater.inflate(layoutResID, null);
        replaceLayout = (LinearLayout) inflater.inflate(R.layout.fragment_status,null);


        if (statusColor != -1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            View statusBarView = new View(getContext());
            statusBarView.setTag("fragment_statusBar");

            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    SystemUtil.getStatusHeight(getContext()));

            statusBarView.setBackgroundColor(statusColor);
            replaceLayout.addView(statusBarView, lp);
        }

        contentView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        replaceLayout.addView(contentView);
        try
        {
            viewDataBinding = DataBindingUtil.bind(replaceLayout);
        } catch (Exception e)
        {

        }
        unbinder = ButterKnife.bind(this, replaceLayout);
    }


    /**
     * 注解绑定生成的Binding
     */
    public void initAnnotation()
    {
        try
        {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields)
            {
                if (field.isAnnotationPresent(OnDataBinding.class))
                {
                    field.setAccessible(true);
                    field.set(this, viewDataBinding);
                    field.setAccessible(false);
                }
            }
        } catch (Exception e)
        {
            LogUtil.e("注解异常", TAG + "initAnnotation");
            e.printStackTrace();
        }
    }

//    public RequestQueue getRequestQueue()
//    {
//        return mQueue;
//    }

    public View getContentView()
    {
        return replaceLayout;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }
}
