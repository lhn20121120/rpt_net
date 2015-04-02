 
package com.fitech.net.template.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AbnormityChangeForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.ETLReportForm;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.form.ReprotExcelMappingForm;
/**
 * ����Excel
 * @author wh
 *
 */
public final class EtlDataBuildAction extends Action {
  private static FitechException log = new FitechException(EtlDataBuildAction.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
                               HttpServletResponse response) throws IOException, ServletException {
	  
	  Locale locale = getLocale(request);
		MessageResources resources = getResources(request);		
		HttpSession session=request.getSession();		
		FitechMessages messages = new FitechMessages();		
		ETLReportForm eTLReportForm=(ETLReportForm)form;
		RequestUtils.populate(eTLReportForm,request);

    /** �Ƿ����������б��� */
    String           isAllRep   = "";
    /** ������������������Ϣ */
    String           childRepId = "";
    String           versionId   = "";
    String           year   = "";
    String 		     month  = "";
    /** ���ɻ���ID  */
    String              orgIdStr = "";

    /* �����Ӧ�Ĳ��� */
    if (request.getParameter("isAllRep") != null) {
      isAllRep = request.getParameter("isAllRep");
    }

    if (request.getParameter("childRepId") != null) {
        String[] childAndVersion =  request.getParameter("childRepId").split("_");
        if(childAndVersion.length>1 && !"".equals(childAndVersion[0])){
	        childRepId =childAndVersion[0];
	        versionId =childAndVersion[1];
        }
      }

    if (request.getParameter("year") != null) {
    	year = request.getParameter("year");
    } 
    if (request.getParameter("month") != null) {
    	month = request.getParameter("month");
    }

    if (request.getParameter("isOrgLevel") != null) {
    	orgIdStr = request.getParameter("isOrgLevel");
 
    }

    MChildReportForm mChildReportForm = new MChildReportForm();
    try {
      /** Ҫ���ɱ����ID�б� */
      List reportList = new ArrayList();

      if (isAllRep != null && !isAllRep.equals("") ) {
        /* ����ȫ������ */
        if (isAllRep.equals("true")) {
        								// System.out.println("****����ȫ������****");
           //  �������л�����ȫ������
        	if(orgIdStr.equals("allOrg")){
        		/* �������л���ȫ������ */
        		reportList = StrutsMChildReportDelegate.findAllReport();
        	}else{
        		// �õ�һ�������ı����б�
        		reportList = StrutsMRepRangeDelegate.getchildrepidList(orgIdStr);
        	}
          
        } else if (isAllRep.equals("false") && childRepId != null && !childRepId.equals("")) {
          // System.out.println("*****��o�ɵ�������****");
        	mChildReportForm.setChildRepId(childRepId);
        	mChildReportForm.setVersionId(versionId);
          reportList.add(mChildReportForm);
        } else {
              System.out.println("��������ʧ��!Line=93");
        }
 
        if (reportList != null && reportList.size() != 0) {
          /* �ɹ����� */
          int successNum = 0;
          /* ʧ������ */
          int failedNum  = 0;

          for (int i = 0; i < reportList.size(); i++) {
            /* ��ñ���Id */
        	 MChildReportForm mcf = (MChildReportForm)reportList.get(i);
            String reportId = mcf.getChildRepId();
            String version = mcf.getVersionId();
            List   orgList  = null;

            if(orgIdStr.equals("allOrg")){
               /* ȡ���л��� */
               orgList = StrutsOrgNetDelegate.findAll();
            } else {
              OrgNetForm orgNetFormTemp = new OrgNetForm();
              orgList = new ArrayList();
              orgNetFormTemp.setOrg_id(orgIdStr);
              orgList.add(orgNetFormTemp);
            }

            if (orgList != null && orgList.size() != 0) {
              /* ѭ������ÿ�������ı��� */
              for (int j = 0; j < orgList.size(); j++) {
                /* ��û���ID */
                String orgId= ((OrgNetForm)orgList.get(j)).getOrg_id();

               // ���ݻ���IDȡ��ӳ��Ĳ���ID
                ETLReportForm etlForm = StrutsMChildReportDelegate.selectMappOrgId(orgId);
                
                // ���ݱ���ID��version�õ�ӳ��EXCELID
                etlForm = StrutsMChildReportDelegate.selectMappExcelId(reportId,version,etlForm);
                etlForm.setYear(year.trim());
                etlForm.setMonth(month.trim());
                /* ����Excel */

           //     Report_Impl rep_Impl = new Report_Impl(reportId, version, orgId, reptDate, saveUrl, modelUrl, orgList);

                try {
                  /* ��ȡ�ñ����� */
                	List etlFromList =StrutsMChildReportDelegate.selectReportValues(etlForm);
                	boolean isSuccess = false;
                	if(etlFromList!=null && etlFromList.size()>0){
                		 isSuccess = StrutsMChildReportDelegate.insertDB(etlFromList);;
                	}
                	
                //  �����report_in �� report_in_info ���ű���Ӧ��ֵ
                	
                  
                  
                  
                  String  logInfo   = "";

                  if (isSuccess == true) {
                    logInfo    = "����:" + reportId + " �汾��:" + version + " ����:" + year+"_"+ month + "����: " + orgId + " ����Excel�ɹ�!";
                    successNum++;
                  } else {
                    logInfo   = "����:" + reportId + " �汾��:" + version + " ����:" + year+"_"+ month + "����: " + orgId + " ����Excelʧ��!";
                    failedNum++;
                  }

                  FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);
                } catch (Exception e) {
                  e.printStackTrace();

                  String logInfo = "����:" + reportId + " �汾��:" + version + " ����:" + year+"_"+ month + "����: " + orgId + " ����Excelʧ��!";

                  FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);

                  failedNum++;

                  continue;
                }
              }
            }
          }

          messages.add("����Excel���!���ɳɹ�" + successNum + "��,ʧ��" + failedNum + "��!������־�в鿴��ϸ��Ϣ!");
        }
      } else {
        messages.add("����Excelʧ��!");
      }
    } catch (Exception e) {
      e.printStackTrace();
      messages.add("����Excelʧ��!");
    }

    if (messages != null && messages.getSize() != 0) {
      request.setAttribute(Config.MESSAGES, messages);
    }

    return mapping.findForward("view");
  }
} 