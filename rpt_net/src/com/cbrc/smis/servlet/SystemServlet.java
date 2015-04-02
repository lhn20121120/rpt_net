package com.cbrc.smis.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cbrc.auth.hibernate.RemindTips;
import com.cbrc.smis.adapter.StrutsLogTypeDelegate;
import com.cbrc.smis.adapter.StrutsSysSetDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.proc.impl.ReportDDImpl;
import com.cbrc.smis.util.FitechException;
import com.gather.db.helper.SequenceDB;

/**
 * @author rds
 * @date 2005-11-22
 * 
 * 系统运行的初始化设置类
 */
public class SystemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FitechException log = new FitechException(SystemServlet.class);

	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	/**
	 * 系统运行
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * 
	 * @return void
	 * @exception IOException,ServletException
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();

		
		//获得应用的真实路径(解决war包中无法获得应用真实路径的问题)
		String appsRealPath = "";
		
		if(context.getRealPath("/")!=null && !context.getRealPath("/").trim().equals("")
				&& !context.getRealPath("/").equals("null")){
			appsRealPath = context.getRealPath("/");
			this.log.println("======================may catch!");
		}else{
			this.log.println("======================may not catch!");
			try{
				URL url = context.getResource("/");	
				appsRealPath = url.getPath();
				if(appsRealPath.trim().startsWith("/")){
					appsRealPath = appsRealPath.substring(1, appsRealPath.length());
					this.log.println(appsRealPath);
					String [] apps = appsRealPath.split("/");
					appsRealPath = "";
					for(int i=0;i<apps.length;i++){
						appsRealPath = !appsRealPath.equals("")
							? appsRealPath + File.separator + apps[i]:apps[i];
					}
					if(!appsRealPath.endsWith(File.separator))
						appsRealPath += File.separator;
					appsRealPath = File.separator + appsRealPath;
					this.log.println("====================" + appsRealPath);
					//appsRealPath = appsRealPath.replaceAll("/", "\\\\");
				}
			}catch(MalformedURLException e){
				log.printStackTrace(e);
			}			
		}
		
		// 设置系统应用的根路径
		//Config.WEBROOTPATH = context.getRealPath("/") + File.separator;
		Config.WEBROOTPATH = appsRealPath.endsWith(Config.FILESEPARATOR) ? appsRealPath : appsRealPath + Config.FILESEPARATOR;
		Config.RAQ_TEMPLATE_PATH= appsRealPath.endsWith(Config.FILESEPARATOR) ? appsRealPath : appsRealPath + Config.FILESEPARATOR;
		// 设置临时文件存放的位置
		Config.TEMP_DIR = Config.WEBROOTPATH + "tmp" + Config.FILESEPARATOR;
		//数据查询模板模板excel文件，使用数据查询模块的生成excel文件将采用此模板xls文件
		Config.XLSFILEPATH = Config.WEBROOTPATH + "temp" + Config.FILESEPARATOR + "result.xls";
		//痕迹查询模板excel文件，使用痕迹查询模板生成excel文件将采用此模板xls文件
		Config.TRACEFILEPATH = Config.WEBROOTPATH + "temp" + Config.FILESEPARATOR + "trace_data_list.xls";
		// PDF报表模板文件存放的物理路径
		Config.PDF_TEMPLATE_PATH = Config.WEBROOTPATH + "template"
				+ Config.FILESEPARATOR + "pdf" + Config.FILESEPARATOR;

		// 对数据入库实现类的配置文件的物理路径进行初始化
		Config.CONFIGBYIMPLPATH = Config.WEBROOTPATH + "WEB-INF"
				+ Config.FILESEPARATOR + "classes" + Config.FILESEPARATOR
				+ "com" + Config.FILESEPARATOR + "cbrc" + Config.FILESEPARATOR
				+ "smis" + Config.FILESEPARATOR + "system"
				+ Config.FILESEPARATOR + "cb" + Config.FILESEPARATOR
				+ "configInputDataImpl.properties";

		// 对数据入库时间间隔配置文件的物理路径进行初始化
		Config.INPUTDATATIMEPREPADDR = Config.WEBROOTPATH + "WEB-INF"
				+ Config.FILESEPARATOR + "classes" + Config.FILESEPARATOR
				+ "com" + Config.FILESEPARATOR + "cbrc" + Config.FILESEPARATOR
				+ "smis" + Config.FILESEPARATOR + "system"
				+ Config.FILESEPARATOR + "cb" + Config.FILESEPARATOR
				+ "InputDataTimeConfig.properties";

		// 设置从外网获得报表数据ZIP文件的存放位置
		Config.ADDRESSZIP = config.getInitParameter("ReportDataPath");
		// 设置从外网获得其他文件存放的位置

		// 设置生成仓库接口XML文件存放的位置
		Config.XMLData_PATH = config.getInitParameter("XMLPath");

		// 信息发布文件备份的目录
		Config.BAK_INFO_FILES_OUTPATH = config
				.getInitParameter("BAK_INFO_FILES_OUTPATH");

		// 信息上传文件备份的目录
		Config.BAK_INFO_FILES_UPPATH = config
				.getInitParameter("BAK_INFO_FILES_UPPATH");

		// 初始化CA服务的IP地址
		Config.CAIP = config.getInitParameter("CAIP");

		// 初始化CA服务的端口号
		String caportByString = config.getInitParameter("CAPORT");
		
		// 初始化生成文件的方式
		Config.DATATYPE = config.getInitParameter("DATATYPE");
		
		/**初始化贴士提醒*/
		File file = new File(Config.WEBROOTPATH+"remindTips.txt");
		
		if(!config.getInitParameter("RAQ_TEMPLATE_PATH").trim().equals("")){
		
			Config.RAQ_TEMPLATE_PATH = config.getInitParameter("RAQ_TEMPLATE_PATH") + Config.FILESEPARATOR;
			Config.RAQ_INIT_TEMP_PATH = Config.RAQ_TEMPLATE_PATH + "templateFiles" + Config.FILESEPARATOR + "initRaq" + Config.FILESEPARATOR;
			File raqInitTempPath = new File(Config.RAQ_INIT_TEMP_PATH);
			if(!raqInitTempPath.exists())
				raqInitTempPath.mkdir();
		}
		
		Config.TAKEDATA_TIME_INTERVAL = new HashMap();

		Config.TAKEDATA_TIME_INTERVAL.put("1", "21");

		Config.TAKEDATA_TIME_INTERVAL.put("2", "21");

		Config.TAKEDATA_TIME_INTERVAL.put("3", "22");
		
		if(file.exists()){
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				
				if(line != null){
					String[] remindTip = line.split(Config.SPLIT_SYMBOL_COMMA);
					if(remindTip.length == 3)
						Config.REMINDTIPS = new RemindTips(remindTip[0],remindTip[1],remindTip[2],null);
					else
						Config.REMINDTIPS = new RemindTips(remindTip[0],remindTip[1],remindTip[2],remindTip[3]);
				}
				reader.close();
			} catch (FileNotFoundException e) {
				Config.REMINDTIPS = null;
				e.printStackTrace();
			} catch (IOException e) {
				Config.REMINDTIPS = null;
				e.printStackTrace();
			}
		}
		
		Properties prop = new Properties();
		try{
			prop.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
			Config.WEB_SERVER_TYPE=Integer.parseInt(prop.getProperty("web.server.type"));
			Config.DB_SERVER_TYPE=prop.getProperty("db.server.type");
			Config.BANK_NAME=prop.getProperty("bank.name");
			ReportDDImpl.needReplaceStr=prop.getProperty("need.replace.str");
			Config.ISADDFITOSA = new Boolean(prop.getProperty("is.add.fitosa"));
			Config.ISADDDESC = new Boolean(prop.getProperty("is.add.desc"));
			try{
				Config.ADD_OLD_COLLECT = new Integer((prop.getProperty("add_old_collect")));
			}catch(Exception e){
				Config.ADD_OLD_COLLECT = new Integer(0);
			}
			try{
				Config.RH_DESC_CONTE = prop.getProperty("rh_desc_conte");
				Config.RH_FORMAT_END =prop.getProperty("rh_format_end");
			}catch(Exception e){
				Config.RH_DESC_CONTE = "outerId,orgName";
				Config.RH_FORMAT_END = "d";
			}
			try {
				//是否连接EAST系统
				Config.PORTAL = new Boolean(prop.getProperty("portal"));
				//EAST系统数据库连接参数
				Config.PORTALDRIVER = prop.getProperty("portal_dirver");
				Config.PORTALURL = prop.getProperty("portal_url");
				Config.PORTALUSERNAME = prop.getProperty("portal_username");
				Config.PORTALPWD = prop.getProperty("portal_pwd");
			} catch (Exception e) {
				e.printStackTrace();
				Config.PORTAL = false;
				//EAST系统数据库连接参数
				Config.PORTALDRIVER = "";
				Config.PORTALURL = "";
				Config.PORTALUSERNAME = "";
				Config.PORTALPWD = "";
			}
			try{
				Config.PORATLSYNAIMPL = prop.getProperty("portal_syna_impl");
			}catch(Exception e){
				Config.PORATLSYNAIMPL = "com.cbrc.auth.adapter";
			}
			// add by 王明明
			try {
				//是否合并角色和用户组管理
				Config.ROLE_GROUP_UNION_FLAG=new Integer(prop.getProperty("role_group_union_flag"));
			} catch (Exception e) {
				// TODO: handle exception
				Config.ROLE_GROUP_UNION_FLAG=0;
			}
			try {
				//任务化模式
				Config.ETL_TASK_CHECK=new Integer(prop.getProperty("system_schema_flag"));
			} catch (Exception e) {
				// TODO: handle exception
				Config.ETL_TASK_CHECK=0;
			}
			try {
				//任务化模式
				Config.SYSTEM_SCHEMA_FLAG=new Integer(prop.getProperty("system_schema_flag"));
			} catch (Exception e) {
				// TODO: handle exception
				Config.SYSTEM_SCHEMA_FLAG=0;
			}
			
			try {
				Config.IS_IGNORE_FLAG = Integer.parseInt(prop.getProperty("is_ignore_flag"));
			} catch (Exception e) {
				e.printStackTrace();
				Config.IS_IGNORE_FLAG = 0;
			}
			try {
				Config.TEMPLATE_MANAGE_FLAG = Integer.parseInt(prop.getProperty("template_manage_flag"));
			} catch (Exception e) {
				e.printStackTrace();
				Config.TEMPLATE_MANAGE_FLAG = 0;
			}
			try {
				Config.SEQUENSE_CONFIG = prop.getProperty("sequense_config");//获取sequence配置
			} catch (Exception e) {
				log.println("获取sequence配置失败！！！");
				log.printStackTrace(e);
				Config.SEQUENSE_CONFIG = "";
			}
			try {
				Config.YJ_EXP_REP_FLAG = Integer.parseInt(prop.getProperty("yj_exp_rep_flag"));//获取银监导出状态配置
			} catch (Exception e) {
				log.println("获取银监导出状态配置失败！！！");
				log.printStackTrace(e);
				Config.YJ_EXP_REP_FLAG = 1;
			}
			try {
				Config.RH_EXP_REP_FLAG = Integer.parseInt(prop.getProperty("rh_exp_rep_flag"));//获取人行导出状态配置
			} catch (Exception e) {
				log.println("获取人行导出状态配置失败！！！");
				log.printStackTrace(e);
				Config.RH_EXP_REP_FLAG = 1;
			}
			try{// 报送时间单位
				Config.REPORT_TIME_UNIT = prop.getProperty("report_time_unit");
			}catch(Exception e){
				log.println("获取报送时间单位配置失败！！！");
				Config.REPORT_TIME_UNIT = "day";
				e.printStackTrace();
			}
			try{// 银监分支是否作为单独条线生效的标志
				Config.YJ_BRANCH_BUSI_LINE = Integer.parseInt(prop.getProperty("yjBranchBusiLine"));
			}catch(Exception e){
				log.println("获取银监分支是否作为单独条线生效配置失败！！！");
				Config.YJ_BRANCH_BUSI_LINE = 0;
				e.printStackTrace();
			}
			try{// 银监法人分支报表的过滤设定规则
				Config.YJ_BRANCH_BUSI_LINE_RULE = prop.getProperty("yjBranchBusiLineRule");
			}catch(Exception e){
				log.println("获取银监法人分支报表的过滤设定规则配置失败！！！");
				Config.YJ_BRANCH_BUSI_LINE_RULE = "";
				e.printStackTrace();
			}
			Config.CODE_LIB = prop.getProperty("code.lib");//人行机构信息编码
			//是否在重新生成报表时保留数据痕迹修改
			Config.ISADDTRACE = new Boolean(prop.getProperty("is_add_trace"));
			Config.ISHAVEDELETE = new Boolean(prop.getProperty("is_have_delete"));
			//是否忽略表间校验 强制上报
			Config.ISFORCEREP = new Boolean(prop.getProperty("is_force_rep"));
			//人行报文导出数据精度
			Config.DOUBLEPERCISION = prop.getProperty("double.precision");
			try{///人行报文导出报文数据精度列表
				String[] DOUBLEPERCISION_STR = prop.getProperty("double.precision.templatelist").split(";");
				for(int i=0;i<DOUBLEPERCISION_STR.length;i++){
					String[] DOUBLEPERCISION_STR_SUB = DOUBLEPERCISION_STR[i].split(",");
				 Config.DOUBLEPERCISION_TEMPLATELIST.put(DOUBLEPERCISION_STR_SUB[0], DOUBLEPERCISION_STR_SUB[1]);
				}
			}catch(Exception e){
				log.println("获取人行报文导出报文数据精度列表配置失败！！！");
				Config.DOUBLEPERCISION_TEMPLATELIST = new HashMap();
				log.printStackTrace(e);
			}
			/***
			 * 是否显示数据痕迹旧页面
			 * 新的数据痕迹页面不支持多行复制粘贴
			 */
			Config.ISOLDHENJI = new Boolean(prop.getProperty("is_old_henji"));
			com.fitech.gznx.common.Config.HEAD_ORG_ID = prop.getProperty("head.org.id");
			com.fitech.gznx.common.Config.RH_DQCX = prop.getProperty("rh_dqcx");
		}catch(Exception e){
			throw new IllegalArgumentException("在CLASSPATH中未找到配置文件applicaion.properties");
		}

		try {
			Config.CAPORT = Integer.parseInt(caportByString);
		} catch (Exception e) {
			Config.CAPORT = 9000;
		}
		
		// 得到系统参数配置信息
		try{
			Integer relultBL=StrutsSysSetDelegate.loadSysParameter("BN_VALIDATE");
			if(relultBL!=null )
				Config.SYS_BN_VALIDATE = relultBL;
			Integer relultBJ=StrutsSysSetDelegate.loadSysParameter("BJ_VALIDATE");
			if(relultBJ!=null )
				Config.SYS_BJ_VALIDATE = relultBJ;
			Integer UP_VALIDATE_BN=StrutsSysSetDelegate.loadSysParameter("UP_VALIDATE_BN");
			if(UP_VALIDATE_BN!=null )
				Config.UP_VALIDATE_BN = UP_VALIDATE_BN;
			Integer UP_VALIDATE_BJ=StrutsSysSetDelegate.loadSysParameter("UP_VALIDATE_BJ");
			if(UP_VALIDATE_BJ!=null )
				Config.UP_VALIDATE_BJ = UP_VALIDATE_BJ;
			Integer IS_NEED_CHECK = StrutsSysSetDelegate.loadSysParameter("IS_NEED_CHECK");
			if(IS_NEED_CHECK!=null)
				Config.IS_NEED_CHECK = IS_NEED_CHECK;
			Integer ENCRYPT=StrutsSysSetDelegate.loadSysParameter("ENCRYPT");
			if(ENCRYPT!=null )
				Config.ENCRYPT = ENCRYPT;
		}catch(Exception e){
			throw new IllegalArgumentException("在CLASSPATH中未找到配置文件applicaion.properties");
		}
		initLogType();
		SequenceDB.batchInitSequense(Config.SEQUENSE_CONFIG);
		
		try {
			//根据配置的head.org.Id属性更新所有数据库中所有的总行机构id;
			//SequenceDB.searchOrgId(com.fitech.gznx.common.Config.HEAD_ORG_ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 系统运行终止
	 * 
	 * @return void
	 */
	public void destroy() {

	}

	/**
	 * 初始化日志类型常量
	 * 
	 * @reuturn void
	 */
	private void initLogType() {
		try {
			HashMap logTypes = StrutsLogTypeDelegate.find();

			if (logTypes != null) {
				Config.LOG_OPERATION = logTypes.get("LOG_OPERATION") == null ? null
						: (Integer) logTypes.get("LOG_OPERATION");
				Config.LOG_APPLICATION = logTypes.get("LOG_APPLICATION") == null ? null
						: (Integer) logTypes.get("LOG_APPLICATION");
				Config.LOG_ALARM = logTypes.get("LOG_ALARM") == null ? null
						: (Integer) logTypes.get("LOG_ALARM");
				Config.LOG_SYSTEM_GETFILES = logTypes
						.get("LOG_SYSTEM_GETFILES") == null ? null
						: (Integer) logTypes.get("LOG_SYSTEM_GETFILES");
				Config.LOG_SYSTEM_SAVEDATA = logTypes
						.get("LOG_SYSTEM_SAVEDATA") == null ? null
						: (Integer) logTypes.get("LOG_SYSTEM_SAVEDATA");
				Config.LOG_SYSTEM_CHECKOUTINSIDEREPORTS = logTypes
						.get("LOG_SYSTEM_CHECKOUTINSIDEREPORTS") == null ? null
						: (Integer) logTypes
								.get("LOG_SYSTEM_CHECKOUTINSIDEREPORTS");
				Config.LOG_SYSTEM_CREATESTORAGEXML = logTypes
						.get("LOG_SYSTEM_CREATESTORAGEXML") == null ? null
						: (Integer) logTypes.get("LOG_SYSTEM_CREATESTORAGEXML");				
				Config.LOG_SYSTEM_TEMPLATEPUT = logTypes
				.get("LOG_SYSTEM_TEMPLATEPUT") == null ? null
				: (Integer) logTypes.get("LOG_SYSTEM_TEMPLATEPUT");

				/*
				 * System.out.println("Config.LOG_OPERATION:" +
				 * Config.LOG_OPERATION);
				 * System.out.println("Config.LOG_APPLICATION:" +
				 * Config.LOG_APPLICATION);
				 * System.out.println("Config.LOG_ALARM:" + Config.LOG_ALARM);
				 * System.out.println("Config.LOG_SYSTEM_GETFILES:" +
				 * Config.LOG_SYSTEM_GETFILES);
				 * System.out.println("Config.LOG_SYSTEM_SAVEDATA:" +
				 * Config.LOG_SYSTEM_SAVEDATA);
				 * System.out.println("Config.LOG_SYSTEM_CHECKOUTINSIDEREPORTS:" +
				 * Config.LOG_SYSTEM_CHECKOUTINSIDEREPORTS);
				 * System.out.println("Config.LOG_SYSTEM_CREATESTORAGEXML:" +
				 * Config.LOG_SYSTEM_CREATESTORAGEXML);
				 */
			} else {
				log.println("初始化日志类型常量失败!");
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}
	}
}
