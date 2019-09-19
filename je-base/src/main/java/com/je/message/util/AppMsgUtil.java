package com.je.message.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.message.vo.app.PayloadInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 手机App推送工具类
 * @author zhangshuaipeng
 *
 */
public class AppMsgUtil {
	private static Logger logger = LoggerFactory.getLogger(AppMsgUtil.class);
	//空的为默认的app
	public static final String appId = "";

	public static void sendMsg(String title,String content, String targetType, String targetValue,String onlySocket, PayloadInfo params) {
		Map<String, String> paramMap = Maps.newHashMap();
		paramMap.put("appId",appId);
		paramMap.put("title",title);
		paramMap.put("content",content);
		paramMap.put("targetType",targetType);
		paramMap.put("targetValue",targetValue);
		paramMap.put("onlySocket",onlySocket);
		paramMap.put("params", JSON.toJSONString(params));
		try {
			 PushMsgHttpUtil.pushMobileMsg(paramMap);
		} catch (Exception e) {
			logger.error("发送手机推送异常,参数:"+JsonBuilder.getInstance().toJson(paramMap),e);
		}

	}


	//	private static AppMsgUtil appMsgUtil = null;
	//
	////	public static String appId = "pDbEYkXyqZ7rfoVKnp1vL9";
	////    public static String appKey = "I7CQdtw2RF8L2A23z97W92";
	////    public static String appSecret = "5OhakCNYOj62yA3Eo4TGa3";
	////    public static String masterSecret = "779oMhtb1W8AwUDGE1Kmx4";
	////    public static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	//    public static String appId = "pDbEYkXyqZ7rfoVKnp1vL9";
	//    public static String appKey = "I7CQdtw2RF8L2A23z97W92";
	//    public static String appSecret = "5OhakCNYOj62yA3Eo4TGa3";
	//    public static String masterSecret = "779oMhtb1W8AwUDGE1Kmx4";
	//    public static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	//    /**缓存当前系统个推的配置信息，  键为app的key*/
	////    public static Map<String,MsgConfigVo> gtConfigs=new HashMap<String,MsgConfigVo>();
	//	private AppMsgUtil(){}
	//	public static AppMsgUtil getInstance(){
	//		if(appMsgUtil==null){
	//			appMsgUtil=new AppMsgUtil();
	//		}
	//		return appMsgUtil;
	//	}
	//	/**
	//	 *  推送消息
	//	 * @param msgVo
	//	 * @param configVo
	//	 */
	//	public DynaBean sendMsg(AppMsgVo msgVo,MsgConfigVo configVo){
	//		DynaBean log=new DynaBean("JE_SYS_GETUILOG",false);
	//		IGtPush push =null;
	//		try{
	//			push=new IGtPush(configVo.getGtAppId(), configVo.getGtAppKey(), configVo.getMaster());
	//		}catch(Exception e){
	//			log.set(BeanUtils.KEY_PK_CODE, "JE_SYS_GETUILOG_ID");
	//			log.set("GETUILOG_TITLE", msgVo.getTitle());
	//			log.set("GETUILOG_CONTENT",msgVo.getContent());
	//			log.set("GETUILOG_TIME", DateUtils.formatDateTime(new Date()));
	//			log.set("GETUILOG_TYPE", configVo.getTargerType());
	//			log.set("GETUILOG_MBZ", configVo.getTargerValue());
	//			log.set("GETUILOG_STATUS", "0");
	//        	log.set("GETUILOG_MSG", "服务器响应异常");
	//        	return log;
	//		}
	//		//平台apkid
	//		String apkKey=configVo.getApkKey();
	//		JsonAssist jsonAssist=JsonAssist.getInstance();
	//		log.set(BeanUtils.KEY_PK_CODE, "JE_SYS_GETUILOG_ID");
	//		log.set("GETUILOG_TITLE", msgVo.getTitle());
	//		log.set("GETUILOG_CONTENT",msgVo.getContent());
	//		log.set("GETUILOG_TIME", DateUtils.formatDateTime(new Date()));
	//		log.set("GETUILOG_TYPE", configVo.getTargerType());
	//		log.set("GETUILOG_MBZ", configVo.getTargerValue());
	//		if(AppSendType.USERID.equals(configVo.getTargerType()) || AppSendType.USERIDS.equals(configVo.getTargerType())){
	//			ListMessage message = new ListMessage();
	//			message.setOffline(true);
	//			message.setOfflineExpireTime(msgVo.getExpireTime());
	//			if(msgVo.getWifi()){
	//	    		message.setPushNetWorkType(1);
	//	    	}else{
	//	    		message.setPushNetWorkType(0);
	//	    	}
	//			TransmissionTemplate template=transmissionTemplate(msgVo);
	////			NotificationTemplate template=notificationTemplate(msgVo);
	//			template.setAppId(configVo.getGtAppId());
	//			template.setAppkey(configVo.getGtAppKey());
	//			JSONObject returnObj=new JSONObject();
	//			PayloadInfo payload=msgVo.getPayload();
	//			if(payload==null){
	//				payload=new PayloadInfo();
	//			}
	//			//声明 _cfg
	//			JSONObject _cfg=new JSONObject();
	//			_cfg.put("apkCode", configVo.getApkKey());
	//			_cfg.put("type", configVo.getTargerType());
	//			_cfg.put("createMsg",StringUtil.isNotEmpty(msgVo.getContent()));
	//			JSONObject params=new JSONObject();
	//			if(payload.getParams()==null){
	//				payload.setParams(params);
	//			}
	//			if(StringUtil.isNotEmpty(msgVo.getContent())){
	//				returnObj.put("title", msgVo.getTitle());
	//				returnObj.put("content", msgVo.getContent());
	//			}
	//			//开始推送
	//			if(AppSendType.USERID.equals(configVo.getTargerType())){
	//				String userId=configVo.getTargerValue()+"";
	//				_cfg.put("userId", userId);
	//				payload.set_cfg(_cfg);
	//				returnObj.put("payload", payload);
	//				template.setTransmissionContent(returnObj.toString());
	//				APNPayload apnPayload=apnPayload(msgVo, jsonAssist.buildModelJson(payload));
	//				template.setAPNInfo(apnPayload);
	//				message.setData(template);
	//				List<Target> targets=getUserTargers(userId, apkKey, configVo);
	//				log.set("GETUILOG_PAYLOAD", returnObj.toString());
	//				if(targets.size()<=0){
	//					System.out.println("未找到该用户个推信息!");
	//					log.set("GETUILOG_STATUS", "0");
	//					log.set("GETUILOG_MSG", "未找到该用户个推信息!");
	//					return log;
	//				}
	//				String taskId = push.getContentId(message);
	//		        IPushResult ret = push.pushMessageToList(taskId, targets);
	//		        //得到推送结果
	//		        if(ret!=null){
	//		        	log.set("GETUILOG_STATUS", "1");
	//		        	log.set("GETUILOG_MSG", ret.getResponse().toString());
	//		        	//System.out.println(ret.getResponse().toString());
	//		        }else{
	//		        	log.set("GETUILOG_STATUS", "0");
	//		        	log.set("GETUILOG_MSG", "服务器响应异常");
	//		        	 System.out.println("服务器响应异常");
	//		        }
	//		        return log;
	//			}else{
	//				String[] userIds=(configVo.getTargerValue()+"").split(",");
	//				for(String userId:userIds){
	//					_cfg.put("userId", userId);
	//					payload.set_cfg(_cfg);
	//					returnObj.put("payload", payload);
	//					template.setTransmissionContent(returnObj.toString());
	//					APNPayload apnPayload=apnPayload(msgVo, jsonAssist.buildModelJson(payload));
	//					template.setAPNInfo(apnPayload);
	//					message.setData(template);
	//					List<Target> targets=getUserTargers(userId, apkKey, configVo);
	//					if(targets.size()<=0){
	//						System.out.println("未找到该用户个推信息!");
	//						continue;
	//					}
	//					String taskId = push.getContentId(message);
	//			        IPushResult ret = push.pushMessageToList(taskId, targets);
	//			        //得到推送结果
	//			        if(ret!=null){
	//			        	if("0".equals(log.getStr("STATUS"))){
	//				        	log.set("GETUILOG_STATUS", "1");
	//				        	log.set("GETUILOG_MSG", ret.getResponse().toString());
	//				        	//System.out.println(ret.getResponse().toString());
	//			        	}
	//			        }else{
	//			        	log.set("GETUILOG_STATUS", "0");
	//			        	log.set("GETUILOG_MSG", "服务器响应异常");
	//			        	System.out.println("服务器响应异常");
	//			        }
	//				}
	//			}
	//			return log;
	//		}else if(AppSendType.TAG.equals(configVo.getTargerType()) || AppSendType.ALL.equals(configVo.getTargerType())){
	//			AppMessage message = new AppMessage();
	//			message.setOffline(true);
	//			message.setOfflineExpireTime(msgVo.getExpireTime());
	//			TransmissionTemplate template=transmissionTemplate(msgVo);
	//			template.setAppId(configVo.getGtAppId());
	//			template.setAppkey(configVo.getGtAppKey());
	//			if(msgVo.getWifi()){
	//	    		message.setPushNetWorkType(1);
	//	    	}else{
	//	    		message.setPushNetWorkType(0);
	//	    	}
	//			PayloadInfo payload=msgVo.getPayload();
	//			if(payload==null){
	//				payload=new PayloadInfo();
	//			}
	//			JSONObject returnObj=new JSONObject();
	//			//声明 _cfg
	//			JSONObject _cfg=new JSONObject();
	//			_cfg.put("apkCode", configVo.getApkKey());
	//			_cfg.put("type", configVo.getTargerType());
	//			_cfg.put("tag", configVo.getTargerValue());
	//			_cfg.put("createMsg",StringUtil.isNotEmpty(msgVo.getContent()));
	//			JSONObject params=new JSONObject();
	//			if(payload.getParams()==null){
	//				payload.setParams(params);
	//			}
	//			returnObj.put("payload", payload);
	//			if(StringUtil.isNotEmpty(msgVo.getContent())){
	//				returnObj.put("title", msgVo.getTitle());
	//				returnObj.put("content", msgVo.getContent());
	//			}
	//			template.setTransmissionContent(returnObj.toString());
	//			APNPayload apnPayload=apnPayload(msgVo, jsonAssist.buildModelJson(payload));
	//			template.setAPNInfo(apnPayload);
	//			message.setData(template);;
	//			//设置个推appid
	//			List appIdList = new ArrayList();
	//        	appIdList.add(configVo.getGtAppId());
	//        	message.setAppIdList(appIdList);
	//        	AppConditions cdt= new AppConditions();
	//        	List<String> tags=new ArrayList<String>();
	//        	if(AppSendType.ALL.equals(configVo.getTargerType())){
	//        		tags.add(apkKey);
	//        	}else{
	//        		String[] tagVals=(configVo.getTargerValue()+"").split(",");
	//        		for(String tagVal:tagVals){
	//        			tags.add(tagVal);
	//        		}
	//        	}
	//        	//可设置手机机型，androd  ios     设置手机省份
	////        	cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
	////        	cdt.addCondition(AppConditions.REGION, provinceList);
	//        	if(tags.size() == 0){
	//        		log.set("GETUILOG_STATUS", "0");
	//        		log.set("GETUILOG_MSG", "传入标签不能为空!");
	//        		System.out.println("传入标签不能为空!");
	//        		return log;
	//        	}
	//        	cdt.addCondition(AppConditions.TAG, tags);
	////        	cdt.addCondition(AppConditions.PHONE_TYPE, new ArrayList<String>());
	////        	cdt.addCondition(AppConditions.REGION, new ArrayList<String>());
	//        	message.setConditions(cdt);
	//        	//message.setSpeed(1000);
	//        	//开始推送
	//        	IPushResult ret = push.pushMessageToApp(message);
	//        	//得到推送结果
	//        	if(ret!=null){
	//	        	log.set("GETUILOG_STATUS", "1");
	//	        	log.set("GETUILOG_MSG", ret.getResponse().toString());
	//	        	System.out.println(ret.getResponse().toString());
	//	        }else{
	//	        	log.set("GETUILOG_STATUS", "0");
	//	        	log.set("GETUILOG_MSG", "服务器响应异常");
	//	        	System.out.println("服务器响应异常");
	//	        }
	//        	return log;
	//		}
	//		log.set("GETUILOG_STATUS", "0");
	//		log.set("GETUILOG_MSG", "暂不支持推送类型!");
	//		return log;
	//	}
	//	/**
	//	 * 得到指定用户推送目标
	//	 * @param userId
	//	 * @param apkKey
	//	 * @param configVo
	//	 * @return
	//	 */
	//	public List<Target> getUserTargers(String userId,String apkKey,MsgConfigVo configVo){
	//		String key=apkKey+"_"+userId;
	//		List<UserAppInfo> userAppInfos=GtUserCacheManager.getCacheValue(key);
	//		if(!(userAppInfos!=null && userAppInfos.size()>0)){
	//			PCDynaServiceTemplate serviceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
	//			List<DynaBean> yhs=serviceTemplate.selectList("JE_SYS_GTYH", " AND GTYH_USERID='"+userId+"' AND GTYH_APKKEY='"+apkKey+"'");
	//			String[] yhIds=ArrayUtils.getBeanFieldArray(yhs, "JE_SYS_GTYH_ID");
	//			List<DynaBean> gts=serviceTemplate.selectList("JE_SYS_GTPUSH", " AND JE_SYS_GTYH_ID IN ("+StringUtil.buildArrayToString(yhIds)+")");
	//			userAppInfos=new ArrayList<UserAppInfo>();
	//			for(DynaBean gt:gts){
	//				String yhId=gt.getStr("JE_SYS_GTYH_ID");
	//				for(DynaBean yh:yhs){
	//					if(yhId.equals(yh.getStr("JE_SYS_GTYH_ID"))){
	//						UserAppInfo info=new UserAppInfo(userId,yh.getStr("GTYH_APKMC"),yh.getStr("GTYH_APKID"),yh.getStr("GTYH_APKKEY"),gt.getStr("GTPUSH_CID"),gt.getStr("GTPUSH_TOKEN"),gt.getStr("GTPUSH_TYPE"));
	//						userAppInfos.add(info);
	//					}
	//				}
	//			}
	//			GtUserCacheManager.putCache(key, userAppInfos);
	//		}
	//		List<Target> targets=new ArrayList<Target>();
	//		for(UserAppInfo userAppInfo:userAppInfos){
	//			Target target = new Target();
	//			target.setAppId(configVo.getGtAppId());
	//			target.setClientId(userAppInfo.getcId());
	//			targets.add(target);
	//		}
	//		return targets;
	//	}
	//	/**
	//	 * 构建透传消息的对象
	//	 * @param msgVo
	//	 * @return
	//	 */
	//    public TransmissionTemplate transmissionTemplate(AppMsgVo msgVo) {
	//        TransmissionTemplate template = new TransmissionTemplate();
	//        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	//        template.setTransmissionType(1);
	//        // 内容
	//        //ios
	//        if(msgVo.getWaitStart()){
	//      	   template.setTransmissionType(2);
	//         }else{
	//      	   template.setTransmissionType(1);
	//         }
	//        return template;
	//    }
	//    public APNPayload apnPayload(AppMsgVo msgVo,String payloadStr){
	//    	APNPayload payload = new APNPayload();
	//        //在已有数字基础上加1显示，设置为-1 时，在已有数字上减1显示，设置为数字时，显示指定数字  +1
	//        if(StringUtil.isNotEmpty(msgVo.getAutoBadge()) && !"no".equals(msgVo.getAutoBadge())){
	//        	payload.setAutoBadge(msgVo.getAutoBadge());
	//        }
	//
	//        // 设置打开的网址地址
	//        payload.setContentAvailable(0);//设置成0，可以解决手机端多次提醒
	//        payload.setSound("default");
	//        payload.setCategory("$由客户端定义");
	//        payload.addCustomMsg("payload",payloadStr);
	//        //简单模式APNPayload.SimpleMsg
	//        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
	//        if(StringUtil.isNotEmpty(msgVo.getContent())){
	//	        alertMsg.setBody(msgVo.getContent());
	//	        alertMsg.setTitle(msgVo.getTitle());
	//        }
	////        alertMsg.setActionLocKey("ActionLockey");
	////        alertMsg.setLocKey("LocKey");
	////        alertMsg.addLocArg("loc-args");
	////        alertMsg.setLaunchImage("launch-image");
	//        // iOS8.2以上版本支持
	////        alertMsg.setTitleLocKey("TitleLocKey");
	////        alertMsg.addTitleLocArg("TitleLocArg");
	//        payload.setAlertMsg(alertMsg);
	//        return payload;
	//    }
	//    public NotificationTemplate notificationTemplate(AppMsgVo msgVo){
	//    	NotificationTemplate template = new NotificationTemplate();
	//    	 template.setTitle(msgVo.getTitle());
	//         template.setText(msgVo.getContent());
	//         template.setIsRing(true);
	//         template.setIsVibrate(true);
	//         template.setIsClearable(true);
	//         template.setTransmissionType(2);
	//         return template;
	//    }
	//    /**
	//     * 设置标签
	//     * @param msgConfigVo
	//     * @param cId
	//     * @param tags
	//     * @param addTag   增加Tag   false就是覆盖当前cid的标签
	//     */
	//    public DynaBean setTag(MsgConfigVo msgConfigVo,String cId,String tags,Boolean addTag){
	//		 IGtPush push = new IGtPush(msgConfigVo.getHost(), msgConfigVo.getGtAppKey(), msgConfigVo.getMaster());
	//		 PushResult result=(PushResult) push.getUserTags(msgConfigVo.getGtAppId(),cId);
	//		 Map<String, Object> response=result.getResponse();
	//		DynaBean log=new DynaBean("JE_SYS_GETUILOG",false);
	//		log.set(BeanUtils.KEY_PK_CODE, "JE_SYS_GETUILOG_ID");
	//		log.set("GETUILOG_TITLE", "设置用户TAG");
	//		log.set("GETUILOG_CONTENT","TAG值："+tags);
	//		log.set("GETUILOG_TIME", DateUtils.formatDateTime(new Date()));
	//		log.set("GETUILOG_TYPE", AppSendType.CID);
	//		log.set("GETUILOG_MBZ", cId);
	//		String userTag=(String) response.get("tags");
	//		List<String> tagVals=new ArrayList<String>();
	//		boolean setTag=true;
	//		if(addTag){
	//			if(StringUtil.isNotEmpty(userTag)){
	//				for(String t:userTag.split(" ")){
	//					if(StringUtil.isNotEmpty(t)){
	//						tagVals.add(t);
	//					}
	//				}
	//			}
	//			int oldSize=tagVals.size();
	//			for(String t:tags.split(",")){
	//				if(StringUtil.isNotEmpty(t) && !tagVals.contains(t)){
	//					tagVals.add(t);
	//				}
	//			}
	//			if(tagVals.size()>oldSize){
	//				//需要设定tag
	//			}else{
	//				setTag=false;
	//				log.set("GETUILOG_STATUS", "0");
	//				log.set("GETUILOG_MSG", "已存在该tag!");
	//			}
	//		}else{
	//			for(String t:tags.split(",")){
	//				if(StringUtil.isNotEmpty(t)){
	//					tagVals.add(t);
	//				}
	//			}
	//		}
	//		if(setTag){
	//			 IQueryResult rt=push.setClientTag(msgConfigVo.getGtAppId(), cId, tagVals);
	//			 if(rt!=null){
	//				 //System.out.println(rt.getResponse().toString());
	//				 log.set("GETUILOG_MSG", rt.getResponse().toString());
	//			 }else{
	//				 	log.set("GETUILOG_STATUS", "0");
	//		        	log.set("GETUILOG_MSG", "服务器响应异常");
	//		        	System.out.println("服务器响应异常");
	//			 }
	//			 return log;
	//		}else{
	//			return log;
	//		}
	//	}
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//	/**
	//	 * 对单体用户推送
	//	 * @param msgConfigVo
	//	 * @param inFormMsgVo
	//	 */
	//	public void sendMsg(MsgConfigVo msgConfigVo,InFormMsgVo inFormMsgVo){
	//		//初始化信息模版
	//		NotificationTemplate template = new NotificationTemplate();
	//        // 设置APPID与APPKEY
	//        template.setAppId(msgConfigVo.getAppId());
	//        template.setAppkey(msgConfigVo.getAppKey());
	//        // 设置通知栏标题与内容
	//        template.setTitle(inFormMsgVo.getTitle());
	//        template.setText(inFormMsgVo.getContext());
	//        // 配置通知栏图标
	//        template.setLogo(inFormMsgVo.getLogo());
	//        // 配置通知栏网络图标，填写图标URL地址
	//        template.setLogoUrl("");
	//        // 设置通知是否响铃，震动，或者可清除
	//        template.setIsRing(inFormMsgVo.getRing());
	//        template.setIsVibrate(inFormMsgVo.getVibrate());
	//        template.setIsClearable(true);
	//        // 设置打开的网址地址
	//       if(inFormMsgVo.getWaitStart()){
	//    	   template.setTransmissionType(2);
	//       }else{
	//    	   template.setTransmissionType(1);
	//       }
	//       template.setTransmissionContent(inFormMsgVo.getMsgContext());
	//       APNPayload payload = new APNPayload();
	//       payload.setBadge(0);
	//       payload.setContentAvailable(1);
	//       payload.setSound("default");
	////       payload.setCategory("$由客户端定义");
	//       payload.setAlertMsg(new APNPayload.SimpleAlertMsg(inFormMsgVo.getTitle()));
	//       template.setAPNInfo(payload);
	//        IGtPush push = new IGtPush(msgConfigVo.getHost(), msgConfigVo.getAppKey(), msgConfigVo.getMaster());
	//        if(AppSendType.USER.equals(msgConfigVo.getTargerType()) || AppSendType.ALIAS.equals(msgConfigVo.getTargerType())){
	//
	//        	SingleMessage message = new SingleMessage();
	//        	message.setOffline(true);
	//        	message.setOfflineExpireTime(msgConfigVo.getExpireTime());
	//        	message.setData(template);
	//        	if(msgConfigVo.getWifi()){
	//        		message.setPushNetWorkType(1);
	//        	}else{
	//        		message.setPushNetWorkType(0);
	//        	}
	//        	Target target = new Target();
	//        	target.setAppId(msgConfigVo.getAppId());
	//        	if(AppSendType.USER.equals(msgConfigVo.getTargerType())){
	//                target.setClientId(msgConfigVo.getTargerValue()+"");
	//        	}else{
	//                target.setAlias(msgConfigVo.getTargerValue()+"");
	//        	}
	//	        IPushResult ret = null;
	//	        try{
	//	            ret = push.pushMessageToSingle(message, target);
	//	        }catch(RequestException e){
	//	            e.printStackTrace();
	//	            ret = push.pushMessageToSingle(message, target, e.getRequestId());
	//	        }
	//	        if(ret != null){
	////	            System.out.println(ret.getResponse().toString());
	//	        }else{
	//	            System.out.println("服务器响应异常");
	//	        }
	//
	//	     //标签和所有用户推送
	//        }else{
	//        	AppMessage message = new AppMessage();
	//        	message.setData(template);
	//        	message.setOffline(true);
	//        	message.setOfflineExpireTime(msgConfigVo.getExpireTime());
	//        	List appIdList = new ArrayList();
	//        	appIdList.add(msgConfigVo.getAppId());
	//        	message.setAppIdList(appIdList);
	//            //设置机型
	//            if(AppSendType.TAG.equals(msgConfigVo.getTargerType())){
	//              List tagList = new ArrayList();
	//              //机型
	//            List phoneTypeList = new ArrayList();
	//            phoneTypeList.add("ANDROID");
	//            phoneTypeList.add("IOS");
	//              //省份
	////            List provinceList = new ArrayList();
	//            	List<String> tags=(List<String>) msgConfigVo.getTargerValue();
	//            	for(String tag:tags){
	//            		tagList.add(tag);
	//            	}
	//            	AppConditions cdt= new AppConditions();
	//            	cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
	////                cdt.addCondition(AppConditions.REGION, provinceList);
	//                cdt.addCondition(AppConditions.TAG,tagList);
	//                message.setConditions(cdt);
	//            }
	//            if(msgConfigVo.getWifi()){
	//        		message.setPushNetWorkType(1);
	//        	}else{
	//        		message.setPushNetWorkType(0);
	//        	}
	//            //message.setSpeed(1000);
	//            IPushResult ret = push.pushMessageToApp(message);
	////            System.out.println(ret.getResponse().toString());
	//        }
	//	}
	//	/**
	//	 * 发送打开网页消息
	//	 * @param msgConfigVo
	//	 * @param webPageMsgVo
	//	 */
	//	public void sendMsg(MsgConfigVo msgConfigVo,WebPageMsgVo webPageMsgVo){
	//		//初始化信息模版
	//		LinkTemplate template = new LinkTemplate();
	//        // 设置APPID与APPKEY
	//        template.setAppId(msgConfigVo.getAppId());
	//        template.setAppkey(msgConfigVo.getAppKey());
	//        // 设置通知栏标题与内容
	//        template.setTitle(webPageMsgVo.getTitle());
	//        template.setText(webPageMsgVo.getContext());
	//        // 配置通知栏图标
	//        template.setLogo(webPageMsgVo.getLogo());
	//        // 配置通知栏网络图标，填写图标URL地址
	//        template.setLogoUrl("");
	//        // 设置通知是否响铃，震动，或者可清除
	//        template.setIsRing(webPageMsgVo.getRing());
	//        template.setIsVibrate(webPageMsgVo.getVibrate());
	//        template.setIsClearable(true);
	//        // 设置打开的网址地址
	//        template.setUrl(webPageMsgVo.getUrl());
	//        IGtPush push = new IGtPush(msgConfigVo.getHost(), msgConfigVo.getAppKey(), msgConfigVo.getMaster());
	//        if(AppSendType.USER.equals(msgConfigVo.getTargerType()) || AppSendType.ALIAS.equals(msgConfigVo.getTargerType())){
	//        	SingleMessage message = new SingleMessage();
	//        	message.setOffline(true);
	//        	message.setOfflineExpireTime(msgConfigVo.getExpireTime());
	//        	message.setData(template);
	//        	if(msgConfigVo.getWifi()){
	//        		message.setPushNetWorkType(1);
	//        	}else{
	//        		message.setPushNetWorkType(0);
	//        	}
	//        	Target target = new Target();
	//        	target.setAppId(msgConfigVo.getAppId());
	//        	if(AppSendType.USER.equals(msgConfigVo.getTargerType())){
	//                target.setClientId(msgConfigVo.getTargerValue()+"");
	//        	}else{
	//                target.setAlias(msgConfigVo.getTargerValue()+"");
	//        	}
	//	        IPushResult ret = null;
	//	        try{
	//	            ret = push.pushMessageToSingle(message, target);
	//	        }catch(RequestException e){
	//	            e.printStackTrace();
	//	            ret = push.pushMessageToSingle(message, target, e.getRequestId());
	//	        }
	//	        if(ret != null){
	////	            System.out.println(ret.getResponse().toString());
	//	        }else{
	//	            System.out.println("服务器响应异常");
	//	        }
	//	     //标签和所有用户推送
	//        }else{
	//        	AppMessage message = new AppMessage();
	//        	message.setData(template);
	//        	message.setOffline(true);
	//        	message.setOfflineExpireTime(msgConfigVo.getExpireTime());
	//        	List appIdList = new ArrayList();
	//        	appIdList.add(msgConfigVo.getAppId());
	//        	message.setAppIdList(appIdList);
	//            //设置机型
	//            if(AppSendType.TAG.equals(msgConfigVo.getTargerType())){
	//              List tagList = new ArrayList();
	//              //机型
	//            List phoneTypeList = new ArrayList();
	//            phoneTypeList.add("ANDROID");
	//            phoneTypeList.add("IOS");
	//              //省份
	////            List provinceList = new ArrayList();
	//            	List<String> tags=(List<String>) msgConfigVo.getTargerValue();
	//            	for(String tag:tags){
	//            		tagList.add(tag);
	//            	}
	//            	AppConditions cdt= new AppConditions();
	//            	cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
	////                cdt.addCondition(AppConditions.REGION, provinceList);
	//                cdt.addCondition(AppConditions.TAG,tagList);
	//                message.setConditions(cdt);
	//            }
	//            if(msgConfigVo.getWifi()){
	//        		message.setPushNetWorkType(1);
	//        	}else{
	//        		message.setPushNetWorkType(0);
	//        	}
	//            //message.setSpeed(1000);
	//            IPushResult ret = push.pushMessageToApp(message);
	////            System.out.println(ret.getResponse().toString());
	//        }
	//	}
	//	public void setTag(MsgConfigVo msgConfigVo,String cId,List<String> tags){
	//		 IGtPush push = new IGtPush(msgConfigVo.getHost(), msgConfigVo.getAppKey(), msgConfigVo.getMaster());
	//		 PushResult result=(PushResult) push.getUserTags(msgConfigVo.getAppId(),cId);
	//		 Map<String, Object> response=result.getResponse();
	//		String userTag=(String) response.get("tags");
	//		if(StringUtil.isNotEmpty(userTag)){
	//			List<String> resTags=new ArrayList<String>();
	//			String[] ts=userTag.split(" ");
	//			for(String t:tags){
	//				if(!ArrayUtils.contains(ts, t)){
	//					resTags.add(t);
	//				}
	//			}
	//			if(resTags.size()>0){
	//				IQueryResult rt=push.setClientTag(msgConfigVo.getAppId(), cId, resTags);
	//				System.out.println(rt.getResponse().toString());
	//			}
	//		}else{
	//			IQueryResult rt=push.setClientTag(msgConfigVo.getAppId(), cId, tags);
	//			System.out.println(rt.getResponse().toString());
	//		}
	//	}
	//	public void sendApns(MsgConfigVo msgConfigVo,InFormMsgVo inFormMsgVo){
	//		//初始化信息模版
	//		TransmissionTemplate template = new TransmissionTemplate();
	//        // 设置APPID与APPKEY
	//        template.setAppId(msgConfigVo.getAppId());
	//        template.setAppkey(msgConfigVo.getAppKey());
	//        // 设置通知栏标题与内容
	////        template.setTitle(inFormMsgVo.getTitle());
	//        template.setTransmissionContent(inFormMsgVo.getContext());
	//        //等待启动
	//        if(inFormMsgVo.getWaitStart()){
	//     	   template.setTransmissionType(2);
	//        }else{
	//     	   template.setTransmissionType(1);
	//        }
	//        APNPayload payload = new APNPayload();
	//        payload.setBadge(0);
	//        payload.setContentAvailable(1);
	//        payload.setSound("default");
	////        payload.setCategory("$由客户端定义");
	//        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(inFormMsgVo.getTitle()));
	//        template.setAPNInfo(payload);
	//        IGtPush push = new IGtPush(msgConfigVo.getHost(), msgConfigVo.getAppKey(), msgConfigVo.getMaster());
	//        if(AppSendType.USER.equals(msgConfigVo.getTargerType()) || AppSendType.ALIAS.equals(msgConfigVo.getTargerType())){
	//        	SingleMessage message = new SingleMessage();
	//        	message.setOffline(true);
	//        	message.setOfflineExpireTime(msgConfigVo.getExpireTime());
	//        	message.setData(template);
	//        	if(msgConfigVo.getWifi()){
	//        		message.setPushNetWorkType(1);
	//        	}else{
	//        		message.setPushNetWorkType(0);
	//        	}
	//        	Target target = new Target();
	//        	target.setAppId(msgConfigVo.getAppId());
	//        	if(AppSendType.USER.equals(msgConfigVo.getTargerType())){
	//                target.setClientId(msgConfigVo.getTargerValue()+"");
	//        	}else{
	//                target.setAlias(msgConfigVo.getTargerValue()+"");
	//        	}
	//	        IPushResult ret = null;
	//	        try{
	//	            ret = push.pushMessageToSingle(message, target);
	//	        }catch(RequestException e){
	//	            e.printStackTrace();
	//	            ret = push.pushMessageToSingle(message, target, e.getRequestId());
	//	        }
	//	        if(ret != null){
	////	            System.out.println(ret.getResponse().toString());
	//	        }else{
	//	            System.out.println("服务器响应异常");
	//	        }
	//	     //标签和所有用户推送
	//        }else{
	//        	AppMessage message = new AppMessage();
	//        	message.setData(template);
	//        	message.setOffline(true);
	//        	message.setOfflineExpireTime(msgConfigVo.getExpireTime());
	//        	List appIdList = new ArrayList();
	//        	appIdList.add(msgConfigVo.getAppId());
	//        	message.setAppIdList(appIdList);
	//            //设置机型
	//            if(AppSendType.TAG.equals(msgConfigVo.getTargerType())){
	//              List tagList = new ArrayList();
	//              //机型
	//            List phoneTypeList = new ArrayList();
	//            phoneTypeList.add("ANDROID");
	//            phoneTypeList.add("IOS");
	//              //省份
	////            List provinceList = new ArrayList();
	//            	List<String> tags=(List<String>) msgConfigVo.getTargerValue();
	//            	for(String tag:tags){
	//            		tagList.add(tag);
	//            	}
	//            	AppConditions cdt= new AppConditions();
	//            	cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
	////                cdt.addCondition(AppConditions.REGION, provinceList);
	//                cdt.addCondition(AppConditions.TAG,tagList);
	//                message.setConditions(cdt);
	//            }
	//            if(msgConfigVo.getWifi()){
	//        		message.setPushNetWorkType(1);
	//        	}else{
	//        		message.setPushNetWorkType(0);
	//        	}
	//            //message.setSpeed(1000);
	//            IPushResult ret = push.pushMessageToApp(message);
	////            System.out.println(ret.getResponse().toString());
	//        }
	//	}
}
