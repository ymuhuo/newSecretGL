package com.bmw.secretgl.notifyService;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.bmw.secretgl.Constant;
import com.bmw.secretgl.utils.DateUtil;
import com.bmw.secretgl.utils.FileUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/30.
 */

@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyNotificationListenerService extends NotificationListenerService {
    private final String TAG = getClass().getSimpleName();


    public static void startService(Context context){
        Intent intent = new Intent(context,MyNotificationListenerService.class);
        context.startService(intent);
    }

    public static void stopService(Context context){
        Intent intent = new Intent(context,MyNotificationListenerService.class);
        context.stopService(intent);
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "onNotificationPosted: "+ sbn);
        if (!"com.tencent.mm".equals(sbn.getPackageName()))
            return;
        Notification notification = sbn.getNotification();
        if (notification == null)
            return;
        PendingIntent pendingIntent = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Bundle bundle = notification.extras;
            if (bundle != null) {
                String title = bundle.getString(Notification.EXTRA_TITLE, "");
                String content = bundle.getString(Notification.EXTRA_TEXT, "");
                Log.d(TAG, "onNotificationPosted: "+content);

                if (!TextUtils.isEmpty(content)) {
                    if(content.contains(Constant.ENVELOPE_TEXT_KEY)){
                    pendingIntent = notification.contentIntent;
                    }else{
                        for (String str : Constant.KEY_WORDS) {
                            if (content.contains(str)) {
                                Log.d(TAG, "onAccessibilityEvent-log: " + content);
                                FileUtils.saveDataToFile(DateUtil.getTime_desc() + "--" + content);
                                break;
                            }
                        }
                    }
                }
            }

        } else {
            List<String> textList = getText(notification);
            if (textList != null && textList.size() > 0) {
                for (String text : textList) {
                    Log.d(TAG, "onNotificationPosted: "+text);
                    if (!TextUtils.isEmpty(text) && text.contains(Constant.ENVELOPE_TEXT_KEY)) {
                        pendingIntent = notification.contentIntent;
                    }
                }
            }

        }
        try {
            if(pendingIntent !=null){
                pendingIntent.send();
            }
        }catch (PendingIntent.CanceledException e){
            e.printStackTrace();
        }

    }


    public List<String> getText(Notification notification) {
        if (null == notification) {
            return null;
        }
        RemoteViews views = notification.bigContentView;
        if (views == null) {
            views = notification.contentView;
        }
        if (views == null) {
            return null;
        }

        // Use reflection to examine the m_actions member of the given RemoteViews object.
        // It's not pretty, but it works.
        List<String> text = new ArrayList<>();
        try {
            Field field = views.getClass().getDeclaredField("mActions");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(views);
            // Find the setText() and setTime() reflection actions
            for (Parcelable p : actions) {
                Parcel parcel = Parcel.obtain();
                p.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);
                // The tag tells which type of action it is (2 is ReflectionAction, from the source)
                int tag = parcel.readInt();
                if (tag != 2) continue;
                // View ID
                parcel.readInt();
                String methodName = parcel.readString();
                if (null == methodName) {
                    continue;
                } else if (methodName.equals("setText")) {
                    // Parameter type (10 = Character Sequence)
                    parcel.readInt();
                    // Store the actual string
                    String t = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString().trim();
                    text.add(t);
                }
                parcel.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

}
