<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" import="com.je.core.constants.LoginErrorType"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%
		String payImg=request.getSession().getAttribute("PAYIMG")+"";
		String name=request.getSession().getAttribute("NAME")+"";
		String money=request.getSession().getAttribute("MONEY")+"";
		String order=request.getSession().getAttribute("ORDER")+"";
		String orderId=request.getSession().getAttribute("ORDERID")+"";
	%>
	<title><%=com.je.core.util.WebUtils.sysVar.get("JE_SYS_TITLE")%></title>
	<link rel="shortcut icon" href="/favicon.ico"/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="/static/ux/login/register/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	<link href="/static/ux/login/register/css/templatemo_style.css" rel="stylesheet" type="text/css">
	<script src="/static/ux/login/register/js/jquery-1.11.1.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/static/ux/login/register/css/bootstrapValidator.min.css"/>
    <script type="text/javascript" src="/static/ux/login/register/js/bootstrapValidator.js"></script>
    <!--<script>
    	var orderId='<%=orderId%>';    	
    	var t = setInterval(function(){
				var obj = $.ajax({
					   type: "POST",async:false,dataType:'json',
					   url: "/saas/saasSellAction!jcOrder.action",
					   data: {orderId:orderId}
					}).responseText;
				obj=jQuery.parseJSON(obj);
				if(obj.success){
					clearInterval(t);
					window.location.href="/static/ux/login/register/paySuccess.jsp";
				}	        						
			},3000);    				
    </script> -->       
</head>
<body class="templatemo-bg-gray" style="color:white;" align="center">
	<h3>订单号：<%=order%></h3>
	<h3>商品：<%=name%></h3>
	<h3>价格：<%=money%></h3>
	<img src="<%=payImg%>" width=200 height=200 />
</body>
</html>