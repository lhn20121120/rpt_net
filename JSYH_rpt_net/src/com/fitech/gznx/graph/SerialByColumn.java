package com.fitech.gznx.graph;

import java.util.List;
import java.util.Map;

/**
 * 
 * ��˵��:����������״ͼ�е�һ��������
 * 
 * @author jack
 * @date 2009-12-12
 */
public class SerialByColumn {
	/**
	 * �����������
	 */
	private String label; // ��������

	/**
	 * �������ֵ
	 */
	private String value; // ����ID
	/**
	 * ��ŷ����б�(���ǵ�����Map�еķ��൱GET������ʱ���������,���Զ���һ��LIST���������Ⱥ�˳��)
	 */
	private List list;

	/**
	 * key:�������� value:����2���ַ���������[0]:����sql [1]����Ӧ��ֵ
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
