package com.cbrc.smis.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cbrc.org.form.AFDataTraceForm;
import com.cbrc.smis.adapter.StrutsLogInDelegate;
import com.cbrc.smis.adapter.StrutsOrgDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.entity.AFDataTrace;
import com.cbrc.smis.form.LogInForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.service.AFReportDelegate;
/**
 * 写日志工具类
 * 
 * @author rds
 * @serialData 2005-12-6
 */
public class FitechLog {
	private static FitechException log=new FitechException(FitechLog.class);

	/**
	 * 写日志
	 * 
	 * @param logTypeId Integer 日志类型
	 * @param operation String 操作内容
	 * @param request HttpServletRequest 
	 * @return void
	 */
	public static void writeLog(Integer logTypeId,String operation,HttpServletRequest request){
		writeLog(logTypeId,operation,"",request);
	}
	
	/**
	 * 写日志
	 * 
	 * @param logTypeId Integer 日志类型
	 * @param operation String 操作内容
	 * @param memo String 备注
	 * @param request HttpServletRequest 
	 * @return void
	 */
	public static void writeLog(Integer logTypeId,String operation,String memo,HttpServletRequest request){
		HttpSession session=request.getSession();
		
		String userName="";
		
		if(session.getAttribute(Config.OPERATOR_SESSION_NAME)!=null){
			Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_NAME);
			userName=operator.getOperatorName();
		}
			
		writeLog(logTypeId,userName,operation,memo);
	}
	public static void writeRepLog(Integer logTypeId,String mes,HttpServletRequest request,String repInId ,String reportFlag){
		Operator operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);	
		ReportInForm reportInform = null;
		if(reportFlag.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			reportInform = StrutsReportInDelegate.getReportIn(new Integer(repInId));
		}else {
			reportInform = AFReportDelegate.getReportIn(Integer.valueOf(repInId));
		}
        String orgName = StrutsOrgDelegate.getOrgName(reportInform.getOrgId());
        String operation = "用户[登录名:" + operator.getUserName() + ",姓名:" + operator.getOperatorName() + "]";
        String repMes = orgName + "下的" + reportInform.getYear()+ "年" + reportInform.getTerm() + "期";
        if(reportFlag.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			repMes+=getDataRange(reportInform.getDataRangeId().intValue());
		}
        repMes+="报表[" + reportInform.getChildRepId()+ "]";
        operation = operation + "对" + repMes + mes + "！";
	    FitechLog.writeLog(new Integer(12),operator.getUserName(),operation);
	}
	
	/***
	 * 保存数据痕迹日志
	 * @param type 类型 1.增加数据痕迹记录日志  2.删除数据痕迹记录日志
	 * @param request
	 * @param traceForm
	 * @param isAccess
	 */
	public static void writeTraceLog(Integer type,HttpServletRequest request,AFDataTrace trace,boolean isAccess ,String reportFlag){
		Operator operator = (Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		System.out.println(trace.getRepInId());
		ReportInForm reportInform = null;
		if(reportFlag.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			reportInform = StrutsReportInDelegate.getReportIn(new Integer(trace.getRepInId()));
		}else {
			reportInform = AFReportDelegate.getReportIn(Integer.valueOf(trace.getRepInId()));
		}
		String orgName = StrutsOrgDelegate.getOrgName(reportInform.getOrgId());
		String operation =  "用户[登录名:" + operator.getUserName() + ",姓名:" + operator.getOperatorName() + "]";
		String repMes = orgName + "下的" + reportInform.getYear()+ "年" + reportInform.getTerm() + "期" ;
		if(reportFlag.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			repMes+=getDataRange(reportInform.getDataRangeId().intValue());
		}
		repMes+= "报表[" + reportInform.getChildRepId()+ "]"+"的单元格["+trace.getCellName()+"]";
		switch (type) {
		case 1:
			if(isAccess){
				repMes +="调整成功,"+"原值为:"+trace.getOriginalData()+",调整值为:"+trace.getChangeData()+","+"调整结果为:"+trace.getFinalData();
			}else
				repMes +="调整失败";
			break;
		case 2:
			if(isAccess){
				repMes +="痕迹信息删除成功";
			}else
				repMes +="痕迹信息删除失败";
			break;
		}
		
		
        operation = operation + "对"+repMes+ "！";
//        System.out.println("operation="+operation);
        FitechLog.writeLog(new Integer(12),operator.getUserName(),operation);
	}
	private static String getDataRange(int dataRangeId){
		String result = "";
		switch(dataRangeId){
		case 1:{result = "境内";break;}
		case 2:{result = "法人";break;}
		case 3:{result = "并表";break;}
		}
		return result;
	}
	/**
	
	/**
	 * 写日志
	 * 
	 * @param logTypeId Integer 日志类型
	 * @param userName String 操作员
	 * @param operation String 操作内容
	 * @return void
	 */
	public static void writeLog(Integer logTypeId,String userName,String operation){
		writeLog(logTypeId,userName,operation,"");
	}
	
	/**
	 * 写日志
	 * 
	 * @param logTypeId Integer 日志类型
	 * @param userName String 操作员
	 * @param operation String 操作内容
	 * @param memo String 备注
	 * @param memo
	 */
	public static void writeLog(Integer logTypeId,String userName,String operation,String memo){
		try{
			StrutsLogInDelegate.create(getLogInForm(logTypeId,userName,operation));
		}catch(Exception e){
			log.printStackTrace(e);
		}
	}
	
	/**
	 * 实例化日志ActionForm对象
	 * 
	 * @param logTypeId Integer 日志类型
	 * @param userName String 操作员
	 * @param operation String 操作内容
	 * @param memo String 备注
	 * @return LogInForm
	 */
	private static LogInForm getLogInForm(Integer logTypeId,String userName,String operation,String memo){
		LogInForm logInForm=new LogInForm();
		
		logInForm.setLogTypeId(logTypeId);
		logInForm.setUserName(userName);
		logInForm.setOperation(operation);
		logInForm.setLogTime(new Date());
		logInForm.setMemo(memo);
		
		return logInForm;
	}
	
	/**
	 * 实例化日志ActionForm对象
	 * 
	 * @param logTypeId Integer 日志类型
	 * @param userName String 操作员
	 * @param operation String 操作内容
	 * @return LogInForm
	 */
	private static LogInForm getLogInForm(Integer logTypeId,String userName,String operation){
		return getLogInForm(logTypeId,userName,operation,"");
	}
}
