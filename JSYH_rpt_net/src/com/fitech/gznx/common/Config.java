package com.fitech.gznx.common;


public class Config {
	
	/**
	 * �����û�
	 */
	public static String SUPERUSER = "admin";
	/**
	 * ����ID
	 */
	public static String HEAD_ORG_ID = "";
	/**
	 * ���д���
	 */
	public static String TOPBANK = "0";
	/**
	 * ���⸸������
	 */
	public static String VIRTUAL_TOPBANK = "-99";
	/**
	 * ��������
	 */
	public static String BANK_NAME = "��������";
	
	public static final String RECORDS = null;
	/*��Ŀ���Ƶ�Ԫ�񱳾�ɫ*/
	public static int PITEM_CELL_BGCOLOR=43;
	
	/*��ʽ��Ԫ�񱳾�ɫ����ֵ��*/
	public static int PID_CELL_BGCOLOR=44;
	
	/*��Ŀ�е�Ԫ�񱳾�ɫ*/
	public static int PCOL_CELL_BGCOLOR=42;
	
	/**
	 * ������Ϣ������
	 */
	public static final String MESSAGES = "Message";
	
	/**
	 * ���ǻ��ܻ���
	 */
	public static String NOT_IS_COLLECT = "0";
	
	/**
	 * ϵͳ��·���ָ��
	 */
	public static final String FILESEPARATOR = System
			.getProperty("file.separator");

	
	/**
	 * ����Ƶ��
	 */
	public static final Integer FREQ_TIME = new Integer("7");
	
	/**
	 * ���ŷָ���
	 */
	public static final String SPLIT_SYMBOL_COMMA = ",";


	/**
	 * �ǻ��ܻ���
	 */
	public static String IS_COLLECT = "1";

	/**
	 * ���ܻ���������ID(��Ϊһ���ڵ����������ĸ�����ID)
	 * �������������org_type
	 */
	public static String COLLECT_ORG_PARENT_ID = "-99";
	
	/**
	 * ���ܻ���������ID(��Ϊһ���ڵ����������ĸ�����ID)
	 * �������������org_type
	 */
	public static String COLLECT_ORG_PARENT_QT_ID = "-98";
	
	/**
	 * �����ݹ�������ļ����ַ���
	 */
	public static String BASE_ORG_TREEXML_STR = "baseOrgTreeIterator.xml";
	
	/***
	 * ���ɹ�ϵ��
	 */
	public static String V_ORG_REL_XML = "vOrgRelTree.xml";
	
	public static String V_ORG_REL_STR = "vOrgRelTree.xml";

	/**
	 * �����ݹ���������ļ���
	 */
	public static String BASE_ORG_TREEXML_NAME = "baseOrgTreeIterator.xml";

	/**
	 * �����ݹ���������ļ���WEB����·��
	 */
	public static String BASE_ORG_TREEXML_WEB_PATH = Config.ORG_TREEXML_WEB_PATH
			+ Config.BASE_ORG_TREEXML_NAME;
	
	
	/**
	 * ����������Web�Ĵ��λ��
	 */
	public static String ORG_TREEXML_WEB_PATH = "";
	
	/**
	 * �û���¼��,���浽Session�е�����
	 */
	public static final String OPERATOR_SESSION_NAME = "Operator";

	/**
	 * �������е����������������
	 */
	public static String XML_ATTRIBUTE_VIRTUAL = "virtual";
;

	
	/**
	 * ��Ƶ��
	 */
	public static final Integer FREQ_DAY = new Integer("6");
	/**
	 * ��Ƶ��
	 */
	public static final Integer FREQ_WEEK = new Integer("7");
	/**
	 * �챨Ƶ��
	 */
	public static final Integer FREQ_MONTH_EXPRESS = new Integer("10");


	/**
	 * ѮƵ��
	 */
	public static final Integer FREQ_TENDAY = new Integer("8");

