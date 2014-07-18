package com.cbrc.smis.common;

import java.util.HashMap;
import java.util.Map;

import com.cbrc.auth.hibernate.RemindTips;

/**
 * ϵͳ���õġ���������Ϣ�������ļ�
 * 
 * @author rds
 * @date 2005-11-22
 */
public class Config {
	public static boolean ISOLDHENJI = false;
	/**
	 * ���б��ĵ�����ݾ���
	 */
	public static String DOUBLEPERCISION = "";
	/***
	 * �Ƿ���Ա��У�� ǿ���ϱ�
	 */
	public static boolean ISFORCEREP = false;
	/***
	 * �ۼ���ѯģ��excel�ļ�
	 * ���մ�ģ���ļ�·����ɷ�Ϲ����excel�ļ�
	 */
	public static String TRACEFILEPATH = "";
	/***
	 * ��ݲ�ѯģ��excel�ļ�
	 * ���մ�ģ���ļ�·����ɷ�Ϲ����excel�ļ�
	 */
	public static String XLSFILEPATH = "";
	/***
	 * �Ƿ������ݺۼ���ɾ����
	 */
	public static boolean ISHAVEDELETE = false;
	
	/**
	 * ����ɱ���ʱ��txt˵���ļ�����ʾ�ĸ�ʽ
	 */
	public static String RH_DESC_CONTE ="outerId,orgName";
	
	/**
	 * �ڱ������ʱ����ɵ��ļ�����������һλ�ĸ�ʽ
	 */
	public static String RH_FORMAT_END ="d";
	
	/***
	 * �Ƿ�������������ʱ������ݺۼ��ĵ���
	 */
	public static boolean ISADDTRACE = false;
	/***
	 * �Ƿ����������Ϣ�ؼ�
	 */
	public static  boolean ISADDDESC = false;
	/**
	 * �ɰ����л��ܰ�ť�Ƿ����0:������ 1:����
	 */
	public static Integer ADD_OLD_COLLECT  = 1;
	/**
	 * ���ʽ�������
	 */
	public static final String[] OPERATOR_SYMBOL = { "+", "-", "*", "/", "%",
			"(", ")", ">=", "<=", ">", "<" };
	/**
	 * ϵͳ��file.separator
	 */
	public static final String FILESEPARATOR = System.getProperty("file.separator");

	/**
	 * ϵͳ����ҳ��Forward
	 */
	public static final String FORWARD_SYS_ERROR_PAGE = "sysErrPage";

	/**
	 * ϵͳWEBӦ�õ�����·��
	 */
	public static String WEBROOTPATH = "";

	/**
	 * ϵͳWEBӦ�õ�URL·��
	 */
	public static String WEBROOTULR = "";
	
	/***
	 * �Ƿ���ӷ���ϵͳ
	 */
	public static boolean ISADDFITOSA = false;
	/**
	 * ��ҳ����Ĵ����Request�е����
	 */
	public static final String APART_PAGE_OBJECT = "ApartPage";

	/**
	 * ��ǰҳ������Request�е����
	 */
	public static final String CUR_PAGE_OBJECT = "CurPage";

	/**
	 * ��ҳ��ʾ��¼ʱ��ÿҳ��ʾ�ļ�¼��
	 */
	public static final int PER_PAGE_ROWS = 10;

	/**
	 * ϵͳ�����û������SESSION�е����
	 */
	public static final String OPERATOR_SESSION_NAME = "Operator";

	/**
	 * ���ز�ѯ��¼�������
	 */
	public static final String RECORDS = "Records";

	/**
	 * ������Ϣ�����
	 */
	public static final String MESSAGES = "Message";

	/**
	 * �ϴ��ļ�������С
	 */
	public static final int FILE_MAX_SIZE = 1024 * 1024 * 4;

	/**
	 * ��ʽ����:����У��
	 */
	public static final Integer CELL_CHECK_INNER = new Integer(1);

	/**
	 * ��ʽ����:���У��
	 */
	public static final Integer CELL_CHECK_BETWEEN = new Integer(2);

