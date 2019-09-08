/**
 *
 */
package com.je.document.service;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.je.core.base.JERequestWrapper;
import com.je.cache.service.config.ConfigCacheManager;
import com.je.cache.service.doc.DocumentCacheManager;
import com.je.core.base.MethodArgument;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.util.SecurityUserHolder;
import com.je.document.util.JeFileUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.je.cache.service.doc.FileTypeCacheManager;
import com.je.core.constants.DocumentType;
import com.je.core.constants.doc.JEFileSaveType;
import com.je.core.constants.doc.JEFileType;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.DateUtils;
import com.je.core.util.JEUUID;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.document.vo.FileType;
import com.je.rbac.model.Department;
import com.je.rbac.model.EndUser;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 文档管理业务逻辑实现
 * @author zhangshuaipeng
 * 2013年6月15日 23:03:41
 *
 */
@Service("docManager")
public class DocumentManagerImpl implements DocumentManager {
	private static Logger logger = LoggerFactory.getLogger(DocumentManagerImpl.class);
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;
	@Autowired
	private PCServiceTemplate pcServiceTemplate;
	@Autowired
	private JeFileManager jeFileManager;

	@Override
	public void doBuildFileInfo(MethodArgument param) {
		HttpServletRequest request = param.getRequest();
		String path = param.getPath();
		String type =param.getType();
		String jeFileSaveType= param.getJeFileSaveType();
		String pkValue=param.getPkValue();
		String fileName=param.getFileName();
		String jeFileType=param.getJeFileType();
		String contextType=param.getContextType();
		DynaBean dynaBean = param.getDynaBean();
		String beanTableCode=request.getParameter("beanTableCode");
		if(StringUtil.isEmpty(type)){
			type="doc";
		}
		if(StringUtil.isEmpty(path) && StringUtil.isNotEmpty(dynaBean.getStr("DOCUMENT_ADDRESS"))){
			path=dynaBean.getStr("DOCUMENT_ADDRESS");
		}
		String realPath=path;
		//文件库
		if("docinfo".equals(type)){
			if(StringUtil.isNotEmpty(pkValue)){
				dynaBean = serviceTemplate.selectOneByPk("JE_SYS_DOCUMENTATION", pkValue, JeFileUtil.queryDocFields);
			}else{
				dynaBean = serviceTemplate.selectOne("JE_SYS_DOCUMENTATION", " AND DOCUMENTATION_ADDRESS='"+realPath+"'",JeFileUtil.queryDocFields);
			}
			if(dynaBean!=null){
				path=dynaBean.getStr("DOCUMENTATION_ADDRESS");
				fileName = dynaBean.getStr("DOCUMENTATION_TEXT");
				jeFileType = dynaBean.getStr("DOCUMENTATION_BUSTYPE");
				contextType=dynaBean.getStr("DOCUMENTATION_FORMAT");
				realPath=dynaBean.getStr("DOCUMENTATION_ADDRESS");
				if(StringUtil.isEmpty(contextType)){
					contextType="application/octet-stream";
				}
				if(StringUtil.isNotEmpty(dynaBean.getStr("DOCUMENTATION_SAVETYPE"))){
					jeFileSaveType=dynaBean.getStr("DOCUMENTATION_SAVETYPE");
				}
				if(StringUtil.isEmpty(jeFileType)){
					jeFileType=JEFileType.PROJECT;
				}
			}
			//附件子功能
		}else if("docfile".equals(type)){
			if(StringUtil.isNotEmpty(pkValue)){
				dynaBean = serviceTemplate.selectOneByPk("JE_SYS_FILE", pkValue, JeFileUtil.queryDocFileFields);
			}else{
				dynaBean = serviceTemplate.selectOne("JE_SYS_FILE", " AND FILE_ADDRESS='"+realPath+"'",JeFileUtil.queryDocFileFields);
			}
			if(dynaBean!=null){
				path=dynaBean.getStr("FILE_ADDRESS");
				fileName = dynaBean.getStr("FILE_NAME");
				jeFileType = dynaBean.getStr("FILE_BUSTYPE");
				contextType=dynaBean.getStr("FILE_FORMAT");
				realPath=dynaBean.getStr("FILE_ADDRESS");
				if(StringUtil.isEmpty(contextType)){
					contextType="application/octet-stream";
				}
				if(StringUtil.isNotEmpty(dynaBean.getStr("FILE_SAVETYPE"))){
					jeFileSaveType=dynaBean.getStr("FILE_SAVETYPE");
				}
				if(StringUtil.isEmpty(jeFileType)){
					jeFileType=JEFileType.PROJECT;
				}
			}
		}else{
			if(StringUtil.isNotEmpty(path)) {
				dynaBean =getDocInfo(path,beanTableCode);
			}else if(StringUtil.isNotEmpty(pkValue)){
				dynaBean=getDocInfoByPk(pkValue,beanTableCode);
			}
			if(dynaBean!=null){
				fileName = dynaBean.getStr("DOCUMENT_DOCNAME");
				jeFileType = dynaBean.getStr("DOCUMENT_BUSTYPE");
				contextType=dynaBean.getStr("DOCUMENT_FORMAT");
				realPath=dynaBean.getStr("DOCUMENT_ADDRESS");
				if(StringUtil.isEmpty(contextType)){
					contextType="application/octet-stream";
				}
				if(StringUtil.isNotEmpty(dynaBean.getStr("DOCUMENT_SAVETYPE"))){
					jeFileSaveType=dynaBean.getStr("DOCUMENT_SAVETYPE");
				}
				if(StringUtil.isEmpty(jeFileType)){
					jeFileType=JEFileType.PROJECT;
				}
			}
		}
		if(StringUtil.isEmpty(jeFileType)){
			jeFileType = JEFileType.PROJECT;
		}
		param.setJeFileType(jeFileType);
		param.setJeFileSaveType(jeFileSaveType);
		param.setRealPath(realPath);
		param.setPath(path);
		param.setContextType(contextType);
		param.setFileName(fileName);
	}

	@Override
	public void doRemoveDocuments(String domainName, String domainField, String domainId) {
		String whereSql = null;
		if(StringUtil.isEmpty(domainField)) {
			whereSql = " and DOCUMENT_TABLECODE = '" + domainName + "' and DOCUMENT_PKVALUE IN (" + StringUtil.buildArrayToString(StringUtil.getDefaultValue(domainId,"").split(",")) + ")";
		} else {
			whereSql = " and DOCUMENT_TABLECODE = '" + domainName + "' and DOCUMENT_FIELDCODE = '" + domainField + "' and DOCUMENT_PKVALUE IN (" + StringUtil.buildArrayToString(StringUtil.getDefaultValue(domainId,"").split(",")) + ")";
		}
		doRemoveDocuments4Sql(whereSql,domainName);
	}

	@Override
	public void doRemoveDocuments4Sql(String whereSql,String beanTableCode) {
		// TODO Auto-generated method stub
		List<DynaBean> docs = getDocInfoList(whereSql,beanTableCode);
		for(DynaBean d : docs) {
			jeFileManager.deleteFile(d);
			String pkValue=d.getStr("JE_CORE_DOCUMENT_ID");
			String path=d.getStr("DOCUMENT_ADDRESS");
			DocumentCacheManager.removeCache(path);
			DocumentCacheManager.removeCache(pkValue);
		}
		deleteDoc(whereSql,beanTableCode);
	}

	@Override
	public void doRemoveTreeDocuments(String domainName, String domainIds) {
		String pkName=BeanUtils.getInstance().getPKeyFieldNames(domainName);
		for(String id:domainIds.split(",")){
			List<DynaBean> docs=getDocInfoList(" AND DOCUMENT_TABLECODE='"+domainName+"' AND DOCUMENT_PKVALUE IN (SELECT "+pkName+" FROM "+domainName+" WHERE SY_PATH LIKE '%"+id+"%') AND DOCUMENT_TYPE!='"+DocumentType.CKEDITOR+"'",domainName);
			for(DynaBean doc:docs){
				jeFileManager.deleteFile(doc);
				String pkValue=doc.getStr("JE_CORE_DOCUMENT_ID");
				String path=doc.getStr("DOCUMENT_ADDRESS");
				DocumentCacheManager.removeCache(path);
				DocumentCacheManager.removeCache(pkValue);
			}
			deleteDoc(" AND DOCUMENT_TABLECODE='"+domainName+"' AND DOCUMENT_PKVALUE IN (SELECT "+pkName+" FROM "+domainName+" WHERE SY_PATH LIKE '%"+id+"%') AND DOCUMENT_TYPE!='"+DocumentType.CKEDITOR+"'",domainName);
		}
	}

	@Override
	public void doRemoveDocuments(String domainName, String domainId) {
		doRemoveDocuments(domainName, null, domainId);
	}

