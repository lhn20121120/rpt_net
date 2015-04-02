/*
 * Created on 2005-12-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.common;

/**
 * @author cb
 * 
 * 
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConfigOncb {
	
	
	/**
	 * 整个操作是否成功的标志,用该标志来判断整个存储过程是否全部结束
	 */
	public static boolean FLOGSTOREPROCESS = false;
	
	/**
	 * 每页显示的记录数
	 */
	public static int recordCountByPage = 20; 

    /**
     * 实现类的配置文件的路径
     */
    public static String CONFIGBYIMPLPATH = Config.CONFIGBYIMPLPATH;
    
    
    /**
     * 输入数据间隔时间的配置文件的地址
     */
    public static String INPUTDATAPREPADDR = Config.INPUTDATATIMEPREPADDR;

    /***************************************************************************
     * 日志消息
     **************************************************************************/
    /**
     * 为系统定义一个操作人的名字
     */
    public static final String HANDLER = "SYSTEM";

    /**
     * 数据入库的日志类型常量类型
     */
    public static final Integer LOGSYSTEMSAVETYPE = new Integer(25);

    /**
     * 当数据录入成功时向数据库日志表中输入的消息
     */
    public static final String INPUTDATASUCCESS = "录入数据结束";

    /**
     * 当数据录入失败时向数据库日志表中输入的消息
     */
    public static final String INPUTDATAWRONG = "录入数据失败";

    /**
     * 当实际表单表REPORT_IN记录入库失败时记入日志的信息
     */
    public static final String SAVEREPORTIN = "实际表单表REPORT_IN记录入库失败";

    /**
     * 当内网基本数据表REPORT_IN_INFO记录入库失败时记入日志的信息
     */
    public static final String SAVEREPORTININFO = "内网基本数据表REPORT_IN_INFO记录入库失败";

    /**
     * 当单元格入库失败时记入日志的信息
     */
    public static final String SAVEMCELL = "单元格入库失败";
    
    /**
     * 当应用服务器启动时需要读取的定时器初始化的参数
     */
    public static final String WRITERCOUNTONSTARTUP = "定时启动初始化时失败";
    

    /***************************************************************************
     * XML解析所用到的常量定义
     **************************************************************************/
    /**
     * 存放XML文件的临时路径
     */
    public static final String TEMP_DIR = Config.TEMP_DIR;

    /**
     * 存放ZIP文件的路径
     */
    public static final String ADDRESSZIP = Config.ADDRESSZIP;

    /**
     * 
     * 定义错误信息
     */
    public static final String GETORGERROR = "拿机构记录时失败";

    /**
     * 解压解析错误信息
     */
    public static final String RELEASEANDPARSEBAD = "解压解析文件失败";

    /**
     * 用于本人调试信息时的输出信息
     */
    public static final String MYERRMESSAGE = "下面是输出的错误信息";

    /***************************************************************************
     * XML元素名常量定义
     **************************************************************************/

    /**
     * 说明XML文件中的子报表元素名
     */
    public static final String REPORT = "report";

    /**
     * 报表名
     */
    public static final String REPORTFILENAME = "reportFileName";

    /**
     * 机构ID常量
     */
    public static final String ORGID = "orgId";

    /**
     * 子报表ID常量
     */
    public static final String REPORTID = "reportId";

    /**
     * 版本号常量
     */
    public static final String VERSION = "version";

    /**
     * 上报年份
     */
    public static final String YEAR = "year";

    /**
     * 上报月份
     */
    public static final String MONTH = "month";

    /**
     * 上报频度
     */
    public static final String FREQUENCYID = "frequencyId";

    /**
     * 第几期
     */
    public static final String TERM = "term";

    /**
     * 数据范围ID常量标示符
     */
    public static final String DATARANGEID = "dataRangeId";

    /**
     * 币种
     */
    public static final String FITECHCURR = "fitechCurr";

    /**
     * 人民币
     */
    public static final String COMMONCURRNAME = "人民币";

    /**
     * 作者
     */
    public static final String WRITER = "writer";
    /**
     * 审核人
     */
    public static final String CHECKER="checker";
    /**
     * 负责人
     */
    public static final String PRINCIPAL="principal";
    /**
     * 报送次数
     */
    public static final String TIMES = "times";
    
    /**
     * 外网数据记录ID 用于对外网进行同步更新
     */
    public static final String REPOUTID = "realId"; //"repOutId";

    /**
     * 报表头元素名,一般靠它得到一些关于报表的附加信息,例如报表名称
     */
    public static final String DETAILHEADER = "detailHeader";

    /**
     * 实际报表名称
     */
    public static final String FITECHTITLE = "fitechTitle";

    /**
     * 上报日期
     */
    public static final String FITECHDATE = "fitechDate";
    /**
     * 上报年份
     */
    public static final String FITECHSUBMITYEAR="fitechSubmitYear";
    /**
     * 上报月份
     */
    public static final String FITECHSUBMITMONTH="fitechSubmitMonth";
    /**
     * 币种元素名
     */
    public static final String FITECHMCURR = "fitechMcurr";

    /**
     * 点对点元素的元素名F
     */
    public static final String UPPERELEMENT = "F";

    /**
     * 点对点的元素名P1
     */
    public static final String SECONDUPPER = "P1";
    
    
    /**
     * G5312点对点报表的根元素名
     */
    public static final String G5301 = "G5301";
    /**
     * G5100报表名
     */
    public static final String G5100="G5100";
    
    /**
     * 清单式的XML文件中的detail元素名
     */
    public static final String DETAIL = "detail";

    /**
     * 清单式的XML文件中的title
     */
    public static final String TOTAL = "total";

    /**
     * 清单式的主键
     */
    public static final String COL1 = "COL1";

    /**
     * 当清单式报表的元素是title时COL1主键的标识符
     */
    public static final String LABELBYLIST = "";

    /**
     * 说明XML文件的文件名
     */
    public static final String EXPLAINFILENAME = "listing.xml";

    /***************************************************************************
     * 四种清单式报表的常量定义
     **************************************************************************/

    /**
     * 第一种清单式的报表
     */
    public static final String TYPE1 = "type1"; //s32_1,s32_2,s32_3,s32_4,s32_5,s32_6,s32_7,s32_9,s33,s35

    /**
     * 第二种清单式的报表
     */
    public static final String TYPE2 = "type2"; //s36,s37,s34_2

    /**
     * 第三种清单式的报表
     */
    public static final String TYPE3 = "type3"; //s34_1,G51

    /**
     * 第四种清单式的报表
     */
    public static final String TYPE4 = "type4"; //s38

    /**
     * 控制反转
     */
    public static final String TEST = "type2";

    /***************************************************************************
     * 常用符号定义
     **************************************************************************/

    /**
     * 指的是符号 sss"-"
     */
    public static final String COMPART = "-";
    
    /**
	 * 合计单元格显示的内容
	 */
	public static String TOTALLABEL="合计";
	
	/**
	 * 填报人
	 */
	public static String FITECHFILLER = "fitechFiller";
	
	/**
	 * 复核人
	 */
	public static String FITECHCHECKER = "fitechChecker";
	
	/**
	 * 负责人
	 */
	public static String FITECHPRINCIPAL = "fitechPrincipal";
	/**
	 * 报送口径
	 */
	public static String FITECHRANGE = "fitechRange";
	/**
	 * 子表名
	 */
	public static String FITECHSUBTITLE = "fitechSubtitle";
}