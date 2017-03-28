package com.library.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by DaiYao on 2016/9/11.
 */
public class HandlerUtil extends Handler
{
    private static HandlerUtil mHandlerUtil;

    public interface IHandlerMsg
    {
        void handlerMsg(Message msg);
    }

    private IHandlerMsg handlerMsg;



    public HandlerUtil(IHandlerMsg handlerMsg)
    {
        this.handlerMsg = handlerMsg;
    }

    @Override
    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);
        if (handlerMsg != null)
        {
            handlerMsg.handlerMsg(msg);
        }
    }
}
