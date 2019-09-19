package com.je.activemq.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import com.je.activemq.filter.ActiveMqDefaultMessageFilterChain;
import com.je.activemq.filter.IMessageFilter;
import com.je.activemq.model.PlatformMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h2>异步消息监听服务</h2>
 * @author Lijun
 */
public abstract class AbstractMessageListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMessageListener.class);

	/**
	 * 默认组名
	 */
	private static final String DEFAULT_GROUP_NAME = "default";
	/**
	 * 消费者
	**/
	private String name;
	/**
	 * 分组名
	 **/
	private String groupName = DEFAULT_GROUP_NAME;
	/**
	 * 默认的接收消息过滤器链
	 */
	private ActiveMqDefaultMessageFilterChain filterChain = new ActiveMqDefaultMessageFilterChain();

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected String getGroupName() {
		return groupName;
	}

	protected void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * 全局实际使用的消费者名称
	 * @return
	 */
	public String getGloablName(){
		return String.format("%s_%s",getGroupName(),getName());
	}

	/**
	 * 获取拦截器链
	 * @Date  2019/7/11
	 * @return: com.je.activemq.filter.ActiveMqDefaultMessageFilterChain
	**/
	protected ActiveMqDefaultMessageFilterChain getFilterChain() {
		return filterChain;
	}

	/**
	 * 设置拦截器链
	 * @Date  2019/7/11
	 * @Param filterChain:
	 * @return: void
	**/
	protected void setFilterChain(ActiveMqDefaultMessageFilterChain filterChain) {
		this.filterChain = filterChain;
	}

	/**
	 * @Date  2019/7/11
	 * @Param consumerName: 消费者
	**/
	public AbstractMessageListener(String name) {
		this.name = name;
	}

    /**
	 * @Date  2019/7/11
	 * @Param consumerName: 消费者
	 * @Param groupName: 组名称
	**/
	public AbstractMessageListener(String name,String groupName) {
		this.name = name;
		this.groupName = groupName;
	}
	
	/**
	 * 默认构造函数
	 * @param filterChain
	 */
	public AbstractMessageListener(ActiveMqDefaultMessageFilterChain filterChain) {
		this.filterChain = filterChain;
	}

	public void addFilter(IMessageFilter messageFilter){
		logger.debug("消费者{}添加拦截器",getGloablName());
		this.filterChain.addFilter(messageFilter);
	}

	/**
	 * 消息转换，以及处理之前所做的操作
	 * @param message 平台封装消息
	 */
	abstract PlatformMessage convertAndProcessBefore(Message message);

	/**
	 * 消息处理之后做的操作
	 * @param message
	 */
	public void after(PlatformMessage message){}

	/**
	 * 正式封装的消息处理方法，在此之前已转换为平台封装的消息
	 * @param message
	 */
	public abstract void doMessage(PlatformMessage message);
	
	@Override
	public void onMessage(Message message) {
		logger.debug("消费者{}接收到消息",getGloablName());
		//执行消息之前的操作
		PlatformMessage platformMessage  = convertAndProcessBefore(message);
		//如果有过滤器，则执行过滤器
		if(filterChain != null){
			filterChain.doFilter(platformMessage, filterChain);
		}
		//执行消息处理操作
		doMessage(platformMessage);
		//执行处理后的操作
		after(platformMessage);
		logger.debug("{}消费者接收消息处理完成！",getGloablName());
	}
}
