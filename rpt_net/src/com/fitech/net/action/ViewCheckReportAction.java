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
 * ��Ϣ�Ա�
 * @author zyl_xh
 *
 */
public class ViewCheckReportAction extends Action{
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		boolean result = true;
		try {
			//ȡ�ô������Ҫ�Աȵ�2�����������
			Integer repInId = Integer.valueOf(request.getParameter("repInId"));
			Integer compRepInId = Integer.valueOf(request.getParameter("compRepInId"));
			
			//ȡ�öԱ��Ժ������Ԫ���ֵ
			StrutsContrastReportDelegate checkReport = new StrutsContrastReportDelegate();
			DB2Excel db2Excel = new DB2Excel(repInId);
			db2Excel.cells = checkReport.getCheckList(repInId, compRepInId);
			String filePath = Config.TEMP_DIR + Config.FILESEPARATOR+ System.currentTimeMillis() + ".xls";
			//���Աȵĸ�����Ԫ���ֵд��Աȵı����ģ��Excel��
			if (!db2Excel.createExcel(filePath)) {
				return mapping.findForward("error");
			}
			//����htmlҳ����չʾ
			ReportDefine rd = new ReportDefine(filePath);
			String fileName = System.currentTimeMillis() + ".html";
			ToHTMLEngine htmEngine = new ToHTMLEngine();
			htmEngine.generateHTML(rd, Config.TEMP_DIR+Config.FILESEPARATOR+fileName);
			request.setAttribute("HTML", request.getContextPath() + "/tmp/"	+ fileName);
		}catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		//ҳ����ת
		if (result) {
			return mapping.findForward("show_checkReprot");
		} else {
			return mapping.findForward("error");
		}
	}
}