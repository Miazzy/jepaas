package com.je.core.constants.push;

import com.je.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO 暂不明确
 */
public class PushType {

    private static Map<String, String> pushs = new HashMap<>();

    /***
     * 流程推送数字
     */
    public static final String WF = "WF";
    public static final String WF_NAME = "流程消息";
    /**
     * 消息(原小铃铛 JE_CORE_USERMSG)
     */
    public static final String MSG = "MSG";

    public static final String MSG_NAME = "系统信息";
    /**
     * 冒泡
     */
    public static final String BUBBLE = "BUBBLE";

    public static final String BUBBLE_NAME = "冒泡消息";
    /**
     * IM聊天
     */
    public static final String IM = "IM";
    public static final String IM_NAME = "IM消息";
    /**
     * 冒泡功能
     */
    public static final String BUBBLEFUNC = "BUBBLEFUNC";
    public static final String BUBBLEFUNC_NAME = "BUBBLEFUNC";
    /**
     * 批注
     */
    public static final String POSTIL = "POSTIL";
    public static final String POSTIL_NAME = "批注消息";
    /**
     * 事务交办
     */
    public static final String TRANSACTION = "TRANSACTION";
    public static final String TRANSACTION_NAME = "事务消息";
    /**
     * 日历任务
     */
    public static final String CALENDAR = "CALENDAR";
    public static final String CALENDAR_NAME = "日历消息";
    /**
     * 新闻数值
     */
    public static final String NOTICE = "NOTICE";
    public static final String NOTICE_NAME = "新闻消息";
    /**
     * 工作日志
     */
    public static final String RZ = "RZ";
    public static final String RZ_NAME = "日志消息";
//    /**
//     * CRM系统四连环
//     */
//    public static final String CRM_SLH="CRM_SLH";
    /**
     * APP功能
     */
    public static final String APPFUNC = "APPFUNC";
    public static final String APPFUNC_NAME = "APP消息";

    static {
        pushs.put(WF,WF_NAME);
        pushs.put(MSG,MSG_NAME);
        pushs.put(BUBBLE,BUBBLE_NAME);
        pushs.put(BUBBLEFUNC,BUBBLEFUNC_NAME);
        pushs.put(POSTIL,POSTIL_NAME);
        pushs.put(TRANSACTION,TRANSACTION_NAME);
        pushs.put(CALENDAR,CALENDAR_NAME);
        pushs.put(NOTICE,NOTICE_NAME);
        pushs.put(RZ,RZ_NAME);
        pushs.put(APPFUNC,APPFUNC_NAME);
    }

    public static String getNameByCode(String code) {
        if (StringUtil.isEmpty(code)) {
            return "";
        }
        return pushs.get(APPFUNC)==null?"系统消息":pushs.get(APPFUNC);
    }
}
