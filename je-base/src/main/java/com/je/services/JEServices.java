package com.je.services;

public interface JEServices {
	/**
	 * 存储客户启动日志
	 * @param startTime 开始时间
	 * @param title  标题
	 * @param bqxx 版权信息 
	 * @param czxt 操作系统
	 * @param javaBb java版本
	 * @param tableNum 表个数
	 * @param processNum 流程个数
	 * @param deptNum 部门个数
	 * @param yhNum 人员个数
	 * @param yhCompany 用户公司
	 * @param ipDz
	 */
	public void doStartLog(String startTime, String title, String bqxx, String czxt, String javaBb, Long tableNum, Long processNum, Long deptNum, Long yhNum, String yhCompany, String country, String province, String city, String macDz, String ipDz);
	/**
	 * 存储客户启动日志
	 * @param startTime 开始时间
	 * @param title  标题
	 * @param bqxx 版权信息 
	 * @param czxt 操作系统
	 * @param javaBb java版本
	 * @param tableNum 表个数
	 * @param processNum 流程个数
	 * @param deptNum 部门个数
	 * @param yhNum 人员个数
	 * @param yhCompany 用户公司
	 * @param type
	 */
	public void doStartLogInfo(String startTime, String title, String bqxx, String czxt, String javaBb, Long tableNum, Long processNum, Long deptNum, Long yhNum, String yhCompany, String country, String province, String city, String macDz, String ipDz, String type);
}
