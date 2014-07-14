package com.fitech.net.collect.comparator;

import java.util.Comparator;

import com.fitech.net.collect.bean.G1301Record;

public class G1301Comparator implements Comparator{
	
	public int compare(Object arg0,Object arg1)
	{
		G1301Record record1=(G1301Record)arg0;
		G1301Record record2=(G1301Record)arg1;
		
		double rec1=record1.getLoan();
		double rec2=record2.getLoan();
		
		if(rec1<rec2)
			return 1;
		else if(rec1>rec2)
			return -1;
		else
			return 0;
	}

}
