package com.je.calendar.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.DateUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.TreeUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;

/**
 * 日历接口实现类
 */
@Component("calendarManager")
public class CalendarManagerImpl implements CalendarManager {
	@Autowired
	private PCDynaServiceTemplate serviceTemplate;

	/**
	 * 获取我的任务树形
	 * @param currentUser
	 * @return
	 */
	@Override
	public JSONTreeNode getMyTaskTree(EndUser currentUser) {
		// TODO Auto-generated method stub
		//最近两个月的数据
		Date nowDate=new Date();
		Integer year=nowDate.getYear()+1900;
		Integer month=nowDate.getMonth()+1;
		String start=DateUtils.formatDate(DateUtils.getDate(DateUtils.getFirstDayOfMonth(year, month-1)))+" 00:00:00";
		String end=DateUtils.formatDate(DateUtils.getDate(DateUtils.getFirstDayOfMonth(year, month+1)))+" 00:00:00";
		List<DynaBean> tasks=serviceTemplate.selectList("JE_SYS_CALENDAR", " AND SY_CREATEUSER='"+currentUser.getUserCode()+"' AND CALENDAR_STARTTIME>='"+start+"'  AND CALENDAR_STARTTIME<'"+end+"' ORDER BY CALENDAR_STARTTIME DESC");
		JSONTreeNode root=TreeUtil.buildRootNode();
		List<JSONTreeNode> lists=new ArrayList<JSONTreeNode>();
		for(DynaBean task:tasks){
			JSONTreeNode taskNode=TreeUtil.buildTreeNode(task.getPkValue(), task.getStr("CALENDAR_TITLE"), "", task.getStr("CALENDAR_NOTES"), task.getStr("CALENDAR_CALENDARTYPE_CODE"), "jeicon jeicon-task", root.getId());
			if(!"-1".equals(task.getStr("CALENDAR_REMINDER_CODE"))){
				Integer remind=task.getInt("CALENDAR_REMINDER_CODE");
				Date startTime=DateUtils.getDate(task.getStr("CALENDAR_STARTTIME"), DateUtils.DAFAULT_DATETIME_FORMAT);
				Date endTime=DateUtils.getDate(task.getStr("CALENDAR_ENDTIME"), DateUtils.DAFAULT_DATETIME_FORMAT);
				if("1".equals(task.getStr("CALENDAR_YESORNO_CODE"))){
					startTime=DateUtils.clearTime(startTime);
					endTime.setHours(23);
					endTime.setMinutes(59);
					endTime.setSeconds(59);
				}
				if(!nowDate.after(endTime)){
					startTime.setMinutes(startTime.getMinutes()-remind);
					if(nowDate.after(startTime)){
						taskNode.setIconCls("JE_CORE_NZPLANTX16");
					}
				}
			}
			taskNode.setBean(task.getValues());
			taskNode.setLeaf(true);
			lists.add(taskNode);
		}
		root.setChildren(lists);
		return root;
	}

	/**
	 * 获取组树形
	 * @param currentUser
	 * @return
	 */
	@Override
	public JSONTreeNode getGroupTree(EndUser currentUser) {
		// TODO Auto-generated method stub
		//找到权限
//		List<DynaBean> groupUsers=serviceTemplate.selectList("JE_SYS_GROUPUSER"," AND GROUPUSER_USERID='"+currentUser.getId()+"'");
//		Map<String,String> permMap=new HashMap<String,String>();
//		for(DynaBean groupUser:groupUsers){
//			permMap.put(groupUser.getStr("GROUPUSER_GROUP_ID"), groupUser.getStr("GROUPUSER_PERM"));
//		}
//		String[] groupIds=ArrayUtils.getArray(permMap.keySet());
		List<DynaBean> groups=serviceTemplate.selectList("JE_SYS_CALENDARGROUP", " AND (SY_CREATEUSER='"+currentUser.getUserCode()+"' OR JE_SYS_CALENDARGROUP_ID IN (SELECT GROUPUSER_GROUP_ID FROM JE_SYS_GROUPUSER WHERE GROUPUSER_USERID='"+currentUser.getId()+"'))");
		JSONTreeNode root=TreeUtil.buildRootNode();
		List<JSONTreeNode> lists=new ArrayList<JSONTreeNode>();
//		Boolean haveDefault=false;
		for(DynaBean group:groups){
//			if("1".equals(group.getStr("CALENDARGROUP_DEFAULT")) && currentUser.getUserCode().equals(group.getStr("SY_CREATEUSER"))){
//				haveDefault=true;
//			}else{
//			group.setStr("CALENDARGROUP_DEFAULT","0");
//			}
			JSONTreeNode groupNode=TreeUtil.buildTreeNode(group.getPkValue(), group.getStr("CALENDARGROUP_NAME"), group.getStr("CALENDARGROUP_COLOR"),"INSERT,UPDATE,DELETE",group.getStr("CALENDARGROUP_DEFAULT"), "jeicon jeicon-group", root.getId());
//			if(permMap.containsKey(group.getStr("JE_SYS_CALENDARGROUP_ID"))){
//				groupNode.setNodeInfo(permMap.get(group.getStr("JE_SYS_CALENDARGROUP_ID")));
//			}
			groupNode.setDescription(group.getStr("CALENDARGROUP_REMARK"));
			groupNode.setLeaf(true);
			groupNode.setBean(group.getValues());
			//根据创建人  图标更换
			lists.add(groupNode);
		}
		//不存在默认组  则创建默认组
//		if(groups.size()<=0 || !haveDefault){
//			DynaBean defaultGroup=new DynaBean("JE_SYS_CALENDARGROUP",false);
//			defaultGroup.set(BeanUtils.KEY_PK_CODE, "JE_SYS_CALENDARGROUP_ID");
//			defaultGroup.set("CALENDARGROUP_NAME", "默认组");
//			defaultGroup.set("CALENDARGROUP_DEFAULT", "1");
//			serviceTemplate.buildModelCreateInfo(defaultGroup);
//			defaultGroup=serviceTemplate.insert(defaultGroup);
//			JSONTreeNode groupNode=TreeUtil.buildTreeNode(defaultGroup.getPkValue(), defaultGroup.getStr("CALENDARGROUP_NAME"), "INSERT,UPDATE,DELETE", defaultGroup.getStr("CALENDARGROUP_COLOR"), defaultGroup.getStr("CALENDARGROUP_DEFAULT"), "", root.getId());
//			groupNode.setBean(defaultGroup.getValues());
//			lists.add(groupNode);
//		}
		root.setChildren(lists);
		return root;
	}

