package com.kxt.pkx.index.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kxt.pkx.R;

import org.json.JSONArray;

import java.util.List;

/**
 * 广告轮播adapter
 *
 * @author dong
 * @data 2015年3月8日下午3:46:35
 * @contance dong854163@163.com
 */
public class AdvertisementAdapter extends PagerAdapter {

    private Context context;
    private List<View> views;
    JSONArray advertiseArray;

    public AdvertisementAdapter() {
        super();
        // TODO Auto-generated constructor stub
    }

    public AdvertisementAdapter(Context context, List<View> views,
                                JSONArray advertiseArray) {
        this.context = context;
        this.views = views;
        this.advertiseArray = advertiseArray;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        View view = views.get(position);
        try {
            String head_img = advertiseArray.optJSONObject(position).optString("head_img");
            final ImageView ivAdvertise = (ImageView) view.findViewById(R.id.ivAdvertise);
            Glide.with(context)
                    .load(head_img)
                    .asBitmap()
                    .placeholder(R.mipmap.backgroutd_slid)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            ivAdvertise.setImageBitmap(resource);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
