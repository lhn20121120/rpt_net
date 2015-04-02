package com.cbrc.smis.system.cb;

import java.util.Iterator;

import org.dom4j.Element;

public class ChooseDetail {

	public static boolean isNull(Element e) throws Exception {
		
		boolean is = true;
		

		
		Iterator  iterator = e.elementIterator();
		
		if(iterator.hasNext())
			
			is = false;
		
		return  is;

	}

}
