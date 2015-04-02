package com.cbrc.smis.servlet;

import java.util.Timer;

import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.service.PreViewReportDelegate;
/**
 * 同步外网数据表操作类
 * 
 * @author rds
 * @date 2006-02-04
 */
public class PreViewReportServlet extends HttpServlet {
	private FitechException log=new FitechException(PreViewReportServlet.class);
	
	Timer timer=new Timer();
	private int ineratorTime ;
	private String reportFlag ;
	
	public void init(ServletConfig config) throws ServletException{
		reportFlag = config.getInitParameter("reportFlag");
		ineratorTime = Integer.valueOf(config.getInitParameter("ineratorTime"));
		ViewReportTimerTask timerTask = new ViewReportTimerTask(reportFlag);
		System.out.println("************Start***********");
		timer.schedule(timerTask,0,ineratorTime*60*1000);
		System.out.println("************End*************");
	}
		
	/**
	 * 消毁
	 * 
	 * @return void
	 */
	public void destroy() {
		if(timer!=null){
			timer.cancel();
		}
	}
}

class ViewReportTimerTask extends TimerTask{
	private FitechException log = new FitechException(PreViewReportServlet.class);
	private String reportFlag ;
	static boolean ISWROKING = false;
	public ViewReportTimerTask(String reportFlag) {
		super();
		this.reportFlag = reportFlag;
	}

	public void run(){
		if(!ISWROKING){
			ISWROKING = true;
			try {
				String term  = PreViewReportDelegate.getParamTerm(reportFlag);
				if(term !=null&&!term.equals(""))
					PreViewReportDelegate.createReportDate(term, reportFlag+"", true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			ISWROKING = false;
		}
	}
}