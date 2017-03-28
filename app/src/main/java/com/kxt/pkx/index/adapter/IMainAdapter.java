package com.kxt.pkx.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kxt.pkx.R;
import com.kxt.pkx.common.constant.SpConstant;
import com.kxt.pkx.common.constant.StringConstant;
import com.kxt.pkx.common.coustom.RoundImageView;
import com.kxt.pkx.common.coustom.TabLinearLayout;
import com.kxt.pkx.common.utils.BaseUtils;
import com.kxt.pkx.index.jsonBean.CjrlDataBean;
import com.kxt.pkx.index.jsonBean.KxDataBean;
import com.kxt.pkx.index.jsonBean.PinnedSectionBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class IMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.root_linear)
    LinearLayout rootLinear;

    private List<PinnedSectionBean> listDatas;
    private Context context;
    private OnItemClickLitener mOnItemClickLitener;

    public IMainAdapter(List<PinnedSectionBean> listDatas, Context context) {
        this.listDatas = listDatas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder mViewHolder = null;
        switch (viewType) {
            case 1://快讯
                view = LayoutInflater.from(context).inflate(R.layout.kx_item, parent, false);
                ViewGroup rootKx = (ViewGroup) view.findViewById(R.id.root_linear);
                mViewHolder = new ViewHolderKx(rootKx);

                break;
            case 3://财经日历
                view = LayoutInflater.from(context).inflate(R.layout.cjrl_item, parent, false);
                ViewGroup rootCjrl = (ViewGroup) view.findViewById(R.id.root_clinear);
                mViewHolder = new ViewHolderCjrl(rootCjrl);

                break;
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.flash_item_separator, parent, false);
                mViewHolder = new ViewHolderSeparator(view);
                break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.item_null, parent, false);
                mViewHolder = new ViewHolderNull(view);
                break;
        }


        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case 1:

                final PinnedSectionBean sectionBean = listDatas.get(position);
                final KxDataBean.MsgBean.ContentBean kxItem = JSON.parseObject(listDatas.get(position).getKxValue().getContent(),
                        KxDataBean.MsgBean.ContentBean.class);
                if (listDatas.get(position).getKxValue().getCode().equals("KUAIXUN")) {
                    final ViewHolderKx kxViewHolder = (ViewHolderKx) holder;
                    kxViewHolder.itemView.setTag(kxItem.getAutoid());

                    setKxButtonBySp(kxViewHolder, kxItem.getAutoid());
                    setKxTitle(kxViewHolder, kxItem.getImportance(), kxItem.getTitle());
                    setKxTime(kxViewHolder, kxItem.getTime());

                    kxViewHolder.titleText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            // TODO Auto-generated method stub
                            kxViewHolder.titleText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            if (kxViewHolder.titleText.getLineCount() > 3) {
                                kxViewHolder.kxImage.setVisibility(View.VISIBLE);
                            } else {
                                kxViewHolder.kxImage.setVisibility(View.GONE);
                            }
                        }
                    });
                    kxViewHolder.kxButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (sectionBean.isSelect()) {
                                kxViewHolder.kxImage.setImageResource(R.mipmap.open_button);
                                kxViewHolder.titleText.setMaxLines(3);
                                sectionBean.setSelect(false);
                                SpConstant.getSelectPreferences().edit().remove(kxItem.getAutoid()).commit();
                            } else {
                                //设置新的位置样式
                                kxViewHolder.kxImage.setImageResource(R.mipmap.pack_button);
                                kxViewHolder.titleText.setMaxLines(100);
                                sectionBean.setSelect(true);
                                SpConstant.getSelectPreferences().edit().
                                        putString(kxItem.getAutoid(), kxItem.getAutoid())
                                        .commit();
                            }
                            mOnItemClickLitener.OnItemClick(kxViewHolder.itemView, position, getItemViewType(position));
                        }
                    });


                }
                break;
            case 3:
                if (listDatas.get(position).getKxValue().getCode().equals("CJRL")) {
                    final ViewHolderCjrl cjViewHolder = (ViewHolderCjrl) holder;
                    CjrlDataBean.MsgBean.ContentBean kxItemCJRL = JSON.parseObject(listDatas.get(position).getKxValue().getContent(), CjrlDataBean.MsgBean.ContentBean.class);

                    setCjrlImportance(cjViewHolder, kxItemCJRL);
                    setCjrlTime(cjViewHolder, kxItemCJRL.getTime());
                    setCjrlState(cjViewHolder, kxItemCJRL.getState());
                    setCjrlEffect(cjViewHolder, kxItemCJRL.getEffect(), listDatas.get(position));

                    cjViewHolder.rootClinear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickLitener.OnItemClick(cjViewHolder.itemView, position, getItemViewType(position));
                        }
                    });
                }

                break;
            case 0:
                final ViewHolderSeparator separatorHolder = (ViewHolderSeparator) holder;
                String dateString = BaseUtils.parseMillisNew(listDatas.get(position).getKxValue().getSocre());
                separatorHolder.mainSeparatorLineTvDate.setText(dateString + "   " + BaseUtils.getWeek(dateString));
                break;
            default:
                final ViewHolderNull viewHolderNull = (ViewHolderNull) holder;
                break;
        }
    }

    /**
     * 设置财经日历的影响值
     *
     * @param cjViewHolder
     * @param effect
     * @param pinnedSectionBean
     */
    private void setCjrlEffect(ViewHolderCjrl cjViewHolder, String effect, PinnedSectionBean pinnedSectionBean) {
        cjViewHolder.effectsLinear.removeAllViews();
        if (null != effect && !"".equals(effect)) {
            if (effect.equals("||")) {
                //影响较小
                TabLinearLayout tabMindLayout = new TabLinearLayout(context);
                tabMindLayout.setEffectMind();
                tabMindLayout.setTextValue("影响较小");
                cjViewHolder.effectsLinear.addView(tabMindLayout);
            } else {
                String[] effects = effect.split("\\|");
                if (effects.length < 2) {
                    //如果数组[0]不为空，数组[1]为空，则利多
                    String[] value0 = effects[0].split("\\ ");
                    addLinearTabs(pinnedSectionBean, value0, cjViewHolder.effectsLinear, true);
                } else {
                    if ((null == effects[0] || "".equals(effects[0])) && (null != effects[1] && !"".equals(effects[1]))) {
                        //如果数组[0]为空，数组[1]不为空，则利空
                        String[] value1 = effects[1].split("\\ ");
                        addLinearTabs(pinnedSectionBean, value1, cjViewHolder.effectsLinear, false);

                    } else if ((null != effects[0] && !"".equals(effects[0])) && (null != effects[1] && !"".equals(effects[1]))) {
                        //如果数组[0]和数组[1]都不为空，则有利多和利空
                        String[] value0 = effects[0].split("\\ ");
                        String[] value1 = effects[1].split("\\ ");
                        addLinearTabs(pinnedSectionBean, value0, cjViewHolder.effectsLinear, true);
                        addLinearTabs(pinnedSectionBean, value1, cjViewHolder.effectsLinear, false);

                    }
                }
            }
        }
    }


    /**
     * 添加财经日历利多利空标签
     *
     * @param pinnedSectionBean
     * @param value
     * @param effectsLinear
     */
    private void addLinearTabs(PinnedSectionBean pinnedSectionBean, String[] value, LinearLayout effectsLinear, boolean isLd) {
        String ImprList = SpConstant.getCheckPreferences().getString(SpConstant.CHECK_DATA_KEY, "");

        for (int i = 0; i < value.length; i++) {
            TabLinearLayout tabLinearLayout = new TabLinearLayout(context);
            tabLinearLayout.setTextValue(value[i]);
            if (isLd) {
                tabLinearLayout.setEffectHigh();
            } else {
                tabLinearLayout.setEffectLow();
            }

            if (value[i].equals("金银")) {
                if (pinnedSectionBean.isShowJy() && ImprList.contains("金银")) {
                    tabLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    tabLinearLayout.setVisibility(View.GONE);
                }
            }

            if (value[i].equals("石油")) {
                if (pinnedSectionBean.isShowSy() && ImprList.contains("石油")) {
                    tabLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    tabLinearLayout.setVisibility(View.GONE);
                }
            }
            if (!value[i].equals("石油") && !value[i].equals("金银")) {

                if (pinnedSectionBean.isShowWh() && ImprList.contains("外汇")) {
                    tabLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    tabLinearLayout.setVisibility(View.GONE);
                }
            }
            effectsLinear.addView(tabLinearLayout);
        }
    }

    /**
     * 设置财经日历国家国旗的显示
     *
     * @param cjViewHolder
     * @param state
     */
    private void setCjrlState(ViewHolderCjrl cjViewHolder, String state) {
        if (state.equals(StringConstant.STATE_CHINA)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_china);
        } else if (state.equals(StringConstant.STATE_AMERICAN)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_american);
        } else if (state.equals(StringConstant.STATE_GERMAN)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_german);
        } else if (state.equals(StringConstant.STATE_ENGLAND)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_england);
        } else if (state.equals(StringConstant.STATE_FRANCE)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_france);
        } else if (state.equals(StringConstant.STATE_AUSTRALIA)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_australia);
        } else if (state.equals(StringConstant.STATE_JAPAN)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_japan);
        } else if (state.equals(StringConstant.STATE_KOREA)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_korea);
        } else if (state.equals(StringConstant.STATE_CANADA)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_canada);
        } else if (state.equals(StringConstant.STATE_HONGKONG)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_hongkong);
        } else if (state.equals(StringConstant.STATE_SWITZERLAND)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_swizerland);
        } else if (state.equals(StringConstant.STATE_ITALY)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_italy);
        } else if (state.equals(StringConstant.STATE_EURO_AREA)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_europe_area);
        } else if (state.equals(StringConstant.STATE_NEW_ZEALAND)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_newzealand);
        } else if (state.equals(StringConstant.STATE_TAIWAN)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_taiwan);
        } else if (state.equals(StringConstant.STATE_SPANISH)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_spanish);
        } else if (state.equals(StringConstant.STATE_SINGAPORE)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_singapore);
        } else if (state.equals(StringConstant.STATE_BRAZIL)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_brazil);
        } else if (state.equals(StringConstant.STATE_SOUTH_AFRICA)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_south_africa);
        } else if (state.equals(StringConstant.STATE_INDIA)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_india);
        } else if (state.equals(StringConstant.STATE_INDONESIA)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_indonesia);
        } else if (state.equals(StringConstant.STATE_RUSSIA)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_russia);
        } else if (state.equals(StringConstant.STATE_GREECE)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_greece);
        } else if (state.equals(StringConstant.STATE_ISRAEL)) {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_israel);
        } else {
            cjViewHolder.cjrlState.setImageResource(R.mipmap.state_default);
        }
    }


    /**
     * 设置财经日历的时间
     *
     * @param cjViewHolder
     * @param time
     */
    private void setCjrlTime(ViewHolderCjrl cjViewHolder, String time) {
        if (null != time && !"".equals(time) && time.length() >= 16) {
            cjViewHolder.cjrlTime.setText(time.substring(11, 16));
        } else {
            cjViewHolder.cjrlTime.setText("---");
        }
    }

    /**
     * 设置快讯的时间
     *
     * @param kxViewHolder
     * @param time
     */
    private void setKxTime(ViewHolderKx kxViewHolder, String time) {
        if (null != time && !"".equals(time) && time.length() >= 16) {
            kxViewHolder.kxTiem.setText(time.substring(11, 16));
        } else {
            kxViewHolder.kxTiem.setText("---");
        }
    }

    /**
     * 根据重要性 设置快讯文字的显示
     *
     * @param kxViewHolder
     * @param importance
     */
    private void setKxTitle(ViewHolderKx kxViewHolder, String importance, String title) {
        if (importance.contains("高")) {
            kxViewHolder.titleText.setTextColor(context.getResources().getColor(R.color.text_title_red));
        } else {
            kxViewHolder.titleText.setTextColor(context.getResources().getColor(R.color.text_title));
        }
        kxViewHolder.titleText.setText(title.replace("<br />", "\n"));
    }

    /**
     * 根据本地缓存id 判断快讯状态（展开或者收起）
     *
     * @param kxViewHolder
     * @param autoid
     */
    private void setKxButtonBySp(ViewHolderKx kxViewHolder, String autoid) {
        if (null != SpConstant.getSelectPreferences().getString(autoid, "")
                && !"".equals(SpConstant.getSelectPreferences().getString(autoid, ""))
                && kxViewHolder.itemView.getTag().equals(autoid)) {
            kxViewHolder.titleText.setMaxLines(100);
            kxViewHolder.kxImage.setImageResource(R.mipmap.pack_button);

        } else {
            kxViewHolder.titleText.setMaxLines(3);
            kxViewHolder.kxImage.setImageResource(R.mipmap.open_button);
        }
    }

    /**
     * 设置财经日历的重要新显示
     *
     * @param cjViewHolder
     * @param kxItemCJRL
     */
    private void setCjrlImportance(ViewHolderCjrl cjViewHolder, CjrlDataBean.MsgBean.ContentBean kxItemCJRL) {

        if (kxItemCJRL.getImportance().contains("高")) {
            cjViewHolder.importanceImg.setImageResource(R.mipmap.heigh_icon);
            cjViewHolder.textTitle.setTextColor(context.getResources().getColor(R.color.text_title_red));
            cjViewHolder.textBefore.setTextColor(context.getResources().getColor(R.color.text_title_red));
            cjViewHolder.textForecast.setTextColor(context.getResources().getColor(R.color.text_title_red));
        } else if (kxItemCJRL.getImportance().contains("中")) {
            cjViewHolder.importanceImg.setImageResource(R.mipmap.mind_icon);
            cjViewHolder.textTitle.setTextColor(context.getResources().getColor(R.color.text_title));
            cjViewHolder.textBefore.setTextColor(context.getResources().getColor(R.color.top_time_text));
            cjViewHolder.textForecast.setTextColor(context.getResources().getColor(R.color.top_time_text));
        } else if (kxItemCJRL.getImportance().contains("低")) {
            cjViewHolder.importanceImg.setImageResource(R.mipmap.low_icon);
            cjViewHolder.textTitle.setTextColor(context.getResources().getColor(R.color.text_title));
            cjViewHolder.textBefore.setTextColor(context.getResources().getColor(R.color.top_time_text));
            cjViewHolder.textForecast.setTextColor(context.getResources().getColor(R.color.top_time_text));
        }
        cjViewHolder.textTitle.setText(kxItemCJRL.getState() + " " + kxItemCJRL.getTitle());

        cjViewHolder.textBefore.setText("前值：" + kxItemCJRL.getBefore());
        cjViewHolder.textForecast.setText("预测：" + kxItemCJRL.getForecast());
        cjViewHolder.textReality.setText("公布：" + kxItemCJRL.getReality());

    }


    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        int Type = -1;
        switch (listDatas.get(position).getTag()) {
            case "SECTION":
                Type = 0;
                break;
            case "ITEM":
                switch (listDatas.get(position).getKxValue().getCode()) {
                    case "KUAIXUN":
                        Type = 1;
                        break;
                    case "CJRL":
                        Type = 3;
                        break;
                }
                break;
        }
        return Type;
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * 定义点击每项的接口,此处只实现了点击，没有实现长按
     */
    public interface OnItemClickLitener {
        void OnItemClick(View view, int positon, int type);

        void OnItemLongClick(View view, int position);
    }

    static class ViewHolderKx extends RecyclerView.ViewHolder {
        @BindView(R.id.title_text)
        TextView titleText;
        @BindView(R.id.root_linear)
        LinearLayout rootLinear;
        @BindView(R.id.kx_button)
        RelativeLayout kxButton;
        @BindView(R.id.kx_image)
        ImageView kxImage;
        @BindView(R.id.kx_time)
        TextView kxTiem;


        ViewHolderKx(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    static class ViewHolderNull extends RecyclerView.ViewHolder {

        ViewHolderNull(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderSeparator extends RecyclerView.ViewHolder {
        @BindView(R.id.main_separator_line_tv_date)
        TextView mainSeparatorLineTvDate;
        @BindView(R.id.separator_rl_date)
        LinearLayout separatorRlDate;

        ViewHolderSeparator(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderCjrl extends RecyclerView.ViewHolder {
        @BindView(R.id.cjrl_time)
        TextView cjrlTime;
        @BindView(R.id.cjrl_state)
        RoundImageView cjrlState;
        @BindView(R.id.text_before)
        TextView textBefore;
        @BindView(R.id.text_forecast)
        TextView textForecast;
        @BindView(R.id.text_reality)
        TextView textReality;
        @BindView(R.id.title_text)
        TextView textTitle;
        @BindView(R.id.effects_linear)
        LinearLayout effectsLinear;
        @BindView(R.id.importance_img)
        ImageView importanceImg;
        @BindView(R.id.root_clinear)
        LinearLayout rootClinear;

        ViewHolderCjrl(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
