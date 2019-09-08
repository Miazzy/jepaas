package com.je.core.util;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.annotations.End;
import org.springframework.security.context.SecurityContextHolder;

import com.je.core.constants.LoginErrorType;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.DateUtils;
import com.je.core.util.SpringContextHolder;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.rbac.model.Department;
import com.je.rbac.model.EndUser;
/**
 * 当前登录用户信息获取
 *
 */
public class SecurityUserHolder {

//	public static Map<String,Integer> userErrorPw=new HashMap<String,Integer>();
	/**
	 * 存放登录用户
	 */
//	public static Map<String,EndUser> tokenUser=new HashMap<>();

	private static ThreadLocal<EndUser> threadLocal = new ThreadLocal<>();
	private static ThreadLocal<String> threadLocalToken = new ThreadLocal<>();
	public static void putToken(String tokenId){threadLocalToken.set(tokenId);}
	public  static String getToken(){return threadLocalToken.get();}
	public static void put(EndUser t) {
		threadLocal.set(t);
	}
	private static EndUser get() {
		return threadLocal.get();
	}
	public static void remove() {
		threadLocal.remove();
	}
	public static void removeAll() {
		threadLocal.remove();
		threadLocalToken.remove();
	}
    public static EndUser getCurrentUser() {
		EndUser currentUser=threadLocal.get();
		if(currentUser==null){
			Department dept=new Department();
			dept.setDeptId("SYSTEM");
			dept.setDeptCode("SYSTEM");
			dept.setDeptName("系统");
			EndUser user=new EndUser();
			user.setUserId("SYSTEM");
			user.setUserCode("SYSTEM");
			user.setUsername("系统");
			user.setDept(dept);
			user.setSaas(true);
			user.setZhId("SYSTEM");
			user.setZhMc("系统");
			user.setZhDs("_global");
			return user;
		}else {
			return currentUser;
		}
	}
//	/**
//	 * 获取登录用户
//	 * @return
//	 */
//	public static EndUser getCurrentUser() {
//		Object o = SecurityContextHolder.getContext().getAuthentication();
//		if(null != o) {
//			EndUser user = (EndUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			if(user.getProxy()){
//				return user.getProxyUser();
//			}else{
//				return user;
//			}
//		} else {
//			EndUser u = new EndUser();
//			u.setUsername("GUEST");
//			u.setSaas(true);
//			return u;
//		}
//	}
	/**
	 * 获取登录用户所在部门
	 * @return
	 */
	public static Department getCurrentUserDept() {
		EndUser currentUser=getCurrentUser();
		if(currentUser==null) {
			Department dept=new Department();
			dept.setDeptId("SYSTEM");
			dept.setDeptCode("SYSTEM");
			dept.setDeptName("系统");
			return dept;
		}else{
			return getCurrentUser().getDept();
		}
//		Object o = SecurityContextHolder.getContext().getAuthentication();
//		if(null != o) {
//			EndUser u = (EndUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			if(u.getProxy()){
//				return u.getProxyUser().getDept();
//			}else{
//				return u.getDept();
//			}
//		} else {
//			Department d = new Department();
//			return d;
//		}
	}
	/**
	 * 获取系统虚拟用户
	 * @return
	 */
	public static EndUser getSystem(){
		EndUser u=new EndUser();
		u.setUserId("JE_SYSTEM_VIRTUALUSER");
		u.setUserCode("JE_SYSTEM_VIRTUALUSER");
		u.setUsername(StringUtil.getDefaultValue(WebUtils.getSysVar("JE_SYS_SYSTEM"),"JEPLUS平台"));
		return u;
	}
	public static Map<String,String> getCurrentInfo(){
		return getCurrentInfo(getCurrentUser());
	}
	/**
	 * 得到登录信息的设置
	 * @return
	 */
	public static Map<String,String> getCurrentInfo(EndUser currentUser){
//		EndUser currentUser=getCurrentUser();
		Department currentDept=currentUser.getDept();
		Map<String,String> values=new HashMap<String,String>();
		values.put("@USER_ID@", currentUser.getUserId());
		values.put("@USER_CODE@", currentUser.getUserCode());
		values.put("@USER_NAME@", currentUser.getUsername());
		values.put("@USER_JTGSID@", currentUser.getJtgsId());
		values.put("@USER_JTGSMC@", currentUser.getJtgsMc());
		values.put("@USER.dept.jtgsId@", currentUser.getJtgsId());
		values.put("@USER.dept.jtgsMc@", currentUser.getJtgsMc());
		values.put("@USER.dept.jtgsDm@", currentUser.getJtgsDm());
		values.put("@USER_NAME@", currentUser.getUsername());
		values.put("@USER_NAME@", currentUser.getUsername());
		values.put("@DEPT_ID@", currentDept.getDeptId());
		values.put("@DEPT_CODE@", currentDept.getDeptCode());
		values.put("@DEPT_NAME@", currentDept.getDeptName());
		values.put("@IS_MANAGER@", currentUser.getIsManager());
		values.put("@NOW_DATE@", DateUtils.formatDate(new Date()));
		values.put("@NOW_MONTH@", DateUtils.formatDate(new Date(), "yyyy-MM"));
		values.put("@NOW_TIME@", DateUtils.formatDateTime(new Date()));
		values.put("@NOW_YEAR@", DateUtils.formatDate(new Date(), "yyyy"));
		values.put("@NOW_ONLYMONTH@", DateUtils.formatDate(new Date(), "MM"));
		return values;
	}
	public static String getLoginError(String errorCode){
		String error="登录密码错误!";
		if(LoginErrorType.NONE.equals(errorCode)){
			error="该用户不存在!";
		}else if(LoginErrorType.ERRORCODE.equals(errorCode)){
			error="无效的验证码!";
		}else if(LoginErrorType.DISABLED.equals(errorCode)){
			error="该用户已被禁用!";
		}else if(LoginErrorType.NOSYS.equals(errorCode)){
			error="该用户不是系统用户!";
		}else if(LoginErrorType.INVALID.equals(errorCode)){
			error="该用户已经失效!";
		}else if(LoginErrorType.ERRORPROXY.equals(errorCode)){
			error="代理登录失败!";
		}else if(LoginErrorType.ERRORDEPT.equals(errorCode)){
			error="该用户没有部门!";
		}else if(LoginErrorType.ERRORJCAPTCHA.equals(errorCode)){
			error="验证码错误!";
		}else if(LoginErrorType.ERRORLOGINKEY.equals(errorCode)){
			error="无效的操作密钥!";
		}else if(LoginErrorType.INVALIDLOGINKEY.equals(errorCode)){
			error="该密钥已失效!";
		}else if(LoginErrorType.REGISTERREPEATEMAIL.equals(errorCode)){
			error="该邮箱已被使用,请更换邮箱!";
		}else if(LoginErrorType.REGISTERREPEATPHONE.equals(errorCode)){
			error="该手机号已被使用,请更换手机号!";
		}else if(LoginErrorType.REGISTERREPEATOPENID.equals(errorCode)){
			error="该认证已存在，请更换注册方式!";
		}else if(errorCode.startsWith(LoginErrorType.WARNLOCKED)){
			String[] cs=errorCode.split("_");
			String errorVal=WebUtils.getSysVar("JE_CORE_ERRORPW")+"";
			int errorCs=0;
			if(StringUtil.isNotEmpty(errorVal)){
				errorCs=Integer.parseInt(errorVal+"");
			}
			error="密码已错误"+cs[1]+"次，"+errorCs+"次用户将锁定!";
		}else if(LoginErrorType.USERLOCKED.equals(errorCode)){
			error="用户已锁定，请找管理员解锁!";
		}else{
			error="登录密码错误!";
		}
		return error;
	}
	public static String getLoginError(Exception e){
		String errorType=e.getMessage();
		String error="登录密码错误!";
		if(e instanceof org.springframework.security.concurrent.ConcurrentLoginException){
			error="该用户已经在别处登录!";
		}else if(LoginErrorType.NONE.equals(errorType)){
			error="该用户不存在!";
		}else if(LoginErrorType.ERRORCODE.equals(errorType)){
			error="无效的验证码!";
		}else if(LoginErrorType.DISABLED.equals(errorType)){
			error="该用户已被禁用!";
		}else if(LoginErrorType.NOSYS.equals(errorType)){
			error="该用户不是系统用户!";
		}else if(LoginErrorType.INVALID.equals(errorType)){
			error="该用户已经失效!";
		}else if(LoginErrorType.ERRORPROXY.equals(errorType)){
			error="代理登录失败!";
		}else if(LoginErrorType.ERRORDEPT.equals(errorType)){
			error="该用户没有部门!";
		}else if(LoginErrorType.ERRORJCAPTCHA.equals(errorType)){
			error="验证码错误!";
		}else if(LoginErrorType.ERRORLOGINKEY.equals(errorType)){
			error="无效的操作密钥!";
		}else if(LoginErrorType.INVALIDLOGINKEY.equals(errorType)){
			error="该密钥已失效!";
		}else if(errorType.startsWith(LoginErrorType.WARNLOCKED)){
			String[] cs=errorType.split("_");
			String errorVal=WebUtils.getSysVar("JE_CORE_ERRORPW")+"";
			int errorCs=0;
			if(StringUtil.isNotEmpty(errorVal)){
				errorCs=Integer.parseInt(errorVal+"");
			}
			error="密码已错误"+cs[1]+"次，"+errorCs+"次用户将锁定!";
		}else if(LoginErrorType.USERLOCKED.equals(errorType)){
			error="用户已锁定，请找管理员解锁!";
		}else{
			error="登录密码错误!";
		}
		return error;
	}
	public static String getLoginErrorCode(Exception e){
		String errorCode=e.getMessage();
		String errorType=e.getMessage();
		if(e instanceof org.springframework.security.concurrent.ConcurrentLoginException){
			errorCode=LoginErrorType.OTHERLOGIN;
		}else if(LoginErrorType.NONE.equals(errorType)){
			errorCode=LoginErrorType.NONE;
		}else if(LoginErrorType.ERRORCODE.equals(errorType)){
			errorCode=LoginErrorType.ERRORCODE;
		}else if(LoginErrorType.DISABLED.equals(errorType)){
			errorCode=LoginErrorType.DISABLED;
		}else if(LoginErrorType.NOSYS.equals(errorType)){
			errorCode=LoginErrorType.NOSYS;
		}else if(LoginErrorType.INVALID.equals(errorType)){
			errorCode=LoginErrorType.INVALID;
		}else if(LoginErrorType.ERRORPROXY.equals(errorType)){
			errorCode=LoginErrorType.ERRORPROXY;
		}else if(LoginErrorType.ERRORDEPT.equals(errorType)){
			errorCode=LoginErrorType.ERRORDEPT;
		}else if(LoginErrorType.ERRORJCAPTCHA.equals(errorType)){
			errorCode=LoginErrorType.ERRORJCAPTCHA;
		}else if(LoginErrorType.ERRORLOGINKEY.equals(errorType)){
			errorCode=LoginErrorType.ERRORLOGINKEY;
		}else if(LoginErrorType.INVALIDLOGINKEY.equals(errorType)){
			errorCode=LoginErrorType.INVALIDLOGINKEY;
		}else if(errorType.startsWith(LoginErrorType.WARNLOCKED)){
			String[] cs=errorType.split("_");
			String errorVal=WebUtils.getSysVar("JE_CORE_ERRORPW")+"";
			int errorCs=0;
			if(StringUtil.isNotEmpty(errorVal)){
				errorCs=Integer.parseInt(errorVal+"");
			}
			errorCode=LoginErrorType.WARNLOCKED+"-"+cs[1]+"-"+errorCs+"";
		}else if(LoginErrorType.USERLOCKED.equals(errorType)){
			errorCode=LoginErrorType.USERLOCKED;
		}else{
			errorCode=LoginErrorType.ERRORPASSWORD;
		}
		return errorCode;
	}
}
