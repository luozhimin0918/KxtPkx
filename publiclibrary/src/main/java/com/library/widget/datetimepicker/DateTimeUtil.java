package com.library.widget.datetimepicker;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DaiYao on 2016/9/27.
 */
public class DateTimeUtil
{
    public static String getWeekdays(int dayOfWeek)
    {
        String[] weekArray = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return weekArray[dayOfWeek - 1];
    }

    public static String getMonths(int month)
    {
        String[] monthArray = new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        return monthArray[month];
    }

    public static String getTimeInMillisToMonths(long millis)
    {
        Date d = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月");
        return sdf.format(d);
    }
}
