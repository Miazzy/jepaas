package com.je.message.thread;

import java.util.Date;
import java.util.List;

import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.DateUtils;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.message.util.EmailUtil;
import com.je.message.vo.EmailMsgVo;
/**
 * 发送Email线程
 * @author zhangshuaipeng
 *
 */
public class EmailSendTread extends Thread{
	private EmailMsgVo msgVo;//消息
	private EmailSendTread(){};
	public EmailSendTread(EmailMsgVo msgVo){
		this();
		this.msgVo=msgVo;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(msgVo==null){
			return;
		}
		DynaBean emailLog=new DynaBean("JE_SYS_EMAILLOG",false);
		emailLog.set(BeanUtils.KEY_PK_CODE, "JE_SYS_EMAILLOG_ID");
		emailLog.set("EMAILLOG_RECEIVEEMAIL", msgVo.getReceiveEmail());
		emailLog.set("EMAILLOG_SUBJECT", msgVo.getSubject());
		emailLog.set("EMAILLOG_CONTEXTTYPE", msgVo.getContextType());
		emailLog.set("EMAILLOG_CONTEXT", msgVo.getContext());
		emailLog.set("EMAILLOG_FL", msgVo.getFl());
		emailLog.set("EMAILLOG_SENDTIME", DateUtils.formatDateTime(new Date()));
		List<String> fileNames=msgVo.getFileNames();
		List<String> fileAddress=msgVo.getAddresses();
		if(fileNames.size()>0 && fileNames.size()==fileAddress.size()){
			String[] files=new String[fileNames.size()];
			for(Integer i=0;i<fileNames.size();i++){
				files[i]=fileNames.get(i)+"*"+fileAddress.get(i);
			}
			emailLog.set("EMAILLOG_FILES", StringUtil.buildSplitString(files, ","));
		}
		try{
			EmailUtil.getInstance().sendEmail(msgVo,null);
			emailLog.set("EMAILLOG_STATUS", "1");
			//成功发送日志
		}catch(Exception e){
			e.printStackTrace();
			emailLog.set("EMAILLOG_STATUS", "0");
			emailLog.set("EMAILLOG_FAILUREINFO", e.getMessage());
		}
		if("1".equals(WebUtils.getSysVar("JE_SYS_EMAILLOG"))){
			PCDynaServiceTemplate serviceTemplate=SpringContextHolder.getBean("PCDynaServiceTemplate");
			serviceTemplate.buildModelCreateInfo(emailLog,msgVo.getCurrentUser());
			serviceTemplate.insert(emailLog);
		}
	}

}