	/**
	 *ϵͳ��־-��Ƶ��У��
	 */
	public static Integer LOG_SYSTEM_CHECKOUTKPDEREPORTS = null;

	/**
	 * ��ʽ����:��Ƶ��У��
	 */
	public static final Integer CELL_CHECK_FREQ=new Integer(3);
	
	/**
	 * ��Ե�ʽ����
	 */
	public static final Integer REPORT_STYLE_DD = CELL_CHECK_INNER;

	/**
	 * �嵥ʽ����
	 */
	public static final Integer REPORT_STYLE_QD = CELL_CHECK_BETWEEN;

	/***************************************************************************
	 * �ļ���׺��������
	 **************************************************************************/
	/**
	 * PDF����ģ���ļ���׺
	 */
	public static final String EXT_PDF = "pdf";
	
	/**
	 * excel����ģ���ļ���׺
	 */
	public static final String EXT_EXCEL = "xls";
	
	/**
	 * ��Ǭ����ģ���ļ���׺
	 */
	public static final String EXT_RAQ = "raq";
	
	/**
	 * ͳ�Ʒ�������ģ���ļ���׺
	 */
	public static final String analysis_REPORT = "cpt";

	/**
	 * ZIP���ļ���׺
	 */
	public static final String EXT_ZIP = "zip";
	/**
	 * PDF����ģ���ļ�����
	 */
	public static final String FILE_CONTENT_TYPE_PDF = "application/pdf";
	/**
	 * EXCEL����ģ���ļ�����
	 */
	public static final String FILE_CONTENT_TYPE_EXCEL = "application/ms-excel";
	
	/**
	 * Excel����ģ���ļ����ͣ�jcm��
	 */
	public static final String FILE_CONTENTTYPE_EXCEL = "application/vnd.ms-excel";
	
	/**
	 * RAQ����ģ���ļ����ͣ�jcm��
	 */
	public static final String FILE_CONTENTTYPE_RAQ = "application/octet-stream";
	
	/**
	 * ZIP����ļ�����
	 */
	public static final String FILE_CONTENTTYPE_ZIP = "application/x-zip-compressed";
	
	/**
	 * TXT��ϵ���ʽ�����ļ���׺
	 */
	public static final String EXT_TXT = "txt";

	/**
	 * TXT��ϵ���ʽ�����ļ�����
	 */
	public static final String FILE_CONTENT_TYPE_TXT = "text/plain";

	/**
	 * XML�ļ����׺
	 */
	public static final String EXT_XML = "xml";

	/***************************************************************************
	 * ��־���ͳ�������
	 **************************************************************************/
	/**
	 * ������־
	 */
	public static Integer LOG_OPERATION = null;

	/**
	 * Ӧ����־
	 */
	public static Integer LOG_APPLICATION = null;

	/**
	 * ������־
	 */
	public static Integer LOG_ALARM = null;

	/**
	 * ϵͳ��־-������ȡ�ļ�
	 */
	public static Integer LOG_SYSTEM_GETFILES = null;

	/**
	 * ϵͳ��־-����������
	 */
	public static Integer LOG_SYSTEM_SAVEDATA = null;

	/**
	 * ϵͳ��־-���У��
	 */
	public static Integer LOG_SYSTEM_CHECKOUTINSIDEREPORTS = null;

	/**
	 * ϵͳ��־-�����ݲֿ��ļ�
	 */
	public static Integer LOG_SYSTEM_CREATESTORAGEXML = null;
	
	/**
	 * ϵͳ��־-ģ�����·���
	 */
	public static Integer LOG_SYSTEM_TEMPLATEPUT = null;
    /**
     * ϵͳ����
     */
    public static String SYSTEM_OPERATOR = "SYSTEM";
   

	/** ********************************************************************* */

	/***************************************************************************
	 * �ָ���������
	 **************************************************************************/
	/**
	 * ���ŷָ���
	 */
	public static String SPLIT_SYMBOL_COMMA = ",";

	/**
	 * ����ָ���
	 */
	public static String SPLIT_SYMBOL_ESP = "&";

	/**
	 * �Ⱥŷָ���
	 */
	public static String SPLIT_SYMBOL_EQUAL = "=";

