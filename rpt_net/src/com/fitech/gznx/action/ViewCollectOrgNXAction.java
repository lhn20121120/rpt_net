package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AfTemplateCollRuleForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfTemplateCollRule;
import com.fitech.gznx.po.AfTemplateCollRuleId;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFTemplateCollRuleDelegate;

/**
 * ������ܻ��������ѯ
 * @author WH
 *
 */
public class ViewCollectOrgNXAction extends Action {
	
	private FitechException log = new FitechException(ViewCollectOrgNXAction.class);

	/**
	 * @param result
	 *            ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	 * @param MRegionForm
	 * @param request
	 * @exception Exception
	 *                ���쳣��׽���׳�
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {

		FitechMessages messages = new FitechMessages();
		AFReportForm afReportForm = (AFReportForm) form;
		
		String date = "";
		
		try {
			
			date = request.getParameter("date");
			
			/** ȡ�õ�ǰ�û���Ȩ����Ϣ */
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

			String templateId = request.getParameter("templateId");
			String versionId = request.getParameter("versionId");
			Integer repFreqId = Integer.valueOf(request.getParameter("repFreqId"));
			Integer year = Integer.valueOf(request.getParameter("year"));
			Integer term = Integer.valueOf(request.getParameter("term"));
			Integer day = Integer.valueOf(request.getParameter("day"));
			Integer curId = Integer.valueOf(request.getParameter("curId"));
			String orgId = request.getParameter("orgId");
			String childOrgIds = operator != null ? operator.getChildOrgIds() : "";

			//��af_template_coll_rule���ѯ�Ƿ��иü�¼���ж��Ƿ���������ܣ������Ϊnull ��ʾ ���������
			AfTemplateCollRuleForm aform= new AfTemplateCollRuleForm();
			aform.setOrg_id(orgId);
			aform.setTemplate_id(templateId);
			aform.setVersion_id(versionId);
			boolean isgacha = AFTemplateCollRuleDelegate.isExistCollRule(aform);
			
			/** ��ñ���������� */
			Integer templateType = null;
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				templateType = Integer.valueOf(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
			
			if (templateId == null || templateId.equals("")
					|| versionId == null || versionId.equals("")
					|| childOrgIds == null || childOrgIds.equals("")) {

				messages.add("�鿴��ϸ��Ϣʧ�ܣ�");

				if (messages.getMessages() != null && messages.getMessages().size() > 0)
					request.setAttribute(Config.MESSAGES, messages);

				return new ActionForward("/viewCollectNX.do");
			}
			
			List resList = null;
			
			//���ܹ���
			Integer collectRule = null;

			if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
				
				orgId = request.getParameter("orgId");
				
				//��õ�ǰ�ñ����ͻ�������Ϣ
				AfOrg reportOrg = AFOrgDelegate.selectOne(orgId);
				
				//���е�ȡ�����������
//				if (reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)){
//					collectRule = 1;
//				}
				//�������ȡ��ʵ��������
				//else 
				if(reportOrg.getIsCollect().equals(Long.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
					collectRule = 2;
				}
				//������������
				else {
					collectRule = 3;
				}
				
				//�����������⴦��(����ʱȡ���ܻ����趨�Ļ��ܹ�ϵ������㼶����)
				if(reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK))
					collectRule = 2;
				
			}else{
				//collectRule = 0;
				orgId = request.getParameter("orgId");
				
				//��õ�ǰ�ñ����ͻ�������Ϣ
				AfOrg reportOrg = AFOrgDelegate.selectOne(orgId);
				if(reportOrg.getIsCollect().equals(Long.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
					//collectRule = 5;
					collectRule = 2;
				}
				//������������
				else {
					//collectRule = 6;
					collectRule = 3;
				}
			}
			
			// �õ�һ�ű���ı��ͻ����б�
			AFOrgDelegate afOrgDele = new AFOrgDelegate();
			List mustOrgs =null;
			if(isgacha){
				String collFormula = AFTemplateCollRuleDelegate.getCollFormulaName(orgId, templateId, versionId);
				mustOrgs = AFTemplateCollRuleDelegate.getNeedOrgs(collFormula,orgId);
			}else{
				mustOrgs = afOrgDele.getMustOrgList(templateId, versionId, childOrgIds, orgId, collectRule);
			}
			
			if(mustOrgs!=null && mustOrgs.size()>0){
				
				resList = new ArrayList();
				
				for(int i = 0 ;i<mustOrgs.size();i++){
					
					Aditing aditing = new Aditing();
					String orgid = ((AfOrg)mustOrgs.get(i)).getOrgId();
					aditing.setOrgId(orgid);
					aditing.setOrgName(((AfOrg)mustOrgs.get(i)).getOrgName());
					aditing.setChildRepId(templateId);
					aditing.setVersionId(versionId);
					
					// ���Ҹû���,�ñ���ı���״̬   ����Ч����
					Integer state = AFReportDelegate.getReportState(templateId, versionId, repFreqId, year, term, day, curId, orgid);
					
					if(state!= null){
						aditing.setIsPass(state);
					}
					
					resList.add(aditing);
				}
			}
			
			request.setAttribute("date", date);
			
			if(resList!=null && resList.size()>0)
		 		 request.setAttribute(Config.RECORDS,resList);
		 	 else
		 		 request.setAttribute(Config.RECORDS,null);
			return mapping.findForward("view");

		}
		catch (Exception ex)
		{
			log.printStackTrace(ex);
			messages.add("�鿴��ϸ��Ϣʧ�ܣ�");

			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);

			return new ActionForward("/viewCollectNX.do?date=" + date);
		}
	}
}
