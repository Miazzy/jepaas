package com.je.message.thread;

import java.util.Date;

import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.DateUtils;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.message.util.NoteUtil;
import com.je.message.vo.NoteMsgVo;
/**
 * 发送短信线程
 * @author zhangshuaipeng
 *
 */
public class NoteSendTread extends Thread{
	private NoteMsgVo msgVo;//消息
	private NoteSendTread() {}
	public NoteSendTread(NoteMsgVo msgVo) {
		this();
		this.msgVo = msgVo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(msgVo==null){
			return;
		}
		if(StringUtil.isEmpty(msgVo.getPhoneNumber())){
			return;
		}
		PCDynaServiceTemplate serviceTemplate=null;
		NoteUtil noteUtil=NoteUtil.getInstance();
		//格式化关键字
		String sendContext=noteUtil.formatSendText(msgVo.getContext());
		//取出当前天规定一共发送多少条短信
		Integer count=Integer.parseInt(StringUtil.getDefaultValue(WebUtils.getSysVar("JE_SYS_NOTE_COUNT"),"10000"));
		DynaBean noteLog=new DynaBean("JE_SYS_NOTELOG",false);
		noteLog.set(BeanUtils.KEY_PK_CODE, "JE_SYS_NOTELOG_ID");
		noteLog.set("NOTELOG_FROMUSER", msgVo.getFromUser());
		noteLog.set("NOTELOG_FROMUSERID", msgVo.getFromUserId());
		noteLog.set("NOTELOG_TOUSER", msgVo.getToUser());
		noteLog.set("NOTELOG_TOUSERID", msgVo.getToUserId());
		noteLog.set("NOTELOG_SENDTIME", DateUtils.formatDateTime(new Date()));
		noteLog.set("NOTELOG_CONTEXT", sendContext);
		noteLog.set("NOTELOG_FL", msgVo.getFl());
		noteLog.set("NOTELOG_PHONENUMBER", msgVo.getPhoneNumber());
		Integer result=noteUtil.sendNote(msgVo.getPhoneNumber(), sendContext,StringUtil.getDefaultValue(msgVo.getJtgsId(),msgVo.getCurrentUser().getJtgsId()));
		if(result>0){
			noteLog.set("NOTELOG_STATUS", "1");
			noteLog.set("NOTELOG_SENDCOUNT", result);
			NoteUtil.send_count+=result;
		}else{
			noteLog.set("NOTELOG_STATUS", "0");
			noteLog.set("NOTELOG_FAILUREINFO",noteUtil.formatSendError(result));
		}
		if("1".equals(WebUtils.getSysVar("JE_SYS_NOTELOG"))){
			serviceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
//			serviceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
			serviceTemplate.buildModelCreateInfo(noteLog,msgVo.getCurrentUser());
//			if(msgVo.getCurrentUser()!=null){
//				noteLog.set("SY_JTGSBM", msgVo.getCurrentUser().getJtgsDm());
//			}
//			if(StringUtil.isNotEmpty(msgVo.getJtgsId())){
//				noteLog.set("SY_JTGSID", msgVo.getJtgsId());
//			}
//			if(StringUtil.isEmpty(noteLog.getStr("SY_JTGSID"))){
//				noteLog.set("SY_JTGSID", "zD9Eqjf2iWWnryJeWB1");
//				noteLog.set("SY_JTGSMC", "某某公司");
//				noteLog.set("SY_JTGSBM", "JE");
//			}
//			serviceTemplate.buildModelCreateInfo(noteLog);
			serviceTemplate.insert(noteLog);
		}
	}
}
