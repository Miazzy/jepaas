<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" import="com.je.core.constants.LoginErrorType"%>

<% 
	//html模版
	String htmlTpl = request.getParameter("htmlTpl");
	try{
		htmlTpl = java.net.URLDecoder.decode(htmlTpl,"UTF-8");
	}catch(java.lang.Exception ex){
		htmlTpl = "打印出错，请联系管理员！</br>错误信息："+ex.getMessage();
	}
	//标题
	String title = request.getParameter("title");
	title =  title == null || "".equals(title)? "预览" : title;
	//显示打印按钮和纸张边框
	String print = request.getParameter("print");
 %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=title%></title>
<style media="print" >   
	.Noprint{display:none;}   
	.PageNext{page-break-after:   always;}  
	.NoBorder{border:none !important;} 
	html,body{padding:0px !important;margin:0px !important;}
</style>  
<style>

	html,body{height:100%;margin:0px;padding:0px;}
	
	.div-box{
		font-family: tahoma, arial, verdana, sans-serif;
		font-size:12px;
		height:100%;
	}
	.pagebody{
		-moz-transform-origin: 0 0;
		-webkit-transform-origin:0 0;
		-o-transform-origin:0 0;
	}
	.div-box *{
		box-sizing: border-box;
		-moz-box-sizing: border-box;
		-ms-box-sizing: border-box;
		-webkit-box-sizing: border-box;
	}
	
	.table-grid-cell-inner{
		text-overflow: ellipsis;
		padding: 3px 6px 4px 6px;
		border:1px solid #000;
		table-layout:fixed;
		word-break:break-all;
	}
	
	.childFunc table tr.firstRow td { 
		border-top: 0px !important; 
	} 
	.childFunc table tr.lastRow td { 
		border-bottom: 0px !important; 
	} 
	.childFunc table tr td.firstCol { 
		border-left: 0px !important; 
	} 
	.childFunc table tr td.lastCol { 
		border-right: 0px !important; 
	} 
	
</style>
<script>
	var scaleX = 1,scaleY = 1;
	var zoom = function(jj,xy){
		var pages = printHtmlBody.querySelectorAll('div.pagediv-border div.pagebody');
		if(xy=='X' || xy == ''){
			scaleX += jj=='+'?0.01:-0.01;
		}
		if(xy=='Y' || xy == ''){
			scaleY += jj=='+'?0.01:-0.01;
		}
		for(var i=0;i<pages.length;i++){
			var page = pages[i];
			page.style['-webkit-transform'] = 'scale('+scaleX+','+scaleY+')';
		}
	
	}
</script>
<link  rel="stylesheet" href="/static/css/je-ueditor.css" />
</head>
<body>
	<% 
		if(!"false".equals(print)){
	 %>
		<style>
			.pagediv-border{
				border:1px solid #cccccc;
			}
		</style>
		<p style="padding:10px" class="Noprint">
			<input class="Noprint" type="button" name="f-" onclick="zoom('+','');" value="等比例+" >
			<input class="Noprint" type="button" name="f-" onclick="zoom('-','');" value="等比例-" >
			<input class="Noprint" type="button" name="f-" onclick="zoom('+','X');" value="横向+" >
			<input class="Noprint" type="button" name="f-" onclick="zoom('-','X');" value="横向-" >
			<input class="Noprint" type="button" name="f-" onclick="zoom('+','Y');" value="纵向+" >
			<input class="Noprint" type="button" name="f-" onclick="zoom('-','Y');" value="纵向-" >
			<input class="Noprint" type="button" name="button_export" onclick="javascript:window.print();" style="margin-left:50px;" value="打印" >
		</p>
	<% } %>
	<!-- 打印的内容 -->
	<div id=printHtmlBody class="div-box">
		<%=htmlTpl%>
	</div>
	<script>
		var pages = printHtmlBody.querySelectorAll('div.pagediv-border');
		for(var i=0;i<pages.length;i++){
			var page = pages[i];
			for(var j=0;j<page.childNodes.length;j++){
				var div = page.childNodes[j];
				var cls = div.getAttribute('class') || '';
				cls += ' pagebody';
				//if(!div.hasAttribute('class')){
					div.setAttribute('class',cls);
				//}
			}
		}
	</script>
</body>
</html>

