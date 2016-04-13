package com.daemon.mobile.mobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.daemon.mobile.service.PhoneSafeService;
import com.daemon.mobile.utils.MyConstants;
import com.daemon.mobile.utils.ServiceUtils;
import com.daemon.mobile.utils.SpTools;

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_protect;

    /**
     *
     * 子类需要重写基类的该方法，来显示页面
     */
    @Override
    public void initView() {
        setContentView(R.layout.activity_setup4);

        cb_protect = (CheckBox)findViewById(R.id.cb_setup4_protect);
    }


    /**
     * 勾选框点击事件
     */
    @Override
    public void initEvent() {
        //给勾选框设置变化监听
        cb_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){//勾选了，true
                    //Log.i("@@service","true");
                    SpTools.putBoolean(getApplicationContext(),MyConstants.ISSAFE,true);
                    //开启服务
                    Intent service = new Intent(Setup4Activity.this,PhoneSafeService.class);
                    startService(service);

                }else{//没勾选
                    //Log.i("@@service","false");
                    SpTools.putBoolean(getApplicationContext(),MyConstants.ISSAFE,false);
                    //关闭服务
                    Intent service = new Intent(Setup4Activity.this,PhoneSafeService.class);
                    stopService(service);

                }
            }
        });

        super.initEvent();
    }

    @Override//初始化数据
    public void initData() {
        //判断服务是否开启,若开启，勾选；否则，不勾选
        if(ServiceUtils.isServiceRunning(getApplicationContext()
                ,"com.daemon.mobile.service.PhoneSafeService")){
            cb_protect.setChecked(true);

        }else{
            cb_protect.setChecked(false);
        }

        super.initData();
    }

    @Override
    public void nextActivity() {
        //设置向导完成，把防盗功能设置状态 设为 true
        SpTools.putBoolean(getApplicationContext(), MyConstants.ISSETUP,true);

        //跳转到防盗主页面
        startActivity(PhoneSafeActivity.class);
    }

    @Override
    public void backActivity() {
        startActivity(Setup3Activity.class);
    }



}
