package com.je.jms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.je.cache.service.jms.GroupChannelCacheManager;
import com.je.cache.service.jms.HistoryStackCacheManager;
import com.je.cache.service.jms.UserChannelCacheManager;
import com.je.rbac.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.core.constants.JmsChannelType;
import com.je.core.constants.tree.NodeType;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.DateUtils;
import com.je.core.util.JEUUID;
import com.je.core.util.StringUtil;
import com.je.core.util.TreeUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.jms.util.JmsDataTread;
import com.je.jms.util.JmsGroupTread;
import com.je.jms.util.JmsSendTread;
import com.je.jms.util.JmsUtil;
import com.je.jms.vo.JmsChannel;
import com.je.jms.vo.JmsGroupInfo;
import com.je.jms.vo.JmsMsgVo;
import com.je.rbac.model.EndUser;

@Component("jmsManager")
public class JmsManagermpl implements JmsManager{
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;
	@Autowired
	private PCServiceTemplate pcServiceTemplate;
	@Autowired
	private UserManager userManager;
	@Override
	public void sendMsg(String channelId, String context,String type) {
		// TODO Auto-generated method stub
		//49f839b1-fb48-47e2-b0f6-2e9aea21147b
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		JmsChannel channel=null;
		if(JmsChannelType.GROUP.equals(type)){
			channel= GroupChannelCacheManager.getCacheValue(channelId);
		}else if (JmsChannelType.USER.equals(type)){
			channel= UserChannelCacheManager.getCacheValue(channelId);
		}
		//构建消息
		JmsMsgVo jmsMsgVo=new JmsMsgVo();
		jmsMsgVo.setSendTime(DateUtils.formatDateTime(new Date()));
		jmsMsgVo.setContext(context);
		jmsMsgVo.setSendUserId(currentUser.getUserId());
		jmsMsgVo.setSendUserName(currentUser.getUsername());
		jmsMsgVo.setSendUserPhoto(currentUser.getPhoto());
		jmsMsgVo.setId(JEUUID.uuid());
		jmsMsgVo.setChannelId(channelId);
		jmsMsgVo.setChannelType(type);
		//放入历史堆栈
		DynaBean msg=JmsUtil.buildHistory(jmsMsgVo, channel.getUsers(),currentUser);
		HistoryStackCacheManager.putCache(jmsMsgVo.getId(), msg);
		//发送消息
		for(DynaBean user:channel.getUsers()){
			if(!user.getStr("USERID").equals(currentUser.getUserId())){
				JmsSendTread jmsSendTread=new JmsSendTread(user.getStr("USERID"),jmsMsgVo);
				jmsSendTread.start();
			}
		}
		//检查堆栈
		if(HistoryStackCacheManager.getKeys().size()>.1000){
			List<DynaBean> lists=new ArrayList<>();
			for(Object val:HistoryStackCacheManager.getCacheValues().values()){
				lists.add((DynaBean) val);
			}
			JmsDataTread dataTread=new JmsDataTread(lists, serviceTemplate, "update");
			dataTread.start();
			HistoryStackCacheManager.clearAllCache();
		}
	}
	@Override
	public void readMsg(String historyIds,String type){
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		//读取人员消息
		if(StringUtil.isNotEmpty(historyIds) && JmsChannelType.USER.equals(type)){
			Integer i=0;
			//持久堆栈
			for(String id:historyIds.split(",")){
				DynaBean history=HistoryStackCacheManager.getCacheValue(id);
				if(history!=null){
					history.set("JMSHISTORY_ACCEPTIDS", currentUser.getUserId());
					i++;
				}
			}
			//持久数据库
			if(i<historyIds.split(",").length){
				pcServiceTemplate.executeSql(" UPDATE JE_SYS_JMSHISTORY SET JMSHISTORY_ACCEPTIDS='"+currentUser.getUserId()+"' WHERE JE_SYS_JMSHISTORY_ID IN ("+StringUtil.buildArrayToString(historyIds.split(","))+")");
			}
		}
		//读取 讨论组消息
		if(StringUtil.isNotEmpty(historyIds) && JmsChannelType.GROUP.equals(type)){
			Integer i=0;
			//持久堆栈
			for(String id:historyIds.split(",")){
				DynaBean history=HistoryStackCacheManager.getCacheValue(id);
				if(history!=null){
					String oldValue=history.getStr("JMSHISTORY_ACCEPTIDS","");
					if(StringUtil.isNotEmpty(oldValue)){
						if(oldValue.indexOf(currentUser.getUserId())==-1){
							oldValue=(oldValue+","+currentUser.getUserId());
						}
						history.set("JMSHISTORY_ACCEPTIDS", oldValue);
					}else{
						history.set("JMSHISTORY_ACCEPTIDS", currentUser.getUserId());
					}
					i++;
				}
			}
			//持久数据库
			if(i<historyIds.split(",").length){
				List<DynaBean> historys=serviceTemplate.selectList("JE_SYS_JMSHISTORY", " AND JE_SYS_JMSHISTORY_ID IN ("+StringUtil.buildArrayToString(historyIds.split(","))+")");
				for(DynaBean history:historys){
					String oldValue=history.getStr("JMSHISTORY_ACCEPTIDS","");
					if(StringUtil.isNotEmpty(oldValue)){
						if(oldValue.indexOf(currentUser.getUserId())==-1){
							oldValue=(oldValue+","+currentUser.getUserId());
						}
						history.set("JMSHISTORY_ACCEPTIDS", oldValue);
					}else{
						history.set("JMSHISTORY_ACCEPTIDS", currentUser.getUserId());
					}
					serviceTemplate.update(history);
				}
			}
		}
	}
	@Override
	public JmsChannel getChannel(String type, String groupId, String groupName,String ids) {
		// TODO Auto-generated method stub
		JmsChannel channel=null;
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		//讨论组获取
		if(JmsChannelType.GROUP.equals(type)){
			if(StringUtil.isNotEmpty(groupId)){
				channel=GroupChannelCacheManager.getCacheValue(groupId);
				if(channel==null){
					DynaBean group=serviceTemplate.selectOneByPk("JE_SYS_JMSGROUP", groupId);
					if(group!=null){
						channel=JmsUtil.buildGroup(group);
						GroupChannelCacheManager.putCache(channel.getId(), channel);
					}
				}
			}
			if(channel==null){
				channel=new JmsChannel();
				if(StringUtil.isNotEmpty(ids)){
					ids+=(","+currentUser.getUserId());
				}else{
					ids=currentUser.getUserId();
				}
				List<DynaBean> users=userManager.getUserByIds(ids);
				channel.setId(JEUUID.uuid());
				channel.setName(groupName);
				channel.setType(JmsChannelType.GROUP);
				channel.setUsers(users);
				channel.setOwnerId(currentUser.getUserId());
				channel.setOwnerName(currentUser.getUsername());
				saveGroup(channel);
				GroupChannelCacheManager.putCache(channel.getId(), channel);
			}
		}else if(JmsChannelType.USER.equals(type)){
			channel=UserChannelCacheManager.getCacheValue(currentUser.getUserId()+"_"+ids);
			if(channel==null){
				channel=UserChannelCacheManager.getCacheValue(ids+"_"+currentUser.getUserId());
			}
			if(channel==null){
				channel=new JmsChannel();
				String channelId=getUserChannelId(currentUser.getUserId(), ids);
				if(StringUtil.isEmpty(channelId)){
					channelId=currentUser.getUserId()+"_"+ids;
				}
				DynaBean user=userManager.getUserById(ids);
				channel.getUsers().add(user);
				DynaBean loginUser=new DynaBean("JE_CORE_ENDUSER",false);
				loginUser.set(BeanUtils.KEY_PK_CODE, "USERID");
				loginUser.set("USERNAME", currentUser.getUsername());
				loginUser.set("USERID", currentUser.getUserId());
				channel.getUsers().add(loginUser);
				channel.setId(channelId);
				channel.setType(JmsChannelType.USER);
				UserChannelCacheManager.putCache(channel.getId(), channel);
			}
		}
		return channel;
	}
	@Override
	public void saveStack(String userId,String channelId){
		List<String> ids=new ArrayList<String>();
		for(Object historyObj:UserChannelCacheManager.getCacheValues().values()){
			DynaBean history= (DynaBean) historyObj;
			//按照人员把未读的记录存放到数据   已方便查询
			if(StringUtil.isNotEmpty(userId)){
				String type=history.getStr("JMSHISTORY_CHANNELTYPE");
				String receiveIds=history.getStr("JMSHISTORY_RECEIVEUSERS","");
				String acceptIds=history.getStr("JMSHISTORY_ACCEPTIDS","");
				if(type.equals("GROUP")){
					if(receiveIds.indexOf(userId)!=-1 && acceptIds.indexOf(userId)==-1){
						ids.add(history.getStr("JE_SYS_JMSHISTORY_ID"));
						serviceTemplate.insert(history);
					}
				}else{
					if(receiveIds.equals(userId) && !acceptIds.equals(userId)){
						ids.add(history.getStr("JE_SYS_JMSHISTORY_ID"));
						serviceTemplate.insert(history);
					}
				}
			}
			//把指定通道的记录存放到数据   方便查询历史记录
			if(StringUtil.isNotEmpty(channelId)){
				String cId=history.getStr("JMSHISTORY_CHANNEL","");
				if(cId.equals(channelId)){
					ids.add(history.getStr("JE_SYS_JMSHISTORY_ID"));
					serviceTemplate.insert(history);
				}
			}
		}
		for(String id:ids){
			HistoryStackCacheManager.removeCache(id);
		}
	}
	@Override
	public JSONTreeNode getGroupTree() {
		// TODO Auto-generated method stub
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		JSONTreeNode rootNode=TreeUtil.buildRootNode();
		List<JSONTreeNode> childrens=new ArrayList<JSONTreeNode>();
		List<DynaBean> groups=serviceTemplate.selectList("JE_SYS_JMSGROUP", " AND JMSGROUP_USERIDS LIKE '%"+currentUser.getUserId()+"%' ORDER BY SY_CREATETIME DESC");
		for(DynaBean group:groups){
			JSONTreeNode node=TreeUtil.buildTreeNode(group.getStr("JE_SYS_JMSGROUP_ID"), group.getStr("JMSGROUP_NAME"), "", "", "GROUP", "jeicon jeicon-group", NodeType.ROOT);
			node.setBean(group.getValues());
			node.setLeaf(true);
			if(currentUser.getUserId().equals(group.getStr("SY_CREATEUSERID"))){
				node.setIconCls("jeicon jeicon-group");
			}
			node.setDisabled("0");
			childrens.add(node);
		}
		rootNode.setChildren(childrens);
		return rootNode;
	}
	@Override
	public JSONTreeNode getRelationTree(Integer limit) {
		// TODO Auto-generated method stub
		List<String> channelInfos=new ArrayList<String>();
		Map<String,Date> channelTimeMaps=new HashMap<String,Date>();
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		String userId=currentUser.getUserId();
		for(Object historyObj:UserChannelCacheManager.getCacheValues().values()){
			DynaBean history= (DynaBean) historyObj;
			if(userId.equals(history.getStr("JMSHISTORY_SENDUSERID")) || history.getStr("JMSHISTORY_RECEIVEUSERS","").indexOf(userId)!=-1){
				String channelInfo=history.getStr("JMSHISTORY_CHANNEL")+"#"+history.getStr("JMSHISTORY_CHANNELTYPE");
				if(!channelInfos.contains(channelInfo)){
					channelInfos.add(channelInfo);
					channelTimeMaps.put(channelInfo, DateUtils.getDate(history.getStr("JMSHISTORY_SENDTIME"),DateUtils.DAFAULT_DATETIME_FORMAT));
					if(channelInfos.size()>=limit)break;
				}else{
					Date date=DateUtils.getDate(history.getStr("JMSHISTORY_SENDTIME"),DateUtils.DAFAULT_DATETIME_FORMAT);
					if(date.after(channelTimeMaps.get(channelInfo))){
						channelTimeMaps.put(channelInfo, date);
					}
				}
			}
		}
		if(channelInfos.size()<limit){
			List<Map> lists=pcServiceTemplate.queryMapBySql("SELECT JMSHISTORY_CHANNEL,JMSHISTORY_CHANNELTYPE,MAX(JMSHISTORY_SENDTIME) AS JMSHISTORY_SENDTIME FROM JE_SYS_JMSHISTORY WHERE JMSHISTORY_RECEIVEUSERS LIKE '%"+userId+"%' OR JMSHISTORY_SENDUSERID='"+userId+"' GROUP BY JMSHISTORY_CHANNEL,JMSHISTORY_CHANNELTYPE");
			for(Map history:lists){
				String channelInfo=history.get("JMSHISTORY_CHANNEL")+"#"+history.get("JMSHISTORY_CHANNELTYPE");
				if(!channelInfos.contains(channelInfo)){
					channelInfos.add(channelInfo);
					channelTimeMaps.put(channelInfo, DateUtils.getDate(history.get("JMSHISTORY_SENDTIME")+"",DateUtils.DAFAULT_DATETIME_FORMAT));
					if(channelInfos.size()>=limit)break;
				}else{
					Date date=DateUtils.getDate(history.get("JMSHISTORY_SENDTIME")+"",DateUtils.DAFAULT_DATETIME_FORMAT);
					if(date.after(channelTimeMaps.get(channelInfo))){
						channelTimeMaps.put(channelInfo, date);
					}
				}
			}
		}
		//根据通道最近消息发送时间排序
		channelInfos=JmsUtil.sortRelation(channelInfos, channelTimeMaps);
		Map<String,DynaBean> groupInfos=new HashMap<String,DynaBean>();
		Map<String,String> userInfos=new HashMap<String,String>();
		Set<String> groupIds=new HashSet<String>();
		Set<String> userIds=new HashSet<String>();
		for(String channelInfo:channelInfos){
			String channelId=channelInfo.split("#")[0];
			String channelType=channelInfo.split("#")[1];
			if(JmsChannelType.GROUP.equals(channelType)){
				groupIds.add(channelId);
			}else{
				String userId1=channelId.split("_")[0];
				if(userId1.equals(userId)){
					userIds.add(channelId.split("_")[1]);
				}else{
					userIds.add(userId1);
				}
			}
		}
		//查询所用到的讨论组通道
		if(groupIds.size()>0){
			List<DynaBean> groups=serviceTemplate.selectList("JE_SYS_JMSGROUP", " AND JE_SYS_JMSGROUP_ID IN ("+StringUtil.buildArrayToString(ArrayUtils.getArray(groupIds))+")");
			for(DynaBean group:groups){
				groupInfos.put(group.getStr("JE_SYS_JMSGROUP_ID"), group);
			}
		}
		//查询所有用到的用户信息
		if(userIds.size()>0){
			List<DynaBean> users=userManager.getUserByIds(StringUtil.buildSplitString(ArrayUtils.getArray(userIds),","));
			for(DynaBean user:users){
				userInfos.put(user.getStr("USERID"), user.getStr("USERNAME"));
			}
		}
		//构建树形
		JSONTreeNode rootNode=TreeUtil.buildRootNode();
		List<JSONTreeNode> childrens=new ArrayList<JSONTreeNode>();
		for(String channelInfo:channelInfos){
			JSONTreeNode node=null;
			String channelId=channelInfo.split("#")[0];
			String channelType=channelInfo.split("#")[1];
			if(JmsChannelType.GROUP.equals(channelType)){
				DynaBean group=groupInfos.get(channelId);
				node=TreeUtil.buildTreeNode(channelId, group.getStr("JMSGROUP_NAME"), "", "", JmsChannelType.GROUP, "jeicon jeicon-group", NodeType.ROOT);
				node.setBean(group.getValues());
				if(userId.equals(group.getStr("SY_CREATEUSERID"))){
					node.setIconCls("jeicon jeicon-group");
				}
			}else{
				String userId1=channelId.split("_")[0];
				if(userId1.equals(userId)){
					userId1=channelId.split("_")[1];
				}
				node=TreeUtil.buildTreeNode(userId1, userInfos.get(userId1), userId1, "", JmsChannelType.USER, "jeicon jeicon-khgl-pc", NodeType.ROOT);
			}
			node.setLeaf(true);
			node.setDisabled("0");
			childrens.add(node);
		}
		rootNode.setChildren(childrens);
		return rootNode;
	}
	@Override
	public void updateGroup(String groupId, String groupName) {
		// TODO Auto-generated method stub
		DynaBean group=serviceTemplate.selectOneByPk("JE_SYS_JMSGROUP",groupId);
		group.set("JMSGROUP_NAME", groupName);
		group=serviceTemplate.update(group);
		JmsChannel channel=GroupChannelCacheManager.getCacheValue(groupId);
		if(channel!=null){
			channel.setName(groupName);
			GroupChannelCacheManager.putCache(groupId, channel);
		}
		JmsGroupInfo groupInfo=new JmsGroupInfo(groupId, "update", "", "", groupName);
		//推送更新组信息
		for(String userId:group.getStr("JMSGROUP_USERIDS","").split(",")){
			JmsGroupTread groupTread=new JmsGroupTread(userId, groupInfo);
			groupTread.start();
		}
	}
	@Override
	public void removeGroup(String groupId) {
		// TODO Auto-generated method stub
		DynaBean group=serviceTemplate.selectOneByPk("JE_SYS_JMSGROUP",groupId);
		serviceTemplate.deleteByWehreSql("JE_SYS_JMSGROUP", " AND JE_SYS_JMSGROUP_ID='"+groupId+"'");
		GroupChannelCacheManager.removeCache(groupId);
		Set<String> removeIds=new HashSet<String>();
		for(Object historyObj:UserChannelCacheManager.getCacheValues().values()){
			DynaBean history= (DynaBean) historyObj;
			if(JmsChannelType.GROUP.equals(history.getStr("JMSHISTORY_CHANNELTYPE")) && groupId.equals(history.getStr("JMSHISTORY_CHANNEL"))){
				removeIds.add(history.getStr("JE_SYS_JMSHISTORY_ID"));
			}
		}
		for(String id:removeIds){
			HistoryStackCacheManager.removeCache(id);
		}
		pcServiceTemplate.executeSql("DELETE FROM JE_SYS_JMSHISTORY WHERE JMSHISTORY_CHANNEL='"+groupId+"' AND JMSHISTORY_CHANNELTYPE='"+JmsChannelType.GROUP+"'");
		//推送更新组信息
		JmsGroupInfo groupInfo=new JmsGroupInfo(groupId, "remove", "", "", group.getStr("JMSGROUP_NAME"));
		for(String userId:group.getStr("JMSGROUP_USERIDS","").split(",")){
			JmsGroupTread groupTread=new JmsGroupTread(userId, groupInfo);
			groupTread.start();
		}
	}
	@Override
	public void updateGroupUser(String groupId, String userIds, String oper) {
		// TODO Auto-generated method stub
		DynaBean group=serviceTemplate.selectOneByPk("JE_SYS_JMSGROUP", groupId);
		JmsChannel channel=GroupChannelCacheManager.getCacheValue(groupId);
		if(channel==null){
			channel=JmsUtil.buildGroup(group);
		}
		String[] userIdArray=userIds.split(",");
		if("add".equalsIgnoreCase(oper)){
			List<DynaBean> addUsers=serviceTemplate.selectList("JE_CORE_ENDUSER", " AND USERID IN ("+StringUtil.buildArrayToString(userIds.split(","))+") AND USERID NOT IN ("+StringUtil.buildArrayToString(group.getStr("JMSGROUP_USERIDS").split(","))+")");
			channel.getUsers().addAll(addUsers);
			group.set("JMSGROUP_USERIDS", StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(channel.getUsers(), "USERID"),","));
			group.set("JMSGROUP_USERNAMES", StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(channel.getUsers(), "USERNAME"),","));
			group.set("JMSGROUP_USERCOUNT", channel.getUsers().size());
			group=serviceTemplate.update(group);
			//推送告知原来讨论组的人员
			for(DynaBean user:channel.getUsers()){
				JmsGroupInfo groupInfo=null;
				String userId=user.getStr("USERID");
				if(ArrayUtils.contains(userIdArray, userId)){
					groupInfo=new JmsGroupInfo(groupId, "join",group.getStr("JMSGROUP_USERNAMES"),group.getStr("JMSGROUP_USERIDS"), group.getStr("JMSGROUP_NAME"));
					groupInfo.setGroupValues(group.getValues());
					groupInfo.setOper("join");//加入讨论组
				}else{
					groupInfo=new JmsGroupInfo(groupId, "refresh",group.getStr("JMSGROUP_USERNAMES"),group.getStr("JMSGROUP_USERIDS"), group.getStr("JMSGROUP_NAME"));
				}
				JmsGroupTread groupTread=new JmsGroupTread(userId, groupInfo);
				groupTread.start();
			}
			GroupChannelCacheManager.putCache(channel.getId(), channel);
		}else if("remove".equals(oper)){
			List<DynaBean> removeUsers=new ArrayList<DynaBean>();
			List<DynaBean> newUsers=new ArrayList<DynaBean>();
			for(DynaBean user:channel.getUsers()){
				if(ArrayUtils.contains(userIdArray,user.getStr("USERID"))){
					removeUsers.add(user);
				}else{
					newUsers.add(user);
				}
			}
			/**如果讨论没有人员了则删除讨论组*/
			if(removeUsers.size()==channel.getUsers().size()){
				removeGroup(groupId);
			}else{
				group.set("JMSGROUP_USERIDS", StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(newUsers, "USERID"),","));
				group.set("JMSGROUP_USERNAMES", StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(newUsers, "USERNAME"),","));
				group.set("JMSGROUP_USERCOUNT", newUsers.size());
				JmsGroupInfo removeGroupInfo=new JmsGroupInfo(groupId, "kickOut",group.getStr("JMSGROUP_USERNAMES"),group.getStr("JMSGROUP_USERIDS"), group.getStr("JMSGROUP_NAME"));
				if(userIds.equals(SecurityUserHolder.getCurrentUser().getUserId())){
					removeGroupInfo.setOper("exit");
				}
				for(DynaBean user:removeUsers){
					JmsGroupTread groupTread=new JmsGroupTread(user.getStr("USERID"), removeGroupInfo);
					groupTread.start();
				}
				channel.setUsers(newUsers);
				JmsGroupInfo refreshGroupInfo=new JmsGroupInfo(groupId, "refresh",group.getStr("JMSGROUP_USERNAMES"),group.getStr("JMSGROUP_USERIDS"), group.getStr("JMSGROUP_NAME"));
				//去刷新还剩下人员的组信息
				for(DynaBean user:channel.getUsers()){
					JmsGroupTread groupTread=new JmsGroupTread(user.getStr("USERID"), refreshGroupInfo);
					groupTread.start();
				}
				//同步堆栈数据
				for(Object historyObj:UserChannelCacheManager.getCacheValues().values()){
					DynaBean history= (DynaBean) historyObj;
					if(JmsChannelType.GROUP.equals(history.getStr("JMSHISTORY_CHANNELTYPE")) && groupId.equals(history.getStr("JMSHISTORY_CHANNEL"))){
						for(String removeId:userIdArray){
							String receiveUsers=history.getStr("JMSHISTORY_RECEIVEUSERS","");
							String acceptIdsUsers=history.getStr("JMSHISTORY_ACCEPTIDS","");
							if(receiveUsers.indexOf(removeId)!=-1){// && acceptIdsUsers.indexOf(removeId)==-1
								receiveUsers=StringUtil.replaceSplit(receiveUsers.replace(removeId, ""));
								acceptIdsUsers=StringUtil.replaceSplit(acceptIdsUsers.replace(removeId, ""));
								history.set("JMSHISTORY_RECEIVEUSERS", receiveUsers);
								history.set("JMSHISTORY_ACCEPTIDS", acceptIdsUsers);
							}
						}
					}
				}
				//同步数据库数据
				StringBuffer whereSql=new StringBuffer(" AND JMSHISTORY_CHANNEL='"+groupId+"' AND JMSHISTORY_CHANNELTYPE='"+JmsChannelType.GROUP+"' AND (");
				for(String removeId:userIdArray){
					whereSql.append(" (JMSHISTORY_RECEIVEUSERS LIKE '%"+removeId+"%') OR"); //AND JMSHISTORY_ACCEPTIDS NOT LIKE '%"+removeId+"%'
				}
				if(userIdArray.length>0){
					whereSql.delete(whereSql.length()-2, whereSql.length());
					whereSql.append(")");
					List<DynaBean> historys=serviceTemplate.selectList("JE_SYS_JMSHISTORY", whereSql.toString());
					for(DynaBean history:historys){
						for(String removeId:userIdArray){
							String receiveUsers=history.getStr("JMSHISTORY_RECEIVEUSERS","");
							String acceptIdsUsers=history.getStr("JMSHISTORY_ACCEPTIDS","");
							if(receiveUsers.indexOf(removeId)!=-1){// && acceptIdsUsers.indexOf(removeId)==-1
								receiveUsers=StringUtil.replaceSplit(receiveUsers.replace(removeId, ""));
								acceptIdsUsers=StringUtil.replaceSplit(acceptIdsUsers.replace(removeId, ""));
								history.set("JMSHISTORY_RECEIVEUSERS", receiveUsers);
								history.set("JMSHISTORY_ACCEPTIDS", acceptIdsUsers);
							}
						}
					}
					//加入线程去更新
					JmsDataTread dataTread=new JmsDataTread(historys, serviceTemplate, "update");
					dataTread.start();
				}
				serviceTemplate.update(group);
				GroupChannelCacheManager.putCache(channel.getId(), channel);
			}
		}
	}
	/**
	 * 持久讨论组
	 * @param channel
	 */
	public void saveGroup(JmsChannel channel){
		DynaBean group=new DynaBean("JE_SYS_JMSGROUP",false);
		group.set(BeanUtils.KEY_PK_CODE, "JE_SYS_JMSGROUP_ID");
		List<DynaBean> users=channel.getUsers();
		String[] userNames=new String[users.size()];
		String[] userIds=new String[users.size()];
		for(Integer i=0;i<users.size();i++){
			DynaBean u=users.get(i);
			userNames[i]=u.getStr("USERNAME");
			userIds[i]=u.getStr("USERID");
		}
		group.set("JMSGROUP_USERIDS", StringUtil.buildSplitString(userIds, ","));
		group.set("JMSGROUP_USERNAMES", StringUtil.buildSplitString(userNames, ","));
		group.set("JMSGROUP_NAME", channel.getName());
		group.set("JMSGROUP_USERCOUNT", users.size());
		group.set("JE_SYS_JMSGROUP_ID", channel.getId());
		serviceTemplate.buildModelCreateInfo(group);
		serviceTemplate.insert(group);
	}
	/**
	 * 获取两人交谈的唯一通道ID  确保两人只有一个ID
	 * @param currentUserId
	 * @param userId
	 * @return
	 */
	private String getUserChannelId(String currentUserId,String userId){
		for(Object historyObj:UserChannelCacheManager.getCacheValues().values()){
			DynaBean history= (DynaBean) historyObj;
			String channelId=history.getStr("JMSHISTORY_CHANNEL");
			if(channelId.equals(currentUserId+"_"+userId) || channelId.equals(userId+"_"+currentUserId)){
				return channelId;
			}
		}
		Long count=serviceTemplate.selectCount("JE_SYS_JMSHISTORY"," AND JMSHISTORY_CHANNEL='"+(currentUserId+"_"+userId)+"'");
		if(count>0){
			return (currentUserId+"_"+userId);
		}
		count=serviceTemplate.selectCount("JE_SYS_JMSHISTORY"," AND JMSHISTORY_CHANNEL='"+(userId+"_"+currentUserId)+"'");
		if(count>0){
			return (userId+"_"+currentUserId);
		}
		return "";
	}
}
