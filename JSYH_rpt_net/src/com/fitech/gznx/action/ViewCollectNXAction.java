package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateCollRuleDelegate;

/**
 * 已使用hibernate 卞以刚 2011-12-21
 * 报表汇总（农商）
 * @author Dennis Yee
 *
 */
public class ViewCollectNXAction extends Action {

	private FitechException log = new FitechException(ViewCollectNXAction.class);

	/**
	 * @param result
	 *            查询返回标志,如果成功返回true,否则返回false
	 * @param ReportInForm
	 * @param request
	 * @exception Exception
	 *                有异常捕捉并抛出
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);
		MessageResources resources = getResources(request);	
		//init
		List resList = null;
		List list = null;
		AFOrgDelegate afOrgDelegate = null;
		AFReportDelegate afReportDelegate = null;
		HttpSession session = request.getSession();
		String ss = (String)request.getAttribute("checkTask");
		if(ss != null&&!ss.equals("")){
			resList = null;
			//recordCount = 0 ;
			String dd = "";
			if(reportInForm.getDate() == null || reportInForm.getDate().equals("")){
				dd = (String) session.getAttribute(Config.USER_LOGIN_DATE);
			}else{
				dd = reportInForm.getDate();
			}
			messages.add(dd+resources.getMessage(ss));
			request.setAttribute(Config.MESSAGES,messages);
			return mapping.findForward("view");	 
		}	
		
		try {
			
			/** 取得当前用户的权限信息 */
			if (session.getAttribute("multi") != null) {
				messages = (FitechMessages) session.getAttribute("multi");
				session.setAttribute("multi", null);
			}
			Operator operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			//获得查询日期
			if (reportInForm.getDate()==null || reportInForm.getDate().equals("")){
				String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
				reportInForm.setDate(yestoday);
			}
			if(reportInForm.getOrgId()==null || reportInForm.getOrgId().equals("")){
				reportInForm.setOrgId(operator.getOrgId());
			}
			if(reportInForm.getSupplementFlag()==null||"".equals(reportInForm.getSupplementFlag()))
				reportInForm.setSupplementFlag("-999");
				
			String childOrgIds = operator.getChildOrgIds();
			//reportInForm.setOrgId(operator.getOrgId());
			
