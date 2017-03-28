package com.kxt.pkx.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.socks.library.KLog;

/**
 * Created by Luozhimin on 2016/8/30.14:46
 */
public class SupportDisplay {

    /**
     * 基准屏横
     */
    private static final float BASIC_SCREEN_WIDTH = 1080f;
    /**
     * 基准屏竖
     */
    private static final float BASIC_SCREEN_HEIGHT = 1920f;

    /**
     * 基准屏dpi
     */
    private static final float BASIC_DENSITY = 3.0f;

    /**
     * 误差率
     */
    private static final float COMPLEMENT_VALUE = 0.5f;

    /**
     * 手机屏宽
     */
    public static int mDisplayWidth;
    /**
     * 手机屏高
     */
    public static int mDisplayHeight;
    /**
     * 水平比例
     */
    private static float mLayoutScale;
    /**
     * 垂直比例
     */
    private static float mVerticalScale;

    private static float mDensityScale;

    private static float mDensityMetrics;

    /**
     * 初始化
     *
     * @param context context
     */
    public static void initLayoutSetParams(Context context) {
        if (context == null) {
            return;
        }
        final WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        final Display disp = wm.getDefaultDisplay();
        final Point point = new Point();
        getSize(disp, point);
        mDisplayWidth = point.x;
        mDisplayHeight = point.y;
        mLayoutScale = mDisplayWidth / BASIC_SCREEN_WIDTH;
        mVerticalScale = mDisplayWidth / BASIC_SCREEN_HEIGHT;
        mDensityMetrics = context.getResources().getDisplayMetrics().density;// 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        mDensityScale = BASIC_DENSITY / mDensityMetrics;
        KLog.d("" + mDisplayWidth + ">" + mDisplayHeight + "<<<" + mLayoutScale + ">>" + mDensityMetrics + "???" + mDensityScale);
    }

    public static float getLayoutScale() {
        return mLayoutScale;
    }

    public static int getmDisplayWidth() {
        return mDisplayWidth;
    }


    public static int getmDisplayHeight() {
        return mDisplayHeight;
    }


    public static float getmVerticalScale() {
        return mVerticalScale;
    }

    /**
     * @param display System display.
     * @param outSize Target size.
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private static void getSize(Display display, Point outSize) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            display.getSize(outSize);
        } else {
            outSize.x = display.getWidth();
            outSize.y = display.getHeight();
        }
    }

    /**
     * @param sizedp
     * @return
     */
    private static int calculateActualControlerSize(float sizedp) {
        return (int) ((sizedp / mDensityMetrics) * mLayoutScale + COMPLEMENT_VALUE);
    }

