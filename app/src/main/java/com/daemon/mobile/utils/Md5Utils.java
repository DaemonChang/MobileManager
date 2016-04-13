package com.daemon.mobile.utils;


import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 10319 on 03/30/16.
 */
public class Md5Utils {
    public static String md5(String str) {
        StringBuilder md5_digest = new StringBuilder();
        try {
            //获取MD5加密器
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes =  str.getBytes();
            byte[] digest = md.digest(bytes);

            for (byte b: digest) {
             //把字节转化为16进制数
                int b_hx = b & 0xff;// & 0x00 00 00 ff
                String digest_string = Integer.toHexString(b_hx);
                if(digest_string.length() == 1){//只有一位的在前面补0
                  digest_string = "0"+digest_string;
                }
               md5_digest.append(digest_string);
            }
            Log.i("MD5@@@@",md5_digest+"");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5_digest+"";
    }

}
