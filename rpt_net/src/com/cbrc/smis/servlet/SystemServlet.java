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
 * ϵͳ���еĳ�ʼ��������
 */
public class SystemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FitechException log = new FitechException(SystemServlet.class);

	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	/**
	 * ϵͳ����
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

		
		//���Ӧ�õ���ʵ·��(���war�����޷����Ӧ����ʵ·��������)
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
		
		// ����ϵͳӦ�õĸ�·��
		//Config.WEBROOTPATH = context.getRealPath("/") + File.separator;
		Config.WEBROOTPATH = appsRealPath.endsWith(Config.FILESEPARATOR) ? appsRealPath : appsRealPath + Config.FILESEPARATOR;
		Config.RAQ_TEMPLATE_PATH= appsRealPath.endsWith(Config.FILESEPARATOR) ? appsRealPath : appsRealPath + Config.FILESEPARATOR;
		// ������ʱ�ļ���ŵ�λ��
		Config.TEMP_DIR = Config.WEBROOTPATH + "tmp" + Config.FILESEPARATOR;
		//���ݲ�ѯģ��ģ��excel�ļ���ʹ�����ݲ�ѯģ�������excel�ļ������ô�ģ��xls�ļ�
		Config.XLSFILEPATH = Config.WEBROOTPATH + "temp" + Config.FILESEPARATOR + "result.xls";
		//�ۼ���ѯģ��excel�ļ���ʹ�úۼ���ѯģ������excel�ļ������ô�ģ��xls�ļ�
		Config.TRACEFILEPATH = Config.WEBROOTPATH + "temp" + Config.FILESEPARATOR + "trace_data_list.xls";
		// PDF����ģ���ļ���ŵ�����·��
		Config.PDF_TEMPLATE_PATH = Config.WEBROOTPATH + "template"
				+ Config.FILESEPARATOR + "pdf" + Config.FILESEPARATOR;

		// ���������ʵ����������ļ�������·�����г�ʼ��
		Config.CONFIGBYIMPLPATH = Config.WEBROOTPATH + "WEB-INF"
				+ Config.FILESEPARATOR + "classes" + Config.FILESEPARATOR
				+ "com" + Config.FILESEPARATOR + "cbrc" + Config.FILESEPARATOR
				+ "smis" + Config.FILESEPARATOR + "system"
				+ Config.FILESEPARATOR + "cb" + Config.FILESEPARATOR
				+ "configInputDataImpl.properties";

		// ���������ʱ���������ļ�������·�����г�ʼ��
		Config.INPUTDATATIMEPREPADDR = Config.WEBROOTPATH + "WEB-INF"
				+ Config.FILESEPARATOR + "classes" + Config.FILESEPARATOR
				+ "com" + Config.FILESEPARATOR + "cbrc" + Config.FILESEPARATOR
				+ "smis" + Config.FILESEPARATOR + "system"
				+ Config.FILESEPARATOR + "cb" + Config.FILESEPARATOR
				+ "InputDataTimeConfig.properties";

		// ���ô�������ñ�������ZIP�ļ��Ĵ��λ��
		Config.ADDRESSZIP = config.getInitParameter("ReportDataPath");
		// ���ô�������������ļ���ŵ�λ��

		// �������ɲֿ�ӿ�XML�ļ���ŵ�λ��
		Config.XMLData_PATH = config.getInitParameter("XMLPath");

		// ��Ϣ�����ļ����ݵ�Ŀ¼
		Config.BAK_INFO_FILES_OUTPATH = config
				.getInitParameter("BAK_INFO_FILES_OUTPATH");

		// ��Ϣ�ϴ��ļ����ݵ�Ŀ¼
		Config.BAK_INFO_FILES_UPPATH = config
				.getInitParameter("BAK_INFO_FILES_UPPATH");

		// ��ʼ��CA�����IP��ַ
		Config.CAIP = config.getInitParameter("CAIP");

		// ��ʼ��CA����Ķ˿ں�
		String caportByString = config.getInitParameter("CAPORT");
		
		// ��ʼ�������ļ��ķ�ʽ
		Config.DATATYPE = config.getInitParameter("DATATYPE");
		
		/**��ʼ����ʿ����*/
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
				//�Ƿ�����EASTϵͳ
				Config.PORTAL = new Boolean(prop.getProperty("portal"));
				//EASTϵͳ���ݿ����Ӳ���
				Config.PORTALDRIVER = prop.getProperty("portal_dirver");
				Config.PORTALURL = prop.getProperty("portal_url");
				Config.PORTALUSERNAME = prop.getProperty("portal_username");
				Config.PORTALPWD = prop.getProperty("portal_pwd");
			} catch (Exception e) {
				e.printStackTrace();
				Config.PORTAL = false;
				//EASTϵͳ���ݿ����Ӳ���
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
			// add by ������
			try {
				//�Ƿ�ϲ���ɫ���û������
				Config.ROLE_GROUP_UNION_FLAG=new Integer(prop.getProperty("role_group_union_flag"));
			} catch (Exception e) {
				// TODO: handle exception
				Config.ROLE_GROUP_UNION_FLAG=0;
			}
			try {
				//����ģʽ
				Config.ETL_TASK_CHECK=new Integer(prop.getProperty("system_schema_flag"));
			} catch (Exception e) {
				// TODO: handle exception
				Config.ETL_TASK_CHECK=0;
			}
			try {
				//����ģʽ
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
				Config.SEQUENSE_CONFIG = prop.getProperty("sequense_config");//��ȡsequence����
			} catch (Exception e) {
				log.println("��ȡsequence����ʧ�ܣ�����");
				log.printStackTrace(e);
				Config.SEQUENSE_CONFIG = "";
			}
			try {
				Config.YJ_EXP_REP_FLAG = Integer.parseInt(prop.getProperty("yj_exp_rep_flag"));//��ȡ���ർ��״̬����
			} catch (Exception e) {
				log.println("��ȡ���ർ��״̬����ʧ�ܣ�����");
				log.printStackTrace(e);
				Config.YJ_EXP_REP_FLAG = 1;
			}
			try {
				Config.RH_EXP_REP_FLAG = Integer.parseInt(prop.getProperty("rh_exp_rep_flag"));//��ȡ���е���״̬����
			} catch (Exception e) {
				log.println("��ȡ���е���״̬����ʧ�ܣ�����");
				log.printStackTrace(e);
				Config.RH_EXP_REP_FLAG = 1;
			}
			try{// ����ʱ�䵥λ
				Config.REPORT_TIME_UNIT = prop.getProperty("report_time_unit");
			}catch(Exception e){
				log.println("��ȡ����ʱ�䵥λ����ʧ�ܣ�����");
				Config.REPORT_TIME_UNIT = "day";
				e.printStackTrace();
			}
			try{// �����֧�Ƿ���Ϊ����������Ч�ı�־
				Config.YJ_BRANCH_BUSI_LINE = Integer.parseInt(prop.getProperty("yjBranchBusiLine"));
			}catch(Exception e){
				log.println("��ȡ�����֧�Ƿ���Ϊ����������Ч����ʧ�ܣ�����");
				Config.YJ_BRANCH_BUSI_LINE = 0;
				e.printStackTrace();
			}
			try{// ���෨�˷�֧����Ĺ����趨����
				Config.YJ_BRANCH_BUSI_LINE_RULE = prop.getProperty("yjBranchBusiLineRule");
			}catch(Exception e){
				log.println("��ȡ���෨�˷�֧����Ĺ����趨��������ʧ�ܣ�����");
				Config.YJ_BRANCH_BUSI_LINE_RULE = "";
				e.printStackTrace();
			}
			Config.CODE_LIB = prop.getProperty("code.lib");//���л�����Ϣ����
			//�Ƿ����������ɱ���ʱ�������ݺۼ��޸�
			Config.ISADDTRACE = new Boolean(prop.getProperty("is_add_trace"));
			Config.ISHAVEDELETE = new Boolean(prop.getProperty("is_have_delete"));
			//�Ƿ���Ա��У�� ǿ���ϱ�
			Config.ISFORCEREP = new Boolean(prop.getProperty("is_force_rep"));
			//���б��ĵ������ݾ���
			Config.DOUBLEPERCISION = prop.getProperty("double.precision");
			try{///���б��ĵ����������ݾ����б�
				String[] DOUBLEPERCISION_STR = prop.getProperty("double.precision.templatelist").split(";");
				for(int i=0;i<DOUBLEPERCISION_STR.length;i++){
					String[] DOUBLEPERCISION_STR_SUB = DOUBLEPERCISION_STR[i].split(",");
				 Config.DOUBLEPERCISION_TEMPLATELIST.put(DOUBLEPERCISION_STR_SUB[0], DOUBLEPERCISION_STR_SUB[1]);
				}
			}catch(Exception e){
				log.println("��ȡ���б��ĵ����������ݾ����б�����ʧ�ܣ�����");
				Config.DOUBLEPERCISION_TEMPLATELIST = new HashMap();
				log.printStackTrace(e);
			}
			/***
			 * �Ƿ���ʾ���ݺۼ���ҳ��
			 * �µ����ݺۼ�ҳ�治֧�ֶ��и���ճ��
			 */
			Config.ISOLDHENJI = new Boolean(prop.getProperty("is_old_henji"));
			com.fitech.gznx.common.Config.HEAD_ORG_ID = prop.getProperty("head.org.id");
			com.fitech.gznx.common.Config.RH_DQCX = prop.getProperty("rh_dqcx");
		}catch(Exception e){
			throw new IllegalArgumentException("��CLASSPATH��δ�ҵ������ļ�applicaion.properties");
		}

		try {
			Config.CAPORT = Integer.parseInt(caportByString);
		} catch (Exception e) {
			Config.CAPORT = 9000;
		}
		
		// �õ�ϵͳ����������Ϣ
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
			throw new IllegalArgumentException("��CLASSPATH��δ�ҵ������ļ�applicaion.properties");
		}
		initLogType();
		SequenceDB.batchInitSequense(Config.SEQUENSE_CONFIG);
		
		try {
			//�������õ�head.org.Id���Ը����������ݿ������е����л���id;
			//SequenceDB.searchOrgId(com.fitech.gznx.common.Config.HEAD_ORG_ID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ϵͳ������ֹ
	 * 
	 * @return void
	 */
	public void destroy() {

	}

	/**
	 * ��ʼ����־���ͳ���
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
				log.println("��ʼ����־���ͳ���ʧ��!");
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}
	}
}
