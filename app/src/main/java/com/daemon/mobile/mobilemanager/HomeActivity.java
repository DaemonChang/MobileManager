package com.daemon.mobile.mobilemanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daemon.mobile.utils.Md5Utils;
import com.daemon.mobile.utils.MyConstants;
import com.daemon.mobile.utils.SpTools;

public class HomeActivity extends ActionBarActivity {

    private GridView gv_home_content;

    private final int COLUMN_NUMBER = 9;

    private int[] iconArray = {R.drawable.ic_phonelink_lock_black_36dp,R.drawable.ic_ring_volume_black_36dp,
    R.drawable.ic_library_books_black_36dp,R.drawable.ic_reorder_black_36dp,R.drawable.ic_router_black_36dp,
    R.drawable.ic_security_black_36dp,R.drawable.ic_delete_black_36dp,R.drawable.ic_build_black_36dp,
    R.drawable.ic_settings_black_36dp};

    private String[] nameArray = {"手机防盗","通讯卫士","软件管理",
            "进程管理","流量统计","手机杀毒",
            "缓存清理","高级工具","设置中心"};
    private AlertDialog dialog;//设置密码对话框


    //-9-创建功能列表页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar supportActionBar =  getSupportActionBar();
        supportActionBar.hide();
        //-10-初始化九宫格
        gv_home_content = (GridView) findViewById(R.id.gv_Home_content);
        gv_home_content.setAdapter(new MyAdapter());

        gv_home_content.setOnItemClickListener(new MyItemOnClickListener());






    }

    /**
     * 手机防盗--自定义登陆密码对话框
     */
    private void showPasswordLoginDialog() {
        View view = View.inflate(this,R.layout.dialog_login_password,null);

        final EditText password = (EditText) view.findViewById(R.id.et_dialog_login_password);


        Button bt_set = (Button)view.findViewById(R.id.bt_dialog_password_login);
        Button bt_cancle = (Button)view.findViewById(R.id.bt_dialog_password_notlogin);

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setView(view);

        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = password.getText().toString().trim();//返回字符串的副本，忽略前导空白和尾部空白。

                if(TextUtils.isEmpty(password1)){
                    Toast.makeText(getApplicationContext(),"密码不为空",Toast.LENGTH_SHORT).show();

                }else{//判断密码是否正确
                    //先加密
                    password1 = Md5Utils.md5(password1);
                    if(password1.equals(SpTools.getString(getApplicationContext(),MyConstants.PASSWORD,""))) {
                        Toast.makeText(getApplicationContext(),"密码正确",Toast.LENGTH_SHORT).show();
                        //进入防盗页面
                        Intent intent = new Intent(HomeActivity.this,PhoneSafeActivity.class);
                        startActivity(intent);

                        dialog.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(),"密码不正确",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        bt_cancle.setOnClickListener(new View.OnClickListener() {//监听取消键
            @Override
            public void onClick(View v) {
                dialog.dismiss();//关闭对话框

            }
        });
        dialog = builder.create();
        dialog.show();



    }

    private void showPasswordSettingDialog() {
        //填充对布局
        View view = View.inflate(this,R.layout.dialog_setting_password,null);

       final EditText password_1 = (EditText) view.findViewById(R.id.et_dialog_setting_password_1);
       final EditText password_2 = (EditText) view.findViewById(R.id.et_dialog_setting_password_2);

        Button bt_set = (Button)view.findViewById(R.id.bt_dialog_password_setting);
        Button bt_cancle = (Button)view.findViewById(R.id.bt_dialog_password_notsetting);

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setView(view);

        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = password_1.getText().toString().trim();//返回字符串的副本，忽略前导空白和尾部空白。
                String password2 = password_2.getText().toString().trim();

                if(TextUtils.isEmpty(password1)|| TextUtils.isEmpty(password2)){
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();

                }else if(!password1.equals(password2)){
                    Toast.makeText(getApplicationContext(),"密码不一致",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
                    //保存密码
                    //采用MD5对密码加密,可多次加密
                    password1 = Md5Utils.md5(password1);
                    SpTools.putString(getApplicationContext(), MyConstants.PASSWORD,password1);
                    dialog.dismiss();

                }
            }
        });

        bt_cancle.setOnClickListener(new View.OnClickListener() {//监听取消键
            @Override
            public void onClick(View v) {
                dialog.dismiss();//关闭对话框

            }
        });
        dialog = builder.create();
        dialog.show();

    }

    //-12-
    class MyItemOnClickListener implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0://手机防盗
                    //需检查是否有设置过密码
                    if(TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, ""))){
                        //从ShearPreference中获取密码，为空，则弹出设置密码
                        showPasswordSettingDialog();//显示密码设置对话框
                    }else{//若不为空，则弹出登录框
                        showPasswordLoginDialog();
                    }

                   // Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                    //Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
                    break;
                
                
                
            }
            
            
        }
    }



    //-11-
    class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return COLUMN_NUMBER;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(HomeActivity.this,R.layout.item_gridview,null);

            ImageView iv_gv_icon = (ImageView) view.findViewById(R.id.iv_gv_icon);
            TextView tv_gv_name = (TextView) view.findViewById(R.id.tv_gv_name);

            iv_gv_icon.setImageResource(iconArray[position]);
            tv_gv_name.setText(nameArray[position]);
            return view;
        }
    }


}
