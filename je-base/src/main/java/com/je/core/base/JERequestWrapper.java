package com.je.core.base;

import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: Request包装类
 *
 * @auther: wangmm@ketr.com.cn
 * @date: 2019/3/5 20:19
 */
public class JERequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> parameterMap;

    private HttpServletRequest originalRequest;

    public JERequestWrapper(HttpServletRequest request) {
        super(request);
        setOriginalRequest(request);
        parameterMap = new HashMap<>(request.getParameterMap());
    }

    public JERequestWrapper(HttpServletRequest request, HttpServletRequest originalRequest) {
        super(request);
        setOriginalRequest(originalRequest);
        parameterMap = new HashMap<>(request.getParameterMap());
    }

    public HttpServletRequest getOriginalRequest() {
        return originalRequest;
    }

    public void setOriginalRequest(HttpServletRequest originalRequest) {
        this.originalRequest = originalRequest;
    }

    /**
     * 描述: 重写获取参数方法
     *
     * @auther: wangmm@ketr.com.cn
     * @date: 2019/3/5 20:36
     */
    @Override
    public String getParameter(String name) {
        String[] values = parameterMap.get(name);
        if (null != values && values.length > 0) {
            if (values.length == 1) {
                return values[0];
            } else {
                return StringUtils.join(values, ",");
            }
        } else {
            return null;
        }
    }

    /**
     * 描述: 添加设置参数方法
     *
     * @auther: wangmm@ketr.com.cn
     * @date: 2019/3/5 20:36
     */
    public void setParameter(String key, String value) {
        String[] values = new String[]{value};
        parameterMap.put(key, values);
    }

    public void setParameter(String key, String[] values) {
        parameterMap.put(key, values);
    }

    public Map<String, String[]> getParameterMap() {
        return this.parameterMap;
    }
}
