package com.kxt.pkx.common.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.LinearLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.sauronsoftware.base64.Base64;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class BaseUtils {
    public static boolean isNetworkAvailable(Context context)
    {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        networkInfo=null;
                        return true;
                    }
                }
            }
        }

        return false;
    }
    /**
     * 获取versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager manager = context.getPackageManager();
            versionName = manager.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 测量底部内置按钮高度
     *
     * @param mContext
     * @return
     */
    public static Integer getNavigationBar(Context mContext) {

        int hegit = 0;
        boolean isHasNavigationBar = DeviceUtil.checkDeviceHasNavigationBar(mContext);
        if (isHasNavigationBar) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            hegit = DeviceUtil.getNavigationBarHeight(mContext);
//            layoutParams.setMargins(0, 0, 0, hegit);//4个参数按顺序分别是左上右下
//            linearLayout.setLayoutParams(layoutParams);
        }

        return hegit;
    }


    /**
     * @param time
     * @return yyyy-MM-dd
     */
    public static String parseMillisNew(String time) {
        SimpleDateFormat sdf_temp1 = new SimpleDateFormat("yyyy-MM-dd"); // yyyy-MM-dd
        long long_time = Long.parseLong(time); // 这个可以
        String res = sdf_temp1.format(new Date(long_time));
        return res;
    }

    /**
     * 序列化对象
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static String Serialize(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");

            return serStr;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                objectOutputStream.close();
                byteArrayOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object DeSerialization(String str) {
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {

            String redStr = java.net.URLDecoder.decode(str, "UTF-8");
            byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object zxBean = (Object) objectInputStream.readObject();

            return zxBean;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
                byteArrayInputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 创建 jwt
     *
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public static String createJWT(String key, String value) throws Exception {

        String jwtString = Jwts.builder()
                .setSubject(value)
                .signWith(SignatureAlgorithm.HS256, getJwtKey(key))
                .compact();
        return jwtString;
    }

    /**
     * 解密 jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt, String key) throws Exception {

        Claims claims = Jwts.parser()
                .setSigningKey(getJwtKey(key))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    /**
     * 获取 jwt  key
     *
     * @param keyValue
     * @return
     */
    public static Key getJwtKey(String keyValue) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = keyValue.getBytes();
        Key key = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return key;
    }

    /**
     * 判断是否有网络连接3-31
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);   //得到一个对象，然后把对象转成manager
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 根据 i 获取ip地址
     *
     * @param i
     * @return
     */
    public static String getIpByInt(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 初步加密
     *
     * @param data   原始数据
     * @param key    加密Key
     * @param expire 过期时间
     * @return 加密后的数据
     */

    public static String encryptTwo(String data, String key, int expire, String todayLong) {
        try {
            long time = 0;
            if (expire != 0) {


                if (todayLong.length() == 10) {
                    todayLong += "000";
                }
                time = Long.parseLong(todayLong);

            }
            StringBuffer _time = foramtTime((time + expire * 1000) / 1000);
            // System.out.println((System.currentTimeMillis() + expire * 1000) /
            // 1000);
            String _key = md5(key);
            // System.out.println("md5加密后的key：" + _key);
            String _data = it.sauronsoftware.base64.Base64.encode(_time + data);// data
            // System.out.println("Base64加密后的数据：" + _data);
            int x = 0;
            int dataLen = _data.length();
            int keyLen = _key.length();
            // System.out.println("dataLen=" + dataLen + "---" + "keyLen=" +
            // keyLen);
            StringBuffer sb1 = new StringBuffer();
            for (int i = 0; i < dataLen; i++) {
                if (x == keyLen)
                    x = 0;
                sb1.append(_key.substring(x, x + 1));
                x++;
            }
            // System.out.println("sb1=" + sb1);
            byte[] bytes = new byte[dataLen];
            for (int i = 0; i < dataLen; i++) {
                int b = (int) _data.substring(i, i + 1).charAt(0)
                        + sb1.substring(i, i + 1).charAt(0) % 256;
                if (b > 127)
                    b -= 256;
                bytes[i] = (byte) b;
            }

            decrypt(new String(Base64.encode(bytes)).replace("+", "-")
                    .replace("=", "").replace("/", "_"), key);
            return new String(Base64.encode(bytes)).replace("+", "-")
                    .replace("=", "").replace("/", "_");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密
     *
     * @param data 加密后的数据
     * @param key  加密时用到的key
     * @return 原始数据
     */
    public static String decrypt(String data, String key)
            throws UnsupportedEncodingException {
        String _key = md5(key);
        String _data = data.replace("-", "+").replace("_", "/");
        int mod4 = _data.length() % 4;
        if (mod4 != 0) {
            _data += "====".substring(mod4);
        }
        byte[] bytes = it.sauronsoftware.base64.Base64.decode(_data.getBytes());
        int dataLen = bytes.length;
        int x = 0;
        int keyLen = _key.length();
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        for (int i = 0; i < dataLen; i++) {
            if (x == keyLen)
                x = 0;
            sb.append(_key.substring(x, x + 1));
            x++;
        }
        for (int i = 0; i < dataLen; i++) {
            if (bytes[i] < sb.substring(i, i + 1).charAt(0)) {
                sb2.append((char) (bytes[i] + 256 - sb.substring(i, i + 1)
                        .charAt(0)));
            } else {
                sb2.append((char) (bytes[i] - sb.substring(i, i + 1).charAt(0)));
            }
        }
        _data = Base64.decode(sb2.toString());
        StringBuffer sb3 = new StringBuffer();
        StringBuffer sb4 = new StringBuffer();
        for (int i = 0; i < _data.length(); i++) {
            if (i < 10) {
                // 将时间对应的ASCII码转换为时间
                sb3.append((char) _data.charAt(i));
            } else {
                // data
                sb4.append(_data.charAt(i));
            }
        }

        boolean isOut = isTimeOk(new String(sb3));
        _data = new String(sb4);
        @SuppressWarnings("unused")
        String str = null;
        if (isOut)
            str = "时间过长，数据失效";
        else
            str = _data;
        return _data;
    }

    /**
     * md5加密
     *
     * @param key
     * @return
     */
    public static String md5(String key) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes("utf-8"));
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    /**
     * 判断日期是否过期
     *
     * @param _data
     * @return
     */
    private static boolean isTimeOk(String _data) {
        long time = Long.parseLong(_data.substring(0, 10));
        if (System.currentTimeMillis() / 1000 > time) {
            return true;
        }
        return false;
    }

    /**
     * 格式化过期时间
     *
     * @param time
     * @return
     */
    private static StringBuffer foramtTime(long time) {
        StringBuffer timeBuffer = new StringBuffer();
        StringBuffer _time = new StringBuffer();

        switch (timeBuffer.append(time).length()) {
            case 1:
                _time.append("000000000").append(time);
                break;
            case 2:
                _time.append("00000000").append(time);
                break;
            case 3:
                _time.append("0000000").append(time);
                break;
            case 4:
                _time.append("000000").append(time);
                break;
            case 5:
                _time.append("00000").append(time);
                break;
            case 6:
                _time.append("0000").append(time);
                break;
            case 7:
                _time.append("000").append(time);
                break;
            case 8:
                _time.append("00").append(time);
                break;
            case 9:
                _time.append("0").append(time);
                break;
            case 10:
                _time.append(time);
                break;
            default:
                _time.append("0000000000");
                break;
        }
        return _time;
    }


    /**
     * 根据时间戳获取日期
     *
     * @param beginDate
     * @return
     */
    public static String getDateBySjc(String beginDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String date = sdf.format(new Date(Long.parseLong(beginDate) * 1000));
        return date;
    }

    //  String pTime = "2012-03-12";
    public static String getWeek(String pTime) {


        String Week = "星期";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "六";
        }


        return Week;
    }

    @SuppressWarnings("unchecked")
    public static List String2SceneList(String SceneListString) throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = android.util.Base64.decode(SceneListString.getBytes(),
                android.util.Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List SceneList = (List) objectInputStream
                .readObject();
        objectInputStream.close();
        return SceneList;
    }

}
