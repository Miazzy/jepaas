package com.project.demo.service;

import com.je.task.vo.TimedTaskParamsVo;

/**
 *  定时任务
 */
public interface DemoTimedService {

    /**
     * 无参定时任务
     */
    public void doTimingQuery();

    /**
     * 有参数定时任务
     *  taskId               实例ID
     *  taskName             实例名称
     *  fireTime             开始时间
     *  Map(String、String)  自定义参数(arg0、arg1)
     * @param paramsVo 传入的参数
     */
    public void doTimingQuery(TimedTaskParamsVo paramsVo);

}
