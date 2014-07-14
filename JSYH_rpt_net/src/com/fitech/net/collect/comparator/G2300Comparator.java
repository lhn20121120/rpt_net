package com.fitech.net.collect.comparator;

import java.util.Comparator;

import com.fitech.net.collect.bean.G2300Record;
import com.fitech.net.common.StringTool;



public class G2300Comparator implements Comparator{
	
	public int compare(Object o1,Object o2) {
		G2300Record g23_1=(G2300Record)o1;
		G2300Record g23_2=(G2300Record)o2;
		if(Double.parseDouble(StringTool.deleteDH(g23_1.getE())) > Double.parseDouble(StringTool.deleteDH(g23_2.getE()))){
			return -1;
		}else if(Double.parseDouble(StringTool.deleteDH(g23_1.getE())) < Double.parseDouble(StringTool.deleteDH(g23_2.getE()))){			
			return 1;
		}else{			
			return 0;
		}
	}
}
