package com.fitech.net.template.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;

/**
 * ����Excel
 * @author wh
 *
 */
public final class CreateExcelFromFormulaActionNEW extends Action {
  private static FitechException log = new FitechException(CreateExcelFromFormulaActionNEW.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
                               HttpServletResponse response) throws IOException, ServletException {
    MessageResources resources  = getResources(request);
    FitechMessages   messages   = new FitechMessages();
    /** �Ƿ����������б��� */
    String           isAllRep   = "";
    /** ������������������Ϣ */
    String           childRepId = "";
    String           version    = "";
    String           reptDate   = "";
    /** ���ɸ����������� ��Ϊ,1,2,3,�� */
    int              isOrgLevel = -1;

    /* �����Ӧ�Ĳ��� */
    if (request.getParameter("isAllRep") != null) {
      isAllRep = request.getParameter("isAllRep");
    }

    if (request.getParameter("childRepId") != null) {
      childRepId = request.getParameter("childRepId").split(",")[0];
    }

    if (request.getParameter("version") != null) {
      version = request.getParameter("version");
    }

    if (request.getParameter("reptDate") != null) {
      reptDate = request.getParameter("reptDate");
    }

    if (request.getParameter("isOrgLevel") != null) {
      String isOrgLevelStr = request.getParameter("isOrgLevel");

      if (isOrgLevelStr != null) {
        try {
          isOrgLevel = Integer.parseInt(isOrgLevelStr);
        } catch (Exception e) {
          isOrgLevel = -1;

          // System.out.println("��ȡ��������!");
        }
      }
    }
    String modelUrl = Config.WEBROOTPATH + "Reports" + Config.FILESEPARATOR + "templates";
    String saveUrl = Config.WEBROOTPATH + "Reports" + Config.FILESEPARATOR + "releaseTemplates";

    try {
      /** Ҫ���ɱ����ID�б� */
      List reportList = new ArrayList();

      if (isAllRep != null && !isAllRep.equals("") && version != null && !version.equals("")) {/*
         ����ȫ������ 
        if (isAllRep.equals("true")) {
          // System.out.println("****����ȫ������****");

          reportList = FormulaParseUtil.getReportInfo(version);
        } else if (isAllRep.equals("false") && childRepId != null && !childRepId.equals("")) {
          // System.out.println("*****��o�ɵ�������****");
          reportList.add(childRepId);
        } else {
          // System.out.println("����Excelʧ��!");
        }
 
        if (reportList != null && reportList.size() != 0) {
           �ɹ����� 
          int successNum = 0;
           ʧ������ 
          int failedNum  = 0;

          for (int i = 0; i < reportList.size(); i++) {
             ��ñ���Id 
            String reportId = (String)reportList.get(i);
            List   orgList  = null;

            if (isOrgLevel != 1) {
               ���ݱ���Id�����ɱ������ݷ�Χ��ȡ����Ҫ������Щ�����ı��� 
              orgList = FormulaParseUtil.findAll(reportId, version);
            } else if (isOrgLevel == 1) {
              orgList = new ArrayList();
              String orgIds = "0";
              orgList.add(orgIds);
            }

            if (orgList != null && orgList.size() != 0) {
               ѭ������ÿ�������ı��� 
              for (int j = 0; j < orgList.size(); j++) {
                 ��û���ID 
                String orgId = (String)orgList.get(j);

                 ����Excel 
                 ���ֻ������� 
                if (orgId.equals("0")) {
                  orgId = "331010000";
                } else {
                  orgId = orgId.trim();
                }

                Report_Impl rep_Impl = new Report_Impl(reportId, version, orgId, reptDate, saveUrl, modelUrl, orgList);

                try {
                   ������Excel 
                  boolean isSuccess = rep_Impl.createExcel();
                  String  logInfo   = "";

                  if (isSuccess == true) {
                    logInfo    = "����:" + reportId + " �汾��:" + version + " ����:" + reptDate + "����: " + orgId + " ����Excel�ɹ�!";
                    successNum++;
                  } else {
                    logInfo   = "����:" + reportId + " �汾��:" + version + " ����:" + reptDate + "����: " + orgId + " ����Excelʧ��!";
                    failedNum++;
                  }

                  FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);
                } catch (Exception e) {
                  e.printStackTrace();

                  String logInfo = "����:" + reportId + " �汾��:" + version + " ����:" + reptDate + "����: " + orgId + " ����Excelʧ��!";

                  FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);

                  failedNum++;

                  continue;
                }
              }
            }
          }

          messages.add("����Excel���!���ɳɹ�" + successNum + "��,ʧ��" + failedNum + "��!������־�в鿴��ϸ��Ϣ!");
        }
      */} else {
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