package com.fitech.gznx.graph;

import java.util.Map;
/**
 * 
 * ��˵��:��������ֱ�Ӷ�Ӧһ������ͼ��һ��������
 * @author jack
 * @date 2009-12-14
 */
public class BreakLineItem {

	private String label;

	/**
	 * key:X�� value:Y��
	 */
	private Map map;

	/**
	 * �����ļ����SQL
	 */
	private String baseSql;

	public String getBaseSql() {
		return baseSql;
	}

	public void setBaseSql(String baseSql) {
		this.baseSql = baseSql;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
