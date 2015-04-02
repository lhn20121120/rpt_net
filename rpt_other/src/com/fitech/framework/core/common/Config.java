package com.fitech.framework.core.common;

public class Config {
	
	/**
     * 系统的file.separator
     */
    public static final String FILESEPARATOR = System
	    .getProperty("file.separator");

	// 系统根路径
	public static String WEBROOTPATH = "";

	// 分页每页记录数
	public static int PageSize = 10000;
	
	//是否显示公告信息 1是 0否
	public static int INCLUDE_COMMONINFO_FLAG = 1;
	
}
