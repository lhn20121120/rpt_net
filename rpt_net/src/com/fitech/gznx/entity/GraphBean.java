package com.fitech.gznx.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import com.fitech.gznx.entity.SerialByColumn;
import com.fitech.gznx.util.FitechUtil;
import com.fitech.gznx.util.DateUtil;

/**
 * 
 * ��˵��:���ඨ��һ������ͼ��չʾ�õ�Bean,��Ӧһ��ͼƬ
 * 
 * @author jack
 * @date 2009-12-11
 */
public class GraphBean {

	/**
	 * ��
	 */
	public static String TABLEPATH = "0";
	/**
	 * ��״ͼ
	 */
	public static String COLUMNGRAPH = "1";

	/**
	 * ��״ͼ
	 */
	public static String PIEGRAPH = "2";

	/**
	 * ����ͼ
	 */
	public static String BREAKLINE = "3";

	/**
	 * ָ�����
	 */
	public static String MEASURE_ALERT = "measure_alert";
 
	/**
	 * ��Ŀ����
	 */
	public static String SUBJECT_TOTAL = "subject_total";

	/**
	 * ͼƬ��Ϣ��XML�ļ��е�����ID
	 */
	private String graphId;

	/**
	 * ͼƬ�����ļ�������
	 */
	private String graphStoreName;

	/**
	 * ͼƬ��������
	 */
	private String graphType;

	/**
	 * ͼ�δ����
	 */
	private String graphTitle;

	/**
	 * ͼƬ�Ŀ��
	 */
	private int graphWidth;

	/**
	 * ͼƬ�ĸ߶�
	 */
	private int graphHeigth;

	/**
	 * �Ƿ���3D
	 */
	private String graphSolid;

	/**
	 * ����������
	 */
	private String alabelTitle;

	/**
	 * �����굥λ
	 */
	private String alableUnit;

	/**
	 * ����������
	 */
	private String vlabelTitle;

	/**
	 * �����굥λ
	 */
	private String vlabelUnit;

	/**
	 * ҵ������б�(�ڲ�װ�ص���ҵ���������)
	 */
	private List itemList;

	/**
	 * ʱ���б�
	 */
	private List repDateList;

	/**
	 * �����б�,�����ŵĶ�����ͬ��ͼ�ζ���ͬ ��״ͼ:�����ŵ���һ������Ϊ3���ַ�������(�����ǩ,����ֵ,baseSql)
	 * ����ͼ:�����ŵ���һ��BreakLineItem�б� ��״ͼ:�����ŵ�ʱSerialByColumn�б�
	 * 
	 */
	private List paramList;

	/**
	 * ��������������ʱ�Ƿ�������л���
	 */
	private String isContailMe;

	/**
	 * ��������
	 */
	private int typeCount = 0;
	
	/**
	 * ��Ĳ���
	 */
	public  static String[] argumentNames = null;
	/**
	 * ��Ĳ�����ֵ
	 */
	public  static String[] argumentValues = null;

	public int getGraphHeigth() {
		return graphHeigth;
	}

	public void setGraphHeigth(int graphHeigth) {
		this.graphHeigth = graphHeigth;
	}

	public int getGraphWidth() {
		return graphWidth;
	}

	public void setGraphWidth(int graphWidth) {
		this.graphWidth = graphWidth;
	}

	/**
	 * 
	 * ����˵��:�÷������ڴ���ʱ��
	 * 
	 * @author jack
	 * @date 2009-12-10
	 * @param repDate
	 */
	public void conductRepDate(String repDate) {

		this.repDateList = null;

		this.repDateList = new ArrayList();

		if (this.graphType.equals(GraphBean.PIEGRAPH)
				|| this.graphType.equals(GraphBean.COLUMNGRAPH)) // ����Ǳ�״ͼ��ֱ�ӽ�ʱ������б��еĵ�һ��λ��

			this.repDateList.add(repDate);

		if (this.graphType.equals(GraphBean.BREAKLINE)) {// ���ʱ����ͼ��������������һ��ʱ���б�

			String startDate = "";

			String endDate = "";

			if (DateUtil.isMonthDay(repDate)) { // ��������ʱ��ʱ��ĩʱ��

				startDate = DateUtil.getLYSameDay(repDate);

				endDate = repDate;

				this.repDateList = DateUtil.getBetweenDate(startDate, endDate);

				this.repDateList.add(endDate);

			} else {

				endDate = DateUtil.getLastMonth(repDate);

				startDate = DateUtil.getLYSameDay(endDate);

				this.repDateList = DateUtil.getBetweenDate(startDate, endDate);

				this.repDateList.add(endDate);
			}

		}

	}

