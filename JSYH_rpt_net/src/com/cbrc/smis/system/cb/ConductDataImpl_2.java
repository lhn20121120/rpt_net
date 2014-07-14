/*
 * Created on 2005-12-18
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
 * 该类用于对S36,S3402,S3209清单式的报表进行处理
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductDataImpl_2 implements ConductListing {

	/**
	 * 用于处理第二中类型的清单式报表 s36,s37,s34_2
	 * 
	 * @param file
	 * @param tableName
	 * @param repId
	 * @throws Exception
	 */
	public void conductData(File file, String tableName, Integer repId,String zipFileName, String xmlFileName) throws Exception {
		List sqls = new ArrayList();
		Element fileRoot = XmlUtils.getRootElement(file); // 形成清单式XML文件的跟目录

		Element element = null;

		List cols=null;
		ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repId);
		if(reportInForm!=null) cols=StrutsListingColsDelegate.findCols(reportInForm.getChildRepId(),reportInForm.getVersionId());
		
		for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i.hasNext();) {
			Element root = (Element) fileRoot_i.next();
			if (!root.getName().equals("Subform1")) // 判断第二层是否是Subform1
				continue;

			for (Iterator i = root.elementIterator(); i.hasNext();) {
				element = (Element) i.next();
				String name = element.getName();
				name = ConductString.getStringNotSpace(name); // 去前后空格

				if (this.isDetailNumberOrTotal(name) && !ChooseDetail.isNull(element)) {
					//拿到插入数据库的SQL字符串
					String sql = this.getSqlStringByTwo(element, tableName,this.existsCol1(element), repId,cols); 
					StrutsListingTableDelegate.insertRecord(sql, zipFileName,xmlFileName, repId);

					// sqls.add(sql);
					// StrutsListingTableDelegate.insertRecords(sqls); // 插入数据库
				}
			}
		}
	}

	/**
	 * 该方法用于对s36,s34_2等有类似detail5等标记的第二类点对点报表 的detail5和total元素进行鉴别
	 * 
	 * @param s
	 * @return
	 */
	public boolean isDetailNumberOrTotal(String s) {

		boolean is = true; // 默认是

		int length = s.length();

		if (length < 5 || length > 8) // 过滤掉小于total长度字串和detailHeader

			return false;

		if (s.substring(0, 5).equals(ConfigOncb.TOTAL))

			return true;

		try {

			if (!s.substring(0, 6).equals(ConfigOncb.DETAIL))

				is = false;

		} catch (Exception e) {

			is = false;

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
	 * @param cols List 需把百分数转换成小数的列
	 * @return
	 * @throws Exception
	 */
	public String getSqlStringByTwo(Element element, String tableName,boolean existsCol, Integer repId,List cols) 
		throws Exception {
		String name = element.getName();
		String typeString = "";

		try {
			if (name.substring(0, 5).equals(ConfigOncb.TOTAL))
				typeString = name.substring(5);
			else
				typeString = name.substring(6); // 拿到type值 例如'detail2'中的'2'
		} catch (Exception e) {
			typeString = "";
		}

		String repIdString = String.valueOf(repId.intValue());
		String sql = "insert into " + tableName + " (REP_IN_ID,TYPE,";
		for (Iterator i = element.elementIterator(); i.hasNext();) {
			Element e = (Element) i.next();
			//if (!this.isCol(e.getName())) // 在这里判断是否是COL元素 如果不是则跳过
			if(!ConductUtil.isField(e.getName(),cols)) continue;

			sql = sql + e.getName();
			sql = sql + ",";
		}
		
		//判断是否有COL1字段 如果没有就加入到SQL字符串中去
		if (!existsCol)	sql = sql + ConfigOncb.COL1 + ",";

		int length = sql.length();

		sql = sql.substring(0, length - 1);
		sql = sql + ")values(" + repIdString + ",'" + typeString + "',";

		for (Iterator i = element.elementIterator(); i.hasNext();) {
			Element e = (Element) i.next();

			//if (!this.isCol(e.getName())) // 在这里判断是否是COL元素 如果不是则跳过
			if(!ConductUtil.isField(e.getName(),cols)) continue;

			String s = e.getStringValue();
			s = this.dataPoor(s); // 去处多余的字串

			sql = sql + "'" + s + "',";
		}

		//判断是否有COL1字段 如果没有就加入到SQL字符串中去
		if (!existsCol){
			sql = sql + "'" + 
				(element.getName().equalsIgnoreCase(ConfigOncb.DETAIL)==true?ConfigOncb.LABELBYLIST:ConfigOncb.TOTALLABEL) + 
				"',";
		}

		length = sql.length();

		sql = sql.substring(0, length - 1);
		sql = sql + ")";
		//// System.out.println("[ConductDataImpl1_2]sql:" + sql);
		return sql;
	}

	/**
	 * 根据输入的次数对detail元素进行第times次的扫描
	 * 
	 * @param element
	 *            一般是detail或total
	 * @param tableName
	 * @param existsCol
	 *            这里的existsCol其实没有什么用,因为detail肯定有 Col1主键字段
	 * @param repId
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public String getSqlStringByThreeDetail(Element element, String tableName,
			boolean existsCol, Integer repId, int time) throws Exception {

		int length;

		String repIdString = String.valueOf(repId.intValue());

		String sql = "insert into " + tableName + " (REP_IN_ID,";

		for (Iterator i = element.elementIterator(); i.hasNext();) { // element元素的名字是detail

			Element e = (Element) i.next();

			String colName = e.getName(); // 元素名

			if (!this.isCol(colName)) // 如果不是Col元素

				continue;

			int at = colName.indexOf('-');

			if (at == -1) { // 如果at等于- 1 说明这个元素是重复填写的 也就是说三次输入的值要一样的

				sql = sql + colName + ",";

			} else { // 如果at不等于-1 说明这个元素不是重复填写的

				String ts = colName.substring(at + 1);

				int t;

				try {

					t = Integer.parseInt(ts);

				} catch (Exception e1) {

					// System.out.println("生成SQL字符串时发生错误"); // ???????????

					throw new Exception(e1.toString());
				}

				if (t == time) { // 如果等于times 说明这一次扫描有这个元素

					String subColName = colName.substring(0, at);

					sql = sql + subColName + ",";
				}
			}
		}

		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")values(" + repIdString + ",";

		for (Iterator ii = element.elementIterator(); ii.hasNext();) { // 扫描Value赋值

			Element ee = (Element) ii.next();

			String colName2 = ee.getName(); // 元素名

			String colValue = ee.getStringValue(); // 得到元素值

			if (!this.isCol(colName2)) // 如果不是Col元素

				continue;

			int at2 = colName2.indexOf('-');

			if (at2 == -1) { // 如果at等于- 1 说明这个元素是重复填写的 也就是说三次输入的值要一样的

				sql = sql + "'" + colValue + "',";

			} else { // 如果at不等于-1 说明这个元素不是重复填写的

				String ts = colName2.substring(at2 + 1);

				int t;

				try {

					t = Integer.parseInt(ts);

				} catch (Exception e1) {

					// System.out.println("生成SQL字符串时发生错误"); // ???????????

					throw new Exception(e1.toString());
				}

				if (t == time) // 如果等于times 说明是这一次扫描有这个元素

					sql = sql + "'" + colValue + "',";
			}

		}

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

//	public ReportInForm getChecker(File file, String tableName, 
//			Integer repInId, String zipFileName, String xmlFileName) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public void updataReportData(File file, String tableName, 
//			Integer repInId, String zipFileName, String xmlFileName) throws Exception {
//		List sqls = new ArrayList();
//		Element fileRoot = XmlUtils.getRootElement(file); // 形成清单式XML文件的跟目录
//
//		Element element = null;
//
//		List cols=null;
//		ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repInId);
//		if(reportInForm!=null) cols=StrutsListingColsDelegate.findCols(reportInForm.getChildRepId(),reportInForm.getVersionId());
//		
//		for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i.hasNext();) {
//			Element root = (Element) fileRoot_i.next();
//			if (!root.getName().equals("Subform1")) // 判断第二层是否是Subform1
//				continue;
//
//			for (Iterator i = root.elementIterator(); i.hasNext();) {
//				element = (Element) i.next();
//				String name = element.getName();
//				name = ConductString.getStringNotSpace(name); // 去前后空格
//
//				if (this.isDetailNumberOrTotal(name) && !ChooseDetail.isNull(element)) {
//					//拿到插入数据库的SQL字符串
//					String sql = this.getSqlStringByTwo(element, tableName,this.existsCol1(element), repInId,cols); 
//					StrutsListingTableDelegate.insertRecord(sql, zipFileName,xmlFileName, repInId);
//
//					// sqls.add(sql);
//					// StrutsListingTableDelegate.insertRecords(sqls); // 插入数据库
//				}
//			}
//		}
//		
//	}
	public ReportInForm getChecker(File file, String tableName, Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void updataReportData(File file, String tableName, Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		// TODO Auto-generated method stub
		
	}

}