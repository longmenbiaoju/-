package com.zzs.meizitu.bean;

import java.util.List;

/**
 * @author zzstar
 * @data 2018/2/10
 */

public class CheckBean {


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
         * versioncode : 2
         * content : 修复了若干BUGA表和B表，创建触发器使当A表插入数据后B表也同步插入数据。其中B表插入数据的字段需要同A表中的字段相对应
         * versions : 2.0.1
         * volume : 2.5MB
         */

        private int id;
        private int versioncode;
        private String content;
        private String versions;
        private String volume;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVersioncode() {
            return versioncode;
        }

        public void setVersioncode(int versioncode) {
            this.versioncode = versioncode;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVersions() {
            return versions;
        }

        public void setVersions(String versions) {
            this.versions = versions;
        }

        public String getVolume() {
            return volume;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }
    }
}
