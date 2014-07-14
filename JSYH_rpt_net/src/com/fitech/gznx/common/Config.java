package com.fitech.gznx.common;


public class Config {
	
	/**
	 * 超级用户
	 */
	public static String SUPERUSER = "admin";
	/**
	 * 总行ID
	 */
	public static String HEAD_ORG_ID = "";
	/**
	 * 总行代码
	 */
	public static String TOPBANK = "0";
	/**
	 * 虚拟父机机构
	 */
	public static String VIRTUAL_TOPBANK = "-99";
	/**
	 * 银行名称
	 */
	public static String BANK_NAME = "江苏银行";
	
	public static final String RECORDS = null;
	/*项目名称单元格背景色*/
	public static int PITEM_CELL_BGCOLOR=43;
	
	/*公式单元格背景色－数值型*/
	public static int PID_CELL_BGCOLOR=44;
	
	/*项目列单元格背景色*/
	public static int PCOL_CELL_BGCOLOR=42;
	
	/**
	 * 返回信息的名称
	 */
	public static final String MESSAGES = "Message";
	
	/**
	 * 不是汇总机构
	 */
	public static String NOT_IS_COLLECT = "0";
	
	/**
	 * 系统的路径分割符
	 */
	public static final String FILESEPARATOR = System
			.getProperty("file.separator");

	
	/**
	 * 单次频度
	 */
	public static final Integer FREQ_TIME = new Integer("7");
	
	/**
	 * 逗号分隔符
	 */
	public static final String SPLIT_SYMBOL_COMMA = ",";


	/**
	 * 是汇总机构
	 */
	public static String IS_COLLECT = "1";

	/**
	 * 汇总机构父机构ID(作为一级节点的虚拟机构的父机构ID)
	 * 人行虚拟机构的org_type
	 */
	public static String COLLECT_ORG_PARENT_ID = "-99";
	
	/**
	 * 汇总机构父机构ID(作为一级节点的虚拟机构的父机构ID)
	 * 其他虚拟机构的org_type
	 */
	public static String COLLECT_ORG_PARENT_QT_ID = "-98";
	
	/**
	 * 基础递归机构树文件名字符串
	 */
	public static String BASE_ORG_TREEXML_STR = "baseOrgTreeIterator.xml";
	
	/***
	 * 生成关系树
	 */
	public static String V_ORG_REL_XML = "vOrgRelTree.xml";
	
	public static String V_ORG_REL_STR = "vOrgRelTree.xml";

	/**
	 * 基础递归机构树的文件名
	 */
	public static String BASE_ORG_TREEXML_NAME = "baseOrgTreeIterator.xml";

	/**
	 * 基础递归机构树的文件的WEB访问路径
	 */
	public static String BASE_ORG_TREEXML_WEB_PATH = Config.ORG_TREEXML_WEB_PATH
			+ Config.BASE_ORG_TREEXML_NAME;
	
	
	/**
	 * 机构生成树Web的存放位置
	 */
	public static String ORG_TREEXML_WEB_PATH = "";
	
	/**
	 * 用户登录后,保存到Session中的名称
	 */
	public static final String OPERATOR_SESSION_NAME = "Operator";

	/**
	 * 机构树中的虚拟机构属性名称
	 */
	public static String XML_ATTRIBUTE_VIRTUAL = "virtual";
;

	
	/**
	 * 日频度
	 */
	public static final Integer FREQ_DAY = new Integer("6");
	/**
	 * 周频度
	 */
	public static final Integer FREQ_WEEK = new Integer("7");
	/**
	 * 快报频度
	 */
	public static final Integer FREQ_MONTH_EXPRESS = new Integer("10");


	/**
	 * 旬频度
	 */
	public static final Integer FREQ_TENDAY = new Integer("8");

	/**
	 * 月频度
	 */
	public static final Integer FREQ_MONTH = new Integer("1");

	/**
	 * 季频度
	 */
	public static final Integer FREQ_SEASON = new Integer("2");

	/**
	 * 半年频度
	 */
	public static final Integer FREQ_HALFYEAR = new Integer("3");

	/**
	 * 年频度
	 */
	public static final Integer FREQ_YEAR = new Integer("4");
	/**
	 * 年初结转频度
	 */
	public static final Integer FREQ_YEARBEGAIN = new Integer("9");

	/***************************************************************************
	 * 报表类型（用于页面切换） *
	 **************************************************************************/
	/**
	 * 银监会报表
	 */
	public static String CBRC_REPORT = "1";
	public static String CBRC_REPORT_NAME = "银监会报表";

	/**
	 * 人行报表
	 */
	public static String PBOC_REPORT = "2";
	public static String PBOC_REPORT_NAME = "人行报表";
	/**
	 * 其他报表
	 */
	public static String OTHER_REPORT = "3";
	public static String OTHER_REPORT_NAME = "其他报表";
	/**
	 * 临时报表
	 */
	public static String TEMP_REPORT = "4";
	public static String TEMP_REPORT_NAME = "其他报表";
	/**
	 * 空白模板excel保存路径
	 */
	public static String TEMPLATE_PATH = com.cbrc.smis.common.Config.RAQ_TEMPLATE_PATH
			+ com.cbrc.smis.common.Config.FILESEPARATOR + "templateFiles" 
			+ com.cbrc.smis.common.Config.FILESEPARATOR + "excel"
			+ com.cbrc.smis.common.Config.FILESEPARATOR;

	/**
	 * 生成excel保存路径
	 */
	public static String REPORT_PATH = com.cbrc.smis.common.Config.RAQ_TEMPLATE_PATH
			+ com.cbrc.smis.common.Config.FILESEPARATOR + "reportFiles"
			+ com.cbrc.smis.common.Config.FILESEPARATOR;

	
	/***************************************************************************
	 * 报表报送类型（IS_REPORT）                                                  *
	 **************************************************************************/
	/**
	 * 分析类报表
	 */
	public static String TEMPLATE_ANALYSIS = "1";
	public static String TEMPLATE_ANALYSIS_NAME = "分析类报表";
	
	/**
	 * 报送类报表
	 */
	public static String TEMPLATE_REPORT = "2";
	public static String TEMPLATE_REPORT_NAME = "报送类报表";

	/**
	 * 查询类报表
	 */
	public static String TEMPLATE_VIEW = "3";
	public static String TEMPLATE_VIEW_NAME = "查询类报表";
	
	
	/***************************************************************************
	 * 报表报送类型（REPORT_STYLE）                                               *
	 **************************************************************************/
	/**
	 * 点对点报表
	 */
	public static String REPORT_DD = "1";
	
	/**
	 * 清单报表
	 */
	public static String REPORT_QD = "2";
	/**
	 * 清单式报表生成标志
	 */
	public static String PROFLG_SENCEN_QD = "1";
	
	/**
	 * 清单式报表查询标志
	 */
	public static String PROFLG_CAXUN_QD = "2";
	
	/**
	 * 清单式报表最大行数
	 */
	public static int MAX_ROW = 10000;
	
	/**
	 * 清单式报表查询标志
	 */
//	public static String CUSTOM_SEARCH = "200";
	
	/**
	 * 人行外报机构编码数据字典号
	 */
	public static String RHORGIDTYPE = "183";
	/**
	 * 人行外报机构编码数据字典类型号
	 */
	public static String RHORGIDID = "1";
	
	
	/**
	 * 标准化人行外报机构编码数据字典号
	 */
	public static String RHFORMATORGIDTYPE = "185";
}
