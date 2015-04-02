package com.fitech.model.commoninfo.common;

public class CommonInfoConfig {

	/**
	 * 系统的file.separator
	 */
	public static final String FILESEPARATOR = System.getProperty("file.separator");
	
	/**
	 * 系统WEB应用的物理路径
	 */
	public static String WEBROOTPATH = "";

	/**
	 * 系统WEB应用的URL路径
	 */
	public static String WEBROOTULR = "";

	public static String NOT_IS_COLLECT="0";

	/**
	 * 系统操作用户存放在SESSION中的名称
	 */
	public static final String OPERATOR_SESSION_NAME = "Operator";
	
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
	
	/*******************任务类型***********************/
	/**
	 * 汇总流程
	 */
	public static final String TASK_TYPE_HZRW = "hzrw";
	/**
	 * 多级审核流程
	 */
	public static final String TASK_TYPE_DJSH = "djsh";
	/**
	 * 拼接汇总流程
	 */
	public static final String TASK_TYPE_PJHZ = "pjhz";
	
	/*******************业务条线***********************/
	/**
	 * 银监条线
	 */
	public static final String BUSI_LINE_YJTX = "yjtx";
	/**
	 * 人行条线
	 */
	public static final String BUSI_LINE_RHTX = "rhtx";
	/**
	 * 其他条线
	 */
	public static final String BUSI_LINE_QTTX = "qttx";
	
	/*******************触发方式***********************/
	/**
	 * 任务手动触发
	 */
	public static final String TRRIGER_TYPE_MANU = "manual";
	/**
	 * 任务自动触发
	 */
	public static final String TRRIGER_TYPE_AUTO = "auto";
	
	/*******************节点状态***********************/
	/**
	 * 等待事务
	 */
	public static final int NODE_FLAG_WAIT = 1;
	/**
	 * 待处理事务
	 */
	public static final int NODE_FLAG_WADC = 2;
	/**
	 * 已提交事务
	 */
	public static final int NODE_FLAG_COMM = 3;
	/**
	 * 事务通过审核
	 */
	public static final int NODE_FLAG_PASS = 4;
	/**
	 * 事务未通过审核
	 */
	public static final int NODE_FLAG_REFU = 5;
	/**
	 * 退回等待
	 */
	public static final int NODE_FLAG_REWA = 6;

	public static final String IS_COLLECT = "1";

	public static final String COLLECT_ORG_PARENT_ID = "-99";
	/**
	 * 退回节点类型(fill:退回至天报出 check:退回至复核)
	 */
	public static String WORK_TASK_COND_TYPE_ID = "fill";
	/**
	 * 填报处理类型
	 */
	public static String WORK_TASK_COND_TYPE_FILL = "fill";
	/**
	 * 复核处理类型
	 */
	public static String WORK_TASK_COND_TYPE_CHEC = "check";
	/**
	 * 父对象为空ID
	 */
	public final static int PRE_OBJECT_NULL = -1;
	/**
	 * 任务执行结束标志
	 */
	public final static int MONI_OVER_FLAG_YES = 1;
	/**
	 *  任务执行未结束标志
	 */
	public final static int MONI_OVER_FLAG_NOT = 0;
	/**
	 * 任务节点输入对象标志
	 */
	public final static int NODE_IO_FLAG_IMPORT = 0;
	/**
	 * 任务节点输出对象标志
	 */
	public final static int NODE_IO_FLAG_EXPORT = 1;
	/**
	 * 历史执行标志
	 */
	public final static int NODE_MONI_FINAL_FLAG_NOT = 0;
	/**
	 * 最终执行标志
	 */
	public final static int NODE_MONI_FINAL_FLAG_YES = 1;
	/**
	 * 正常报送标志位
	 */
	public final static int REREP_FLAG_NOT = 0;
	/**
	 * 重报标志位
	 */
	public final static int REREP_FLAG_YES = 1;
	/**
	 * 未迟报标志
	 */
	public final static int LATE_REP_FLAG_NOT = 0;
	/**
	 * 迟报标志
	 */
	public final static int LATE_REP_FLAG_YES = 1;
	/**
	 * 未漏报标志
	 */
	public final static int LEAV_REP_FLAG_NOT = 0;
	/**
	 * 漏报标志
	 */
	public final static int LEAV_REP_FLAG_YES = 1;
	/**
	 * 任务执行表失效标志
	 */
	public final static int TASK_EXEC_FLAG_NOT = 0;
	/**
	 * 任务执行表任务生效标志
	 */
	public final static int TASK_EXEC_FLAG_YES = 1;
	/**
	 * 任务未生效标志
	 */
	public final static int IS_PUBLIC_FLAG_NOT = 0;
	/**
	 * 任务已生效标志
	 */
	public final static int IS_PUBLIC_FLAG_YES = 1;
	
	/**
	 * 总行
	 */
	public final static String ORG_LEVLE_ZH = "0";
	/**
	 * 分行
	 */
	public final static String ORG_LEVLE_FH = "1";
	/**
	 * 支行
	 */
	public final static String ORG_LEVLE_CH = "2";
	/**
	 * 虚拟机构
	 */
	public final static String ORG_LEVLE_XN = "-99";
	/**
	 * 系统管理员登陆名称
	 */
	public final static String ADMIN = "admin";
	
	/**用户组权限分配--审签权限**/
	public static final Integer POWERTYPECHECK = new Integer(1);
	
	/**用户组权限分配--查看权限**/
	public static final Integer POWERTYPESEARCH = new Integer(2);
	
	/**用户组权限分配--报送权限**/
	public static final Integer POWERTYPEREPORT = new Integer(3);
	/**
	 * 选中全部value值
	 */
	public static final String LIST_SELECTED_ALL = "all";
	public static final String MSG_CONTENT = "模板信息变更,请重新下载制度包";
	
}
