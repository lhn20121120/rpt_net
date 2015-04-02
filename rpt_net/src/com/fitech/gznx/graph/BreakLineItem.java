package com.fitech.gznx.graph;

import java.util.Map;
/**
 * 
 * 类说明:该类用于直接对应一个折线图的一个折线项
 * @author jack
 * @date 2009-12-14
 */
public class BreakLineItem {

	private String label;

	/**
	 * key:X轴 value:Y轴
	 */
	private Map map;

	/**
	 * 配置文件里的SQL
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