	@Override
	public String doCopyDocument(DynaBean dynaBean, String fieldCode,String pkValue) {
		// TODO Auto-generated method stub
		List<DynaBean> documents=getDocInfoList(" AND DOCUMENT_TABLECODE='"+dynaBean.getStr(BeanUtils.KEY_TABLE_CODE)+"' AND DOCUMENT_FIELDCODE='"+fieldCode+"' AND DOCUMENT_PKVALUE='"+dynaBean.getPkValue()+"'",dynaBean.getStr(BeanUtils.KEY_TABLE_CODE));
		String value="";
		for(DynaBean document:documents){
			DynaBean newDoc=new DynaBean("JE_CORE_DOCUMENT",false);
			newDoc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DOCUMENT_ID");
			newDoc.set("DOCUMENT_FUNCCODE", document.getStr("DOCUMENT_FUNCCODE"));
			newDoc.set("DOCUMENT_FILETYPENAME", document.getStr("DOCUMENT_FILETYPENAME"));
			newDoc.set("DOCUMENT_FILETYPE", document.getStr("DOCUMENT_FILETYPE"));
			newDoc.set("DOCUMENT_FIELDCODE", document.getStr("DOCUMENT_FIELDCODE"));
			newDoc.set("DOCUMENT_TABLECODE", document.getStr("DOCUMENT_TABLECODE"));
			newDoc.set("DOCUMENT_DOCSIZE", document.get("DOCUMENT_DOCSIZE"));
			newDoc.set("DOCUMENT_FORMAT", document.getStr("DOCUMENT_FORMAT"));
			newDoc.set("DOCUMENT_DOCNAME", document.getStr("DOCUMENT_DOCNAME"));
			newDoc.set("DOCUMENT_TYPE", document.getStr("DOCUMENT_TYPE"));
			newDoc.set("DOCUMENT_ICONCLS", document.getStr("DOCUMENT_ICONCLS"));
			newDoc.set("DOCUMENT_BIGICONCLS", document.getStr("DOCUMENT_BIGICONCLS"));
			newDoc.set("DOCUMENT_THUMBNAILCLS", document.getStr("DOCUMENT_THUMBNAILCLS"));
			newDoc.set("DOCUMENT_BUSTYPE", document.getStr("DOCUMENT_BUSTYPE"));
			newDoc.set("DOCUMENT_SAVETYPE",JeFileUtil.getFileSaveType());
			newDoc.set("DOCUMENT_PKVALUE", pkValue);
			serviceTemplate.buildModelCreateInfo(newDoc);
			serviceTemplate.buildModelModifyInfo(newDoc);
			String filePath=document.getStr("DOCUMENT_ADDRESS");
			String uploadPath = WebUtils.getConfigVar("struts.upload.path");
			String newFilePath=getFolderPath("", "", uploadPath)+"/"+JEUUID.uuid()+"."+jeFileManager.getFileTypeSuffix(document.getStr("DOCUMENT_DOCNAME"));
			String privateUrl=jeFileManager.copyFile(filePath, document.getStr("DOCUMENT_BUSTYPE"), document.getStr("DOCUMENT_SAVETYPE"),newFilePath,document.getStr("DOCUMENT_BUSTYPE"));
			newDoc.set("DOCUMENT_PRIVATEKEY",privateUrl);
			newDoc.set("DOCUMENT_ADDRESS", newFilePath);
			buildIconCls(newDoc);
			insertDoc(newDoc);
			value=newDoc.getStr("DOCUMENT_DOCNAME")+"*"+newDoc.getStr("DOCUMENT_ADDRESS");
		}
		return value;
	}

	@Override
	public String doCopyDocument(DynaBean dynaBean, String fieldCode,String newFuncCode, String newTableCode, String newFieldCode,String newPkValue) {
		// TODO Auto-generated method stub
		List<DynaBean> documents=getDocInfoList(" AND DOCUMENT_TABLECODE='"+dynaBean.getStr(BeanUtils.KEY_TABLE_CODE)+"' AND DOCUMENT_FIELDCODE='"+fieldCode+"' AND DOCUMENT_PKVALUE='"+dynaBean.getPkValue()+"'",dynaBean.getStr(BeanUtils.KEY_TABLE_CODE));
		String value="";
		for(DynaBean document:documents){
			DynaBean newDoc=new DynaBean("JE_CORE_DOCUMENT",false);
			newDoc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DOCUMENT_ID");
			newDoc.set("DOCUMENT_FUNCCODE", newFuncCode);
			newDoc.set("DOCUMENT_FILETYPENAME", document.getStr("DOCUMENT_FILETYPENAME"));
			newDoc.set("DOCUMENT_FILETYPE", document.getStr("DOCUMENT_FILETYPE"));
			newDoc.set("DOCUMENT_FIELDCODE", newFieldCode);
			newDoc.set("DOCUMENT_TABLECODE", newTableCode);
			newDoc.set("DOCUMENT_DOCSIZE", document.get("DOCUMENT_DOCSIZE"));
			newDoc.set("DOCUMENT_FORMAT", document.getStr("DOCUMENT_FORMAT"));
			newDoc.set("DOCUMENT_DOCNAME", document.getStr("DOCUMENT_DOCNAME"));
			newDoc.set("DOCUMENT_TYPE", document.getStr("DOCUMENT_TYPE"));
			newDoc.set("DOCUMENT_ICONCLS", document.getStr("DOCUMENT_ICONCLS"));
			newDoc.set("DOCUMENT_BIGICONCLS", document.getStr("DOCUMENT_BIGICONCLS"));
			newDoc.set("DOCUMENT_THUMBNAILCLS", document.getStr("DOCUMENT_THUMBNAILCLS"));
			newDoc.set("DOCUMENT_BUSTYPE", document.getStr("DOCUMENT_BUSTYPE"));
			newDoc.set("DOCUMENT_SAVETYPE", JeFileUtil.getFileSaveType());
			newDoc.set("DOCUMENT_PKVALUE", newPkValue);
			serviceTemplate.buildModelCreateInfo(newDoc);
			serviceTemplate.buildModelModifyInfo(newDoc);
			String filePath=document.getStr("DOCUMENT_ADDRESS");
			String uploadPath = WebUtils.getConfigVar("struts.upload.path");
			String newFilePath=getFolderPath("", "", uploadPath)+"/"+JEUUID.uuid()+"."+jeFileManager.getFileTypeSuffix(document.getStr("DOCUMENT_DOCNAME"));
			String privateUrl=jeFileManager.copyFile(filePath, document.getStr("DOCUMENT_BUSTYPE"), document.getStr("DOCUMENT_SAVETYPE"),newFilePath, document.getStr("DOCUMENT_BUSTYPE"));
			newDoc.set("DOCUMENT_ADDRESS", newFilePath);
			newDoc.set("DOCUMENT_PRIVATEKEY",privateUrl);
			buildIconCls(newDoc);
			insertDoc(newDoc);
			value=newDoc.getStr("DOCUMENT_DOCNAME")+"*"+newDoc.getStr("DOCUMENT_ADDRESS");
		}
		return value;
	}

	@Override
	public DynaBean doSaveDcoument(String relativeFilePath, String jeFileType,String funcCode, String tableCode, String fieldCode,String pkValue, String fileName, String address) {
		File file=jeFileManager.readFile(relativeFilePath, jeFileType);
		return doSaveDcoument(file, funcCode, tableCode, fieldCode, pkValue, fileName, address,jeFileType,DocumentType.BEAN);
	}

	@Override
	public DynaBean doSaveDcoument(File file, String funcCode, String tableCode,String fieldCode,String pkValue, String fileName, String address,String jeFileType,String type) {
		String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
		DynaBean doc = new DynaBean("JE_CORE_DOCUMENT",false);
		doc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DOCUMENT_ID");
		doc.set("DOCUMENT_TABLECODE", tableCode);
		doc.set("DOCUMENT_FIELDCODE", fieldCode);
		doc.set("DOCUMENT_PKVALUE", pkValue);
		doc.set("DOCUMENT_ADDRESS", address);
		doc.set("DOCUMENT_PRIVATEKEY", "");
		doc.set("DOCUMENT_DOCSIZE", file.length());
		doc.set("DOCUMENT_DOCNAME", fileName);
		doc.set("DOCUMENT_FORMAT", "");
		doc.set("DOCUMENT_TYPE", type);
		doc.set("DOCUMENT_FUNCCODE", funcCode);
		doc.set("DOCUMENT_FILETYPE", fileType);
		doc.set("DOCUMENT_BUSTYPE", jeFileType);
		doc.set("DOCUMENT_SAVETYPE",JeFileUtil.getFileSaveType());
		doc.set("SY_STATUS", "1");
		//构建文件类型 信息       包括图标   大图标    文件格式名
		FileType typeVo=jeFileManager.getFileTypeInfo(fileType);
		doc.set("DOCUMENT_FILETYPENAME", typeVo.getName());
		doc.set("DOCUMENT_ICONCLS", typeVo.getIconCls());
		if(jeFileManager.isImage(address)){
			doc.set("DOCUMENT_THUMBNAILCLS", jeFileManager.getImageInfo(address,file));
		}
//		doc.set("DOCUMENT_BIGICONCLS", typeVo.getBigIconCls());
//		doc.set("DOCUMENT_THUMBNAILCLS", typeVo.getThumbnailCls());
//		if(StringUtil.isEmpty(doc.getStr("DOCUMENT_FORMAT"))){
//			doc.set("DOCUMENT_FORMAT", typeVo.getFormat());
//		}
		serviceTemplate.buildModelCreateInfo(doc);
		doc=insertDoc(doc);
		return doc;
	}

