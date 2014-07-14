package com.fitech.net.collect.comparator;

import java.util.Comparator;

import com.fitech.net.collect.bean.S3403Record;

public class S3403Comparator implements Comparator{
	
	public int compare(Object o1,Object o2) {
		S3403Record s34_1=(S3403Record)o1;
		S3403Record s34_2=(S3403Record)o2;
		if(s34_1.getG()>s34_2.getG()){
			return -1;
		}else if(s34_1.getG()<s34_2.getG()){			
			return 1;
		}else{			
			return 0;
		}
	}

}
