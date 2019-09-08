package com.je.postil.service;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.constants.push.PushAct;
import com.je.core.constants.push.PushType;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.StringUtil;
import com.je.core.util.bean.DynaBean;
import com.je.develop.service.FunInfoManager;
import com.je.develop.vo.FuncInfo;
import com.je.message.service.PcMessageManager;
import com.je.message.vo.app.UserMsgAppInfo;
import com.je.portal.service.PortalManager;
import com.je.postil.vo.PostilVo;
import com.je.rbac.model.EndUser;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批注信息
 */
@Component("postilService")
public class PostilServiceImpl implements  PostilService {
    @Autowired
    private PCDynaServiceTemplate serviceTemplate;
    @Autowired
    private PcMessageManager pcMessageManager;
    @Autowired
    private PCServiceTemplate pcServiceTemplate;
    @Autowired
    private PortalManager portalManager;
    @Autowired
    private FunInfoManager funInfoManager;

    /**
     * 保存批注实现类
     * @param dynaBean 动态类
     * @param currentUser 当前登陆人
     * @return
     */
    @Override
    public List<DynaBean> savePostil(DynaBean dynaBean, EndUser currentUser) {
        List<DynaBean> lists=new ArrayList<>();
        String pkValue=dynaBean.getStr("POSTIL_MODELID");

        if(StringUtil.isNotEmpty(dynaBean.getStr("POSTIL_HFRID", ""))) {
            String[] hfrIds = dynaBean.getStr("POSTIL_HFRID", "").split(",");
            String[] hfrNames = dynaBean.getStr("POSTIL_HFR", "").split(",");
            pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_POSTIL WHERE POSTIL_HFRID IN ("+StringUtil.buildArrayToString(hfrIds)+") AND POSTIL_TABLECODE='JE_CORE_POSTIL' AND POSTIL_MODELID='"+dynaBean.getStr("POSTIL_MODELID")+"'");
            for (int i = 0; i < hfrIds.length; i++) {
                String hfrId = hfrIds[i];
                String hfrName = hfrNames[i];
                DynaBean postil = new DynaBean("JE_CORE_POSTIL", true);
                postil.set("POSTIL_PZ",dynaBean.getStr("POSTIL_PZ"));
                postil.set("POSTIL_FUNCID",dynaBean.getStr("POSTIL_FUNCID"));
                postil.set("POSTIL_HFR",hfrName);
                postil.set("POSTIL_HFRID",hfrId);
                postil.set("POSTIL_FUNCCODE",dynaBean.getStr("POSTIL_FUNCCODE"));
                postil.set("POSTIL_TABLECODE",dynaBean.getStr("POSTIL_TABLECODE"));
                postil.set("POSTIL_MODELID",dynaBean.getStr("POSTIL_MODELID"));
                postil.set("POSTIL_FUNCNAME",dynaBean.getStr("POSTIL_FUNCNAME"));
                postil.set("POSTIL_FUNCNR",dynaBean.getStr("POSTIL_FUNCNR"));
                postil.set("POSTIL_FUNCCLS",dynaBean.getStr("POSTIL_FUNCCLS"));
                postil.set("SY_PARENT",dynaBean.getStr("SY_PARENT"));
                serviceTemplate.buildModelCreateInfo(postil);
                postil=serviceTemplate.insert(postil);
                lists.add(postil);
                String title = postil.getStr("SY_CREATEUSERNAME") + "在" + postil.getStr("SY_CREATETIME") + "，功能【" + postil.getStr("POSTIL_FUNCNAME") + "】给您回复了一条内容!";
                StringBuffer context = new StringBuffer();
                context.append(postil.getStr("SY_CREATEUSERNAME") + "在" + postil.getStr("SY_CREATETIME") + "，功能【<a href=\"javascript:void(0)\" onclick=\"javascript:JE.showFunc('" + dynaBean.getStr("POSTIL_FUNCCODE", "") + "',{type:'form',id:'" + dynaBean.getStr("POSTIL_MODELID", "") + "'});\">" + postil.getStr("POSTIL_FUNCNAME") + "</a>】给您回复了一条内容，");
                context.append("内容：" + dynaBean.getStr("POSTIL_PZ") + "，");
                context.append("详细信息请点击,<a href=\"javascript:void(0)\" onclick=\"javascript:JE.CoreUtil.showPostil({funcCode:'" + dynaBean.getStr("POSTIL_FUNCCODE", "") + "',pkValue:'" + dynaBean.getStr("POSTIL_MODELID", "") + "',userId:'" + currentUser.getUserId() + "'});\">查看</a>!");

                //发送消息提醒
                if (!currentUser.getUserId().equals(hfrId)) {
                    pcMessageManager.sendDwr(hfrId, title, context.toString(), "JE.showMsg", "JE.showBatchMsg", true);
                    List<UserMsgAppInfo> userMsgAppInfos=new ArrayList<>();
                    net.sf.json.JSONObject params=new net.sf.json.JSONObject();
                    params.put("pkValue",dynaBean.getStr("POSTIL_MODELID", ""));
                    UserMsgAppInfo appInfo=new UserMsgAppInfo(postil.getStr("POSTIL_FUNCNAME"),"APPFUNC",dynaBean.getStr("POSTIL_FUNCCODE", ""),"form",params);
                    userMsgAppInfos.add(appInfo);
                    pcMessageManager.sendDwr(hfrId, title, context.toString(), "JE.showMsg", "JE.showBatchMsg", true);
                    pcMessageManager.sendUserMsg(hfrId,title, context.toString(), "PZ",userMsgAppInfos);
                }
            }
        }else{
            serviceTemplate.buildModelCreateInfo(dynaBean);
            dynaBean = serviceTemplate.insert(dynaBean);
            lists.add(dynaBean);
        }
        //找到批注所有的人然后置未读 推送数字
        List<Map> pzs=serviceTemplate.queryMapBySql(" SELECT SY_CREATEUSERID FROM JE_CORE_POSTIL WHERE POSTIL_FUNCCODE='"+dynaBean.getStr("POSTIL_FUNCCODE")+"' AND POSTIL_TABLECODE='"+dynaBean.getStr("POSTIL_TABLECODE")+"' AND POSTIL_MODELID='"+pkValue+"' GROUP BY SY_CREATEUSERID");
        serviceTemplate.executeSql(" UPDATE JE_CORE_FUNCEDIT SET FUNCEDIT_NEW='2' WHERE  FUNCEDIT_FUNCCODE='JE_CORE_POSTIL' AND FUNCEDIT_PKVALUE='"+pkValue+"'");
        for(Map pz:pzs){
            String uId=pz.get("SY_CREATEUSERID")+"";
            portalManager.push(uId,new String[]{PushType.POSTIL},new String[]{PushAct.ALL});
        }
        return lists;
    }

