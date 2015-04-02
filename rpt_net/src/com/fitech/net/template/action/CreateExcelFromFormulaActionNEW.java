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
 * 生成Excel
 * @author wh
 *
 */
public final class CreateExcelFromFormulaActionNEW extends Action {
  private static FitechException log = new FitechException(CreateExcelFromFormulaActionNEW.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
                               HttpServletResponse response) throws IOException, ServletException {
    MessageResources resources  = getResources(request);
    FitechMessages   messages   = new FitechMessages();
    /** 是否是生成所有报表 */
    String           isAllRep   = "";
    /** 单个报表生成所需信息 */
    String           childRepId = "";
    String           version    = "";
    String           reptDate   = "";
    /** 生成各级机构数据 分为,1,2,3,级 */
    int              isOrgLevel = -1;

    /* 获得相应的参数 */
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

          // System.out.println("获取参数错误!");
        }
      }
    }
    String modelUrl = Config.WEBROOTPATH + "Reports" + Config.FILESEPARATOR + "templates";
    String saveUrl = Config.WEBROOTPATH + "Reports" + Config.FILESEPARATOR + "releaseTemplates";

    try {
      /** 要生成报表的ID列表 */
      List reportList = new ArrayList();

      if (isAllRep != null && !isAllRep.equals("") && version != null && !version.equals("")) {/*
         生成全部报表 
        if (isAllRep.equals("true")) {
          // System.out.println("****生成全部报表****");

          reportList = FormulaParseUtil.getReportInfo(version);
        } else if (isAllRep.equals("false") && childRepId != null && !childRepId.equals("")) {
          // System.out.println("*****生o成单个报表****");
          reportList.add(childRepId);
        } else {
          // System.out.println("生成Excel失败!");
        }
 
        if (reportList != null && reportList.size() != 0) {
           成功数量 
          int successNum = 0;
           失败数量 
          int failedNum  = 0;

          for (int i = 0; i < reportList.size(); i++) {
             获得报表Id 
            String reportId = (String)reportList.get(i);
            List   orgList  = null;

            if (isOrgLevel != 1) {
               根据报表Id和生成报表数据范围来取得需要生成哪些机构的报表 
              orgList = FormulaParseUtil.findAll(reportId, version);
            } else if (isOrgLevel == 1) {
              orgList = new ArrayList();
              String orgIds = "0";
              orgList.add(orgIds);
            }

            if (orgList != null && orgList.size() != 0) {
               循环生成每个机构的报表 
              for (int j = 0; j < orgList.size(); j++) {
                 获得机构ID 
                String orgId = (String)orgList.get(j);

                 生成Excel 
                 不分机构生成 
                if (orgId.equals("0")) {
                  orgId = "331010000";
                } else {
                  orgId = orgId.trim();
                }

                Report_Impl rep_Impl = new Report_Impl(reportId, version, orgId, reptDate, saveUrl, modelUrl, orgList);

                try {
                   创建该Excel 
                  boolean isSuccess = rep_Impl.createExcel();
                  String  logInfo   = "";

                  if (isSuccess == true) {
                    logInfo    = "报表:" + reportId + " 版本号:" + version + " 日期:" + reptDate + "机构: " + orgId + " 生成Excel成功!";
                    successNum++;
                  } else {
                    logInfo   = "报表:" + reportId + " 版本号:" + version + " 日期:" + reptDate + "机构: " + orgId + " 生成Excel失败!";
                    failedNum++;
                  }

                  FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);
                } catch (Exception e) {
                  e.printStackTrace();

                  String logInfo = "报表:" + reportId + " 版本号:" + version + " 日期:" + reptDate + "机构: " + orgId + " 生成Excel失败!";

                  FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);

                  failedNum++;

                  continue;
                }
              }
            }
          }

          messages.add("生成Excel完成!生成成功" + successNum + "张,失败" + failedNum + "张!请在日志中查看详细信息!");
        }
      */} else {
        messages.add("生成Excel失败!");
      }
    } catch (Exception e) {
      e.printStackTrace();
      messages.add("生成Excel失败!");
    }

    if (messages != null && messages.getSize() != 0) {
      request.setAttribute(Config.MESSAGES, messages);
    }

    return mapping.findForward("view");
  }
} 