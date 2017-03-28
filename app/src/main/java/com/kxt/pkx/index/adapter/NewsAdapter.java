package com.kxt.pkx.index.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kxt.pkx.R;
import com.kxt.pkx.common.constant.SpConstant;
import com.kxt.pkx.common.coustom.MyTabView;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.index.activity.DetailsActivity;
import com.kxt.pkx.index.jsonBean.NewsDataBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<NewsDataBean.DataBean> dataBeans;

    public NewsAdapter(Context context, List<NewsDataBean.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return dataBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.news_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.newTitle.setText(dataBeans.get(position).getTitle());
        String reads = SpConstant.getReadPreferences().getString(dataBeans.get(position).getId(), "");
        if (null != reads && !"".equals(reads)) {
            viewHolder.newTitle.setTextColor(context.getResources().getColor(R.color.font_bb2));
        } else {
            viewHolder.newTitle.setTextColor(context.getResources().getColor(R.color.text_title));
        }
        try {
            String time = BaseUtils.getDateBySjc(dataBeans.get(position).getAddtime()).substring(5, 16);
            viewHolder.newsTime.setText(time);
        } catch (Exception e) {
            e.getMessage();
            viewHolder.newsTime.setText("");
        }

        Glide.with(context)
                .load(dataBeans.get(position).getThumb())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        viewHolder.newsImg.setImageBitmap(resource);
                    }
                });

        ArrayList<String> tags = (ArrayList<String>) dataBeans.get(position).getTags();
        viewHolder.newsTab.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            MyTabView myTabView = new MyTabView(context);
            if (null != tags.get(i) && !"".equals(tags.get(i))) {
                if (tags.get(i).equals("广告")) {
                    myTabView.setTabStyle(true, tags.get(i));
                } else {
                    myTabView.setTabStyle(false, tags.get(i));
                }
                viewHolder.newsTab.addView(myTabView);
            }

        }

        viewHolder.newsRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpConstant.getReadPreferences().edit().putString(dataBeans.get(position).getId(), dataBeans.get(position).getId()).commit();
                viewHolder.newTitle.setTextColor(context.getResources().getColor(R.color.font_bb2));
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("from", "news");
                intent.putExtra("url", dataBeans.get(position).getUrl());
                intent.putExtra("title", "要闻");
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.news_img)
        ImageView newsImg;
        @BindView(R.id.new_title)
        TextView newTitle;
        @BindView(R.id.news_time)
        TextView newsTime;
        @BindView(R.id.news_tab)
        LinearLayout newsTab;
        @BindView(R.id.news_root)
        LinearLayout newsRoot;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
