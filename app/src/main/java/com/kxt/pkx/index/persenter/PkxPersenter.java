package com.kxt.pkx.index.persenter;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.kxt.pkx.R;
import com.kxt.pkx.common.base.CommunalPresenter;
import com.kxt.pkx.common.constant.SpConstant;
import com.kxt.pkx.common.constant.UrlConstant;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.common.utils.ObserverData;
import com.kxt.pkx.common.utils.PopupWindowUtils;
import com.kxt.pkx.index.adapter.IMainAdapter;
import com.kxt.pkx.index.jsonBean.BaseBean;
import com.kxt.pkx.index.jsonBean.CjrlDataBean;
import com.kxt.pkx.index.jsonBean.ConfigJson;
import com.kxt.pkx.index.jsonBean.KxHisItemBen;
import com.kxt.pkx.index.jsonBean.PinnedSectionBean;
import com.kxt.pkx.index.jsonBean.WebSocketKeyBean;
import com.kxt.pkx.index.model.IPkxModelImp;
import com.kxt.pkx.index.view.IPkxView;
import com.library.util.volley.load.PageLoadLayout;
import com.library.widget.window.ToastView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketOptions;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class PkxPersenter extends CommunalPresenter<IPkxView> implements IMainAdapter.OnItemClickLitener {

    private IPkxModelImp pkxModelImp;
    private RecyclerView recyclerView;
    private MaterialRefreshLayout materialRefreshLayout;
    private PageLoadLayout pageLoad;
    private LinearLayout separatorRlDate;
    private WebSocketConnection webSocket;
    private WebSocketOptions options = new WebSocketOptions();
    private List<BasicNameValuePair> heards = new ArrayList<BasicNameValuePair>();
    static List<KxHisItemBen> kxHisItemBenList = new ArrayList<KxHisItemBen>();//最外层数据
    static List<KxHisItemBen> kxHisItemBenListTemp = new ArrayList<KxHisItemBen>();//最外层数据
    static Map<String, Integer> KxHisItemMap = new HashMap<String, Integer>();//存储数据的下标对应id
    static List<PinnedSectionBean> LastKxData = new ArrayList<>();//最终数据
    private static Map<Integer, Integer> mapNextSecPos = new HashMap<Integer, Integer>();//存每个Section的下一个分类postion
    private GridLayoutManager gManager;
    private int isShowTopInt = 0;//recycleView是否滑动顶部
    private int expiredTime = 6000;//过期时间
    private static String isReconnect = "login";
    private int lastFirstVisibleItem = -1;
    private String wscUrl, dateString, adUrl;
    private IMainAdapter adapter;
    boolean isNullCache = false;
    private String serverTime = "";
    private long startRequestTime = 0;
    private long lastRequestTime = 6000;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    chuliData();//处理数据
                    break;
                case 1:
                    if (webSocket != null && webSocket.isConnected()) {
                        webSocket.disconnect();
                    }
                    break;
                case 2:
                    if (materialRefreshLayout != null) {
                        refreshComplete();
                        pageLoad.loadError("加载失败，请求超时");
                        isReconnect = "reconnct";
                        removeMessages(1);
                        sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
                case 3:
                    if (BaseUtils.isNetworkConnected(getContext())) {
                        getServerTime();
                    }
                    break;
                case 4:
                    if (webSocket != null && !webSocket.isConnected()) {
                        isReconnect = "reconnct";
                        webSocket.reconnect();
                    }
                    break;
            }
        }
    };

    public PkxPersenter() {
        pkxModelImp = new IPkxModelImp(this);
    }

    public void initViews(final RecyclerView recyclerView, MaterialRefreshLayout materialRefreshLayout,
                          final PageLoadLayout pageLoad, final LinearLayout separatorRlDate, final TextView mainSeparatorLineTvDate) {

        this.recyclerView = recyclerView;
        this.materialRefreshLayout = materialRefreshLayout;
        this.pageLoad = pageLoad;
        this.separatorRlDate = separatorRlDate;
        adapter = new IMainAdapter(LastKxData, getContext());
        adapter.setOnItemClickLitener(this);
        recyclerView.setAdapter(adapter);

        gManager = new GridLayoutManager(getContext(), 1);
        gManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        gManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gManager);

        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                if (BaseUtils.isNetworkConnected(getContext())) {
                    lastRequestTime = startRequestTime;
                    startRequestTime = System.currentTimeMillis();
                    if (Math.abs((startRequestTime - lastRequestTime)) < 5000) {
                        refreshComplete();
                    } else {
                        if (webSocket != null && webSocket.isConnected()) {
                            String str = "{\"cmd\":\"login\"}";
                            webSocket.sendTextMessage(str);
                        } else if (webSocket != null && !webSocket.isConnected()) {
                            isReconnect = "login";
                            webSocket.reconnect();
                            lastRequestTime = System.currentTimeMillis();
                        } else if (webSocket == null) {
                            isReconnect = "login";
                            handler.removeMessages(3);
                            handler.sendEmptyMessage(3);
                        }
                        recyclerView.setFocusable(false);
                        recyclerView.setClickable(false);
                        materialRefreshLayout.setClickable(false);
                        if (handler != null) {
                            handler.removeMessages(2);
                            handler.sendEmptyMessageDelayed(2, 5000);
                        }

                    }


                } else {
                    refreshComplete();
//                    pageLoad.loadError("亲，网络出错了！");

                }

            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                if (BaseUtils.isNetworkConnected(getContext())) {
                    if (webSocket != null && webSocket.isConnected()) {
                        if (kxHisItemBenListTemp != null && kxHisItemBenListTemp.size() > 1) {
                            String str = "{\"cmd\":\"history\",\"lastid\":" + kxHisItemBenListTemp.get(kxHisItemBenListTemp.size() - 1).getSocre() + ",\"number\":50}";
                            webSocket.sendTextMessage(str);
                        }
                    } else if (webSocket != null && !webSocket.isConnected()) {
                        isReconnect = "history";
                        webSocket.reconnect();
                    } else if (webSocket == null) {
                        isReconnect = "history";
                        handler.removeMessages(3);
                        handler.sendEmptyMessage(3);
                    }
                    recyclerView.setFocusable(false);
                    recyclerView.setClickable(false);
                    materialRefreshLayout.setClickable(false);
                    if (handler != null) {
                        handler.removeMessages(2);
                        handler.sendEmptyMessageDelayed(2, 5000);
                    }
                } else {
                    refreshComplete();
//                    pageLoad.loadError("亲，网络出错了！");
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // TODO Auto-generated method stub

                if (gManager.findFirstVisibleItemPosition() > 10) {
                    mView.setTopImage(View.VISIBLE);
                } else if (gManager.findFirstVisibleItemPosition() < 10) {
                    mView.setTopImage(View.GONE);
                }
                //顶部时间条的判断显示
                isShowTopInt = dy;
                if (gManager.findFirstVisibleItemPosition() >= 0 && isShowTopInt > 0) {
                    separatorRlDate.setVisibility(View.VISIBLE);
//                        myToast.show("滑动到顶部",Toast.LENGTH_SHORT);
                } else if (gManager.findFirstVisibleItemPosition() == 0 && isShowTopInt == 0) {
                    separatorRlDate.setVisibility(View.GONE);
//                    myToast.show("滑动到中间",Toast.LENGTH_SHORT);
                } else if (gManager.findFirstVisibleItemPosition() == 0 && isShowTopInt < 0) {
                    if (recyclerView.getChildAt(0).getTop() == 0) {
                        separatorRlDate.setVisibility(View.GONE);
                    }
//                    myToast.show("滑动到中间",Toast.LENGTH_SHORT);
                }


                //取得当前屏幕可见数据的第一个值
                int firstVisibleItem = gManager.findFirstVisibleItemPosition();

                //取得当前屏幕可见数据的第一个值的类别值
                int section = LastKxData.get(firstVisibleItem).getSort();

                //取得当前屏幕可见数据的第一个值的类别值在类别顺序中的下一个类别值
                int nextSecPosition = getLastIndex(section);

                if (firstVisibleItem != lastFirstVisibleItem) {
                    LinearLayout.MarginLayoutParams params = (LinearLayout.MarginLayoutParams) separatorRlDate.getLayoutParams();
                    params.topMargin = 0;
                    separatorRlDate.setLayoutParams(params);
                    dateString = BaseUtils.parseMillisNew(LastKxData.get(firstVisibleItem).getKxValue().getSocre());
                    mainSeparatorLineTvDate.setText(dateString + "   " + BaseUtils.getWeek(dateString));
                }


                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = recyclerView.getChildAt(0);
                    if (childView != null) {

                        int titleHeight = separatorRlDate.getHeight();
                        int bottom = childView.getBottom();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) separatorRlDate
                                .getLayoutParams();
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            separatorRlDate.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                separatorRlDate.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
        recyclerView.setAdapter(adapter);


        if (kxHisItemBenListTemp != null && kxHisItemBenListTemp.size() > 1) {

        } else {
            pageLoad.startLoading();
        }
        if (!BaseUtils.isNetworkConnected(getContext())) {
            try {
                String cacheData = SpConstant.getCachePreferences().getString(SpConstant.CACHE_DATA_KEY, "");
                if (null != cacheData && !"".equals(cacheData)) {
                    List<PinnedSectionBean> tempLastKxData = (List<PinnedSectionBean>) BaseUtils.DeSerialization(cacheData);
                    this.LastKxData.clear();
                    this.LastKxData.addAll(tempLastKxData);
                    if (null != this.LastKxData && this.LastKxData.size() > 0) {
                        pageLoad.loadSuccess();
                        adapter.notifyDataSetChanged();
                    } else {
                        handler.sendEmptyMessage(1);
                    }

                } else {
                    pageLoad.loadError(getContext().getResources().getString(R.string.load_nonet));
                }

            } catch (Exception e) {
                e.printStackTrace();
                pageLoad.loadError(getContext().getResources().getString(R.string.load_nonet));
            }
        } else if (isNullCache) {
            isReconnect = "login";
            isNullCache = false;
            if (webSocket != null && BaseUtils.isNetworkConnected(getContext())) {
                String str = "{\"cmd\":\"login\"}";
                webSocket.sendTextMessage(str);
            }

        } else {
            handler.sendEmptyMessage(3);
        }

    }

    /**
     * 获取服务器的时间
     *
     * @return
     */
    public void getServerTime() {
        Map<String, String> map = new HashMap<>();
        Gson gson = new Gson();
        try {

            map.put("content", BaseUtils.createJWT(UrlConstant.URL_PRIVATE_KEY, gson.toJson(new ConfigJson())));
            pkxModelImp.getServerTime(new ObserverData<BaseBean>() {
                @Override
                public void onCallback(BaseBean data) {
                    super.onCallback(data);
                    if (null != data && !"".equals(data)) {

                        if (data.getStatus().equals("1") && null != data.getData() && !"".equals(data.getData())) {
                            serverTime = data.getData();
                        } else {
                            serverTime = String.valueOf(new Date().getTime()).toString().substring(0, 10);
                        }
                    } else {
                        serverTime = String.valueOf(new Date().getTime()).toString().substring(0, 10);
                    }
                    connentWsc(serverTime);
                }

                @Override
                public void onError(String error) {
                    super.onError(error);
                    serverTime = String.valueOf(new Date().getTime()).toString().substring(0, 10);
                    connentWsc("");
                }
            }, map, UrlConstant.SERVER_TIME_URL);
        } catch (Exception e) {
            e.printStackTrace();
            serverTime = String.valueOf(new Date().getTime()).toString().substring(0, 10);
            connentWsc("");
        }


    }

    public void connentWsc(final String serverTime) {
        wscUrl = SpConstant.getWscPreferences().getString(SpConstant.WEBSOCKET_URL, "");
        if (null == wscUrl || "".equals(wscUrl)) {
            Map<String, String> map = new HashMap<>();
            Gson gson = new Gson();
            try {
                map.put("content", BaseUtils.createJWT(UrlConstant.URL_PRIVATE_KEY, gson.toJson(new ConfigJson())));
                //缓存的 wscUrl为空，重新请求
                pkxModelImp.getWscUrl(new ObserverData<BaseBean>() {
                    @Override
                    public void onCallback(BaseBean data) {
                        super.onCallback(data);
                        if (null != data && !"".equals(data)) {
                            if (data.getStatus().equals("1") && null != data.getData() && !"".equals(data.getData())) {
                                wscUrl = data.getData();
                                SpConstant.getWscPreferences().edit().putString(SpConstant.WEBSOCKET_URL, wscUrl).commit();
                            } else {
                                SpConstant.getWscPreferences().edit().putString(SpConstant.WEBSOCKET_URL, "ws://116.62.34.195:9502").commit();
                            }
                        } else {
                            SpConstant.getWscPreferences().edit().putString(SpConstant.WEBSOCKET_URL, "ws://116.62.34.195:9502").commit();

                        }
                        connentWscByUrl(serverTime);
                    }

                    @Override
                    public void onError(String error) {
                        super.onError(error);
                        SpConstant.getWscPreferences().edit().putString(SpConstant.WEBSOCKET_URL, "ws://116.62.34.195:9502").commit();
                        connentWscByUrl(serverTime);
                    }
                }, map, UrlConstant.WEBSOCKET_URL);
            } catch (Exception e) {
                e.printStackTrace();
                SpConstant.getWscPreferences().edit().putString(SpConstant.WEBSOCKET_URL, "ws://116.62.34.195:9502").commit();
                connentWscByUrl(serverTime);
            }

        } else {
            connentWscByUrl(serverTime);
        }
    }


    public void connentWscByUrl(String serverTime) {
        //连接webSocekt
        if (null == webSocket) {
            WebSocketKeyBean keyBean = new WebSocketKeyBean();
            String strIp = "";
            webSocket = new WebSocketConnection();
            options.setReceiveTextMessagesRaw(false);
            heards.add(new BasicNameValuePair("origin", getContext().getString(R.string.websocket_domain)));
            heards.add(new BasicNameValuePair("zml", "2544"));

            //获取wifi服务
            WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启

            if (!wifiManager.isWifiEnabled()) {
//                wifiManager.setWifiEnabled(true);
                strIp = BaseUtils.getIpByInt(120);
            } else {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                strIp = BaseUtils.getIpByInt(ipAddress);
            }

            keyBean.setRemote_addr(strIp);
            keyBean.setDomain(getContext().getString(R.string.websocket_domain));

            if (TextUtils.isEmpty(serverTime)) {
                keyBean.setTime(String.valueOf(new Date().getTime()).toString().substring(0, 10));
            } else {
                keyBean.setTime(serverTime);
            }
            try {
                String entyString = BaseUtils.encryptTwo(JSON.toJSONString(keyBean), getContext().getString(R.string.websocket_key), expiredTime, keyBean.getTime());

                webSocket.connect(wscUrl + "?token=" + entyString, null, new WebSocket.ConnectionHandler() {
                    @Override
                    public void onOpen() {
                        if (isReconnect.equals("reconnct")) {

                        } else if (isReconnect.equals("login")) {
                            //第一次请求数据
                            String payload = "{\"cmd\":\"login\"}";
                            webSocket.sendTextMessage(payload);
                        } else if (isReconnect.equals("history")) {
                            if (null != kxHisItemBenListTemp && kxHisItemBenListTemp.size() > 1) {
                                String str = "{\"cmd\":\"history\",\"lastid\":" + kxHisItemBenListTemp.get(kxHisItemBenListTemp.size() - 1).getSocre() + ",\"number\":50}";
                                webSocket.sendTextMessage(str);
                            }
                        }
                        isReconnect = "login";
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        if (BaseUtils.isNetworkConnected(getContext())) {
                            handler.sendEmptyMessage(4);
                            handler.sendEmptyMessage(3);
                            if (code == 1 && reason == null) {
                                webSocket = null;
                                handler.sendEmptyMessageDelayed(3, 1000);

                            } else {
                                handler.sendEmptyMessageDelayed(4, 5000);
                            }
                        }


                    }

                    @Override
                    public void onTextMessage(String payload) {
                        try {
                            JSONObject jsonObject = new JSONObject(payload);
                            String cmdStr = jsonObject.getString("cmd");
                            if (1 == jsonObject.getInt("status")) {
                                switch (cmdStr) {
                                    case "login":
                                        kxHisItemBenListTemp.clear();//清除所有数据
                                        break;
                                    case "history":
                                        break;
                                    case "timely":
                                        JSONObject timelyObj = jsonObject.getJSONObject("msg");
                                        KxHisItemBen timeKxObj = JSON.parseObject(timelyObj.toString(), KxHisItemBen.class);
                                        if (timeKxObj != null && !TextUtils.isEmpty(timeKxObj.getWodo())) {
                                            switch (timeKxObj.getWodo()) {
                                                case "modify":
                                                    if (KxHisItemMap != null && KxHisItemMap.size() > 0 && kxHisItemBenListTemp.size() > 0) {
                                                        if (KxHisItemMap.containsKey(timeKxObj.getId())) {
                                                            int postionMod = KxHisItemMap.get(timeKxObj.getId());
                                                            kxHisItemBenListTemp.set(postionMod, timeKxObj);
                                                            handler.sendEmptyMessage(0);
                                                        }
                                                    }
                                                    break;
                                                case "delete":
                                                    if (KxHisItemMap != null && KxHisItemMap.size() > 0 && kxHisItemBenListTemp.size() > 0) {

                                                        if (KxHisItemMap.containsKey(timeKxObj.getId())) {
                                                            int postionMod = KxHisItemMap.get(timeKxObj.getId());
                                                            kxHisItemBenListTemp.remove(postionMod);
                                                            handler.sendEmptyMessage(0);
                                                        }
                                                    }
                                                    break;
                                            }

                                        } else {
                                            if (kxHisItemBenListTemp != null && kxHisItemBenListTemp.size() > 0 && KxHisItemMap != null && KxHisItemMap.size() > 0) {

                                                if (!KxHisItemMap.containsKey(timeKxObj.getId())) {
                                                    kxHisItemBenListTemp.add(0, timeKxObj);
                                                    handler.sendEmptyMessage(0);
                                                }


                                            }
                                        }

                                        break;
                                }
                                switch (cmdStr) {
                                    case "login":
                                    case "history":
                                        JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                        List<String> dataBeanList = JSON.parseArray(jsonArray.toString(), String.class);

                                        if (dataBeanList != null && dataBeanList.size() > 0) {
                                            List<KxHisItemBen> hisItemBens = new ArrayList<KxHisItemBen>();
                                            for (int d = 0; d < dataBeanList.size(); d++) {
                                                KxHisItemBen kxHisItemBenTem = JSON.parseObject(dataBeanList.get(d), KxHisItemBen.class);
                                                hisItemBens.add(kxHisItemBenTem);
                                            }
                                            if (hisItemBens != null && hisItemBens.size() > 0) {
                                                if (kxHisItemBenListTemp.size() > 5) {
                                                    KxHisItemBen kxHisObj = kxHisItemBenListTemp.get(kxHisItemBenListTemp.size() - 1);
                                                    KxHisItemBen kxHisNew = hisItemBens.get(0);
                                                    long kxobjInt = Long.parseLong(kxHisObj.getSocre());
                                                    long kxhisNewInt = Long.parseLong(kxHisNew.getSocre());

                                                    if (kxobjInt > kxhisNewInt) {
                                                        kxHisItemBenListTemp.addAll((kxHisItemBenListTemp.size()), hisItemBens);
                                                        hisItemBens.clear();
                                                    }
                                                } else {
                                                    kxHisItemBenListTemp.addAll((kxHisItemBenListTemp.size()), hisItemBens);
                                                    hisItemBens.clear();
                                                }

                                            }

                                            handler.sendEmptyMessage(0);

                                        } else {
                                            switch (cmdStr) {
                                                case "login":
                                                    isReconnect = "login";
                                                    refreshComplete();
                                                    break;
                                                case "history":
                                                    isReconnect = "reconnct";
                                                    ToastView.makeText3(getContext(), "无更多类容");
                                                    refreshComplete();
                                                    break;
                                            }
                                            handler.removeMessages(1);
                                            handler.sendEmptyMessageDelayed(1, 800);
                                        }
                                        handler.removeMessages(2);//加载成功，移除消失加载进度条
                                        break;
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            pageLoad.loadError("数据加载出错");
                        }
                    }

                    @Override
                    public void onRawTextMessage(byte[] payload) {

                    }

                    @Override
                    public void onBinaryMessage(byte[] payload) {

                    }
                }, options, heards);

            } catch (WebSocketException e) {
                e.printStackTrace();
                pageLoad.loadError("数据加载出错");
            }
        }

    }

    public void chuliData() {
        String toggleString = SpConstant.getTogglePreferences().getString(SpConstant.TOGGLE_DATA_KEY, "false").trim();
        kxHisItemBenList.clear();
        kxHisItemBenList.addAll(kxHisItemBenListTemp);
        if (toggleString.contains("true")) {
            List<KxHisItemBen> posIndf = new ArrayList<KxHisItemBen>();
            for (int c = 0; c < kxHisItemBenList.size(); c++) {
                if (kxHisItemBenList.get(c).getCode().equals("CJRL")) {
                    CjrlDataBean.MsgBean.ContentBean cj = JSON.parseObject(kxHisItemBenList.get(c).getContent(), CjrlDataBean.MsgBean.ContentBean.class);
                    if (!cj.getImportance().equals("高")) {
                        posIndf.add(kxHisItemBenList.get(c));
                    }

                }
            }
            for (KxHisItemBen dd : posIndf) {
                kxHisItemBenList.remove(dd);
            }
        } else {
            kxHisItemBenList.clear();
            kxHisItemBenList.addAll(kxHisItemBenListTemp);
            adapter.notifyDataSetChanged();
        }

        if (kxHisItemBenList != null && kxHisItemBenList.size() > 0) {
            LastKxData.clear();
            List<PinnedSectionBean> pinnedSectionBeenTemp = getKxpinnedsectionData(kxHisItemBenList);
            LastKxData.addAll(pinnedSectionBeenTemp);
            adapter.notifyDataSetChanged();
            refreshComplete();
            pageLoad.loadSuccess();
            SpConstant.getCachePreferences().edit().putString(SpConstant.CACHE_DATA_KEY, BaseUtils.Serialize(LastKxData)).commit();

        } else {
            pageLoad.loadNoData("筛选后没有数据显示");
        }

        KxHisItemMap.clear();//清除列表
        for (int kk = 0; kk < kxHisItemBenListTemp.size(); kk++) {
            KxHisItemMap.put(kxHisItemBenListTemp.get(kk).getId(), kk);
        }

    }

    /**
     * 通过HashMap键值对的特性，将ArrayList的数据进行分组，返回带有分组Header的ArrayList。
     * 从后台接受到的ArrayList的数据，其中日期格式为：yyyy-MM-dd HH:mm:ss
     * 返回的list是分类后的包含header（yyyy-MM-dd）和item（HH:mm:ss）的ArrayList
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static synchronized List<PinnedSectionBean> getKxpinnedsectionData(List<KxHisItemBen> kxHisItemBenList) {
        List<PinnedSectionBean> pinnedSectionBeenList = new ArrayList<>();
        String key = "";
        int sortIn = 1;
        int nextSorPos = 0;
        mapNextSecPos.clear();
        KxHisItemBen kxHisItemBenTemp = new KxHisItemBen();

        key = BaseUtils.parseMillisNew(kxHisItemBenList.get(0).getSocre());
        long jjjj = Long.parseLong(kxHisItemBenList.get(0).getSocre()) + 1;

        kxHisItemBenTemp.setSocre(jjjj + "");
        pinnedSectionBeenList.add(new PinnedSectionBean("SECTION", kxHisItemBenTemp, sortIn));
        for (int k = 0; k < kxHisItemBenList.size(); k++) {
            kxHisItemBenTemp = kxHisItemBenList.get(k);

            String keyTwo = BaseUtils.parseMillisNew(kxHisItemBenTemp.getSocre());
            nextSorPos++;
            if (key.equals(keyTwo)) {

                pinnedSectionBeenList.add(new PinnedSectionBean("ITEM", kxHisItemBenTemp, sortIn));
            } else {
                key = keyTwo;

                mapNextSecPos.put(sortIn, nextSorPos);
                ++sortIn;
                nextSorPos++;
                pinnedSectionBeenList.add(new PinnedSectionBean("SECTION", kxHisItemBenTemp, sortIn));
                pinnedSectionBeenList.add(new PinnedSectionBean("ITEM", kxHisItemBenTemp, sortIn));
            }


        }

        return pinnedSectionBeenList;
    }

    private int getLastIndex(int section) {
        //用迭代器遍历map添加到list里面
        int nextPostionLast = 0;
        if (mapNextSecPos.size() > 0) {
            Iterator iterator = mapNextSecPos.entrySet().iterator();
            int postionMap = 1;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Integer key = (Integer) entry.getKey();

                if (section == key) {
                    nextPostionLast = (int) entry.getValue();
                    break;
                }

                postionMap++;
            }
        }

        return nextPostionLast;
    }

    public void notifyAdapter() {
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
        handler.removeMessages(0);
    }

    public void onLayoutAfresh(PageLoadLayout pageLoad) {
        if (BaseUtils.isNetworkConnected(getContext())) {
            if (webSocket != null && webSocket.isConnected()) {
                String str = "{\"cmd\":\"login\"}";
                webSocket.sendTextMessage(str);
            } else {
                isReconnect = "login";
                getServerTime();
            }
        }
    }

    /**
     * 刷新和加载更多完成
     */
    private synchronized void refreshComplete() {
        recyclerView.setFocusable(true);
        recyclerView.setClickable(true);
        materialRefreshLayout.setClickable(true);
        materialRefreshLayout.finishRefreshing();
        materialRefreshLayout.finishRefresh();
        materialRefreshLayout.finishRefreshLoadMore();
    }

    public void onActDestory() {
        if (webSocket != null) {
            if (webSocket.isConnected()) {
                webSocket.disconnect();
            }
        }
    }

    @Override
    public void OnItemClick(View view, int positon, int type) {
    }

    @Override
    public void OnItemLongClick(View view, int position) {

    }
}
