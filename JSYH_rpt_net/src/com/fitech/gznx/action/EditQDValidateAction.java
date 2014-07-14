package com.fitech.gznx.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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

import com.cbrc.smis.action.ViewTemplateDetailAction;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

import com.fitech.gznx.form.QDValidateForm;

import com.fitech.gznx.service.AFQDValidateFormulaDelegate;


public class EditQDValidateAction extends Action {
	   private static FitechException log = new FitechException(ViewTemplateDetailAction.class);

	    /** 
	     * Method execute
	     * @param mapping
	     * @param form
	     * @param request
	     * @param response
	     * @return ActionForward
	     */
	    public ActionForward execute(
	        ActionMapping mapping,
	        ActionForm form,
	        HttpServletRequest request,
	        HttpServletResponse response)  
	    throws IOException, ServletException
	   {
	        MessageResources resources = getResources(request);
	        FitechMessages messages = new FitechMessages();
	
	        QDValidateForm qdValidateForm=(QDValidateForm) form;
			RequestUtils.populate(qdValidateForm,request);
			String formulaId  = request.getParameter("formulaId");
			String vFlg = request.getParameter("vFlg");
			qdValidateForm.setVFlg(vFlg);
	        if(qdValidateForm.getVFlg()!= null && qdValidateForm.getVFlg().equals("update")){
	        	return updateValidate(qdValidateForm,mapping,request);
	        }
			try{
				qdValidateForm = AFQDValidateFormulaDelegate.getValidateFormula(formulaId);	
            }catch (Exception e) 
            {
                log.printStackTrace(e);
                messages.add(resources.getMessage("log.select.fail"));      
            }

	         if(messages.getMessages() != null && messages.getMessages().size() > 0)
	           request.setAttribute(Config.MESSAGES,messages);
	         request.setAttribute("templateId", qdValidateForm.getTemplateId());
	         request.setAttribute("versionId", qdValidateForm.getVersionId());
	         request.setAttribute("form1", qdValidateForm);
	         //将查询条件存放在request范围内
	        
	         return mapping.findForward("view");
	    }

		private ActionForward updateValidate(QDValidateForm qdValidateForm,ActionMapping mapping,HttpServletRequest request) {
			
			FitechMessages messages = new FitechMessages();

			boolean result = false;
			result = AFQDValidateFormulaDelegate.updateValidateFormula(qdValidateForm);
			
			if(result){

				if(messages.getSize()==0)
				messages.add("修改模板信息成功");
				request.setAttribute(Config.MESSAGES, messages);
						
				String url = "/gznx/viewQDValidateList.do?templateId=" + qdValidateForm.getTemplateId() + "&versionId=" + qdValidateForm.getVersionId(); 
				return new ActionForward(url);

				
			} else{
				messages.add("修改模板信息失败");
				if (messages.getMessages() != null && messages.getMessages().size() > 0)
					request.setAttribute(Config.MESSAGES, messages);
				
				String url = "/gznx/editQDValidate.do?templateId=" + qdValidateForm.getTemplateId() + "&versionId=" + qdValidateForm.getVersionId()+"&formulaId="+qdValidateForm.getFormulaId(); 
				return new ActionForward(url);
			}
				
		}
}