	/**
	 * ��ߴ�����
	 */
	public static String SPLIT_SYMBOL_LEFT_BIG_KUOHU = "{";

	/**
	 * �ұߴ�����
	 */
	public static String SPLIT_SYMBOL_RIGHT_BIG_HUOHU = "}";

	/**
	 * ���������
	 */
	public static String SPLIT_SYMBOL_LEFT_MID_KUOHU = "[";

	/**
	 * �ұ�������
	 */
	public static String SPLIT_SYMBOL_RIGHT_MID_HUOHU = "]";
	
	/**
	 * �ұ�������
	 */
	public static String SPLIT_SYMBOL_RIGHT_SMALL_HUOHU = ")";
	
	

	/**
	 * �»���
	 */
	public static String SPLIT_SYMBOL_OUTLINE = "_";
	/**
	 * �ܵ���
	 */
	public static String SPLIT_SYMBOL_PIPE = "|";

	/**
	 * �����
	 */
	public static String SPLIT_SYMBOL_SIGNLE_QUOTES = "'";
	
	/**
	 * ����ָ���
	 */
	public static String SPLIT_SYMBOL_SPECIAL = "##";
	
	/**
	 * if��
	 */
	public static String SPLIT_SYMBOL_IF = "if";
	/**
	 * if��ǰ�·�
	 */
	public static String SPLIT_SYMBOL_CURRMONTH = "@month";
	/**
	 * ��Ŀƽ��У���
	 */
	public static String SPLIT_SYMBOL_ACCOUNTING = "acct";
	/**
	 * ��ָ���
	 */
	public static String SPLIT_SYMBOL_DIAN = ".";

	/** ********************************************************************** */
	/**
	 * ģ���ٴη�����־
	 */
	public static HashMap TEMPLATE_PUT=new HashMap();
	/**
	 * ���������Ϣ
	 */
	public static final String GETORGERROR = "�û��¼��ʧ��";

	/**
	 * �Ѿ��趨
	 */
	public static final Integer SET_FLAG = Integer.valueOf("1");

	/**
	 * δ�趨
	 */
	public static final Integer UNSET_FLAG = Integer.valueOf("0");

	/**
	 * ����δ������־
	 */
	public static final Integer NOT_PUBLIC = Integer.valueOf("0");

	/**
	 * �����ѷ�����־
	 */
	public static final Integer IS_PUBLIC = Integer.valueOf("1");

	/**
	 * ��˱�־
	 */
	public static final Short AUDITING_FLAG = Short.valueOf("1");

	/**
	 * �?��־
	 */
	public static final Integer REP_RANGE_FLAG = Integer.valueOf("1");

	/**
	 * ©����־
	 */
	public static final Integer NOT_REPORT_FLAG = Integer.valueOf("1");
	
	/**
	 * ���ͨ���־
	 */
	public static final Integer CHECK_FLAG_OK=Integer.valueOf("1");
	/**
	 * ���δͨ���־
	 */
	public static final Integer CHECK_FLAG_NO=Integer.valueOf("-1");
	/**
	 * δ���ͨ���־
	 */
	public static final Integer CHECK_FLAG_UN=Integer.valueOf("0");
	
	/**
	 * �쳣��־
	 */
	public static final Integer ABMORMITY_FLAG = Integer.valueOf("1");
	/**
	 * �쳣�仯���־
	 */
	public static final Integer ABMORMITY_FLAG_OK = Integer.valueOf("1");
	/**
	 * �쳣�仯�쳣��־
	 */
	public static final Integer ABMORMITY_FLAG_NO = Integer.valueOf("-1");
	
	/**
	 * ����У���־
	 */
	public static final Integer TBL_INNER_VALIDATE_FLAG = Integer.valueOf("1");

	/**
	 * ���У���־
	 */
	public static final Integer TBL_OUTER_VALIDATE_FLAG = Integer.valueOf("1");
	
	/**
	 * ����У����˲�ͨ��
	 */
	public static final Integer TBL_INNER_VALIDATE_NO_FLAG=Integer.valueOf("-1");

