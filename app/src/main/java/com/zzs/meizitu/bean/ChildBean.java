package com.zzs.meizitu.bean;

import java.util.List;

/**
 * @author zzstar
 * @data 2018/1/25
 */

public class ChildBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 42
         * srcchildid : 1
         * srclist : http://i.meizitu.net/2018/01/24d01.jpg
         */

        private int id;
        private int srcchildid;
        private String srclist;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSrcchildid() {
            return srcchildid;
        }

        public void setSrcchildid(int srcchildid) {
            this.srcchildid = srcchildid;
        }

        public String getSrclist() {
            return srclist;
        }

        public void setSrclist(String srclist) {
            this.srclist = srclist;
        }
    }
}
