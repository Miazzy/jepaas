package com.je.rbac.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.jbpm.api.identity.User;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.je.core.entity.BaseEntity;

/**
 * 最终用户实体,由于Oracle不能创建user表故,改用ENDUSER
 * @author 研发部:云凤程
 * 2012-1-18 下午02:22:09
 */
//@Entity
//@GenericGenerator(name="systemUUID",strategy="uuid")
//@Table(name="JE_CORE_ENDUSER",uniqueConstraints={@UniqueConstraint(columnNames="USERCODE")})
public class EndUser extends BaseEntity implements User,Serializable {
	private static final long serialVersionUID = 7988184503759859594L;
	/**
	 *
	 */
	//主键
	private String userId;
	//用户编码
	private String userCode;
	//用户名称
	private String username;
	//用户密码
	private String password;
	//是否主管
	private String isManager;
	//监管部门
	private String monitorDept;
	//监管部门
	private String monitorDeptCode;
	//是否系统用户
	private String isSysUser;
	//所在部门
	private Department dept = new Department();
	//性别  0男  1女
	private String gender="0";
	//是否分属部门用户
	private String shadow;
	//原用户名
	private String backUserCode;
	//图标样式
	private String iconCls;
	//部门ID
	private String deptId;
	//部门编码
	private String deptCode;
	//部门名称
	private String deptName;
	//角色名称
	private String roleNames;
	//角色编码
	private String roleCodes;
	//角色主键
	private String roleIds;
	//岗位名称
	private String sentryNames;
	//岗位编码
	private String sentryCodes;
	//岗位主键
	private String sentryIds;
	//身份证
	private String idCard;
	//员工卡
	private String userCard;
	//工号
	private String jobNum;
	//其他证件
	private String otherCard;
	//公司邮箱
	private String companyEmail;
	//其他邮箱
	private String otherEmail;
	//出生日期
	private String birthday;
	//民族
	private String nation;
	//籍贯
	private String nativePlace;
	//文化程度
	private String degree;
	//婚姻状况
	private String married;
	//头像
	private String photo;
	//工作经历
	private String workExperience;
	//紧急联系人
	private String contacts;
	private String loginLocked="0";
	//成员算在的角色
	private Set<Role> roles = new HashSet<Role>();
	private Set<Sentry> sentrys=new HashSet<Sentry>();
	//用户拥有的权限
	private Set<Permission> permissions = new HashSet<Permission>();
	// Transient 下级部门code列表
	private String subordinates;
	// RTX ID
	private String rtxId;
	//全称
	private String fullName;
	//简称
	private String easyName;
	//工种名称
	private String craftName;
	//工种编码
	private String craftCode;
	//行政职务
	private String executiveName;
	//行政职务编码
	private String executiveCode;
	//电话
	private String phone;
	//座机
	private String zuoJi;
	//有效期
	private String expiryDate;
	//失效时间
	private String failureTime;
	//用户排序字段
	private Double userOrder;
	//部门排序信息
	private String deptOrderIndex;
	//代理用户
	private EndUser proxyUser;
	//是否代理登录
	private Boolean proxy=false;
	//是否拥有开发权限
	private Boolean funcConfig=false;
	//代理排除权限ID
	private String excludePerms;
	//登录用户名称
	private String loginUserName;
	//登录用户编码
	private String loginUserCode;
	//登录用户ID
	private String loginUserId;
	//集团公司名称
	private String jtgsMc;
	//公司部门ID
	private String gsbmId;
	//集团公司代码
	private String jtgsDm;
	//集团公司主键
	private String jtgsId;
	//主题
	private String theme;
	//直属领导
	private String zsldName;
	//直属领导ID
	private String zsldId;
	//第三方登录
	private String singleLogin;
	//声音
	private String sound;
	//PLUS账号或者手机
	private String plusUserCode;
	//PLUS密码
	private String plusUserPass;
	//PLUS是否记住密码
	private String plusRememberPass;
	//PLUS是否自动登录
	private String plusAutoLogin;
	//租户名称
	private String zhMc;
	//租户ID
	private String zhId;
	//租户数据源
	private String zhDs;
	//是否是开发人员
	private Boolean saas;
	//是否saas管理员
	private Boolean saasAdmin;
	private AdminPermInfo adminPermInfo;
	private String locked;
	private String menuType;
	private int loginNumber;
	private String loginTime;
	private String identPhone;
	//是否有效
	private String valid;
	/**
	 * 是否初始化了密码
	 */
	private String initPassWord;
	/**
	 * 该人员领导的信息
	 */

