package com.daemon.mobile.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by 10319 on 03/25/16.
 */
//-9-
//用于保存数据
public class MyApplication extends Application{
    public static String SERVER_PATH;
    //-14-
    public static  SharedPreferences configSp;

    @Override
    public void onCreate() {
        super.onCreate();

        SERVER_PATH = "http://192.168.3.18/MobileManager";

        configSp = getSharedPreferences("config",MODE_PRIVATE);

    }
}
