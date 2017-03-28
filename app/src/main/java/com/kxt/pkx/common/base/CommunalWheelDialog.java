package com.kxt.pkx.common.base;

import android.app.Dialog;
import android.content.Context;

import com.kxt.pkx.R;


/**
 * 项目名:FirstGold_2.1.0
 * 类描述:
 * 创建人:苟蒙蒙
 * 创建日期:2016/12/24.
 */

public class CommunalWheelDialog extends Dialog {

    public Context context;

    public CommunalWheelDialog(Context context) {
        super(context, R.style.wheelStyle);
        this.context = context;
    }
}