	@Override
	public String doCopyBatchDocument(DynaBean dynaBean, String fieldCode,String pkValue) {
		// TODO Auto-generated method stub
		List<DynaBean> documents=getDocInfoList(" AND DOCUMENT_TABLECODE='"+dynaBean.getStr(BeanUtils.KEY_TABLE_CODE)+"' AND DOCUMENT_FIELDCODE='"+fieldCode+"' AND DOCUMENT_PKVALUE='"+dynaBean.getPkValue()+"' AND DOCUMENT_TYPE='"+DocumentType.BATCH+"'",dynaBean.getStr(BeanUtils.KEY_TABLE_CODE));
		JSONArray arrays=new JSONArray();
		for(DynaBean document:documents){
			DynaBean newDoc=new DynaBean("JE_CORE_DOCUMENT",false);
			newDoc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DOCUMENT_ID");
			newDoc.set("DOCUMENT_FUNCCODE", document.getStr("DOCUMENT_FUNCCODE"));
			newDoc.set("DOCUMENT_FILETYPENAME", document.getStr("DOCUMENT_FILETYPENAME"));
			newDoc.set("DOCUMENT_FILETYPE", document.getStr("DOCUMENT_FILETYPE"));
			newDoc.set("DOCUMENT_FIELDCODE", document.getStr("DOCUMENT_FIELDCODE"));
			newDoc.set("DOCUMENT_TABLECODE", document.getStr("DOCUMENT_TABLECODE"));
			newDoc.set("DOCUMENT_DOCSIZE", document.get("DOCUMENT_DOCSIZE"));
			newDoc.set("DOCUMENT_FORMAT", document.getStr("DOCUMENT_FORMAT"));
			newDoc.set("DOCUMENT_DOCNAME", document.getStr("DOCUMENT_DOCNAME"));
			newDoc.set("DOCUMENT_TYPE", document.getStr("DOCUMENT_TYPE"));
			newDoc.set("DOCUMENT_ICONCLS", document.getStr("DOCUMENT_ICONCLS"));
			newDoc.set("DOCUMENT_BIGICONCLS", document.getStr("DOCUMENT_BIGICONCLS"));
			newDoc.set("DOCUMENT_THUMBNAILCLS", document.getStr("DOCUMENT_THUMBNAILCLS"));
			newDoc.set("DOCUMENT_BUSTYPE", document.getStr("DOCUMENT_BUSTYPE"));
			newDoc.set("DOCUMENT_SAVETYPE", JeFileUtil.getFileSaveType());
			newDoc.set("DOCUMENT_PKVALUE", pkValue);
			serviceTemplate.buildModelCreateInfo(newDoc);
			serviceTemplate.buildModelModifyInfo(newDoc);
			String filePath=document.getStr("DOCUMENT_ADDRESS");
			String uploadPath = WebUtils.getConfigVar("struts.upload.path");
			String newFilePath=getFolderPath("", "", uploadPath)+"/"+JEUUID.uuid()+"."+jeFileManager.getFileTypeSuffix(document.getStr("DOCUMENT_DOCNAME"));
			String privateUrl=jeFileManager.copyFile(filePath, document.getStr("DOCUMENT_BUSTYPE"), document.getStr("DOCUMENT_SAVETYPE"), newFilePath, document.getStr("DOCUMENT_BUSTYPE"));
			newDoc.set("DOCUMENT_PRIVATEKEY",privateUrl);
			newDoc.set("DOCUMENT_ADDRESS", newFilePath);
			buildIconCls(newDoc);
			newDoc=insertDoc(newDoc);
			JSONObject values=new JSONObject();
			values.put("id", newDoc.getStr("JE_CORE_DOCUMENT_ID"));
			values.put("name",newDoc.getStr("DOCUMENT_DOCNAME"));
			values.put("cls", newDoc.getStr("DOCUMENT_BIGICONCLS"));
			values.put("path", newDoc.getStr("DOCUMENT_ADDRESS"));
			values.put("extend", newDoc.getStr("DOCUMENT_THUMBNAILCLS"));
			arrays.add(values);
		}
		return arrays.toString();
	}

	@Override
	public String doCopyBatchDocument(DynaBean dynaBean, String fieldCode,String newFuncCode, String newTableCode, String newFieldCode,String newPkValue) {
		// TODO Auto-generated method stub
		List<DynaBean> documents=getDocInfoList(" AND DOCUMENT_TABLECODE='"+dynaBean.getStr(BeanUtils.KEY_TABLE_CODE)+"' AND DOCUMENT_FIELDCODE='"+fieldCode+"' AND DOCUMENT_PKVALUE='"+dynaBean.getPkValue()+"' AND DOCUMENT_TYPE='"+DocumentType.BATCH+"'",dynaBean.getStr(BeanUtils.KEY_TABLE_CODE));
		JSONArray arrays=new JSONArray();
		for(DynaBean document:documents){
			DynaBean newDoc=new DynaBean("JE_CORE_DOCUMENT",false);
			newDoc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DOCUMENT_ID");
			newDoc.set("DOCUMENT_FUNCCODE", newFuncCode);
			newDoc.set("DOCUMENT_FILETYPENAME", document.getStr("DOCUMENT_FILETYPENAME"));
			newDoc.set("DOCUMENT_FILETYPE", document.getStr("DOCUMENT_FILETYPE"));
			newDoc.set("DOCUMENT_FIELDCODE", newFieldCode);
			newDoc.set("DOCUMENT_TABLECODE", newTableCode);
			newDoc.set("DOCUMENT_DOCSIZE", document.get("DOCUMENT_DOCSIZE"));
			newDoc.set("DOCUMENT_FORMAT", document.getStr("DOCUMENT_FORMAT"));
			newDoc.set("DOCUMENT_DOCNAME", document.getStr("DOCUMENT_DOCNAME"));
			newDoc.set("DOCUMENT_TYPE", document.getStr("DOCUMENT_TYPE"));
			newDoc.set("DOCUMENT_ICONCLS", document.getStr("DOCUMENT_ICONCLS"));
			newDoc.set("DOCUMENT_BIGICONCLS", document.getStr("DOCUMENT_BIGICONCLS"));
			newDoc.set("DOCUMENT_THUMBNAILCLS", document.getStr("DOCUMENT_THUMBNAILCLS"));
			newDoc.set("DOCUMENT_BUSTYPE", document.getStr("DOCUMENT_BUSTYPE"));
			newDoc.set("DOCUMENT_SAVETYPE", JeFileUtil.getFileSaveType());
			newDoc.set("DOCUMENT_PKVALUE", newPkValue);
			serviceTemplate.buildModelCreateInfo(newDoc);
			serviceTemplate.buildModelModifyInfo(newDoc);
			String filePath=document.getStr("DOCUMENT_ADDRESS");
			String uploadPath = WebUtils.getConfigVar("struts.upload.path");
			String newFilePath=getFolderPath("", "", uploadPath)+"/"+JEUUID.uuid()+"."+jeFileManager.getFileTypeSuffix(document.getStr("DOCUMENT_DOCNAME"));
			String privateUrl=jeFileManager.copyFile(filePath, document.getStr("DOCUMENT_BUSTYPE"), document.getStr("DOCUMENT_SAVETYPE"),newFilePath, document.getStr("DOCUMENT_BUSTYPE"));
			newDoc.set("DOCUMENT_ADDRESS", newFilePath);
			newDoc.set("DOCUMENT_PRIVATEKEY",privateUrl);
			buildIconCls(newDoc);
			newDoc=insertDoc(newDoc);
			JSONObject values=new JSONObject();
			values.put("id", newDoc.getStr("JE_CORE_DOCUMENT_ID"));
			values.put("name",newDoc.getStr("DOCUMENT_DOCNAME"));
			values.put("cls", newDoc.getStr("DOCUMENT_BIGICONCLS"));
			values.put("path", newDoc.getStr("DOCUMENT_ADDRESS"));
			values.put("extend", newDoc.getStr("DOCUMENT_THUMBNAILCLS"));
			arrays.add(values);
		}
		return arrays.toString();
	}
	@Override
	public void buildIconCls(DynaBean doc){
		String fileType=doc.getStr("DOCUMENT_FILETYPE");
		FileType typeVo=jeFileManager.getFileTypeInfo(fileType);
		doc.set("DOCUMENT_FILETYPENAME", typeVo.getName());
		doc.set("DOCUMENT_ICONCLS", typeVo.getIconCls());
		if(jeFileManager.isImage(doc.getStr("DOCUMENT_ADDRESS"))) {
			String jeFileType=JEFileType.PROJECT;
			if(StringUtil.isNotEmpty(doc.getStr("DOCUMENT_BUSTYPE"))){
				jeFileType=doc.getStr("DOCUMENT_BUSTYPE");
			}
			String jeFileSaveType=JeFileUtil.getFileSaveType();
			if(StringUtil.isNotEmpty(doc.getStr("DOCUMENT_SAVETYPE"))){
				jeFileSaveType=doc.getStr("DOCUMENT_SAVETYPE");
			}
			doc.set("DOCUMENT_THUMBNAILCLS", jeFileManager.getImageInfo(doc.getStr("DOCUMENT_ADDRESS"),jeFileManager.readFileIo(doc.getStr("DOCUMENT_ADDRESS"),jeFileType,jeFileSaveType)));
		}
	}

