package com.daemon.mobile.mobilemanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.daemon.mobile.utils.MyConstants;
import com.daemon.mobile.utils.SpTools;

public class PhoneSafeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断是否第一次进入防盗设置。是，需要进入设置向导页面；否，进入防盗主页面
        if(SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP,false)){
            initView();

        }else{//无设置过,进入设置向导页面
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();//关闭当前页面，否则在设置向导页面1按返回键时，会返回此页面，从而无限循环
        }

    }

    private void initView() {
        setContentView(R.layout.activity_phone_safe);
    }


}
