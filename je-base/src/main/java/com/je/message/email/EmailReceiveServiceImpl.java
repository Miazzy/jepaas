package com.je.message.email;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import com.je.cache.service.message.RunEmailCacheManager;
import com.je.document.service.DocumentManager;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.DateUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.message.service.DwrManager;
import com.je.message.thread.EmailReceiveTread;
import com.je.message.util.EmailUtil;
import com.je.message.vo.EmailInfoVo;

/**
 * 接受邮件服务层(不含事务控制)
 * @author zhangshuaipeng
 *
 */
@Component("emailReceiveService")
public class EmailReceiveServiceImpl implements EmailReceiveService{
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;
	@Autowired
	private PCServiceTemplate pcServiceTemplate;
	@Autowired
	private DwrManager dwrManager;
	@Autowired
	private DocumentManager documentManager;
	@Override
	public void receive(EmailInfoVo emailInfoVo, JSONObject returnObj,String type,Boolean dwrFlag) {
		EmailUtil emailUtil=EmailUtil.getInstance();
		String context = "";
		//获取连接
		context="正在连接服务器...";
		JSONObject dwrObj=new JSONObject();
		dwrObj.put("id", emailInfoVo.getId());
		dwrObj.put("msg", context);
		if(dwrFlag){
			dwrManager.sendMsg(emailInfoVo.getUserId(), "msg1",dwrObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo","",false);
		}
		Store store=emailUtil.connect(emailInfoVo,type);
		if(store==null){
			returnObj.put("error", "未连接到服务器,请稍后重试!");
			RunEmailCacheManager.removeCache(emailInfoVo.getId());
			return;
		}
		context="正在获取邮件列表...";
		dwrObj.put("msg", context);
		if(dwrFlag){
			dwrManager.sendMsg(emailInfoVo.getUserId(), "msg2",dwrObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo","",false);
		}
		//获取邮件列表
		Folder folder=emailUtil.getFolder(store,type);
		if(folder==null){
			returnObj.put("error", "打开邮件目录失败,请稍后重试!");
			RunEmailCacheManager.removeCache(emailInfoVo.getId());
			return;
		}
		Message[] emails=emailUtil.getMessages(folder, emailInfoVo.getStartDate(),dwrFlag,emailInfoVo.getUserId(),dwrManager,dwrObj);
		if(emails==null){
			returnObj.put("error", "获取邮件数据失败，请稍后重试!");
			RunEmailCacheManager.removeCache(emailInfoVo.getId());
			return;
		}
		context="共扫描到"+emails.length+"封需接受邮件,开始接受...";
		dwrObj.put("msg", context);
		if(dwrFlag){
			dwrManager.sendMsg(emailInfoVo.getUserId(), "msg3",dwrObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo","",false);
		}
		//正在收取邮件
		int count=emails.length; //总邮件数
		int success=0;//成功收取个数
		int failure=0;//失败收取个数
		Date lastDate=null;
        for(int i=0;i<emails.length;i++){
        	JSONObject sendObj=new JSONObject();
        	try{
        		String status="receive";
        		if("imap".equals(type)){
        			status="rubbish";
        		}
        		sendObj.put("count", count);
	        	sendObj.put("success", success);
	        	sendObj.put("failure", failure);
	        	sendObj.put("id", emailInfoVo.getId());
	        	if(dwrFlag){
	        		dwrManager.sendMsg(emailInfoVo.getUserId(), "accept",sendObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo","",false);
	        	}
	        	MimeMessage email = (MimeMessage) emails[i];
	        	//email.setFlag(Flags.Flag.DELETED, true);//什么意思
	        	DynaBean emailInfo=new DynaBean("JE_SYS_EMAILDATA",false);
	        	emailInfo.set(BeanUtils.KEY_PK_CODE, "JE_SYS_EMAILDATA_ID");
	        	emailInfo.set("EMAILDATA_TDZT", "1");//投递状态(发件箱用)
	        	emailInfo.set("EMAILDATA_ZT", status);//状态
	        	emailInfo.set("EMAILDATA_GSRID", emailInfoVo.getUserId());//用户ID
	        	emailInfo.set("EMAILDATA_GSR", emailInfoVo.getUserName());//用户
	        	emailInfo.set("EMAILDATA_DQZT", "0");//读取到系统中设定成未读       实际可读原来邮件是否是已读
	        	emailInfo.set("EMAILDATA_BJXB", "0");//标记星标
	        	emailInfo.set("EMAILDATA_SFZF", "0");//是新信,还是转发(发件箱用)
	        	emailInfo.set("EMAILDATA_YHZ", "0");//已回执
	        	emailInfo.set("EMAILDATA_SSYX", emailInfoVo.getAddress()); //邮箱地址
	        	emailInfo.set("JE_SYS_EMAILCONFIG_ID", emailInfoVo.getId());//邮箱设置外键
	        	emailInfo.set("SY_CREATEUSER", emailInfoVo.getUserCode());
	        	emailInfo.set("SY_CREATEUSERID", emailInfoVo.getUserId());
	        	emailInfo.set("SY_CREATEUSERNAME", emailInfoVo.getUserName());
	        	emailInfo.set("SY_CREATETIME", DateUtils.formatDateTime(new Date()));
	        	//读取邮件信息
	        	sendObj.put("dwrFlag", dwrFlag);
	        	List<DynaBean> documents=emailUtil.buildEmail(email, emailInfo,sendObj,dwrManager);
	        	if(documents!=null){
	        		emailInfo=serviceTemplate.insert(emailInfo);
	        		for(DynaBean document:documents){
						document.set("DOCUMENT_FUNCCODE", "JE_SYS_EMAILDATA");
						document.set("DOCUMENT_TABLECODE", "JE_SYS_EMAILDATA");
						document.set("DOCUMENT_FIELDCODE", "EMAILDATA_FJ");
						document.set("DOCUMENT_PKVALUE", emailInfo.getPkValue());
						document.set("SY_CREATEUSER", emailInfo.getStr("SY_CREATEUSER"));
						document.set("SY_CREATEUSERNAME", emailInfo.getStr("SY_CREATEUSERNAME"));
						document.set("SY_CREATEUSERID", emailInfo.getStr("SY_CREATEUSERID"));
						document.set("SY_CREATETIME", emailInfo.getStr("SY_CREATETIME"));
						documentManager.insertDoc(document);
					}
	        		success++;
	        		Date receiveDate=DateUtils.getDate(emailInfo.getStr("EMAILDATA_SJ"), DateUtils.DAFAULT_DATETIME_FORMAT);
	        		if(lastDate==null){
						lastDate=receiveDate;
					}else if(receiveDate.after(lastDate)){
						lastDate=receiveDate;
					}
	        	}else{
	        		//读取失败邮件
	        		failure++;
	        	}
//	        	context="共获取到"+count+"封邮件,已成功接受"+success+"封，接受失败"+failure+"封...";
	        	//如果原来没有推送 消息，且用户请求需推送消息
				String runType=RunEmailCacheManager.getCacheValue(emailInfoVo.getId());
	        	if("send".equals(runType) && !dwrFlag){
	        		dwrFlag=true;
	        		count=(emails.length-success-failure);
	        		success=0;
	        		failure=0;
	        	}
	        	/**
	        	 * 终止接受
	        	 */
	        	if("end".equals(runType)){
	        		break;
	        	}
	        }catch(FolderClosedException fce){
	        	failure++;
//	        	context="共获取到"+count+"封邮件,已成功接受"+success+"封，接受失败"+failure+"封...";
	        	sendObj.put("count", count);
	        	sendObj.put("success", success);
	        	sendObj.put("failure", failure);
	        	if(dwrFlag){
	        		dwrManager.sendMsg(emailInfoVo.getUserId(), "accept",sendObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo","",false);
	        	}
	        	//重新连接
	        	//System.out.println("重新连接!");
	        	folder=emailUtil.getFolder(store, type);
	        }catch(Exception e){
	        	failure++;
//	        	context="共获取到"+count+"封邮件,已成功接受"+success+"封，接受失败"+failure+"封...";
	        	sendObj.put("count", count);
	        	sendObj.put("success", success);
	        	sendObj.put("failure", failure);
	        	if(dwrFlag){
	        		dwrManager.sendMsg(emailInfoVo.getUserId(), "accept",sendObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo","",false);
	        	}
//	        	e.printStackTrace();
	        }
        }
        //关闭连接
//        context="正在关闭连接...";
//		if(dwrFlag){
//			dwrManager.sendMsg(emailInfoVo.getUserId(), "msg3",context, "JE.sys.email.util.Util.updateReceiveInfo","",false);
//		}
		emailUtil.close(store, folder);
        returnObj.put("count", emails.length);
        returnObj.put("success", success);
        returnObj.put("failure", failure);
        if(lastDate!=null){
//        	if(emailInfoVo.getEndDate().after(lastDate)){
//        		lastDate=emailInfoVo.getEndDate();
//        	}
        	returnObj.put("lastDate", DateUtils.formatDateTime(lastDate));
        	pcServiceTemplate.executeSql("UPDATE JE_SYS_EMAILCONFIG SET EMAILCONFIG_ZHJSSJ='"+DateUtils.formatDateTime(lastDate)+"' where JE_SYS_EMAILCONFIG_ID='"+emailInfoVo.getId()+"' AND (EMAILCONFIG_ZHJSSJ is null or EMAILCONFIG_ZHJSSJ<='"+DateUtils.formatDateTime(lastDate)+"')");
        }
        RunEmailCacheManager.removeCache(emailInfoVo.getId());
      //关闭连接
        context="接受完毕...";
        dwrObj.put("msg", context);
		if(dwrFlag){
			dwrManager.sendMsg(emailInfoVo.getUserId(), "end",dwrObj.toString(), "JE.sys.email.util.Util.updateReceiveInfo","",false);
		}
		if("imap".equals(emailInfoVo.getType()) && "pop3".equals(type)){
//			if(lastDate!=null){
//			emailInfoVo.setEndDate(lastDate);
//			}
			EmailReceiveTread emailReceiveTread=new EmailReceiveTread(emailInfoVo, "imap");
			emailReceiveTread.start();
		}
	}
	/**
	 * 定时任务接受邮件(只接受在线用户)
	 */
	public void onLineReceive(){
		
	}

	/**
	 * TODO未处理
	 * @param val TODO未处理
	 */
	@Override
	public void doSyncRunEmail(String val) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotEmpty(val) && val.split("#").length==3){
			String[] valArray=val.split("#");
			String type=valArray[0];
			String pkValue=valArray[1];
			String typeVal=valArray[2];
			if(type.equals("add")){
				RunEmailCacheManager.putCache(pkValue, typeVal);
			}else{
				RunEmailCacheManager.removeCache(pkValue);
			}
		}
	}
}
