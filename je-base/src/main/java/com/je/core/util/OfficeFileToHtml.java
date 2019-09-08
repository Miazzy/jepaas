package com.je.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
/**
 * Office文档文档转成html文件工具类
 * @author zhangshuaipeng
 *
 */
public class OfficeFileToHtml {
	public static int WORD_HTML = 8;
	public static int WORD_TXT = 7;
	public static int EXCEL_HTML = 44;
	public static int PPT_HTML = 12;
	/**
	 * WORD转HTML
	 * @param docfile WORD文件全路径
	 * @param htmlfile 转换后HTML存放路径
	 */
	public static void wordToHtml(String docfile, String htmlfile){
		ActiveXComponent app = new ActiveXComponent("Word.Application"); // 启动word
		try
		{
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			Dispatch doc = Dispatch.invoke(docs,"Open",Dispatch.Method,new Object[] { docfile, new Variant(false),new Variant(true) }, new int[1]).toDispatch();
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {htmlfile, new Variant(WORD_HTML) }, new int[1]);
			Variant f = new Variant(false);
			Dispatch.call(doc, "Close", f);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			app.invoke("Quit", new Variant[] {});
		}
	}
	/**
	 * EXCEL转HTML
	 * @param xlsfile EXCEL文件全路径
	 * @param htmlfile 转换后HTML存放路径
	 */
	public static void excelToHtml(String xlsfile, String htmlfile){
	ActiveXComponent app = new ActiveXComponent("Excel.Application"); // 启动word
	try {
		app.setProperty("Visible", new Variant(false));
		Dispatch excels = app.getProperty("Workbooks").toDispatch();
		Dispatch excel = Dispatch.invoke(
				excels,
				"Open",
				Dispatch.Method,
				new Object[] { xlsfile, new Variant(false),
						new Variant(true) }, new int[1]).toDispatch();
		Dispatch.invoke(excel, "SaveAs", Dispatch.Method, new Object[] {
				htmlfile, new Variant(44) }, new int[1]);
		Variant f = new Variant(false);
		Dispatch.call(excel, "Close", f);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		app.invoke("Quit", new Variant[] {});
	}
	}
	/**
	 * 将PPT转Html
	 * @param pptPath
	 * @param htmlPath
	 */
	public static void pptToHtml(String pptPath, String htmlPath) {
		ActiveXComponent offCom = new ActiveXComponent("PowerPoint.Application");
		try {
			offCom.setProperty("Visible", new Variant(true));
			Dispatch excels = offCom.getProperty("Presentations").toDispatch();
			Dispatch excel = Dispatch.invoke(excels, "Open", Dispatch.Method,
					new Object[] { pptPath, new Variant(false),

					new Variant(false) }, new int[1]).toDispatch();
			Dispatch.invoke(excel, "SaveAs", Dispatch.Method, new Object[] {
					htmlPath, new Variant(12) }, new int[1]);
			Dispatch.call(excel, "Close");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			offCom.invoke("Quit", new Variant[] {});
		}
	} 
	/**
	 * 将PDF转成图片
	 * @param pdfPath
	 * @param imgDir
	 * @return 返回pdf总共多少页   返回-1代表出错
	 */
	public static Integer pdfToImg(String pdfPath,String imgDir,String fileName){
		PDDocument document;
		try {
			document = PDDocument.load(pdfPath);
			File dirFile=new File(imgDir);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			PDFImageWriter imageWriter = new PDFImageWriter();
			imageWriter.writeImage(document, "png", "", 1, document.getPageCount(),imgDir+"/"+fileName , 1, 96);
			return document.getPageCount();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;	
	}
	/**
	 * 修改转换后的HTML文件的编码方式
	 * @param filePath
	 * @param codeType
	 */
	public static void changeFileCharset(String filePath, String codeType) {
		
		File file = new File(filePath);
		
		if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".htm") || 
				file.getName().substring(file.getName().lastIndexOf(".")).equals(".html")) {
			try {
				int index = file.getAbsolutePath().lastIndexOf('\\') + 1;
				String fileName = file.getName();
				File file1 = new File(file.getAbsolutePath()
						.substring(0, index)
						+ fileName + "_bak");				
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file)));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file1)));
				String line = null;
				while ((line = br.readLine()) != null) {
					
					if (line.lastIndexOf("charset=") > 0){
						String oldCodeType = line.substring(line.lastIndexOf("charset=") + 8, line.length()-2);
						
						if (oldCodeType != null && !oldCodeType.equals(codeType)){
							line = line.substring(0, line.lastIndexOf("charset=")) + "charset=" + codeType + "\">";
						}
					}
					
					bw.write(line + "\n");
				}
				bw.flush();
				bw.close();
				br.close();
				file.delete();
				file1.renameTo(new File(file.getAbsolutePath().substring(0,
						index)
						+ fileName));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("没有可修改的文件");
		}
	}
	
}
