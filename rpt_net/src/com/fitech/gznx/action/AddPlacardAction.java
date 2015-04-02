package com.fitech.gznx.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.PlacardForm;
import com.fitech.gznx.service.StrutsPlacardDelegate;
import com.gather.common.DateUtil;


public class AddPlacardAction extends Action {
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		FitechMessages messages = new FitechMessages();
		PlacardForm placardForm = (PlacardForm) form;
		RequestUtils.populate(placardForm, request);
		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_NAME);
		else
			operator = new Operator();
		boolean result = false;
		try
		{
			
			if(placardForm!=null && !StringUtil.isEmpty(placardForm.getContents()) 
					&& !StringUtil.isEmpty(placardForm.getTitle()) && !StringUtil.isEmpty(placardForm.getUserIdStr())){
				placardForm.setPublicUserId(String.valueOf(operator.getOperatorId()));
				placardForm.setPublicDate(DateUtil.getToday("yyyy-mm-dd"));
				if(placardForm.getPlacardFile().getFileSize() < Config.FILE_MAX_SIZE){
				/** �����¼* */
				result = StrutsPlacardDelegate.create(placardForm);
				//	 �Ƴ�request��session��Χ�ڵ�����
				FitechUtil.removeAttribute(mapping, request);
				if(result==true)
				{
					messages.add("���湫����Ϣ�ɹ�");
					FitechLog.writeLog(Config.LOG_APPLICATION,operator.getUserName(),"���湫����Ϣ�ɹ�");
				}
				else
				{
					messages.add("���湫����Ϣʧ��");
					FitechLog.writeLog(Config.LOG_APPLICATION,operator.getUserName(),"���湫����Ϣʧ��");
					if (messages.getMessages() != null && messages.getMessages().size() > 0)
						request.setAttribute(Config.MESSAGES, messages);					
				}
				}else
				{
					messages.add("�ļ���С�������ƣ�ֻ������С��4M���ļ���");
				}
			}
		}
		catch (Exception e)
		{
			messages.add("���湫����Ϣʧ��");
			FitechLog.writeLog(Config.LOG_APPLICATION,operator.getUserName(),"���湫����Ϣʧ��");
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			
		}
		if(!result){
			String operatorId = String.valueOf(operator.getOperatorId());
			// �����ӡ�ļ���
    		String deleteFilePath = Config.WEBROOTPATH +"xml"+ File.separator + operatorId;
       		File outfile = new File(deleteFilePath);
       		if(outfile.exists())
    			StringUtil.deleteUploadFile(deleteFilePath);    		
    		outfile.mkdir();
    		String filePath = deleteFilePath + File.separator + "placardadduser.xml";
			StrutsPlacardDelegate.createUserTree(filePath,new HashMap());
			
			request.setAttribute("placrdadduser", operatorId+ "/placardadduser.xml");
			return mapping.findForward("add");
		}
		
		
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		return new ActionForward("/placard_mgr/viewPlacardAction.do?title=&startDate=&endDate=");
	}
}