	@Override
	public void processFileUpload(HttpServletRequest request,Map<String, Object> params, DynaBean dynaBean, EndUser currentUser,String type) {
		//如果是文件库 文件子功能则直接不处理
		String docType=request.getParameter("docType");
		if(ArrayUtils.contains(new String[]{"docinfo","docfile"},docType)){
			return;
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)((JERequestWrapper) request).getRequest();
		//获取传入参数
		String funcCode=request.getParameter("funcCode");
		String beanTableCode=request.getParameter("beanTableCode");
		String pkValue = null;
		if(dynaBean != null){
			String pkCode = dynaBean.getStr(BeanUtils.KEY_PK_CODE);
			pkValue=dynaBean.getStr(pkCode);
		}
		if(StringUtil.isNotEmpty(request.getParameter("pkValue"))){
			pkValue=request.getParameter("pkValue");
		}
		if(StringUtil.isNotEmpty(beanTableCode)){
			dynaBean.set("beanTableCode",beanTableCode);
		}
		//默认文件上传字段名
		String funcUploadField = ConfigCacheManager.getCacheValue("struts.upload.formUploadField");
		String uploadPath = ConfigCacheManager.getCacheValue("struts.upload.path");
		//存储目录
		String funcFolderPath=request.getParameter("filePath");
		//获取平台文件类型( PROJECT 会上传到 阿里云OSS )
		String jeFileType=request.getParameter("jeFileType");
		if(StringUtil.isEmpty(jeFileType)){
			jeFileType=JEFileType.PROJECT;
		}
		//全目录
		String fullFilePath=request.getParameter("fullFilePath");
		if(StringUtil.isNotEmpty(fullFilePath)){
			dynaBean.set("FULLFILEPATH", fullFilePath);
		}
		List<DynaBean> documentInfo = new ArrayList<DynaBean>();
		//定义文件
		List<MultipartFile> files =null;
		String[] fileNames =null;
		String[] fileTypes =null;
		String folderPath=getFolderPath(funcCode, funcFolderPath, uploadPath);
		//功能附件上传
		if(DocumentType.FUNC.equals(type)){
			if(StringUtil.isEmpty(pkValue)){
				logger.error("功能主键传入失败，无法上传!");
				return;
			}
			files = multipartRequest.getFiles(funcUploadField);
			if(null != files && 0 != files.size()) {
				fileNames = buildFileNamesArr(files);
				fileTypes = buildFileContentTypeArr(files);
				try {
					processUploadedFiles(folderPath,fullFilePath, dynaBean, funcUploadField, pkValue, files, fileNames, fileTypes, documentInfo, type,jeFileType, funcCode, currentUser);
				} catch (IOException e) {
					throw new PlatformException("功能上传附件异常!", PlatformExceptionEnum.JE_DOC_FUNC_UPLOAD_ERROR,request,e);
				}
			}
			//只上传附件，不持久
		}else if(DocumentType.OTHER.equals(type)){
			files = multipartRequest.getFiles(funcUploadField);
			if(null != files && 0 != files.size()) {
				fileNames = buildFileNamesArr(files);
				fileTypes = buildFileContentTypeArr(files);
				try {
					processUploadedFiles(folderPath,fullFilePath, dynaBean, funcUploadField, pkValue, files, fileNames, fileTypes, documentInfo, type,jeFileType, funcCode, currentUser);
				} catch (IOException e) {
					throw new PlatformException("其他附件上传异常!", PlatformExceptionEnum.JE_DOC_OTHER_UPLOAD_ERROR,request,e);
				}
			}
		}else if(DocumentType.BEAN.equals(type)){
			String uploadableFields = request.getParameter("uploadableFields");
			if(StringUtil.isNotEmpty(uploadableFields)) {
				String[] ufs = uploadableFields.split(ArrayUtils.SPLIT);
				for(String field : ufs) {
					List<MultipartFile> fs = multipartRequest.getFiles(field);
					if(null != fs && 0 != fs.size()) {
						String[] fns = buildFileNamesArr(fs);
						String[] fts = buildFileContentTypeArr(fs);
						try {
							processUploadedFiles(folderPath,fullFilePath, dynaBean, field, pkValue, fs, fns, fts, documentInfo, type,jeFileType, funcCode, currentUser);
						} catch (IOException e) {
							throw new PlatformException("数据字段上传附件异常!", PlatformExceptionEnum.JE_DOC_BEAN_UPLOAD_ERROR,request,e);
						}
					}else if(dynaBean.containsKey(field) && StringUtil.isEmpty(dynaBean.getStr(field))){
						dynaBean.remove(field);
					}
				}
			}
		}
		dynaBean.set(BeanUtils.KEY_DOC_INFO, documentInfo);
		request.setAttribute("dynaBean",dynaBean);
	}

