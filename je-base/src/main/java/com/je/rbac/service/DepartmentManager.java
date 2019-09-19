package com.je.rbac.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DicInfoVo;

public interface DepartmentManager {
	/**
	 * 保存
	 * @param dynaBean
	 * @return
	 */
	public DynaBean doSave(DynaBean dynaBean);
	/**
	 * 修改
	 * @param dynaBean
	 */
	public void doUpdate(DynaBean dynaBean);
	/**
	 * 删除
	 * @param ids
	 * @param deleteFlag 是否真删除
	 * @return
	 */
	public Integer doRemove(String ids, Boolean deleteFlag, Boolean delAll);
	/**
	 * 取消删除
	 * @param ids
	 * @return
	 */
	public Integer doEnabled(String ids);
	/**
	 * 导入用户
	 * @param userIds
	 * @param dynaBean
	 */
	public List<DynaBean> implUsers(String userIds, DynaBean dynaBean);
	/**
	 * 部门移动
	 * @param dynaBean
	 * @return
	 */
	public DynaBean deptMove(DynaBean dynaBean, HttpServletRequest request);
	/**
	 * 部门数据导入
	 * @param dynaBean
	 * @return
	 */
	public DynaBean implDeptData(DynaBean dynaBean);
	/**
	 * 获取层级关系树形
	 * @param dicInfoVo
	 * @return
	 */
	public JSONTreeNode getCjglTree(DicInfoVo dicInfoVo);
	/**
	 * 公司信息级联
	 * @param jtgsIds
	 */
	public void syncCompanyInfo(String jtgsIds);

	/**
	 * 同步用户的是否主管信息
	 * @param dept
	 */
	public void syncUserZgld(DynaBean dept);
}
