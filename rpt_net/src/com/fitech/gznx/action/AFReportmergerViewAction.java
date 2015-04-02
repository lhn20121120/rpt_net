//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;

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

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.form.AFGatherformulaFrom;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.po.AfGatherformula;
import com.fitech.gznx.service.AFReportmergerView;

/** 
 * ģ����ϸ��Ϣ��ʾ
 * @author Ҧ��
 * Creation date: 12-15-2005
 * 
 * XDoclet definition:
 * @struts.action path="/template/viewTemplateDetail" name="mChildReportForm" scope="request" validate="true"
 * @struts.action-forward name="templateDetailView" path="/template/view/templateDetailView.jsp"
 */
public class AFReportmergerViewAction extends Action {
    private static FitechException log = new FitechException(AFReportmergerViewAction.class);

    /** 
     * ��ʹ��hibernate ���Ը� 2011-12-22
     * Ӱ�����AfGatherformula AfCellinfo 
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
    	HttpSession session = request.getSession();
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        
         List list = null;
         AFGatherformulaFrom afgatherformulaform = (AFGatherformulaFrom) form;
         RequestUtils.populate(afgatherformulaform, request);
         String templateId = request.getParameter("templateId");
 		 String versionId = request.getParameter("versionId");
 		 AfGatherformula afgather = new AfGatherformula();

         try {
             //��ʾ��ҳ��ļ�¼
        	 /**��ʹ��hibernate ���Ը� 2011-12-22
        	  * Ӱ�����AfGatherformula AfCellinfo **/
        	 list=AFReportmergerView.getList(templateId,versionId);
         }catch (Exception e) {
             log.printStackTrace(e);
             messages.add(resources.getMessage("log.select.fail"));      
         }
         //�Ƴ�request��session��Χ�ڵ�����
         FitechUtil.removeAttribute(mapping,request);
         //��ApartPage��������request��Χ��
  
         if(messages.getMessages() != null && messages.getMessages().size() > 0)
           request.setAttribute(Config.MESSAGES,messages);
         if(list!=null && list.size()!=0)
           request.setAttribute(Config.RECORDS,list);
         
         return mapping.findForward("guibin");
    }

 }
