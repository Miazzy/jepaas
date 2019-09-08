package com.je.phone.vo;

import com.je.core.util.bean.DynaBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * APK的所有功能跟插件
 */
public class ApkFuncVo implements Serializable {
    /**
     * TODO 未处理
     */
    private static final long serialVersionUID = 9122244749867438934L;
    /**
     * 所有功能
     */
    private List<DynaBean> funcs=new ArrayList<>();
    /**
     * 所有插件
     */
    private List<DynaBean> plugins=new ArrayList<>();
    /**
     * 所有角标的功能编码
     */
    private Set<String> badgeCodes=new HashSet<>();

    public List<DynaBean> getFuncs() {
        return funcs;
    }

    public void setFuncs(List<DynaBean> funcs) {
        this.funcs = funcs;
    }

    public List<DynaBean> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<DynaBean> plugins) {
        this.plugins = plugins;
    }

    public Set<String> getBadgeCodes() {
        return badgeCodes;
    }

    public void setBadgeCodes(Set<String> badgeCodes) {
        this.badgeCodes = badgeCodes;
    }
}
