package com.daemon.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;

import com.daemon.mobile.utils.MyConstants;
import com.daemon.mobile.utils.SpTools;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 10319 on 04/01/16.
 */
public class LocationService  extends Service{

    private LocationManager lm;
    private StringBuffer location_data;
    private LocationListener ll;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //获取定位管理器
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //设置定位监听
        ll = new LocationListener() {
            /**
             * 位置变化，就触发此方法。
             * 覆盖此方法就可以追踪回调结果信息
             * @param location
             */
            @Override
            public void onLocationChanged(Location location) {
                //获取位置信息
                float accuracy = location.getAccuracy();//精确度，单位：米
                double longitude = location.getLongitude();//经度
                double latitude = location.getLatitude();//经度
                double altitude = location.getAltitude();//高度/海拔
                float speed = location.getSpeed();//移动速度

                location_data = new StringBuffer();
                location_data.append("accuracy:" + accuracy + "\n");
                location_data.append("longitude:" + longitude + "\n");
                location_data.append("latitude:" + latitude + "\n");
                location_data.append("altitude:" + altitude + "\n");
                location_data.append("speed:" + speed + "\n");


                //给安全号码发送短信
                String phone = SpTools.getString(getApplicationContext(), MyConstants.SAFENUMBER, "");

                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(phone, "", location_data.toString(), null, null);
                Log.i("@@location:",location_data.toString());

                //关闭自己,触发onDestroy()
                stopSelf();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        //获取所有定位方式
        List<String> providers = lm.getAllProviders();
        for (String p:providers) {
            Log.i("@@allProviders:",p);  //passive,gps
        }
        //动态获取最佳定位方式
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);//允许产生费用
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        String bestProvider = lm.getBestProvider(criteria,true);


        //注册gps来监听定位
        lm.requestLocationUpdates(bestProvider, 0, 0, ll);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //关闭定位监听
        lm.removeUpdates(ll);
        lm = null ;
        super.onDestroy();
    }
}
