package com.kxt.pkx.index.jsonBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class WebSocketKeyBean implements Serializable {

    public String time;
    public String domain;
    public String remote_addr;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    @Override
    public String toString() {
        return "WebSocketKeyBean{" +
                "time='" + time + '\'' +
                ", domain='" + domain + '\'' +
                ", remote_addr='" + remote_addr + '\'' +
                '}';
    }
}
