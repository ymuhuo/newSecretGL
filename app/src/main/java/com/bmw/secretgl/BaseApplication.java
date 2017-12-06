package com.bmw.secretgl;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.bmw.secretgl.location.LocationService;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yMuhuo on 2016/12/12.
 */
public class BaseApplication extends Application {

    static Context mContext;
    static Resources mResources;
    private static final String PREF_PEEK2S = "PREF_PEEK2S";
    private static long last_duration_time;
    private static String last_toast_msg;
    private List<Activity> oList;
    private static BaseApplication application;

    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mResources = mContext.getResources();
        oList = new ArrayList<Activity>();
        initBaiduMapLocate();
        application = (BaseApplication) mContext;
    }

    public static BaseApplication getApplication(){
        return application;
    }

    private void initBaiduMapLocate(){
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }

    public synchronized static BaseApplication context(){
        return (BaseApplication) mContext;
    }

    public static Resources resources(){
        return mResources;
    }

    public static SharedPreferences getSharedPreferences(){
        return context().getSharedPreferences(PREF_PEEK2S, Context.MODE_PRIVATE);
    }

    public static void toast(String msg){
        toast(msg, Toast.LENGTH_SHORT);
    }

    public static void toast(String msg, int duration){
        if(msg != null && !msg.equalsIgnoreCase("")){
            long current_time = System.currentTimeMillis();
            if( !msg.equalsIgnoreCase(last_toast_msg) || current_time - last_duration_time>2000){
                View view = LayoutInflater.from(context()).inflate(R.layout.toast_view,null);
                TextView textView = (TextView) view.findViewById(R.id.toast_tv);
                textView.setText(msg);
                Toast toast = new Toast(context());
                toast.setView(view);
                toast.setDuration(duration);
                toast.show();

                last_duration_time = System.currentTimeMillis();
                last_toast_msg = msg;
            }
        }
    }


    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
//判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
        System.exit(0);
    }

}
