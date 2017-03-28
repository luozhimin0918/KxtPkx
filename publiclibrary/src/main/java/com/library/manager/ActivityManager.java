package com.library.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * 管理所有Activity
 */
public class ActivityManager
{
    private static ActivityManager instance;
    private Stack<Activity> activityStack;//activity栈

    private ActivityManager()
    {

    }

    /**
     * 单例模式
     */
    public static ActivityManager getInstance()
    {
        if (instance == null)
        {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 把一个activity压入栈中
     */
    public void pushOneActivity(Activity actvity)
    {
        if (activityStack == null)
        {
            activityStack = new Stack<>();
        }
        activityStack.add(actvity);
    }

    public int getActivityCount()
    {
        if (activityStack == null)
        {
            activityStack = new Stack<>();
        }
        return activityStack.size();
    }

    /**
     * 获取栈顶的activity，先进后出原则
     */
    public Activity getLastActivity()
    {
        return activityStack.lastElement();
    }

    /**
     * 移除一个activity
     */
    public void popOneActivity(Activity activity)
    {
        if (activityStack != null && activityStack.size() > 0)
        {
            if (activity != null)
            {
                activity.finish();
                activityStack.remove(activity);
                activity = null;
            }
        }
    }

    //退出所有activity
    public void finishAllActivity()
    {
        if (activityStack != null)
        {
            while (activityStack.size() > 0)
            {
                Activity activity = getLastActivity();
                if (activity == null)
                    break;
                popOneActivity(activity);
            }
        }
    }

    /**
     * 是否存在某一个Activity
     *
     * @param activity
     * @return
     */
    public boolean isExistActivity(Class<?> activity)
    {
        for (int i = 0; i < activityStack.size(); i++)
        {
            if (activityStack.get(i).getClass().getSimpleName().equals(activity.getSimpleName()))
            {
                return true;
            }
        }
        return false;
    }
}
