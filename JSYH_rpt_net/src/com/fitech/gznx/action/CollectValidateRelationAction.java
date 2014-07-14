package com.fitech.gznx.action;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.form.CollectValidateRelationForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFCollectRelationDelegate;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFProductRelationDelegate;
import com.fitech.gznx.service.AFValiRelationDelegate;
import com.fitech.gznx.service.AfCollectValidateDelegate;
import com.fitech.gznx.treexml.CreateOrgTreeByValidate;

/**
 * �����ܷ�У���ϵ
 * @author Administrator
 *
 */
public class CollectValidateRelationAction extends Action {

	private static FitechException log = new FitechException(
			CollectRelationAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// ���ݴ���Ĳ�ͬ�Ĳ�����ִ�в�ͬ�Ķ���
		String parameter = mapping.getParameter();
//		String parameter = request.getParameter("execType");
		String type = request.getParameter("type");
		request.setAttribute("execType", parameter);
		request.setAttribute("orgId", request.getParameter("orgId"));
//		System.out.println(request.getParameter("orgId"));
		request.setAttribute("type", type);
		if (parameter.equals("beforeAdd")) {
		//	return beforeAdd(mapping, form, request, response);
		} else if (parameter.equals("add")) {
			//return add(mapping, form, request, response);
			/**�鿴���ܻ�����ϵ**/
		} else if (parameter.equals("beforeEdit")) {
			return beforeEdit(mapping, form, request, response);
		} else {
			return edit(mapping, form, request, response);
		}
		
		return null;
	}
	
	
	private ActionForward beforeEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		CollectValidateRelationForm crForm = (CollectValidateRelationForm) form;

		boolean result = true;
		try {
			String orgId = request.getParameter("orgId");
			//���ܻ�����������
			String style = request.getParameter("style");
			
			/** 1�õ���������ϸ��Ϣ */
			/**��ʹ��hibernate  ���Ը� 2011-12-22**/
			AfOrg orgInfo = AFOrgDelegate.getOrgInfo(orgId);
			request.setAttribute("orgInfo", orgInfo);
			
			if(!style.equals("product")&&!style.equals("vali")){
				// �õ��޸Ļ��ܻ����Ļ�����Ϣ
				/**��ʹ��hibernate ���Ը� 2011-12-22**/
				List lstOrgId = AFCollectRelationDelegate.getCRList(orgId);
				//List lstOrgId = new ArrayList();
				/** 2 ���ػ����� */
			//	makeCheckedOrgTree(request, lstOrgId , false);
			}else if(!style.equals("collect")&&!style.equals("vali")){
				// �õ��޸����ɻ����Ļ�����Ϣ
				/**��ʹ��hibernate ���Ը� 2011-12-22**/
//				List lstOrgId = AFProductRelationDelegate.getCRList(orgId);
//				/** 2 ���ػ�����,��������� */
//				makeCheckedOrgRelTree(request, lstOrgId , true);
			}else{
				//�ܷ�У���ϵ
				/** ���Ϊ 2012-09-14**/
				List lstOrgId = AFValiRelationDelegate.getCRList(orgId);
				/** 2 ���ػ�����,��������� */
				makeCheckedOrgRelTree(request, lstOrgId , false);
			}
			
			
			request.setAttribute("style", style);
			
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			addMyMessage(request, "select.fail", "valiRelation.info");
		}

		if (result == false) {
			return mapping.findForward("failed");
		}

		String type = request.getParameter("type");
		if (type == null || type.equals("")) {
			type = (String) request.getAttribute("type");
		}
		
