package com.bzw.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class Md5 {

    public static String getMD5Str(byte[] bytes) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }

    public static String getMD5Str(String str) {
        try {
            return getMD5Str(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
        }
        return null;
    }
}

