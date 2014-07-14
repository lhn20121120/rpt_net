package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
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

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.util.ReportStatisticsCollection;
import com.fitech.gznx.util.TranslatorUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.form.OrgNetForm;

/**
 * 报送情况统计
 * 
 * @author jcm
 * @serialData 2008-02-26
 */
public class ReportStatisticsCollectAction extends Action {
	
	private FitechException log = new FitechException(
			ReportStatisticsCollectAction.class);

	/**
	 * jdbc技术 无特殊oracle语法 可能需要修改 卞以刚 2011-12-22
	 * Performs action.
	 * 
	 * @param mapping
	 *            Action mapping.
	 * @param form
	 *            Action form.
	 * @param request
	 *            HTTP request.
	 * @param response
	 *            HTTP response.
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

			MessageResources resources = getResources(request);
			FitechMessages messages = new FitechMessages();

			HttpSession session = request.getSession();

			OrgNetForm orgNetForm = (OrgNetForm) form;
			RequestUtils.populate(orgNetForm, request);

			if (orgNetForm.getStartDate() == null
					|| orgNetForm.getStartDate().equals("")){
				String yestoday = (String) session.getAttribute(Config.USER_LOGIN_DATE);
				//月报统计，仅支持到月统计
				yestoday = yestoday.substring(0, 7);
				orgNetForm.setStartDate(yestoday);
				orgNetForm.setEndDate(yestoday);
			}
			//orgNetForm.setYear(Integer.valueOf(orgNetForm.getDate().substring(0, 4)));
			//orgNetForm.setTerm(Integer.valueOf(orgNetForm.getDate().substring(5, 7)));
			
			int recordCount = 0; // 记录总数
			int offset = 0; // 偏移量
			int limit = 0; // 每页显示的记录条数

			List resList = null;
			ApartPage aPage = new ApartPage();
			String strCurPage = request.getParameter("curPage");
			if (strCurPage != null) {
				if (!strCurPage.equals(""))
					aPage.setCurPage(new Integer(strCurPage).intValue());
			} else
				aPage.setCurPage(1);
			// 计算偏移量
			offset = (aPage.getCurPage() - 1) * Config.PER_PAGE_ROWS;
			limit = Config.PER_PAGE_ROWS*aPage.getCurPage();
			aPage.setRows(Config.PER_PAGE_ROWS);
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session
						.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			
			//分别处理报表统计
			Integer templateType = 1;
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				templateType = Integer.valueOf(session.getAttribute(Config.REPORT_SESSION_FLG).toString()); 
			/**jdbc技术 oracle语法 可能需要修改 卞以刚 2011-12-22
			 * 将oracle的to_date函数 修改为sqlserver 的convert函数
			 * 将oracle nvl语法修改为sqlserver isnull语法
			 * 卞以刚 2011-12-27 待测试**/
			recordCount=ReportStatisticsCollection.reportStatCount(orgNetForm, templateType);
			if(recordCount>0)
				/**将oracle的to_date函数 修改为sqlserver 的convert函数
				 * 将oracle nvl语法修改为sqlserver isnull语法
				 * oracle rownum伪列修改为sqlserver ROW_NUMBER()函数*/
				resList = ReportStatisticsCollection.reportStat(orgNetForm, templateType,offset,limit);
			
//			if(templateType.toString().equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
//				
//				// 1104部分
//				try {
//					orgNetForm.setPre_org_id(operator.getOrgId()); // 查询当前用户的下级机构
//					// 取得记录总数
//					recordCount = StrutsOrgNetDelegate.selectSubOrgCount(orgNetForm ,operator);
//					// 显示分页后的记录
//					if (recordCount > 0) {
//						resList = new ArrayList();
//						List list = StrutsOrgNetDelegate.selectSubOrgList(orgNetForm, operator, offset, limit);
//						
//						ReportInForm reportInForm = new ReportInForm();
//						reportInForm.setYear(orgNetForm.getYear());
//						reportInForm.setTerm(orgNetForm.getTerm());
//						
//						OrgNetForm orgForm = null;
//
//						for (Iterator iter = list.iterator(); iter.hasNext();) {
//							
//							orgForm = (OrgNetForm) iter.next();
//							this.setSubOrgInfo(orgForm, reportInForm);
//
//							resList.add(orgForm);
//						}
//					}
//				} catch (Exception ex) {
//					log.printStackTrace(ex);
//					messages.add(resources
//							.getMessage("select.dataReport.failed"));
//				}
//
//			} else {
//
//				// 非1104部分	
//				OrgInfoForm orgInfoForm = new OrgInfoForm();
//				orgInfoForm.setYear(orgNetForm.getYear());
//				orgInfoForm.setTerm(orgNetForm.getTerm());
//				orgInfoForm.setOrgName(orgNetForm.getOrg_name());
//
//				try {
//					orgInfoForm.setParentOrgId(operator.getOrgId()); // 查询当前用户的下级机构
//					
//					// 取得机构总数
//					recordCount = AFOrgDelegate.selectSubOrgCount(orgInfoForm, operator, templateType);
//					
//					// 显示分页后的记录
//					if (recordCount > 0) {
//						
//						resList = new ArrayList();
//						List list = AFOrgDelegate.selectSubOrgList(orgInfoForm, operator, templateType, offset, limit);
//
//						OrgInfoForm orgForm = null;
//						OrgNetForm orgNForm = null;
//
//						for (Iterator iter = list.iterator(); iter.hasNext();) {
//							orgForm = (OrgInfoForm) iter.next();
//							this.setSubOrgInfoNoCBRC(orgForm, orgNetForm.getDate(),Integer.valueOf(templateType));
//							orgNForm = new OrgNetForm();
//							orgNForm.setOrg_id(orgForm.getOrgId());
//							orgNForm.setOrg_type_name(orgForm.getOrgType());
//							orgNForm.setOrg_name(orgForm.getOrgName());
//							orgNForm.setBsReportNum(orgForm.getBsReportNum());
//							orgNForm.setCbReportNum(orgForm.getCbReportNum());
//							orgNForm.setFhReportNum(orgForm.getFhReportNum());
//							orgNForm.setSqReportNum(orgForm.getSqReportNum());
//							orgNForm.setYbReportNum(orgForm.getYbReportNum());
//							resList.add(orgNForm);
//						}
//					}
//				} catch (Exception ex) {
//					log.printStackTrace(ex);
//					messages.add(resources
//							.getMessage("select.dataReport.failed"));
//				}
//
//			}
			