    /**
     * 加载批注实现类
     * @param type 类型
     * @param userId 用户id
     * @param keyWord TODO 暂不明确
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param start 开始页
     * @param limit 没页条数
     * @param returnObj 返回参数
     * @return
     */
    @Override
    public List<PostilVo> loadPostils(String type,String userId, String keyWord, String startDate, String endDate, int start, int limit, JSONObject returnObj) {
        StringBuffer whereSql=new StringBuffer();
        String  timeThereSql = "";

        whereSql.append(" AND (SY_CREATEUSERID='"+userId+"' OR POSTIL_HFRID='"+userId+"') ");
        //加入关键字过滤
        if(StringUtil.isNotEmpty(keyWord)){
            whereSql.append(" AND (POSTIL_FUNCNR LIKE '%"+keyWord+"%' OR POSTIL_PZ LIKE '%"+keyWord+"%' OR SY_CREATEUSERNAME LIKE '%"+keyWord+"%')");
        }
        if(StringUtil.isNotEmpty(startDate) && StringUtil.isNotEmpty(endDate)){
            timeThereSql = " AND SY_CREATETIME>='"+startDate+" 00:00:00' AND SY_CREATETIME <='"+endDate+" 12:59:59'";
            whereSql.append(timeThereSql);
        }
        if("YD".equals(type)){
            whereSql.append(" AND POSTIL_MODELID IN (SELECT FUNCEDIT_PKVALUE FROM JE_CORE_FUNCEDIT WHERE FUNCEDIT_USERID='"+userId+"' AND FUNCEDIT_FUNCCODE='JE_CORE_POSTIL' AND FUNCEDIT_NEW='0')");
        }else if("WD".equals(type)){
            whereSql.append(" AND POSTIL_MODELID NOT IN (SELECT FUNCEDIT_PKVALUE FROM JE_CORE_FUNCEDIT WHERE FUNCEDIT_USERID='"+userId+"' AND FUNCEDIT_FUNCCODE='JE_CORE_POSTIL' AND FUNCEDIT_NEW='0')");
        }
//        if(StringUtil.isNotEmpty(startDate) && StringUtil.isNotEmpty(endDate)){
//            whereSql.append(" AND SY_CREATETIME>='"+(startDate+" 00:00:00")+"' AND SY_CREATETIME<='"+(endDate+" 23:59:59")+"'");
//        }
        String querySql=" SELECT POS.POSTIL_FUNCCODE,POS.POSTIL_TABLECODE,POS.POSTIL_MODELID,POS.POSTILCOUNT,POS.SY_CREATETIME FROM (SELECT POSTIL_FUNCCODE,POSTIL_TABLECODE,POSTIL_MODELID,COUNT(*) POSTILCOUNT,MAX(SY_CREATETIME) SY_CREATETIME FROM JE_CORE_POSTIL WHERE 1=1 "+whereSql.toString()+" GROUP BY POSTIL_FUNCCODE,POSTIL_TABLECODE,POSTIL_MODELID) POS ORDER BY POS.SY_CREATETIME DESC";
        String countSql=" SELECT POSTIL_FUNCCODE,POSTIL_TABLECODE,POSTIL_MODELID,COUNT(*) POSTILCOUNT,MAX(SY_CREATETIME) SY_CREATETIME FROM JE_CORE_POSTIL WHERE 1=1 "+whereSql.toString()+" GROUP BY POSTIL_FUNCCODE,POSTIL_TABLECODE,POSTIL_MODELID ";
        String groupPkSql=" SELECT POSTIL_MODELID FROM JE_CORE_POSTIL WHERE 1=1  AND (SY_CREATEUSERID='"+userId+"' OR POSTIL_HFRID='"+userId+"') "+timeThereSql+"  GROUP BY POSTIL_TABLECODE,POSTIL_MODELID";
        //拼装whereSql
        List<Map> lists=pcServiceTemplate.queryMapBySql(querySql,new String[]{},start,limit);
        Long totalCount=new Long(lists.size());
        if (limit > 0) {
            totalCount = pcServiceTemplate.countMapBySql(countSql , new ArrayList<DbFieldVo>());
        }
        //无查询所有数量
        Long allCount=totalCount;
        if(StringUtil.isNotEmpty(keyWord) || !"ALL".equals(type)) {
            allCount=pcServiceTemplate.countMapBySql(groupPkSql, new ArrayList<DbFieldVo>());
        }
        List<PostilVo> postilVos=new ArrayList<>();
        List<String> ids=new ArrayList<>();
        for(Map obj:lists){
            String tableCode=obj.get("POSTIL_TABLECODE")+"";
            String pkValue=obj.get("POSTIL_MODELID")+"";
            String funcCode=obj.get("POSTIL_FUNCCODE")+"";
            String createTime=obj.get("SY_CREATETIME")+"";
            PostilVo postilVo=new PostilVo();
            //查询出最后批注的信息
            List<DynaBean> lastPostils=serviceTemplate.selectList("JE_CORE_POSTIL"," AND POSTIL_FUNCCODE='"+funcCode+"' AND POSTIL_TABLECODE='"+tableCode+"' AND POSTIL_MODELID='"+pkValue+"' ORDER BY SY_CREATETIME DESC",0,1);
            if(lastPostils.size()>0){
                DynaBean lastPostil=lastPostils.get(0);
                if(lastPostil!=null) {
                    postilVo.setLastPostil(lastPostil.getValues());
                }
            }
            Integer postilCount=Integer.parseInt(StringUtil.getDefaultValue(obj.get("POSTILCOUNT")+"","0"));
//            FuncPostilConfig postilConfig=getFuncPostilConfig(funcCode);
//            if(postilConfig==null){
//                continue;
//            }
            FuncInfo funcInfo=funInfoManager.getFuncInfo(funcCode);
            DynaBean ywBean=serviceTemplate.selectOneByPk(tableCode,pkValue);
            if(ywBean!=null){
                postilVo.setBean(ywBean.getValues());
            }
            postilVo.setFuncName(funcInfo.getFuncName());
            postilVo.setFuncCode(funcInfo.getFuncCode());
            postilVo.setFuncId(funcInfo.getFuncId());
            postilVo.setFuncIconCls(funcInfo.getIconCls());
            postilVo.setShowConfig(funcInfo.getPostilShowConfig());
            postilVo.setTitleConfig(funcInfo.getPostilTitleConfig());
            postilVo.setPostilCount(postilCount);
            postilVo.setCreateTime(createTime);
            postilVo.setPkValue(pkValue);
            postilVo.setTableCode(tableCode);
            postilVos.add(postilVo);
            ids.add(pkValue);
        }
        //处理红点编辑 没有加时间限制
        List<Map> funcEdits=pcServiceTemplate.queryMapBySql("SELECT FUNCEDIT_PKVALUE,FUNCEDIT_USERID,FUNCEDIT_NEW FROM JE_CORE_FUNCEDIT WHERE FUNCEDIT_USERID='"+userId+"' AND FUNCEDIT_FUNCCODE='JE_CORE_POSTIL' AND FUNCEDIT_PKVALUE IN ("+groupPkSql+") ");
        Map<String,String> dataVals=new HashMap<>();
        Long readCount =0L;
        Long noReadCount=0L;
        for(Map funcEdit:funcEdits){
            String modelId=funcEdit.get("FUNCEDIT_PKVALUE")+"";
            dataVals.put(modelId,funcEdit.get("FUNCEDIT_NEW")+"");
            if("0".equals(funcEdit.get("FUNCEDIT_NEW")+"")){
                readCount++;
            }
        }
        for(PostilVo vo:postilVos){
            String pkValue=vo.getPkValue();
            if(dataVals.containsKey(pkValue)){
                vo.setFuncEdit(dataVals.get(pkValue)+"");
            }else{
                vo.setFuncEdit("2");
            }
        }
        noReadCount=allCount-readCount;
        returnObj.put("allCount",allCount);
        returnObj.put("readCount",readCount);
        returnObj.put("noReadCount",noReadCount);
        returnObj.put("totalCount",totalCount);
        return postilVos;
    }

