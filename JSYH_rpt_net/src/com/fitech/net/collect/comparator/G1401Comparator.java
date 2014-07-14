package com.fitech.net.collect.comparator;

import java.util.Comparator;

import com.fitech.net.collect.bean.G1401Record;
import com.fitech.net.common.StringTool;


public class G1401Comparator implements Comparator{
	
	public int compare(Object arg0,Object arg1)
	{
		G1401Record templet1=(G1401Record)arg0;
		G1401Record templet2=(G1401Record)arg1;
		
		double templ1=Double.parseDouble(StringTool.deleteDH(templet1.getTruth()));
		double templ2=Double.parseDouble(StringTool.deleteDH(templet2.getTruth()));
		
		if(templ1<templ2)
			return 1;
		else if(templ1>templ2)
			return -1;
		else
			return 0;
	}

}
