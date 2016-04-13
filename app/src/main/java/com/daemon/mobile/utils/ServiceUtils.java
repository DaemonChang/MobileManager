package com.daemon.mobile.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**服务管理工具类
 * Created by 10319 on 04/01/16.
 */
public class ServiceUtils {

    /**
     * 检测服务是否在运行
     * @param context
     * @param serviceName  完整的服务名字：包名+类名
     * @return 是否运行
     */

    public static boolean isServiceRunning(Context context,String serviceName){
        boolean isRunning = false;
        //生成ActivityManager对象，获取动态信息
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取手机运行中的服务（max 50条）
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = manager.getRunningServices(50);
        //循环所有服务，寻找指定的服务
        for (ActivityManager.RunningServiceInfo services: runningServiceInfos) {
            //System.out.println(services.service.getClassName());//不能直接打印services

            if(services.service.getClassName().equals(serviceName)){//找到
                isRunning =  true;
                //Log.i("serviceIsRunning@@",isRunning+"");
                //找到就退出循环
                break;
            }
        }
        //Log.i("@@serviceIsRunning@@",isRunning+"");
        return isRunning;
    }
}
