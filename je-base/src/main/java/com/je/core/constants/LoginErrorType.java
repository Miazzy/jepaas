package com.je.core.constants;
/**
 * 登录错误信息
 * @author zhangshuaipeng
 *
 */
public class LoginErrorType {
	/**
	 * 没有该用户
	 */
	public static final String NONE = "NONE";
	/**
	 * 用户被禁用
	 */
	public static final String DISABLED="DISABLED";
	/**
	 * 该用户已经失效
	 */
	public static final String INVALID="INVALID";
	/**
	 * 无效的验证码
	 */
	public static final String ERRORCODE="ERRORCODE";
	/**
	 * 该用户不是系统用户
	 */
	public static final String NOSYS="NOSYS";
	/**
	 * 该用户在别处登录
	 */
	public static final String OTHERLOGIN="OTHERLOGIN";
	/**
	 * 用户密码错误
	 */
	public static final String ERRORPASSWORD="ERRORPASSWORD";
	/**
	 * 用户密码错误
	 */
	public static final String ERRORPROXY="ERRORPROXY";
	/**
	 * 提醒锁定
	 */
	public static final String WARNLOCKED="WARNLOCKED";
	/**
	 * 需要输入验证码错误标识
	 */
	public static final String WARNCODE="WARNCODE";
	/**
	 * 用户锁定
	 */
	public static final String USERLOCKED="USERLOCKED";
	/**
	 * 该用户没有部门
	 */
	public static final String ERRORDEPT="ERRORDEPT";
	/**
	 * 未注册
	 */
	public static final String NOREGISTER="NOREGISTER";
	/**
	 * 验证码错误
	 */
	public static final String ERRORJCAPTCHA="ERRORJCAPTCHA";
	/**
	 * 无效的操作密钥
	 */
	public static final String ERRORLOGINKEY="ERRORLOGINKEY";
	/**
	 * 该密钥已失效
	 */
	public static final String INVALIDLOGINKEY="INVALIDLOGINKEY";
	/**
	 * 用户登录次数限制异常
	 */
	public static final String USERNUMERROR="USERNUMERROR";
	/**
	 * 该密钥已失效
	 */
	public static final String OTHER="OTHER";
	/**
	 * 用户未认证
	 */
	public static final String NOTCERTIFIED="NOTCERTIFIED";
	/**
	 * 用户认证失败
	 */
	public static final String AUTHERROR="AUTHERROR";
	/**
	 * 注册重复邮箱
	 */
	public static final String REGISTERREPEATEMAIL="REGISTERREPEATEMAIL";
	/**
	 * 注册重复手机号
	 */
	public static final String REGISTERREPEATPHONE="REGISTERREPEATPHONE";
	/**
	 * 认证已存在
	 */
	public static final String REGISTERREPEATOPENID="REGISTERREPEATOPENID";
}
