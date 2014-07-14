package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
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

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportProductDelegate;

public class ExportRhAFReportAction extends Action {
    private static FitechException log = new FitechException(ExportAFReportAction.class);

    /** 
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
    	//List对象的初始化
    	List resList=null;
    	List rsList=null;
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        //取得request范围内的请求参数，并存放在reportForm内
        AFReportForm reportInForm = (AFReportForm)form ;
        RequestUtils.populate(reportInForm, request);	

		int recordCount =0; //记录总数
		int offset=0; //偏移量
		int limit=0;  //每页显示的记录条数
		ApartPage aPage=new ApartPage();
		String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
			if(!strCurPage.equals(""))
				aPage.setCurPage(new Integer(strCurPage).intValue());
		}else
			aPage.setCurPage(1);
			
		//计算偏移量   
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;	
		
        /**
         * 取得当前用户的权限信息
         */   
		HttpSession session = request.getSession();
		Operator operator = null; 
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		
		/** 
		 * 报表选中标志 
		 */
		String reportFlg = "0";
		//选中标志判断
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		if(StringUtil.isEmpty(reportInForm.getDate()) || reportInForm.getDate()==null){
			String yestoday = (String)session.getAttribute(Config.USER_LOGIN_DATE);
			reportInForm.setDate(yestoday);
		}
		
		if(reportInForm.getOrgId() == null)
			reportInForm.setOrgId(operator.getOrgId());
		if(reportInForm.getRepFreqId()==null)
			reportInForm.setRepFreqId("1");
		if(reportInForm.getSupplementFlag()==null)
			reportInForm.setSupplementFlag("-999");
		
		int newFlg = 0;
		if(request.getParameter("styleFlg")!=null 
				&& request.getParameter("styleFlg").equals("new")){
			newFlg = 1;
		}
		
		reportInForm.setOrgId(reportInForm.getOrgId().indexOf("'")==-1 
				? "'" + reportInForm.getOrgId() + "'" :reportInForm.getOrgId());
		
		String orgs = "";
		Map selectCheckOrgIds=new HashMap();
		if(reportInForm.getSelectedOrgIds()!=null && reportInForm.getSelectedOrgIds().length>0) {
			if(reportInForm.getSelectedOrgIds()[0].split(",").length>=2){
				for(String org : reportInForm.getSelectedOrgIds()[0].split(",")){
					orgs += orgs.equals("") ? "'" + org + "'" : ",'" + org + "'";
					selectCheckOrgIds.put(org, org);
				}
				reportInForm.setSelectedOrgIds(reportInForm.getSelectedOrgIds()[0].split(","));
			}else{
			for(String org : reportInForm.getSelectedOrgIds()){
				orgs += orgs.equals("") ? "'" + org + "'" : ",'" + org + "'";
				selectCheckOrgIds.put(org, org);
				
			}
			}
			request.setAttribute("selectCheckOrgIds", selectCheckOrgIds);
			
			reportInForm.setOrgId(orgs);
		}
//		else if(reportInForm.getOrgId()!=null) {
//			for(String org : reportInForm.getOrgId().split(",")){
//				orgs += orgs.equals("") ? "'" + org + "'" : ",'" + org + "'";
//				selectCheckOrgIds.put(org, org);
//				
//			}
//			request.setAttribute("selectCheckOrgIds", selectCheckOrgIds);
//			
//			reportInForm.setOrgId(orgs);
//		}
		
		//搜索报表记录
		
