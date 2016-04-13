package com.daemon.mobile.mobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.daemon.mobile.utils.MyConstants;
import com.daemon.mobile.utils.SpTools;

public class Setup3Activity extends BaseSetupActivity {

    private EditText et_safenumber;//号码编辑框

    /**
     *
     * 子类需要重写基类的该方法，来显示页面
     */
    @Override
    public void initView() {
        setContentView(R.layout.activity_setup3);
        et_safenumber = (EditText) findViewById(R.id.et_setup3_safenumber);

    }
    @Override
    public void nextActivity() {
        startActivity(Setup4Activity.class);
    }

    @Override
    public void backActivity() {
        startActivity(Setup2Activity.class);
    }

    @Override
    public void nextPage(View view) {
        //假如没有设置安全号码，则不能跳转到下一页
        String safeNumber = et_safenumber.getText().toString().trim();//获取对话框安全号码
        if(TextUtils.isEmpty(safeNumber)){
            Toast.makeText(getApplicationContext(),"请先设置安全号码",Toast.LENGTH_SHORT).show();
            return;
        }else {//编辑框号码不为空，则保存到配置文件中
            SpTools.putString(getApplicationContext(), MyConstants.SAFENUMBER,safeNumber);

        }

        super.nextPage(view);
    }

    @Override
    public void initData() {
        //初始化数据
        et_safenumber.setText(SpTools.getString(getApplicationContext(),
                MyConstants.SAFENUMBER, ""));
        super.initData();
    }

    /**
     * 选择联系人 按钮 点击事件
     * @param view
     */
    public void selectSafeNumber(View view){
        Intent intent = new Intent(this,ContactsActivity.class);
        //联系人选择页面 需要 有数据返回，因此用ForResult
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){//返回数据不为空，说明用户不是按返回键返回，而是选择数据后返回
            //获取数据
            String phone = data.getStringExtra(MyConstants.SAFENUMBER);
            //显示号码
            et_safenumber.setText(phone);
        }


        super.onActivityResult(requestCode, resultCode, data);


    }

}
