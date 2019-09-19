package com.je.activemq;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

/**
 * <h2>ActiveMQ异步消费者</h2>
 * @author Lijun
 */
public class ActivemqAsynConsumer {

	private static final Logger logger = LoggerFactory.getLogger(ActivemqAsynConsumer.class);

	/**
	 * 默认组名
	 */
	private static final String DEFAULT_GROUP_NAME = "default";
	/**
	 * 是否是队列
	 */
	private boolean quene;
	/**
	 * Quene名称
	 */
	private String name;
	/**
	 * 组名
	 */
	private String group = DEFAULT_GROUP_NAME;
	/**
	 * JMS连接
	 */
	private Connection connection = null;
	/**
	 * JMS Session
	 */
	private Session session = null;
	/**
	 * 实际消费者
	 */
	private MessageConsumer consumer = null;
	/**
	 * 异步消息监听器
	 */
	private MessageListener listener;

	public boolean isQuene() {
		return quene;
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}

	public Connection getConnection() {
		return connection;
	}

	public Session getSession() {
		return session;
	}

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public String getGlobalName(){
		return String.format("%s_%s",group,name);
	}

	/**
	 * 构造函数，使用此构造函数，开发人员需要手动设置异步监听器
	 * @param name Quene名称
	 * @param quene 是否是队列
	 */
	public ActivemqAsynConsumer(boolean quene, String name) {
		this.quene = quene;
		this.name = name;
	}

	/**
	 * 构造函数，使用此构造函数，开发人员需要手动设置异步监听器
	 * @param name Quene名称
	 * @param quene 是否是队列
	 * @param group 组名
	 */
	public ActivemqAsynConsumer(boolean quene, String name,String group) {
		this.quene = quene;
		this.name = name;
		this.group = group;
	}

	/**
	 * 构造函数
	 * @param quene 是否是队列
	 * @param name Quene或者Topic名称
	 * @param listener 消息异步监听器
	 */
	public ActivemqAsynConsumer(boolean quene, String name, MessageListener listener) {
		this.quene = quene;
		this.name = name;
		this.listener = listener;
	}

	/**
	 * 构造函数
	 * @param quene 是否是队列
	 * @param name Quene或者Topic名称
	 * @param group 组名
	 * @param listener 消息异步监听器
	 */
	public ActivemqAsynConsumer(boolean quene, String name,String group, MessageListener listener) {
		this.quene = quene;
		this.name = name;
		this.group = group;
		this.listener = listener;
	}

	/**
	 * 进行初始化操作
	 * 	1、初始化JMS连接
	 * 	2、初始化JMS会话
	 * 	3、初始化消费者
	 * @param connectionFactory
	 * @throws JMSException
	 */
	private void init(ConnectionFactory connectionFactory) throws JMSException{
		connection = connectionFactory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination;
		if(isQuene()){
			destination =  session.createQueue(getGlobalName());
		}else{
			destination =  session.createTopic(getGlobalName());
		}
		consumer = session.createConsumer(destination);
		connection.start();
	}

	/**
	 * 消费者启动监听操作
	 * 	1、首先初始化消费者，参照方法init(connectionFactory)
	 * 	2、设置异步监听操作
	 * @param connectionFactory
	 * @throws JMSException
	 */
	public void start(ConnectionFactory connectionFactory) throws JMSException{
		if(connectionFactory == null) {
			if(logger.isDebugEnabled()) {
				logger.warn("ActiveMQ ConnectionFactory没有初始化成功！");
			}else {
				logger.warn("ActiveMQ ConnectionFactory没有初始化成功！");
			}
			return;
		}
		init(connectionFactory);
		//开始监听
		if(listener == null){
			throw new PlatformException("当前消费者没有设置监听器", PlatformExceptionEnum.JE_CORE_MQ_ERROR);
		}
		//(异步接收)
		consumer.setMessageListener(listener);
	}

	/**
	 * 停止异步消息监听
	 * @throws JMSException
	 */
	public void stop() throws JMSException{
		if(getConsumer() != null){
			getConsumer().close();
		}
		if(getSession() != null){
			getSession().close();
		}
		if(getConnection() != null){
			getConnection().close();
		}
	}

}
