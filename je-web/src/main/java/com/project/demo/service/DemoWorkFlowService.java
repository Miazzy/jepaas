package com.project.demo.service;

import com.je.core.util.bean.DynaBean;
import com.je.wf.processVo.WfAssgineSubmitInfo;
import com.je.wf.processVo.WfEventSubmitInfo;

import java.util.List;

/**
 *  工作流处理
 */
public interface DemoWorkFlowService {

    /**
     * 流程开始事件
     *  taskId              任务ID
     *  currentTaskName     任务名称
     *  targerTaskName      目标任务
     *  targerTransition    目标路线
     *  submitType          提交类型（通过或退回）
     *  submitComments      提交意见
     *  assigeeName         目标名称
     *  assigeeCode         目标编码
     *  assigeeId           目标主键
     *  funcCode            功能编码
     *  demoBean            执行Bean
     * @param eventInfo 工作流封装的参数
     */
    public void doWfstarted(WfEventSubmitInfo eventInfo);

    /**
     * 工作流 任务动作 执行事件
     *  taskId              任务ID
     *  currentTaskName     任务名称
     *  targerTaskName      目标任务
     *  targerTransition    目标路线
     *  submitType          提交类型（通过或退回）
     *  submitComments      提交意见
     *  assigeeName         目标名称
     *  assigeeCode         目标编码
     *  assigeeId           目标主键
     *  funcCode            功能编码
     *  demoBean            执行Bean
     * @param eventInfo 工作流封装的参数
     * @param eventInfo
     */
    public void doWfBeforeSubmitting(WfEventSubmitInfo eventInfo);

    /**
     * 工作流自定义人员获取事件
     *  processInfo     流程部署信息
     *  currentTask     当前执行节点
     *  demoBean        业务Bean
     *  pdid            流程PDID
     *  piid            流程PIID
     *  currentUser     当前执行人
     * @param eventInfo 工作流封装的参数
     */
    public List<DynaBean> doWfAccessPersonnel(WfAssgineSubmitInfo eventInfo);

}