    /**
     * 设置成全部已读实现类
     * @param keyWord TODO 暂不明确
     */
    @Override
    public void readAllPostil(String keyWord) {
        EndUser currentUser=SecurityUserHolder.getCurrentUser();
        String userId=currentUser.getUserId();
        StringBuffer whereSql=new StringBuffer();
        whereSql.append(" AND (SY_CREATEUSERID='"+userId+"' OR POSTIL_HFRID='"+userId+"') ");
        //加入关键字过滤
        if(StringUtil.isNotEmpty(keyWord)){
            whereSql.append(" AND POSTIL_FUNCNR LIKE '%"+keyWord+"%'");
        }
        whereSql.append(" AND POSTIL_MODELID NOT IN (SELECT FUNCEDIT_PKVALUE FROM JE_CORE_FUNCEDIT WHERE FUNCEDIT_USERID='"+userId+"' AND FUNCEDIT_FUNCCODE='JE_CORE_POSTIL' AND FUNCEDIT_NEW='0')");
        String querySql=" SELECT POSTIL_FUNCCODE,POSTIL_TABLECODE,POSTIL_MODELID,COUNT(*) POSTILCOUNT,MAX(SY_CREATETIME) SY_CREATETIME FROM JE_CORE_POSTIL WHERE 1=1 "+whereSql.toString()+" GROUP BY POSTIL_FUNCCODE,POSTIL_TABLECODE,POSTIL_MODELID ";
        List<Map> lists=serviceTemplate.queryMapBySql(querySql);
        for(Map vals:lists){
            String pkValue=vals.get("POSTIL_MODELID")+"";
            serviceTemplate.doDataFuncEdit("JE_CORE_POSTIL","JE_CORE_POSTIL",pkValue,userId,"0");
        }
    }
}