	public static final Integer PUBLICED = Integer.valueOf("1");

	/**
	 * ����δ������־
	 */
	public static final Integer UNPUBLICED = new Integer(0);
	
	/**
	 * ����У����͹�ϵ 
	 */
	public static Integer SYS_BN_VALIDATE= new Integer(0);
	/**
	 * ���У����͹�ϵ 
	 */
	public static Integer SYS_BJ_VALIDATE= new Integer(0);
	/**
	 * ����ʱ���У�鰴ť �Ƿ���б���У��
	 */
	public static Integer UP_VALIDATE_BN= new Integer(0);
	/**
	 * ����ʱ���У�鰴ť �Ƿ���б��У�� 
	 */
	public static Integer UP_VALIDATE_BJ= new Integer(0);
	
	/**
	 * �ϱ����ʱ������У���Ƿ�ͬʱ���б��У��
	 */
	public static Integer UP_BATCH_VALIDATE= new Integer(0);
	/**
	 * �����Ƿ���Ҫͨ�����0:����Ҫ 1:��Ҫ
	 */
	public static Integer IS_NEED_CHECK = new Integer(0);
	
	/**
	 * �Ƿ�Ե����ļ����м���
	 */
	public static Integer ENCRYPT= new Integer(0);
	/**
	 * excel ����
	 */
	public static String  EXCEL_PASSWORD="FitDRS/N";
	
	//����У�鲻ͨ�����ϱ��ñ��?";
	public static String BN_VALIDATE_NOTPASS = "����У�鲻ͨ�����ϱ��ñ��?";
	//���У�鲻ͨ�����ϱ��ñ���
	public static String BJ_VALIDATE_NOTPASS = "���У�鲻ͨ�����ϱ��ñ���!";
	
	
    /**************************************
     * *************���XML�ļ�***********     
     ****************************************/
	/**
	 * ���xml�ļ����·��
	 */
	public static String XMLData_PATH = "";
	/**
     *  ���xml�ļ��ɹ� 
	 */
    public static int DataToXML_SUCCESS = 1;
    /**
     * ���xml�ļ�ʧ��
     */
    public static int DataToXML_FAILED = -1;
    /**
     * û����Ҫ���xml�ļ��ļ�¼
     */
    public static int NO_DataToXML = 0;
    /**
     * �ѱ���ݲֿ��ʶ
     */
    public static int Reported_Data_Warehouse=1;
    /**
     * �ѱ���ݲֿ��ʶ
     */
    public static int Not_Report_Data_Warehouse=0;
    
    /**
	 * ǿ���ر���־ �ر�
	 */
	public static Short FORSE_REPORT_AGAIN_FLAG_1 = Short.valueOf("1");

	/**
	 * ǿ���ر���־ δ�ر�
	 */
	public static Short FORSE_REPORT_AGAIN_FLAG_0 = Short.valueOf("0");

	/**
	 * ��δ��˵��ı�
	 */
	public static String CIRCLE_FLAG = "��˲�ͨ��";

	/**
	 * ͨ����˵��ı�
	 */
	public static String HOCK_FLAG = "ͨ�����";

	/**
	 * δͨ����˵��ı�
	 */
	public static String FORK_FLAG = "δ�����";

	/**
	 * ��Ϣ�����ļ���ŵ�Ŀ¼
	 */

	public static String INFO_FILES_PATH = "";

	/**
	 * PDF����ģ���ļ���ŵ�����·��
	 */
	public static String PDF_TEMPLATE_PATH = "";
	
	/**
	 * RAQ����ģ���ļ���ŵ�����·��
	 */
	public static String RAQ_TEMPLATE_PATH = "";
	
	/**
	 * ��ʼ��ģ��·��(��·���´��û�������κ��޸ĵ�ԭʼģ��)
	 */
	public static String RAQ_INIT_TEMP_PATH = "";

	/**
	 * PDF����ģ���ļ�URLλ��
	 */
	public static String PDF_TEMPLATE_URL = "";

	/** ********************************************************************* */

	/** ********************************************************************* */

