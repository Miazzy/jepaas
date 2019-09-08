package com.je.core.util;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientUtil {

    public static String HttpDoGet(String url) throws IOException {
        CloseableHttpClient httpCilent = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
//        httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
//        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
//        httpGet.setHeader("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU0MTEzMzk0OSwiaWF0IjoxNTQxMTI2NzQ5fQ.i3pVtpknjDrzyAXiPgfFBQIsSEsXwB1TBHPyfTFI3VM");
//        httpGet.setHeader("noSession","1");
        CloseableHttpResponse response = httpCilent.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String get = EntityUtils.toString(entity);
        return get;
    }
    public static String HttpPostWithJson(String url, String json) {
        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法
        }catch(Exception e){
            throw new PlatformException("HTTP请求工具类发送请求异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_SEND_ERROR,new Object[]{url,json},e);
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                throw new PlatformException("HTTP请求工具类关闭请求链接异常", PlatformExceptionEnum.JE_CORE_UTIL_HTTP_SEND_ERROR,new Object[]{url,json},e);
            }
        }
        //第五步：处理返回值
        return returnValue;
    }
}