	/**
	 * 获取共享任务树形
	 * @param currentUser
	 * @return
	 */
	@Override
	public JSONTreeNode getShareTaskTree(EndUser currentUser) {
		// TODO Auto-generated method stub
//		//找到权限
//		List<DynaBean> groupUsers=serviceTemplate.selectList("JE_SYS_GROUPUSER"," AND GROUPUSER_USERID='"+currentUser.getId()+"'");
//		Map<String,String> permMap=new HashMap<String,String>();
//		for(DynaBean groupUser:groupUsers){
//			permMap.put(groupUser.getStr("GROUPUSER_GROUP_ID"), groupUser.getStr("GROUPUSER_PERM"));
//		}
//		String[] groupIds=ArrayUtils.getArray(permMap.keySet());
		JSONTreeNode root=TreeUtil.buildRootNode();
//		if(groupIds.length>0){
		List<JSONTreeNode> lists=new ArrayList<JSONTreeNode>();
		Date nowDate=new Date();
		Integer year=nowDate.getYear()+1900;
		Integer month=nowDate.getMonth()+1;
		String start=DateUtils.formatDate(DateUtils.getDate(DateUtils.getFirstDayOfMonth(year, month-1)))+" 00:00:00";
		String end=DateUtils.formatDate(DateUtils.getDate(DateUtils.getFirstDayOfMonth(year, month+1)))+" 00:00:00";
		
		List<DynaBean> shareTasks=serviceTemplate.selectList("JE_SYS_CALENDAR", " AND CALENDAR_STARTTIME>='"+start+"'  AND CALENDAR_STARTTIME<'"+end+"' AND SY_CREATEUSER!='"+currentUser.getUserCode()+"' AND (CALENDAR_GROUPID IN (SELECT GROUPUSER_GROUP_ID FROM JE_SYS_GROUPUSER WHERE GROUPUSER_USERID='"+currentUser.getId()+"') OR CALENDAR_GROUPID IN (SELECT JE_SYS_CALENDARGROUP_ID FROM JE_SYS_CALENDARGROUP WHERE SY_CREATEUSER='"+currentUser.getUserCode()+"')) ORDER BY SY_CREATETIME DESC");
		for(DynaBean shareTask:shareTasks){
			JSONTreeNode taskNode=TreeUtil.buildTreeNode(shareTask.getPkValue(), shareTask.getStr("CALENDAR_TITLE"), "", shareTask.getStr("CALENDAR_NOTES"), shareTask.getStr("CALENDAR_CALENDARTYPE_CODE"), "jeicon jeicon-task", root.getId());
			taskNode.setLeaf(true);
			taskNode.setBean(shareTask.getValues());
			lists.add(taskNode);
		}
		root.setChildren(lists);
//		}
		return root;
	}

	/**
	 * 导入组人员
	 * @param groupId
	 * @param ids
	 * @return
	 */
	@Override
	public List<DynaBean> addGroupUsers(String groupId,String ids){
		List<DynaBean> users=serviceTemplate.selectList("JE_CORE_ENDUSER"," AND USERID IN ("+StringUtil.buildArrayToString(ids.split(","))+")");
		List<DynaBean> groupUsers=new ArrayList<DynaBean>();
		for(DynaBean user:users){
			DynaBean groupUser=new DynaBean("JE_SYS_GROUPUSER",false);
			groupUser.set(BeanUtils.KEY_PK_CODE, "JE_SYS_GROUPUSER");
			groupUser.set("GROUPUSER_USERCODE", user.getStr("USERCODE"));
			groupUser.set("GROUPUSER_PERM", "INSERT,UPDATE");
			groupUser.set("GROUPUSER_USERID", user.getStr("USERID"));
			groupUser.set("GROUPUSER_USERNAME", user.getStr("USERNAME"));
			groupUser.set("GROUPUSER_DEPTNAME", user.getStr("DEPTNAME"));
			groupUser.set("GROUPUSER_DEPTCODE", user.getStr("DEPTCODE"));
			groupUser.set("GROUPUSER_DEPTID", user.getStr("DEPTID"));
			groupUser.set("GROUPUSER_GROUP_ID", groupId);
			serviceTemplate.buildModelCreateInfo(groupUser);
			groupUsers.add(serviceTemplate.insert(groupUser));
		}
		return groupUsers;
	}
}
