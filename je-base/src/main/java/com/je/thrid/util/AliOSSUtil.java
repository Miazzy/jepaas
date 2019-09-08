package com.je.thrid.util;


import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.document.util.JeFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * @author Nature.King
 * 2018-11-23
 */
public class AliOSSUtil {

    private static final Logger logger = LoggerFactory.getLogger(AliOSSUtil.class);

    public static String accessKeyId;
    public static String accessKeySecret;
    public static String endpoint;
    public static String bucketName;
    public static OSS client=null;
//
//    static {
//        try {
//            Properties sys = PropertiesLoaderUtils.loadAllProperties("sysconfig.properties");
//            accessKeyId = sys.getProperty("oss.accessKeyId");
//            accessKeySecret = sys.getProperty("oss.accessKeySecret");
//            endpoint = sys.getProperty("oss.endpoint");
//            bucketName = sys.getProperty("oss.bucketName");
//            ClientConfiguration conf = new ClientConfiguration();
////            UserAgent	用户代理，指HTTP的User-Agent头。默认为”aliyun-sdk-java”
////            ProxyHost	代理服务器主机地址
////            ProxyPort	代理服务器端口
////            ProxyUsername	代理服务器验证的用户名
////            ProxyPassword	代理服务器验证的密码
////            ProxyDomain	访问NTLM验证的代理服务器的Windows域名
////            ProxyWorkstation	NTLM代理服务器的Windows工作站名称
//            //允许打开的最大HTTP连接数。默认为50
//            conf.setMaxConnections(50);
//            // 连接超时，默认50秒
//            conf.setConnectionTimeout(50 * 1000);
//            // socket超时，默认50秒
//            conf.setSocketTimeout(50 * 1000);
//            // 失败后最大重试次数，默认3次
//            conf.setMaxErrorRetry(3);
//            client = new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static void initClient(){
        if(client==null){
            try {
                accessKeyId = WebUtils.getSysVar("ALIYUN_OSS_ACCESSKEYID");
                accessKeySecret = WebUtils.getSysVar("ALIYUN_OSS_ACCESSKEYSECRET");
                endpoint = WebUtils.getSysVar("ALIYUN_OSS_ENDPOINT");
                bucketName = WebUtils.getSysVar("ALIYUN_OSS_BUCKETNAME");
                ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
//            UserAgent	用户代理，指HTTP的User-Agent头。默认为”aliyun-sdk-java”
//            ProxyHost	代理服务器主机地址
//            ProxyPort	代理服务器端口
//            ProxyUsername	代理服务器验证的用户名
//            ProxyPassword	代理服务器验证的密码
//            ProxyDomain	访问NTLM验证的代理服务器的Windows域名
//            ProxyWorkstation	NTLM代理服务器的Windows工作站名称
                //允许打开的最大HTTP连接数。默认为50
                conf.setMaxConnections(50);
                // 连接超时，默认50秒
                conf.setConnectionTimeout(50 * 1000);
                // socket超时，默认50秒
                conf.setSocketTimeout(50 * 1000);
                // 失败后最大重试次数，默认3次
                conf.setMaxErrorRetry(3);
//              开启支持CNAME。CNAME是指将自定义域名绑定到存储空间上。
                conf.setSupportCname(true);
                client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret,conf);
            }catch (Exception e){
                logger.error("阿里云OSS服务初始化异常!",e);
//                throw new PlatformException("阿里云OSS服务初始化异常!",PlatformExceptionEnum.JE_DOC_ERROR,new Object[]{accessKeyId,accessKeySecret,endpoint,bucketName});
            }
        }
    }
    /**
     * 获取文件流
     *
     * @param key
     * @return
     */
    public static InputStream readFile(String key) {
        initClient();
        try {
            if (key.startsWith("/")) {
                key = key.substring(1);
            }
            OSSObject object = client.getObject(bucketName, key);
            InputStream objectContent = object.getObjectContent();
            return objectContent;
        } catch (Exception e) {
            logger.error("阿里云读取文件失败!",e);
//            throw new PlatformException("阿里云读取文件失败!", PlatformExceptionEnum.JE_DOC_FILE_READ_ERROR,new Object[]{key});
        }
        return null;
    }
    /**
     * 上传文件
     *
     * @param fileKey
     * @param file
     * @return
     */
    public static String uploadFile(String fileKey, File file) {
        if (StringUtil.isEmpty(fileKey)) {
            return null;
        }
        initClient();
        InputStream content = null;
        try {
            content = new FileInputStream(file);
            return uploadFile(fileKey,content);
        } catch (Exception e) {
            logger.error("阿里云上传文件失败!",e);
//            throw new PlatformException("阿里云上传文件失败!", PlatformExceptionEnum.JE_DOC_FILE_UPLOAD_ERROR,new Object[]{fileKey});
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param fileKey
     * @param content
     * @return
     */
    public static String uploadFile(String fileKey, InputStream content) {
        if (StringUtil.isEmpty(fileKey)) {
            return null;
        }
        initClient();
        try {
            String aliFileKey="";
            if (fileKey.startsWith("\\") || fileKey.startsWith("/")) {
                aliFileKey = fileKey.substring(1, fileKey.length());
            }else{
                aliFileKey=fileKey;
            }
            ObjectMetadata meta = new ObjectMetadata();
            client.putObject(bucketName, aliFileKey, content, meta);
            String privateKey = autherFileTime(aliFileKey,null).toString();
            return privateKey;
        } catch (Exception e) {
            logger.error("阿里云上传文件失败!",e);
//            throw new PlatformException("阿里云上传文件失败!", PlatformExceptionEnum.JE_DOC_FILE_UPLOAD_ERROR,new Object[]{fileKey});
        }finally {
            JeFileUtil.closeInputStream(content);
        }
        return "";
    }

    /**
     * 删除文件
     *
     * @param fileKey
     */
    public static void deleteFile(String fileKey) {
        initClient();
        if (fileKey.startsWith("\\") || fileKey.startsWith("/")) {
            fileKey = fileKey.substring(1);
        }
        try {
            client.deleteObject(bucketName, fileKey);
        }catch (Exception e){
            logger.error("阿里云删除文件失败!",e);
        }
    }


    /**
     * 本bucketName复制文件
     *
     * @param sourceKey
     * @param destinationKey
     */
    public static String copyFile(String sourceKey, String destinationKey) {
        initClient();
        if (destinationKey.startsWith("\\") || destinationKey.startsWith("/")) {
            destinationKey = destinationKey.substring(1, destinationKey.length());
        }
        if (sourceKey.startsWith("\\") || sourceKey.startsWith("/")) {
            sourceKey = sourceKey.substring(1, sourceKey.length());
        }
       client.copyObject(bucketName, sourceKey, bucketName, destinationKey);
        String privateKey = autherFileTime(destinationKey,null).toString();
        return privateKey;
    }

    /**
     * 检测文件是否存在
     * @param key
     * @return
     */
    public static Boolean existFile(String key){
        initClient();
        if (key.startsWith("/")) {
            key = key.substring(1);
        }
        try{
            return client.doesObjectExist(bucketName,key);
        } catch (Exception e) {
            logger.error("阿里云检查文件文件存在失败!",e);
//            throw new PlatformException("阿里云检查文件文件存在失败!", PlatformExceptionEnum.JE_DOC_FILE_READ_ERROR,new Object[]{key});
        }
        return false;
    }
    /**
     * 临时授权
     * 100年是无效的。由于Object为私有权限，通过在URL携带临时AK生成的签名进行鉴权且在指定时间段有效。主用户有效时长最多为23小时，子用户及STS用户有效时长最多为3600秒。
     * 本方法传null默认给永久读权限
     * @param fileKey    上传时候的文件路径 JE/data/upload/document/201812/xxx.jpg
     * @param expiration 设置URL过期时间为1小时 : Date expiration = new Date(new Date().getTime() + 3600 * 1000);
     * @return
     */
    public static URL autherFileTime(String fileKey, Date expiration) {
        URL url = null;
        if (null == expiration) {
            try {
                url = autherGradeFile(fileKey,"publicRead");
            } catch (MalformedURLException e) {
                logger.error("阿里云上传文件失败!",e);
//                throw new PlatformException("阿里云上传文件失败!", PlatformExceptionEnum.JE_DOC_FILE_UPLOAD_ERROR,new Object[]{fileKey});
            }
        }else {
            url = client.generatePresignedUrl(bucketName, fileKey, expiration);
        }
        return url;
    }

    /**
     * 访问级别授权
     *
     * 文件的访问权限优先级高于存储空间的访问权限。例如存储空间的访问权限是私有，而文件的访问权限是公共读写，则所有用户都有该文件的读写权限。如果某个文件没有设置过访问权限，则遵循存储空间的访问权限。
     * @param authGrade
     * authGrade 为 default 文件遵循存储空间的访问权限
     * authGrade 为 private 文件的拥有者和授权用户有该文件的读写权限，其他用户没有权限操作该文件。
     * authGrade 为 publicRead 文件的拥有者和授权用户有该文件的读写权限，其他用户只有文件的读权限。请谨慎使用该权限。
     * authGrade 为 publicRead 所有用户都有该文件的读写权限。请谨慎使用该权限
     * @param fileKey 上传时候的文件路径 JE/data/upload/document/201812/xxx.jpg
     */
    public static URL autherGradeFile(String fileKey,String authGrade) throws MalformedURLException {
        CannedAccessControlList cannedAccessControlList=null;
        if (fileKey.startsWith("\\") || fileKey.startsWith("/")) {
            fileKey = fileKey.substring(1, fileKey.length());
        }else{
            fileKey=fileKey;
        }
        if ("default".equals(authGrade)){
            cannedAccessControlList = CannedAccessControlList.Default;
        }else if("private".equals(authGrade)){
            cannedAccessControlList = CannedAccessControlList.Private;
        }else if("publicRead".equals(authGrade)){
            cannedAccessControlList = CannedAccessControlList.PublicRead;
        }else if("publicReadWrite".equals(authGrade)){
            cannedAccessControlList = CannedAccessControlList.PublicReadWrite;
        }else {
            logger.error("AliOSSUtil.autherFile()方法 auth参数错误");
//            throw new PlatformException("AliOSSUtil.autherFile()方法 auth参数错误!", PlatformExceptionEnum.JE_DOC_FILE_UPLOAD_ERROR,new Object[]{fileKey});
        }
        client.setObjectAcl(bucketName, fileKey, cannedAccessControlList);
        return new URL(getUrl(fileKey));
    }

    public static String getUrl(String fileKey){
        return "http://"+bucketName+"."+endpoint+"/"+fileKey;
    }
}