	private UserLeader leaderInfo=new UserLeader();
	/**
	 * 该人员下属的信息
	 */
	private UserLeader branchInfo=new UserLeader();
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public Set<Sentry> getSentrys() {
		return sentrys;
	}
	public void setSentrys(Set<Sentry> sentrys) {
		this.sentrys = sentrys;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getUserCard() {
		return userCard;
	}
	public void setUserCard(String userCard) {
		this.userCard = userCard;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
		if(null != dept) {
			deptId = dept.getDeptId();
		}
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#getUsername()
	 */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getSubordinates() {
		return subordinates;
	}
	public void setSubordinates(String subordinates) {
		this.subordinates = subordinates;
	}
	public String getIsManager() {
		return isManager;
	}
	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}
	public String getMonitorDept() {
		return monitorDept;
	}
	public void setMonitorDept(String monitorDept) {
		this.monitorDept = monitorDept;
	}
	public String getMonitorDeptCode() {
		return monitorDeptCode;
	}
	public void setMonitorDeptCode(String monitorDeptCode) {
		this.monitorDeptCode = monitorDeptCode;
	}
	public String getRtxId() {
		return rtxId;
	}
	public void setRtxId(String rtxId) {
		this.rtxId = rtxId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getIsSysUser() {
		return isSysUser;
	}
	public void setIsSysUser(String isSysUser) {
		this.isSysUser = isSysUser;
	}
	public String getJobNum() {
		return jobNum;
	}
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEasyName() {
		return easyName;
	}
	public void setEasyName(String easyName) {
		this.easyName = easyName;
	}
	public String getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	public String getRoleCodes() {
		return roleCodes;
	}
	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}
	public String getSentryNames() {
		return sentryNames;
	}
	public void setSentryNames(String sentryNames) {
		this.sentryNames = sentryNames;
	}
	public String getSentryCodes() {
		return sentryCodes;
	}
	public void setSentryCodes(String sentryCodes) {
		this.sentryCodes = sentryCodes;
	}
	public String getSentryIds() {
		return sentryIds;
	}
	public void setSentryIds(String sentryIds) {
		this.sentryIds = sentryIds;
	}
	public String getShadow() {
		return shadow;
	}
	public void setShadow(String shadow) {
		this.shadow = shadow;
	}
	public String getBackUserCode() {
		return backUserCode;
	}
	public void setBackUserCode(String backUserCode) {
		this.backUserCode = backUserCode;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getOtherCard() {
		return otherCard;
	}
	public void setOtherCard(String otherCard) {
		this.otherCard = otherCard;
	}
	public String getCompanyEmail() {
		return companyEmail;
	}
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}
	public String getOtherEmail() {
		return otherEmail;
	}
	public void setOtherEmail(String otherEmail) {
		this.otherEmail = otherEmail;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getMarried() {
		return married;
	}
	public void setMarried(String married) {
		this.married = married;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getWorkExperience() {
		return workExperience;
	}
	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getCraftName() {
		return craftName;
	}
	public void setCraftName(String craftName) {
		this.craftName = craftName;
	}
	public String getCraftCode() {
		return craftCode;
	}
	public void setCraftCode(String craftCode) {
		this.craftCode = craftCode;
	}
	public String getExecutiveName() {
		return executiveName;
	}
	public void setExecutiveName(String executiveName) {
		this.executiveName = executiveName;
	}
	public String getExecutiveCode() {
		return executiveCode;
	}
	public void setExecutiveCode(String executiveCode) {
		this.executiveCode = executiveCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getFailureTime() {
		return failureTime;
	}
	public void setFailureTime(String failureTime) {
		this.failureTime = failureTime;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public Double getUserOrder() {
		return userOrder;
	}
	public void setUserOrder(Double userOrder) {
		this.userOrder = userOrder;
	}
	public String getDeptOrderIndex() {
		return deptOrderIndex;
	}
	public void setDeptOrderIndex(String deptOrderIndex) {
		this.deptOrderIndex = deptOrderIndex;
	}
	public EndUser getProxyUser() {
		return proxyUser;
	}
	public void setProxyUser(EndUser proxyUser) {
		this.proxyUser = proxyUser;
	}
	public Boolean getProxy() {
		return proxy;
	}
	public void setProxy(Boolean proxy) {
		this.proxy = proxy;
	}
	public Boolean getFuncConfig() {
		return funcConfig;
	}
	public void setFuncConfig(Boolean funcConfig) {
		this.funcConfig = funcConfig;
	}
	public String getExcludePerms() {
		return excludePerms;
	}
	public void setExcludePerms(String excludePerms) {
		this.excludePerms = excludePerms;
	}
	public String getLoginUserName() {
		return loginUserName;
	}
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	public String getLoginUserCode() {
		return loginUserCode;
	}
	public void setLoginUserCode(String loginUserCode) {
		this.loginUserCode = loginUserCode;
	}
	public String getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}
	public String getJtgsMc() {
		return jtgsMc;
	}
	public void setJtgsMc(String jtgsMc) {
		this.jtgsMc = jtgsMc;
	}
	public String getJtgsDm() {
		return jtgsDm;
	}
	public void setJtgsDm(String jtgsDm) {
		this.jtgsDm = jtgsDm;
	}
	public String getJtgsId() {
		return jtgsId;
	}
	public void setJtgsId(String jtgsId) {
		this.jtgsId = jtgsId;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getZuoJi() {
		return zuoJi;
	}
	public void setZuoJi(String zuoJi) {
		this.zuoJi = zuoJi;
	}
	public String getGsbmId() {
		return gsbmId;
	}
	public void setGsbmId(String gsbmId) {
		this.gsbmId = gsbmId;
	}

	public String getZsldName() {
		return zsldName;
	}
	public void setZsldName(String zsldName) {
		this.zsldName = zsldName;
	}
	public String getZsldId() {
		return zsldId;
	}
	public void setZsldId(String zsldId) {
		this.zsldId = zsldId;
	}
	public String getSingleLogin() {
		return singleLogin;
	}
	public void setSingleLogin(String singleLogin) {
		this.singleLogin = singleLogin;
	}
	public AdminPermInfo getAdminPermInfo() {
		return adminPermInfo;
	}
	public void setAdminPermInfo(AdminPermInfo adminPermInfo) {
		this.adminPermInfo = adminPermInfo;
	}
	public String getLocked() {
		return locked;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	public Boolean getSaas() {
		return saas;
	}
	public void setSaas(Boolean saas) {
		this.saas = saas;
	}
	public String getPlusUserCode() {
		return plusUserCode;
	}
	public void setPlusUserCode(String plusUserCode) {
		this.plusUserCode = plusUserCode;
	}
	public String getPlusUserPass() {
		return plusUserPass;
	}
	public void setPlusUserPass(String plusUserPass) {
		this.plusUserPass = plusUserPass;
	}
	public String getPlusRememberPass() {
		return plusRememberPass;
	}
	public void setPlusRememberPass(String plusRememberPass) {
		this.plusRememberPass = plusRememberPass;
	}
	public String getPlusAutoLogin() {
		return plusAutoLogin;
	}
	public void setPlusAutoLogin(String plusAutoLogin) {
		this.plusAutoLogin = plusAutoLogin;
	}
	public String getSound() {
		return sound;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getLoginLocked() {
		return loginLocked;
	}
	public void setLoginLocked(String loginLocked) {
		this.loginLocked = loginLocked;
	}
	public String getZhMc() {
		return zhMc;
	}
	public void setZhMc(String zhMc) {
		this.zhMc = zhMc;
	}
	public String getZhId() {
		return zhId;
	}
	public void setZhId(String zhId) {
		this.zhId = zhId;
	}
	public String getZhDs() {
		return zhDs;
	}
	public void setZhDs(String zhDs) {
		this.zhDs = zhDs;
	}

	public int getLoginNumber() {
		return loginNumber;
	}

	public void setLoginNumber(int loginNumber) {
		this.loginNumber = loginNumber;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getIdentPhone() {
		return identPhone;
	}

	public void setIdentPhone(String identPhone) {
		this.identPhone = identPhone;
	}

	public Boolean getSaasAdmin() {
		return saasAdmin;
	}

	public void setSaasAdmin(Boolean saasAdmin) {
		this.saasAdmin = saasAdmin;
	}
	public UserLeader getLeaderInfo() {
		return leaderInfo;
	}

	public void setLeaderInfo(UserLeader leaderInfo) {
		this.leaderInfo = leaderInfo;
	}
	public UserLeader getBranchInfo() {
		return branchInfo;
	}

	public void setBranchInfo(UserLeader branchInfo) {
		this.branchInfo = branchInfo;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}
	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#isAccountNonExpired()
	 */
//	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#isAccountNonLocked()
	 */
//	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#isCredentialsNonExpired()
	 */
//	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetails#isEnabled()
	 */
//	@Transient
	public boolean isEnabled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.User#getBusinessEmail()
	 */
//	@Override
//	@Transient
	public String getBusinessEmail() {
		return this.companyEmail;
	}
	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.User#getFamilyName()
	 */
//	@Override
//	@Transient
	public String getFamilyName() {
		return this.username;
	}
	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.User#getGivenName()
	 */
	@Override
//	@Transient
	public String getGivenName() {
		return this.username;
	}
	/* (non-Javadoc)
	 * @see org.jbpm.api.identity.User#getId()
	 */
	@Override
//	@Transient
	public String getId() {
		return this.userId;
	}

	public String getInitPassWord() {
		return initPassWord;
	}

	public void setInitPassWord(String initPassWord) {
		this.initPassWord = initPassWord;
	}
}
