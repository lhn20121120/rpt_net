package com.fitech.net.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsContrastReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.excel.DB2Excel;

import excelToHTML.engine.ToHTMLEngine;
import excelToHTML.usermodel.ReportDefine;

/**
 * 信息对比
 * @author zyl_xh
 *
 */
public class ViewCheckReportAction extends Action{
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		boolean result = true;
		try {
			//取得传入的需要对比的2个报表的主键
			Integer repInId = Integer.valueOf(request.getParameter("repInId"));
			Integer compRepInId = Integer.valueOf(request.getParameter("compRepInId"));
			
			//取得对比以后各个单元格的值
			StrutsContrastReportDelegate checkReport = new StrutsContrastReportDelegate();
			DB2Excel db2Excel = new DB2Excel(repInId);
			db2Excel.cells = checkReport.getCheckList(repInId, compRepInId);
			String filePath = Config.TEMP_DIR + Config.FILESEPARATOR+ System.currentTimeMillis() + ".xls";
			//讲对比的各个单元格的值写入对比的报表的模板Excel内
			if (!db2Excel.createExcel(filePath)) {
				return mapping.findForward("error");
			}
			//生成html页面做展示
			ReportDefine rd = new ReportDefine(filePath);
			String fileName = System.currentTimeMillis() + ".html";
			ToHTMLEngine htmEngine = new ToHTMLEngine();
			htmEngine.generateHTML(rd, Config.TEMP_DIR+Config.FILESEPARATOR+fileName);
			request.setAttribute("HTML", request.getContextPath() + "/tmp/"	+ fileName);
		}catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		//页面跳转
		if (result) {
			return mapping.findForward("show_checkReprot");
		} else {
			return mapping.findForward("error");
		}
	}
}