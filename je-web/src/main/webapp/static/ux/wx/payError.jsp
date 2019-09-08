<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" import="com.je.core.constants.LoginErrorType"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%
		String error=session.getAttribute("error")+"";
	%>
	<title><%=com.je.core.util.WebUtils.sysVar.get("JE_SYS_TITLE")%></title>
	<link rel="shortcut icon" href="/favicon.ico"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="/static/ux/login/register/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	<link href="/static/ux/login/register/css/templatemo_style.css" rel="stylesheet" type="text/css">	
	<script src="/static/ux/JE/resource/login/register/register/css/jquery-1.9.1.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/ux/login/register/css/bootstrapValidator.min.css"/>
    <script type="text/javascript" src="/static/ux/login/register/js/bootstrapValidator.js"></script>      
</head>
<body class="templatemo-bg-gray">
	<h1>跳转支付界面,请联系管理员!</h1>
	<h4>原因：<%=error%></h4>
</body>
</html>