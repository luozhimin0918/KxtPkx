package com.kxt.pkx.index.view;

import com.kxt.pkx.common.base.CommunalView;
import com.kxt.pkx.index.jsonBean.AdConfigBean;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public interface IWelcomeView extends CommunalView {

    void toMainActivity();

    void showAd(AdConfigBean adConfigBean);
}
