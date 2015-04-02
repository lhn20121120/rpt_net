package com.fitech.gznx.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.CustomViewForm;
import com.fitech.gznx.service.StrutsViewCustom;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.util.ReportUtils;

public class ViewCustomAFReportDetailAction extends Action {
	private static FitechException log = new FitechException(ViewCustomAFReportDetailAction.class);

    /** 
     * ��ʹ��hibernate  ���Ը� 2011-12-21
	 * Ӱ�����MActuRep OrgNet AfTemplateOrgRelation ||MRepRange ,AfOrg || OrgNet
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		HttpSession session = request.getSession();
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		Long operatorId = operator.getOperatorId();
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        //ȡ��request��Χ�ڵ�����������������logInForm��
        CustomViewForm customViewForm = (CustomViewForm) form;
        RequestUtils.populate(customViewForm, request);
        String templateId=request.getParameter("templateId");
        String versionId=request.getParameter("versionId");
        String templateName=request.getParameter("templateName");
        String forwardflg = request.getParameter("forwardflg");
        String backQry = request.getParameter("backQry");
        String[] backQrys = null;
        if(backQry != null){
        backQrys = backQry.split("_");
        if(backQrys.length == 2)
        	backQry = "templateType="+backQrys[0]+"&templateName="+backQrys[1];
        else if(backQrys.length ==1)
        	backQry = "templateType="+backQrys[0];
        else backQry="templateType=&templateName=";
        }
        request.setAttribute("backQry", backQry);
        if(customViewForm == null){
        	customViewForm = new CustomViewForm();
        }
		if(!StringUtil.isEmpty(templateId))
			customViewForm.setTemplateId(templateId);
		if(!StringUtil.isEmpty(versionId))
			customViewForm.setVersionId(versionId);
		if(!StringUtil.isEmpty(templateName))
			customViewForm.setTemplateName(templateName);
		customViewForm.setReportFlg(reportFlg);
		try {
			//��ѯ ���б���
			List<MCurrForm> currList = StrutsMCurrDelegate.findAll();
			//��ѯ����Ƶ��
			List<MDataRgTypeForm> datargList = StrutsMDataRgTypeDelegate.findAll();
			//��ѯ����ģ����Ϣ
			List<MChildReportForm> reportList = StrutsMChildReportDelegate.findAll();
			//����reqeuest������
			request.setAttribute("currList", currList);
			request.setAttribute("datargList", datargList);
			request.setAttribute("reportList", reportList);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(!StringUtil.isEmpty(forwardflg) && forwardflg.equals("1")){
			// �����ӡ�ļ���
    		String deleteFilePath = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ File.separator + operatorId;
       		File outfile = new File(deleteFilePath);
       		if(outfile.exists())
    			deleteUploadFile(deleteFilePath);
    		outfile.mkdir();
    		
			String cellIds = customViewForm.getMeaStr();
			String startDate = customViewForm.getStartDate();
			String endDate = customViewForm.getEndDate();
			String orgIds = customViewForm.getOrgList();
			String curId = customViewForm.getCurId();
			String repFreqId = customViewForm.getRepFreqId();
			String datarangeId = "1";
			String raqFileName = "";
//			AfTemplate template = AFTemplateDelegate.getTemplate(templateId,versionId);
//			if(StrutsCodeLibDelegate.getCodeLib(com.fitech.gznx.common.Config.CUSTOM_SEARCH,"1").getCodeName().equals(template.getTemplateName())){
//				if(orgIds.indexOf(",")<0){
//					raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq"+ 
//					File.separator + "custom_view_subject_s.raq";
//				} else {
//					raqFileName = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "Raq"+ 
//					File.separator + "custom_view_subject.raq";
//				}	
//			} else {
				if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
					/**��ʹ��hibernate  ���Ը� 2011-12-21
					 * Ӱ�����MActuRep**/
					datarangeId = StrutsViewCustom.getdatarangeId(templateId,versionId,repFreqId);
					if(StringUtil.isEmpty(datarangeId)){
						datarangeId = "1";
					}
					if(orgIds.indexOf(",")<0){
						raqFileName = Config.WEBROOTPATH +"templateFiles" + File.separator + "Raq"+ 
						File.separator + "custom_view_cbrc_s.raq";
					} else {
						raqFileName = Config.WEBROOTPATH +"templateFiles" + File.separator + "Raq"+ 
						File.separator + "custom_view_cbrc.raq";
					}
				} else if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
					if(orgIds.indexOf(",")<0){
						raqFileName = Config.WEBROOTPATH +"templateFiles" + File.separator + "Raq"+ 
						File.separator + "custom_view_pboc_s.raq";
					} else {
						raqFileName = Config.WEBROOTPATH +"templateFiles" + File.separator + "Raq"+ 
						File.separator + "custom_view_pboc.raq";
					}
					
				} else if(reportFlg.equals(com.fitech.gznx.common.Config.OTHER_REPORT)){
					if(orgIds.indexOf(",")<0){
						raqFileName = Config.WEBROOTPATH +"templateFiles" + File.separator + "Raq"+ 
						File.separator + "custom_view_other_s.raq";
					} else {
						raqFileName = Config.WEBROOTPATH +"templateFiles" + File.separator + "Raq"+ 
						File.separator + "custom_view_other.raq";
					}
					
				}
//			}
			
			
			try {
				ReportDefine rd = (ReportDefine) ReportUtils.read(raqFileName);
				raqFileName = deleteFilePath + 
				File.separator+templateId+"_"+versionId + ".raq";
				ReportUtils.write(raqFileName,rd);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			Map report = new HashMap();
			report.put("templateId",templateId);
			report.put("versionId",versionId);
			report.put("cellIds",cellIds);
			report.put("orgIds",orgIds);
			report.put("curId",curId);
			report.put("datarangeId",datarangeId);
			report.put("startDate",startDate.replace("-", ""));
			report.put("endDate",endDate.replace("-", ""));
			report.put("repFreqId",repFreqId);
			report.put("filename",operatorId + "/"+templateId+"_"+versionId + ".raq");
         	request.setAttribute("reportParam", report);
			//String url = "/gznx/customview/viewCustomReport.jsp?";
			return mapping.findForward("view");
//			ActionForward af = new ActionForward(url);
//			return af;
         //����ѯ���������request��Χ��
		}else{
			String xmlorgname = FitechUtil.getObjectName()+"bsfw.xml";
			String fileName = Config.WEBROOTPATH + "xml" + File.separator + xmlorgname; // ����һ��XML�ļ�
			
			// ���ɻ�����
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ�����OrgNet AfTemplateOrgRelation ||MRepRange ,AfOrg || OrgNet**/
			StrutsViewCustom.createOrgTree(session,fileName,templateId,versionId);
			request.setAttribute("xmlorgname", xmlorgname);
			request.setAttribute("form",customViewForm);			
			return mapping.findForward("index");
		}
		
	}
    // ɾ���ϴ���ZIP�ļ�����ѹ�ļ���
    private void deleteUploadFile(String filePath) {

		File f = new File(filePath);
		if (f.isDirectory()) {
		    File[] fileList = f.listFiles();
		    if(fileList != null && fileList.length>0){
			    for (int i = 0; i < fileList.length; i++) {
					File excelFile = fileList[i];			
					excelFile.delete();
			    }
		    }
		    f.delete();
		}			
    }

}
