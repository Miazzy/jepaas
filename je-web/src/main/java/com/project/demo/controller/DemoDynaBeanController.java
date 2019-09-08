package com.project.demo.controller;

import com.je.cache.service.config.BackCacheManager;
import com.je.cache.service.config.FrontCacheManager;
import com.je.core.ann.entity.DbFieldVo;
import com.je.core.base.AbstractDynaController;
import com.je.core.base.MethodArgument;
import com.je.core.constants.db.DbFieldType;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.core.facade.extjs.JsonAssist;
import com.je.core.facade.extjs.JsonBuilder;
import com.je.core.service.PCDynaBeanTemplate;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.DbProduceUtil;
import com.je.core.util.SecurityUserHolder;
import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.je.core.util.bean.BeanUtils;
import com.je.core.util.bean.DynaBean;
import com.je.datasource.runner.SqlRunner;
import com.je.develop.service.FunInfoManager;
import com.je.develop.vo.FuncInfo;
import com.je.rbac.model.EndUser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 *  重写父类AbstractDynaController的增删改查方法，在原来封装的方法上加需要的业务逻辑
 *  注意事项：controller请求路径“/je/demoDynaAction”要与功能配置里面Action项对应，增删改查方法页面不做操作，后台只需继承AbstractDynaController重写父类方法即可。
 *  基本的五个方法：load，doSave，doUpdate，doRemove，getInfoById
 *  本次事例只在增删改的方法上增加其他的业务逻辑处理
 */
@Controller
@RequestMapping(value = "/je/demoDyna")
public class DemoDynaBeanController extends AbstractDynaController {

    @Autowired
    protected PCDynaBeanTemplate dynaBeanTemplate;
    @Autowired
    protected PCDynaServiceTemplate serviceTemplate;
    @Autowired
    protected FunInfoManager funInfoManager;

    protected static BeanUtils beanUtils = null;
    protected static JsonAssist jsonAssist = null;
    protected static JsonBuilder jsonBuilder = null;

    /**
     *  重写保存方法
     * @param param
     */
    @RequestMapping(
            value = {"/doSave"},
            method = {RequestMethod.POST},
            produces = {"application/json; charset=utf-8"}
    )
    @ResponseBody
    public void doSave(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();
        DynaBean dynaBean = param.getDynaBean();
        this.serviceTemplate.buildModelCreateInfo(dynaBean);
        String codeGenFieldInfo = param.getCodeGenFieldInfo();
        if (StringUtil.isNotEmpty(codeGenFieldInfo)) {
            if (WebUtils.isSaas()) {
                EndUser currentUser = SecurityUserHolder.getCurrentUser();
                this.dynaBeanTemplate.buildCode(codeGenFieldInfo, dynaBean, currentUser.getJtgsId());
            } else {
                this.dynaBeanTemplate.buildCode(codeGenFieldInfo, dynaBean);
            }
        }

        String batchFilesFields = param.getBatchFilesFields();
        if (StringUtil.isNotEmpty(batchFilesFields)) {
            this.dynaBeanTemplate.doSaveBatchFiles(dynaBean, batchFilesFields, param.getFuncCode(), true, request);
        }

        String funcType = request.getParameter("funcType");
        String viewConfigInfo = request.getParameter("viewConfigInfo");
        if ("view".equals(funcType) && StringUtil.isNotEmpty(viewConfigInfo)) {
            this.dynaBeanTemplate.doViewData(viewConfigInfo, dynaBean);
        }

        //保存之前的业务操作 start
            //例如校验数据规则,这边取的项目名称字段
            String csb_bz = dynaBean.getStr("XMINFO_XMMC");
            if(csb_bz != null && csb_bz.toString().length() > 100){   //判断长度不可超过100
                this.toWrite(jsonBuilder.returnFailureJson("\"项目长度不可超过100\""), request, response);
            }
        //保存之前的业务操作 end

        DynaBean inserted = this.dynaBeanTemplate.doSave(dynaBean); //执行保存

        //保存之后的业务操作 start
            //取刚刚插入的数据的主键
            String inserted_Id = inserted.getStr("JE_DEMO_XMINFO_ID");
            //往其他表插入关联数据，取得要插入的表的信息，测试用的是同一张表。
            DynaBean psi_csb = new DynaBean("JE_DEMO_XMINFO", true); //取得要插入表的实例
            psi_csb.set("XMINFO_XMBM", "新增的编码"); //set数据，如果是set，则后面的值是Object类型
            psi_csb.setStr("XMINFO_XMMC", "新增的名称"); //setStr数据。如果是setStr,则后面的值是String类型
            psi_csb.setStr("XMINFO_PARENTID", inserted_Id); //放入之前数据中主键进行关联
            serviceTemplate.buildModelCreateInfo(psi_csb);  //因为是insert，buildModelCreateInfo如把字段有默认值情况自动赋值，update不需要
            serviceTemplate.insert(psi_csb); //插入新的数据
        // 保存之后的业务操作  end

        this.dynaBeanTemplate.doChildrenTree(inserted, param.getFuncCode());
        String viewTableCode = request.getParameter("viewTableCode");
        String ckeditorFields;
        String strData;
        if (StringUtil.isNotEmpty(viewTableCode)) {
            ckeditorFields = beanUtils.getPKeyFieldNames(viewTableCode);
            strData = inserted.getStr(ckeditorFields);
            if (StringUtil.isNotEmpty(strData)) {
                inserted = this.serviceTemplate.selectOneByPk(viewTableCode, strData);
            }
        }

        ckeditorFields = request.getParameter("ckeditorFields");
        if (StringUtil.isNotEmpty(ckeditorFields)) {
            String[] var17 = ckeditorFields.split(",");
            int var13 = var17.length;

            for(int var14 = 0; var14 < var13; ++var14) {
                String f = var17[var14];
                inserted.remove(f);
            }
        }

        strData = jsonBuilder.toJson(inserted);
        this.toWrite(jsonBuilder.returnSuccessJson(strData), request, response);
    }

