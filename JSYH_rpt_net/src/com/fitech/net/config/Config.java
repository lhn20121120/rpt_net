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
	/**项目根路径(相对于文件系统)**/
	public static String REAL_ROOT_PATH = com.cbrc.smis.common.Config.WEBROOTPATH;	
	/**报表文件相对路径*/
	public static final String REPORT_NAME = "Reports";	
	/**统计分析报表文件相对路径*/
	public static final String ANALYSIS_REPORT_NAME = "reportlets";	
	/**图片文件夹名称**/
	public static final String IMAGES_NAME = "image";
	/**模板文件夹*/
	public static final String TEMPLATE_NAME = "templates";
	/**取数模板文件夹(配置完取数信息的Excel文件夹)*/
	public static final String OBTAIN_TEMPLATE = "ObtainTemplates";
	/**取数数据源文件夹*/
	public static final String OBTAIN_DATASOURCE = "ObtainDatasource";
	/**汇总文件存放文件夹*/
	public static final String COLLECT_EXCEL = "collectExcels";
	/**汇总文件存放文件夹全路径*/
	public static final String COLLECT_EXCEL_ALL_PATH = "Reports/collectExcels";
	/**模版文件存放文件夹全路径*/
	public static final String TEMPLATE_EXCEL_ALL_PATH = "Reports/templates";
	
	/**取数发布文件夹 准备发布的文件存放地release*/
	public static final String OBTAIN_RELEASE = "releaseTemplates";
	/**系统配置文件文件夹名称*/
	public static final String SYSTEM_FOLDER = "system";	
	/**汇总配置文件名称*/
	public static final String CONFIGURATION_COLLECT_FILE = "collectConfig.xml";
	/**取数配置文件*/
	public static final String CONFIGURATION_OBTAIN_FILE = "obtainConfig.xml";	
	
	/**取数，Excel数据源所在地*/
	public static String DATA_SOURCE_EXCEL = "d:"+ com.cbrc.smis.common.Config.FILESEPARATOR +"fitech"+ com.cbrc.smis.common.Config.FILESEPARATOR +"excel";
	/**取数，Excel数据源的配置状态－临时*/
	public static final String DATA_SOURCE_STATE_TEMP = "temp";
	/**取数，Excel数据源的配置状态－持久化*/
	public static final String DATA_SOURCE_STATE_SAVED = "saved";
	/**取数模式，txt取数*/
	public static final String OBTAIN_MODE_TXT = "txt";
	/**取数模式，excel取数*/
	public static final String OBTAIN_MODE_EXCEL = "excel";
	/**webRoot_temp*/
	public static final String WEBROOT_TEMP = "temp"; 
	/**报表临时目录*/
	static public final String REPORT_TEMP = "temp";
	/**报表临时目录下的docFile目录*/
	public static final String WEBROOT_TEMP_DOCFILE = "docFile";
	/**添加指标的excel的名称*/
	public static final String TARGET_EXCEL_NAME="targetFumal.xls";
	
	/**报表状态*/
	/** 审签通过 */
	public static final Short CHECK_FLAG_PASS = new Short(new String("1"));  //审签通过
	/** 审签不通过 */
	public static final Short CHECK_FLAG_FAILED = new Short(new String("-1"));  //审签不通过
	/** 未复核 */
	public static final Short CHECK_FLAG_UNCHECK =new Short(new String("0"));  //未复核
	/** 已校验 */
	public static final Short CHECK_FLAG_AFTERJY = new Short(new String("2")); // 已校验	
	/** 未报送 */
	public static final Short CHECK_FLAG_UNREPORT = new Short(new String("3"));   //未报送
	/** 已填报 */
	public static final Short CHECK_FLAG_AFTERSAVE = new Short(new String("4"));   //已填报
	/** 已复核 */
	//public static final Short CHECK_FLAG_AFTERRECHECK = new Short(new String("5"));   //未审签
	/** 复核不通过 */
	//public static final Short CHECK_FLAG_RECHECKFAILED = new Short(new String("-5"));   //复核未通过
	
	
	
	/**模拟服务器端文件的存放位置*/
	public static final String SERVICE_FILE="templates"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";	
	
	/**文件在服务器端的临时存放位置*/
	public static final String SERVICE_TEMP="download_temp"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";	
	
	/**模拟客户端文件的存放位置*/
	//public static final String CLIENT_FILE="F:"+ com.cbrc.smis.common.Config.FILESEPARATOR +"B";
	
	/**下载所有文件所在压缩包的名字*/
	public static final String DOWNLOAD_ALL="all";
	
	/**下载所有最新文件所在压缩包的名字*/
	public static final String DOWNLOAD_NEW="new";
	
	/**下载选择的文件所放的压缩包名字*/
	public static final String DOWNLOAD_SELECT="select";
	
	/**上传文件在服务器上的临时位置*/
	public static final String SERVICE_UP_TEMP="upload_temp"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";
	
	/**所有xml文件在服务器上的临时位置*/
	public static final String SERVICE_UP_XML="xml"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";	
	
	/**上传文件解压缩后在服务器上的临时位置*/
	public static final String SERVICE_UP_RELEASE="release_temp"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";
	
	/**上传文件确定要入库的文件在服务器上的临时位置*/
	public static final String SERVICE_UP_IN="into_temp"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";
	
	/**没有必要上传的文件集合*/
	public static final String NO_NEED_INFO="noNeedInfo";
	
	/**备份文件的存放位置*/
	public static final String BACKUP="backup"+ com.cbrc.smis.common.Config.FILESEPARATOR +"";
	
	/**判定是否要显示入库按钮*/
	public static final String NOT_SHOW="notshow";
	/**指标定义预警类型*/
	public static final String Target_Warn="1";
	/**比上期*/
	public static final String Target_Pre_Standard="2";
	/**立即数*/
	public static final String IMMEDIATE_DATA="immdiatedata";
	
	
	/**
	 * 用户登录后,保存到Session中的名称
	 */
	public static final String OPERATOR_SESSION_ATTRIBUTE_NAME = "Operator";
	
	/**提示信息*/
	public static final String MESSAGE="Message";
	
	public static String FILENAME = null;
	
	/** 模板类型  */
	public static final String FZ_SF_TAMPLATE="SF";
	public static final String FZ_GF_TAMPLATE="GF";
	
	/** 分支模板 */
	public static final Integer FZ_TEMPLATE = new Integer(1);
	/** 法人模板 */
	public static final Integer FR_TEMPLATE = new Integer(0);
	
	
    /** 境内汇总数据*/
	public static final String reportRange="境内汇总数据";
	
	
	/**比上年同期**/
	public static final String Target_Last_Year_Same_Month_Standard="3";
	/**比上年年末(12月)**/
	public static final String Target_Last_Year_December_Standard="4";
	
	/**
	 * 获得汇总文件存放路径
	 * @return
	 */
	public static String getCollectExcelFolder(){
		String path = REAL_ROOT_PATH + REPORT_NAME + File.separator + COLLECT_EXCEL;
		return path;
	}
	
	/**
	 * 获得存放报表取数配置完毕模板的目录路径
	 * @return 存放报表取数配置完毕模板的目录路径
	 */
	public static String getObtainTemplateFolderRealPath(){
		String path = REAL_ROOT_PATH + REPORT_NAME + File.separator + OBTAIN_TEMPLATE ;
		return path;
	}
	
	/**
	 * 获得存放报表模板的临时目录路径
	 * @return 存放报表模板的临时目录路径
	 */
	public static String getTemplateTempFolderRealPath(){
		String path = REAL_ROOT_PATH + REPORT_NAME + File.separator + REPORT_TEMP;
		return path;
	}
	
	/**
	 * 获得报表模板存放路径
	 * @return 模板存放路径
	 */
	public static String getTemplateFolderRealPath(){
		String path = com.cbrc.smis.common.Config.RAQ_TEMPLATE_PATH + REPORT_NAME + File.separator + TEMPLATE_NAME;
		return path;
	}
	
	/**
	 * 获得报表发布模板存放路径
	 * @return
	 */
	public static String getReleaseTemplatePath(){
		String path = REAL_ROOT_PATH + REPORT_NAME + File.separator + OBTAIN_RELEASE;
		return path;
	}
	/**	 
	 * @return 报表目录的绝对路径
	 */
	public static String getReportsFolderRealPath(){
		return (REAL_ROOT_PATH + REPORT_NAME);
	}
	
	/**	 
	 * @return 汇总配置文件绝对路径
	 */
	public static String getCollectConfigFilePath(){
		
		String sysPath = getSystemFolderPath();		
		String fPath = sysPath + CONFIGURATION_COLLECT_FILE;
		
		return fPath;
	}
	
	/**
	 * 
	 * @return取数配置文件绝对路径
	 */
	public static String getObtainConfigFilePath(){
		String sysPath = getSystemFolderPath();		
		String fPath = sysPath + Config.CONFIGURATION_OBTAIN_FILE;
		
		return fPath;
		
	}
	
	/**
	 * 
	 * @return 系统配置文件文件夹绝对路径
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
	 * text存放的路径
	 */
	public static String GetTextPath()
	{
		String path="d:"+ com.cbrc.smis.common.Config.FILESEPARATOR +"fitech"+ com.cbrc.smis.common.Config.FILESEPARATOR +"txt";
		return path;
	}
	/**
	 * 取数excel目录
	 * @return
	 */
	public static String GetExcelPath()
	{
		String path="d:"+ com.cbrc.smis.common.Config.FILESEPARATOR +"test"+ com.cbrc.smis.common.Config.FILESEPARATOR +"excel";
		return path;
	}
	/**
	 * 存放在Session中的已经设置的数据关系
	 * */
	public static String DATA_RELATION_IS_SET= "DataRelationIsSet";


}
