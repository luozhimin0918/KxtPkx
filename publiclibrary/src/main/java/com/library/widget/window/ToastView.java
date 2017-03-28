package com.library.widget.window;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.library.R;


/**
 * Created by DaiYao on 2016/9/20.
 */
public class ToastView
{
    private static Toast toast;

    public static void makeText(Context context, String txt)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_toast, null);
        TextView tvTip = (TextView) view.findViewById(R.id.tips_msg);
        tvTip.setText(txt);

        if (toast != null)
        {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public static void makeText2(Context context, String txt)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_toast2, null);
        TextView tvTip = (TextView) view.findViewById(R.id.tips_msg);
        tvTip.setText(txt);

        if (toast != null)
        {
            toast.cancel();
        }
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
    public static void makeText3(Context context, String txt) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, txt, Toast.LENGTH_SHORT);
        toast.show();
    }
}
