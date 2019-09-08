/**
 *
 */
package com.je.core.util;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import cn.hutool.http.HttpUtil;
import com.je.cache.service.config.BackCacheManager;
import com.je.cache.service.config.ConfigCacheManager;
import com.je.cache.service.config.SaasCacheManager;
import com.je.cache.service.config.SysCacheManager;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import com.je.config.service.SysConfigManager;
import com.je.rbac.model.EndUser;
import com.je.rbac.model.Role;

/**
 * Web的目录信息，由于系统环境加载的先后顺序问题，这个工具采用单例实现
 * @author chenmeng
 * 2012-4-27 上午10:59:26
 */
public class WebUtils {
	private static final String PING_URL = "https://baidu.com";
	/**ping网络状态*/
	public static String NET_PING_FLAG = "0";
	/**是否进行过ping*/
	public static boolean hasPing = false;
	public static String cluster=null;

	private static WebUtils webUtils;
//	/**前台变量*/
//	public final static Map<String, String> frontVar = new HashMap<String, String>();
//	/**后台变量*/
//	public final static Map<String, String> backVar = new HashMap<String, String>();
//	/**系统变量*/
//	public final static Map<String, String> sysVar = new HashMap<String,String>();
	/**文档存储目录，读取sysconfig.properties的设定*/
//	public final static Map<String,String> docVar=new HashMap<String,String>();
//	/**SAAS模式公司变量*/
//	public final static Map<String,Map<String,String>> saasVar = new HashMap<String,Map<String,String>>();
//	/**模式过滤变量*/
//	public final static Map<String,String> jepfVar=new HashMap<String,String>();

	/**
	 * 集群变量获取
	 * @param key
	 * @return
	 */
	public static String getClusterVar(String key){
		String val="";
		return val;
	}

	/**
	 * 是否是集群
	 * */
	public static boolean isCluster(){
		if(StringUtil.isEmpty(cluster)){
			try {
				Properties properties = PropertiesLoaderUtils.loadAllProperties("cluster.properties");
				String startCluster = properties.getProperty("cluster.enable");
				cluster=startCluster;
				if("1".equals(startCluster)) {
					return true;
				}
			} catch (IOException e) {
				throw new PlatformException("系统工具类获取是否集群异常", PlatformExceptionEnum.JE_CORE_UTIL_WEBUTILS_GETCLUSTER_ERROR,e);
			}
		}else if("1".equals(cluster)){
			return true;
		}
		return false;
	}

	/**
	 * 获取后台变量
	 * @return
	 */
	public static String getBackVar(String key) {
//		if(backVar.isEmpty()) {
//			SysConfigManager manager = SpringContextHolder.getBean("sysConfigManager");
//			manager.doLoadUserConfigVariables();
//		}
		return BackCacheManager.getCacheValue(key);
	}

	/**
	 * 获取配置文件的值
	 * @param key
	 * @return
	 */
	public static String getConfigVar(String key){
		return ConfigCacheManager.getCacheValue(key);
	}