	/**
	 * 
	 * ����˵��:�÷������ڽ�ʱ���б�ת����һ���ö���ָ���ַ���
	 * 
	 * @author jack
	 * @date 2009-12-10
	 * @return
	 */
	public String getRepDates() {

		StringBuffer sb = new StringBuffer("");

		String result = null;

		if (this.repDateList != null && this.repDateList.size() != 0) {

			sb.append((String) this.repDateList.get(0) + "'");

			for (int i = 1; i < this.repDateList.size(); i++) {

				if (i != this.repDateList.size() - 1)

					sb.append("," + "'" + this.repDateList.get(i) + "'");

				else

					sb.append("," + "'" + this.repDateList.get(i));
			}

			result = sb.toString();

		} else

			result = "'1906-12-31'";

		return result;

	}

	/**
	 * ���췽��
	 * 
	 * @param element
	 * @param orgId
	 * @param ccyId
	 * @param repDate
	 */
	public GraphBean(Element element, String orgId, String orgName,
			String ccyId, String repDate) {

		this.paramList = new ArrayList(); // ��ʼ�������б�

		this.graphId = element.attributeValue("id");

		this.graphType = element.attributeValue("type");

		this.graphSolid = element.attributeValue("solid");

		this.graphTitle = element.attributeValue("title");

		try {

			this.graphWidth = Integer.parseInt(element.attributeValue("width")); // ���ÿ��

		} catch (Exception e) {

			this.graphWidth = 350;
		}
		try {

			this.graphHeigth = Integer.parseInt(element
					.attributeValue("height"));

		} catch (Exception e) {

			this.graphHeigth = 300;
		}

		this.conductRepDate(repDate); // ����ʱ���б�
		
		if(this.graphType.equals(GraphBean.TABLEPATH)) //����Ǳ�
			
			this.initTable(element);

		if (this.graphType.equals(GraphBean.PIEGRAPH)) // ����Ǳ�״ͼ

			this.initPie(element, orgId, ccyId, repDate); // ����XML��Ϣ

		if (this.graphType.equals(GraphBean.BREAKLINE)) // ���������ͼ

			this.initBeakLine(element, orgId, ccyId, repDate); // ��������ͼ

		if (this.graphType.equals(GraphBean.COLUMNGRAPH))

			this.initColumn(element, orgId, orgName, ccyId, repDate); // ������״ͼ

	}

	/**
	 * 
	 * ����˵��:�÷������ڶԱ�״ͼ���н�����ʼ��
	 * 
	 * @author jack
	 * @date 2009-12-10
	 * @param Element
	 */
	public void initPie(Element element, String orgId, String ccyId,
			String repDate) {

		this.paramList = null;

		this.paramList = new ArrayList();

		List list = element.selectNodes("dataset/item");

		if (list != null && list.size() != 0) {

			for (int i = 0; i < list.size(); i++) {

				Element e = (Element) list.get(i);

				String datasetBaseSql = e.attributeValue("sql"); // �õ�XML�ļ��еĲ�ѯSQL

				String[] params = new String[3];

				params[0] = e.attributeValue("label");

				params[1] = "0";

				params[2] = datasetBaseSql;

				this.paramList.add(params);

			}
			this.conductPie(orgId, ccyId); // ��ѯȡ��
		}

	}

	/**
	 * 
	 * ����˵��:�÷������ڶ�����ͼ���н�����ʼ��
	 * 
	 * @author jack
	 * @date 2009-12-10
	 * @param element
	 * @param orgId
	 * @param ccyId
	 * @param repDate
	 */
	public void initBeakLine(Element element, String orgId, String ccyId,
			String repDate) {

		List list = element.selectNodes("alabel");

		String str = ((Element) list.get(0)).attributeValue("title");

		this.setAlabelTitle(str);

		list = element.selectNodes("vlabel");

		str = ((Element) list.get(0)).attributeValue("title");

		this.setVlabelTitle(str);

		this.paramList = null;

		this.paramList = new ArrayList();

		list = element.selectNodes("dataset/item");

		if (list != null && list.size() != 0) {

			for (int i = 0; i < list.size(); i++) {

				Element e = (Element) list.get(i);

				String datasetBaseSql = e.attributeValue("sql"); // �õ�XML�ļ��еĲ�ѯSQL

				BreakLineItem breakLineItem = new BreakLineItem();

				breakLineItem.setBaseSql(datasetBaseSql);

				breakLineItem.setLabel(e.attributeValue("label"));

				this.paramList.add(breakLineItem);

			}
			this.conductBreakLine(orgId, ccyId); // ��ѯȡ��
		}
	}

