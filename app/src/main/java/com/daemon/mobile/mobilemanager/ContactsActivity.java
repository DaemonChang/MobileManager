package com.daemon.mobile.mobilemanager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daemon.mobile.domain.ContactsBean;
import com.daemon.mobile.engine.ReadContactsEngine;
import com.daemon.mobile.utils.MyConstants;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends ListActivity {

    private static final int LOADING = 1;
    private static final int FINISH = 2;
    private ProgressDialog pd;
    private List<ContactsBean> datas = new ArrayList<>();
    private MyAdapter mAdapter;
    private ListView lv_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ListActivity自带listview
        //用listview来显示联系人信息
        lv_contact = getListView();

        //为listview添加适配器
        mAdapter = new MyAdapter();
        lv_contact.setAdapter(mAdapter);//此时数据为空，需要在mHandler添加信息变更通知语句

        //初始化数据
        initData();
        //初始化事件
        initEvent();
    }

    private void initEvent() {
        //设置listview的item监听
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击条目，获取电话号码
                ContactsBean bean = datas.get(position);
                String phone = bean.getPhone();
                Intent intent = new Intent();
                //保存号码信息
                intent.putExtra(MyConstants.SAFENUMBER,phone);
                //给setup3传回信息
                setResult(1,intent);

                //关闭自己
                finish();

            }
        });


    }

    class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ContactsActivity.this, R.layout.item_contacts_listview, null);

            TextView tv_name = (TextView) view.findViewById(R.id.tv_setup3_contacts_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_setup3_contacts_phone);
            //获取联系人对象
            ContactsBean bean = datas.get(position);
            tv_name.setText(bean.getName());//添加名字
            Log.i("@@@name:", "第" + position + "个：" + bean.getName());
            tv_phone.setText(bean.getPhone());//添加手机号码
            Log.i("@@@phone:", "第" + position + "个：" + bean.getPhone());
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }

    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //更新界面
            switch (msg.what){
                case LOADING://正在加载信息
                    pd = new ProgressDialog(ContactsActivity.this);//对话框显示
                    pd.setTitle("提示");
                    pd.setMessage("正在加载联系人信息中。。");
                    pd.show();
                    break;
                case FINISH://加载完成
                    if(pd!=null) {
                        pd.dismiss();
                        pd = null;//垃圾回收，释放资源
                    }
                    //通过适配器通知listview，让其来显示数据
                    mAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }


        }
    };

    private void initData() {
     //获取数据，无论本地数据，还是网络数据，都是耗时操作，需要在子线程执行
        new Thread(){
            @Override
            public void run() {
                //显示正在获取数据
                Message msg = Message.obtain();
                msg.what = LOADING;
                mHandler.sendMessage(msg);

                //模拟耗时2s
                SystemClock.sleep(1000);
                //获取数据
                datas = ReadContactsEngine.readContacts(getApplicationContext());

                //完成获取数据
                msg = Message.obtain();
                msg.what = FINISH;
                mHandler.sendMessage(msg);

            }
        }.start();


    }
}
