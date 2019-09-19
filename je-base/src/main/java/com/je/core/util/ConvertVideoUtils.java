package com.je.core.util;

import com.je.core.constants.doc.JEFileSaveType;
import com.je.core.constants.doc.JEFileType;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.document.util.DiskFileUtil;
import com.je.document.util.JeFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * 转换视频的工具类
 * @author zhangshuaipeng
 *
 */
public class ConvertVideoUtils {
	private static Logger logger = LoggerFactory.getLogger(ConvertVideoUtils.class);
	/**
	 * 转换指定格式的视频文件
	 * @param fromPath  文件路径
	 * @param toPath   目标路径
	 * @param aviPath  中间avi存放的路径(wmv9，rm，rmvb等需要转成成avi再从avi转成的flv)
	 * @return
	 */
	public static Boolean convertVideo(String fromPath,String toPath,String aviPath){
		boolean status = false;
		File file=new File(fromPath);
		if(!file.exists()){
			logger.error("文件不存在");
			return false;
		}
		 int type = checkContentType(fromPath);
         if (type==0) {
             status = processFLV(fromPath,toPath);// 直接将文件转为flv文件           
         } else if (type==1) {
             String avifilepath = processAVI(type,fromPath,toPath);
             if (avifilepath == null)
                 return false;// avi文件没有得到
             status = processFLV(avifilepath,toPath);// 将avi转为flv
         }
         return status;
	}
	/**
	 * 获取文件的类型
	 * @param filePath
	 * @return
	 */
	private static int checkContentType(String filePath) {
	         String type = filePath.substring(filePath.lastIndexOf(".") + 1,
	        		 filePath.length()).toLowerCase();
	//ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	         if (type.equals("avi")) {
	             return 0;
	         } else if (type.equals("mpg")) {
	             return 0;
	         } else if (type.equals("wmv")) {
	             return 0;
	         } else if (type.equals("3gp")) {
	             return 0;
	         } else if (type.equals("mov")) {
	             return 0;
	         } else if (type.equals("mp4")) {
	             return 0;
	         } else if (type.equals("asf")) {
	             return 0;
	         } else if (type.equals("asx")) {
	             return 0;
	         } else if (type.equals("flv")) {
	             return 0;
	         }
	         //对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
	         else if (type.equals("wmv9")) {
	             return 1;
	         } else if (type.equals("rm")) {
	             return 1;
	         } else if (type.equals("rmvb")) {
	             return 1;
	         }       
	         return 9;
	     }
	/**
	 * 判断是否是文件
	 * @param path
	 * @return
	 */
	  private static boolean checkfile(String path){
	      File file=new File(path);
	      if(!file.isFile()){
	       return false;
	      }
	      return true;
	     }
	  /**
	   * 利用mencoder将其他格式转换成avi文件
	   * @param type
	   * @param fromPath
	   * @param aviPath
	   * @return
	   */
	  private static String processAVI(int type,String fromPath,String aviPath) {
	         List commend=new java.util.ArrayList();
	         commend.add("e:\\mencoder");
	         commend.add(fromPath);
	         commend.add("-oac");
	         commend.add("lavc");
	         commend.add("-lavcopts");
	         commend.add("acodec=mp3:abitrate=64");
	         commend.add("-ovc");
	         commend.add("xvid");
	         commend.add("-xvidencopts");
	         commend.add("bitrate=600");
	         commend.add("-of");
	         commend.add("avi");
	         commend.add("-o");
	         commend.add(aviPath);
	       
	         try{
	          ProcessBuilder builder = new ProcessBuilder();
	             builder.command(commend);
	             builder.start();
	             return aviPath;
	         }catch(Exception e){
	         	throw new PlatformException("利用mencoder将其他格式转换成avi文件", PlatformExceptionEnum.JE_CORE_UTIL_TOVIDEO_ERROR,new Object[]{type,fromPath,aviPath},e);
	         }
	     }
	//   ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	  	/**
	  	 * 转换成flv格式的视频文件
	  	 * @param oldfilepath
	  	 * @return
	  	 */
	     private static boolean processFLV(String oldfilepath,String toPath) { 
	       if(!checkfile(oldfilepath)){
	           logger.error(oldfilepath+" is not file");
	           return false;
	          }     
	       
	         List commend=new java.util.ArrayList();
	         commend.add(DiskFileUtil.getBasePath(JEFileSaveType.DEFAULT)+"/resource/videConver/ffmpeg.exe");
	         commend.add("-i");
	         commend.add(oldfilepath);
	         commend.add("-y");
	         commend.add("32");
	         commend.add("-ar");
	         commend.add("22050");
	         commend.add("-b");
	         commend.add("800000");
	         commend.add("-s");
	         commend.add("640*480");
	         commend.add(toPath);
	         try {
	        	 Runtime runtime = Runtime.getRuntime();
	             String cmd = "";
	             String cut = JeFileUtil.webrootAbsPath+"/resource/videConver/ffmpeg.exe -i "+oldfilepath+" -y -ab 32 -ar 22050 -b 800000 -s 640*480 "+toPath;
	             String cutCmd = cmd + cut;
	             runtime.exec(cutCmd);
	             ProcessBuilder builder = new ProcessBuilder();
	             builder.command(commend);
	             builder.start();
	             return true;
	         } catch (Exception e) {
				 throw new PlatformException("转换成flv格式的视频文件", PlatformExceptionEnum.JE_CORE_UTIL_TOVIDEO_ERROR,new Object[]{oldfilepath,toPath},e);
	         }
	     }
//	     public static void main(String[] args) {
//	    	 ConvertVideoUtils.convertVideo("D:/1.3gp", "D:/3.flv", "");
//
//		}
}
