package com.fitech.net.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.excel.DB2Excel;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsCollectDelegate;
import com.fitech.net.adapter.StrutsReportInDelegate;
import com.fitech.net.config.Config;


/**
*
* <p>Title: TranslationReportAction </p>
*
* <p>Description ��������ת�ϱ�����Action</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2009-09-14
* @version 1.0
*
*/
public final class TranslationReportAction extends Action {
	
   /**
    * @param    mapping         ActionMapping
    * @param    form            ActionForm
    * @param    request         HttpServletRequest
    * @param    response        HttpServletResponse
    * IOEception  ���쳣��׽���׳�
    * SeverletException
    */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
			,HttpServletResponse response)throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		ReportInForm reportInForm = (ReportInForm)form ;	   
		RequestUtils.populate(reportInForm, request);
        
     //�¶�ע�͵�����Ϊ���÷���������·����벻����ɹ�����������ô˶δ������
     /* String childRepId = null;
        String versionId  = null;
        String reportName = null;
        String dataRangeId = null;
        String dataRangeDesc = null;
        String year = null;
        String month = null;
        String curId = null;
        //�����ϱ��������
        CollectReportPK key = new CollectReportPK();
       
        //��������ʵ���ӱ���Id
         if(request.getParameter("childRepId") != null){
            childRepId = request.getParameter("childRepId");
            key.setChildRepId(childRepId);
         }
         //�汾��
        if(request.getParameter("versionId") != null){
            versionId  = request.getParameter("versionId");
            key.setVersionId(versionId);
        }
        //������
        if(request.getParameter("reportName") != null)
            reportName = request.getParameter("reportName");
        //���ݷ�ΧId
        if(request.getParameter("dataRangeId") != null)
            dataRangeId = request.getParameter("dataRangeId");
        //���ݷ�Χ����
        if(request.getParameter("dataRanageDesc") != null)
            dataRangeDesc = request.getParameter("dataRanageDesc");
        //�������
        if(request.getParameter("year") != null){
            year = request.getParameter("year");
            key.setYear(new Integer(year));
        }
        //��������
        if(request.getParameter("month") != null){
            month = request.getParameter("month");
            key.setTerm(new Integer(month));
        }
        //����Id
        if(request.getParameter("curId") != null)
            curId = request.getParameter("curId");*/
       
		/** ȡ�õ�ǰ�û���Ȩ����Ϣ */
		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
        Integer repInId = reportInForm.getRepInId();       
    //    if(operator != null)
    //         key.setOrgId(operator.getOrgId());
        
        //���ݻ���������Ƿ��ж�Ӧ�ı��ڵ��ѻ��ܵ������ж��Ƿ�ת�ϱ�����
//        CollectReport report = StrutsCollectReportDelegate.findById(key);
        
        //if(report == null){
         //   messages.add("û�л������ݣ�����ת�ϱ����ݣ�");
       // ���ݻ���������Ƿ��ж�Ӧ�ı��ڵ��ѻ��ܵ������ж��Ƿ�ת�ϱ�����
        if(repInId == null){
                messages.add("û�л������ݣ�����ת�ϱ����ݣ�");
        }else{
            //ReportIn reportIn = StrutsReportInDelegate.findById(report.getRepInId());
            //����������ת�ϱ����ݵļ�¼
            ReportIn _reportIn = StrutsReportInDelegate.findHasTranslation(repInId,operator.getOrgId());
            if(_reportIn != null && Config.CHECK_FLAG_PASS.equals(_reportIn.getCheckFlag())){
                messages.add("���ڱ�������ˣ�����ת�ϱ����ݣ�");
            }else if(_reportIn != null && Config.CHECK_FLAG_UNCHECK.equals(_reportIn.getCheckFlag())){
                messages.add("���ڱ������ϱ�������ת�ϱ�����");
            }else{       
                ReportIn reportIn = StrutsReportInDelegate.findById(repInId);
               // ExecuteCollect collect = new ExecuteCollect();
                DB2Excel db2Excel=new DB2Excel(reportIn.getRepInId());
                if(!new File(Config.getReleaseTemplatePath()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getYear()+"_"+reportIn.getTerm()+com.cbrc.smis.common.Config.FILESEPARATOR+operator.getOrgId()).exists()){
                	new File(Config.getReleaseTemplatePath()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getYear()+"_"+reportIn.getTerm()).mkdir();
                	new File(Config.getReleaseTemplatePath()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getYear()+"_"+reportIn.getTerm()+com.cbrc.smis.common.Config.FILESEPARATOR+operator.getOrgId()).mkdir();
                }
                boolean createExcel=db2Excel.createExcel(Config.getReleaseTemplatePath()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getYear()+"_"+reportIn.getTerm()+com.cbrc.smis.common.Config.FILESEPARATOR+operator.getOrgId()+com.cbrc.smis.common.Config.FILESEPARATOR+reportIn.getMChildReport().getComp_id().getChildRepId()+"_"+reportIn.getMChildReport().getComp_id().getVersionId()+".xls");
                
                //if(createExcel&&collect.collectReports(childRepId,versionId,reportName,new Integer(dataRangeId),dataRangeDesc,Integer.parseInt(year),Integer.parseInt(month),operator,new Integer(curId),false,report.getRepInId())){
                if(createExcel && StrutsCollectDelegate.transCollectDatat(reportIn)){   
                	messages.add("��������ת�ϱ����ݳɹ���");
                }
            }
        }
        
        String term = mapping.findForward("view").getPath();
        /**���뱨��������*/
		if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "childRepId=" + reportInForm.getChildRepId();
		}
		/**���뱨����������*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			/**����WebLogic����Ҫ����ת�룬ֱ����Ϊ��������*/
			term += "repName=" + reportInForm.getRepName();
			/**����WebSphere����Ҫ�Ƚ���ת�룬����Ϊ��������*/
			//term += "repName=" + new String(reportInForm.getRepName().getBytes("gb2312"), "iso-8859-1");
		}
		/**����ģ����������*/
		if(reportInForm.getFrOrFzType() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "frOrFzType=" + reportInForm.getFrOrFzType();
		}			
		/**���뱨���������*/
		if(reportInForm.getYear() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "year=" + reportInForm.getYear();				
		}
		/**���뱨����������*/
		if(reportInForm.getTerm() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "term=" + reportInForm.getTerm();
		}
        request.setAttribute(Config.MESSAGE,messages);
	 	return  new ActionForward(term);
	}
}
