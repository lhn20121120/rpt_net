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
 * 该类用于对S34_1和G51进行入库处理
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductDataImpl_3 implements ConductListing {

	/**
	 * 用于处理的第三种类型的清单式报表
	 * 
	 * @param file
	 * @param tableName
	 * @param repId
	 * @throws Exception
	 */
	public void conductData(File file, String tableName, Integer repId,
			String zipFileName, String xmlFileName) throws Exception {

		List sqls = null;

		Element fileRoot = XmlUtils.getRootElement(file); // 得到跟元素form1第一层

		Element element = null;
		
		List cols=null;
		ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repId);
		if(reportInForm!=null) 
			cols=StrutsListingColsDelegate.findCols(reportInForm.getChildRepId(),reportInForm.getVersionId());
		
		for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i.hasNext();) {
			element = (Element) fileRoot_i.next(); // 到了Subform1第二层
			/**
			 * 注意下面这一句的Element元素是处于的第二层即detail层的父层 第三层的布尔型参数在这里没有用,所以可以随便设
			 * 第四个参数在这里是对detail的扫描次数
			 */
			sqls = this.getSqlStringByThree(element, tableName, false, repId, 3,cols);

			// StrutsListingTableDelegate.insertRecords(sqls,repId,zipFileName,xmlFileName);//
			// 在同一个事务中处理多个SQL字符串

			int size = sqls.size();

			for (int m = 0; m < size; m++) {
				String sql = (String) sqls.get(m);
				//// System.out.println("[ConductDataImpl1_3]sql:" + sql);
				StrutsListingTableDelegate.insertRecord(sql, zipFileName,xmlFileName, repId);
			}
		}
	}

	/**
	 * 根据输入的结点返回SQL字符串(用于第三种类型的清单式的报表s34_1与G52
	 * 由于要分别对detail元素按detail中的记录数进行扫描和对total扫描
	 * 所以该方法分别调用了getSqlStringByThreeDetail()和getSqlStringByThreeTotal()
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @param times  扫描的总次数
	 * @param cols List 数据表的列名列表
	 * @return
	 * @throws Exception
	 */
	public List getSqlStringByThree(Element element, 
			String tableName,
			boolean existsCol, 
			Integer repId, 
			int times,
			List cols) throws Exception {

		List sqls = new ArrayList();

		String repIdString = String.valueOf(repId.intValue());

		String sql = ""; // SQL语句的中间字符串

		int length;

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next(); // 应该是detailh或total的一些元素

			String name = e.getName();

			if (name.equals(ConfigOncb.DETAIL) && !ChooseDetail.isNull(e)) { // 如果是detail元素就要分别对其进行times次的扫描
				for (int m = 1; m <= times; m++) {
					//sql = this.getSqlStringByThreeDetail(e, tableName, this.existsCol1(e), repId, m,cols);
					sql = this.getSqlStringByThreeDetail(e, tableName, this.existsCol1(e), repId, m,cols);
					sqls.add(sql);
				}
			} else {
				if (name.equals(ConfigOncb.TOTAL) && !ChooseDetail.isNull(e)) {
					//如果是total元素就调用getSqlStringByThreeTotal
					sql = this.getSqlStringByThreeTotal(e, tableName, this.existsCol1(e), repId,cols);
					sqls.add(sql);
				}
			}
		}

		return sqls;

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
	 * @param cols List 数据表的列名称列表
	 * @return
	 * @throws Exception
	 */
	public String getSqlStringByThreeDetail(Element element, 
			String tableName,
			boolean existsCol, 
			Integer repId, 
			int time,
			List cols) throws Exception {

		int length;

		String repIdString = String.valueOf(repId.intValue());

		String sql = "insert into " + tableName + " (REP_IN_ID,";

		for (Iterator i = element.elementIterator(); i.hasNext();) { // element元素的名字是detail

			Element e = (Element) i.next();

			String colName = e.getName(); // 元素名

			//如果不是Col元素
			//if (!this.isCol(colName)) continue;
						
			int at = colName.indexOf('-');

			if (at == -1) { // 如果at等于- 1 说明这个元素是重复填写的 也就是说三次输入的值要一样的
				if(!ConductUtil.isField(colName,cols)) continue;
				//if(time==1) 
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
					if(!ConductUtil.isField(subColName,cols)) continue;
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

			colValue = this.dataPoor(colValue); // 去处多余的字串

			//如果不是Col元素
			//if (!this.isCol(colName2)) continue;
						
			int at2 = colName2.indexOf('-');

			if (at2 == -1) { // 如果at等于- 1 说明这个元素是重复填写的 也就是说三次输入的值要一样的
				if(!ConductUtil.isField(colName2,cols)) continue;
				//if(time==1) 
					sql = sql + "'" + colValue + "',";
			} else { // 如果at不等于-1 说明这个元素不是重复填写的
				if(!ConductUtil.isField(colName2.substring(0,at2),cols)) continue;
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
	 * 该方法属于处理第G51类型的清单式报表中的返回SQL字符串 中的处理total元素的子方法
	 * 
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @param cols List 数据表的列名称的列表
	 * @return String
	 * @throws Exception
	 */
	public String getSqlStringByThreeTotal(Element element, 
			String tableName,
			boolean existsCol, 
			Integer repId,
			List cols) throws Exception {
		String repIdString = String.valueOf(repId.intValue());

		String sql = "insert into " + tableName + " (REP_IN_ID,";

		Element e = null;

		int length,at;

		String colName="",subColName="";
		for (Iterator i = element.elementIterator(); i.hasNext();) {
			e = (Element) i.next();
			colName = e.getName();
			//在这里判断是否是COL元素
			//if (!this.isCol(colName)) continue;
			
			at = colName.indexOf('-');
			if(at>-1){
				subColName=colName.substring(0,at);
			}else{
				subColName=colName;
			}
			if(!ConductUtil.isField(subColName,cols)) continue;
			
			sql = sql + colName + ",";

		}
		if (!existsCol)

			sql = sql + ConfigOncb.COL1 + ","; // 如果没有

		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")values(" + repId + ",";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			e = (Element) i.next();

			colName = e.getName();

			at = colName.indexOf('-');
			if(at>=0){
				subColName=colName.substring(0,at);
			}else{
				subColName=colName;
			}
			//if (!this.isCol(colName)) continue;
			if(!ConductUtil.isField(subColName,cols)) continue;
			
			String s = e.getStringValue();

			s = this.dataPoor(s); // 去掉多余的字串

			sql = sql + "'" + s + "',";
		}

		if (!existsCol){ // 判断是否有COL1字段 如果没有就加入到SQL字符串中去
			//sql = sql + "'" + ConfigOncb.LABELBYLIST + "',";
			sql = sql + "'" + ConfigOncb.TOTALLABEL + "',";
		}

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