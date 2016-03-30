package com.daemon.mobile.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daemon.mobile.application.MyApplication;
import com.daemon.mobile.mobilemanager.R;

/**
 * Created by 10319 on 03/28/16.
 */
public class SettingItem extends RelativeLayout implements View.OnClickListener{
    private TextView tv_setting_update;
    private TextView tv_setting_updateState;
    private CheckBox cb_setting_update;

    private SharedPreferences.Editor editor;
    private String item_title;
    private String sp_keyname;
    //-16-自定义控件，把三个控件集成在这里

    public SettingItem(Context context) {
        super(context);
        init(null);
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);//-17-自定义控件后，需要传递属性来初始化
    }

    private void init(AttributeSet attrs) {
        Log.i("@@@@AttributeSet:",attrs+"");

        editor = MyApplication.configSp.edit();

        View view =  View.inflate(getContext(), R.layout.item_update,null);
        tv_setting_update = (TextView) view.findViewById(R.id.tv_setting_update);


        tv_setting_updateState = (TextView) view.findViewById(R.id.tv_setting_updateState);
        cb_setting_update = (CheckBox) view.findViewById(R.id.cb_setting_update);

        if(attrs!=null){
            //获取自定义控件SettingItem的标题
            item_title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "item_title");
            //获取自定义控件的key，用于SharedPreference
            sp_keyname = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "sp_keyname");
            Log.i("@@@@item_title:", item_title);
            tv_setting_update.setText(item_title);

            //初始化控件内的子控件
            if(MyApplication.configSp.getBoolean(sp_keyname,false)){//默认关闭
                tv_setting_updateState.setText("已开启");
                cb_setting_update.setChecked(true);
            }else {
                tv_setting_updateState.setText("已关闭");
                cb_setting_update.setChecked(false);
            }
        }

        addView(view);

        view.setOnClickListener(this);

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);

    }

    @Override
    public void onClick(View v) {

        if (cb_setting_update.isChecked()) {//获取cb当前的状态(默认false)
            tv_setting_updateState.setText("已关闭");
            cb_setting_update.setChecked(false);
            editor.putBoolean(sp_keyname, false);
            editor.commit();
            Log.i("@@@@SettingItem1", cb_setting_update.isChecked() + "");

        }else{

            Log.i("@@@@SettingItem2", cb_setting_update.isChecked() + "");
            tv_setting_updateState.setText("已开启");
            cb_setting_update.setChecked(true);
            editor.putBoolean(sp_keyname, true);
            editor.commit();
        }


    }
}
