package com.library.util.volley.load;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.library.R;

/**
 * Created by DaiYao on 2016/5/21 0021.
 */
public class VolleyLoadLayout extends FrameLayout implements View.OnClickListener
{
    private final String TAG = this.getClass().getName();
    public interface OnAfreshLoadListener
    {
        void OnAfreshLoad();
    }
    private OnAfreshLoadListener onAfreshLoadListener;

    private ViewGroup mFullLoad;
    private TextView errorTip;
    private ContentLoadingProgressBar progressBar;

    private LayoutInflater mInflater;

    public VolleyLoadLayout(Context context)
    {
        this(context, null);
    }

    public VolleyLoadLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public VolleyLoadLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);

        mFullLoad = (ViewGroup) mInflater.inflate(R.layout.volley_fullload, null);
        errorTip = (TextView) mFullLoad.findViewById(R.id.id_volley_fullload_errortip);
        progressBar = (ContentLoadingProgressBar) mFullLoad.findViewById(R.id.id_volley_fullload_loadingbar);

        addView(mFullLoad);

        errorTip.setOnClickListener(this);
    }

    public void setOnAfreshLoadListener(OnAfreshLoadListener onAfreshLoadListener)
    {
        this.onAfreshLoadListener = onAfreshLoadListener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        mFullLoad.bringToFront();
    }

    public void hindFullLoad()
    {
        resetLayout();
        mFullLoad.setVisibility(View.GONE);
    }

    public void showLoading()
    {
        resetLayout();
        if (!mFullLoad.isShown())
            mFullLoad.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.show();
    }


    public void showLoadNotData()
    {
        showLoadError(R.string.volley_load_no_data);
    }

    public void showLoadError()
    {
        showLoadError(R.string.volley_load_error);
    }

    private void showLoadError(int tip)
    {
        resetLayout();
        if (!mFullLoad.isShown())
            mFullLoad.setVisibility(View.VISIBLE);

        errorTip.setVisibility(View.VISIBLE);
        errorTip.setText(tip);
    }


    /**
     * 重置布局
     */
    private void resetLayout()
    {
        for (int i = 0; i < mFullLoad.getChildCount(); i++)
        {
            View childAt = mFullLoad.getChildAt(i);
            if (childAt instanceof ContentLoadingProgressBar)
            {
                ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) childAt;
                progressBar.hide();
            }

            if (childAt.getVisibility() == View.VISIBLE)
                childAt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (onAfreshLoadListener != null)
            onAfreshLoadListener.OnAfreshLoad();
    }
}
