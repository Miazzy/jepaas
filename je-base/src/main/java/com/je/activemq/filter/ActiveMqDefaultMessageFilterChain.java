package com.je.activemq.filter;

import com.je.activemq.model.PlatformMessage;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>过滤器链</h2>
 * @author Lijun
 */
public class ActiveMqDefaultMessageFilterChain implements IMessageFilter{
	
	private List<IMessageFilter> filters = new ArrayList<IMessageFilter>();
	private int index = 0;

	/**
	 * 过滤器链构造方法
	 * @Date  2019/7/11
	 * @Param filter: 消息过滤器集合
	 * @return: com.je.activemq.filter.ActiveMqDefaultMessageFilterChain
	**/
	public ActiveMqDefaultMessageFilterChain addFilter(IMessageFilter filter){
		this.filters.add(filter);
		return this;
	}
	
	@Override
	public void doFilter(PlatformMessage message, ActiveMqDefaultMessageFilterChain chain){
		try{
			if(index == filters.size()){
				index = 0;
				return;
			}
			IMessageFilter filter = filters.get(index);
			index++;
			filter.doFilter(message, chain);
		}catch (Throwable e){
			throw new PlatformException("ActiveMQ执行消息处理异常！",PlatformExceptionEnum.JE_CORE_ERROR,e);
		}
	}
	
}
