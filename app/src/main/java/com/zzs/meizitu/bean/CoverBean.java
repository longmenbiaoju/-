package com.zzs.meizitu.bean;

import java.util.List;

/**
 * @author zzstar
 * @data 2018/1/25
 */

public class CoverBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * srctitle : 这眼神太勾人了 爆乳女王恩一红色睡袍配黑丝气质不凡
         * srcid : 0
         * coversrc : http://i.meizitu.net/2018/01/24c01.jpg
         */

        private int id;
        private String srctitle;
        private int srcid;
        private String coversrc;
        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSrctitle() {
            return srctitle;
        }

        public void setSrctitle(String srctitle) {
            this.srctitle = srctitle;
        }

        public int getSrcid() {
            return srcid;
        }

        public void setSrcid(int srcid) {
            this.srcid = srcid;
        }

        public String getCoversrc() {
            return coversrc;
        }

        public void setCoversrc(String coversrc) {
            this.coversrc = coversrc;
        }
    }
}
