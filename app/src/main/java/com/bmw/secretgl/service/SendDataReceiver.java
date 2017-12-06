package com.bmw.secretgl.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bmw.secretgl.BaseApplication;
import com.bmw.secretgl.Constant;
import com.bmw.secretgl.sendData.BmobHelper;

import cn.bmob.v3.Bmob;

public class SendDataReceiver extends BroadcastReceiver {
//    private BmobHelper helper;

    public SendDataReceiver() {
//        if (helper == null)
//            helper = new BmobHelper(BaseApplication.context());
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        double jingdu = intent.getDoubleExtra(Constant.KEY_LOCATION_INFO_JINGDU, -11);
        double weidu = intent.getDoubleExtra(Constant.KEY_LOCATION_INFO_WEIDU, -11);

        if(jingdu != -11 && weidu != -11){
//            helper.savaData(jingdu,weidu);
        }

    }
}
