/**
 *
 */
package com.je.document.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.je.core.base.MethodArgument;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author marico
 * 2012-5-8 下午1:04:51
 */
public interface DocumentManager {
	/**
	 * 构建文件信息
	 * @param param
	 */
	public void doBuildFileInfo(MethodArgument param);

	/**
	 * 删除当前表的指定字段所有文件
	 * @param domainName TODO未处理
	 * @param domainField TODO未处理
	 * @param domainId TODO未处理
	 */
	public void doRemoveDocuments(String domainName, String domainField, String domainId);
	/**
	 * 删除当前表的所有文件
	 * @param domainName TODO未处理
	 * @param domainId TODO未处理
	 */
	public void doRemoveDocuments(String domainName, String domainId);
	/**
	 * 删除当前表的所有文件
	 * @param whereSql 条件
	 * @param beanTableCode 表名
	 */
	public void doRemoveDocuments4Sql(String whereSql, String beanTableCode);
	/**
	 * 删除当前表的所有文件
	 * @param domainName TODO未处理
	 * @param domainId TODO未处理
	 */
	public void doRemoveTreeDocuments(String domainName, String domainId);
	/**
	 * 复制文档文件
	 * @param dynaBean 原dynaBean
	 * @param fieldCode 文件编码
	 * @param pkValue TODO未处理
	 * @return
	 */
	public String doCopyDocument(DynaBean dynaBean, String fieldCode, String pkValue);
	/**
	 * 复制文档
	 * @param dynaBean TODO未处理
	 * @param newFuncCode TODO未处理
	 * @param newTableCode TODO未处理
	 * @param newFieldCode TODO未处理
	 * @param newPkValue TODO未处理
	 */
	public String doCopyDocument(DynaBean dynaBean, String fieldCode, String newFuncCode, String newTableCode, String newFieldCode, String newPkValue);
	/**
	 * 复制批量文档文件
	 * @param dynaBean TODO未处理
	 * @param fieldCode TODO未处理
	 * @param pkValue TODO未处理
	 * @return
	 */
	public String doCopyBatchDocument(DynaBean dynaBean, String fieldCode, String pkValue);
	/**
	 * 复制批量文档
	 * @param dynaBean TODO未处理
	 * @param newFuncCode TODO未处理
	 * @param newTableCode TODO未处理
	 * @param newFieldCode TODO未处理
	 * @param newPkValue TODO未处理
	 */
	public String doCopyBatchDocument(DynaBean dynaBean, String fieldCode, String newFuncCode, String newTableCode, String newFieldCode, String newPkValue);
	/**
	 * 持久document表数据
	 * @param file 文件
	 * @param funcCode  TODO未处理
	 * @param tableCode 表名
	 * @param fieldCode 文件编码
	 * @param fileName 文件名称
	 * @param address 地址
	 */
	public DynaBean doSaveDcoument(File file, String funcCode, String tableCode, String fieldCode, String pkValue, String fileName, String address, String jeFileType, String type);
	/**
	 * 持久document表数据
	 * @param relativeFilePath  TODO未处理
	 * @param jeFileType TODO未处理
	 * @param funcCode TODO未处理
	 * @param tableCode 表名
	 * @param fieldCode 文件编码
	 * @param pkValue  TODO未处理
	 * @param fileName 文件名称
	 * @param address 地址
	 * @return
	 */
	public DynaBean doSaveDcoument(String relativeFilePath, String jeFileType, String funcCode, String tableCode, String fieldCode, String pkValue, String fileName, String address);
	/**
	 * 处理所有附件上传  单附件，多附件
	 * @param request 请求报文
	 * @param params 参数
	 * @param dynaBean TODO未处理
	 * @param currentUser 创建用户
	 * @param type 类型
	 */
	public void processFileUpload(HttpServletRequest request, Map<String, Object> params, DynaBean dynaBean, EndUser currentUser, String type);
	/**
	 * 功能多附件上传
	 * @param dynaBean TODO未处理
	 * @param batchFilesFields TODO未处理
	 * @param funcCode TODO未处理
	 * @param request TODO未处理
	 * @param doSave TODO未处理
	 */
	public void processBatchUpload(DynaBean dynaBean, String batchFilesFields, String funcCode, Boolean doSave, HttpServletRequest request);
	/**
	 * 批量上传附件处理
	 * @param dynaBean TODO未处理
	 * @param batchFilesFields TODO未处理
	 * @param funcCode TODO未处理
	 * @param funcFolderPath TODO未处理
	 * @param doSave TODO未处理
	 * @param jeFileType TODO未处理
	 */
	public void doSaveBatchFiles(DynaBean dynaBean, String batchFilesFields, String funcCode, String funcFolderPath, Boolean doSave, String jeFileType);
	/**
	 * 上传UEEDITORER文件
	 * @param file 文件
	 * @param params TODO未处理
	 */
	public DynaBean processUeEditorFileUpload(File file, Map<String, Object> params);

