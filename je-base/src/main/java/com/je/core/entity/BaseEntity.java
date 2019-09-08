package com.je.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.core.ann.entity.FieldChineseName;
import com.je.core.ann.entity.TreeNode;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.constants.wf.AudFlagStatus;
import com.je.core.util.bean.DynaBean;

import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
/**
 * 基本实体类
 * @author YUNFENGCHENG
 * 2011-8-30 下午01:41:03
 */
@MappedSuperclass
public abstract class BaseEntity {
    @FieldChineseName(value="审核标记")
    private String audFlag = AudFlagStatus.NOSTATUS;
    @FieldChineseName(value="登记者所在部门编码")
    private String createOrg;
    @FieldChineseName(value="登记者所在部门名称")
    private String createOrgName;
    @FieldChineseName(value="登记时间")
    private String createTime;
    @FieldChineseName(value="登记人编码")
    private String createUser;
    @FieldChineseName(value="登记人姓名")
    private String createUserName;
    @FieldChineseName(value="是否启用本条数据")
    private String flag;
    @FieldChineseName(value="修改人部门编码")
    private String modifyOrg;
    @FieldChineseName(value="修改人部门名称")
    private String modifyOrgName;
    @FieldChineseName(value="修改时间")
    private String modifyTime;
    @FieldChineseName(value="修改人编码")
    private String modifyUser;
    @FieldChineseName(value="修改人姓名")
    private String modifyUserName;
    @FieldChineseName(value="数据状态")
    private String status;
    @FieldChineseName(value="排序字段")
    @TreeNode(type=TreeNodeType.ORDERINDEX)
    private Integer orderIndex;//排序字段
    @FieldChineseName(value="流程实例ID")
    private String PIID;
    @FieldChineseName(value="流程定义ID")
    private String PDID;
    @FieldChineseName(value="表单上传虚字段")
    private String formUploadFiles;

    private List<DynaBean> documentInfo;

    public String getAudFlag() {
        return audFlag;
    }
    public String getCreateOrg() {
        return createOrg;
    }
    public String getCreateOrgName() {
        return createOrgName;
    }
    public String getCreateTime() {
        return createTime;
    }
    public String getCreateUser() {
        return createUser;
    }
    public String getCreateUserName() {
        return createUserName;
    }
    public String getFlag() {
        return flag;
    }
    public String getModifyOrg() {
        return modifyOrg;
    }
    public String getModifyOrgName() {
        return modifyOrgName;
    }
    public String getModifyTime() {
        return modifyTime;
    }
    public String getModifyUser() {
        return modifyUser;
    }
    public String getModifyUserName() {
        return modifyUserName;
    }
    public String getStatus() {
        return status;
    }
    public void setAudFlag(String audFlag) {
        this.audFlag = audFlag;
    }
    public void setCreateOrg(String createOrg) {
        this.createOrg = createOrg;
    }
    public void setCreateOrgName(String createOrgName) {
        this.createOrgName = createOrgName;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public void setModifyOrg(String modifyOrg) {
        this.modifyOrg = modifyOrg;
    }
    public void setModifyOrgName(String modifyOrgName) {
        this.modifyOrgName = modifyOrgName;
    }
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }
    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @JsonIgnore
    @Transient
    public List<DynaBean> getDocumentInfo() {
        return documentInfo;
    }
    public void setDocumentInfo(List<DynaBean> documentInfo) {
        this.documentInfo = documentInfo;
    }
    public Integer getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    public String getPIID() {
        return PIID;
    }
    public void setPIID(String pIID) {
        PIID = pIID;
    }
    public String getPDID() {
        return PDID;
    }
    public void setPDID(String pDID) {
        PDID = pDID;
    }
    @Transient
    public String getFormUploadFiles() {
        return formUploadFiles;
    }
    public void setFormUploadFiles(String formUploadFiles) {
        this.formUploadFiles = formUploadFiles;
    }




}
