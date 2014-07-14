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

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cbrc.smis.adapter.StrutsListingColsDelegate;
import com.cbrc.smis.adapter.StrutsListingTableDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.ConductString;
import com.cbrc.smis.common.ConfigOncb;
import com.cbrc.smis.common.XmlUtils;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.util.FitechException;
/**
 * @author cb
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductDataImpl_3_G51 implements ConductListing {
	private FitechException log=new FitechException(ConductDataImpl_3_G51.class);
	
	/**
	 * XML数据元素名对应的数据表的列名
	 */
	private String[][] ELEMENTTOCOL={
		{"COL2","Col2"},
		{"COL3","Col3"},
		{"COL4","Col4"},
		{"COL5","Col5"},
		{"COL6","Col6"},
		{"COL7","Col7"},
		{"COL8","Col8"},
		{"COL10-1","Col9"},
		{"COL11-1","Col10"},
		{"COL12-1","Col11"},
		{"COL13-1","Col12"},
		{"COL14-1","Col13"},
		{"COL15-1","Col14"},
		{"COL16-1","Col15"},
		{"COL17-1","Col16"},
		{"COL18-1","Col17"},
		{"COL10-2","Col18"},
		{"COL11-2","Col19"},
		{"COL12-2","Col20"},
		{"COL13-2","Col21"},
		{"COL14-2","Col22"},
		{"COL15-2","Col23"},
		{"COL16-2","Col24"},
		{"COL17-2","Col25"},
		{"COL18-2","Col26"},
		{"COL10-3","Col27"},
		{"COL11-3","Col28"},
		{"COL12-3","Col29"},
		{"COL13-3","Col30"},
		{"COL14-3","Col31"},
		{"COL15-3","Col32"},
		{"COL16-3","Col33"},
		{"COL17-3","Col34"},
		{"COL18-3","Col35"},
		{"COL10-4","Col36"},
		{"COL11-4","Col37"},
		{"COL12-4","Col38"},
		{"COL13-4","Col39"},
		{"COL14-4","Col40"},
		{"COL15-4","Col41"},
		{"COL16-4","Col42"},
		{"COL17-4","Col43"},
		{"COL18-4","Col44"}
	};
	
	/**
	 * 根据XML元素的名称获取其对应的数据表的列名
	 *
	 * @author rds
	 * @date 2006-01-11
	 * 
	 * @param key String 键
	 * @return String 值
	 */
	private String getColName(String key){
		String colName="";
		if(key==null) return colName;
		
		try{
			for(int i=0;i<ELEMENTTOCOL.length;i++){
				if(ELEMENTTOCOL[i][0].toUpperCase().equals(key)){
					colName=ELEMENTTOCOL[i][1];
					break;
				}
			}
		}catch(Exception e){
			colName="";
			log.printStackTrace(e);
		}
		
		return colName;
	}
	
	/**
	 * 用于处理的第三种类型的清单式报表 G5100子报表
	 * 
	 * @param file File
	 * @param tableName String
	 * @param repId Integer
	 * @param zipFileName String
	 * @param xmlFileName String
	 * @return void
	 * @throws Exception
	 */
	public void conductData(File file, 
			String tableName, 
			Integer repId,
			String zipFileName, 
			String xmlFileName) throws Exception {
		List sqls = null;

		//Element fileRoot = XmlUtils.getRootElement(file); // 得到跟元素form1第一层

		//Element element = null;
		
		//for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i.hasNext();) {

			//element = (Element) fileRoot_i.next(); // 到了Subform1第二层
			/**
			 * 注意下面这一句的Element元素是处于的第二层即detail层的父层 第三层的布尔型参数在这里没有用,所以可以随便设
			 * 第四个参数在这里是对detail的扫描次数
			 */
			//sqls = this.getSqlStringByThree(element, tableName, false, repId, 4);
			//sqls.add(getSqlStringByDetail(element,tableName,repId));
			// StrutsListingTableDelegate.insertRecords(sqls,repId,zipFileName,xmlFileName);//
			// 插入数据库
			List cols=null;
			ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repId);
			if(reportInForm!=null) 
				cols=StrutsListingColsDelegate.findCols(reportInForm.getChildRepId(),reportInForm.getVersionId());
		
			SAXReader reader = new SAXReader();
	        Document document = reader.read(file);
			
			List nodes=document.selectNodes("//form1/Subform1/detail");
			if(nodes!=null && nodes.size()>0) sqls=new ArrayList();
			
			Iterator it=nodes.iterator();
			String sql="";
			int i=1;
			while(it.hasNext()){
				sql=getSqlStringByDetail((Element)it.next(),tableName,repId,i++,cols);
				if(sql!=null && !sql.equals("")) sqls.add(sql);
			}
			
			if(sqls!=null && sqls.size()>0){
				for (int m = 0; m < sqls.size(); m++) {	
					sql = (String) sqls.get(m);	
					StrutsListingTableDelegate.insertRecord(sql, zipFileName,xmlFileName, repId);
				}
			}
		//}
	}

	/**
	 * 根据输入的结点返回SQL字符串(用于第三种类型的清单式的报表s34_1与G52
	 * 由于要分别对detail元素按detail中的记录数进行扫描和对total扫描
	 * 所以该方法分别调用了getSqlStringByThreeDetail()和getSqlStringByThreeTotal()
	 * 
	 * @param element
	 *            元素名
	 * @param tableName
	 *            表名
	 * @param existsCol
	 *            布尔型变量 是否存在COL主键字段
	 * @param repId
	 * @param times
	 *            扫描的总次数
	 * @return
	 * @throws Exception
	 */
	public List getSqlStringByThree(Element element, String tableName,
			boolean existsCol, Integer repId, int times) throws Exception {

		List sqls = new ArrayList();

		String repIdString = String.valueOf(repId.intValue());

		String sql = ""; // SQL语句的中间字符串

		int length;

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next(); // 应该是detailh或total的一些元素

			String name = e.getName();

			if (name.equals(ConfigOncb.DETAIL) && !ChooseDetail.isNull(e)) { // 如果是detail元素就要分别对其进行times次的扫描
				for (int m = 1; m <= times; m++) {
					sql = this.getSqlStringByThreeDetail(e, tableName, this.existsCol1(e), repId, m);
					sqls.add(sql);
				}
			} else {
				if (name.equals(ConfigOncb.TOTAL) && !ChooseDetail.isNull(e)) {
					try {
						sql = this.getSqlStringByThreeTotal(e, tableName, this.existsCol1(e), repId); // 如果是total元素就调用getSqlStringByThreeTotal
					} catch (Exception ee) {
						continue;
					}
					sqls.add(sql);
				}
			}
		}

		return sqls;
	}
	
	/**
	 * 根据XML数据文件中的Detail数据域生成SQL语句
	 * 
	 * @author rds 
	 * @date 2006-01-11
	 * 
	 * @param element Element Detail数据域元素
	 * @param tableName String 数据表名称
	 * @param repInId Integer 实际子报表ID
	 * @param row int 行号
	 * @param cols List 数据表的列名列表
	 * @return String
	 */
	private String getSqlStringByDetail(Element element,String tableName,Integer repInId,int row,List cols){
		String sql="";
		if(element==null || tableName==null || repInId==null) return sql;
		if(tableName.equals("")) return sql;
		
		Iterator it=element.elementIterator();
		if(it==null) return sql;
		
		Element elem=null;
		String fields="",values="",field="",value="";
		while(it.hasNext()){
			elem=(Element)it.next();
			field=getColName(elem.getName());
			value=elem.getTextTrim();
			
			if(field.equalsIgnoreCase(ConfigOncb.TOTAL)){
				value=ConfigOncb.TOTALLABEL;
			}
			
			if(field==null) field="";
			if(value==null) value="";
			field=field.trim();
			value=value.trim();
			
			if(!ConductUtil.isField(field,cols)) continue;
			
			if(!field.equals("") && !value.equals("")){
				fields+=(fields.equals("")?"":",") + field;
				values+=(values.equals("")?"":",") + "'" + value + "'";
			}
		}
		
		if(!fields.equals("") && !values.equals("")) 
			sql="insert into " + tableName + "(rep_in_id,Col1," + fields + ") values(" + 
				repInId + ",'" + row + "'," + values + ")"; 
		
		//// System.out.println("[ConductDataImpl_3_G51]" + sql);	
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

			colValue = this.dataPoor(colValue); // 去处多余的字串

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

			s = this.dataPoor(s); // 去掉多余的字串

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

			f = Float.parseFloat(oldString); // 转成float 如果成功则说明是数字字符串

			newString = String.valueOf(f); // 如果不成功则说明是普通字符串

		} catch (Exception e) {

			newString = oldString;
		}

		return newString;
	}

	/**
	 * Main测试方法
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		File file=new File("c:\\tmp\\E001G51000510120057.xml");
		SAXReader reader = new SAXReader();
        Document document = reader.read(file);
		
		List nodes=document.selectNodes("//form1/Subform1/detail");
		// System.out.println("nodes.size:" + nodes.size());
		Iterator details=nodes.iterator();
		int i=1;
		while(details.hasNext()){
			// System.out.println("lkkkk");
			//new ConductDataImpl_3_G51().getSqlStringByDetail((Element)details.next(),"G51",new Integer(3999),i++);
		}		
	}
	
	public ReportInForm getChecker(File file, String tableName, Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void updataReportData(File file, String tableName, Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		// TODO Auto-generated method stub
		
	}
}