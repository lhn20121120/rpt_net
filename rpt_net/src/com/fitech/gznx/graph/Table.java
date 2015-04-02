package com.fitech.gznx.graph;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class Table {
	
	/***参数和参数值的映射Map****/
	public Map map = new HashMap();
	
	
	
	/**
	 * 初始化map
	 *
	 */
	public static void initMap(GraphDataSource gs , Table tb){
		
		/***初始化map****/
		
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
