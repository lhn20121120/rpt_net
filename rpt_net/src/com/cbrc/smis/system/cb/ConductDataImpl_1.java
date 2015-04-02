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
	 * �÷������ڴ����һ�����͵��嵥ʽ����
	 * s32_1,s32_2,s32_3,s32_4,s32_5,s32_6,s32_7,s32_9,s33,s35
	 * 
	 * @param file
	 */
	public void conductData(File file, String tableName, Integer repId,
			String zipFileName, String xmlFileName) throws Exception {

		Element fileRoot = XmlUtils.getRootElement(file); // �γ��嵥ʽXML�ļ��ĸ�Ŀ¼

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
				// �ж��Ƿ���detial��total
				if ((elementName.equals(ConfigOncb.DETAIL)
						|| elementName.equals(ConfigOncb.TOTAL))
						&& !ChooseDetail.isNull(element)) {
					String sql = this.getSqlStringByOne(element, tableName,
							this.existsCol1(element), repId, cols); // �õ��������ݿ��SQL�ַ���
					StrutsListingTableDelegate.insertRecord(sql, zipFileName,
							xmlFileName, repId);

					// sqls.add(sql);
					// // System.out.println(sql);
				}
			}

			// StrutsListingTableDelegate.insertRecords(sqls); //�־û������ݿ�
		}

	}
	
	public void updataReportData(File file, String tableName, Integer repInId, 
			String zipFileName, String xmlFileName) throws Exception {

		Element fileRoot = XmlUtils.getRootElement(file); // �γ��嵥ʽXML�ļ��ĸ�Ŀ¼

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
				// �ж��Ƿ���detial��total
				if ((elementName.equals(ConfigOncb.DETAIL)
						|| elementName.equals(ConfigOncb.TOTAL))
						&& !ChooseDetail.isNull(element)) {
					String sql = this.getUpdateSqlStringByOne(element, tableName,
							this.existsCol1(element), repInId, cols); // �õ��������ݿ��SQL�ַ���
					if(sql.equals("")) continue;
					StrutsListingTableDelegate.insertRecord(sql, zipFileName,
							xmlFileName, repInId);
				}
			}
		}

	}

	/**
	 * ��������Ľ�㷵��SQL�ַ���(���ڵ�һ�����͵��嵥ʽ�ı���)
	 * s32_1,s32_2,s32_3,s32_4,s32_5,s32_6,s32_7,s32_9,s33,s35
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @param ���ݱ�������б�
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

		// �ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ
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

			s = this.dataPoor(s); // ȥ��������ִ�
			sql = sql + "'" + s + "',";
		}

		// �ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ
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
	 * �÷������ڶ�s36,s37,s34_2��������detail5�ȱ�ǵĵڶ����Ե㱨�� ��detail5��totalԪ�ؽ��м���
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
	 * ��������Ľ�㷵��SQL�ַ���(���ڵڶ������͵��嵥ʽ�ı���) s36,s37,s34_2
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

		String typeString = name.substring(6); // �õ�typeֵ ����'detail2'�е�'2'

		String repIdString = String.valueOf(repId.intValue());

		String sql = "insert into " + tableName + " (REP_IN_ID,TYPE,";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (!this.isCol(e.getName())) // �������ж��Ƿ���COLԪ�� �������������

				continue;

			sql = sql + e.getName();

			sql = sql + ",";
		}

		if (!existsCol) // �ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ

			sql = sql + ConfigOncb.COL1 + ",";

		int length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")values(" + repIdString + ",'" + typeString + "',";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (!this.isCol(e.getName())) // �������ж��Ƿ���COLԪ�� �������������

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

	public void conductByFourList(File file, String tableName, Integer repId)
			throws Exception {

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

		if (col.toUpperCase().equals("COL0")) // ���������"COL0"��ֱ���˳� ��Ϊ���б���û�и��ֶ�

			return false;

		boolean is = true; // ��ʼ����COLԪ��

		String s = col.substring(0, 3);

		if (!s.toUpperCase().equals("COL"))

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
	 * ��������һ��ZIP�ļ���XML�ļ�����
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
		if (oldString == null)
			oldString = "";

		String newString = "";

		float f = 0;

		try {
			return oldString.trim();
			// f = Float.parseFloat(oldString); // ת��float ����ɹ���˵���������ַ���

			// newString = String.valueOf(f); // ������ɹ���˵������ͨ�ַ���

		} catch (Exception e) {

			newString = oldString;
		}

		return newString;
	}

	public ReportInForm getChecker(File file, String tableName, 
			Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		
		Element fileRoot = XmlUtils.getRootElement(file); // �γ��嵥ʽXML�ļ��ĸ�Ŀ¼
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
				// �ж��Ƿ���detial��total
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
