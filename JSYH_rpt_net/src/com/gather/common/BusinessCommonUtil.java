package com.gather.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.gather.adapter.StrutsCalendarDetailDelegate;
import com.gather.adapter.StrutsMActuRepDelegate;
import com.gather.adapter.StrutsMChildReportDelegate;
import com.gather.adapter.StrutsMDataRgTypeDelegate;
import com.gather.adapter.StrutsMMainRepDelegate;
import com.gather.adapter.StrutsMOrg;
import com.gather.adapter.StrutsMRepFreqDelegate;
import com.gather.bean.DataRangeAndFrequencyBean;
import com.gather.refer.data.ShowInfoUtil;
import com.gather.struts.forms.CalendarDetailForm;
import com.gather.struts.forms.MActuRepForm;
import com.gather.struts.forms.MDataRgTypeForm;
import com.gather.struts.forms.MOrgForm;
import com.gather.struts.forms.MRepFreqForm;

public class BusinessCommonUtil {
	
	/**
	 * @author linfeng
	 * @function 获得机构id
	 * @param orgId String  机构id
	 * @return orgId String[]  机构id 数组
	 */
	public static String[] getOrgId(String orgId){

		return ShowInfoUtil.getOrgId(orgId);
	}
	/**
	 * @author linfeng
	 * @function 获得机构名称
     * @param orgId String 机构id
	 * @return orgName String  机构名称
	 */
	public static String getOrgName(String orgId){
		return ShowInfoUtil.getOrgName(orgId);
	}
	
	
	  /**
	   * @author linfeng
	   * @function 得到相关报表的数据范围
	   * @param childReportId
	   * @param versionId
	   * @param freq
	   * @return
	   */
	  public static String[] getDataRange(String childReportId,String versionId,String freq){
		  return ShowInfoUtil.getDataRange(childReportId,versionId,freq);
	  }
	
	  /**
	   * @author linfeng
	   * @function 得到某版本的最新版本
	   * @param childReportId String
	   * @return String newVersion
	   */

    public static String getNewVersion(String childReportId){
    	return ShowInfoUtil.getNewVersion(childReportId);
    }
	
	/**
	 * @author linfeng
	 * @function 获得子报表名称
	 * @param orgId String  机构id
	 * @param 
	 * @return orgName String  机构名称
	 */
//	public static String getChildReportName(String orgId){
//		return ShowInfoUtil.getOrgName(orgId);
//	}
	
	/**
	 * @author linfeng
	 * @function 获得子报表名称
	 * @param childReportId String  子报表id
	 * @param version String  版本号
	 * @return childReportName String  子报表名称
	 */
	public static String getChildReportName(String childReportId,String versionId){
		return StrutsMChildReportDelegate.getChildReportName(childReportId,versionId);
	}
	
	  /**
	   * @author linfeng
	   * @function 得到子报表名称
	   * @param childRepId String
	   * @return childReportId String
	   */
	  public static String getChildReportName(String childRepId){ 
		  return StrutsMChildReportDelegate.getChildReportName(childRepId);
	  }
	/**
	 * @author linfeng
	 * @function 获得子报表id列表
	 * @param orgId String[]  机构id 数组
	 * @return subReportIds String[]  子报表id 数组
	 */
	public synchronized static String[] getSubReportId(String[] orgIds){
		return ShowInfoUtil.getSubReportId(orgIds);
	}
	/**
	 * @author linfeng
	 * @function 获得子报表信息列表
	 * @param subReportIds String[] 子报表id
	 * @return MChildReportForm List  子报表 formList
	 */
	public static List getMySubReports(String[] subReportIds){
		return ShowInfoUtil.getMySubReports(subReportIds);
	}
	
	/**
	 * @author linfeng
	 * @function 获得最新版本的子报表信息列表
	 * @param MChildReportForm List  所属机构的所有子报表 formList
	 * @return MChildReportForm Collection   所属机构的最新子报表 form集合
	 */
	public static Collection getNewVersionReports(List ChildReportForms){
	     return ShowInfoUtil.getNewVersionReports(ChildReportForms);
	}
	/**
	 * @author linfeng
	 * @function 获得最新版本的子报表信息列表
	 * @param orgId  所属机构的orgId
	 * @return MChildReportForm List   所属机构的最新子报表 form集合
	 * 
	 */
	public synchronized static List getNewVersionReports(String orgId){
		return ShowInfoUtil.getNewVersionReports(orgId);
	}
	
