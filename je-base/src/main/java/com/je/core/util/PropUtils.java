/**
 * 
 */
package com.je.core.util;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author chenmeng
 * 2012-4-19 下午4:45:13
 */
public class PropUtils {
	
	private static final String NULL_PROP = "";
	/**
     * 根据一个key读取sysconfig.properties文件的值，如果读取失败则返回NULL_PROP字符串
     * @param key
     * @return
     */
    public static String get(String key) {
		try {
			Properties props = PropertiesLoaderUtils.loadAllProperties("sysconfig.properties");
			return props.getProperty(key, NULL_PROP);
		} catch (IOException e) {
			throw new PlatformException("系统配置文件工具类获取CONFIG的配置文件属性值异常", PlatformExceptionEnum.JE_CORE_UTIL_SYSPROP_CONFIG_ERROR,new Object[]{key},e);
		}
    	
    }
    /**
     * 根据一个key读取sysconfig.properties文件的值，如果读取失败则返回NULL_PROP字符串
     * @param key
     * @return
     */
    public static String getJdbc(String key) {
    	try {
    		Properties props = PropertiesLoaderUtils.loadAllProperties("jdbc.properties");
    		return props.getProperty(key, NULL_PROP);
    	} catch (IOException e) {
    		throw new PlatformException("系统配置文件工具类获取JDBC的配置文件属性值异常", PlatformExceptionEnum.JE_CORE_UTIL_SYSPROP_JDBC_ERROR,new Object[]{key},e);
    	}
    	
    }
}
