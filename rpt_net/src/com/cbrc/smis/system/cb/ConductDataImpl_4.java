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
 * �������ڶ�S38B���������⴦��
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

		if (!existsCol) // �ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ

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

		if (!existsCol){ // �ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ
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
	 * �÷����ô���S38���������͵��嵥����ķ���
	 */

	public void conductData(File file, String tableName, Integer repId,
			String zipFileName, String xmlFileName) throws Exception {

		Element fileRoot = XmlUtils.getRootElement(file); // �γ��嵥ʽXML�ļ��ĸ�Ŀ¼

		Element e1 = null;

		Element e2 = null;

		Element e3 = null;

		Element e4 = null;

		List sqls = new ArrayList();

		for (Iterator i = fileRoot.elementIterator(); i // �����һ��ѭ��,���ж��Ƿ���Subform1
				.hasNext();) {

			e1 = (Element) i.next();

			String name = e1.getName();

			if (!name.equals("Subform1"))

				continue;

			for (Iterator ii = e1.elementIterator(); ii.hasNext();) { // ����ڶ���ѭ��,���ж��Ƿ���detail��total

				e2 = (Element) ii.next();

				String elementName = e2.getName();

				elementName = ConductString.getStringNotSpace(elementName);

				// // System.out.println(elementName);

				if (elementName.equals(ConfigOncb.DETAIL) && !ChooseDetail.isNull(e2)) { // �ж������detail�����ѭ��

					for (Iterator iii = e2.elementIterator(); iii.hasNext();) { // ���������ѭ��,���ж��Ƿ���Subform1

						e3 = (Element) iii.next();

						if (e3.getName().equals("Subform1") && !ChooseDetail.isNull(e3)) {

							for (Iterator iiii = e3.elementIterator(); iiii
									.hasNext();) { // ������Ĳ�ѭ��,���ж��Ƿ���detail���ͷ�Ļ���total

								e4 = (Element) iiii.next();

								String eName = e4.getName();

								if (this.isAvailability(eName) && !ChooseDetail.isNull(e4)) {

									String sql = this.getSqlStringByFour(e4,
											tableName, this.existsCol1(e4),
											repId); // �õ��������ݿ��SQL�ַ���

									StrutsListingTableDelegate.insertRecord(
											sql, zipFileName, xmlFileName,
											repId);

								}
							}
						}
					}

				}
				if (elementName.equals(ConfigOncb.TOTAL)) {     //�����ж������TOTAL����Ե��õ�һ���嵥ʽ�ı����

					String sql_1 = this.getSqlStringByOne(e2, tableName, this
							.existsCol1(e2), repId);

					StrutsListingTableDelegate.insertRecord(sql_1, zipFileName,
							xmlFileName, repId);

				}

			}

			// StrutsListingTableDelegate.insertRecords(sqls); //�־û������ݿ�
		}

	}

	/**
	 * �÷������ڵ��������͵�����SQL�ַ���
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

		if (!existsCol) // �ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ

			sql = sql + ConfigOncb.COL1 + ",";

		int length = sql.length();

		sql = sql.substring(0, length - 1);

		sql = sql + ")values(" + repIdString + ",";

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next();

			if (!this.isCol(e.getName()))

				continue;

			String s = e.getStringValue();

			s = this.dataPoor(s); // ȥ��������ִ�

			sql = sql + "'" + s + "',";

		}

		if (!existsCol){ // �ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ
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
	 * ���ڵ���������S38�����е�COL�����ж�
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
	public ReportInForm getChecker(File file, String tableName, Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void updataReportData(File file, String tableName, Integer repInId, String zipFileName, String xmlFileName) throws Exception {
		// TODO Auto-generated method stub
		
	}
}