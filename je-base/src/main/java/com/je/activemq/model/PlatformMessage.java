package com.je.activemq.model;

import com.alibaba.fastjson.JSON;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;

/**
 * 封装平台的消息体
 *
 * @ProjectName: je-platform
 * @Package: com.je.activemq.model
 * @ClassName: PlatformMessage
 * @Description: 通过平台实现的MQ消息通讯的消息体，都需要实现此接口
 * @Author: LIULJ
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public interface PlatformMessage extends Serializable,MessageCreator {

    /**
     * 获取消息ID
     * @return
     */
    String getId();

    /**
     * 获取发送者
     * @return
     */
    String getSender();

    /**
     * 获取接收者
     * @return
     */
    String getReceiver();

    /**
     * 转为json
     * @return
     */
    default String toJson(){
        return JSON.toJSONString(this);
    }

    /**
     * 从JSON转换为对象
     * @param json
     * @return
     */
    default PlatformMessage fromJson(String json){
        return JSON.parseObject(json,this.getClass());
    }

}
