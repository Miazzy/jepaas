package com.je.core.base;

import com.je.core.util.StringUtil;
import com.je.core.util.bean.DynaBean;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MethodArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * TODO 暂不明确
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if(parameter.getParameterType().equals(MethodArgument.class)) {
            return true;
        }
        return false;
    }

    /**
     * TODO 暂不明确
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest
            , WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpServletResponse response = (HttpServletResponse) webRequest.getNativeResponse();
        MethodArgument param = new MethodArgument();
        param.setRequest(request);
        param.setResponse(response);

        //绑定请求参数
        org.apache.commons.beanutils.BeanUtils.populate(param,request.getParameterMap());

        //得到表的Code
        String tableCode = request.getParameter("tableCode");
        //赋值查询条件和排序条件
        String whereSql = request.getParameter("whereSql");
        if (StringUtil.isEmpty(whereSql)) {
            whereSql = "";
        }
        String orderSql = request.getParameter("orderSql");
        if (StringUtil.isEmpty(orderSql)) {
            orderSql = "";
        }

        DynaBean dynaBean = request.getAttribute("dynaBean")==null?null:(DynaBean) request.getAttribute("dynaBean");

        if(dynaBean != null){
            param.setDynaBean(dynaBean);
        }

		/*String modelName = request.getParameter("modelName");
		if(StringUtil.isNotEmpty(modelName)) {
			tableCode = modelName;
		}*/
        //研发部:云凤程
        if (StringUtil.isEmpty(tableCode, null)) {
            if (tableCode != null) {
                //logger.error("tableCode is null!");
            }
            return param;
        }
        param.setTableCode(tableCode);

        return param;
    }
}
