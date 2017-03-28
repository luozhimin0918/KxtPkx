package com.kxt.pkx.index.jsonBean;

import com.kxt.pkx.PkxApplicaion;
import com.kxt.pkx.common.constant.UrlConstant;
import com.kxt.pkx.common.utils.BaseUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class ConfigJson implements Serializable {
    private String version;
    private String system;

    public ConfigJson() {
        this.setVersion();
        this.setSystem();
    }

    public String getSystem() {
        return this.system;
    }

    public void setSystem() {
        this.system = UrlConstant.SYSTEM_VALUE;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion() {
        String verison = BaseUtils.getVersionName(PkxApplicaion.getInstance());
        this.version = verison;
    }

    @Override
    public String toString() {
        return "DataJson{" +
                "version='" + version + '\'' +
                ", system='" + system + '\'' +
                '}';
    }
}