	/**
	 * 删除批量附件处理
	 * @param tableCode 表名
	 * @param ids ID集合
	 */
	public void doRemoveBatchFiles(String tableCode, String ids);
	/**
	 * 删除树形批量附件处理
	 * @param tableCode 表名
	 * @param ids ID集合
	 */
	public void doRemoveTreeBatchFiles(String tableCode, String ids);
	/**
	 * 保存网盘文件
	 * @param file 文件
	 * @param fileName 文件名称
	 * @param dynaBean  TODO未处理
	 */
	public void saveDocumentionMultipart(MultipartFile file, String fileName, DynaBean dynaBean, String formatType);
	/**
	 * 保存网盘文件
	 * @param file 文件
	 * @param fileName 文件名称
	 * @param dynaBean 传入信息
	 */
	public void saveDocumentionFile(File file, String fileName, DynaBean dynaBean, String formatType);
	/**
	 * 保存网盘文件
	 * @param file 文件
	 * @param fileName 文件名称
	 * @param dynaBean 传入信息
	 */
	public void saveDocumentionMultipartFile(MultipartFile file, String fileName, DynaBean dynaBean, String formatType);
	/**
	 * 赋值网盘文件
	 * @param oldBean  TODO未处理
	 * @param dynaBean TODO未处理
	 */
	public void copyDocumentionFile(DynaBean oldBean, DynaBean dynaBean);
	/**
	 * 保存网盘文件
	 * @param file 文件
	 * @param fileName 文件名称
	 * @param dynaBean  TODO未处理
	 */
	public void saveDocumention(File file, String fileName, DynaBean dynaBean, String formatType);
	/**
	 * 赋值网盘文件
	 * @param oldBean TODO未处理
	 * @param dynaBean TODO未处理
	 */
	public void copyDocumention(DynaBean oldBean, DynaBean dynaBean);
	/**
	 * 获取上传目录
	 * @param funcCode TODO未处理
	 * @param funcFolderPath TODO未处理
	 * @param uploadPath TODO未处理
	 * @return
	 */
	public String getFolderPath(String funcCode, String funcFolderPath, String uploadPath);

	/**
	 * 构建图标
	 * @param doc TODO未处理
	 */
	public void buildIconCls(DynaBean doc);

	/**
	 * 上传文件
	 * @param folderPath 路径
	 * @param fullFilePath TODO未处理
	 * @param dynaBean TODO未处理
	 * @param uploadFieldName 上传的名字
	 * @param pkValue TODO未处理
	 * @param files 文件集合
	 * @param fileNames 文件名称
	 * @param fileTypes 文件类型
	 * @param documentInfo TODO未处理
	 * @param type 类型
	 * @param jeFileType TODO未处理
	 * @param funcCode TODO未处理
	 * @param currentUser 创建用户
	 */
	public void processUploadedFiles(String folderPath, String fullFilePath, DynaBean dynaBean, String uploadFieldName, String pkValue, File[] files, String[] fileNames, String[] fileTypes, List<DynaBean> documentInfo, String type, String jeFileType, String funcCode, EndUser currentUser);

	/**
	 * 将文件保存成文档形式
	 * @param file 文件对象
	 * @param relativeFilePath 文件相对路径
	 * @param fileName 文件名
	 * @param miniType 文件miniType
	 * @param jeFileType 保存的文件模式
	 * @param funcCode 功能名
	 * @param tableCode 表名
	 * @param pkValue 主键值
	 * @param fieldCode 字段名
	 */
	public void saveDocFile(File file, String relativeFilePath, String fileName, String miniType, String jeFileType, String funcCode, String tableCode, String pkValue, String fieldCode);

	/**
	 * 构建spring附件名称
	 * @param files 文件集合
	 * @return
	 */
	public String[] buildFileNamesArr(List<MultipartFile> files);

	/**
	 * 构建spring附件类型
	 * @param files 文件集合
	 * @return
	 */
	public String[] buildFileContentTypeArr(List<MultipartFile> files);

	/**
	 * 根据文件路径获取附件对象
	 * @param address 地址
	 * @param beanTableCode 表名
	 * @return
	 */
	public DynaBean getDocInfo(String address, String beanTableCode);
	/**
	 * 根据条件获取附件对象
	 * @param whereSql 条件
	 * @param beanTableCode 表名
	 * @return
	 */
	public List<DynaBean> getDocInfoList(String whereSql, String beanTableCode);
	/**
	 * 根据条件获取附件对象
	 * @param whereSql 条件
	 * @param beanTableCode 表名
	 * @param queryFields TODO未处理
	 * @return
	 */
	public List<DynaBean> getDocInfoList(String whereSql, String beanTableCode, String queryFields);

	/**
	 * 根据文件主键获取附件对象
	 * @param address 地址
	 * @param beanTableCode 表名
	 * @return
	 */
	public DynaBean getDocInfoByPk(String address, String beanTableCode);

	/**
	 * 保存附件对象
	 * @param dynaBean TODO未处理
	 * @return
	 */
	public DynaBean insertDoc(DynaBean dynaBean);

	/**
	 * 修改附件对象
	 * @param dynaBean TODO未处理
	 * @return
	 */
	public DynaBean updateDoc(DynaBean dynaBean);

	/**
	 * 删除附件对象
	 * @param deleteSql 删除SQL
	 * @param beanTableCode 表名
	 */
	public void deleteDoc(String deleteSql, String beanTableCode);
	/**
	 * 删除附件对象
	 * @param dynaBean  TODO未处理
	 */
	public void deleteDoc(DynaBean dynaBean);

	/**
	 * 清除与业务数据未关联得脏数据
	 * @param whereSql 条件
	 */
	public void doClearNoDataDoc(String whereSql);
	/**
	 * 清除已丢失的文件
	 * @param whereSql 条件
	 */
	public void doClearNoExistDoc(String whereSql);
	/**
	 * 清除预删除的文件
	 * @param whereSql 条件
	 */
	public void doClearDisableDoc(String whereSql);

	/**
	 * 检测文档文件
	 * @param whereSql 条件
	 */
	public void doCheckDoc(String whereSql);

	/**
	 * 保存到本地文件
	 * @param whereSql 条件
	 */
	public void doSaveDiskDoc(String whereSql);
}