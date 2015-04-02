package com.fitech.gznx.action;

import java.io.IOException;
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

import com.cbrc.smis.action.ViewMChildReportAction;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AddcheckForm;
import com.fitech.gznx.form.ReportCheckForm;

import com.fitech.gznx.service.DeleteCheck;
import com.fitech.gznx.service.ReportCheckView;
import com.fitech.gznx.service.StrutsAFCellFormuDelegate;


public class DeleteCheckAction extends Action{
	private static FitechException log = new FitechException(DeleteCheckAction.class);
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		HttpSession session = request.getSession();
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);

		FitechMessages messages = new FitechMessages();
		AddcheckForm addcheckform = (AddcheckForm) form;
       
        RequestUtils.populate(addcheckform, request);
        /** 报表选中标志 **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
        String templateId = request.getParameter("templateId");
		String versionId = request.getParameter("versionId");
		boolean addcheck =true;
		try{
			String cellFormuIds=null;
			
			if(request.getParameter("cellFormuIds")!=null){
				cellFormuIds=(String)request.getParameter("cellFormuIds");
			
				if(StrutsAFCellFormuDelegate.delete(cellFormuIds,templateId,versionId,reportFlg)==true){
					messages.add(FitechResource.getMessage(locale,resources,"delete.success","bjgx.info"));
				}else{
					messages.add(FitechResource.getMessage(locale,resources,"delete.failed","bjgx.info"));
				}
			}
		}catch(Exception e){
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale,resources,"errors.system"));
			
		}

		String formulaId=(String)request.getParameter("formulaId");

		if(!StringUtil.isEmpty(formulaId)){
	
			 try{
				 //flag = DeleteCheck.ValidateFlag(templateId,versionId,formulaName,formulaValue);
				 addcheck =DeleteCheck.deletecheck(templateId,versionId,formulaId);
	         }catch (Exception e){
	             log.printStackTrace(e);
	             messages.add(resources.getMessage("log.select.fail"));      
	         }
		}
         if( addcheck != true){
        	 return mapping.findForward("check");
         }else{
        	 return mapping.findForward("deletecheck");
         }
	}
	
}

