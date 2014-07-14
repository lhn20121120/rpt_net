package com.fitech.net.collect;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.fitech.net.collect.util.CollectUtil;

/**
 * 
 * @author wh 清单式报表汇总类
 * 
 */
public class ListingCollect {

	private int startCol;

	private int endCol;

	private String[] specialCol = null;

	/**
	 * 初始化所有参数
	 */
	public ListingCollect(int startCol, int endCol, String[] specialCol) {

		this.startCol = startCol;
		this.endCol = endCol;
		this.specialCol = specialCol;
	}

	public Document start(ArrayList alDoc) {
		/** 需要汇总的Document */
		Document totalDoc = CollectUtil.initListDoc(alDoc);
		/** 开始汇总 */
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
	 * 把清单式报表的单元格入report_in_info表
	 * 
	 * @return
	 */
	public void insertListRep(Document totalDoc, Integer repInId,
			String tableName) {
		// 序号
		int seq = 1;
		// 存放sql语句的集合对象
		ArrayList sqls = new ArrayList();
		// 根节点
		Element root = null;
		root = totalDoc.getRootElement();
		if (root == null)
			return;
		// Subform1节点
		Element sf = null;
		sf = (Element) root.selectSingleNode(CollectUtil.Subform1Name);
		if (sf == null)
			return;
		// detail节点集合
		List details = null;
		details = sf.selectNodes(CollectUtil.detailName);
		if (details == null)
			return;
		if (details.size() == 0)
			return;
		// detail节点
		Element detail = null;
		// sql语句
		StringBuffer sql = null;
		// 遍历details
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
			// COL节点
			Element col = null;
			// COL节点的值
			String colValue = "0";
			// 取所有COL节点的值，写入sql语句
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
		// total节点
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
			// total的COL节点
			Element total_col = null;
			// COL节点的值
			String total_colValue = "0";
			// 取所有COL节点的值，写入sql语句
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
		// 入report_in_info表
		StrutsListingTableDelegate.insertListRep(sqls);
	}
}
