package com.fitech.net.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.ExpExcelData;


public class ExpExcelDataAction extends Action {
	/**
	 * 处理导出excel的action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		request.setCharacterEncoding("utf-8");
		String backQry=request.getSession().getAttribute("backQry")!=null?
				request.getSession().getAttribute("backQry").toString():"";
		String reportFlg = (String) request.getSession().getAttribute(Config.REPORT_SESSION_FLG);
		FitechMessages messages=new FitechMessages();
		boolean res=false;
		Operator operator = (Operator) request.getSession().getAttribute(Config.OPERATOR_SESSION_NAME);
		try {
			Map map=new HashMap();
			String repInId = request.getParameter("repInId");
			if(repInId!=null && !repInId.equals("")){
				map.put("repInId",repInId);
			}
			map.put("reportFlg", reportFlg);
			ExpExcelData.expData(map,response,operator.getOperatorId().toString());
			res=true;
		} catch (Exception e) {
			e.printStackTrace();
			messages.add("导出失败!");
			request.setAttribute(com.cbrc.smis.common.Config.MESSAGES, messages);
			res=false;
		}
		if(res)
			return mapping.findForward("");
		return new ActionForward("/viewDataReport.do?"+backQry);
	}

}
