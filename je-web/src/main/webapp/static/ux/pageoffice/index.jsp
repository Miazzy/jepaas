<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file= "office.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% 
	String wdVal="auto";
	String hgVal="100%";
	
	String wd=request.getParameter("wd");
	if(com.je.core.util.StringUtil.isNotEmpty(wd)){
		wdVal=wd+"px";
	}
	String hg=request.getParameter("hg");
	if(com.je.core.util.StringUtil.isNotEmpty(hg)){
		hgVal=hg+"px";
	}
 %>
<html>
	<head>
		<title>OFFICE</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="">
		<style>
			body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,p,blockquote,th,td { margin:0; padding:0; font-size:12px; list-style:none; }
		</style>
	</head>

	<body>
		<script type="text/javascript">
			//文件打印事件
			var printWindowShow = function(){
				 document.getElementById("PageOfficeCtrl1").ShowDialog(4);
			}
			//文件保存事件
			var fileSave = function(){
				var obj = document.getElementById("PageOfficeCtrl1");
				//设置保存文件页面
				obj.SaveFilePage = 'saveTempFile.jsp';
	            obj.webSave();
			}
	    </script>
	    <form id="officeForm">
	    	<input name="filePath" id="filePath" type="hidden" value="<%=path%>" />
			<div style="width: <%=wdVal%>; height:<%=hgVal%>;">
				<po:PageOfficeCtrl id="PageOfficeCtrl1"></po:PageOfficeCtrl>
			</div>
		</form>
	</body>
</html>
