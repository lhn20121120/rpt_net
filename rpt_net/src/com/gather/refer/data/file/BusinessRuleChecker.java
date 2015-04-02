package com.gather.refer.data.file;

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.gather.adapter.StrutsReportDelegate;
import com.gather.common.BusinessCommonUtil;
import com.gather.common.DateUtil;
import com.gather.hibernate.Report;
import com.gather.refer.data.OtherUtil;


public class BusinessRuleChecker {

	private String errorMessage="";
	
	public static String BASE_INFO = "";
	public static final String NO_ORG_RIGHT = "您没有权限代报此机构的数据";
	public static final String NOT_REQUIRE_REFER = "机构不需上报此报表";
	public static final String NOT_EXIST_VERSION = "系统不存在此版本的报表";
	public static final String ERRLY_VERSION = "此版本还未启用";
	public static final String BACK_VERSION = "版本已经过期，请下载新的版本";
	public static final String NOT_REACH_THIS_TIME = "上报时间太早，还没有到上报时间";
	public static final String ERROR_TIME = "错误的时间和期数，请检查后重新提交";
	public static final String ERROR_DATA_REANGE  = "错误的数据范围";
	public static final String ERROR_FREQUENCE = "错误的频率";
	public static final String ERROR_YEAR = "日期格式异常(年)";
	
	public static final String NOT_REFERED = "数据没有提交";
	public static final String REFERED = "数据已经提交";
	public static final String PASS_CHECK = "数据已经提交，并通过检查";
	public static final String NOT_PASS_CHECK = "数据已经提交，没有通过检查";
	public static final String NEED_REPEAT_REFER = "上次上报数据存在问题，需要重报";
	
	public static final String SYS_NO_ORG = "系统不存在此机构";
	public static final String SYS_NO_REPORT = "系统不存在此报表";
	public static final String SYS_NO_DATA_RANGE = "系统不存在此数据范围";
	public static final String SYS_NO_FREQUENCY = "系统不存在此数据频率";
	
	public static final String STATE_YES = "通过";
	public static final String STATE_NO = "不通过";
	public static final String STATE_TO_LATE = "迟报";
	public static final String STATE_REPEAT = "重报";
	
	public BusinessRuleChecker(){}
	
	public boolean check(ListingXmlBean xmlBean,HttpSession session){
		
		if(!checkOrgReange(xmlBean,session)) return false;  //1，首先检查机构是否存在
		if(!checkReportId(xmlBean,session)) return false; //2，检查机构下的报表模版(id)是否存在
		if(!checkSysInfo(xmlBean)) return false;//3，检查系统中相应的数据范围、频率是否存在
		if(!checkVersion(xmlBean)) return false; //4，检查版本，如果过期提示下载新版本
		if(!checkFrequency(xmlBean)) return false;//5，验证频率
		if(!checkDataRange(xmlBean)) return false; //6，验证数据范围
		if(!checkRepPeriodVaidity(xmlBean)) return false; //检查提交的数据是否在版本有效期内
		if(!checkTerm(xmlBean)) return false; //7，验证当前的频率是否包含相应的期数
		if(!checkYearAndTerm(xmlBean)) return false; //8，验证 年 和 期数
		if(!checkReferedState(xmlBean)) return false;  //9，最后检查是否已经提交并通过
        return true;
		
		//1,检查对应的频率是否存在
		//2,如果频率对应的期数的时间还没有到，提示太早
		//3,否则查找是否已经提交
		//3.1如果已经提交，提示已经提交，不能再次上报(false)
		//3.2如果状态是不合格，提示不合格，可以重报，上报状态为(true)
	}
	
