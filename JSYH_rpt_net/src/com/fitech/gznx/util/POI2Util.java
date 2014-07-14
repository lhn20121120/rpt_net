package com.fitech.gznx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/**
	 * ��ֵ��������ʽʵ��(��������/������/��ѧ���㷨��ֵ)
	 */
	public static Pattern p = Pattern.compile("[\\+\\-]?[\\d]+([\\.][\\d]*)?([Ee][+-]?[\\d]+)?");

	/**
	 * �жϴ�����ַ����Ƿ�Ϊ��ֵ
	 * 
	 * @param str
	 *            String �����ַ���
	 * @return boolean
	 */
	public static boolean isNumber(String str) {
		if (str == null || str.trim().equals("")) {
			return false;
		}
		// ��֤������ַ����Ƿ����������ʽ
		boolean result = p.matcher(str).matches();
		// G13/G14�����֤�ź���֯����������ܻ���0��ʼ�������Ҫ�����⴦��
		// ����ַ�������������ʽ������֤�Ƿ�Ϊ��0��ʼ������������ǣ���Ϊ�ַ���
		if (result && str.indexOf(".") == -1 && !str.equals("0") && str.startsWith("0")) {
			result = false;
		}
		return result;
	}

}
