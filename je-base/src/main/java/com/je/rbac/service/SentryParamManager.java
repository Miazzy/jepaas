package com.je.rbac.service;

import com.je.core.util.bean.DynaBean;

public interface SentryParamManager {
	/**
	 * 保存
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	public Boolean doSave(DynaBean dynaBean);

	/**
	 * 列表更新
	 * @param strData 开始时间
	 * @param dynaBean 自定义动态类
	 * @return
	 */
	public Integer doUpdateList(String strData, DynaBean dynaBean);
	/**
	 * 删除
	 * @param ids id
	 * @return
	 */
	public Integer doRemove(String ids);
	
}
