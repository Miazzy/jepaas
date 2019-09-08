package com.je.activemq.service;

import java.util.HashMap;
import java.util.Map;
import com.je.activemq.ActivemqAsynConsumer;
import com.je.activemq.filter.ActiveMqDefaultMessageFilterChain;
import com.je.activemq.listener.AbstractMessageListener;
import com.je.activemq.model.PlatformMessage;

import javax.jms.JMSException;

/**
 * <h2>发送消息服务</h2>
 * @author Lijun
 */
public interface ActiveMessageManager {
	
	/**
	 * 消费者寄存器
	 */
	Map<String, ActivemqAsynConsumer> MQ_QUENE_CONSUERMS = new HashMap<String, ActivemqAsynConsumer>();
	
	/**
	 * 消费者寄存器
	 */
	Map<String, ActivemqAsynConsumer> MQ_TOPIC_CONSUERMS = new HashMap<String, ActivemqAsynConsumer>();
	
	/**
	 * 启动并创建队列异步消费者
	 * @param queneName: 队列名称
	 * @param messageListener: 异步消息监听服务
	 */
	void startQueneAsynConsumer(String queneName, AbstractMessageListener messageListener) throws JMSException;

	/**
	 * 启动并创建队列异步消费者
	 * @param queneName: 队列名称
	 * @param groupName：组名称
	 * @param messageListener: 异步消息监听服务
	 */
	void startQueneAsynConsumer(String queneName,String groupName, AbstractMessageListener messageListener) throws JMSException;

	/**
	 * 启动并创建Topic异步消费者
	 * @param topicName 主题名称
 	 * @param messageListener: 异步消息监听服务
	 */
	void startTopicAsynConsumer(String topicName, AbstractMessageListener messageListener) throws JMSException;

	/**
	 * 启动并创建Topic异步消费者
	 * @param topicName 主题名称
	 * @param groupName 组名称
	 * @param messageListener: 异步消息监听服务
	 */
	void startTopicAsynConsumer(String topicName,String groupName, AbstractMessageListener messageListener) throws JMSException;

	/**
	 * 停止队列消费者监听服务
	 * @param queneName 队列或主题名称
	 */
	void stopQueneAsynConsumer(String queneName) throws JMSException;

	/**
	 * 停止队列消费者监听服务
	 * @param queneName 队列或主题名称
	 * @param groupName 组名
	 */
	void stopQueneAsynConsumer(String queneName,String groupName) throws JMSException;

	/**
	 * 停止Topic消费者监听服务
	 * @param topicName: 消费者ID
	 */
	void stopTopicAsynConsumer(String topicName) throws JMSException;

	/**
	 * 发送队列消息
	 * @param message: 消息体
	 * @param chain 拦截器链
	 */
	void sendQueneMessage(PlatformMessage message, ActiveMqDefaultMessageFilterChain chain);

	/**
	 * 发送Topic消息
	 * @param message 消息体
	 * @param chain 拦截器链
	 */
	void sendTopicMessage(PlatformMessage message, ActiveMqDefaultMessageFilterChain chain);

	/**
	 * 不使用拦截器链发送队列消息
	 * @param message: 消息体
	 */
	void sendQueneMessageWithNoFilter(PlatformMessage message);

	/**
	 * 不使用拦截器链发送Topic消息
	 * @param message 消息体
	 */
	void sendTopicMessageWithNoFilter(PlatformMessage message);
}
