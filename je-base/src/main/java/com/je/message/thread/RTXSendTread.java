package com.je.message.thread;

import java.util.Date;
import java.util.Map;

import rtx.RTXSvrApi;

import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.DateUtils;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.message.vo.RTXMsgVo;

public class RTXSendTread extends Thread {
	private RTXMsgVo msgVo;//消息
	private RTXSendTread(){}
	public RTXSendTread(RTXMsgVo msgVo){
		this();
		this.msgVo=msgVo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(msgVo==null){
			return;
		}
		DynaBean rtxLog=new DynaBean("JE_SYS_RTXLOG",false);
		rtxLog.set(BeanUtils.KEY_PK_CODE, "JE_SYS_RTXLOG_ID");
		rtxLog.set("RTXLOG_RTXUSER", msgVo.getRtxUser());
		rtxLog.set("RTXLOG_FASTTYPE", msgVo.getFastType());
		rtxLog.set("RTXLOG_TITLE", msgVo.getTitle());
		rtxLog.set("RTXLOG_CONTEXT", msgVo.getContext());
		rtxLog.set("RTXLOG_SENDTIME", DateUtils.formatDateTime(new Date()));
		rtxLog.set("RTXLOG_SENDUSER", msgVo.getFromUserName());
		rtxLog.set("RTXLOG_SENDDEPT", msgVo.getFromDeptName());
		rtxLog.set("RTXLOG_DELAYTIME", msgVo.getDelayTime());
		try{
		RTXSvrApi rtxApi=new RTXSvrApi();
			if(rtxApi.Init()){
//				Map<String,String> sysVars=WebUtils.getSysVar(msgVo.getJtgsId());
				rtxApi.setServerIP(WebUtils.getSysVar("JE_SYS_RTXIP"));
				rtxApi.setServerPort(Integer.parseInt(WebUtils.getSysVar("JE_SYS_RTXPORT")));
				StringBuffer context=new StringBuffer(msgVo.getContext());
				context.append("\n\n\n");
				context.append("-----------------------------------\n");
				if(StringUtil.isNotEmpty(msgVo.getFromUserName())){
					context.append("发送人:").append(msgVo.getFromUserName());
				}
				if(StringUtil.isNotEmpty(msgVo.getFromDeptName())){
					context.append("\t 发送部门:").append(msgVo.getFromDeptName());
				}
				Integer result=rtxApi.sendNotify(msgVo.getRtxUser(), msgVo.getTitle(), context.toString(), msgVo.getFastType(), msgVo.getDelayTime().toString());
				
				if(result!=0){
					rtxLog.set("RTXLOG_STATUS", "0");
					rtxLog.set("RTXLOG_FAILUREINFO", "RTX错误代码："+result);
				}else{
					rtxLog.set("RTXLOG_STATUS", "1");
				}
				rtxApi.UnInit();
			}else{
				rtxApi.UnInit();
			}
		}catch (Exception e){
			e.printStackTrace();
			rtxLog.set("RTXLOG_STATUS", "0");
			rtxLog.set("RTXLOG_FAILUREINFO", e.getMessage());
		}
		if("1".equals(WebUtils.getSysVar("JE_SYS_RTXLOG"))){
			PCDynaServiceTemplate serviceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
			serviceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
			serviceTemplate.buildModelCreateInfo(rtxLog,msgVo.getCurrentUser());
			if(StringUtil.isNotEmpty(msgVo.getJtgsId())){
				rtxLog.set("SY_JTGSID", msgVo.getJtgsId());
			}
			serviceTemplate.insert(rtxLog);
		}
	}

}
