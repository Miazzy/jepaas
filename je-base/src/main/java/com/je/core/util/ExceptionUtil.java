package com.je.core.util;

import com.je.core.facade.extjs.JsonBuilder;
import com.je.rbac.model.EndUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常工具类
 */
public class ExceptionUtil {
    /**
     * 构建请求参数信息
     * @return
     */
    public static Map<String,Object> buildRequestParam(HttpServletRequest request){
        Map<String, Object> params = new HashMap<>();
        Enumeration<String> enumerations = request.getParameterNames();
        while (enumerations.hasMoreElements()) {
            String key = enumerations.nextElement();
            params.put(key, request.getParameter(key));
        }
        EndUser currentUser=SecurityUserHolder.getCurrentUser();
        params.put("操作用户：",currentUser.getUsername());
        params.put("操作用户ID：",currentUser.getUserId());
        params.put("操作数据源：",currentUser.getZhDs());
        params.put("TOKENID：",SecurityUserHolder.getToken());
        return params;
    }

    public static String buildRequestParamToJson(HttpServletRequest request){
        Map<String, Object> params = new HashMap<>();
        Enumeration<String> enumerations = request.getParameterNames();

        while (enumerations.hasMoreElements()) {
            String key = enumerations.nextElement();
            params.put(key, request.getParameter(key));
        }

        EndUser currentUser=SecurityUserHolder.getCurrentUser();
        params.put("操作用户：",currentUser.getUsername());
        params.put("操作用户ID：",currentUser.getUserId());
        params.put("操作数据源：",currentUser.getZhDs());
        params.put("TOKENID：",SecurityUserHolder.getToken());

        return JsonBuilder.getInstance().toJson(params);
    }
}
