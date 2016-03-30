package com.daemon.mobile.mobilemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.mobile.application.MyApplication;
import com.daemon.mobile.utils.HttpUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends ActionBarActivity {

    private static final String TAG = "SplashActivity";

    private String currentVersion;
    private ProgressBar pb_splash_download;

    private static final int MSG_SERVER_OK = 1;
    private static final int MSG_SERVER_ERROR = -1;
    private static final int MSG_URL_ERROR = -2;
    private static final int MSG_IO_ERROR = -3;
    private static final int MSG_JSON_ERROR = -4;
    private static final int MSG_TIMEOUT_WAIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar supportActionBar =  getSupportActionBar();
        supportActionBar.hide();

        TextView tv_Splash_Version = (TextView) findViewById(R.id.tv_Splash_Version);
        pb_splash_download = (ProgressBar) findViewById(R.id.pb_Splash_Download);

        currentVersion = getVersionName();//获取当前版本号
        tv_Splash_Version.setText("Version:"+currentVersion);


        //-14-
        if(MyApplication.configSp.getBoolean("autoUpdate",false)) {
            getNewVersion();//获取服务器最新版本
        }else {
            waitAwhile();
        }

        initAnimation();//初始化动画
    }

    private void initAnimation() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_splash_root);
        //创建Alpha动画
        AlphaAnimation aa =  new AlphaAnimation(0.0f,1.0f);
        //动画时间
        aa.setDuration(3000);
        //让页面停留在最后状态
        aa.setFillAfter(true);
        //给指定控件设置动画
        rl.setAnimation(aa);
    }

    private void waitAwhile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
              /*  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enterHome();
                    }
                });//因为已有handler，暂不用此方法*/

                Message msg = mHandler.obtainMessage();
                msg.what = MSG_TIMEOUT_WAIT;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    //-1-获取本地应用版本
    private String getVersionName(){
        String versionName="";
        int versionCode = -1;
        //管理当前手机的应用
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo= manager.getPackageInfo(getPackageName(), 0);
            versionName= packageInfo.versionName;
            versionCode = packageInfo.versionCode;


            Log.i(TAG,"##versionName:"+versionName+",##versionCode:"+versionCode);//versionName:1.0,versionCode:1

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    //-4-
     Handler mHandler = new Handler(){

        @Override
        public  void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what){
                case MSG_SERVER_OK:
                    String[] info = (String[]) msg.obj;

                    String versionInfo = info[0];
                    String desc = info[1];
                    String down_url = info[2];

                    float  lv = Float.parseFloat(versionInfo);
                    float cv = Float.parseFloat(currentVersion);
                    if(lv>cv){//最新版本高于当前版本
                        versionUpdate(info);
                    }
                    break;
                case MSG_SERVER_ERROR:
                     Toast.makeText(SplashActivity.this,"服务器连接失败，代码:"+MSG_SERVER_ERROR,Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_URL_ERROR:
                    Toast.makeText(SplashActivity.this,"异常代码:"+MSG_URL_ERROR,Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_IO_ERROR:
                    Toast.makeText(SplashActivity.this,"异常代码:"+MSG_IO_ERROR,Toast.LENGTH_SHORT).show();
                    waitAwhile();
                    break;
                case MSG_JSON_ERROR:
                    Toast.makeText(SplashActivity.this,"异常代码:"+MSG_JSON_ERROR,Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_TIMEOUT_WAIT:
                    enterHome();

            }


        }
    };
    //-5-
    private void versionUpdate(final String[] info) {
       new AlertDialog.Builder(this)//对话框的上下文是Activity的class
               //.setCancelable(false)//方式1：弹框时，用户按回退键无效。但用户体验欠佳
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //点击返回键时，进入主页面
                        enterHome();
                    }
                })
               .setTitle("发现有新版本")
               .setMessage(info[1])
               .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //下载
                       AsyncHttpClient client = new AsyncHttpClient();
                       pb_splash_download.setVisibility(View.VISIBLE);
                       client.get(MyApplication.SERVER_PATH + info[2], new AsyncHttpResponseHandler() {
                           @Override
                           public void onSuccess(int i, Header[] headers, byte[] bytes) {
                               try {
                                   File file = new File(Environment.getExternalStorageDirectory()
                                           .getAbsolutePath() + "/mobile.apk");//囧，竟然保存在data/media目录下
                                   FileOutputStream os = new FileOutputStream(file);
                                   os.write(bytes);
                                   os.close();

                                   installApp(file);

                               } catch (FileNotFoundException e) {
                                   e.printStackTrace();
                                   enterHome();
                               } catch (IOException e) {
                                   e.printStackTrace();
                                   enterHome();
                               }


                           }

                           @Override
                           public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                               Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                               //进入到主页面
                               enterHome();
                           }

                           @Override
                           public void onProgress(long bytesWritten, long totalSize) {
                               super.onProgress(bytesWritten, totalSize);
                               pb_splash_download.setMax((int) totalSize);
                               pb_splash_download.setProgress((int) bytesWritten);
                           }
                       });


                   }
               })
               .setNegativeButton("暂不", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //进入主页面
                       enterHome();
                   }
               }).show();



        Log.i(TAG, "versionUpdate");
    }
    //-6-安装应用
    private void installApp(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        //startActivity(intent);
        //为区分是由哪个页面返回,需增加一个resultCode来区别开
        //作用是：当用户取消安装后，执行onActivityResult里的方法
        startActivityForResult(intent, 100);

    }

    //-7-
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED){
            //进入主页面
            enterHome();

        }

    }
    //-8-
    private void enterHome() {
        startActivity(new Intent(this,HomeActivity.class));
        finish();//关闭splash页面本身，以便在主页面时，用户按返回键时返回splash页面
    }

    //-2-获取服务器最新应用版本与下载地址
    private void getNewVersion(){
        //子线程中访问服务器
        new Thread(){
            @Override
            public void run() {//匿名内部类方式
                String path = MyApplication.SERVER_PATH+"/version.json";
                Message msg = mHandler.obtainMessage();
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("GET");
                    conn.connect();
                    int responseCode =  conn.getResponseCode();
                    Log.i("getNewVersion",url.toString());
                    if(responseCode == 200){
                        InputStream in =  conn.getInputStream();
                        String text = HttpUtils.getTextFromStream(in);//读取json数据

                        //解析json数据
                        //把数据封装成json对象
                        JSONObject obj = new JSONObject(text);

                        String latestVersion = obj.getString("version");
                        String downLoadUrl = obj.getString("download_url");
                        String description = obj.getString("description");

                        String[] versionInfo = {latestVersion,description,downLoadUrl};
                        Log.v(TAG,"####latestVersion:"+latestVersion+",####downLoadUrl:"+downLoadUrl);

                        

                        msg.what = MSG_SERVER_OK;
                        msg.obj = versionInfo;

                    }else{
                        if(responseCode==500){
                            msg.what = MSG_SERVER_ERROR;

                        }

                    }

                    conn.disconnect();//断开连接

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = MSG_URL_ERROR;
                    Log.i("getNewVersion",msg.what+"");
                } catch (IOException e) {//网络连接不成功
                    e.printStackTrace();
                    msg.what = MSG_IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = MSG_JSON_ERROR;
                }finally {
                    mHandler.sendMessage(msg);
                }


            }
        }.start();



    }
}
