package com.je.thrid.util;

import com.je.core.util.HttpUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 阿里云高德地图IP识别
 */
public class AliIpAddressUtil {

    public static String host;
    public static String path;
    public static String appkey;
    public static String appsecret;
    public static String appcode;

    private static Logger logger = LoggerFactory.getLogger(AliIpAddressUtil.class);
//    static {
//        try {
//            Properties sys = PropertiesLoaderUtils.loadAllProperties("sysconfig.properties");
//            host = sys.getProperty("amap.ip.host");
//            path = sys.getProperty("amap.ip.path");
//            appkey = sys.getProperty("amap.ip.appkey");
//            appsecret = sys.getProperty("amap.ip.appsecret");
//            appcode = sys.getProperty("amap.ip.appcode");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static void init(){
        if(StringUtil.isEmpty(host)){
            host = WebUtils.getSysVar("ALIYUN_IP_HOST");
            path = WebUtils.getSysVar("ALIYUN_IP_PATH");
            appkey = WebUtils.getSysVar("ALIYUN_IP_APPKEY");
            appsecret = WebUtils.getSysVar("ALIYUN_IP_APPSECRET");
            appcode = WebUtils.getSysVar("ALIYUN_IP_APPCODE");
        }
    }
    public static HttpResponse getIpAddress(String ip) {
        init();
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap();
        querys.put("ip", ip);

        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(host, path, method, headers, querys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
