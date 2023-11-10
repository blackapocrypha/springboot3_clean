package com.deeeelete.utils;


import java.util.*;


public class StringUtil {


    /**
     * 判断字符串是否是null或空字符串：是，返回true
     *
     * @param str 要判断的字符串
     * @return boolean  是，返回true    否，返回false
     */
    public static boolean isEmpty(String str) {
        return (str == null) || (str.isBlank());
    }

    /**
     * 判断字符串是否是null或空字符串：否，返回true
     *
     * @param str 要判断的字符串
     * @return boolean  否，返回true    是，返回false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否是null或空字符串：否，返回true
     *
     * @param str 要判断的字符串
     * @return boolean  否，返回true    是，返回false
     */
    public static boolean isNotEmpty(Object str) {
        if(str == null){
            return false;
        }else {
            return !isEmpty(str.toString());
        }
    }

    /**
     * 判断字符串是否是null或空字符串：否，返回false
     *
     * @param str 要判断的字符串
     * @return boolean  否，返回true    是，返回false
     */
    public static boolean isEmpty(Object str) {
        if(str == null){
            return true;
        }else {
            return isEmpty(str.toString());
        }
    }

    /**
     * 获取随机n位数
     *
     * @param n 字数
     * @return String
     */
    public static String getRandNumStr(int n) {
        Random random = new Random();
        StringBuilder sRand = new StringBuilder();
        for (var i = 0; i < n; i++) {
            sRand.append(String.valueOf(random.nextInt(10)));
        }
        return sRand.toString();
    }



    /**
     * 获取随机字符串
     */
    public static String getRandomString(int strLength) {
        StringBuilder buffer = new StringBuilder();
        Random random = new Random();
        for (var i = 0; i < strLength; i++) {
            if (random.nextBoolean()) {
                int charInt = 48 + random.nextInt(10);
                char c = (char) charInt;
                buffer.append(c);
            } else {
                int charInt;
                if (random.nextBoolean()) {
                    charInt = 65 + random.nextInt(26);
                } else {
                    charInt = 97 + random.nextInt(26);
                }
                if (charInt == 79) {
                    charInt = 111;
                }
                char c = (char) charInt;
                buffer.append(c);
            }
        }

        return buffer.toString();
    }




    /**
     * 文件名切割
     *
     * @param fileName 文件名 如  abc.png
     * @return 切割后的数组如["abc","png"]
     */
    public static String[] splitFileName(String fileName) {
        return fileName.split("\\.");
    }

    public static String getStrEncode(String str) {
        String encode = "GB2312";
        try {
            //判断是不是GB2312
            if (str.equals(new String(str.getBytes(encode), encode))) {
                //是的话，返回“GB2312“，以下代码同理
                return encode;
            }
            encode = "ISO-8859-1";
            //判断是不是ISO-8859-1
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
            encode = "UTF-8";
            //判断是不是UTF-8
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
            encode = "GBK";
            //判断是不是GBK
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

}
