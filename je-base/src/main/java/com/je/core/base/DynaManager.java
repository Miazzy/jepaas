package com.je.core.base;

import com.je.core.ann.entity.DbFieldVo;
import com.je.core.constants.ConstantVars;
import com.je.core.constants.db.DbFieldType;
import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.facade.extjs.JsonAssist;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.result.BaseRespResult;
import com.je.core.util.SecurityUserHolder;
import com.je.core.service.PCDataService;
import com.je.core.service.PCDynaBeanTemplate;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.service.PCServiceTemplate;
import com.je.core.util.ArrayUtils;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.datasource.runner.SqlRunner;
import com.je.develop.service.FunInfoManager;
import com.je.develop.vo.FuncInfo;
import com.je.message.service.PcMessageManager;
import com.je.message.vo.app.UserMsgAppInfo;
import com.je.rbac.model.EndUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * TODO 暂不明确
 */
@Component
public class DynaManager {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PCDynaBeanTemplate dynaBeanTemplate;
    @Autowired
    private PCServiceTemplate pcServiceTemplate;
    @Autowired
    private PCDataService pcDataService;
    @Autowired
    private PCDynaServiceTemplate serviceTemplate;
    @Autowired
    private FunInfoManager funInfoManager;
    @Autowired
    private PcMessageManager pcMessageManager;

    public static BeanUtils beanUtils = null;
    public static JsonAssist jsonAssist = null;
    public static JsonBuilder jsonBuilder = null;

    static {
        beanUtils = BeanUtils.getInstance();
        jsonAssist = JsonAssist.getInstance();
        jsonBuilder = JsonBuilder.getInstance();
    }