    /**
     *  重写修改方法
     * @param param
     */
    @RequestMapping(
            value = {"/doUpdate"},
            method = {RequestMethod.POST},
            produces = {"application/json; charset=utf-8"}
    )
    @ResponseBody
    public void doUpdate(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();
        DynaBean dynaBean = param.getDynaBean();
        this.serviceTemplate.buildModelModifyInfo(dynaBean);
        if (StringUtil.isNotEmpty(param.getBatchFilesFields())) {
            this.dynaBeanTemplate.doSaveBatchFiles(dynaBean, param.getBatchFilesFields(), param.getFuncCode(), false, request);
        }

        String funcType = request.getParameter("funcType");
        String viewConfigInfo = request.getParameter("viewConfigInfo");
        if ("view".equals(funcType) && StringUtil.isNotEmpty(viewConfigInfo)) {
            this.dynaBeanTemplate.doViewData(viewConfigInfo, dynaBean);
        }

        //修改之前的业务操作 start
            //例如校验数据规则,这边取的项目名称字段
            String csb_bz = dynaBean.getStr("XMINFO_XMMC");
            if(csb_bz != null && csb_bz.toString().length() > 100){   //判断长度不可超过100
                this.toWrite(jsonBuilder.returnFailureJson("\"项目名称长度不可超过100\""), request, response);
            }
        //修改之前的业务操作 end

        DynaBean updated = this.serviceTemplate.update(dynaBean);   //执行修改

        //修改之后的业务操作 start
            //假如修改之后需要修改关联的数据的话，操作如下
            //取得本次数据的主键
            String updatedId = updated.getStr("JE_DEMO_XMINFO_ID");
            //取得关联数据的信息
            DynaBean updateDynaBean = serviceTemplate.selectOne("JE_DEMO_XMINFO", "AND XMINFO_PARENTID='"+ updatedId +"'");
            updateDynaBean.setStr("XMINFO_XMBM", "修改过得编码"); //修改要修改的数据
            updateDynaBean.setStr("XMINFO_XMMC", "修改过得名称"); //修改要修改的数据
            serviceTemplate.update(updateDynaBean); //更新数据
        //修改之后的业务操作 end

        String viewTableCode = request.getParameter("viewTableCode");
        boolean newUpdate = false;
        String ckeditorFields;
        if (StringUtil.isNotEmpty(viewTableCode)) {
            ckeditorFields = beanUtils.getPKeyFieldNames(viewTableCode);
            String pkVal = request.getParameter(ckeditorFields);
            if (StringUtil.isNotEmpty(pkVal)) {
                updated = this.serviceTemplate.selectOneByPk(viewTableCode, pkVal);
                newUpdate = true;
            }
        }

        if (param.getFuncEdit()) {
            this.serviceTemplate.executeSql("UPDATE JE_CORE_FUNCEDIT set FUNCEDIT_NEW='1' where FUNCEDIT_NEW='0' AND FUNCEDIT_FUNCCODE='" + param.getFuncCode() + "' AND FUNCEDIT_PKVALUE='" + dynaBean.getPkValue() + "'");
        }

        if (StringUtil.isNotEmpty(request.getParameter("uploadableFields"))) {
            if (!newUpdate) {
                updated = this.serviceTemplate.selectOneByPk(param.getTableCode(), updated.getPkValue());
            }

            ckeditorFields = request.getParameter("ckeditorFields");
            if (StringUtil.isNotEmpty(ckeditorFields)) {
                String[] var15 = ckeditorFields.split(",");
                int var12 = var15.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    String f = var15[var13];
                    updated.remove(f);
                }
            }
        }

