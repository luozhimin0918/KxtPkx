package com.library.util.volley.load;

import com.library.util.volley.VolleyHttpListener;

import java.util.Map;

/**
 * Created by DaiYao on 2016/5/22 0022.
 */
public class VolleyLoadBean
{
    public int method;
    public String url;
    public Map<String, String> params;
    public VolleyHttpListener volleyHttpListener;
}
