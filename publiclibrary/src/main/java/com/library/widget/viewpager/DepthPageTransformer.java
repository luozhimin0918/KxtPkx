package com.library.widget.viewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by DaiYao on 2016/9/21.
 */
public class DepthPageTransformer implements ViewPager.PageTransformer
{
    public void transformPage(View view, float position)
    {

        if (position < -1)
        {

        } else if (position <= 0)
        {
            view.setPivotX(view.getMeasuredWidth());
            view.setPivotY(0);
            view.setScaleX(1 + position);

        } else if (position <= 1)
        {
            view.setPivotX(0);
            view.setPivotY(0);
            view.setScaleX(1 - position);
        } else
        {
        }
    }
}
