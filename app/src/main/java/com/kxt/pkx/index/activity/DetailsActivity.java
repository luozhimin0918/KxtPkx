package com.kxt.pkx.index.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kxt.pkx.R;
import com.kxt.pkx.common.base.CommunalActivity;
import com.kxt.pkx.common.coustom.LoadWebView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class DetailsActivity extends CommunalActivity {


    @BindView(R.id.load_webview)
    LoadWebView loadWebview;
    @BindView(R.id.back_adweb)
    RelativeLayout backAdweb;
    @BindView(R.id.text_webtitle)
    TextView textWebtitle;
    private String url;
    private String title;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBindingView(R.layout.activity_detail);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        from = getIntent().getStringExtra("from");

        loadWebview.loadUrl(url);
        if (!"".equals(title) && null!=title) {
            textWebtitle.setText(title);
        } else {
            loadWebview.setWebTitle(textWebtitle);
        }

    }

    @OnClick({R.id.back_adweb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_adweb:
                if (from.equals("main") ||from.equals("news") ) {
                    finish();
                } else {
                    Intent intent = new Intent(this, FristActivity.class);
                    startActivity(intent);
                }

                break;

        }
    }
}
