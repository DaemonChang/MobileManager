package com.daemon.mobile.mobilemanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daemon.mobile.utils.MyConstants;
import com.daemon.mobile.utils.SpTools;

public class Setup2Activity extends BaseSetupActivity {

    private Button bt_bindsim;
    private ImageView iv_isbind;

    /**
     *
     * 子类需要重写基类的该方法，来显示页面
     */
    @Override
    public void initView() {
        setContentView(R.layout.activity_setup2);
        //初始化控件
        bt_bindsim = (Button) findViewById(R.id.bt_setup2_bindsim);
        iv_isbind = (ImageView) findViewById(R.id.iv_setup2_isdone);
        initEvent();
    }

    @Override
    public void nextPage(View view) {
        //假如没绑定sim卡，则不允许跳转到下一页
        if(TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))){
            Toast.makeText(getApplicationContext(),"请先绑定Sim卡",Toast.LENGTH_SHORT).show();
            return;//跳出该方法，不会进入下一页
        }
        super.nextPage(view);//调用父类的 下一页 方法
    }

    @Override
    public void nextActivity() {

        startActivity(Setup3Activity.class);
    }

    @Override
    public void backActivity() {
        startActivity(Setup1Activity.class);
    }

    /**
     * 初始化组件的数据
     */
    @Override
    public void initData() {
        if(TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))) {
            //没绑定下的显示
            bt_bindsim.setBackgroundColor(0xffE3F2FD);
            iv_isbind.setImageResource(R.drawable.ic_thumb_down_black_18dp);



        }else {//有绑定下的显示
            bt_bindsim.setBackgroundColor(0xff2196F3);
            iv_isbind.setImageResource(R.drawable.ic_thumb_up_black_18dp);
        }

        super.initData();
    }

    @Override//设置自己的事件
    public void initEvent() {
        super.initEvent();
        bt_bindsim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //绑定/解绑，首先先判断状态
                if(TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.SIM, ""))){
                    //假如sim卡信息为空，则绑定sim卡
                    {  //绑定sim卡：获取+保存
                        //获取电话管理服务对象
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        //获取sim卡信息
                        String simSerialNumber =  tm.getSimSerialNumber();
                        //保存sim卡信息到sp中
                        SpTools.putString(getApplicationContext(), MyConstants.SIM,simSerialNumber);
                    }
                    {  //设置按钮背景和图片
                        bt_bindsim.setBackgroundColor(0xff2196F3);
                        iv_isbind.setImageResource(R.drawable.ic_thumb_up_black_18dp);
                        Toast.makeText(getApplicationContext(),"已绑定",Toast.LENGTH_SHORT).show();

                    }

                }else{//sim卡信息不为空，则解绑sim卡，实际就是把sim卡信息置空
                    SpTools.putString(getApplicationContext(),MyConstants.SIM,"");
                    {
                        //设置按钮背景和图片
                        bt_bindsim.setBackgroundColor(0xffE3F2FD);
                        iv_isbind.setImageResource(R.drawable.ic_thumb_down_black_18dp);
                        Toast.makeText(getApplicationContext(), "已解绑", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }
}
