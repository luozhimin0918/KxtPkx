package com.kxt.pkx.index.jsonBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class UpdateBean implements Serializable {


    /**
     * aud : http://adi.kuaixun56.com
     * status : 1
     * msg : 获取成功
     * data : {"Version":"1.1.0","Content":"1、修复了某些BUG|2、新加了某些功能!","Size":"11M","DownloadUrl":"itms://itunes.apple.com/gb/app/yi-dong-cai-bian/id391945719?mt=8"}
     */

    private String aud;
    private String status;
    private String msg;
    private DataBean data;

    @Override
    public String toString() {
        return "UpdateBean{" +
                "aud='" + aud + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        @Override
        public String toString() {
            return "DataBean{" +
                    "Version='" + Version + '\'' +
                    ", Content='" + Content + '\'' +
                    ", Size='" + Size + '\'' +
                    ", DownloadUrl='" + DownloadUrl + '\'' +
                    '}';
        }

        /**
         * Version : 1.1.0
         * Content : 1、修复了某些BUG|2、新加了某些功能!
         * Size : 11M
         * DownloadUrl : itms://itunes.apple.com/gb/app/yi-dong-cai-bian/id391945719?mt=8
         */

        private String Version;
        private String Content;
        private String Size;
        private String DownloadUrl;

        public String getVersion() {
            return Version;
        }

        public void setVersion(String Version) {
            this.Version = Version;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public String getSize() {
            return Size;
        }

        public void setSize(String Size) {
            this.Size = Size;
        }

        public String getDownloadUrl() {
            return DownloadUrl;
        }

        public void setDownloadUrl(String DownloadUrl) {
            this.DownloadUrl = DownloadUrl;
        }
    }
}
