package com.fitech.gznx.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.fitech.gznx.common.Config;
import com.fitech.gznx.po.AfOrg;

/**
 * org处理
 * 
 * @author YUVAIOXP
 *
 */
public class OrgDisposal {
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public List<AfOrg> orgSort(List<AfOrg> list){
		
		List<AfOrg> lists = null;
		Map map = new HashMap();
		List vaf = null;//new Vector();
		
		int level = 0;
		String levels = "";
		
		//先查出需要几层
		for(Iterator<AfOrg> iter=lists.iterator();iter.hasNext();){
			
			AfOrg org = iter.next();
			
			if(levels.indexOf(org.getOrgLevel().toString())==-1) 
				levels += org.getOrgLevel().toString() + ",";
				level++;
		}
		
		if(levels.indexOf(Config.COLLECT_ORG_PARENT_ID)!=-1) 
			level--;
		
		
		return lists;
		
	}

}
