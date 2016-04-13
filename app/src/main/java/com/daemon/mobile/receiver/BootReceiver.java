package com.daemon.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.daemon.mobile.service.PhoneSafeService;
import com.daemon.mobile.utils.MyConstants;
import com.daemon.mobile.utils.SpTools;

/**开机启动的广播接收者
 * Created by 10319 on 04/01/16.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取已绑定的sim卡
        String oldSim = SpTools.getString(context, MyConstants.SIM,"");

        //获取当前sim卡
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String currentSim = tm.getSimSerialNumber();

        //判断是否变更
        if(!oldSim.equals(currentSim)){//变更
            //给安全号码发送短信
            String phone = SpTools.getString(context,MyConstants.SAFENUMBER,"");

            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(phone,"","I am the thief!",null,null);

        }

        //开机启动防盗保护
        if(SpTools.getBoolean(context,MyConstants.ISSAFE,false)){
            Intent service = new Intent(context,PhoneSafeService.class);
            context.startService(service);
        }
    }
}
