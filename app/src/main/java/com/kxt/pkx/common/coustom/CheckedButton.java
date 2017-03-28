package com.kxt.pkx.common.coustom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.kxt.pkx.R;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

public class CheckedButton extends View {
    private View view;
    private TextView textView;
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        invalidate();
    }


    public CheckedButton(Context context) {
        super(context);
        drawView(context);
    }

    public CheckedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawView(context);
    }

    public CheckedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawView(context);
    }

    public void drawView(Context context) {
        view = View.inflate(context, R.layout.check_button_view, null);
        textView = (TextView) view.findViewById(R.id.text_btn);
        if (select) {
            textView.setTextColor(context.getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.blue_shap);
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.text_title));
            textView.setBackgroundResource(R.drawable.gray_shap);
        }
    }

    public void setTextView(String textValue) {
        textView.setText(textValue);
    }
}
