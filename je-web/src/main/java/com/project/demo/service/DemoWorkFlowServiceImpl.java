package com.project.demo.service;

import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.bean.DynaBean;
import com.je.wf.processVo.WfAssgineSubmitInfo;
import com.je.wf.processVo.WfEventSubmitInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *  工作流处理
 */
@Component("demoWorkFlowService")
public class DemoWorkFlowServiceImpl implements DemoWorkFlowService {

    @Autowired
    private PCDynaServiceTemplate serviceTemplate;

    /**
     *  流程事件
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
    @Override
    public void doWfstarted(WfEventSubmitInfo eventInfo) {
        // 获取当前业务表数据
        DynaBean dynaBean = eventInfo.getDynaBean();
        //比如表中有name字段，将name字段重新赋值
        dynaBean.set("name", 123);
        //更新当前业务表的数据
        serviceTemplate.update(dynaBean);
        System.out.println("流程事件执行完毕");
        //如果想修改其他表中的数据，先获取表信息，然后修改相应的字段插入就行了
        DynaBean bean = new DynaBean("表编码", true);
        //如果是插入新数据，有编号自动生成情况的,获取自动生成字段的编号
        String RKDBH = serviceTemplate.buildCode("表编码", "自动生成编号的字段", bean);
        //插入新数据要执行一下buildModelCreateInfo方法，参数为表获取的实例bean，然后在insert，目的为了填充某些字段有默认值的情况
        serviceTemplate.buildModelCreateInfo(bean);
        //保存数据
        serviceTemplate.update(bean);
    }

    /**
     * 工作流 任务动作 执行事件（提交之前）
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
    @Override
    public void doWfBeforeSubmitting(WfEventSubmitInfo eventInfo) {
        // 获取当前业务表数据
        DynaBean dynaBean = eventInfo.getDynaBean();
        //比如表中有name字段，将name字段重新赋值
        dynaBean.set("name", 123);
        //更新当前业务表的数据
        serviceTemplate.update(dynaBean);
        System.out.println("任务动作执行完毕");
        //如果想修改其他表中的数据，先获取表信息，然后修改相应的字段插入就行了
        DynaBean bean = new DynaBean("表编码", true);
        //如果是插入新数据，有编号自动生成情况的,获取自动生成字段的编号
        String RKDBH = serviceTemplate.buildCode("表编码", "自动生成编号的字段", bean);
        //插入新数据要执行一下buildModelCreateInfo方法，参数为表获取的实例bean，然后在insert，目的为了填充某些字段有默认值的情况
        serviceTemplate.buildModelCreateInfo(bean);
        //保存数据
        serviceTemplate.update(bean);
    }

    /**
     * 工作流自定义人员获取事件，用于场景：例如节点选人时
     *  processInfo     流程部署信息
     *  currentTask     当前执行节点
     *  demoBean        业务Bean
     *  pdid            流程PDID
     *  piid            流程PIID
     *  currentUser     当前执行人
     * @param eventInfo 工作流封装的参数
     */
    @Override
    public List<DynaBean> doWfAccessPersonnel(WfAssgineSubmitInfo eventInfo) {
        // 给指定条件的用户
        List<DynaBean> users = serviceTemplate.selectList("表编码",
                " AND USERCODE='admin'");
        // 如果不查询则指定 用户三个属性即可 USERNAME USERCODE USERID
        List<DynaBean> user = new ArrayList<DynaBean>();
        DynaBean us = new DynaBean("JE_CORE_ENDUSER", true);
        us.set("USERID", "");
        us.set("USERCODE", "");
        us.set("USERNAME", "");
        user.add(us);   // 返回us也可以实现
        System.out.println("动态获取人员时间已执行..");
        return users;
    }

}
