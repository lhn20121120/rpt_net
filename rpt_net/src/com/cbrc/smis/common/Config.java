package com.cbrc.smis.common;

import java.util.HashMap;
import java.util.Map;

import com.cbrc.auth.hibernate.RemindTips;

/**
 * 系统公用的、常量的信息设置类文件
 * 
 * @author rds
 * @date 2005-11-22
 */
public class Config
{

    // 杭州联合银行
    public static boolean HZLHYH = false;

    /**
     * 是否同步EAST系统
     */
    public static boolean PORTAL = false;

    public static String PORTALDRIVER = "";

    public static String PORTALURL = "";

    public static String PORTALUSERNAME = "";

    public static String PORTALPWD = "";

    public static String PORATLSYNAIMPL = "";// 同步门户实现类

    public static boolean ISOLDHENJI = false;

    /**
     * 人行报文导出数据精度
     */
    public static String DOUBLEPERCISION = "";

    /**
     * 人行报文导出报文数据精度列表
     */
    public static Map DOUBLEPERCISION_TEMPLATELIST = new HashMap();

    /***
     * 是否忽略表间校验 强制上报
     */
    public static boolean ISFORCEREP = true;

    /***
     * 痕迹查询模板excel文件 按照此模板文件路径生成符合规则的excel文件
     */
    public static String TRACEFILEPATH = "";

    /***
     * 数据查询模板excel文件 按照此模板文件路径生成符合规则的excel文件
     */
    public static String XLSFILEPATH = "";

    /***
     * 是否添加数据痕迹的删除功能
     */
    public static boolean ISHAVEDELETE = false;

    /**
     * 在生成报文时，txt说明文件中显示的格式
     */
    public static String RH_DESC_CONTE = "outerId,orgName";

    /**
     * 在报文生成时，生成的文件编号设置最后一位的格式
     */
    public static String RH_FORMAT_END = "d";

    /***
     * 是否在数据重新生成时保留数据痕迹的调整
     */
    public static boolean ISADDTRACE = false;

    /***
     * 是否添加配置信息控件
     */
    public static boolean ISADDDESC = false;

    /**
     * 旧版人行汇总按钮是否存在0:不存在 1:存在
     */
    public static Integer ADD_OLD_COLLECT = 0;

    /**
     * 表达式的运算符
     */
    public static final String[] OPERATOR_SYMBOL = { "+", "-", "*", "/", "%", "(", ")", ">=", "<=", ">", "<" };

    /**
     * 系统的file.separator
     */
    public static final String FILESEPARATOR = System.getProperty("file.separator");

    /**
     * 系统错误页的Forward
     */
    public static final String FORWARD_SYS_ERROR_PAGE = "sysErrPage";

    /**
     * 系统WEB应用的物理路径
     */
    public static String WEBROOTPATH = "";

    /**
     * 系统WEB应用的URL路径
     */
    public static String WEBROOTULR = "";

    /**
     * web服务器端口号
     */
    public static Integer WEB_SERVER_PORT = null;

    /***
     * 是否添加分析系统
     */
    public static boolean ISADDFITOSA = false;

    /**
     * 分页对象的存放在Request中的名称
     */
    public static final String APART_PAGE_OBJECT = "ApartPage";

    /**
     * 当前页码存放在Request中的名称
     */
    public static final String CUR_PAGE_OBJECT = "CurPage";

    /**
     * 分页显示记录时，每页显示的记录数
     */
    public static final int PER_PAGE_ROWS = 10000;

    /**
     * 系统操作用户存放在SESSION中的名称
     */
    public static final String OPERATOR_SESSION_NAME = "Operator";

    /**
     * 返回查询记录集的名称
     */
    public static final String RECORDS = "Records";

    /**
     * 分页对象的存放在Request中的名称
     */
    public static final String BPART_PAGE_OBJECT = "BpartPage";

    /**
     * 返回查询记录集的名称
     */
    public static final String RECORDSJG = "RecordJGs";

