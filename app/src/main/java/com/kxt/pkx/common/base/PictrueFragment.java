package com.kxt.pkx.common.base;

/**
 * Created by Administrator on 2016/2/22.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.kxt.pkx.R;
import com.kxt.pkx.common.coustom.ScaleView;
import com.kxt.pkx.common.coustom.ZdpImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * 显示大图的实现，并且可以放大缩小
 *
 * @author http://yecaoly.taobao.com
 */
@SuppressLint("ValidFragment")
public class PictrueFragment extends Fragment {

    private String urlString;

    Bitmap bitmapDrawable;
    Activity mContext;

    @SuppressLint("ValidFragment")
    public PictrueFragment(String urlString, Activity mContext) {
        this.mContext = mContext;
        this.urlString = urlString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.scale_pic_item, null);
        initView(view);
        return view;
    }

    ScaleView imageView;
    ZdpImageView zdpImageView;

    FrameLayout linearBar;

    private void initView(View view) {
        imageView = (ScaleView) view.findViewById(R.id.scale_pic_item);
        zdpImageView = (ZdpImageView) view.findViewById(R.id.zdpImage);
        linearBar = (FrameLayout) view.findViewById(R.id.linearBar);
        zdpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.finish();
            }
        });
        Runnable downloadRun = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if (urlString != null && !urlString.equals("")) {


                        if (urlString.endsWith(".gif")) {


                            handler.sendEmptyMessage(SHOW_VIEW_GIF);

                        } else if (!urlString.endsWith(".gif")) {
                            bitmapDrawable = null;
                            bitmapDrawable = ((BitmapDrawable) loadImageFromUrl(urlString)).getBitmap();
                            if (bitmapDrawable != null) {
                                handler.sendEmptyMessage(SHOW_VIEW);
                            }


                        }


                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };


        new Thread(downloadRun).start();


    }


    public Drawable loadImageFromUrl(String url) throws IOException {

        URL m = new URL(url);
        InputStream i = (InputStream) m.getContent();
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }


    private final int SHOW_VIEW = 0;
    private final int SHOW_VIEW_GIF = 1;
    Handler handler = new Handler(new Handler.Callback() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_VIEW:
                    zdpImageView.setImageBitmap(bitmapDrawable);
                    break;
                case SHOW_VIEW_GIF:
                    Glide.with(mContext).load(urlString).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .into(zdpImageView);


                    break;
                default:
                    break;
            }
            return false;
        }
    });


    public Bitmap getBitmapDrawable() {
        return bitmapDrawable;
    }

    public void setBitmapDrawable(Bitmap bitmapDrawable) {
        this.bitmapDrawable = bitmapDrawable;
    }
}

