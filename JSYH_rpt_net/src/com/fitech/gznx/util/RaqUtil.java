package com.fitech.gznx.util;

import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.DataSetConfig;
import com.runqian.report4.usermodel.DataSetMetaData;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.util.ReportUtils;

/**
 * 删除润乾模板中的无效数据集（一般ds_org、以报表编号开头的、fitech开头的数据集不删除）， <br>
 * 同时删除单元格的取数规则，加快润乾报表的导出效率
 * 
 * @author dou
 * 
 */
public class RaqUtil {

	/**
	 * 删除润乾模板中的无效数据集（一般ds_org、以报表编号开头的、fitech开头的数据集不删除），同时删除无效的单元格的取数规则，加快润乾报表的导出效率
	 */
	public static void removeDataSetFromRaq(ReportDefine rd, String templateId) {
		if (rd == null) {
			return;
		}

		DataSetMetaData dataSet = rd.getDataSetMetaData();
		if (dataSet == null || dataSet.getDataSetConfigCount() == 0) {
			return;
		}

		try {
			// 取得最大行数和列数
			int maxrow = rd.getRowCount();
			int maxcol = rd.getColCount();

			/** 1.迭代删除无效的单元格取数规则 */
			int count = dataSet.getDataSetConfigCount();
			INormalCell icell = null;
			DataSetConfig df = null;
			String expMap = null, dfName = null;
			for (int i = 1; i <= maxrow; i++) {// 润乾模板的首行为1
				for (int j = 1; j <= maxcol; j++) {// 润乾模板的首列为1
					icell = rd.getCell(i, (short) j);// 取得一个单元格对象
					if (icell.getExpMap() == null) {// 判断单元格规则是否为NULL
						continue;
					}
					expMap = icell.getExpMap().toString();
					if (expMap.equals("{}")) {// 判断单元格规则是否为空
						continue;
					}
					for (int m = 0; m < count; m++) {// 迭代所有的数据集，进行无效单元格取数规则的删除
						df = dataSet.getDataSetConfig(m);
						if (expMap.indexOf(templateId.trim()) != -1 || expMap.toLowerCase().indexOf("ds_org") != -1 || expMap.toLowerCase().indexOf("fitech") != -1) {
							break;// 判断是否为需要保留的数据集
						}
						dfName = df.getName().trim()+".";//20140401:如果单元格中有ROUND函数，同时数据集名字为ND，此时误删除单元格自动计算公式，因此需要加上"."进行判断
						if (expMap.indexOf(dfName) != -1) {// 判断是否引用数据集的表达式
							icell.setExpMap(null);
							break;
						}
					}
				}
			}
			// System.out.println("单元格规则删除结束。。。。。");
			/** 2.迭代删除无效的数据集 */
			int DSnum = 0;// 需要保留的数据集数量
			while (count > DSnum) {
				// ReportUtils.write("C:\\test" + count + ".raq", rd);
				df = dataSet.getDataSetConfig(0 + DSnum);
				if (df != null && df.getName() != null) {
					dfName = df.getName().trim();
					if (!dfName.startsWith(templateId.trim()) && !dfName.toLowerCase().equals("ds_org") && !dfName.toLowerCase().startsWith("fitech")) {
						dataSet.removeDataSetConfig(0 + DSnum);
						count--;
					} else {
						DSnum++;
					}
				}
			}
			// System.out.println("数据集删除结束。。。。。");

			// ReportUtils.write("C:\\test.raq", rd);
			// System.out.println("模板保存结束。。。。。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