    /**
     * 返回信息的名称
     */
    public static final String MESSAGES = "Message";

    /**
     * 上传文件的最大大小
     */
    public static final int FILE_MAX_SIZE = 1024 * 1024 * 4;

    /**
     * 公式类型:表内校验
     */
    public static final Integer CELL_CHECK_INNER = new Integer(1);

    /**
     * 公式类型:表间校验
     */
    public static final Integer CELL_CHECK_BETWEEN = new Integer(2);

    /**
     * 系统日志-跨频度校验
     */
    public static Integer LOG_SYSTEM_CHECKOUTKPDEREPORTS = null;

    /**
     * 公式类型:跨频度校验
     */
    public static final Integer CELL_CHECK_FREQ = new Integer(3);

    /**
     * 点对点式报表
     */
    public static final Integer REPORT_STYLE_DD = CELL_CHECK_INNER;

    /**
     * 清单式报表
     */
    public static final Integer REPORT_STYLE_QD = CELL_CHECK_BETWEEN;

    /***************************************************************************
     * 文件后缀常量定义
     **************************************************************************/
    /**
     * PDF报表模板文件后缀
     */
    public static final String EXT_PDF = "pdf";

    /**
     * excel报表模板文件后缀
     */
    public static final String EXT_EXCEL = "xls";

    /**
     * 润乾报表模板文件后缀
     */
    public static final String EXT_RAQ = "raq";

    /**
     * 统计分析报表模板文件后缀
     */
    public static final String analysis_REPORT = "cpt";

    /**
     * ZIP包文件后缀
     */
    public static final String EXT_ZIP = "zip";

    /**
     * PDF报表模板文件类型
     */
    public static final String FILE_CONTENT_TYPE_PDF = "application/pdf";

    /**
     * EXCEL报表模板文件类型
     */
    public static final String FILE_CONTENT_TYPE_EXCEL = "application/ms-excel";

    /**
     * Excel报表模板文件类型（jcm）
     */
    public static final String FILE_CONTENTTYPE_EXCEL = "application/vnd.ms-excel";

    /**
     * RAQ报表模板文件类型（jcm）
     */
    public static final String FILE_CONTENTTYPE_RAQ = "application/octet-stream";

    /**
     * ZIP包报表文件类型
     */
    public static final String FILE_CONTENTTYPE_ZIP = "application/x-zip-compressed";

    /**
     * TXT关系表达式定义文件后缀
     */
    public static final String EXT_TXT = "txt";

    /**
     * TXT关系表达式定义文件类型
     */
    public static final String FILE_CONTENT_TYPE_TXT = "text/plain";

    /**
     * XML文件名后缀
     */
    public static final String EXT_XML = "xml";

    /***************************************************************************
     * 日志类型常量定义
     **************************************************************************/
    /**
     * 操作日志
     */
    public static Integer LOG_OPERATION = null;

    /**
     * 应用日志
     */
    public static Integer LOG_APPLICATION = null;

    /**
     * 报警日志
     */
    public static Integer LOG_ALARM = null;

    /**
     * 系统日志-从外网取文件
     */
    public static Integer LOG_SYSTEM_GETFILES = null;

    /**
     * 系统日志-报表数据入库
     */
    public static Integer LOG_SYSTEM_SAVEDATA = null;

    /**
     * 系统日志-表间校验
     */
    public static Integer LOG_SYSTEM_CHECKOUTINSIDEREPORTS = null;

    /**
     * 系统日志-生成数据仓库文件
     */
    public static Integer LOG_SYSTEM_CREATESTORAGEXML = null;

    /**
     * 系统日志-模板重新发布
     */
    public static Integer LOG_SYSTEM_TEMPLATEPUT = null;

    /**
     * 系统操作
     */
    public static String SYSTEM_OPERATOR = "SYSTEM";

    /** ********************************************************************* */

    /***************************************************************************
     * 分隔符常量定义
     **************************************************************************/
    /**
     * 逗号分隔符
     */
    public static String SPLIT_SYMBOL_COMMA = ",";

