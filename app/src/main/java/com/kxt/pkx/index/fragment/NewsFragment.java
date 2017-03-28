package com.kxt.pkx.index.fragment;

import android.os.Bundle;
import android.widget.ListView;

import com.kxt.pkx.R;
import com.kxt.pkx.common.base.CommunalFragment;
import com.kxt.pkx.index.persenter.NewsPersenter;
import com.kxt.pkx.index.view.INewsView;
import com.library.util.volley.load.PageLoadLayout;
import com.library.widget.handmark.PullToRefreshBase;
import com.library.widget.handmark.PullToRefreshListView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class NewsFragment extends CommunalFragment implements INewsView, PageLoadLayout.OnAfreshLoadListener {
    private static NewsFragment newsFragment = new NewsFragment();
    @BindView(R.id.data_listview)
    PullToRefreshListView dataListview;
    @BindView(R.id.page_load)
    PageLoadLayout pageLoad;

    public static NewsFragment getInstance() {
        return newsFragment;

    }

    private NewsPersenter newsPersenter;

    @Override
    protected void onInitialize(Bundle savedInstanceState) {
        super.onInitialize(savedInstanceState);
        setBindingView(R.layout.fragment_news);
        newsPersenter = new NewsPersenter();
        newsPersenter.attach(this);

        newsPersenter.getAdDate(pageLoad, dataListview, getActivity().getLayoutInflater());
        newsPersenter.getNewsDate(pageLoad, dataListview);
        init();
    }

    public void init() {
        dataListview.setMode(PullToRefreshBase.Mode.BOTH);
        dataListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                dataListview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newsPersenter.getAdDate(pageLoad, dataListview, getActivity().getLayoutInflater());
                        newsPersenter.getNewsDate(pageLoad, dataListview);
                    }
                }, 200);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                newsPersenter.getMoreNewsData(dataListview);
            }
        });
    }

    @Override
    public void OnAfreshLoad() {
//        pageLoad.startLoading();
        newsPersenter.getAdDate(pageLoad, dataListview, getActivity().getLayoutInflater());
        newsPersenter.getNewsDate(pageLoad, dataListview);
    }
}
