package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.RangeTempForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;

/**
 * 生成银监局报送范围数据
 * 
 * @author jcm
 * @serialData 2006-03-31
 */
public class InsertRangeAction extends Action {
	private FitechException log=new FitechException(InsertRangeAction.class);
	
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
		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
				
		RangeTempForm rangeTempForm=new RangeTempForm();
		RequestUtils.populate(rangeTempForm,request);
		
		try {
	   		if(StrutsMRepRangeDelegate.insertRange(rangeTempForm)){
	   			messages.add(resources.getMessage("insert.range.success"));
	   		}else{
	   			messages.add(resources.getMessage("insert.range.failed"));
	   		}
		}	
		catch (Exception e) {
			log.printStackTrace(e);
			messages.add(resources.getMessage("insert.range.failed"));		
		}
		if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);		
		return mapping.findForward("viewRange");
	}
}
