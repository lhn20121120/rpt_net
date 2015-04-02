package com.fitech.gznx.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.fitosa.adapter.ImpReportData;
import com.fitech.fitosa.bean.OrgInfoBean;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.XmlTreeUtil;
import com.fitech.net.adapter.StrutsMRegionDelegate;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.adapter.StrutsOrgTypeDelegate;
import com.fitech.net.form.MRegionForm;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.form.OrgTypeForm;

public class OrgInfoAction extends Action {
	
	private static FitechException log = new FitechException(
			OrgInfoAction.class);

	FitechMessages messages = new FitechMessages();

	OrgNetForm orgNetForm = null;

	// ��־��Ϣ
	private String msg = "";

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// ���ݴ���Ĳ�ͬ�Ĳ�����ִ�в�ͬ�Ķ���
		String parameter = mapping.getParameter();
		request.setAttribute("execType", parameter);
		String type = request.getParameter("type");
		
		request.setAttribute("type", type);
		request.setAttribute("orgId", request.getParameter("orgId"));
		// System.out.println(parameter);
		//�鿴���л���
		if (parameter.equals("view")) {
			return view(mapping, form, request, response);
			//���ӻ���
		} else if (parameter.equals("beforeAdd")) {
			return beforeAdd(mapping, form, request, response);
			//���ӻ���
		} else if (parameter.equals("add")) {
			return add(mapping, form, request, response);
		} else if (parameter.equals("beforeEdit")) {
			return beforeEdit(mapping, form, request, response);
		} else if (parameter.equals("edit")) {
			return edit(mapping, form, request, response);
		} else if (parameter.equals("delete")) {
			return delete(mapping, form, request, response);
		} else if (parameter.equals("setFirstNode")) {
			// ���û���Ϊһ���ڵ�
			return setFirstNode(mapping, form, request, response);
		} else {
			return view(mapping, form, request, response);
		}
	}

	private ActionForward setFirstNode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		OrgInfoForm orgInfoForm = (OrgInfoForm) form;
		orgNetForm = new OrgNetForm();
		
		boolean result = true;
		try {
			/** 1����û���Ϊ��ʵ�������������ó�Ϊһ���ڵ�? */
			AfOrg orgInfo = AFOrgDelegate.getOrgInfo(orgInfoForm.getOrgId());

			if (orgInfo.getIsCollect() != null
					&& orgInfo.getIsCollect().equals(
							new Integer(Config.NOT_IS_COLLECT))) {
				addMyMessage(request, "orgInfo.could.not.setFirstNode");
				return mapping.findForward("failed");
			}
			orgInfoForm.setIsCollect(new Integer(Config.IS_COLLECT));
			/** 2���»�����Ϣ */
			if (AFOrgDelegate.setFirstNode(orgInfoForm)) {
				// ͬ������1104����
				this.copyProperties(orgNetForm, orgInfoForm);
				//StrutsOrgNetDelegate.remove(orgNetForm);
				StrutsOrgNetDelegate.update(orgNetForm);

				addMyMessage(request, "update.success", "orgInfo.info");
			} else {
				throw new Exception("update.failed");
			}
		} catch (Exception e) {
			addMyMessage(request, "update.failed", "orgInfo.info");
			log.printStackTrace(e);
			result = false;
		}

		// ҳ����ת
		if (result) {
			return mapping.findForward("success");
		} else {
			return mapping.findForward("failed");
		}
	}

	private ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OrgInfoForm orgInfoForm = (OrgInfoForm) form;
		boolean result = true;
		try {
			// �������ɻ�����
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);
			String orgId = orgInfoForm.getOrgId();

			if (orgId == null || orgId.equals("")) {
				throw new Exception("����IDΪ�գ�");
			}

			AfOrg oi = AFOrgDelegate.getOrgInfo(orgId);

			/** 1����û���Ϊ���ڵ�"����"�����ֹɾ�� */
			if (oi.getPreOrgId().equals(Config.TOPBANK)) {
				addMyMessage(request, "orgInfo.forbid.delete");
				return mapping.getInputForward();
			}
			/** 2 �ж�ɾ���Ļ����Ƿ����ӻ���������У��������ɾ���ӻ��� */
			List lstChildList = AFOrgDelegate.getChildList(orgId);
			if (lstChildList != null && lstChildList.size() > 0) {
				addMyMessage(request, "orgInfo.has.childAfOrg");
				return mapping.getInputForward();
			}
			/** 3 �ж�ɾ���Ļ����Ƿ���������Ա������У��������ɾ����Ա */
			if (AFOrgDelegate.hasUsers(orgId)) {
				addMyMessage(request, "orgInfo.has.userInfo");
				return mapping.getInputForward();
			}

			// /** 4 �ж�ɾ����[��ʵ]�����Ƿ��ж�Ӧ��ָ�꼯���ݣ�����У����ֹɾ����ָ�꼯������Ҫ����̨ɾ�� */
			// if (!oi.getIsCollect().toString().equals(Config.IS_COLLECT)) {
			// if (AFOrgDelegate.hasMeasureAlert(orgId)) {
			// addMyMessage(request, "AfOrg.has.hasMeasureAlert");
			// return mapping.getInputForward();
			// }
			// }

			/** 5 ɾ��������Ϣ */
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
				operator = (Operator) session
						.getAttribute(Config.OPERATOR_SESSION_NAME);
			else
				operator = new Operator();

			// String userId = operator.getUserName();
			// String ipAddress = operator.getIpAdd();

			if (AFOrgDelegate.delete(orgInfoForm)) {
				//if (oi.getIsCollect() == null
				//		|| !oi.getIsCollect().equals(Long.valueOf(Config.IS_COLLECT))) {
					// ͬ������1104����
					this.copyProperties(orgNetForm, orgInfoForm);
					StrutsOrgNetDelegate.remove(orgNetForm);
				//}
				addMyMessage(request, "delete.success", "orgInfo.info");
			}
			
			
			if(com.cbrc.smis.common.Config.ISADDFITOSA){
				OrgInfoBean orgInfoBean = new OrgInfoBean();
				orgInfoBean.setOrgId(orgInfoForm.getOrgId());

				ImpReportData ird = new ImpReportData();
				ird.setWebroot(com.cbrc.smis.common.Config.WEBROOTPATH);
				ird.deleteOrg(orgInfoBean);
			}
			

			/**
			 * ͬ������ϵͳ����
			 */
			
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			addMyMessage(request, "delete.failed", "orgInfo.info");
		}
		// ҳ����ת
		if (result) {
			return mapping.findForward("success");
		} else {
			return mapping.findForward("failed");
		}
	}

	private ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OrgInfoForm orgInfoForm = (OrgInfoForm) form;

		String newOrgName = request.getParameter("newOrgName");
		String newOrgId = orgInfoForm.getNewOrgId();
		AfOrg parentAfOrg = null;

		// 0:�޸Ļ���ʧ�ܣ�1���޸Ļ����ɹ���2����Ҫ�޸Ļ��ܹ�ϵ
		int result = 1;
		// �Ƿ�Ϊ���������0Ϊ��ʵ������1Ϊ�������
		int isCollect = 1;
		try {
			// �õ����ڵ����ϸ��Ϣ
			parentAfOrg = AFOrgDelegate.getOrgInfo(orgInfoForm.getParentOrgId());
			// �ı��������
			if(!StringUtil.isEmpty(newOrgId) && !newOrgId.equals(orgInfoForm.getOrgId().trim())){
				if (newOrgName != null
						&& !newOrgName.equals(orgInfoForm.getOrgName())) {
					orgInfoForm.setOrgName(newOrgName);
				}
				if (AFOrgDelegate.updateAllOrgInfo(orgInfoForm)) {
					addMyMessage(request, "update.success", "orgInfo.info");
				} else{
					throw new Exception("update.failed");
				}
			} else{
				/** 1 �ж��Ƿ�����ͬ�Ļ�������(��ʵ���������ƽ�ֹ�޸�) */
				if (newOrgName != null
						&& !newOrgName.equals(orgInfoForm.getOrgName())) {

					orgInfoForm.setOrgName(newOrgName);

					if (AFOrgDelegate.isExistSameOrgName(orgInfoForm)) {

						addMyMessage(request, "orgInfo.same.name");
						AfOrg AfOrg = AFOrgDelegate.getOrgInfo(orgInfoForm.getOrgId());

						// if
						// (!AfOrg.getIsCollect().toString().equals(Config.IS_COLLECT))
						// {
						//						
						// request.setAttribute("parentOrgInfo", parentAfOrg);
						// isCollect = 0;
						// request.setAttribute("isCollect", new Integer(0));
						//						
						// } else {
						// isCollect = 1;
						// request.setAttribute("isCollect", new Integer(1));
						// }

						// ������Ϣ
						this.copyProperties(orgInfoForm, AfOrg);
						orgInfoForm.setOrgName(newOrgName);

						// ��������ݵı������request����
						request.setAttribute("orgInfoForm", orgInfoForm);
						AFOrgDelegate.makeOrgTree();
						request.setAttribute("FileName",
								Config.BASE_ORG_TREEXML_NAME);

						// // ����ʵ�������в���
						// if (isCollect == 0) {
						// // ���ػ������
						// request.setAttribute("lstOrgLevel",
						// StrutsCodeLibDelegate
						// .getCodeLibList("10", true));
						// // ���ػ�������
						// request.setAttribute("lstOrgType",
						// StrutsCodeLibDelegate
						// .getCodeLibList("24", true));
						// }
						// ��������ҳ��
						return mapping.getInputForward();
					}
				}

				/** 2���»�����Ϣ */
				HttpSession session = request.getSession();
				Operator operator = null;
				if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
					operator = (Operator) session
							.getAttribute(Config.OPERATOR_SESSION_NAME);
				else
					operator = new Operator();

				if (AFOrgDelegate.updateOrgInfo(orgInfoForm)) {

					//if (orgInfoForm.getIsCollect() == null
					//		|| !orgInfoForm.getIsCollect().equals(Config.IS_COLLECT)) {
						
						orgNetForm = new OrgNetForm();
						// ͬ������1104����
						this.copyProperties(orgNetForm, orgInfoForm);
						orgNetForm.setSetOrgId(operator.getOrgId());
						StrutsOrgNetDelegate.update(orgNetForm);
					//}
					addMyMessage(request, "update.success", "orgInfo.info");

				} else {

					throw new Exception("update.failed");
				}
				/**
				 * ͬ������ϵͳ��ʼ
				 */
				if(com.cbrc.smis.common.Config.ISADDFITOSA){
					OrgInfoBean orgInfoBean = new OrgInfoBean();
					orgInfoBean.setOrgId(orgInfoForm.getOrgId());
					orgInfoBean.setOrgName(orgInfoForm.getOrgName());
					orgInfoBean.setParentOrgId(orgInfoForm.getParentOrgId());

					ImpReportData ird = new ImpReportData();
					ird.setWebroot(com.cbrc.smis.common.Config.WEBROOTPATH);
					ird.updateOrg(orgInfoBean);

				}
				
				/**
				 * ͬ������ϵͳ����
				 */
				
			}

			

			/** 3�ж��޸ĵĻ����Ƿ�Ϊ�������������ǣ�����Ҫ�޸Ļ��ܹ�ϵ */
			// if (!orgInfoForm.getParentOrgId().equals(Config.TOPBANK)) {
			// // �����������(���ڵ�)�����ж��Ƿ�Ϊ�������
			// if (parentAfOrg == null || parentAfOrg.getIsCollect() != null
			// && parentAfOrg.getIsCollect().intValue() == 1) {
			// result = 2;
			// request.setAttribute("orgInfoForm", orgInfoForm);
			// }
			// }
		} catch (Exception e) {
			log.printStackTrace(e);
			result = 0;
			addMyMessage(request, "update.failed", "orgInfo.info");
			// �������ɻ�����
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);
			AfOrg AfOrg = AFOrgDelegate.getOrgInfo(orgInfoForm.getOrgId());
			// ������Ϣ
			try {
				this.copyProperties(orgInfoForm, AfOrg);
			} catch (Exception e1) {
				log.printStackTrace(e1);
			}

			// ��������ݵı������request����
			request.setAttribute("orgInfoForm", orgInfoForm);
			request.setAttribute("parentAfOrg", parentAfOrg);

			// // ���ػ������
			// request.setAttribute("lstOrgLevel", StrutsCodeLibDelegate
			// .getCodeLibList("10", true));
			// // ���ػ�������
			// request.setAttribute("lstOrgType", StrutsCodeLibDelegate
			// .getCodeLibList("24", true));
		}

		// ҳ����ת
		if (result == 1) {
			return mapping.findForward("success");
		} else if (result == 0) {
			return mapping.getInputForward();
		} else {
			request.setAttribute("type", "update");
			return mapping.findForward("updateCollect");
		}
	}
	
	/***
	 * �鿴����
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward beforeEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OrgInfoForm orgInfoForm = (OrgInfoForm) form;
		String orgId = request.getParameter("orgId");

		String type = request.getParameter("type");
		
		/** ���ػ�����ϸ��Ϣ */
		boolean result = true;
		// �Ƿ�Ϊ���������0Ϊ��ʵ������1Ϊ�������
		int isCollect = 1;
		try {
			// �������ɻ�����
			/**��ʹ��hibernate ���Ը� 2011-12-22**/
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);
			// �õ�һ��������ϸ��Ϣ
			AfOrg afOrg = AFOrgDelegate.getOrgInfo(orgId);

			// ������Ϣ
			this.copyProperties(orgInfoForm, afOrg);

			// ��������ݵı������request����
			request.setAttribute("orgInfoForm", orgInfoForm);

			AfOrg parentAfOrg = null;
			// ������ǻ��ܻ����������ϼ�����
			if (afOrg.getIsCollect() == null
					|| !afOrg.getIsCollect().equals(Long.valueOf(Config.IS_COLLECT))) {

				parentAfOrg = AFOrgDelegate.getOrgInfo(afOrg.getPreOrgId());
				request.setAttribute("parentOrgInfo", parentAfOrg);
				isCollect = 0;
				request.setAttribute("isCollect", new Integer(0));

			} else {
				isCollect = 1;
				request.setAttribute("isCollect", new Integer(1));
			}

			// ����ʵ�������в���
			//if (isCollect == 0) {
				// // ���ػ������
				// request.setAttribute("lstOrgLevel", StrutsCodeLibDelegate
				// .getCodeLibList("10", true));
				
				// ���ػ�������
				if(afOrg.getOrgType()!=null)
					/**��ʹ��hibernate ���Ը� 2011-12-22**/
					request.setAttribute("orgType", 
						StrutsOrgTypeDelegate.selectOne(Integer.valueOf(afOrg.getOrgType())));
			
				if(type.equals("update")){
					//����
					/**��ʹ��hibernate ���Ը� 2011-12-22**/
					request.setAttribute("lstMRegion", StrutsMRegionDelegate.findAll());
				}else{
					if(afOrg.getRegionId() != null)
						/**��ʹ��hibernate ���Ը� 2011-12-22**/
					request.setAttribute("mRegion", 
							StrutsMRegionDelegate.selectOne(afOrg.getRegionId().intValue()));					
				}
			//}	
			
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			addMyMessage(request, "select.fail", "orgInfo.info");
		}

		if (result == false) {
			return mapping.getInputForward();
		}

		
		// System.out.println("test.................");
		/** detail-�û��鿴JSP��update-�û��޸�JSP */
		if (type.equals("update")) {
			return mapping.findForward("update");
		} else if (type.equals("detail")) {
			return mapping.findForward("detail");
		} else {
			return mapping.getInputForward();
		}
	}

	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OrgInfoForm orgInfoForm = (OrgInfoForm) form;
		orgNetForm = new OrgNetForm();
		
		// �õ����ڵ����ϸ��Ϣ
		AfOrg parentAfOrg = AFOrgDelegate.getOrgInfo(orgInfoForm.getParentOrgId());

		// 0:���ӻ���ʧ�ܣ�1�����ӻ����ɹ���2����Ҫ���ӻ��ܹ�ϵ
		int result = 1;

		try {
			if (parentAfOrg!=null &&
					parentAfOrg.getIsCollect()!=null &&
					parentAfOrg.getIsCollect().equals(Config.IS_COLLECT)){
				// ���������������ӻ���
				addMyMessage(request, "orgInfo.collectchild.info");
				return mapping.findForward("failed");
			}
			
			/** 1 �ж��Ƿ�Ϊ��*/
			if (orgInfoForm.getOrgId()==null || orgInfoForm.getOrgName()==null
					|| orgInfoForm.getOrgId().trim().equals("") 
					|| orgInfoForm.getOrgName().trim().equals("")) {
				// ���ش�����Ϣ
				addMyMessage(request, "orgInfo.null.info");
				return mapping.findForward("failed");
			}
			
			/** 1 �ж��Ƿ�����ͬ�Ļ������� */
			if (AFOrgDelegate.isExistSameOrgId(orgInfoForm)) {
				// ���ش�����Ϣ
				addMyMessage(request, "orgInfo.same.id");
				return mapping.findForward("failed");
			}

			/** 2 �ж��Ƿ�����ͬ�Ļ������� */
			if (AFOrgDelegate.isExistSameOrgName(orgInfoForm)) {
				// ���ش�����Ϣ
				addMyMessage(request, "orgInfo.same.name");
				return mapping.findForward("failed");
			}

			/** 3���ӻ�����Ϣ */
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
				operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_NAME);
			else
				operator = new Operator();
			
			if(operator.getOrgId()!=null)
				orgInfoForm.setSetOrgId(operator.getOrgId());
			if(orgInfoForm.getOrgOuterId()==null || orgInfoForm.getOrgOuterId().trim().equals(""))
				orgInfoForm.setOrgOuterId(orgInfoForm.getOrgId());
			if (AFOrgDelegate.add(orgInfoForm, operator.getUserName())) {

				//if (parentAfOrg != null) {
					// ͬ������1104����
					this.copyProperties(orgNetForm, orgInfoForm);
					
					if(orgNetForm.getPre_org_id() == null 
							|| orgNetForm.getPre_org_id().equals("")){
						orgNetForm.setPre_org_id(Config.VIRTUAL_TOPBANK);
						orgNetForm.setOrg_type_id(new Integer(Config.VIRTUAL_TOPBANK));
					}
					
					//orgNetForm.setSetOrgId(operator.getOrgId());
					StrutsOrgNetDelegate.create(orgNetForm);
				//}
				addMyMessage(request, "add.success", "orgInfo.info");

			} else {
				addMyMessage(request, "add.failed", "orgInfo.info");
			}
			
			/**
			 * ͬ������ϵͳ��ʼ
			 */
			if(com.cbrc.smis.common.Config.ISADDFITOSA){
				if(!StringUtil.isEmpty(orgInfoForm.getParentOrgId()) && Integer.valueOf(orgInfoForm.getParentOrgId())>=0){
					OrgInfoBean orgInfoBean = new OrgInfoBean();
					orgInfoBean.setOrgId(orgInfoForm.getOrgId());
					orgInfoBean.setOrgName(orgInfoForm.getOrgName());
					orgInfoBean.setParentOrgId(orgInfoForm.getParentOrgId());

					ImpReportData ird = new ImpReportData();
					ird.setWebroot(com.cbrc.smis.common.Config.WEBROOTPATH);
					ird.addOrg(orgInfoBean);
				}
			}
			
			

			/**
			 * ͬ������ϵͳ����
			 */

			/** 4�ж����ӵĻ����Ƿ�Ϊ��Ҫ���ӻ��ܹ�ϵ������ǣ�����Ҫ���ӻ��ܹ�ϵ */
			if (parentAfOrg == null) {
				result = 2;
				request.setAttribute("orgInfoForm", orgInfoForm);
			}
		} catch (Exception e) {
			/** �����򷵻�ԭҳ�� */
			log.printStackTrace(e);
			result = 0;
			addMyMessage(request, "add.failed", "orgInfo.info");
		}

		// ҳ����ת
		if (result == 1) {
			return mapping.findForward("success");
		} else if (result == 0) {
			return mapping.findForward("failed");
		} else {
			return mapping.findForward("addCollect");
		}
	}
	
	/***
	 * ���ӻ���
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward beforeAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		OrgInfoForm orgInfoForm = (OrgInfoForm) form;

		boolean result = true;
		// �Ƿ�Ϊ���������0Ϊ��ʵ������1Ϊ�������
		int isCollect = 1;
		try {
			// �������ɻ�����
			/**��ʹ��hibernate ���Ը� 2011-12-22**/
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);
			// �õ����ڵ����ϸ��Ϣ
			String orgId = orgInfoForm.getParentOrgId();
			

			
			if (orgId != null && !orgId.equals("")) {
				/**��ʹ��hibernate ���Ը� 2011-12-21**/
				AfOrg parentAfOrg = AFOrgDelegate.getOrgInfo(orgId);		
				request.setAttribute("parentOrgInfo", parentAfOrg);
				
				if (parentAfOrg.getIsCollect() == null
						|| !parentAfOrg.getIsCollect().toString().equals(
								Config.IS_COLLECT)) {

					// ��ʵ����
					isCollect = 0;
					request.setAttribute("isCollect", new Integer(0));
				} else {
					// ���������������ӻ���
					addMyMessage(request, "orgInfo.collectchild.info");
					return mapping.getInputForward();
					//request.setAttribute("isCollect", new Integer(1));
				}
			} else {
				request.setAttribute("isCollect", new Integer(1));
			}
			// ����ʵ�������в���
			if (isCollect == 0) {
				// // ���ػ������
				// request.setAttribute("lstOrgLevel", StrutsCodeLibDelegate
				// .getCodeLibList("10", true));
				// ���ػ�������
				/**��ʹ��hibernate ���Ը� 2011-12-22**/
				request.setAttribute("lstOrgType", StrutsOrgTypeDelegate
						.findAll());
			}
				/**��ʹ��hibernate ���Ը� 2011-12-22**/
				request.setAttribute("lstMRegion", StrutsMRegionDelegate
						.findAll());
			
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			addMyMessage(request, "select.fail", "orgInfo.info");
		}

		// ҳ����ת
		if (result) {
			return mapping.findForward("success");
		} else {
			return mapping.getInputForward();
		}
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * �鿴���л���
	 */
	private ActionForward view(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// OrgInfoForm orgInfoForm = null;
		//		
		// if(form!=null)
		// orgInfoForm = (OrgInfoForm) form;
		// else
		// orgInfoForm = new OrgInfoForm();
		//		
		// try {
		// orgInfoForm.setOrgTreeContent(XmlTreeUtil.createOrgXml(request,"TREE1_NODES",null,false,true,true))
		// ;
		//
		// if (orgInfoForm != null)
		// request.setAttribute("QueryForm", orgInfoForm);
		//
		// } catch (Exception e) {
		//			
		// log.printStackTrace(e);
		//			
		// }
		//
		// return mapping.findForward("success");
		try {
			/**��ʹ��hibernate ���Ը� 2011-12-22**/
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);

			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
				operator = (Operator) session
						.getAttribute(Config.OPERATOR_SESSION_NAME);
			else
				operator = new Operator();
			// ˢ�»�����,ʹҳ����ʾ����һ��
			/**��ʹ��hibernate  ���Ը� 2011-12-21**/
			operator.reFreshOrgTree();
		} catch (Exception e) {
			log.printStackTrace(e);
			addMyMessage(request, "select.fail", "orgInfo.info");
		}

		return mapping.findForward("success");
	}

	/**
	 * ����ִ���еĴ�����Ϣ
	 * 
	 * @author Nick
	 * @param request
	 *            HttpServletRequest
	 * @param msg
	 *            String ������Ϣ
	 * @return void
	 */
	private void addMyMessage(HttpServletRequest request, String msg) {
		// ȡ�ò�����Ϣ
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		// ���������Ϣ
		messages.add(FitechResource.getMessage(locale, resources, msg));
		// ��������Ϣ�б����Request����
		if (messages.getMessages() != null && messages.getMessages().size() > 0) {
			request.setAttribute(Config.MESSAGES, messages);
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

	/**
	 * ��org��Ϣ����form
	 */
	private void copyProperties(OrgInfoForm orgInfoForm, AfOrg afOrg) {

		if (orgInfoForm == null)
			orgInfoForm = new OrgInfoForm();

		orgInfoForm.setOrgId(afOrg.getOrgId());
		orgInfoForm.setOrgName(afOrg.getOrgName());

		if (afOrg.getIsCollect() != null)
			orgInfoForm.setIsCollect(afOrg.getIsCollect().intValue());
		if (afOrg.getOrgAttr() != null)
			orgInfoForm.setOrgAttr(afOrg.getOrgAttr());
		if (afOrg.getOrgLevel() != null)
			orgInfoForm.setOrgLevel(afOrg.getOrgLevel().intValue());
		if (afOrg.getOrgOuterId() != null)
			orgInfoForm.setOrgOuterId(afOrg.getOrgOuterId());
		if (afOrg.getRegionId() != null)
			orgInfoForm.setOrgRegion(afOrg.getRegionId().toString());
		if (afOrg.getOrgType() != null)
			orgInfoForm.setOrgType(afOrg.getOrgType());
		if (afOrg.getPreOrgId() != null)
			orgInfoForm.setParentOrgId(afOrg.getPreOrgId());
		if (afOrg.getBeginDate() != null)
			orgInfoForm.setBeginDate(afOrg.getBeginDate());

	}

	/**
	 * ���µ�org��Ϣ����1104org
	 */
	private void copyProperties(OrgNetForm orgNetForm, OrgInfoForm orgInfoForm) {

		if (orgNetForm == null)
			orgNetForm = new OrgNetForm();

		orgNetForm.setOrg_id(orgInfoForm.getOrgId());

		if (orgInfoForm.getOrgName() != null)
			orgNetForm.setOrg_name(orgInfoForm.getOrgName());
		if (orgInfoForm.getOrgType() != null)
			orgNetForm.setOrg_type_id(new Integer(orgInfoForm.getOrgType()));
		if (orgInfoForm.getOrgRegion() != null)
			orgNetForm.setRegion_id(new Integer(orgInfoForm.getOrgRegion()));
		if (orgInfoForm.getSetOrgId() != null)
			orgNetForm.setSetOrgId(orgInfoForm.getSetOrgId());
		if (orgInfoForm.getParentOrgId() != null)
			orgNetForm.setPre_org_id(orgInfoForm.getParentOrgId());
	}
}
