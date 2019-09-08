package com.project.demo.service;

/**
 *  按钮接口
 */
public interface DemoMenuService {

    /**
     *  取表中数据的条数
     *  在菜单项上启用数字提醒，
     *  数据来源选择Action
     *  来源内容：menuService,getCount
     * @return
     */
    public long getCount();

}