	public static String getSysVar(String key){
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		if(isSaas() && !currentUser.getSaas()){
			if(StringUtil.isEmpty(currentUser.getZhId()) || "SYSTEM".equals(currentUser.getZhId())){
				return SysCacheManager.getCacheValue(key);
			}
			Map<String,Object> saasVar= SaasCacheManager.getCacheValue(currentUser.getZhId());
			if(saasVar==null){
				SysConfigManager sysConfigManager=SpringContextHolder.getBean("sysConfigManager");
				saasVar=sysConfigManager.doLoadSaasConfig(currentUser.getZhId());
			}
			return (String) saasVar.get(key);
		}else{
			return SysCacheManager.getCacheValue(key);
		}
	}
	public static Map<String, Object> getAllSysVar(){
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		if(isSaas() && !currentUser.getSaas()){
			if(StringUtil.isEmpty(currentUser.getZhId()) || "SYSTEM".equals(currentUser.getZhId())){
				return SysCacheManager.getCacheValues();
			}
			Map<String,Object> saasVar= SaasCacheManager.getCacheValue(currentUser.getZhId());
			if(saasVar==null){
				SysConfigManager sysConfigManager=SpringContextHolder.getBean("sysConfigManager");
				saasVar=sysConfigManager.doLoadSaasConfig(currentUser.getZhId());
			}
			return saasVar;
		}else{
			return SysCacheManager.getCacheValues();
		}
	}
	public static Map<String, Object> getAllSysVar(String zhId){
		if(isSaas() && !"SYSTEM".equals(zhId)){
			Map<String,Object> saasVar= SaasCacheManager.getCacheValue(zhId);
			if(saasVar==null){
				SysConfigManager sysConfigManager=SpringContextHolder.getBean("sysConfigManager");
				saasVar=sysConfigManager.doLoadSaasConfig(zhId);
			}
			return saasVar;
		}else{
			return SysCacheManager.getCacheValues();
		}
	}
//	public static String getWebRootPath() {
//		return JeFileUtil.webrootAbsPath;
//	}
//
//	public static String getAbsClassPath() {
//		return JeFileUtil.absClassPath;
//	}
	public static Boolean isSaas(){
		String sysMode=StringUtil.getDefaultValue(SysCacheManager.getCacheValue("JE_SYSMODE"), "0");
		//如果是SAAS模式
		if("1".equals(sysMode)){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 获取集团公司过滤
	 * @return
	 */
	public static String getJtgsSql(){
		if(isSaas()){
			return " AND SY_JTGSID='"+SecurityUserHolder.getCurrentUser().getJtgsId()+"'";
		}else{
			return "";
		}
	}
	/**
	 * 获取集团公司过滤
	 * @return
	 */
	public static String getZhSql(){
		if(isSaas()){
			return " AND SY_ZHID='"+SecurityUserHolder.getCurrentUser().getZhId()+"'";
		}else{
			return "";
		}
	}
	/**
	 * 判断用户身份是否在基础角色里
	 * @param currentUser
	 * @return
	 */
	public static Boolean haveBaseRole(EndUser currentUser){
		boolean flag=false;
		for(Role r:currentUser.getRoles()){
			if("SYS".equals(r.getRoleRank())){
				flag=true;
			}
		}
		return flag;
	}
	/**
	 * 获取SAAS权限的SQL
	 * @return
	 */
	public static String getCpYhWhere(){
		String cpSql=" AND CPYH_AZZT_CODE='1'";
		return cpSql;
	}
	/**
	 * 得到是否负载均衡
	 * @return
	 */
	public static Boolean getBlanc(){
		Boolean flag=false;
		return flag;
	}
	//	/**
//	 * 获取前台
//	 * @return
//	 */
//	public static String getJEFontFilePath(){
//		return JeFileUtil.webrootAbsPath;
//	}
	//	/**
//	 * 获取平台后台核心操作文件的目录。。 如果config.properties  文件由界面开发，只有后台使用
//	 * @return
//	 */
//	public static String getJEBackFilePath(){
//		return JeFileUtil.webrootAbsPath;
//	}

//	/**
//	 * 获取虚拟存储路径
//	 * @return
//	 */
//	public static String getVirtualFilePath(){
//		return ConfigCacheManager.getCacheValue("sys.virtual.filePath");
//	}

//	/**
//	 * 获取阿里云存储路径
//	 */
//	public static String getOssPicLocation(){
//		return ConfigCacheManager.getCacheValue("oss.picLocation").toString();
//	}

//	/**
//	 * 获取文件存储类型
//	 * @return
//	 */
//	public static String getProjectStorageType(){
//		String docType="default";
//		String saveType=ConfigCacheManager.getCacheValue("sys.virtual.saveType");
//		if(StringUtil.isNotEmpty(saveType)){
//			docType=saveType;
//		}
//		return docType;
//	}

	/**
	 * 进行网络ping,改为HTTP请求
	 * @return
	 */
	public static boolean ping(){
		boolean flag = false;
		try {
			HttpUtil.get(PING_URL,5000);
			flag = true;
			hasPing = true;
			NET_PING_FLAG = "1";
		}catch (Throwable e){
			System.out.println(e.getMessage());
		}
		return flag;
	}

}
