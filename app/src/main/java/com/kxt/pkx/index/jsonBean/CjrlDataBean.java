package com.kxt.pkx.index.jsonBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class CjrlDataBean implements Serializable {

    /**
     * msg : [{"id":"59626","socre":"1489022996566","code":"CJRL","content":{"title":"2月PPI年率","predicttime":"2017-03-0909: 30: 00","time":"2017-03-0909: 29: 56","state":"中国","before":"6.9%","forecast":"7.7%","reality":"7.8%","importance":"中","effect":"金银人民币石油||","autoid":"273182"}},{"id":"59626","socre":"1489022996566","code":"CJRL","content":{"title":"2月PPI年率","predicttime":"2017-03-0909: 30: 00","time":"2017-03-0909: 29: 56","state":"中国","before":"6.9%","forecast":"7.7%","reality":"7.8%","importance":"中","effect":"金银人民币石油||","autoid":"273182"}}]
     * status : 1
     * cmd : login
     */

    private int status;
    private String cmd;
    private List<MsgBean> msg;

    @Override
    public String toString() {
        return "CjrlDataBean{" +
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
         * id : 59626
         * socre : 1489022996566
         * code : CJRL
         * content : {"title":"2月PPI年率","predicttime":"2017-03-0909: 30: 00","time":"2017-03-0909: 29: 56","state":"中国","before":"6.9%","forecast":"7.7%","reality":"7.8%","importance":"中","effect":"金银人民币石油||","autoid":"273182"}
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
             * title : 2月PPI年率
             * predicttime : 2017-03-0909: 30: 00
             * time : 2017-03-0909: 29: 56
             * state : 中国
             * before : 6.9%
             * forecast : 7.7%
             * reality : 7.8%
             * importance : 中
             * effect : 金银人民币石油||
             * autoid : 273182
             */

            private String title;
            private String predicttime;
            private String time;
            private String state;
            private String before;
            private String forecast;
            private String reality;
            private String importance;
            private String effect;
            private String autoid;
            private boolean select;

            public boolean getSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getPredicttime() {
                return predicttime;
            }

            public void setPredicttime(String predicttime) {
                this.predicttime = predicttime;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getBefore() {
                return before;
            }

            public void setBefore(String before) {
                this.before = before;
            }

            public String getForecast() {
                return forecast;
            }

            public void setForecast(String forecast) {
                this.forecast = forecast;
            }

            public String getReality() {
                return reality;
            }

            public void setReality(String reality) {
                this.reality = reality;
            }

            public String getImportance() {
                return importance;
            }

            public void setImportance(String importance) {
                this.importance = importance;
            }

            public String getEffect() {
                return effect;
            }

            public void setEffect(String effect) {
                this.effect = effect;
            }

            public String getAutoid() {
                return autoid;
            }

            public void setAutoid(String autoid) {
                this.autoid = autoid;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "title='" + title + '\'' +
                        ", predicttime='" + predicttime + '\'' +
                        ", time='" + time + '\'' +
                        ", state='" + state + '\'' +
                        ", before='" + before + '\'' +
                        ", forecast='" + forecast + '\'' +
                        ", reality='" + reality + '\'' +
                        ", importance='" + importance + '\'' +
                        ", effect='" + effect + '\'' +
                        ", autoid='" + autoid + '\'' +
                        '}';
            }
        }
    }
}
