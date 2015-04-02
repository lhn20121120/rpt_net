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
	 * @function ��û���id
	 * @param orgId String  ����id
	 * @return orgId String[]  ����id ����
	 */
	public static String[] getOrgId(String orgId){

		return ShowInfoUtil.getOrgId(orgId);
	}
	/**
	 * @author linfeng
	 * @function ��û�������
     * @param orgId String ����id
	 * @return orgName String  ��������
	 */
	public static String getOrgName(String orgId){
		return ShowInfoUtil.getOrgName(orgId);
	}
	
	
	  /**
	   * @author linfeng
	   * @function �õ���ر�������ݷ�Χ
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
	   * @function �õ�ĳ�汾�����°汾
	   * @param childReportId String
	   * @return String newVersion
	   */

    public static String getNewVersion(String childReportId){
    	return ShowInfoUtil.getNewVersion(childReportId);
    }
	
	/**
	 * @author linfeng
	 * @function ����ӱ�������
	 * @param orgId String  ����id
	 * @param 
	 * @return orgName String  ��������
	 */
//	public static String getChildReportName(String orgId){
//		return ShowInfoUtil.getOrgName(orgId);
//	}
	
	/**
	 * @author linfeng
	 * @function ����ӱ�������
	 * @param childReportId String  �ӱ���id
	 * @param version String  �汾��
	 * @return childReportName String  �ӱ�������
	 */
	public static String getChildReportName(String childReportId,String versionId){
		return StrutsMChildReportDelegate.getChildReportName(childReportId,versionId);
	}
	
	  /**
	   * @author linfeng
	   * @function �õ��ӱ�������
	   * @param childRepId String
	   * @return childReportId String
	   */
	  public static String getChildReportName(String childRepId){ 
		  return StrutsMChildReportDelegate.getChildReportName(childRepId);
	  }
	/**
	 * @author linfeng
	 * @function ����ӱ���id�б�
	 * @param orgId String[]  ����id ����
	 * @return subReportIds String[]  �ӱ���id ����
	 */
	public synchronized static String[] getSubReportId(String[] orgIds){
		return ShowInfoUtil.getSubReportId(orgIds);
	}
	/**
	 * @author linfeng
	 * @function ����ӱ�����Ϣ�б�
	 * @param subReportIds String[] �ӱ���id
	 * @return MChildReportForm List  �ӱ��� formList
	 */
	public static List getMySubReports(String[] subReportIds){
		return ShowInfoUtil.getMySubReports(subReportIds);
	}
	
	/**
	 * @author linfeng
	 * @function ������°汾���ӱ�����Ϣ�б�
	 * @param MChildReportForm List  ���������������ӱ��� formList
	 * @return MChildReportForm Collection   ���������������ӱ��� form����
	 */
	public static Collection getNewVersionReports(List ChildReportForms){
	     return ShowInfoUtil.getNewVersionReports(ChildReportForms);
	}
	/**
	 * @author linfeng
	 * @function ������°汾���ӱ�����Ϣ�б�
	 * @param orgId  ����������orgId
	 * @return MChildReportForm List   ���������������ӱ��� form����
	 * 
	 */
	public synchronized static List getNewVersionReports(String orgId){
		return ShowInfoUtil.getNewVersionReports(orgId);
	}
	
	/**
	 * @author linfeng
	 * @function ������°汾���ӱ�����Ϣ�б�(ֻ�����Լ�)
	 * @param orgId  ����������orgId
	 * @return MChildReportForm List   ���������������ӱ��� form����
	 */
	public static List getOneOrgNewVersionReports(String orgId){
		return ShowInfoUtil.getOneOrgNewVersionReports(orgId);
	}
	/**
	 * @author linfeng
	 * @function ���ϵͳ���ݷ�Χ
	 * @return int[] ���ݷ�Χid
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
	 * @function ���ϵͳƵ������
	 * @return int[]  Ƶ��id
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
	 * @function �õ��������ݷ�Χid��Ƶ��id�ļ���
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
	 * @return MDataRgTypeForm list ����ϵͳ�������ݷ�Χ������
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
	 * @return MRepFreqForm list ����ϵͳ����Ƶ�ʵ�����
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
	   * @function �õ�������ص�Ƶ�ʷ�Χ
	   * @param childReportId
	   * @param versionId
	   * @return frequencyId String[] 
	   */
	  public static String[] getFreqIds(String childReportId,String versionId){
		  return ShowInfoUtil.getFreqIds(childReportId,versionId);
	  }
	
	/**
	 * @author linfeng
	 * @function �õ�Ӧ���ύ������
	 * @param searchDate   //��ѯ������
	 * @param subReportId  //�ӱ���id
	 * @param versionId    //�汾id
	 * @param dataRangeId  //���ݷ�Χid
	 * @param frequencyId  //Ƶ��id 
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
	 * @function �õ�����������
	 * @param ������id
	 * @return string ����������
	 */
	public static String getMRepName(Integer repId){
		return StrutsMMainRepDelegate.getRepName(repId);
	}
	
	/**
	 * @author linfeng
	 * @function �õ�������(yig)
	 * @param searchDate   //��ѯ������
	 * @param frequencyId  //Ƶ��id
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
		
		//���򷵻�Ĭ�ϵ����ڣ�����ʼ�·�+�ӳ�ʱ����
		return DateUtil.addDay(searchDate,delayNumber);
		//throw new SMMISException("("+year+"��"+month+"��)ϵͳ��������Ӧ�Ĺ������ڼ�¼");
	}
	
	/**
	 * @author linfeng
	 * @function �õ�ʵ�ʵ��ӳ�ʱ����
	 * @param subReportId  //�ӱ���id
	 * @param versionId    //�汾id
	 * @param dataRangeId  //���ݷ�Χid
	 * @param frequencyId  //Ƶ��id 
	 */
	
	public static int getDelayDayNumber(String subReportId,String versionId,Integer dataRangeId,Integer frequencyId){
		 MActuRepForm myForm=StrutsMActuRepDelegate.getCalendarDetailInfo(subReportId,versionId,dataRangeId,frequencyId);
		return myForm.getNormalTime().intValue()+myForm.getDelayTime().intValue();
	}
	
	
	//Ϊjsp��ʾ�����б�׼����ԭ����
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
	 * @function ���ĳ�����ӱ���Ŀ�ʼ�ͽ���ʱ��
	 * @param childReportId String �ӱ���id
	 * @param versionId String �汾id
	 * @return Date[]  date[0] ��ʼʱ�� date[1] ����ʱ��
	 */
	public static Date[] getOneReportTime(String childRepId,String versionId){
		return ShowInfoUtil.getOneReportTime(childRepId,versionId);
	}
	/**
	 * @author linfeng
	 * @function �õ��ٱ�������
	 * @param String repId
	 * @param String versionId
	 * @param String year
	 * @param String sequencyId
	 * @param String term
	 * @param String referDate
	 * @return int ����
	 */
	   public static int getDelayReferTime(String repId,String versionId,String year,String dataRangeId,String freqId,String term,String referDate){
		   Date normalReferDate=null;
		   try{
	       Date ParamDate_for_getWorkDay=com.gather.refer.data.OtherUtil.getStartAndEndDate(repId,versionId,dataRangeId,freqId,year,term)[1];
		        normalReferDate=getReferDate(ParamDate_for_getWorkDay,repId,versionId,new Integer(Integer.parseInt(dataRangeId)),new Integer(Integer.parseInt(freqId)));
		   }catch(Exception e){
			   e.printStackTrace();
			   return 0;   //�����쳣������0
		   }
	       Date referedDate=DateUtil.getDateByString(referDate.substring(0,10),DateUtil.NORMALDATE);
	       return DateUtil.getOneGreatlessTwoDaysNumber(referedDate,normalReferDate);
	   }
	
	public static void main(String[] args){
		
	}

}
