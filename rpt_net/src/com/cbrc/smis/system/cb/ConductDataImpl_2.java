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
 * �������ڶ�S36,S3402,S3209�嵥ʽ�ı�����д���
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductDataImpl_2 implements ConductListing {

	/**
	 * ���ڴ���ڶ������͵��嵥ʽ���� s36,s37,s34_2
	 * 
	 * @param file
	 * @param tableName
	 * @param repId
	 * @throws Exception
	 */
	public void conductData(File file, String tableName, Integer repId,String zipFileName, String xmlFileName) throws Exception {
		List sqls = new ArrayList();
		Element fileRoot = XmlUtils.getRootElement(file); // �γ��嵥ʽXML�ļ��ĸ�Ŀ¼

		Element element = null;

		List cols=null;
		ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repId);
		if(reportInForm!=null) cols=StrutsListingColsDelegate.findCols(reportInForm.getChildRepId(),reportInForm.getVersionId());
		
		for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i.hasNext();) {
			Element root = (Element) fileRoot_i.next();
			if (!root.getName().equals("Subform1")) // �жϵڶ����Ƿ���Subform1
				continue;

			for (Iterator i = root.elementIterator(); i.hasNext();) {
				element = (Element) i.next();
				String name = element.getName();
				name = ConductString.getStringNotSpace(name); // ȥǰ��ո�

				if (this.isDetailNumberOrTotal(name) && !ChooseDetail.isNull(element)) {
					//�õ��������ݿ��SQL�ַ���
					String sql = this.getSqlStringByTwo(element, tableName,this.existsCol1(element), repId,cols); 
					StrutsListingTableDelegate.insertRecord(sql, zipFileName,xmlFileName, repId);

					// sqls.add(sql);
					// StrutsListingTableDelegate.insertRecords(sqls); // �������ݿ�
				}
			}
		}
	}

	/**
	 * �÷������ڶ�s36,s34_2��������detail5�ȱ�ǵĵڶ����Ե㱨�� ��detail5��totalԪ�ؽ��м���
	 * 
	 * @param s
	 * @return
	 */
	public boolean isDetailNumberOrTotal(String s) {

		boolean is = true; // Ĭ����

		int length = s.length();

		if (length < 5 || length > 8) // ���˵�С��total�����ִ���detailHeader

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
	 * ��������Ľ�㷵��SQL�ַ���(���ڵڶ������͵��嵥ʽ�ı���) s36,s37,s34_2
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @param cols List ��Ѱٷ���ת����С������
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
				typeString = name.substring(6); // �õ�typeֵ ����'detail2'�е�'2'
		} catch (Exception e) {
			typeString = "";
		}

		String repIdString = String.valueOf(repId.intValue());
		String sql = "insert into " + tableName + " (REP_IN_ID,TYPE,";
		for (Iterator i = element.elementIterator(); i.hasNext();) {
			Element e = (Element) i.next();
			//if (!this.isCol(e.getName())) // �������ж��Ƿ���COLԪ�� �������������
			if(!ConductUtil.isField(e.getName(),cols)) continue;

			sql = sql + e.getName();
			sql = sql + ",";
		}
		
		//�ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ
		if (!existsCol)	sql = sql + ConfigOncb.COL1 + ",";

		int length = sql.length();

		sql = sql.substring(0, length - 1);
		sql = sql + ")values(" + repIdString + ",'" + typeString + "',";

		for (Iterator i = element.elementIterator(); i.hasNext();) {
			Element e = (Element) i.next();

			//if (!this.isCol(e.getName())) // �������ж��Ƿ���COLԪ�� �������������
			if(!ConductUtil.isField(e.getName(),cols)) continue;

			String s = e.getStringValue();
			s = this.dataPoor(s); // ȥ��������ִ�

			sql = sql + "'" + s + "',";
		}

		//�ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ
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
	 * ��������Ĵ�����detailԪ�ؽ��е�times�ε�ɨ��
	 * 
	 * @param element
	 *            һ����detail��total
	 * @param tableName
	 * @param existsCol
	 *            �����existsCol��ʵû��ʲô��,��Ϊdetail�϶��� Col1�����ֶ�
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

		for (Iterator i = element.elementIterator(); i.hasNext();) { // elementԪ�ص�������detail

			Element e = (Element) i.next();

			String colName = e.getName(); // Ԫ����

			if (!this.isCol(colName)) // �������ColԪ��

				continue;

			int at = colName.indexOf('-');

			if (at == -1) { // ���at����- 1 ˵�����Ԫ�����ظ���д�� Ҳ����˵���������ֵҪһ����

				sql = sql + colName + ",";

			} else { // ���at������-1 ˵�����Ԫ�ز����ظ���д��

				String ts = colName.substring(at + 1);

				int t;

				try {

					t = Integer.parseInt(ts);

				} catch (Exception e1) {

					// System.out.println("����SQL�ַ���ʱ��������"); // ???????????

					throw new Exception(e1.toString());
				}

				if (t == time) { // �������times ˵����һ��ɨ�������Ԫ��

					String subColName = colName.substring(0, at);

					sql = sql + subColName + ",";
				}
			}
		}

		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")values(" + repIdString + ",";

		for (Iterator ii = element.elementIterator(); ii.hasNext();) { // ɨ��Value��ֵ

			Element ee = (Element) ii.next();

			String colName2 = ee.getName(); // Ԫ����

			String colValue = ee.getStringValue(); // �õ�Ԫ��ֵ

			if (!this.isCol(colName2)) // �������ColԪ��

				continue;

			int at2 = colName2.indexOf('-');

			if (at2 == -1) { // ���at����- 1 ˵�����Ԫ�����ظ���д�� Ҳ����˵���������ֵҪһ����

				sql = sql + "'" + colValue + "',";

			} else { // ���at������-1 ˵�����Ԫ�ز����ظ���д��

				String ts = colName2.substring(at2 + 1);

				int t;

				try {

					t = Integer.parseInt(ts);

				} catch (Exception e1) {

					// System.out.println("����SQL�ַ���ʱ��������"); // ???????????

					throw new Exception(e1.toString());
				}

				if (t == time) // �������times ˵������һ��ɨ�������Ԫ��

					sql = sql + "'" + colValue + "',";
			}

		}

		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")";

		return sql;
	}

	/**
	 * �÷������ڴ����S34_1,G51���͵��嵥ʽ�����еķ���SQL�ַ��� �еĴ���totalԪ�ص��ӷ���
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

			if (!this.isCol(colName)) // �������ж��Ƿ���COLԪ��

				continue;

			sql = sql + colName + ",";

		}
		if (!existsCol)

			sql = sql + ConfigOncb.COL1 + ","; // ���û��

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

		if (!existsCol) // �ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ

			sql = sql + "'" + ConfigOncb.LABELBYLIST + "',";

		length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")";

		return sql;

	}

	/**
	 * �÷�����������Ľ�㷵��һ��detail�еļ�¼�� ����Ľڵ�һ����detail �÷������� s34_1,G51 ����
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

				mid = Integer.parseInt(sub); // �õ�һ��COL�������

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
	 * �ж��Ƿ����Col1�����ֶ�
	 * 
	 * @param element
	 * @return
	 */
	public boolean existsCol1(Element element) throws Exception {

		boolean exists = false; // Ĭ���ǲ�����

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
	 * �ж��Ƿ���COLԪ��
	 * 
	 * @param col
	 * @return ������򷵻�true ���򷵻�false
	 * @throws Exception
	 */
	public boolean isCol(String col) throws Exception {

		if (col.length() < 4)

			return false;

		if (col.equals("COL0")) // ���������"COL0"��ֱ���˳� ��Ϊ���б���û�и��ֶ�

			return false;

		boolean is = true; // ��ʼ����COLԪ��

		String s = col.substring(0, 3);

		if (!s.equals("COL"))

			is = false;

		return is;
	}

	/**
	 * �÷��������ж��Ƿ���COLԪ��
	 * 
	 * @param col
	 *            Sting
	 * @return ��������� ����COLԪ�� ���򷵻ؼ�
	 * @throws Exception
	 */
	public boolean isColByTwo(String col) throws Exception {

		boolean is = true; // ��ʼ����COL�ֶ�

		String s = col.substring(0, 3);

		if (!s.equals("COL"))

			return false; // ���ͷ�����ַ�����COL��ֱ�ӷ��ؼ�

		String ss = col.substring(3);

		try {

			int i = Integer.parseInt(ss); // ���������ַ����������򷵻ؼ�

		} catch (Exception e) {

			is = false;
		}

		return is;
	}

	/**
	 * ��������Ľ��ƴ��һ��XML�ļ���
	 * 
	 * @param e
	 * @return String XML�ļ���
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
	 * ��������Ľ��Ԫ���ҳ��ý��Ԫ�ص���Ԫ�ض�Ӧ��ֵ
	 * 
	 * @param element
	 *            ����Ľڵ�
	 * @param elementName
	 *            �������Ԫ����
	 * @return �ַ���
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
	 * �÷������ڷ���XML�ļ������� ?????????
	 * 
	 * @param file
	 * @return String XML�ļ�������
	 * @throws Exception
	 */
	public String getListingType(File file) throws Exception {

		String type = ConfigOncb.TYPE1;

		return type;
	}

	/**
	 * ȥ���ַ����е���Ч�ַ� ����������ַ�����ȥ��������� ����ǲ��������ַ��������κδ���
	 * 
	 * @param oldString
	 * @return
	 */
	public String dataPoor(String oldString) {

		String newString = "";

		float f = 0;

		try {
			newString=oldString==null?"":oldString.trim();
			//f = Float.parseFloat(oldString); // ת��float ����ɹ���˵���������ַ���

			//newString = String.valueOf(f); // ������ɹ���˵������ͨ�ַ���

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
//		Element fileRoot = XmlUtils.getRootElement(file); // �γ��嵥ʽXML�ļ��ĸ�Ŀ¼
//
//		Element element = null;
//
//		List cols=null;
//		ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repInId);
//		if(reportInForm!=null) cols=StrutsListingColsDelegate.findCols(reportInForm.getChildRepId(),reportInForm.getVersionId());
//		
//		for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i.hasNext();) {
//			Element root = (Element) fileRoot_i.next();
//			if (!root.getName().equals("Subform1")) // �жϵڶ����Ƿ���Subform1
//				continue;
//
//			for (Iterator i = root.elementIterator(); i.hasNext();) {
//				element = (Element) i.next();
//				String name = element.getName();
//				name = ConductString.getStringNotSpace(name); // ȥǰ��ո�
//
//				if (this.isDetailNumberOrTotal(name) && !ChooseDetail.isNull(element)) {
//					//�õ��������ݿ��SQL�ַ���
//					String sql = this.getSqlStringByTwo(element, tableName,this.existsCol1(element), repInId,cols); 
//					StrutsListingTableDelegate.insertRecord(sql, zipFileName,xmlFileName, repInId);
//
//					// sqls.add(sql);
//					// StrutsListingTableDelegate.insertRecords(sqls); // �������ݿ�
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