package com.fitech.gznx.graph;

import java.util.List;
import java.util.Map;

/**
 * 
 * 类说明:该类用于柱状图中的一个序列项
 * 
 * @author jack
 * @date 2009-12-12
 */
public class SerialByColumn {
	/**
	 * 序列项的名称
	 */
	private String label; // 机构名称

	/**
	 * 序列项的值
	 */
	private String value; // 机构ID
	/**
	 * 存放分类列表(考虑到存入Map中的分类当GET出来的时候是随机的,所以定义一个LIST来作定义先后顺序)
	 */
	private List list;

	/**
	 * key:分类名称 value:包含2个字符串的数组[0]:分类sql [1]：对应的值
	 */
	private Map map;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
    
}
