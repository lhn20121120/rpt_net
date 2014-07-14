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
 * д��־������
 * 
 * @author rds
 * @serialData 2005-12-6
 */
public class FitechLog {
	private static FitechException log=new FitechException(FitechLog.class);

	/**
	 * д��־
	 * 
	 * @param logTypeId Integer ��־����
	 * @param operation String ��������
	 * @param request HttpServletRequest 
	 * @return void
	 */
	public static void writeLog(Integer logTypeId,String operation,HttpServletRequest request){
		writeLog(logTypeId,operation,"",request);
	}
	
	/**
	 * д��־
	 * 
	 * @param logTypeId Integer ��־����
	 * @param operation String ��������
	 * @param memo String ��ע
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
        String operation = "�û�[��¼��:" + operator.getUserName() + ",����:" + operator.getOperatorName() + "]";
        String repMes = orgName + "�µ�" + reportInform.getYear()+ "��" + reportInform.getTerm() + "��";
        if(reportFlag.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			repMes+=getDataRange(reportInform.getDataRangeId().intValue());
		}
        repMes+="����[" + reportInform.getChildRepId()+ "]";
        operation = operation + "��" + repMes + mes + "��";
	    FitechLog.writeLog(new Integer(12),operator.getUserName(),operation);
	}
	
	/***
	 * �������ݺۼ���־
	 * @param type ���� 1.�������ݺۼ���¼��־  2.ɾ�����ݺۼ���¼��־
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
		String operation =  "�û�[��¼��:" + operator.getUserName() + ",����:" + operator.getOperatorName() + "]";
		String repMes = orgName + "�µ�" + reportInform.getYear()+ "��" + reportInform.getTerm() + "��" ;
		if(reportFlag.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			repMes+=getDataRange(reportInform.getDataRangeId().intValue());
		}
		repMes+= "����[" + reportInform.getChildRepId()+ "]"+"�ĵ�Ԫ��["+trace.getCellName()+"]";
		switch (type) {
		case 1:
			if(isAccess){
				repMes +="�����ɹ�,"+"ԭֵΪ:"+trace.getOriginalData()+",����ֵΪ:"+trace.getChangeData()+","+"�������Ϊ:"+trace.getFinalData();
			}else
				repMes +="����ʧ��";
			break;
		case 2:
			if(isAccess){
				repMes +="�ۼ���Ϣɾ���ɹ�";
			}else
				repMes +="�ۼ���Ϣɾ��ʧ��";
			break;
		}
		
		
        operation = operation + "��"+repMes+ "��";
//        System.out.println("operation="+operation);
        FitechLog.writeLog(new Integer(12),operator.getUserName(),operation);
	}
	private static String getDataRange(int dataRangeId){
		String result = "";
		switch(dataRangeId){
		case 1:{result = "����";break;}
		case 2:{result = "����";break;}
		case 3:{result = "����";break;}
		}
		return result;
	}
	/**
	
	/**
	 * д��־
	 * 
	 * @param logTypeId Integer ��־����
	 * @param userName String ����Ա
	 * @param operation String ��������
	 * @return void
	 */
	public static void writeLog(Integer logTypeId,String userName,String operation){
		writeLog(logTypeId,userName,operation,"");
	}
	
	/**
	 * д��־
	 * 
	 * @param logTypeId Integer ��־����
	 * @param userName String ����Ա
	 * @param operation String ��������
	 * @param memo String ��ע
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
	 * ʵ������־ActionForm����
	 * 
	 * @param logTypeId Integer ��־����
	 * @param userName String ����Ա
	 * @param operation String ��������
	 * @param memo String ��ע
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
	 * ʵ������־ActionForm����
	 * 
	 * @param logTypeId Integer ��־����
	 * @param userName String ����Ա
	 * @param operation String ��������
	 * @return LogInForm
	 */
	private static LogInForm getLogInForm(Integer logTypeId,String userName,String operation){
		return getLogInForm(logTypeId,userName,operation,"");
	}
}
