package com.je.message.thread;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.je.core.constants.message.SendContextType;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
import com.je.message.util.EmailUtil;
import com.je.message.vo.EmailInfoVo;
import com.je.message.vo.EmailMsgVo;

public class OutEmailSendTread extends Thread{
	// TODO Auto-generated method stub
	private EmailMsgVo msgVo;//消息
	private EmailInfoVo emailInfoVo;//邮箱信息
	private String pkValue;//TODO未处理
	private OutEmailSendTread(){};
	public OutEmailSendTread(EmailMsgVo msgVo,EmailInfoVo emailInfoVo,String pkValue){
		this();
		this.msgVo=msgVo;
		this.pkValue=pkValue;
		this.emailInfoVo=emailInfoVo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(msgVo==null){
			return;
		}
		String flag="1";
		try{
			EmailUtil emailUtil=EmailUtil.getInstance();
			if("1".equals(msgVo.getFb())){
				String sjrStr=msgVo.getReceiveEmail();
				if(StringUtil.isNotEmpty(sjrStr)){
					JSONArray arrays=JSONArray.fromObject(sjrStr);
					for(int i=0;i<arrays.size();i++){
						JSONObject infos=arrays.getJSONObject(i);
						JSONArray newArrays=new JSONArray();
						newArrays.add(infos);
						EmailMsgVo emailMsgVo=new EmailMsgVo(newArrays.toString(), msgVo.getSubject(), msgVo.getContextType(), msgVo.getContext(), msgVo.getFileNames(), msgVo.getAddresses());
						emailMsgVo.setMs(msgVo.getMs());
						emailMsgVo.setCs(msgVo.getCs());
						emailMsgVo.setFb(msgVo.getFb());
						emailMsgVo.setFaster(msgVo.getFaster());
						emailMsgVo.setReplySign(msgVo.getReplySign());
						emailUtil.sendEmail(emailMsgVo,emailInfoVo);
					}
				}
			}else{
				emailUtil.sendEmail(msgVo,emailInfoVo);
			}
		}catch(Exception e){
			e.printStackTrace();
			flag="0";
		}
		if(StringUtil.isNotEmpty(pkValue)){
			PCServiceTemplate pcServiceTemplate=SpringContextHolder.getBean("PCServiceTemplateImpl");
			pcServiceTemplate.executeSql("UPDATE JE_SYS_EMAILDATA SET EMAILDATA_TDZT='"+flag+"' WHERE JE_SYS_EMAILDATA_ID='"+pkValue+"'");
		}
	}
}
