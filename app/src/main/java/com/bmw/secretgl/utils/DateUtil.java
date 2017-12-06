package com.bmw.secretgl.utils;

import java.text.SimpleDateFormat;

/**
 * Created by admin on 2017/8/28.
 */

public class DateUtil {

    public static String getTimeHanZi(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    public static String getTime_desc(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }
}
