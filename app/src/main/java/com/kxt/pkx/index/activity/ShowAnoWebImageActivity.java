package com.kxt.pkx.index.activity;

/**
 * Created by Administrator on 2016/1/27.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.kxt.pkx.R;
import com.kxt.pkx.common.base.PictrueFragment;
import com.kxt.pkx.common.coustom.HackyViewPager;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.DeviceUtil;
import com.library.widget.window.ToastView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

public class ShowAnoWebImageActivity extends FragmentActivity implements OnClickListener {

    private HackyViewPager viewPager;
    private String[] resId = new String[]{};
    /**
     * 得到上一个界面点击图片的位置
     */
    public static int position = 0;
    public String[] urlStrArr = new String[]{};
    List<String> imgList = new ArrayList<String>();

    @BindView(R.id.textTag)
    TextView textTag;

    @BindView(R.id.saveBut)
    TextView saveBut;

    Activity mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 透明状态栏
        getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.show_big_pictrue_a);
        ButterKnife.bind(this);
        mContext = this;
        if (getIntent() != null) {
            Intent intent = getIntent();
            urlStrArr = intent.getStringArrayExtra("images");

            if (urlStrArr != null && urlStrArr.length > 0) {
                imgList.clear();
                Collections.addAll(imgList, urlStrArr);


                String lastImg = imgList.get(imgList.size() - 1);
                imgList.remove(imgList.size() - 1);


                for (int j = 0; j < imgList.size(); j++) {
                    if (imgList.get(j).equals(lastImg)) {
                        position = j;
                    }
                }

                initViewPager();
            }

        }


    }


    private void initViewPager() {

        if (!BaseUtils.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
        }
        viewPager = (HackyViewPager) findViewById(R.id.viewPager_show_bigPic);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int positions) {
                textTag.setText(positions + 1 + "/" + imgList.size());
                position = positions;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //跳转到第几个界面
        viewPager.setCurrentItem(position);
        textTag.setText(position + 1 + "/" + imgList.size());

        saveBut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBut:
                if (position < imgList.size()) {
                    String urlDown = imgList.get(position);
                    if (!TextUtils.isEmpty(urlDown) && urlDown.startsWith("http")) {
                        new SaveImage().execute(urlDown, null, null); // Android 4.0以后要使用线程来访问网络
                    }
                }

                break;
        }

    }

    /***
     * 功能：用线程保存图片
     *
     * @author wangyp
     */
    private class SaveImage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String sdcard = Environment.getExternalStorageDirectory().toString();

                File file = new File(sdcard + "/KxDownload");
                if (!file.exists()) {
                    file.mkdirs();
                }
                boolean isSd = file.canWrite();

                if (isSd) {

                    String imgurl = params[0];
                    int idx = imgurl.lastIndexOf(".");
                    String ext = imgurl.substring(idx);
                    file = new File(sdcard + "/KxDownload/" + new Date().getTime()
                            + ext);
                    InputStream inputStream = null;
                    URL url = new URL(imgurl);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(20000);
                    if (conn.getResponseCode() == 200) {
                        inputStream = conn.getInputStream();
                    }
                    byte[] buffer = new byte[4096];
                    int len = 0;
                    FileOutputStream outStream = new FileOutputStream(file);
                    while ((len = inputStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    outStream.close();
                    result = "图片已保存至：" + file.getAbsolutePath();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file
                }
            } catch (Exception e) {
                if (!BaseUtils.isNetworkConnected(mContext)) {
                    result = "网络异常";
                } else {
                    result = "保存失败！";
                    e.getLocalizedMessage();
                }

            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (null != result && !"".equals(result)) {
                ToastView.makeText3(ShowAnoWebImageActivity.this, result);
            }

        }

    }


    private void savaPicture() {

        File pictureFileDir = new File(
                Environment.getExternalStorageDirectory(), "/KxApp");
        if (!pictureFileDir.exists()) {
            pictureFileDir.mkdirs();
        }

        if (pictrueFragmentList.size() > 0 && imgList.size() > 0) {


            if (pictrueFragmentList.get(position).getBitmapDrawable() != null && imgList.get(position) != null) {
                File picFile = new File(pictureFileDir, DeviceUtil.fileName(imgList.get(position)));


                //                            picFile.createNewFile();
                Boolean isSava = DeviceUtil.saveBitmap(pictrueFragmentList.get(position).getBitmapDrawable(), picFile);

                if (isSava) {
                    Toast.makeText(getApplicationContext(), "已保存至" + pictureFileDir.getAbsolutePath() + "文件夹下", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

    List<PictrueFragment> pictrueFragmentList = new ArrayList<PictrueFragment>();

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            pictrueFragmentList.clear();
        }


        @Override
        public Fragment getItem(int position) {
            String show_resId = imgList.get(position);
            PictrueFragment p = new PictrueFragment(show_resId, mContext);
            pictrueFragmentList.add(p);

            return p;
        }

        @Override
        public int getCount() {
            return imgList.size();
        }


    }
}
