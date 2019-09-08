package com.je.document.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.FileTypeUtil;
import com.alibaba.fastjson.JSONObject;
import com.je.cache.service.config.ConfigCacheManager;
import com.je.cache.service.doc.FileTypeCacheManager;
import com.je.core.constants.doc.JEFileSaveType;
import com.je.core.constants.doc.JEFileType;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.util.*;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DicInfoVo;
import com.je.document.vo.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

/**
 * 文件工具类
 */
public class JeFileUtil {
    private static Logger logger = LoggerFactory.getLogger(JeFileUtil.class);

    public static final String absClassPath = JeFileUtil.class.getResource("/").getPath().replaceAll("test-classes", "classes");
    private static JeFileUtil jeFileUtil = null;
    public static final String webrootAbsPath = System.getProperty("jeplus.webapp");
    public static String queryFileFields="JE_CORE_DOCUMENT_ID,DOCUMENT_FORMAT,DOCUMENT_ADDRESS,DOCUMENT_DOCNAME,DOCUMENT_BUSTYPE,DOCUMENT_SAVETYPE,DOCUMENT_TYPE";
    public static String queryDocFields="JE_SYS_DOCUMENTATION_ID,DOCUMENTATION_TEXT,DOCUMENTATION_ADDRESS,DOCUMENTATION_DOWNTIME,DOCUMENTATION_BUSTYPE,DOCUMENTATION_SAVETYPE,DOCUMENTATION_FORMAT";
    public static String queryDocFileFields="JE_SYS_FILE_ID,FILE_FORMAT,FILE_ADDRESS,FILE_NAME,FILE_BUSTYPE,FILE_SAVETYPE,FILE_TYPE";
    public static String[] imageSuffixs=new String[]{"jpg","jpeg","png","gif","bmp","ico","raw","psd"};
    public static JeFileUtil getInstance(){
        if(jeFileUtil == null){
            jeFileUtil = new JeFileUtil();
        }
        return jeFileUtil;
    }
    /**
     * 读取文件
     * @param relativePath
     * @param jeFileType
     * @return
     */
    public File readFile(String relativePath, String jeFileType){
        return readFile(relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 读取文件
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     * @return
     */
    public File readFile(String relativePath, String jeFileType,String jeFileSaveType) {
        // TODO Auto-generated method stub
        File file=null;
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                InputStream io=ThirdFileUtil.readFile(relativePath);
                DiskFileUtil.saveFile(io,relativePath,JEFileSaveType.DEFAULT);
                file=DiskFileUtil.readFile(relativePath,JEFileSaveType.DEFAULT);
            }else{
                file=DiskFileUtil.readFile(relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            file=DiskFileUtil.readFile(relativePath,jeFileType);
        }else{
            file=DiskFileUtil.readFile(relativePath,JEFileSaveType.DEFAULT);
        }
        return file;
    }
    /**
     * 读取文本文件内容
     * @param relativePath
     * @param encoding
     * @param hh
     * @param jeFileType
     * @return
     */
    public String readTxt(String relativePath, String encoding, String hh, String jeFileType){
        return readTxt(relativePath,encoding,hh,jeFileType,getFileSaveType());
    }
    /**
     * 读取文本文件内容
     * @param relativePath
     * @param encoding
     * @param hh
     * @param jeFileType
     * @param jeFileSaveType
     * @return
     */
    public String readTxt(String relativePath, String encoding, String hh, String jeFileType,String jeFileSaveType){
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                InputStream io=ThirdFileUtil.readFile(relativePath);
                return DiskFileUtil.readTxt(io,encoding,hh);
            }else{
                return DiskFileUtil.readTxt(relativePath,encoding,hh,jeFileSaveType);
            }
        //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            return DiskFileUtil.readTxt(relativePath,encoding,hh,jeFileType);
        }else{
            return DiskFileUtil.readTxt(relativePath,encoding,hh,JEFileSaveType.DEFAULT);
        }
    }
    /**
     * 读取文件为IO
     * @param relativePath
     * @param jeFileType
     * @return
     */
    public InputStream readFileIo(String relativePath, String jeFileType){
        return readFileIo(relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 读取文件为IO
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     * @return
     */
    public InputStream readFileIo(String relativePath, String jeFileType,String jeFileSaveType){
        // TODO Auto-generated method stub
        InputStream io;
        //如果是项目文件
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                io=ThirdFileUtil.readFile(relativePath);
            }else{
                io=DiskFileUtil.readFileIo(relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            io=DiskFileUtil.readFileIo(relativePath,jeFileType);
        }else{
            io=DiskFileUtil.readFileIo(relativePath,JEFileSaveType.DEFAULT);
        }
        return io;
    }

    /**
     * 读取成字节信息
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     * @return
     */
    public byte[] readFileByte(String relativePath, String jeFileType,String jeFileSaveType){
        // TODO Auto-generated method stub
        byte[] bytes=null;
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                InputStream io=ThirdFileUtil.readFile(relativePath);
                try {
                    io.read(bytes);
                    bytes = new byte[io.available()];
                } catch (IOException e) {
                    throw new PlatformException("读取第三方服务文件字节信息异常!",PlatformExceptionEnum.JE_DOC_FILE_READ_ERROR,new Object[]{relativePath,jeFileType,jeFileSaveType},e);
                }finally {
                    closeInputStream(io);
                }
            }else{
                bytes=DiskFileUtil.readFileByte(relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            bytes=DiskFileUtil.readFileByte(relativePath,jeFileType);
        }else{
            bytes=DiskFileUtil.readFileByte(relativePath,JEFileSaveType.DEFAULT);
        }
        return bytes;
    }
    /**
     * 创建文件
     * @param relativePath
     * @param context
     * @param encoding
     * @param jeFileType
     * @return privateUrl
     */
    public String createFile(String relativePath,String context,String encoding,String jeFileType){
        return createFile(relativePath,context,encoding,jeFileType,getFileSaveType());
    }
    /**
     * 创建文件
     * @param relativePath
     * @param context
     * @param encoding
     * @param jeFileType
     * @param jeFileSaveType
     * @return privateUrl
     */
    public String createFile(String relativePath,String context,String encoding,String jeFileType,String jeFileSaveType){
        String privateUrl="";
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
               DiskFileUtil.createFile(relativePath,context,encoding,JEFileSaveType.DEFAULT);
               privateUrl=ThirdFileUtil.uploadFile(relativePath,DiskFileUtil.readFile(relativePath,JEFileSaveType.DEFAULT));
               DiskFileUtil.deleteFile(relativePath,JEFileType.PLATFORM);
            }else{
                DiskFileUtil.createFile(relativePath,context,encoding,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            DiskFileUtil.createFile(relativePath,context,encoding,jeFileType);
        }else{
            DiskFileUtil.createFile(relativePath,context,encoding,JEFileSaveType.DEFAULT);
        }
        return privateUrl;
    }
    /**
     * 保存文件
     * @param file
     * @param relativePath
     * @param jeFileType
     */
    public String saveFile(File file,String relativePath,String jeFileType){
        return saveFile(file,relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 保存文件
     * @param file
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public String saveFile(File file,String relativePath,String jeFileType,String jeFileSaveType){
        String privateUrl="";
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                privateUrl=ThirdFileUtil.uploadFile(relativePath,file);
            }else{
               DiskFileUtil.saveFile(file,relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            DiskFileUtil.saveFile(file,relativePath,jeFileType);
        }else{
            DiskFileUtil.saveFile(file,relativePath,JEFileSaveType.DEFAULT);
        }
        return privateUrl;
    }
    /**
     * 保存文件
     * @param file
     * @param relativePath
     * @param jeFileType
     */
    public String saveFile(MultipartFile file, String relativePath, String jeFileType){
        return saveFile(file,relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 保存文件
     * @param file
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public String saveFile(MultipartFile file,String relativePath,String jeFileType,String jeFileSaveType){
        String privateUrl="";
        InputStream io= null;
        try {
            io = file.getInputStream();
        } catch (IOException e) {
            throw new PlatformException("框架获取上传文件流信息出错",PlatformExceptionEnum.JE_DOC_FILE_READ_ERROR,new Object[]{relativePath,jeFileType,jeFileSaveType});
        }
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                privateUrl=ThirdFileUtil.uploadFile(relativePath,io);
            }else{
                DiskFileUtil.saveFile(io,relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            DiskFileUtil.saveFile(io,relativePath,jeFileType);
        }else{
            DiskFileUtil.saveFile(io,relativePath,JEFileSaveType.DEFAULT);
        }
        return privateUrl;
    }
    /**
     * 保存文件
     * @param fileIo
     * @param relativePath
     * @param jeFileType
     */
    public String saveFile(InputStream fileIo,String relativePath,String jeFileType){
        return saveFile(fileIo,relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 保存文件
     * @param fileIo
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public String saveFile(InputStream fileIo,String relativePath,String jeFileType,String jeFileSaveType){
        String privateUrl="";
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                privateUrl=ThirdFileUtil.uploadFile(relativePath,fileIo);
            }else{
                DiskFileUtil.saveFile(fileIo,relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            DiskFileUtil.saveFile(fileIo,relativePath,jeFileType);
        }else{
            DiskFileUtil.saveFile(fileIo,relativePath,JEFileSaveType.DEFAULT);
        }
        return privateUrl;
    }
    /**
      * 保存Base64文件
      * @param base64Str
      * @param relativePath
      * @param jeFileType
      */
    public String saveBase64File(String base64Str,String relativePath,String jeFileType){
        return saveBase64File(base64Str,relativePath,jeFileType,getFileSaveType());
    }
    /**
      * 保存Base64文件
      * @param base64Str
      * @param relativePath
      * @param jeFileType
      * @param jeFileSaveType
      */
    public String saveBase64File(String base64Str,String relativePath,String jeFileType,String jeFileSaveType){
        String privateUrl="";
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                DiskFileUtil.saveBase64File(base64Str,relativePath,JEFileSaveType.DEFAULT);
                privateUrl=ThirdFileUtil.uploadFile(relativePath,readFile(relativePath,JEFileType.PLATFORM));
                DiskFileUtil.deleteFile(relativePath,JEFileType.PLATFORM);
            }else{
                DiskFileUtil.saveBase64File(base64Str,relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            DiskFileUtil.saveBase64File(base64Str,relativePath,jeFileType);
        }else{
            DiskFileUtil.saveBase64File(base64Str,relativePath,JEFileSaveType.DEFAULT);
        }
        return privateUrl;
    }
    /**
     * 判断文件是否存在
     * @param relativePath
     * @param jeFileType
     * @return
     */
    public Boolean existsFile(String relativePath, String jeFileType){
        return existsFile(relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 判断文件是否存在
     * @param relativePath
     * @param jeFileSaveType
     * @return
     */
    public Boolean existsFile(String relativePath, String jeFileType,String jeFileSaveType){
        if(JEFileSaveType.THIRD.equalsIgnoreCase(jeFileSaveType) && JEFileType.PROJECT.equalsIgnoreCase(jeFileType)){
            return ThirdFileUtil.existFile(relativePath);
        }else {
            return DiskFileUtil.existsFile(relativePath,jeFileSaveType);
        }

    }
    /**
     * 删除文件
     * @param relativePath
     */
    public void deleteFile(String relativePath,String jeFileType){
        deleteFile(relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 删除文件
     * @param relativePath
     */
    public void deleteFile(String relativePath,String jeFileType,String jeFileSaveType){
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                ThirdFileUtil.deleteFile(relativePath);
            }else{
                DiskFileUtil.deleteFile(relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            DiskFileUtil.deleteFile(relativePath,jeFileType);
        }else{
            DiskFileUtil.deleteFile(relativePath,JEFileSaveType.DEFAULT);
        }
    }
    /**
     * 复制文件
     * @param sourceRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerRelativePath
     * @param targerJeFileType
     */
    public String copyFile(String sourceRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerRelativePath,String targerJeFileType){
        return copyFile(sourceRelativePath,sourceJeFileType,sourceJeFileSaveType,targerRelativePath,targerJeFileType,getFileSaveType());
    }
    /**
      * 复制文件
      * @param sourceRelativePath
      * @param sourceJeFileType
      * @param sourceJeFileSaveType
      * @param targerRelativePath
      * @param targerJeFileType
      * @param targerJeFileSaveType
      */
    public String copyFile(String sourceRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerRelativePath,String targerJeFileType,String targerJeFileSaveType){
        String privateUrl="";
        //如果都重复  无须复制
        if(sourceRelativePath.equals(targerRelativePath) && sourceJeFileType.equals(targerJeFileType) && sourceJeFileSaveType.equals(targerJeFileSaveType)){
            return targerRelativePath;
        }
        //如果复制源文件跟目标文件存储结构一致
        if(sourceJeFileType.equals(targerJeFileType) && sourceJeFileSaveType.equals(targerJeFileSaveType)){
            if(JEFileType.PROJECT.equals(sourceJeFileType)){
                if(JEFileSaveType.THIRD.equals(sourceJeFileSaveType)){
                    privateUrl=ThirdFileUtil.copyFile(sourceRelativePath,targerRelativePath);
                }else{
                    DiskFileUtil.copyFile(sourceRelativePath,targerRelativePath,sourceJeFileSaveType);
                }
            }else if(JEFileType.JEJAVACLASS.equals(sourceJeFileType)){
                DiskFileUtil.copyFile(sourceRelativePath,targerRelativePath,sourceJeFileType);
            }else{
                DiskFileUtil.copyFile(sourceRelativePath,targerRelativePath,JEFileSaveType.DEFAULT);
            }
        //如果平台模式一致
        }else if(sourceJeFileType.equals(targerJeFileType)){
            if(JEFileType.PROJECT.equals(sourceJeFileType)){
                if(JEFileSaveType.THIRD.equals(sourceJeFileSaveType)){
                    InputStream io=ThirdFileUtil.readFile(sourceRelativePath);
                    DiskFileUtil.saveFile(io,targerRelativePath,targerJeFileSaveType);
                }else if(JEFileSaveType.THIRD.equals(targerJeFileSaveType)){
                    File file=DiskFileUtil.readFile(sourceRelativePath,sourceJeFileSaveType);
                    privateUrl=ThirdFileUtil.uploadFile(targerRelativePath,file);
                }else{
                    DiskFileUtil.copyFile(sourceRelativePath,targerRelativePath,sourceJeFileSaveType,targerJeFileSaveType);
                }
            }else if(JEFileType.JEJAVACLASS.equals(sourceJeFileType)){
                DiskFileUtil.copyFile(sourceRelativePath,targerRelativePath,sourceJeFileType);
            }else{
                DiskFileUtil.copyFile(sourceRelativePath,targerRelativePath,JEFileSaveType.DEFAULT);
            }
        //如果平台存储方式一致
        }else if(sourceJeFileSaveType.equalsIgnoreCase(targerJeFileSaveType)){
            if(JEFileSaveType.THIRD.equals(sourceJeFileSaveType)){
                if(JEFileType.JEJAVACLASS.equals(targerJeFileType)){
                    InputStream io=ThirdFileUtil.readFile(sourceRelativePath);
                    DiskFileUtil.saveFile(io,targerRelativePath,targerJeFileType);
                }else if(JEFileType.PROJECT.equals(targerJeFileType)){
                    privateUrl=ThirdFileUtil.copyFile(sourceRelativePath,targerRelativePath);
                }else{
                    InputStream io=ThirdFileUtil.readFile(sourceRelativePath);
                    DiskFileUtil.saveFile(io,targerRelativePath,targerJeFileSaveType);
                }
            }else{
                if(JEFileType.JEJAVACLASS.equals(targerJeFileType)){
                    DiskFileUtil.copyFile(sourceRelativePath,targerRelativePath,sourceJeFileSaveType,targerJeFileType);
                }else if(JEFileType.PROJECT.equals(targerJeFileType)){
                    DiskFileUtil.copyFile(sourceRelativePath,targerRelativePath,sourceJeFileSaveType,targerJeFileSaveType);
                }else{
                    DiskFileUtil.copyFile(sourceRelativePath,targerRelativePath,sourceJeFileSaveType,JEFileSaveType.DEFAULT);
                }
            }
        }else{
            InputStream io=null;
            if(JEFileSaveType.THIRD.equals(sourceJeFileSaveType)){
                io=ThirdFileUtil.readFile(sourceRelativePath);
            }else if(JEFileType.JEJAVACLASS.equals(sourceJeFileType)){
                io=DiskFileUtil.readFileIo(sourceRelativePath,sourceJeFileType);
            }else if(JEFileType.PLATFORM.equals(sourceJeFileType)){
                io=DiskFileUtil.readFileIo(sourceRelativePath,JEFileSaveType.DEFAULT);
            }else{
                io=DiskFileUtil.readFileIo(sourceRelativePath,sourceJeFileSaveType);
            }
            privateUrl=saveFile(io,targerJeFileType,targerJeFileSaveType);
        }
        return privateUrl;
    }
    /**
     * 创建目录
     * @param relativePath
     * @param jeFileType
     */
    public void createFolder(String relativePath,String jeFileType){
        createFolder(relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 创建目录
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public void createFolder(String relativePath,String jeFileType,String jeFileSaveType){
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                throw new PlatformException("第三方服务不允许创建目录!",PlatformExceptionEnum.JE_DOC_ERROR,new Object[]{relativePath,jeFileType,jeFileSaveType});
            }else{
                DiskFileUtil.createFolder(relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            DiskFileUtil.createFolder(relativePath,jeFileType);
        }else{
            DiskFileUtil.createFolder(relativePath,JEFileSaveType.DEFAULT);
        }
    }
    /**
     * 删除目录
     * @param relativePath
     * @param jeFileType
     */
    public void deleteFolder(String relativePath,String jeFileType){
        deleteFolder(relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 删除目录
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public void deleteFolder(String relativePath,String jeFileType,String jeFileSaveType){
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                throw new PlatformException("第三方服务不允许删除目录!",PlatformExceptionEnum.JE_DOC_ERROR,new Object[]{relativePath,jeFileType,jeFileSaveType});
            }else{
                DiskFileUtil.deleteFolder(relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            DiskFileUtil.deleteFolder(relativePath,jeFileType);
        }else{
            DiskFileUtil.deleteFolder(relativePath,JEFileSaveType.DEFAULT);
        }
    }
    /**
     * 读取文件编码
     * @param relativePath
     * @param jeFileType
     * @return
     */
    public String readFileEncoding(String relativePath,String jeFileType){
        return readFileEncoding(relativePath,jeFileType,getFileSaveType());
    }
    /**
     * 读取文件编码
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     * @return
     */
    public String readFileEncoding(String relativePath,String jeFileType,String jeFileSaveType){
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                throw new PlatformException("第三方服务无法读取文件编码!",PlatformExceptionEnum.JE_DOC_ERROR,new Object[]{relativePath,jeFileType,jeFileSaveType});
            }else{
                return DiskFileUtil.readFileEncoding(relativePath,jeFileSaveType);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            return DiskFileUtil.readFileEncoding(relativePath,jeFileType);
        }else{
            return DiskFileUtil.readFileEncoding(relativePath,JEFileSaveType.DEFAULT);
        }
    }

//
    /**
     * 压缩文件
     * @param sourceFolderRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerFileRelativePath
     * @param jeFileType
     */
    public String zip(String sourceFolderRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerFileRelativePath,String jeFileType){
        return zip(sourceFolderRelativePath,sourceJeFileType,sourceJeFileSaveType,targerFileRelativePath,jeFileType,getFileSaveType());
    }
    /**
     * 压缩文件
     * @param sourceFolderRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerFileRelativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public String zip(String sourceFolderRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerFileRelativePath,String jeFileType,String jeFileSaveType){
        if(JEFileType.PROJECT.equals(sourceJeFileType) && JEFileSaveType.THIRD.equals(sourceJeFileSaveType)){
            throw new PlatformException("压缩文件原文件目录不允许有外部存储目录!",PlatformExceptionEnum.JE_DOC_ZIP_ERROR,new Object[]{sourceFolderRelativePath,sourceJeFileType,sourceJeFileSaveType,targerFileRelativePath,jeFileType,jeFileSaveType});
        }
        String privateUrl="";
        String folderJeFileSaveType=sourceJeFileSaveType;
        if(JEFileType.JEJAVACLASS.equals(sourceJeFileType)){
            folderJeFileSaveType=JEFileType.JEJAVACLASS;
        }else if(JEFileType.PLATFORM.equals(sourceJeFileType)){
            folderJeFileSaveType=JEFileSaveType.DEFAULT;
        }
        String folderBasePath=DiskFileUtil.getBasePath(folderJeFileSaveType);
        String zipBasePath="";
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                zipBasePath=DiskFileUtil.getBasePath(JEFileSaveType.DEFAULT);
                ZipUtils.zip(folderBasePath+sourceFolderRelativePath, zipBasePath+targerFileRelativePath, null, false);
                privateUrl=ThirdFileUtil.uploadFile(targerFileRelativePath,DiskFileUtil.readFile(targerFileRelativePath,JEFileSaveType.DEFAULT));
                DiskFileUtil.deleteFile(targerFileRelativePath,JEFileSaveType.DEFAULT);
            }else{
                zipBasePath=DiskFileUtil.getBasePath(jeFileSaveType);
                ZipUtils.zip(folderBasePath+sourceFolderRelativePath, zipBasePath+targerFileRelativePath, null, false);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            zipBasePath=DiskFileUtil.getBasePath(jeFileType);
            ZipUtils.zip(folderBasePath+sourceFolderRelativePath, zipBasePath+targerFileRelativePath, null, false);
        }else{
            zipBasePath=DiskFileUtil.getBasePath(JEFileSaveType.DEFAULT);
            ZipUtils.zip(folderBasePath+sourceFolderRelativePath, zipBasePath+targerFileRelativePath, null, false);
        }
        return privateUrl;
    }
    /**
     * 压缩文件
     * @param docs
     * @param zipRelativePath
     * @param jeFileType
     * @throws IOException
     */
    public String zipFiles(List<DynaBean> docs,String zipRelativePath,String jeFileType){
        return zipFiles(docs,zipRelativePath,jeFileType,getFileSaveType());
    }
    /**
     * 压缩文件
     * @param docs
     * @param zipRelativePath
     * @param jeFileType
     * @throws IOException
     */
    public String zipFiles(List<DynaBean> docs,String zipRelativePath,String jeFileType,String jeFileSaveType){
        // TODO Auto-generated method stub
        List<InputStream> filesIos=new ArrayList<>();
        List<String> fileNames=new ArrayList<String>();
        for(DynaBean doc:docs){
            String docJeFileType=doc.getStr("DOCUMENT_BUSTYPE");
            String docJeFileSaveType=doc.getStr("DOCUMENT_SAVETYPE");
            String docName=doc.getStr("DOCUMENT_DOCNAME");
            String address=doc.getStr("DOCUMENT_ADDRESS");
            if(StringUtil.isEmpty(docJeFileType)) {
                docJeFileType=JEFileType.PROJECT;
            }
            if(StringUtil.isEmpty(docJeFileSaveType)) {
                docJeFileSaveType=JEFileSaveType.DEFAULT;
            }
            InputStream io=readFileIo(address,docJeFileType,docJeFileSaveType);
            if(io!=null) {
                filesIos.add(io);
                fileNames.add(docName);
            }
        }
        String zipBasePath="";
        String privateUrl="";
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                zipBasePath=DiskFileUtil.getBasePath(JEFileSaveType.DEFAULT);
                ZipUtils.zipFiles4IoInput(filesIos, fileNames, zipBasePath+zipRelativePath);
                privateUrl=ThirdFileUtil.uploadFile(zipRelativePath,DiskFileUtil.readFile(zipRelativePath,JEFileSaveType.DEFAULT));
                DiskFileUtil.deleteFile(zipRelativePath,JEFileSaveType.DEFAULT);
            }else{
                zipBasePath=DiskFileUtil.getBasePath(jeFileSaveType);
                ZipUtils.zipFiles4IoInput(filesIos, fileNames, zipBasePath+zipRelativePath);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            zipBasePath=DiskFileUtil.getBasePath(jeFileType);
            ZipUtils.zipFiles4IoInput(filesIos, fileNames, zipBasePath+zipRelativePath);
        }else{
            zipBasePath=DiskFileUtil.getBasePath(JEFileSaveType.DEFAULT);
            ZipUtils.zipFiles4IoInput(filesIos, fileNames, zipBasePath+zipRelativePath);
        }
        return privateUrl;
    }
    /**
     * 解压压缩文件
     * @param sourceFileRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerFolderRelativePath
     * @param targerJeFileType
     */
    public void unZip(String sourceFileRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerFolderRelativePath,String targerJeFileType){
        unZip(sourceFileRelativePath,sourceJeFileType,sourceJeFileSaveType,targerFolderRelativePath,targerJeFileType,getFileSaveType());
    }
    /**
     * 解压压缩文件
     * @param sourceFileRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerFolderRelativePath
     * @param targerJeFileType
     * @param targerJeFileSaveType
     */
    public void unZip(String sourceFileRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerFolderRelativePath,String targerJeFileType,String targerJeFileSaveType){
        if(JEFileType.PROJECT.equals(targerJeFileType) && JEFileSaveType.THIRD.equals(targerJeFileSaveType)){
            throw new PlatformException("解压文件目标文件目录不允许有外部存储目录!",PlatformExceptionEnum.JE_DOC_ZIP_ERROR,new Object[]{sourceFileRelativePath,sourceJeFileType,sourceJeFileSaveType,targerFolderRelativePath,targerJeFileType,targerJeFileSaveType});
        }
        String zipBasePath=null;
        String folderBasePath="";
        String folderJeFileSaveType=targerJeFileSaveType;
        if(JEFileType.JEJAVACLASS.equals(targerJeFileType)){
            folderJeFileSaveType=JEFileType.JEJAVACLASS;
        }else if(JEFileType.PLATFORM.equals(targerJeFileType)){
            folderJeFileSaveType=JEFileSaveType.DEFAULT;
        }
        folderBasePath=DiskFileUtil.getBasePath(folderBasePath);
        if(JEFileType.PROJECT.equals(sourceJeFileType)){
            if(JEFileSaveType.THIRD.equals(sourceJeFileSaveType)){
                InputStream io=ThirdFileUtil.readFile(sourceFileRelativePath);
                DiskFileUtil.saveFile(io,sourceFileRelativePath,JEFileSaveType.DEFAULT);
                zipBasePath=DiskFileUtil.getBasePath(JEFileSaveType.DEFAULT);
                ZipUtils.unZip(zipBasePath+sourceFileRelativePath, folderBasePath+targerFolderRelativePath);
                DiskFileUtil.deleteFile(sourceFileRelativePath,JEFileSaveType.DEFAULT);
            }else{
                zipBasePath=DiskFileUtil.getBasePath(sourceJeFileSaveType);
                ZipUtils.unZip(zipBasePath+sourceFileRelativePath, folderBasePath+targerFolderRelativePath);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(sourceJeFileType)){
            zipBasePath=DiskFileUtil.getBasePath(sourceJeFileType);
            ZipUtils.unZip(zipBasePath+sourceFileRelativePath, folderBasePath+targerFolderRelativePath);
        }else{
            zipBasePath=DiskFileUtil.getBasePath(JEFileSaveType.DEFAULT);
            ZipUtils.unZip(zipBasePath+sourceFileRelativePath, folderBasePath+targerFolderRelativePath);
        }

    }
    /**
     * 下载文件
     * @param httpFileUrl
     * @param targerRelativePath
     * @param jeFileType
     * @return
     */
    public String downloadFile(String httpFileUrl, String targerRelativePath,String jeFileType){
        return downloadFile(httpFileUrl,targerRelativePath,jeFileType,getFileSaveType());
    }
    /**
     * 下载文件
     * @param httpFileUrl
     * @param targerRelativePath
     * @param jeFileType
     * @param jeFileSaveType
     * @return
     */
    public String downloadFile(String httpFileUrl, String targerRelativePath,String jeFileType,String jeFileSaveType) {
        // TODO Auto-generated method stub
        String privateUrl="";
        if(JEFileType.PROJECT.equals(jeFileType)){
            if(JEFileSaveType.THIRD.equals(jeFileSaveType)){
                String basePath=DiskFileUtil.getBasePath(JEFileSaveType.DEFAULT);
                HttpIOTool.downloadFile(httpFileUrl, basePath+targerRelativePath);
                privateUrl=ThirdFileUtil.uploadFile(targerRelativePath,DiskFileUtil.readFile(targerRelativePath,JEFileSaveType.DEFAULT));
                DiskFileUtil.deleteFile(targerRelativePath,JEFileSaveType.DEFAULT);
            }else{
                String basePath=DiskFileUtil.getBasePath(jeFileSaveType);
                HttpIOTool.downloadFile(httpFileUrl, basePath+targerRelativePath);
            }
            //平台类型文件
        }else if(JEFileType.JEJAVACLASS.equals(jeFileType)){
            String basePath=DiskFileUtil.getBasePath(jeFileType);
            HttpIOTool.downloadFile(httpFileUrl, basePath+targerRelativePath);
        }else{
            String basePath=DiskFileUtil.getBasePath(JEFileSaveType.DEFAULT);
            HttpIOTool.downloadFile(httpFileUrl, basePath+targerRelativePath);
        }
        return privateUrl;
    }
    public static void closeInputStream(InputStream inputStream){
        if(inputStream!=null){
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new PlatformException("文件流关闭异常!", PlatformExceptionEnum.JE_DOC_FILE_INPUTSTRAM_CLOSE_ERROR);
            }
        }
    }
    public static void closeOutputStream(OutputStream outputStream){
        if(outputStream!=null){
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new PlatformException("文件输出流关闭异常!", PlatformExceptionEnum.JE_DOC_FILE_OUTPUTSTRAM_CLOSE_ERROR);
            }
        }
    }

    /**
     * 关闭FileWriter流
     * @param writer
     */
    public static void closeFileWriter(FileWriter writer){
        if(writer != null){
            try {
                writer.close();
            } catch (IOException e) {
                throw new PlatformException("FileWriter流关闭异常!", PlatformExceptionEnum.JE_DOC_FILE_OUTPUTSTRAM_CLOSE_ERROR);
            }
        }
    }

    /**
     * 获取文件类型
     * @param filePath
     * @return
     */
    public String getFileTypeSuffix(String filePath){
        String fileType="";
        if(StringUtil.isNotEmpty(filePath)){
            int lastIndex=filePath.lastIndexOf(".");
            if(lastIndex!=-1){
                fileType=filePath.substring(lastIndex+1);
            }
        }
        return fileType;
    }
    /**
     * 获取文件类型
     * @param fileType
     * @return
     */
    public FileType getFileType(String fileType){
        FileType typeVo= FileTypeCacheManager.getCacheValue(fileType);
        if(typeVo==null){
            typeVo=FileTypeCacheManager.getDefaultValue();
        }
        return typeVo;
    }

    /**
     * 判断是否是图片
     * @param filePath
     * @return
     */
    public Boolean isImage(String filePath){
        Boolean flag=false;
        String fileSuffix=getFileTypeSuffix(filePath);
        if(StringUtil.isNotEmpty(fileSuffix)){
            fileSuffix=fileSuffix.toLowerCase();
        }
        if(ArrayUtils.contains(imageSuffixs,fileSuffix)){
            flag=true;
        }
        return flag;
    }
    /**
     * 获取图片的高宽信息
     * @param filePath
     * @param file
     * @return
     */
    public String getImageInfo(String filePath,File file){
        try {
            return getImageInfo(filePath,new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new PlatformException("文件上传获取文件的高宽异常",PlatformExceptionEnum.JE_DOC_FILE_READIMAGE_ERROR,new Object[]{filePath});
        }
    }
    /**
     * 获取图片的高宽信息
     * @param filePath
     * @param io
     * @return
     */
    public String getImageInfo(String filePath,InputStream io){
        JSONObject imageInfo=new JSONObject();
        try {
            if(io==null)return "";
            BufferedImage sourceImg=ImageIO.read(io);
            int width=sourceImg.getWidth();
            int height=sourceImg.getHeight();
            imageInfo.put("width",width);
            imageInfo.put("height",height);
            return imageInfo.toString();
        } catch (Exception e) {
            throw new PlatformException("文件上传获取文件的高宽异常",PlatformExceptionEnum.JE_DOC_FILE_READIMAGE_ERROR,new Object[]{filePath});
        }finally {
            closeInputStream(io);
        }
    }
//
    /**
     * 获取项目目录
     * @param baseRelativePath
     * @param jeFileType
     * @param replacePath
     * @param dicInfoVo
     * @return
     */
    public static JSONTreeNode getFilesTree(String baseRelativePath,String jeFileType, String replacePath, DicInfoVo dicInfoVo) {
        if (JEFileType.PROJECT.equals(jeFileType)) {
            String jeFileSaveType=getFileSaveType();
            if (JEFileSaveType.THIRD.equals(jeFileSaveType)) {
                throw new PlatformException("获取项目目录树形结构出错，项目文件并且第三方存储，无法获取!",PlatformExceptionEnum.JE_DOC_ERROR,new Object[]{baseRelativePath,jeFileType,replacePath,dicInfoVo});
            } else {
                return DiskFileUtil.getFilesTree(baseRelativePath, jeFileSaveType, replacePath, dicInfoVo);
            }
        } else if (JEFileType.JEJAVACLASS.equals(jeFileType)) {
            return DiskFileUtil.getFilesTree(baseRelativePath, jeFileType, replacePath, dicInfoVo);
        } else {
            return DiskFileUtil.getFilesTree(baseRelativePath, JEFileSaveType.DEFAULT, replacePath, dicInfoVo);
        }
    }
    /**
     * 获取文件存储类型
     * @return
     */
    public static String getFileSaveType(){
        String docType="default";
        String saveType=ConfigCacheManager.getCacheValue("sys.virtual.saveType");
        if(StringUtil.isNotEmpty(saveType)){
            docType=saveType;
        }
        return docType;
    }
    /**
     * 得到虚拟硬盘存储地址
     * @return
     */
    public static String getVirtualFilePath(){
        return ConfigCacheManager.getCacheValue("sys.virtual.filePath");
    }
}
