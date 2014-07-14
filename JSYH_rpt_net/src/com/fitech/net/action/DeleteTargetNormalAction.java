package com.fitech.net.action;

import java.io.IOException;
import java.util.List;
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

import com.cbrc.smis.action.DeleteLogInAction;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.form.TargetNormalForm;

/**
 * ָ��ҵ��ɾ��action��ʵ��
 * 
 * @author masclnj
 * 
 */
public final class DeleteTargetNormalAction extends Action {
    private static FitechException log = new FitechException(
            DeleteLogInAction.class);

    /**
     * @"view" mapping Action
     * @return request request����
     * @return response respponse����
     * @exception IOException
     *                �Ƿ���IO�쳣��������׽���׳�
     * @exception ServletException
     *                �Ƿ�Servlet�쳣��������׽���׳�
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // mRepFreqForm MCurrForm�ĳ�ʼ��
      //  MCurrForm mCurrForm = new MCurrForm();
        TargetNormalForm targetNormal = (TargetNormalForm)form;
        // ��request�����mCurrForm
        RequestUtils.populate(targetNormal, request);
        
            Locale locale=getLocale(request);
           MessageResources resources=this.getResources(request);
           FitechMessages messages=new FitechMessages();
        try
        {
        	Integer id=targetNormal.getNormalId();
        	List list=com.fitech.net.adapter.StrutsTargetDelegate.selectNormalInfo(id.toString());
        	 boolean result=false;
        	if(list.size()==0)
        		result=true;
        	else{
        		result=false;
        		messages.add("��ָ��ҵ����ʹ��");
        	}
        	if(result)
        		result = com.fitech.net.adapter.StrutsTargetDelegate
                    .remove(targetNormal);
           if(result)
            {
                messages.add("ɾ���ɹ�");
            }
           else if(messages.getMessages() == null || messages.getMessages().size() == 0)
               messages.add("ɾ��ʧ��");
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);

            messages.add("ɾ��ʧ��");
        }

        if(messages.getMessages() != null && messages.getMessages().size() != 0)
               request.setAttribute(Config.MESSAGES,messages);
        String path = "/target/viewTargetNormal.do";
/*
        Integer curId = mCurrForm.getCurId();
        String curName = mCurrForm.getCurName();

        if (curId != null) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "curId=" + curId.toString();
        }
        if (curName != null && !curName.equals("")) {
            path += (path.indexOf("?") >= 0 ? "&" : "?");
            path += "curName=" + curName.toString();
        }

        // System.out.println("Path= " + path);*/
        return new ActionForward(path);
    }
}
