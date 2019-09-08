package com.suanban.demo;

import com.je.wf.processVo.WfEventSubmitInfo;

public interface DemoService {

    /**
     * 流程事件
     * @param eventInfo
     */
    void doDemoService(WfEventSubmitInfo eventInfo);

    /**
     * 提交之后
     * @param eventInfo
     */
    void doTaskEvent(WfEventSubmitInfo eventInfo);

}
