package com.je.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtils {
	/**
	 * 加载xml文档文件
	 * @param filePath
	 * @return
	 */
	public static Document loadXMLFile(String filePath){
         SAXReader saxReader = new SAXReader();
         try {
			return saxReader.read(new File(filePath));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
     }	
	 public static boolean saveFormatXMLDocumentToFile(Document doc, String encoding, String filePath)  {
      boolean flag = false;
      OutputFormat outputFormat = OutputFormat.createPrettyPrint();
       outputFormat.setEncoding(encoding);
       FileOutputStream fos = null;
	        XMLWriter xmlWriter  = null;
	          try{
	              fos = new FileOutputStream(filePath);// 可解决UTF-8编码问题
	              xmlWriter = new XMLWriter(fos, outputFormat);
	              xmlWriter.write(doc);
	              flag = true;
				  xmlWriter.flush();
				  fos.flush();
	          }catch(IOException e){
	             throw new PlatformException("XML工具类读取文件信息异常", PlatformExceptionEnum.JE_CORE_UTIL_XML_READ_ERROR,new Object[]{filePath},e);
	          }finally{
	              try {
					  if (xmlWriter != null) {
						  xmlWriter.close();
					  }
					  if (fos != null) {
						  fos.close();
					  }
				  } catch (IOException e) {
	                  throw new PlatformException("XML工具类关闭文件流异常",PlatformExceptionEnum.JE_CORE_UTIL_XML_CLOSEIO_ERROR,new Object[]{},e);
	              }
	              
	          }
	          return flag;
	      }
}
