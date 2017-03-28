package com.library.base;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Mr'Dai on 2016/10/13.
 */

public class BaseJson2
{
    @JSONField(name = "code")
    private String code;

    @JSONField(name = "data")
    private String data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
