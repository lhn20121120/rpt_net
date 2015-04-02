package com.gather.refer.data;

import java.util.Date;

import com.gather.common.BusinessCommonUtil;
import com.gather.common.DateUtil;
import com.gather.struts.forms.UploadDataShowForm;


public class DataStateMaker {
	
	public static final String WAIT_FOR_REFER = "����";
	public static final String MISS_REFER = "©��";
	public static final String REFERED = "�ѱ�";
	public static final String REFERED_LATE = "�ٱ�";
	public static final String ELIGIBLE = "�ϸ�";
	public static final String DISQUALIFICATION = "���ϸ�";
	public static final String REPEAT_REFER = "�ر�";
	
	public static Date today=DateUtil.getTodayDate();
	
	public static void make(UploadDataShowForm myForm){
		Date today=new Date();
		if(myForm.getIfReferedFlag()==0){
			//������©��
			
			//// System.out.println("myForm.getSetDate() is :"+myForm.getSetDate());
			//// System.out.println("myForm.getReportId() is : "+myForm.getReportId());
			//// System.out.println("myForm.getVersion() is :"+myForm.getVersion());
			//// System.out.println("myForm.getDataRange() is :"+myForm.getDataRange());
			//// System.out.println("myForm.getFrequency() is :"+myForm.getFrequency());
			Date setDate=null;
			try{
				setDate=BusinessCommonUtil.getReferDate(myForm.getSetDate(),myForm.getReportId(),myForm.getVersion(),myForm.getDataRange(),myForm.getFrequency());
			}catch(Exception e){
				e.getMessage();
				e.printStackTrace();
			}
			// System.out.println("today is: "+today+"setDate is: "+setDate);
			if(DateUtil.compareDate(today,setDate)==1){
				//��ǰʱ�����Ӧ���ϱ���ʱ�䣬Ϊ©��
				myForm.setState(DataStateMaker.MISS_REFER);
			}else{
				//�����Ǵ���
				myForm.setState(DataStateMaker.WAIT_FOR_REFER);
			}
		}else{
			//�ѱ����ٱ����ϸ񡢲��ϸ��ر�
			Date referedDate=myForm.getReferedDate();
			if(DateUtil.compareDate(today,referedDate)==1){
				myForm.setState(DataStateMaker.REFERED);
			}else{
				myForm.setState(DataStateMaker.REFERED_LATE);
			}
			if(myForm.getRepState().intValue()==1){
				myForm.setState(DataStateMaker.ELIGIBLE);
			}else if(myForm.getRepState().intValue()==2){
				myForm.setState(DataStateMaker.DISQUALIFICATION);
			}
			if(myForm.getReportFlag()!=null && !myForm.getReportFlag().equals("")){
				if(myForm.getReportFlag().equals("1")){
					myForm.setState(DataStateMaker.REPEAT_REFER);
				}	
			}
		}
		return;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
