package com.bmw.secretgl.service;

/**
 * Created by yMuhuo on 2016/12/7.
 */

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.bmw.secretgl.BaseApplication;
import com.bmw.secretgl.location.BaiduMapLocateImpl;
import com.bmw.secretgl.location.BaiduMapLocatePresenter;

import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * <p>Created by Administrator</p>
 * <p/>
 *
 */
public class EnvelopeService extends AccessibilityService {

    static final String TAG = "服务";
    private BaiduMapLocatePresenter baiduMapLocate;
    private String mKeyWord;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();

        Log.d(TAG, "事件---->" + event);

        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                System.out.println("d点击");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                //界面文字改动
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                BaseApplication.toast("word wait ");
                List<CharSequence> texts = event.getText();
                if (!texts.isEmpty()) {
                    for (CharSequence t : texts) {
                        String text = String.valueOf(t);
                        BaseApplication.toast("word: "+text);
                        if (text.contains(mKeyWord)) {

                            break;
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                BaseApplication.toast("claseName = "+event.getClassName());
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }





    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "服务开启成功！", Toast.LENGTH_SHORT).show();
        Bmob.initialize(this, "a901abc1decbde31b3a59e29038e8e13");


        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED|AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.packageNames = new String[]{"com.tencent.mm","com.tencent.mobileqq"};
        serviceInfo.notificationTimeout=100;
        setServiceInfo(serviceInfo);

//        baiduMapLocate = new BaiduMapLocateImpl(this);
//        baiduMapLocate.startLocate();
    }

}