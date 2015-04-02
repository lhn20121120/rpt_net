package com.fitech.net.collect;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.fitech.net.collect.util.CollectUtil;

/**
 * 
 * @author wh �嵥ʽ���������
 * 
 */
public class ListingCollect {

	private int startCol;

	private int endCol;

	private String[] specialCol = null;

	/**
	 * ��ʼ�����в���
	 */
	public ListingCollect(int startCol, int endCol, String[] specialCol) {

		this.startCol = startCol;
		this.endCol = endCol;
		this.specialCol = specialCol;
	}

	public Document start(ArrayList alDoc) {
		/** ��Ҫ���ܵ�Document */
		Document totalDoc = CollectUtil.initListDoc(alDoc);
		/** ��ʼ���� */
		totalDoc = CollectUtil.collectListReport(alDoc, startCol, endCol,
				specialCol);

		// System.out.println("collect  OK*************");

		return totalDoc;
	}

	public void stop() {
	}

	public Object getLogs() {
		Object obj = new Object();
		return obj;
	}

	/**
	 * ���嵥ʽ����ĵ�Ԫ����report_in_info��
	 * 
	 * @return
	 */
	public void insertListRep(Document totalDoc, Integer repInId,
			String tableName) {
		// ���
		int seq = 1;
		// ���sql���ļ��϶���
		ArrayList sqls = new ArrayList();
		// ���ڵ�
		Element root = null;
		root = totalDoc.getRootElement();
		if (root == null)
			return;
		// Subform1�ڵ�
		Element sf = null;
		sf = (Element) root.selectSingleNode(CollectUtil.Subform1Name);
		if (sf == null)
			return;
		// detail�ڵ㼯��
		List details = null;
		details = sf.selectNodes(CollectUtil.detailName);
		if (details == null)
			return;
		if (details.size() == 0)
			return;
		// detail�ڵ�
		Element detail = null;
		// sql���
		StringBuffer sql = null;
		// ����details
		for (int i = 0; i < details.size(); i++) {
			detail = (Element) details.get(i);
			sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append("(rep_in_id,col1");
			for (int m = 2; m <= endCol; m++) {
				sql.append(",col");
				sql.append(String.valueOf(m));
			}
			sql.append(")");
			sql.append(" values(");
			sql.append(String.valueOf(repInId.intValue()));
			sql.append(",");
			sql.append("'");
			sql.append(String.valueOf(seq));
			sql.append("'");
			// COL�ڵ�
			Element col = null;
			// COL�ڵ��ֵ
			String colValue = "0";
			// ȡ����COL�ڵ��ֵ��д��sql���
			for (int j = 2; j <= endCol; j++) {
				col = (Element) detail
						.selectSingleNode(CollectUtil.colName + j);
				if (col != null) {
					colValue = col.getText();
					if (colValue == null || colValue.equals("")) {
						colValue = "0";
					}
				}
				if (col == null) {
					colValue = "0";
				}
				sql.append(",");
				sql.append("'");
				sql.append(colValue);
				sql.append("'");
			}
			sql.append(")");
			sqls.add(sql.toString());
			seq++;
		}
		// total�ڵ�
		Element total = null;
		total = (Element) sf.selectSingleNode(CollectUtil.totalName);
		if (total != null) {
			sql = new StringBuffer();
			sql.append("insert into ");
			sql.append(tableName);
			sql.append("(rep_in_id,col1");
			for (int m = 2; m <= endCol; m++) {
				sql.append(",col");
				sql.append(String.valueOf(m));
			}
			sql.append(")");
			sql.append(" values(");
			sql.append(String.valueOf(repInId.intValue()));
			sql.append(",");
			sql.append("'");
			sql.append(String.valueOf(String.valueOf(seq)));
			sql.append("'");
			// total��COL�ڵ�
			Element total_col = null;
			// COL�ڵ��ֵ
			String total_colValue = "0";
			// ȡ����COL�ڵ��ֵ��д��sql���
			for (int j = 2; j <= endCol; j++) {
				total_col = (Element) total
						.selectSingleNode(CollectUtil.colName + j);
				if (total_col != null) {
					total_colValue = total_col.getText();
					if (total_colValue == null || total_colValue.equals("")) {
						total_colValue = "0";
					}
				}
				if (total_col == null) {
					total_colValue = "0";
				}
				sql.append(",");
				sql.append("'");
				sql.append(total_colValue);
				sql.append("'");
			}
			sql.append(")");
			sqls.add(sql.toString());
		}
		// ��report_in_info��
		StrutsListingTableDelegate.insertListRep(sqls);
	}
}
