package com.je.core.util;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * java端发送ajax工具类
 *
 * @author zhangshuaipeng
 */
public class JavaAjaxUtil {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + "?" + param;
            URL realUrl = new URL(urlName);
            //打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//	   conn.setRequestProperty("Cookie", "JSESSIONID=abcQ57dtPX5mqfnBE7Azu; loginUserCode=admin");
            //建立实际的连接
            conn.connect();
            //获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            //遍历所有的响应头字段
            for (String key : map.keySet()) {
            }
            //定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            throw new PlatformException("HTTP工具类GET请求异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_DOGET_ERROR,new Object[]{url,param},e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    throw new PlatformException("HTTP工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_CLOSEIO_ERROR,new Object[]{url,param},ex);
                }
            }
        }
        return result;
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, String param, HttpServletRequest request) {
        return sendPost(url, param, request, null);
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, String param, HttpServletRequest request, JSONObject config) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        boolean exception = true;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//			if(request!=null){
//				conn.setRequestProperty("Cookie", request.getHeader("Cookie"));
//			}
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if (config != null) {
                if (config.containsKey("timeout")) {
                    int timeout = config.getInt("timeout");
                    conn.setConnectTimeout(timeout);
                }
                if (config.containsKey("exception")) {
                    exception = config.getBoolean("exception");
                }
            }
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            if (exception) {
                throw new PlatformException("HTTP工具类POST请求异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_DOPOST_ERROR,new Object[]{url,param},e);
            }
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new PlatformException("HTTP工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_CLOSEIO_ERROR,new Object[]{url,param},e);
            }
        }
        return result;
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     * @throws IOException
     */
    public static String sendPostCatch(String url, String param, HttpServletRequest request) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//			if(request!=null){
//				conn.setRequestProperty("Cookie", request.getHeader("Cookie"));
//			}
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);

        // 获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        out.print(param);
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            result += "\n" + line;
        }
        // 使用finally块来关闭输出流、输入流
        if (out != null) {
            out.close();
        }
        if (in != null) {
            in.close();
        }
        return result;
    }

    /**
     * 向指定URL发送POST方法的请求  发送XML内容  研发部:云凤程
     *
     * @param url
     * @param xml
     * @return
     */
    public static String sendPost4Xml(String url, String xml) {
        String responseString = null;
        //创建httpclient工具对象
        HttpClient client = new HttpClient();
        //创建post请求方法
        PostMethod myPost = new PostMethod(url);
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        try {
            //设置请求头部类型
            myPost.setRequestHeader("Content-Type", "text/xml");
            myPost.setRequestHeader("charset", "utf-8");
            myPost.setRequestEntity(new StringRequestEntity(xml, "text/xml", "utf-8"));
            int statusCode = client.executeMethod(myPost);
            if (statusCode == HttpStatus.SC_OK) {
                bis = new BufferedInputStream(myPost.getResponseBodyAsStream());
                byte[] bytes = new byte[1024];
                bos = new ByteArrayOutputStream();
                int count = 0;
                while ((count = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, count);
                }
                byte[] strByte = bos.toByteArray();
                responseString = new String(strByte, 0, strByte.length, "utf-8");
                bos.close();
                bis.close();
            }
        } catch (Exception e) {
            throw new PlatformException("HTTP工具类XML请求异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_DOXML_ERROR,new Object[]{url,xml},e);
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException ex) {
                throw new PlatformException("HTTP工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_CLOSEIO_ERROR,new Object[]{url,xml},ex);
            }
        }
        return responseString;
    }

    public static String sendFile(String url, String param, String fileFieldName, String fileName, String filePath) {
        String result = "";
        OutputStream out = null;
        InputStream inputStream = null;
        try {
            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            if (StringUtil.isNotEmpty(param)) {
                url += ("?" + param);
            }
            // 服务器的域名
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            out = new DataOutputStream(conn.getOutputStream());
            // 上传文件
            File file = new File(filePath);
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"" + fileFieldName + "\";filename=\"" + fileName
                    + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);

            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            // 数据输入流,用于读取文件数据
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            out.write(newLine.getBytes());
            in.close();

            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
            inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();
        } catch (Exception e) {
            throw new PlatformException("HTTP工具类文件请求异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_DOFILE_ERROR,new Object[]{ url,param,fileFieldName,fileName,filePath},e);
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new PlatformException("HTTP工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_CLOSEIO_ERROR,new Object[]{ url,param,fileFieldName,fileName,filePath},e);
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new PlatformException("HTTP工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_CLOSEIO_ERROR,new Object[]{ url,param,fileFieldName,fileName,filePath},e);
                }
            }
        }
        return result;
    }

    /**
     * 格式化请求参数
     *
     * @param varSet
     * @return
     */
    public static String formatParams(Set<Entry<String, Object>> varSet) {
        StringBuffer paramStr = new StringBuffer();
        for (Entry<String, Object> param : varSet) {
            paramStr.append("&" + param.getKey() + "=" + StringUtil.getDefaultValue(param.getValue(), ""));
        }
        return paramStr.toString();
    }
}
