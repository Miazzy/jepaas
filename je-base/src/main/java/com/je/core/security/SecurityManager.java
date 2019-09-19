package com.je.core.security;

import java.util.Map;

import net.sf.json.JSONObject;
import org.hibernate.Session;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
/**
 * @author 安全管理器 //TODO 张帅鹏 该类可删除
 */
public interface SecurityManager {

	/**
	 * 得到URL权限集合
	 * @return
	 */
	public Map<String, String> loadUrlAuthorities();
	/**
	 *
	 * @param user
	 * @param deptId
	 * @param session
	 * @return
	 */
	public EndUser buildCurrentUser(DynaBean user, String deptId, Session session);

	/**
	 * 根据第三方code获取openId
	 * @param dsfStateBean
	 * @param dsfCode
	 * @param obj
	 * @return
	 */
	public String getDsfLoginOpenId(DynaBean dsfStateBean, String dsfCode, JSONObject obj);
}