    /**
     * 特殊分隔符
     */
    public static String SPLIT_SYMBOL_ESP = "&";

    /**
     * 等号分隔符
     */
    public static String SPLIT_SYMBOL_EQUAL = "=";

    /**
     * 左边大括号
     */
    public static String SPLIT_SYMBOL_LEFT_BIG_KUOHU = "{";

    /**
     * 右边大括号
     */
    public static String SPLIT_SYMBOL_RIGHT_BIG_HUOHU = "}";

    /**
     * 左边中括号
     */
    public static String SPLIT_SYMBOL_LEFT_MID_KUOHU = "[";

    /**
     * 右边中括号
     */
    public static String SPLIT_SYMBOL_RIGHT_MID_HUOHU = "]";

    /**
     * 右边中括号
     */
    public static String SPLIT_SYMBOL_RIGHT_SMALL_HUOHU = ")";

    /**
     * 下划线
     */
    public static String SPLIT_SYMBOL_OUTLINE = "_";

    /**
     * 管道符
     */
    public static String SPLIT_SYMBOL_PIPE = "|";

    /**
     * 单引号
     */
    public static String SPLIT_SYMBOL_SIGNLE_QUOTES = "'";

    /**
     * 特殊分割符合
     */
    public static String SPLIT_SYMBOL_SPECIAL = "##";

    /**
     * if符
     */
    public static String SPLIT_SYMBOL_IF = "if";

    /**
     * if当前月份
     */
    public static String SPLIT_SYMBOL_CURRMONTH = "@month";

    /**
     * 科目平衡校验符
     */
    public static String SPLIT_SYMBOL_ACCOUNTING = "acct";

    /**
     * 点分隔符
     */
    public static String SPLIT_SYMBOL_DIAN = ".";

    /** ********************************************************************** */
    /**
     * 模板再次发布标志
     */
    public static HashMap TEMPLATE_PUT = new HashMap();

    /**
     * 定义错误信息
     */
    public static final String GETORGERROR = "拿机构记录是失败";

    /**
     * 已经设定
     */
    public static final Integer SET_FLAG = Integer.valueOf("1");

    /**
     * 未设定
     */
    public static final Integer UNSET_FLAG = Integer.valueOf("0");

    /**
     * 报表未发布标志
     */
    public static final Integer NOT_PUBLIC = Integer.valueOf("0");

    /**
     * 报表已发布标志
     */
    public static final Integer IS_PUBLIC = Integer.valueOf("1");

    /**
     * 审核标志
     */
    public static final Short AUDITING_FLAG = Short.valueOf("1");

    /**
     * 错报标志
     */
    public static final Integer REP_RANGE_FLAG = Integer.valueOf("1");

    /**
     * 漏报标志
     */
    public static final Integer NOT_REPORT_FLAG = Integer.valueOf("1");

    /**
     * 审核通过标志
     */
    public static final Integer CHECK_FLAG_OK = Integer.valueOf("1");

    /**
     * 审核未通过标志
     */
    public static final Integer CHECK_FLAG_NO = Integer.valueOf("-1");

    /**
     * 未审核通过标志
     */
    public static final Integer CHECK_FLAG_UN = Integer.valueOf("0");

    /**
     * 异常标志
     */
    public static final Integer ABMORMITY_FLAG = Integer.valueOf("1");

    /**
     * 异常变化正常标志
     */
    public static final Integer ABMORMITY_FLAG_OK = Integer.valueOf("1");

    /**
     * 异常变化异常标志
     */
    public static final Integer ABMORMITY_FLAG_NO = Integer.valueOf("-1");

    /**
     * 表内校验标志
     */
    public static final Integer TBL_INNER_VALIDATE_FLAG = Integer.valueOf("1");

    /**
     * 表间校验标志
     */
    public static final Integer TBL_OUTER_VALIDATE_FLAG = Integer.valueOf("1");

