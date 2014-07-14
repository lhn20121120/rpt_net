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
 * ��ʹ��hibernate ���Ը� 2011-12-21
 * ������ܣ�ũ�̣�
 * @author Dennis Yee
 *
 */
public class ViewCollectNXAction extends Action {

	private FitechException log = new FitechException(ViewCollectNXAction.class);

	/**
	 * @param result
	 *            ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	 * @param ReportInForm
	 * @param request
	 * @exception Exception
	 *                ���쳣��׽���׳�
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
			
			/** ȡ�õ�ǰ�û���Ȩ����Ϣ */
			if (session.getAttribute("multi") != null) {
				messages = (FitechMessages) session.getAttribute("multi");
				session.setAttribute("multi", null);
			}
			Operator operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			//��ò�ѯ����
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
			
			/** ȡ�ñ���������� */
			if (session.getAttribute(Config.REPORT_SESSION_FLG)!=null)
				reportInForm.setTemplateType(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
			
			//ȡ�ø��û��豨����
			/**��ʹ��hibernate ���Ը� 2011-12-21**/
			resList = AFReportDelegate.selectNeedReportList(reportInForm, operator);

			if (resList != null && resList.size() > 0) {
				
				list = new ArrayList();
				afOrgDelegate = new AFOrgDelegate();
				afReportDelegate = new AFReportDelegate();
				
				for (Iterator iter = resList.iterator(); iter.hasNext();) {
					
					Aditing aditing = (Aditing) iter.next();
					
					// �ñ���Ĺ�������
					String childRep = aditing.getTemplateId();
					
					// Ӧ������������
					int needOrgCount = 0;
					// ʵ����������
					int donum = 0;
					// ʵ��/Ӧ���ַ���
					String doNeedStr = "";

					
					//���б���
//					if(reportInForm.getTemplateType().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){

						//���ܹ���
						Integer collectRule = null;

						//��õ�ǰ�ñ����ͻ�������Ϣ
						/**��ʹ��hibernate ���Ը� 2011-12-21**/
						AfOrg reportOrg = AFOrgDelegate.selectOne(aditing.getOrgId());
						
						//����Ա���˵������к���������Ļ�������
//						if(operator.isSuperManager() && 
//								(!reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)
//								&&!reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.VIRTUAL_TOPBANK)))
//							continue;
						
						//���е�ȡ�����������
//						if (reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)){
//							collectRule = 1;
//						}
						//�������ȡ��ʵ��������
						//else
						if(reportOrg.getIsCollect()!=null 
								&& reportOrg.getIsCollect().equals(Long
										.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
							collectRule = 2;
						}
						//������������
						else {
							collectRule = 3;
						}
						
						//�����������⴦��(����ʱȡ���ܻ����趨�Ļ��ܹ�ϵ������㼶����)
						if(reportInForm.getTemplateType().equals(com.fitech.gznx.common.Config.PBOC_REPORT)
								&& reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK))
							collectRule = 2;
						
						/**�ж��Ƿ�Ϊ������ܣ����Ϊ�������ֱ�Ӵ�����*/
						String collFormula = AFTemplateCollRuleDelegate.getCollFormulaName(aditing.getOrgId(),aditing.getTemplateId(),aditing.getVersionId());
						if(collFormula!=null&&!collFormula.trim().equals("")){//�������
				            needOrgCount = AFTemplateCollRuleDelegate.getNeedReportNUM(collFormula, aditing.getOrgId());//����Ӧ����
				            String gzOrgIds = AFTemplateCollRuleDelegate.getNeedOrgIds(collFormula, aditing.getOrgId());//Ӧ�������ַ���
				            donum = afReportDelegate.getAvailabilityOrgIdCount(childRep, aditing.getVersionId(),
									aditing.getYear(),aditing.getTerm(),aditing.getDay(),
									aditing.getCurId(), aditing.getActuFreqID(),gzOrgIds,null,8,false);//����ʵ����
				         }else{//һ�����
						// �õ�����ı��ͻ�������
						/**��ʹ��hibernate ���Ը� 2011-12-21**/
						needOrgCount = afOrgDelegate.getMustOrgCount(childRep, aditing.getVersionId(), 
								childOrgIds, aditing.getOrgId(), collectRule);
						
						if(true || needOrgCount!=0){ //(2013-5-20ȥ���ù����ж��Լ���û���¼��еĻ�������)
							// ��û����ѱ��ͱ�������
							/**��ʹ��hibernate ���Ը� 2011-12-21**/
							donum = afReportDelegate.getAvailabilityOrgIdCount(childRep, aditing.getVersionId(),
										aditing.getYear(),aditing.getTerm(),aditing.getDay(),
										aditing.getCurId(), aditing.getActuFreqID(),childOrgIds,aditing.getOrgId(),collectRule,false);
						}else{
							continue;
						}
				        }
//					}
					//��������
//					else{
//						
//						//���ܹ���
//						Integer collectRule = null;
//
//						//��õ�ǰ�ñ����ͻ�������Ϣ
//						AfOrg reportOrg = AFOrgDelegate.selectOne(aditing.getOrgId());
//						
//						//����Ա���˵������к���������Ļ�������
//						if(operator.isSuperManager() && 
//								(!reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)
//								&&!reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.VIRTUAL_TOPBANK)))
//							continue;
//						//�������ȡ��ʵ��������
//						if(reportOrg.getIsCollect()!=null 
//								&& reportOrg.getIsCollect().equals(Long.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
//							collectRule = 2;
//						}
//						//������������
//						else {
//							collectRule = 3;
//						}
//						// �õ�����ı��ͻ�������
//						needOrgCount = afOrgDelegate.getMustOrgCount(childRep, aditing.getVersionId(), 
//								childOrgIds, aditing.getOrgId(), collectRule);
//						
//						if(needOrgCount!=0){
//							// ��û����ѱ��ͱ�������
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
						

					if(true || needOrgCount>0){//��ӣ���ȥһЩ����Ҫ�Ļ��ܹ�ϵ  (2013-5-20ȥ���ù����ж��Լ���û���¼��еĻ�������)
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
			messages.add("�����������ʧ�ܣ�");
		}

		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);
		//����list!=null ���� ���� ҳ�汨��
		if (resList != null && resList.size() > 0&&list!=null)
			request.setAttribute(Config.RECORDS, list);
		else
			request.setAttribute(Config.RECORDS, null);

		request.setAttribute("date", reportInForm.getDate().toString());
		return mapping.findForward("view");
	}
}
