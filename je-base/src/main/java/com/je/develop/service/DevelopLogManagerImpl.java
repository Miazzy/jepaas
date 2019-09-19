package com.je.develop.service;

import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.SecurityUserHolder;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("developLogManager")
public class DevelopLogManagerImpl implements DevelopLogManager {

    @Autowired
    private PCDynaServiceTemplate serviceTemplate;

    @Override
    public long getMenuNum(String menuCode) {
        long count=0;
        //资源表
        if("SYS_TABLE".equals(menuCode)){
            count=serviceTemplate.selectCount("JE_CORE_RESOURCETABLE"," AND SY_JECORE!='1' AND RESOURCETABLE_TYPE IN ('PT','VIEW','TREE')");
         //功能
        }else if("FUNCCFG_SUBSYSTEM".equals(menuCode)){
            count=serviceTemplate.selectCount("JE_CORE_FUNCINFO"," AND SY_JECORE!='1' AND FUNCINFO_NODEINFOTYPE IN ('FUNC','FUNCFIELD')");
        //菜单
        }else if("SYS_MENU".equals(menuCode)){
            count=serviceTemplate.selectCount("JE_CORE_MENU"," AND SY_JECORE!='1' AND MENU_NODEINFOTYPE!='MENU'");
         //工作流
        }else if("JE_CORE_PROCESSINFO".equals(menuCode)){
            count=serviceTemplate.selectCount("JE_CORE_PROCESSINFO","  AND PROCESSINFO_LASTVERSION='none'");
        //数据字典
        }else if("JE_CORE_DICTIONARY".equals(menuCode)){
            count=serviceTemplate.selectCount("JE_CORE_DICTIONARY"," AND SY_JECORE!='1'");
        }else if("JE_CORE_CHARTS".equals(menuCode)){
            count=serviceTemplate.selectCount("JE_CORE_REPORT"," AND SY_DISABLED='0' AND SY_JECORE!='1' ");
            long chartCount=serviceTemplate.selectCount("JE_CORE_CHARTS"," AND SY_DISABLED='0' AND SY_JECORE!='1'");
            count+=chartCount;
        }else if("JE_SYS_TIMEDTASK".equals(menuCode)){
            count=serviceTemplate.selectCount("JE_SYS_TIMEDTASK","");
        }
        return count;
    }

    @Override
    public void doDevelopLog(String act, String actName, String type, String typeName, String name, String code, String id) {
        if(!"1".equals(WebUtils.getSysVar("JE_SYS_DEVELOPLOG")))return;
        DynaBean log=new DynaBean("JE_CORE_DEVELOPLOG",true);
        EndUser currentUser= SecurityUserHolder.getCurrentUser();
        log.set("DEVELOPLOG_USERNAME",currentUser.getUsername());
        log.set("DEVELOPLOG_USERID",currentUser.getUserId());
        log.set("DEVELOPLOG_TYPE_NAME",typeName);
        log.set("DEVELOPLOG_TYPE_CODE",type);
        log.set("DEVELOPLOG_ACT_NAME",actName);
        log.set("DEVELOPLOG_ACT_CODE",act);
        log.set("DEVELOPLOG_NAME",name);
        log.set("DEVELOPLOG_CODE",code);
        log.set("DEVELOPLOG_ID",id);
        if("LOGIN".equals(act)){
            log.set("DEVELOPLOG_USERNAME",name);
            log.set("DEVELOPLOG_USERID",id);
            log.set("DEVELOPLOG_NAME","");
            log.set("DEVELOPLOG_CODE","");
            log.set("DEVELOPLOG_ID","");
        }
        serviceTemplate.buildModelCreateInfo(log);
        serviceTemplate.insert(log);
    }
}
