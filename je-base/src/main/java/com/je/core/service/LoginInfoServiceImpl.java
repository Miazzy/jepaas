package com.je.core.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.core.util.bean.DynaBean;

/**
 * 登录操作接口实现类
 */
@Component("loginInfoService")
public class LoginInfoServiceImpl implements LoginInfoService{
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;

	/**
	 * 保存登录操作
	 * @param type 类型
	 * @param yxTime TODO 暂不明确
	 * @param userName 用户名称
	 * @param userId 用户id
	 * @param funcCode 功能code
	 * @param showView 显示视图
	 * @param modelId TODO 暂不明确
	 * @return
	 */
	@Override
	public String getLoginKey(String type,String yxTime,String userName,String userId, String funcCode, String showView,
							  String modelId) {
		// TODO Auto-generated method stub
		DynaBean key=new DynaBean("JE_CORE_LOGININFO",true);
		key.set("TYPE", type);
		key.set("YXTIME", yxTime);
		key.set("USERNAME", userName);
		key.set("USERID", userId);
		key.set("FUNCCODE", funcCode);
		key.set("SHOWVIEW", showView);
		key.set("MODELID", modelId);
		serviceTemplate.buildModelCreateInfo(key);
		key=serviceTemplate.insert(key);
		return key.getStr("JE_CORE_LOGININFO_ID");
	}

	/**
	 * 获取该任务的key
	 * @param type 类型
	 * @param yxTime TODO 暂不明确
	 * @param userName 用户名称
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public String getLoginKey(String type,String yxTime,String userName,String userId) {
		// TODO Auto-generated method stub
		DynaBean key=new DynaBean("JE_CORE_LOGININFO",true);
		key.set("TYPE", type);
		key.set("YXTIME", yxTime);
		key.set("USERNAME", userName);
		key.set("USERID", userId);
		serviceTemplate.buildModelCreateInfo(key);
		key=serviceTemplate.insert(key);
		return key.getStr("JE_CORE_LOGININFO_ID");
	}

}
