package com.gather.refer.data;

import java.util.ArrayList;
import java.util.List;

import com.gather.struts.forms.RealReportForm;
import com.gather.struts.forms.ReportForm;
public class Compare {
	private List reportSource=null;
	private List referedReport=null;

	private String reportId="";
	private String version="";
	private Integer dataRange;
	private Integer frequency;
	
	public Compare(List pReportDource,List pReferedReport){
		this.reportSource=pReportDource;
		this.referedReport=pReferedReport;
	}
	public Compare(){}
	
    /**
     *@author linfeng 
     *@function 得到没有上报的数据列表
     *@param RealReportForm lise
     *@param ReportForm list
     *@return RealReportForm list  返回还没有上报的报表的列表
     */
	public List getNoReferedReports(List all,List refered){
		if(all==null) return null;
		//如果已提交的form==null,说明还没有上报的报表
		if(refered==null) return all;
		if(all.size()<=refered.size()) return null;
		List resultList=all;
		//得到说有相同的
		for(int i=0;i<refered.size();i++){
			ReportForm referedForm=(ReportForm)refered.get(i);
			for(int j=0;j<all.size();j++){
				RealReportForm realForm=(RealReportForm)all.get(j);
				// System.out.println("-------------realForm is"+realForm.getChildRepId());
				// System.out.println("-------------referedForm"+referedForm.getChildrepid());
	            if(fieldValueIfSame(realForm,referedForm)){
	            	//如果相同，就删掉，剩下的都是没有提交的
	                resultList.remove(i);
	                //// System.out.println("--resultList.add(realForm) size is : "+resultList.size());
	            }
			 }
           }
		return resultList;
	}
	//判断两个报表(form)是否相同
	private boolean fieldValueIfSame(RealReportForm realForm,ReportForm referedForm){
		boolean result=true;
		if(!(realForm.getChildRepId().trim()).equals(referedForm.getChildrepid().trim()))
		    {return false;}
		if(!(realForm.getVersionId().trim()).equals(referedForm.getVersionId().trim()))
		    {return false;}
		if(realForm.getDataRangeId()!=referedForm.getDatarangeid())
		    {return false;}
		if(realForm.getRepFreqId()!=referedForm.getFrequency())
		    {return false;}
		return result;
	}
	/**
	 * @author linfeng
	 * @function 得到可以显示的数据 以提交的form 和 未提交的form
     *@param RealReportForm list 所有该机构需要上报的formList
     *@param ReportForm list     所有该机构已经上报的formList
     *@return list 包括两个list 1,未提交的list(RealReportForm) 2,已提交的list(ReportForm)
	 */
	public List getShowReportList(List all,List refered){
		List resultList=new ArrayList();
		List noReferedReports=getNoReferedReports(all,refered);
		if(noReferedReports!=null && noReferedReports.size()>0){
			for(int i=0;i<noReferedReports.size();i++){
				RealReportForm myForm=(RealReportForm)noReferedReports.get(i);
				resultList.add(myForm);
			}
		}
		
        if(refered!=null && refered.size()>0){
			for(int i=0;i<refered.size();i++){
				ReportForm myForm=(ReportForm)refered.get(i);
				resultList.add(myForm);
			}
		}
		//resultList.add(noReferedReports);
		//resultList.add(refered);
		return resultList;
	}
	/**
	 * @return Returns the dataRange.
	 */
	public Integer getDataRange() {
		return dataRange;
	}
	/**
	 * @param dataRange The dataRange to set.
	 */
	public void setDataRange(Integer dataRange) {
		this.dataRange = dataRange;
	}
	/**
	 * @return Returns the frequency.
	 */
	public Integer getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency The frequency to set.
	 */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return Returns the referedReport.
	 */
	public List getReferedReport() {
		return referedReport;
	}
	/**
	 * @param referedReport The referedReport to set.
	 */
	public void setReferedReport(List referedReport) {
		this.referedReport = referedReport;
	}
	/**
	 * @return Returns the reportId.
	 */
	public String getReportId() {
		return reportId;
	}
	/**
	 * @param reportId The reportId to set.
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	/**
	 * @return Returns the reportSource.
	 */
	public List getReportSource() {
		return reportSource;
	}
	/**
	 * @param reportSource The reportSource to set.
	 */
	public void setReportSource(List reportSource) {
		this.reportSource = reportSource;
	}
	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
