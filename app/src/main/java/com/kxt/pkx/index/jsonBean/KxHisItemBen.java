package com.kxt.pkx.index.jsonBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class KxHisItemBen implements Serializable {

    /**
     * autoid : 134112
     * time : 1477271025
     * code : KUAIXUN
     * id : 4193
     * content :
     * status : 1
     */

    private String code;
    private String id;
    private String content;
    private String status;
    private String socre;

    public String getSocre() {
        return socre;
    }

    public void setSocre(String socre) {
        this.socre = socre;
    }

    @JSONField(name = "do")
    private String wodo;//自定义的

    public String getWodo() {
        return wodo;
    }

    public void setWodo(String wodo) {
        this.wodo = wodo;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "KxHisItemBen{" +
                "code='" + code + '\'' +
                ", id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", socre='" + socre + '\'' +
                ", wodo='" + wodo + '\'' +
                '}';
    }
}
