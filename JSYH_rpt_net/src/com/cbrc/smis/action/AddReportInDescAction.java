package com.cbrc.smis.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.service.AFReportDealDelegate;

/***
 * 获取report_in备注信息，并追加备注信息
 * @author 卞以刚 2012-01-20
 *
 */
public class AddReportInDescAction extends Action{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("utf-8");
		//获取reportin表主键信息
		String repInId = request.getParameter("repInId");
		
		//获取新添加的备注信息
		String addDesc=request.getParameter("addDesc");
		
		
		boolean isOK=true;
		/**若addDesc为null 则没有添加备注信息，则不需要更新备注信息
		 *则下面操作不需要执行 卞以刚 2012-01-20*/
		if(addDesc!=null && !addDesc.trim().equals(""))
		{
			isOK=false;
			
			/**卞以刚  添加报表的备注信息*/
			//获取已有的备注信息
			//String desc=new String(request.getParameter("desc").getBytes("ISO-8859-1"),"UTF-8");
			
			/** 报表选中标志 **/
			String reportFlg = "0";
			HttpSession session = request.getSession();
			if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
				reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
			}
			
			//从sessiong中获取用户信息
			Operator operator=(Operator)request.getSession().getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
			//获取当前日期
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//拼接字符串，符合备注规范
			;
			String msg="--用户[登录名:" + operator.getUserName() + ",姓名:" + operator.getOperatorName() + "]"+" "+sdf.format(new Date())+"\n";
			//拼接最后完整的修改信息
			String addDescMsg=addDesc.trim()+"\n"+msg;
			System.out.println(addDescMsg);
			//获取reportInForm
			//银监会报表
			if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
				ReportIn reportIn=StrutsReportInDelegate.getReportInByReportInId(Integer.valueOf(repInId));
				//重新设置备注信息,并添加非空判断
				if(reportIn.getRepDesc()!=null && !reportIn.getRepDesc().trim().equals(""))
					reportIn.setRepDesc(addDescMsg+reportIn.getRepDesc());
				else {
					reportIn.setRepDesc(addDescMsg);
				}
				//更新reportin表
				isOK=StrutsReportInDelegate.updateReportIn(reportIn);
			}else{//人行
				AfReport afReport=AFReportDealDelegate.getAFReportByRepId(Long.valueOf(repInId));
				if(afReport.getRepDesc()!=null && !afReport.getRepDesc().trim().equals(""))
					afReport.setRepDesc(addDescMsg+afReport.getRepDesc());
				else
					afReport.setRepDesc(addDescMsg);
				//更新afreport表
				isOK=AFReportDealDelegate.updateAFReport(afReport);
			}
		}
		
		return null;
	}
	
}
