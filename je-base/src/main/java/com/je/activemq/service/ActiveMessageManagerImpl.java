package com.je.activemq.service;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import com.google.common.base.Strings;
import com.je.activemq.model.PlatformMessage;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.je.activemq.ActivemqAsynConsumer;
import com.je.activemq.filter.ActiveMqDefaultMessageFilterChain;
import com.je.activemq.listener.AbstractMessageListener;

@Component("activeMessageManager")
public class ActiveMessageManagerImpl implements ActiveMessageManager,ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(ActiveMessageManagerImpl.class);
	private static final String JMS_QUENE_TEMPLATE = "jmsQueneTemplate";
	private static final String JMS_TOPIC_TEMPLATE = "jmsTopicTemplate";
	private static final String JMS_CONNECTION_FACTORY = "jmsConnectionFactory";
	private static final String JMS_DEFAULT_NAME = "default";

	/**
	 * 队列发送模板
	 */
	private JmsTemplate jmsQueneTemplate;

	/**
	 * Topic发送模板
	 */
	private JmsTemplate jmsTopicTemplate;

	/**
	 * 连接工厂
	 */
	private ConnectionFactory connectionFactory;

	@Override
	public void startQueneAsynConsumer(String queneName, AbstractMessageListener messageListener) throws JMSException {
		String fullName = String.format("%s_%s",JMS_DEFAULT_NAME,queneName);
		if(MQ_QUENE_CONSUERMS.containsKey(fullName)){
			logger.error("消费者{}已存在！",fullName);
			throw new PlatformException("消费者已存在！",PlatformExceptionEnum.JE_CORE_ERROR);
		}
		ActivemqAsynConsumer asynConsumer = new ActivemqAsynConsumer(true, queneName,messageListener);
		asynConsumer.start(connectionFactory);
		MQ_QUENE_CONSUERMS.put(fullName, asynConsumer);
		logger.debug("消费者{}启动成功！",fullName);
	}

	@Override
	public void startQueneAsynConsumer(String queneName, String groupName, AbstractMessageListener messageListener) throws JMSException {
		String fullName = String.format("%s_%s",groupName,queneName);
		if(MQ_QUENE_CONSUERMS.containsKey(fullName)){
			logger.error("消费者{}已存在！",fullName);
			throw new PlatformException("消费者已存在！",PlatformExceptionEnum.JE_CORE_ERROR);
		}
		ActivemqAsynConsumer asynConsumer = new ActivemqAsynConsumer(true, queneName,groupName,messageListener);
		asynConsumer.start(connectionFactory);
		MQ_QUENE_CONSUERMS.put(fullName, asynConsumer);
		logger.debug("消费者{}启动成功！",fullName);
	}

	@Override
	public void stopQueneAsynConsumer(String queneName) throws JMSException {
		String fullName = String.format("%s_%s",JMS_DEFAULT_NAME,queneName);
		ActivemqAsynConsumer asynConsumer = MQ_QUENE_CONSUERMS.get(fullName);
		if(asynConsumer == null){
			logger.error("消费者{}不存在！",fullName);
			throw new PlatformException("消费者不存在！",PlatformExceptionEnum.JE_CORE_ERROR);
		}
		asynConsumer.stop();
		MQ_QUENE_CONSUERMS.remove(queneName);
		logger.debug("消费者{}停止成功！",fullName);
	}

	@Override
	public void stopQueneAsynConsumer(String queneName, String groupName) throws JMSException {
		String fullName = String.format("%s_%s",groupName,queneName);
		ActivemqAsynConsumer asynConsumer = MQ_QUENE_CONSUERMS.get(fullName);
		if(asynConsumer == null){
			logger.error("消费者{}不存在！",fullName);
			throw new PlatformException("消费者不存在！",PlatformExceptionEnum.JE_CORE_ERROR);
		}
		asynConsumer.stop();
		MQ_QUENE_CONSUERMS.remove(String.format("%s_%s",groupName,queneName));
		logger.debug("消费者{}停止成功！",asynConsumer.getGlobalName());
	}

	@Override
	public void startTopicAsynConsumer(String topicName, AbstractMessageListener messageListener) throws JMSException {
		String fullName = String.format("%s_%s",JMS_DEFAULT_NAME,topicName);
		if(MQ_TOPIC_CONSUERMS.containsKey(fullName)){
			logger.error("消费者{}已存在！",fullName);
			throw new PlatformException("消费者已存在！",PlatformExceptionEnum.JE_CORE_ERROR);
		}
		ActivemqAsynConsumer asynConsumer = new ActivemqAsynConsumer(false, topicName,messageListener);
		asynConsumer.start(connectionFactory);
		MQ_TOPIC_CONSUERMS.put(fullName,asynConsumer);
		logger.debug("消费者{}启动成功！",fullName);
	}

	@Override
	public void startTopicAsynConsumer(String topicName, String groupName, AbstractMessageListener messageListener) throws JMSException {
		String fullName = String.format("%s_%s",groupName,topicName);
		if(MQ_TOPIC_CONSUERMS.containsKey(fullName)){
			logger.error("消费者{}已存在！",fullName);
			throw new PlatformException("消费者已存在！",PlatformExceptionEnum.JE_CORE_ERROR);
		}
		ActivemqAsynConsumer asynConsumer = new ActivemqAsynConsumer(false, topicName,groupName,messageListener);
		asynConsumer.start(connectionFactory);
		MQ_TOPIC_CONSUERMS.put(fullName,asynConsumer);
		logger.debug("消费者{}启动成功！",fullName);
	}

	@Override
	public void stopTopicAsynConsumer(String topicName) throws JMSException {
		String fullName = String.format("%s_%s",JMS_DEFAULT_NAME,topicName);
		ActivemqAsynConsumer asynConsumer = MQ_TOPIC_CONSUERMS.get(fullName);
		if(asynConsumer == null){
			logger.error("消费者{}已存在！",fullName);
			throw new PlatformException("消费者不存在！",PlatformExceptionEnum.JE_CORE_ERROR);
		}
		asynConsumer.stop();
		MQ_TOPIC_CONSUERMS.remove(topicName);
		logger.debug("消费者{}启动成功！",fullName);
	}

	@Override
	public void sendQueneMessage(PlatformMessage message,ActiveMqDefaultMessageFilterChain chain) {
		String receiver = message.getReceiver();
		if(Strings.isNullOrEmpty(receiver)){
			return;
		}
		if(chain != null){
			chain.doFilter(message, chain);
			logger.debug("发送消息{}拦截器链执行成功！",receiver);
		}
		jmsQueneTemplate.send(receiver, message);
		logger.debug("发送队列{}消息成功！",receiver);
	}

	@Override
	public void sendTopicMessage(PlatformMessage message,ActiveMqDefaultMessageFilterChain chain) {
		String receiver = message.getReceiver();
		if(Strings.isNullOrEmpty(receiver)){
			return;
		}
		if(chain != null){
			chain.doFilter(message, chain);
			logger.debug("发送消息{}拦截器链执行成功！",receiver);
		}
		jmsTopicTemplate.send(receiver, message);
		logger.debug("发送队列{}消息成功！",receiver);
	}

	@Override
	public void sendQueneMessageWithNoFilter(PlatformMessage message) {
		sendQueneMessage(message,null);
	}

	@Override
	public void sendTopicMessageWithNoFilter(PlatformMessage message) {
		sendTopicMessage(message,null);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		logger.debug("执行动态设置JmsTemplate,该动作主要包括三个模板类，包括jmsQueneTemplate,jmsTopicTemplate,jmsConnectionFactory");
		applicationContext.getBeansOfType(JmsTemplate.class).forEach((name, jmsTemplate) -> {
			if(JMS_QUENE_TEMPLATE.equals(name)){
				jmsQueneTemplate = jmsTemplate;
				logger.debug("设置jmsQueneTemplate成功！",jmsTemplate);
			}
			if(JMS_TOPIC_TEMPLATE.equals(name)){
				jmsTopicTemplate = jmsTopicTemplate;
				logger.debug("设置jmsTopicTemplate成功！",jmsTemplate);
			}
		});
		applicationContext.getBeansOfType(ConnectionFactory.class).forEach((name, connectionFactory) -> {
			if(JMS_CONNECTION_FACTORY.equals(name)){
				connectionFactory =connectionFactory;
				logger.debug("设置jmsConnectionFactory成功！",connectionFactory);
			}
		});
		logger.debug("执行动态设置JmsTemplate成功！");
	}
}
