package com.kxt.pkx.index.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kxt.pkx.R;
import com.kxt.pkx.common.base.CommunalActivity;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.index.jsonBean.AdConfigBean;
import com.kxt.pkx.index.persenter.WelcomePersenter;
import com.kxt.pkx.index.view.IWelcomeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class WelcomeActivity extends CommunalActivity implements IWelcomeView {

    @BindView(R.id.image_welcome)
    ImageView imageWelcome;
    @BindView(R.id.relative_welcome)
    RelativeLayout relativeWelcome;
    @BindView(R.id.welcome_root)
    LinearLayout welcomeRoot;
    private WelcomePersenter persenter;
    private AdConfigBean adConfigBean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Intent intent = new Intent(WelcomeActivity.this, FristActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    handler.removeMessages(0);
                    relativeWelcome.setVisibility(View.VISIBLE);
                    Glide.with(getContext())
                            .load(adConfigBean.getData().getAdvertisement().getImageUrl())
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    imageWelcome.setImageBitmap(resource);
                                    handler.sendEmptyMessageDelayed(0, 3000);
                                }
                            });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setBindingView(R.layout.activity_welcome, false);
        persenter = new WelcomePersenter();
        persenter.attach(this);

        if (BaseUtils.isNetworkConnected(this)) {
            persenter.getAdConfig();
        } else {
            welcomeRoot.setBackgroundResource(R.mipmap.welcome);
            handler.sendEmptyMessageDelayed(0, 3000);
        }

    }


    @Override
    public void toMainActivity() {
        welcomeRoot.setBackgroundResource(R.mipmap.welcome);
        handler.sendEmptyMessageDelayed(0, 1000);

    }

    @Override
    public void showAd(AdConfigBean adConfigBean) {
        this.adConfigBean = adConfigBean;
        welcomeRoot.setBackgroundResource(R.mipmap.welcome_ad);
        handler.sendEmptyMessage(1);
    }

    @OnClick({R.id.image_welcome})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_welcome:
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("url", adConfigBean.getData().getAdvertisement().getUrl());
                intent.putExtra("title", adConfigBean.getData().getAdvertisement().getTitle());
                intent.putExtra("from", "welcome");
                startActivity(intent);
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }
}
