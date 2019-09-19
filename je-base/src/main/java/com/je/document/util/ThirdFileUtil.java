package com.je.document.util;

import com.je.thrid.util.AliOSSUtil;

import java.io.File;
import java.io.InputStream;

/**
 * 第三方文件服务
 */
public class ThirdFileUtil {
    /**
     *  上传文件
     * @param fileKey
     * @param file
     * @return
     */
    public static String uploadFile(String fileKey, File file){
        return AliOSSUtil.uploadFile(fileKey,file);
    }
    /**
     *  上传文件
     * @param fileKey
     * @param content
     * @return
     */
    public static String uploadFile(String fileKey, InputStream content){
        return AliOSSUtil.uploadFile(fileKey,content);
    }
    /**
     * 删除文件
     * @param fileKey
     * @return
     */
    public static void deleteFile(String fileKey){
        AliOSSUtil.deleteFile(fileKey);
    }

    /**
     * 复制文件
     * @param sourceKey
     * @param targerKey
     * @return
     */
    public static String copyFile(String sourceKey,String targerKey){
        return AliOSSUtil.copyFile(sourceKey,targerKey);
    }

    /**
     * 读取文件
     * @param fileKey
     * @return
     */
    public static InputStream readFile(String fileKey){
        return AliOSSUtil.readFile(fileKey);
    }
    /**
     * 检测文件是否存在
     * @param fileKey
     * @return
     */
    public static Boolean existFile(String fileKey){
        return AliOSSUtil.existFile(fileKey);
    }

}
