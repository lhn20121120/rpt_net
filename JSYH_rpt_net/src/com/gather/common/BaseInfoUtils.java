package com.gather.common;

import java.util.List;

import com.gather.adapter.StrutsMChildReportDelegate;
import com.gather.adapter.StrutsMOrgDelegate;
import com.gather.struts.forms.MChildReportForm;
import com.gather.struts.forms.MDataRgTypeForm;
import com.gather.struts.forms.MOrgForm;
import com.gather.struts.forms.MRepFreqForm;

public class BaseInfoUtils {

	  /**
	   * @author linfeng
	   * @function 得到子报表名称
	   * @param childRepId String
	   * @param versionId String
	   * @return childReportId String
	   */
	  public static String getChildReportName(String childRepId,String versionId){ 
		  List list=BaseInfoBuffer.getInstance().getRealReps();
		  String repName="";
		  if(list!=null){
			  for(int i=0;i<list.size();i++){
				  MChildReportForm myForm=(MChildReportForm)list.get(i);
				  if(childRepId.equals(myForm.getChildRepId()) && versionId.equals(myForm.getVersionId())){
					 repName=myForm.getReportName();
					 if(repName!=null || !repName.equals("")) break;
				  }
			  }
		  }
		  if(repName==null || repName.equals("")){
			  return StrutsMChildReportDelegate.getChildReportName(childRepId,versionId);  
		  }else{
			  return repName;
		  }
	  }
	  
	  /**
	   * @author linfeng
	   * @function 得到子报表名称
	   * @param childRepId String
	   * @return childReportId String
	   */
	  public static String getChildReportName(String childRepId){ 
		  List list=BaseInfoBuffer.getInstance().getRealReps();
		  String repName="";
		  if(list!=null){
			  for(int i=0;i<list.size();i++){
				  MChildReportForm myForm=(MChildReportForm)list.get(i);
				  if(childRepId.equals(myForm.getChildRepId())){
					 repName=myForm.getReportName();
					 if(repName!=null || !repName.equals("")) break;
				  }
			  }
		  }
		  if(repName==null || repName.equals("")){
			  return StrutsMChildReportDelegate.getChildReportName(childRepId);  
		  }else{
			  return repName;
		  }
	  }
	  
	  /**
	   * @author linfeng
	   * @function 得到机构名称
	   * @param orgId
	   * @return
	   */
	  public static String getOrgName (String orgId){
		  List list=BaseInfoBuffer.getInstance().getOrgs();
		  String orgName="";
		  if(list!=null && list.size()>0){
			  for(int i=0;i<list.size();i++){
				  // System.out.println("-----if is MOrgForm ? "+list.get(i).getClass().getName());
				  MOrgForm org=(MOrgForm)list.get(i);
				  if(org.getOrgId().trim().equals(orgId.trim())){
					  orgName=org.getOrgName();
					  if(orgName!=null && !orgName.equals("")) break;
				  }
			  }
		  }
		  if(orgName!=null && !orgName.equals("")){
			  return orgName;
		  }else{
			  return StrutsMOrgDelegate.getOrgName(orgId);  
		  }
	  }
	  
		/**
		 * @author linfeng
		 * @param frequencyId Integer
		 * @return frequencyName String
		 */
		public static String getFrequencyName(Integer frequencyId){
			if(frequencyId==null) return null;
			 List list=BaseInfoBuffer.getInstance().getFreqs();
			 if(list==null) list=BusinessCommonUtil.getAllFrequency();
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					MRepFreqForm bean=(MRepFreqForm)list.get(i);
					if(bean.getRepFreqId().intValue()==frequencyId.intValue()){
						return bean.getRepFreqName();
					}
				}
			}
			return "";
			
		}
		/**
		 * @author linfeng
		 * @param dataRangeId Integer
		 * @return dataRangeName String
		 */
		public static String getDataRangeName(Integer dataRangeId){
			if(dataRangeId==null) return null;
			List list=BaseInfoBuffer.getInstance().getDataRanges();
			if(list==null || list.size()<1) list=BusinessCommonUtil.getAllDataRange();
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					MDataRgTypeForm bean=(MDataRgTypeForm)list.get(i);
					if(bean.getDataRangeId().intValue()==dataRangeId.intValue()){
				        return bean.getDataRgDesc();
					}
				}
			}
			return "";
			
		}
}
