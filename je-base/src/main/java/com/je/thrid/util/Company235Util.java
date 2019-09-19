package com.je.thrid.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.je.core.util.HttpUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;

import java.util.HashMap;
import java.util.Map;

public class Company235Util {
    private static String APP_ID;
    private static String APP_KEY;
    private static String API_URL;
    private static String KEY_TYPE;
    private static String SEARCH_API_URL;
    public static void init(){
        if(StringUtil.isEmpty(APP_ID)){
            APP_ID = WebUtils.getSysVar("COMPANY_253_APPID");
            APP_KEY = WebUtils.getSysVar("COMPANY_253_APPKEY");
            API_URL = WebUtils.getSysVar("COMPANY_253_APIURL");
            KEY_TYPE = WebUtils.getSysVar("COMPANY_253_KEYTYPE");
            SEARCH_API_URL = WebUtils.getSysVar("COMPANY_253_SEARCHAPIURL");
        }
    }

    /**
     * 执行关键字
     * @param name
     * @return
     */
    public static JSONObject invokebusinessLicense(String name) {
        init();
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", APP_ID);
        params.put("appKey", APP_KEY);
        params.put("companyKey", name); //搜索关键字（公司全名、公司id）
        params.put("keyType", KEY_TYPE); //1-公司名、2-公司key
        String result = HttpUtils.post(API_URL, params);
        // 解析json,并返回结果
        return JSON.parseObject(result);
    }

    /**
     * 执行查询企业信息
     * @param name
     * @return
     */
    public static JSONObject invokeSearch(String name) {
        init();
        Map<String, String> params = Maps.newHashMap();
        params.put("appId", APP_ID);
        params.put("appKey", APP_KEY);
        params.put("keyWord", name); //搜索关键字
        params.put("exactlyMatch", "true"); //是否精准匹配，默认否，非必传
        params.put("sourceKey", "true"); //是否返回命中字段内容，默认否，非必传
        params.put("searchType", "1"); //搜索范围，非必传， 1默认（企业名称）、2企业名称、3法人股东、4机构代码、5经营范围、6地址、7电话、8邮箱、9域名网址、10专利、11商标品牌、12著作权、13法人、14股东、15主要成员、16高管团队
        params.put("pageSize", "20");//每页条数，默认为10,最大不超过20条,非必传
        params.put("pageIndex", "0");//页码（从0开始）,非必传
        String result = HttpUtils.post(SEARCH_API_URL, params);
        // 解析json,并返回结果
        return JSON.parseObject(result);
    }
}
