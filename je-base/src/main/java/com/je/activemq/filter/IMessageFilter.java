package com.je.activemq.filter;

import com.je.activemq.model.PlatformMessage;
import java.io.IOException;
import javax.jms.JMSException;

/**
 * <h2>消息过滤器</h2>
 * @author Lijun
 */
public interface IMessageFilter {
	
	/**
	 * 执行过滤操作
	 * @Date  2019/7/2
	 * @Param message: 消息体
	 * @Param chain: 过滤器链
	 * @throws JMSException
	 * @throws IOException
	**/
	void doFilter(PlatformMessage message, ActiveMqDefaultMessageFilterChain chain);
}
