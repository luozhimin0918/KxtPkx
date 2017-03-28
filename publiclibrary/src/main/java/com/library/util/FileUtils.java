package com.library.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @author Mr'Dai
 * @date 2016/5/16 16:00
 * @Title: MobileLibrary
 * @Package com.library.util
 * @Description:
 */
public class FileUtils
{
    /**
     * 将图片保存到Android文件夹下的Cache目录下面
     */
    public static File getDiskCacheDir(Context context, String uniqueName)
    {
        String cachePath = getSaveFilePath(context);
        File file = new File(cachePath + File.separator + uniqueName);
        if (!file.exists())
        {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 得到文件存储路径
     */
    public static String getSaveFilePath(Context context)
    {
        boolean isWriteExternal = false; //对于部分手机 sd卡有读写权限但是就存入不了, 例如三星的测试机
//
//        try
//        {
//            //判断文件是否创建成功,如果能成功创建则保存到sd卡 如果不能成功创建则保存到CacheDir中
//            File fileTestDir = new File(context.getExternalCacheDir().getPath());
//            if (!fileTestDir.exists())
//            {
//                fileTestDir.mkdirs();
//            }
//
//            String fileTestName = "first_test.txt";
//            File file = new File(fileTestDir, fileTestName);
//            if (!file.exists())
//            {
//                boolean newFile = file.createNewFile();
//                if (newFile)
//                {
//                    isWriteExternal = true;
//                } else
//                {
//                    isWriteExternal = false;
//                }
//            } else
//            {
//                isWriteExternal = true;
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }

        String cachePath;
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !Environment.isExternalStorageRemovable())  && isWriteExternal )
        {
            cachePath = context.getExternalCacheDir().getPath();
        } else
        {
//            cachePath = context.getCacheDir().getPath();
            cachePath = context.getApplicationContext().getFilesDir().getPath();
        }
        return cachePath;
    }
    public static String getSDSaveFilePath(Context context)
    {
        String sdCachePath = "";
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !Environment.isExternalStorageRemovable()))
        {
            sdCachePath = context.getExternalCacheDir().getPath();
        }
        return sdCachePath;
    }
}