			// 把ApartPage对象存放在request范围内
			
			aPage.setTerm(this.getTerm(orgNetForm));
			aPage.setCount(recordCount);
			request.setAttribute(Config.APART_PAGE_OBJECT, aPage);

			if (messages.getMessages() != null
					&& messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			if (resList != null && resList.size() > 0){
				request.setAttribute("Records", resList);
				//aPage.setCount(resList.size());
			}

		return mapping.findForward("view");
	}

	/**
	 * 设置查询传递参数
	 * 
	 * @param orgNetForm
	 * @return String 查询的URL
	 */
	public String getTerm(OrgNetForm orgNetForm) {
		String term = "";

		/** 加入机构名称查询条件 */
		if (orgNetForm.getOrg_name() != null
				&& !orgNetForm.getOrg_name().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "orgName=" + orgNetForm.getOrg_name();
		}
		/** 加入报表期数条件 */
		if (orgNetForm.getDate() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "date=" + orgNetForm.getDate();
		}
		if (term.indexOf("?") >= 0)
			term = term.substring(term.indexOf("?") + 1);

		return term;
	}
	
//	/**
//	 * 递规获取机构报送情况及分行报送情况信息CBRC
//	 * 
//	 * @param orgNetForm
//	 *            当前机构信息
//	 * @param reportInForm
//	 *            机构应报报表信息
//	 * @return void
//	 */
//	private void setSubOrgInfo(OrgNetForm orgNetForm, ReportInForm reportInForm) {
//		if (orgNetForm != null && orgNetForm.getOrg_id() != null
//				&& reportInForm != null) {
//
//			// 机构应报报表
//			orgNetForm.setYbReportNum(new Integer(StrutsReportInDelegate
//					.selectOrgReportStatisticsYB(orgNetForm.getOrg_id(),
//							reportInForm)));
//			// 机构已报报表
//			orgNetForm.setBsReportNum(new Integer(StrutsReportInDelegate
//					.selectOrgReportStatisticsBS(orgNetForm.getOrg_id(),
//							reportInForm)));
//
//			// 机构已复核报表
///*			orgNetForm.setFhReportNum(new Integer(StrutsReportInDelegate
//					.selectOrgReportStatisticsFH(orgNetForm.getOrg_id(),
//							reportInForm)));*/
//
//			// 机构已审签报表
//			orgNetForm.setSqReportNum(new Integer(StrutsReportInDelegate
//					.selectOrgReportStatisticsSQ(orgNetForm.getOrg_id(),
//							reportInForm)));
//
//			// 机构重报过的报表
//			orgNetForm.setCbReportNum(new Integer(StrutsReportInDelegate
//					.selectOrgReportStatisticsCB(orgNetForm.getOrg_id(),
//							reportInForm)));
//
//			orgNetForm.setSubOrgList(StrutsOrgNetDelegate
//					.selectSubOrgList(orgNetForm.getOrg_id()));
//
//			if (orgNetForm.getSubOrgList() != null
//					&& orgNetForm.getSubOrgList().size() > 0) {
//				OrgNetForm orgForm = null;
//				for (int i = 0; i < orgNetForm.getSubOrgList().size(); i++) {
//					orgForm = (OrgNetForm) orgNetForm.getSubOrgList().get(i);
//
//					this.setSubOrgInfo(orgForm, reportInForm);
//				}
//			} else
//				return;
//		}
//	}
//
//	/**
//	 * 递规获取机构报送情况及分行报送情况信息
//	 * 
//	 * @param orgNetForm
//	 *            当前机构信息
//	 * @param reportInForm
//	 *            机构应报报表信息
//	 * @return void
//	 */
//	private void setSubOrgInfoNoCBRC(OrgInfoForm orgInfoForm,
//			String date ,Integer templateType) {
//		
//		if (orgInfoForm != null && orgInfoForm.getOrgId() != null
//				&& date != null && templateType != null) {
//
//			// 机构应报报表
//			orgInfoForm.setYbReportNum(new Integer(AFReportDelegate
//					.selectOrgReportStatisticsYB(orgInfoForm.getOrgId(),
//							date , templateType)));
//			// 机构已报报表
//			orgInfoForm.setBsReportNum(new Integer(AFReportDelegate
//					.selectOrgReportStatisticsBS(orgInfoForm.getOrgId(),
//							date , templateType)));
//
//			// 机构已复核报表
///*			orgInfoForm.setFhReportNum(new Integer(AFReportDelegate
//					.selectOrgReportStatisticsFH(orgInfoForm.getOrgId(),
//							date , templateType)));*/
//
//			// 机构已审签报表
//			orgInfoForm.setSqReportNum(new Integer(AFReportDelegate
//					.selectOrgReportStatisticsSQ(orgInfoForm.getOrgId(),
//							date , templateType)));
//
//			// 机构重报过的报表
//			orgInfoForm.setCbReportNum(new Integer(AFReportDelegate
//					.selectOrgReportStatisticsCB(orgInfoForm.getOrgId(),
//							date , templateType)));
//
//			orgInfoForm.setSubOrgList(AFOrgDelegate
//					.selectSubOrgList(orgInfoForm.getOrgId()));
//
//			if (orgInfoForm.getSubOrgList() != null
//					&& orgInfoForm.getSubOrgList().size() > 0) {
//				OrgInfoForm orgForm = null;
//				for (int i = 0; i < orgInfoForm.getSubOrgList().size(); i++) {
//					orgForm = (OrgInfoForm) orgInfoForm.getSubOrgList().get(i);
//
//					this.setSubOrgInfoNoCBRC(orgForm, date, templateType);
//				}
//			} else
//				return;
//		}
//	}
}