package com.fitech.net.common;

import java.util.Calendar;
import java.util.List;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.fitech.net.config.Config;

public class InfoUtil {

	public static String getWBSL(Operator operator){
		String result = null;
		
		try{
			Calendar calendar = Calendar.getInstance();  
			calendar.add(Calendar.MONTH, -1);
			ReportInForm reportInForm = new ReportInForm();
			reportInForm.setOrgId(operator.getOrgId());
			if(reportInForm.getYear() == null || reportInForm.getYear().equals(""))			   
				reportInForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
			if(reportInForm.getSetDate() == null || reportInForm.getSetDate().equals(""))			   
				reportInForm.setSetDate(String.valueOf(calendar.get(Calendar.MONTH)+1));
			if(reportInForm.getTerm() == null || reportInForm.getSetDate().equals(""))
				reportInForm.setTerm(new Integer(calendar.get(Calendar.MONTH)+1));
			
			int ybsl = 0,wshsl = 0,hgsl = 0;  //Ӧ����������	
			/* ���δͨ���ı������� */
			int wtgsl = 0;

			List list =new StrutsMRepRangeDelegate().selectYBSL(reportInForm,operator);
		//Ӧ���ӱ���ID
			StringBuffer ybrepIds=new StringBuffer("");
			if(list != null && list.size() > 0) {
				for(int i=0;i<list.size();i++){
					Aditing aditing =(Aditing)list.get(i);
					ybrepIds.append(ybrepIds.toString().equals("")?"'"+aditing.get_childRepId()+"'":",'"+aditing.get_childRepId()+"'");
				}
				ybsl = list.size();

			}
			//δ�ӱ������ǰ
			if(ybsl>0)wshsl = StrutsReportInDelegate.getYBRecordCount(reportInForm.getYear(),reportInForm.getTerm(),reportInForm.getOrgId(),Config.CHECK_FLAG_UNCHECK,ybrepIds.toString());  //δ��ı�������
			if(ybsl>0)hgsl = StrutsReportInDelegate.getYBRecordCount(reportInForm.getYear(),reportInForm.getTerm(),reportInForm.getOrgId(),Config.CHECK_FLAG_PASS,ybrepIds.toString());   //���ͨ���ı�������
			wtgsl = StrutsReportInDelegate.getYBRecordCount(reportInForm.getYear(),reportInForm.getTerm(),reportInForm.getOrgId(),Config.CHECK_FLAG_FAILED,ybrepIds.toString());//���δͨ���ı�������
			
			/* δ���͵ı���,δͨ���ı��� */
			result = (ybsl - wshsl - hgsl)+","+wtgsl;
		}catch(Exception ex){
			result = null;
			ex.printStackTrace();
		}
		return result;
	}
	
	public static int getWSSL(Operator operator){
		int result = 0;
		
		try{
			result = StrutsReportInDelegate.getWSRecordCountOfmanual(operator);
		}catch(Exception ex){
			result = 0;
			ex.printStackTrace();
		}
		return result;
	}
}