	/**
	 * ��Ƶ��
	 */
	public static final Integer FREQ_MONTH = new Integer("1");

	/**
	 * ��Ƶ��
	 */
	public static final Integer FREQ_SEASON = new Integer("2");

	/**
	 * ����Ƶ��
	 */
	public static final Integer FREQ_HALFYEAR = new Integer("3");

	/**
	 * ��Ƶ��
	 */
	public static final Integer FREQ_YEAR = new Integer("4");
	/**
	 * �����תƵ��
	 */
	public static final Integer FREQ_YEARBEGAIN = new Integer("9");

	/***************************************************************************
	 * �������ͣ�����ҳ���л��� *
	 **************************************************************************/
	/**
	 * ����ᱨ��
	 */
	public static String CBRC_REPORT = "1";
	public static String CBRC_REPORT_NAME = "����ᱨ��";

	/**
	 * ���б���
	 */
	public static String PBOC_REPORT = "2";
	public static String PBOC_REPORT_NAME = "���б���";
	/**
	 * ��������
	 */
	public static String OTHER_REPORT = "3";
	public static String OTHER_REPORT_NAME = "��������";
	/**
	 * ��ʱ����
	 */
	public static String TEMP_REPORT = "4";
	public static String TEMP_REPORT_NAME = "��������";
	/**
	 * �հ�ģ��excel����·��
	 */
	public static String TEMPLATE_PATH = com.cbrc.smis.common.Config.RAQ_TEMPLATE_PATH
			+ com.cbrc.smis.common.Config.FILESEPARATOR + "templateFiles" 
			+ com.cbrc.smis.common.Config.FILESEPARATOR + "excel"
			+ com.cbrc.smis.common.Config.FILESEPARATOR;

	/**
	 * ����excel����·��
	 */
	public static String REPORT_PATH = com.cbrc.smis.common.Config.RAQ_TEMPLATE_PATH
			+ com.cbrc.smis.common.Config.FILESEPARATOR + "reportFiles"
			+ com.cbrc.smis.common.Config.FILESEPARATOR;

	
	/***************************************************************************
	 * ���������ͣ�IS_REPORT��                                                  *
	 **************************************************************************/
	/**
	 * �����౨��
	 */
	public static String TEMPLATE_ANALYSIS = "1";
	public static String TEMPLATE_ANALYSIS_NAME = "�����౨��";
	
	/**
	 * �����౨��
	 */
	public static String TEMPLATE_REPORT = "2";
	public static String TEMPLATE_REPORT_NAME = "�����౨��";

	/**
	 * ��ѯ�౨��
	 */
	public static String TEMPLATE_VIEW = "3";
	public static String TEMPLATE_VIEW_NAME = "��ѯ�౨��";
	
	
	/***************************************************************************
	 * ���������ͣ�REPORT_STYLE��                                               *
	 **************************************************************************/
	/**
	 * ��Ե㱨��
	 */
	public static String REPORT_DD = "1";
	
	/**
	 * �嵥����
	 */
	public static String REPORT_QD = "2";
	/**
	 * �嵥ʽ�������ɱ�־
	 */
	public static String PROFLG_SENCEN_QD = "1";
	
	/**
	 * �嵥ʽ�����ѯ��־
	 */
	public static String PROFLG_CAXUN_QD = "2";
	
	/**
	 * �嵥ʽ�����������
	 */
	public static int MAX_ROW = 10000;
	
	/**
	 * �嵥ʽ�����ѯ��־
	 */
//	public static String CUSTOM_SEARCH = "200";
	
	/**
	 * �����ⱨ�������������ֵ��
	 */
	public static String RHORGIDTYPE = "183";
	/**
	 * �����ⱨ�������������ֵ����ͺ�
	 */
	public static String RHORGIDID = "1";
	
	
	/**
	 * ��׼�������ⱨ�������������ֵ��
	 */
	public static String RHFORMATORGIDTYPE = "185";
}
