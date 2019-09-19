package com.je.core.util;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * js、css处理工具【压缩、加密、不混淆】  确认工具类是否使用  TODO 吝
 * @author 研发部:云凤程
 */
public class JsCssPluginsUtil {

	public static final int JS = 0;
	public static final int CSS = 1;

//	public static void main(String[] args) {
//		String path = "E:/GridRendererUtil.js";
//		String topath = "E:/GridRendererUtil_min.js";
//		JsCssPluginsUtil.compress(JS, path, topath);
//	}

	/**
	 * 压缩方法
	 * @param type
	 * @param path
	 * @param topath
	 */
	public static void compress(int type, String path, String topath){
		
		if(type == JsCssPluginsUtil.CSS){
			
			InputStreamReader in = null;
			OutputStreamWriter out = null;
			
			try {
				in = new InputStreamReader(new FileInputStream(path), "utf-8");
				CssCompressor jsc = new CssCompressor(in);
				out = new OutputStreamWriter(new FileOutputStream(topath), "utf-8");
				jsc.compress(out, -1);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else if(type == JsCssPluginsUtil.JS){
			InputStreamReader in = null;
			OutputStreamWriter out = null;
			try {
				in = new InputStreamReader(new FileInputStream(path), "utf-8");
				JavaScriptCompressor jsc = new JavaScriptCompressor(in, new JavaScriptErrorReporter());
				out = new OutputStreamWriter(new FileOutputStream(topath), "utf-8");
				//压缩不混淆
				jsc.compress(out, -1, false, true, true, true);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		}else{
			new Exception("不支持此类型的文件压缩！");
		}
	}
}