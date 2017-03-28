package com.kxt.pkx.index.persenter;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.kxt.pkx.R;
import com.kxt.pkx.common.base.CommunalPresenter;
import com.kxt.pkx.common.constant.SpConstant;
import com.kxt.pkx.common.constant.UrlConstant;
import com.kxt.pkx.common.coustom.Advertisements;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.ObserverData;
import com.kxt.pkx.index.adapter.NewsAdapter;
import com.kxt.pkx.index.jsonBean.ConfigJson;
import com.kxt.pkx.index.jsonBean.NewsAdBean;
import com.kxt.pkx.index.jsonBean.NewsDataBean;
import com.kxt.pkx.index.jsonBean.NewsJson;
import com.kxt.pkx.index.jsonBean.NewsMoreJson;
import com.kxt.pkx.index.model.INewsModelImp;
import com.kxt.pkx.index.view.INewsView;
import com.library.util.volley.load.PageLoadLayout;
import com.library.widget.handmark.PullToRefreshListView;
import com.library.widget.window.ToastView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class NewsPersenter extends CommunalPresenter<INewsView> {
    private INewsModelImp newsModelImp;
    private View advertisements;
    private NewsAdapter newsAdapter;
    private boolean isHsHead = false;
    private List<NewsDataBean.DataBean> newsDataBeans = new ArrayList<>();

    public NewsPersenter() {
        newsModelImp = new INewsModelImp(this);
    }

    public void getAdDate(final PageLoadLayout pageLoad, final PullToRefreshListView dataListview, final LayoutInflater layoutInflater) {
        if (BaseUtils.isNetworkConnected(getContext())) {
            Map<String, String> map = new HashMap<>();
            Gson gson = new Gson();
            try {
                map.put("content", BaseUtils.createJWT(UrlConstant.URL_PRIVATE_KEY, gson.toJson(new ConfigJson())));
                newsModelImp.getAdData(new ObserverData<NewsAdBean>() {
                    @Override
                    public void onCallback(NewsAdBean data) {
                        super.onCallback(data);
                        if (null != data && data.getStatus().equals("1")) {
                            SpConstant.getNewsAdPreferences().edit().putString(SpConstant.NEWS_AD_KEY, BaseUtils.Serialize(data)).commit();
                            if (advertisements == null) {
                                advertisements = new Advertisements((FragmentActivity) getContext(), true,
                                        layoutInflater, 3000, data.getData()).initView();
                            }
                            if (!isHsHead) {
                                dataListview.getRefreshableView().addHeaderView(advertisements);
                                isHsHead = true;
                            }

                        } else {
                            getCacheNewsAD(dataListview, layoutInflater);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        getCacheNewsAD(dataListview, layoutInflater);
                    }
                }, map, UrlConstant.NEWS_AD_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getCacheNewsAD(dataListview, layoutInflater);
        }

    }

    public void getCacheNewsAD(PullToRefreshListView dataListview, LayoutInflater layoutInflater) {
        String newsAd = SpConstant.getNewsAdPreferences().getString(SpConstant.NEWS_AD_KEY, "");
        if (null != newsAd && !"".equals(newsAd)) {
            NewsAdBean cacheData = (NewsAdBean) BaseUtils.DeSerialization(newsAd);
            if (advertisements == null) {
                advertisements = new Advertisements((FragmentActivity) getContext(), true,
                        layoutInflater, 3000, cacheData.getData()).initView();
            }
            if (!isHsHead) {
                dataListview.getRefreshableView().addHeaderView(advertisements);
                isHsHead = true;
            }
        } else {

        }
    }

    public void getNewsDate(final PageLoadLayout pageLoad, final PullToRefreshListView dataListview) {
        if (BaseUtils.isNetworkConnected(getContext())) {
            Map<String, String> map = new HashMap<>();
            Gson gson = new Gson();
            try {
                NewsJson newsJson = new NewsJson();
                newsJson.setNum("15");
                map.put("content", BaseUtils.createJWT(UrlConstant.URL_PRIVATE_KEY, gson.toJson(newsJson)));
                newsModelImp.getNewsData(new ObserverData<NewsDataBean>() {
                    @Override
                    public void onCallback(NewsDataBean data) {
                        super.onCallback(data);
                        if (null != data && data.getStatus().equals("1")) {
                            Log.i("zzz","data=="+data);
                            pageLoad.loadSuccess();
                            newsDataBeans.clear();
                            newsDataBeans.addAll(data.getData());
                            SpConstant.getNewsDataPreferences().edit().putString(SpConstant.NEWS_DATA_KEY, BaseUtils.Serialize(data)).commit();
                            if (null == newsAdapter) {
                                newsAdapter = new NewsAdapter(getContext(), newsDataBeans);
                            }
                            dataListview.setAdapter(newsAdapter);
                            newsAdapter.notifyDataSetChanged();
                            dataListview.onRefreshComplete();

                        } else {
                            getCacheData(pageLoad, dataListview);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        pageLoad.loadError(getContext().getResources().getString(R.string.load_err));
                    }
                }, map, UrlConstant.NEWS_DATA_URL);
            } catch (Exception e) {
                e.printStackTrace();
                pageLoad.loadError(getContext().getResources().getString(R.string.load_err));
            }
        } else {
            ToastView.makeText3(getContext(), getContext().getResources().getString(R.string.load_nonet));
            getCacheData(pageLoad, dataListview);
        }

    }

    public void getCacheData(PageLoadLayout pageLoad, PullToRefreshListView dataListview) {
        String newsData = SpConstant.getNewsDataPreferences().getString(SpConstant.NEWS_DATA_KEY, "");
        if (null != newsData && !"".equals(newsData)) {
            NewsDataBean newsDataBean = (NewsDataBean) BaseUtils.DeSerialization(newsData);
            pageLoad.loadSuccess();
            newsDataBeans.clear();
            newsDataBeans.addAll(newsDataBean.getData());
            if (null == newsAdapter) {
                newsAdapter = new NewsAdapter(getContext(), newsDataBeans);
            }
            dataListview.setAdapter(newsAdapter);
            newsAdapter.notifyDataSetChanged();
            dataListview.onRefreshComplete();

        } else {
            pageLoad.loadError(getContext().getResources().getString(R.string.load_nonet));
        }
    }

    public void getMoreNewsData( final PullToRefreshListView dataListview) {
        if (BaseUtils.isNetworkConnected(getContext())) {
            try {
                final Map<String, String> map = new HashMap<>();
                Gson gson = new Gson();
                NewsMoreJson newsMoreJson = new NewsMoreJson();
                newsMoreJson.setNum("15");
                newsMoreJson.setMarkid(newsDataBeans.get(newsDataBeans.size() - 1).getId());
                map.put("content", BaseUtils.createJWT(UrlConstant.URL_PRIVATE_KEY, gson.toJson(newsMoreJson)));
                newsModelImp.getNewsData(new ObserverData<NewsDataBean>() {
                    @Override
                    public void onCallback(NewsDataBean data) {
                        super.onCallback(data);
                        if (null != data && data.getStatus().equals("1")) {
                            List<NewsDataBean.DataBean> databeas = data.getData();
                            newsDataBeans.addAll(databeas);
                            newsAdapter.notifyDataSetChanged();
                            dataListview.onRefreshComplete();
                        } else {
                            dataListview.onRefreshComplete();
                            ToastView.makeText3(getContext(), getContext().getResources().getString(R.string.load_err));
                        }
                    }
                }, map, UrlConstant.NEWS_DATA_URL);
            } catch (Exception e) {
                e.printStackTrace();
                dataListview.onRefreshComplete();
                ToastView.makeText3(getContext(), getContext().getResources().getString(R.string.load_err));
            }
        } else {
            dataListview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dataListview.onRefreshComplete();
                }
            }, 200);

            ToastView.makeText3(getContext(), getContext().getResources().getString(R.string.load_nonet));
        }


    }
}
