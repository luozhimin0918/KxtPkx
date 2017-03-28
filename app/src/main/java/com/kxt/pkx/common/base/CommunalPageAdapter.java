package com.kxt.pkx.common.base;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr'Dai on 2016/10/12.
 */

public abstract class CommunalPageAdapter<T> extends PagerAdapter
{
    protected Context context;
    protected List<T> data;

    private List<View> list;
    private boolean isRemain;

    private int remainder = 3;

    public CommunalPageAdapter(Context context, List<T> data, boolean isRemain)
    {
        super();
        this.context = context;
        this.data = data;
        this.isRemain = isRemain;

        list = new ArrayList<>();
        initBindingLayout(list);
    }

    @Override
    public int getCount()
    {
        if (data == null)
        {
            return list.size();
        } else
        {
            return data.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        View view;
        if (isRemain)
        {
            view = list.get(position % remainder);
        } else
        {
            view = list.get(position);
        }
        getView(view, position);

        if (!isRemain)
        {
            if (view.getParent() != null && view.getParent().equals(container))
            {
                return view;
            }
        }
        if (view.getParent() != null)
        {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        container.addView(view);
        return view;
    }

    protected abstract void initBindingLayout(List<View> list);

    /**
     * 设置余数
     *
     * @param remainder
     */
    public void setRemainder(int remainder)
    {
        this.remainder = remainder;
    }


    protected abstract void getView(View view, int position);
}