	/**
	 * 构建文件名称
	 * @param files
	 * @return
	 */
	@Override
	public String[] buildFileNamesArr(List<MultipartFile> files){
		String[] fileNames = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
			fileNames[i]=files.get(i).getOriginalFilename();
		}
		return fileNames;
	}

	/**
	 * 构建文件类型
	 * @param files
	 * @return
	 */
	@Override
	public String[] buildFileContentTypeArr(List<MultipartFile> files){
		String[] contentTypes = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
			contentTypes[i]=files.get(i).getContentType();
		}
		return contentTypes;
	}

	@Override
	public String getFolderPath(String funcCode,String funcFolderPath,String uploadPath){
		String todayUploadDir="";
		if(StringUtil.isEmpty(funcFolderPath)){
			// 当前时间，用以生成上传文件名和目录
			todayUploadDir = uploadPath + "/" + DateUtils.formatDate(new Date(), "yyyyMM");
		}else{
			//如果指定路径则默认上传目录+指定目录
			todayUploadDir = uploadPath+funcFolderPath;
		}
		return todayUploadDir;
	}

	@Override
	public void processBatchUpload(DynaBean dynaBean, String batchFilesFields,String funcCode,Boolean doSave,HttpServletRequest request) {
		String funcFolderPath=request.getParameter("filePath");
		String jeFileType=request.getParameter("jeFileType");
		if(StringUtil.isEmpty(jeFileType)){
			jeFileType=JEFileType.PROJECT;
		}
		doSaveBatchFiles(dynaBean, batchFilesFields, funcCode, funcFolderPath, doSave, jeFileType);
	}

	@Override
	public void doSaveBatchFiles(DynaBean dynaBean, String batchFilesFields,String funcCode,
								 String funcFolderPath,Boolean doSave,String jeFileType) {
		String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
		String pkCode=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
		String pkValue=dynaBean.getPkValue();
		if(StringUtil.isEmpty(pkValue)){
			pkValue=JEUUID.uuid();
			dynaBean.set(pkCode, pkValue);
		}
		String fileSaveType=JEFileSaveType.DEFAULT;
		if(JEFileType.PROJECT.equalsIgnoreCase(jeFileType)){
			fileSaveType=JeFileUtil.getFileSaveType();
		}
		String uploadPath=ConfigCacheManager.getCacheValue("struts.upload.path");
		for(String field:batchFilesFields.split(",")){
			JSONArray files=JSONArray.fromObject(StringUtil.getDefaultValue(dynaBean.getStr(field),"[]"));
			List<String> haveDocIds=new ArrayList<String>();
			List<String> oldDocIds=new ArrayList<>();
			for(Integer i=0;i<files.size();i++){
				JSONObject fileObj=files.getJSONObject(i);
				String docId="";
				if(fileObj.containsKey("id")){
					docId=fileObj.getString("id");
				}
				if(StringUtil.isNotEmpty(docId)){
					haveDocIds.add(docId);
					oldDocIds.add(docId);
				}else{
					//复制附件
					String fileName=fileObj.getString("name");
					String path=fileObj.getString("path");
//					// 生成目录
					String folderPath=getFolderPath(funcCode, funcFolderPath, uploadPath);
					String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
					String filePath=folderPath+"/"+JEUUID.uuid()+"."+fileType;
					String privateUrl=jeFileManager.copyFile(path,jeFileType,fileSaveType,filePath,jeFileType);
					DynaBean doc=new DynaBean("JE_CORE_DOCUMENT",false);
//					jeFileManager.copyFile()
					doc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DOCUMENT_ID");
					doc.set("JE_CORE_DOCUMENT_ID", JEUUID.uuid());
					doc.set("DOCUMENT_TABLECODE", tableCode);
					doc.set("DOCUMENT_FIELDCODE", field);
					doc.set("DOCUMENT_PKVALUE", pkValue);
					doc.set("DOCUMENT_ADDRESS", filePath);
					doc.set("DOCUMENT_PRIVATEKEY", privateUrl);
//					doc.set("DOCUMENT_DOCSIZE", file.length());
					doc.set("DOCUMENT_DOCNAME", fileName);
					doc.set("DOCUMENT_FORMAT", "");
					doc.set("DOCUMENT_TYPE", DocumentType.BATCH);
					doc.set("DOCUMENT_FUNCCODE", funcCode);
					doc.set("DOCUMENT_FILETYPE", fileType);
					doc.set("DOCUMENT_BUSTYPE", jeFileType);
					doc.set("DOCUMENT_SAVETYPE", JeFileUtil.getFileSaveType());
					doc.set("SY_STATUS", "1");
					buildIconCls(doc);
					serviceTemplate.buildModelCreateInfo(doc);
					serviceTemplate.buildModelModifyInfo(doc);
					doc=insertDoc(doc);
					fileObj.put("id", doc.getStr("JE_CORE_DOCUMENT_ID"));
					fileObj.put("path", filePath);
					haveDocIds.add(doc.getStr("JE_CORE_DOCUMENT_ID"));
				}
			}
			if(oldDocIds.size()>0 && StringUtil.isNotEmpty(pkValue)){
				List<DynaBean> documents=serviceTemplate.selectList("JE_CORE_DOCUMENT"," AND JE_CORE_DOCUMENT_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(oldDocIds))+")");
				for(DynaBean doc:documents){
					if(!doc.getStr("DOCUMENT_PKVALUE","").equalsIgnoreCase(pkValue)){
						doc.set("DOCUMENT_PKVALUE",pkValue);
						doc.set("DOCUMENT_FIELDCODE",field);
						updateDoc(doc);
					}
				}
			}
			//如果是修改操作则删除原有且现在不存在的附件
			if(!doSave){
				List<DynaBean> documents=getDocInfoList(" AND DOCUMENT_TABLECODE='"+tableCode+"' AND DOCUMENT_FIELDCODE='"+field+"' AND DOCUMENT_PKVALUE='"+pkValue+"' AND DOCUMENT_TYPE='"+DocumentType.BATCH+"' AND JE_CORE_DOCUMENT_ID NOT IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(haveDocIds))+")",tableCode);
				List<String> deleteIds=new ArrayList<String>();
				for(DynaBean document:documents){
					jeFileManager.deleteFile(document);
					deleteIds.add(document.getPkValue());
					String docPkValue=document.getStr("JE_CORE_DOCUMENT_ID");
					String path=document.getStr("DOCUMENT_ADDRESS");
					DocumentCacheManager.removeCache(path);
					DocumentCacheManager.removeCache(docPkValue);
				}
				if(deleteIds.size()>0){
					deleteDoc(" AND JE_CORE_DOCUMENT_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(deleteIds))+")",tableCode);
				}
			}
			dynaBean.set(field, files.toString());
		}
	}

	@Override
	public DynaBean processUeEditorFileUpload(File file, Map<String, Object> params) {
		String funcCode=params.get("funcCode")+"";
		String pkValue=params.get("pkValue")+"";
		String beanTableCode=params.get("beanTableCode")+"";
		String jeFileType=params.get("jeFileType")+"";
		String fileName=params.get("fileName")+"";
		String fileFormat=params.get("fileFormat")+"";
		String fieldCode=params.get("fieldCode")+"";
		String fullFilePath=params.get("fullFilePath")+"";
		//默认文件上传字段名
		//存储目录
//		String uploadPath = WebUtils.getConfigVar("struts.upload.path");
		//获取平台文件类型( PROJECT 会上传到 阿里云OSS )
		if(StringUtil.isEmpty(jeFileType)){
			jeFileType=JEFileType.PROJECT;
		}
		//全目录
		//保存功能文件
		String uploadPath = WebUtils.getConfigVar("struts.upload.path");
		String folderPath=getFolderPath(funcCode, "",uploadPath) ;
		List<DynaBean> documentInfo = new ArrayList<DynaBean>();
		DynaBean dynaBean=null;
		if(StringUtil.isNotEmpty(beanTableCode)){
			dynaBean=new DynaBean(beanTableCode,true);
			dynaBean.set(dynaBean.getStr(BeanUtils.KEY_PK_CODE),pkValue);
		}
		processUploadedFiles(folderPath,fullFilePath,dynaBean,fieldCode,pkValue,new File[]{file},new String[]{fileName},new String[]{fileFormat},documentInfo,DocumentType.CKEDITOR,JEFileType.PROJECT,funcCode,SecurityUserHolder.getCurrentUser());
		DynaBean docInfo=null;
		if(documentInfo!=null && documentInfo.size()>0){
			docInfo=documentInfo.get(0);
			insertDoc(docInfo);
		}
		return docInfo;
	}

	@Override
	public void doRemoveBatchFiles(String tableCode,String ids) {
		// TODO Auto-generated method stub
		List<DynaBean> documents=getDocInfoList(" AND DOCUMENT_TABLECODE='"+tableCode+"'  AND DOCUMENT_PKVALUE IN ("+StringUtil.buildArrayToString(ids.split(","))+") AND DOCUMENT_TYPE='"+DocumentType.BATCH+"'",tableCode);
		List<String> deleteIds=new ArrayList<String>();
		for(DynaBean document:documents){
			jeFileManager.deleteFile(document);
			deleteIds.add(document.getPkValue());
			String docPkValue=document.getStr("JE_CORE_DOCUMENT_ID");
			String path=document.getStr("DOCUMENT_ADDRESS");
			DocumentCacheManager.removeCache(path);
			DocumentCacheManager.removeCache(docPkValue);
		}
		if(deleteIds.size()>0){
			deleteDoc(" AND JE_CORE_DOCUMENT_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(deleteIds))+")",tableCode);
		}
	}

	@Override
	public void doRemoveTreeBatchFiles(String tableCode,String ids) {
		for(String id:ids.split(",")){
			String pkCode=BeanUtils.getInstance().getPKeyFieldNames(tableCode);
			String whereSql=" AND DOCUMENT_PKVALUE IN (SELECT "+pkCode+" FROM "+tableCode+" WHERE SY_PATH LIKE '%"+id+"%')";
			List<DynaBean> documents=getDocInfoList(" AND DOCUMENT_TABLECODE='"+tableCode+"'"+whereSql+" AND DOCUMENT_TYPE='"+DocumentType.BATCH+"'",tableCode);
			List<String> deleteIds=new ArrayList<String>();
			for(DynaBean document:documents){
				jeFileManager.deleteFile(document);
				String docPkValue=document.getStr("JE_CORE_DOCUMENT_ID");
				String path=document.getStr("DOCUMENT_ADDRESS");
				DocumentCacheManager.removeCache(path);
				DocumentCacheManager.removeCache(docPkValue);
				deleteIds.add(document.getPkValue());
			}
			if(deleteIds.size()>0){
				deleteDoc(" AND JE_CORE_DOCUMENT_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(deleteIds))+")",tableCode);
			}
		}
	}


	@Override
	public void processUploadedFiles(String folderPath,String fullFilePath, DynaBean dynaBean, String uploadFieldName,String pkValue, File[] files, String[] fileNames, String[] fileTypes, List<DynaBean> documentInfo,String type,String jeFileType,String funcCode,EndUser currentUser){
		for(int i=0; i<files.length; i++) {
			String newFileName = JEUUID.uuid() +"."+ jeFileManager.getFileTypeSuffix(fileNames[i]);
//			File dst =null;
			String filePath="";
			String tableCode="";
			String pkCode="";
			if(StringUtil.isNotEmpty(fullFilePath)){
				filePath=fullFilePath;
			}else{
				filePath=folderPath + "/" + newFileName;
			}
			if(dynaBean!=null && StringUtil.isNotEmpty(dynaBean.getStr(BeanUtils.KEY_PK_CODE))){
				pkCode=dynaBean.getStr(BeanUtils.KEY_PK_CODE);
			}
			if(dynaBean!=null && StringUtil.isNotEmpty(dynaBean.getStr(BeanUtils.KEY_TABLE_CODE))){
				tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			}
			String privateUrl=jeFileManager.saveFile(files[i],filePath,jeFileType);
			if(dynaBean!=null && StringUtil.isEmpty(pkValue) && StringUtil.isNotEmpty(pkCode)){
				pkValue=dynaBean.getStr(dynaBean.getStr(BeanUtils.KEY_PK_CODE),"");
			}
			JSONObject returnObj=new JSONObject();
			returnObj.put("docSize",files[i].length());
			returnObj.put("privateKey",privateUrl);
			DynaBean doc = buildDocuemnt(tableCode, uploadFieldName,pkValue, fileNames[i], fileTypes[i], files[i].length(),filePath,type,jeFileType,funcCode,currentUser,returnObj);
			doc.set("DOCUMENT_DOCSIZE",files[i].length());
			doc.set("DOCUMENT_PRIVATEKEY",privateUrl);
			documentInfo.add(doc);
			if(DocumentType.BEAN.equals(type)){
				dynaBean.setStr(uploadFieldName, fileNames[i]+"*"+filePath);
			}
		}
	}

	@Override
	public void saveDocFile(File file, String relativeFilePath,String fileName,String miniType, String jeFileType,String funcCode,String tableCode,String pkValue,String fieldCode) {
		String privateUrl=jeFileManager.saveFile(file,relativeFilePath,jeFileType);
		JSONObject returnObj=new JSONObject();
		returnObj.put("privateKey",privateUrl);
		DynaBean doc = buildDocuemnt(tableCode,fieldCode,pkValue, fileName, miniType, file.length(),relativeFilePath,DocumentType.BEAN,jeFileType,funcCode, SecurityUserHolder.getCurrentUser(),returnObj);
		doc.set("DOCUMENT_PRIVATEKEY",privateUrl);
		insertDoc(doc);

	}
	public void processUploadedFiles(String folderPath,String fullFilePath, DynaBean dynaBean, String uploadFieldName,String pkValue, List<MultipartFile> files, String[] fileNames, String[] fileTypes, List<DynaBean> documentInfo,String type,String jeFileType,String funcCode,EndUser currentUser) throws IOException {
		Map<String, String> map ;
		for(int i=0; i<files.size(); i++) {
			if(StringUtil.isEmpty(fileNames[i])){
				continue;
			}
			MultipartFile multipartFile = files.get(i);
			long fileSize=multipartFile.getSize();
			String newFileName = JEUUID.uuid() +"."+ jeFileManager.getFileTypeSuffix(fileNames[i]);
			String filePath=folderPath+"/"+newFileName;
			if(StringUtil.isNotEmpty(fullFilePath)){
				filePath=fullFilePath;
			}
//			map = jeFileManager.uploadedFiles(folderPath,fullFilePath,newFileName,multipartFile,jeFileType);
			String privateUrl=jeFileManager.saveFile(multipartFile,filePath,jeFileType);
			if(StringUtil.isEmpty(pkValue) && dynaBean != null && StringUtil.isNotEmpty(dynaBean.getStr(BeanUtils.KEY_PK_CODE))){
				pkValue=dynaBean.getStr(dynaBean.getStr(BeanUtils.KEY_PK_CODE),"");
			}
			String tableCode=dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
			if(StringUtil.isEmpty(tableCode) && StringUtil.isNotEmpty(dynaBean.getStr("beanTableCode"))){
				tableCode=dynaBean.getStr("beanTableCode");
			}
			DynaBean doc = buildDocuemnt(tableCode, uploadFieldName,pkValue, fileNames[i], fileTypes[i], fileSize,filePath,type,jeFileType,funcCode,currentUser);
			doc.set("DOCUMENT_PRIVATEKEY",privateUrl);
			documentInfo.add(doc);
			if(DocumentType.BEAN.equals(type)){
//				dynaBean.setStr(uploadFieldName, fileNames[i]+"*"+filePath);
				dynaBean.setStr(uploadFieldName, fileNames[i]+"*"+ filePath);
			}
		}
	}
	/**
	 * 构造Document对象
	 * @param tableCode
	 * @param fieldCode
	 * @param pkValue
	 * @param fileName
	 * @param contentTypes
	 * @param fileSize
	 * @param filePath
	 * @param type
	 * @param jeFileType
	 * @param funcCode
	 * @param currentUser
	 * @return
	 */
	private DynaBean buildDocuemnt(String tableCode, String fieldCode,String pkValue, String fileName, String contentTypes,long fileSize,String filePath,String type,String jeFileType,String funcCode,EndUser currentUser,JSONObject returnObj) {
		DynaBean doc = new DynaBean("JE_CORE_DOCUMENT",false);
		doc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DOCUMENT_ID");
		doc.set("DOCUMENT_TABLECODE", tableCode);
		doc.set("DOCUMENT_FIELDCODE", fieldCode);
		if(StringUtil.isNotEmpty(pkValue)){
			doc.set("DOCUMENT_PKVALUE", pkValue);
		}
		doc.set("DOCUMENT_ADDRESS", filePath);
		doc.set("DOCUMENT_DOCSIZE", fileSize);
		doc.set("DOCUMENT_DOCNAME", fileName);
		doc.set("DOCUMENT_FORMAT", contentTypes);
		doc.set("DOCUMENT_TYPE", type);
		doc.set("DOCUMENT_BUSTYPE", jeFileType);
		if(JEFileType.PROJECT.equals(jeFileType)){
			doc.set("DOCUMENT_SAVETYPE", JeFileUtil.getFileSaveType());
		}
		doc.set("DOCUMENT_FUNCCODE", funcCode);
		String fileType=jeFileManager.getFileTypeSuffix(fileName);
		doc.set("DOCUMENT_FILETYPE", fileType);
		doc.set("SY_STATUS", "1");
		if(returnObj.containsKey("docSize")){
			doc.set("DOCUMENT_DOCSIZE",returnObj.get("docSize"));
		}
		if(returnObj.containsKey("privateKey")){
			doc.set("DOCUMENT_PRIVATEKEY",returnObj.get("privateKey"));
		}
		buildIconCls(doc);
		//构建登录人信息
		if(currentUser!=null){
			doc.set("SY_CREATEUSER", currentUser.getUserCode());
			doc.set("SY_CREATEUSERNAME", currentUser.getUsername());
			doc.set("SY_CREATEUSERID", currentUser.getUserId());
			Department currentDept=currentUser.getDept();
			doc.set("SY_CREATEORG", currentDept.getDeptCode());
			doc.set("SY_CREATEORGID", currentDept.getDeptId());
			doc.set("SY_CREATEORGNAME", currentDept.getDeptName());
		}
		return doc;
	}
	/**
	 * 构造Document对象
	 * @param tableCode
	 * @param fieldCode
	 * @param pkValue
	 * @param fileName
	 * @param contentTypes
	 * @param fileSize
	 * @param type
	 * @param jeFileType
	 * @param funcCode
	 * @param currentUser
	 * @return
	 */
	private DynaBean buildDocuemnt(String tableCode, String fieldCode,String pkValue, String fileName, String contentTypes, long fileSize,String filePath,String type,String jeFileType,String funcCode,EndUser currentUser) {
		DynaBean doc = new DynaBean("JE_CORE_DOCUMENT",false);
		doc.set(BeanUtils.KEY_PK_CODE, "JE_CORE_DOCUMENT_ID");
		doc.set("DOCUMENT_TABLECODE", tableCode);
		doc.set("DOCUMENT_FIELDCODE", fieldCode);
		if(StringUtil.isNotEmpty(pkValue)){
			doc.set("DOCUMENT_PKVALUE", pkValue);
		}
		doc.set("DOCUMENT_ADDRESS", filePath);
		doc.set("DOCUMENT_DOCSIZE", fileSize);
		doc.set("DOCUMENT_DOCNAME", fileName);
		doc.set("DOCUMENT_FORMAT", contentTypes);
		doc.set("DOCUMENT_TYPE", type);
		doc.set("DOCUMENT_BUSTYPE", jeFileType);
		if(JEFileType.PROJECT.equals(jeFileType)){
			doc.set("DOCUMENT_SAVETYPE",JeFileUtil.getFileSaveType());
		}
		doc.set("DOCUMENT_FUNCCODE", funcCode);
		String fileType=jeFileManager.getFileTypeSuffix(fileName);
		doc.set("DOCUMENT_FILETYPE", fileType);
		doc.set("SY_STATUS", "1");
		buildIconCls(doc);
		//构建登录人信息
		if(currentUser!=null){
			doc.set("SY_CREATEUSER", currentUser.getUserCode());
			doc.set("SY_CREATEUSERNAME", currentUser.getUsername());
			doc.set("SY_CREATEUSERID", currentUser.getUserId());
			Department currentDept=currentUser.getDept();
			doc.set("SY_CREATEORG", currentDept.getDeptCode());
			doc.set("SY_CREATEORGID", currentDept.getDeptId());
			doc.set("SY_CREATEORGNAME", currentDept.getDeptName());
		}
		return doc;
	}
	@Override
	public void saveDocumentionMultipart(MultipartFile file, String fileName, DynaBean dynaBean, String formatType) {
		String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
		String uploadPath=WebUtils.getConfigVar("struts.documentation.path");
		String folderPath=getFolderPath("JE_SYS_DOCUMENTATION", "", uploadPath);
		String newFileName=JEUUID.uuid()+"."+fileType;
		String filePath=folderPath+"/"+newFileName;
		long fileSize=file.getSize();
		String privateUrl=jeFileManager.saveFile(file,filePath,JEFileType.PROJECT);
		dynaBean.set("DOCUMENTATION_TEXT", fileName);
		dynaBean.set("DOCUMENTATION_CODE", "");
		dynaBean.set("DOCUMENTATION_FILETYPE", fileType);
		dynaBean.set("DOCUMENTATION_ADDRESS", filePath);
		dynaBean.set("DOCUMENTATION_PRIVATEKEY", privateUrl);
		dynaBean.set("DOCUMENTATION_TYPE", "FILE");
		dynaBean.set("DOCUMENTATION_SIZE", fileSize);
		dynaBean.set("DOCUMENTATION_BUSTYPE", JEFileType.PROJECT);
		dynaBean.set("DOCUMENTATION_SAVETYPE", JeFileUtil.getFileSaveType());
		FileType typeVo=jeFileManager.getFileTypeInfo(fileType);
		dynaBean.set("DOCUMENTATION_FILETYPENAME", typeVo.getName());
		dynaBean.set("DOCUMENTATION_ICONCLS", typeVo.getIconCls());
//		dynaBean.set("DOCUMENTATION_BIGICONCLS", typeVo.getBigIconCls());
		if(jeFileManager.isImage(filePath)) {
			dynaBean.set("DOCUMENTATION_THUMBNAILCLS", jeFileManager.getImageInfo(filePath,jeFileManager.readFileIo(filePath,JEFileType.PROJECT,JeFileUtil.getFileSaveType())));
		}
		dynaBean.set("DOCUMENTATION_FORMAT", formatType);
		dynaBean.set("SY_STATUS", "1");
		dynaBean.set("SY_CHILDRENSTATUS", "1");
		dynaBean.set("DOCUMENTATION_PACKAGE", "0");
		//得到文件图标
		serviceTemplate.buildModelCreateInfo(dynaBean);
		serviceTemplate.buildModelModifyInfo(dynaBean);
		//处理父节点信息
		if(StringUtil.isNotEmpty(dynaBean.getStr("SY_PARENT")) && StringUtil.isEmpty(dynaBean.getStr("SY_PATH"))){
			DynaBean parentNode=serviceTemplate.selectOneByPk("JE_SYS_DOCUMENTATION", dynaBean.getStr("SY_PARENT"));
			dynaBean.set("SY_PATH", parentNode.getStr("SY_PATH"));
			dynaBean.set("SY_LAYER", parentNode.getInt("SY_LAYER",0)+1);
		}
	}
	@Override
	public void saveDocumention(File file, String fileName, DynaBean dynaBean,String formatType) {
		String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
		String uploadPath=WebUtils.getConfigVar("struts.documentation.path");
		String folderPath=getFolderPath("JE_SYS_DOCUMENTATION", "", uploadPath);
		String filePath=folderPath+"/"+JEUUID.uuid()+"."+fileType;
		String privateUrl=jeFileManager.saveFile(file, filePath, JEFileType.PROJECT);
		dynaBean.set("DOCUMENTATION_TEXT", fileName);
		dynaBean.set("DOCUMENTATION_CODE", "");
		dynaBean.set("DOCUMENTATION_FILETYPE", fileType);
		dynaBean.set("DOCUMENTATION_ADDRESS", filePath);
		dynaBean.set("DOCUMENTATION_PRIVATEKEY", privateUrl);
		dynaBean.set("DOCUMENTATION_TYPE", "FILE");
		dynaBean.set("DOCUMENTATION_SIZE", file.length());
		dynaBean.set("DOCUMENTATION_BUSTYPE", JEFileType.PROJECT);
		dynaBean.set("DOCUMENTATION_SAVETYPE", JeFileUtil.getFileSaveType());
		FileType typeVo=jeFileManager.getFileTypeInfo(fileType);

//				FileTypeCacheManager.getCacheValue(fileType);
		dynaBean.set("DOCUMENTATION_FILETYPENAME", typeVo.getName());
		dynaBean.set("DOCUMENTATION_ICONCLS", typeVo.getIconCls());
		if(jeFileManager.isImage(filePath)) {
			dynaBean.set("DOCUMENTATION_THUMBNAILCLS", jeFileManager.getImageInfo(filePath,file));
		}
//		dynaBean.set("DOCUMENTATION_BIGICONCLS", typeVo.getBigIconCls());
//		dynaBean.set("DOCUMENTATION_THUMBNAILCLS", typeVo.getThumbnailCls());
		dynaBean.set("DOCUMENTATION_FORMAT", formatType);
//		if(StringUtil.isEmpty(dynaBean.getStr("DOCUMENTATION_FORMAT"))){
//			dynaBean.set("DOCUMENTATION_FORMAT", typeVo.getFormat());
//		}
		dynaBean.set("SY_STATUS", "1");
		dynaBean.set("SY_CHILDRENSTATUS", "1");
		dynaBean.set("DOCUMENTATION_PACKAGE", "0");
		//得到文件图标
		serviceTemplate.buildModelCreateInfo(dynaBean);
		serviceTemplate.buildModelModifyInfo(dynaBean);
		//处理父节点信息
		if(StringUtil.isNotEmpty(dynaBean.getStr("SY_PARENT")) && StringUtil.isEmpty(dynaBean.getStr("SY_PATH"))){
			DynaBean parentNode=serviceTemplate.selectOneByPk("JE_SYS_DOCUMENTATION", dynaBean.getStr("SY_PARENT"));
			dynaBean.set("SY_PATH", parentNode.getStr("SY_PATH"));
			dynaBean.set("SY_LAYER", parentNode.getInt("SY_LAYER",0)+1);
		}
	}

	@Override
	public void copyDocumention(DynaBean oldFileBean, DynaBean newFileBean) {
		String fileType=oldFileBean.getStr("DOCUMENTATION_FILETYPE");
		String address=oldFileBean.getStr("DOCUMENTATION_ADDRESS");
		File f=jeFileManager.readFile(address, JEFileType.PROJECT);
		if(f.exists()){
			String dirPath=address.substring(0,address.lastIndexOf("/"));
			String newFileName=JEUUID.uuid();
			String filePath=dirPath+"/"+newFileName+"."+fileType;
			String privateUrl=jeFileManager.copyFile(address,  JEFileType.PROJECT,JeFileUtil.getFileSaveType(),filePath, JEFileType.PROJECT);
//			FileOperate.copyFile(BaseAction.webrootAbsPath+address, BaseAction.webrootAbsPath+dirPath+"/"+newFileName+"."+fileType);
			newFileBean.set("DOCUMENTATION_ADDRESS",filePath);
			newFileBean.set("DOCUMENTATION_PRIVATEKEY",privateUrl);
			//重新获取类型的样式， 处理缩略图的路径使用新的路径
			FileType typeVo=jeFileManager.getFileTypeInfo(fileType);
			newFileBean.set("DOCUMENTATION_FILETYPENAME", typeVo.getName());
			newFileBean.set("DOCUMENTATION_ICONCLS", typeVo.getIconCls());
			if(jeFileManager.isImage(filePath)) {
				newFileBean.set("DOCUMENTATION_THUMBNAILCLS", jeFileManager.getImageInfo(filePath,f));
			}
//			newFileBean.set("DOCUMENTATION_BIGICONCLS", typeVo.getBigIconCls());
//			newFileBean.set("DOCUMENTATION_THUMBNAILCLS", typeVo.getThumbnailCls());
			newFileBean.set("DOCUMENTATION_FORMAT", oldFileBean.getStr("DOCUMENTATION_FORMAT"));
			newFileBean.set("DOCUMENTATION_BUSTYPE", newFileBean.getStr("DOCUMENT_BUSTYPE"));
			newFileBean.set("DOCUMENTATION_SAVETYPE", newFileBean.getStr("DOCUMENT_SAVETYPE"));
		}else{
			newFileBean.set("DOCUMENTATION_ADDRESS", oldFileBean.getStr("DOCUMENTATION_ADDRESS"));
		}
	}
	@Override
	public void saveDocumentionMultipartFile(MultipartFile file, String fileName,DynaBean dynaBean,String formatType) {
		// TODO Auto-generated method stub
		String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
		String uploadPath=WebUtils.getConfigVar("struts.upload.path");
		String folderPath=getFolderPath("JE_CORE_FILE", "", uploadPath);
		String newFileName=JEUUID.uuid()+"."+fileType;
		String filePath=folderPath+"/"+newFileName;
		long fileSize=file.getSize();
		String privateUrl=jeFileManager.saveFile(file,filePath,JEFileType.PROJECT);
		dynaBean.set("FILE_NAME", fileName);
		dynaBean.set("FILE_CODE", "");
		dynaBean.set("FILE_FILETYPE", fileType);
		dynaBean.set("FILE_ADDRESS", filePath);
		dynaBean.set("FILE_PRIVATEKEY", privateUrl);
		dynaBean.set("FILE_TYPE", "FILE");
		dynaBean.set("FILE_SIZE", fileSize);
		dynaBean.set("FILE_BUSTYPE", JEFileType.PROJECT);
		dynaBean.set("FILE_SAVETYPE", JeFileUtil.getFileSaveType());
		FileType typeVo=jeFileManager.getFileTypeInfo(fileType);
		dynaBean.set("FILE_FILETYPENAME", typeVo.getName());
		dynaBean.set("FILE_ICONCLS", typeVo.getIconCls());
//		dynaBean.set("FILE_BIGICONCLS", typeVo.getBigIconCls());
		if(jeFileManager.isImage(filePath)) {
			dynaBean.set("FILE_THUMBNAILCLS", jeFileManager.getImageInfo(filePath,jeFileManager.readFileIo(filePath,JEFileType.PROJECT,JeFileUtil.getFileSaveType())));
		}
//		dynaBean.set("FILE_THUMBNAILCLS", typeVo.getThumbnailCls());
		dynaBean.set("FILE_FORMAT", formatType);
//		if(StringUtil.isEmpty(dynaBean.getStr("FILE_FORMAT"))){
//			dynaBean.set("FILE_FORMAT", typeVo.getFormat());
//		}
		dynaBean.set("SY_STATUS", "1");
		//得到文件图标
		serviceTemplate.buildModelCreateInfo(dynaBean);
		serviceTemplate.buildModelModifyInfo(dynaBean);
	}
	@Override
	public void saveDocumentionFile(File file, String fileName,DynaBean dynaBean,String formatType) {
		// TODO Auto-generated method stub
		String fileType=fileName.substring(fileName.lastIndexOf(".")+1);
		String uploadPath=WebUtils.getConfigVar("struts.upload.path");
		String folderPath=getFolderPath("JE_CORE_FILE", "", uploadPath);
		String filePath=folderPath+"/"+JEUUID.uuid()+"."+fileType;
		String privateUrl=jeFileManager.saveFile(file, filePath, JEFileType.PROJECT);
		dynaBean.set("FILE_NAME", fileName);
		dynaBean.set("FILE_CODE", "");
		dynaBean.set("FILE_FILETYPE", fileType);
		dynaBean.set("FILE_ADDRESS", filePath);
		dynaBean.set("FILE_PRIVATEKEY", privateUrl);
		dynaBean.set("FILE_TYPE", "FILE");
		dynaBean.set("FILE_SIZE", file.length());
		dynaBean.set("FILE_BUSTYPE", JEFileType.PROJECT);
		dynaBean.set("FILE_SAVETYPE", JeFileUtil.getFileSaveType());
		FileType typeVo=jeFileManager.getFileTypeInfo(fileType);
		dynaBean.set("FILE_FILETYPENAME", typeVo.getName());
		dynaBean.set("FILE_ICONCLS", typeVo.getIconCls());
		if(jeFileManager.isImage(filePath)) {
			dynaBean.set("FILE_THUMBNAILCLS", jeFileManager.getImageInfo(filePath,file));
		}
//		dynaBean.set("FILE_BIGICONCLS", typeVo.getBigIconCls());
//		dynaBean.set("FILE_THUMBNAILCLS", typeVo.getThumbnailCls());
		dynaBean.set("FILE_FORMAT", formatType);
//		if(StringUtil.isEmpty(dynaBean.getStr("FILE_FORMAT"))){
//			dynaBean.set("FILE_FORMAT", typeVo.getFormat());
//		}
		dynaBean.set("SY_STATUS", "1");
		//得到文件图标
		serviceTemplate.buildModelCreateInfo(dynaBean);
		serviceTemplate.buildModelModifyInfo(dynaBean);
	}

	@Override
	public void copyDocumentionFile(DynaBean oldBean, DynaBean dynaBean) {
		// TODO Auto-generated method stub
		String address=oldBean.getStr("FILE_ADDRESS");
		dynaBean.set("FILE_DIC_ID", oldBean.getStr("FILE_DIC_ID"));
		dynaBean.set("FILE_DIC_CODE", oldBean.getStr("FILE_DIC_CODE"));
		dynaBean.set("FILE_DIC_NAME", oldBean.getStr("FILE_DIC_NAME"));
		//复制文件
		if(jeFileManager.existsFile(address, JEFileType.PROJECT)){
			String fileType=oldBean.getStr("FILE_FILETYPE");
			String dirPath=address.substring(0,address.lastIndexOf("/"));
			String newFileName=JEUUID.uuid();
			String filePath=dirPath+"/"+newFileName+"."+fileType;
			String privateUrl=jeFileManager.copyFile(address,JEFileType.PROJECT,JeFileUtil.getFileSaveType(),filePath, JEFileType.PROJECT);
			dynaBean.set("FILE_ADDRESS", dirPath+"/"+newFileName+"."+fileType);
			dynaBean.set("FILE_PRIVATEKEY", privateUrl);
			//重新处理文件类型的图标 (主要是缩略图的文件地址)
			FileType typeVo=jeFileManager.getFileTypeInfo(fileType);
			dynaBean.set("FILE_FILETYPENAME", typeVo.getName());
			dynaBean.set("FILE_ICONCLS", typeVo.getIconCls());
			if(jeFileManager.isImage(filePath)) {
				dynaBean.set("FILE_THUMBNAILCLS", jeFileManager.getImageInfo(filePath,jeFileManager.readFileIo(filePath,JEFileType.PROJECT)));
			}
//			dynaBean.set("FILE_BIGICONCLS", typeVo.getBigIconCls());
//			dynaBean.set("FILE_THUMBNAILCLS", typeVo.getThumbnailCls());
//			dynaBean.set("FILE_FORMAT", typeVo.getFormat());
		}
		serviceTemplate.buildModelCreateInfo(dynaBean);
		serviceTemplate.buildModelModifyInfo(dynaBean);
	}
	@Override
	public DynaBean getDocInfo(String address, String beanTableCode) {
		DynaBean document= DocumentCacheManager.getCacheValue(address);
		if(document==null){
				document=serviceTemplate.selectOne("JE_CORE_DOCUMENT", " AND DOCUMENT_ADDRESS='" + address + "'");
			if(document!=null){
				DocumentCacheManager.putCache(address,document);
			}
		}
		return document;
	}

	@Override
	public List<DynaBean> getDocInfoList(String whereSql, String beanTableCode) {
		List<DynaBean> lists=serviceTemplate.selectList("JE_CORE_DOCUMENT",whereSql);
		return lists;
	}
	@Override
	public List<DynaBean> getDocInfoList(String whereSql, String beanTableCode,String queryFields) {
		List<DynaBean> lists= serviceTemplate.selectList("JE_CORE_DOCUMENT",whereSql,queryFields);
		return lists;
	}
	@Override
	public DynaBean getDocInfoByPk(String pkValue, String beanTableCode) {
		DynaBean document= DocumentCacheManager.getCacheValue(pkValue);
		if(document==null){
				document=serviceTemplate.selectOne("JE_CORE_DOCUMENT", " AND JE_CORE_DOCUMENT_ID='" + pkValue + "'");
			if(document!=null){
				DocumentCacheManager.putCache(pkValue,document);
			}
		}
		return document;
	}

	@Override
	public DynaBean insertDoc(DynaBean dynaBean) {
		dynaBean.set("SY_FLAG","1");
		dynaBean=serviceTemplate.insert(dynaBean);
		return dynaBean;
	}

	@Override
	public DynaBean updateDoc(DynaBean dynaBean) {
			dynaBean=serviceTemplate.update(dynaBean);
		return dynaBean;
	}

	@Override
	public void deleteDoc(String deleteSql, String beanTableCode) {
		serviceTemplate.executeSql("DELETE FROM JE_CORE_DOCUMENT WHERE 1=1 "+deleteSql);
	}

	@Override
	public void deleteDoc(DynaBean dynaBean) {
		jeFileManager.deleteFile(dynaBean);
		String pkValue=dynaBean.getStr("JE_CORE_DOCUMENT_ID");
		String path=dynaBean.getStr("DOCUMENT_ADDRESS");
		DocumentCacheManager.removeCache(path);
		DocumentCacheManager.removeCache(pkValue);
		deleteDoc(" AND JE_CORE_DOCUMENT_ID='"+pkValue+"'",dynaBean.getStr("DOCUMENT_TABLECODE"));
	}

	@Override
	public void doClearNoDataDoc(String whereSql) {
		List<DynaBean> documents=serviceTemplate.selectList("JE_CORE_DOCUMENT",whereSql);
		for(DynaBean document:documents){
			String tableCode=document.getStr("DOCUMENT_TABLECODE");
			String pkValue=document.getStr("DOCUMENT_PKVALUE");
			String type=document.getStr("DOCUMENT_TYPE");
			String fieldCode=document.getStr("DOCUMENT_FIELDCODE");
			String address=document.getStr("DOCUMENT_ADDRESS","");
			if(DocumentType.BEAN.equalsIgnoreCase(type)){
				DynaBean dynaBean=getDocDataBean(tableCode,pkValue);
				if(dynaBean!=null){
					String fieldValue=dynaBean.getStr(fieldCode);
					if(StringUtil.isNotEmpty(fieldValue)){
						String[] fileValues=fieldValue.split("\\*");
						if(fileValues.length==2 && address.equalsIgnoreCase(fileValues[1])){

						}else{
							setDisableDoc(document);
						}
					}else{
						setDisableDoc(document);
					}
				}else{
					setDisableDoc(document);
				}
			}else if(DocumentType.BATCH.equalsIgnoreCase(type)){
				DynaBean dynaBean=getDocDataBean(tableCode,pkValue);
				if(dynaBean!=null){
					String fieldValue=dynaBean.getStr(fieldCode);
					if(StringUtil.isNotEmpty(fieldValue) && fieldValue.startsWith("[")){
						JSONArray arrays=JSONArray.fromObject(fieldValue);
						boolean flag=false;
						for(int i=0;i<arrays.size();i++){
							JSONObject obj=arrays.getJSONObject(i);
							if(address.equalsIgnoreCase(obj.get("path")+"")){
								flag=true;
							}
						}
						if(!flag){
							setDisableDoc(document);
						}
					}else{
						setDisableDoc(document);
					}
				}else{
					setDisableDoc(document);
				}
			}else if(DocumentType.FUNC.equalsIgnoreCase(type)){
				DynaBean dynaBean=getDocDataBean(tableCode,pkValue);
				if(dynaBean!=null){

				}else{
					setDisableDoc(document);
				}
			}else{
				//富文本、其他暂不处理
			}
		}
	}
	private DynaBean getDocDataBean(String tableCode,String pkValue){
		return serviceTemplate.selectOneByPk(tableCode,pkValue);
	}

	/**
	 * 设定文档预删除
	 * @param document
	 */
	private void setDisableDoc(DynaBean document){
		document.set("SY_FLAG","2");
		serviceTemplate.update(document);
	}
	@Override
	public void doClearNoExistDoc(String whereSql) {
		List<DynaBean> documents=serviceTemplate.selectList("JE_CORE_DOCUMENT",whereSql);
		for(DynaBean document:documents){
			if("0".equalsIgnoreCase(document.getStr("SY_FLAG"))){
				setDisableDoc(document);
			}
		}
	}
	@Override
	public void doClearDisableDoc(String whereSql) {
		List<DynaBean> documents=serviceTemplate.selectList("JE_CORE_DOCUMENT",whereSql+" AND SY_FLAG='2'");
		for(DynaBean document:documents){
			deleteDoc(document);
		}
	}
	@Override
	public void doCheckDoc(String whereSql) {
		List<DynaBean> documents=serviceTemplate.selectList("JE_CORE_DOCUMENT",whereSql);
		for(DynaBean document:documents){
			boolean existFile=jeFileManager.existsFile(document);
			document.set("SY_FLAG",existFile?"1":"0");
			serviceTemplate.update(document);
		}
	}

	@Override
	public void doSaveDiskDoc(String whereSql) {
		List<DynaBean> documents=serviceTemplate.selectList("JE_CORE_DOCUMENT",whereSql);
		for(DynaBean document:documents){
			String address=document.getStr("DOCUMENT_ADDRESS");
			try {
				jeFileManager.copyFile(address, JEFileType.PROJECT, document.getStr("DOCUMENT_SAVETYPE"),address, JEFileType.PROJECT);
				document.set("DOCUMENT_BUSTYPE", document.getStr("DOCUMENT_BUSTYPE"));
				document.set("DOCUMENT_SAVETYPE",JeFileUtil.getFileSaveType());
				serviceTemplate.update(document);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
