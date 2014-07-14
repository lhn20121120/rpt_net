package com.fitech.net.collect.comparator;

import java.util.Comparator;

import com.fitech.net.collect.bean.S2300Record;
import com.fitech.net.common.StringTool;

public class S2300Comparator implements Comparator{
	
	public int compare(Object o1,Object o2) {
		S2300Record s23_1=(S2300Record)o1;
		S2300Record s23_2=(S2300Record)o2;
		if(Double.parseDouble(StringTool.deleteDH(s23_1.getE())) > Double.parseDouble(StringTool.deleteDH(s23_2.getE()))){
			return -1;
		}else if(Double.parseDouble(StringTool.deleteDH(s23_1.getE())) < Double.parseDouble(StringTool.deleteDH(s23_2.getE()))){			
			return 1;
		}else{			
			return 0;
		}
	}
}
