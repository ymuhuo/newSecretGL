package com.bmw.secretgl.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by admin on 2017/8/30.
 */

public class FileUtils {

    public static void saveDataToFile(String str){
        File file = new File(getSDPath(),"/secretLog.txt");
        writeStringToFile(file,true,str);

    }


    public static boolean writeStringToFile(File file,boolean isAddToEnd,String string){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(file,isAddToEnd);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(string,0,string.length());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            fileWriter.flush();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static String getSDPath() {

        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            return Environment.getExternalStorageDirectory().toString();
        } else
            return Environment.getDownloadCacheDirectory().toString();
    }
}
