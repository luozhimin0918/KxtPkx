package com.kxt.pkx.index.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.kxt.pkx.R;
import com.kxt.pkx.common.base.CommunalFragment;
import com.kxt.pkx.index.persenter.PkxPersenter;
import com.kxt.pkx.index.view.IPkxView;
import com.library.util.volley.load.PageLoadLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class PkxFragment extends CommunalFragment implements IPkxView, View.OnClickListener, PageLoadLayout.OnAfreshLoadListener {
    private static PkxFragment pkxFragment = new PkxFragment();

    public static PkxFragment getInstance() {
        return pkxFragment;

    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.page_load)
    PageLoadLayout pageLoad;
    @BindView(R.id.materialRefreshLayout)
    MaterialRefreshLayout materialRefreshLayout;
    @BindView(R.id.separator_rl_date)
    LinearLayout separatorRlDate;
    @BindView(R.id.main_separator_line_tv_date)
    TextView mainSeparatorLineTvDate;

    @BindView(R.id.top_image)
    ImageView topImage;
    private PkxPersenter pkxPersenter;

    @Override
    protected void onInitialize(Bundle savedInstanceState) {
        setBindingView(R.layout.fragment_pkx);
        super.onInitialize(savedInstanceState);
        pageLoad.setOnAfreshLoadListener(this);
        pkxPersenter = new PkxPersenter();
        pkxPersenter.attach(this);
        pkxPersenter.initViews(recyclerView, materialRefreshLayout, pageLoad, separatorRlDate, mainSeparatorLineTvDate);
    }

    @Override
    public void setTopImage(int visible) {
        topImage.setVisibility(visible);
    }

    @OnClick({R.id.top_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_image:
                recyclerView.scrollToPosition(0);
                break;
        }
    }

    public void notifyAdapter() {
        pkxPersenter.notifyAdapter();
    }

    public void chuliData() {
        pkxPersenter.chuliData();
    }


    @Override
    public void OnAfreshLoad() {
        pkxPersenter.onLayoutAfresh(pageLoad);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pkxPersenter.onActDestory();
    }
}
