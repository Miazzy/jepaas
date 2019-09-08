package com.je.jms.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.je.core.constants.JmsChannelType;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.jms.vo.JmsChannel;
import com.je.jms.vo.JmsMsgVo;
import com.je.rbac.model.EndUser;

public class JmsUtil {
//	/**
//	 * 消息记录堆栈
//	 */
//	public static Map<String,DynaBean> historyStack=new HashMap<String,DynaBean>();
//	/**
//	 * 讨论组通道
//	 */
//	public static Map<String,JmsChannel> groupChannel=new HashMap<String,JmsChannel>();
//	/**
//	 * 人员通道
//	 */
//	public static Map<String,JmsChannel> userChannel=new HashMap<String,JmsChannel>();
	/**
	 * 构建组通道
	 * @param group
	 * @return
	 */
	public static JmsChannel buildGroup(DynaBean group){
		JmsChannel channel=new JmsChannel();
		channel.setId(group.getStr("JE_SYS_JMSGROUP_ID"));
		channel.setName(group.getStr("JMSGROUP_NAME"));
		channel.setType(JmsChannelType.GROUP);
		channel.setOwnerName(group.getStr("SY_CREATEUSERNAME"));
		channel.setOwnerId(group.getStr("SY_CREATEUSERID"));
		List<DynaBean> users=new ArrayList<DynaBean>();
		String[] userNames=group.getStr("JMSGROUP_USERNAMES","").split(",");
		String[] userIds=group.getStr("JMSGROUP_USERIDS","").split(",");
		for(Integer i=0;i<userIds.length;i++){
			if(StringUtil.isNotEmpty(userIds[i])){
				DynaBean u=new DynaBean("PC_CORE_ENDUSER",false);
				u.set(BeanUtils.KEY_PK_CODE, "USERID");
				u.set("USERID", userIds[i]);
				u.set("USERNAME", userNames[i]);
				users.add(u);
			}
		}
		channel.setUsers(users);
		return channel;
	}
	/**
	 * 根据发送消息和接受人构建聊天记录
	 * @param jmsMsgVo
	 * @param users
	 * @param currentUser
	 * @return
	 */
	public static DynaBean buildHistory(JmsMsgVo jmsMsgVo,List<DynaBean> users, EndUser currentUser) {
		// TODO Auto-generated method stub
		DynaBean history=new DynaBean("JE_SYS_JMSHISTORY",false);
		history.set(BeanUtils.KEY_PK_CODE, "JE_SYS_JMSHISTORY_ID");
		history.set("JMSHISTORY_CONTEXT", jmsMsgVo.getContext());
		history.set("JMSHISTORY_SENDTIME", jmsMsgVo.getSendTime());
		history.set("JMSHISTORY_SENDUSERID", jmsMsgVo.getSendUserId());
		history.set("JMSHISTORY_SENDUSERNAME", jmsMsgVo.getSendUserName());
		history.set("JMSHISTORY_SENDUSERPHOTO", jmsMsgVo.getSendUserPhoto());
		history.set("JMSHISTORY_CHANNEL", jmsMsgVo.getChannelId());
		history.set("JMSHISTORY_CHANNELTYPE", jmsMsgVo.getChannelType());
		history.set("JE_SYS_JMSHISTORY_ID", jmsMsgVo.getId());
		String[] userNames=new String[users.size()];
		String[] userIds=new String[users.size()];
		String[] receiveUsers=new String[users.size()-1];
		Integer i=0;
		for(DynaBean user:users){
			userNames[i]=user.getStr("USERNAME");
			userIds[i]=user.getStr("USERID");
			i++;
		}
		i=0;
		for(DynaBean user:users){
			if(currentUser.getUserId().equals(user.getStr("USERID")))continue;
			receiveUsers[i]=user.getStr("USERID");
			i++;
		}
		history.set("JMSHISTORY_USERNAMES", StringUtil.buildSplitString(userNames, ","));
		history.set("JMSHISTORY_USERIDS", StringUtil.buildSplitString(userIds, ","));
		history.set("JMSHISTORY_RECEIVEUSERS", StringUtil.buildSplitString(receiveUsers, ","));
		return history;
	}
	/**
	 * 构建消息VO
	 * @param history
	 * @return
	 */
	public static JmsMsgVo buildMsgVo(DynaBean history){
		JmsMsgVo jmsMsgVo=new JmsMsgVo();
		jmsMsgVo.setId(history.getStr("JE_SYS_JMSHISTORY_ID"));
		jmsMsgVo.setChannelId(history.getStr("JMSHISTORY_CHANNEL"));
		jmsMsgVo.setContext(history.getStr("JMSHISTORY_CONTEXT"));
		jmsMsgVo.setSendTime(history.getStr("JMSHISTORY_SENDTIME"));
		jmsMsgVo.setChannelType(history.getStr("JMSHISTORY_CHANNELTYPE"));
		jmsMsgVo.setSendUserName(history.getStr("JMSHISTORY_SENDUSERNAME"));
		jmsMsgVo.setSendUserId(history.getStr("JMSHISTORY_SENDUSERID"));
		jmsMsgVo.setSendUserPhoto(history.getStr("JMSHISTORY_SENDUSERPHOTO"));
		return jmsMsgVo;
	}
	/**
	 * 按照时间从大到小排列
	 * @param channelInfos
	 * @param channelTimeMaps
	 * @return
	 */
	public static List<String> sortRelation(List<String> channelInfos,Map<String,Date> channelTimeMaps){
		for(Integer i=0;i<channelInfos.size();i++){
			for(Integer j=i+1;j<channelInfos.size();j++){
				Date iDate=channelTimeMaps.get(channelInfos.get(i));
				Date jDate=channelTimeMaps.get(channelInfos.get(j));
				if(iDate.before(jDate)){
					String iValue=channelInfos.get(i);
					channelInfos.set(i, channelInfos.get(j));
					channelInfos.set(j, iValue);
				}
			}
		}
		return channelInfos;
	}
}
