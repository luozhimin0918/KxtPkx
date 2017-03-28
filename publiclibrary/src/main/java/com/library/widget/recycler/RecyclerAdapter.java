package com.library.widget.recycler;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;


/**
 * @author Mr'Dai
 * @date 2016/3/29 13:44
 * @Title: ddp
 * @package cn.com.longbang.kdy.adapter
 * <p/>
 * mRoleInfoRecycler.setLayoutManager(new LinearLayoutManager(this));
 * mRoleInfoRecycler.addItemDecoration(new DividerItemDecoration(this));
 * mRoleInfoRecycler.setAdapter(mRoleInfoAdapter);
 * @Description:
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter
{
    public AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener)
    {
        mOnItemClickListener = onItemClickListener;
    }

    protected Context mContext;
    protected List<T> mListData;
    protected int mLayoutRes;
    protected LayoutInflater mInflate;

    public RecyclerAdapter(Context mContext, List<T> mListData, int mLayoutRes)
    {
        this.mContext = mContext;
        this.mListData = mListData;
        this.mLayoutRes = mLayoutRes;
        mInflate = LayoutInflater.from(mContext);
    }


    @Override
    public int getItemCount()
    {
        return this.mListData.size();
    }

    public T getItem(int position)
    {
        return this.mListData.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mRoot;
        ViewDataBinding mViewDataBinding = null;

        try
        {
            mViewDataBinding = DataBindingUtil.inflate(mInflate, mLayoutRes, parent, false);
            mRoot = mViewDataBinding.getRoot();
        } catch (Exception e)
        {
            mRoot = mInflate.inflate(mLayoutRes, parent, false);
        }
        RecyclerHolder viewHolder = (RecyclerHolder) onCreateViewHolders(mRoot,viewType);
        if (mViewDataBinding != null)
            viewHolder.setViewDataBinding(mViewDataBinding);
        return viewHolder;
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolders(View inflate, int viewType);


    public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private ViewDataBinding mViewDataBinding;

        public ViewDataBinding getViewDataBinding()
        {
            return mViewDataBinding;
        }

        public void setViewDataBinding(ViewDataBinding viewDataBinding)
        {
            mViewDataBinding = viewDataBinding;
        }

        public RecyclerHolder(View view)
        {
            super(view);
            view.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v)
        {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(null, v, getLayoutPosition(), 0);
            else
                System.out.println("Item 没有设置单击事件");
        }
    }
}
