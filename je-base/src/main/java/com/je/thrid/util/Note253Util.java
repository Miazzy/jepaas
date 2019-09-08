package com.je.thrid.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Note253Util {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private  String username;
    private  String password;
    private  String sign;
    private  String report;
    private  String sendUrl;

//    static{
//        InputStream is = null;
//        try {
//            is = SortMesssageUtil.class.getResourceAsStream("/sortmessage.properties");
//            Properties properties = new Properties();
//            properties.load(is);
//            sendUrl = properties.getProperty("account.send.url");
//            username = properties.getProperty("username");
//            password = properties.getProperty("password");
//            sign = properties.getProperty("sign");
//            report = properties.getProperty("report");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {}
//            }
//        }
//
//    }
    private static Note253Util note253Util=null;

    private Note253Util(String username, String password, String sign, String report,String sendUrl) {
        this.username = username;
        this.password = password;
        this.sign = sign;
        this.report = report;
        this.sendUrl = sendUrl;
    }

    public static Note253Util getInstance(String username, String password, String sign, String report, String sendUrl){
        if(note253Util==null){
            note253Util=new Note253Util(username,password,sign,report,sendUrl);
        }
        return note253Util;
    }

    /**
     * 发送短信
     * @param phoneNumber
     * @param context
     * @return
     */
    public Integer sendNote(String phoneNumber,String context){
        //短信下发
        Map map = new HashMap();
        //API账号
        map.put("account",username);
        //API密码
        map.put("password",password);
        //短信内容
        map.put("msg",context);
        //手机号
        map.put("phone",phoneNumber);
        //是否需要状态报告
        map.put("report",report);
        String post = sendSmsByPost(sendUrl, JSONObject.toJSONString(map));
        return 1;
    }

    /**
     * 发送短信
     * @param path 路径
     * @param postContent 请求的内容
     * @return
     */
    private String sendSmsByPost(String path, String postContent) {
        URL url = null;
        OutputStream os = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();
            os=httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();
            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            }
        } catch (Exception e) {
            logger.info("短信发送失败");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) { }
            }
        }
        return "{}";
    }
//
//    private String getRandomDigit(){
//        String base = "0123456789";
//        Random random = new Random();
//        StringBuffer sb = new StringBuffer();
//        for (int i=0;i<6;i++){
//            int number = random.nextInt(base.length());
//            sb.append(base.charAt(number));
//        }
//        return sb.toString();
//    }
}
