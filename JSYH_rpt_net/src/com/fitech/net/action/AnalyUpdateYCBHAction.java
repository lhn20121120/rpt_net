package com.fitech.net.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.action.SaveYCBHAction;
import com.cbrc.smis.adapter.StrutsAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsColAbnormityChangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AbnormityChangeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
/**
 * 异常变化修改的更新
 * 
 * @author rds
 * @serialData 2005-12-24 17:04
 */
public class AnalyUpdateYCBHAction extends Action {

	private FitechException log=new FitechException(AnalyUpdateYCBHAction.class);
	
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);		
		HttpSession session=request.getSession();		
		FitechMessages messages = new FitechMessages();		
		AbnormityChangeForm abnormityChangeForm=(AbnormityChangeForm)form;
		RequestUtils.populate(abnormityChangeForm,request);
		
		String orgIds=abnormityChangeForm.getOrgCls();
		ArrayList list=new ArrayList();
		if(orgIds!=null){
			String[] arrOrgIds=orgIds.split(",");
			for(int i=0;i<arrOrgIds.length;i++){
				list.add(arrOrgIds[i]);				
			}
		}		
		
		HashMap hMap=new HashMap();
		if(session.getAttribute("SelectedOrgIds")!=null){
			session.setAttribute("SelectedOrgIds",null);       
		}	
		//加上选中的项
		if(orgIds!=null){
			String orgId[]=orgIds.split(",");
			 for(int i=0;i<orgId.length;i++)
	         {
	             hMap.put(orgId[i].trim(),"ok");
	                          
	         }
		}
		session.setAttribute("SelectedOrgIds",hMap);		 
		boolean flag=false;
		
		try{
			if(abnormityChangeForm.getStandard()==null){ //获取异常变化设定标准失败
				messages.add(FitechResource.getMessage(locale,resources,"ycbh.get.standard.failed"));
			}else if(abnormityChangeForm.getReportStyle()==null ||
					abnormityChangeForm.getChildRepId()==null ||
					abnormityChangeForm.getVersionId()==null){  //获取需要设定异常变化标准的报表信息失败
				messages.add(FitechResource.getMessage(locale,resources,"ycbh.get.report.failed"));
			}else{
				SaveYCBHAction saveYCBHAction=new SaveYCBHAction();
			//	List orgList=saveYCBHAction.getOrgList(abnormityChangeForm.getOrgCls(),session);
				List forms=saveYCBHAction.getACFormList(abnormityChangeForm.getStandard(),
						abnormityChangeForm.getChildRepId(),
						abnormityChangeForm.getVersionId(),
						abnormityChangeForm.getReportStyle());
				// System.out.println("前"+orgList.size());
			
				String curOrgId=((Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId();
			//去除没有父机构的
//			orgList=StrutsOrgNetDelegate.selectAllLowerOrgListBBB(orgList,((Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId());
				// System.out.println("后"+orgList.size());
				/*// System.out.println("orgList.size:" + orgList.size());
				// System.out.println("forms.size:"  + forms.size());*/
				if(abnormityChangeForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD)==0){ //点对点
					flag=StrutsAbnormityChangeDelegate.updatePatch(curOrgId,forms);
				}
				
				if(flag==true){ //操作成功
					messages.add(FitechResource.getMessage(locale,resources,"ycbh.save.success"));
				}else{ //操作失败
					messages.add(FitechResource.getMessage(locale,resources,"ycbh.save.failed"));
				}
			}
		}catch(Exception e){ //系统错误
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale,resources,"errors.system"));
			return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
		}
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
		request.setAttribute("ObjForm",abnormityChangeForm);
		
		request.setAttribute("ChildRepId",abnormityChangeForm.getChildRepId());
		request.setAttribute("VersionId",abnormityChangeForm.getVersionId());
		request.setAttribute("ReportStyle",abnormityChangeForm.getReportStyle());
		request.setAttribute("ReportName",abnormityChangeForm.getReportName());
		request.setAttribute("curPage",FitechUtil.getRequestParameter(request,"curPage"));
		
		if(flag==true){	 //更新成功
			//return mapping.findForward("view");
			String curPage = "1";
			if(request.getParameter("curPage") != null) curPage = request.getParameter("curPage");
			return new ActionForward("/analysis/viewMChildReport.do?reportName=&childRepId=&versionId=&curPage=" + curPage);
		}else{  //更新失败
			return mapping.getInputForward();
		}
	}
	
}
