package com.kxt.pkx;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.kxt.pkx.common.utils.CrashHandler;
import com.kxt.pkx.index.jsonBean.AdConfigBean;
import com.kxt.pkx.index.jsonBean.UpdateBean;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class PkxApplicaion extends Application {
    private static PkxApplicaion pkxApplicaion;
    private UpdateBean updateBean;
    private AdConfigBean adConfigBean;

    public AdConfigBean getAdConfigBean() {
        return adConfigBean;
    }

    public void setAdConfigBean(AdConfigBean adConfigBean) {
        this.adConfigBean = adConfigBean;
    }

    public UpdateBean getUpdateBean() {
        return updateBean;
    }

    public void setUpdateBean(UpdateBean updateBean) {
        this.updateBean = updateBean;
    }


    public static PkxApplicaion getInstance() {
        return pkxApplicaion;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.pkxApplicaion = this;
        Glide.get(getApplicationContext()).setMemoryCategory(MemoryCategory.LOW);
        CrashHandler crashHandler = new CrashHandler();
        crashHandler.init(getApplicationContext());
    }


}
