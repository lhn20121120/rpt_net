package com.fitech.gznx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi2.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi2.hssf.usermodel.HSSFWorkbook;

/**
 * ʹ��POI2��jar���е���ش�����в����ĸ�����
 * 
 * @author Nick
 * 
 */
public class POI2Util {
	/**
	 * �򿪵�����Excel�����¼������еĹ�ʽ���򲢱���
	 * 
	 * @param file
	 *            File ��Ҫ���¼����Excel�ļ�
	 * @throws Exception
	 */
	public static void excelFormulaEval(File file) throws Exception {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			if (file == null) {
				return;
			}

			fis = new FileInputStream(file);
			HSSFWorkbook book2 = new HSSFWorkbook(fis);
			// �Զ��������еĹ�ʽ����
			HSSFFormulaEvaluator.evaluateAllFormulaCells(book2);
			// ����ԭ��Excel�ļ�
			fos = new FileOutputStream(file);
			book2.write(fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				fis.close();
			if (fos != null)
				fos.close();
		}
	}
}
