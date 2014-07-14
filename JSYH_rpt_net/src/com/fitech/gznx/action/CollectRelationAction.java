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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.form.CollectRelationForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.service.AFCollectRelationDelegate;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFProductRelationDelegate;
import com.fitech.gznx.service.AFValiRelationDelegate;
import com.fitech.gznx.treexml.CreateOrgTreeByUser;


public class CollectRelationAction extends Action {
	private static FitechException log = new FitechException(
			CollectRelationAction.class);

	// 日志消息
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
		// 根据传入的不同的参数，执行不同的动作
		String parameter = mapping.getParameter();
		String execType = request.getParameter("execType");
		String type = request.getParameter("type");
		request.setAttribute("execType", execType);
		request.setAttribute("orgId", request.getParameter("orgId"));
		request.setAttribute("type", type);
		if (parameter.equals("beforeAdd")) {
			return beforeAdd(mapping, form, request, response);
		} else if (parameter.equals("add")) {
			return add(mapping, form, request, response);
			/**查看汇总机构关系**/
		} else if (parameter.equals("beforeEdit")) {
			return beforeEdit(mapping, form, request, response);
		} else {
			return edit(mapping, form, request, response);
		}
	}

	private ActionForward edit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		CollectRelationForm crForm = (CollectRelationForm) form;

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
			
				/** 1 更新汇总关系 */
				if (AFCollectRelationDelegate.update(crForm)) {
	//				msg = "修改机构代码为[" + crForm.getCollectId() + "]名称为["
	//						+ crForm.getOrgName() + "]的机构的汇总关系成功";
	//				/* 写日志 */
	//				FitechLog
	//						.writeLog(Config.LOG_OPERATION, userId, msg, ipAddress);
					addMyMessage(request, "update.success", "collectRelation.info");
				} else {
	//				msg = "修改机构代码为[" + crForm.getCollectId() + "]名称为["
	//						+ crForm.getOrgName() + "]的机构的汇总关系失败";
	//				/* 写日志 */
	//				FitechLog
	//						.writeLog(Config.LOG_OPERATION, userId, msg, ipAddress);
					throw new Exception("collectRelation.update.failed");
				}
			
			}else if(crForm.getStyle()!=null && crForm.getStyle().equals("product")){
				/** 1 更新生成关系 */
				if (AFProductRelationDelegate.update(crForm)) {
					addMyMessage(request, "update.success", "productRelation.info");
				} else {
					throw new Exception("productRelation.update.failed");
				}
			}else if(crForm.getStyle()!=null && crForm.getStyle().equals("vali")){
				/** 1 更新校验关系 */
				if (true/*AFValiRelationDelegate.update(crForm)*/) {
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
				addMyMessage(request, "update.failed", "collectRelation.info");
				// 得到修改汇总机构的机构信息
				List lstOrgId = AFCollectRelationDelegate.getCRList(orgInfo
						.getOrgId());
				makeCheckedOrgTree(request, lstOrgId , false);
			}else{
				addMyMessage(request, "update.failed", "productRelation.info");
				//得到修改生成机构的机构信息
				List lstOrgId = AFProductRelationDelegate.getCRList(orgInfo
						.getOrgId());
				makeCheckedOrgRelTree(request,lstOrgId,false);
			}
		}
		
		// 页面跳转
		if (result) {
			return mapping.findForward("success");
		} else {
			return mapping.getInputForward();
		}
	}

	private ActionForward beforeEdit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		CollectRelationForm crForm = (CollectRelationForm) form;

		boolean result = true;
		try {
			String orgId = request.getParameter("orgId");
			//汇总机构处理类型
			String style = request.getParameter("style");
			
			/** 1得到机构的详细信息 */
			/**已使用hibernate  卞以刚 2011-12-22**/
			AfOrg orgInfo = AFOrgDelegate.getOrgInfo(orgId);
			request.setAttribute("orgInfo", orgInfo);
			
			if(!style.equals("product")&&!style.equals("vali")){
				// 得到修改汇总机构的机构信息
				/**已使用hibernate 卞以刚 2011-12-22**/
				List lstOrgId = AFCollectRelationDelegate.getCRList(orgId);
				//List lstOrgId = new ArrayList();
				/** 2 加载机构树 */
				makeCheckedOrgTree(request, lstOrgId , false);
			}else if(!style.equals("collect")&&!style.equals("vali")){
				// 得到修改生成机构的机构信息
				/**已使用hibernate 卞以刚 2011-12-22**/
				List lstOrgId = AFProductRelationDelegate.getCRList(orgId);
				/** 2 加载机构树,含虚拟机构 */
				makeCheckedOrgRelTree(request, lstOrgId , true);
			}else{
				//总分校验关系
				/** 孙大为 2012-09-14**/
				List lstOrgId = AFValiRelationDelegate.getCRList(orgId);
				/** 2 加载机构树,含虚拟机构 */
				makeCheckedOrgRelTree(request, lstOrgId , true);
			}
			
			
			request.setAttribute("style", style);
			
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			addMyMessage(request, "select.fail", "collectRelation.info");
		}

		if (result == false) {
			return mapping.findForward("failed");
		}

		String type = request.getParameter("type");
		if (type == null || type.equals("")) {
			type = (String) request.getAttribute("type");
		}
		
		/** detail-用户查看JSP；update-用户修改JSP */
		if (type.equals("update")) {
			return mapping.findForward("update");
		} else if (type.equals("detail")) {
			return mapping.findForward("detail");
		} else {
			return mapping.findForward("failed");
		}
	}

	private ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		CollectRelationForm crForm = (CollectRelationForm) form;

		boolean result = true;
		try {
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
				operator = (Operator) session
						.getAttribute(Config.OPERATOR_SESSION_NAME);
			else
				operator = new Operator();

			String userId = operator.getUserName();
			String ipAddress = operator.getIpAdd();

			if (AFCollectRelationDelegate.add(crForm)) {
//				msg = "增加机构代码为[" + crForm.getCollectId() + "]名称为["
//						+ crForm.getOrgName() + "]的机构的汇总关系成功";
//				/* 写日志 */
//				FitechLog
//						.writeLog(Config.LOG_OPERATION, userId, msg, ipAddress);
//				addMyMessage(request, "add.success", "collectRelation.info");
			} else {
//				msg = "增加机构代码为[" + crForm.getCollectId() + "]名称为["
//						+ crForm.getOrgName() + "]的机构的汇总关系失败";
//				/* 写日志 */
//				FitechLog
//						.writeLog(Config.LOG_OPERATION, userId, msg, ipAddress);
				throw new Exception("add.failed");
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			addMyMessage(request, "add.failed", "collectRelation.info");

			AfOrg orgInfo = new AfOrg();
			orgInfo.setOrgId(request.getParameter("collectId"));
			orgInfo.setOrgName(request.getParameter("orgName"));
			request.setAttribute("orgInfo", orgInfo);

			// 得到修改汇总机构的机构信息
			makeCheckedOrgTree(request, null ,false);
		}

		// 页面跳转
		if (result) {
			return mapping.findForward("success");
		} else {
			return mapping.getInputForward();
		}
	}

	private ActionForward beforeAdd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		CollectRelationForm crForm = (CollectRelationForm) form;

		boolean result = true;
		try {
			String orgId = request.getParameter("orgId");
			
			/** 1得到机构的详细信息 */
			AfOrg orgInfo = AFOrgDelegate.getOrgInfo(orgId);
			request.setAttribute("orgInfo", orgInfo);
			
			// 得到汇总机构的机构信息
			List lstOrgId = AFCollectRelationDelegate.getCRList(orgId);
			/** 2 加载机构树 */
			makeCheckedOrgTree(request, lstOrgId , false);
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			addMyMessage(request, "select.fail", "collectRelation.info");
		}

		// 页面跳转
		if (result) {
			return mapping.findForward("success");
		} else {
			return mapping.findForward("failed");
		}
	}

	/**
	 * 加载执行中的错误信息
	 * 
	 * @author Nick
	 * @param request
	 *            HttpServletRequest
	 * @param msg
	 *            String 错误消息
	 * @return void
	 */
	private void addMyMessage(HttpServletRequest request, String msg) {
		// 取得参数信息
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		// 加入错误信息
		messages.add(FitechResource.getMessage(locale, resources, msg));
		// 将错误信息列表加入Request对象
		if (messages.getMessages() != null && messages.getMessages().size() > 0) {
			request.setAttribute(Config.MESSAGES, messages);
		}
	}

	/**
	 * 加载执行中的错误信息
	 * 
	 * @author Nick
	 * @param request
	 *            HttpServletRequest
	 * @param msg
	 *            String 错误消息
	 * @param String
	 *            打印的错误消息的对象
	 * @return void
	*/
	private void addMyMessage(HttpServletRequest request, String msg,
			String errorObject) {
		// 取得参数信息
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		// 加入错误信息
		messages.add(FitechResource.getMessage(locale, resources, msg,
				errorObject));
		// 将错误信息列表加入Request对象
		if (messages.getMessages() != null && messages.getMessages().size() > 0) {
			request.setAttribute(Config.MESSAGES, messages);
		}
	}

	/**
	 * 加载汇总关系机构树
	 * 
	 * @author Nick
	 * @param request
	 *            HttpServletRequest
	 * @param lstOrgId
	 *            List 机构Id列表
	 * @return void
	 */
	private void makeCheckedOrgTree(HttpServletRequest request, List lstOrgId ,boolean isContainCollect) {
		HttpSession session = request.getSession();

		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_NAME);
		else
			operator = new Operator();

		String path = CreateOrgTreeByUser.createAllOrg(operator, lstOrgId, isContainCollect);
		request.setAttribute("FileName", path);
	}
	
	/***
	 * 加载生成关系机构树
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
		
		String path = CreateOrgTreeByUser.createAllVorgRel(operator, lstOrgId, isContainCollect);
		request.setAttribute("FileName", path);
	}
}
