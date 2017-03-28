package com.kxt.pkx.common.utils;

/**
 * 项目名:firstgold2
 * 类描述:点击工具类
 * 创建人:苟蒙蒙
 * 创建日期:2016/11/18.
 */

public class DoubleClickUtils {
    private static long lastClickTime;

    /**
     * 限制频率
     *
     * @param interval 最短间隔时间
     * @return
     */
    public static boolean isFastDoubleClick(int interval) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < interval) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
