
package com.je.core.util;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import com.je.core.util.FileOperate;

/**
 * ZIP工具包
 * 依赖：ant-1.7.1.jar
 */
public class ZipUtils {

	/**
	 * 使用GBK编码可以避免压缩中文文件名乱码
	 */
	private static final String CHINESE_CHARSET = "GBK";

	/**
	 * 文件读取缓冲区大小
	 */
	private static final int CACHE_SIZE = 1024 * 64;

	/**
	 * 压缩文件
	 * @param sourceFolder 压缩文件夹
	 * @param zipFilePath 压缩文件输出路径
	 * @param root 是否要根目录
	 * @throws Exception
	 */
	public static void zip(String sourceFolder, String zipFilePath ,String[] zipFiles,boolean root){
		OutputStream out=null;
		BufferedOutputStream bos=null;
		ZipOutputStream zos=null;
		try{
			File file = new File(sourceFolder);//压缩文件目录
			String basePath = file.getParent();
			if(root == false){
				basePath = file.getPath();
			}
			File zip = new File(zipFilePath);//输出文件路径
			File dir = zip.getParentFile();
			if (!dir.exists()) dir.mkdirs();

			out = new FileOutputStream(zip);
			bos = new BufferedOutputStream(out);
			zos = new ZipOutputStream(bos);

			// 解决中文文件名乱码
			zos.setEncoding(CHINESE_CHARSET);
			//压缩
			zipFile(file, basePath, zos ,zipFiles);
			//关闭流
			zos.closeEntry();
//			zos.close();
//			bos.close();
//			out.close();
		}catch(Exception e){
			throw new PlatformException("ZIP压缩工具类压缩文件异常", PlatformExceptionEnum.JE_CORE_UTIL_ZIP_DOZIP_ERROR,new Object[]{sourceFolder,zipFilePath ,zipFiles,root},e);
		}finally {
			FileOperate.close(null,zos,null,null);
			FileOperate.close(null,bos,null,null);
			FileOperate.close(null,out,null,null);
		}
	}
	/**
	 * 递归压缩文件
	 * @param baseFile	基础文件
	 * @param basePath	基础文件的上级目录
	 * @param zos
	 * @param zipFiles	压缩的文件，null压缩baseFile下的所有文件
	 * @throws Exception
	 */
	private static void zipFile(File baseFile, String basePath, ZipOutputStream zos,String[] zipFiles) throws Exception {
		String 	pathName = baseFile.getPath().equals(basePath) ?
				"" :
				baseFile.getPath().substring(basePath.length() + 1);//压缩包内的文件名
		if(zipFiles != null){//过滤打包
			boolean flag = false;
			for(String tf : zipFiles){
				if(!tf.equals(tf.replace(pathName, ""))){
					flag = true;break;//存在文件
				}
			}
			if(!flag)return;
		}
		if (baseFile.isDirectory()) {
			pathName +=  "/";
			if(!"/".equals(pathName)){
				zos.putNextEntry(new ZipEntry(pathName));
			}
			for(File f : baseFile.listFiles()){
				zipFile(f, basePath, zos, zipFiles);
			}
		} else {
			byte[] cache = new byte[CACHE_SIZE];
			InputStream is = new FileInputStream(baseFile);
			BufferedInputStream bis = new BufferedInputStream(is);
			zos.putNextEntry(new ZipEntry(pathName));
			int nRead = 0;
			while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
				zos.write(cache, 0, nRead);
			}
			bis.close();
			is.close();
		}
	}
	/**
	 * 将指定的文件压缩
	 * @param addresses 指定的文件地址
	 * @param reportPath 目标文件路径
	 * @param fileNames 指定文件名
	 * @throws IOException
	 */
	public static void zipFiles4Io(List<String> addresses,List<String> fileNames,String reportPath) throws IOException{
		File zipFile = new File(reportPath);
		if(!zipFile.getParentFile().exists()){
			zipFile.getParentFile().mkdirs();
		}
		ZipOutputStream out=new ZipOutputStream(zipFile);
		byte[] buf = new byte[1024];
		for(int i=0;i<addresses.size();i++){
			FileInputStream in = new FileInputStream(addresses.get(i));
			out.putNextEntry(new ZipEntry(fileNames.get(i)));
			out.setEncoding("GBK");
			int len;
			while((len=in.read(buf))>0){
				out.write(buf,0,len);
			}
			out.closeEntry();
			in.close();
		}
		out.close();
	}

	/**
	 * @param inputStreams
	 * @param fileNames
	 * @param reportPath
	 * @throws IOException
	 */
	public static void zipFiles4IoInput(List<InputStream> inputStreams,List<String> fileNames,String reportPath) {
		File zipFile = new File(reportPath);
		if(!zipFile.getParentFile().exists()){
			zipFile.getParentFile().mkdirs();
		}
		ZipOutputStream out= null;

		try {
			out = new ZipOutputStream(zipFile);
			for (int i =0;i<inputStreams.size();i++){
				InputStream inputStream = inputStreams.get(i);
				ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
				byte[] buff = new byte[1024];
				int rc = 0;
				while ((rc = inputStream.read(buff, 0, 1024)) > 0) {
					swapStream.write(buff, 0, rc);
				}
				byte[] filesBytes = swapStream.toByteArray();
				out.putNextEntry(new ZipEntry(fileNames.get(i)));
				out.setEncoding("GBK");
				out.write(filesBytes,0,filesBytes.length);
				out.closeEntry();
				inputStream.close();
				swapStream.close();
			}
		} catch (IOException e) {
			throw new PlatformException("压缩文件流异常",PlatformExceptionEnum.JE_CORE_UTIL_ZIP_DOZIP_ERROR,new Object[]{fileNames,reportPath},e);
		}finally {
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					throw new PlatformException("压缩文件流关闭输出流异常",PlatformExceptionEnum.JE_CORE_UTIL_ZIP_DOZIP_ERROR,new Object[]{fileNames,reportPath},e);
				}
			}
		}
	}
	/**
	 *将指定的字节码进行文件压缩
	 * @param filesBytes
	 * @param fileNames
	 * @param reportPath
	 * @throws IOException
	 */
	public static void zipBytes4Io(List<byte[]> filesBytes,List<String> fileNames,String reportPath) throws IOException {
		File zipFile = new File(reportPath);
		if(!zipFile.getParentFile().exists()){
			zipFile.getParentFile().mkdirs();
		}
		ZipOutputStream out=new ZipOutputStream(zipFile);
		for(int i=0;i<filesBytes.size();i++){
			out.putNextEntry(new ZipEntry(fileNames.get(i)));
			out.setEncoding("GBK");
			out.write(filesBytes.get(i),0,filesBytes.get(i).length);
			out.closeEntry();
		}
		out.close();
	}

	/**
	 * 解压压缩包
	 * @param zipFilePath 压缩文件路径
	 * @param destDir 压缩包释放目录
	 * @throws Exception
	 */
	public static void unZip(String zipFilePath, String destDir){
		BufferedInputStream bis=null;
		FileOutputStream fos=null;
		BufferedOutputStream bos=null;
		try {
			ZipFile zipFile = new ZipFile(zipFilePath, CHINESE_CHARSET);
			Enumeration<?> emu = zipFile.getEntries();
			File file, parentFile;
			ZipEntry entry;
			byte[] cache = new byte[CACHE_SIZE];
			while (emu.hasMoreElements()) {
				entry = (ZipEntry) emu.nextElement();
				if (entry.isDirectory()) {
					new File(destDir + entry.getName()).mkdirs();
					continue;
				}
				bis = new BufferedInputStream(zipFile.getInputStream(entry));
				file = new File(destDir + entry.getName());
				parentFile = file.getParentFile();
				if (parentFile != null && (!parentFile.exists())) {
					parentFile.mkdirs();
				}
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, CACHE_SIZE);
				int nRead = 0;
				while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
					fos.write(cache, 0, nRead);
				}
				bos.flush();
			}
			zipFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new PlatformException("ZIP压缩工具类解压文件异常", PlatformExceptionEnum.JE_CORE_UTIL_ZIP_UNZIP_ERROR,new Object[]{zipFilePath ,destDir},e);
		}finally {
			FileOperate.close(bis,bos,null,null);
			FileOperate.close(null,fos,null,null);
		}
	}
	static void jarFile(JarOutputStream out, File dir, String root,String[] zipFiles) throws Exception {
		String entryName = dir.getAbsolutePath().replace(root, "").replace('\\', '/');
		entryName = entryName.replaceFirst("^/", "");
		JarEntry entry = null;
		if(!"".equals(entryName)){
			entry = new JarEntry(entryName+"/");
			out.putNextEntry(entry);
			out.closeEntry();
		}
		File[] files = dir.listFiles();
		FileInputStream in = null;
		byte[] buff = new byte[1024*64];
		int length = 0;
		for (File file : files) {

			String tempName = file.getPath().replace(root, "");
			boolean flag = false;
			for(String tf : zipFiles){
				if(!tf.equals(tf.replace(tempName, ""))){
					flag = true;break;
				}
			}
			if(!flag)continue;
//        	if(".svn".equals(file))continue;
			if (file.isDirectory()) {
				jarFile(out, file, root, zipFiles);
			} else if (!"MANIFEST.MF".endsWith(file.getName())) {
				String eName = entryName + "/" + file.getName();
				eName = eName.replaceFirst("^/", "");
				entry = new JarEntry(eName);
				in = new FileInputStream(file);
				out.putNextEntry(entry);
				while ((length=in.read(buff)) > -1) {
					out.write(buff, 0, length);
					out.flush();
				}
				out.closeEntry();
				in.close();
			}
		}
	}
	/**
	 *
	 * @param resDirPath calss文件夹下的路径
	 * @param jarPath	jar文件的路径
	 * @throws Exception
	 */
	public static void jar(String resDirPath, String jarPath,String[] zipFiles) throws Exception {
		//项目的class路径
		String calsspath = Class.class.getClass().getResource("/").getPath();

		File resDir = new File(calsspath + resDirPath);
		File jar = new File(jarPath);

		File dir = jar.getParentFile();
		if (!dir.exists()) dir.mkdirs();
		File cf = new File(calsspath);
		//项目的webroot路径
		File webRoot = cf.getParentFile().getParentFile();

		OutputStream fileOutputStream = new FileOutputStream(jar);
		BufferedOutputStream cs = new BufferedOutputStream(fileOutputStream);

		Manifest man = new Manifest();
		FileInputStream in = new FileInputStream(new File(webRoot.getAbsolutePath(), JarFile.MANIFEST_NAME));
		man.read(in);
		in.close();

		JarOutputStream out = new JarOutputStream(cs, man);
		jarFile(out, resDir, resDir.getParentFile().getAbsolutePath(), zipFiles);
		out.close();
	}

	/**
	 * 复制制定文件夹下的文件
	 * @param basePathF 源文件夹
	 * @param basePathT 目标文件夹
	 * @param files 复制的文件
	 */
	public static void copyFile(String basePathF ,String basePathT, String[] files){
		File bf = new File(basePathT);
		File bt = new File(basePathF);
		if(!bf.exists()){
			bf.mkdir();
		}
		for(String file : files){
			String dir = file.substring(0,file.lastIndexOf("\\"));
			File tf = new File(bf.getPath()+ "\\" + dir);
			if(!tf.exists()){
				tf.mkdirs();
			}

			File ff = new File(bt.getPath()+"\\"+file);
			if(ff.exists()){
				FileOperate.copyFile(ff.getPath(),bf.getPath()+"\\"+file);
			}
		}

	}
//	public static void main(String[] args) throws Exception{
////    	List<String> addresses=new ArrayList<String>();
////    	addresses.add("F:/workspace/jelephant/WebRoot/JE/data/upload/201402/225f47d9-1d3d-4699-9fbc-f81cb2f0c653.txt");
////    	List<String> fileNames=new ArrayList<String>();
////    	fileNames.add("JE功能.txt");
////    	ZipUtils.zipFiles4Io(addresses, fileNames, "F:/workspace/jelephant/WebRoot/JE/data/upload/tem/download.zip");
////    	ZipUtils.zip("F:/workspace/jelephant/WebRoot/JE/resource/phone/app/JE_CORE_DICTIONARY", "F:/workspace/jelephant/WebRoot/JE/data/upload/phone/app/JE_CORE_DICTIONARY/1.1.zip", null, true);
////    	ZipUtils.unZip("d:/aa.zip", "D:/aa");
//		ZipUtils.zip("D:/zip", "D:/a.zip", null, false);
//	}
}
