package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsTargetDelegate;
import com.fitech.net.form.TargetNormalForm;
/**
 * ����ָ�����������Action����
 *
 * @author masclnj

 */
public final class UpdateTargetNormalAction extends Action {
	private static FitechException log = new FitechException(UpdateTargetNormalAction.class);
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
	   
		FitechMessages messages=new FitechMessages();
		TargetNormalForm targetNormal = (TargetNormalForm)form;
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
		boolean updateResult = false;
		
		try {
			if (targetNormal != null) {
				if(StrutsTargetDelegate.isMNormalExist(targetNormal.getNormalName()) == false){
					updateResult = com.fitech.net.adapter.StrutsTargetDelegate.update(targetNormal);
					if (updateResult == true){	//���³ɹ�
						messages.add("���³ɹ�");
					}else{						//����ʧ��
						messages.add("����ʧ��");
					}
				}else{
					messages.add("ָ��ҵ�������Ѿ����ڣ�");
					updateResult = true;
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		//�ж�������ʾ��Ϣ�����н���洢��Request�����У���������
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		
		String path="";
		if(updateResult==true){	//�ɹ������ر����б�ҳ
			form = null;
			path = mapping.findForward("update").getPath() +
				"?curPage=" + curPage + 
				"&normalName=";
		}else{					//ʧ�ܣ������ύҳ
			path = mapping.getInputForward().getPath();
		}
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
		
		return new ActionForward(path);
	}
}