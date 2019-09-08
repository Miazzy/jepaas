package com.project.demo.service;

import com.je.core.service.PCDynaServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("demoMenuService")
public class DemoMenuServiceImpl implements DemoMenuService {

    @Autowired
    private PCDynaServiceTemplate serviceTemplate;

    /**
     *  页面操作
     *  在菜单项上启用数字提醒，
     *  数据来源选择Action
     *  来源内容：demoMenuService,getCount
     */
    /**
     *  取表中数据的条数
     *  如果选择SQL，则直接写SQL语句
     * @return long类型
     */
    @Override
    public long getCount() {

        long count = (long)serviceTemplate.selectCount("JE_DEMO_XMINFO", "AND 1 = 1");

        return count;
    }

}
