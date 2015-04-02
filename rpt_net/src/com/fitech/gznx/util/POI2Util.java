package com.fitech.gznx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
}
