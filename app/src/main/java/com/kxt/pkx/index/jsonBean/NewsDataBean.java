package com.kxt.pkx.index.jsonBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class NewsDataBean implements Serializable {


    /**
     * aud : http: //adi.kuaixun56.com
     * status : 1
     * msg : 获取成功
     * data : [{"id":"26160","title":"午评：沪指延续震荡格局白酒、港口板块异军突起","url":"http: //appapi.kxt.com/page/article_app/id/26160","weburl":"http: //appapi.kxt.com/page/article/id/26160","addtime":"1490068272","thumb":"http: //img.kuaixun360.com/Uploads/Picture/2017-03-21/58d0a32c97c7d.jpg","description":"周二，两市早盘低开，随后小幅走高后回落，之后小幅整理再度上冲。盘面上，酿酒、园林工程、水利建设、PPP、高送转等板块涨幅居前；丝绸之路、贵金属、水泥建材、钒电池...","category_id":"19","tags":["股市"],"dianzan":"35"}]
     */

    private String aud;
    private String status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 26160
         * title : 午评：沪指延续震荡格局白酒、港口板块异军突起
         * url : http: //appapi.kxt.com/page/article_app/id/26160
         * weburl : http: //appapi.kxt.com/page/article/id/26160
         * addtime : 1490068272
         * thumb : http: //img.kuaixun360.com/Uploads/Picture/2017-03-21/58d0a32c97c7d.jpg
         * description : 周二，两市早盘低开，随后小幅走高后回落，之后小幅整理再度上冲。盘面上，酿酒、园林工程、水利建设、PPP、高送转等板块涨幅居前；丝绸之路、贵金属、水泥建材、钒电池...
         * category_id : 19
         * tags : ["股市"]
         * dianzan : 35
         */

        private String id;
        private String title;
        private String url;
        private String weburl;
        private String addtime;
        private String thumb;
        private String description;
        private String category_id;
        private String dianzan;
        private List<String> tags;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWeburl() {
            return weburl;
        }

        public void setWeburl(String weburl) {
            this.weburl = weburl;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getDianzan() {
            return dianzan;
        }

        public void setDianzan(String dianzan) {
            this.dianzan = dianzan;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", weburl='" + weburl + '\'' +
                    ", addtime='" + addtime + '\'' +
                    ", thumb='" + thumb + '\'' +
                    ", description='" + description + '\'' +
                    ", category_id='" + category_id + '\'' +
                    ", dianzan='" + dianzan + '\'' +
                    ", tags=" + tags +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsDataBean{" +
                "aud='" + aud + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
