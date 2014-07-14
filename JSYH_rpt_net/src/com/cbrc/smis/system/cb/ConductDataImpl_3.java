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
 * �������ڶ�S34_1��G51������⴦��
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConductDataImpl_3 implements ConductListing {

	/**
	 * ���ڴ���ĵ��������͵��嵥ʽ����
	 * 
	 * @param file
	 * @param tableName
	 * @param repId
	 * @throws Exception
	 */
	public void conductData(File file, String tableName, Integer repId,
			String zipFileName, String xmlFileName) throws Exception {

		List sqls = null;

		Element fileRoot = XmlUtils.getRootElement(file); // �õ���Ԫ��form1��һ��

		Element element = null;
		
		List cols=null;
		ReportInForm reportInForm=StrutsReportInDelegate.getReportIn(repId);
		if(reportInForm!=null) 
			cols=StrutsListingColsDelegate.findCols(reportInForm.getChildRepId(),reportInForm.getVersionId());
		
		for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i.hasNext();) {
			element = (Element) fileRoot_i.next(); // ����Subform1�ڶ���
			/**
			 * ע��������һ���ElementԪ���Ǵ��ڵĵڶ��㼴detail��ĸ��� ������Ĳ����Ͳ���������û����,���Կ��������
			 * ���ĸ������������Ƕ�detail��ɨ�����
			 */
			sqls = this.getSqlStringByThree(element, tableName, false, repId, 3,cols);

			// StrutsListingTableDelegate.insertRecords(sqls,repId,zipFileName,xmlFileName);//
			// ��ͬһ�������д�����SQL�ַ���

			int size = sqls.size();

			for (int m = 0; m < size; m++) {
				String sql = (String) sqls.get(m);
				//// System.out.println("[ConductDataImpl1_3]sql:" + sql);
				StrutsListingTableDelegate.insertRecord(sql, zipFileName,xmlFileName, repId);
			}
		}
	}

	/**
	 * ��������Ľ�㷵��SQL�ַ���(���ڵ��������͵��嵥ʽ�ı���s34_1��G52
	 * ����Ҫ�ֱ��detailԪ�ذ�detail�еļ�¼������ɨ��Ͷ�totalɨ��
	 * ���Ը÷����ֱ������getSqlStringByThreeDetail()��getSqlStringByThreeTotal()
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @param times  ɨ����ܴ���
	 * @param cols List ���ݱ�������б�
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

		String sql = ""; // SQL�����м��ַ���

		int length;

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next(); // Ӧ����detailh��total��һЩԪ��

			String name = e.getName();

			if (name.equals(ConfigOncb.DETAIL) && !ChooseDetail.isNull(e)) { // �����detailԪ�ؾ�Ҫ�ֱ�������times�ε�ɨ��
				for (int m = 1; m <= times; m++) {
					//sql = this.getSqlStringByThreeDetail(e, tableName, this.existsCol1(e), repId, m,cols);
					sql = this.getSqlStringByThreeDetail(e, tableName, this.existsCol1(e), repId, m,cols);
					sqls.add(sql);
				}
			} else {
				if (name.equals(ConfigOncb.TOTAL) && !ChooseDetail.isNull(e)) {
					//�����totalԪ�ؾ͵���getSqlStringByThreeTotal
					sql = this.getSqlStringByThreeTotal(e, tableName, this.existsCol1(e), repId,cols);
					sqls.add(sql);
				}
			}
		}

		return sqls;

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
	 * @param cols List ���ݱ���������б�
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

		for (Iterator i = element.elementIterator(); i.hasNext();) { // elementԪ�ص�������detail

			Element e = (Element) i.next();

			String colName = e.getName(); // Ԫ����

			//�������ColԪ��
			//if (!this.isCol(colName)) continue;
						
			int at = colName.indexOf('-');

			if (at == -1) { // ���at����- 1 ˵�����Ԫ�����ظ���д�� Ҳ����˵���������ֵҪһ����
				if(!ConductUtil.isField(colName,cols)) continue;
				//if(time==1) 
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
					if(!ConductUtil.isField(subColName,cols)) continue;
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

			colValue = this.dataPoor(colValue); // ȥ��������ִ�

			//�������ColԪ��
			//if (!this.isCol(colName2)) continue;
						
			int at2 = colName2.indexOf('-');

			if (at2 == -1) { // ���at����- 1 ˵�����Ԫ�����ظ���д�� Ҳ����˵���������ֵҪһ����
				if(!ConductUtil.isField(colName2,cols)) continue;
				//if(time==1) 
					sql = sql + "'" + colValue + "',";
			} else { // ���at������-1 ˵�����Ԫ�ز����ظ���д��
				if(!ConductUtil.isField(colName2.substring(0,at2),cols)) continue;
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
	 * �÷������ڴ����G51���͵��嵥ʽ�����еķ���SQL�ַ��� �еĴ���totalԪ�ص��ӷ���
	 * 
	 * 
	 * @param element
	 * @param tableName
	 * @param existsCol
	 * @param repId
	 * @param cols List ���ݱ�������Ƶ��б�
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
			//�������ж��Ƿ���COLԪ��
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

			sql = sql + ConfigOncb.COL1 + ","; // ���û��

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

			s = this.dataPoor(s); // ȥ��������ִ�

			sql = sql + "'" + s + "',";
		}

		if (!existsCol){ // �ж��Ƿ���COL1�ֶ� ���û�оͼ��뵽SQL�ַ�����ȥ
			//sql = sql + "'" + ConfigOncb.LABELBYLIST + "',";
			sql = sql + "'" + ConfigOncb.TOTALLABEL + "',";
		}

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