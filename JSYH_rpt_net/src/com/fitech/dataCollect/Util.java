package com.fitech.dataCollect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {
	/**
	 * 建文件夹
	 */
	protected static boolean mkdir(String dir){
		boolean re=false;
		try{
		  (new File(dir)).mkdirs();
		  re=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return re;
	}

	/**
	 * 把字符对应得列转换成int类型
	 * @param ref
	 * @return
	 */

	protected static int convertColStringToNum(String ref)
	{
	    int len = ref.length();
	    int retval = 0;
	    int pos = 0;
	    for(int k = ref.length() - 1; k > -1; k--)
	    {
	        char thechar = ref.charAt(k);
	        if(pos == 0)
	            retval += Character.getNumericValue(thechar) - 9;
	        else
	            retval += (Character.getNumericValue(thechar) - 9) * (pos * 26);
	        pos++;
	    }
	    return retval - 1;
	}
	/**
	 * 转化编码 把输出到Excel的编码转换为gb2312
	 * @param toEncoded
	 * @param encoding
	 * @return
	 */
	public static String getUnicode(String toEncoded,String encoding)
	{
		//// System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	  String retString="";
	  if(toEncoded.equals("")||toEncoded.trim().equals(""))
	  {
	    return toEncoded;
	  }
	  try
	  {
	  byte[] b=toEncoded.getBytes(encoding);
	  sun.io.ByteToCharConverter  convertor=sun.io.ByteToCharConverter.getConverter(encoding);
	  char [] c=convertor.convertAll(b);
	  for(int i=0;i<c.length;i++)
	  {
	    retString+=String.valueOf(c[i]);
	  }
	  }catch(java.io.UnsupportedEncodingException usee)
	  {
	   // System.out.println("不支持"+encoding+"编码方式");
	   usee.printStackTrace();
	  }catch(sun.io.MalformedInputException mfie)
	  {
	   // System.out.println("输入参数无效!!!");
	   mfie.printStackTrace();
	  }
	  return retString;
	}

	/***
	 * 检查传过来的字串是不是数值型
	 * @param value
	 * @return 是数组则返回true ， 否则返回false
	 */
	public static boolean isDouble(String value)
	{
		boolean result = false;
		try
		{
			Double.parseDouble(value);
			result =  true;
		}
		catch(NumberFormatException e)
		{
			result = false;
		}	
		return result;	
	}
	/**
	 * 获得汇总文件夹下，指定日期下的所有文件
	 */
	public static List getExcels(String year,String month){
		List re=new ArrayList();
		File folder=new File(CollectConfig.Excel_OutPath+CollectConfig.FILESEPARATOR+year+month);
		if(folder.exists()&&folder.isDirectory()){
			File[] excels=folder.listFiles();
			for(int i=0;i<excels.length;i++){
				ExcelBean excel=new ExcelBean();
				excel.setName(excels[i].getName());
				excel.setUrl(excels[i].getPath());
				re.add(excel);
			}
		}
		return re;
		
	}

}
