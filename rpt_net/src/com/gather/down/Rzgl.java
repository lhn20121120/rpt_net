package com.gather.down;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import com.gather.struts.forms.LogTypeForm;
public class Rzgl {
public static List getrzgl(List loglist){
	List list = new ArrayList();
	if(loglist!=null && loglist.size()!=0)
	{
		// System.out.println(loglist.size());
		for(int i=0;i<loglist.size();i++)
		{
			LogTypeForm item=(LogTypeForm)loglist.get(i);
			list.add(new LabelValueBean(item.getLogType(),item.getLogTypeId().toString()));
			}
		return list;
	}	
	else
		return null;
}
}