    /**
     * 表内校验审核不通过
     */
    public static final Integer TBL_INNER_VALIDATE_NO_FLAG = Integer.valueOf("-1");

    public static final Integer PUBLICED = Integer.valueOf("1");

    /**
     * 报表未发布标志
     */
    public static final Integer UNPUBLICED = new Integer(0);

    /**
     * 表内校验过报送关系
     */
    public static Integer SYS_BN_VALIDATE = new Integer(0);

    /**
     * 表间校验过报送关系
     */
    public static Integer SYS_BJ_VALIDATE = new Integer(0);

    /**
     * 报送时点击校验按钮 是否进行表内校验
     */
    public static Integer UP_VALIDATE_BN = new Integer(0);

    /**
     * 报送时点击校验按钮 是否进行表间校验
     */
    public static Integer UP_VALIDATE_BJ = new Integer(0);

    /**
     * 上报数据时的批量校验是否同时进行表间校验
     */
    public static Integer UP_BATCH_VALIDATE = new Integer(0);

    /**
     * 汇总是否需要通过审核0:不需要 1:需要
     */
    public static Integer IS_NEED_CHECK = new Integer(0);

    /**
     * 是否对导出文件进行加密
     */
    public static Integer ENCRYPT = new Integer(0);

    /**
     * excel 密码
     */
    public static String EXCEL_PASSWORD = "FitDRS/N";

    // 表内校验不通过，不能上报该报表！";
    public static String BN_VALIDATE_NOTPASS = "表内校验不通过，不能上报该报表！";

    // 表间校验不通过，不能上报该报表
    public static String BJ_VALIDATE_NOTPASS = "表间校验不通过，不能上报该报表!";

    /**************************************
     * *************生成XML文件***********
     ****************************************/
    /**
     * 生成xml文件存放路径
     */
    public static String XMLData_PATH = "";

    /**
     * 生成xml文件成功
     */
    public static int DataToXML_SUCCESS = 1;

    /**
     * 生成xml文件失败
     */
    public static int DataToXML_FAILED = -1;

    /**
     * 没有需要生成xml文件的记录
     */
    public static int NO_DataToXML = 0;

    /**
     * 已报数据仓库标识
     */
    public static int Reported_Data_Warehouse = 1;

    /**
     * 已报数据仓库标识
     */
    public static int Not_Report_Data_Warehouse = 0;

    /**
     * 强制重报标志 重报
     */
    public static Short FORSE_REPORT_AGAIN_FLAG_1 = Short.valueOf("1");

    /**
     * 强制重报标志 未重报
     */
    public static Short FORSE_REPORT_AGAIN_FLAG_0 = Short.valueOf("0");

    /**
     * 善未审核的文本
     */
    public static String CIRCLE_FLAG = "审核不通过";

    /**
     * 通过审核的文本
     */
    public static String HOCK_FLAG = "通过审核";

    /**
     * 未通过审核的文本
     */
    public static String FORK_FLAG = "未过审核";

    /**
     * 信息发布文件存放的目录
     */

    public static String INFO_FILES_PATH = "";

    /**
     * PDF报表模板文件存放的物理路径
     */
    public static String PDF_TEMPLATE_PATH = "";

    /**
     * RAQ报表模板文件存放的物理路径
     */
    public static String RAQ_TEMPLATE_PATH = "";

    /**
     * 初始化模板路径(该路径下存放没有做过任何修改的原始模板)
     */
    public static String RAQ_INIT_TEMP_PATH = "";

    /**
     * PDF报表模板文件URL位置
     */
    public static String PDF_TEMPLATE_URL = "";

    /** ********************************************************************* */

    /** ********************************************************************* */

    /***************************************************************************
     * 后台报表数据入库的一些常量定义
     **************************************************************************/
    /**
     * 临时事件存放的目录
     */
    public static String TEMP_DIR = "";

    /**
     * 临时文件web存放路径
     */
    public static String TEMP_DIR_WEB_PATH = "";

