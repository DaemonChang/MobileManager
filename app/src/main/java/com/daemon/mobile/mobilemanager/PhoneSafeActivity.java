package com.daemon.mobile.mobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daemon.mobile.utils.MyConstants;
import com.daemon.mobile.utils.ServiceUtils;
import com.daemon.mobile.utils.SpTools;

public class PhoneSafeActivity extends Activity {

    private TextView tv_safeNumber;
    private ImageView iv_isLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //判断是否第一次进入防盗设置。是，需要进入设置向导页面；否，进入防盗主页面
        if(SpTools.getBoolean(getApplicationContext(), MyConstants.ISSETUP,false)){
            initView();
            initData();

        }else{//无设置过,进入设置向导页面
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();//关闭当前页面，否则在设置向导页面1按返回键时，会返回此页面，从而无限循环
        }

    }

    /**
     * 初始化数据：安全号码、是否开启防盗功能的锁 图片
     */
    private void initData() {
        String phone = SpTools.getString(getApplicationContext(),MyConstants.SAFENUMBER,"");
        if (!phone.isEmpty())
            tv_safeNumber.setText(phone);//显示设置的联系人号码

        //判断服务是否开启,若开启，上锁图；否则，解锁图
        if(ServiceUtils.isServiceRunning(getApplicationContext()
                , "com.daemon.mobile.service.PhoneSafeService")){
            iv_isLock.setImageResource(R.drawable.ic_lock_outline_black_24dp);

        }else{
            iv_isLock.setImageResource(R.drawable.ic_lock_open_black_24dp);
        }



    }

    private void initView() {
        setContentView(R.layout.activity_phone_safe);
        tv_safeNumber = (TextView) findViewById(R.id.tv_phonesafe_number);
        iv_isLock = (ImageView) findViewById(R.id.iv_phonesafe_islock);
    }

    /**
     * 重新进入设置向导页面
     * @param view
     */
    public void resetSetup(View view){
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();

    }

    /**
     * 点击菜单时会调用
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    /**
     * 处理菜单事件
     * @param featureId
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }
}
