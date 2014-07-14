package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;

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
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.entity.CollResultBean;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfTemplateCollRule;
import com.fitech.gznx.service.AFDataDelegate;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;

/**
 * ���ܸ������ı��ͱ���
 * @author �ף�
 */
public class CollectReportDataNXAction extends Action {

	private FitechException log = new FitechException(
			CollectReportDataNXAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		FitechMessages messages = new FitechMessages();
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);
//		if(!StringUtil.isEmpty(reportInForm.getDate())){
//			String ttday = reportInForm.getDate();
//			reportInForm.setYear(ttday.substring(0, 4));
//			reportInForm.setTerm(ttday.substring(5, 7));
//			reportInForm.setDay(ttday.substring(8, 10));
//		}
		String childRepId = null;
		String versionId = null;
		Integer repFreqId = null;
		Integer curId = null;
		String orgId = null;
		String term = mapping.findForward("view").getPath();
		String backStr = null;
		
		String donum = request.getParameter("donum");
		try {

			/** ȡ�õ�ǰ�û���Ȩ����Ϣ */
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			
			/** ȡ��ģ��������� */
			Integer templateType = null;
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				templateType = Integer.valueOf(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
			
			String isMulti = request.getParameter("select_collect_type");
			//�ж��Ƿ��ǵݹ���� ���������ֻ����һ����� ��
			if (isMulti == null || !isMulti.equals("multi")) {
				childRepId = request.getParameter("templateId");
				versionId = request.getParameter("versionId");
				String  sRepFreqId = request.getParameter("repFreqId");
				String  scurId = request.getParameter("curId");
				if(sRepFreqId != null)
					repFreqId = new Integer(sRepFreqId.trim());
				if(scurId != null)
					curId = new Integer(scurId.trim());
				orgId = request.getParameter("orgId");
				backStr = request.getParameter("backStr");
			}
			String iterFlag = request.getParameter("iterFlag");
			boolean isIterator = iterFlag!=null && iterFlag.equals("1") ? true : false;
			String childOrgIds = operator != null ? operator.getChildOrgIds() : "";
			
			//orgId = operator != null ? operator.getOrgId() : "";
			//
			if(backStr!=null && !backStr.equals("")){
				
				String[] splitQry = backStr.split("_");					
				if(splitQry.length==6)
					backStr = "?date=" + splitQry[0]
					             + "&templateId="+ splitQry[1]
					             + "&repName="+ splitQry[2]
					             + "&orgId="+ splitQry[3]
					             + "&bak1=" + splitQry[4]
					             + "&repFreqId=" + splitQry[5];
			
			}
			//�пռȱ�ʾ����ʧ�ܣ����ʧ�ܼ�¼
			if ((childRepId == null || childRepId.equals("")
					|| versionId == null || versionId.equals("")
					/*|| childOrgIds == null || childOrgIds.equals("")*/)
					&& (isMulti == null || !isMulti.equals("multi"))) {
				
				messages.add("����ʧ�ܣ�");
				
				if (messages.getMessages() != null
						&& messages.getMessages().size() > 0)
					request.setAttribute(Config.MESSAGES, messages);

				return new ActionForward(term + this.getTerm(reportInForm));
			}
			
			AFOrgDelegate afOrgDele = new AFOrgDelegate();
			List mustOrgs = null;
			
			//��ʼ����
			AFDataDelegate afDataDele = new AFDataDelegate();
			if (isMulti != null && isMulti.equals("multi")) {//��������
				int m = 0;
				String parameterList = "";
				while (m >= 0) {
					String parameters = request.getParameter("select_data_collect_id" + m);
					
					if (parameters != null && !parameters.equals("")) {
						m++;
						parameterList += parameters;
					} else
						break;
				}
				parameterList = parameterList.substring(0, parameterList.length() - 1);
				String[] parArr = parameterList.split("#");
				
				CollResultBean collResult = new CollResultBean();
				String strArr[] = null;
				boolean rs = false;
				for (int i = 0; i < parArr.length; i++) {//����ѭ��
					strArr = parArr[i].split(",");
					if(strArr!=null && strArr.length>0){
						for(int k=0;k<strArr.length;k++){
							strArr[k] = strArr[k].trim();
						}
					}
					if(strArr[5] != null && strArr[5].equals("null")){
						strArr[5] = donum;
					}
					AfTemplateCollRule rule = AFDataDelegate.findCollectGacha(strArr[0], strArr[1], strArr[2]);
					if(rule==null)//��ͨ����
						rs = AFDataDelegate.collectDataIter(strArr,messages, afDataDele,
								 templateType,childOrgIds, afOrgDele,collResult,operator,isIterator,strArr[2]);
					else//�������
						rs = AFDataDelegate.collectDataByGacha(rule,strArr,messages, afDataDele,
								 templateType,childOrgIds, afOrgDele,collResult,operator,isIterator);
					AfOrg org = AFOrgDelegate.getOrgInfo(strArr[2].trim());
//					messages.add("");
//					messages.add("");
					if(rs)
						messages.add(org.getOrgName() +  strArr[0] + "���ݻ��ܳɹ�������У��ͨ����");
//					else//2013-01-03:LuYueFei:������ܴ������⣬���Ѿ���������ʧ�ܵ���ʾ����˲����ظ���ʾ����ʧ�ܵ���Ϣ
//						messages.add(org.getOrgName() +  strArr[0] + "���ݻ���δ��ɣ�");
		//				messages.add(org.getOrgName() +  strArr[0] + "���ݻ���δ���" + collResult.getSucceed() + "�� ʧ��" + collResult.getFailure() + "�ţ�");
				}
				request.getSession().setAttribute("multi", messages);
			} else {
				//���Ż���
				
				//���ܹ���
				Integer collectRule = null;

//				if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
										
					//��õ�ǰ�ñ����ͻ�������Ϣ
					/**��ʹ��hibernate ���Ը� 2011-12-21**/
					AfOrg reportOrg = AFOrgDelegate.selectOne(orgId);
					
					//���е�ȡ�����������
//					if (reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)){
//						collectRule = 1;
//					}
					//�������ȡ��ʵ��������
//					else 
					if(reportOrg.getIsCollect().equals(Long.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
						collectRule = 2;
					}
					//������������
					else {
						collectRule = 3;
					}
//				}else{
//					collectRule = 0;
//				}

				//�����������⴦��(����ʱȡ���ܻ����趨�Ļ��ܹ�ϵ������㼶����)
				if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)
						&& reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK))
					collectRule = 2;
				
				// �õ�һ�ű���ı��ͻ����б�
//				if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
					
					//�������ܵĻ������
					/**��ʹ��hibernate ���Ը� 2011-12-21**/
					mustOrgs = afOrgDele.getMustOrgList(childRepId,versionId, childOrgIds, orgId, collectRule);					
					childOrgIds = "";
					for (int j=0;j<mustOrgs.size();j++){
						AfOrg afOrg = (AfOrg) mustOrgs.get(j);
						childOrgIds += childOrgIds.equals("") ? "'" + afOrg.getOrgId() + "'" : ",'" + afOrg.getOrgId() + "'";
					}
//				}

				Integer reportInID = null;
				
				//�����嵥�����ֱ���
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				int reportStyle = AFTemplateDelegate.getReportStyle(childRepId, versionId);
				
				if(reportStyle == Config.REPORT_STYLE_QD.intValue()){
					//�嵥ʽ����
					/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21**/
					reportInID = afDataDele.collectQDReport(childRepId, versionId, 
							Integer.valueOf(reportInForm.getYear()),
							Integer.valueOf(reportInForm.getTerm()), 
							Integer.valueOf(reportInForm.getDay()),
							repFreqId, curId, orgId, childOrgIds);
					
				}else{
					// ���ܷ���,�ɹ����ؼ�¼ID ʧ�ܷ��� null
					/**jdbc���� ��Ҫ�޸� ���Ը� 2011-12-21
					 * oracle�﷨**/
					reportInID = afDataDele.doCollect(childRepId, versionId,
							orgId, childOrgIds, Integer.valueOf(reportInForm.getYear().trim()),
							Integer.valueOf(reportInForm.getTerm().trim()), Integer.valueOf(reportInForm.getDay().trim()), 
							repFreqId, curId ,templateType,orgId);
				}
				
				if (reportInID == null)
					messages.add(childRepId + "�������ʧ�ܣ����������쳣����");
				else if (reportInID.intValue() == -1)
					messages.add(childRepId + "�������ʧ�ܣ�δ���û��ܹ�ϵ����");
				else
					messages.add(childRepId + "������ܳɹ���");
			}
			if (messages.getMessages() != null
					&& messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);

