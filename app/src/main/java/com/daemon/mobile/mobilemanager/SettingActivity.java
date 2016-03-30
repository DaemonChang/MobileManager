package com.daemon.mobile.mobilemanager;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.daemon.mobile.application.MyApplication;

//-13-
public class SettingActivity extends ActionBarActivity {

    private CheckBox cb_setting_update;
    private TextView tv_setting_updateState;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


    }
}