	/**
	 * 
	 * ����˵��:�÷���������״ͼ�ĳ�ʼ��
	 * 
	 * @author jack
	 * @date 2009-12-13
	 * @param element
	 * @param orgId
	 * @param ccyId
	 * @param repDate
	 */
	public void initColumn(Element element, String orgId, String orgName,
			String ccyId, String repDate) {

		List list = element.selectNodes("vlabel");

		String str = ((Element) list.get(0)).attributeValue("title");

		this.setVlabelTitle(str);

		list = element.selectNodes("dataset");

		str = ((Element) list.get(0)).attributeValue("isContailMe");

		this.setIsContailMe(str);

		this.paramList = null;

		this.paramList = new ArrayList();

		if (this.isContailMe.equals("y")) {

			SerialByColumn sbc = new SerialByColumn();

			sbc.setLabel(orgName);

			sbc.setValue(orgId);

			this.paramList.add(sbc);

		}

		list = element.selectNodes("dataset/serial");

		if (list != null && list.size() != 0) {

			for (int i = 0; i < list.size(); i++) {

				Element e = (Element) list.get(i);

				SerialByColumn sbc = new SerialByColumn();

				sbc.setLabel(e.attributeValue("label"));

				sbc.setLabel(e.attributeValue("value"));

				this.paramList.add(sbc);
			}
		}

		list = element.selectNodes("dataset/item");

		this.typeCount = list.size(); // ��ʼ������

		if (list != null && list.size() != 0) {

			for (int i = 0; i < this.paramList.size(); i++) {

				Map map = new HashMap();

				List lis = new ArrayList();

				for (int j = 0; j < list.size(); j++) {

					Element e = (Element) list.get(j);

					String[] strs = new String[2];

					strs[0] = e.attributeValue("sql");

					strs[1] = e.attributeValue("0");

					map.put(e.attributeValue("label").toString(), strs);

					lis.add(e.attributeValue("label").toString());

					System.out.println((String) lis.get(j));
				}

				SerialByColumn sbc = (SerialByColumn) this.paramList.get(i);

				sbc.setMap(map);

				sbc.setList(lis);

			}

			this.conductColumn(orgId, ccyId); // ��ѯȡ��
		}

	}

	/**
	 * 
	 * ����˵��:�÷������ڴ����ͼ��ѯ
	 * 
	 * @author jack
	 * @date 2009-12-7
	 * @param orgId
	 * @param ccyId
	 */
	public void conductPie(String orgId, String ccyId) {

		for (int i = 0; i < this.paramList.size(); i++) {

			String[] params = (String[]) this.paramList.get(i);

			String sql = this.getSql(params[2], orgId, ccyId,
					(String) this.repDateList.get(0));

			List tmp = FitechUtil.jdbcQueryUtil(sql);

			if (tmp != null && tmp.size() != 0) {

				params[1] = ((Double) tmp.get(0)).toString(); // ������

			}

			else

				params[1] = "0"; // ����鲻������ϵͳ

		}

	}

	/**
	 * 
	 * ����˵��:�÷�����������ͼ�Ĳ�ѯ
	 * 
	 * @author jack
	 * @date 2009-12-13
	 * @param orgId
	 * @param ccyId
	 */
	public void conductBreakLine(String orgId, String ccyId) {

		for (int i = 0; i < this.paramList.size(); i++) {

			BreakLineItem item = (BreakLineItem) paramList.get(i);

			String sql = this.getSql(item.getBaseSql(), orgId, ccyId, this
					.getRepDates());

			// System.out.println(sql);

			item.setMap(FitechUtil.jdbcQueryUtilOfMap(sql));

		}
	}

	/**
	 * 
	 * ����˵��:�÷�������
	 * 
	 * @author jack
	 * @date 2009-12-14
	 * @param orgId
	 * @param ccyId
	 */
	public void conductColumn(String orgId, String ccyId) {

		for (int i = 0; i < this.paramList.size(); i++) {

			SerialByColumn sbc = (SerialByColumn) this.paramList.get(i);

			Map map = sbc.getMap();

			Iterator iterator = map.keySet().iterator();

			Map hm = new HashMap();

			while (iterator.hasNext()) {

				String label = (String) iterator.next();

				String[] strs = (String[]) map.get(label);

				String sql = this.getSql(strs[0], orgId, ccyId,
						(String) this.repDateList.get(0));

				List tmp = FitechUtil.jdbcQueryUtil(sql);

				if (tmp != null && tmp.size() != 0) {

					strs[1] = ((Double) tmp.get(0)).toString(); // ������

				}

				else

					strs[1] = "0"; // ����鲻������ϵͳ

				hm.put(label, strs); // ������map

			}
			sbc.setMap(hm); // ����map����
		}
	}
	
