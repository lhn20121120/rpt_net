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
	 * ���������Ƿ�ɹ��ı�־,�øñ�־���ж������洢�����Ƿ�ȫ������
	 */
	public static boolean FLOGSTOREPROCESS = false;
	
	/**
	 * ÿҳ��ʾ�ļ�¼��
	 */
	public static int recordCountByPage = 20; 

    /**
     * ʵ����������ļ���·��
     */
    public static String CONFIGBYIMPLPATH = Config.CONFIGBYIMPLPATH;
    
    
    /**
     * �������ݼ��ʱ��������ļ��ĵ�ַ
     */
    public static String INPUTDATAPREPADDR = Config.INPUTDATATIMEPREPADDR;

    /***************************************************************************
     * ��־��Ϣ
     **************************************************************************/
    /**
     * Ϊϵͳ����һ�������˵�����
     */
    public static final String HANDLER = "SYSTEM";

    /**
     * ����������־���ͳ�������
     */
    public static final Integer LOGSYSTEMSAVETYPE = new Integer(25);

    /**
     * ������¼��ɹ�ʱ�����ݿ���־�����������Ϣ
     */
    public static final String INPUTDATASUCCESS = "¼�����ݽ���";

    /**
     * ������¼��ʧ��ʱ�����ݿ���־�����������Ϣ
     */
    public static final String INPUTDATAWRONG = "¼������ʧ��";

    /**
     * ��ʵ�ʱ���REPORT_IN��¼���ʧ��ʱ������־����Ϣ
     */
    public static final String SAVEREPORTIN = "ʵ�ʱ���REPORT_IN��¼���ʧ��";

    /**
     * �������������ݱ�REPORT_IN_INFO��¼���ʧ��ʱ������־����Ϣ
     */
    public static final String SAVEREPORTININFO = "�����������ݱ�REPORT_IN_INFO��¼���ʧ��";

    /**
     * ����Ԫ�����ʧ��ʱ������־����Ϣ
     */
    public static final String SAVEMCELL = "��Ԫ�����ʧ��";
    
    /**
     * ��Ӧ�÷���������ʱ��Ҫ��ȡ�Ķ�ʱ����ʼ���Ĳ���
     */
    public static final String WRITERCOUNTONSTARTUP = "��ʱ������ʼ��ʱʧ��";
    

    /***************************************************************************
     * XML�������õ��ĳ�������
     **************************************************************************/
    /**
     * ���XML�ļ�����ʱ·��
     */
    public static final String TEMP_DIR = Config.TEMP_DIR;

    /**
     * ���ZIP�ļ���·��
     */
    public static final String ADDRESSZIP = Config.ADDRESSZIP;

    /**
     * 
     * ���������Ϣ
     */
    public static final String GETORGERROR = "�û�����¼ʱʧ��";

    /**
     * ��ѹ����������Ϣ
     */
    public static final String RELEASEANDPARSEBAD = "��ѹ�����ļ�ʧ��";

    /**
     * ���ڱ��˵�����Ϣʱ�������Ϣ
     */
    public static final String MYERRMESSAGE = "����������Ĵ�����Ϣ";

    /***************************************************************************
     * XMLԪ������������
     **************************************************************************/

    /**
     * ˵��XML�ļ��е��ӱ���Ԫ����
     */
    public static final String REPORT = "report";

    /**
     * ������
     */
    public static final String REPORTFILENAME = "reportFileName";

    /**
     * ����ID����
     */
    public static final String ORGID = "orgId";

    /**
     * �ӱ���ID����
     */
    public static final String REPORTID = "reportId";

    /**
     * �汾�ų���
     */
    public static final String VERSION = "version";

    /**
     * �ϱ����
     */
    public static final String YEAR = "year";

    /**
     * �ϱ��·�
     */
    public static final String MONTH = "month";

    /**
     * �ϱ�Ƶ��
     */
    public static final String FREQUENCYID = "frequencyId";

    /**
     * �ڼ���
     */
    public static final String TERM = "term";

    /**
     * ���ݷ�ΧID������ʾ��
     */
    public static final String DATARANGEID = "dataRangeId";

    /**
     * ����
     */
    public static final String FITECHCURR = "fitechCurr";

    /**
     * �����
     */
    public static final String COMMONCURRNAME = "�����";

    /**
     * ����
     */
    public static final String WRITER = "writer";
    /**
     * �����
     */
    public static final String CHECKER="checker";
    /**
     * ������
     */
    public static final String PRINCIPAL="principal";
    /**
     * ���ʹ���
     */
    public static final String TIMES = "times";
    
    /**
     * �������ݼ�¼ID ���ڶ���������ͬ������
     */
    public static final String REPOUTID = "realId"; //"repOutId";

    /**
     * ����ͷԪ����,һ�㿿���õ�һЩ���ڱ���ĸ�����Ϣ,���籨������
     */
    public static final String DETAILHEADER = "detailHeader";

    /**
     * ʵ�ʱ�������
     */
    public static final String FITECHTITLE = "fitechTitle";

    /**
     * �ϱ�����
     */
    public static final String FITECHDATE = "fitechDate";
    /**
     * �ϱ����
     */
    public static final String FITECHSUBMITYEAR="fitechSubmitYear";
    /**
     * �ϱ��·�
     */
    public static final String FITECHSUBMITMONTH="fitechSubmitMonth";
    /**
     * ����Ԫ����
     */
    public static final String FITECHMCURR = "fitechMcurr";

    /**
     * ��Ե�Ԫ�ص�Ԫ����F
     */
    public static final String UPPERELEMENT = "F";

    /**
     * ��Ե��Ԫ����P1
     */
    public static final String SECONDUPPER = "P1";
    
    
    /**
     * G5312��Ե㱨��ĸ�Ԫ����
     */
    public static final String G5301 = "G5301";
    /**
     * G5100������
     */
    public static final String G5100="G5100";
    
    /**
     * �嵥ʽ��XML�ļ��е�detailԪ����
     */
    public static final String DETAIL = "detail";

    /**
     * �嵥ʽ��XML�ļ��е�title
     */
    public static final String TOTAL = "total";

    /**
     * �嵥ʽ������
     */
    public static final String COL1 = "COL1";

    /**
     * ���嵥ʽ�����Ԫ����titleʱCOL1�����ı�ʶ��
     */
    public static final String LABELBYLIST = "";

    /**
     * ˵��XML�ļ����ļ���
     */
    public static final String EXPLAINFILENAME = "listing.xml";

    /***************************************************************************
     * �����嵥ʽ����ĳ�������
     **************************************************************************/

    /**
     * ��һ���嵥ʽ�ı���
     */
    public static final String TYPE1 = "type1"; //s32_1,s32_2,s32_3,s32_4,s32_5,s32_6,s32_7,s32_9,s33,s35

    /**
     * �ڶ����嵥ʽ�ı���
     */
    public static final String TYPE2 = "type2"; //s36,s37,s34_2

    /**
     * �������嵥ʽ�ı���
     */
    public static final String TYPE3 = "type3"; //s34_1,G51

    /**
     * �������嵥ʽ�ı���
     */
    public static final String TYPE4 = "type4"; //s38

    /**
     * ���Ʒ�ת
     */
    public static final String TEST = "type2";

    /***************************************************************************
     * ���÷��Ŷ���
     **************************************************************************/

    /**
     * ָ���Ƿ��� sss"-"
     */
    public static final String COMPART = "-";
    
    /**
	 * �ϼƵ�Ԫ����ʾ������
	 */
	public static String TOTALLABEL="�ϼ�";
	
	/**
	 * ���
	 */
	public static String FITECHFILLER = "fitechFiller";
	
	/**
	 * ������
	 */
	public static String FITECHCHECKER = "fitechChecker";
	
	/**
	 * ������
	 */
	public static String FITECHPRINCIPAL = "fitechPrincipal";
	/**
	 * ���Ϳھ�
	 */
	public static String FITECHRANGE = "fitechRange";
	/**
	 * �ӱ���
	 */
	public static String FITECHSUBTITLE = "fitechSubtitle";
}