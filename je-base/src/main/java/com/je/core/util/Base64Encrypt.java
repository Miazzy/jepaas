package com.je.core.util;

import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;

/**
 * @Auther: wangmm@ketr.com.cn
 * @Date: 2019/4/13 15:37
 * @Description: BASE64乱序偏移加密
 */
public class Base64Encrypt {

    private static final String BASE64_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    /**
     * 描述: 混淆段长度,范围:4-255.
     */
    private static final int BLOCK_MAX_LENGTH = 20;

    /**
     * 描述: 混淆段长度,关键字长度.
     */
    private static final int BLOCK_KEY_LENGTH = 4;

    /**
     * @param args
     */
    public static void main(String[] args) {

        String str = "加密内容测试";
        long time = System.currentTimeMillis();
        String enstr = strEncode(str, "qqq", "aaaaa", "qwertyuiop");
        System.out.println("加密-" + (System.currentTimeMillis() - time) + "毫秒,密文长度" + enstr.length() + "\t" + enstr);
        time = System.currentTimeMillis();
        String destr = strDecode(enstr, "qqq", "aaaaa", "qwertyuiop");
        System.out.println("1解密-" + (System.currentTimeMillis() - time) + "毫秒,内容长度" + destr.length() + "\t" + destr);

    }

    public static String strEncode(String data, String firstKey, String secondKey, String thirdKey) {
        //验证参数
        if (data == null) return "";

        //获取时间戳
        long currentTimeMillis = System.currentTimeMillis();
        //计算key长度
        long keyLength = 0;
        for (char c : firstKey.toCharArray()) {
            keyLength += c;
        }
        for (char c : secondKey.toCharArray()) {
            keyLength -= c;
        }
        boolean isSum = false;
        for (char c : thirdKey.toCharArray()) {
            int cc = (int) c;
            if (isSum) {
                keyLength += c;
            } else {
                keyLength -= c;
            }
            isSum = !isSum;
        }
        if (keyLength < 0) {
            keyLength = -keyLength;
        }
        //计算混淆段长度
        int blockLength = (int) (4 + (keyLength + currentTimeMillis) % (BLOCK_MAX_LENGTH - 4));
        //计算偏移量
        int offset = (int) ((keyLength + blockLength) % BASE64_CODE.length());
        String base64Str = Base64.encodeBase64String(data.getBytes());
        StringBuilder sb = new StringBuilder();
        char[] chars = base64Str.toCharArray();
        ArrayList<Character> temp = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            int index = BASE64_CODE.indexOf(chars[i]);
            //偏移替换
            index = (index + offset) % BASE64_CODE.length();
            temp.add(BASE64_CODE.charAt(index));
            if (temp.size() == blockLength) {
                for (int j = temp.size() - 1; j >= 0; j--) {
                    sb.append(temp.get(j));
                }
                temp.clear();
            }
        }
        if (temp.size() != 0) {
            for (int j = temp.size() - 1; j >= 0; j--) {
                sb.append(temp.get(j));
            }
        }
        blockLength += sb.length() + BLOCK_KEY_LENGTH;
        String blockLengthStr = Integer.toHexString(blockLength);
        while (blockLengthStr.length() < BLOCK_KEY_LENGTH) {
            blockLengthStr = "0" + blockLengthStr;
        }
        sb.append(blockLengthStr);
        return sb.toString();
    }

    public static String strDecode(String data, String firstKey, String secondKey, String thirdKey) {
        //验证参数
        if (data == null || data.trim().length() < BLOCK_KEY_LENGTH) return "";
        data = data.trim();
        //计算key长度
        long keyLength = 0;
        for (char c : firstKey.toCharArray()) {
            keyLength += c;
        }
        for (char c : secondKey.toCharArray()) {
            keyLength -= c;
        }
        boolean isSum = false;
        for (char c : thirdKey.toCharArray()) {
            if (isSum) {
                keyLength += c;
            } else {
                keyLength -= c;
            }
            isSum = !isSum;
        }
        if (keyLength < 0) {
            keyLength = -keyLength;
        }
        //计算混淆段长度
        int blockLength = Integer.parseInt(data.substring(data.length() - BLOCK_KEY_LENGTH, data.length()), 16) - data.length();
        //计算偏移量
        int offset = (int) ((keyLength + blockLength) % BASE64_CODE.length());

        String base64Str = data.substring(0, data.length() - BLOCK_KEY_LENGTH);
        StringBuilder sb = new StringBuilder();
        char[] chars = base64Str.toCharArray();

        ArrayList<Character> temp = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            int index = BASE64_CODE.indexOf(chars[i]);
            //偏移替换
            index = (index + BASE64_CODE.length() - offset) % BASE64_CODE.length();
            temp.add(BASE64_CODE.charAt(index));
            if (temp.size() == blockLength) {
                for (int j = temp.size() - 1; j >= 0; j--) {
                    sb.append(temp.get(j));
                }
                temp.clear();
            }
        }
        if (temp.size() != 0) {
            for (int j = temp.size() - 1; j >= 0; j--) {
                sb.append(temp.get(j));
            }
        }
        return new String(Base64.decodeBase64(sb.toString()));
    }
}
