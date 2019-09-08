package com.je.develop.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  功能SAAS租户定制信息
 */
public class FuncInfoSaas implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1907310192185037128L;
    private String funcId;
    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    /**
     * 工作流信息
     */
    private List<String> wfs=new ArrayList<>();
    private FuncPerm funcPerm;

    public List<String> getWfs() {
        return wfs;
    }

    public void setWfs(List<String> wfs) {
        this.wfs = wfs;
    }

    public FuncPerm getFuncPerm() {
        return funcPerm;
    }

    public void setFuncPerm(FuncPerm funcPerm) {
        this.funcPerm = funcPerm;
    }
}
