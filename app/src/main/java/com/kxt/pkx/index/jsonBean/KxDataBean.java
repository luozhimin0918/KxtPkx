package com.kxt.pkx.index.jsonBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class KxDataBean implements Serializable {


    /**
     * msg : [{"id":"59631","socre":"1489023583252","code":"KUAIXUN","content":{"title":"【行情】现货金一度触及2月1日以来低位1205.05美元；现报1206.97美元，下挫1.35美元","time":"2017-03-09 09:39:43","importance":"高","autoid":"194710"}},{"id":"59626","socre":"1489022996566","code":"CJRL","content":{"title":"2月PPI年率","predicttime":"2017-03-0909: 30: 00","time":"2017-03-0909: 29: 56","state":"中国","before":"6.9%","forecast":"7.7%","reality":"7.8%","importance":"中","effect":"金银人民币石油||","autoid":"273182"}},{"id":"59602","socre":"1489020625416","code":"KUAIXUN","content":{"title":"【美国WTI原油上涨，受OPEC减产协议高执行率所支撑】<br/>美国WTI原油价格周四在亚洲盘小幅上涨，石油输出国组织(OPEC)对减产协议的高执行率带来支撑，尽管美国原油库存创纪录高位打压市场人气。美油现报每桶50.53美元，上涨0.5%，上日重跌5.38%至每桶50.28美元，触及12月以来最低水准。布伦特原油到9点才开盘。","time":"2017-03-0908: 50: 25","importance":"低","autoid":"194671"}}]
     * status : 1
     * cmd : login
     */

    private int status;
    private String cmd;
    private List<MsgBean> msg;

    @Override
    public String toString() {
        return "KxDataBean{" +
                "status=" + status +
                ", cmd='" + cmd + '\'' +
                ", msg=" + msg +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public List<MsgBean> getMsg() {
        return msg;
    }

    public void setMsg(List<MsgBean> msg) {
        this.msg = msg;
    }

    public static class MsgBean implements Serializable {
        /**
         * id : 59631
         * socre : 1489023583252
         * code : KUAIXUN
         * content : {"title":"【行情】现货金一度触及2月1日以来低位1205.05美元；现报1206.97美元，下挫1.35美元","time":"2017-03-09 09:39:43","importance":"高","autoid":"194710"}
         */

        private String id;
        private String socre;
        private String code;
        private ContentBean content;

        @Override
        public String toString() {
            return "MsgBean{" +
                    "id='" + id + '\'' +
                    ", socre='" + socre + '\'' +
                    ", code='" + code + '\'' +
                    ", content=" + content +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSocre() {
            return socre;
        }

        public void setSocre(String socre) {
            this.socre = socre;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean implements Serializable {
            /**
             * title : 【行情】现货金一度触及2月1日以来低位1205.05美元；现报1206.97美元，下挫1.35美元
             * time : 2017-03-09 09:39:43
             * importance : 高
             * autoid : 194710
             */

            private String title;
            private String time;
            private String importance;
            private String autoid;
            private String image;
            private String image_pos;
            private String description;
            private UrlEntity url;

            public boolean getSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            private boolean select;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "title='" + title + '\'' +
                        ", time='" + time + '\'' +
                        ", importance='" + importance + '\'' +
                        ", autoid='" + autoid + '\'' +
                        ", image='" + image + '\'' +
                        ", image_pos='" + image_pos + '\'' +
                        ", description='" + description + '\'' +
                        '}';
            }

            public UrlEntity getUrl() {
                return url;
            }

            public void setUrl(UrlEntity url) {
                this.url = url;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getImage_pos() {
                return image_pos;
            }

            public void setImage_pos(String image_pos) {
                this.image_pos = image_pos;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getImportance() {
                return importance;
            }

            public void setImportance(String importance) {
                this.importance = importance;
            }

            public String getAutoid() {
                return autoid;
            }

            public void setAutoid(String autoid) {
                this.autoid = autoid;
            }
        }
    }

    public static class UrlEntity {
        private String c;
        private String i;
        private String u;

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getI() {
            return i;
        }

        public void setI(String i) {
            this.i = i;
        }

        public String getU() {
            return u;
        }

        public void setU(String u) {
            this.u = u;
        }
    }
}
