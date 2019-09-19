package com.je.develop.service;

public interface DevelopLogManager {
    /**
     * 获取菜单统计数字
     * @param menuCode TODO未处理
     * @return
     */
    public long getMenuNum(String menuCode);

    /**
     * 记录开发日志
     * @param act  动作  参考字典 JE_DEVELOP_ACT
     * @param actName TODO未处理
     * @param type 类型  参考字典 JE_DEVELOP_TYPE
     * @param typeName 类型名称
     * @param name 名称
     * @param code 编码
     * @param id   主键
     */
    public void doDevelopLog(String act, String actName, String type, String typeName, String name, String code, String id);
}
