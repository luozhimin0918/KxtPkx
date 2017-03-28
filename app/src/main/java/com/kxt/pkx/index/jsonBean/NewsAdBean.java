package com.kxt.pkx.index.jsonBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class NewsAdBean implements Serializable {


    /**
     * status : 1
     * msg : 获取成功
     * aud :
     * data : [{"id":"26097","title":"","weburl":"","url":""}]
     */

    private String status;
    private String msg;
    private String aud;
    private List<DataBean> data;

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

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements  Serializable{
        /**
         * id : 26097
         * title :
         * weburl :
         * url :
         */

        private String id;
        private String title;
        private String weburl;
        private String url;
        private String thumb;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getWeburl() {
            return weburl;
        }

        public void setWeburl(String weburl) {
            this.weburl = weburl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", weburl='" + weburl + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsAdBean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", aud='" + aud + '\'' +
                ", data=" + data +
                '}';
    }
}