	/**
	 * 
	 * @param element
	 * @param orgId
	 * @param orgName
	 * @param ccyId
	 * @param repDate
	 */
	public void initTable(Element element) {

		List list = element.selectNodes("dataset");

		Element items = (Element) list.get(0);

		list = items.selectNodes("item");
		
		argumentNames = new String[list.size()];
		
		argumentValues = new String[list.size()];

		if (list != null && list.size() != 0) {

			for (int i = 0; i < list.size(); i++) {

				Element e = (Element) list.get(i);

				String argumentName = e.attributeValue("label");
				
				argumentNames[i] = argumentName; 

				String argumentValue = e.attributeValue("value");
				
				argumentValues[i] = argumentValue;

			}
			
			
		}


	}


	/**
	 * 
	 * ����˵��:�÷�������ͳһ����SQL����װ(���͸��Ӷ�),�˷�������
	 * 
	 * @author jack
	 * @date 2009-12-7
	 * @param datasetBaseSql
	 * @param orgId
	 * @param ccyId
	 * @param repDateList
	 * @return
	 */
	public String getSql(String datasetBaseSql, String orgId, String ccyId,
			String repDate) {

		String sql = datasetBaseSql;

		sql = sql.replaceAll("@OrgID@", "'" + orgId + "'");

//		sql = sql.replaceAll("@ccyId@", "'" + ccyId + "'");

		sql = sql.replaceAll("@ReptDate@", "'" + repDate.replaceAll("-", "") + "'");

		return sql.toString();
	}

	/**
	 * 
	 * ����˵��:�÷������ڶԸ���ͼ�ν��е��ȴ���
	 * 
	 * @author jack
	 * @date 2009-12-13
	 * @param orgId
	 * @param ccyId
	 */
	public void conductGraphBean(String orgId, String ccyId, String repDate) {

		this.conductRepDate(repDate);

		if (this.graphType.equals(GraphBean.PIEGRAPH)) // ����Ǳ�״ͼ

			this.conductPie(orgId, ccyId); // װ��SQL�ﲢ��ѯ���ݿ�õ����

		if (this.graphType.equals(GraphBean.BREAKLINE))

			this.conductBreakLine(orgId, ccyId);
		
		if(this.graphType.equals(GraphBean.COLUMNGRAPH))
			
			this.conductColumn(orgId,ccyId);

	}

	public static String getBREAKLINE() {
		return BREAKLINE;
	}

	public static void setBREAKLINE(String breakline) {
		BREAKLINE = breakline;
	}

	public static String getCOLUMNGRAPH() {
		return COLUMNGRAPH;
	}

	public static void setCOLUMNGRAPH(String columngraph) {
		COLUMNGRAPH = columngraph;
	}

	public static String getPIEGRAPH() {
		return PIEGRAPH;
	}

	public static void setPIEGRAPH(String piegraph) {
		PIEGRAPH = piegraph;
	}

	public String getAlabelTitle() {
		return alabelTitle;
	}

	public void setAlabelTitle(String alabelTitle) {
		this.alabelTitle = alabelTitle;
	}

	public String getAlableUnit() {
		return alableUnit;
	}

	public void setAlableUnit(String alableUnit) {
		this.alableUnit = alableUnit;
	}

	public String getGraphId() {
		return graphId;
	}

	public void setGraphId(String graphId) {
		this.graphId = graphId;
	}

	public String getGraphSolid() {
		return graphSolid;
	}

	public void setGraphSolid(String graphSolid) {
		this.graphSolid = graphSolid;
	}

	public String getGraphTitle() {
		return graphTitle;
	}

	public void setGraphTitle(String graphTitle) {
		this.graphTitle = graphTitle;
	}

	public String getGraphType() {
		return graphType;
	}

	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}

	public String getVlabelTitle() {
		return vlabelTitle;
	}

	public void setVlabelTitle(String vlabelTitle) {
		this.vlabelTitle = vlabelTitle;
	}

	public String getVlabelUnit() {
		return vlabelUnit;
	}

	public void setVlabelUnit(String vlabelUnit) {
		this.vlabelUnit = vlabelUnit;
	}

	public String getGraphStoreName() {
		return graphStoreName;
	}

	public void setGraphStoreName(String graphStoreName) {
		this.graphStoreName = graphStoreName;
	}

	public List getItemList() {
		return itemList;
	}

	public void setItemList(List itemList) {
		this.itemList = itemList;
	}

	public List getRepDateList() {
		return repDateList;
	}

	public void setRepDateList(List repDateList) {
		this.repDateList = repDateList;
	}

	public List getParamList() {
		return paramList;
	}

	public void setParamList(List paramList) {
		this.paramList = paramList;
	}

	public String getIsContailMe() {
		return isContailMe;
	}

	public void setIsContailMe(String isContailMe) {
		this.isContailMe = isContailMe;
	}

	public int getTypeCount() {
		return typeCount;
	}

	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}

	public String[] getArgumentNames() {
		return argumentNames;
	}

	public void setArgumentNames(String[] argumentNames) {
		this.argumentNames = argumentNames;
	}

}
