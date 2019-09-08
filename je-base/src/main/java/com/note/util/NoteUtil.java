package com.note.util;

import com.je.core.util.WebUtils;

public class NoteUtil {
	/**
	 * 自定义发送短信
	 * @param phoneNumber
	 * @param context
	 * @param jtgsId
	 * @error
	 * 错误信息值含义
	 * -1 ：没有该用户账户!
	 * -2 ：密钥不正确!
	 * -3 ：短信数量不足!
	 * -4 ：手机号格式不正确!
	 * -11 ：该用户被禁用!
	 * -14 ：短信内容出现非法内容!
	 * -41 ：手机号为空!
	 * -42 ：短信内容为空!
	 * -51 ：短信签名格式不正确!
	 * -999 ：短信服务剩余条数不足，请及时续费!
	 * @return
	 */
	public static Integer sendNote(String phoneNumber,String context,String jtgsId){
		try{
			String uId=WebUtils.getSysVar("JE_SYS_NOTE_USER");
			int result=0;
			//实现短信发送业务
			
		    return result;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
}
