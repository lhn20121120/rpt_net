package com.fitech.dataCollect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {
	/**
	 * ���ļ���
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
	 * ���ַ���Ӧ����ת����int����
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
	 * ת������ �������Excel�ı���ת��Ϊgb2312
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
	   // System.out.println("��֧��"+encoding+"���뷽ʽ");
	   usee.printStackTrace();
	  }catch(sun.io.MalformedInputException mfie)
	  {
	   // System.out.println("���������Ч!!!");
	   mfie.printStackTrace();
	  }
	  return retString;
	}

	/***
	 * ��鴫�������ִ��ǲ�����ֵ��
	 * @param value
	 * @return �������򷵻�true �� ���򷵻�false
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
	 * ��û����ļ����£�ָ�������µ������ļ�
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
