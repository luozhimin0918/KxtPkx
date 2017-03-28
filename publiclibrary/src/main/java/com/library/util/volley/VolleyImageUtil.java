package com.library.util.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.library.R;

/**
 * @author Mr'Dai
 * @date 2016/5/17 15:40
 * @Title: MobileLibrary
 * @Package com.dxmobile.library.util
 * @Description:
 */
public class VolleyImageUtil
{

    private static VolleyImageUtil mVolleyUtil;

    public static VolleyImageUtil getInstance(Context mContext, RequestQueue queue)
    {
        if (mVolleyUtil == null)
        {
            mVolleyUtil = new VolleyImageUtil(mContext, queue);
        }
        return mVolleyUtil;
    }

    private Context mContext;
    private RequestQueue mQueue;
    private LruCache<String, Bitmap> mImageCache;
    private ImageLoader mImageLoader;

    private static int defaultImageResId = R.mipmap.ico_def_load;
    private static int errorImageResId = R.mipmap.ico_def_load;

    public VolleyImageUtil(Context mContext, RequestQueue mQueue)
    {

        this.mContext = mContext;
        this.mQueue = mQueue;

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        mImageCache = new LruCache<>(maxMemory); //初始化加载大量图片时,避免内存溢出

        mImageLoader = new ImageLoader(mQueue, imageCache); //图片加载器
    }

    public static void initConfig(int defaultImageResId, int errorImageResId)
    {
        VolleyImageUtil.defaultImageResId = defaultImageResId;
        VolleyImageUtil.errorImageResId = errorImageResId;
    }

    /**
     * 下载网络图片
     */
    public void downNetWorkImage(String mImageUrl, VolleyImageListener mVolleyLoadListener)
    {
        downNetWorkImage(mImageUrl, null, mVolleyLoadListener);
    }

    /**
     * 下载网络图片并且显示出来
     */
    public void downNetWorkImage(String mImageUrl, final ImageView imageView, final VolleyImageListener
            mVolleyLoadListener)
    {
        ImageRequest mImageRequest = new ImageRequest(mImageUrl, new Response.Listener<Bitmap>()
        {
            @Override
            public void onResponse(Bitmap response)
            {
                mVolleyLoadListener.onSuccess(response);
                if (imageView != null)
                {
                    imageView.setImageBitmap(response);
                    setAnimation(imageView);
                }
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                mVolleyLoadListener.onError(error);
                if (errorImageResId != 0 && imageView != null)
                {
                    imageView.setImageResource(errorImageResId);
                }
            }
        }

        );
        mQueue.add(mImageRequest);
    }

    /**
     * 加载单个网络图片
     *
     * @param url
     * @param mImageView
     */

    public void loadNetWorkImageView(String url, NetworkImageView mImageView)
    {
        ImageLoader imageLoader = new ImageLoader(mQueue, imageCache);
        mImageView.setImageUrl(url, imageLoader);

        mImageView.setDefaultImageResId(defaultImageResId);
        mImageView.setErrorImageResId(errorImageResId);

        setAnimation(mImageView);
    }

    /**
     * 加载列表中的ImageView
     *
     * @param url
     * @param imageview
     */
    public void loadListImageView(String url, final ImageView imageview)
    {
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if (errorImageResId != 0)
                {
                    imageview.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
            {
                if (response.getBitmap() != null)
                {
                    imageview.setImageBitmap(response.getBitmap());
                    setAnimation(imageview);
                } else if (defaultImageResId != 0)
                {
                    imageview.setImageResource(defaultImageResId);
                }
            }
        };

        mImageLoader.get(url, listener);
    }

    /**
     * 配置图片缓存
     */
    ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache()
    {
        @Override
        public void putBitmap(String key, Bitmap value)
        {
            mImageCache.put(key, value);
        }

        @Override
        public Bitmap getBitmap(String key)
        {
            return mImageCache.get(key);
        }
    };

    /**
     * 加载动画
     *
     * @param mView
     */
    private void setAnimation(View mView)
    {
//        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_anim);
//        mView.setAnimation(animation);
    }
}
