package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsVParameterDelegate;
import com.fitech.net.form.VParameterForm;
/**
 * ��������²�����Action����
 *
 */
public final class UpdateVParameterAction extends Action {
	private static FitechException log = new FitechException(UpdateVParameterAction.class);
   /**
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException�Ƿ�������/������쳣
    * @exception ServletException�Ƿ���servlet���쳣ռ��
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	   
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();
	   	   
		VParameterForm vParamForm = (VParameterForm) form;
		
		String curPage = "";
		if(request.getParameter("curPage")!=null) 
			curPage = (String)request.getParameter("curPage");
			
		boolean updateResult = false;
		
		try {
			if (vParamForm != null) {
				updateResult = StrutsVParameterDelegate.update(vParamForm);
				if (updateResult == true){	//���³ɹ�
					messages.add(resources.getMessage("update.vParameter.success"));	
				}
                else{
                	messages.add(resources.getMessage("update.vParameter.failed"));	
				}
			}
		} 
        catch (Exception e) 
        {
			updateResult=false;
			messages.add(resources.getMessage("update.vParameter.failed"));	
			log.printStackTrace(e);
		}

		FitechUtil.removeAttribute(mapping,request);
			
		//�ж�������ʾ��Ϣ�����н���洢��Request�����У���������
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		        
		String path="";
		if(updateResult==true){
			form = null;
			path = mapping.findForward("update").getPath() + 
				"?curPage=" + curPage + "&vpId=" + vParamForm.getVpId();
		}else{
			path = mapping.getInputForward().getPath();
		}
		
		path= path==null? mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath() : path;
		
		return new ActionForward(path);
	}
  }
