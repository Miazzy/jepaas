package com.je.core.base;

import com.baidu.ueditor.ActionEnter;
import com.je.core.util.FileOperate;
import net.coobird.thumbnailator.Thumbnails;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * UEditor的Servlet用于处理Ueditor的文件上传
 *
 * @ProjectName: je-platform
 * @Package: com.je.core.web.servlet
 * @ClassName: ${TYPE_NAME}
 * @Description: UEditor文件上传的处理类，需要在ueditor.JEconfig.js中指定
 * @Author: LIULJ
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2018</p>
 */
public class UEditorServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(UEditorServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doUEditorOperation(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doUEditorOperation(request,response);
    }

    /**
     * 进行Ueditor的相关处理
     * @param request 请求
     * @param response 响应
     * @return
     */
    private void doUEditorOperation(HttpServletRequest request,HttpServletResponse response){
        String result = null;
        try {
            request.setCharacterEncoding( "utf-8" );
            response.setHeader("Content-Type" , "text/html");
            String rootPath = request.getRealPath( "/" );
            result = new ActionEnter( request, rootPath ).exec();
            //处理图片的url
            String action = request.getParameter("action");
            String imageUrlType = request.getParameter("imageUrlType");
            if("uploadimage".equals(action) && "base64".equals(imageUrlType)){
                JSONObject jb = JSONObject.fromObject(result);
                if("SUCCESS".equals(jb.get("state").toString())){
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
        }catch (Exception e){
            logger.error("doUEditorOperation error ", e);
            result = e.getMessage();
        }

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Writer writer = null;
        try {
            writer = response.getWriter();
            writer.write(result);
            writer.flush();
        } catch (IOException e) {
            logger.error("doUEditorOperation writer.write error ", e);
        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) { }
            }
        }
    }
}