        ckeditorFields = jsonBuilder.toJson(updated);
        this.toWrite(jsonBuilder.returnSuccessJson(ckeditorFields), request, response);
    }

    /**
     *  重写删除方法
     * @param param
     */
    @RequestMapping(
            value = {"/doRemove"},
            method = {RequestMethod.POST},
            produces = {"application/json; charset=utf-8"}
    )
    @ResponseBody
    public void doRemove(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();
        if (!StringUtil.isEmpty(param.getTableCode()) && !StringUtil.isEmpty(param.getIds())) {
            String viewConfigInfo;
            String sql;
            DynaBean dynaBean;
            if (StringUtil.isNotEmpty(param.getFuncCode())) {
                FuncInfo funcInfo = this.funInfoManager.getFuncInfo(param.getFuncCode());
                dynaBean = param.getDynaBean();
                viewConfigInfo = dynaBean.getStr("$PK_CODE$");
                if (this.dynaBeanTemplate.decideDeleteChildren(funcInfo)) {
                    sql = " AND " + viewConfigInfo + " IN (" + StringUtil.buildArrayToString(param.getIds().split(",")) + ")";
                    if (param.getDoTree()) {
                        sql = "";
                        String[] var8 = param.getIds().split(",");
                        int var9 = var8.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                            String id = var8[var10];
                            sql = sql + " OR SY_PATH LIKE '%" + id + "%'";
                        }

                        sql = " AND (1!=1 " + sql + ")";
                    }

                    List<DynaBean> lists = this.serviceTemplate.selectList(param.getTableCode(), sql);
                    Iterator var16 = lists.iterator();

                    while(var16.hasNext()) {
                        dynaBean = (DynaBean)var16.next();
                        this.dynaBeanTemplate.removeChild(dynaBean, funcInfo);
                    }
                }
            }

            String uploadableFields = request.getParameter("uploadableFields");
            if (StringUtil.isNotEmpty(uploadableFields)) {
                if (param.getDoTree()) {
                    this.serviceTemplate.removeTreeDocument(param.getTableCode(), param.getIds());
                } else {
                    this.serviceTemplate.removeDocument(param.getTableCode(), param.getIds());
                }
            }

            if (StringUtil.isNotEmpty(param.getBatchFilesFields())) {
                if (param.getDoTree()) {
                    this.dynaBeanTemplate.doRemoveTreeBatchFiles(param.getTableCode(), param.getIds());
                } else {
                    this.dynaBeanTemplate.doRemoveBatchFiles(param.getTableCode(), param.getIds());
                }
            }

            String funcType = request.getParameter("funcType");
            viewConfigInfo = request.getParameter("viewConfigInfo");
            sql = request.getParameter("viewTableCode");
            String pkCode;
            if ("view".equals(funcType) && StringUtil.isNotEmpty(viewConfigInfo) && StringUtil.isNotEmpty(sql)) {
                pkCode = beanUtils.getPKeyFieldNames(sql);
                this.dynaBeanTemplate.doViewDelData(viewConfigInfo, sql, pkCode, param.getIds());
            }

            pkCode = beanUtils.getPKeyFieldNames(param.getTableCode());
            this.dynaBeanTemplate.doRemoveData(param.getTableCode(), pkCode, param.getIds(), param.getMark(), param.getFuncEdit(), param.getPostil(), param.getDoTree());

            //删除之前的业务操作  start
                //判断一下自己需要的业务规则
                //取得本次数据的主键
                String[] idArray = param.getIds().split(",");
                String str = "'" + String.join("','",idArray) + "'";
                //取得关联数据的信息
                List<DynaBean> dynaBeanList = serviceTemplate.selectList("JE_DEMO_XMINFO", "AND XMINFO_PARENTID in("+ str +")");
                //已经取到要删除的数据，判断自己业务场景需要判断是否可以删除

            //删除之后的业务操作  end

            Integer count = 0;
            if (param.getDoTree()) {
                count = this.serviceTemplate.deleteTreeByIds(param.getIds(), param.getTableCode());
            } else {
                dynaBean = param.getDynaBean();
                count = this.serviceTemplate.deleteByIds(param.getIds(), param.getTableCode(), dynaBean.getStr("$PK_CODE$"));
            }

            //删除之后的业务操作  start
                //关联删除一些业务操作
            String[] idArrays = param.getIds().split(",");
            //循环删除关联的数据
            for(String id : idArrays){
                serviceTemplate.deleteByWehreSql("JE_DEMO_XMINFO", "AND XMINFO_PARENTID='"+ id +"'");
            }
            //删除之后的业务操作  end

            this.toWrite(jsonBuilder.returnSuccessJson("\"" + count + "条记录被删除!\""), request, response);
        } else {
            this.toWrite(jsonAssist.buildReturnFailureJson4Obj("\"传入表名或者主键失败!\""), request, response);
        }
    }

    /**
     *   点创建或者修改会调此方法，非特殊情况不做更改
     * @param param
     */
    @RequestMapping(
            value = {"/getInfoById"},
            method = {RequestMethod.POST},
            produces = {"application/json; charset=utf-8"}
    )
    @ResponseBody
    public void getInfoById(MethodArgument param) {
        HttpServletRequest request = param.getRequest();
        HttpServletResponse response = param.getResponse();
        String pkValue = param.getPkValue();
        DynaBean dynaBean = param.getDynaBean();
        if (StringUtil.isEmpty(pkValue)) {
            pkValue = dynaBean.getPkValue();
        }

        String tableCode = dynaBean.getStr("$TABLE_CODE$");
        DynaBean selectOne = this.serviceTemplate.selectOneByPk(tableCode, pkValue);
        if (selectOne != null) {
            this.toWrite(jsonBuilder.toJson(selectOne), request, response);
        } else {
            this.toWrite(jsonBuilder.returnFailureJson("\"未查询到记录!\""), request, response);
        }

    }

    /**
     * 后端给前端发送响应的公共方法
     * @param contents
     * @param request
     * @param response
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
            PrintWriter writer = null;

            try {
                response.setCharacterEncoding("utf-8");
                writer = response.getWriter();
                writer.write(contents);
            } catch (IOException var13) {
                throw new PlatformException("后端返回前端数据出错，url链接：" + request.getRequestURI(), PlatformExceptionEnum.JE_CORE_CONTROLLER_ERROR, var13);
            } finally {
                try {
                    writer.flush();
                    writer.close();
                    response.flushBuffer();
                } catch (IOException var12) {
                    throw new PlatformException("后端返回前端数据出错，url链接：" + request.getRequestURI(), PlatformExceptionEnum.JE_CORE_CONTROLLER_ERROR, var12);
                }
            }
        }

    }

    static {
        beanUtils = BeanUtils.getInstance();
        jsonAssist = JsonAssist.getInstance();
        jsonBuilder = JsonBuilder.getInstance();
    }

    /**
     *  默认加载列表的方法
     * @param param
     */
    @RequestMapping(
            value = {"/load"},
            method = {RequestMethod.POST},
            produces = {"application/json; charset=utf-8"}
    )
    @ResponseBody
    public void load(MethodArgument param) {
        String order = this.dynaBeanTemplate.buildOrderSql(param.getSort(), param.getOrderSql(), param.getUseOrderSql());
        DynaBean dynaBean = param.getDynaBean();
        dynaBean.set("$ORDER$", order);
        if (StringUtil.isNotEmpty(param.getExpandSql())) {
            dynaBean.set("$WHERE$", param.getWhereSql() + param.getExpandSql());
        }

        String queryType = param.getQueryType();
        String funcId;
        String strData;
        String queryParamValueStr;
        HashSet ddSet;
        ArrayList fieldVos;
        String totalCountName;
        String codeName;
        String msgName;
        JSONObject sortObj;
        String sortCode;
        EndUser currentUser;
        JSONArray arrays;
        JSONObject paramValues;
        String paramType;
        String defaultValue;
        JSONObject valObj;
        if ("procedure".equals(queryType)) {
            currentUser = SecurityUserHolder.getCurrentUser();
            funcId = param.getDatasourceName();
            strData = param.getProcedureName();
            queryParamValueStr = param.getQueryParamsStr();
            queryParamValueStr = param.getDbQueryObj();
            arrays = JSONArray.fromObject(queryParamValueStr);
            paramValues = JSONObject.fromObject(queryParamValueStr);
            Object[] params = new Object[arrays.size()];
            ddSet = new HashSet();
            ddSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
            ddSet.addAll(FrontCacheManager.getCacheValues().entrySet());
            ddSet.addAll(BackCacheManager.getCacheValues().entrySet());
            ddSet.addAll(WebUtils.getAllSysVar().entrySet());
            fieldVos = new ArrayList();
            totalCountName = "";
            codeName = "";
            msgName = "";
            paramType = "";
            defaultValue = "";

            String fieldType;
            for(int i = 0; i < arrays.size(); ++i) {
                valObj = arrays.getJSONObject(i);
                paramType = valObj.getString("name");
                fieldType = valObj.getString("fieldType");
                defaultValue = "1".equals(valObj.getString("paramType")) ? "out" : "in";
                DbFieldVo fieldVo = new DbFieldVo(paramType, defaultValue, fieldType);
                if (DbFieldType.NOWPAGE.equals(fieldType)) {
                    fieldVo.setValue(param.getPage());
                } else if (DbFieldType.LIMIT.equals(fieldType)) {
                    fieldVo.setValue(param.getLimit());
                } else if (DbFieldType.TOTALCOUNT.equals(fieldType)) {
                    totalCountName = paramType;
                } else if (DbFieldType.CODE.equals(fieldType)) {
                    codeName = paramType;
                } else if (DbFieldType.MSG.equals(fieldType)) {
                    msgName = paramType;
                } else {
                    JSONArray sortArrays;
                    if (DbFieldType.ORDERFIELD.equals(fieldType)) {
                        sortArrays = JSONArray.fromObject(StringUtil.getDefaultValue(param.getSort(), "[]"));
                        if (sortArrays.size() > 0) {
                            sortObj = sortArrays.getJSONObject(0);
                            sortCode = sortObj.getString("property");
                            fieldVo.setValue(sortCode);
                        }
                    } else if (DbFieldType.ORDERTYPE.equals(fieldType)) {
                        sortArrays = JSONArray.fromObject(StringUtil.getDefaultValue(param.getSort(), "[]"));
                        if (sortArrays.size() > 0) {
                            sortObj = sortArrays.getJSONObject(0);
                            sortCode = sortObj.getString("direction");
                            fieldVo.setValue(sortCode);
                        }
                    } else if (DbFieldType.ORDER.equals(fieldType)) {
                        sortArrays = JSONArray.fromObject(StringUtil.getDefaultValue(param.getSort(), "[]"));
                        String[] sortVals = new String[sortArrays.size()];

                        for(int j = 0; j < sortArrays.size(); ++j) {
                            sortObj = sortArrays.getJSONObject(j);
                            sortCode = sortObj.getString("property");
                            String sortType = sortObj.getString("direction");
                            sortVals[j] = sortCode + " " + sortType;
                        }

                        fieldVo.setValue(StringUtil.buildSplitString(sortVals, ","));
                    }
                }

                 defaultValue = "";
                if (valObj.containsKey("defaultValue")) {
                    defaultValue = valObj.getString("defaultValue");
                    if (StringUtil.isNotEmpty(defaultValue)) {
                        defaultValue = StringUtil.parseKeyWord(defaultValue, ddSet);
                        String val = StringUtil.getVarDefaultValue(defaultValue);
                        if (StringUtil.isNotEmpty(val)) {
                            fieldVo.setValue(val);
                        }
                    }
                }

                if (!"out".equalsIgnoreCase(defaultValue) && paramValues.containsKey(paramType)) {
                    sortObj = paramValues.getJSONObject(paramType);
                    Object val = null;
                    if (sortObj.containsKey("value")) {
                        val = sortObj.get("value");
                    }

                    if (StringUtil.isNotEmpty(val + "")) {
                        fieldVo.setValue(val);
                    }
                }

                fieldVos.add(fieldVo);
            }

            new HashMap();
            Map map;
            if (!StringUtil.isEmpty(funcId) && !"local".equals(funcId)) {
                SqlRunner sqlRunner = SqlRunner.getInstance(funcId);
                map = sqlRunner.queryMapProcedure("{call " + strData + "(" + this.pcDataService.getCallParams(params) + ")}", fieldVos);
            } else {
                map = this.pcServiceTemplate.queryMapProcedure("{call " + strData + "(" + this.pcDataService.getCallParams(params) + ")}", fieldVos);
            }

            if (StringUtil.isNotEmpty(codeName)) {
                Integer codeVal = Integer.parseInt(StringUtil.getDefaultValue(map.get(codeName) + "", "1"));
                if (codeVal < 0) {
                    paramType = "数据加载出错!";
                    if (StringUtil.isNotEmpty(msgName)) {
                        paramType = map.get(msgName) + "";
                    }

                    this.toWrite(jsonBuilder.returnFailureJson("\"" + paramType + "\""), param.getRequest(), param.getResponse());
                    return;
                }
            }

            List<Map> lists = new ArrayList();
            Integer allCount = 0;
            if (map.containsKey("rows")) {
                lists = (List)map.get("rows");
                allCount = ((List)lists).size();
            }

            if (map.containsKey(totalCountName)) {
                allCount = Integer.parseInt(map.get(totalCountName) + "");
            }

            fieldType = jsonBuilder.buildObjListToJson(new Long((long)allCount), (Collection)lists, true);
            this.toWrite(fieldType, param.getRequest(), param.getResponse());
        } else {
            String fieldType;
            if ("sql".equals(queryType)) {
                currentUser = SecurityUserHolder.getCurrentUser();
                funcId = param.getDatasourceName();
                strData = param.getDbSql();
                queryParamValueStr = param.getQueryParamsStr();
                queryParamValueStr = param.getDbQueryObj();
                arrays = JSONArray.fromObject(queryParamValueStr);
                paramValues = JSONObject.fromObject(queryParamValueStr);
                ddSet = new HashSet();
                ddSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
                ddSet.addAll(FrontCacheManager.getCacheValues().entrySet());
                ddSet.addAll(BackCacheManager.getCacheValues().entrySet());
                ddSet.addAll(WebUtils.getAllSysVar().entrySet());
                fieldVos = new ArrayList();

                for(int i = 0; i < arrays.size(); ++i) {
                    JSONObject obj = arrays.getJSONObject(i);
                    codeName = obj.getString("name");
                    msgName = obj.getString("fieldType");
                    paramType = "1".equals(obj.getString("paramType")) ? "out" : "in";
                    defaultValue = "";
                    DbFieldVo fieldVo = new DbFieldVo(codeName, paramType, msgName);
                    if (obj.containsKey("defaultValue")) {
                        defaultValue = obj.getString("defaultValue");
                        if (StringUtil.isNotEmpty(defaultValue)) {
                            defaultValue = StringUtil.parseKeyWord(defaultValue, ddSet);
                            fieldType = StringUtil.getVarDefaultValue(defaultValue);
                            if (StringUtil.isNotEmpty(fieldType)) {
                                fieldVo.setValue(fieldType);
                            }
                        }
                    }

                    if (!"out".equalsIgnoreCase(paramType) && paramValues.containsKey(codeName)) {
                        valObj = paramValues.getJSONObject(codeName);
                        Object val = null;
                        if (valObj.containsKey("value")) {
                            val = valObj.get("value");
                        }

                        if (StringUtil.isNotEmpty(val + "")) {
                            fieldVo.setValue(val);
                        }
                    }

                    fieldVos.add(fieldVo);
                }

                new ArrayList();
                new Long(0L);
                List lists;
                Long allCount;
                if (!StringUtil.isEmpty(funcId) && !"local".equals(funcId)) {
                    SqlRunner sqlRunner = SqlRunner.getInstance(funcId);
                    strData = DbProduceUtil.buildQuerySql(strData);
                    lists = sqlRunner.queryMapBySql(strData + param.getWhereSql() + order, fieldVos, param.getStart(), param.getLimit());
                    allCount = new Long((long)lists.size());
                    if (param.getLimit() > 0) {
                        allCount = sqlRunner.countMapBySql(strData + param.getWhereSql(), fieldVos);
                    }
                } else {
                    strData = DbProduceUtil.buildQuerySql(strData);
                    lists = this.pcServiceTemplate.queryMapBySql(strData + param.getWhereSql() + order, fieldVos, param.getStart(), param.getLimit());
                    allCount = new Long((long)lists.size());
                    if (param.getLimit() > 0) {
                        allCount = this.pcServiceTemplate.countMapBySql(strData + param.getWhereSql(), fieldVos);
                    }
                }

                codeName = jsonBuilder.buildObjListToJson(allCount, lists, true);
                this.toWrite(codeName, param.getRequest(), param.getResponse());
            } else if (!"iditprocedure".equals(queryType)) {
                new ArrayList();
                if (StringUtil.isNotEmpty(param.getQueryColumns())) {
                    dynaBean.set("$QUERY_FIELDS$", param.getQueryColumns());
                } else if (StringUtil.isNotEmpty(param.getFuncCode()) && param.getColumnLazy()) {
                    FuncInfo funcInfo = this.funInfoManager.getFuncInfo(param.getFuncCode());
                    strData = funcInfo.getQueryColumns();
                    dynaBean.set("$QUERY_FIELDS$", strData);
                }

                ArrayList list;
                if (param.getLimit() == -1) {
                    list = this.serviceTemplate.selectList(dynaBean);
                } else {
                    list = this.serviceTemplate.selectList(dynaBean, param.getStart(), param.getLimit());
                }

                if (param.getMark()) {
                    funcId = param.getFuncId();
                    currentUser = SecurityUserHolder.getCurrentUser();
                    this.dynaBeanTemplate.buildMarkInfo(list, param.getTableCode(), currentUser, funcId);
                }

                if (param.getPostil()) {
                    funcId = param.getFuncId();
                    this.dynaBeanTemplate.buildPostilInfo(list, param.getTableCode(), funcId);
                }

                if (param.getFuncEdit()) {
                    currentUser = SecurityUserHolder.getCurrentUser();
                    strData = param.getFuncCode();
                    this.dynaBeanTemplate.buildFuncEditInfo(list, param.getTableCode(), currentUser, strData);
                }

                Long count = dynaBean.getLong("$A_COUNT$");
                strData = jsonBuilder.buildListPageJson(count, list, true);
                this.toWrite(strData, param.getRequest(), param.getResponse());
            } else {
                String datasourceName = param.getDatasourceName();
                funcId = param.getProcedureName();
                strData = param.getQueryParamsStr();
                queryParamValueStr = param.getDbQueryObj();
                arrays = JSONArray.fromObject(strData);
                paramValues = JSONObject.fromObject(queryParamValueStr);
                Object[] params = new Object[arrays.size()];
                currentUser = SecurityUserHolder.getCurrentUser();
                ddSet = new HashSet();
                ddSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
                ddSet.addAll(FrontCacheManager.getCacheValues().entrySet());
                ddSet.addAll(BackCacheManager.getCacheValues().entrySet());
                ddSet.addAll(WebUtils.getAllSysVar().entrySet());
                fieldVos = new ArrayList();
                totalCountName = "";
                codeName = "";
                msgName = "";

                String name;
                for(int i = 0; i < arrays.size(); ++i) {
                    JSONObject obj = arrays.getJSONObject(i);
                    name = obj.getString("name");
                    fieldType = obj.getString("fieldType");
                    paramType = "1".equals(obj.getString("paramType")) ? "out" : "in";
                    DbFieldVo fieldVo = new DbFieldVo(name, paramType, fieldType);
                    if (DbFieldType.NOWPAGE.equals(fieldType)) {
                        fieldVo.setValue(param.getPage());
                    } else if (DbFieldType.LIMIT.equals(fieldType)) {
                        fieldVo.setValue(param.getLimit());
                    } else if (DbFieldType.TOTALCOUNT.equals(fieldType)) {
                        totalCountName = name;
                    } else if (DbFieldType.CODE.equals(fieldType)) {
                        codeName = name;
                    } else if (DbFieldType.MSG.equals(fieldType)) {
                        msgName = name;
                    } else if (DbFieldType.ORDER.equals(fieldType)) {
                        JSONArray sortArrays = JSONArray.fromObject(StringUtil.getDefaultValue(param.getSort(), "[]"));
                        String[] sortVals = new String[sortArrays.size()];

                        for(int j = 0; j < sortArrays.size(); ++j) {
                            sortObj = sortArrays.getJSONObject(j);
                            sortCode = sortObj.getString("property");
                            String sortType = sortObj.getString("direction");
                            sortVals[j] = sortCode + " " + sortType;
                        }

                        fieldVo.setValue(StringUtil.buildSplitString(sortVals, ","));
                    }

                    defaultValue = "";
                    if (obj.containsKey("defaultValue")) {
                        defaultValue = obj.getString("defaultValue");
                        if (StringUtil.isNotEmpty(defaultValue)) {
                            defaultValue = StringUtil.parseKeyWord(defaultValue, ddSet);
                            String val = StringUtil.getVarDefaultValue(defaultValue);
                            if (StringUtil.isNotEmpty(val)) {
                                fieldVo.setValue(val);
                            }
                        }
                    }

                    if (!"out".equalsIgnoreCase(paramType) && paramValues.containsKey(name)) {
                        valObj = paramValues.getJSONObject(name);
                        Object val = null;
                        if (valObj.containsKey("value")) {
                            val = valObj.get("value");
                        }

                        if (StringUtil.isNotEmpty(val + "")) {
                            fieldVo.setValue(val);
                        }
                    }

                    fieldVos.add(fieldVo);
                }

                new HashMap();
                Map map;
                if (!StringUtil.isEmpty(datasourceName) && !"local".equals(datasourceName)) {
                    SqlRunner sqlRunner = SqlRunner.getInstance(datasourceName);
                    map = sqlRunner.queryMapProcedure("{call " + funcId + "(" + this.pcDataService.getCallParams(params) + ")}", fieldVos);
                } else {
                    map = this.pcServiceTemplate.queryMapProcedure("{call " + funcId + "(" + this.pcDataService.getCallParams(params) + ")}", fieldVos);
                }

                if (StringUtil.isNotEmpty(codeName)) {
                    Integer codeVal = Integer.parseInt(StringUtil.getDefaultValue(map.get(codeName) + "", "1"));
                    if (codeVal < 0) {
                        name = "数据加载出错!";
                        if (StringUtil.isNotEmpty(msgName)) {
                            name = map.get(msgName) + "";
                        }

                        this.toWrite(jsonBuilder.returnFailureJson("\"" + name + "\""), param.getRequest(), param.getResponse());
                        return;
                    }
                }

                List<Map> lists = new ArrayList();
                Integer allCount = 0;
                if (map.containsKey("rows")) {
                    lists = (List)map.get("rows");
                    allCount = ((List)lists).size();
                }

                if (map.containsKey(totalCountName)) {
                    allCount = Integer.parseInt(map.get(totalCountName) + "");
                }

                fieldType = jsonBuilder.buildObjListToJson(new Long((long)allCount), (Collection)lists, true);
                this.toWrite(fieldType, param.getRequest(), param.getResponse());
            }
        }
    }


}
