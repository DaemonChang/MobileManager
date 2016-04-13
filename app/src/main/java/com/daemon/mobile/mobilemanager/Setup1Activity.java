package com.daemon.mobile.mobilemanager;

import android.app.Activity;
import android.os.Bundle;

public class Setup1Activity extends BaseSetupActivity {
    /**
     *
     * 子类需要重写基类的该方法，来显示页面
     */
    @Override
    public void initView() {
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void nextActivity() {
        startActivity(Setup2Activity.class);
    }

    @Override
    public void backActivity() {

    }
}
