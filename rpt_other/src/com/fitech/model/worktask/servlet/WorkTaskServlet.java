package com.fitech.model.worktask.servlet;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fitech.framework.core.common.Config;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.service.ISequence;

/**
 */
public class WorkTaskServlet extends HttpServlet {

	/**
	 * ServletContext
	 */
	private ServletContext context = null;

	/**
	 * 
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * 
	 * @return void
	 * @exception IOException
	 *                ,ServletException
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = config.getServletContext();
		//获得应用的真实路径(解决war包中无法获得应用真实路径的问题)
		String appsRealPath = "";
		
		if(context.getRealPath("/")!=null && !context.getRealPath("/").trim().equals("")
				&& !context.getRealPath("/").equals("null")){
			appsRealPath = context.getRealPath("/");
			//this.log.println("======================may catch!");
		}else{
			//this.log.println("======================may not catch!");
			try{
				URL url = context.getResource("/");	
				appsRealPath = url.getPath();
				if(appsRealPath.trim().startsWith("/")){
					appsRealPath = appsRealPath.substring(1, appsRealPath.length());
				//	this.log.println(appsRealPath);
					String [] apps = appsRealPath.split("/");
					appsRealPath = "";
					for(int i=0;i<apps.length;i++){
						appsRealPath = !appsRealPath.equals("")
							? appsRealPath + File.separator + apps[i]:apps[i];
					}
					if(!appsRealPath.endsWith(File.separator))
						appsRealPath += File.separator;
					appsRealPath = File.separator + appsRealPath;
				//	this.log.println("====================" + appsRealPath);
					//appsRealPath = appsRealPath.replaceAll("/", "\\\\");
				}
			}catch(MalformedURLException e){
				//log.printStackTrace(e);
				e.printStackTrace();
			}			
		}
		
	//	Config.WEBROOTPATH = context.getRealPath("/");
		Config.WEBROOTPATH = appsRealPath+Config.FILESEPARATOR;
		Properties prop = new Properties();
		try {
			prop.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
			try {
				//填报处理类型
				WorkTaskConfig.WORK_TASK_COND_TYPE_ID =prop.getProperty("work_task_conm_type_id");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				WorkTaskConfig.WORK_TASK_COND_TYPE_ID ="tb";
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			WorkTaskConfig.SEQUENSE_CONFIG= prop.getProperty("sequense_config");//获取sequence配置
		} catch (Exception e) {
			WorkTaskConfig.SEQUENSE_CONFIG = "";
		}
		try {
			//公告信息首页是否显示
			Config.INCLUDE_COMMONINFO_FLAG = new Integer(prop.getProperty("include_commoninfo_flag"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			Config.INCLUDE_COMMONINFO_FLAG = 0;
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			Config.INCLUDE_COMMONINFO_FLAG = 0;
			e.printStackTrace();
		}
		try{// 登陆用户验证
			WorkTaskConfig.loginWebServiceURL = prop.getProperty("login_webservice_url");
		}catch(Exception e){
			WorkTaskConfig.loginWebServiceURL = "";
			e.printStackTrace();
		}
		try{// 报送时间单位
			WorkTaskConfig.REPORT_TIME_UNIT = prop.getProperty("report_time_unit");
		}catch(Exception e){
			WorkTaskConfig.REPORT_TIME_UNIT = "day";
			e.printStackTrace();
		}
		try{
			WorkTaskConfig.MANUAL_SPLIT_TASK_FLAG = Integer.parseInt(prop.getProperty("manual_split_task_flag"));
		}catch(Exception e){
			WorkTaskConfig.MANUAL_SPLIT_TASK_FLAG = 0;
			e.printStackTrace();
		}
		try{
			WorkTaskConfig.SYSN_LOCK_TASK_FLAG = Integer.parseInt(prop.getProperty("sysn_lock_task_flag"));
		}catch(Exception e){
			WorkTaskConfig.SYSN_LOCK_TASK_FLAG = 0;
			e.printStackTrace();
		}
		
		try{
			WorkTaskConfig.DB_SERVER_TYPE =prop.getProperty("db.server.type");
		}catch(Exception e){
			WorkTaskConfig.DB_SERVER_TYPE = "oracle";
			e.printStackTrace();
		}
		
		WebApplicationContext wa = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		ISequence sequimpl = (ISequence)wa.getBean("sequenceServiceImpl");
		sequimpl.batchInitSequense(WorkTaskConfig.SEQUENSE_CONFIG);
	}

	public void destroy() {
		this.destroy();
	}
}
