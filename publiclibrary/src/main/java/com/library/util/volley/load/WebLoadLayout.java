package com.library.util.volley.load;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.library.R;
import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Created by DaiYao on 2016/5/21 0021.
 */
public class WebLoadLayout extends FrameLayout implements View.OnClickListener
{
    private final String TAG = this.getClass().getName();

    private final String LOADING = "loading";
    private final String LOADERROR = "loaderror";
    private final String NETERROR = "neterror";

    public interface OnAfreshLoadListener
    {
        void OnAfreshLoad();
    }

    private OnAfreshLoadListener onAfreshLoadListener;

    private LayoutInflater mInflater;

    public WebLoadLayout(Context context)
    {
        this(context, null);
    }

    public WebLoadLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public WebLoadLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
    }

    private RelativeLayout llLoading;

    public void startLoading()
    {
        removeLoading();
        llLoading = (RelativeLayout) mInflater.inflate(R.layout.web_loading, null);
        llLoading.setOnClickListener(null);

        llLoading.setTag(LOADING);

        ProgressWheel progressWheel = (ProgressWheel) llLoading.findViewById(R.id.id_volley_loading_pro);
        progressWheel.spin();

        updateState();
    }

    public void loadSuccess()
    {
        removeLoading();
    }

    public void loadError()
    {
        removeLoading();
        llLoading = (RelativeLayout) mInflater.inflate(R.layout.volley_load_error, null);
        llLoading.setOnClickListener(this);
        llLoading.setTag(LOADERROR);
        updateState();
    }

    public void netError()
    {
        removeLoading();
        llLoading = (RelativeLayout) mInflater.inflate(R.layout.volley_load_offnet, null);
        llLoading.setOnClickListener(this);
        llLoading.setTag(NETERROR);
        updateState();
    }


    private void updateState()
    {
        addView(llLoading);
    }

    private void removeLoading()
    {
        if (llLoading != null)
        {
            if (LOADING.equals(llLoading.getTag()))
            {
                ProgressWheel progresswheel = (ProgressWheel) llLoading.findViewWithTag("progresswheel");
                progresswheel.stopSpinning();
            }
            removeView(llLoading);
        }
    }

    public void setOnAfreshLoadListener(OnAfreshLoadListener onAfreshLoadListener)
    {
        this.onAfreshLoadListener = onAfreshLoadListener;
    }

    @Override
    public void onClick(View v)
    {
        //重新调用接口
        if (onAfreshLoadListener != null)
        {
            startLoading();
            onAfreshLoadListener.OnAfreshLoad();
        }
    }

}
