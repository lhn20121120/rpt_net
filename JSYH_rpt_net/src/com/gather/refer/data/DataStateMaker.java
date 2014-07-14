package com.gather.refer.data;

import java.util.Date;

import com.gather.common.BusinessCommonUtil;
import com.gather.common.DateUtil;
import com.gather.struts.forms.UploadDataShowForm;


public class DataStateMaker {
	
	public static final String WAIT_FOR_REFER = "待报";
	public static final String MISS_REFER = "漏报";
	public static final String REFERED = "已报";
	public static final String REFERED_LATE = "迟报";
	public static final String ELIGIBLE = "合格";
	public static final String DISQUALIFICATION = "不合格";
	public static final String REPEAT_REFER = "重报";
	
	public static Date today=DateUtil.getTodayDate();
	
	public static void make(UploadDataShowForm myForm){
		Date today=new Date();
		if(myForm.getIfReferedFlag()==0){
			//待报、漏报
			
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
				//当前时间大于应该上报的时间，为漏报
				myForm.setState(DataStateMaker.MISS_REFER);
			}else{
				//否则是待报
				myForm.setState(DataStateMaker.WAIT_FOR_REFER);
			}
		}else{
			//已报、迟报、合格、不合格、重报
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
