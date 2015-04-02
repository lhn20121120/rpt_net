package com.fitech.framework.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FitUtil {
	
	public static  List findMatcList(String srcStr,String reg)throws Exception{
		List result = new ArrayList();
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(srcStr);
		while(m.find())
			result.add(srcStr.substring(m.start(), m.end()));
		return result;
	}
	public static boolean checkByReg(String srcStr,String reg)throws Exception{
		boolean result = false;
		Pattern p = Pattern.compile(reg);    
        Matcher m = p.matcher(srcStr);    
        if(m.matches())
        	result = true;
		return result;
	}
}
