package com.je.message.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.je.core.util.WebUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO未处理
 */
public class PushMsgHttpUtil {
    private static Logger log = LoggerFactory.getLogger(PushMsgHttpUtil.class);
    private static final int TIME_OUT = 30000;
    public static final String IM_USER_NAME = "jeplus";
    public static final String IM_USER_PASSWORD = "123456";


    public static boolean pushMobileMsg(Map<String, String> paramMap) throws Exception {
		String resultStr =  post(WebUtils.getSysVar("JE_CORE_BACKWEBSOCKETURL")+"/instant/phonePush/pushMsg",paramMap);
		JSONObject jsonObject = JSON.parseObject(resultStr);
		if (jsonObject.getBoolean("success")){
			return true;
		}else {
			throw new Exception("出现异常:"+jsonObject.getString("message"));
		}
	}

    /**
     *  TODO未处理
     * @param groupId 分组ID
     * @param userIds 用户ID
     * @return
     * @throws Exception
     */
    public static boolean createGroup(String groupId,String userIds) throws Exception{
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("userName",IM_USER_NAME);
        paramMap.put("password",IM_USER_PASSWORD);
        paramMap.put("groupId",groupId);
        paramMap.put("userIds",userIds);
        String resultStr =  post(WebUtils.getSysVar("JE_CORE_BACKWEBSOCKETURL")+"/instant/userGroup/createGroup",paramMap);
        JSONObject jsonObject = JSON.parseObject(resultStr);
        if (jsonObject.getBoolean("success")){
            return true;
        }else {
            throw new Exception("出现异常:"+jsonObject.getString("message"));
        }
    }

    /**
     * TODO未处理
     * @param groupId 分组ID
     * @return
     * @throws Exception
     */
    public static boolean delGroup(String groupId) throws Exception{
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("userName",IM_USER_NAME);
        paramMap.put("password",IM_USER_PASSWORD);
        paramMap.put("groupId",groupId);
        String resultStr =  post(WebUtils.getSysVar("JE_CORE_BACKWEBSOCKETURL")+"/instant/userGroup/delGroup",paramMap);
        JSONObject jsonObject = JSON.parseObject(resultStr);
        if (jsonObject.getBoolean("success")){
            return true;
        }else {
            throw new Exception("出现异常:"+jsonObject.getString("message"));
        }
    }

    /**
     * TODO未处理
     * @param groupId 分组ID
     * @param userIds 用户ID
     * @return
     * @throws Exception
     */
    public static boolean addGroupDetail(String groupId,String userIds) throws Exception{
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("userName",IM_USER_NAME);
        paramMap.put("password",IM_USER_PASSWORD);
        paramMap.put("groupId",groupId);
        paramMap.put("userIds",userIds);
        String resultStr =  post(WebUtils.getSysVar("JE_CORE_BACKWEBSOCKETURL")+"/instant/userGroup/addGroupDetail",paramMap);
        JSONObject jsonObject = JSON.parseObject(resultStr);
        if (jsonObject.getBoolean("success")){
            return true;
        }else {
            throw new Exception("出现异常:"+jsonObject.getString("message"));
        }
    }

    /**
     * 同步IM用户信息
     * @param allUser
     * @return
     * @throws Exception
     */
    public static String  doSyncUser(String allUser) throws Exception{
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("userName",IM_USER_NAME);
        paramMap.put("password",IM_USER_PASSWORD);
        paramMap.put("allUser",allUser);
        String resultStr =  post(WebUtils.getSysVar("JE_CORE_BACKWEBSOCKETURL")+"/instant/rbac/syncUser",paramMap);
        JSONObject jsonObject = JSON.parseObject(resultStr);
        if (jsonObject.getBoolean("success")){
            return resultStr;
        }else {
            throw new Exception("出现异常:"+jsonObject.getString("message"));
        }
    }
    public static boolean delGroupDetail(String groupId,String userIds) throws Exception{
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("userName",IM_USER_NAME);
        paramMap.put("password",IM_USER_PASSWORD);
        paramMap.put("groupId",groupId);
        paramMap.put("userIds",userIds);
        String resultStr = post(WebUtils.getSysVar("JE_CORE_BACKWEBSOCKETURL")+"/instant/userGroup/delGroupDetail",paramMap);
        JSONObject jsonObject = JSON.parseObject(resultStr);
        if (jsonObject.getBoolean("success")){
            return true;
        }else {
            throw new Exception("出现异常:"+jsonObject.getString("message"));
        }
    }

    /**
     * @note targetIds 推送的人Id
     * @note text 推送消息内容
     * @note userId  请求人ID
     * @note must=0/1 是否一致性
     *
     * @author [qiaoshipeng]
     * @date 2019/01/04 20:01
     */
    public static String postToImServer(String url, Map<String, String> paramMap) throws Exception{
        return post(WebUtils.getSysVar("JE_CORE_BACKWEBSOCKETURL")+url,paramMap);
    }







    /*
     * @note post 请求
     * @author [qiaoshipeng]
     * @date 2018/11/06 16:54
     */
    private static String post(String url, Map<String, String> paramMap) throws Exception {
        CloseableHttpClient httpClient = null;
        long start = new Date().getTime();
        try {
            httpClient = HttpClients.createDefault();
            // 配置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).setRedirectsEnabled(true).build();
            // 拼接url
            HttpPost httpPost = new HttpPost(url);
            // 设置超时时间
            httpPost.setConfig(requestConfig);
            // 设置一些头信息可以为以后的加密准备
            // 装配post请求参数
            List<BasicNameValuePair> list = Lists.newArrayList();
            if (null != paramMap && !paramMap.isEmpty()) {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    Object value = entry.getValue();
                    final StringBuilder sb = new StringBuilder();
                    sb.append((String) value);
                    list.add(new BasicNameValuePair(entry.getKey(), sb.toString())); // 请求参数
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse != null) {
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
//                    log.info("[" + url + "]请求时间为:" + (new Date().getTime() - start) + "参数为:"+ JSON.toJSONString(list) +"结果:" + strResult);
                    return strResult;
                } else {
                    log.error("[" + url + "]" + "请求失败," + httpResponse.getStatusLine().toString());
                    throw new Exception("请求失败," + httpResponse.getStatusLine().toString());

                }
            } else {
                log.error("[" + url + "]" + "请求失败,返回的数据为空!");
                throw new Exception("请求失败,返回的数据为空!");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("[" + url + "]" + "请求失败" + e.getMessage());
           // throw e;
        } catch (ClientProtocolException e) {
            log.error("[" + url + "]" + "请求失败" + e.getMessage());
            //throw e;
        } catch (IOException e) {
            log.error("[" + url + "]" + "请求失败" + e.getMessage());
            //throw e;
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
               // e.printStackTrace();
            }
        }
        return "";
    }
	/**
	 * 获取用户未读数量
	 * @param zhId 租户ID
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getAllNoReadCount(String zhId, String userId) throws Exception {
		Map<String, String> paramMap = Maps.newHashMap();
		paramMap.put("userName",IM_USER_NAME);
		paramMap.put("password",IM_USER_PASSWORD);
		paramMap.put("userId",userId);
		String resultStr = post(WebUtils.getSysVar("JE_CORE_BACKWEBSOCKETURL")+"/instant/news/getAllNoReadCount",paramMap);
		JSONObject jsonObject = JSON.parseObject(resultStr);
		if (jsonObject.getBoolean("success")){
			return jsonObject;
		}else {
			return null;
		}
	}
}
