package com.cbrc.org.entity;

import java.util.Date;

public class SysUser {

    private String userName;
    private String passWord;  //密码
    private String realName;  //姓名
    private String isSuper;   //超级用户标志
    private String orgId;   //机构代码
    private String updateDate;  //更新日期
    private String telphoneNumber;   //电话号码
    private String deparTment;   //所属部门
    private String email;   //email
    private String address;  //通讯地址
    private String postCode;  //邮编
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassWord() {
        return passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getIsSuper() {
        return isSuper;
    }
    public void setIsSuper(String isSuper) {
        this.isSuper = isSuper;
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public String getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    public String getTelphoneNumber() {
        return telphoneNumber;
    }
    public void setTelphoneNumber(String telphoneNumber) {
        this.telphoneNumber = telphoneNumber;
    }
    public String getDeparTment() {
        return deparTment;
    }
    public void setDeparTment(String deparTment) {
        this.deparTment = deparTment;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPostCode() {
        return postCode;
    }
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
    public SysUser(String userName, String passWord, String realName,
                   String isSuper, String orgId, String updateDate,
                   String telphoneNumber, String deparTment, String email,
                   String address, String postCode) {
        this.userName = userName;
        this.passWord = passWord;
        this.realName = realName;
        this.isSuper = isSuper;
        this.orgId = orgId;
        this.updateDate = updateDate;
        this.telphoneNumber = telphoneNumber;
        this.deparTment = deparTment;
        this.email = email;
        this.address = address;
        this.postCode = postCode;
    }
    public SysUser() {
    }
}