    /**
     * 对文字大小计算
     *
     * @param textView   TextView
     * @param textSizedp
     */
    private static void resetContrlerTextSize(TextView textView,
                                              float textSizedp) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizedp
                * mLayoutScale * mDensityScale);

    }

    /**
     * 对Button计算
     *
     * @param button     Button
     * @param textSizedp
     */
    public static void resetContrlerTextSize(Button button, float textSizedp) {
        resetContrlerTextSize((TextView) button, textSizedp);

    }

    /**
     * 重新计算所有Layout
     *
     * @param rootView
     */
    public static void resetAllChildViewParam(ViewGroup rootView) {
        int childCount = rootView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = rootView.getChildAt(i);
            if (childView instanceof ViewGroup) {
                setChildViewParam((ViewGroup) childView);
            } else {
                setViewParam(childView);
            }
        }
    }

    /**
     * 重新计算所有Layout
     *
     * @param parentView
     */
    private static void setChildViewParam(ViewGroup parentView) {
        if (parentView == null) {
            return;
        }
        setViewParam(parentView);
        int childCount = parentView.getChildCount();
        if (childCount == 0) {
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View childView = parentView.getChildAt(i);
            if (childView instanceof ViewGroup) {
                setChildViewParam((ViewGroup) childView);
            } else {
                setViewParam(childView);
            }
        }
    }

    /**
     * 重新计算所有Layout
     *
     * @param view
     */
    private static void setViewParam(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int height = lp.height;
        if (height != -1 && height != -2) {
            lp.height = calculateActualControlerSize(height);
        }
        int width = lp.width;
        if (width != -1 && width != -2) {
            lp.width = calculateActualControlerSize(width);
        }

        if (!(lp instanceof RelativeLayout.LayoutParams
                || lp instanceof LinearLayout.LayoutParams
                || lp instanceof FrameLayout.LayoutParams || lp instanceof TableLayout.LayoutParams)) {
            return;
        }
        if (lp instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) view
                    .getLayoutParams();
            int leftMargin = param.leftMargin;
            if (leftMargin != -1) {
                param.leftMargin = calculateActualControlerSize(leftMargin);
            }
            int rightMargin = param.rightMargin;
            if (rightMargin != -1) {
                param.rightMargin = calculateActualControlerSize(rightMargin);
            }
            int topMargin = param.topMargin;
            if (topMargin != -1) {
                param.topMargin = calculateActualControlerSize(topMargin);
            }
            int bottomMargin = param.bottomMargin;
            if (bottomMargin != -1) {
                param.bottomMargin = calculateActualControlerSize(bottomMargin);
            }
        } else if (lp instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) view
                    .getLayoutParams();
            int leftMargin = param.leftMargin;
            if (leftMargin != -1) {
                param.leftMargin = calculateActualControlerSize(leftMargin);
            }
            int rightMargin = param.rightMargin;
            if (rightMargin != -1) {
                param.rightMargin = calculateActualControlerSize(rightMargin);
            }
            int topMargin = param.topMargin;
            if (topMargin != -1) {
                param.topMargin = calculateActualControlerSize(topMargin);
            }
            int bottomMargin = param.bottomMargin;
            if (bottomMargin != -1) {
                param.bottomMargin = calculateActualControlerSize(bottomMargin);
            }

        } else if (lp instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) view
                    .getLayoutParams();
            int leftMargin = param.leftMargin;
            if (leftMargin != -1) {
                param.leftMargin = calculateActualControlerSize(leftMargin);
            }
            int rightMargin = param.rightMargin;
            if (rightMargin != -1) {
                param.rightMargin = calculateActualControlerSize(rightMargin);
            }
            int topMargin = param.topMargin;
            if (topMargin != -1) {
                param.topMargin = calculateActualControlerSize(topMargin);
            }
            int bottomMargin = param.bottomMargin;
            if (bottomMargin != -1) {
                param.bottomMargin = calculateActualControlerSize(bottomMargin);
            }

        } else if (lp instanceof TableLayout.LayoutParams) {
            TableLayout.LayoutParams param = (TableLayout.LayoutParams) view
                    .getLayoutParams();
            int leftMargin = param.leftMargin;
            if (leftMargin != -1) {
                param.leftMargin = calculateActualControlerSize(leftMargin);
            }
            int rightMargin = param.rightMargin;
            if (rightMargin != -1) {
                param.rightMargin = calculateActualControlerSize(rightMargin);
            }
            int topMargin = param.topMargin;
            if (topMargin != -1) {
                param.topMargin = calculateActualControlerSize(topMargin);
            }
            int bottomMargin = param.bottomMargin;
            if (bottomMargin != -1) {
                param.bottomMargin = calculateActualControlerSize(bottomMargin);
            }
        }
        int leftPadding = view.getPaddingLeft();
        if (leftPadding != -1) {
            leftPadding = calculateActualControlerSize(leftPadding);
        }
        int rightPadding = view.getPaddingRight();
        if (rightPadding != -1) {
            rightPadding = calculateActualControlerSize(rightPadding);
        }
        int topPadding = view.getPaddingTop();
        if (topPadding != -1) {
            topPadding = calculateActualControlerSize(topPadding);
        }
        int bottomPadding = view.getPaddingBottom();
        if (bottomPadding != -1) {
            bottomPadding = calculateActualControlerSize(bottomPadding);
        }
        view.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        if (view instanceof EditText || view instanceof TextView) {
            TextView tv = (TextView) view;
            float textSize = tv.getTextSize();
            resetContrlerTextSize(tv, textSize);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (view instanceof GridView) {
                GridView gridView = (GridView) view;
                int horizontalSpacing = gridView.getHorizontalSpacing();
                int verticalSpacing = gridView.getVerticalSpacing();
                if (horizontalSpacing != -1) {
                    gridView.setHorizontalSpacing(calculateActualControlerSize(horizontalSpacing));
                }
                if (verticalSpacing != -1) {
                    gridView.setVerticalSpacing(calculateActualControlerSize(verticalSpacing));
                }
            }
        }

    }
}
