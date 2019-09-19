package com.je.wf.vo;

import com.je.core.util.bean.DynaBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  TODO 暂不明确
 */
public class TaskUserVo {
    /**
     *  ToDoID待办或待阅的唯一标识
     */
    private String ToDoID;
    /**
     *  待办所属的人员数组，人员为登录名（短名） ，非姓名。必填。
     */
    private String UserIDs;
    private List<DynaBean> users=new ArrayList<DynaBean>();
    /**
     * 待办产生日期和时间。必填
     */
    private Date GenDate;
    /**
     * 待办的提交人，取登录名（短名） ，非姓名。
     */
    private String Submitter;
    /**
     * 待办的标题，最大长度为500个字符。必填。
     */
    private String ToDoTitle;
    /**
     * 待办的 URL。通过该URL可以打开待办，进行处理。最大长度为200个字符。必填。
     */
    private String ToDoURL;
    /**
     * 待办待阅标识， “0”为待办， “1”为待阅。
     */
    private String toDoFlag;
    /**
     *  紧急程度， “0”为“一般” ， “1”为“紧急” 。
     */
    private String instancyLv;
    /**
     * 保密程度， “0”为“一般” ， “1”为“秘密” 。
     */
    private String secrecyLv;
    /**
     * 文种类型，例如公司发文、公司收文、工作签报等。
     */
    private String fileClass;
    /**
     * 起草部门名称。
     */
    private String deptName;
    /**
     *  关键字，在检索时有用。
     */
    private String keyName;
    private String funcCode;
    private  String pkValue;
    public String getToDoID() {
        return ToDoID;
    }
    public void setToDoID(String toDoID) {
        ToDoID = toDoID;
    }
    public String getUserIDs() {
        return UserIDs;
    }
    public void setUserIDs(String userIDs) {
        UserIDs = userIDs;
    }
    public Date getGenDate() {
        return GenDate;
    }
    public void setGenDate(Date genDate) {
        GenDate = genDate;
    }
    public String getSubmitter() {
        return Submitter;
    }
    public void setSubmitter(String submitter) {
        Submitter = submitter;
    }
    public String getToDoTitle() {
        return ToDoTitle;
    }
    public void setToDoTitle(String toDoTitle) {
        ToDoTitle = toDoTitle;
    }
    public String getToDoURL() {
        return ToDoURL;
    }
    public void setToDoURL(String toDoURL) {
        ToDoURL = toDoURL;
    }
    public String getToDoFlag() {
        return toDoFlag;
    }
    public void setToDoFlag(String toDoFlag) {
        this.toDoFlag = toDoFlag;
    }
    public String getInstancyLv() {
        return instancyLv;
    }
    public void setInstancyLv(String instancyLv) {
        this.instancyLv = instancyLv;
    }
    public String getSecrecyLv() {
        return secrecyLv;
    }
    public void setSecrecyLv(String secrecyLv) {
        this.secrecyLv = secrecyLv;
    }
    public String getFileClass() {
        return fileClass;
    }
    public void setFileClass(String fileClass) {
        this.fileClass = fileClass;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getKeyName() {
        return keyName;
    }
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public List<DynaBean> getUsers() {
        return users;
    }

    public void setUsers(List<DynaBean> users) {
        this.users = users;
    }

    public String getPkValue() {
        return pkValue;
    }

    public String getFuncCode() {
        return funcCode;
    }

    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    public void setPkValue(String pkValue) {
        this.pkValue = pkValue;
    }
}
