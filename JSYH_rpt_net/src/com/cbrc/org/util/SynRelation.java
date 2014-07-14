package com.cbrc.org.util;

import com.gather.adapter.StrutsJieKou;
import com.gather.struts.forms.MappingRelationForm;
public class SynRelation {
	
	public static void conduct(String orgId,String mfoId,Integer state)throws Exception{
		
		
		MappingRelationForm  mrf = new MappingRelationForm();
		
		mrf.setOrgid(orgId);
		
		mrf.setReplaceOrgId(mfoId);
		
		mrf.setState(state);
		
		mrf.setStartDateAsString("1-1-1");
		
		mrf.setEndDateAsString("1-1-1");
		
		StrutsJieKou.create(mrf);
	}

}
