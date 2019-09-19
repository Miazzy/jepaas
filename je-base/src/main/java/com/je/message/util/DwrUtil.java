package com.je.message.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.util.StringUtil;
import com.je.message.vo.DwrMsgVo;

/**
 * DWR工具类
 * @author zhangshuaipeng
 */
public class DwrUtil {
	/*
	 * DWR推送消息
	 * @param msgVo
	 */
	public static void sendMsg(DwrMsgVo msgVo){
		Map<String, String> paramMap=new HashMap<>();
		if(StringUtil.isNotEmpty(msgVo.getUserId())) {
			paramMap.put("targetIds", msgVo.getUserId());
			paramMap.put("text", JsonBuilder.getInstance().toJson(msgVo));
			paramMap.put("userId", "SYSTEM");
			paramMap.put("must", msgVo.getLoginHistory() ? "1" : "0");
			try {
				PushMsgHttpUtil.postToImServer("/instant/message/pushMessage", paramMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(StringUtil.isNotEmpty(msgVo.getGroupId())){
			String text=JsonBuilder.getInstance().toJson(msgVo);
			sendGroupMsg(msgVo.getLoginHistory(),msgVo.getGroupId(),text);
		}
	}
	/*
	 * DWR推送消息
	 * @param msgVo
	 */
	public static void sendGroupMsg(boolean history,String groupId,String text){
		Map<String, String> paramMap=new HashMap<>();
		paramMap.put("userName",PushMsgHttpUtil.IM_USER_NAME);
		paramMap.put("password",PushMsgHttpUtil.IM_USER_PASSWORD);
		paramMap.put("sendType","4");
		paramMap.put("must",history?"1":"0");

		paramMap.put("groupIds",groupId);
		paramMap.put("text",text);
		try {
			PushMsgHttpUtil.postToImServer("/instant/message/pushGroupMessage",paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定租户的在线用户集合
	 * @param zhId 用户ID集合
	 * @return
	 */
	public static Set<String> getOnlineUser(String zhId){
		Map<String, String> paramMap=new HashMap<>();
		paramMap.put("userName",PushMsgHttpUtil.IM_USER_NAME);
		paramMap.put("password",PushMsgHttpUtil.IM_USER_PASSWORD);
		paramMap.put("groupId",zhId);
		Set<String> userIds=new HashSet<>();
		try {
			String resStr=PushMsgHttpUtil.postToImServer("/instant/rbac/getOnlineUsers",paramMap);
			JSONObject resObj=JSONObject.parseObject(resStr);
			if(resObj.containsKey("success") && resObj.getBoolean("success")){
				JSONArray userArrays=resObj.getJSONArray("obj");
				for(int i=0;i<userArrays.size();i++) {
					String uId=userArrays.getString(i);
					userIds.add(uId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userIds;
	}
}
