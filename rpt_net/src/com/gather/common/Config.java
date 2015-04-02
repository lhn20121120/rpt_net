/*
 * Created on 2005-5-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gather.common;

/**
 * @author rds
 * 
 * 基础信息配置类
 */
public class Config {
	/**
	 * 系统的file.separator
	 */
	public static String FILESEPARATOR = System.getProperty("file.separator");
	
	/**
	 * zip包中的xml文件名
	 */
	public static final String REFER_FILE_XML_NAME = "listing.xml";
	/**
	 * 文件保存的目录
	 */
	public static final String FILE_ROOT = "data";
	/**
	 * 文件最终保存的目录
	 */
	public static final String FILE_RESULT = FILE_ROOT+FILESEPARATOR+"result";
	/**
	 * 下载用的目录
	 */
	public static final String DOWNLOAD = FILE_ROOT+FILESEPARATOR+"down";
	/**
	 * 下载模板用的目录
	 */
	public static final String 	DOWNLOAD_REPORT = DOWNLOAD+FILESEPARATOR+"report";
	/**
	 * 资源下载用的目录
	 */
	public static final String 	DOWNLOAD_RESOURCE = DOWNLOAD+FILESEPARATOR+"resource";
	/**
	 * 上传用的目录
	 */
	public static final String REFER = FILE_ROOT+FILESEPARATOR+"refer";
    /**
     * 上传报表用的目录
     */
	public static final String REFER_REPORT = REFER+FILESEPARATOR+"report";
    /**
     * 上传文件用的目录
     */
	public static final String REFER_FILE = REFER+FILESEPARATOR+"file";

	
	/**
	 * 外网采集系统名称
	 */
	public static String OUTER_SYSTEM_NAME = "gather";
	
	/**
	 * 模板下载zip文件临时文件夹
	 */
	public static String DOWNLOAD_DIR = "data";

	/**
	 * 系统错误Global Forward
	 */
	public static final String SYSTEM_ERROR_FORWARD = "systemError";

	/**
	 * 操作员登录后,将操作员对象保存到Session中的名称
	 */
	public static final String OPERATOR_SESSION_ATTRIBUTE_NAME = "Operator";

	/**
	 * 系统用户登录Global Forward
	 */
	public static final String LOGON_FORWARD = "logon";

	/**
	 * 返回给客户端的记录在Request和Session中的Attribute的名称
	 */
	public static final String RECORDS_ATTRIBUTE_NAME = "Records";

	/**
	 * MESSAGE在Request和Session中Attribute的名称
	 */
	public static final String MESSAGE_ATTRIBUTE_NAME = "Messages";

	/**
	 * ApartGage在Request和Session中Attribute的名称
	 */
	public static final String APARTPAGE_ATTRIBUTE_NAME = "ApartPage";



	/**
	 * 每页显示的记录数
	 */
	public static final int PER_PAGE_ROWS = 8;

	/**
	 * 日志类型(添加)
	 */
	public static final int LOG_TYPE_ADD = 1;

	/**
	 * 日志类型(修改)
	 */
	public static final int LOG_TYPE_EDIT = 2;

	/**
	 * 日志类型(删除)
	 */
	public static final int LOG_TYPE_DEL = 3;

	/**
	 * 日志类型(登录)
	 */
	public static final int LOG_TYPE_LOGIN = 4;

	/**
	 * 日志类型(退出)
	 */
	public static final int LOG_TYPE_EXIT = 5;

	/**
	 * @频度 ：月
	 */
	public static final int FREQUENCY_MONTH = 1;

	/**
	 * @频度 ：季
	 */
	public static final int FREQUENCY_SEASON = 2;

	/**
	 * @频度 ：半年
	 */
	public static final int FREQUENCY_HALF_YEAR = 3;

	/**
	 * @频度 ：年
	 */
	public static final int FREQUENCY_YEAR = 4;
	
	/**
	 * @数据范围 :法人
	 */
	public static final int CORPORATION = 1;
	/**
	 * @数据范围：国内
	 */
	public static final int REALM_IN = 2;
	/**
	 * @数据范围：境外
	 */
	public static final int REALM_OUT = 3;
	/**
	 * 工作类型id
	 */
	public static final int WORK_TYPE_ID = 1;
	
	/**
	 * 日志类别：登录
	 */
	
	public static final int LOG_LOGIN = 1;
	/**
	 * 日志类别：退出
	 */
	public static final int LOG_LOGOUT = 2;
	/**
	 * 日志类别：模板下载
	 */
	public static final int LOG_DOWN_REPORT = 3;
	/**
	 * 日志类别：数据上报
	 */
	public static final int LOG_REFER_REPORT = 4;
	/**
	 * 日志类别：上传文件
	 */
	public static final int LOG_REFER_FILE = 5;
}
