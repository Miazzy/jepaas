package com.je.rbac.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;

public interface RbacServiceTemplate {
	/**
	 * 根据部门id查询该部门及子部门下所有人员
	 * @param deptIds 部门主键
	 * @param whereSql  人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryUserByDeptId(String deptIds, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 根据部门编码查询该部门及子部门下所有人员
	 * @param deptCodes 部门编码
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryUserByDeptCode(String deptCodes, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 统计部门下人员数量
	 * @param deptIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countUserByDeptId(String deptIds, String whereSql, Boolean cascade);
	/**
	 * 统计部门下人员数量
	 * @param deptCodes
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countUserByDeptCode(String deptCodes, String whereSql, Boolean cascade);
	/**
	 * 根据角色id查询该部门及子角色下所有人员
	 * @param roleIds 角色主键
	 * @param whereSql  人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryUserByRoleId(String roleIds, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 根据角色编码查询该部门及子角色下所有人员
	 * @param roleCodes 角色编码
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryUserByRoleCode(String roleCodes, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 统计角色下人员数量
	 * @param roleIds 角色id
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countUserByRoleId(String roleIds, String whereSql, Boolean cascade);
	/**
	 * 统计角色下人员数量
	 * @param roleCodes 角色编码
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countUserByRoleCode(String roleCodes, String whereSql, Boolean cascade);
	/**
	 * 根据岗位id查询该部门及子岗位下所有人员
	 * @param sentryIds 岗位主键
	 * @param whereSql  人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryUserBySentryId(String sentryIds, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 根据岗位编码查询该部门及子岗位下所有人员
	 * @param sentryCodes 岗位编码
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryUserBySentryCode(String sentryCodes, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 统计岗位下人员数量
	 * @param sentryIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countUserBySentryId(String sentryIds, String whereSql, Boolean cascade);
	/**
	 * 统计岗位下人员数量
	 * @param sentryCodes
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countUserBySentryCode(String sentryCodes, String whereSql, Boolean cascade);
	/**
	 * 统计所有人员数量
	 * @param whereSql 人员查询条件
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 * @return
	 */
	public List<DynaBean> queryUser(String whereSql, String queryFields);
	/**
	 * 统计所有人员数量
	 * @param whereSql 人员查询条件
	 * @return
	 */
	public Long countUser(String whereSql);
	/**
	 * 根据部门id查询该部门及子部门
	 * @param deptIds 部门主键
	 * @param whereSql  查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryDeptById(String deptIds, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 根据部门编码查询该部门及子部门
	 * @param deptCodes 部门编码
	 * @param whereSql 查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryDeptByCode(String deptCodes, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 统计部门数量
	 * @param deptIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countDeptById(String deptIds, String whereSql, Boolean cascade);
	/**
	 * 统计部门数量
	 * @param deptCodes
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countDeptByCode(String deptCodes, String whereSql, Boolean cascade);
	/**
	 * 根据角色id查询该角色及子角色
	 * @param roleIds 角色主键
	 * @param whereSql  查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryRoleById(String roleIds, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 根据角色编码查询该角色及子角色
	 * @param roleCodes 角色编码
	 * @param whereSql 查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> queryRoleByCode(String roleCodes, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 统计角色数量
	 * @param roleIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countRoleById(String roleIds, String whereSql, Boolean cascade);
	/**
	 * 统计角色数量
	 * @param roleCodes
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countRoleByCode(String roleCodes, String whereSql, Boolean cascade);
	/**
	 * 根据岗位id查询该岗位及子岗位
	 * @param sentryIds 岗位主键
	 * @param whereSql  查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> querySentryById(String sentryIds, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 根据岗位编码查询该岗位及子岗位
	 * @param sentryCodes 岗位编码
	 * @param whereSql 查询条件
	 * @param cascade 是否级联
	 * @param queryFields 查询字段   按逗号隔开，如果传空则查询全部字段
	 */
	public List<DynaBean> querySentryByCode(String sentryCodes, String whereSql, Boolean cascade, String queryFields);
	/**
	 * 统计岗位数量
	 * @param sentryIds
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countSentryById(String sentryIds, String whereSql, Boolean cascade);
	/**
	 * 统计岗位数量
	 * @param sentryCodes TODO 暂不明确
	 * @param whereSql 人员查询条件
	 * @param cascade 是否级联
	 * @return
	 */
	public Long countSentryByCode(String sentryCodes, String whereSql, Boolean cascade);

	/**
	 * 查询出用户树形
	 * @param ddInfos TODO 暂不明确
	 * @param onlyItem TODO 暂不明确
	 * @param whereSql 查询sql
	 * @return
	 */
	public List queryAppUser(JSONArray ddInfos, Boolean onlyItem, String whereSql);
}
