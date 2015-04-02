package com.fitech.net.action;

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
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFDataDelegate;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsCollectDelegate;
import com.fitech.net.adapter.VorgCollectDelegate;

/**
 * @���ܸ������ı��ͱ���
 * @author �ף�
 */
public final class CollectReportDataAction extends Action{
	private FitechException log = new FitechException(CollectReportDataAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form
			, HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{

		FitechMessages messages = new FitechMessages();
		ReportInForm reportInForm = (ReportInForm)form;
		RequestUtils.populate(reportInForm, request);
		
		String childRepId = null;
		String versionId = null;
		Integer dataRangeId = null;
		Integer curId = null;
		String term = mapping.findForward("view").getPath();
		try{
			/**���뱨��������*/
			if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "childRepId=" + reportInForm.getChildRepId();
			}
			/**���뱨����������*/
			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				/**����WebLogic����Ҫ����ת�룬ֱ����Ϊ��������*/
				term += "repName=" + reportInForm.getRepName();
				/**����WebSphere����Ҫ�Ƚ���ת�룬����Ϊ��������*/
				//term += "repName=" + new String(reportInForm.getRepName().getBytes("gb2312"), "iso-8859-1");
			}
			/**����ģ����������*/
			if(reportInForm.getFrOrFzType() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "frOrFzType=" + reportInForm.getFrOrFzType();
			}			
			/**���뱨���������*/
			if(reportInForm.getYear() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "year=" + reportInForm.getYear();				
			}
			/**���뱨����������*/
			if(reportInForm.getTerm() != null){
				term += (term.indexOf("?")>=0 ? "&" : "?");
				term += "term=" + reportInForm.getTerm();
			}
			
			/** ȡ�õ�ǰ�û���Ȩ����Ϣ */
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

			String isMulti = request.getParameter("select_collect_type");
			if (isMulti == null || !isMulti.equals("multi")){
				childRepId = request.getParameter("repId");
				versionId = request.getParameter("versionId");
				dataRangeId = new Integer(request.getParameter("dataRangeId"));
				curId = new Integer(request.getParameter("curId"));
			}

			//String childOrgIds = operator != null ? operator.getChildOrgIds() : "";
//			String orgId = request.getParameter("orgId");
//			List orgLst=VorgCollectDelegate.getRepOrg(childRepId, versionId, dataRangeId, reportInForm.getYear(), reportInForm.getTerm(), orgId);
//			String childOrgIds="";
//			if(orgLst!=null && orgLst.size()>0){
//				Aditing aditing=null;
//				for(int i=0;i<orgLst.size();i++){
//					aditing=(Aditing)orgLst.get(i);
//					if(childOrgIds!=null && !childOrgIds.equals(""))
//						childOrgIds=childOrgIds+",'"+aditing.getOrgId()+"'";
//					else
//						childOrgIds="'"+aditing.getOrgId()+"'";
//				}
//			}
//			AfOrg aforg=AFOrgDelegate.getOrgInfo(orgId);
//			/**
//			 * �ж��Ƿ�Ϊ�������
//			 * **/
//			if (aforg.getOrgType().equals("-99")){   
//				AFDataDelegate afconllect =new AFDataDelegate();
//				afconllect.doCollectCBRC(childRepId, versionId, orgId, childOrgIds, reportInForm.getYear(), reportInForm.getTerm(), dataRangeId, curId, null);
//				}
//			/**
//			 * ��ʵ��������
//			 * */
//			else{
					if ((childRepId == null || childRepId.equals("") || versionId == null || versionId.equals(""))
							&& (isMulti == null || !isMulti.equals("multi"))){
						messages.add("����ʧ�ܣ�");
						if (messages.getMessages() != null && messages.getMessages().size() > 0)
							request.setAttribute(Config.MESSAGES, messages);
		
						return new ActionForward(term);
					}
					StrutsCollectDelegate scd = new StrutsCollectDelegate();
					int succeed = 0;  // �ɹ���
					int failure = 0;  //ʧ����
					if (isMulti != null && isMulti.equals("multi")){
		
						int m = 0;
						String parameterList = "";
						while (m >= 0){
							String parameters = request.getParameter("select_data_collect_id" + m);
							if (parameters != null && !parameters.equals("")){
								m++;
								parameterList += parameters;
							}else break;
						}
						parameterList = parameterList.substring(0, parameterList.length() - 1);
						String parArr[] = parameterList.split("#");
						String strArr[] = null;
						
						for (int i = 0; i < parArr.length; i++){
							strArr = parArr[i].split(",");
							String childRepIdAll = strArr[0].trim();
							String versionIdAll = strArr[1].trim();
							Integer dataRangeIdAll = new Integer(strArr[2].trim());
							Integer curIdAll = new Integer(strArr[3].trim());
							Integer donum = new Integer(strArr[4].trim());        //�������ܼ�¼�Ƿ��������ϱ�
							String orgId=strArr[5].trim();
							if(donum.intValue() == 0){
								messages.add(childRepIdAll+"�������ʧ�ܣ����ϱ����ݣ���");
								failure++;
							}else{
								/**************************/
								/**
								 * ��ȡ�ӻ���Id
								 * **/
								List orgLst=VorgCollectDelegate.getRepOrg(childRepIdAll, versionIdAll, dataRangeIdAll, reportInForm.getYear(), reportInForm.getTerm(), orgId);
								String childOrgIds="";
								if(orgLst!=null && orgLst.size()>0){
									Aditing aditing=null;
									for(int j=0;j<orgLst.size();j++){
										aditing=(Aditing)orgLst.get(j);
										if(childOrgIds!=null && !childOrgIds.equals(""))
											childOrgIds=childOrgIds+",'"+aditing.getOrgId()+"'";
										else
											childOrgIds="'"+aditing.getOrgId()+"'";
									}
								}
								AfOrg aforg=AFOrgDelegate.getOrgInfo(orgId);
								/**
								 * �ж��Ƿ�Ϊ�������
								 * **/
								if (aforg.getOrgType().equals("-99")){   
									AFDataDelegate afconllect =new AFDataDelegate();
									afconllect.doCollectCBRC(childRepIdAll, versionIdAll, orgId, childOrgIds, reportInForm.getYear(), reportInForm.getTerm(), dataRangeIdAll, curIdAll, null);
									}
								/**
								 * ��ʵ��������
								 * */
								else{
								/**************************/
									Integer reportInID = scd.doCollect(childRepIdAll,versionIdAll,orgId,childOrgIds,reportInForm.getYear(),reportInForm.getTerm(),dataRangeIdAll,curIdAll);
									if (reportInID == null){
										messages.add(childRepIdAll+"�������ʧ�ܣ����������쳣����");
										failure++;
									}else if(reportInID.intValue()==-1){
										messages.add(childRepIdAll+"�������ʧ�ܣ�δ���û��ܹ�ϵ����");
										failure++;
									}else{
										messages.add(childRepIdAll+"������ܳɹ���");
										succeed ++;
									}
								}
							}
						}
						
						messages.add("");
						messages.add("");
						messages.add("���ݻ�����ɣ����ܳɹ�"+succeed+"�� ʧ��"+failure+"�ţ�");
						request.getSession().setAttribute("multi",messages);
					}else{
						// ���ܷ���,�ɹ����ؼ�¼ID ʧ�ܷ��� null
						/*****************************************/
						String orgId=request.getParameter("orgId");
						List orgLst=VorgCollectDelegate.getRepOrg(childRepId, versionId, dataRangeId, reportInForm.getYear(), reportInForm.getTerm(), orgId);
						String childOrgIds="";
						if(orgLst!=null && orgLst.size()>0){
							Aditing aditing=null;
							for(int i=0;i<orgLst.size();i++){
								aditing=(Aditing)orgLst.get(i);
								if(childOrgIds!=null && !childOrgIds.equals(""))
									childOrgIds=childOrgIds+",'"+aditing.getOrgId()+"'";
								else
									childOrgIds="'"+aditing.getOrgId()+"'";
							}
						}
						AfOrg aforg=AFOrgDelegate.getOrgInfo(orgId);
						/**
						 * �ж��Ƿ�Ϊ�������
						 * **/
						if (aforg.getOrgType().equals("-99")){   
							AFDataDelegate afconllect =new AFDataDelegate();
							afconllect.doCollectCBRC(childRepId, versionId, orgId, childOrgIds, reportInForm.getYear(), reportInForm.getTerm(), dataRangeId, curId, null);
							}
						/**
						 * ��ʵ��������
						 * */
						else{
						/****************************************/
							Integer reportInID = scd.doCollect(childRepId,versionId,orgId,childOrgIds,reportInForm.getYear(),reportInForm.getTerm(),dataRangeId,curId);
							if(reportInID == null)
								messages.add(childRepId+"�������ʧ�ܣ����������쳣����");
							else if(reportInID.intValue() == -1)
								messages.add(childRepId+"�������ʧ�ܣ�δ���û��ܹ�ϵ����");
							else
								messages.add(childRepId+"������ܳɹ���");
						}
					}
					if (messages.getMessages() != null && messages.getMessages().size() > 0)
						request.setAttribute(Config.MESSAGES, messages);
			return new ActionForward(term);
			
		}catch (Exception ex){
			if(ex.getMessage().equals("���������ͨ�����������ٻ��ܣ�"))
				messages.add(ex.getMessage());
			messages.add("����ʧ�ܣ�");
			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			log.printStackTrace(ex);

			return new ActionForward(term);
		}
	}
}