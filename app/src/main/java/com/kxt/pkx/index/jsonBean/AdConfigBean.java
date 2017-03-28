package com.kxt.pkx.index.jsonBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class AdConfigBean implements Serializable {

    /**
     * aud : http://adi.kuaixun56.com
     * status : 1
     * msg : 获取成功
     * data : {"Advertisement":{"Title":"测试","Type":"normal","ImageUrl":"http://pic.qiantucdn.com/58pic/11/79/85/13t58PICsap.jpg","Url":"https://www.baidu.com"}}
     */

    private String aud;
    private String status;
    private String msg;
    private DataBean data;

    @Override
    public String toString() {
        return "AdConfigBean{" +
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
                    "Advertisement=" + Advertisement +
                    '}';
        }

        /**
         * Advertisement : {"Title":"测试","Type":"normal","ImageUrl":"http://pic.qiantucdn.com/58pic/11/79/85/13t58PICsap.jpg","Url":"https://www.baidu.com"}
         */

        private AdvertisementBean Advertisement;

        public AdvertisementBean getAdvertisement() {
            return Advertisement;
        }

        public void setAdvertisement(AdvertisementBean Advertisement) {
            this.Advertisement = Advertisement;
        }

        public static class AdvertisementBean implements Serializable {
            /**
             * Title : 测试
             * Type : normal
             * ImageUrl : http://pic.qiantucdn.com/58pic/11/79/85/13t58PICsap.jpg
             * Url : https://www.baidu.com
             */

            private String Title;
            private String Type;
            private String ImageUrl;
            private String Url;

            @Override
            public String toString() {
                return "AdvertisementBean{" +
                        "Title='" + Title + '\'' +
                        ", Type='" + Type + '\'' +
                        ", ImageUrl='" + ImageUrl + '\'' +
                        ", Url='" + Url + '\'' +
                        '}';
            }

            public String getTitle() {
                return Title;
            }

            public void setTitle(String Title) {
                this.Title = Title;
            }

            public String getType() {
                return Type;
            }

            public void setType(String Type) {
                this.Type = Type;
            }

            public String getImageUrl() {
                return ImageUrl;
            }

            public void setImageUrl(String ImageUrl) {
                this.ImageUrl = ImageUrl;
            }

            public String getUrl() {
                return Url;
            }

            public void setUrl(String Url) {
                this.Url = Url;
            }
        }
    }
}
