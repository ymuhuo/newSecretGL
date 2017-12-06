package com.bmw.secretgl.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by admin on 2017/4/18.
 */

public class Place extends BmobObject {
    private double jingdu;
    private double weidu;
    private String device;
    private String IMEI;
    private BmobDate createAt;
    private String time;

    public Place(double jingdu, double weidu, String device, String IMEI) {
        this.jingdu = jingdu;
        this.weidu = weidu;
        this.device = device;
        this.IMEI = IMEI;
    }


    public Place(double jingdu, double weidu, String device, String IMEI,String time) {
        this.jingdu = jingdu;
        this.weidu = weidu;
        this.device = device;
        this.IMEI = IMEI;
        this.time = time;
    }

    public Place() {
    }

    public double getJingdu() {
        return jingdu;
    }

    public void setJingdu(double jingdu) {
        this.jingdu = jingdu;
    }

    public double getWeidu() {
        return weidu;
    }

    public void setWeidu(double weidu) {
        this.weidu = weidu;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public BmobDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(BmobDate createAt) {
        this.createAt = createAt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
