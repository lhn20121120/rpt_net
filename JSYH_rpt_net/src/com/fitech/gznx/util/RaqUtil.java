package com.fitech.gznx.util;

import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.usermodel.DataSetConfig;
import com.runqian.report4.usermodel.DataSetMetaData;
import com.runqian.report4.usermodel.INormalCell;
import com.runqian.report4.util.ReportUtils;

/**
 * ɾ����Ǭģ���е���Ч���ݼ���һ��ds_org���Ա����ſ�ͷ�ġ�fitech��ͷ�����ݼ���ɾ������ <br>
 * ͬʱɾ����Ԫ���ȡ�����򣬼ӿ���Ǭ����ĵ���Ч��
 * 
 * @author dou
 * 
 */
public class RaqUtil {

	/**
	 * ɾ����Ǭģ���е���Ч���ݼ���һ��ds_org���Ա����ſ�ͷ�ġ�fitech��ͷ�����ݼ���ɾ������ͬʱɾ����Ч�ĵ�Ԫ���ȡ�����򣬼ӿ���Ǭ����ĵ���Ч��
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
			// ȡ���������������
			int maxrow = rd.getRowCount();
			int maxcol = rd.getColCount();

			/** 1.����ɾ����Ч�ĵ�Ԫ��ȡ������ */
			int count = dataSet.getDataSetConfigCount();
			INormalCell icell = null;
			DataSetConfig df = null;
			String expMap = null, dfName = null;
			for (int i = 1; i <= maxrow; i++) {// ��Ǭģ�������Ϊ1
				for (int j = 1; j <= maxcol; j++) {// ��Ǭģ�������Ϊ1
					icell = rd.getCell(i, (short) j);// ȡ��һ����Ԫ�����
					if (icell.getExpMap() == null) {// �жϵ�Ԫ������Ƿ�ΪNULL
						continue;
					}
					expMap = icell.getExpMap().toString();
					if (expMap.equals("{}")) {// �жϵ�Ԫ������Ƿ�Ϊ��
						continue;
					}
					for (int m = 0; m < count; m++) {// �������е����ݼ���������Ч��Ԫ��ȡ�������ɾ��
						df = dataSet.getDataSetConfig(m);
						if (expMap.indexOf(templateId.trim()) != -1 || expMap.toLowerCase().indexOf("ds_org") != -1 || expMap.toLowerCase().indexOf("fitech") != -1) {
							break;// �ж��Ƿ�Ϊ��Ҫ���������ݼ�
						}
						dfName = df.getName().trim()+".";//20140401:�����Ԫ������ROUND������ͬʱ���ݼ�����ΪND����ʱ��ɾ����Ԫ���Զ����㹫ʽ�������Ҫ����"."�����ж�
						if (expMap.indexOf(dfName) != -1) {// �ж��Ƿ��������ݼ��ı��ʽ
							icell.setExpMap(null);
							break;
						}
					}
				}
			}
			// System.out.println("��Ԫ�����ɾ����������������");
			/** 2.����ɾ����Ч�����ݼ� */
			int DSnum = 0;// ��Ҫ���������ݼ�����
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
			// System.out.println("���ݼ�ɾ����������������");

			// ReportUtils.write("C:\\test.raq", rd);
			// System.out.println("ģ�屣���������������");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
