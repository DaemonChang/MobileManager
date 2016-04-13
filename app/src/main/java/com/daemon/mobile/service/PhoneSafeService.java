package com.daemon.mobile.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.daemon.mobile.mobilemanager.R;

public class PhoneSafeService extends Service {

    private SmsReceiver smsReceiver;

    public PhoneSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }

    @Override
    public void onCreate() {
        //注册短信的监听广播服务
        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);//最高优先级
        //注册
        registerReceiver(smsReceiver, intentFilter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //关闭短信的监听广播服务
        unregisterReceiver(smsReceiver);
        super.onDestroy();
    }

    /**
     * 短信的广播接收者
     */
    private class SmsReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
        //实现短信拦截功能
            Bundle extras = intent.getExtras();
            Object[] datas = (Object[]) extras.get("pdus");
            for (Object data : datas){
                SmsMessage sm = SmsMessage.createFromPdu((byte[])data);
                //Log.i("@@smsMessage:",sm.getMessageBody()+":"+sm.getOriginatingAddress());
                //获取信息内容
                String message = sm.getMessageBody();
                //判断指令
                if(message.equals("#*gps*#")){//gps追踪指令
                    //耗时操作，放到服务中执行
                    Intent gps_Service = new Intent(context,LocationService.class);
                    startService(gps_Service);//开启定位服务

                    abortBroadcast();//终止广播，隐藏短信提醒,高版本不生效
                }else if(message.equals("#*lockscreen*#")){
                    //获得设备管理器(在此之前，需要更新清单文件，创建xml功能文件)
                    DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                    //设置重新开启屏幕密码
                    dpm.resetPassword("123",0);
                    //一键锁屏
                    dpm.lockNow();

                    abortBroadcast();
                }else if(message.equals("#*wipedata*#")){
                    //获得设备管理器(在此之前，需要更新清单文件，创建xml功能文件,创建DeviceAdminReceiver子类)
                    DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                    //抹除sd卡数据
                    dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);

                    abortBroadcast();

                }else if(message.equals("#*alarm*#")){
                    //获得媒体播放器
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                   //播放
                    mp.start();
                    mp.setVolume(1,1);
                    abortBroadcast();
                }
            }


        }
    }
}
