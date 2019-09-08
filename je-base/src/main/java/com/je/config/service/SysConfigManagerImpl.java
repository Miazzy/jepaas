package com.je.config.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.je.cache.service.config.*;
import com.je.core.dao.PCDaoTemplateImpl;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.DynaBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 系统级常量业务逻辑接口
 * 2012-5-28 上午10:18:32
 * zhangshuaipeng
 */
@Component("sysConfigManager")
public class SysConfigManagerImpl implements SysConfigManager {

	private static Logger logger = LoggerFactory.getLogger(SysConfigManagerImpl.class);

	@Autowired
	private PCDynaServiceTemplate serviceTemplate;
	@Autowired
	private PCServiceTemplate pcServiceTemplate;

	/**
	 * 加载用户变量
	 */
	@Override
	public void doLoadUserConfigVariables() {
		FrontCacheManager.clearAllCache();
		BackCacheManager.clearAllCache();
		List<DynaBean> lists = serviceTemplate.selectList("JE_CORE_CONFIG", " AND CONFIG_ENABLED='1'");
		for (DynaBean var : lists) {
			if ("FRONT".equals(var.getStr("CONFIG_TYPE_CODE"))) {
				FrontCacheManager.putCache(var.getStr("CONFIG_CODE"), var.getStr("CONFIG_VALUE"));
			} else if ("BACK".equals(var.getStr("CONFIG_TYPE_CODE"))) {
				BackCacheManager.putCache(var.getStr("CONFIG_CODE"), var.getStr("CONFIG_VALUE"));
			} else {
				BackCacheManager.putCache(var.getStr("CONFIG_CODE"), var.getStr("CONFIG_VALUE"));
				FrontCacheManager.putCache(var.getStr("CONFIG_CODE"), var.getStr("CONFIG_VALUE"));
			}
		}
	}

	/**
	 * 加载系统变量
	 */
	@Override
	public void doLoadSysConfigVariables() {
		List<DynaBean> sysVars=serviceTemplate.selectList("JE_CORE_SETTING","");
		for(DynaBean sysVar:sysVars){
			SysCacheManager.putCache(sysVar.getStr("CODE"), sysVar.getStr("VALUE"));
		}
//		JeFileUtil jeFileUtil=JeFileUtil.getInstance();
//		if (jeFileUtil.existsFile("/JE/data/config/resource/logo.png", JEFileType.PLATFORM)) {
//				WebUtils.sysVar.put("JE_SYS_LOGO", "logo.png*/JE/data/config/resource/logo.png");
//		} else {
//			WebUtils.sysVar.put("JE_SYS_LOGO", "");
//		}
//		if (jeFileUtil.existsFile("/favicon.ico", JEFileType.PLATFORM)) {
//			WebUtils.sysVar.put("JE_SYS_SYSLOGO", "favicon.ico*/favicon.ico");
//		} else {
//			WebUtils.sysVar.put("JE_SYS_SYSLOGO", "");
//		}
//	            File saasLogo=new File(WebUtils.getJEFilePath()+"/JE/data/config/resource/saaslogo.png");
//		if (jeFileUtil.existsFile("/JE/data/config/resource/saaslogo.png", JEFileType.PLATFORM)) {
//			WebUtils.sysVar.put("JE_SYS_SAASLOGO", "saaslogo.png*/JE/data/config/resource/saaslogo.png");
//		} else {
//			WebUtils.sysVar.put("JE_SYS_SAASLOGO", "");
//		}
//		//如果是SAAS云，则关闭权限分级
//		if (WebUtils.isSaas()) {
//			WebUtils.sysVar.put("JE_SYS_ADMINPERM", "0");
//		}
//		if (StringUtil.isEmpty(WebUtils.sysVar.get("JE_SYS_CIRCLE"))) {
//			WebUtils.sysVar.put("JE_SYS_CIRCLE", "1");
//		}

		DynaBean loadBean = serviceTemplate.selectOne("JE_CORE_TABLECOLUMN"," AND JE_CORE_TABLECOLUMN_ID='92d53bf4-ecd9-43ac-be47-29fed3d8ef76'");
		if(loadBean != null && !Strings.isNullOrEmpty(loadBean.getStr("TABLECOLUMN_REMARK"))){
            JSONObject loadObj = JSON.parseObject(loadBean.getStr("TABLECOLUMN_REMARK"));
            SysCacheManager.putCache("SY_JECORE", Strings.isNullOrEmpty(loadObj.getString("t"))?"1":loadObj.getString("t"));
		}else{
            SysCacheManager.putCache("SY_JECORE", "1");
		}
		SysCacheManager.putCache("JE_SYS_STATICIZE", "1");
		SysCacheManager.putCache("JE_SYS_DB", PCDaoTemplateImpl.DBNAME);
		SysCacheManager.putCache("JE_SYS_REDIS", WebUtils.isCluster() ? "1" : "0");
		//设置网络环境
		SysCacheManager.putCache("JE_SYS_INTERNET",WebUtils.ping()?"1":"0");
		//覆盖系统我的公司设定的信息
		DynaBean systemZh=serviceTemplate.selectOneByPk("JE_SAAS_YH","SYSTEM");
		if(systemZh!=null){
			//LOGO
			SysCacheManager.putCache("JE_SYS_LOGO", systemZh.getStr("YH_LOGO"));
		}else{
			SysCacheManager.putCache("JE_SYS_LOGO", "");
		}
		//手机APP
		long count=serviceTemplate.selectCount("JE_PHONE_APK","");
		if(count>0){
			SysCacheManager.putCache("JE_PHONE_APK", "1");
		}else{
			SysCacheManager.putCache("JE_PHONE_APK", "0");
		}
	}

