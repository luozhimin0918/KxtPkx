package com.library.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class StatusBarCompat
{
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#00000000");

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor)
    {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            if (statusColor != INVALID_VAL)
//            {
//                activity.getWindow().setStatusBarColor(statusColor);
//            }
//            return;
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES
// .LOLLIPOP)
//        {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            int color = COLOR_DEFAULT;
            final ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL)
            {
                color = statusColor;
            }

            View userContentView = contentView.getChildAt(0);

            ViewGroup parent = (ViewGroup) contentView.getParent();
            contentView.removeView(userContentView);
            parent.removeView(contentView);

            LinearLayout replaceLayout = new LinearLayout(activity);
            replaceLayout.setId(com.library.R.id.contents);

            replaceLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            replaceLayout.setOrientation(LinearLayout.VERTICAL);


            View statusBarView = new View(activity);
            statusBarView.setTag("statusBar");

            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));

            statusBarView.setBackgroundColor(color);
            replaceLayout.addView(statusBarView, lp);

            replaceLayout.addView(userContentView);

            parent.addView(replaceLayout);
        }
    }

    public static void compat(Activity activity)
    {
        compat(activity, INVALID_VAL);
    }


    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}