package com.kxt.pkx.index.jsonBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class PinnedSectionBean implements Serializable {
    private KxHisItemBen kxValue;
    private String tag;
    private int sort;
    private boolean select;
    private boolean showJy = true;
    private boolean showWh = true;
    private boolean showSy = true;

    public boolean isShowWh() {
        return showWh;
    }

    public void setShowWh(boolean showWh) {
        this.showWh = showWh;
    }

    public boolean isShowJy() {
        return showJy;
    }

    public void setShowJy(boolean showJy) {
        this.showJy = showJy;
    }

    public boolean isShowSy() {
        return showSy;
    }

    public void setShowSy(boolean showSy) {
        this.showSy = showSy;
    }


    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }


    public PinnedSectionBean(String tag, KxHisItemBen kxValue, int sort) {
        this.kxValue = kxValue;
        this.tag = tag;
        this.sort = sort;
    }

    public KxHisItemBen getKxValue() {
        return kxValue;
    }

    @Override
    public String toString() {
        return "PinnedSectionBean{" +
                "kxValue=" + kxValue +
                ", tag='" + tag + '\'' +
                ", sort=" + sort +
                '}';
    }

    public void setKxValue(KxHisItemBen kxValue) {
        this.kxValue = kxValue;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
