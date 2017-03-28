package com.kxt.pkx.common.coustom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kxt.pkx.R;
import com.kxt.pkx.index.activity.ShowAnoWebImageActivity;
import com.kxt.pkx.index.activity.ShowWebImageActivity;
import com.library.util.SPUtils;
import com.library.util.SystemUtil;

import org.w3c.dom.Text;

import java.util.LinkedList;


/**
 * Created by Mr'Dai on 2016/11/22.
 */
public class LoadWebView extends FrameLayout {
    private boolean isLoadSuccess = true;

    private WebSettings webSettings;
    private WebView webView;


    private String webTitle = "";
    private String webUrl = "";

    private ProgressBar progressbar;
    private View loadFailureView;
    private boolean isService;//是否为客服跳转网页
    private TextView textview;
    /*
    * 历史URL
     */
    private LinkedList<String> loadHistoryUrls = new LinkedList<>();

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(TextView text) {
        this.textview = text;
        text.setText(webTitle);
    }

    public LoadWebView(Context context) {
        super(context);
        this.initWebView();
    }

    public LoadWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initWebView();
    }

    public LoadWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initWebView();
    }

    private void initWebView() {
        progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, SystemUtil.dp2px(getContext(), 2), 0));
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.shape_progress_bar_bg));

        webView = new WebView(getContext());
        webView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        webSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //如果手机是5.0以上 URL地址是Https 但是图片是Http 则会访问不了图片 加上这句就好
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);

        webSettings.setUserAgentString(SystemUtil.getWebViewUA(getContext(), webSettings));
        webView.addJavascriptInterface(new JavascriptInterface(getContext()), "imagelistner");

        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);//设置是否打开。默认关闭，即，H5的缓存无法使用。

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());

        this.addView(webView);
        this.addView(progressbar);
    }

    /**
     * 回收之后进入会出现一些问题,暂时去掉回收WebView
     */
    public void onDestroy() {
        webView.onPause(); //pauses background threads, stops playing sound
       /* try {
            webView.stopLoading();
            webView.onPause(); //pauses background threads, stops playing sound
            webView.pauseTimers(); //pauses the WebViewCore
            webView.destroyDrawingCache(); //removes the view from RAM
            webView = null; //you need to nullify the WebView because due to the odd way that the Activity lifecyle
            // works, sometimes onPause is called when you start up your activity
        } finally {

        }*/
    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            addToHistory(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (isLoadSuccess) {
                progressbar.setVisibility(GONE);
                if (loadFailureView != null) {
                    LoadWebView.this.removeView(loadFailureView);
                }
            }
            if (url.contains("mqqwpa")) {
                view.setVisibility(View.GONE);
                view.loadUrl(webUrl);
            } else {
                view.setVisibility(View.VISIBLE);
            }
            addImageClickListner();
        }

        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            super.onReceivedError(webView, errorCode, description, failingUrl);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
            return super.shouldInterceptRequest(webView, url);
        }
    }

    /**
     * 使用   google chrome
     */
    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100 && isLoadSuccess) {
            } else {
                if (progressbar.getVisibility() == View.GONE) {
                    progressbar.setVisibility(View.VISIBLE);
                }
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            webTitle = title;
            if (null != textview) {
                textview.setText(title);
            }
            CharSequence notFound = "404";
            CharSequence notFound2 = "找不到网页";
            CharSequence notFound3 = "网页无法打开";

            if (title.contains(notFound) ||
                    title.contains(notFound2) ||
                    title.contains(notFound3)) {
                isLoadSuccess = false;
                if (!isService) {
                    webView.loadUrl("about:blank");
                    showNotFontPage();
                }
            }


        }
    }

    private void showNotFontPage() {
        progressbar.setVisibility(GONE);
        loadFailureView = LayoutInflater.from(getContext()).inflate(R.layout.web_load_error, null);
        loadFailureView.findViewById(R.id.error_reload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressbar.setVisibility(VISIBLE);
                    removeView(loadFailureView);
                    webView.loadUrl(webUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.addView(loadFailureView);
    }

    public void loadUrl(String url) {
        this.webUrl = url;

        webView.loadUrl(url);
        addToHistory(url);


    }


    public WebView getWebView() {
        return webView;
    }


    public LinkedList<String> getLoadHistoryUrls() {
        return loadHistoryUrls;
    }

    public void addToHistory(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (loadHistoryUrls.contains(url)) {
            return;
        }
        if (url.startsWith("http://appapi.dyhjw.com") || url.startsWith("https://appapi.dyhjw.com")) {
            loadHistoryUrls.add(url);
        }
    }


    public void goBackOrForward(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.startsWith("http://appapi.dyhjw.com") || url.startsWith("https://appapi.dyhjw.com")) {
            webView.goBackOrForward(-1);
        } else {
            webView.goBackOrForward(-2);
        }
    }


    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String[] img) {

            Intent intent = new Intent();
            intent.putExtra("images", img);
            intent.setClass(context, ShowAnoWebImageActivity.class);
            context.startActivity(intent);


        }
      /*  @android.webkit.JavascriptInterface
        public void openImage(String img) {
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, ShowWebImageActivity.class);
            context.startActivity(intent);
            // System.out.println(img);
        }*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
      /*  webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementById('article').getElementsByTagName('img');"
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "    objs[i].onclick=function()  " + "    {  "
                + "        window.imagelistner.openImage(this.src);  "
                + "    }  " + "}" + "})()");*/
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
        webView.loadUrl("javascript:(function(){" +
                " var srcs = [];" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "var aList= document.getElementsByTagName(\"a\"); " +
                "for(var i=0;i<objs.length;i++){" +
                "srcs[i] = objs[i].src; }" +


                "for(var a=0;a<aList.length;a++){" +
                " aList[a].onclick=function(){" +
                " window.imagelistner.openWebview(this.href);}}" +


                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "  srcs.push(this.src);      window.imagelistner.openImage(srcs);  srcs.pop(); " +
                "    }  " +
                "}" +
                "})()");
    }

}
