package com.kxt.pkx.common.coustom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class RoundImageView extends ImageView {
    private boolean outsideRound = false;// 是否开启外圆

    public RoundImageView(Context context) {
        super(context);
        initRound(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRound(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initRound(context, attrs);
    }

    private void initRound(Context context, AttributeSet attrs) {
    /*    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImg);
        outsideRound = a.getBoolean(R.styleable.RoundImg_outsideRound, false);
        a.recycle();*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = null;
        if (drawable instanceof BitmapDrawable) {
            b = ((BitmapDrawable) drawable).getBitmap();
        }
        if (null == b) {
            return;
        }
        /**
         * 此处应该处理是否有内存溢出的情况
         */
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);
        if (bitmap == null) {
            return;
        }
        int w = getWidth(), h = getHeight();
        Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);

        if (outsideRound) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(3); //设定默认宽度3px
            canvas.drawCircle(w / 2, w / 2, w / 2 - 3, paint);
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        } else {
            sbmp = bmp;
        }
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();

        //
        // paint.setColor(CircleColor.WHITE);
        // paint.setStyle(Paint.Style.FILL_AND_STROKE);
        // paint.setStyle(Paint.Style.STROKE); // 设置空心
        // paint.setStrokeWidth(2); // 设置圆环的宽度
        // paint.setAntiAlias(true); // 消除锯齿
        // canvas.drawCircle(sbmp.getWidth() / 2 + 0.9f,
        // sbmp.getHeight() / 2 + 0.9f, sbmp.getWidth() / 2 + 0.2f, paint); //
        // 画出圆环

        final int color = 0xffa19774;

        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        paint.setColor(Color.WHITE);
        paint.setStyle(Style.FILL_AND_STROKE);
        canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor(CircleColor.parseColor("#FFFFFF"));

        if (outsideRound)
            canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2 - 8, paint);
        else
            canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

}