    /**
     * 默认的列表读取方法
     * @param param
     */
    public void load(MethodArgument param) {
        //构建排序条件
        String order = dynaBeanTemplate.buildOrderSql(param.getSort(), param.getOrderSql(), param.getUseOrderSql());
        DynaBean dynaBean = param.getDynaBean();

        dynaBean.set(BeanUtils.KEY_ORDER, order);
        if (StringUtil.isNotEmpty(param.getExpandSql())) {
            dynaBean.set(BeanUtils.KEY_WHERE, param.getWhereSql() + param.getExpandSql());
        }
        //如果是SQL或者存储过程查询
        String queryType = param.getQueryType();
        if(ArrayUtils.contains(new String[]{"procedure","sql","iditprocedure"},queryType)){
            Map<String,Object> returnObj=dynaBeanTemplate.doLoadOtherData(queryType,param,order);
            //如果需要返回
            if("1".equals(returnObj.get("returnFlag")+"")){
                //如果返回成功
                if("1".equals(returnObj.get("success")+"")){
                    Long count=Long.parseLong(returnObj.get("totalCount")+"");
                    List<Map> lists= (List<Map>) returnObj.get("rows");
                    String strData = jsonBuilder.buildObjListToJson(new Long(count), lists, true);
                    toWrite(strData,param.getRequest(),param.getResponse());
                }else{
                    String errorMsg=returnObj.get("msg")+"";
                    toWrite(jsonBuilder.returnFailureJson("\""+errorMsg+"\""),param.getRequest(),param.getResponse());
                }
                return;
            }
        }
        ArrayList<DynaBean> list = new ArrayList<DynaBean>();
        //查询  如果limit为-1表示不分页，查询全部数据
        if (StringUtil.isNotEmpty(param.getQueryColumns())) {
            dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, param.getQueryColumns());
        } else if (StringUtil.isNotEmpty(param.getFuncCode()) && param.getColumnLazy()) {
            FuncInfo funcInfo=funInfoManager.getFuncInfo(param.getFuncCode());
            String queryColumns = funcInfo.getQueryColumns();
            dynaBean.set(BeanUtils.KEY_QUERY_FIELDS, queryColumns);
        }
        if (param.getLimit() == -1) {
            list = serviceTemplate.selectList(dynaBean);
        } else {
            list = serviceTemplate.selectList(dynaBean, param.getStart(), param.getLimit());
        }
        if (param.getMark()) {
            String funcId = param.getFuncId();
            EndUser currentUser = SecurityUserHolder.getCurrentUser();
            dynaBeanTemplate.buildMarkInfo(list, param.getTableCode(), currentUser, funcId);
        }
        if (param.getPostil()) {
            String funcId = param.getFuncId();
            dynaBeanTemplate.buildPostilInfo(list, param.getTableCode(), funcId);
        }
        if(param.getFuncEdit()){
            EndUser currentUser = SecurityUserHolder.getCurrentUser();
            String funcCode = param.getFuncCode();
            dynaBeanTemplate.buildFuncEditInfo(list, param.getTableCode(), currentUser, funcCode);
        }
        //获取总条数
        Long count = dynaBean.getLong(BeanUtils.KEY_ALL_COUNT);
        String strData = jsonBuilder.buildListPageJson(count, list, true);
        toWrite(strData, param.getRequest(), param.getResponse());
    }

    /**
     * 默认的单记录添加、保存方法
     * @param param
     */
    @Transactional
    public void doSave(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();
        //构建创新基础信息
        DynaBean dynaBean = param.getDynaBean();
        serviceTemplate.buildModelCreateInfo(dynaBean);
        //构建编号
        String codeGenFieldInfo = param.getCodeGenFieldInfo();
        if (StringUtil.isNotEmpty(codeGenFieldInfo)) {
            if (WebUtils.isSaas()) {
                EndUser currentUser = SecurityUserHolder.getCurrentUser();
                dynaBeanTemplate.buildCode(codeGenFieldInfo, dynaBean, currentUser.getJtgsId());
            } else {
                dynaBeanTemplate.buildCode(codeGenFieldInfo, dynaBean);
            }
        }
        //处理多附件上传
        String batchFilesFields = param.getBatchFilesFields();
        if (StringUtil.isNotEmpty(batchFilesFields)) {
            dynaBeanTemplate.doSaveBatchFiles(dynaBean, batchFilesFields, param.getFuncCode(), true, request);
        }
        String funcType = request.getParameter("funcType");
        String viewConfigInfo = request.getParameter("viewConfigInfo");
        if ("view".equals(funcType) && StringUtil.isNotEmpty(viewConfigInfo)) {
            dynaBeanTemplate.doViewData(viewConfigInfo, dynaBean);
        }
        //保存数据
        DynaBean inserted = dynaBeanTemplate.doSave(dynaBean);
        //检测子功能是否增加ROOT节点 (子功能多树)
        dynaBeanTemplate.doChildrenTree(inserted, param.getFuncCode());
        //如果是操作视图，则数据重新查询
        String viewTableCode = request.getParameter("viewTableCode");
        if (StringUtil.isNotEmpty(viewTableCode)) {
            String pkName = beanUtils.getPKeyFieldNames(viewTableCode);
            String pkVal = inserted.getStr(pkName);
            if (StringUtil.isNotEmpty(pkVal)) {
                inserted = serviceTemplate.selectOneByPk(viewTableCode, pkVal);
            }
        }
        //解决表单附件和ueeditor冲突问题
        String ckeditorFields = request.getParameter("ckeditorFields");
        if (StringUtil.isNotEmpty(ckeditorFields)) {
            for (String f : ckeditorFields.split(",")) {
                inserted.remove(f);
            }
        }
        //返回给前台
        String strData = jsonBuilder.toJson(inserted);
        toWrite(jsonBuilder.returnSuccessJson(strData), request, response);
    }

    /**
     * 默认的单记录复制方法
     * @param param
     */
    @Transactional
    public void doCopy(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();
        if (StringUtil.isEmpty(param.getPkValue())) {
            toWrite(jsonBuilder.returnFailureJson("\"传入原记录主键失败，无法复制!\""), request, response);
            return;
        }
        DynaBean dynaBean = param.getDynaBean();
        dynaBean.set(dynaBean.getStr(BeanUtils.KEY_PK_CODE), param.getPkValue());
        //级联复制记录
        DynaBean inserted = dynaBeanTemplate.doCopy(dynaBean, param.getFuncCode(), param.getCodeGenFieldInfo()
                , request.getParameter("uploadableFields"));
        //返回给前台
        String strData = jsonBuilder.toJson(inserted);
        toWrite(jsonBuilder.returnSuccessJson(strData), request, response);
    }

    /**
     * 默认的单记录更新方法
     * @param param
     */
    @Transactional
    public void doUpdate(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();
        //构建修改信息
        DynaBean dynaBean = param.getDynaBean();
        serviceTemplate.buildModelModifyInfo(dynaBean);
        if (StringUtil.isNotEmpty(param.getBatchFilesFields())) {
            dynaBeanTemplate.doSaveBatchFiles(dynaBean, param.getBatchFilesFields(), param.getFuncCode(), false, request);
        }
        String funcType = request.getParameter("funcType");
        String viewConfigInfo = request.getParameter("viewConfigInfo");
        if ("view".equals(funcType) && StringUtil.isNotEmpty(viewConfigInfo)) {
            dynaBeanTemplate.doViewData(viewConfigInfo, dynaBean);
        }
        //更新数据
        DynaBean updated = serviceTemplate.update(dynaBean);
        String viewTableCode = request.getParameter("viewTableCode");
        boolean newUpdate=false;
        if (StringUtil.isNotEmpty(viewTableCode)) {
            String pkName = beanUtils.getPKeyFieldNames(viewTableCode);
            String pkVal = request.getParameter(pkName);
            if (StringUtil.isNotEmpty(pkVal)) {
                updated = serviceTemplate.selectOneByPk(viewTableCode, pkVal);
                newUpdate=true;
            }
        }
        if(param.getFuncEdit()){
            serviceTemplate.executeSql("UPDATE JE_CORE_FUNCEDIT set FUNCEDIT_NEW='1' where FUNCEDIT_NEW='0'"
                    +" AND FUNCEDIT_FUNCCODE='"+param.getFuncCode()+"' AND FUNCEDIT_PKVALUE='"+dynaBean.getPkValue()+"'");
        }
        //如果表单内有附件则查询出新的数据返回
        if (StringUtil.isNotEmpty(request.getParameter("uploadableFields"))) {
            if(!newUpdate){
                updated = serviceTemplate.selectOneByPk(param.getTableCode(), updated.getPkValue());
            }
            //解决表单附件和ueeditor冲突问题
            String ckeditorFields = request.getParameter("ckeditorFields");
            if (StringUtil.isNotEmpty(ckeditorFields)) {
                for (String f : ckeditorFields.split(",")) {
                    updated.remove(f);
                }
            }
        }
        //如果是操作视图，则数据重新查询
        String strData = jsonBuilder.toJson(updated);
        toWrite(jsonBuilder.returnSuccessJson(strData), request, response);
    }

    /**
     * 默认的批量删除方法
     * @param param
     */
    @Transactional
    public void doRemove(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();

        if (StringUtil.isEmpty(param.getTableCode()) || StringUtil.isEmpty(param.getIds())) {
            toWrite(jsonAssist.buildReturnFailureJson4Obj("\"传入表名或者主键失败!\""), request, response);
            return;
        }
        //级联删除子功能
        if (StringUtil.isNotEmpty(param.getFuncCode())) {
            FuncInfo funcInfo=funInfoManager.getFuncInfo(param.getFuncCode());
            DynaBean dynaBean = param.getDynaBean();
            String pkCode = dynaBean.getStr(BeanUtils.KEY_PK_CODE);
            if (dynaBeanTemplate.decideDeleteChildren(funcInfo)) {
                String sql = " AND " + pkCode + " IN (" + StringUtil.buildArrayToString(param.getIds().split(",")) + ")";
                if (param.getDoTree()) {
                    sql = "";
                    for (String id : param.getIds().split(",")) {
                        sql += " OR SY_PATH LIKE '%" + id + "%'";
                    }
                    sql = " AND (1!=1 " + sql + ")";
                }
                List<DynaBean> lists = serviceTemplate.selectList(param.getTableCode(), sql);
                for (DynaBean bean : lists) {
                    dynaBeanTemplate.removeChild(bean, funcInfo);
                }
            }
        }
        //删除该功能下的文档数据和文件
        String uploadableFields = request.getParameter("uploadableFields");
        if (StringUtil.isNotEmpty(uploadableFields)) {
            if (param.getDoTree()) {
                serviceTemplate.removeTreeDocument(param.getTableCode(), param.getIds());
            } else {
                serviceTemplate.removeDocument(param.getTableCode(), param.getIds());
            }
        }
        if (StringUtil.isNotEmpty(param.getBatchFilesFields())) {
            if (param.getDoTree()) {
                dynaBeanTemplate.doRemoveTreeBatchFiles(param.getTableCode(), param.getIds());
            } else {
                dynaBeanTemplate.doRemoveBatchFiles(param.getTableCode(), param.getIds());
            }
        }
        String funcType = request.getParameter("funcType");
        String viewConfigInfo = request.getParameter("viewConfigInfo");
        String viewTableCode = request.getParameter("viewTableCode");
        if ("view".equals(funcType) && StringUtil.isNotEmpty(viewConfigInfo) && StringUtil.isNotEmpty(viewTableCode)) {
            String mainPkCode = beanUtils.getPKeyFieldNames(viewTableCode);
            dynaBeanTemplate.doViewDelData(viewConfigInfo, viewTableCode, mainPkCode, param.getIds());
        }
        //删除批注、编辑标记、标记
        String pkCode=beanUtils.getPKeyFieldNames(param.getTableCode());
        dynaBeanTemplate.doRemoveData(param.getTableCode(),pkCode,param.getIds(),param.getMark(),param.getFuncEdit(),param.getPostil(),param.getDoTree());
        //删除数据
        Integer count = 0;
        if (param.getDoTree()) {
            count = serviceTemplate.deleteTreeByIds(param.getIds(), param.getTableCode());
        } else {
            DynaBean dynaBean = param.getDynaBean();
            count = serviceTemplate.deleteByIds(param.getIds(), param.getTableCode(), dynaBean.getStr(BeanUtils.KEY_PK_CODE));
        }
        toWrite(jsonBuilder.returnSuccessJson("\"" + count + "条记录被删除!\""), request, response);
    }

    /**
     * 默认的批量启用方法
     * @param param
     */
    @Transactional
    public void doEnable(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();
        if (StringUtil.isNotEmpty(param.getTableCode()) && StringUtil.isNotEmpty(param.getIds())) {
            Integer count = serviceTemplate.enableFake(param.getTableCode(), param.getIds().split(ArrayUtils.SPLIT));
            toWrite(jsonBuilder.returnSuccessJson("\"" + count + "条记录被启用!\""), request, response);
        } else {
            toWrite(jsonBuilder.returnFailureJson("\"传入表名或者主键失败!\""), request, response);
        }
    }

    /**
     * 默认的批量停用方法
     * @param param
     */
    @Transactional
    public void doDisable(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();

        if (StringUtil.isNotEmpty(param.getTableCode()) && StringUtil.isNotEmpty(param.getIds())) {
            Integer count = serviceTemplate.deleteFake(param.getTableCode(), param.getIds().split(ArrayUtils.SPLIT));
            toWrite(jsonBuilder.returnSuccessJson("\"" + count + "条记录被禁用!\""), request, response);
        } else {
            toWrite(jsonBuilder.returnFailureJson("\"传入表名或者主键失败!\""), request, response);
        }
    }

    /**
     *  列表批量更新方法，返回新添加数据的id数组
     * @param param
     */
    @Transactional
    public void doInsertUpdateList(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();

        DynaBean dynaBean = param.getDynaBean();
        String tableCode = dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
        @SuppressWarnings("rawtypes")
        List<HashMap> updateList = new ArrayList<HashMap>();
        String funcType = request.getParameter("funcType");
        String viewConfigInfo = request.getParameter("viewConfigInfo");
        List<DynaBean> lists = beanUtils.buildUpdateList(param.getStrData(), tableCode);
        for (DynaBean bean : lists) {
            if ("view".equals(funcType) && StringUtil.isNotEmpty(viewConfigInfo)) {
                dynaBeanTemplate.doViewData(viewConfigInfo, bean);
            }
            String pkValue = bean.getPkValue();
            //操作    如果不包含该参数，  则按原来处理，， 如果包含。。 是update  有主键才会去更新
            String action = bean.getStr("__action__");
            if (StringUtil.isNotEmpty(pkValue) && (!bean.containsKey("__action__") || "doUpdate".equals(action))) {
                serviceTemplate.buildModelModifyInfo(bean);
                bean = serviceTemplate.update(bean);
                updateList.add(bean.getValues());
            } else {
                serviceTemplate.buildModelCreateInfo(bean);
                String codeGenFieldInfo=param.getCodeGenFieldInfo();
                if(StringUtil.isNotEmpty(codeGenFieldInfo)) {
                    if(WebUtils.isSaas()){
                        EndUser currentUser=SecurityUserHolder.getCurrentUser();
                        dynaBeanTemplate.buildCode(codeGenFieldInfo, bean,currentUser.getZhId());
                    }else{
                        dynaBeanTemplate.buildCode(codeGenFieldInfo, bean);
                    }
                }
                bean = serviceTemplate.insert(bean);
                updateList.add(bean.getValues());
            }
        }

        toWrite(jsonBuilder.returnSuccessJson(jsonBuilder.toJson(updateList)), request, response);
    }

    /**
     * 默认的列表批量更新方法
     * @param param
     */
    @Transactional
    public void doUpdateList(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();

        String funcType = request.getParameter("funcType");
        String viewConfigInfo = request.getParameter("viewConfigInfo");
        DynaBean dynaBean = param.getDynaBean();
        List<DynaBean> updateList = dynaBeanTemplate.doUpdateList(dynaBean, param.getStrData(), funcType, viewConfigInfo);

        toWrite(jsonBuilder.returnSuccessJson("\"" + updateList.size() + "条记录被更新\""), request, response);
    }

    /**
     * 批量更新。 根据sql更新记录
     * @param param
     */
    public void listUpdate(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();

        DynaBean dynaBean = param.getDynaBean();
        dynaBean.set(BeanUtils.KEY_WHERE, param.getWhereSql());
        Integer count = serviceTemplate.listUpdate(dynaBean);

        toWrite(jsonBuilder.returnSuccessJson("\"" + count + "条记录被更新!\""), request, response);
    }

    /**
     * 根据主键获取业务数据
     * @param param
     */
    public void getInfoById(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();

        String pkValue = param.getPkValue();
        DynaBean dynaBean = param.getDynaBean();
        if (StringUtil.isEmpty(pkValue)) {
            pkValue = dynaBean.getPkValue();
        }
        String tableCode = dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
        DynaBean selectOne = serviceTemplate.selectOneByPk(tableCode, pkValue);
        if (selectOne != null) {
            toWrite(jsonBuilder.toJson(selectOne), request, response);
        } else {
            toWrite(jsonBuilder.returnFailureJson("\"未查询到记录!\""), request, response);
        }
    }

    /**
     * 默认获取树形数据
     * @param param
     */
    public void getTree(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();

        String node = param.getNode();
        if (StringUtils.isEmpty(node)) {
            node = ConstantVars.TREE_ROOT;
        }
        if (StringUtils.isNotEmpty(param.getRootId())) {
            node = param.getRootId();
        }

        DynaBean dynaBean = param.getDynaBean();
        if (param.getMoreRoot()) {
            String pkName = dynaBean.getStr(BeanUtils.KEY_PK_CODE);
            DynaBean root = serviceTemplate.selectOne(dynaBean.getStr(BeanUtils.KEY_TABLE_CODE), param.getParentSql()
                    + " and SY_NODETYPE='ROOT'");
            if (root != null) {
                node = root.getStr(pkName);
            } else {
                toWrite("{}", request, response);
                return;
            }
        }
        String tableCode = dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
        JSONTreeNode template = beanUtils.getTreeTemplate(tableCode);
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setWhereSql(param.getWhereSql());
        queryInfo.setOrderSql(param.getOrderSql());
        List<JSONTreeNode> jsonTreeNodeList = pcServiceTemplate.getJsonTreeNodeList(node, tableCode, template, queryInfo);
        JSONTreeNode rootNode = pcServiceTemplate.buildJSONNewTree(jsonTreeNodeList, node);

        toWrite(jsonAssist.buildModelJson(rootNode, param.getExcludes().split(",")), request, response);
    }

    /**
     * 默认加载表格树方法
     * @param param
     */
    public void loadGridTree(MethodArgument param) {
        String node = param.getNode();
        if (StringUtils.isEmpty(node)) {
            node = ConstantVars.TREE_ROOT;
        }
        if (StringUtils.isNotEmpty(param.getRootId())) {
            node = param.getRootId();
        }
        DynaBean dynaBean = param.getDynaBean();
        if (param.getMoreRoot()) {
            String pkName = dynaBean.getStr(BeanUtils.KEY_PK_CODE);
            DynaBean root = serviceTemplate.selectOne(dynaBean.getStr(BeanUtils.KEY_TABLE_CODE), param.getParentSql()
                    + " and SY_NODETYPE='ROOT'");
            if (root != null) {
                node = root.getStr(pkName);
            } else {
                toWrite("{}", param.getRequest(), param.getResponse());
                return;
            }
        }
        String tableCode = dynaBean.getStr(BeanUtils.KEY_TABLE_CODE);
        QueryInfo queryInfo = new QueryInfo();
        queryInfo.setWhereSql(param.getWhereSql());
        queryInfo.setOrderSql(param.getOrderSql());
        @SuppressWarnings("rawtypes")
        List<HashMap> records = serviceTemplate.loadTree(node, tableCode, param.getExcludes(), jsonAssist.yesOrNo2boolean(param.getChecked())
                , queryInfo);
        if (param.getLimit() != -1) {
            Integer endIndex = param.getStart() + param.getLimit();
            if (endIndex >= records.size()) {
                endIndex = records.size();
            }
            @SuppressWarnings("rawtypes")
            List<HashMap> datas = records.subList(param.getStart(), endIndex);
            toWrite(jsonBuilder.buildTreePageJson(new Long(records.size()), datas, true), param.getRequest(), param.getResponse());
        } else {
            toWrite(jsonBuilder.buildTreePageJson(new Long(records.size()), records, true), param.getRequest(), param.getResponse());
        }
    }

    /**
     * 树形节点移动方法
     * @param param
     */
    @Transactional
    public void treeMove(MethodArgument param) {
        DynaBean dynaBean = param.getDynaBean();
        DynaBean updated = dynaBeanTemplate.treeMove(dynaBean);
        toWrite(jsonBuilder.returnSuccessJson(jsonBuilder.toJson(updated)), param.getRequest(), param.getResponse());
    }

    /**
     * 针对表单上传文件的更新
     * @param param
     */
    public void doUploadFuncFile(MethodArgument param) {
        DynaBean dynaBean = param.getDynaBean();
        if (null != dynaBean) {
            List<DynaBean> docs = (List<DynaBean>) dynaBean.get(BeanUtils.KEY_DOC_INFO);
            if (null != docs && 0 != docs.size()) {
                DynaBean doc = docs.iterator().next();
                dynaBeanTemplate.doSaveDocumentInfo(doc);
                toWrite(jsonBuilder.returnSuccessJson((jsonBuilder.toJson(doc))), param.getRequest(), param.getResponse());
            } else {
                toWrite(jsonBuilder.returnFailureJson("\"传入信息失败!\""), param.getRequest(), param.getResponse());
            }
        }
    }

    /**
     * 默认只上传附件方法
     * @param param
     */
    public void uploadFile(MethodArgument param) {
        HttpServletRequest request=param.getRequest();
        String returnType=request.getParameter("returnType");
        DynaBean dynaBean = param.getDynaBean();
        List<DynaBean> docs = (List<DynaBean>) dynaBean.get(BeanUtils.KEY_DOC_INFO);
        if (null != docs && 0 != docs.size()) {
            DynaBean doc = docs.iterator().next();
            doc=dynaBeanTemplate.doSaveDocumentInfo(doc);
            if("all".equalsIgnoreCase(returnType)){
                toWrite(jsonBuilder.returnSuccessJson(jsonBuilder.toJson(doc)),request,param.getResponse());
            }else {
                toWrite(jsonBuilder.returnSuccessJson("'" + doc.getStr("DOCUMENT_ADDRESS") + "'"), param.getRequest(), param.getResponse());
            }
        } else {
            toWrite(jsonBuilder.returnFailureJson("\"上传失败!\""), param.getRequest(), param.getResponse());
        }
    }

    /**
     * 验证字段的唯一性
     * @param param
     */
    public void checkFieldUniquen(MethodArgument param) {
        String fieldCode = param.getRequest().getParameter("FIELDCODE");
        DynaBean dynaBean = param.getDynaBean();
        Boolean success = dynaBeanTemplate.checkFieldUniquen(dynaBean, fieldCode);
        if (success) {
            toWrite(jsonBuilder.returnSuccessJson("\"验证成功!\""), param.getRequest(), param.getResponse());
        } else {
            toWrite(jsonBuilder.returnFailureJson("\"违反唯一性约束!\""), param.getRequest(), param.getResponse());
        }
    }

    /**
     * 执行数据流转sql
     * @param param
     */
    @Transactional
    public void executeBatchSql(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        String act = request.getParameter("act");
        String sqls = request.getParameter("sqls");
        if (StringUtil.isNotEmpty(sqls)) {
            JSONArray array = JSONArray.fromObject(sqls);
            for (int i = 0; i < array.size(); i++) {
                String sql = array.getString(i);
                if (StringUtil.isNotEmpty(sql)) {
                    pcServiceTemplate.executeSql(act + " " + sql);
                }
            }
            toWrite(jsonBuilder.returnSuccessJson("\"操作成功!\""), param.getRequest(), param.getResponse());
        } else {
            toWrite(jsonBuilder.returnFailureJson("\"传入sql值为空!\""), param.getRequest(), param.getResponse());
        }
    }

    /**
     * 查询数据集合，返回为数组对象
     * @param param
     */
    public void selectList(MethodArgument param) {
        List<DynaBean> lists = serviceTemplate.selectList(param.getTableCode(), param.getWhereSql());
        toWrite(jsonBuilder.buildListPageJson(new Long(lists.size()), lists, false), param.getRequest(), param.getResponse());
    }

    /**
     * 查询单条数据
     * @param param
     */
    public void selectOne(MethodArgument param) {
        DynaBean one = serviceTemplate.selectOne(param.getTableCode(), param.getWhereSql());
        if (one != null) {
            toWrite(jsonBuilder.returnSuccessJson(jsonBuilder.toJson(one)), param.getRequest(), param.getResponse());
        } else {
            toWrite(jsonBuilder.returnFailureJson("\"未查询到数据!\""), param.getRequest(), param.getResponse());
        }
    }

    /**
     * 保存或修改mark信息
     * @param param
     */
    @Transactional
    public void saveMark(MethodArgument param) {
        DynaBean dynaBean = param.getDynaBean();
        pcServiceTemplate.executeSql(" DELETE FROM JE_CORE_MARK WHERE MARK_FUNCID='" + dynaBean.getStr("MARK_FUNCID") + "' AND MARK_MODELID='" + dynaBean.getStr("MARK_MODELID") + "' AND MARK_USERID='" + dynaBean.getStr("MARK_USERID") + "'");
        DynaBean inserted = serviceTemplate.insert(dynaBean);
        String strData = jsonBuilder.toJson(inserted);

        toWrite(jsonBuilder.returnSuccessJson(strData), param.getRequest(), param.getResponse());
    }

    /**
     * 保存批注信息
     * @param param
     */
    @Transactional
    public void savePostil(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        DynaBean dynaBean = param.getDynaBean();

        String createUserId = request.getParameter("createUserId");
        String hfrId = dynaBean.getStr("POSTIL_HFRID", "");
        EndUser currentUser = SecurityUserHolder.getCurrentUser();
        String funcName = request.getParameter("funcName");
        serviceTemplate.buildModelCreateInfo(dynaBean);
        dynaBean = serviceTemplate.insert(dynaBean);
        String title = dynaBean.getStr("SY_CREATEUSERNAME") + "在" + dynaBean.getStr("SY_CREATETIME") + "，功能【" + funcName + "】给您回复了一条内容!";
        StringBuffer context = new StringBuffer();
//		context.append(dynaBean.getStr("SY_CREATEUSERNAME")+"在"+dynaBean.getStr("SY_CREATETIME")+"，功能【"+funcName+"】给您回复了一条内容，");
        context.append(dynaBean.getStr("SY_CREATEUSERNAME") + "在" + dynaBean.getStr("SY_CREATETIME") + "，功能【<a href=\"javascript:void(0)\" onclick=\"javascript:JE.showFunc('" + dynaBean.getStr("POSTIL_FUNCCODE", "") + "',{type:'form',id:'" + dynaBean.getStr("POSTIL_MODELID", "") + "'});\">" + funcName + "</a>】给您回复了一条内容，");
        context.append("内容：" + dynaBean.getStr("POSTIL_PZ") + "，");
        context.append("详细信息请点击,<a href=\"javascript:void(0)\" onclick=\"javascript:JE.CoreUtil.showPostil({funcCode:'" + dynaBean.getStr("POSTIL_FUNCCODE", "") + "',pkValue:'" + dynaBean.getStr("POSTIL_MODELID", "") + "',userId:'" + createUserId + "'});\">查看</a>!");
        Set<String> userIds = new HashSet<String>();
        if (StringUtil.isNotEmpty(hfrId)) {
            userIds.add(hfrId);
        }
        userIds.add(createUserId);
        for (String uI : userIds) {
            if (!currentUser.getUserId().equals(uI)) {
                List<UserMsgAppInfo> userMsgAppInfos=new ArrayList<>();
                net.sf.json.JSONObject params=new net.sf.json.JSONObject();
                params.put("pkValue",dynaBean.getStr("POSTIL_MODELID", ""));
                UserMsgAppInfo appInfo=new UserMsgAppInfo(dynaBean.getStr("POSTIL_FUNCNAME"),"APPFUNC",dynaBean.getStr("POSTIL_FUNCCODE", ""),"form",params);
                userMsgAppInfos.add(appInfo);
                pcMessageManager.sendDwr(uI, title, context.toString(), "JE.showMsg", "JE.showBatchMsg", true);
                pcMessageManager.sendUserMsg(uI,title, context.toString(), "PZ",userMsgAppInfos);
            }
        }

        String strData = jsonBuilder.toJson(dynaBean);
        toWrite(jsonBuilder.returnSuccessJson(strData), param.getRequest(), param.getResponse());
    }

    /**
     * 编辑标记标识已读
     * @param param
     */
    public void readFuncEdit(MethodArgument param) {
        String tableCode=param.getTableCode();
        String funcCode=param.getFuncCode();
        String pkValue=param.getPkValue();
        EndUser currentUser=SecurityUserHolder.getCurrentUser();
        serviceTemplate.doDataFuncEdit(funcCode,tableCode,pkValue,currentUser.getUserId(),"0");
        toWrite(jsonBuilder.returnSuccessJson("\"成功\""), param.getRequest(), param.getResponse());
    }

    public void loadBadge(MethodArgument param) {
        String tableCode = param.getTableCode();
        String whereSql = param.getWhereSql();
        JSONArray items = JSONArray.fromObject(param.getStrData());//"[{field:'',codes:['A','B','C'],sql:''}]";
        JSONObject result = new JSONObject();
        for (int i = 0; i < items.size(); i++) {
            JSONObject objs = items.getJSONObject(i);
            String fieldCode = objs.getString("field");
            JSONArray codeVals = objs.getJSONArray("codes");
            String sql = objs.getString("sql");
            //多选单选
            String sm = objs.getString("sm");
            JSONObject valsInfo = new JSONObject();
            if (!"1".equals(sm)) {
                //多选使用like
                List<Map> countVals = pcServiceTemplate.queryMapBySql("SELECT " + fieldCode + ",COUNT(*) FIELDCOUNT from " + tableCode + " WHERE 1=1 " + whereSql + " " + sql + " GROUP BY  " + fieldCode + "");
                for (Map valusInfo : countVals) {
                    String val = valusInfo.get(fieldCode) + "";
                    if(StringUtil.isEmpty(val))continue;
                    long count = Long.parseLong(StringUtil.getDefaultValue(valusInfo.get("FIELDCOUNT") + "", "0"));
                    valsInfo.put(val, count);
                }
                for(int j=0;j<codeVals.size();j++){
                    String code=codeVals.getString(j);
                    if(StringUtil.isNotEmpty(code) && !valsInfo.containsKey(code)){
                        valsInfo.put(code,0);
                    }
                }
            } else {
                for (int j = 0; j < codeVals.size(); j++) {
                    String val = codeVals.getString(j);
                    long count = serviceTemplate.selectCount(tableCode, whereSql + sql + " AND " + fieldCode + " LIKE '%" + val + "%'");
                    valsInfo.put(val, count);
                }
            }
            result.put(fieldCode, valsInfo);
        }
        toWrite(jsonBuilder.toJson(result), param.getRequest(), param.getResponse());
    }

    /**
     * 获取动态列的列规划数据
     * @param param
     * @return
     */
    public BaseRespResult getIditProcedureColumn(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        // TODO Auto-generated method stub
        String funcId = request.getParameter("funcId");
        DynaBean funcInfo = serviceTemplate.selectOneByPk("JE_CORE_FUNCINFO", funcId, "JE_CORE_FUNCINFO_ID,FUNCINFO_DBNAME,FUNCINFO_PROCEDURE,FUNCINFO_QUERYPARAM");
        String dataSource = funcInfo.getStr("FUNCINFO_DBNAME");
        String procedureName = funcInfo.getStr("FUNCINFO_PROCEDURE");
        String queryParamStr = funcInfo.getStr("FUNCINFO_QUERYPARAM");
        JSONArray arrays = JSONArray.fromObject(queryParamStr);
        List<DbFieldVo> fieldVos = new ArrayList<DbFieldVo>();
        Object[] params = new Object[arrays.size()];
        String columnConfigName = "";
        for (int i = 0; i < arrays.size(); i++) {
            JSONObject obj = arrays.getJSONObject(i);
            String name = obj.getString("name");
            String fieldType = obj.getString("fieldType");
            String paramType = "1".equals(obj.getString("paramType")) ? "out" : "in";
            DbFieldVo fieldVo = new DbFieldVo(name, paramType, fieldType);
            if (DbFieldType.NOWPAGE.equals(fieldType)) {
                fieldVo.setValue(1);
            } else if (DbFieldType.LIMIT.equals(fieldType)) {
                fieldVo.setValue(30);
            } else if (DbFieldType.TOTALCOUNT.equals(fieldType)) {
            } else if (DbFieldType.CODE.equals(fieldType)) {
            } else if (DbFieldType.MSG.equals(fieldType)) {
            } else if (DbFieldType.COLUMNCONFIG.equals(fieldType)) {
                columnConfigName = fieldVo.getName();
            }
            String defaultValue = "";
            if (obj.containsKey("defaultValue")) {
                defaultValue = obj.getString("defaultValue");
                if (StringUtil.isNotEmpty(defaultValue)) {
                    String val = StringUtil.getVarDefaultValue(defaultValue);
                    if (StringUtil.isNotEmpty(val)) {
                        defaultValue = val;
                        fieldVo.setValue(val);
                    }
                }
            }
            if ("out".equalsIgnoreCase(paramType)) {

            } else {
                if (StringUtil.isEmpty(defaultValue)) {
                    fieldVo.setValue(null);
                }
            }
            fieldVos.add(fieldVo);
        }
        if (StringUtil.isEmpty(columnConfigName)) {
            return BaseRespResult.successResult("");
        }
        Map map = new HashMap();
        if (StringUtil.isEmpty(dataSource) || "local".equals(dataSource)) {
            map = pcServiceTemplate.queryMapProcedure("{call " + procedureName + "(" + pcDataService.getCallParams(params) + ")}", fieldVos);
        } else {
            SqlRunner sqlRunner = SqlRunner.getInstance(dataSource);
            map = sqlRunner.queryMapProcedure("{call " + procedureName + "(" + pcDataService.getCallParams(params) + ")}", fieldVos);
        }
        String columnConfigValue = map.get(columnConfigName) + "";

        return BaseRespResult.successResult(columnConfigValue);
    }

    /**
     * 将一段字符串写入response.writer，默认为UTF-8编码
     *
     * @param contents
     * @throws IOException
     */
    public void toWrite(String contents, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtil.getDefaultValue(WebUtils.getSysVar("JE_CORE_PHONE"), "1").equals(request.getParameter("phone"))) {
            String callbackName = request.getParameter("callback");
            if (StringUtil.isNotEmpty(callbackName)) {
                response.setContentType("text/javascript");
                contents = callbackName + "(" + contents + ")";
            } else {
                response.setContentType("text/html;charset=UTF-8");
            }
        } else {
            response.setContentType("text/html;charset=UTF-8");
        }
        if (null != response) {
            Writer writer = null;
            try {
                response.setCharacterEncoding("utf-8");
                writer = response.getWriter();
                writer.write(contents);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new PlatformException("后端返回前端数据出错，url链接："+request.getRequestURI(), PlatformExceptionEnum.JE_CORE_CONTROLLER_ERROR,e);
            } finally {
                try {
                    response.flushBuffer();
                } catch (IOException e) {
                    throw new PlatformException("后端返回前端数据出错，url链接："+request.getRequestURI(), PlatformExceptionEnum.JE_CORE_CONTROLLER_ERROR,e);
//                    logger.error(e.getMessage());
//                    e.printStackTrace();
                }
            }
        }
    }

}