	/**
	 * @author linfeng
	 * @function 获得最新版本的子报表信息列表(只包含自己)
	 * @param orgId  所属机构的orgId
	 * @return MChildReportForm List   所属机构的最新子报表 form集合
	 */
	public static List getOneOrgNewVersionReports(String orgId){
		return ShowInfoUtil.getOneOrgNewVersionReports(orgId);
	}
	/**
	 * @author linfeng
	 * @function 获得系统数据范围
	 * @return int[] 数据范围id
	 * 
	 */
	public static int[] getDataRange(){
		List list=null;
		
		try{
			list=StrutsMDataRgTypeDelegate.findAll();
		}catch(Exception e){
			e.printStackTrace();
		}
		int[] data=new int[list.size()];
		for(int i=0;i<list.size();i++){
			data[i]=((MDataRgTypeForm)list.get(i)).getDataRangeId().intValue();
		}
		return data;
	}
	/**
	 * @author linfeng
	 * @function 获得系统频率数据
	 * @return int[]  频度id
	 * 
	 */
	public static int[] getFrequency(){
		List list=null;
		
		try{
			list=StrutsMRepFreqDelegate.findAll();
		}catch(Exception e){
			e.printStackTrace();
		}
		int[] data=new int[list.size()];
		for(int i=0;i<list.size();i++){
			data[i]=((MRepFreqForm)list.get(i)).getRepFreqId().intValue();
		}
		return data;
	}
	
	/**
	 * @author linfeng
	 * @function 得到所有数据范围id和频度id的集合
	 * @return List 
	 */
	public static List getDataRangeAndFreq(){
		List list=new ArrayList();
		int[] freq=getFrequency();
		int[] dataRange=getDataRange();
		for(int i=0;i<freq.length;i++){
			for(int j=0;j<dataRange.length;j++){
				DataRangeAndFrequencyBean bean=new DataRangeAndFrequencyBean();
				bean.setRepFreqId(freq[i]);
				bean.setDataRangeId(dataRange[j]);
				list.add(bean);
			}
		}
		return list;
	}
	/**
	 * @author linfeng
	 * @return MDataRgTypeForm list 返回系统所有数据范围的数据
	 */
	public static List getAllDataRange(){
		List list=null;
		try{
		    list=StrutsMDataRgTypeDelegate.findAll();
		}catch(Exception e){e.printStackTrace();}
		return list;
	}
	/**
	 * @author linfeng
	 * @return MRepFreqForm list 返回系统所有频率的数据
	 */
	public static List getAllFrequency(){
		List list=null;
		try{
		    list=StrutsMRepFreqDelegate.findAll();
		}catch(Exception e){e.printStackTrace();}
		return list;
	}
	/**
	 * @author linfeng
	 * @param frequencyId Integer
	 * @return frequencyName String
	 */
	public static String getFrequencyName(Integer frequencyId){
		return BaseInfoUtils.getFrequencyName(frequencyId);
		
	}
	/**
	 * @author linfeng
	 * @param dataRangeId Integer
	 * @return dataRangeName String
	 */
	public static String getDataRangeName(Integer dataRangeId){
		return BaseInfoUtils.getDataRangeName(dataRangeId);
		
	}
	
	  /**
	   * @author linfeng
	   * @function 得到报表相关的频率范围
	   * @param childReportId
	   * @param versionId
	   * @return frequencyId String[] 
	   */
	  public static String[] getFreqIds(String childReportId,String versionId){
		  return ShowInfoUtil.getFreqIds(childReportId,versionId);
	  }
	
	/**
	 * @author linfeng
	 * @function 得到应该提交的日期
	 * @param searchDate   //查询的日期
	 * @param subReportId  //子报表id
	 * @param versionId    //版本id
	 * @param dataRangeId  //数据范围id
	 * @param frequencyId  //频率id 
	 * @return Date
	 */
	
	public static Date getReferDate(Date searchDate,String subReportId,String versionId,Integer dataRangeId,Integer frequencyId)throws Exception{
		// System.out.println(searchDate.toString()+"-"+subReportId+"-"+versionId+"-"+dataRangeId+"-"+frequencyId);
		int delayNumber=getDelayDayNumber(subReportId,versionId,dataRangeId,frequencyId);
		// System.out.println("The delayNumber is: "+delayNumber);
		Date referDate=getWorkDay(searchDate,frequencyId,delayNumber);
		// System.out.println("referDate is"+referDate);
		return referDate;
	}
	
	/**
	 * @author linfeng
	 * @function 得到主报表名称
	 * @param 主报表id
	 * @return string 主报表名称
	 */
	public static String getMRepName(Integer repId){
		return StrutsMMainRepDelegate.getRepName(repId);
	}
	
	/**
	 * @author linfeng
	 * @function 得到工作日(yig)
	 * @param searchDate   //查询的日期
	 * @param frequencyId  //频率id
	 * @return Date
	 */
	
