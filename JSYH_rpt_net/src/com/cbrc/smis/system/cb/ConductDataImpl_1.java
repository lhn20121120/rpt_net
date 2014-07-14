/*
 * Created on 2005-12-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.system.cb;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.cbrc.smis.adapter.StrutsListingColsDelegate;
import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ConductString;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.common.XmlUtils;
import com.cbrc.smis.form.ReportInForm;

/**
 * @author cb
 * 
 * 
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductDataImpl_1 implements ConductListing {

	/**
	 * 该方法用于处理第一种类型的清单式报表
	 * s32_1,s32_2,s32_3,s32_4,s32_5,s32_6,s32_7,s32_9,s33,s35
	 * 
	 * @param file
	 */
	public void conductData(File file, String tableName, Integer repId,
			String zipFileName, String xmlFileName) throws Exception {

		Element fileRoot = XmlUtils.getRootElement(file); // 形成清单式XML文件的跟目录

		Element root = null;

		Element element = null;

		// List sqls = new ArrayList();

		List cols = null;
		ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(repId);
		if (reportInForm != null)
			cols = StrutsListingColsDelegate.findCols(reportInForm
					.getChildRepId(), reportInForm.getVersionId());

		for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i
				.hasNext();) {
			root = (Element) fileRoot_i.next();
			String name = root.getName();

			if (!name.equals("Subform1"))
				continue;

			for (Iterator i = root.elementIterator(); i.hasNext();) {
				element = (Element) i.next();
				String elementName = element.getName();
				elementName = ConductString.getStringNotSpace(elementName);
				// 判断是否是detial或total
				if ((elementName.equals(ConfigOncb.DETAIL)
						|| elementName.equals(ConfigOncb.TOTAL))
						&& !ChooseDetail.isNull(element)) {
					String sql = this.getSqlStringByOne(element, tableName,
							this.existsCol1(element), repId, cols); // 拿到插入数据库的SQL字符串
					StrutsListingTableDelegate.insertRecord(sql, zipFileName,
							xmlFileName, repId);

					// sqls.add(sql);
					// // System.out.println(sql);
				}
			}

			// StrutsListingTableDelegate.insertRecords(sqls); //持久化到数据库
		}

	}
	
	public void updataReportData(File file, String tableName, Integer repInId, 
			String zipFileName, String xmlFileName) throws Exception {

		Element fileRoot = XmlUtils.getRootElement(file); // 形成清单式XML文件的跟目录

		Element root = null;

		Element element = null;

		List cols = null;
		ReportInForm reportInForm = StrutsReportInDelegate.getReportIn(repInId);
		if (reportInForm != null)
			cols = StrutsListingColsDelegate.findCols(reportInForm
					.getChildRepId(), reportInForm.getVersionId());

		for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i
				.hasNext();) {
			root = (Element) fileRoot_i.next();
			String name = root.getName();

			if (!name.equals("Subform1"))
				continue;

			for (Iterator i = root.elementIterator(); i.hasNext();) {
				element = (Element) i.next();
				String elementName = element.getName();
				elementName = ConductString.getStringNotSpace(elementName);
				// 判断是否是detial或total
				if ((elementName.equals(ConfigOncb.DETAIL)
						|| elementName.equals(ConfigOncb.TOTAL))
						&& !ChooseDetail.isNull(element)) {
					String sql = this.getUpdateSqlStringByOne(element, tableName,
							this.existsCol1(element), repInId, cols); // 拿到插入数据库的SQL字符串
					if(sql.equals("")) continue;
					StrutsListingTableDelegate.insertRecord(sql, zipFileName,
							xmlFileName, repInId);
				}
			}
		}

	}

	/**
	 * 根据输入的结点返回SQL字符串(用于第一种类型的清单式的报表)
	 * s32_1,s32_2,s32_3,s32_4,s32_5,s32_6,s32_7,s32_9,s33,s35
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @param 数据表的列名列表
	 * @return String
	 */
	public String getSqlStringByOne(Element element, String tableName,
			boolean existsCol, Integer repId, List cols) throws Exception {

		String repIdString = String.valueOf(repId.intValue());

		String sql = "insert into " + tableName + " (REP_IN_ID,";

		for (Iterator i = element.elementIterator(); i.hasNext();) {
			Element e = (Element) i.next();

			// if (!this.isCol(e.getName())) continue;
			if (!ConductUtil.isField(e.getName(), cols))
				continue;

			sql = sql + e.getName();
			sql = sql + ",";
		}

		// 判断是否有COL1字段 如果没有就加入到SQL字符串中去
		if (!existsCol)
			sql = sql + ConfigOncb.COL1 + ",";

		int length = sql.length();

		sql = sql.substring(0, length - 1);
		sql = sql + ")values(" + repIdString + ",";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			// if (!this.isCol(e.getName())) continue;
			if (!ConductUtil.isField(e.getName(), cols))
				continue;

			String s = e.getStringValue();

			s = this.dataPoor(s); // 去处多余的字串
			sql = sql + "'" + s + "',";
		}

		// 判断是否有COL1字段 如果没有就加入到SQL字符串中去
		if (!existsCol) {
			sql = sql
					+ "'"
					+ (element.getName().equalsIgnoreCase(ConfigOncb.DETAIL) == true ? ConfigOncb.LABELBYLIST
							: ConfigOncb.TOTALLABEL) + "',";
		}

		length = sql.length();
		sql = sql.substring(0, length - 1);
		sql = sql + ")";
		// // System.out.println("[ConductDataImpl_1]sql:" + sql);
		return sql;
	}

	public String getUpdateSqlStringByOne(Element element, String tableName,
			boolean existsCol, Integer repId, List cols) throws Exception {

		if(!existsCol) return "";
		
		String repIdString = String.valueOf(repId.intValue());
		
		String where = " where REP_IN_ID=" + repIdString;

		String sql = "update " + tableName + " set ";

		for (Iterator i = element.elementIterator(); i.hasNext();) {
			Element e = (Element) i.next();

			if (!ConductUtil.isField(e.getName(), cols))
				continue;

			String value = this.dataPoor(e.getStringValue());
			if(!e.getName().equals(ConfigOncb.COL1))
				sql = sql + e.getName() + "='" + value + "',";
			else
				where = where + " and " + ConfigOncb.COL1 + "='" + value + "'";
		}
		
		boolean bool = StrutsListingTableDelegate.isCol1Exist("select * from " + tableName.toUpperCase() + where);
		if(bool){
			int length = sql.length();

			sql = sql.substring(0, length - 1);
			sql = sql + where;
		}else{
			sql = this.getSqlStringByOne(element,tableName,existsCol,repId,cols);
		}
		return sql;
	}
	
	/**
	 * 该方法用于对s36,s37,s34_2等有类似detail5等标记的第二类点对点报表 的detail5和total元素进行鉴别
	 * 
	 * @param s
	 * @return
	 */
	public boolean isDetailNumberOrTotal(String s) {

		if (s.length() < 5)

			return false;

		boolean is = true;

		if (s.equals(ConfigOncb.TOTAL))

			return is;

		String sub = s.substring(0, 6);

		if (!sub.equals(ConfigOncb.DETAIL)) {

			is = false;

			return is;
		}

		sub = s.substring(6);

		try {

			int i = Integer.parseInt(sub);

		} catch (Exception e) {

			is = false;

			return is;
		}

		return is;

	}

	/**
	 * 根据输入的结点返回SQL字符串(用于第二种类型的清单式的报表) s36,s37,s34_2
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @return
	 * @throws Exception
	 */
	public String getSqlStringByTwo(Element element, String tableName,
			boolean existsCol, Integer repId) throws Exception {

		String name = element.getName();

		String typeString = name.substring(6); // 拿到type值 例如'detail2'中的'2'

		String repIdString = String.valueOf(repId.intValue());

		String sql = "insert into " + tableName + " (REP_IN_ID,TYPE,";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (!this.isCol(e.getName())) // 在这里判断是否是COL元素 如果不是则跳过

				continue;

			sql = sql + e.getName();

			sql = sql + ",";
		}

		if (!existsCol) // 判断是否有COL1字段 如果没有就加入到SQL字符串中去

			sql = sql + ConfigOncb.COL1 + ",";

		int length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")values(" + repIdString + ",'" + typeString + "',";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (!this.isCol(e.getName())) // 在这里判断是否是COL元素 如果不是则跳过

				continue;

			String s = e.getStringValue();

			sql = sql + "'" + s + "',";

		}

		if (!existsCol) // 判断是否有COL1字段 如果没有就加入到SQL字符串中去

			sql = sql + "'" + ConfigOncb.LABELBYLIST + "',";

		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")";

		return sql;

	}

	/**
	 * 该方法属于处理第S34_1,G51类型的清单式报表中的返回SQL字符串 中的处理total元素的子方法
	 * 
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @return
	 * @throws Exception
	 */
	public String getSqlStringByThreeTotal(Element element, String tableName,
			boolean existsCol, Integer repId) throws Exception {

		String repIdString = String.valueOf(repId.intValue());

		String sql = "insert into " + tableName + " (REP_IN_ID,";

		Element e = null;

		int length;

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			e = (Element) i.next();

			String colName = e.getName();

			if (!this.isCol(colName)) // 在这里判断是否是COL元素

				continue;

			sql = sql + colName + ",";

		}
		if (!existsCol)

			sql = sql + ConfigOncb.COL1 + ","; // 如果没有

		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")values(" + repId + ",";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			e = (Element) i.next();

			String colName = e.getName();

			if (!this.isCol(colName))

				continue;

			String s = e.getStringValue();

			sql = sql + "'" + s + "',";
		}

		if (!existsCol) // 判断是否有COL1字段 如果没有就加入到SQL字符串中去

			sql = sql + "'" + ConfigOncb.LABELBYLIST + "',";

		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")";

		return sql;

	}

	/**
	 * 该方法根据输入的结点返回一个detail中的记录数 输入的节点一般是detail 该方法用于 s34_1,G51 报表
	 * 
	 * @param element
	 *            COL2_8
	 * @return
	 * @throws Exception
	 */
	public int getRecordCountByThree(Element element) throws Exception {

		int recordCount = 1;

		int max = 0;

		int mid = 0;

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			String name = e.getName();

			int m = name.indexOf('_');

			m++;

			String sub = name.substring(m);

			try {

				mid = Integer.parseInt(sub); // 得到一个COL后的数字

			} catch (Exception ee) {

				continue;
			}

			if (mid > max)

				max = mid;

			return max;

		}

		return recordCount;
	}

	public void conductByFourList(File file, String tableName, Integer repId)
			throws Exception {

	}

	/**
	 * 判断是否存在Col1主键字段
	 * 
	 * @param element
	 * @return
	 */
	public boolean existsCol1(Element element) throws Exception {

		boolean exists = false; // 默认是不存在

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (e.getName().equals(ConfigOncb.COL1)) {

				exists = true;

				break;

			}
		}

		return exists;
	}

	/**
	 * 判断是否是COL元素
	 * 
	 * @param col
	 * @return 如果是则返回true 否则返回false
	 * @throws Exception
	 */
	public boolean isCol(String col) throws Exception {

		if (col.length() < 4)

			return false;

		if (col.toUpperCase().equals("COL0")) // 如果发现是"COL0"则直接退出 因为所有表里没有该字段

			return false;

		boolean is = true; // 初始化是COL元素

		String s = col.substring(0, 3);

		if (!s.toUpperCase().equals("COL"))

			is = false;

		return is;
	}

	/**
	 * 该方法用于判断是否是COL元素
	 * 
	 * @param col
	 *            Sting
	 * @return 如果返回真 则是COL元素 否则返回假
	 * @throws Exception
	 */
	public boolean isColByTwo(String col) throws Exception {

		boolean is = true; // 初始化是COL字段

		String s = col.substring(0, 3);

		if (!s.equals("COL"))

			return false; // 如果头三个字符不是COL则直接返回假

		String ss = col.substring(3);

		try {

			int i = Integer.parseInt(ss); // 如果后面的字符不是数字则返回假

		} catch (Exception e) {

			is = false;
		}

		return is;
	}

	/**
	 * 根据输入的结点拼出一个XML文件名
	 * 
	 * @param e
	 * @return String XML文件名
	 * @throws Exception
	 */
	public String createXmlName(Element e) throws Exception {

		String xmlName = this.getElementValue(e, ConfigOncb.ORGID);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.REPORTID);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.VERSION);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.DATARANGEID);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.YEAR);

		xmlName = xmlName + this.getElementValue(e, ConfigOncb.MONTH);

		xmlName = xmlName + ".xml";

		return xmlName;

	}

	/**
	 * 根据输入的结点元素找出该结点元素的子元素对应的值
	 * 
	 * @param element
	 *            输入的节点
	 * @param elementName
	 *            输入的子元素名
	 * @return 字符串
	 */
	public String getElementValue(Element element, String elementName) {

		String elementValue = "";

		Element e = null;

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			e = (Element) i.next();

			if (e.getName().equals(elementName)) {

				elementValue = e.getStringValue();

				elementValue = ConductString.getStringNotSpace(elementValue);

			}
		}
		return elementValue;
	}

	/**
	 * 返回里面一层ZIP文件的XML文件数量
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public int getXmlCount(File file) throws Exception {

		Element root = XmlUtils.getRootElement(file);

		Element element = null;

		int size = 0;

		for (Iterator i = root.elementIterator(); i.hasNext();) {

			element = (Element) i.next();

			if (element.getName().equals(ConfigOncb.REPORT))

				size++;
		}

		return size;
	}

	/**
	 * 该方法用于返回XML文件是类型 ?????????
	 * 
	 * @param file
	 * @return String XML文件的类型
	 * @throws Exception
	 */
	public String getListingType(File file) throws Exception {

		String type = ConfigOncb.TYPE1;

		return type;
	}

	/**
	 * 去处字符串中的无效字符 如果是数字字符串则去处多余的零 如果是不是数字字符串则不作任何处理
	 * 
	 * @param oldString
	 * @return
	 */
	public String dataPoor(String oldString) {
		if (oldString == null)
			oldString = "";

		String newString = "";

		float f = 0;

		try {
			return oldString.trim();
			// f = Float.parseFloat(oldString); // 转成float 如果成功则说明是数字字符串

			// newString = String.valueOf(f); // 如果不成功则说明是普通字符串

		} catch (Exception e) {

			newString = oldString;
		}

		return newString;
	}

	public ReportInForm getChecker(File file, String tableName, 
			Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		
		Element fileRoot = XmlUtils.getRootElement(file); // 形成清单式XML文件的跟目录
		Element root = null;
		Element element = null;
		ReportInForm reportInForm = new ReportInForm();

		for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i
				.hasNext();) {
			root = (Element) fileRoot_i.next();
			String name = root.getName();

			if (!name.equals("Subform1"))
				continue;
			
			for (Iterator i = root.elementIterator(); i.hasNext();) {
				element = (Element) i.next();
				String elementName = element.getName();
				elementName = ConductString.getStringNotSpace(elementName);
				// 判断是否是detial或total
				if (elementName.equals(ConfigOncb.TOTAL) && !ChooseDetail.isNull(element)) {
					for(Iterator iter = element.elementIterator();iter.hasNext();){
						Element e = (Element)iter.next();
						String eName = e.getName();
						if(eName.equals(ConfigOncb.FITECHFILLER)){
							reportInForm.setWriter(e.getStringValue().trim());
							continue;
						}else if(eName.equals(ConfigOncb.FITECHCHECKER)){
							reportInForm.setChecker(e.getStringValue().trim());
							continue;
						}else if(eName.equals(ConfigOncb.FITECHPRINCIPAL)){
							reportInForm.setPrincipal(e.getStringValue().trim());
						}
					}
				}
			}
		}
		return reportInForm;
	}
	
}
