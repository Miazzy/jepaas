package com.je.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 在java中有时我们会调用系统命令或批处理或shell脚本
 * @author YUNFENGCHENG
 * 2011-9-19 下午04:29:13
 */
public abstract class ExecCommand {
	private static final Log log = LogFactory.getLog(ExecCommand.class);

	/**
	 * 
	 * @描述 在单独的进程中执行指定的字符串命令。
	 * @作者 云凤程
	 * @日期 Sep 9, 2011
	 * @时间 5:15:48 PM
	 * @param command 一条指定的系统命令
	 * @throws IOException
	 */
	public void exec(String command) throws IOException {
		exec(command , null , null);
	}
	/**
	 * 
	 * @描述 在有指定工作目录的独立进程中执行指定的字符串命令。
	 * @作者 云凤程
	 * @日期 Sep 17, 2011
	 * @时间 11:17:59 AM
	 * @param command 一条指定的系统命令
	 * @param workpath 子进程的工作目录；如果子进程应该继承当前进程的工作目录，则该参数为null。
	 * @throws IOException
	 */
	public void exec(String command,String workpath) throws IOException {
		exec(command , null , workpath);
	}
	
	/**
	 * 
	 * @描述 在有指定环境和工作目录的独立进程中执行指定的字符串命令。
	 * @作者 云凤程
	 * @日期 Sep 17, 2011
	 * @时间 11:21:28 AM
	 * @param command 一条指定的系统命令
	 * @param envp 环境变量，字符串数组，其中每个元素的环境变量设置格式为 name=value；如果子进程应该继承当前进程的环境，则为null。
	 * @param path 子进程的工作目录；如果子进程应该继承当前进程的工作目录，则该参数为null。
	 * @throws IOException
	 */
	public void exec(String command,String[] envp,String workpath) throws IOException {
		InputStream is = null;
		BufferedInputStream in = null;
		BufferedReader br = null;
		try {
			File dir=null;
			if(null != workpath)
				dir=new File(workpath);
			log.info("【COMMAND】>>> "+command);
			// InputStream is = Runtime.getRuntime().exec(new String[]{"ping","127.0.0.1"}).getInputStream();
			is = Runtime.getRuntime().exec(command,envp,dir).getInputStream();
			in = new BufferedInputStream(is);
			br = new BufferedReader(new InputStreamReader(in));
			String ss = "";
			while ((ss = br.readLine()) != null) {
				lineHandler(ss);
			}
		} finally {
			if (null != br)
				br.close();
			if (null != in)
				in.close();
			if (null != is)
				is.close();
		}
	}
	
	/**
	 * 
	 * @描述 在单独的进程中执行指定的命令和参数。
	 * @作者 云凤程
	 * @日期 Sep 9, 2011
	 * @时间 5:15:48 PM
	 * @param commands 包含所调用命令及其参数的数组。例如：new String[]{"/home/user1/test.sh","arg1","arg2"};
	 * @throws IOException
	 */
	public void exec(String[] commands) throws IOException {
		exec(commands , null , null);
	}
	/**
	 * 
	 * @描述 在有指定工作目录的独立进程中执行指定的字符串命令。
	 * @作者 云凤程
	 * @日期 Sep 17, 2011
	 * @时间 11:17:59 AM
	 * @param commands 包含所调用命令及其参数的数组。例如：new String[]{"/home/user1/test.sh","arg1","arg2"};
	 * @param workpath 子进程的工作目录；如果子进程应该继承当前进程的工作目录，则该参数为null。
	 * @throws IOException
	 */
	public void exec(String[] commands,String workpath) throws IOException {
		exec(commands , null , workpath);
	}
	
	/**
	 * 
	 * @描述 在有指定环境和工作目录的独立进程中执行指定的字符串命令。
	 * @作者 云凤程
	 * @日期 Sep 9, 2011
	 * @时间 5:18:00 PM
	 * @param commands 包含所调用命令及其参数的数组。例如：new String[]{"/home/user1/test.sh","arg1","arg2"};
	 * @param envp 环境变量，字符串数组，其中每个元素的环境变量设置格式为 name=value；如果子进程应该继承当前进程的环境，则为null。
	 * @param path 子进程的工作目录；如果子进程应该继承当前进程的工作目录，则该参数为null。
	 * @throws IOException 
	 */
	public void exec(String[] commands,String[] envp , String workpath) throws IOException {
		InputStream is = null;
		BufferedInputStream in = null;
		BufferedReader br = null;
		try {
			File dir=null;
			if(null != workpath)
				dir=new File(workpath);
			log.info("【COMMAND】>>>："+getCommandString(commands));
			is = Runtime.getRuntime().exec(commands,envp,dir).getInputStream();
			in = new BufferedInputStream(is);
			br = new BufferedReader(new InputStreamReader(in));
			String ss = "";
			while ((ss = br.readLine()) != null) {
				lineHandler(ss);
			}
		} finally {
			if (null != br)
				br.close();
			if (null != in)
				in.close();
			if (null != is)
				is.close();
		}
	}
	/**
	 * 
	 * @描述 仅为日志输出，无其他作用
	 * @作者 云凤程
	 * @日期 Sep 13, 2011
	 * @时间 1:48:06 PM
	 * @param commands
	 * @return
	 */
	private String getCommandString(String[] commands){
		StringBuffer sb=new StringBuffer();
		for(String command:commands){
			sb.append(command);
			sb.append(" ");
		}
		return sb.toString();
	}
	/**
	 * 
	 * @描述 行处理
	 * @作者 云凤程
	 * @日期 Sep 9, 2011
	 * @时间 5:22:11 PM
	 * @param lineStr
	 */
	protected abstract void lineHandler(String lineStr);
}
