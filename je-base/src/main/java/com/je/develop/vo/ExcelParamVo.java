package com.je.develop.vo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.je.core.util.bean.DynaBean;
/**
 * Excel的参数的VO
 * @author zhangshuaipeng
 *
 */
public class ExcelParamVo {
    /**
     * 请求对象
     */
    private HttpServletRequest request;
    /**
     * 所有excel的数据
     */
    private Map<String,List<DynaBean>> allValues;
    /**
     * 当前sheet的数据
     */
    private List<DynaBean> sheetValues;
    /**
     * 当前行的数据
     */
    private DynaBean dynaBean;
    /**
     * 执行操作     workbook   sheet  data
     */
    private String doType;
    /**
     * 当前sheet
     */
    private Integer nowSheet;
    /**
     * 当前sheet名称
     */
    private String nowSheetName;
    /**
     * 当前行
     */
    private Integer nowRow;
    /**
     * 数据字段信息
     */
    private Map<String,DynaBean> fieldInfos;
    /**
     * 全局执行前参数
     * @param request
     * @param doType
     */
    public ExcelParamVo(HttpServletRequest request, String doType) {
        super();
        this.request = request;
        this.doType = doType;
    }
    /**
     * 全局执行后参数
     * @param request
     * @param allValues
     * @param doType
     * @param fieldInfos
     */
    public ExcelParamVo(HttpServletRequest request,Map<String, List<DynaBean>> allValues, String doType) {
        super();
        this.request = request;
        this.allValues = allValues;
        this.doType = doType;
    }
    /**
     * 当前执行sheet前 数据处理 后事件
     * @param request
     * @param allValues
     * @param sheetValues
     * @param nowSheet
     * @param doType
     * @param fieldInfos
     */
    public ExcelParamVo(HttpServletRequest request,Map<String, List<DynaBean>> allValues, List<DynaBean> sheetValues,Integer nowSheet,String nowSheetName,String doType, Map<String, DynaBean> fieldInfos) {
        super();
        this.request = request;
        this.allValues = allValues;
        this.sheetValues = sheetValues;
        this.doType = doType;
        this.fieldInfos = fieldInfos;
        this.nowSheet=nowSheet;
        this.nowSheetName=nowSheetName;
    }
    /**
     * 逐条处理方法
     * @param request
     * @param allValues
     * @param dynaBean
     * @param doType
     * @param nowSheet
     * @param nowRow
     * @param fieldInfos
     */
    public ExcelParamVo(HttpServletRequest request,
                        Map<String, List<DynaBean>> allValues, DynaBean dynaBean,
                        String doType, Integer nowSheet,String nowSheetName, Integer nowRow,
                        Map<String, DynaBean> fieldInfos) {
        super();
        this.request = request;
        this.allValues = allValues;
        this.dynaBean = dynaBean;
        this.doType = doType;
        this.nowSheet = nowSheet;
        this.nowRow = nowRow;
        this.fieldInfos = fieldInfos;
        this.nowSheetName=nowSheetName;
    }
    public Map<String, List<DynaBean>> getAllValues() {
        return allValues;
    }
    public void setAllValues(Map<String, List<DynaBean>> allValues) {
        this.allValues = allValues;
    }
    public List<DynaBean> getSheetValues() {
        return sheetValues;
    }
    public void setSheetValues(List<DynaBean> sheetValues) {
        this.sheetValues = sheetValues;
    }
    public DynaBean getDynaBean() {
        return dynaBean;
    }
    public void setDynaBean(DynaBean dynaBean) {
        this.dynaBean = dynaBean;
    }
    public HttpServletRequest getRequest() {
        return request;
    }
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getDoType() {
        return doType;
    }

    public void setDoType(String doType) {
        this.doType = doType;
    }

    public Integer getNowSheet() {
        return nowSheet;
    }

    public void setNowSheet(Integer nowSheet) {
        this.nowSheet = nowSheet;
    }

    public Integer getNowRow() {
        return nowRow;
    }

    public void setNowRow(Integer nowRow) {
        this.nowRow = nowRow;
    }

    public Map<String, DynaBean> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(Map<String, DynaBean> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }
    public String getNowSheetName() {
        return nowSheetName;
    }
    public void setNowSheetName(String nowSheetName) {
        this.nowSheetName = nowSheetName;
    }

}
