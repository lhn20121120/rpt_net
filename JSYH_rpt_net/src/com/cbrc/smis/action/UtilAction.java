package com.cbrc.smis.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Session;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.po.AfValidateformula;

public class UtilAction extends Action {
	private static FitechException log = new FitechException(UtilAction.class);

	/**
	 * Performs action.
	 * 
	 * @param mapping
	 *            Action mapping.
	 * @param form
	 *            Action form.
	 * @param request
	 *            HTTP request.
	 * @param response
	 *            HTTP response.
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);

		FitechMessages messages = new FitechMessages();

		OperatorForm operatorForm = (OperatorForm) form;

		DBConn conn = null;
		Session session = null;
		boolean result = false;
		File file = new File("E:\\人行校验公式.xls");
		HSSFWorkbook workbook = null;
		InputStream input = new FileInputStream(file);
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		try {
			conn = new DBConn();
			session = conn.beginTransaction();
			workbook = new HSSFWorkbook(input);
			sheet = workbook.getSheetAt(0); // 只取第一个sheet
			// for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum();
			for (int i = 1; i < 2490; i++) {
				row = sheet.getRow(i);
				if (row != null) {
					AfValidateformula formula = new AfValidateformula();
					formula.setFormulaId((long) row.getCell((short) 1).getNumericCellValue());
					formula.setFormulaValue(row.getCell((short) 2).getStringCellValue().trim());
					formula.setFormulaName(row.getCell((short) 3).getStringCellValue().trim());
					formula.setValidateTypeId((long) row.getCell((short) 4).getNumericCellValue());
					formula.setTemplateId(row.getCell((short) 5).getStringCellValue().trim());
					formula.setVersionId(row.getCell((short) 6).getStringCellValue().trim());
					formula.setCellId((long) row.getCell((short) 7).getNumericCellValue());
					session.save(formula);
				} else {
					log.println("row=" + i + "     为 NULL");
				}
			}
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
			if (input != null)
				input.close();
		}
		log.println("======================执行结束！");
		return null;
	}

}
