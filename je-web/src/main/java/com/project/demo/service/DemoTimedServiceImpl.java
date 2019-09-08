package com.project.demo.service;

import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.bean.DynaBean;
import com.je.task.vo.TimedTaskParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component("demoTimedService")
public class DemoTimedServiceImpl implements DemoTimedService {

    @Autowired
    private PCDynaServiceTemplate serviceTemplate;

    /**
     *  页面操作
     *  进入定时任务界面，创建，
     *  填写基本信息，
     *  执行策略：如果SQL直接能满足需求，直接选择执行SQL即可，如若不能满足，选择执行方法
     *  执行Bean：demoTimedService
     *  执行方法：doTask
     *  执行次数：看具体情况
     *  参数：看具体情况
     */
    /**
     * 无参定时任务
     */
    @Override
    public void doTimingQuery() {
        //直接获取DynaBean，
        DynaBean bean = new DynaBean("表编码", true);
        //进行数据的赋值或者修改操作
        bean.set("CSB_BM", 111);
        bean.set("CSB_MC", 222);
        //调用修改方法，修改
        serviceTemplate.update(bean);
        System.out.println("无参数定时任务执行完...........");
    }

    /**
     * 有参数定时任务
     *  taskId               实例ID
     *  taskName             实例名称
     *  fireTime             开始时间
     *  Map(String、String)  自定义参数(arg0、arg1)
     * @param paramsVo 传入的参数
     */
    @Override
    public void doTimingQuery(TimedTaskParamsVo paramsVo) {
        //获取实例ID
        String taskId = paramsVo.getTaskId();
        //获取自定义参数
        Map<String, String> params = paramsVo.getParams();
        String arg0 = params.get("arg0"); //获取第一个参数
        String arg1 = params.get("arg1"); //获取第二个自定义参数
        //如果想修改其他表中的数据，先获取表信息，然后修改相应的字段插入就行了
        DynaBean bean = new DynaBean("表编码", true);
        //比如创建日期大于设定的日期多少天，数据为过期数据
        serviceTemplate.executeSql("update psi_csb set SY_STATUS='"+"0"+"' where '"+ new Date() +"' - SY_CREATETIME > '"+ arg0 +"'");
        System.out.println("有参数定时任务执行完...........");
    }

}
