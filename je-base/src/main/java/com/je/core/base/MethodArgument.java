package com.je.core.base;

import com.je.core.entity.QueryInfo;
import com.je.core.util.bean.DynaBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class MethodArgument {
    private String nowYear = (new Date().getYear() + 1900) + ""; //当前年
    @SuppressWarnings("deprecation")
    private String nowMonth = (new Date().getMonth() + 1) + ""; //当前月
    /**************************************辅助字段*****************************************/
    private DynaBean dynaBean = new DynaBean(); //动态实体
    private String tableCode;//表名称
    private String pkValue; // 主键值
    private String modelName;//前台向后面传递的类的全名称
    private String checked = "0"; // 1为checked 0为unchecked
    /**************************************VO字段*****************************************/
    private int start = 0; //从多少条开始
    private int page = 0;   //第几页
    private int limit = 30; //每页几条
    private String sort = "";//排序字段
    private String whereSql = "";//查询条件
    private String expandSql = "";//扩展查询条件
    private String parentSql = "";//主子功能SQL
    private String orderSql = ""; // 综合排序子句
    private String useOrderSql = "0";//是否引用orderSql  解决功能分组排序和点击列排序 传参一样， 列排序不追加orderSql，分组追加
    private String strData;//用于大数据资料的传输:大多是对象集合
    private QueryInfo queryInfo;//查询信息
    private String queryColumns;//查询字段
    private String ids; // 进行删除和选中操作时的ID列表，以逗号分隔
    private String sql = "";
    private String node;
    private String foreignKey = "";
    private String funcCode;
    private String codeGenFieldInfo = "";
    private String batchFilesFields = "";
    /**
     * 树形字段
     */
    private String rootId;
    private Boolean onlyItem;
    private String excludes = "";
    private String parentId;//父节点主键
    private int layer = 0;//所在的层数
    private String nodeType;
    private Boolean doTree = false; //是否树形操作
    private Boolean doCombine = false;
    private Boolean moreRoot = false;
    private Boolean columnLazy = false;
    private Boolean mark = false;
    private Boolean funcEdit=false;
    private Boolean postil = false;
    private Boolean removeAll = false;
    private Boolean initSys = false;
    private Boolean cascade=false;

    private String queryType;
    private String datasourceName;
    private String procedureName;
    private String queryParamsStr;
    private String dbQueryObj;
    private String dbSql;
    private String funcId;
    private String zhId;
    private String zhMc;
    private String type;
    private String code;
    private String value;

    /***
     * 文档参数
     */
    private String fileName;
    private String realPath;
    private String jeFileType;
    private String jeFileSaveType;
    private String contextType;
    private String path;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public MethodArgument() {

    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Boolean getInitSys() {
        return initSys;
    }

    public void setInitSys(Boolean initSys) {
        this.initSys = initSys;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getDbSql() {
        return dbSql;
    }

    public void setDbSql(String dbSql) {
        this.dbSql = dbSql;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getQueryParamsStr() {
        return queryParamsStr;
    }

    public void setQueryParamsStr(String queryParamsStr) {
        this.queryParamsStr = queryParamsStr;
    }

    public String getDbQueryObj() {
        return dbQueryObj;
    }

    public void setDbQueryObj(String dbQueryObj) {
        this.dbQueryObj = dbQueryObj;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getNowYear() {
        return nowYear;
    }

    public void setNowYear(String nowYear) {
        this.nowYear = nowYear;
    }

    public String getNowMonth() {
        return nowMonth;
    }

    public void setNowMonth(String nowMonth) {
        this.nowMonth = nowMonth;
    }

    public DynaBean getDynaBean() {
        return dynaBean;
    }

    public void setDynaBean(DynaBean dynaBean) {
        this.dynaBean = dynaBean;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getPkValue() {
        return pkValue;
    }

    public void setPkValue(String pkValue) {
        this.pkValue = pkValue;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

    public String getExpandSql() {
        return expandSql;
    }

    public void setExpandSql(String expandSql) {
        this.expandSql = expandSql;
    }

    public String getParentSql() {
        return parentSql;
    }

    public void setParentSql(String parentSql) {
        this.parentSql = parentSql;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public void setOrderSql(String orderSql) {
        this.orderSql = orderSql;
    }

    public String getUseOrderSql() {
        return useOrderSql;
    }

    public void setUseOrderSql(String useOrderSql) {
        this.useOrderSql = useOrderSql;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public QueryInfo getQueryInfo() {
        return queryInfo;
    }

    public void setQueryInfo(QueryInfo queryInfo) {
        this.queryInfo = queryInfo;
    }

    public String getQueryColumns() {
        return queryColumns;
    }

    public void setQueryColumns(String queryColumns) {
        this.queryColumns = queryColumns;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public String getCodeGenFieldInfo() {
        return codeGenFieldInfo;
    }

    public void setCodeGenFieldInfo(String codeGenFieldInfo) {
        this.codeGenFieldInfo = codeGenFieldInfo;
    }

    public String getBatchFilesFields() {
        return batchFilesFields;
    }

    public void setBatchFilesFields(String batchFilesFields) {
        this.batchFilesFields = batchFilesFields;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public Boolean getOnlyItem() {
        return onlyItem;
    }

    public void setOnlyItem(Boolean onlyItem) {
        this.onlyItem = onlyItem;
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Boolean getDoTree() {
        return doTree;
    }

    public void setDoTree(Boolean doTree) {
        this.doTree = doTree;
    }

    public Boolean getDoCombine() {
        return doCombine;
    }

    public void setDoCombine(Boolean doCombine) {
        this.doCombine = doCombine;
    }

    public Boolean getMoreRoot() {
        return moreRoot;
    }

    public void setMoreRoot(Boolean moreRoot) {
        this.moreRoot = moreRoot;
    }

    public Boolean getColumnLazy() {
        return columnLazy;
    }

    public void setColumnLazy(Boolean columnLazy) {
        this.columnLazy = columnLazy;
    }

    public Boolean getMark() {
        return mark;
    }

    public void setMark(Boolean mark) {
        this.mark = mark;
    }

    public Boolean getPostil() {
        return postil;
    }

    public void setPostil(Boolean postil) {
        this.postil = postil;
    }

    public Boolean getRemoveAll() {
        return removeAll;
    }

    public void setRemoveAll(Boolean removeAll) {
        this.removeAll = removeAll;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getJeFileSaveType() {
        return jeFileSaveType;
    }

    public void setJeFileSaveType(String jeFileSaveType) {
        this.jeFileSaveType = jeFileSaveType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getJeFileType() {
        return jeFileType;
    }

    public void setJeFileType(String jeFileType) {
        this.jeFileType = jeFileType;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getFuncEdit() {
        return funcEdit;
    }

    public void setFuncEdit(Boolean funcEdit) {
        this.funcEdit = funcEdit;
    }

    public String getZhId() {
        return zhId;
    }

    public void setZhId(String zhId) {
        this.zhId = zhId;
    }

    public String getZhMc() {
        return zhMc;
    }

    public void setZhMc(String zhMc) {
        this.zhMc = zhMc;
    }

    public Boolean getCascade() {
        return cascade;
    }

    public void setCascade(Boolean cascade) {
        this.cascade = cascade;
    }
}
