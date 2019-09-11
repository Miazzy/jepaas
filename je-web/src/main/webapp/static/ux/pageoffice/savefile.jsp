<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*"%>
<%
FileSaver fs = new FileSaver(request, response);
String filePath = fs.getFormField("filePath");
String localFileName = fs.getFormField("localFileName");
String parentPkId = fs.getFormField("parentPkId");
if(filePath == null || "".equals(filePath.trim()) || "null".equals(filePath.trim())){
	if(localFileName == null || "".equals(localFileName.trim()) || "null".equals(localFileName.trim())){
		localFileName = fs.getLocalFileName();
	}
	filePath = "/static/ux/pageoffice/doc/"+ parentPkId+"[_]"+localFileName;
}
fs.saveToFile(request.getSession().getServletContext().getRealPath(filePath));
fs.setCustomSaveResult("{filePath:'"+filePath+"'}");
fs.close();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title></title>
</head>
<body>
    <div>
    
    </div>
</body>
</html>
