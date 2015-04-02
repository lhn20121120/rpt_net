package com.fitech.net.collect.comparator;

import java.util.Comparator;

import com.fitech.net.collect.bean.G1500Record;
import com.fitech.net.common.StringTool;

public class G1500Comparator implements Comparator{
	
	public int compare(Object o1,Object o2) {
		G1500Record g15_1=(G1500Record)o1;
		G1500Record g15_2=(G1500Record)o2;
		if(g15_1.getM()==null || g15_2.getM()==null){
			return 1;
		}
		if(Double.parseDouble(StringTool.deleteDH(g15_1.getM())) > Double.parseDouble(StringTool.deleteDH(g15_2.getM()))){
			return -1;
		}else if(Double.parseDouble(StringTool.deleteDH(g15_1.getM())) < Double.parseDouble(StringTool.deleteDH(g15_2.getM()))){			
			return 1;
		}else{			
			return 0;
		}
		
		
	}

}
