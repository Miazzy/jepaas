package me.chanjar.dingding.common.util.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.HashMap;


public class DingDingTools {
	
	private static boolean isEmpty(String string) { return (string != null && !string.equals("null") && string.length() != 0) ? false : true; }
	
	public static boolean isBlank(String string) { return (!isEmpty(string) && string.trim().length() != 0) ? false : true; }
	
	/**
	 * 发送Get请求
	 * @param url
	 * @param charset
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String httpGet(String url, String charset) throws ClientProtocolException, IOException {
	    if (isBlank(charset)) charset = "UTF-8";
	    
	    HttpGet httpget = new HttpGet(url);
	    CloseableHttpClient httpclient = HttpClients.custom().build();
	    CloseableHttpResponse response = httpclient.execute(httpget);
	    try {
	      HttpEntity entity = response.getEntity();
	      if (entity != null) {
	        InputStream instream = entity.getContent();
	        String str = streamToString(instream, charset);
	        return str;
	      } 
	    } catch (Exception e) {
	      e.printStackTrace();
	      return "";
	    } finally {
	      response.close();
	    } 
	    return "";
	  }
	  
	  public static String streamToString(InputStream is, String charset) throws Exception {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
		    StringBuilder sb = new StringBuilder();
		    
		    String line = null;
		    try {
		      while ((line = reader.readLine()) != null) {
		        sb.append(String.valueOf(line) + "\n");
		      }
		    } catch (IOException e) {
		      e.printStackTrace();
		    } finally {
		      try {
		        is.close();
		      } catch (IOException e) {
		        e.printStackTrace();
		      } 
		    } 
		    return sb.toString();
		  }
	  
	  public static HashMap<String, String> jsonStr2Map(String jsonStr) {
		    JSONObject jsonobj = JSON.parseObject(jsonStr);
		    HashMap<String, String> map = new HashMap<String, String>(jsonobj.size());
		    for (String fdName : jsonobj.keySet()) {
		      map.put(fdName, jsonobj.getString(fdName));
		    }
		    return map;
		  }
	  
	  public static String httpPost(String url, String params) throws ClientProtocolException, IOException {
		    HttpPost httpPost = new HttpPost(url);
		    CloseableHttpClient httpclient = HttpClients.custom().build();
		    StringEntity myEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
		    httpPost.setEntity(myEntity);
		    CloseableHttpResponse response = httpclient.execute(httpPost);
		    try {
		      HttpEntity rsEntity = response.getEntity();
		      if (rsEntity != null) {
		        InputStream instream = rsEntity.getContent();
		        String str = streamToString(instream, "UTF-8");
		        return str;
		      } 
		    } catch (Exception e) {
		      e.printStackTrace();
		      return "";
		    } finally {
		      response.close();
		    } 
		    return "";
		  }

	public static long getAmrDuration(File file) throws IOException {
		long duration = -1;
		int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			long length = file.length();//文件的长度
			int pos = 6;//设置初始位置
			int frameCount = 0;//初始帧数
			int packedPos = -1;
			byte[] datas = new byte[1];//初始数据值
			while (pos <= length) {
				randomAccessFile.seek(pos);
				if (randomAccessFile.read(datas, 0, 1) != 1) {
					duration = length > 0 ? ((length - 6) / 650) : 0;
					break;
				}
				packedPos = (datas[0] >> 3) & 0x0F;
				pos += packedSize[packedPos] + 1;
				frameCount++;
			}
			duration += frameCount * 20;//帧数*20
		} finally {
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}
		}
		return duration/1000;
	}

	/**
	 * 将inputStream转化为file
	 * @param is
	 * @param file 要输出的文件目录
	 */
	public static void inputStream2File(InputStream is, File file) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int len = 0;
			byte[] buffer = new byte[8192];

			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
		} finally {
			os.close();
			is.close();
		}
	}

}
