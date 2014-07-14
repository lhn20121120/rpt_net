package com.fitech.net.collect.comparator;

import java.util.Comparator;

import com.fitech.net.collect.bean.S4400Record;
import com.fitech.net.common.StringTool;


public class S4400Comparator implements Comparator{
	
	public int compare(Object arg0,Object arg1)
	{
		S4400Record templet1=(S4400Record)arg0;
		S4400Record templet2=(S4400Record)arg1;
		
		double templ1=Double.parseDouble(StringTool.deleteDH(templet1.getThis_Last()));
		double templ2=Double.parseDouble(StringTool.deleteDH(templet2.getThis_Last()));
		
		if(templ1<templ2)
			return 1;
		else if(templ1>templ2)
			return -1;
		else
			return 0;
	}

}
