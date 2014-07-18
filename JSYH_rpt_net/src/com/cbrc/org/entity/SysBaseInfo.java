package com.cbrc.org.entity;


public class SysBaseInfo {

    private String userId;  //用户id
    private String mainType; //业务条线 如：khfx
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getMainType() {
        return mainType;
    }
    public void setMainType(String mainType) {
        this.mainType = mainType;
    }
    public SysBaseInfo(String userId, String mainType) {
        this.userId = userId;
        this.mainType = mainType;
    }
    public SysBaseInfo() {
    }

}
