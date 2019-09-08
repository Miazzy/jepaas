package com.je.rbac.service;

import com.je.core.util.bean.DynaBean;
import com.je.wf.processVo.WfEventSubmitInfo;

/**
 * TODO 暂不明确
 */
public interface RoleUserPermManager {
    /**
     * 保存用户申请的角色权限信息
     * @param mainId 主要id
     * @param roleId 角色id
     * @param permIds 目标id
     * @param startWf TODO 暂不明确
     * @return
     */
    public DynaBean doSaveRolePerm(String mainId, String roleId, String permIds, boolean startWf);
    /**
     * 用户申请完
     * @param eventInfo 获取流程结束信息
     */
    public void doEndUserPerm(WfEventSubmitInfo eventInfo);
}
