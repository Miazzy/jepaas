<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" import="com.je.core.constants.LoginErrorType"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>打印预览</title>
<% 
	//html模版
	String htmlTpl = request.getParameter("htmlTpl");
 %>
<script language="javascript">
	function onload(){
		document.getElementById('printHtmlBody').innerHTML = '<%=htmlTpl%>';
	}
</script>
</head>
<body onload="onload();">
	<!-- 打印的内容 -->
	<div id=printHtmlBody ></div>
</body>
</html>

