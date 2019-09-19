package com.je.document.service;

import java.io.*;
import java.util.List;
import java.util.Map;

import com.je.core.constants.doc.JEFileSaveType;
import com.je.core.constants.doc.JEFileType;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.DynaBean;
import com.je.document.util.DiskFileUtil;
import com.je.document.util.ThirdFileUtil;
import com.je.document.vo.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface JeFileManager {
    /**
     * 读取文件
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @return
     */
    public File readFile(String relativePath, String jeFileType);
    /**
     * 读取文件
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @param jeFileSaveType 文件存储类型   THIRH DEFAULT VIRTUAL
     * @return
     */
    public File readFile(String relativePath, String jeFileType,String jeFileSaveType);
    /**
     * 读取文本文件内容
     * @param relativePath
     * @param encoding
     * @param hh
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @return
     */
    public String readTxt(String relativePath, String encoding, String hh, String jeFileType);
    /**
     * 读取文本文件内容
     * @param relativePath
     * @param encoding
     * @param hh
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @param jeFileSaveType 文件存储类型   THIRH DEFAULT VIRTUAL
     * @return
     */
    public String readTxt(String relativePath, String encoding, String hh, String jeFileType, String jeFileSaveType);
    /**
     * 读取文件为IO
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @return
     */
    public InputStream readFileIo(String relativePath, String jeFileType);
    /**
     * 读取文件为IO
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @param jeFileSaveType 文件存储类型   THIRH DEFAULT VIRTUAL
     * @return
     */
    public InputStream readFileIo(String relativePath, String jeFileType,String jeFileSaveType);
    /**
     * 创建文件
     * @param relativePath 项目目录下相对地址
     * @param context 文件内容
     * @param encoding 文件编码
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     */
    public String createFile(String relativePath, String context, String encoding, String jeFileType);
    /**
     * 创建文件
     * @param relativePath 项目目录下相对地址
     * @param context 文件内容
     * @param encoding 文件编码
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @param jeFileSaveType 文件存储类型   THIRH DEFAULT VIRTUAL
     */
    public String createFile(String relativePath, String context, String encoding, String jeFileType,String jeFileSaveType);
    /**
     * 保存文件
     * @param file
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     */
    public String saveFile(File file, String relativePath, String jeFileType);
    /**
     * 保存文件
     * @param file
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @param jeFileSaveType 文件存储类型   THIRH DEFAULT VIRTUAL
     */
    public String saveFile(File file, String relativePath, String jeFileType,String jeFileSaveType);
    /**
     * 保存文件
     * @param file
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     */
    public String saveFile(MultipartFile file, String relativePath, String jeFileType);
    /**
     * 保存文件
     * @param file
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @param jeFileSaveType 文件存储类型   THIRH DEFAULT VIRTUAL
     */
    public String saveFile(MultipartFile file, String relativePath, String jeFileType,String jeFileSaveType);
    /**
     * 保存文件
     * @param fileIo
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     */
    public String saveFile(InputStream fileIo, String relativePath, String jeFileType);
    /**
     * 保存文件
     * @param fileIo
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @param jeFileSaveType 文件存储类型   THIRH DEFAULT VIRTUAL
     */
    public String saveFile(InputStream fileIo, String relativePath, String jeFileType,String jeFileSaveType);
    /**
     * 保存Base64文件
     * @param base64Str
     * @param relativePath
     * @param jeFileType
     */
    public String saveBase64File(String base64Str,String relativePath,String jeFileType);
    /**
     * 保存Base64文件
     * @param base64Str
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public String saveBase64File(String base64Str,String relativePath,String jeFileType,String jeFileSaveType);
    /**
     * 判断文件是否存在
     * @param docInfo
     * @return
     */
    public Boolean existsFile(DynaBean docInfo);
    /**
     * 判断文件是否存在
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @return
     */
    public Boolean existsFile(String relativePath, String jeFileType);
    /**
     * 判断文件是否存在
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @param jeFileSaveType 文件存储类型   THIRH DEFAULT VIRTUAL
     * @return
     */
    public Boolean existsFile(String relativePath, String jeFileType,String jeFileSaveType);
    /**
     * 删除文件
     * @param docInfo
     */
    public void deleteFile(DynaBean docInfo);
    /**
     * 删除文件
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     */
    public void deleteFile(String relativePath, String jeFileType);
    /**
     * 删除项目文件
     * @param relativePath
     * @param jeFileType 文件类型 PROJECT JEJAVACLASS PLATFORM
     * @param jeFileSaveType 文件存储类型   THIRH DEFAULT VIRTUAL
     */
    public void deleteFile(String relativePath, String jeFileType, String jeFileSaveType);
    /**
     * 复制文件
     * @param docInfo
     * @param targerRelativePath
     * @param targerJeFileType
     */
    public String copyFile(DynaBean docInfo,String targerRelativePath,String targerJeFileType);
    /**
     * 复制文件
     * @param sourceRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerRelativePath
     * @param targerJeFileType
     */
    public String copyFile(String sourceRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerRelativePath,String targerJeFileType);
    /**
     * 复制文件
     * @param sourceRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerRelativePath
     * @param targerJeFileType
     * @param targerJeFileSaveType
     */
    public String copyFile(String sourceRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerRelativePath,String targerJeFileType,String targerJeFileSaveType);
    /**
     * 创建目录
     * @param relativePath
     * @param jeFileType
     */
    public void createFolder(String relativePath,String jeFileType);
    /**
     * 创建目录
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public void createFolder(String relativePath,String jeFileType,String jeFileSaveType);
    /**
     * 删除目录
     * @param relativePath
     * @param jeFileType
     */
    public void deleteFolder(String relativePath,String jeFileType);
    /**
     * 删除目录
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public void deleteFolder(String relativePath,String jeFileType,String jeFileSaveType);
    /**
     * 读取文件编码
     * @param relativePath
     * @param jeFileType
     * @return
     */
    public String readFileEncoding(String relativePath,String jeFileType);
    /**
     * 读取文件编码
     * @param relativePath
     * @param jeFileType
     * @param jeFileSaveType
     * @return
     */
    public String readFileEncoding(String relativePath,String jeFileType,String jeFileSaveType);
    /**
     * 压缩文件
     * @param sourceFolderRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerFileRelativePath
     * @param jeFileType
     */
    public String zip(String sourceFolderRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerFileRelativePath,String jeFileType);
    /**
     * 压缩文件
     * @param sourceFolderRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerFileRelativePath
     * @param jeFileType
     * @param jeFileSaveType
     */
    public String zip(String sourceFolderRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerFileRelativePath,String jeFileType,String jeFileSaveType);
    /**
     * 压缩文件
     * @param docs
     * @param zipRelativePath
     * @param jeFileType
     * @throws IOException
     */
    public String zipFiles(List<DynaBean> docs,String zipRelativePath,String jeFileType);
    /**
     * 压缩文件
     * @param docs
     * @param zipRelativePath
     * @param jeFileType
     * @throws IOException
     */
    public String zipFiles(List<DynaBean> docs,String zipRelativePath,String jeFileType,String jeFileSaveType);

    /**
     * 解压压缩文件
     * @param sourceFileRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerFolderRelativePath
     * @param targerJeFileType
     */
    public void unZip(String sourceFileRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerFolderRelativePath,String targerJeFileType);
    /**
     * 解压压缩文件
     * @param sourceFileRelativePath
     * @param sourceJeFileType
     * @param sourceJeFileSaveType
     * @param targerFolderRelativePath
     * @param targerJeFileType
     * @param targerJeFileSaveType
     */
    public void unZip(String sourceFileRelativePath,String sourceJeFileType,String sourceJeFileSaveType,String targerFolderRelativePath,String targerJeFileType,String targerJeFileSaveType);
    /**
     * 下载文件
     * @param httpFileUrl
     * @param targerRelativePath
     * @param jeFileType
     * @return
     */
    public String downloadFile(String httpFileUrl, String targerRelativePath,String jeFileType);
    /**
     * 下载文件
     * @param httpFileUrl
     * @param targerRelativePath
     * @param jeFileType
     * @param jeFileSaveType
     * @return
     */
    public String downloadFile(String httpFileUrl, String targerRelativePath,String jeFileType,String jeFileSaveType);

    /**
      * 获取文件后缀类型
      * @param filePath
      * @return
      */
    public String getFileTypeSuffix(String filePath);

    /**
     * 获取文件类型
     * @param fileType
     * @return
     */
    public FileType getFileType(String fileType);
    /**
     * 获取图片信息
     * @param filePath
     * @return
     */
    public Boolean isImage(String filePath);
    /**
     * 获取图片信息
     * @param filePath
     * @param file
     * @return
     */
    public String getImageInfo(String filePath,File file);
    /**
     * 获取图片信息
     * @param filePath
     * @param io
     * @return
     */
    public String getImageInfo(String filePath,InputStream io);
    /**
     * 获取文件类型
     * @param fileType 文件后缀
     * @return
     */
    public FileType getFileTypeInfo(String fileType);

//    /**
//     * SPRING MVC 文件上传
//     * @param folderPath
//     * @param fullFilePath
//     * @param newFileName
//     * @param multipartFile
//     * @param jeFileType
//     * @return
//     */
//    public Map uploadedFiles(String folderPath, String fullFilePath, String newFileName, MultipartFile multipartFile, String jeFileType);
//    /**
//     * 创建文件
//     * @param relativePath 项目目录下相对地址
//     * @param context 文件内容
//     * @param encoding 文件编码
//     * @param type 文件类型 PROJECT JEJAVACLASS JEJAVASOURCE PLATFORM JEBACK JEFRONT JECOMMON
//     */
//    public String createFile(String relativePath, String context, String encoding, String type);

//    /**
//     * 保存文件
//     * @param file
//     * @param relativePath
//     * @param type
//     */
//    public String saveFile(File file, String relativePath, String type);
//    /**
//     * 保存文件
//     * @param file
//     * @param relativePath
//     * @param type
//     */
//    public String saveFile(File file, String fileName, String relativePath, String type);

//    /**
//     * 保存文件
//     * @param relativePath
//     * @param file
//     * @param jeFileType
//     * @return
//     */
//    public Map<String,Object> saveFile(String relativePath,MultipartFile file,String jeFileType);
//    /**
//     * 保存BASE64文件
//     * @param base64Str
//     * @param relativePath
//     * @param type
//     */
//    public String saveBase64File(String base64Str, String relativePath, String type);

//    /**
////     * 复制文件
////     * @param sourceRelativePath
////     * @param targerRelativePath
////     * @param type
////     */
//    public String copyFile(String sourceRelativePath, String targerRelativePath, String type);
//
//    /**
//     * 复制文件
//     * @param sourceRelativePath
//     * @param targerRelativePath
//     * @param createParentFolder
//     * @param type
//     * @return
//     */
//    public String copyFile(String sourceRelativePath, String targerRelativePath, Boolean createParentFolder, String type);
//
//    /**
//     * 复制文件
//     * @param sourceRelativePath
//     * @param targerRelativePath
//     * @param createParentFolder
//     * @param sourceType
//     * @param targerType
//     * @return
//     */
//    public String copyFile(String sourceRelativePath, String targerRelativePath, Boolean createParentFolder, String sourceType, String targerType);
//
//    /**
//     * 复制文件
//     * @param sourceRelativePath
//     * @param targerRelativePath
//     * @param createParentFolder
//     * @param sourceType
//     * @param sourceSaveType
//     * @param targerType
//     * @return
//     */
//    public String copyFile(String sourceRelativePath, String targerRelativePath, Boolean createParentFolder, String sourceType, String sourceSaveType, String targerType);
//
//    /**
//     * 下载文件
//     * @param httpFileUrl
//     * @param targerRelativePath
//     * @param type
//     * @return
//     */
//    public String downloadFile(String httpFileUrl, String targerRelativePath, String type);
//
//    /**
//     * 删除文件
//     * @param relativePath
//     */
//    public void deleteFile(String relativePath, String type);
//    /**
//     * 删除项目文件
//     * @param relativePath
//     */
//    public void deleteFile(String relativePath, String type, String saveType);
//    /**
//     * 删除文件
//     * @param doc
//     */
//    public void deleteFile(DynaBean doc);
//


//
//
//    /**
//     * 读取文本文件内容
//     * @param relativePath
//     * @param type
//     * @return
//     */
//    public String readFileEncoding(String relativePath, String type);
//
//    /**
//     * 读取文本文件内容
//     * @param relativePath
//     * @param type
//     * @param saveType
//     * @return
//     */
//    public String readFileEncoding(String relativePath, String type, String saveType);
//    /**
//     * 读取文件
//     * @param relativePath
//     * @param type
//     * @return
//     */
//    public File readFile(String relativePath, String type);
//    /**
//     * 读取项目文件
//     * @param relativePath
//     * @param type
//     * @return
//     */
//    public File readFile(String relativePath, String type, String saveType);
//    /**
//     * 读取文件流
//     * @param relativePath
//     * @param type
//     * @return
//     */
//    public InputStream readFileIo(String relativePath, String type) throws FileNotFoundException;
//    /**
//     * 读取项目文件流
//     * @param relativePath
//     * @param type
//     * @param saveType
//     * @return
//     */
//    public InputStream readFileIo(String relativePath, String type, String saveType) throws FileNotFoundException;

//    /**
//     * 获取基础根目录
//     * @param type
//     */
//    public String getBasePath(String type);
//    /**
//     * 判断文件是否存在
//     * @param document
//     * @return
//     */
//    public Boolean existsFile(DynaBean document);
//    /**
//     * 判断文件是否存在
//     * @param relativePath
//     * @param type
//     * @return
//     */
//    public Boolean existsFile(String relativePath, String type);
//    /**
//     * 判断文件是否存在
//     * @param relativePath
//     * @param type
//     * @param saveType
//     * @return
//     */
//    public Boolean existsFile(String relativePath, String type, String saveType);

//    /**
//     * 获取文件类型信息
//     * @param filePath
//     * @param jeFileType
//     * @return
//     */
//    public FileType getFileTypeInfo(String fileType, String filePath, String jeFileType, String jeSaveFileType, String docType, String privateUrl, String beanTableCode);
//
//    /**
//     * 关闭文件流
//     * @param inputStream
//     */
//    public void closeInputStream(InputStream inputStream);
//    /**
//     * 关闭文件输出流
//     * @param outputStream
//     */
//    public void closeOutputStream(OutputStream outputStream);
}
