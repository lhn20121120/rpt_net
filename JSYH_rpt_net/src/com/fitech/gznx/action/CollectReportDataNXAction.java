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
 * 汇总各机构的报送报表
 * @author ＷＨ
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

			/** 取得当前用户的权限信息 */
			HttpSession session = request.getSession();
			Operator operator = null;
			if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
				operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			
			/** 取得模板大类类型 */
			Integer templateType = null;
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null)
				templateType = Integer.valueOf(session.getAttribute(Config.REPORT_SESSION_FLG).toString());
			
			String isMulti = request.getParameter("select_collect_type");
			//判断是否是递归汇总 如果不是则只进行一层汇总 。
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
			//判空既表示汇总失败，添加失败记录
			if ((childRepId == null || childRepId.equals("")
					|| versionId == null || versionId.equals("")
					/*|| childOrgIds == null || childOrgIds.equals("")*/)
					&& (isMulti == null || !isMulti.equals("multi"))) {
				
				messages.add("汇总失败！");
				
				if (messages.getMessages() != null
						&& messages.getMessages().size() > 0)
					request.setAttribute(Config.MESSAGES, messages);

				return new ActionForward(term + this.getTerm(reportInForm));
			}
			
			AFOrgDelegate afOrgDele = new AFOrgDelegate();
			List mustOrgs = null;
			
			//开始汇总
			AFDataDelegate afDataDele = new AFDataDelegate();
			if (isMulti != null && isMulti.equals("multi")) {//批量汇总
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
				for (int i = 0; i < parArr.length; i++) {//多张循环
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
					if(rule==null)//普通加总
						rs = AFDataDelegate.collectDataIter(strArr,messages, afDataDele,
								 templateType,childOrgIds, afOrgDele,collResult,operator,isIterator,strArr[2]);
					else//轧差汇总
						rs = AFDataDelegate.collectDataByGacha(rule,strArr,messages, afDataDele,
								 templateType,childOrgIds, afOrgDele,collResult,operator,isIterator);
					AfOrg org = AFOrgDelegate.getOrgInfo(strArr[2].trim());
//					messages.add("");
//					messages.add("");
					if(rs)
						messages.add(org.getOrgName() +  strArr[0] + "数据汇总成功，并且校验通过！");
//					else//2013-01-03:LuYueFei:如果汇总存在问题，则已经包含汇总失败的提示，因此不用重复提示汇总失败的消息
//						messages.add(org.getOrgName() +  strArr[0] + "数据汇总未完成！");
		//				messages.add(org.getOrgName() +  strArr[0] + "数据汇总未完成" + collResult.getSucceed() + "张 失败" + collResult.getFailure() + "张！");
				}
				request.getSession().setAttribute("multi", messages);
			} else {
				//单张汇总
				
				//汇总规则
				Integer collectRule = null;

//				if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
										
					//获得当前该报表报送机构的信息
					/**已使用hibernate 卞以刚 2011-12-21**/
					AfOrg reportOrg = AFOrgDelegate.selectOne(orgId);
					
					//总行的取虚拟机构汇总
//					if (reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK)){
//						collectRule = 1;
//					}
					//虚拟机构取真实机构汇总
//					else 
					if(reportOrg.getIsCollect().equals(Long.valueOf(com.fitech.gznx.common.Config.IS_COLLECT))){
						collectRule = 2;
					}
					//其他正常汇总
					else {
						collectRule = 3;
					}
//				}else{
//					collectRule = 0;
//				}

				//加入总行特殊处理(人行时取汇总机构设定的汇总关系，脱离层级汇总)
				if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)
						&& reportOrg.getPreOrgId().equals(com.fitech.gznx.common.Config.TOPBANK))
					collectRule = 2;
				
				// 得到一张报表的报送机构列表
//				if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
					
					//获得需汇总的机构编号
					/**已使用hibernate 卞以刚 2011-12-21**/
					mustOrgs = afOrgDele.getMustOrgList(childRepId,versionId, childOrgIds, orgId, collectRule);					
					childOrgIds = "";
					for (int j=0;j<mustOrgs.size();j++){
						AfOrg afOrg = (AfOrg) mustOrgs.get(j);
						childOrgIds += childOrgIds.equals("") ? "'" + afOrg.getOrgId() + "'" : ",'" + afOrg.getOrgId() + "'";
					}
//				}

				Integer reportInID = null;
				
				//新增清单、点点分别处理
				/**已使用hibernate 卞以刚 2011-12-21**/
				int reportStyle = AFTemplateDelegate.getReportStyle(childRepId, versionId);
				
				if(reportStyle == Config.REPORT_STYLE_QD.intValue()){
					//清单式处理
					/**jdbc技术 需要修改 卞以刚 2011-12-21**/
					reportInID = afDataDele.collectQDReport(childRepId, versionId, 
							Integer.valueOf(reportInForm.getYear()),
							Integer.valueOf(reportInForm.getTerm()), 
							Integer.valueOf(reportInForm.getDay()),
							repFreqId, curId, orgId, childOrgIds);
					
				}else{
					// 汇总方法,成功返回记录ID 失败返回 null
					/**jdbc技术 需要修改 卞以刚 2011-12-21
					 * oracle语法**/
					reportInID = afDataDele.doCollect(childRepId, versionId,
							orgId, childOrgIds, Integer.valueOf(reportInForm.getYear().trim()),
							Integer.valueOf(reportInForm.getTerm().trim()), Integer.valueOf(reportInForm.getDay().trim()), 
							repFreqId, curId ,templateType,orgId);
				}
				
				if (reportInID == null)
					messages.add(childRepId + "报表汇总失败（汇总数据异常）！");
				else if (reportInID.intValue() == -1)
					messages.add(childRepId + "报表汇总失败（未设置汇总关系）！");
				else
					messages.add(childRepId + "报表汇总成功！");
			}
			if (messages.getMessages() != null
					&& messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);

			return new ActionForward(term+(backStr==null?"":backStr)); //+ this.getTerm(reportInForm));
			
		} catch (Exception ex) {
			messages.add("汇总失败！");
			if (messages.getMessages() != null
					&& messages.getMessages().size() > 0)
				request.setAttribute(Config.MESSAGES, messages);
			log.printStackTrace(ex);

			return new ActionForward(term + this.getTerm(reportInForm));
		}
	}
	
	/**
	 * 得到
	 * @param reportInForm
	 * @return
	 */
	private String getTerm(AFReportForm reportInForm){
		String term = "";
		/** 加入报表编号条件 */
		if (reportInForm.getTemplateId() != null
				&& !reportInForm.getTemplateId().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "templateId=" + reportInForm.getTemplateId();
		}
		/** 加入报表名称条件 */
		if (reportInForm.getRepName() != null
				&& !reportInForm.getRepName().equals("")) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			/** 若是WebLogic则不需要进行转码，直接作为参数传递 */
			term += "repName=" + reportInForm.getRepName();
			/** 若是WebSphere则需要先进行转码，再作为参数传递 */
			// term += "repName=" + new
			// String(reportInForm.getRepName().getBytes("gb2312"),
			// "iso-8859-1");
		}
		/** 加入模板类型条件 */
		if (reportInForm.getBak1() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "bak1=" + reportInForm.getBak1();
		}
		/** 加入报表年份条件 */
		if (reportInForm.getDate() != null) {
			term += (term.indexOf("?") >= 0 ? "&" : "?");
			term += "date=" + reportInForm.getDate();
		}

		
		return term;
	}
}