	/**
	 * 加载JE文档路径默认
	 */
	@Override
	public void doLoadSysDocVariables() {
		// TODO Auto-generated method stub
		try {
			Properties properties = PropertiesLoaderUtils.loadAllProperties("sysconfig.properties");
			for (Object keyObj : properties.keySet()) {
				String key = (String) keyObj;
				if (StringUtil.isNotEmpty(key)) {
					ConfigCacheManager.putCache(key, properties.getProperty(key, "/"));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new PlatformException("读取sysconfig配置文件出错了", PlatformExceptionEnum.JE_CORE_CACHE_ERROR,e);

		}


	}

	/**
	 * 更新系统变量
	 * @param dynaBean
	 * @param allFields  TODO 暂不明确
	 */
	@Override
	public void doWriteSysConfigVariables(DynaBean dynaBean, String[] allFields) {
		List<DynaBean> sysVars=serviceTemplate.selectList("JE_CORE_SETTING","");
		Map<String,DynaBean> varInfos=new HashMap<>();
		for(DynaBean sysVar:sysVars){
			String code=sysVar.getStr("CODE");
			varInfos.put(code,sysVar);
		}
		for(String fieldName:allFields){
			if(varInfos.containsKey(fieldName)){
				DynaBean varInfo=varInfos.get(fieldName);
				varInfo.set("VALUE",dynaBean.getStr(fieldName));
				serviceTemplate.update(varInfo);
			}else{
				DynaBean varInfo=new DynaBean("JE_CORE_SETTING",true);
				varInfo.set("VALUE",dynaBean.getStr(fieldName));
				varInfo.set("CODE",fieldName);
				serviceTemplate.buildModelCreateInfo(varInfo);
				serviceTemplate.insert(varInfo);
			}
		}
	}

	/**
	 * 更新开发变量
	 * @param dynaBean
	 */
	@Override
	public void doWriteDevelopConfigVariables(DynaBean dynaBean) {
		//清空不持久变量
		String[] modeFields = new String[]{"JE_DEVELOP_REMARK", "JE_DEVELOP_USERNAME", "JE_DEVELOP_USERID", "JE_DEVELOP_MENUIDS"};
		doWriteSysConfigVariables(dynaBean,modeFields);
	}

	/**
	 * 修改系统模式
	 * @param sysMode TODO 暂不明确
	 */
	@Override
	public void doWriteSysModeVariables(String sysMode) {
		//清空不持久变量
		DynaBean varInfo=serviceTemplate.selectOne("JE_CORE_SETTING"," AND CODE='JE_CORE_MODE'");
		if(varInfo==null){
			varInfo = new DynaBean("JE_CORE_SETTING",true);
			varInfo.set("CODE","JE_CORE_MODE");
			varInfo.set("VALUE",sysMode);
			serviceTemplate.buildModelCreateInfo(varInfo);
			serviceTemplate.insert(varInfo);
		}else{
			varInfo.set("VALUE",sysMode);
			serviceTemplate.update(varInfo);
		}
	}

	/**
	 * 加载过滤功能和资源表信息
	 */
	@Override
	public void doLoadJepfConfig() {
		//控制资源表过滤的SQL
		//设置平台核心功能为核心SQL
		//UPDATE JE_CORE_FUNCINFO SET SY_JECORE='1' WHERE 1=1 AND FUNCINFO_FUNCCODE in ('JE','system','GNZT','JE_CORE_COMBINE','JE_CORE_COMBINEFIELDS','rbac','JE_CORE_QUERYUSER','JE_CORE_DEPARTMENT','JE_CORE_DEPTUSERS','JE_CORE_ENDUSER','JE_CORE_WORKEXP','JE_CORE_USERPROXY','JE_CORE_WORKGROUP','JE_CORE_WORKGROUPUSERS','JE_CORE_JDGSINFO','JE_CORE_SENTRY','JE_CORE_SENTRYUSERS','JE_CORE_SENTRYPARAMS','JE_TEST','JE_G2_DIMISSION','testFunc','testChildFunc','YUN_TEST','TEST_LINK_MIAN','JE_CORE_ATOM','JE_CORE_ATOMCOLUMN','JE_TEST_TESTTREE','JE_TEST_TESTTREE_UPGRADE','JE_TESTAA_TESTTREE','JE_TEST_TESTALL','JE_CORE_TESTCHILD','JE_CORE_VPROCESSINSTANCE','JE_V_TEST','JE_SYS_FUN','MESSAGELOG','JE_SYS_EMAILLOG','JE_SYS_RTXLOG','JE_SYS_NOTELOG','EXPRESS','JE_SYS_DRAFT','JE_SYS_OUTBOX','JE_SYS_INBOX','JE_CORE_FONTICON','FILEBUILDER','JE_SYS_FILEBUILDER','JE_CORE_UP','JE_CORE_UPPACKAGE','JE_CORE_UPPACKAGE_H','JE_CORE_UPDD_H','JE_CORE_UPICON_H','JE_CORE_UPICON','JE_CORE_UPTIMEDTASK','JE_CORE_UPFILEBUILDER','JE_CORE_UPDD','JE_CORE_UPHISTORY','JE_CORE_DICTIONARY','JE_CORE_DICTIONARYITEM','JE_CORE_VCOLUMNANDFIELD','JE_CORE_JEAPI','JE_SYS_MYASSIGN','JE_CORE_EXECUTIONLOG','JE_CORE_HELPMSG','JE_SYS_TIMEDTASK','JE_SYS_TASKEXCLUDES','JE_SYS_TASKLOG','JE_CORE_RESOURCETABLE','JE_SYS_CALENDAR','JE_CORE_VIEWEVENTS','JE_CORE_PROCESSINFO','JE_SYS_ASSIGN','JE_SYS_ASSIGNMX','JE_CORE_RESOURCECOLUMN','JE_SYS_FILETYPE','JE_CORE_VCURRENTTASK','JE_SYS_ASSIGNQUERY','JE_SYS_ASSIGNQUERYMX','JE_CORE_PCMAIL','JE_CORE_IMGNEW','JE_CORE_EXCEPTION','JE_CORE_RESOURCEFIELD','JE_CORE_RESOURCEBUTTON','JE_CORE_PROCESSLOG','JE_CORE_TABLECOLUMN','JE_CORE_FUNCINFO','JE_CORE_CONFIG','JE_CORE_JEJAVAAPI','JE_CORE_JEJAVAAPIFUNC','JE_CORE_PROTAL','JE_CORE_CODEGENSEQ','JE_SYS_NOTICE')
		//设置平台核心表为核心SQL
		//UPDATE JE_CORE_RESOURCETABLE SET SY_JECORE='1' WHERE 1=1 AND  RESOURCETABLE_TABLECODE in ('KFPT','GN','JE_CORE_VCOLUMNANDFIELD','JE_CORE_QUERYSTRATEGY','JE_CORE_TESTCASE','JE_CORE_FUNCINFO','JE_CORE_RESOURCECOLUMN','JE_CORE_RESOURCEFIELD','JE_CORE_FUNCRELATION','JE_CORE_RESOURCEBUTTON','JE_CORE_FUNCRELYON','JE_CORE_ASSOCIATIONFIELD','JE_CORE_FUNC_PERM','ZYB','JE_CORE_RESOURCETABLE','JE_CORE_TABLECOLUMN','JE_CORE_TABLEKEY','JE_CORE_TABLEINDEX','JE_CORE_TABLETRACE','JE_CORE_ATOM','JE_CORE_ATOMCOLUMN','JE_CORE_TABLEDISPLAY','SJZD','JE_CORE_DICTIONARY','JE_CORE_DICTIONARYITEM','RBAC','JE_CORE_WORKGROUP','JE_CORE_DEPARTMENT','JE_CORE_WORKGROUP_USER','JE_CORE_PERMISSION','JE_CORE_SENTRY_USER','JE_CORE_ROLEGROUP','JE_CORE_WORKEXP','JE_CORE_SENTRY','GWCSZB','JE_CORE_SENTRY_CWG','JE_CORE_SENTRY_XMG','JE_CORE_SENTRY_PTYFG','JE_CORE_SENTRY_YFG','JE_CORE_SENTRY_SSG','JE_CORE_SENTRY_KFG','JE_CORE_SENTRY_CPYFG','JE_CORE_SENTRY_LDG','JE_CORE_SENTRY_SJ','JE_CORE_VROLEUSER','QXFJ','JE_CORE_ADMIN_USER','JE_CORE_ADMIN_PERM','JE_CORE_ADMININFO','JE_CORE_ADMINPERM','JE_CORE_ENDUSER','JE_CORE_VDEPTUSER','JE_CORE_SENTRYPARAMS','JE_CORE_USERPROXY','JE_CORE_ROLE','JE_CORE_ROLEGROUP_PERM','JE_CORE_ROLE_PERM','JE_CORE_ROLE_USER','JE_CORE_VSENTRYUSER','JE_CORE_JDGSINFO','PTYL','JE_CORE_HELPMSG','JE_CORE_PAGENICKED','JE_CORE_CODEGENSEQ','JE_CORE_MENU','JE_CORE_CONFIG','JE_CORE_VIEWEVENTS','SY','JE_CORE_PROTAL','JE_CORE_IMGNEW','GZL','JE_CORE_PROCESSINFO','JE_CORE_VCURRENTTASK','JE_CORE_PROCESSLOG','JE_CORE_PROMPTLOG','JE_CORE_VPROCESSINSTANCE','JE_CORE_PCMAIL','PC_CORE_VCURRENTTASK','GNZT','JE_CORE_COMBINEFIELDS','JE_CORE_COMBINE')
		//UPDATE JE_CORE_RESOURCETABLE SET SY_JECORE='1' WHERE 1=1 and  RESOURCETABLE_TABLECODE in ('PTGN','SJ','JE_CORE_UPPACKAGE','JE_CORE_UPHISTORY','JE_CORE_UPDD','JE_CORE_UPICON','JE_CORE_UPTIMEDTASK','JE_CORE_UPFILEBUILDER','JE_SYS_TASKEXCLUDES','TB','JE_CORE_REPORT','JE_CORE_DATASOURCE','JE_CORE_CHARTS','GZRL','JE_SYS_CALENDARGROUP','JE_SYS_GROUPUSER','JE_SYS_CALENDAR','WDGL','JE_SYS_FASTERDOC','JE_SYS_FUNCSELECT','JE_SYS_DOCUMENTATION','JE_SYS_FILETYPE','WDKD','JE_SYS_OUTBOX','JE_SYS_INBOX','JSTX','JE_SYS_JMSGROUP','JE_SYS_JMSHISTORY','XXRZ','JE_SYS_NOTELOG','JE_SYS_RTXLOG','JE_SYS_EMAILLOG','JTLSCQ','JE_SYS_FILEBUILDER','JE_SYS_ASSIGN','JE_CORE_JEJAVAAPI','JE_CORE_JEAPI','JE_SYS_ASSIGNMX','JE_SYS_TIMEDTASK','JE_CORE_FONTICON','JE_CORE_EXCEPTION','JE_CORE_EXECUTIONLOG','JE_CORE_DOCUMENT','JE_SYS_TASKLOG','JE_SYS_NOTICE','JE_CORE_JEJAVAAPIFUNC')
		//设置平台核心字典为核心SQL
//		WebUtils.jepfVar.clear();
		DynaBean loadBean = serviceTemplate.selectOne("JE_CORE_TABLECOLUMN"," AND JE_CORE_TABLECOLUMN_ID='92d53bf4-ecd9-43ac-be47-29fed3d8ef76'");
		if(loadBean != null && !Strings.isNullOrEmpty(loadBean.getStr("TABLECOLUMN_REMARK"))){
            JSONObject loadObj = JSON.parseObject(loadBean.getStr("TABLECOLUMN_REMARK"));
            JepfCacheManager.putCache("SY_JECORE", Strings.isNullOrEmpty(loadObj.getString("t"))?"1":loadObj.getString("t"));
		}else{
            JepfCacheManager.putCache("SY_JECORE", "1");
		}
		JepfCacheManager.putCache("SY_JECORETABLES", "JE_CORE_DICTIONARY,JE_CORE_FUNCINFO,JE_CORE_RESOURCETABLE,JE_CORE_CHARTS,JE_CORE_REPORT,JE_CORE_DATASOURCE");
	}

	/**
	 * 加载SAAS系统设置
	 */
	@Override
	public Map<String,Object> doLoadSaasConfig(String zhId) {
		Map<String,Object> saasVar=new HashMap<>();
		DynaBean khInfo = serviceTemplate.selectOne("JE_SAAS_YH", " AND JE_SAAS_YH_ID='" + zhId + "'");
		List sysKeys= SysCacheManager.getKeys();
		for(Object keyObj:sysKeys){
			String key= (String) keyObj;
			saasVar.put(key,SysCacheManager.getCacheValue(key));
		}
		if(khInfo==null) return saasVar;
		/**--------短信---------**/
		if ("1".equals(khInfo.getStr("YH_DXSYH_CODE")) && "1".equals(khInfo.getStr("YH_DXPZQY"))) {
			saasVar.put("JE_SYS_NOTE_USER", khInfo.getStr("JE_SYS_NOTE_USER", ""));
			saasVar.put("JE_SYS_NOTE_PASSWORD", khInfo.getStr("JE_SYS_NOTE_PASSWORD", ""));
			saasVar.put("JE_SYS_NOTE_COUNT", khInfo.getStr("JE_SYS_NOTE_COUNT", ""));
		}
		/**--------邮箱---------**/
		saasVar.put("JE_SYS_EMAIL_SERVERHOST", khInfo.getStr("JE_SYS_EMAIL_SERVERHOST", ""));
		saasVar.put("JE_SYS_EMAIL_SERVERPORT", khInfo.getStr("JE_SYS_EMAIL_SERVERPORT", ""));
		saasVar.put("JE_SYS_EMAIL_USERNAME", khInfo.getStr("JE_SYS_EMAIL_USERNAME", ""));
		saasVar.put("JE_SYS_EMAIL_PASSWORD", khInfo.getStr("JE_SYS_EMAIL_PASSWORD", ""));
		saasVar.put("JE_SYS_EMAIL_SERVERVALIDATE", khInfo.getStr("JE_SYS_EMAIL_SERVERVALIDATE", ""));
		saasVar.put("JE_SYS_RTXIP", khInfo.getStr("JE_SYS_RTXIP"));
		saasVar.put("JE_SYS_RTXPORT", khInfo.getStr("JE_SYS_RTXPORT"));
		/**-------企业微信号-------**/
		saasVar.put("JE_WXCP_CORPID", khInfo.getStr("JE_WXCP_CORPID"));
		saasVar.put("JE_WXCP_USERCORPSECRET", khInfo.getStr("JE_WXCP_USERCORPSECRET"));
		saasVar.put("JE_WXCP_CORPSECRET", khInfo.getStr("JE_WXCP_CORPSECRET"));
		saasVar.put("JE_WXCP_AGENTID", khInfo.getStr("JE_WXCP_AGENTID"));
		saasVar.put("JE_WXCP_ADMINID", khInfo.getStr("JE_WXCP_ADMINID"));
		saasVar.put("JE_WXCP_JOINTITLE", khInfo.getStr("JE_WXCP_JOINTITLE"));
		saasVar.put("JE_WXCP_JOINURL", khInfo.getStr("JE_WXCP_JOINURL"));
		/**-------企业公众号-------**/
		saasVar.put("WX_GZH_APPID", khInfo.getStr("WX_GZH_APPID"));
		saasVar.put("WX_GZH_APPSECRET", khInfo.getStr("WX_GZH_APPSECRET"));
		saasVar.put("WX_GZH_URL", khInfo.getStr("WX_GZH_URL"));
		saasVar.put("WX_GZH_TOKEN", khInfo.getStr("WX_GZH_TOKEN"));
		saasVar.put("WX_GZH_ENCODINGAESKEY", khInfo.getStr("WX_GZH_ENCODINGAESKEY"));
		saasVar.put("YH_GLYBMID", khInfo.getStr("YH_GLYBMID"));
		saasVar.put("YH_GLYID", khInfo.getStr("YH_GLYID"));
		if(StringUtil.isNotEmpty(
				khInfo.getStr("YH_LOGO"))){
			saasVar.put("JE_SYS_LOGO", khInfo.getStr("YH_LOGO"));
		}
		SaasCacheManager.putCache(zhId,saasVar);
		//个性化变量
		return saasVar;
	}

	/**
	 * 修改图标版本
	 * @param version
	 */
	@Override
	public void doWriteIconVersion(String version) {
		// TODO Auto-generated method stub
		//清空不持久变量
		DynaBean varInfo=serviceTemplate.selectOne("JE_CORE_SETTING"," AND CODE='JE_ICON_VERSION'");
		if(varInfo==null){
			varInfo = new DynaBean("JE_CORE_SETTING",true);
			varInfo.set("CODE","JE_ICON_VERSION");
			varInfo.set("VALUE",version);
			serviceTemplate.buildModelCreateInfo(varInfo);
			serviceTemplate.insert(varInfo);
		}else{
			varInfo.set("VALUE",version);
			serviceTemplate.update(varInfo);
		}
	}

	/**
	 * 初始化版本
	 */
	@Override
	public void initJepfVersion() {
		// TODO Auto-generated method stub
		String version = getJepfVersion();
		if (!(SysCacheManager.getCacheValue("JE_JEPF_VERSION") + "").equals(version)) {
			doWriteJepfVersion(version);
			SysCacheManager.putCache("JE_JEPF_VERSION", version);
		}
	}

	/**
	 * 修改平台版本
	 * @param version
	 */
	@Override
	public void doWriteJepfVersion(String version) {
		// TODO Auto-generated method stub
		//清空不持久变量
		DynaBean varInfo=serviceTemplate.selectOne("JE_CORE_SETTING"," AND CODE='JE_JEPF_VERSION'");
		if(varInfo==null){
			varInfo = new DynaBean("JE_CORE_SETTING",true);
			varInfo.set("CODE","JE_JEPF_VERSION");
			varInfo.set("VALUE",version);
			serviceTemplate.buildModelCreateInfo(varInfo);
			serviceTemplate.insert(varInfo);
		}else{
			varInfo.set("VALUE",version);
			serviceTemplate.update(varInfo);
		}
	}

	/**
	 * 获取平台最新版本
	 * @return
	 */
	@Override
	public String getJepfVersion() {
		// TODO Auto-generated method stub
		String version = "";
		List<Map> maxVersions = pcServiceTemplate.queryMapBySql("select Max(UPHISTORY_BB) VERSION from JE_CORE_UPHISTORY where  UPHISTORY_TYPE='CORE'");
		if (maxVersions.size() == 1) {
			version = maxVersions.get(0).get("VERSION") + "";
		}
		if (StringUtil.isEmpty(version)) {
			version = "V5.0.0.0";
		}
		return version;
	}

}
