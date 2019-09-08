package com.suanban.demo;

import com.je.wf.processVo.WfEventSubmitInfo;
import org.springframework.stereotype.Service;

@Service("demoService")
public class DemoServiceImpl implements DemoService {

    /**
     * 流程事件
     * @param eventInfo
     */
    @Override
    public void doDemoService(WfEventSubmitInfo eventInfo) {
        System.out.println("流程事件启动!");
    }

    /**
     * 任务提交之后
     * @param eventInfo
     */
    @Override
    public void doTaskEvent(WfEventSubmitInfo eventInfo) {
        System.out.println("任务提交之后");
    }
}