	public static Date getWorkDay(Date searchDate,Integer frequencyId,int delayNumber)throws Exception {
		Date toDate=null;
		int addNumber=0;
		if(frequencyId.intValue()==Config.FREQUENCY_MONTH){
			  addNumber=2;
		}else if(frequencyId.intValue()==Config.FREQUENCY_SEASON){
			addNumber=2;
		}else if(frequencyId.intValue()==Config.FREQUENCY_HALF_YEAR){
			addNumber=3;
		}else if(frequencyId.intValue()==Config.FREQUENCY_YEAR){
			addNumber=4;
		}
		int year=Integer.parseInt(DateUtil.toSimpleDateFormat(searchDate,"yyyy"));
		int month=Integer.parseInt(DateUtil.toSimpleDateFormat(searchDate,"MM"));
		// System.out.println("before----year is: "+year+" and month is: "+month+" and addNumber is:"+addNumber);
		int[] temp=DateUtil.addMonth(year,month,addNumber);
		int afterYear=temp[0];
		int afterMonth=temp[1];
		// System.out.println("after----year is: "+afterYear+" and month is: "+afterMonth);
		toDate=DateUtil.getDateByString(""+afterYear+"-"+afterMonth,DateUtil.YEAR+"-"+DateUtil.MONTH);
		
		//
		//// System.out.println("---The searchDate is: "+searchDate.toLocaleString());
		//// System.out.println("---The toDate is : "+toDate.toLocaleString());
		CalendarDetailForm myForm=null;
		List list=StrutsCalendarDetailDelegate.findByTwoDays(searchDate,toDate);
		if(list!=null && list.size()>0){
			if(list.size()>delayNumber){
			   myForm=(CalendarDetailForm)list.get(delayNumber);	
			}
		}
		if(myForm!=null) return myForm.getCalDate();
		
		//否则返回默认的日期，既起始月份+延迟时间数
		return DateUtil.addDay(searchDate,delayNumber);
		//throw new SMMISException("("+year+"年"+month+"月)系统不存在相应的工作日期记录");
	}
	
	/**
	 * @author linfeng
	 * @function 得到实际的延迟时间数
	 * @param subReportId  //子报表id
	 * @param versionId    //版本id
	 * @param dataRangeId  //数据范围id
	 * @param frequencyId  //频率id 
	 */
	
	public static int getDelayDayNumber(String subReportId,String versionId,Integer dataRangeId,Integer frequencyId){
		 MActuRepForm myForm=StrutsMActuRepDelegate.getCalendarDetailInfo(subReportId,versionId,dataRangeId,frequencyId);
		return myForm.getNormalTime().intValue()+myForm.getDelayTime().intValue();
	}
	
	
	//为jsp显示机构列表准备的原数据
	public static List getOrgNameList(String[] orgIds){
		List repRangeList=StrutsMOrg.getmorg(orgIds);
		List list = new ArrayList();
		if(repRangeList!=null && repRangeList.size()!=0)
		{
			for(int i=0;i<repRangeList.size();i++)
			{
				MOrgForm item = (MOrgForm)repRangeList.get(i);
				list.add(new LabelValueBean(item.getOrgName(),item.getOrgId()));	
			}
			return list;
		}
		else 
			return null;
	}
	
	/**
	 * @author linfeng
	 * @function 获得某具体子报表的开始和结束时间
	 * @param childReportId String 子报表id
	 * @param versionId String 版本id
	 * @return Date[]  date[0] 开始时间 date[1] 结束时间
	 */
	public static Date[] getOneReportTime(String childRepId,String versionId){
		return ShowInfoUtil.getOneReportTime(childRepId,versionId);
	}
	/**
	 * @author linfeng
	 * @function 得到迟报的天数
	 * @param String repId
	 * @param String versionId
	 * @param String year
	 * @param String sequencyId
	 * @param String term
	 * @param String referDate
	 * @return int 天数
	 */
	   public static int getDelayReferTime(String repId,String versionId,String year,String dataRangeId,String freqId,String term,String referDate){
		   Date normalReferDate=null;
		   try{
	       Date ParamDate_for_getWorkDay=com.gather.refer.data.OtherUtil.getStartAndEndDate(repId,versionId,dataRangeId,freqId,year,term)[1];
		        normalReferDate=getReferDate(ParamDate_for_getWorkDay,repId,versionId,new Integer(Integer.parseInt(dataRangeId)),new Integer(Integer.parseInt(freqId)));
		   }catch(Exception e){
			   e.printStackTrace();
			   return 0;   //出现异常，返回0
		   }
	       Date referedDate=DateUtil.getDateByString(referDate.substring(0,10),DateUtil.NORMALDATE);
	       return DateUtil.getOneGreatlessTwoDaysNumber(referedDate,normalReferDate);
	   }
	
	public static void main(String[] args){
		
	}

}
