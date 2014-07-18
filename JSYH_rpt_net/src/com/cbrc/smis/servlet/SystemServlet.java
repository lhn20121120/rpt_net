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
import java.util.Timer;

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

/**
 * @author rds
 * @date 2005-11-22
 * 
 * 系统锟斤拷锟叫的筹拷始锟斤拷锟斤拷锟斤拷锟斤拷
 */
public class SystemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FitechException log = new FitechException(SystemServlet.class);

	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	/**
	 * 系统锟斤拷锟斤拷
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

		
		//锟斤拷锟接︼拷玫锟斤拷锟绞德凤拷锟�锟斤拷锟絯ar锟斤拷锟斤拷锟睫凤拷锟斤拷锟接︼拷锟斤拷锟绞德凤拷锟斤拷锟斤拷锟斤拷锟�
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
		
		// 锟斤拷锟斤拷系统应锟矫的革拷路锟斤拷
		//Config.WEBROOTPATH = context.getRealPath("/") + File.separator;
		Config.WEBROOTPATH = appsRealPath.endsWith(Config.FILESEPARATOR) ? appsRealPath : appsRealPath + Config.FILESEPARATOR;
		Config.RAQ_TEMPLATE_PATH= appsRealPath.endsWith(Config.FILESEPARATOR) ? appsRealPath : appsRealPath + Config.FILESEPARATOR;
		// 锟斤拷锟斤拷锟斤拷时锟侥硷拷锟斤拷诺锟轿伙拷锟�
		Config.TEMP_DIR = Config.WEBROOTPATH + "tmp" + Config.FILESEPARATOR;
		//锟斤拷莶锟窖ｏ拷锟侥ｏ拷锟絜xcel锟侥硷拷锟斤拷使锟斤拷锟斤拷莶锟窖ｏ拷锟斤拷锟斤拷锟絜xcel锟侥硷拷锟斤拷锟斤拷锟矫达拷模锟斤拷xls锟侥硷拷
		Config.XLSFILEPATH = Config.WEBROOTPATH + "temp" + Config.FILESEPARATOR + "result.xls";
		//锟桔硷拷锟斤拷询模锟斤拷excel锟侥硷拷锟斤拷使锟矫痕硷拷锟斤拷询模锟斤拷锟斤拷锟絜xcel锟侥硷拷锟斤拷锟斤拷锟矫达拷模锟斤拷xls锟侥硷拷
		Config.TRACEFILEPATH = Config.WEBROOTPATH + "temp" + Config.FILESEPARATOR + "trace_data_list.xls";
		// PDF锟斤拷锟斤拷模锟斤拷锟侥硷拷锟斤拷诺锟斤拷锟斤拷锟铰凤拷锟�
		Config.PDF_TEMPLATE_PATH = Config.WEBROOTPATH + "template"
				+ Config.FILESEPARATOR + "pdf" + Config.FILESEPARATOR;

		// 锟斤拷锟斤拷锟斤拷锟斤拷实锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷募锟斤拷锟斤拷锟斤拷锟铰凤拷锟斤拷锟斤拷谐锟绞硷拷锟�
		Config.CONFIGBYIMPLPATH = Config.WEBROOTPATH + "WEB-INF"
				+ Config.FILESEPARATOR + "classes" + Config.FILESEPARATOR
				+ "com" + Config.FILESEPARATOR + "cbrc" + Config.FILESEPARATOR
				+ "smis" + Config.FILESEPARATOR + "system"
				+ Config.FILESEPARATOR + "cb" + Config.FILESEPARATOR
				+ "configInputDataImpl.properties";

		// 锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷锟斤拷锟斤拷路锟斤拷锟斤拷锟叫筹拷始锟斤拷
		Config.INPUTDATATIMEPREPADDR = Config.WEBROOTPATH + "WEB-INF"
				+ Config.FILESEPARATOR + "classes" + Config.FILESEPARATOR
				+ "com" + Config.FILESEPARATOR + "cbrc" + Config.FILESEPARATOR
				+ "smis" + Config.FILESEPARATOR + "system"
				+ Config.FILESEPARATOR + "cb" + Config.FILESEPARATOR
				+ "InputDataTimeConfig.properties";

		// 锟斤拷锟矫达拷锟斤拷锟斤拷锟矫憋拷锟斤拷锟斤拷锟絑IP锟侥硷拷锟侥达拷锟轿伙拷锟�
		Config.ADDRESSZIP = config.getInitParameter("ReportDataPath");
		// 锟斤拷锟矫达拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥硷拷锟斤拷诺锟轿伙拷锟�

		// 锟斤拷锟斤拷锟斤拷刹挚锟接匡拷XML锟侥硷拷锟斤拷诺锟轿伙拷锟�
		Config.XMLData_PATH = config.getInitParameter("XMLPath");

		// 锟斤拷息锟斤拷锟斤拷锟侥硷拷锟斤拷锟捷碉拷目录
		Config.BAK_INFO_FILES_OUTPATH = config
				.getInitParameter("BAK_INFO_FILES_OUTPATH");

		// 锟斤拷息锟较达拷锟侥硷拷锟斤拷锟捷碉拷目录
		Config.BAK_INFO_FILES_UPPATH = config
				.getInitParameter("BAK_INFO_FILES_UPPATH");

		// 锟斤拷始锟斤拷CA锟斤拷锟斤拷锟絀P锟斤拷址
		Config.CAIP = config.getInitParameter("CAIP");

		// 锟斤拷始锟斤拷CA锟斤拷锟斤拷亩丝诤锟�
		String caportByString = config.getInitParameter("CAPORT");
		
		// 锟斤拷始锟斤拷锟斤拷锟斤拷募锟斤拷姆锟绞�
		Config.DATATYPE = config.getInitParameter("DATATYPE");
		
		/**锟斤拷始锟斤拷锟斤拷士锟斤拷锟斤拷*/
		File file = new File(Config.WEBROOTPATH+"remindTips.txt");
		
		if(!config.getInitParameter("RAQ_TEMPLATE_PATH").trim().equals("")){
		
			Config.RAQ_TEMPLATE_PATH = config.getInitParameter("RAQ_TEMPLATE_PATH") + Config.FILESEPARATOR;
//			Config.RAQ_INIT_TEMP_PATH = Config.RAQ_TEMPLATE_PATH + "templateFiles" + Config.FILESEPARATOR + "initRaq" + Config.FILESEPARATOR;
//			File raqInitTempPath = new File(Config.RAQ_INIT_TEMP_PATH);
//			if(!raqInitTempPath.exists())
//				raqInitTempPath.mkdir();
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
			
			  //add by hu
            context.setAttribute("IS_INTEGRATE_PORTAL", Boolean.valueOf(prop.getProperty("new_portal")));
            context.setAttribute("INTEGRATE_PORTAL_DESC",prop.getProperty("integrate.portal.desc"));
            Config.IS_INTEGRATE_PORTAL = Boolean.valueOf(prop.getProperty("new_portal"));
            Config.NEW_PORTAL_URL = prop.getProperty("new_portal_url");
            try{
                Config.PORATLSYNAIMPL = prop.getProperty("portal_syna_impl");
            }catch(Exception e){
                Config.PORATLSYNAIMPL = "com.cbrc.auth.adapter";
            }
//			try{
//				Config.ADD_OLD_COLLECT = new Integer((prop.getProperty("add_old_collect")));
//			}catch(Exception e){
//				Config.ADD_OLD_COLLECT = new Integer(0);
//			}
//			try{
//				Config.RH_DESC_CONTE = prop.getProperty("rh_desc_conte");
//				Config.RH_FORMAT_END =prop.getProperty("rh_format_end");
//			}catch(Exception e){
//				Config.RH_DESC_CONTE = "outerId,orgName";
//				Config.RH_FORMAT_END = "d";
//			}
			Config.CODE_LIB = prop.getProperty("code.lib");//锟斤拷锟叫伙拷锟斤拷息锟斤拷锟斤拷
			//锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷杀锟斤拷锟绞憋拷锟斤拷锟斤拷锟捷痕硷拷锟睫革拷
			Config.ISADDTRACE = new Boolean(prop.getProperty("is_add_trace"));
			Config.ISHAVEDELETE = new Boolean(prop.getProperty("is_have_delete"));
			//锟角凤拷锟斤拷员锟斤拷校锟斤拷 强锟斤拷锟较憋拷
			Config.ISFORCEREP = new Boolean(prop.getProperty("is_force_rep"));
			//锟斤拷锟叫憋拷锟侥碉拷锟斤拷锟斤拷菥锟斤拷锟�
			Config.DOUBLEPERCISION = prop.getProperty("double.precision");
			/***
			 * 锟角凤拷锟斤拷示锟斤拷莺奂锟斤拷锟揭筹拷锟�
			 * 锟铰碉拷锟斤拷莺奂锟揭筹拷娌恢э拷侄锟斤拷懈锟斤拷锟秸筹拷锟�
			 */
			Config.ISOLDHENJI = new Boolean(prop.getProperty("is_old_henji"));
			com.fitech.gznx.common.Config.HEAD_ORG_ID = prop.getProperty("head.org.id");
			
		}catch(Exception e){
			throw new IllegalArgumentException("锟斤拷CLASSPATH锟斤拷未锟揭碉拷锟斤拷锟斤拷锟侥硷拷applicaion.properties");
		}

		try {
			Config.CAPORT = Integer.parseInt(caportByString);
		} catch (Exception e) {
			Config.CAPORT = 9000;
		}
		
		// 锟矫碉拷系统锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷息
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
			throw new IllegalArgumentException("锟斤拷CLASSPATH锟斤拷未锟揭碉拷锟斤拷锟斤拷锟侥硷拷applicaion.properties");
		}
		initLogType();
		//锟斤拷锟斤拷锟秸憋拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
		new Timer().schedule(new com.cbrc.smis.util.DayReportTask(), 1000, 5*60*1000);//锟斤拷锟斤拷锟斤拷锟绞硷拷锟斤拷锟斤拷1锟斤拷锟秸匡拷始执锟斤拷锟斤拷锟斤拷每5锟斤拷锟斤拷锟斤拷询一锟斤拷
	}

	/**
	 * 系统锟斤拷锟斤拷锟斤拷止
	 * 
	 * @return void
	 */
	public void destroy() {

	}

	/**
	 * 锟斤拷始锟斤拷锟斤拷志锟斤拷锟酵筹拷锟斤拷
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
				log.println("锟斤拷始锟斤拷锟斤拷志锟斤拷锟酵筹拷锟斤拷失锟斤拷!");
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}
	}
}
