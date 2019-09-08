package com.je.core.util;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class HttpIOTool {
	private static  String Cookie = null;
	public static void downloadFile(String remoteFilePath, String localFilePath)
    {
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFilePath);
        if(!f.getParentFile().exists()) {
        	f.getParentFile().mkdirs();
        }
        try
        {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            if(Cookie == null){
            	String sessionId = "";
                Map hfs=httpUrl.getHeaderFields();
                Set<String> keys=hfs.keySet();
                for(String str:keys){
                    List<String> vs=(List)hfs.get(str);
                	if("Set-Cookie".equals(str)){
	                    for(String v:vs){
	                    	v = v.split(";")[0];
	                    	sessionId+=v+";";
	                    }
                	}	                    
                }
                Cookie = sessionId;
            }else{
            	System.out.println(Cookie);
            	httpUrl.addRequestProperty("Cookie", Cookie);
            }
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }catch (Exception e){
            throw new PlatformException("HTTP文件工具类下载文件异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTPIO_DOWNLOAD_ERROR,new Object[]{remoteFilePath,localFilePath},e);
        }finally{
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    throw new PlatformException("HTTP文件工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTPIO_CLOSEIO_ERROR,new Object[]{remoteFilePath,localFilePath},e);
                }
            }
            if(bos!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    throw new PlatformException("HTTP文件工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTPIO_CLOSEIO_ERROR,new Object[]{remoteFilePath,localFilePath},e);
                }
            }
        }
    }		
}
