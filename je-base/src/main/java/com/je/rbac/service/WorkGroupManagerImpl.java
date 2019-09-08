package com.je.rbac.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.StringUtil;
import com.je.core.util.TreeUtil;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DicInfoVo;
import com.je.rbac.model.EndUser;

/**
 * TODO 暂不明确
 */
@Component("workGroupManager")
public class WorkGroupManagerImpl implements WorkGroupManager {
	private PCDynaServiceTemplate serviceTemplate;
	private PCServiceTemplate pcServiceTemplate;

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@Override
	public Integer doRemove(String ids) {
		// TODO Auto-generated method stub
		pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_WORKGROUP_USER WHERE GROUPID IN ("+StringUtil.buildArrayToString(ids.split(","))+")");
		pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_WORKGROUP WHERE JE_CORE_WORKGROUP_ID IN ("+StringUtil.buildArrayToString(ids.split(","))+")");
		return ids.split(",").length;
	}

	/**
	 * 导入人员
	 * @param userIds 用户id
	 * @param workGroupId TODO 暂不明确
	 */
	@Override
	public void implUsers(String userIds, String workGroupId) {
		// TODO Auto-generated method stub
		for(String userId:userIds.split(",")){
			String insertSql=" INSERT INTO JE_CORE_WORKGROUP_USER(GROUPID,USERID) VALUES('"+workGroupId+"','"+userId+"')";
			pcServiceTemplate.executeSql(insertSql);
		}
	}

	/**
	 * 移除人员
	 * @param userIds 用户id
	 * @param workGroupId TODO 暂不明确
	 */
	@Override
	public void removeUsers(String userIds, String workGroupId){
		// TODO Auto-generated method stub
		String insertSql=" DELETE FROM JE_CORE_WORKGROUP_USER WHERE GROUPID='"+workGroupId+"' AND USERID IN ("+StringUtil.buildArrayToString(userIds.split(","))+")";
		pcServiceTemplate.executeSql(insertSql);
	}
	@Resource(name="PCDynaServiceTemplate")
	public void setServiceTemplate(PCDynaServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}
	@Resource(name="PCServiceTemplateImpl")
	public void setPcServiceTemplate(PCServiceTemplate pcServiceTemplate) {
		this.pcServiceTemplate = pcServiceTemplate;
	}

	/**
	 * 获取当前工作组树形
	 * @param dicInfoVo TODO 暂不明确
	 * @return
	 */
	@Override
	public JSONTreeNode getWorkGroupUser(DicInfoVo dicInfoVo) {
		// TODO Auto-generated method stub
		EndUser currentUser=SecurityUserHolder.getCurrentUser();
		String whereSql=dicInfoVo.getWhereSql();
		String orderSql=dicInfoVo.getOrderSql();
		JSONTreeNode rootNode=TreeUtil.buildRootNode();
		rootNode.setId(rootNode.getId()+dicInfoVo.getIdSuffix());
		rootNode.setText(dicInfoVo.getFieldName());
		rootNode.setIconCls("jeicon jeicon-tree-query");
		rootNode.setNodeInfo(dicInfoVo.getFieldCode());
		List<DynaBean> workGroups=serviceTemplate.selectList("JE_CORE_WORKGROUP", " AND SY_CREATEUSER='"+currentUser.getUserCode()+"' "+whereSql+" "+orderSql);
		for(DynaBean workGroup:workGroups){
			JSONTreeNode groupNode=TreeUtil.buildTreeNode(workGroup.getStr("JE_CORE_WORKGROUP_ID")+dicInfoVo.getIdSuffix(), workGroup.getStr("WORKGROUP_NAME"), workGroup.getStr("WORKGROUP_CODE"), dicInfoVo.getFieldCode(), "GROUP", "jeicon jeicon-group", rootNode.getId());
			groupNode.setDisabled("1");
			groupNode.setLeaf(false);
			List<DynaBean> groupUsers=serviceTemplate.selectList("JE_CORE_ENDUSER", " AND USERID IN (SELECT USERID FROM JE_CORE_WORKGROUP_USER WHERE GROUPID='"+workGroup.getStr("JE_CORE_WORKGROUP_ID")+"')");
			for(DynaBean user:groupUsers){
				JSONTreeNode userNode=TreeUtil.buildTreeNode(workGroup.getStr("JE_CORE_WORKGROUP_ID")+"_"+user.getStr("USERID")+dicInfoVo.getIdSuffix(), user.getStr("USERNAME"), user.getStr("USERCODE"), dicInfoVo.getFieldCode(), "USER", "jeicon jeicon-khgl-pc", groupNode.getId());
				userNode.setDisabled("0");
				userNode.setLeaf(true);
				groupNode.getChildren().add(userNode);
			}
			rootNode.getChildren().add(groupNode);
		}
		return rootNode;
	}
}
