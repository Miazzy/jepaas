<%@page import="java.io.File"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="net.coobird.thumbnailator.Thumbnails"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter,com.je.core.util.*,net.sf.json.JSONObject"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	String result = new ActionEnter( request, rootPath ).exec();
	//处理图片的url
	String action = request.getParameter("action");
	String imageUrlType = request.getParameter("imageUrlType");
	if("uploadimage".equals(action) && "base64".equals(imageUrlType)){
		JSONObject jb = JSONObject.fromObject(result);
		if("SUCCESS".equals(jb.get("state").toString())){
			OutputStream os = new ByteArrayOutputStream();
			String filePath = rootPath+jb.get("url").toString();
			File file = new File(filePath);
			String newFileUrl = file.getParent() + file.getName().substring(0, file.getName().lastIndexOf("."));
			Thumbnails.of(rootPath+jb.get("url").toString())
				.scale(1f).outputQuality(0.5f).outputFormat("jpg").toFile(newFileUrl);
			String base64 = FileOperate.encodeBase64File(newFileUrl + ".jpg");
			jb.put("url","data:image/jpeg;base64,"+base64);
		}
		result = jb.toString();
	}
	out.write( result );
	
%>