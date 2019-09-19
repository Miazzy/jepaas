package com.je.thrid.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import com.je.core.util.StringUtil;

/**
 * FastDFS操作工具类
 * @author LIULJ
 */
public class FastDfsUtil {

    private static FastDfsUtil fastDfsUtil = null;
    private static final String PATH_REG_STR = "group\\d+.*";
    private static final String GROUP_REG_STR = "group\\d+";

    /**
     * 获取工具类对象
     * @return
     */
    public static FastDfsUtil getInstance(){
        if(fastDfsUtil == null){
            fastDfsUtil = new FastDfsUtil();
            fastDfsUtil.init();
        }
        return fastDfsUtil;
    }

    private void init(){
        try {
            ClientGlobal.init(getClass().getResource("/").getPath() + "fastdfs.conf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 平台真实文件地址转换为group和相对地址
     * @param fastFdsPath
     * @return
     */
    private String[] parseFastDfsPathToGroupAndPath(String fastFdsPath){
        String[] groupAndPathArr = new String[2];
        //查找组信息
        Pattern groupPt = Pattern.compile(GROUP_REG_STR);
        Matcher groupMatch = groupPt.matcher(fastFdsPath);
        if(groupMatch.find()){
            groupAndPathArr[0]=StringUtil.isNotEmpty(groupMatch.group())?groupMatch.group():"";
        }

        //查找真实路径
        Pattern pathPt = Pattern.compile(PATH_REG_STR);
        Matcher pathMatch = pathPt.matcher(fastFdsPath);
        if(pathMatch.find() && StringUtil.isNotEmpty(groupAndPathArr[0])){
            String path = StringUtil.isNotEmpty(pathMatch.group())?pathMatch.group():"";
            groupAndPathArr[1] = path.replaceAll(groupAndPathArr[0] + "/", "");
        }
        return groupAndPathArr;
    }

    /**
     * 转换成真实平台可用文件地址
     * @param group
     * @param path
     * @return
     */
    private String parseGroupAndPathToRealPath(String group,String path){
        return group + "/" + path;
    }

    /**
     * FastDFS文件上传操作
     * @param fileSimpleName 文件名称
     * @param ext 文件扩展名
     * @param bytes 文件字节数组
     * @return fileIds[0]为文件group信息，fileIds[1]为文件路径信息
     */
    public String[] doUpload(String fileSimpleName,String ext,byte[] bytes){
        String[] fileIds = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            NameValuePair nvp[] = new NameValuePair[] {
                    new NameValuePair("fileName", fileSimpleName),
                    new NameValuePair("fileExtName", ext),
                    new NameValuePair("fileLength", String.valueOf(bytes.length))
            };
            fileIds = storageClient.upload_file(bytes, ext, nvp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return fileIds;
    }

    /**
     * FastDFS文件上传操作
     * @param fileSimpleName 文件名称
     * @param ext 文件扩展名
     * @param bytes 文件字节数组
     * @return fileIds[0]为文件group信息，fileIds[1]为文件路径信息
     */
    public String doPathUpload(String fileSimpleName,String ext,byte[] bytes){
        String filePath = null;
        String[] fileIds = doUpload(fileSimpleName, ext, bytes);
        if(fileIds != null){
            filePath = parseGroupAndPathToRealPath(fileIds[0], fileIds[1]);
        }
        return filePath;
    }

    /**
     * FastDFS文件上传操作
     * @param fileSimpleName
     * @param ext
     * @param file
     * @return
     */
    public String doPathUpload(String fileSimpleName,String ext,File file){
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        String path = null;
        try {
            byte[] buf = new byte[1024];
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            int len = 0;
            while((len=fis.read(buf)) != -1){
                bos.write(buf,0,len);
            }
            doPathUpload(fileSimpleName, ext, bos.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }

    /**
     * FastDFS文件上传操作
     * @param filePath 文件路径
     * @return
     */
    public String[] doUploadFile(String filePath){
        String[] fileIds = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            File file = new File(filePath);
            String fileName = file.getName();
            String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
            NameValuePair nvp[] = new NameValuePair[] {
                    new NameValuePair("fileName", fileName),
                    new NameValuePair("fileExtName", suffix),
                    new NameValuePair("fileLength", String.valueOf(file.length()))
            };
            fileIds = storageClient.upload_file(filePath, suffix, nvp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return fileIds;
    }

    /**
     * FastDFS文件上传操作
     * @param filePath 文件路径
     * @return
     */
    public String doPathUploadFile(String filePath){
        String fastDfsPath = null;
        String[] fileIds = doUploadFile(filePath);
        if(fileIds != null && fileIds.length>1){
            fastDfsPath = parseGroupAndPathToRealPath(fileIds[0], fileIds[1]);
        }
        System.out.println("--------------" + fastDfsPath);
        return fastDfsPath;
    }

    /**
     * 进行文件更新操作
     * @param group 文件所在组
     * @param fastDfsPath 文件在组中的路径
     * @param bytes 文件字节数组
     */
    public int doUpdateFile(String group,String fastDfsPath,byte[] bytes){
        int result = 0;
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            result = storageClient.modify_file(group, fastDfsPath, 0, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 进行文件更新操作，会根据文件路径自动解析出组和fastDfs在组中的路径
     * @param fastDfsPath 文件在FastDFS中的路径，可以是相对地址，也可以是绝对地址
     * @param bytes 文件字节数组
     * @return
     */
    public int doUpdateFile(String fastDfsPath,byte[] bytes){
        String[] groupAndFilePath = parseFastDfsPathToGroupAndPath(fastDfsPath);
        return doUpdateFile(groupAndFilePath[0], groupAndFilePath[1], bytes);
    }

    /**
     * 进行文件更新操作
     * @param group 文件所在组
     * @param fastDfsPath 文件在组中的路径
     * @param filePath 文件字节数组
     * @return
     */
    public int doUpdateFile(String group,String fastDfsPath,String filePath){
        int result = 0;
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            result = storageClient.modify_file(group, fastDfsPath, 0, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 进行文件更新操作，会自动根据路径解析出组和文件在组中的路径
     * @param fastDfsFilePath 文件在FastDFS中的路径，可以是相对地址，也可以是绝对地址
     * @param filePath 文件在本地的绝对路径
     * @return
     */
    public int doUpdateFile(String fastDfsFilePath,String filePath){
        String[] groupAndFilePath = parseFastDfsPathToGroupAndPath(fastDfsFilePath);
        return doUpdateFile(groupAndFilePath[0], groupAndFilePath[1], filePath);
    }

    /**
     * 下载文件
     * @param group 组信息
     * @param filePath 文件地址
     * @return
     */
    public byte[] doDownload(String group,String filePath){
        byte[] fileBytes = null;
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            fileBytes = storageClient.download_file(group, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    /**
     * 下载文件
     * @param fastDfsPath
     * 	分布式文件系统地址 group1/M00/00/00/wKiIgVt79laAWOjiACGHGwEjMNk262.jpg
     * @return
     */
    public byte[] doDownload(String fastDfsPath){
        String[] pathArr = parseFastDfsPathToGroupAndPath(fastDfsPath);
        String group = pathArr[0];
        String filePath = pathArr[1];
        byte[] fileBytes = doDownload(group,filePath);
        return fileBytes;
    }

    /**
     * 进行文件复制操作
     * @param group
     * @param fastDfsPath
     * @return
     */
    public String doCopyFile(String group,String fastDfsPath){
        String result = null;
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            byte[] bytes = storageClient.download_file(group, fastDfsPath);
            NameValuePair[] nvps = storageClient.get_metadata(group, fastDfsPath);
            String fileExtName = null;
            for (NameValuePair nameValuePair : nvps) {
                if("fileExtName".equals(nameValuePair.getName())){
                    fileExtName = nameValuePair.getValue();
                    break;
                }
            }
            String[] paths = storageClient.upload_file(bytes, fileExtName, nvps);
            result = paths[0] + "/" + paths[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 复制文件
     * @param relativePath 相对地址
     * @return
     */
    public String doCopyFile(String relativePath){
        String[] groupAndPathInfo = parseFastDfsPathToGroupAndPath(relativePath);
        return doCopyFile(groupAndPathInfo[0], groupAndPathInfo[1]);
    }

    /**
     * 读取文件相关信息，文件大小，创建时间等
     */
    public FileInfo doReadFileInfo(String group,String filePath){
        FileInfo fileInfo = null;
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            fileInfo = storageClient.get_file_info(group, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfo;
    }

    /**
     * 读取文件相关信息，文件大小，创建时间等
     * @param fastDfsPath
     * 	分布式文件系统地址 group1/M00/00/00/wKiIgVt79laAWOjiACGHGwEjMNk262.jpg
     * @return
     */
    public FileInfo doReadFileInfo(String fastDfsPath){
        String[] pathArr = parseFastDfsPathToGroupAndPath(fastDfsPath);
        String group = pathArr[0];
        String filePath = pathArr[1];
        FileInfo fileInfo = doReadFileInfo(group, filePath);
        return fileInfo;
    }

    /**
     * 读取文件元数据信息
     */
    public NameValuePair[] doReadFileMeta(String group,String filePath){
        NameValuePair[] nvps = null;
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer,
                    storageServer);
            nvps = storageClient.get_metadata(group, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nvps;
    }

    /**
     * 读取文件元数据信息
     * @param fastDfsPath
     * 	分布式文件系统地址 group1/M00/00/00/wKiIgVt79laAWOjiACGHGwEjMNk262.jpg
     * @return
     */
    public NameValuePair[] doReadFileMeta(String fastDfsPath){
        String[] pathArr = parseFastDfsPathToGroupAndPath(fastDfsPath);
        String group = pathArr[0];
        String filePath = pathArr[1];
        return doReadFileMeta(group, filePath);
    }

    /**
     * 进行文件删除操作
     * @param group 文件所在组信息
     * @param filePath 文件地址
     * @return
     */
    public boolean doDelete(String group,String filePath){
        boolean success = false;
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = null;
            StorageClient storageClient = new StorageClient(trackerServer,storageServer);
            int i = storageClient.delete_file(group, filePath);
            success = i==0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 进行文件删除操作
     * @param fastDfsPath
     * 	分布式文件系统地址 group1/M00/00/00/wKiIgVt79laAWOjiACGHGwEjMNk262.jpg
     * @return
     */
    public boolean doDelete(String fastDfsPath){
        String[] pathArr = parseFastDfsPathToGroupAndPath(fastDfsPath);
        String group = pathArr[0];
        String filePath = pathArr[1];
        return doDelete(group, filePath);
    }
}
