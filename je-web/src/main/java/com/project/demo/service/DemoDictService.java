package com.project.demo.service;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.dd.vo.DicInfoVo;

/**
 *  字典接口
 */
public interface DemoDictService {

    /**
     * 自定义字典
     * @param dicInfoVo
     * ddCode  字典编码
     * rootId  默认跟节点ID，值为root
     * whereSql  过滤条件
     * orderSql  排序SQL
     * @return
     */
    public JSONTreeNode getProjectTree(DicInfoVo dicInfoVo);

}
