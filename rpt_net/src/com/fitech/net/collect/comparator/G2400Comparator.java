package com.fitech.net.collect.comparator;

import java.util.Comparator;

import com.fitech.net.collect.bean.G2400Record;
import com.fitech.net.common.StringTool;


public class G2400Comparator implements Comparator{
	
	public int compare(Object arg0,Object arg1)
	{
		G2400Record templet1=(G2400Record)arg0;
		G2400Record templet2=(G2400Record)arg1;
		
		double templ1=Double.parseDouble(StringTool.deleteDH(templet1.getRest()));
		double templ2=Double.parseDouble(StringTool.deleteDH(templet2.getRest()));
		
		if(templ1<templ2)
			return 1;
		else if(templ1>templ2)
			return -1;
		else
			return 0;
	}

}
