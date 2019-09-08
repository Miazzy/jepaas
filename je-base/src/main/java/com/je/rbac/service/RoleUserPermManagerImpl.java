package com.je.rbac.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.je.core.constants.rbac.PermType;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.DateUtils;
import com.je.core.util.JEUUID;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
import com.je.staticize.service.StaticizeManager;
import com.je.wf.processVo.WfEventSubmitInfo;
import com.je.wf.service.WfServiceTemplate;

/**
 * 暂不明确
 */
@Component("roleUserPermManager")
public class RoleUserPermManagerImpl implements RoleUserPermManager{

    private PCDynaServiceTemplate serviceTemplate;
    private WfServiceTemplate wfServiceTemplate;
    private PCServiceTemplate pcServiceTemplate;
    private StaticizeManager staticizeManager;

    /**
     * 保存用户申请的角色权限信息
     * @param mainId 主要id
     * @param roleIds
     * @param permIds 目标id
     * @param startWf TODO 暂不明确
     * @return
     */
    @Override
    public DynaBean doSaveRolePerm(String mainId, String roleIds,
                                   String permIds, boolean startWf) {
        // TODO Auto-generated method stub
        DynaBean mainBean=null;
        if(StringUtil.isEmpty(mainId)){
            mainBean=new DynaBean("JE_SYS_PERMWF",true);
            List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLEID IN ("+StringUtil.buildArrayToString(roleIds.split(","))+")");
            serviceTemplate.buildModelCreateInfo(mainBean);
            mainBean.set("PERMWF_ROLENAME",StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(roles, "ROLENAME"),","));
            mainBean.set("PERMWF_ROLEID",StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(roles, "ROLEID"),","));
            mainBean=serviceTemplate.insert(mainBean);
        }else{
            mainBean=serviceTemplate.selectOneByPk("JE_SYS_PERMWF", mainId);
            List<DynaBean> roles=serviceTemplate.selectList("JE_CORE_ROLE", " AND ROLEID IN ("+StringUtil.buildArrayToString(roleIds.split(","))+")");
            mainBean.set("PERMWF_ROLENAME",StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(roles, "ROLENAME"),","));
            mainBean.set("PERMWF_ROLEID",StringUtil.buildSplitString(ArrayUtils.getBeanFieldArray(roles, "ROLEID"),","));
            mainBean=serviceTemplate.update(mainBean);
            pcServiceTemplate.executeSql(" DELETE FROM JE_SYS_FDPERM WHERE JE_SYS_PERMWF_ID='"+mainId+"'");
        }
        mainId=mainBean.getStr("JE_SYS_PERMWF_ID");
        for(String permId:permIds.split(",")){
            if(StringUtil.isEmpty(permId))continue;
            String[] permArray=permId.split("#");
            String moduleCode=permArray[0];
            String permsType=permArray[1];
            String permCode=permArray[2];
            List<DynaBean> otherPerms=new ArrayList<DynaBean>();
            if(PermType.SUB_FUNC.equalsIgnoreCase(permsType)){
                eachChildBtns(otherPerms, permCode, moduleCode,mainId);
            }
            DynaBean permission=serviceTemplate.selectOne("JE_CORE_PERMISSION", " and PERMTYPE='"+permsType+"' and PERMCODE='"+permCode+"'");
            if(permission==null){
                permission=new DynaBean("JE_CORE_PERMISSION",false);
                permission.set(BeanUtils.KEY_PK_CODE, "PERID");
                permission.set("PERMCODE", permCode);
                permission.set("PERMTYPE", permsType);
                permission.set("MODULE", moduleCode);
                //把功能主键也放入
                if(PermType.BUTTON.equalsIgnoreCase(permsType)){
                    DynaBean button=serviceTemplate.selectOneByPk("JE_CORE_RESOURCEBUTTON",permCode);
                    if(button!=null){
                        permission.set("FUNCID", button.getStr("RESOURCEBUTTON_FUNCINFO_ID"));
                    }
                }else if(PermType.SUB_FUNC.equalsIgnoreCase(permsType)){
                    DynaBean funRelation=serviceTemplate.selectOneByPk("JE_CORE_FUNCRELATION", permCode);
                    if(funRelation!=null){
                        permission.set("FUNCID", funRelation.getStr("FUNCRELATION_FUNCINFO_ID"));
                    }
                }else if(PermType.APPCHILDFUNC.equalsIgnoreCase(permsType)){
                    permission.set("FUNCID", permArray[3]);
                }else if(PermType.APPBUTTON.equalsIgnoreCase(permsType)){
                    permission.set("FUNCID", permArray[3]);
                }else{
                    permission.set("FUNCID", "");
                }
                permission=serviceTemplate.insert(permission);
            }else{
                if(!permission.getStr("MODULE").equals(moduleCode)){
                    permission.set("MODULE", moduleCode);
                    permission=serviceTemplate.update(permission);
                }

            }
            DynaBean gx=new DynaBean("JE_SYS_FDPERM",true);
            gx.set("FDPERM_PERMID", permission.getStr("PERID"));
            gx.set("JE_SYS_PERMWF_ID", mainId);
            serviceTemplate.insert(gx);
            for(DynaBean otherPerm:otherPerms){
                DynaBean otherGx=new DynaBean("JE_SYS_FDPERM",true);
                otherGx.set("FDPERM_PERMID", otherPerm.getStr("PERID"));
                otherGx.set("JE_SYS_PERMWF_ID", mainId);
                serviceTemplate.insert(otherGx);
            }
        }
        if(startWf){
            Map<String, String> nodeComments=new HashMap<String,String>();
            Map<String, String> nodeUsers=new HashMap<String,String>();
            EndUser currentUser=SecurityUserHolder.getCurrentUser();
            nodeUsers.put("申请人", currentUser.getUserCode());
            nodeUsers.put("管理员审核", "admin");
            nodeComments.put("申请人", "请审批权限!");
            wfServiceTemplate.doSponsorProcess(mainBean, "quanxianshenqing_18071016", "JE_SYS_PERMWF", "申请人", "管理员审核", nodeUsers, nodeComments);
        }
        return mainBean;
    }
    private void eachChildBtns(List<DynaBean> lists,String childId,String moduleCode,String mainId){
        DynaBean funRelation=serviceTemplate.selectOneByPk("JE_CORE_FUNCRELATION", childId);
        List<DynaBean> buttons=serviceTemplate.selectList("JE_CORE_RESOURCEBUTTON", " AND RESOURCEBUTTON_FUNCINFO_ID='"+funRelation.getStr("FUNCRELATION_FUNCID")+"'");
        for(DynaBean button:buttons){
            DynaBean perm=serviceTemplate.selectOne("JE_CORE_PERMISSION", " and PERMTYPE='"+PermType.BUTTON+"' and PERMCODE='"+button.getStr("JE_CORE_RESOURCEBUTTON_ID")+"'");
            if(perm==null){
                perm=new DynaBean("JE_CORE_PERMISSION",true);
                perm.set(BeanUtils.KEY_PK_CODE, "PERID");
                perm.set("PERMCODE", button.getStr("JE_CORE_RESOURCEBUTTON_ID"));
                perm.set("PERMTYPE", PermType.BUTTON);
                perm.set("MODULE", moduleCode);
                perm.set("FUNCID", button.getStr("RESOURCEBUTTON_FUNCINFO_ID"));
                perm=serviceTemplate.insert(perm);
            }
            lists.add(perm);
        }
        List<DynaBean> funRelations=serviceTemplate.selectList("JE_CORE_FUNCRELATION", " AND FUNCRELATION_FUNCINFO_ID='"+funRelation.getStr("FUNCRELATION_FUNCID")+"'");
        for(DynaBean funReLation:funRelations){
            DynaBean perm=serviceTemplate.selectOne("JE_CORE_PERMISSION", " and PERMTYPE='"+PermType.SUB_FUNC+"' and PERMCODE='"+funReLation.getStr("JE_CORE_FUNCRELATION_ID")+"'");
            if(perm==null){
                perm=new DynaBean("JE_CORE_PERMISSION",true);
                perm.set(BeanUtils.KEY_PK_CODE, "PERID");
                perm.set("PERMCODE", funReLation.getStr("JE_CORE_FUNCRELATION_ID"));
                perm.set("PERMTYPE", PermType.SUB_FUNC);
                perm.set("MODULE", moduleCode);
                perm.set("FUNCID", funReLation.getStr("FUNCRELATION_FUNCINFO_ID"));
                perm=serviceTemplate.insert(perm);
                DynaBean gx=new DynaBean("JE_SYS_FDPERM",true);
                gx.set("FDPERM_PERMID", perm.getStr("PERID"));
                gx.set("JE_SYS_PERMWF_ID", mainId);
                serviceTemplate.insert(gx);
            }
            lists.add(perm);
            eachChildBtns(lists, funReLation.getStr("JE_CORE_FUNCRELATION_ID"), moduleCode,mainId);
        }

    }

    /**
     * 用户申请完
     * @param eventInfo 获取流程结束信息
     */
    @Override
    public void doEndUserPerm(WfEventSubmitInfo eventInfo) {
        // TODO Auto-generated method stub
        DynaBean mainData=eventInfo.getDynaBean();
        List<DynaBean> diyPerms=serviceTemplate.selectList("JE_SYS_FDPERM", " AND JE_SYS_PERMWF_ID='"+mainData.getStr("JE_SYS_PERMWF_ID")+"'");
        String[] roleIds=mainData.getStr("PERMWF_ROLEID","").split(",");
        for(String rId:roleIds){
            long count=serviceTemplate.selectCount("JE_CORE_ROLE_USER"," AND ROLEID='"+rId+"' AND USERID='"+mainData.getStr("SY_CREATEUSERID")+"'");
            if(count<=0){
                DynaBean roleUser=new DynaBean("JE_CORE_ROLE_USER",true);
                roleUser.set("ROLEID", rId);
                roleUser.set("USERID", mainData.getStr("SY_CREATEUSERID"));
                serviceTemplate.insert(roleUser);
            }
        }
        if(diyPerms.size()>0){
            EndUser currentUser=SecurityUserHolder.getCurrentUser();
            String roleId=JEUUID.uuid();
            DynaBean role=new DynaBean("JE_CORE_ROLE",true);
            role.set("ROLENAME", mainData.getStr("SY_CREATEUSERNAME")+"_角色");
            role.set("ROLECODE", mainData.getStr("SY_CREATEUSER")+"");
            role.set("ROLEID", roleId);
            role.set("ROLETYPE", "USER");
            role.set("ORDERINDEX", 0);
            role.set("DEPTID", mainData.getStr("SY_CREATEUSERID"));
            role.set("CREATEUSER", currentUser.getUserCode());
            role.set("CREATEUSERNAME", currentUser.getUsername());
            role.set("CREATEORG", currentUser.getDeptCode());
            role.set("CREATEORGNAME", currentUser.getDeptName());
            role.set("CREATETIME", DateUtils.formatDateTime(new Date()));
            role.set("LAYER", "1");
            role.set("STATUS", "1");
            role.set("NODETYPE", "LEAF");
            role.set("TREEORDERINDEX", "000000000001");
            role.set("ISSUPERADMIN", "0");
            role.set("MANAGER", "0");
            role.set("DEVELOP", "0");
            role.set("ROLETYPE", "DEPT");
            role.set("JTGSID", mainData.get("SY_JTGSID"));
            role.set("JTGSMC", mainData.getStr("SY_JTGSMC"));
            role.set("PARENTNAME", "ROOT");
            role.set("PARENTCODE", "ROOT");
            role.set("PARENT", "ROOT");
            role.set("PATH", "/ROOT/"+roleId);
            serviceTemplate.insert(role);
            DynaBean roleUser=new DynaBean("JE_CORE_ROLE_USER",true);
            roleUser.set("ROLEID", roleId);
            roleUser.set("USERID", mainData.getStr("SY_CREATEUSERID"));
            serviceTemplate.insert(roleUser);
            for(DynaBean diyPerm:diyPerms){
                String permId=diyPerm.getStr("FDPERM_PERMID");
                DynaBean rolePerm=new DynaBean("JE_CORE_ROLE_PERM",true);
                rolePerm.set("ROLEID", roleId);
                rolePerm.set("ENABLED", "1");
                rolePerm.set("PERID", permId);
                rolePerm.set("TYPE", "SELF");
                serviceTemplate.insert(rolePerm);
            }
            staticizeManager.clearUserStatize(" AND USERID='"+mainData.getStr("SY_CREATEUSERID")+"'",false);
        }
    }
    @Resource(name="PCDynaServiceTemplate")
    public void setServiceTemplate(PCDynaServiceTemplate serviceTemplate) {
        this.serviceTemplate = serviceTemplate;
    }
    @Resource(name="wfServiceTemplate")
    public void setWfServiceTemplate(WfServiceTemplate wfServiceTemplate) {
        this.wfServiceTemplate = wfServiceTemplate;
    }
    @Resource(name="PCServiceTemplateImpl")
    public void setPcServiceTemplate(PCServiceTemplate pcServiceTemplate) {
        this.pcServiceTemplate = pcServiceTemplate;
    }
    @Resource(name="staticizeManager")
    public void setStaticizeManager(StaticizeManager staticizeManager) {
        this.staticizeManager = staticizeManager;
    }

}
