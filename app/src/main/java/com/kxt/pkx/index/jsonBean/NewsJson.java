package com.kxt.pkx.index.jsonBean;

import com.kxt.pkx.PkxApplicaion;
import com.kxt.pkx.common.constant.UrlConstant;
import com.kxt.pkx.common.utils.BaseUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class NewsJson implements Serializable {
    private String version;
    private String system;
    private String num;


    public NewsJson() {
        this.setVersion();
        this.setSystem();
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
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