    /**
     * 存放ZIP文件的路径
     */

    public static String ADDRESSZIP = "";

    /**
     * 针对每一各种不同的清单式报表对应不同实现类的属性配置文件的路径
     */
    public static String CONFIGBYIMPLPATH = "";

    /**
     * 输入数据间隔时间的属性文件配置文件的地址
     */
    public static String INPUTDATATIMEPREPADDR = "";

    /**
     * 信息发布文件存放的目录
     */
    public static String INFO_FILES_OUTPATH = WEBROOTPATH + "file" + FILESEPARATOR + "out";

    /**
     * 信息上传文件存放的目录
     */
    public static String INFO_FILES_UPPATH = WEBROOTPATH + "file" + FILESEPARATOR + "up";

    /**
     * 信息发布文件备份的目录
     */
    public static String BAK_INFO_FILES_OUTPATH = "";

    /**
     * 信息上传文件备份的目录
     */
    public static String BAK_INFO_FILES_UPPATH = "";

    /**
     * 用户登录后,保存到Session中的名称
     */
    public static final String OPERATOR_SESSION_ATTRIBUTE_NAME = "Operator";

    /**
     * 用户登录后,记录用户名称,用于日志处理
     */
    public static String LOG_OPERATOR__NAME = "";

    /**
     * 用户选择报表类型
     */
    public static final String REPORT_SESSION_FLG = "Reportflg";

    /**
     * 文件类型：上传文件
     */
    public static final String INFO_FILES_STYLE_UP = "A";

    /**
     * 文件类型：发布文件
     */
    public static final String INFO_FILES_STYLE_OUT = "B";

    /**
     * CA服务的IP地址
     */
    public static String CAIP = "";

    /**
     * CA服务的端口号
     */
    public static int CAPORT = 0;

    /**
     * 报表的上报时间
     */
    public static String SUBMITDATE = "submitDate";

    /**
     * 生成数据文件的方式
     */
    public static String DATATYPE = "";

    /**
     * 模板存放路径
     */
    public static String DATA = "data";

    /**
     * DATA下PDF模板存放路径
     */
    public static String DATA_PDF = "pdf";

    /**
     * 生成上报文件的类型（excel）
     */
    public static String EXCEL = "excel";

    /**
     * 生成上报文件的类型(xml)
     */
    public static String XML = "xml";

    // -----------------ETL映射关系常量Start---------------------

    /** 参数表维护表中事实表值 **/
    public static final String FACTTABLE = "1";

    /** 参数表维护表中维度表值 **/
    public static final String WEIDUTABLE = "2";

    /** 参数表维护表中指标表值 **/
    public static final String TARGETTABLE = "3";

    /** 维度表字段类型（字符型） **/
    public static final Integer WDCOLUMNTYPECHAR = new Integer(1);

    /** 维度表字段类型（数字型） **/
    public static final Integer WDCOLUMNTYPENUMBER = new Integer(0);

    /** 关联方式--业务系统生成 **/
    public static final String RELATIIONYWXTSC = "1";

    /** 关联方式--手工维护 **/
    public static final String RELATIONSGWH = "2";

    /** 关联方式--计算项 **/
    public static final String RELATONJSX = "3";

    // -----------------ETL映射关系常量End---------------------

    // -----------------机构权限常量Start---------------------

    /** 用户组权限分配--审签权限 **/
    public static final Integer POWERTYPECHECK = new Integer(1);

    /** 用户组权限分配--查看权限 **/
    public static final Integer POWERTYPESEARCH = new Integer(2);

    /** 用户组权限分配--报送权限 **/
    public static final Integer POWERTYPEREPORT = new Integer(3);

    /** 用户组权限分配--复核权限 **/
    public static final Integer POWERTYPEVERIFY = new Integer(4);

    // -----------------机构权限常量End---------------------

    // web服务器类型 1 表示需weblogic websphere 0 表示(tomcate)
    public static int WEB_SERVER_TYPE = 0;

