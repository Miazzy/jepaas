package com.je.core.security.service;

import com.je.core.result.BaseRespResult;
import com.je.core.util.bean.DynaBean;
import com.je.rbac.model.EndUser;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface LoginService  {
    /**
     * 登录
     * @param request
     * @return
     */
    public BaseRespResult<String> login(HttpServletRequest request);
    /**
     * 选中登录用户
     * @param request
     * @return
     */
    public BaseRespResult<String> checkUser(HttpServletRequest request);
    /**
     * TODO 暂不明确
     * @param user
     * @param deptId
     * @return
     */
    public EndUser buildCurrentUser(DynaBean user, String deptId);

    /**
     * 构建用户信息
     * @param user
     * @return
     */
    public EndUser buildUserInfo(DynaBean user);
    public void doGtInfo(EndUser currentUser, HttpServletRequest request);

    /**
     * 记录登录日志
     * @param currentUser
     */
    public void doLoginLog(EndUser currentUser, JSONObject loginInfo);

    /**
     * 检测登录日志
     */
    public void doCheckLoginLog();
    /**
     * @param userId    登陆用户id
     * @param loginType 登陆类型:web,mobile
     * @param uid       设备id
     * @param tokenId   新的tokenId
     * @return
     */
    public String buildTokenId(String userId, String loginType, String uid, String tokenId);

    /**
     * 退出登录
     * @param tokenId
     */
    void logOut(String tokenId);

    /**
     * TODO 暂不明确
     * @param dsfStateBean
     * @param dsfCode
     * @param obj
     * @param request
     * @return
     */
    public String getDsfLoginOpenId(DynaBean dsfStateBean, String dsfCode, JSONObject obj, HttpServletRequest request);
}
