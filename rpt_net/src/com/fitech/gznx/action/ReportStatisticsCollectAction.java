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
 * �������ͳ��
 * 
 * @author jcm
 * @serialData 2008-02-26
 */
public class ReportStatisticsCollectAction extends Action {
	
	private FitechException log = new FitechException(
			ReportStatisticsCollectAction.class);

	/**
	 * jdbc���� ������oracle�﷨ ������Ҫ�޸� ���Ը� 2011-12-22
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
				//�±�ͳ�ƣ���֧�ֵ���ͳ��
				yestoday = yestoday.substring(0, 7);
				orgNetForm.setStartDate(yestoday);
				orgNetForm.setEndDate(yestoday);
			}
			//orgNetForm.setYear(Integer.valueOf(orgNetForm.getDate().substring(0, 4)));
			//orgNetForm.setTerm(Integer.valueOf(orgNetForm.getDate().substring(5, 7)));
			
			int recordCount = 0; // ��¼����
			int offset = 0; // ƫ����
			int limit = 0; // ÿҳ��ʾ�ļ�¼����

			List resList = null;
			ApartPage aPage = new ApartPage();
			String strCurPage = request.getParameter("curPage");
			if (strCurPage != null) {
				if (!strCurPage.equals(""))
					aPage.setCurPage(new Integer(strCurPage).intValue());
			} else
				aPage.setCurPage(1);
			// ����ƫ����
			offset = (aPage.getCurPage() - 1) * Config.PER_PAGE_ROWS;
			limit = Config.PER_PAGE_ROWS*aPage.getCurPage();
			aPage.setRows(Config.PER_PAGE_ROWS);
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session
						.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			
			//�ֱ�����ͳ��
			Integer templateType = 1;
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				templateType = Integer.valueOf(session.getAttribute(Config.REPORT_SESSION_FLG).toString()); 
			/**jdbc���� oracle�﷨ ������Ҫ�޸� ���Ը� 2011-12-22
			 * ��oracle��to_date���� �޸�Ϊsqlserver ��convert����
			 * ��oracle nvl�﷨�޸�Ϊsqlserver isnull�﷨
			 * ���Ը� 2011-12-27 ������**/
			recordCount=ReportStatisticsCollection.reportStatCount(orgNetForm, templateType);
			if(recordCount>0)
				/**��oracle��to_date���� �޸�Ϊsqlserver ��convert����
				 * ��oracle nvl�﷨�޸�Ϊsqlserver isnull�﷨
				 * oracle rownumα���޸�Ϊsqlserver ROW_NUMBER()����*/
				resList = ReportStatisticsCollection.reportStat(orgNetForm, templateType,offset,limit);
			
//			if(templateType.toString().equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
//				
//				// 1104����
//				try {
//					orgNetForm.setPre_org_id(operator.getOrgId()); // ��ѯ��ǰ�û����¼�����
//					// ȡ�ü�¼����
//					recordCount = StrutsOrgNetDelegate.selectSubOrgCount(orgNetForm ,operator);
//					// ��ʾ��ҳ��ļ�¼
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
//				// ��1104����	
//				OrgInfoForm orgInfoForm = new OrgInfoForm();
//				orgInfoForm.setYear(orgNetForm.getYear());
//				orgInfoForm.setTerm(orgNetForm.getTerm());
//				orgInfoForm.setOrgName(orgNetForm.getOrg_name());
//
//				try {
//					orgInfoForm.setParentOrgId(operator.getOrgId()); // ��ѯ��ǰ�û����¼�����
//					
//					// ȡ�û�������
//					recordCount = AFOrgDelegate.selectSubOrgCount(orgInfoForm, operator, templateType);
//					
//					// ��ʾ��ҳ��ļ�¼
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
			
			// ��ApartPage��������request��Χ��
			
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
	 * ���ò�ѯ���ݲ���
	 * 
	 * @param orgNetForm
	 * @return String ��ѯ��URL
	 */
	public String getTerm(OrgNetForm orgNetForm) {
		String term = "";

		/** ����������Ʋ�ѯ���� */
		if (orgNetForm.getOrg_name() != null
				&& !orgNetForm.getOrg_name().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "orgName=" + orgNetForm.getOrg_name();
		}
		/** ���뱨���������� */
		if (orgNetForm.getDate() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "date=" + orgNetForm.getDate();
		}
		if (term.indexOf("?") >= 0)
			term = term.substring(term.indexOf("?") + 1);

		return term;
	}
	
//	/**
//	 * �ݹ��ȡ����������������б��������ϢCBRC
//	 * 
//	 * @param orgNetForm
//	 *            ��ǰ������Ϣ
//	 * @param reportInForm
//	 *            ����Ӧ��������Ϣ
//	 * @return void
//	 */
//	private void setSubOrgInfo(OrgNetForm orgNetForm, ReportInForm reportInForm) {
//		if (orgNetForm != null && orgNetForm.getOrg_id() != null
//				&& reportInForm != null) {
//
//			// ����Ӧ������
//			orgNetForm.setYbReportNum(new Integer(StrutsReportInDelegate
//					.selectOrgReportStatisticsYB(orgNetForm.getOrg_id(),
//							reportInForm)));
//			// �����ѱ�����
//			orgNetForm.setBsReportNum(new Integer(StrutsReportInDelegate
//					.selectOrgReportStatisticsBS(orgNetForm.getOrg_id(),
//							reportInForm)));
//
//			// �����Ѹ��˱���
///*			orgNetForm.setFhReportNum(new Integer(StrutsReportInDelegate
//					.selectOrgReportStatisticsFH(orgNetForm.getOrg_id(),
//							reportInForm)));*/
//
//			// ��������ǩ����
//			orgNetForm.setSqReportNum(new Integer(StrutsReportInDelegate
//					.selectOrgReportStatisticsSQ(orgNetForm.getOrg_id(),
//							reportInForm)));
//
//			// �����ر����ı���
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
//	 * �ݹ��ȡ����������������б��������Ϣ
//	 * 
//	 * @param orgNetForm
//	 *            ��ǰ������Ϣ
//	 * @param reportInForm
//	 *            ����Ӧ��������Ϣ
//	 * @return void
//	 */
//	private void setSubOrgInfoNoCBRC(OrgInfoForm orgInfoForm,
//			String date ,Integer templateType) {
//		
//		if (orgInfoForm != null && orgInfoForm.getOrgId() != null
//				&& date != null && templateType != null) {
//
//			// ����Ӧ������
//			orgInfoForm.setYbReportNum(new Integer(AFReportDelegate
//					.selectOrgReportStatisticsYB(orgInfoForm.getOrgId(),
//							date , templateType)));
//			// �����ѱ�����
//			orgInfoForm.setBsReportNum(new Integer(AFReportDelegate
//					.selectOrgReportStatisticsBS(orgInfoForm.getOrgId(),
//							date , templateType)));
//
//			// �����Ѹ��˱���
///*			orgInfoForm.setFhReportNum(new Integer(AFReportDelegate
//					.selectOrgReportStatisticsFH(orgInfoForm.getOrgId(),
//							date , templateType)));*/
//
//			// ��������ǩ����
//			orgInfoForm.setSqReportNum(new Integer(AFReportDelegate
//					.selectOrgReportStatisticsSQ(orgInfoForm.getOrgId(),
//							date , templateType)));
//
//			// �����ر����ı���
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