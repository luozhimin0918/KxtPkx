package com.library.util.disklrucache;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.library.util.ConvertUtils;
import com.library.util.FileUtils;
import com.library.util.SystemUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Mr'Dai
 * @date 2016/5/19 16:09
 * @Title: MobileLibrary
 * @Package com.library.util
 * @Description:
 */
public class DiskLruCacheUtils
{
    private static final String DEFAULT_CACHE_DIR = "lruCache";
    private static DiskLruCacheUtils mDiskLruCacheUtils;

    public static DiskLruCacheUtils getInstance(Context mContext)
    {
        if (mDiskLruCacheUtils == null)
        {
            mDiskLruCacheUtils = new DiskLruCacheUtils(mContext);
        }
        return mDiskLruCacheUtils;
    }

    private Context mContext;
    private File mDiskCacheFile;
    private DiskLruCache mDiskLruCache;
    private LruCache<String, Bitmap> mLruCacheMap;

    public DiskLruCacheUtils(Context mContext)
    {
        this.mContext = mContext;
        //如果是6.0以上手机检查是否有权限

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int hasWritePermission = mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED)
            {
                if (mContext instanceof Activity)
                {
                    ActivityCompat.requestPermissions((Activity) mContext,
                            new String[]
                                    {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE},
                            101);
                }
            } else
            {
                initDiskLruCache();
            }
        } else
        {
            initDiskLruCache();
        }
    }

    private void initDiskLruCache()
    {
        try
        {
            int versionCode = SystemUtil.getVersionCode(mContext);
            mLruCacheMap = new LruCache<>(5 * 1024 * 1024);
            mDiskCacheFile = FileUtils.getDiskCacheDir(mContext, DEFAULT_CACHE_DIR);
            mDiskLruCache = DiskLruCache.open(mDiskCacheFile, versionCode, 1, 10 * 1024 * 1024);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void putDiskLruCache(String key, Bitmap bitmap)
    {
        String md5Key = ConvertUtils.md5(key);
        DiskLruCache.Editor mEdit = null;
        try
        {
            mEdit = mDiskLruCache.edit(md5Key);
            OutputStream outputStream = mEdit.newOutputStream(0);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            outputStream.close();
            outputStream.flush();
            mEdit.commit();

            mDiskLruCache.flush();
        } catch (Exception e)
        {
            e.printStackTrace();

            try
            {
                mEdit.abort();
            } catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
    }

    public Bitmap getDiskLruCache(String key)
    {
        if (mLruCacheMap.get(key) != null)
        {
            return mLruCacheMap.get(key);
        }
        String md5Key = ConvertUtils.md5(key);
        try
        {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(md5Key);
            if (snapshot != null)
            {
                InputStream inputStream = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                mLruCacheMap.put(key, bitmap);
                return bitmap;
            } else
            {
                return null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteLruCache(String key)
    {
        if (mLruCacheMap.get(key) != null)
        {
            mLruCacheMap.remove(key);
        }
    }
}
