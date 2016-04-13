package com.daemon.mobile.engine;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


import com.daemon.mobile.domain.ContactsBean;

import java.util.ArrayList;
import java.util.List;

/**读取手机联系人的功能类
 * Created by 10319 on 03/31/16.
 */
public class ReadContactsEngine {
    /**
     * 读取手机联系人
     * @param context
     */
    public static List<ContactsBean> readContacts(Context context){
        List<ContactsBean> contactsBeans = new ArrayList<>();
        //contacts表的uri
        Uri uriContacts = Uri.parse("content://com.android.contacts/contacts");
        //data表的uri
        Uri uriData = Uri.parse("content://com.android.contacts/data");

         Cursor cursor =  context.getContentResolver().query(uriContacts,new String[]{"_id"},null,null,null);
        //循环取数据
        while (cursor.moveToNext()){
            //联系人信息的封装bean
            ContactsBean bean = new ContactsBean();
            //获取联系人id
            String id = cursor.getString(0);//获取第一列的数据

            Cursor cursor1 = context.getContentResolver().query(uriData,new String[]{"data1","mimetype"}
                    ,"raw_contact_id = ?",new String[]{id},null);//表名uri，列名字，条件，条件的参数，排序

            //循环每条联系人信息的部分信息（姓名与号码）
            while (cursor1.moveToNext()) {
                String data = cursor1.getString(0);//包括电话号码与姓名
                String mimeType = cursor1.getString(1);
                if (mimeType.equals("vnd.android.cursor.item/name")) {//姓名
                    System.out.println("第" + id + "个用户，姓名：" + data);
                    bean.setName(data);
                } else if (mimeType.equals("vnd.android.cursor.item/phone_v2")) {//电话号码
                   System.out.println("第" + id + "个用户，电话号码：" + data);
                    bean.setPhone(data);
                }
            }
            cursor1.close();//关闭游标，释放数据库服务器端的资源
        contactsBeans.add(bean);//添加该联系人信息到数组中
    }
        cursor.close();
        return  contactsBeans;
    }


}
