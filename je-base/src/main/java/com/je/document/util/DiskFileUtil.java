package com.je.document.util;

import cn.hutool.core.io.FileUtil;
import com.je.core.constants.doc.JEFileSaveType;
import com.je.core.constants.doc.JEFileType;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.util.*;
import com.je.dd.vo.DicInfoVo;

import java.io.*;

/**
 * 本地文件操作工具类
 */
public class DiskFileUtil {
    /**
     * 创建文件
     * @param relativePath
     * @param context
     * @param encoding
     * @param jeSaveFileType
     */
    public static String createFile(String relativePath,String context,String encoding,String jeSaveFileType){
        String basePath=getBasePath(jeSaveFileType);
        File targerFile=new File(basePath+relativePath);
        File parentFolder=targerFile.getParentFile();
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }
        FileOperate.createFile(basePath+relativePath, context, encoding);
        return relativePath;
    }
    /**
     * 保存文件
     * @param file
     * @param relativePath
     * @param jeSaveFileType
     */
    public static String saveFile(File file,String relativePath,String jeSaveFileType){
        String basePath=getBasePath(jeSaveFileType);
        File targerFile=new File(basePath+relativePath);
        File parentFolder=targerFile.getParentFile();
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }
        FileOperate.copyFileFullPath(file, basePath+relativePath);
        return relativePath;
    }

    /**
     * 将流保存文件
     * @param fileIo
     * @param relativePath
     * @param jeSaveFileType
     * @return
     */
    public static String saveFile(InputStream fileIo, String relativePath, String jeSaveFileType){
        String basePath=getBasePath(jeSaveFileType);
        String filePath=basePath+relativePath;
        File file=new File(basePath+relativePath);
        File parentFolder=file.getParentFile();
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }
        FileUtil.writeFromStream(fileIo,new File(filePath));
        return relativePath;
    }
    /**
     * 保存Base64文件
     * @param base64Str
     * @param relativePath
     * @param jeSaveFileType
     */
    public static String saveBase64File(String base64Str,String relativePath,String jeSaveFileType){
        String basePath=getBasePath(jeSaveFileType);
        String filePath=basePath+relativePath;
        File file=new File(filePath);
        File parentFolder=file.getParentFile();
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }
        FileOperate.decoderBase64File(base64Str,filePath);
        return relativePath;
    }
    /**
     * 复制文件
     * @param sourceRelativePath
     * @param targerRelativePath
     * @param jeSaveFileType
     */
    public static String copyFile(String sourceRelativePath,String targerRelativePath,String jeSaveFileType){
        String basePath=getBasePath(jeSaveFileType);
        File file=new File(basePath+targerRelativePath);
        File parentFolder=file.getParentFile();
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }
        FileOperate.copyFile(basePath+sourceRelativePath, basePath+targerRelativePath);
        return targerRelativePath;
    }
    /**
     * 复制文件
     * @param sourceRelativePath
     * @param targerRelativePath
     * @param sourceJeFileSaveType
     * @param targerJeFileSaveType
     */
    public static String copyFile(String sourceRelativePath,String targerRelativePath,String sourceJeFileSaveType,String targerJeFileSaveType){
        String sourceBasePath=getBasePath(sourceJeFileSaveType);
        String basePath=getBasePath(targerJeFileSaveType);
        File file=new File(basePath+targerRelativePath);
        File parentFolder=file.getParentFile();
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }
        FileOperate.copyFile(sourceBasePath+sourceRelativePath, basePath+targerRelativePath);
        return targerRelativePath;
    }
    /**
     * 下载文件
     * @param httpFileUrl
     * @param targerRelativePath
     * @param jeFileSaveType
     * @return
     */
    public static String downloadFile(String httpFileUrl, String targerRelativePath,String jeFileSaveType) {
        // TODO Auto-generated method stub
        String filePath=getBasePath(jeFileSaveType)+targerRelativePath;
        File file=new File(filePath);
        File parentFolder=file.getParentFile();
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }
        HttpIOTool.downloadFile(httpFileUrl, filePath);
        return targerRelativePath;
    }

    /**
     * 删除文件
     * @param relativePath
     */
    public static void deleteFile(String relativePath,String jeFileSaveType){
        String basePath=getBasePath(jeFileSaveType);
        FileOperate.delFile(basePath+relativePath);
    }
    /**
     * 删除文件
     * @param fullPath
     */
    public static void deleteFile(String fullPath){
        FileOperate.delFile(fullPath);
    }

    /**
     * 创建目录
     * @param relativePath
     * @param jeFileSaveType
     */
    public static void createFolder(String relativePath,String jeFileSaveType){
        String basePath=getBasePath(jeFileSaveType);
        FileOperate.createFolder(basePath+relativePath);
    }

    /**
     * 创建目录
     * @param fullPath
     */
    public static void createFolder(String fullPath){
        FileOperate.createFolder(fullPath);
    }

    /**
     * 删除目录
     * @param relativePath
     * @param jeFileSaveType
     */
    public static void deleteFolder(String relativePath,String jeFileSaveType){
        String basePath=getBasePath(jeFileSaveType);
        FileOperate.delFolder(basePath+relativePath);
    }

    /**
     * 删除目录
     * @param fullPath
     */
    public static void deleteFolder(String fullPath){
        FileOperate.delFolder(fullPath);
    }

    /**
     * 压缩文件
     * @param sourceFolderRelativePath
     * @param targerFileRelativePath
     * @param jeFileSaveType
     */
    public static void zip(String sourceFolderRelativePath,String targerFileRelativePath,String jeFileSaveType){
        String basePath=getBasePath(jeFileSaveType);
        ZipUtils.zip(basePath+sourceFolderRelativePath, basePath+targerFileRelativePath, null, false);
    }
    /**
     * 解压压缩文件
     * @param sourceFileRelativePath
     * @param targerFolderRelativePath
     * @param jeFileSaveType
     */
    public static void unZip(String sourceFileRelativePath,String targerFolderRelativePath,String jeFileSaveType){
        String basePath=getBasePath(jeFileSaveType);
        ZipUtils.unZip(basePath+sourceFileRelativePath, basePath+targerFolderRelativePath);
    }
    /**
     * 读取文本文件内容
     * @param io
     * @param encoding
     * @param hh
     * @return
     */
    public static String readTxt(InputStream io, String encoding, String hh) {
        // TODO Auto-generated method stub
        StringBuffer str = new StringBuffer();
        InputStreamReader isr=null;
        BufferedReader br=null;
        try {
            if (encoding.equals("")) {
                isr = new InputStreamReader(io);
            } else {
                isr = new InputStreamReader(io, encoding);
            }
            br = new BufferedReader(isr);
            String data = "";
            while ((data = br.readLine()) != null) {
                str.append(data + (hh==null?" ":hh));
            }
            return str.toString();
        } catch (IOException es) {
            throw new PlatformException("文件流读取内容失败!", PlatformExceptionEnum.JE_DOC_FILE_READTXT_ERROR,new Object[]{encoding,hh},es);
        }finally {
            FileOperate.close(io,null,isr,br);
        }
    }
    /**
     * 读取文本文件内容
     * @param relativePath
     * @param encoding
     * @param hh
     * @return
     */
    public static String readTxt(String relativePath, String encoding, String hh,String jeFileSaveType) {
        // TODO Auto-generated method stub
        String basePath=getBasePath(jeFileSaveType);
        String result =FileOperate.readTxt(basePath+relativePath, encoding, hh);
        return result;
    }
    /**
     * 读取文件
     * @param relativePath
     * @param jeFileSaveType
     * @return
     */
    public static File readFile(String relativePath, String jeFileSaveType) {
        // TODO Auto-generated method stub
        File file=new File(getBasePath(jeFileSaveType)+relativePath);
        return file;
    }
    /**
     * 读取文件编码
     * @param relativePath
     * @param jeFileSaveType
     * @return
     */
    public static String readFileEncoding(String relativePath, String jeFileSaveType) {
        // TODO Auto-generated method stub
        return FileOperate.resolveCode(getBasePath(jeFileSaveType)+relativePath);
    }
    /**
     * 读取文件为IO
     * @param relativePath
     * @param jeFileSaveType
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream readFileIo(String relativePath, String jeFileSaveType){
        // TODO Auto-generated method stub
        File file=new File(getBasePath(jeFileSaveType)+relativePath);
        InputStream io= null;
        try {
            io = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            JeFileUtil.closeInputStream(io);
            throw new PlatformException("文件【"+relativePath+"】硬盘未找到!", PlatformExceptionEnum.JE_DOC_FILE_NOTFOUND_ERROR,new Object[]{relativePath,jeFileSaveType},e);
        }
        return io;
    }
    /**
     * 读取指定目录的字节信息
     * @param relativePath
     * @param jeFileSaveType
     * @return
     */
    public static byte[] readFileByte(String relativePath, String jeFileSaveType) {
        // TODO Auto-generated method stub
        byte[] bytes=FileOperate.readByte(getBasePath(jeFileSaveType)+relativePath);
        return bytes;
    }
    /**
     * 判断文件是否存在
     * @param relativePath
     * @param jeFileSaveType
     * @return
     */
    public static Boolean existsFile(String relativePath, String jeFileSaveType){
        String basePath=getBasePath(jeFileSaveType);
        return FileOperate.existsFile(basePath+relativePath);
    }
    /**
     * 获取项目目录
     * @param baseRelativePath
     * @param jeFileSaveType
     * @param replacePath
     * @param dicInfoVo
     * @return
     */
    public static JSONTreeNode getFilesTree(String baseRelativePath,String jeFileSaveType, String replacePath, DicInfoVo dicInfoVo){
        File file=readFile(baseRelativePath, jeFileSaveType);
        return FileOperate.getFilesTree(file, replacePath, dicInfoVo);
    }
    /**
     * 获取基础物理路径
     * @param jeFileSaveType
     * @return
     */
    public static String getBasePath(String jeFileSaveType){
        String basePath="";
        if(JEFileSaveType.VIRTUAL.equals(jeFileSaveType)){
            basePath=JeFileUtil.getVirtualFilePath();
        } else if(JEFileType.JEJAVACLASS.equals(jeFileSaveType)){
            basePath=JeFileUtil.absClassPath;
        }else{
            basePath=JeFileUtil.webrootAbsPath;
        }
        return basePath;
    }
    /**
     * 获取基础物理路径
     * @param jeFileType
     * @param jeFileSaveType
     * @return
     */
    public static String getBasePath(String jeFileType,String jeFileSaveType){
        String basePath="";
        if(JEFileType.PLATFORM.equals(jeFileType)){
            basePath= JeFileUtil.webrootAbsPath;
        }else{
            if(StringUtil.isEmpty(jeFileSaveType)){
                jeFileSaveType=JeFileUtil.getFileSaveType();
            }
            if(JEFileSaveType.VIRTUAL.equals(jeFileSaveType)){
                basePath=JeFileUtil.getVirtualFilePath();
            }else{
                basePath=JeFileUtil.webrootAbsPath;
            }
        }
        return basePath;
    }
}
