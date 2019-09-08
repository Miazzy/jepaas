package com.je.thrid.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.je.core.util.OCRHttpUtils;
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

public class AliOCRUtil {
    public static String host;
    public static String path;
    public static String appkey;
    public static String appsecret;
    public static String appcode;
    private static Logger logger = LoggerFactory.getLogger(AliOCRUtil.class);
//    static {
//        try {
//            Properties sys = PropertiesLoaderUtils.loadAllProperties("sysconfig.properties");
//            host = sys.getProperty("alibaba.orc.host");
//            path = sys.getProperty("alibaba.orc.path");
//            appkey = sys.getProperty("alibaba.orc.appkey");
//            appsecret = sys.getProperty("alibaba.orc.appsecret");
//            appcode = sys.getProperty("alibaba.orc.appcode");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static void init(){
        if(StringUtil.isEmpty(host)){
            host = WebUtils.getSysVar("ALIYUN_OSS_HOST");
            path = WebUtils.getSysVar("ALIYUN_OCR_PATH");
            appkey = WebUtils.getSysVar("ALIYUN_OCR_APPKEY");
            appsecret = WebUtils.getSysVar("ALIYUN_OCR_APPSECRET");
            appcode = WebUtils.getSysVar("ALIYUN_OCR_APPCODE");
        }
    }
    public static JSONObject autherFileTime(String imgBase64) throws Exception {
        init();
        JSONObject configObj = new JSONObject();
        configObj.put("side", "face");
        String config_str = configObj.toString();

        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359d73e9498385570ec139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
//        byte[] bytes = new byte[inputStream.available()];
//        inputStream.read(bytes);
//        inputStream.close();
//        BASE64Encoder encoder = new BASE64Encoder();
//        String imgBase64 = encoder.encode(bytes);

        JSONObject requestObj = new JSONObject();
        requestObj.put("image", imgBase64);
        if(config_str.length() > 0) {
            requestObj.put("configure", config_str);
        }

        String bodys = requestObj.toString();
        HttpResponse response = OCRHttpUtils.doPost(host, path, method, headers, querys, bodys);
        String res = EntityUtils.toString(response.getEntity());
        JSONObject res_obj = JSON.parseObject(res);
        return res_obj;
    }

}