    // ----------Excel文件解析－－－－－－－－－－－－

    /* 数值类型单元格背景色 */
    public static Integer NUMBER_CELL_BGCOLOR = new Integer(43);

    /* 公式单元格背景色－数值型 */
    public static Integer FORMUAL_CELL_BGCOLOR = new Integer(44);

    /* 字符类型单元格背景色 */
    public static Integer STRING_CELL_BGCOLOR = new Integer(42);

    /* 数值类型 */
    public static Integer NUMBER_CELL_TYPE = new Integer(2);

    /* 字符类型 */
    public static Integer STRING_CELL_TYPE = new Integer(3);

    /* 公式类型 */
    public static Integer FORMUAL_CELL_TYPE = new Integer(4);

    // -------------------单元格值表(M_CELL) 汇总类型常量值--------------------

    /** 不列入汇总类型 */
    public static final Integer COLLECT_TYPE_NO_COLLECT = new Integer("0");

    /** 数值相加汇总类型 */
    public static final Integer COLLECT_TYPE_SUM = new Integer("1");

    /** 平均值汇总类型 */
    public static final Integer COLLECT_TYPE_AVG = new Integer("2");

    /** 最大值汇总类型 */
    public static final Integer COLLECT_TYPE_MAX = new Integer("3");

    /** 最小值汇总类型 */
    public static final Integer COLLECT_TYPE_MIN = new Integer("4");

    /** 数据库类型 */
    public static String DB_SERVER_TYPE = "";

    /** 银行名称 */
    public static String BANK_NAME = "";

    /** 下拉列表默认值（全部） */
    public static final String DEFAULT_VALUE = "-999";

    /** 温馨贴士提醒 */
    public static RemindTips REMINDTIPS = null;

    /** 项目名称单元格背景色 */
    public static int PITEM_CELL_BGCOLOR = -256;

    /** 公式单元格背景色－数值型 */
    public static int PID_CELL_BGCOLOR = -65536;

    /** 项目列单元格背景色 */
    public static int PCOL_CELL_BGCOLOR = -16776961;

    /** 项目列单元格背景色 */
    public static int FORMULA_BGCOLOR = -6697729;

    /** 报表状态 */
    /** 审签通过 */
    public static final Short CHECK_FLAG_PASS = new Short(new String("1")); // 审签通过

    /** 审签不通过 */
    public static final Short CHECK_FLAG_FAILED = new Short(new String("-1")); // 审签不通过

    /** 未复核 */
    public static final Short CHECK_FLAG_UNCHECK = new Short(new String("0")); // 未复核

    /** 已校验 */
    public static final Short CHECK_FLAG_AFTERJY = new Short(new String("2")); // 已校验

    /** 未报送 */
    public static final Short CHECK_FLAG_UNREPORT = new Short(new String("3")); // 未报送

    /** 已填报 */
    public static final Short CHECK_FLAG_AFTERSAVE = new Short(new String("4")); // 已填报

    /** 已复核 */
    // public static final Short CHECK_FLAG_AFTERRECHECK = new Short(new
    // String("5")); //未审签
    /** 复核不通过 */
    // public static final Short CHECK_FLAG_RECHECKFAILED = new Short(new
    // String("-5")); //复核未通过
    /** 已生成 */
    public static final Short CHECK_FLAG_PRODUCT = new Short(new String("-2")); // 已生成

    /**
     * 全部记录
     */
    public static Integer FLAG_ALL = new Integer("9999");

    /**
     * 查询条件字串
     */
    public static final String QUERY_TERM = "QueryTerm";

    /**
     * 公告附件
     */
    public static Integer FILE_TYPE_PLACARD_FILE = new Integer(5);

    /**
     * 任务类型时间段对照
     */
    public static Map TAKEDATA_TIME_INTERVAL = null;

    public static String RHTEMPLATE_TYPE = "141";

    public static String QTTEMPLATE_TYPE = "142";

    /**
     * 数据属性
     */
    public static String DATATYPE_TYPE = "172";

