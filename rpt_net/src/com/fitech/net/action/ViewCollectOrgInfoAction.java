package com.fitech.net.action;

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
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.dataCollect.DB2ExcelHandler;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.adapter.StrutsCollectDelegate;
import com.fitech.net.adapter.VorgCollectDelegate;
import com.fitech.net.hibernate.CollectReal;
import com.fitech.net.hibernate.OrgNet;

/**
 * @�鿴�������ı�������ϸ���
 * @author �ף�
 */
public final class ViewCollectOrgInfoAction extends Action
{
	private FitechException log = new FitechException(ViewMRegionAction.class);

	/**
	 * @param result
	 *            ��ѯ���ر�־,����ɹ�����true,���򷵻�false
	 * @param MRegionForm
	 * @param request
	 * @exception Exception
	 *                ���쳣��׽���׳�
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{

		FitechMessages messages = new FitechMessages();
		
		int year = Integer.parseInt("-1");
		int month = Integer.parseInt("-1");
		try
		{
			/** ȡ�õ�ǰ�û���Ȩ����Ϣ */
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

			String childRepId = request.getParameter("childRepId");
			String versionId = request.getParameter("versionId");
			Integer dataRangeId = new Integer(request.getParameter("dataRangeId"));
			year = Integer.parseInt(request.getParameter("year"));
			month = Integer.parseInt(request.getParameter("mon"));
			Integer currId = new Integer(request.getParameter("currId"));
			String orgId=request.getParameter("orgId");
			AfOrg aforg=AFOrgDelegate.getOrgInfo(orgId);
			List resList = null;
			/**
			 * �������
			 * **/
			if (aforg.getOrgType().equals("-99")){
				resList=VorgCollectDelegate.getRepOrg(childRepId,versionId,dataRangeId,year,month,orgId);
				request.setAttribute("year", String.valueOf(year));
				request.setAttribute("mon", String.valueOf(month));
				if (resList!=null && resList.size()>0)
					request.setAttribute(Config.RECORDS,resList);
			}else{
				/**
				 * ��ʵ����
				 * **/
					List childOrg=AFOrgDelegate.getChildList(orgId);
					//AFOrgDelegate.getChildListByOrgId(reportInForm.getOrgId())
					String childOrgIds="";
					if(childOrg!=null && childOrg.size()>0){
						
						for(int k=0;k<childOrg.size();k++){
							AfOrg org=(AfOrg)childOrg.get(k);
							if(childOrgIds!=null && !childOrgIds.equals("")){
								childOrgIds=childOrgIds+",'"+org.getOrgId()+"'";
							}else{
								childOrgIds="'"+org.getOrgId()+"'";
							}
						}
						
					}
					
					//String childOrgIds = operator != null ? operator.getChildOrgIds() : "";
		
					if (childRepId == null || childRepId.equals("") || versionId == null || versionId.equals("") || childOrgIds == null || childOrgIds.equals(""))
					{
		
						messages.add("�鿴��ϸ��Ϣʧ�ܣ�");
		
						if (messages.getMessages() != null && messages.getMessages().size() > 0)
							request.setAttribute(Config.MESSAGES, messages);
		
						return new ActionForward("/viewCollectData.do");
					}
					// ���Ҹñ���Ĺ�������
					
					CollectReal collectReal = StrutsCollectDelegate.getRealReportInfo(childRepId, versionId, dataRangeId, currId);			
					if(collectReal==null ){
						messages.add("�鿴��ϸ��Ϣʧ�ܣ�");
						if (messages.getMessages() != null && messages.getMessages().size() > 0)
							request.setAttribute(Config.MESSAGES, messages);
		
						return new ActionForward("/viewCollectData.do");
						
					}
		
					String childRepArr[] = null;
					childRepArr =collectReal.getRealRepId().split(",");
					String childOrgIdArr[]=childOrgIds.split(",");
					
					
					
					DB2ExcelHandler handler = new DB2ExcelHandler();
					//�õ�Ӧ�������б�
					if(childRepArr!=null && childRepArr.length>0){
						resList = new ArrayList();
						for(int j =0 ;j<childRepArr.length;j++){
							// �õ�һ�ű���ı��ͻ����б�
							List mustOrgs = handler.getMustOrgList(childRepArr[j],collectReal.getRealVerId(), childOrgIds);
							if(mustOrgs!=null && mustOrgs.size()>0){
								for(int i = 0 ;i<mustOrgs.size();i++){			
									Aditing aditing = new Aditing();
									String orgid = ((OrgNet)mustOrgs.get(i)).getOrgId();
									aditing.setOrgId(orgid);
									aditing.setOrgName(((OrgNet)mustOrgs.get(i)).getOrgName());
									aditing.setChildRepId(childRepArr[j]);
									aditing.setVersionId(collectReal.getRealVerId());
									
									// ���Ҹû���,�ñ���ı���״̬   ����Ч����
									Integer state = StrutsCollectDelegate.getReportState(childRepArr[j], collectReal.getRealVerId(), dataRangeId, year, month, currId, orgid);
									if(state!= null){
										aditing.setIsPass(state);
									}
									Integer repId = StrutsCollectDelegate.getReportId(childRepArr[j], collectReal.getRealVerId(), dataRangeId, year, month, currId, orgid);
									if(state!= null){
										aditing.setRepInId(repId);
									}
									resList.add(aditing);
								}						
							}
						}	
					}
		
					request.setAttribute("year", String.valueOf(year));
					request.setAttribute("mon", String.valueOf(month));
					if(resList!=null && resList.size()>0)
				 		 request.setAttribute(Config.RECORDS,resList);
				 	 else
				 		 request.setAttribute(Config.RECORDS,null);
			}		
			return mapping.findForward("view");

		}
		catch (Exception ex)
		{
			log.printStackTrace(ex);
			messages.add("�鿴��ϸ��Ϣʧ�ܣ�");

			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);

			return new ActionForward("/viewCollectData.do?year=" + year + "&term=" + month);
		}
	}
}