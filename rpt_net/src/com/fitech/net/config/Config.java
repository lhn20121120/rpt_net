/*
 * Created on 2006-5-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.fitech.net.config;

import java.io.File;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class Config {
	/**��Ŀ��·��(������ļ�ϵͳ)**/
	public static String REAL_ROOT_PATH = com.cbrc.smis.common.Config.WEBROOTPATH;	
	/**�����ļ����·��*/
	public static final String REPORT_NAME = "Reports";	
	/**ͳ�Ʒ��������ļ����·��*/
	public static final String ANALYSIS_REPORT_NAME = "reportlets";	
	/**ͼƬ�ļ�������**/
	public static final String IMAGES_NAME = "image";
	/**ģ���ļ���*/
	public static final String TEMPLATE_NAME = "templates";
	/**ȡ��ģ���ļ���(������ȡ����Ϣ��Excel�ļ���)*/
	public static final String OBTAIN_TEMPLATE = "ObtainTemplates";
	/**ȡ������Դ�ļ���*/
	public static final String OBTAIN_DATASOURCE = "ObtainDatasource";
	/**�����ļ�����ļ���*/
	public static final String COLLECT_EXCEL = "collectExcels";
	/**�����ļ�����ļ���ȫ·��*/
	public static final String COLLECT_EXCEL_ALL_PATH = "Reports/collectExcels";
	/**ģ���ļ�����ļ���ȫ·��*/
	public static final String TEMPLATE_EXCEL_ALL_PATH = "Reports/templates";
	
	/**ȡ�������ļ��� ׼���������ļ���ŵ�release*/
	public static final String OBTAIN_RELEASE = "releaseTemplates";
	/**ϵͳ�����ļ��ļ�������*/
	public static final String SYSTEM_FOLDER = "system";	
	/**���������ļ�����*/
	public static final String CONFIGURATION_COLLECT_FILE = "collectConfig.xml";
	/**ȡ�������ļ�*/
	public static final String CONFIGURATION_OBTAIN_FILE = "obtainConfig.xml";	
	
	/**ȡ����Excel����Դ���ڵ�*/
	public static String DATA_SOURCE_EXCEL = "d:"+ com.cbrc.smis.common.Config.FILESEPARATOR +"fitech"+ com.cbrc.smis.common.Config.FILESEPARATOR +"excel";
	/**ȡ����Excel����Դ������״̬����ʱ*/
	public static final String DATA_SOURCE_STATE_TEMP = "temp";
	/**ȡ����Excel����Դ������״̬���־û�*/
	public static final String DATA_SOURCE_STATE_SAVED = "saved";
	/**ȡ��ģʽ��txtȡ��*/
	public static final String OBTAIN_MODE_TXT = "txt";
	/**ȡ��ģʽ��excelȡ��*/
	public static final String OBTAIN_MODE_EXCEL = "excel";
	/**webRoot_temp*/
	public static final String WEBROOT_TEMP = "temp"; 
	/**������ʱĿ¼*/
	static public final String REPORT_TEMP = "temp";
	/**������ʱĿ¼�µ�docFileĿ¼*/
	public static final String WEBROOT_TEMP_DOCFILE = "docFile";
	/**���ָ���excel������*/
	public static final String TARGET_EXCEL_NAME="targetFumal.xls";
	
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
	
	
	
	/**ģ����������ļ��Ĵ��λ��*/
	public static final String SERVICE_FILE="templates"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";	
	
	/**�ļ��ڷ������˵���ʱ���λ��*/
	public static final String SERVICE_TEMP="download_temp"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";	
	
	/**ģ��ͻ����ļ��Ĵ��λ��*/
	//public static final String CLIENT_FILE="F:"+ com.cbrc.smis.common.Config.FILESEPARATOR +"B";
	
	/**���������ļ�����ѹ����������*/
	public static final String DOWNLOAD_ALL="all";
	
	/**�������������ļ�����ѹ����������*/
	public static final String DOWNLOAD_NEW="new";
	
	/**����ѡ����ļ����ŵ�ѹ��������*/
	public static final String DOWNLOAD_SELECT="select";
	
	/**�ϴ��ļ��ڷ������ϵ���ʱλ��*/
	public static final String SERVICE_UP_TEMP="upload_temp"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";
	
	/**����xml�ļ��ڷ������ϵ���ʱλ��*/
	public static final String SERVICE_UP_XML="xml"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";	
	
	/**�ϴ��ļ���ѹ�����ڷ������ϵ���ʱλ��*/
	public static final String SERVICE_UP_RELEASE="release_temp"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";
	
	/**�ϴ��ļ�ȷ��Ҫ�����ļ��ڷ������ϵ���ʱλ��*/
	public static final String SERVICE_UP_IN="into_temp"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";
	
	/**û�б�Ҫ�ϴ����ļ�����*/
	public static final String NO_NEED_INFO="noNeedInfo";
	
	/**�����ļ��Ĵ��λ��*/
	public static final String BACKUP="backup"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";
	
	/**�ж��Ƿ�Ҫ��ʾ��ⰴť*/
	public static final String NOT_SHOW="notshow";
	/**ָ�궨��Ԥ������*/
	public static final String Target_Warn="1";
	/**������*/
	public static final String Target_Pre_Standard="2";
	/**������*/
	public static final String IMMEDIATE_DATA="immdiatedata";
	
	
	/**
	 * �û���¼��,���浽Session�е�����
	 */
	public static final String OPERATOR_SESSION_ATTRIBUTE_NAME = "Operator";
	
	/**��ʾ��Ϣ*/
	public static final String MESSAGE="Message";
	
	public static String FILENAME = null;
	
	/** ģ������  */
	public static final String FZ_SF_TAMPLATE="SF";
	public static final String FZ_GF_TAMPLATE="GF";
	
	/** ��֧ģ�� */
	public static final Integer FZ_TEMPLATE = new Integer(1);
	/** ����ģ�� */
	public static final Integer FR_TEMPLATE = new Integer(0);
	
	
    /** ���ڻ�������*/
	public static final String reportRange="���ڻ�������";
	
	
	/**������ͬ��**/
	public static final String Target_Last_Year_Same_Month_Standard="3";
	/**��������ĩ(12��)**/
	public static final String Target_Last_Year_December_Standard="4";
	
	/**
	 * ��û����ļ����·��
	 * @return
	 */
	public static String getCollectExcelFolder(){
		String path = REAL_ROOT_PATH + REPORT_NAME + File.separator + COLLECT_EXCEL;
		return path;
	}
	
	/**
	 * ��ô�ű���ȡ���������ģ���Ŀ¼·��
	 * @return ��ű���ȡ���������ģ���Ŀ¼·��
	 */
	public static String getObtainTemplateFolderRealPath(){
		String path = REAL_ROOT_PATH + REPORT_NAME + File.separator + OBTAIN_TEMPLATE ;
		return path;
	}
	
	/**
	 * ��ô�ű���ģ�����ʱĿ¼·��
	 * @return ��ű���ģ�����ʱĿ¼·��
	 */
	public static String getTemplateTempFolderRealPath(){
		String path = REAL_ROOT_PATH + REPORT_NAME + File.separator + REPORT_TEMP;
		return path;
	}
	
	/**
	 * ��ñ���ģ����·��
	 * @return ģ����·��
	 */
	public static String getTemplateFolderRealPath(){
		String path = com.cbrc.smis.common.Config.RAQ_TEMPLATE_PATH + REPORT_NAME + File.separator + TEMPLATE_NAME;
		return path;
	}
	
	/**
	 * ��ñ�����ģ����·��
	 * @return
	 */
	public static String getReleaseTemplatePath(){
		String path = REAL_ROOT_PATH + REPORT_NAME + File.separator + OBTAIN_RELEASE;
		return path;
	}
	/**	 
	 * @return ����Ŀ¼�ľ���·��
	 */
	public static String getReportsFolderRealPath(){
		return (REAL_ROOT_PATH + REPORT_NAME);
	}
	
	/**	 
	 * @return ���������ļ�����·��
	 */
	public static String getCollectConfigFilePath(){
		
		String sysPath = getSystemFolderPath();		
		String fPath = sysPath + CONFIGURATION_COLLECT_FILE;
		
		return fPath;
	}
	
	/**
	 * 
	 * @returnȡ�������ļ�����·��
	 */
	public static String getObtainConfigFilePath(){
		String sysPath = getSystemFolderPath();		
		String fPath = sysPath + Config.CONFIGURATION_OBTAIN_FILE;
		
		return fPath;
		
	}
	
	/**
	 * 
	 * @return ϵͳ�����ļ��ļ��о���·��
	 */
	public static String getSystemFolderPath(){
		String sysPath =  String.valueOf(
				new StringBuffer(REAL_ROOT_PATH)
				.append(SYSTEM_FOLDER)
				.append(File.separator)
				);
		
		return sysPath;
	}
	/*
	 * text��ŵ�·��
	 */
	public static String GetTextPath()
	{
		String path="d:"+ com.cbrc.smis.common.Config.FILESEPARATOR +"fitech"+ com.cbrc.smis.common.Config.FILESEPARATOR +"txt";
		return path;
	}
	/**
	 * ȡ��excelĿ¼
	 * @return
	 */
	public static String GetExcelPath()
	{
		String path="d:"+ com.cbrc.smis.common.Config.FILESEPARATOR +"test"+ com.cbrc.smis.common.Config.FILESEPARATOR +"excel";
		return path;
	}
	/**
	 * �����Session�е��Ѿ����õ����ݹ�ϵ
	 * */
	public static String DATA_RELATION_IS_SET= "DataRelationIsSet";


}
