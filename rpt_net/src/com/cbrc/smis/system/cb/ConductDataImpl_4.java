/*
 * Created on 2005-12-17
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.system.cb;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.cbrc.smis.common.ConductString;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.common.XmlUtils;
import com.cbrc.smis.form.ReportInForm;

/**
 * @author cb
 * 该类用于对S38B报表进行入库处理
 * 
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductDataImpl_4 implements ConductListing {

	/**
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @return
	 */
	public String getSqlStringByOne(Element element, String tableName,
			boolean existsCol, Integer repId) throws Exception {

		String repIdString = String.valueOf(repId.intValue());

		String sql = "insert into " + tableName + " (REP_IN_ID,";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (!this.isCol(e.getName()))

				continue;

			sql = sql + e.getName();

			sql = sql + ",";
		}

		if (!existsCol) // 判断是否有COL1字段 如果没有就加入到SQL字符串中去

			sql = sql + ConfigOncb.COL1 + ",";

		int length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")values(" + repIdString + ",";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (!this.isCol(e.getName()))

				continue;

			String s = e.getStringValue();
			
			s = this.dataPoor(s);

			sql = sql + "'" + s + "',";

		}

		if (!existsCol){ // 判断是否有COL1字段 如果没有就加入到SQL字符串中去
			sql = sql + "'" + 
			(element.getName().equalsIgnoreCase(ConfigOncb.DETAIL)==true?ConfigOncb.LABELBYLIST:ConfigOncb.TOTALLABEL) + 
				"',";
		}

		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")";

		return sql;
	}

	/**
	 * 该方法用处理S38第四种类型的清单报表的方法
	 */

	public void conductData(File file, String tableName, Integer repId,
			String zipFileName, String xmlFileName) throws Exception {

		Element fileRoot = XmlUtils.getRootElement(file); // 形成清单式XML文件的跟目录

		Element e1 = null;

		Element e2 = null;

		Element e3 = null;

		Element e4 = null;

		List sqls = new ArrayList();

		for (Iterator i = fileRoot.elementIterator(); i // 进入第一层循环,并判断是否是Subform1
				.hasNext();) {

			e1 = (Element) i.next();

			String name = e1.getName();

			if (!name.equals("Subform1"))

				continue;

			for (Iterator ii = e1.elementIterator(); ii.hasNext();) { // 进入第二层循环,并判断是否是detail或total

				e2 = (Element) ii.next();

				String elementName = e2.getName();

				elementName = ConductString.getStringNotSpace(elementName);

				// // System.out.println(elementName);

				if (elementName.equals(ConfigOncb.DETAIL) && !ChooseDetail.isNull(e2)) { // 判断如果是detail则继续循环

					for (Iterator iii = e2.elementIterator(); iii.hasNext();) { // 进入第三层循环,并判断是否是Subform1

						e3 = (Element) iii.next();

						if (e3.getName().equals("Subform1") && !ChooseDetail.isNull(e3)) {

							for (Iterator iiii = e3.elementIterator(); iiii
									.hasNext();) { // 进入第四层循环,并判断是否是detail后打头的还是total

								e4 = (Element) iiii.next();

								String eName = e4.getName();

								if (this.isAvailability(eName) && !ChooseDetail.isNull(e4)) {

									String sql = this.getSqlStringByFour(e4,
											tableName, this.existsCol1(e4),
											repId); // 拿到插入数据库的SQL字符串

									StrutsListingTableDelegate.insertRecord(
											sql, zipFileName, xmlFileName,
											repId);

								}
							}
						}
					}

				}
				if (elementName.equals(ConfigOncb.TOTAL)) {     //这里判断如果是TOTAL则可以调用第一种清单式的报表的

					String sql_1 = this.getSqlStringByOne(e2, tableName, this
							.existsCol1(e2), repId);

					StrutsListingTableDelegate.insertRecord(sql_1, zipFileName,
							xmlFileName, repId);

				}

			}

			// StrutsListingTableDelegate.insertRecords(sqls); //持久化到数据库
		}

	}

	/**
	 * 该方法用于第四种类型的生成SQL字符串
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @return
	 * @throws Exception
	 */
	public String getSqlStringByFour(Element element, String tableName,
			boolean existsCol, Integer repId) throws Exception {

		String repIdString = String.valueOf(repId.intValue());

		String sql = "insert into " + tableName + " (REP_IN_ID,";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (!this.isCol(e.getName()))

				continue;

			sql = sql + e.getName();

			sql = sql + ",";
		}

		if (!existsCol) // 判断是否有COL1字段 如果没有就加入到SQL字符串中去

			sql = sql + ConfigOncb.COL1 + ",";

		int length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")values(" + repIdString + ",";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (!this.isCol(e.getName()))

				continue;

			String s = e.getStringValue();

			s = this.dataPoor(s); // 去掉多余的字串

			sql = sql + "'" + s + "',";

		}

		if (!existsCol){ // 判断是否有COL1字段 如果没有就加入到SQL字符串中去
			sql = sql + "'" + 
				(element.getName().equalsIgnoreCase(ConfigOncb.DETAIL)==true?ConfigOncb.LABELBYLIST:ConfigOncb.TOTALLABEL) + 
				"',";
		}
		
		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")";

		return sql;
	}

	/**
	 * 用于第四种类型S38报表中的COL类型判断
	 * 
	 * @param col
	 * @return
	 */
	public boolean isAvailability(String col) {

		boolean is = false;

		if (col.equals("detail1") || col.equals("total1")
				|| col.equals("detailHeader1"))

			is = true;

		return is;

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

		if (col.equals("COL0")) // 如果发现是"COL0"则直接退出 因为所有表里没有该字段

			return false;

		boolean is = true; // 初始化是COL元素

		String s = col.substring(0, 3);

		if (!s.equals("COL"))

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
	 * 去处字符串中的无效字符 如果是数字字符串则去处多余的零 如果是不是数字字符串则不作任何处理
	 * 
	 * @param oldString
	 * @return
	 */
	public String dataPoor(String oldString) {

		String newString = "";

		float f = 0;

		try {
			newString=oldString==null?"":oldString.trim();
			//f = Float.parseFloat(oldString); // 转成float 如果成功则说明是数字字符串

			//newString = String.valueOf(f); // 如果不成功则说明是普通字符串

		} catch (Exception e) {

			newString = oldString;
		}

		return newString;
	}
	public ReportInForm getChecker(File file, String tableName, Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void updataReportData(File file, String tableName, Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		// TODO Auto-generated method stub
		
	}
}