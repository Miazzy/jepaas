package com.je.core.security;

import com.je.core.constants.ConstantVars;

import com.je.core.util.SecurityUserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.ConfigAttributeEditor;
import org.springframework.security.intercept.web.FilterInvocation;
import org.springframework.security.intercept.web.FilterInvocationDefinitionSource;
import org.springframework.security.util.AntUrlPathMatcher;
import org.springframework.security.util.RegexUrlPathMatcher;
import org.springframework.security.util.UrlMatcher;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
/**
 * 资源权限拦截器 //TODO 张帅鹏 该类可删除
 * @author chenmeng
 * 
 */
public class ResourceInvocation implements FilterInvocationDefinitionSource, InitializingBean {
	protected static Logger logger = LoggerFactory.getLogger(ResourceInvocation.class);
	//URL适配器
    private UrlMatcher urlMatcher;
    //用户权限地址
    private boolean useAntPath = true;
    //小写比较
    private boolean lowercaseComparisons = true;
    //URL与参数分隔符
    @SuppressWarnings("unused")
	private String paramFlag = "?";

    public void setUseAntPath(boolean useAntPath) {
        this.useAntPath = useAntPath;
    }
    
    public void setLowercaseComparisons(boolean lowercaseComparisons) {
        this.lowercaseComparisons = lowercaseComparisons;
    }
    
    public void afterPropertiesSet() throws Exception {
        
        this.urlMatcher = new RegexUrlPathMatcher();
        if (useAntPath) {
            this.urlMatcher = new AntUrlPathMatcher();
        }
        if ("true".equals(lowercaseComparisons)) {
            if (!this.useAntPath) {
                ((RegexUrlPathMatcher) this.urlMatcher).setRequiresLowerCaseUrl(true);
            }
        } else if ("false".equals(lowercaseComparisons)) {
            if (this.useAntPath) {
                ((AntUrlPathMatcher) this.urlMatcher).setRequiresLowerCaseUrl(false);
            }
        }
        
    }
    public ConfigAttributeDefinition getAttributes(Object filter) throws IllegalArgumentException {
    	// 得到当前用户的角色
     	FilterInvocation fi=(FilterInvocation)filter;
    	String username = SecurityUserHolder.getCurrentUser().getUsername();
    	String login=fi.getHttpRequest().getParameter("login");
    	if(login!=null && login.equalsIgnoreCase(ConstantVars.STR_TRUE)){
    		return null;
    	}
    	// admin用户不受拦截
    	if(!"GUEST".equalsIgnoreCase(username)){
        	return null;
        }
//    	logger.debug("开始权限拦截...");
//        FilterInvocation filterInvocation = (FilterInvocation) filter;
//        String requestURI = filterInvocation.getRequestUrl();
//        logger.debug("得到请求的URL: " + requestURI);
//        if(requestURI != null) {
//        	int paramIndex = requestURI.indexOf(paramFlag);
//        	if(-1 != paramIndex) {
//        		requestURI = requestURI.substring(0, paramIndex);
//        		logger.debug("URL参数截除后: " + requestURI);
//        	}
//            Map<String, String> urlAuthorities = this.getUrlAuthorities(filterInvocation);
//            logger.debug("得到系统中全部的URL资源, 总数为：" + urlAuthorities.size());
//            String grantedAuthorities = null;
//            for(Iterator<Map.Entry<String, String>> iter = urlAuthorities.entrySet().iterator(); iter.hasNext();) {
//                Map.Entry<String, String> entry = iter.next();
//                String url = entry.getValue();
//                logger.debug("用请求的URL和资源库中的匹配：");
//                if(urlMatcher.pathMatchesUrl(url, requestURI)) {
//                	logger.debug("资源库中发现请求URL的拦截信息：" + url);
//                    grantedAuthorities = entry.getValue();
//                    break;
//                }
//                
//            }        
            /********************************得到当前用户权限状态***************************************/
            //logger.info("获得当前用户的所有权限");
//            Authtype[] authtypes = new Authtype[1];
//            authtypes[0] = Authtype.URL;
//    		  Set<Authorities> authorities = SecurityAssist.getCurrentUserAuth(authtypes);
    		
            /*********************************URL权限过滤******************************************/
    		//如果比对很慢的影响效率,建议变成数值之间的比对
            //判断得到的URL链接是否在权限空库中进行管理
//            if(grantedAuthorities != null) { //在其中进行管理
//                ConfigAttributeEditor configAttrEditor = new ConfigAttributeEditor();
//                configAttrEditor.setAsText(grantedAuthorities);
//                boolean i = false ;//权限标示
//    			for(Authorities auth : authorities) {
//    				if(grantedAuthorities.equalsIgnoreCase(auth.getAuthValue())){
//    					logger.debug("用户" + SecurityUserHolder.getCurrentUser().getUsername() +"具备URL："+ grantedAuthorities +"的访问权限");
//    					i = true;
//    					break;
//    				}
//    			}
//    			if(!i){
//    				System.out.println("用户["+SecurityUserHolder.getCurrentUser().getUsername()+"]对该URL["+grantedAuthorities+"]没有操作权限");
//    				return(ConfigAttributeDefinition) configAttrEditor.getValue();
//    			}else{
//    				return null;
//    			}
//            }
//        }
        
    	ConfigAttributeEditor caEditor = new ConfigAttributeEditor();
        caEditor.setAsText("grantedAuthorities");
        
        /*********************************URL权限过滤END******************************************/        
        return (ConfigAttributeDefinition)caEditor.getValue();
    }

	@SuppressWarnings("rawtypes")
	public Collection getConfigAttributeDefinitions() {
        return null;
    }

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
        return true;
    }
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getUrlAuthorities(FilterInvocation filterInvocation) {
        ServletContext servletContext = filterInvocation.getHttpRequest().getSession().getServletContext();
        return (Map<String, String>)servletContext.getAttribute("urlAuthorities");
    }
}
