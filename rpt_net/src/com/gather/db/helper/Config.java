/** 姬怀宝
	 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
	 * It has a set of methods to handle persistence for MRepRange data (i.e. 
	 * com.gather.struts.forms.MRepRangeForm objects).
	 * 
	 * @author <strong>Generated by Middlegen.</strong>
	 */
package com.gather.db.helper;

public class Config {
	/**
	 * 系统的file.separator
	 */
	public static final String FILESEPARATOR = System.getProperty("file.separator");
	
	/**
	 * 系统错误页的Forward
	 */
	public static final String FORWARD_SYS_ERROR_PAGE="sysErrPage";
	
	/**
	 * 系统WEB应用的虚拟路径 
	 */
	public static String WEBROOTPATH = "";
	
	/**
	 * 分页对象的存放在Request中的名称
	 */
	public static final String APART_PAGE_OBJECT="ApartPage";
	/**
	 * 当前页码存放在Request中的名称
	 */
	public static final String CUR_PAGE_OBJECT="CurPage";
	/**
	 * 分页显示记录时，每页显示的记录数
	 */
	public static final int PER_PAGE_ROWS=8;
	
	/**
	 * 系统操作用户存放在SESSION中的名称
	 */
	public static final String OPERATOR_SESSION_NAME="Operator";
    /**
     * 返回查询记录集的名称
     */
	public static final String RECORDS="Records";
	
	/**
	 * 返回信息的名称
	 */
	public static final String MESSAGES ="Message";
	
	/**
	 * 临时事件存放的目录
	 */
	public static String TEMP_DIR="";
	/**
	 * 上传文件的最大大小
	 */
	public static final int FILE_MAX_SIZE=1024*1024;
	
	
	/************************************************************************
	 * 文件后缀常量定义
	 ************************************************************************/
	/**
	 * PDF报表模板文件后缀
	 */
	public static final String EXT_PDF="pdf";
	/**
	 * PDF报表模板文件类型
	 */
	public static final String FILE_CONTENT_TYPE_PDF="application/pdf";
	/**
	 * TXT关系表达式定义文件后缀
	 */
	public static final String EXT_TXT="txt";
	/**
	 * TXT关系表达式定义文件类型
	 */
	public static final String FILE_CONTENT_TYPE_TXT="";
	/************************************************************************
	 * 日志类型常量定义
	 ************************************************************************/
	/**
	 * 操作日志
	 */
	public static Integer LOG_OPERATION=null;
	/**
	 * 应用日志
	 */
	public static Integer LOG_APPLICATION=null;
	/**
	 * 报警日志
	 */
	public static Integer LOG_ALARM=null;
	/**
	 * 系统日志-从外网取文件
	 */
	public static Integer LOG_SYSTEM_GETFILES=null;
	/**
	 * 系统日志-报表数据入库
	 */
	public static Integer LOG_SYSTEM_SAVEDATA=null;
	/**
	 * 系统日志-表间校验
	 */
	public static Integer LOG_SYSTEM_CHECKOUTINSIDEREPORTS=null;
	/**
	 * 系统日志-生成数据仓库文件
	 */
	public static Integer LOG_SYSTEM_CREATESTORAGEXML=null;
	/************************************************************************/

	/************************************************************************
	 * 分隔符常量定义
	 ************************************************************************/
	public static String SPLIT_SYMBOL_COMMA=",";
	/*************************************************************************/
}
