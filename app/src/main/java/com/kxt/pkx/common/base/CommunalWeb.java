package com.kxt.pkx.common.base;

import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kxt.pkx.R;
import com.library.util.SystemUtil;
import com.library.util.volley.load.PageLoadLayout;
import com.library.widget.window.ToastView;

/**
 * WebView 的基类
 */
public class CommunalWeb extends CommunalActivity
{
    public static final String WEB_TITLE = "webTitle";
    public static final String WEB_URL = "webUrl";

    protected String communalWebTitle = "";
    protected String communalWebUrl;

    protected PageLoadLayout pageLoadLayout;

    private boolean isLoadSuccess = true;

    /**
     * 调用父类绑定初始化
     *
     * @param webView
     */
    public void superInit(WebView webView, PageLoadLayout pageLoadLayout)
    {
        communalWebTitle = getIntent().getStringExtra(WEB_TITLE);
        communalWebUrl = getIntent().getStringExtra(WEB_URL);

        WebSettings webSettings = webView.getSettings();

        this.pageLoadLayout = pageLoadLayout;
        /*
         * 控制缩放，打开JS, 使用缓存
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);

        webSettings.setUserAgentString(SystemUtil.getWebViewUA(getContext(), webSettings));

        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);//设置是否打开。默认关闭，即，H5的缓存无法使用。
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 默认使用缓存

        webView.loadUrl(communalWebUrl);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
    }

    private void showNotFontPage()
    {
        ToastView.makeText3(this,"服务器连接不稳定,加载失败~");
        View inflate = LayoutInflater.from(this).inflate(R.layout.web_load_error, null);
        pageLoadLayout.addCustomView(inflate);
    }

    class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }

    /**
     * 使用   google chrome
     */
    class MyWebChromeClient extends WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            if (newProgress == 100 && isLoadSuccess)
            {
                pageLoadLayout.loadSuccess();
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title)
        {
            super.onReceivedTitle(view, title);

            CharSequence notFound = "404";
            CharSequence notFound2 = "找不到网页";
            CharSequence notFound3 = "网页无法打开";

            if (title.contains(notFound) ||
                    title.contains(notFound2) ||
                    title.contains(notFound3))
            {
                isLoadSuccess = false;
                showNotFontPage();
            }
        }
    }
}