			return new ActionForward(term+(backStr==null?"":backStr)); //+ this.getTerm(reportInForm));
			
		} catch (Exception ex) {
			messages.add("����ʧ�ܣ�");
			if (messages.getMessages() != null
					&& messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			log.printStackTrace(ex);

			return new ActionForward(term + this.getTerm(reportInForm));
		}
	}
	
	/**
	 * �õ�
	 * @param reportInForm
	 * @return
	 */
	private String getTerm(AFReportForm reportInForm){
		String term = "";
		/** ���뱨�������� */
		if (reportInForm.getTemplateId() != null
				&& !reportInForm.getTemplateId().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "templateId=" + reportInForm.getTemplateId();
		}
		/** ���뱨���������� */
		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			/** ����WebLogic����Ҫ����ת�룬ֱ����Ϊ�������� */
			term += "repName=" + reportInForm.getRepName();
			/** ����WebSphere����Ҫ�Ƚ���ת�룬����Ϊ�������� */
			// term += "repName=" + new
			// String(reportInForm.getRepName().getBytes("gb2312"),
			// "iso-8859-1");
		}
		/** ����ģ���������� */
		if (reportInForm.getBak1() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/** ���뱨��������� */
		if (reportInForm.getDate() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}

		
		return term;
	}
}
