package com.kxt.pkx.common.coustom;

/**
 * Created by Luozhimin on 2017/1/14.9:51
 */

import android.content.Context;
import android.os.Build.VERSION;

public abstract class ScrollerProxy {
    public ScrollerProxy() {
    }

    public static ScrollerProxy getScroller(Context context) {
        return (ScrollerProxy)(VERSION.SDK_INT < 9?new PreGingerScroller(context):(VERSION.SDK_INT < 14?new GingerScroller(context):new IcsScroller(context)));
    }

    public abstract boolean computeScrollOffset();

    public abstract void fling(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);

    public abstract void forceFinished(boolean var1);

    public abstract boolean isFinished();

    public abstract int getCurrX();

    public abstract int getCurrY();
}

