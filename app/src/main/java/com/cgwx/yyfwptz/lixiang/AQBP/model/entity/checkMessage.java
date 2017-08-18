package com.cgwx.yyfwptz.lixiang.AQBP.model.entity;

/**
 * Created by yyfwptz on 2017/7/7.
 */

public class checkMessage {
    String meta;
    policeInfo policeInfo;

    public checkMessage(String meta, com.cgwx.yyfwptz.lixiang.AQBP.model.entity.policeInfo policeInfo) {
        this.meta = meta;
        this.policeInfo = policeInfo;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public com.cgwx.yyfwptz.lixiang.AQBP.model.entity.policeInfo getPoliceInfo() {
        return policeInfo;
    }

    public void setPoliceInfo(com.cgwx.yyfwptz.lixiang.AQBP.model.entity.policeInfo policeInfo) {
        this.policeInfo = policeInfo;
    }
}
