 
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
 * 生成Excel
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

    /** 是否是生成所有报表 */
    String           isAllRep   = "";
    /** 单个报表生成所需信息 */
    String           childRepId = "";
    String           versionId   = "";
    String           year   = "";
    String 		     month  = "";
    /** 生成机构ID  */
    String              orgIdStr = "";

    /* 获得相应的参数 */
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
      /** 要生成报表的ID列表 */
      List reportList = new ArrayList();

      if (isAllRep != null && !isAllRep.equals("") ) {
        /* 生成全部报表 */
        if (isAllRep.equals("true")) {
        								// System.out.println("****生成全部报表****");
           //  生成所有机构的全部报表
        	if(orgIdStr.equals("allOrg")){
        		/* 生成所有机构全部报表 */
        		reportList = StrutsMChildReportDelegate.findAllReport();
        	}else{
        		// 得到一个机构的报表列表
        		reportList = StrutsMRepRangeDelegate.getchildrepidList(orgIdStr);
        	}
          
        } else if (isAllRep.equals("false") && childRepId != null && !childRepId.equals("")) {
          // System.out.println("*****生o成单个报表****");
        	mChildReportForm.setChildRepId(childRepId);
        	mChildReportForm.setVersionId(versionId);
          reportList.add(mChildReportForm);
        } else {
              System.out.println("生成数据失败!Line=93");
        }
 
        if (reportList != null && reportList.size() != 0) {
          /* 成功数量 */
          int successNum = 0;
          /* 失败数量 */
          int failedNum  = 0;

          for (int i = 0; i < reportList.size(); i++) {
            /* 获得报表Id */
        	 MChildReportForm mcf = (MChildReportForm)reportList.get(i);
            String reportId = mcf.getChildRepId();
            String version = mcf.getVersionId();
            List   orgList  = null;

            if(orgIdStr.equals("allOrg")){
               /* 取所有机构 */
               orgList = StrutsOrgNetDelegate.findAll();
            } else {
              OrgNetForm orgNetFormTemp = new OrgNetForm();
              orgList = new ArrayList();
              orgNetFormTemp.setOrg_id(orgIdStr);
              orgList.add(orgNetFormTemp);
            }

            if (orgList != null && orgList.size() != 0) {
              /* 循环生成每个机构的报表 */
              for (int j = 0; j < orgList.size(); j++) {
                /* 获得机构ID */
                String orgId= ((OrgNetForm)orgList.get(j)).getOrg_id();

               // 根据机构ID取得映射的部门ID
                ETLReportForm etlForm = StrutsMChildReportDelegate.selectMappOrgId(orgId);
                
                // 根据报表ID和version得到映射EXCELID
                etlForm = StrutsMChildReportDelegate.selectMappExcelId(reportId,version,etlForm);
                etlForm.setYear(year.trim());
                etlForm.setMonth(month.trim());
                /* 生成Excel */

           //     Report_Impl rep_Impl = new Report_Impl(reportId, version, orgId, reptDate, saveUrl, modelUrl, orgList);

                try {
                  /* 抽取该表数据 */
                	List etlFromList =StrutsMChildReportDelegate.selectReportValues(etlForm);
                	boolean isSuccess = false;
                	if(etlFromList!=null && etlFromList.size()>0){
                		 isSuccess = StrutsMChildReportDelegate.insertDB(etlFromList);;
                	}
                	
                //  插入表report_in 和 report_in_info 二张表相应的值
                	
                  
                  
                  
                  String  logInfo   = "";

                  if (isSuccess == true) {
                    logInfo    = "报表:" + reportId + " 版本号:" + version + " 日期:" + year+"_"+ month + "机构: " + orgId + " 生成Excel成功!";
                    successNum++;
                  } else {
                    logInfo   = "报表:" + reportId + " 版本号:" + version + " 日期:" + year+"_"+ month + "机构: " + orgId + " 生成Excel失败!";
                    failedNum++;
                  }

                  FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);
                } catch (Exception e) {
                  e.printStackTrace();

                  String logInfo = "报表:" + reportId + " 版本号:" + version + " 日期:" + year+"_"+ month + "机构: " + orgId + " 生成Excel失败!";

                  FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);

                  failedNum++;

                  continue;
                }
              }
            }
          }

          messages.add("生成Excel完成!生成成功" + successNum + "张,失败" + failedNum + "张!请在日志中查看详细信息!");
        }
      } else {
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