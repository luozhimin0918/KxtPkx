package com.kxt.pkx.common.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.kxt.pkx.R;
import com.kxt.pkx.index.activity.FristActivity;
import com.library.manager.ActivityManager;

import butterknife.BindView;

/**
 * Created by Mr'Dai on 2016/11/16.
 */

public class ErrorActivity extends CommunalActivity {
    public static final String ERROR_MSG = "errorMsg";

    @BindView(R.id.error_title) TextView tvErrorTitle;
    @BindView(R.id.error_content) TextView tvErrorContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBindingView(R.layout.error_activity);
        String errorMsg = getIntent().getStringExtra(ERROR_MSG);
        tvErrorContent.setText(errorMsg);
        tvErrorContent.setMovementMethod(ScrollingMovementMethod.getInstance());
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setPositiveButton("进入",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ErrorActivity.super.onBackPressed();
                                Intent intent = new Intent(ErrorActivity.this, FristActivity.class);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("退出",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ErrorActivity.super.onBackPressed();
                                ActivityManager.getInstance().finishAllActivity();
                            }
                        })
                .setMessage("是否进入到首页呢?").show();

    }
}
