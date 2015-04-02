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
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.form.MOrgClForm;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;



/**
 *  ���ݷ�Χ�鿴
 * @author Ҧ��
 *
 * @struts.action
 *    path="/template/viewMRepRange"
 *
 * @struts.action-forward
 *    name="tbfw"
 *    path="/template/view/tbfw.jsp"
 *    redirect="false"
 *

 */
public final class ViewMRepRangeAction extends Action {
	 private static FitechException log = new FitechException(ViewMRepRangeAction.class);
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException 
   {

        FitechMessages messages = new FitechMessages();
        MessageResources resources = getResources(request);
    
        // �Ƿ���Request
        MOrgClForm mOrgClForm = new MOrgClForm();
        RequestUtils.populate(mOrgClForm,request);
        
        String childRepId = mOrgClForm.getChildRepId();
        String versionId = mOrgClForm.getVersionId();
        
        childRepId=request.getParameter("childRepId");
        versionId=request.getParameter("versionId");
        String orgId=request.getParameter("orgId");
        String curOrgId=request.getParameter("curOrgId");
        List resList = null;
    
        try 
        {
        	
        	
        	
        	resList=StrutsOrgNetDelegate.selectLowerOrgListInMRepRange(orgId,childRepId,versionId);
           // resList = StrutsMOrgClDelegate.findAll();
          /**ȡ�û�������id��*/
        //   String orgClsString =  StrutsMRepRangeDelegate.getOrgClsString(childRepId,versionId);
           //// System.out.println("CLS IDS====" + orgClsString);
           /**��ѯ�û���id������Ӧ�Ļ���*/
         //  resList = StrutsMOrgClDelegate.findOrgCls(orgClsString);
        } 
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(resources.getMessage("BSFW.select.failed"));
        }
        //�Ƴ�request��session��Χ�ڵ�����
        FitechUtil.removeAttribute(mapping,request);
    
        if (messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        //���StrutsMOrgClDelegate���з��ص�reslist����Ϊ�ղ��Ҷ���Ĵ�С����0��
        //�򷵻�һ������reslist���ϵ�request����
        if (resList != null && resList.size() > 0){
            request.setAttribute("lowerOrgList",resList);
        }
        request.setAttribute("orgId",orgId);
        request.setAttribute("reportName", mOrgClForm.getReportName());
        request.setAttribute("childRepId",childRepId);
        request.setAttribute("versionId",versionId);
        request.setAttribute("curOrgId",curOrgId);
        //���ص�ҳ��view     
        return mapping.findForward("tbfw");
   }
}
