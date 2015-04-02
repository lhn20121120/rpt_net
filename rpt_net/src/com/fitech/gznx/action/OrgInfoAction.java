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
		request.setAttribute("execType", parameter);
		String type = request.getParameter("type");
		
		request.setAttribute("type", type);
		request.setAttribute("orgId", request.getParameter("orgId"));
		// System.out.println(parameter);
		//查看所有机构
		if (parameter.equals("view")) {
			return view(mapping, form, request, response);
			//增加机构
		} else if (parameter.equals("beforeAdd")) {
			return beforeAdd(mapping, form, request, response);
			//增加机构
		} else if (parameter.equals("add")) {
			return add(mapping, form, request, response);
		} else if (parameter.equals("beforeEdit")) {
			return beforeEdit(mapping, form, request, response);
		} else if (parameter.equals("edit")) {
			return edit(mapping, form, request, response);
		} else if (parameter.equals("delete")) {
			return delete(mapping, form, request, response);
		} else if (parameter.equals("setFirstNode")) {
			// 设置机构为一级节点
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
			/** 1如果该机构为真实机构，则不能设置成为一级节点? */
			AfOrg orgInfo = AFOrgDelegate.getOrgInfo(orgInfoForm.getOrgId());

			if (orgInfo.getIsCollect() != null
					&& orgInfo.getIsCollect().equals(
							new Integer(Config.NOT_IS_COLLECT))) {
				addMyMessage(request, "orgInfo.could.not.setFirstNode");
				return mapping.findForward("failed");
			}
			orgInfoForm.setIsCollect(new Integer(Config.IS_COLLECT));
			/** 2更新机构信息 */
			if (AFOrgDelegate.setFirstNode(orgInfoForm)) {
				// 同步更新1104部分
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

		// 页面跳转
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
			// 重新生成机构树
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);
			String orgId = orgInfoForm.getOrgId();

			if (orgId == null || orgId.equals("")) {
				throw new Exception("机构ID为空！");
			}

			AfOrg oi = AFOrgDelegate.getOrgInfo(orgId);

			/** 1如果该机构为根节点"总行"，则禁止删除 */
			if (oi.getPreOrgId().equals(Config.TOPBANK)) {
				addMyMessage(request, "orgInfo.forbid.delete");
				return mapping.getInputForward();
			}
			/** 2 判断删除的机构是否有子机构，如果有，则必须先删除子机构 */
			List lstChildList = AFOrgDelegate.getChildList(orgId);
			if (lstChildList != null && lstChildList.size() > 0) {
				addMyMessage(request, "orgInfo.has.childAfOrg");
				return mapping.getInputForward();
			}
			/** 3 判断删除的机构是否有下属人员，如果有，则必须先删除人员 */
			if (AFOrgDelegate.hasUsers(orgId)) {
				addMyMessage(request, "orgInfo.has.userInfo");
				return mapping.getInputForward();
			}

			// /** 4 判断删除的[真实]机构是否有对应的指标集数据，如果有，则禁止删除，指标集数据需要到后台删除 */
			// if (!oi.getIsCollect().toString().equals(Config.IS_COLLECT)) {
			// if (AFOrgDelegate.hasMeasureAlert(orgId)) {
			// addMyMessage(request, "AfOrg.has.hasMeasureAlert");
			// return mapping.getInputForward();
			// }
			// }

			/** 5 删除机构信息 */
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
					// 同步更新1104部分
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
			 * 同步分析系统结束
			 */
			
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			addMyMessage(request, "delete.failed", "orgInfo.info");
		}
		// 页面跳转
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

		// 0:修改机构失败；1：修改机构成功；2：需要修改汇总关系
		int result = 1;
		// 是否为虚拟机构，0为真实机构，1为虚拟机构
		int isCollect = 1;
		try {
			// 得到父节点的详细信息
			parentAfOrg = AFOrgDelegate.getOrgInfo(orgInfoForm.getParentOrgId());
			// 改变机构代码
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
				/** 1 判断是否有相同的机构名称(真实机构的名称禁止修改) */
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

						// 复制信息
						this.copyProperties(orgInfoForm, AfOrg);
						orgInfoForm.setOrgName(newOrgName);

						// 将填充数据的表单存放至request对象
						request.setAttribute("orgInfoForm", orgInfoForm);
						AFOrgDelegate.makeOrgTree();
						request.setAttribute("FileName",
								Config.BASE_ORG_TREEXML_NAME);

						// // 对真实机构进行操作
						// if (isCollect == 0) {
						// // 加载机构层次
						// request.setAttribute("lstOrgLevel",
						// StrutsCodeLibDelegate
						// .getCodeLibList("10", true));
						// // 加载机构类型
						// request.setAttribute("lstOrgType",
						// StrutsCodeLibDelegate
						// .getCodeLibList("24", true));
						// }
						// 返回请求页面
						return mapping.getInputForward();
					}
				}

				/** 2更新机构信息 */
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
						// 同步更新1104部分
						this.copyProperties(orgNetForm, orgInfoForm);
						orgNetForm.setSetOrgId(operator.getOrgId());
						StrutsOrgNetDelegate.update(orgNetForm);
					//}
					addMyMessage(request, "update.success", "orgInfo.info");

				} else {

					throw new Exception("update.failed");
				}
				/**
				 * 同步分析系统开始
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
				 * 同步分析系统结束
				 */
				
			}

			

			/** 3判断修改的机构是否为虚拟机构，如果是，则需要修改汇总关系 */
			// if (!orgInfoForm.getParentOrgId().equals(Config.TOPBANK)) {
			// // 如果不是总行(根节点)，则判断是否为虚拟机构
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
			// 重新生成机构树
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);
			AfOrg AfOrg = AFOrgDelegate.getOrgInfo(orgInfoForm.getOrgId());
			// 复制信息
			try {
				this.copyProperties(orgInfoForm, AfOrg);
			} catch (Exception e1) {
				log.printStackTrace(e1);
			}

			// 将填充数据的表单存放至request对象
			request.setAttribute("orgInfoForm", orgInfoForm);
			request.setAttribute("parentAfOrg", parentAfOrg);

			// // 加载机构层次
			// request.setAttribute("lstOrgLevel", StrutsCodeLibDelegate
			// .getCodeLibList("10", true));
			// // 加载机构类型
			// request.setAttribute("lstOrgType", StrutsCodeLibDelegate
			// .getCodeLibList("24", true));
		}

		// 页面跳转
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
	 * 查看机构
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
		
		/** 加载机构详细信息 */
		boolean result = true;
		// 是否为虚拟机构，0为真实机构，1为虚拟机构
		int isCollect = 1;
		try {
			// 重新生成机构树
			/**已使用hibernate 卞以刚 2011-12-22**/
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);
			// 得到一个机构详细信息
			AfOrg afOrg = AFOrgDelegate.getOrgInfo(orgId);

			// 复制信息
			this.copyProperties(orgInfoForm, afOrg);

			// 将填充数据的表单存放至request对象
			request.setAttribute("orgInfoForm", orgInfoForm);

			AfOrg parentAfOrg = null;
			// 如果不是汇总机构，则找上级机构
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

			// 对真实机构进行操作
			//if (isCollect == 0) {
				// // 加载机构层次
				// request.setAttribute("lstOrgLevel", StrutsCodeLibDelegate
				// .getCodeLibList("10", true));
				
				// 加载机构类型
				if(afOrg.getOrgType()!=null)
					/**已使用hibernate 卞以刚 2011-12-22**/
					request.setAttribute("orgType", 
						StrutsOrgTypeDelegate.selectOne(Integer.valueOf(afOrg.getOrgType())));
			
				if(type.equals("update")){
					//地区
					/**已使用hibernate 卞以刚 2011-12-22**/
					request.setAttribute("lstMRegion", StrutsMRegionDelegate.findAll());
				}else{
					if(afOrg.getRegionId() != null)
						/**已使用hibernate 卞以刚 2011-12-22**/
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
		/** detail-用户查看JSP；update-用户修改JSP */
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
		
		// 得到父节点的详细信息
		AfOrg parentAfOrg = AFOrgDelegate.getOrgInfo(orgInfoForm.getParentOrgId());

		// 0:增加机构失败；1：增加机构成功；2：需要增加汇总关系
		int result = 1;

		try {
			if (parentAfOrg!=null &&
					parentAfOrg.getIsCollect()!=null &&
					parentAfOrg.getIsCollect().equals(Config.IS_COLLECT)){
				// 虚拟机构不可添加子机构
				addMyMessage(request, "orgInfo.collectchild.info");
				return mapping.findForward("failed");
			}
			
			/** 1 判断是否为空*/
			if (orgInfoForm.getOrgId()==null || orgInfoForm.getOrgName()==null
					|| orgInfoForm.getOrgId().trim().equals("") 
					|| orgInfoForm.getOrgName().trim().equals("")) {
				// 加载错误信息
				addMyMessage(request, "orgInfo.null.info");
				return mapping.findForward("failed");
			}
			
			/** 1 判断是否有相同的机构代码 */
			if (AFOrgDelegate.isExistSameOrgId(orgInfoForm)) {
				// 加载错误信息
				addMyMessage(request, "orgInfo.same.id");
				return mapping.findForward("failed");
			}

			/** 2 判断是否有相同的机构名称 */
			if (AFOrgDelegate.isExistSameOrgName(orgInfoForm)) {
				// 加载错误信息
				addMyMessage(request, "orgInfo.same.name");
				return mapping.findForward("failed");
			}

			/** 3增加机构信息 */
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
					// 同步新增1104部分
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
			 * 同步分析系统开始
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
			 * 同步分析系统结束
			 */

			/** 4判断增加的机构是否为需要增加汇总关系，如果是，则需要增加汇总关系 */
			if (parentAfOrg == null) {
				result = 2;
				request.setAttribute("orgInfoForm", orgInfoForm);
			}
		} catch (Exception e) {
			/** 错误则返回原页面 */
			log.printStackTrace(e);
			result = 0;
			addMyMessage(request, "add.failed", "orgInfo.info");
		}

		// 页面跳转
		if (result == 1) {
			return mapping.findForward("success");
		} else if (result == 0) {
			return mapping.findForward("failed");
		} else {
			return mapping.findForward("addCollect");
		}
	}
	
	/***
	 * 增加机构
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
		// 是否为虚拟机构，0为真实机构，1为虚拟机构
		int isCollect = 1;
		try {
			// 重新生成机构树
			/**已使用hibernate 卞以刚 2011-12-22**/
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);
			// 得到父节点的详细信息
			String orgId = orgInfoForm.getParentOrgId();
			

			
			if (orgId != null && !orgId.equals("")) {
				/**已使用hibernate 卞以刚 2011-12-21**/
				AfOrg parentAfOrg = AFOrgDelegate.getOrgInfo(orgId);		
				request.setAttribute("parentOrgInfo", parentAfOrg);
				
				if (parentAfOrg.getIsCollect() == null
						|| !parentAfOrg.getIsCollect().toString().equals(
								Config.IS_COLLECT)) {

					// 真实机构
					isCollect = 0;
					request.setAttribute("isCollect", new Integer(0));
				} else {
					// 虚拟机构不可添加子机构
					addMyMessage(request, "orgInfo.collectchild.info");
					return mapping.getInputForward();
					//request.setAttribute("isCollect", new Integer(1));
				}
			} else {
				request.setAttribute("isCollect", new Integer(1));
			}
			// 对真实机构进行操作
			if (isCollect == 0) {
				// // 加载机构层次
				// request.setAttribute("lstOrgLevel", StrutsCodeLibDelegate
				// .getCodeLibList("10", true));
				// 加载机构类型
				/**已使用hibernate 卞以刚 2011-12-22**/
				request.setAttribute("lstOrgType", StrutsOrgTypeDelegate
						.findAll());
			}
				/**已使用hibernate 卞以刚 2011-12-22**/
				request.setAttribute("lstMRegion", StrutsMRegionDelegate
						.findAll());
			
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
			addMyMessage(request, "select.fail", "orgInfo.info");
		}

		// 页面跳转
		if (result) {
			return mapping.findForward("success");
		} else {
			return mapping.getInputForward();
		}
	}

	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 查看所有机构
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
			/**已使用hibernate 卞以刚 2011-12-22**/
			AFOrgDelegate.makeOrgTree();
			request.setAttribute("FileName", Config.BASE_ORG_TREEXML_NAME);

			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
				operator = (Operator) session
						.getAttribute(Config.OPERATOR_SESSION_NAME);
			else
				operator = new Operator();
			// 刷新机构树,使页面显示保持一致
			/**已使用hibernate  卞以刚 2011-12-21**/
			operator.reFreshOrgTree();
		} catch (Exception e) {
			log.printStackTrace(e);
			addMyMessage(request, "select.fail", "orgInfo.info");
		}

		return mapping.findForward("success");
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
	 * 将org信息传入form
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
	 * 将新的org信息传入1104org
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
