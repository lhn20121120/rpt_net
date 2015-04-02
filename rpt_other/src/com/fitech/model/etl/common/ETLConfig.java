package com.fitech.model.etl.common;

public class ETLConfig {
	
	/**
     * 系统的file.separator
     */
    public static final String FILESEPARATOR = System.getProperty("file.separator");
    /**
     * 操作系统类型
     */
    public static final String OS_TYPE = System.getProperty("file.separator").equals("\\") ? "windows" : "other";
	/**
	 * 频度任务是否并发 0:不是 1:是
	 */
	public static String getOsType() {
		return OS_TYPE;
	}

	/**
	 * 主题任务是否并发 0:不是 1:是
	 */
	public static final int SUBJECT_TASK_CONCURRENT = 0;
	/**
	 * 监控任务未结束标志
	 */
	public static final int TASK_OVER_NOT_FLAG = 0;
	/**
	 * 监控任务已结束标志
	 */
	public static final int TASK_OVER_YES_FLAG = 1;
	/**
	 * 主任务标志
	 */
	public static final int MAIN_TASK_ONE = 1;
	/**
	 * 非主任务标志
	 */
	public static final int MAIN_TASK_ZERO = 0;
	
	/****************ETL过程定义*******************/
	/**
	 * 过程等待阶段(等待上游系统初始化)
	 */
	public static final int PROC_ID_WAIT = 0;
	/**
	 * 数据抓取过程ID
	 */
	public static final int PROC_ID_CATCH = 1;
	/**
	 * 数据装载过程ID
	 */
	public static final int PROC_ID_LOAD = 2;
	/**
	 * 数据转换过程ID
	 */
	public static final int PROC_ID_CONVER = 3;
	
	/****************调用方式*******************/
	/**
	 * 常规调用
	 */
	public static final int ETL_CALL_NORM = 0;
	/**
	 * 自定义调用
	 */
	public static final int ETL_CALL_COUS = 1;
	
	/****************执行状态位标志*******************/
	/**
	 * 等待状态
	 */
	public static final int PROC_STATUS_WAIT = 1;
	/**
	 * 正在执行
	 */
	public static final int PROC_STATUS_RUNNING = 2;
	/**
	 * 结束
	 */
	public static final int PROC_STATUS_OVER = 3;
	/**
	 * 中断
	 */
	public static final int PROC_STATUS_BREAK =4;
	
	/****************问题类型标志位*******************/
	/**
	 * 没有异常标志
	 */
	public static final int PROBLUM_FLAG_INIT = 0;
	/**
	 * 异常标志位
	 */
	public static final int PROBLUM_FLAG_ERROR = 1;
	/**
	 * 预警标志位
	 */
	public static final int PROBLUM_FLAG_WARNING = 2;
	/**
	 * 流程异常/警告信息初始化未读取标志
	 */
	public static final int PROBLUM_READED_FLAG_INIT = 0;
	/**
	 * 流程异常/警告信息读取标志
	 */
	public static final int PROBLUM_READED_FLAG = 1;
	/**
	 * 不允许主任务执行标志
	 */
	public static final int EXEC_FLAG_NOT = 0;
	/**
	 * 允许主任务执行标志
	 */
	public static final int EXEC_FLAG_YES = 1;
	/**
	 * 外部接口的根路径
	 */
	public static String OUTER_BASE_PATH = "c:\\fitech2\\";
	/**
	 * 顶层用户的上级任务ID
	 */
	public static int SUPER_PRE_TASK_ID = -1;
	/**
	 * JAVA
	 */
	public static final String CUSTOM_TYPE_JAVA = "java";
	/**
	 * 存储过程
	 */
	public static final String CUSTOM_TYPE_PRODUCE = "produce";
	/**
	 * 脚本
	 */
	public static final String CUSTOM_TYPE_SCRIPT = "script";
	/**
	 * 存储过程执行成功标志
	 */
	public static final String PROCEDURE_SUCESS_FLAG = "yes";
	/**
	 * 初始化存储过程执行等待标志
	 */
	public static final String PROCEDURE_WAIT_FLAG = "wait";
	
	/***
	 * 数据库类型sqlserver,
	 */
	public static  String DB_SERVER_TYPE = "";
	
	/***
	 * 创建视图的名称
	 */
	public static final String CREATE_VIEW_NAME = "view_all_table";
	/*******************频度列表***********************/
	/**
	 * 日频度
	 */
	public static final String FREQ_DAY = "day";
	/**
	 * 月频度
	 */
	public static final String FREQ_MONTH = "month";
	/**
	 * 季频度
	 */
	public static final String FREQ_SEASON = "season";
	/**
	 * 半年频度
	 */
	public static final String FREQ_HALF_YEAR = "halfyear";
	/**
	 * 年频度
	 */
	public static final String FREQ_YEAR = "year";
	
	/**
	 * 系统路径
	 */
	public static String WEBROOTPATH ="";
	/**
	 * 脚本路径
	 */
	public static String SCRIPT_PATH = "";
	/**
	 * 脚本临时路径
	 */
	public static String SCRIPT_TMP_PATH = "";
	
	/***
	 * 是否执行美国银行特有环境发送email AutoPage类
	 */
	public static  boolean IS_SEND_EMAIL = true;
	
	
}
