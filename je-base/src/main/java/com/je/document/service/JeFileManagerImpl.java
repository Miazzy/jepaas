package com.je.document.service;

import java.io.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.je.cache.service.doc.FileTypeCacheManager;
import com.je.core.constants.doc.JEFileSaveType;
import com.je.core.constants.doc.JEFileType;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.DynaBean;
import com.je.document.util.JeFileUtil;
import com.je.document.vo.FileType;
import org.springframework.web.multipart.MultipartFile;

@Component("jeFileManager")
public class JeFileManagerImpl implements JeFileManager{

    private static Logger logger = LoggerFactory.getLogger(JeFileManagerImpl.class);
    private static JeFileUtil jeFileUtil;

    static {
        jeFileUtil=JeFileUtil.getInstance();
    }

    @Override
    public File readFile(String relativePath, String jeFileType) {
        return jeFileUtil.readFile(relativePath,jeFileType);
    }

    @Override
    public File readFile(String relativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.readFile(relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String readTxt(String relativePath, String encoding, String hh, String jeFileType) {
        return jeFileUtil.readTxt(relativePath,encoding,hh,jeFileType);
    }

    @Override
    public String readTxt(String relativePath, String encoding, String hh, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.readTxt(relativePath,encoding,hh,jeFileType,jeFileSaveType);
    }

    @Override
    public InputStream readFileIo(String relativePath, String jeFileType) {
        return jeFileUtil.readFileIo(relativePath,jeFileType);
    }

    @Override
    public InputStream readFileIo(String relativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.readFileIo(relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String createFile(String relativePath, String context, String encoding, String jeFileType) {
        return jeFileUtil.createFile(relativePath,context,encoding,jeFileType);
    }

    @Override
    public String createFile(String relativePath, String context, String encoding, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.createFile(relativePath,context,encoding,jeFileType,jeFileSaveType);
    }

    @Override
    public String saveFile(File file, String relativePath, String jeFileType) {
        return jeFileUtil.saveFile(file,relativePath,jeFileType);
    }

    @Override
    public String saveFile(File file, String relativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.saveFile(file,relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String saveFile(MultipartFile file, String relativePath, String jeFileType) {
        return jeFileUtil.saveFile(file,relativePath,jeFileType);
    }

    @Override
    public String saveFile(MultipartFile file, String relativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.saveFile(file,relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String saveFile(InputStream fileIo, String relativePath, String jeFileType) {
        return jeFileUtil.saveFile(fileIo,relativePath,jeFileType);
    }

    @Override
    public String saveFile(InputStream fileIo, String relativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.saveFile(fileIo,relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String saveBase64File(String base64Str, String relativePath, String jeFileType) {
        return jeFileUtil.saveBase64File(base64Str,relativePath,jeFileType);
    }

    @Override
    public String saveBase64File(String base64Str, String relativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.saveBase64File(base64Str,relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public Boolean existsFile(DynaBean docInfo) {
        String relativePath=docInfo.getStr("DOCUMENT_ADDRESS");
        String jeFileType=docInfo.getStr("DOCUMENT_BUSTYPE");
        String jeFileSaveType=docInfo.getStr("DOCUMENT_SAVETYPE");
        if(StringUtil.isEmpty(jeFileType)){
            jeFileType = JEFileType.PROJECT;
        }
        if(StringUtil.isEmpty(jeFileSaveType)){
            jeFileSaveType = JEFileSaveType.DEFAULT;
        }
        return existsFile(relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public Boolean existsFile(String relativePath, String jeFileType) {
        return jeFileUtil.existsFile(relativePath,jeFileType);
    }

    @Override
    public Boolean existsFile(String relativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.existsFile(relativePath,jeFileType,jeFileSaveType);
    }
    @Override
    public void deleteFile(DynaBean docInfo) {
        String relativePath=docInfo.getStr("DOCUMENT_ADDRESS");
        String jeFileType=docInfo.getStr("DOCUMENT_BUSTYPE");
        String jeFileSaveType=docInfo.getStr("DOCUMENT_SAVETYPE");
        if(StringUtil.isEmpty(jeFileType)){
            jeFileType = JEFileType.PROJECT;
        }
        if(StringUtil.isEmpty(jeFileSaveType)){
            jeFileSaveType = JEFileSaveType.DEFAULT;
        }
        deleteFile(relativePath, jeFileType, jeFileSaveType);
    }

    @Override
    public void deleteFile(String relativePath, String jeFileType) {
        jeFileUtil.deleteFile(relativePath,jeFileType);
    }

    @Override
    public void deleteFile(String relativePath, String jeFileType, String jeFileSaveType) {
        jeFileUtil.deleteFile(relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String copyFile(DynaBean docInfo, String targerRelativePath, String targerJeFileType) {
        String relativePath=docInfo.getStr("DOCUMENT_ADDRESS");
        String jeFileType=docInfo.getStr("DOCUMENT_BUSTYPE");
        String jeFileSaveType=docInfo.getStr("DOCUMENT_SAVETYPE");
        if(StringUtil.isEmpty(jeFileType)){
            jeFileType = JEFileType.PROJECT;
        }
        if(StringUtil.isEmpty(jeFileSaveType)){
            jeFileSaveType = JEFileSaveType.DEFAULT;
        }
        return copyFile(relativePath,jeFileType,jeFileSaveType,targerRelativePath,targerJeFileType);
    }

    @Override
    public String copyFile(String sourceRelativePath, String sourceJeFileType, String sourceJeFileSaveType, String targerRelativePath, String targerJeFileType) {
        return jeFileUtil.copyFile(sourceRelativePath,sourceJeFileType,sourceJeFileSaveType,targerRelativePath,targerJeFileType);
    }

    @Override
    public String copyFile(String sourceRelativePath, String sourceJeFileType, String sourceJeFileSaveType, String targerRelativePath, String targerJeFileType, String targerJeFileSaveType) {
        return jeFileUtil.copyFile(sourceRelativePath,sourceJeFileType,sourceJeFileSaveType,targerRelativePath,targerJeFileType,targerJeFileSaveType);
    }

    @Override
    public void createFolder(String relativePath, String jeFileType) {
        jeFileUtil.createFolder(relativePath,jeFileType);
    }

    @Override
    public void createFolder(String relativePath, String jeFileType, String jeFileSaveType) {
        jeFileUtil.createFolder(relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public void deleteFolder(String relativePath, String jeFileType) {
        jeFileUtil.deleteFolder(relativePath,jeFileType);
    }

    @Override
    public void deleteFolder(String relativePath, String jeFileType, String jeFileSaveType) {
        jeFileUtil.deleteFolder(relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String readFileEncoding(String relativePath, String jeFileType) {
        return jeFileUtil.readFileEncoding(relativePath,jeFileType);
    }

    @Override
    public String readFileEncoding(String relativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.readFileEncoding(relativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String zip(String sourceFolderRelativePath, String sourceJeFileType, String sourceJeFileSaveType, String targerFileRelativePath, String jeFileType) {
        return jeFileUtil.zip(sourceFolderRelativePath,sourceJeFileType,sourceJeFileSaveType,targerFileRelativePath,jeFileType);
    }

    @Override
    public String zip(String sourceFolderRelativePath, String sourceJeFileType, String sourceJeFileSaveType, String targerFileRelativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.zip(sourceFolderRelativePath,sourceJeFileType,sourceJeFileSaveType,targerFileRelativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String zipFiles(List<DynaBean> docs, String zipRelativePath, String jeFileType) {
        return jeFileUtil.zipFiles(docs,zipRelativePath,jeFileType);
    }

    @Override
    public String zipFiles(List<DynaBean> docs, String zipRelativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.zipFiles(docs,zipRelativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public void unZip(String sourceFileRelativePath, String sourceJeFileType, String sourceJeFileSaveType, String targerFolderRelativePath, String targerJeFileType) {
        jeFileUtil.unZip(sourceFileRelativePath,sourceJeFileType,sourceJeFileSaveType,targerFolderRelativePath,targerJeFileType);
    }

    @Override
    public void unZip(String sourceFileRelativePath, String sourceJeFileType, String sourceJeFileSaveType, String targerFolderRelativePath, String targerJeFileType, String targerJeFileSaveType) {
        jeFileUtil.unZip(sourceFileRelativePath,sourceJeFileType,sourceJeFileSaveType,targerFolderRelativePath,targerJeFileType,targerJeFileSaveType);
    }

    @Override
    public String downloadFile(String httpFileUrl, String targerRelativePath, String jeFileType) {
        return jeFileUtil.downloadFile(httpFileUrl,targerRelativePath,jeFileType);
    }

    @Override
    public String downloadFile(String httpFileUrl, String targerRelativePath, String jeFileType, String jeFileSaveType) {
        return jeFileUtil.downloadFile(httpFileUrl,targerRelativePath,jeFileType,jeFileSaveType);
    }

    @Override
    public String getFileTypeSuffix(String filePath) {
        return jeFileUtil.getFileTypeSuffix(filePath);
    }

    @Override
    public FileType getFileType(String fileType) {
        return jeFileUtil.getFileType(fileType);
    }

    @Override
    public Boolean isImage(String filePath) {
        return jeFileUtil.isImage(filePath);
    }

    @Override
    public String getImageInfo(String filePath, File file) {
        return jeFileUtil.getImageInfo(filePath,file);
    }

    @Override
    public String getImageInfo(String filePath, InputStream io) {
        return jeFileUtil.getImageInfo(filePath,io);
    }

    @Override
    public FileType getFileTypeInfo(String fileType) {
        // TODO Auto-generated method stub
        FileType typeInfo=new FileType();
        FileType typeVo=jeFileUtil.getFileType(fileType);
        typeInfo.setName(typeVo.getName());
        typeInfo.setIconCls(typeVo.getIconCls());
//        typeInfo.setBigIconCls(typeVo.getBigIconCls());
//        String thumbnailCls=typeVo.getThumbnailCls();
//        if(StringUtil.isNotEmpty(typeVo.getThumbnailCls())){
//            thumbnailCls="<div class=\""+typeVo.getThumbnailCls()+"\" > </div>";
//        }else{
//            String imgUrl="/je/doc/document/doLoadFile?path="+filePath+"&jeFileType="+jeFileType+"&type="+docType+"&jeSaveFileType="+jeSaveFileType+"&beanTableCode="+beanTableCode;
//            if(StringUtil.isNotEmpty(privateUrl)){
//                imgUrl=privateUrl;
//            }
//            thumbnailCls="<img width=96 height=96 src=\""+imgUrl+"\" />";
//        }
//        typeInfo.setThumbnailCls(thumbnailCls);
        return typeInfo;
    }
//    @Override
//    public Map uploadedFiles(String folderPath, String fullFilePath, String newFileName, MultipartFile multipartFile, String jeFileType) {
//        Map<String, Object> map = new HashMap();
//        String filePath=folderPath + "/" + newFileName;
//        if(StringUtil.isNotEmpty(fullFilePath)) {
//            filePath = fullFilePath;
//        }
//        saveFile(filePath,multipartFile,jeFileType);
////        if(JEFileSaveType.ALIYUN.equals(WebUtils.getProjectStorageType()) && JEFileType.PROJECT.equals(jeFileType)){
////            map = AliOSSUtil.putObjectForMultipartFile(folderPath + "/" + newFileName, multipartFile);
////        }else {
////            try {
////                File dst ;
////                String filePath;
////                if(StringUtil.isNotEmpty(fullFilePath)){
////                    filePath=fullFilePath;
////                    dst=new File(getBasePath(jeFileType)+fullFilePath);
////                }else {
////                    File folder = new File(getBasePath(jeFileType)+folderPath);
////                    if(!folder.exists()) {
////                        logger.debug("创建目录：" + folder.toString());
////                        folder.mkdirs();
////                    }
////
////                    dst=new File(getBasePath(jeFileType)+filePath);
////                }
////                multipartFile.transferTo(dst);
////                map = new HashMap<>();
////                map.put("fileLenght",dst);
////                map.put("upUrl",filePath);
////            } catch (IOException e) {
////                throw new PlatformException("文件上传异常!", PlatformExceptionEnum.JE_DOC_FILE_UPLOAD_ERROR,new Object[]{folderPath,fullFilePath,newFileName,jeFileType},e);
////            }
////        }
//        return map;
//    }
//    @Override
//    public String createFile(String relativePath, String context,String encoding, String type) {
//        return jeFileUtil.createFile(relativePath, context,encoding, type);
//    }
//
//    @Override
//    public String saveFile(File file, String relativePath, String type) {
//        String result = null;
//        if(WebUtils.getProjectStorageType().equals(JEFileSaveType.THIRD) && JEFileType.PROJECT.equals(type)){
//            result=ThirdFileUtil.uploadFile(relativePath,file);
//        }else{
//            result = jeFileUtil.saveFile(file, relativePath, type);
//        }
//        return result;
//    }
//
//    @Override
//    public String saveFile(File file,String fileName, String relativePath, String type) {
//        String result = null;
//        if(JEFileSaveType.THIRD.equals(WebUtils.getProjectStorageType()) && JEFileType.PROJECT.equals(type)){
//            result=ThirdFileUtil.uploadFile(relativePath,file);
//        }else{
//            result = jeFileUtil.saveFile(file, relativePath, type);
//        }
//        return result;
//    }
//
//    @Override
//    public Map<String, Object> saveFile(String relativePath, MultipartFile file, String jeFileType) {
//        Map<String, Object> map = new HashMap<>();
//        InputStream content = null;
//        try {
//            content = file.getInputStream();
//            long length=file.getSize();
//            if(JEFileType.PROJECT.equals(jeFileType) && JEFileSaveType.THIRD.equals(WebUtils.getProjectStorageType())){
//                String privateKey= ThirdFileUtil.uploadFile(relativePath,content);
//                map.put("privateKey", privateKey);
//            }else{
//                jeFileUtil.saveFile(content,relativePath,jeFileType);
//            }
//            map.put("fileLength", length);
//            map.put("upUrl",relativePath);
//            return map;
//        } catch (Exception e) {
////            e.printStackTrace();
//            throw new PlatformException("文件上传异常!", PlatformExceptionEnum.JE_DOC_FILE_UPLOAD_ERROR,new Object[]{relativePath,jeFileType},e);
//        } finally {
//            closeInputStream(content);
//        }
//    }
//
//    @Override
//    public String saveBase64File(String base64Str, String relativePath, String type) {
//        return jeFileUtil.saveBase64File(base64Str,relativePath,type);
//    }
//
//    @Override
//    public String copyFile(String sourceRelativePath,String targerRelativePath, String type) {
//        String result = null;
//        if(WebUtils.getProjectStorageType().equals(JEFileSaveType.FASTDFS) && JEFileType.PROJECT.equals(type)){
//            result = FastDfsUtil.getInstance().doCopyFile(sourceRelativePath);
//        }else{
//            result = jeFileUtil.copyFile(sourceRelativePath, targerRelativePath, type);
//        }
//        return result;
//    }
//
//    @Override
//    public String copyFile(String sourceRelativePath,String targerRelativePath,Boolean createParentFolder, String type) {
//        String result = null;
//        if(WebUtils.getProjectStorageType().equals(JEFileSaveType.FASTDFS) && JEFileType.PROJECT.equals(type)){
//            String basePath=jeFileUtil.getBasePath(type);
//            File file=new File(basePath+targerRelativePath);
//            if(createParentFolder){
//                File parentFolder=file.getParentFile();
//                if(!parentFolder.exists()){
//                    parentFolder.mkdirs();
//                }
//            }
//            result = FastDfsUtil.getInstance().doCopyFile(sourceRelativePath);
//        }else{
//            result = jeFileUtil.copyFile(sourceRelativePath, targerRelativePath,createParentFolder, type);
//        }
//        return result;
//    }
//
//    @Override
//    public String copyFile(String sourceRelativePath,String targerRelativePath,Boolean createParentFolder,String sourceType,String targerType) {
//        String result = null;
//        if(WebUtils.getProjectStorageType().equals(JEFileSaveType.FASTDFS) && (JEFileType.PROJECT.equals(sourceType) || JEFileType.PROJECT.equals(targerType))){
//            if(JEFileType.PROJECT.equals(sourceType) && JEFileType.PROJECT.equals(targerType)){//都是项目文件
//                result = FastDfsUtil.getInstance().doCopyFile(sourceRelativePath);
//            }
//            if(!JEFileType.PROJECT.equals(sourceType) && JEFileType.PROJECT.equals(targerType)){//源文件不是项目文件
//                String sourceBasePath=jeFileUtil.getBasePath(sourceType);
//                String filePath = sourceBasePath + sourceRelativePath;
//                result = FastDfsUtil.getInstance().doPathUploadFile(filePath);
//            }
//            if(JEFileType.PROJECT.equals(sourceType) && !JEFileType.PROJECT.equals(targerType)){//目标文件不是项目文件
//                byte[] sourceFileBytes = FastDfsUtil.getInstance().doDownload(sourceRelativePath);
//                String targetBasePath = jeFileUtil.getBasePath(targerType);
//                File targetFile = new File(targetBasePath + targerRelativePath);
//                FileOutputStream fos = null;
//                try {
//                    fos = new FileOutputStream(targetFile);
//                    fos.write(sourceFileBytes);
//                    fos.flush();
//                    result = targerRelativePath;
//                } catch (FileNotFoundException e) {
//                   throw new PlatformException("文件复制异常!",PlatformExceptionEnum.JE_DOC_FILE_COPY_ERROR,new Object[]{sourceRelativePath,targerRelativePath,createParentFolder,sourceType,targerType},e);
//                } catch (IOException e) {
//                    throw new PlatformException("文件复制异常!",PlatformExceptionEnum.JE_DOC_FILE_COPY_ERROR,new Object[]{sourceRelativePath,targerRelativePath,createParentFolder,sourceType,targerType},e);
//                }finally{
//                   closeOutputStream(fos);
//                }
//            }
//        //将本地文件赋值到阿里云
//        }else if(WebUtils.getProjectStorageType().equals(JEFileSaveType.ALIYUN) && (JEFileType.PLATFORM.equals(sourceType) && JEFileType.PROJECT.equals(targerType))){
//            return AliOSSUtil.putObjectForFile(targerRelativePath,readFile(sourceRelativePath,sourceType));
//        }else{
//            result = jeFileUtil.copyFile(sourceRelativePath, targerRelativePath,createParentFolder,sourceType,targerType);
//        }
//        return result;
//    }
//
//    @Override
//    public String copyFile(String sourceRelativePath,String targerRelativePath, Boolean createParentFolder,String sourceType, String sourceSaveType, String targerType) {
//        if(WebUtils.getProjectStorageType().equals(JEFileSaveType.FASTDFS) && JEFileType.PROJECT.equals(targerType)){
//
//        //如果阿里云文件拷贝到阿里云文件
//        }else if(JEFileSaveType.ALIYUN.equals(sourceSaveType) && JEFileSaveType.ALIYUN.equals(WebUtils.getProjectStorageType()) && JEFileType.PROJECT.equals(targerType)){
//            return AliOSSUtil.copyObject(sourceRelativePath,targerRelativePath);
//        //如果阿里云存储，赋值给其他格式
//        }else if(JEFileSaveType.ALIYUN.equals(sourceSaveType)){
//            InputStream inputStream=AliOSSUtil.getObject(sourceRelativePath);
//            return jeFileUtil.saveFile(inputStream,targerRelativePath,targerType);
//        }else{
//            return jeFileUtil.copyFile(sourceRelativePath, targerRelativePath,createParentFolder,sourceType,targerType);
//        }
//
//        return null;
//    }
//    @Override
//    public String downloadFile(String httpFileUrl, String targerRelativePath,String type) {
//        return jeFileUtil.downloadFile(httpFileUrl, targerRelativePath, type);
//    }
//    @Override
//    public void deleteFile(DynaBean doc) {
//        String address=doc.getStr("DOCUMENT_ADDRESS");
//        String busType=doc.getStr("DOCUMENT_BUSTYPE");
//        String saveType=doc.getStr("DOCUMENT_SAVETYPE");
//        if(StringUtil.isEmpty(busType)){
//            busType = JEFileType.PROJECT;
//        }
//        if(StringUtil.isEmpty(saveType)){
//            saveType = JEFileSaveType.DEFAULT;
//        }
//        deleteFile(address, busType, saveType);
//    }
//    @Override
//    public void deleteFile(String relativePath, String type) {
//        if(StringUtil.isNotEmpty(relativePath)) {
//            jeFileUtil.deleteFile(relativePath, type);
//        }
////		if(WebUtils.getProjectStorageType().equals(JEStorageType.FASTDFS.getValue()) && JEFileType.PROJECT.equals(type)){
////			FastDfsUtil.getInstance().doDelete(relativePath);
////		}else{
////		}
//    }
//    @Override
//    public void deleteFile(String relativePath, String type, String saveType) {
//        // TODO Auto-generated method stub
//        jeFileUtil.deleteFile(relativePath, type, saveType);
//    }
//    @Override
//    public void createFolder(String relativePath, String type) {
//        jeFileUtil.createFolder(relativePath, type);
//    }
//
//    @Override
//    public void deleteFolder(String relativePath, String type) {
//        jeFileUtil.deleteFolder(relativePath, type);
//    }
//
//    @Override
//    public void zip(String sourceFolderRelativePath,String targerFileRelativePath, String type) {
//        jeFileUtil.zip(sourceFolderRelativePath, targerFileRelativePath, type);
//    }
//
//    @Override
//    public void unZip(String sourceFileRelativePath,String targerFolderRelativePath, String type) {
//        jeFileUtil.unZip(sourceFileRelativePath, targerFolderRelativePath, type);
//    }
//    @Override
//    public void zipFiles(List<DynaBean> docs,String zipRelativePath,String jeFileType)  throws IOException {
//        // TODO Auto-generated method stub
//        jeFileUtil.zipFiles(docs,zipRelativePath,jeFileType);
//    }
//    @Override
//    public void unZip(String sourceFileRelativePath,String targerFolderRelativePath, String type, String sourceSaveType) {
//        // TODO Auto-generated method stub
//        jeFileUtil.unZip(sourceFileRelativePath, targerFolderRelativePath, type, sourceSaveType);
//    }
//
//    @Override
//    public String readTxt(String relativePath, String encoding, String hh,String type) {
//        return jeFileUtil.readTxt(relativePath, encoding, hh, type);
//
//    }
//    @Override
//    public String readTxt(String relativePath, String encoding, String hh,String type, String saveType) {
//        // TODO Auto-generated method stub
//        return jeFileUtil.readTxt(relativePath, encoding, hh, type, saveType);
//    }
//    @Override
//    public String readFileEncoding(String relativePath,String type) {
//        return jeFileUtil.readFileEncoding(relativePath, type);
//
//    }
//    @Override
//    public String readFileEncoding(String relativePath, String type, String saveType) {
//        // TODO Auto-generated method stub
//        return jeFileUtil.readFileEncoding(relativePath,type, saveType);
//    }
//
//    @Override
//    public void closeInputStream(InputStream inputStream) {
//        jeFileUtil.closeInputStream(inputStream);
//    }
//
//    @Override
//    public void closeOutputStream(OutputStream outputStream) {
//        jeFileUtil.closeOutputStream(outputStream);
//    }
//
//    @Override
//    public File readFile(String relativePath, String type) {
//        // TODO Auto-generated method stub
//        return jeFileUtil.readFile(relativePath, type);
//    }
//
//    @Override
//    public File readFile(String relativePath, String type, String saveType) {
//        // TODO Auto-generated method stub
//        return jeFileUtil.readFile(relativePath, type, saveType);
//    }
//
//    @Override
//    public InputStream readFileIo(String relativePath, String type,
//                                  String saveType) throws FileNotFoundException {
//        // TODO Auto-generated method stub
//        return jeFileUtil.readFileIo(relativePath, type, saveType);
//    }
//    @Override
//    public Boolean existsFile(DynaBean document) {
//        // TODO Auto-generated method stub
//        String address=document.getStr("DOCUMENT_ADDRESS");
//        String busType=document.getStr("DOCUMENT_BUSTYPE");
//        String saveType=document.getStr("DOCUMENT_SAVETYPE");
//        if(StringUtil.isEmpty(busType)){
//            busType = JEFileType.PROJECT;
//        }
//        if(StringUtil.isEmpty(saveType)){
//            saveType = JEFileSaveType.DEFAULT;
//        }
//        return jeFileUtil.existsFile(address,busType,saveType);
//    }
//    @Override
//    public Boolean existsFile(String relativePath, String type, String saveType) {
//        // TODO Auto-generated method stub
//        return jeFileUtil.existsFile(relativePath,type,saveType);
////        if(JEFileSaveType.ALIYUN.equalsIgnoreCase(saveType)){
////            return AliOSSUtil.existObject(relativePath);
////        }else {
////            return jeFileUtil.existsFile(relativePath, type, saveType);
////        }
//    }
//    @Override
//    public InputStream readFileIo(String relativePath, String type) throws FileNotFoundException {
//        // TODO Auto-generated method stub
//        return jeFileUtil.readFileIo(relativePath, type);
//    }
//    @Override
//    public String getBasePath(String type) {
//        return jeFileUtil.getBasePath(type);
//    }
//
//    @Override
//    public Boolean existsFile(String relativePath, String type) {
//        return jeFileUtil.existsFile(relativePath,type);
////        if(JEFileSaveType.ALIYUN.equalsIgnoreCase(WebUtils.getProjectStorageType()) && type.equalsIgnoreCase(JEFileType.PROJECT)){
////            return AliOSSUtil.existObject(relativePath);
////        }else {
////            return jeFileUtil.existsFile(relativePath, type);
////        }
//    }
//
}
