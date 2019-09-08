package com.je.phone.vo;

import com.je.core.util.bean.DynaBean;

import java.io.Serializable;
import java.util.List;

/**
 *TODO未处理
 */
public class AppFuncVo implements Serializable {
    /**
     * TODO未处理
     */
    private static final long serialVersionUID = -835506299595537221L;
    private DynaBean app;
    private List<DynaBean> fields;

    public DynaBean getApp() {
        return app;
    }

    public void setApp(DynaBean app) {
        this.app = app;
    }

    public List<DynaBean> getFields() {
        return fields;
    }

    public void setFields(List<DynaBean> fields) {
        this.fields = fields;
    }
}
