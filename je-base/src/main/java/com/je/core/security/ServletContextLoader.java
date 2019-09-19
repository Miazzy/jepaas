/**
 * 
 */
package com.je.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 系统资源初始化加载类 //TODO 张帅鹏 该类可删除
 * @author chenmeng
 * 2012-2-15 上午10:29:26
 */
public class ServletContextLoader implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(ServletContextLoader.class);
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("系统关闭...............");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();   
		SecurityManager securityManager = this.getSecurityManager(servletContext);
		logger.info("加载系统资源...............");
		// loading authorities
		Map<String, String> urlAuthorities = securityManager.loadUrlAuthorities();
		servletContext.setAttribute("urlAuthorities", urlAuthorities);
	}

	protected SecurityManager getSecurityManager(ServletContext servletContext) {
		//通过Spring依赖来注入把securityManager(UserDaoImpl)注入到街口SecurityManager中
		return (SecurityManager) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean("securityManager");    
	} 
}
