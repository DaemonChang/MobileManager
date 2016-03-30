package com.daemon.mobile.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 10319 on 03/24/16.
 */
public class HttpUtils {
    //-3-
    public static String getTextFromStream(InputStream in){
        String result = "";

        ByteArrayOutputStream os = new ByteArrayOutputStream();//保存在内存中

        byte[] bytes = new byte[1024];
        int length ;

        try {
            while((length = in.read(bytes,0,1024))!=-1){
                os.write(bytes,0,length);

            }

            os.close();
            result = os.toString();
            Log.v("getTextFromStream","###result:"+result);
        } catch (IOException e) {
            e.printStackTrace();
        }

       return result;

    }
}
