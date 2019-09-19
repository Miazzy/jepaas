package com.je.message.thread;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件保存线程
 * @author zhangshuaipeng
 *
 */
public class FileSaveTread extends Thread{
	/**文件保存地址*/
	private String filePath;
	/**文件流*/
	private InputStream in;
	
	public FileSaveTread(String filePath, InputStream in) {
		super();
		this.filePath = filePath;
		this.in = in;
	}

	@Override
	public void run() {
		FileOutputStream out =null;   
		try {
			out =new FileOutputStream(filePath);
			int data;  
			while((data = in.read()) != -1) {  
	          out.write(data);  
	      }  
	      in.close();  
	      out.close(); 
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 System.out.println("文件写入失败...");
			 e.printStackTrace();
		 }   
	}
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}
	
	
}