		try{ 					
			resList = AFReportProductDelegate.selectRhExportReportList(reportInForm,operator,reportFlg,newFlg);
			
			//分页操作
			if (resList != null && resList.size() > 0) {
				// 取得记录总数
				recordCount = resList.size();
			}
			if (recordCount > 0) {
				rsList=new ArrayList();
				int arraySize = limit + offset;
				if (resList.size() < (limit + offset))
					arraySize = resList.size();
				for (int i = offset; i < arraySize; i++) {
					Aditing aditing = (Aditing) resList.get(i);
					rsList.add(aditing);
				}
			}
		}catch(Exception ex){
			log.printStackTrace(ex);
			messages.add(resources.getMessage("select.dataReport.failed"));		
		}  
		if(request.getParameter("styleFlg")!=null && request.getParameter("styleFlg").equals("new")){
        //把ApartPage对象存放在request范围内
		aPage.setTerm(this.getTerm(reportInForm));
		}else{
		aPage.setTerm(this.getOldTerm(reportInForm));
		}
		aPage.setCount(recordCount);
		request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		request.setAttribute("orgId",reportInForm.getOrgId());
		request.setAttribute("date",reportInForm.getDate());
		request.setAttribute("selectedOrgIds",reportInForm.getSelectedOrgIds());
		request.setAttribute("RequestParam", aPage.getTerm());
		if(reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){//全部机构
			reportInForm.setOrgName("全部机构");
		}else{
			AfOrg org = AFOrgDelegate.getOrgInfo(reportInForm.getOrgId().substring(1,reportInForm.getOrgId().length()-1));
			if(org==null || org.getOrgName()==null || org.getOrgName().equals(""))
				reportInForm.setOrgName("全部机构");
			else
				reportInForm.setOrgName(org.getOrgName());
		}
		request.setAttribute("orgName", reportInForm.getOrgName());
		if(messages.getMessages() != null && messages.getMessages().size() > 0)		
			request.setAttribute(Config.MESSAGES,messages);
		  
		if(resList!=null && resList.size()>0){
			request.setAttribute("Records",rsList);			
		}
		session.setAttribute("downLoadRh", resList);
		if(request.getParameter("styleFlg")!=null && request.getParameter("styleFlg").equals("new"))
			return mapping.findForward("newindex");
		else
			return mapping.findForward("index");
		
    }
    
    
	public String getTerm(AFReportForm reportInForm){
		String term="";  
		String[] orgIds = reportInForm.getSelectedOrgIds();
		String orgId = reportInForm.getOrgId();
		String date = String.valueOf(reportInForm.getDate());
		//添加报表编号传递参数便于分页  author lzl time 2010112
		String templateId = String.valueOf(reportInForm.getTemplateId());
		String repName = String.valueOf(reportInForm.getRepName());
		String repFreqId = reportInForm.getRepFreqId();
		String supplementFlag = reportInForm.getSupplementFlag();
		//添加下一页标志styleFlag 
		String styleFlg = "";
		styleFlg = reportInForm.getStyleFlg();
		
		if(orgIds!=null&&!orgIds.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			String orgs ="" ;
			for(String org:orgIds){
				orgs += orgs.equals("")?"'"+org+"'":",'"+org+"'";
			}
			term += "selectedOrgIds="+orgs;
		}
		if(orgId!=null&&!orgId.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			String org="";
				org += orgId;
				term += "orgId="+org;
		}
		if(repFreqId!=null&&!repFreqId.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId="+repFreqId;
		}
		if(supplementFlag!=null&&!supplementFlag.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "supplementFlag="+supplementFlag;
		}
		if(date!=null&&!date.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date="+date.toString();
		}
		if(!templateId.equals("null")&&!templateId.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "templateId="+templateId;
		}
		if(!repName.equals("null")&&!repName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName="+repName;
		}
		if(styleFlg!=null&&styleFlg.equals("new")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "styleFlg="+styleFlg;
		}
		if(term.indexOf("?")>=0)
			term = term.substring(term.indexOf("?")+1);		
		return term;
	}
	public String getOldTerm(AFReportForm reportInForm){
		String term="";  
		String orgId = reportInForm.getOrgId();
		String date = String.valueOf(reportInForm.getDate());	
		String repFreqId = reportInForm.getRepFreqId();
		String templateId = String.valueOf(reportInForm.getTemplateId());
		String repName = String.valueOf(reportInForm.getRepName());
		String supplementFlag = reportInForm.getSupplementFlag();
		if(orgId!=null&&!orgId.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			String org="";
				org += orgId;
				term += "orgId="+org;
			}
		if(repFreqId!=null&&!repFreqId.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repFreqId="+repFreqId;
		}
		if(supplementFlag!=null&&!supplementFlag.equals("")){
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "supplementFlag="+supplementFlag;
		}
		if(date!=null&&!date.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "date="+date.toString();
		}
		if(!templateId.equals("null")&&!templateId.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "templateId="+templateId;
		}
		if(!repName.equals("null")&&!repName.equals("")){		
			term += (term.indexOf("?")>=0 ? "&" : "?");
			term += "repName="+repName;
		}
		if(term.indexOf("?")>=0)
			term = term.substring(term.indexOf("?")+1);		
		return term;
	}
}
