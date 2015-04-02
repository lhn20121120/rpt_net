package com.cbrc.smis.action;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsColAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsQDDataValidateInfoDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.form.ReportInParticularForm;
import com.cbrc.smis.util.FitechException;



public final class ViewReportInParticularAction extends Action{
	private FitechException log = new FitechException(ViewReportInParticularAction.class);
	   /**
	    * 唐磊
	    * @param result 查询返回标志,如果成功返回true,否则返回false
	    * @param ReportInParticularForm 
	    * @param request 
	    * @exception Exception 有异常捕捉并抛出
	    */
	 public ActionForward execute(
		      ActionMapping mapping,
		      ActionForm form,
		      HttpServletRequest request,
		      HttpServletResponse response
		   )
		      throws IOException, ServletException {
		 
		   List list=null;
		   List abnormityList=null;
		   ReportInParticularForm reportInParticularForm= (ReportInParticularForm)form;
		   RequestUtils.populate(reportInParticularForm, request);
		   
		   if(reportInParticularForm!=null ){
			   request.setAttribute("repName", reportInParticularForm.getRepName());
			   try{
				   //返回的laterReportDay的迟报天数
				   int laterReportDay=StrutsReportInDelegate.selectLaterReportDay(reportInParticularForm.getRepInId());
				   reportInParticularForm.setLaterReportDay(String.valueOf(laterReportDay));
				   
				   //根据返回的查找result，如果为1则是点对点式，如果为2则是清单式
				   int result=StrutsMChildReportDelegate.getReportStyle(reportInParticularForm.getChildRepId(), reportInParticularForm.getVersionId());
				   
				   if(result==1)
				   {
					   //返回点对点式的表间表内的数据校验关系查找记录
					   list=StrutsDataValidateInfoDelegate.select_AllRecords(reportInParticularForm.getRepInId().toString());
					   //返回点对点式的异常变化的查找记录
					   abnormityList=StrutsAbnormityChangeDelegate.select(reportInParticularForm.getChildRepId(), reportInParticularForm.getVersionId());
					   //如果记录集不为空或大于0，则返回包含记录的request对象
					   if(list!=null && list.size()>0)
					   {
						   request.setAttribute("p2p",list);  
					   }
					   //如果异常记录不为空或大于0，则返回包含记录的request对象
					   if(abnormityList!=null&&abnormityList.size()>0){
						   request.setAttribute("abnormity", abnormityList);
					   }
					   //如果迟报天数大于等于0，则返回包含记录的request对象
					   if(laterReportDay>=0){
						   request.setAttribute("day", reportInParticularForm.getLaterReportDay());
					   }
				   }
				   else if(result==2)
				   {
					   //返回清单式的表间表内的数据校验关系查找记录
					   list=StrutsQDDataValidateInfoDelegate.select_AllRecords(reportInParticularForm.getRepInId().toString());
					   //返回清单式的异常变化的查找记录
					   abnormityList=StrutsColAbnormityChangeDelegate.select(reportInParticularForm.getChildRepId(), reportInParticularForm.getVersionId());
					   //如果记录集不为空或大于0，则返回包含记录的request对象
					   if(list!=null && list.size()>0)
					   {
						  request.setAttribute("p2p", list);
					   }
					   //如果异常记录不为空或大于0，则返回包含记录的request对象
					   if (abnormityList!=null&&abnormityList.size()>0){
						   request.setAttribute("abnormity", abnormityList);
					   }
					   //如果迟报天数大于等于0，则返回包含记录的request对象
					   if(laterReportDay>=0){
						  request.setAttribute("day", reportInParticularForm.getLaterReportDay());
					   }
				   }
				 
			   }catch(Exception e){
				   log.printStackTrace(e);
			   }
		   }
		   return mapping.findForward("viewResult");
	   }
	   
	   
}
