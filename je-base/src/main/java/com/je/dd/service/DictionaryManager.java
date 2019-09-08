package com.je.dd.service;


import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.je.core.entity.QueryInfo;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.core.util.bean.DynaBean;
import com.je.dd.vo.DictionaryItemVo;

/**
 * TODO 暂不明确
 */
public interface DictionaryManager {
	/**
	 * 构建字典项
	 * @param voList TODO 暂不明确
	 * @param dic TODO 暂不明确
	 * @param queryInfo TODO 暂不明确
	 * @param itemCode TODO 暂不明确
	 * @param zwfFlag TODO 暂不明确
	 */
	public void buildChildrenList(List<DictionaryItemVo> voList, DynaBean dic, Boolean en, QueryInfo queryInfo, String itemCode, String zwfFlag);
	/**
	 * 获取指定列表字典的字典项
	 * @param whereSql 查询sql
	 * @return
	 */
	public JSONObject getAllListDicItem(String whereSql);
	/**
	 * 缓存所有列表字典
	 */
	public void doProAllListDicItem();
	/**
	 * 缓存所有字典信息
	 */
	public void doProAllDicInfo();

	/**
	 * 得到列表字典信息
	 * @param ddCode TODO 暂不明确
	 * @param queryInfo  TODO 暂不明确
	 * @param en  TODO 暂不明确
	 * @return
	 */
	public List<DictionaryItemVo> getDicList(String ddCode, QueryInfo queryInfo, Boolean en);
	/**
	 * 根据字典项Code获取字典Name值
	 * @param dicItems  TODO 暂不明确
	 * @param itemCode TODO 暂不明确
	 * @return
	 */
	public String getItemNameByCode(List<DictionaryItemVo> dicItems, String itemCode);

	/**
	 * 获取字典所有项  支持所有字典
	 * @param ddCode  TODO 暂不明确
	 * @param params  TODO 暂不明确
	 * @param queryInfo  TODO 暂不明确
	 * @param en  TODO 暂不明确
	 * @return
	 */
	public List<JSONTreeNode>getAllTyepDdListItems(String ddCode, Map<String, String> params, QueryInfo queryInfo, Boolean en);

	/**
	 * 更新省市县的分类信息
	 */
	public void syncSsxDic();
}