			/** 取得报表大类类型 */
			if (session.getAttribute(Config.REPORT_SESSION_FLG)!=null)
				reportInForm.setTemplateType(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
			
			//取得该用户需报报表
			/**已使用hibernate 卞以刚 2011-12-21**/
			resList = AFReportDelegate.selectNeedReportList(reportInForm, operator);

			if (resList != null && resList.size() > 0) {
				
				list = new ArrayList();
				afOrgDelegate = new AFOrgDelegate();
				afReportDelegate = new AFReportDelegate();
				
				for (Iterator iter = resList.iterator(); iter.hasNext();) {
					
					Aditing aditing = (Aditing) iter.next();
					
					// 该报表的关联报表
					String childRep = aditing.getTemplateId();
					
					// 应报机构的数量
					int needOrgCount = 0;
					// 实报机构数量
					int donum = 0;
					// 实报/应报字符串
					String doNeedStr = "";

					
					//人行报表
//					if(reportInForm.getTemplateType().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){

						//汇总规则
						Integer collectRule = null;

						//获得当前该报表报送机构的信息
						/**已使用hibernate 卞以刚 2011-12-21**/
						AfOrg reportOrg = AFOrgDelegate.selectOne(aditing.getOrgId());
						
						//管理员过滤掉除总行和虚拟机构的机构汇总
//						if(operator.isSuperManager() && 
//								(!reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)
//								&&!reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.VIRTUAL_TOPBANK)))
//							continue;
						
						//总行的取虚拟机构汇总
//						if (reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)){
//							collectRule = 1;
//						}
						//虚拟机构取真实机构汇总
						//else
						if(reportOrg.getIsCollect()!=null 
								&& reportOrg.getIsCollect().equals(Long
										.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
							collectRule = 2;
						}
						//其他正常汇总
						else {
							collectRule = 3;
						}
						
						//加入总行特殊处理(人行时取汇总机构设定的汇总关系，脱离层级汇总)
						if(reportInForm.getTemplateType().equals(com.fitech.gznx.common.Config.PBOC_REPORT)
								&& reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK))
							collectRule = 2;
						
						/**判断是否为轧差汇总，如果为轧差处理则直接处理即可*/
						String collFormula = AFTemplateCollRuleDelegate.getCollFormulaName(aditing.getOrgId(),aditing.getTemplateId(),aditing.getVersionId());
						if(collFormula!=null&&!collFormula.trim().equals("")){//轧差汇总
				            needOrgCount = AFTemplateCollRuleDelegate.getNeedReportNUM(collFormula, aditing.getOrgId());//计算应报数
				            String gzOrgIds = AFTemplateCollRuleDelegate.getNeedOrgIds(collFormula, aditing.getOrgId());//应报机构字符串
				            donum = afReportDelegate.getAvailabilityOrgIdCount(childRep, aditing.getVersionId(),
									aditing.getYear(),aditing.getTerm(),aditing.getDay(),
									aditing.getCurId(), aditing.getActuFreqID(),gzOrgIds,null,8,false);//计算实报数
				         }else{//一般汇总
						// 得到报表的报送机构数量
						/**已使用hibernate 卞以刚 2011-12-21**/
						needOrgCount = afOrgDelegate.getMustOrgCount(childRep, aditing.getVersionId(), 
								childOrgIds, aditing.getOrgId(), collectRule);
						
						if(true || needOrgCount!=0){ //(2013-5-20去掉该过滤判断以兼容没有下级行的机构汇总)
							// 获得机构已报送报表数量
							/**已使用hibernate 卞以刚 2011-12-21**/
							donum = afReportDelegate.getAvailabilityOrgIdCount(childRep, aditing.getVersionId(),
										aditing.getYear(),aditing.getTerm(),aditing.getDay(),
										aditing.getCurId(), aditing.getActuFreqID(),childOrgIds,aditing.getOrgId(),collectRule,false);
						}else{
							continue;
						}
				        }
//					}
					//其他报表
//					else{
//						
//						//汇总规则
//						Integer collectRule = null;
//
//						//获得当前该报表报送机构的信息
//						AfOrg reportOrg = AFOrgDelegate.selectOne(aditing.getOrgId());
//						
//						//管理员过滤掉除总行和虚拟机构的机构汇总
//						if(operator.isSuperManager() && 
//								(!reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)
//								&&!reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.VIRTUAL_TOPBANK)))
//							continue;
//						//虚拟机构取真实机构汇总
//						if(reportOrg.getIsCollect()!=null 
//								&& reportOrg.getIsCollect().equals(Long.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
//							collectRule = 2;
//						}
//						//其他正常汇总
//						else {
//							collectRule = 3;
//						}
//						// 得到报表的报送机构数量
//						needOrgCount = afOrgDelegate.getMustOrgCount(childRep, aditing.getVersionId(), 
//								childOrgIds, aditing.getOrgId(), collectRule);
//						
//						if(needOrgCount!=0){
//							// 获得机构已报送报表数量
//							donum = afReportDelegate.getAvailabilityOrgIdCount(childRep, aditing.getVersionId(),
//										aditing.getYear(),aditing.getTerm(),aditing.getDay(),
//										aditing.getCurId(), aditing.getActuFreqID(),childOrgIds,aditing.getOrgId(),collectRule);
//						}else{
//							continue;
//						}
//					}
					
					doNeedStr += childRep + "[" + donum + "/"
									+ needOrgCount + "]" + "   ";
					AFReportForm afReportForm = new AFReportForm();
					
					BeanUtils.copyProperties(afReportForm, aditing);
					
					afReportForm.setRepFreqId(aditing.getActuFreqID().toString());
					AfReport afReport=afReportDelegate.insertNewReport(afReportForm);
					aditing.set_repInId(afReport.getRepId().intValue());
						

					if(true || needOrgCount>0){//添加，滤去一些不必要的汇总关系  (2013-5-20去掉该过滤判断以兼容没有下级行的机构汇总)
						aditing.setDonum(new Integer(donum));
						aditing.setRepInFo(doNeedStr.trim());
						list.add(aditing);
					}else{
						continue;
					}
					
				}
				
				if(list.size()==0) list=null;
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.printStackTrace(e);
			messages.add("汇总数据浏览失败！");
		}

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		//增加list!=null 条件 以免 页面报错
		if (resList != null && resList.size() > 0&&list!=null)
			request.setAttribute(Config.RECORDS, list);
		else
			request.setAttribute(Config.RECORDS, null);

		request.setAttribute("date", reportInForm.getDate().toString());
		return mapping.findForward("view");
	}
}
