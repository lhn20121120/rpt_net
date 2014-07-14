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
	 * XML����Ԫ������Ӧ�����ݱ������
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
	 * ����XMLԪ�ص����ƻ�ȡ���Ӧ�����ݱ������
	 *
	 * @author rds
	 * @date 2006-01-11
	 * 
	 * @param key String ��
	 * @return String ֵ
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
	 * ���ڴ���ĵ��������͵��嵥ʽ���� G5100�ӱ���
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

		//Element fileRoot = XmlUtils.getRootElement(file); // �õ���Ԫ��form1��һ��

		//Element element = null;
		
		//for (Iterator fileRoot_i = fileRoot.elementIterator(); fileRoot_i.hasNext();) {

			//element = (Element) fileRoot_i.next(); // ����Subform1�ڶ���
			/**
			 * ע��������һ���ElementԪ���Ǵ��ڵĵڶ��㼴detail��ĸ��� ������Ĳ����Ͳ���������û����,���Կ��������
			 * ���ĸ������������Ƕ�detail��ɨ�����
			 */
			//sqls = this.getSqlStringByThree(element, tableName, false, repId, 4);
			//sqls.add(getSqlStringByDetail(element,tableName,repId));
			// StrutsListingTableDelegate.insertRecords(sqls,repId,zipFileName,xmlFileName);//
			// �������ݿ�
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
	 * ��������Ľ�㷵��SQL�ַ���(���ڵ��������͵��嵥ʽ�ı���s34_1��G52
	 * ����Ҫ�ֱ��detailԪ�ذ�detail�еļ�¼������ɨ��Ͷ�totalɨ��
	 * ���Ը÷����ֱ������getSqlStringByThreeDetail()��getSqlStringByThreeTotal()
	 * 
	 * @param element
	 *            Ԫ����
	 * @param tableName
	 *            ����
	 * @param existsCol
	 *            �����ͱ��� �Ƿ����COL�����ֶ�
	 * @param repId
	 * @param times
	 *            ɨ����ܴ���
	 * @return
	 * @throws Exception
	 */
	public List getSqlStringByThree(Element element, String tableName,
			boolean existsCol, Integer repId, int times) throws Exception {

		List sqls = new ArrayList();

		String repIdString = String.valueOf(repId.intValue());

		String sql = ""; // SQL�����м��ַ���

		int length;

		for (Iterator i = element.elementIterator(); i.hasNext();) {

			Element e = (Element) i.next(); // Ӧ����detailh��total��һЩԪ��

			String name = e.getName();

			if (name.equals(ConfigOncb.DETAIL) && !ChooseDetail.isNull(e)) { // �����detailԪ�ؾ�Ҫ�ֱ�������times�ε�ɨ��
				for (int m = 1; m <= times; m++) {
					sql = this.getSqlStringByThreeDetail(e, tableName, this.existsCol1(e), repId, m);
					sqls.add(sql);
				}
			} else {
				if (name.equals(ConfigOncb.TOTAL) && !ChooseDetail.isNull(e)) {
					try {
						sql = this.getSqlStringByThreeTotal(e, tableName, this.existsCol1(e), repId); // �����totalԪ�ؾ͵���getSqlStringByThreeTotal
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
	 * ����XML�����ļ��е�Detail����������SQL���
	 * 
	 * @author rds 
	 * @date 2006-01-11
	 * 
	 * @param element Element Detail������Ԫ��
	 * @param tableName String ���ݱ�����
	 * @param repInId Integer ʵ���ӱ���ID
	 * @param row int �к�
	 * @param cols List ���ݱ�������б�
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

			colValue = this.dataPoor(colValue); // ȥ��������ִ�

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

			s = this.dataPoor(s); // ȥ��������ִ�

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

			f = Float.parseFloat(oldString); // ת��float ����ɹ���˵���������ַ���

			newString = String.valueOf(f); // ������ɹ���˵������ͨ�ַ���

		} catch (Exception e) {

			newString = oldString;
		}

		return newString;
	}

	/**
	 * Main���Է���
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