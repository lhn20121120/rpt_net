
package com.fitech.net.action;

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

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.excel.CreateExcel;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.fitech.net.common.CommMethod;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CheckFailedOnLineAction extends Action{
//	private static FitechException log=new FitechException(CheckFailedOnLineAction.class);
	  /**
	    * execute action.
	    * @param mapping Action mapping.
	    * @param form Action form.
	    * @param request HTTP request.
	    * @param response HTTP response.
	    * @exception IOException if an input/output error occurs
	    * @exception ServletException if a servlet exception occurs
	    */
		public ActionForward execute(ActionMapping mapping,
				 					 ActionForm form,
									 HttpServletRequest request,
									 HttpServletResponse response)
									throws IOException, ServletException {
			
			HttpSession session = request.getSession();
			Operator operator = null; 
			if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)  
				operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);   
			
			String orgId = operator.getOrgId();
			
		    response.addHeader("Pragma", "no-cache");
		    response.addHeader("Cache-Control", "no-cache");
		    response.addHeader("Expires", "Thu,01 Jan 1970 00:00:01 GMT");
		    
		    ReportInForm reportForm = (ReportInForm)form ;
			RequestUtils.populate(reportForm, request);
			
			String fileName = "";
			String year = request.getParameter("year");
			String term = request.getParameter("term");
			String curPage = request.getParameter("curPage");
			
		    Integer repInId = reportForm.getRepInId() != null 
							&& !reportForm.getRepInId().equals("") ? 
							reportForm.getRepInId() : new Integer(-1);
			ReportInForm reportInForm= StrutsReportInDelegate.getReportIn(repInId);

			if(reportInForm != null){
				CreateExcel createExcel = new CreateExcel(repInId);
				createExcel.createExcel(year,term);
				fileName = reportInForm.getChildRepId() + "_" + reportInForm.getVersionId()+"_"+reportInForm.getDataRangeId()+"_"+reportInForm.getCurId()+ ".xls";
			}
			
		    String hrefUrl = CommMethod.getAbsolutePath(request,  "reportSearch/searchMyRep.do?curPage=" + curPage);		   
			request.setAttribute("hrefUrl", hrefUrl);		    
		    String reportUrl = CommMethod.getAbsolutePath(request, com.fitech.net.config.Config.COLLECT_EXCEL_ALL_PATH +"/"  + year + "_" + term + "/" +  operator.getOrgId() + "/" + fileName);
		    request.setAttribute("reportUrl", reportUrl);
			request.setAttribute("reportName", fileName);			
			request.setAttribute("saveUrl", CommMethod.getAbsolutePath(request, "template/data_report/uploadDoc.jsp?orgId="+orgId+"&childRepId="+reportInForm.getChildRepId()+"&versionId="+reportInForm.getVersionId()+"&year="+year+"&term="+term));
			request.setAttribute("saveExcelUrl",CommMethod.getAbsolutePath(request, "template/data_report/upJYLoadDoc.jsp?orgId="+orgId+"&childRepId="+reportInForm.getChildRepId()+"&versionId="+reportInForm.getVersionId()+"&year="+year+"&term="+term));
			
		    CommMethod.buidPageInfo(request);		    
		    return mapping.findForward("success");
		}
}