	/***************************************************************************
	 * ��̨�����������һЩ��������
	 **************************************************************************/
	/**
	 * ��ʱ�¼���ŵ�Ŀ¼
	 */
	public static String TEMP_DIR = "";
	/**
	 * ��ʱ�ļ�web���·��
	 */
	public static String TEMP_DIR_WEB_PATH="";

	/**
	 * ���ZIP�ļ���·��
	 */

	public static String ADDRESSZIP = "";

	/**
	 * ���ÿһ���ֲ�ͬ���嵥ʽ�����Ӧ��ͬʵ��������������ļ���·��
	 */
	public static String CONFIGBYIMPLPATH = "";

	/**
	 * ������ݼ��ʱ��������ļ������ļ��ĵ�ַ
	 */
	public static String INPUTDATATIMEPREPADDR = "";

	/**
	 * ��Ϣ�����ļ���ŵ�Ŀ¼
	 */
	public static String INFO_FILES_OUTPATH = WEBROOTPATH + "file"
			+ FILESEPARATOR + "out";

	/**
	 * ��Ϣ�ϴ��ļ���ŵ�Ŀ¼
	 */
	public static String INFO_FILES_UPPATH = WEBROOTPATH + "file"
			+ FILESEPARATOR + "up";
    /**
     * ��Ϣ�����ļ����ݵ�Ŀ¼
     */
    public static String BAK_INFO_FILES_OUTPATH = "";
    /**
     * ��Ϣ�ϴ��ļ����ݵ�Ŀ¼
     */
    public static String BAK_INFO_FILES_UPPATH = "";
    
	/**
	 * �û���¼��,���浽Session�е����
	 */
	public static final String OPERATOR_SESSION_ATTRIBUTE_NAME = "Operator";
	
	
	/**
	 * �û���¼��,��¼�û����,������־����
	 */
	public static  String LOG_OPERATOR__NAME = "";
	
	/**
	 * �û�ѡ�񱨱�����
	 */
	public static final String REPORT_SESSION_FLG = "Reportflg";
	
	/**
	 * �ļ����ͣ��ϴ��ļ�
	 */
	public static final String INFO_FILES_STYLE_UP = "A";

	/**
	 * �ļ����ͣ������ļ�
	 */
	public static final String INFO_FILES_STYLE_OUT = "B";

    /**
	 * CA�����IP��ַ
	 */
	public static String  CAIP = ""; 
	
	/**
	 * CA����Ķ˿ں�
	 */
	public static int CAPORT = 0;
	
	/**
	 * ������ϱ�ʱ��
	 */
	public static String SUBMITDATE ="submitDate";
	
	/**
	 * �������ļ��ķ�ʽ
	 */
	public static String DATATYPE = "";
	
	/**
	 * ģ����·��
	 */
	public static String DATA ="data";
	
	/**
	 * DATA��PDFģ����·��
	 */
	public static String DATA_PDF ="pdf";
	
	/**
	 * ����ϱ��ļ������ͣ�excel��
	 */
	public static String EXCEL = "excel";

	/**
	 * ����ϱ��ļ�������(xml)
	 */
	public static String XML = "xml";
	
	
	//	-----------------ETLӳ���ϵ����Start---------------------
	
	/**�����ά��������ʵ��ֵ**/
	public static final String FACTTABLE = "1";
	
	/**�����ά������ά�ȱ�ֵ**/
	public static final String WEIDUTABLE = "2";
	
	/**�����ά������ָ���ֵ**/
	public static final String TARGETTABLE = "3";
	
	/**ά�ȱ��ֶ����ͣ��ַ��ͣ�**/
	public static final Integer WDCOLUMNTYPECHAR = new Integer(1);
	
	/**ά�ȱ��ֶ����ͣ������ͣ�**/
	public static final Integer WDCOLUMNTYPENUMBER = new Integer(0);
	
	/**������ʽ--ҵ��ϵͳ���**/
	public static final String RELATIIONYWXTSC = "1";
	
	/**������ʽ--�ֹ�ά��**/
	public static final String RELATIONSGWH = "2";
	
