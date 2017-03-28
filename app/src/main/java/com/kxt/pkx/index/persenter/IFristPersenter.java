package com.kxt.pkx.index.persenter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kxt.pkx.PkxApplicaion;
import com.kxt.pkx.R;
import com.kxt.pkx.common.base.CommunalPresenter;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.PopupWindowUtils;
import com.kxt.pkx.index.activity.DetailsActivity;
import com.kxt.pkx.index.jsonBean.AdConfigBean;
import com.kxt.pkx.index.jsonBean.UpdateBean;
import com.kxt.pkx.index.view.IFristView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class IFristPersenter extends CommunalPresenter<IFristView> implements View.OnClickListener {

    private PopupWindowUtils popupUtils;
    private String adUrl;

    public IFristPersenter() {
        popupUtils = new PopupWindowUtils();
    }

    public void showAdPop(RelativeLayout filterIcon) {
        AdConfigBean adBean = PkxApplicaion.getInstance().getAdConfigBean();
        View adPopView = View.inflate(getContext(), R.layout.pop_ad_view, null);
        RelativeLayout imgRelatve = (RelativeLayout) adPopView.findViewById(R.id.relative_img);
        RelativeLayout webRelatve = (RelativeLayout) adPopView.findViewById(R.id.relative_web);
        ImageView imageClsoe = (ImageView) adPopView.findViewById(R.id.ad_close);
        ImageView imageBg = (ImageView) adPopView.findViewById(R.id.ad_bg);
        WebView webView = (WebView) adPopView.findViewById(R.id.ad_webview);
        ImageView webClose = (ImageView) adPopView.findViewById(R.id.web_close);
        if (adBean.getStatus().equals("1")) {
            if (adBean.getData().getAdvertisement().getType().equals("normal")) {
                adUrl = adBean.getData().getAdvertisement().getUrl();
                if (adUrl.startsWith("http")) {
                    imgRelatve.setVisibility(View.VISIBLE);
                    Glide.with(getContext())
                            .load(adBean.getData().getAdvertisement().getImageUrl())
                            .asBitmap()
                            .into(imageBg);
                    imageClsoe.setOnClickListener(this);
                    imageBg.setOnClickListener(this);
                    popupUtils.dismiss();
                    popupUtils.initPopupWindwo(filterIcon, adPopView,
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                            Color.TRANSPARENT, R.style.popwindow_register_animation, 0,
                            0);
                }

            } else {
                webRelatve.setVisibility(View.VISIBLE);
                if (adBean.getData().getAdvertisement().getUrl() != null && !"".equals(adBean.getData().getAdvertisement().getUrl())
                        && adBean.getData().getAdvertisement().getUrl().startsWith("http")) {
                    webClose.setOnClickListener(this);
                    webView.loadUrl(adBean.getData().getAdvertisement().getUrl());
                    webView.setWebViewClient(new MyWebViewClient());
                    popupUtils.dismiss();
                    popupUtils.initPopupWindwo(filterIcon, adPopView,
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                            Color.TRANSPARENT, R.style.popwindow_register_animation, 0,
                            0);
                }
            }
        }
    }

    public void showUpdatePop(RelativeLayout filterIcon) {
        UpdateBean updateBean = PkxApplicaion.getInstance().getUpdateBean();
        View updatePop = View.inflate(getContext(), R.layout.pop_update_view, null);
        ImageView updateClose = (ImageView) updatePop.findViewById(R.id.update_close);
        TextView textVersion = (TextView) updatePop.findViewById(R.id.update_version);
        TextView textSize = (TextView) updatePop.findViewById(R.id.update_size);
        TextView textContent = (TextView) updatePop.findViewById(R.id.update_content);
        TextView textUpdate = (TextView) updatePop.findViewById(R.id.upate_update);

        String version = updateBean.getData().getVersion();
        boolean isNumOk = true;
        if (version != null && !"".equals(version)) {
            String[] strs = version.split("\\.");
            if (null != strs && strs.length == 3) {
                for (int i = 0; i < strs.length; i++) {
                    Pattern pattern = Pattern.compile("[0-9]*");
                    Matcher isNum = pattern.matcher(strs[i]);
                    if (!isNum.matches()) {
                        isNumOk = false;
                        mView.ShowAdPopView();
                        break;
                    }
                }
                if (isNumOk && !version.equals(BaseUtils.getVersionName(getContext()))) {
                    textVersion.setText("最新版本：" + updateBean.getData().getVersion());
                    textSize.setText("新版本大小：" + updateBean.getData().getSize());
                    String[] contents = updateBean.getData().getContent().split("\\|");
                    String content = "";
                    for (int i = 0; i < contents.length; i++) {
                        content += contents[i] + "\n";
                    }
                    textContent.setText(content);
                    updateClose.setOnClickListener(this);
                    textUpdate.setOnClickListener(this);

                    popupUtils.dismiss();
                    popupUtils.initPopupWindwo(filterIcon, updatePop,
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                            Color.TRANSPARENT, R.style.popwindow_register_animation, 0,
                            0);
                } else {
                    mView.ShowAdPopView();
                }

            } else {
                mView.ShowAdPopView();
            }
        } else {
            mView.ShowAdPopView();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_close:
            case R.id.ad_close:
            case R.id.web_close:
                popupUtils.dismiss();
                break;
            case R.id.upate_update:
                popupUtils.dismiss();
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
//                Uri uri = Uri.parse("market://details?id=com.jyh.kxt");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                break;
            case R.id.ad_bg:
                popupUtils.dismiss();
                Intent in = new Intent(getContext(), DetailsActivity.class);
                in.putExtra("url", adUrl);
                in.putExtra("from", "main");
                getContext().startActivity(in);
                break;
        }
    }


    public class MyWebViewClient extends WebViewClient {
        private boolean isFirst = true;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isFirst) {
                view.loadUrl(url);
                isFirst = false;
            } else {
                popupUtils.dismiss();
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("from", "main");
                getContext().startActivity(intent);
            }

            return true;
        }
    }
}
