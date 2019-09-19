package com.je.postil.service;

import com.je.core.util.bean.DynaBean;
import com.je.postil.vo.PostilVo;
import com.je.rbac.model.EndUser;
import net.sf.json.JSONObject;

import java.util.List;

public interface PostilService {
    /**
     * 保存批注信息
     * @param dynaBean 动态类
     * @return
     */
    public List<DynaBean> savePostil(DynaBean dynaBean, EndUser currentUser);

    /**
     * 加载批注信息
     * @param userId 用户id
     * @param keyWord TODO 暂不明确
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    public List<PostilVo> loadPostils(String type, String userId, String keyWord, String startDate, String endDate, int start, int limitt, JSONObject returnObj);

    /**
     * 设置成全部已读
     * @param keyWord
     */
    public void readAllPostil(String keyWord);
//    /**
//     * 获取平台功能配置信息
//     * @param funcCode
//     * @return
//     */
//    public FuncPostilConfig getFuncPostilConfig(String funcCode);
}
