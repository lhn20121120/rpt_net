/***
 * 姬怀宝
 * 把登陆机构和该机构的所有带报机构保存到struts.util.LabelValueBean
 * 
 */
package com.gather.down;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.gather.adapter.StrutsMOrg;
import com.gather.struts.forms.MOrgForm;

public class Jigoumichneg { 
	public static List getSubReportId(String[] orgIds){
		List repRangeList=StrutsMOrg.getmorg(orgIds);
		List list = new ArrayList();
		if(repRangeList!=null && repRangeList.size()!=0)
		{
			for(int i=0;i<repRangeList.size();i++)
			{
				MOrgForm item = (MOrgForm)repRangeList.get(i);
				list.add(new LabelValueBean(item.getOrgName(),item.getOrgId()));	
			}
			return list;
		}
		else 
			return null;
	}
}
