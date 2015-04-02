package com.fitech.gznx.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateTypeDelegate;

/**
 * 农信报表查询
 * @author Dennis Yee
 *
 */
public class ViewReportGatherStatAction extends Action {
	
    private static FitechException log = new FitechException(ViewReportGatherStatAction.class);

    /** 
     * jdbc技术 有oracle语法(rownum)需要修改 卞以刚 2011-12-22
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
    		,HttpServletResponse response)  throws IOException, ServletException{
        
    	
//    	if(request.getParameter("method")!=null){
//    		return export(mapping,form,request,response);
//    	}
    	
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        
        AFReportForm reportInInfoForm = (AFReportForm) form ;
        RequestUtils.populate(reportInInfoForm, request);
         

		if(request.getParameter("isLeader")!=null && !request.getParameter("isLeader").equals("")){
			reportInInfoForm.setIsLeader("1");
    	}
		int recordCount = 0; // 记录总数
		int offset = 0; // 偏移量
		int limit = 0; // 每页显示的记录条数
		
		List resList = null;
		ApartPage aPage = new ApartPage();	   	
		String strCurPage = request.getParameter("curPage");
		if(strCurPage!=null){		
			if(!strCurPage.equals(""))		    
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		
		//计算偏移量
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 		
		limit = Config.PER_PAGE_ROWS;
		
        HttpSession session = request.getSession();
        Operator operator = null; 
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
        
		if(reportInInfoForm.getDate() == null || reportInInfoForm.getDate().equals("")) {
			//获得昨天时间
			String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			reportInInfoForm.setDate(yestoday);	
		}
        /** 报表选中标志 **/
		String reportFlg = "";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		//hbyh添加功能块
		if(request.getParameter("gatherFlg")!=null && request.getParameter("gatherFlg").equals("1"))
			reportFlg = com.fitech.gznx.common.Config.OTHER_REPORT;
		
        //取得模板类型
		reportInInfoForm.setTemplateType(reportFlg);
		
		reportInInfoForm.setIsReport(Integer.valueOf(com.fitech.gznx.common.Config.TEMPLATE_ANALYSIS));
		
        try{
        	//取得记录总数
        	/**jdbc技术 可能需要修改 卞以刚 2011-12-22
        	 * 无特殊oracle语法 不需要修改 卞以刚 2011-12-28**/
	   		recordCount = AFReportDelegate.getReportStatCount(reportInInfoForm,operator);	  
	   		//显示分页后的记录
	   		if(recordCount > 0)
	   			/**jdbc技术 oracle语法(rownum) 需要修改 卞以刚 2011-12-22
	   			 * 已修改为sqlserver top  卞以刚 2011-12-27 待测试**/
		   	    resList = AFReportDelegate.getReportStatRecord(reportInInfoForm,offset,limit,operator);
	   		
        }catch(Exception ex){
        	log.printStackTrace(ex);
        	messages.add(resources.getMessage("select.dataReport.failed"));  
        }
        
        //把ApartPage对象存放在request范围内
	 	aPage.setTerm(this.getTerm(reportInInfoForm));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
//	 	if(!StringUtil.isEmpty(reportInInfoForm.getBak1()) && StringUtil.isEmpty(reportInInfoForm.getTemplateTypeName())){
//       	 String templateName = AFTemplateTypeDelegate.getTemplateTypeName(reportInInfoForm.getBak1(), reportFlg);
//       	reportInInfoForm.setTemplateTypeName(templateName);
//        }
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
        	request.setAttribute(Config.MESSAGES,messages);
        if(resList != null && resList.size() > 0)
        	request.setAttribute(Config.RECORDS,resList);
         if(!StringUtil.isEmpty(reportInInfoForm.getIsLeader()) && reportInInfoForm.getIsLeader().equals("1")){
        	 request.setAttribute("isLeader","1");
         }
        return mapping.findForward("view");
    }
    
    
    /**
	 * 设置查询传递参数
	 * 
	 * @param reportInInfoForm
	 * @return String 查询的URL
	 */
	public String getTerm(AFReportForm reportInForm){	   
		String term="";
		
		if(reportInForm.getIsLeader() != null && !reportInForm.getIsLeader().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "isLeader=" + reportInForm.getIsLeader();
		}
		/**加入报表编号条件*/
		if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "templateId=" + reportInForm.getTemplateId();
		}
		/**加入报表名称条件*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName=" + reportInForm.getRepName();
		}
		/**加入模板类型条件*/
		if(reportInForm.getBak1() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/**加入报送频度条件*/
		if(reportInForm.getRepFreqId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId=" + reportInForm.getRepFreqId();
		}	
		/**加入报表期数条件*/
		if(reportInForm.getDate() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}	
		if(reportInForm.getOrgId() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "orgId=" + reportInForm.getOrgId();
		}
		String orgName = reportInForm.getOrgName();
		if(orgName!=null&&!orgName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");			
			term += "orgName="+orgName;    
		}
		if(reportInForm.getCheckFlag() != null){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "checkFlag=" + reportInForm.getCheckFlag();
		}
		if(term.indexOf("?")>=0)		
			term = term.substring(term.indexOf("?")+1);
		
		return term;   
	}

}
