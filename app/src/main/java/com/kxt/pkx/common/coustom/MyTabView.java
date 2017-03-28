package com.kxt.pkx.common.coustom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kxt.pkx.R;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class MyTabView extends LinearLayout {
    private Context context;
    private View view;
    private TextView textView;

    public MyTabView(Context context) {
        super(context);
        drawView(context);
    }

    public MyTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawView(context);
    }

    public MyTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawView(context);
    }

    private void drawView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.tab_viw, this, true);
        textView = (TextView) view.findViewById(R.id.tab_text);
    }

    public void setTabStyle(boolean isAd, String textValue) {
        if (isAd) {
            textView.setTextColor(context.getResources().getColor(R.color.text_title_red));
            textView.setBackgroundResource(R.drawable.tab_red_shap);
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.blue_ff));
            textView.setBackgroundResource(R.drawable.tab_blue_shap);
        }
        textView.setText(textValue);

    }
}
