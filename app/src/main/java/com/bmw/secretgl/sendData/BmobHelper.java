package com.bmw.secretgl.sendData;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bmw.secretgl.model.Place;
import com.bmw.secretgl.utils.DateUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by admin on 2017/2/15.
 */

public class BmobHelper {
    Context context;



    public void upload(String filePath) {
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Log.i("aaa", "上传文件成功:" + bmobFile.getFileUrl());
                    String url = bmobFile.getFileUrl();
                    if(listener != null)
                        listener.doneGetUrl(url);
                } else {
//                    toast("上传文件失败：" + e.getMessage());
                    Bmob.initialize(context, "a901abc1decbde31b3a59e29038e8e13");
                }
                if(listener!= null)
                    listener.doneFaile();
            }

            @Override
            public void onProgress(final Integer value) {
                /*tv.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("正在上传： " + value + "%已上传");
                    }
                });*/

                if(listener!= null)
                    listener.loading(value);

                // 返回的上传进度（百分比）
            }
        });
    }

    public interface OnFileUploadListener{
        void doneGetUrl(String url);
        void doneFaile();
        void loading(Integer value);
    }

    OnFileUploadListener listener;

    public void setOnFileUploadListener(OnFileUploadListener listener){
        this.listener = listener;
    }


    public void updateData( double jingdu,double weidu, String device, String imei) {
        Place updateInfo = new Place(jingdu,weidu,device,imei);

        updateInfo.update("b7541cb53d", new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "更新成功");
                } else {
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    public void savaData( double jingdu,double weidu, String device, String imei) {

        String time = DateUtil.getTimeHanZi();

        Place updateInfo = new Place(jingdu,weidu,device,imei,time);

        updateInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
//                    toast("创建数据成功：" + s);
                } else {
//                    toast("bmob失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public void getData() {

        BmobQuery query = new BmobQuery("UpdateInfo");
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    log(ary.toString());
                    try {
                        JSONObject json = ary.getJSONObject(0);
                        Gson g = new Gson();
                        Place u = g.fromJson(json.toString(), Place.class);
                        log(u.toString());

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                } else {
                    log(e.toString());
                }
            }
        });

/*
BmobQuery query =new BmobQuery("Person");
		query.addWhereEqualTo("age", 25);
		query.setLimit(2);
		query.order("createdAt");
		query.findObjectsByTable(new QueryListener<JSONArray>() {
			@Override
			public void done(JSONArray ary, BmobException e) {
				if(e==null){
					log(ary.toString());
				}else{
					loge(e);
				}
			}
		});
*/
    }


    public BmobHelper(Context context) {
        this.context = context;
    }

    public void downloadFile() {
//        DownLoadFile downLoadFile = new DownLoadFile("temp.jpg","","http://bmob-cdn-7943.b0.upaiyun.com/2016/12/06/5c0d6402c113430dbace112f12ea3b2b.jpg");
        BmobFile bomfile = new BmobFile("temp.jpg", "", "http://bmob-cdn-7943.b0.upaiyun.com/2016/12/06/5c0d6402c113430dbace112f12ea3b2b.jpg");
        downloadFile(bomfile);
    }

    private void downloadFile(BmobFile file) {
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {
                toast("开始下载...");
            }

            @Override
            public void done(String savePath, BmobException e) {
                if (e == null) {
//                    toast("下载成功,保存路径:" + savePath);
                } else {
//                    toast("下载失败：" + e.getErrorCode() + "," + e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob", "下载进度：" + value + "," + newworkSpeed);
            }

        });
    }

    public void getDataList(final int limit, String date, final findDataListener listener){
        BmobQuery<Place> query = new BmobQuery<Place>();
        query.addWhereEqualTo("time",date);
        query.setLimit(limit);
        query.findObjects(new FindListener<Place>() {
            @Override
            public void done(List<Place> list, BmobException e) {
                if(e==null && listener!=null){
                    listener.done(list);
                }else {
                    toast("getDataError : "+ e.getMessage()+" code = "+e.getErrorCode());
                }
            }
        });
    }

    public void delete(final int limit, String choose){
        getDataList(limit, choose, new findDataListener() {
            @Override
            public void done(List<Place> list) {
                List<BmobObject> places = new ArrayList<BmobObject>();
                for(Place place:list){
                    places.add(place);
                }
                new BmobBatch().deleteBatch(places).doBatch(new QueryListListener<BatchResult>() {

                    @Override
                    public void done(List<BatchResult> o, BmobException e) {
                        if(e==null){
                            for(int i=0;i<o.size();i++){
                                BatchResult result = o.get(i);
                                BmobException ex =result.getError();
                                if(ex==null){
//                                    toast("第"+i+"个数据批量删除成功");
                                }else{
//                                    toast("第"+i+"个数据批量删除失败："+ex.getMessage()+","+ex.getErrorCode());
                                }
                            }
                        }else{
//                            toast("bmob 失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });

    }

    private void toast(String msg){
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show();
    }

    private void log(String msg){
        Log.i("Bmob",msg);
    }


    public interface findDataListener{
        void done(List<Place> list);
    }


}