    /**
     * 数值类型
     */
    public static String PSUZITYPE_TYPE = "173";

    /**
     * 单位编码
     */
    public static String DANWEIID_TYPE = "174";

    /**
     * 币种
     */
    public static String CURID_TYPE = "175";

    /**
     * 登录日期
     */
    public static String USER_LOGIN_DATE = "userLoginDate";

    /**
     * raq使用路径
     */
    public static String SHARE_DATA_PATH = RAQ_TEMPLATE_PATH;

    /**
     * 表间状态位
     */
    public static String BJ_VALIDATE = "BJ_VALIDATE";

    /**
     * 分析系统路径
     */
    public static String FITOSA_URL = "";

    public static String DATASOURCE_NAME = "rsmis";

    /***
     * 
     * 获得数据字典（总行人行编码及总行所在地区） 旧：数据字典类型编号183，共11位， 1-4位人行编码，5-11所在地区码
     * 例：54011310001 新：数据字典类型编号185，共13位， 1位一级码，2位二级码，3-6位3级码（人行编码），7-13所在地区码
     * 例：C154011310001
     */
    public static String CODE_LIB = "";

    /**
     * 汇总类型-轧差汇总
     */
    public static final int HZLX_GCHZ = 1;

    /**
     * 汇总公式定义中的同级行
     */
    public static final String HZGS_TJH = "HZTJH";

    /**
     * 汇总公式定义中的自定义机构
     */
    public static final String HZGS_ZDY = "CUSTOM_ORG";

    /**
     * 是否忽略校验状态 1:忽略 0:不忽略
     */
    public static int IS_IGNORE_FLAG = 0;

    /**
     * 0:简单工具访问模式 1:任务化模式
     */
    public static int SYSTEM_SCHEMA_FLAG = 0;

    /**
     * 0:忽略etl任务跑批过滤 1:任务跑批过滤
     */
    public static int ETL_TASK_CHECK = 0;

    /**
     * 0:角色和用户组分离模式 1:角色和用户组合并模式
     */
    public static int ROLE_GROUP_UNION_FLAG = 0;

    /**
     * 模板维护模式 0：工具模式，该模式下所有模板维护功能正常存在 1：任务模式下，该模式模板维护下银监新增和修改时的报送范围被隐藏，
     * 人行的新增时汇总设定和公式设定被隐藏，修改时则汇总设定被隐藏
     */
    public static int TEMPLATE_MANAGE_FLAG = 0;

    /**
     * 初始化sequence配置
     */
    public static String SEQUENSE_CONFIG = "";

    /**
     * 银监导出表状态
     */
    public static int YJ_EXP_REP_FLAG = 1;

    /**
     * 人行导出表状态
     */
    public static int RH_EXP_REP_FLAG = 1;

    /**
     * 报送时间单位(day:日 hour:小时)
     */
    public static String REPORT_TIME_UNIT = "day";

    /**
     * 上报时间单位(日)
     */
    public static final String REPORT_TIME_UNIT_DAY = "day";

    /**
     * 上报时间单位(小时)
     */
    public static final String REPORT_TIME_UNIT_HOUR = "hour";

    /**
     * 银监分支是否作为单独条线生效的标志(1为生效，0为不生效)
     */
    public static int YJ_BRANCH_BUSI_LINE = 0;

    /**
     * 银监法人分支报表的过滤设定规则
     */
    public static String YJ_BRANCH_BUSI_LINE_RULE = "";

    /**
     * 银监法人分支报表的前台显示标志位：G为法人,F为分支
     */
    public static String FLAG_1104 = "G";

    /**
     * 汇总公式列表选项
     */
    public static Map HZGS_LIST;

    public static int NODE_ID = -1;

    //public static int BATCHADDSIZE;

    static
    {
        HZGS_LIST = new HashMap();
        HZGS_LIST.put(HZGS_TJH, "同级行");
        HZGS_LIST.put(HZGS_ZDY, "自定义");
    }
}
