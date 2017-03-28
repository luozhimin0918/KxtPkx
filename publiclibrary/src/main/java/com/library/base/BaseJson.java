package com.library.base;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Mr'Dai on 2016/10/13.
 */

public class BaseJson
{

    @JSONField(name = "status")
    private int status;

    @JSONField(name = "data")
    private String data;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
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
