package com.kxt.pkx.index.jsonBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class BaseBean implements Serializable {


    /**
     * aud : http: //adi.kuaixun56.com
     * status : 1
     * msg : 获取成功
     * data : 1488959055
     */

    private String aud;
    private String status;
    private String msg;
    private String data;

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "aud='" + aud + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
