<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*"%>
<%
FileSaver fs = new FileSaver(request, response);
String filePath = fs.getFormField("filePath");
fs.saveToFile(request.getSession().getServletContext().getRealPath("//") + filePath);
//fs.showPage(300,300);
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
