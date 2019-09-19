package com.je.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import jxl.common.Logger;

/**
 * propties文件的工具类  只能做一次修改
 * @author zhangshuaipeng
 *
 */
public class PropFileUtils {
	private static Logger logger=Logger.getLogger(PropFileUtils.class); 
	private Properties properties=new Properties();
	private FileInputStream fis=null;
	private FileOutputStream fos=null;
	public PropFileUtils(File file){
		try{
		fis = new FileInputStream(file);
		fos = new FileOutputStream(file);
		properties.load(fis);
		}catch(Exception e){
			throw new PlatformException("配置文件工具类读取文件异常", PlatformExceptionEnum.JE_CORE_UTIL_PROP_READFILE_ERROR,new Object[]{file.getPath()},e);
		}
	}
	public PropFileUtils(File file,boolean write,boolean read){
		try{
		if(read){
			fis = new FileInputStream(file);
			properties.load(fis);
		}
		if(write){
			fos = new FileOutputStream(file);
		}
		}catch(Exception e){
			throw new PlatformException("配置文件工具类读取文件异常", PlatformExceptionEnum.JE_CORE_UTIL_PROP_READFILE_ERROR,new Object[]{file.getPath()},e);
		}
	}
	public String readKey(String key){
		if(properties.containsKey(key)){
			return properties.getProperty(key);
		}else{
			return "";
		}
	}
	public boolean containsKey(String key){
		return properties.contains(key);
	}
	public void put(String key,String value){
		properties.setProperty(key, StringUtil.getDefaultValue(value,""));
	}
	
	/**
	 * 添加键值对，添加完流关闭
	 * @param key
	 * @param value
	 */
	public void createFile(List<String> keys,List<String> values){
		for(int i=0;i<keys.size();i++){
			properties.put(keys.get(i), values.get(i));			
		}
		try {
			properties.store(fos, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			FileOperate.close(fis,fos,null,null);
		}
	}
	public void closeWrite(){
		try {
			properties.store(fos, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			FileOperate.close(null,fos,null,null);
		}
	}
	public void close(){
		FileOperate.close(fis,fos,null,null);
	}
}
