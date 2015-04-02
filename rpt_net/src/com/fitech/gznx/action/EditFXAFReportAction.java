package com.fitech.gznx.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.util.DateFromExcel;

public class EditFXAFReportAction extends Action {
    /** 
     * 已使用hibernate 卞以刚 2011-12-22
     * 
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
        HttpServletResponse response)  throws IOException, ServletException{
    	
    	FitechMessages messages = new FitechMessages();
		AFReportForm reportForm = (AFReportForm)form ;
		RequestUtils.populate(reportForm, request);
    	/** 报表选中标志 **/
		String reportFlg = "0";
		HttpSession session = request.getSession();
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String operatorId = String.valueOf(operator.getOperatorId());
		// 清理打印文件夹
		String deleteFilePath = Config.RAQ_TEMPLATE_PATH +"templateFiles" + File.separator + "printRaq"+ File.separator + operatorId;
   		File outfile = new File(deleteFilePath);
   		if(outfile.exists())
			deleteUploadFile(deleteFilePath);
		
		outfile.mkdir();
		DateFromExcel dataFrom = new DateFromExcel();
		dataFrom.setPrintPath(operatorId);
		dataFrom.setMessages(messages);
		String requestUrl = request.getQueryString();
		String repInIdString = request.getParameter("repInId") ;	

		String year = request.getParameter("year");
		String term = request.getParameter("term");
		String day = request.getParameter("day") ;
		String printorgId =request.getParameter("orgId") ;
		String statusFlg = request.getParameter("statusFlg") ;
		if(day != null && day.length() == 1 && day.indexOf("0") != 0){
			day = "0"+day;
		}
		if(term != null && term.length() == 1 && term.indexOf("0") != 0){
			term = "0"+term;
		}
		if(StringUtil.isEmpty(day)){
			day = DateUtil.getMonthLastDayStr(Integer.valueOf(year),Integer.valueOf(term));
		}
		String rdate = year +term+day;
		String orgId = request.getParameter("orgId") != null ? request.getParameter("orgId") : "";
		
		//处理返回页面
		String backQry = request.getParameter("backQry") != null ? request.getParameter("backQry") : "";
		
		if(backQry!=null && !backQry.equals("")){
			
			String[] splitQry = backQry.split("_");					
			if(splitQry.length==5)
				backQry = "date=" + splitQry[0]
				             + "&repName="+ splitQry[1]
				             + "&orgId="+ splitQry[2]
				             + "&repFreqId="+ splitQry[3]
				             + "&curPage=" + splitQry[4];
			
				
			session.setAttribute("backQry", backQry);
			
		}
		
		if(!StringUtil.isEmpty(statusFlg) && statusFlg.equals("2")){	
			// 在线填报修改
    		Map report = new HashMap();
			String repInId = repInIdString;
    		String templateId = "";
    		String versionId = "";
    		String curid ="";
    		String repFrq = "";
    		String RangeID ="";
    		
    		/**已使用hibernate 卞以刚 2011-12-21**/
			AFReportForm reportInForm = AFReportDealDelegate.getReportIn(Integer.valueOf(repInId));
			RangeID = "1";
			report.put("RangeID", 1);
			repFrq = reportInForm.getRepFreqId();
			report.put("Freq", repFrq); 
			 curid = reportInForm.getCurId();
			
			report.put("CCY",curid );
			templateId = reportInForm.getTemplateId();
			versionId = reportInForm.getVersionId();
			report.put("templateId",templateId );
			report.put("versionId",versionId );

    		report.put("ReptDate", rdate);
    		report.put("OrgID", printorgId);
    		/**已使用hibernate 卞以刚 2011-12-21**/
			AfTemplate template = AFTemplateDelegate.getTemplate(templateId,versionId);
			if(template == null){
				ActionForward af = new ActionForward("/viewGatherReport.do?" +backQry);
				return af;
			}
			String raqFileName="";

			raqFileName = dataFrom.saveFromExcel(repInId,templateId,versionId, reportFlg,template.getIsReport().intValue());
			if(raqFileName == null && dataFrom.getMessages().getSize()>0){
				request.setAttribute(Config.MESSAGES, dataFrom.getMessages());
				ActionForward af = new ActionForward("/viewGatherReport.do?" +backQry);
				return af;
			}
			report.put("repId",repInId );
			report.put("filename",raqFileName );
			report.put("requestUrl",requestUrl );
			
    		request.setAttribute("reportParam", report);
//			String url = "/gznx/reportadd/editFXReport.jsp?";
//
//			ActionForward af = new ActionForward(url);
//			return af;
    		return mapping.findForward("index");
		} else if(!StringUtil.isEmpty(statusFlg) && statusFlg.equals("3")){ 
			// 在线填报添加
    		Map report = new HashMap();		
    		String templateId = request.getParameter("templateId");
    		String versionId = request.getParameter("versionId");
    		String curid =request.getParameter("curId");
    		String repFrq = request.getParameter("repFreqId");
    		String rangeID ="";
    		report.put("RangeID", 1);

    		report.put("Freq", repFrq);
    		
			report.put("CCY",curid );

			report.put("templateId",templateId );
			report.put("versionId",versionId );
    		report.put("ReptDate", rdate);
    		report.put("OrgID", printorgId);
    		/**已使用hibernate 卞以刚 2011-12-21**/
			AfTemplate template = AFTemplateDelegate.getTemplate(templateId,versionId);
			if(template == null){
				ActionForward af = new ActionForward("/viewGatherReport.do?" + backQry);
				return af;
			}
			String raqFileName="";
			String repId = "";
			// 生成报表信息
			
			/**已使用hibernate 卞以刚 2011-12-21**/
			repId = AFReportDelegate.insertNXReport(report,operator,reportFlg);

			// 生出出错，返回页面
			if(StringUtil.isEmpty(repId)){
				ActionForward af = new ActionForward("/viewGatherReport.do?" + backQry);
				return af;
			}				
			/**已使用hibernate技术 卞以刚 2011-12-22**/
			raqFileName = dataFrom.saveFromRAQ(templateId,versionId,repId,reportFlg);		
			if(raqFileName == null && dataFrom.getMessages().getSize()>0){
				request.setAttribute(Config.MESSAGES, dataFrom.getMessages());
				ActionForward af = new ActionForward("/viewGatherReport.do?" +backQry);
				return af;
			}
			requestUrl = "statusFlg=2&repInId="+repId+"&year="+year+"&term="+term+"&day="+day+"&orgId="+printorgId;
			report.put("filename",raqFileName );
			report.put("repId",repId );
			report.put("requestUrl",requestUrl );
			
    		request.setAttribute("reportParam", report);
//			String url = "/gznx/reportadd/editFXReport.jsp?";
//
//			ActionForward af = new ActionForward(url);
//			return af;
    		return mapping.findForward("index");
		}
		// 返回

		ActionForward af = new ActionForward("/viewGatherReport.do?" + backQry);
		return af;
		
	}
    
    
    // 删除上传的ZIP文件及解压文件夹
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
