package com.project.demo.service;

import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.TreeUtil;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DicInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("demoDictService")
public class DemoDictServiceImpl implements DemoDictService {

    @Autowired
    private PCDynaServiceTemplate serviceTemplate;

    /**
     *  页面操作
     *  进入字典菜单页面，点击自定义，点击新建
     *  录入基本信息
     *  类名：demoDictService
     *  方法名：getProjectTree
     */
    /**
     * 自定义字典
     * ddCode  字典编码
     * rootId  默认跟节点ID，值为root
     * whereSql  过滤条件
     * orderSql  排序SQL
     * @param dicInfoVo
     * @return
     */
    @Override
    public JSONTreeNode getProjectTree(DicInfoVo dicInfoVo) {
        JSONTreeNode rootNode = TreeUtil.buildRootNode();
        //获取自定义字典表中数据集合
        List<DynaBean> dynaBeanList = serviceTemplate.selectList("JE_DEMO_XMINFO", "AND 1 = 1");
        //获取根节点数据集合
        List<DynaBean> dynaBeanNodeList = this.getRootNode(dynaBeanList);
        //定义一个集合，用来存放根节点信息
        List<JSONTreeNode> childrenList = new ArrayList<>();
        //循环根节点，并且封装根节点以下节点
        for(DynaBean dynaBean : dynaBeanNodeList){
            JSONTreeNode node = TreeUtil.buildRootNode();
            node.setId((String)dynaBean.get("JE_DEMO_XMINFO_ID"));//主键ID
            node.setCode((String)dynaBean.get("XMINFO_XMBM"));//编码
            node.setText((String)dynaBean.get("XMINFO_XMMC"));//名称
            node.setIcon("");//图标自己看着定义
            //看是否有下级节点
            buildChilNode(node, dynaBeanList);
            childrenList.add(node);
        }
        //把根节点放入集合中
        rootNode.setChildren(childrenList);
        return rootNode;
    }
    /**
     *  递归迭代，封装根节点下面的节点
     * @param rootNode    跟节点信息
     * @param dynaBeanList 所有数据集合
     * @return
     */
    private JSONTreeNode buildChilNode(JSONTreeNode rootNode, List<DynaBean> dynaBeanList){
        List<JSONTreeNode> childrenList = new ArrayList<>();
        for(DynaBean dynaBean : dynaBeanList){
            if(dynaBean.get("XMINFO_PARENTID").equals(rootNode.getId())){  //寻找父节点下有无信息,
                JSONTreeNode node = new JSONTreeNode();
                node.setId((String)dynaBean.get("JE_DEMO_XMINFO_ID"));
                node.setCode((String)dynaBean.get("XMINFO_XMBM"));
                node.setText((String)dynaBean.get("XMINFO_XMMC"));
                node.setIcon("");//图标自己看着定义
                //把找到的下级节点放入集合中，并且看此节点下还有没有下级节点
                childrenList.add(buildChilNode(node, dynaBeanList));
            }
        }
        //把此节点下的下级节点集合set进去
        rootNode.setChildren(childrenList);
        return rootNode;
    }
    /**
     *  获取根节点信息
     * @param dynaBeanList 所有数据集合
     * @return
     */
    private List<DynaBean> getRootNode(List<DynaBean> dynaBeanList){
        List<DynaBean> beanList = new ArrayList<>();
        for(DynaBean dynaBean : dynaBeanList){
            //表中根节点的值为ROOT_RES
            if("ROOT".equals((String)dynaBean.get("XMINFO_PARENTID"))){ //获取根节点数据
                beanList.add(dynaBean);
            }
        }
        return beanList;
    }

}
