package com.daemon.mobile.mobilemanager;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    //-12-
    class MyItemOnClickListener implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    Toast.makeText(HomeActivity.this, nameArray[position], Toast.LENGTH_SHORT).show();
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