	/**������ʽ--������**/
	public static final String RELATONJSX = "3";	
	//	-----------------ETLӳ���ϵ����End---------------------
	
	
	//	-----------------��Ȩ�޳���Start---------------------
	
	/**�û���Ȩ�޷���--��ǩȨ��**/
	public static final Integer POWERTYPECHECK = new Integer(1);
	
	/**�û���Ȩ�޷���--�鿴Ȩ��**/
	public static final Integer POWERTYPESEARCH = new Integer(2);
	
	/**�û���Ȩ�޷���--����Ȩ��**/
	public static final Integer POWERTYPEREPORT = new Integer(3);
	
	/**�û���Ȩ�޷���--����Ȩ��**/
	public static final Integer POWERTYPEVERIFY = new Integer(4);
	//	-----------------��Ȩ�޳���End---------------------
	
	//web���������� 1 ��ʾ��weblogic websphere 0 ��ʾ(tomcate)
	public static int WEB_SERVER_TYPE=0;
	
	//----------Excel�ļ�����������������������������
	
	/*��ֵ���͵�Ԫ�񱳾�ɫ*/
	public static Integer NUMBER_CELL_BGCOLOR=new Integer(43);
	
	/*��ʽ��Ԫ�񱳾�ɫ����ֵ��*/
	public static Integer FORMUAL_CELL_BGCOLOR=new Integer(44);
	
	/*�ַ����͵�Ԫ�񱳾�ɫ*/
	public static Integer STRING_CELL_BGCOLOR=new Integer(42);
	
	/*��ֵ����*/
	public static Integer NUMBER_CELL_TYPE = new Integer(2);
	
	/*�ַ�����*/
	public static Integer STRING_CELL_TYPE = new Integer(3);
	
	/*��ʽ����*/
	public static Integer FORMUAL_CELL_TYPE = new Integer(4);
	
    
    //-------------------��Ԫ��ֵ��(M_CELL) �������ͳ���ֵ--------------------
    
    /**�������������*/
    public static final Integer COLLECT_TYPE_NO_COLLECT = new Integer("0");
    
    /**��ֵ��ӻ�������*/
    public static final Integer COLLECT_TYPE_SUM = new Integer("1");
    
    /**ƽ��ֵ��������*/
    public static final Integer COLLECT_TYPE_AVG = new Integer("2");
    
    /**���ֵ��������*/
    public static final Integer COLLECT_TYPE_MAX = new Integer("3");
    
    /**��Сֵ��������*/
    public static final Integer COLLECT_TYPE_MIN = new Integer("4");
    
    /**��ݿ�����*/
    public static String DB_SERVER_TYPE="";
    /**�������*/
    public static String BANK_NAME="";
    
    /**�����б�Ĭ��ֵ��ȫ����*/
    public static final String DEFAULT_VALUE = "-999";
    
    /**��ܰ��ʿ����*/
    public static RemindTips REMINDTIPS = null;
    
	/*��Ŀ��Ƶ�Ԫ�񱳾�ɫ*/
	public static int PITEM_CELL_BGCOLOR=-256;
	
	/*��ʽ��Ԫ�񱳾�ɫ����ֵ��*/
	public static int PID_CELL_BGCOLOR=-65536;
	
	/*��Ŀ�е�Ԫ�񱳾�ɫ*/
	public static int PCOL_CELL_BGCOLOR=-16776961;
	
	/*��Ŀ�е�Ԫ�񱳾�ɫ*/
	public static int FORMULA_BGCOLOR=-6697729;
	
