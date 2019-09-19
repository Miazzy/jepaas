package com.je.core.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class FileOperation {
	protected File file;

	public FileOperation(File file) {
		this.file = file;
	}

	/**
	 * @return 文件大小
	 */
	public int getSize() throws PlatformException {
		InputStream in = null;
		try {
			in = createBufferedInputStream();
			return in.available();
		} catch (IOException e) {
//			e.printStackTrace();
			throw new PlatformException("文件工具类获取文件大小异常。", PlatformExceptionEnum.JE_CORE_UTIL_FILE_GETSIZE_ERROR,new Object[]{file.getPath()},e);
		} finally {
			closeable(in);
		}
	}

	/**
	 * 获得文件内容
	 * @return
	 * @throws PlatformException
	 */
	public String getContent() throws PlatformException {
		BufferedReader reader = null;
		try {
			reader = createBufferedReader();
			StringBuilder content = new StringBuilder();
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				content.append(line);
			}
			return content.toString();
		} catch(IOException e) {
//			throw new FileOperationException("获取文件内容时发生错误。", e);
			throw new PlatformException("文件工具类获取文件内容异常!", PlatformExceptionEnum.JE_CORE_UTIL_FILE_GETCONTENT_ERROR,new Object[]{file.getPath()},e);
		} finally {
			closeable(reader);
		}
	}

	/**
	 * 保存文件
	 * @param toFile
	 * @throws PlatformException
	 */
	public void save(File toFile) throws PlatformException {
		InputStream in = null;
		OutputStream out = null;

		try {
			in = createBufferedInputStream();
			out = createBufferedOutputStream(toFile);
			byte[] buffer = new byte[8192];
			// 存储文件
			while (read(in, buffer) > 0) {
				write(out, buffer);
			}
		}catch (Exception e){
			throw new PlatformException("文件工具类文件写入异常。", PlatformExceptionEnum.JE_CORE_UTIL_FILE_SAVEFILE_ERROR,new Object[]{file.getPath()},e);
		}finally {
			closeable(in);
			closeable(out);
		}
	}

	/**
	 * 创建文件读取器
	 * @return
	 */
	protected BufferedReader createBufferedReader() {
		try {
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			throw new PlatformException("创建文件读取器时发生错误。", PlatformExceptionEnum.JE_DOC_FILE_READER_ERROR,new Object[]{file.getPath()},e);
		}
	}

	/**
	 * 创建输入流
	 * @return
	 * @throws PlatformException
	 */
	protected BufferedInputStream createBufferedInputStream() throws PlatformException {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new PlatformException("文件工具类创建文件输入流异常。", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CREATEIO_ERROR,new Object[]{file.getPath()},e);
//			e.printStackTrace();
//			throw new FileOperationException("创建文件输入流时发生错误。", e);
		}
	}

	/**
	 * 创建输出流
	 * @param file
	 * @throws
	 */
	protected BufferedOutputStream createBufferedOutputStream(File file) throws PlatformException{
		try {
			return new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			throw new PlatformException("文件工具类创建文件输出流异常。", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CREATEIO_ERROR,new Object[]{file.getPath()},e);
//			e.printStackTrace();
//			throw new FileOperationException("创建文件输入流时发生错误。", e);
		}
	}

	/**
	 * 从输入流中读取下一个字节
	 * @param in
	 * @param buffer
	 * @return
	 * @throws PlatformException
	 */
	protected int read(InputStream in, byte[] buffer) throws PlatformException {
		try {
			return in.read(buffer);
		} catch (IOException e) {
			throw new PlatformException("文件工具类读取文件异常。", PlatformExceptionEnum.JE_CORE_UTIL_FILE_READ_ERROR,new Object[]{file.getPath()},e);
//			e.printStackTrace();
//			throw new FileOperationException("从输入流中读取下一个字节时发生错误。", e);
		}
	}

	/**
	 * 输出流写入
	 * @param out
	 * @param buffer
	 */
	protected void write(OutputStream out, byte[] buffer) throws PlatformException{
		try {
			out.write(buffer);
		} catch (IOException e) {
			throw new PlatformException("文件工具类输出流写入异常。", PlatformExceptionEnum.JE_CORE_UTIL_FILE_WRITEIO_ERROR,new Object[]{file.getPath()},e);
//			e.printStackTrace();
//			throw new FileOperationException("输出流写入时发生错误。", e);
		}
	}

	/**
	 * 关闭数据源或者目标
	 * @param source
	 */
	protected void closeable(Closeable source) {
		if (source != null) {
			try {
				source.close();
			} catch (IOException e) {
				throw new PlatformException("文件工具类关闭流异常。", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,new Object[]{file.getPath()},e);
//				e.printStackTrace();
//				throw new PlatformException("关闭数据源或者目标时发生异常。", e);
			}
		}

	}

	/**
	 * @return 文件
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 根据指定目录，遍历制定深度下的，指定文件
	 * @param folderPath 目录结构
	 * @param depth	深度
	 * @param fileName	文件名
	 * @return List 文件名集合
	 */
	public static  List<String> getFiles(String folderPath, int depth ,String fileName){
		File folder = new File(folderPath);
		List<String> list = new ArrayList<String>();
		if(folder.isDirectory()){
			depth--;
			String[] str = folder.list();//得到文件目录下的所有文件信息
			for (int i = 0; i < str.length; i++){
				String f = str[i];//目录信息
				if(depth == 0){//到指定层次的目录
					if(fileName.equals(f)){//判断是否跟文件信息一样
						list.add(folder.getPath() + "\\" + f);
					}
				}else{
					if(!".svn".equals(f)){//不遍历svn下的文件
						List<String> temp = getFiles(folder.getPath() + "\\" + f , depth ,fileName);//继续便利
						for(String t : temp){list.add(t);}//把遍历的结果整合
					}
				}
			}
		}
		return list;
	}
	/**
	 * 解析xml信息，单层结构
	 * @param xmlFile
	 * @return
	 */
	public static JSONObject getXmlInfo(String xmlFile){
		JSONObject jb = new JSONObject();
		try{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new File(xmlFile));
			Element root = document.getRootElement();
			Iterator<?> iter = root.elementIterator();
			while (iter.hasNext()) {
				Node node = (Node) iter.next();
				jb.put(node.getName(), node.getStringValue());
			}
		}catch(Exception e){
			throw new PlatformException(" 文件工具类获取XML信息异常",PlatformExceptionEnum.JE_CORE_UTIL_FILE_UPDATEXML_ERROR,new Object[]{xmlFile},e);
		}
		return jb;
	}
	public static JSONObject updateXmlInfo(String xmlFile,Map<?,?> updateInfo){
		JSONObject jb = new JSONObject();
		FileOutputStream outStream=null;
		SAXReader saxReader=null;
		XMLWriter writer=null;
		try{
			saxReader = new SAXReader();
			Document document = saxReader.read(new File(xmlFile));
			Element root = document.getRootElement();
			Iterator<?> iter = root.elementIterator();
			while (iter.hasNext()) {
				Node node = (Node) iter.next();
				for(Iterator<?> i = updateInfo.keySet().iterator();i.hasNext();){
					String key = i.next().toString();
					if(node.getName().equals(key)){
						node.setText(updateInfo.get(key).toString());
					}

				}
			}

			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			outStream = new FileOutputStream(xmlFile);
			writer=new XMLWriter(new OutputStreamWriter(outStream,"utf-8"),format);
			writer.write(document);
			writer.flush();
		}catch(Exception e){
			throw new PlatformException(" 文件工具类修改XML信息异常",PlatformExceptionEnum.JE_CORE_UTIL_FILE_UPDATEXML_ERROR,new Object[]{xmlFile,updateInfo},e);
		}finally {
			if(outStream!=null){
				try {
					outStream.close();
				} catch (IOException e) {
					throw new PlatformException("文件工具类关闭流异常。", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,new Object[]{xmlFile,updateInfo},e);
				}
			}
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					throw new PlatformException("文件工具类关闭流异常。", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,new Object[]{xmlFile,updateInfo},e);
				}
			}
		}
		return jb;
	}

}
