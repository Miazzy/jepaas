package com.je.develop.vo;
/**
 * Excel输出信息
 * @author zhangshuaipeng
 *
 */
public class ExcelReturnVo {
    /**
     * 执行操作   全局执行前 ALL_BEFORE  SHEET执行前 SHEET_BEFORE  SHEET单条执行前 SHEET_ONE_BEFORE  数据处理  SHEET_DATA 单条数据处理 SHEET_ONE_DATA SHEET执行后 SHEET_AFTER 全局数据处理 ALL_DATA  全局执行后  ALL_AFTER
     */
    private String doType;
    /**
     * 当前工作簿位置
     */
    private Integer sheetIndex;
    /**
     * 当前行位置
     */
    private Integer rowIndex;
    /**
     * 状态码
     * 取值参考 ExcelErrorCode
     */
    private int code;
    /**
     * 状态信息
     */
    private String msg;

    public ExcelReturnVo(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getDoType() {
        return doType;
    }
    public void setDoType(String doType) {
        this.doType = doType;
    }
    public Integer getSheetIndex() {
        return sheetIndex;
    }
    public void setSheetIndex(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
    }
    public Integer getRowIndex() {
        return rowIndex;
    }
    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

}