	//特殊报表的处理，见到就不做验证，直接通过
	private boolean checkSpecial(ListingXmlBean xmlBean){
		String orgId=xmlBean.getReportId();
		if(orgId.equalsIgnoreCase("G3301") || orgId.equalsIgnoreCase("G3302")){
			xmlBean.setState(BusinessRuleChecker.STATE_YES);
		    return true;
		}
		return false;
	}
	//检查系统中相应的数据范围、频率是否存在
	private boolean checkSysInfo(ListingXmlBean xmlBean){
		int[] freqs=BusinessCommonUtil.getFrequency();
		int[] dataRanges=BusinessCommonUtil.getDataRange();
			int freq=Integer.parseInt(xmlBean.getFrequencyId());
			int dataRange=Integer.parseInt(xmlBean.getDataRangeId());
			
			int x=0;
			//检查频率
			for(int j=0;j<freqs.length;j++){
				if(freq==freqs[j]){
					x=1;         //发现标志
					break;
				}
			}
          //检查数据范围
			if(x==0){//说明频率检查发生问题，不需要检查数据范围了
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.SYS_NO_FREQUENCY);
				return false;
			}else{   //否则检查数据范围 
				for(int j=0;j<dataRanges.length;j++){
					if(dataRange==dataRanges[j]){
						x=1;    //发现标志
						break;
					}
				}
			}
			if(x==0) { //说明数据范围发生问题
				//creatBaseInfo(xmlBean.getOrgId(),xmlBean.getReportId(),xmlBean.getVersion(),xmlBean.getYear(),xmlBean.getTerms(),xmlBean.getFrequencyId(),xmlBean.getDataRangeId());
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.SYS_NO_FREQUENCY);
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	
	//机构范围  如果机构不存在，返回false ，否则返回true
	private boolean checkOrgReange(ListingXmlBean xmlBean,HttpSession session){
		String[] orgIds=BusinessCommonUtil.getOrgId((String)session.getAttribute("orgId"));
	
			String orgId=xmlBean.getOrgId();
			int x=0;  //标志 如果为0 不存在 ，如果为1 存在
			for(int j=0;j<orgIds.length;j++){
				// System.out.println("check org,the orgid is:"+orgIds[j]);
				if(orgId.trim().equals(orgIds[j].trim())){
					// System.out.println("====wether was executed ,and j is: "+j+"---");
					x=1;
					break;
				}
			}
			// System.out.println("========xmlBean orgId is: "+orgId+" x is:"+x);
			if(x==0){//(xx机构xx报表xx版本xx年xx月xx报)
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.NO_ORG_RIGHT+" : "+ BusinessCommonUtil.getOrgName(xmlBean.getOrgId()));
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	//机构下的模版名称(id)
	private boolean checkReportId(ListingXmlBean xmlBean,HttpSession session){
		String[] childReportIds=BusinessCommonUtil.getSubReportId(BusinessCommonUtil.getOrgId((String)session.getAttribute("orgId")));
	
			String childReportId=xmlBean.getReportId();
			int x=0;  //标志 如果为0 不存在 ，如果为1 存在
			for(int j=0;j<childReportIds.length;j++){
				if(childReportId.trim().equals(childReportIds[j].trim())){
					x=1;
					break;
				}
			}
			if(x==0){//(xx机构xx报表xx版本xx年xx月xx报)
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.NOT_REQUIRE_REFER+" : "+ BusinessCommonUtil.getChildReportName(xmlBean.getReportId(),xmlBean.getVersion()));
				//this.setErrorMessage(BusinessRuleChecker.NOT_REQUIRE_REFER+" : "+ BusinessCommonUtil.getChildReportName(xmlBean.getReportId(),xmlBean.getVersion()));
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	//版本，如果过期提示下载新版本
	private boolean checkVersion(ListingXmlBean xmlBean){
			if(!BusinessCommonUtil.getNewVersion(xmlBean.getReportId()).equals(xmlBean.getVersion())){
				String reportName=BusinessCommonUtil.getChildReportName(xmlBean.getReportId());
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(reportName+"-v"+xmlBean.getVersion()+")"+BusinessRuleChecker.BACK_VERSION);
				return true;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	
	//验证当前上传版本是否有效(是否在有效期)
	private boolean checkRepPeriodVaidity(ListingXmlBean xmlBean){
		Date[] checkDate=BusinessCommonUtil.getOneReportTime(xmlBean.getReportId(),xmlBean.getVersion());
		Date today=new Date();
		if(checkDate==null || checkDate[0]==null){
			//系统不存在此本本的报表
			xmlBean.setState(BusinessRuleChecker.STATE_NO);
			xmlBean.setMsg(BusinessRuleChecker.BASE_INFO+BusinessRuleChecker.NOT_EXIST_VERSION);
			return false;
		}else if(DateUtil.compareDate(checkDate[0],today)==1){
			//如果开始时间大于当天，说明版本还未启用
			xmlBean.setState(BusinessRuleChecker.STATE_NO);
			xmlBean.setMsg(BusinessRuleChecker.BASE_INFO+BusinessRuleChecker.ERRLY_VERSION);
			return false;
		}else if(DateUtil.compareDate(today,checkDate[1])==1){
            //如果当天大于结束时间，说明版本已经过期
			xmlBean.setState(BusinessRuleChecker.STATE_NO);
			xmlBean.setMsg(BusinessRuleChecker.BASE_INFO+BusinessRuleChecker.BACK_VERSION);
			return false;
		}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	
	//验证相应报表是否存在此频率
	private boolean checkFrequency(ListingXmlBean xmlBean){
			String childReportId=xmlBean.getReportId();
			String version=xmlBean.getVersion(); 
			// System.out.println("---xmlBean.childReportId is: "+childReportId+"xmlBean.getVersion() is: "+version);
			String[] freqIds=BusinessCommonUtil.getFreqIds(childReportId,version);
			int x=0;
			for(int j=0;j<freqIds.length;j++){
				// System.out.println("-----freqIds[j]"+freqIds[j]+" and xmlBean.getFrequencyId() is:"+xmlBean.getFrequencyId());
			if(Integer.parseInt(xmlBean.getFrequencyId())==Integer.parseInt(freqIds[j])){
					x=1;
				    break;
				}
			}
			if(x==0){   //说明没有找到
				String reportName=BusinessCommonUtil.getChildReportName(childReportId);
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg("("+reportName+"-v"+xmlBean.getVersion()+")"+BusinessRuleChecker.ERROR_FREQUENCE);
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	
	private boolean checkTerm(ListingXmlBean xmlBean){
		//验证根据当前频率是否包含相应的期数
		return true;
	}
	//验证 年 和 期数
	private boolean checkYearAndTerm(ListingXmlBean xmlBean){
		//验证频率对应的时间是否合适
		//1根据提供的数据确定正常提交的时间段
		//1.1如果还没有到，提示太早(false)
		//1.2如果已经过期，查看是否已经上报
		//1.2.1如果已经上报。。。。
		//1.2.2如果没有上报，为迟报(true),需在数据库中加上迟报日期(default:today)
			String year=xmlBean.getYear();
			String term=xmlBean.getTerms();
			String freq=xmlBean.getFrequencyId();
			String reportId=xmlBean.getReportId();
			String version=xmlBean.getVersion();
			String dataRange=xmlBean.getDataRangeId();
			Date[] seDate=null;
			try{
			    seDate=OtherUtil.getStartAndEndDate(reportId,version,dataRange,freq,year,term);
			}catch(Exception e){
				e.printStackTrace();
				//如果数据有问题，是错误的数据(year)造成的,或者没有取道工作日的值
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.ERROR_TIME);
				return false;
			}
			Date startDate=seDate[0];
			Date endDate=seDate[1];
			if(DateUtil.compareDate(startDate,new Date())==1){
				//太早,没有到期
				creatBaseInfo(xmlBean.getOrgId(),reportId,version,year,term,freq,dataRange);
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.BASE_INFO+BusinessRuleChecker.NOT_REACH_THIS_TIME);
				return false;
			}else if(DateUtil.compareDate(new Date(),endDate)==1){
			    //太迟
		        xmlBean.setState(BusinessRuleChecker.STATE_TO_LATE);
		        xmlBean.setMsg(BusinessRuleChecker.STATE_TO_LATE);
		        return true;
			}else{
				return checkReferedState(xmlBean);
			}
	}
	//验证数据范围
	private boolean checkDataRange(ListingXmlBean xmlBean){
			String childReportId=xmlBean.getReportId();
			String version=xmlBean.getVersion(); 
			String freq=xmlBean.getFrequencyId();
			String[] dataRangeId=BusinessCommonUtil.getDataRange(childReportId,version,freq);
			int x=0;
			for(int j=0;j<dataRangeId.length;j++){
				if(Integer.parseInt(xmlBean.getDataRangeId())==Integer.parseInt(dataRangeId[j])){
					x=1;
					break;
				}
			}
			if(x==0){   //说明没有找到
				String reportName=BusinessCommonUtil.getChildReportName(childReportId);
				String freqName=BusinessCommonUtil.getFrequencyName(new Integer(Integer.parseInt(freq)));
				xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg("("+reportName+"-v"+xmlBean.getVersion()+" "+freqName+"报)"+BusinessRuleChecker.ERROR_DATA_REANGE);
				return false;
			}
		xmlBean.setState(BusinessRuleChecker.STATE_YES);
		return true;
	}
	//验证是否已经提交并通过，如果已经提交 和 验证合格，不能再次提交，如果不合格，可以提交
	private boolean checkReferedState(ListingXmlBean xmlBean){
          Report report=StrutsReportDelegate.getReport(xmlBean);
          if(report==null){
      	    xmlBean.setState(BusinessRuleChecker.STATE_YES);
			xmlBean.setMsg(BusinessRuleChecker.NOT_REFERED);
    	     return true;
          }else if(report.getFileFlag().intValue()==0){
        	    xmlBean.setState(BusinessRuleChecker.STATE_NO);
				xmlBean.setMsg(BusinessRuleChecker.REFERED);
        	  return false;
          }else if(report.getFileFlag().intValue()==1){
      	    xmlBean.setState(BusinessRuleChecker.STATE_NO);
			xmlBean.setMsg(BusinessRuleChecker.PASS_CHECK);
        	  return false;
          }else if(report.getFileFlag().intValue()==2){
      	    xmlBean.setState(BusinessRuleChecker.STATE_REPEAT);
			xmlBean.setMsg(BusinessRuleChecker.NOT_PASS_CHECK);
        	  return true;
          }else if(report.getReportFlag().equals("1")){
      	    xmlBean.setState(BusinessRuleChecker.STATE_REPEAT);
			xmlBean.setMsg(BusinessRuleChecker.NEED_REPEAT_REFER);
        	  return true;
          }
		return false;
	}
	
	/**
	 * 	public static final String REFERED = "数据已经提交";
	public static final String PASS_CHECK = "数据已经提交，并通过检查";
	public static final String NOT_PASS_CHECK = "数据已经提交，没有通过检查";
	public static final String NEED_REPEAT_REFER = "上次上报数据存在问题，需要重报";
	 */
	
	/**
	 * @return Returns the errorMessage.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage The errorMessage to set.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	private void creatBaseInfo(String orgId,String reportId,String version,String year,String term,String freq,String dataRangeId){
	   StringBuffer temp=new StringBuffer();
	   String orgName=BusinessCommonUtil.getOrgName(orgId);
	   String reportName=BusinessCommonUtil.getChildReportName(reportId,version);
	   String dataRangeName=BusinessCommonUtil.getDataRangeName(new Integer(Integer.parseInt(dataRangeId)));
	   String frequency=BusinessCommonUtil.getFrequencyName(new Integer(Integer.parseInt(freq)));
       //(xx机构xx报表xx版本xx年xx期xx范围报表xx报)
	   temp.append("(");
	   temp.append(orgName+" ");
	   temp.append(reportName+"报表 ");
	   temp.append("第"+version+"版本 ");
	   temp.append(year+"年 ");
	   temp.append(term+"期");
	   temp.append(frequency+"报");
	   temp.append(dataRangeName+"范围报表 ");
	   temp.append(")");
	   
	   this.BASE_INFO=temp.toString();
	   // System.out.println("BusinessRuleChecker.BASE_INFO is: "+this.BASE_INFO);
	}

}
