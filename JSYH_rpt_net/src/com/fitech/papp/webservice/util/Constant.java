package com.fitech.papp.webservice.util;

public class Constant {
	public static final String AUTH_SUCCESS = "0"; //认证接口设置session操作成功
	public static final String AUTH_FAILURE = "1"; //认证接口设置session操作失败
	public static final String AUTH_NOT_EXIST = "0"; // 认证接口用户不存在
	
	public static final String ORG_INSERT_SUCCESS = "0";//机构添加成功
	public static final String ORG_INSERT_FAILURE = "2";//机构添加失败
	public static final String ORG_INSERT_EXIST = "1";//机构添加时已经存在
	public static final String ORG_UPDATE_SUCCESS = "0";//机构更新成功
	public static final String ORG_UPDATE_FAILURE = "2";//机构更新失败
	public static final String ORG_UPDATE_EXIST = "1";//机构更新时已经存在
	public static final String ORG_DELETE_FAILURE = "0";//机构删除失败 
	public static final String ORG_DELETE_EXIST_USER = "1";//机构下存在用户信息，不能删除
	public static final String ORG_DELETE_EXIST_ORG = "2";//机构下存在下级机构，不能删除
	public static final String ORG_DELETE_SUCCESS = "3";//机构删除成功
	
	public static final String USER_INSERT_SUCCESS = "0";//用户添加成功
	public static final String USER_INSERT_EXIST_EMPLOY = "2";//根据工号判断用户存在
	public static final String USER_INSERT_EXIST_ID = "1";//用户添加时ID已经存在
	public static final String USER_INSERT_FAILURE = "3";//用户添加失败
	public static final String USER_UPDATE_SUCCESS = "1";//用户更新成功
	public static final String USER_UPDATE_FAILURE = "0";//用户更新失败
	public static final String USER_DELETE_FAILURE = "1";//用户删除失败 
	public static final String USER_DELETE_SUCCESS = "0";//用户删除成功

}
