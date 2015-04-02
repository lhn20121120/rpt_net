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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
/**
 * ���±��ֲ�����Action����
 *
 * @author ����
 *
 * @struts.action
 *    path="/struts/updateMCurr"
 *    name="MCurrForm"
 *    scope=request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMCurr.do"
 *    redirect="false"
 */
public final class UpdateMCurrAction extends Action {
	private static FitechException log = new FitechException(UpdateMCurrAction.class);   

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
   )throws IOException, ServletException {
	   
		Locale locale=getLocale(request);	   
		MessageResources resources=this.getResources(request);	   
		FitechMessages messages=new FitechMessages();	   
		MCurrForm mCurrForm = (MCurrForm) form;
		
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		boolean updateResult = false;
		
		try {
			if (mCurrForm != null) {
				updateResult = com.cbrc.smis.adapter.StrutsMCurrDelegate.update(mCurrForm);
				if (updateResult == true){	//���³ɹ�
					messages.add(FitechResource.getMessage(locale,resources,
							"update.success","curr.info"));
				}else{						//����ʧ��
					messages.add(FitechResource.getMessage(locale,resources,
							"update.failed","curr.info"));
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}

		FitechUtil.removeAttribute(mapping,request);
			
		//�ж�������ʾ��Ϣ�����н���洢��Request�����У���������
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		
		String path="";
		if(updateResult==true){	//�ɹ������ر����б�ҳ
			form = null;
			path = mapping.findForward("update").getPath() + 
				"?curPage=" + curPage + 
				"&curName=";
		}else{					//ʧ�ܣ������ύҳ
			path = mapping.getInputForward().getPath();
		}
		
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
		
		return new ActionForward(path);
	}  
}
