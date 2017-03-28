package com.kxt.pkx.common.coustom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kxt.pkx.R;

/**
 * Created by Administrator on 2017/2/16 0016.
 */

public class TabLinearLayout extends LinearLayout {

    private View view;
    private TextView textView;
    private ImageView imageView;
    private LinearLayout bg_linear;
    private Context context;

    public TabLinearLayout(Context context) {
        super(context);
        drawView(context);
    }

    public TabLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawView(context);
    }

    public TabLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawView(context);
    }

    private void drawView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.tab_linear_view, this, true);
        bg_linear = (LinearLayout) view.findViewById(R.id.bg_linear);
        textView = (TextView) view.findViewById(R.id.tab_text);
        imageView = (ImageView) view.findViewById(R.id.tab_img);
    }

    public void setEffectHigh() {
        setEffectHigh(false);
    }

    public void setEffectLow() {
        setEffectLow(false);
    }

    public void setEffectHigh(boolean isRotate) {
        if (isRotate) {
            imageView.setImageResource(R.drawable.rotate_red);
        } else {
            imageView.setImageResource(R.drawable.red_icon);
        }
        bg_linear.setBackgroundResource(R.drawable.cjrl_red_shap);
        textView.setTextColor(context.getResources().getColor(R.color.red_2c));
    }

    public void setEffectLow(boolean isRotate) {
        if (isRotate) {
            imageView.setImageResource(R.drawable.rotate_green);
        } else {
            imageView.setImageResource(R.drawable.green_icon);
        }
        bg_linear.setBackgroundResource(R.drawable.cjrl_green_shap);
        textView.setTextColor(context.getResources().getColor(R.color.green_3d));
    }

    public void setEffectMind() {
        imageView.setVisibility(View.GONE);
        bg_linear.setBackgroundResource(R.drawable.cjrl_yellow_shap);
        textView.setTextColor(context.getResources().getColor(R.color.yellow_25));
    }

    public void setTextValue(String textValue) {
        textView.setText(textValue);
    }

}