		/** detail-�û��鿴JSP��update-�û��޸�JSP */
		if (type.equals("update")) {
			return mapping.findForward("update");
		} else if (type.equals("detail")) {
			return mapping.findForward("detail");
		} else {
			return mapping.findForward("failed");
		}
	}
	
	/**
	 * ����ִ���еĴ�����Ϣ
	 * 
	 * @author Nick
	 * @param request
	 *            HttpServletRequest
	 * @param msg
	 *            String ������Ϣ
	 * @param String
	 *            ��ӡ�Ĵ�����Ϣ�Ķ���
	 * @return void
	*/
	private void addMyMessage(HttpServletRequest request, String msg,
			String errorObject) {
		// ȡ�ò�����Ϣ
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		// ���������Ϣ
		messages.add(FitechResource.getMessage(locale, resources, msg,
				errorObject));
		// ��������Ϣ�б����Request����
		if (messages.getMessages() != null && messages.getMessages().size() > 0) {
			request.setAttribute(Config.MESSAGES, messages);
		}
	}
	
	private ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		CollectValidateRelationForm crForm = (CollectValidateRelationForm) form;

		boolean result = true;
		try {
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
				operator = (Operator) session
						.getAttribute(Config.OPERATOR_SESSION_NAME);
			else
				operator = new Operator();

//			String userId = operator.getUserName();
//			String ipAddress = operator.getIpAdd();
			
			request.setAttribute("style", crForm.getStyle());
			
			if(crForm.getStyle() != null && crForm.getStyle().equals("collect")){
			
				/** 1 ���»��ܹ�ϵ */
				//if (AFCollectRelationDelegate.update(crForm)) {
	//				msg = "�޸Ļ�������Ϊ[" + crForm.getCollectId() + "]����Ϊ["
	//						+ crForm.getOrgName() + "]�Ļ����Ļ��ܹ�ϵ�ɹ�";
	//				/* д��־ */
	//				FitechLog
	//						.writeLog(Config.LOG_OPERATION, userId, msg, ipAddress);
					addMyMessage(request, "update.success", "valiRelation.info");
			//	} else {
	//				msg = "�޸Ļ�������Ϊ[" + crForm.getCollectId() + "]����Ϊ["
	//						+ crForm.getOrgName() + "]�Ļ����Ļ��ܹ�ϵʧ��";
	//				/* д��־ */
	//				FitechLog
	//						.writeLog(Config.LOG_OPERATION, userId, msg, ipAddress);
				//	throw new Exception("collectRelation.update.failed");
				//}
			
			}else if(crForm.getStyle()!=null && crForm.getStyle().equals("product")){
				/** 1 �������ɹ�ϵ */
//				if (AFProductRelationDelegate.update(crForm)) {
//					addMyMessage(request, "update.success", "productRelation.info");
//				} else {
//					throw new Exception("productRelation.update.failed");
//				}
			}else if(crForm.getStyle()!=null && crForm.getStyle().equals("vali")){
				/** 1 ����У���ϵ */
				if (AFValiRelationDelegate.update(crForm)) {
					addMyMessage(request, "update.success", "valiRelation.info");
				} else {
					throw new Exception("valiRelation.update.failed");
				}
			}
			
			

		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			

			AfOrg orgInfo = new AfOrg();
			orgInfo.setOrgId(request.getParameter("collectId"));
			orgInfo.setOrgName(request.getParameter("orgName"));
			request.setAttribute("orgInfo", orgInfo);
			if(crForm.getStyle() == null || !crForm.getStyle().equals("product")){
				addMyMessage(request, "update.failed", "valiRelation.info");
				// �õ��޸Ļ��ܻ����Ļ�����Ϣ
				List lstOrgId = AFCollectRelationDelegate.getCRList(orgInfo
						.getOrgId());
				//makeCheckedOrgTree(request, lstOrgId , false);
			}else{
				addMyMessage(request, "update.failed", "productRelation.info");
				//�õ��޸����ɻ����Ļ�����Ϣ
//				List lstOrgId = AFProductRelationDelegate.getCRList(orgInfo
//						.getOrgId());
//				makeCheckedOrgRelTree(request,lstOrgId,false);
			}
		}
		
		// ҳ����ת
		if (result) {
			return mapping.findForward("success");
		} else {
			return mapping.getInputForward();
		}
	}
	
	/***
	 * �������ɹ�ϵ������
	 * @param request
	 * @param lstOrgId
	 * @param isContainCollect
	 */
	private void makeCheckedOrgRelTree(HttpServletRequest request, List lstOrgId ,boolean isContainCollect) {
		HttpSession session = request.getSession();
		
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_NAME);
		else
			operator = new Operator();
		//�ο����ܹ�ϵ�趨Ϊfalse�����ɹ�ϵ�趨Ϊtrue,isContainCollect�趨Ϊfalse
		String path = CreateOrgTreeByValidate.createAllVorgRel(operator, lstOrgId, isContainCollect);
		request.setAttribute("FileName", path);
	}
}