	/**����ɫ��Ԫ�񱳾�ɫRGB(192,192,192)*/
	public static int LGRAY_BGCOLOR=-4144960;
	/**��ɫ��Ԫ�񱳾�ɫRGB(150,150,150)*/
	public static int GRAY_BGCOLOR=-6908266;
	/**���ɫ��Ԫ�񱳾�ɫRGB(128,128,128)*/
	public static int DGRAY_BGCOLOR=-8355712;
	
	
	/**����״̬*/
	/** ��ǩͨ�� */
	public static final Short CHECK_FLAG_PASS = new Short(new String("1"));  //��ǩͨ��
	/** ��ǩ��ͨ�� */
	public static final Short CHECK_FLAG_FAILED = new Short(new String("-1"));  //��ǩ��ͨ��
	/** δ���� */
	public static final Short CHECK_FLAG_UNCHECK =new Short(new String("0"));  //δ����
	/** ��У�� */
	public static final Short CHECK_FLAG_AFTERJY = new Short(new String("2")); // ��У��	
	/** δ���� */
	public static final Short CHECK_FLAG_UNREPORT = new Short(new String("3"));   //δ����
	/** ��� */
	public static final Short CHECK_FLAG_AFTERSAVE = new Short(new String("4"));   //���
	/** �Ѹ��� */
	//public static final Short CHECK_FLAG_AFTERRECHECK = new Short(new String("5"));   //δ��ǩ
	/** ���˲�ͨ�� */
	//public static final Short CHECK_FLAG_RECHECKFAILED = new Short(new String("-5"));   //����δͨ��
	/** ����� */
	public static final Short CHECK_FLAG_PRODUCT = new Short(new String("-2"));   //�����
	
	/**
	 * ȫ����¼
	 */
	public static Integer FLAG_ALL = new Integer("9999");
	
	/**
	 * ��ѯ�����ִ�
	 */
	public static final String QUERY_TERM = "QueryTerm";
	
	/**
	 * ���渽��
	 */
	public static Integer FILE_TYPE_PLACARD_FILE = new Integer(5);
	
	/**
	 * ��������ʱ��ζ���
	 */
	public static Map TAKEDATA_TIME_INTERVAL = null;
	
	public static String RHTEMPLATE_TYPE = "141";
	public static String QTTEMPLATE_TYPE = "142";
	/**
	 * �������
	 */
	public static String DATATYPE_TYPE = "172";
	/**
	 * ��ֵ����
	 */
	public static String PSUZITYPE_TYPE = "173";
	/**
	 * ��λ����
	 */
	public static String DANWEIID_TYPE = "174";
	/**
	 * ����
	 */
	public static String CURID_TYPE = "175";
	/**
	 * ��¼����
	 */
	public static String USER_LOGIN_DATE = "userLoginDate";
	
    /**
     * raqʹ��·��
     */
    public static String SHARE_DATA_PATH = RAQ_TEMPLATE_PATH;
    /**
     * ���״̬λ
     */
    public static String BJ_VALIDATE = "BJ_VALIDATE";
	/**
	 * ����ϵͳ·��
	 */
    public static String FITOSA_URL = "";
	
    
    public static String DATASOURCE_NAME="rsmis";
    
    /***
     *  
			 * �������ֵ䣨�������б��뼰�������ڵ���
			 * �ɣ�����ֵ����ͱ��183����11λ��
			 * 1-4λ���б��룬5-11���ڵ�����
			 * ��54011310001
			 * �£�����ֵ����ͱ��185����13λ��
			 * 1λһ���룬2λ�����룬3-6λ3���루���б��룩��7-13���ڵ�����
			 * ��C154011310001
     */
    public static String CODE_LIB = "";
    /**
     * ��������-�������
     */
    public static final int HZLX_GCHZ = 1;
    /**
     * ���ܹ�ʽ�����е�ͬ����
     */
    public static final String HZGS_TJH = "HZTJH";
    /**
     * ���ܹ�ʽ�����е��Զ����
     */
    public static final String HZGS_ZDY = "CUSTOM_ORG";
    /**
     * ���ܹ�ʽ�б�ѡ��
     */
    public static Map HZGS_LIST ;
    
    public static int NODE_ID = -1; 
    
    static{
    	HZGS_LIST = new HashMap();
    	HZGS_LIST.put(HZGS_TJH, "ͬ����");
    	HZGS_LIST.put(HZGS_ZDY, "�Զ���");
    }
    
    /** 是否与平台整合 */
    public static boolean IS_INTEGRATE_PORTAL = false;
    public static String NEW_PORTAL_URL = "";
    public static boolean PORTAL = false;
    public static String PORATLSYNAIMPL = "";//同步门户实现类
}
