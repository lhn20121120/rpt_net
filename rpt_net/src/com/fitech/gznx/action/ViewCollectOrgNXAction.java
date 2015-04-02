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
 * 报表汇总机构情况查询
 * @author WH
 *
 */
public class ViewCollectOrgNXAction extends Action {
	
	private FitechException log = new FitechException(ViewCollectOrgNXAction.class);

	/**
	 * @param result
	 *            查询返回标志,如果成功返回true,否则返回false
	 * @param MRegionForm
	 * @param request
	 * @exception Exception
	 *                有异常捕捉并抛出
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {

		FitechMessages messages = new FitechMessages();
		AFReportForm afReportForm = (AFReportForm) form;
		
		String date = "";
		
		try {
			
			date = request.getParameter("date");
			
			/** 取得当前用户的权限信息 */
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

			//从af_template_coll_rule表查询是否有该记录，判断是否是轧差汇总，结果不为null 表示 是轧差汇总
			AfTemplateCollRuleForm aform= new AfTemplateCollRuleForm();
			aform.setOrg_id(orgId);
			aform.setTemplate_id(templateId);
			aform.setVersion_id(versionId);
			boolean isgacha = AFTemplateCollRuleDelegate.isExistCollRule(aform);
			
			/** 获得报表大类类型 */
			Integer templateType = null;
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				templateType = Integer.valueOf(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
			
			if (templateId == null || templateId.equals("")
					|| versionId == null || versionId.equals("")
					|| childOrgIds == null || childOrgIds.equals("")) {

				messages.add("查看详细信息失败！");

				if (messages.getMessages() != null && messages.getMessages().size() > 0)
					request.setAttribute(Config.MESSAGES, messages);

				return new ActionForward("/viewCollectNX.do");
			}
			
			List resList = null;
			
			//汇总规则
			Integer collectRule = null;

			if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
				
				orgId = request.getParameter("orgId");
				
				//获得当前该报表报送机构的信息
				AfOrg reportOrg = AFOrgDelegate.selectOne(orgId);
				
				//总行的取虚拟机构汇总
//				if (reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)){
//					collectRule = 1;
//				}
				//虚拟机构取真实机构汇总
				//else 
				if(reportOrg.getIsCollect().equals(Long.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
					collectRule = 2;
				}
				//其他正常汇总
				else {
					collectRule = 3;
				}
				
				//加入总行特殊处理(人行时取汇总机构设定的汇总关系，脱离层级汇总)
				if(reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK))
					collectRule = 2;
				
			}else{
				//collectRule = 0;
				orgId = request.getParameter("orgId");
				
				//获得当前该报表报送机构的信息
				AfOrg reportOrg = AFOrgDelegate.selectOne(orgId);
				if(reportOrg.getIsCollect().equals(Long.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
					//collectRule = 5;
					collectRule = 2;
				}
				//其他正常汇总
				else {
					//collectRule = 6;
					collectRule = 3;
				}
			}
			
			// 得到一张报表的报送机构列表
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
					
					// 查找该机构,该报表的报表状态   １有效报表
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
			messages.add("查看详细信息失败！");

			if (messages.getMessages() != null && messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);

			return new ActionForward("/viewCollectNX.do?date=" + date);
		}
	}
}
