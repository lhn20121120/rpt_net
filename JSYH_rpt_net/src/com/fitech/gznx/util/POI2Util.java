package com.fitech.gznx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi2.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi2.hssf.usermodel.HSSFWorkbook;

/**
 * 使用POI2的jar包中的相关代码进行操作的辅助类
 * 
 * @author Nick
 * 
 */
public class POI2Util {
	/**
	 * 打开导出的Excel，重新计算所有的公式区域并保存
	 * 
	 * @param file
	 *            File 需要重新计算的Excel文件
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
			// 自动计算所有的公式区域
			HSSFFormulaEvaluator.evaluateAllFormulaCells(book2);
			// 覆盖原有Excel文件
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
	 * 数值型正则表达式实例(包括整数/浮点数/科学计算法数值)
	 */
	public static Pattern p = Pattern.compile("[\\+\\-]?[\\d]+([\\.][\\d]*)?([Ee][+-]?[\\d]+)?");

	/**
	 * 判断传入的字符串是否为数值
	 * 
	 * @param str
	 *            String 传入字符串
	 * @return boolean
	 */
	public static boolean isNumber(String str) {
		if (str == null || str.trim().equals("")) {
			return false;
		}
		// 验证传入的字符型是否符合正则表达式
		boolean result = p.matcher(str).matches();
		// G13/G14中身份证号和组织机构代码可能会以0开始，因此需要做特殊处理
		// 如果字符串符合正则表达式，则验证是否为以0开始的整数，如果是，则为字符型
		if (result && str.indexOf(".") == -1 && !str.equals("0") && str.startsWith("0")) {
			result = false;
		}
		return result;
	}

}
