<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*"%>
<%@ page import="com.zhuozhengsoft.pageoffice.wordwriter.*"%>
<%@ page import="com.je.services.DBManager"%>
<%@ page import="net.sf.json.*"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.util.Map.*"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%!
// 拷贝文件
public void copyFile(String oldPath, String newPath){
	try {
		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) { //文件存在时 
			InputStream inStream = new FileInputStream(oldPath); //读入原文件 
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[1444];
			int length;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread; //字节数 文件大小 
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
			fs.close();
		}
	} catch (Exception e) {
		System.out.println("复制单个文件操作出错");
		e.printStackTrace();
	}
}
%>
<%
	PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
	//自定义工具栏
	poCtrl.setCustomToolbar(true);
	//标题栏
	poCtrl.setTitlebar(false);
	//隐藏office菜单栏
	poCtrl.setOfficeToolbars(true);
	//隐藏菜单栏
	poCtrl.setMenubar(false);
	poCtrl.setSaveFilePage("saveTempFile.jsp");
	poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");
	// 文档保存前事件
	//poCtrl.setJsFunction_AfterDocumentSaved( "JsFunction_AfterDocumentSaved()");
	//文档保存后事件
	//poCtrl.setJsFunction_BeforeDocumentSaved( "JsFunction_BeforeDocumentSaved()");
	//文档打开后事件
	//poCtrl.setJsFunction_AfterDocumentOpened("adjustCell()");
	poCtrl.addCustomToolButton("打印","printWindowShow()",6);
	poCtrl.addCustomToolButton("保存","fileSave()",1);
	
	String path = request.getParameter("path");
	String openPath = path;
	String docType = openPath.substring(openPath.lastIndexOf(".")+1,openPath.length());
	String readOnly = request.getParameter("readOnly");
	String isRevisionOnly = request.getParameter("isRevisionOnly");//是否开启强制留痕模式
	String username = request.getParameter("username");
	String mbName = request.getParameter("mb");
	// 选择模板后执行套红
	if (mbName != null && !"".equals(mbName.trim())) {
		// 复制模板，命名为正式发文的文件名：zhengshi.doc
		String templatePath = getServletContext().getRealPath(mbName);
		String oldPath  = getServletContext().getRealPath(openPath);
		StringBuffer pathBuf = new StringBuffer(oldPath);
		WordDocument doc = new WordDocument();
		DataRegion sTextS = doc.openDataRegion("PO_STextS");
		sTextS.setValue("[word]"+oldPath+"[/word]");
		poCtrl.setWriter(doc);
    	int offsetLen = pathBuf.length() - 4;
    	if(pathBuf.charAt(pathBuf.length() - 5) == '.'){
    		offsetLen = pathBuf.length() - 5;
    	}
    	pathBuf.insert(offsetLen, "_new");
    	String newPath = pathBuf.toString();
		copyFile( templatePath, newPath); 
		openPath = newPath;
	}
	if(openPath != null && !"".equals(openPath)){
		OpenModeType type = null;
		if(readOnly == null || !Boolean.parseBoolean(readOnly)){
			if("doc".equals(docType) || "docx".equals(docType)){
				if(Boolean.parseBoolean(isRevisionOnly)){
					type = OpenModeType.docRevisionOnly;
				}else{
					type = OpenModeType.docNormalEdit;
				}
			}else if("xls".equals(docType) || "xlsx".equals(docType)){
				type = OpenModeType.xlsNormalEdit;
			}else if("ppt".equals(docType) || "pptx".equals(docType)){
				type = OpenModeType.pptNormalEdit;
			}
		}else{
			//隐藏工具栏，以只读方式打开
			poCtrl.setCustomToolbar(false);
			if("doc".equals(docType) || "docx".equals(docType)){
				type = OpenModeType.docReadOnly;
			}else if("xls".equals(docType) || "xlsx".equals(docType)){
				type = OpenModeType.xlsReadOnly;
			}else if("ppt".equals(docType) || "pptx".equals(docType)){
				type = OpenModeType.pptReadOnly;
			}
		}
		poCtrl.webOpen(openPath,type , username);
	}
	poCtrl.setTagId("PageOfficeCtrl1");
%>
