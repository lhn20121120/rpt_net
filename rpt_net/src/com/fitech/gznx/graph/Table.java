package com.fitech.gznx.graph;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class Table {
	
	/***�����Ͳ���ֵ��ӳ��Map****/
	public Map map = new HashMap();
	
	
	
	/**
	 * ��ʼ��map
	 *
	 */
	public static void initMap(GraphDataSource gs , Table tb){
		
		/***��ʼ��map****/
		
		Map tempMap = new HashMap();
		
		tempMap.put("ReptDate",gs.getReptDate());
		
		tempMap.put("OrgID",gs.getOrgId());
		
		tempMap.put("CCY",gs.getCcyId());
		
		tempMap.put("OrgPurview",gs.getOrgPurview());
		
		tempMap.put("Freq", String.valueOf(gs.getFreq()));
		
		tempMap.put("NotAllCCY","");
		
		tb.setMap(tempMap);
		
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
}
