   package com.fitech.net.common;

import java.util.ArrayList;

//if(s!=null && s.length()>0){
//for(int i=0;i<s.length();i++)
//{
//	char tmpC=s.charAt(i);
//	if(Character.getNumericValue(tmpC)<0 || Character.getNumericValue(tmpC)>9){
//		return false;
//	}
//	// System.out.println("char of number "+i+" is "+tmpC);
//}
//flag=true;
//}
public class StringTool {
	
	/**����ַ����Ƿ�������*/
	public static boolean strIsNum(String s)
	{
		boolean flag=false;
		if(s==null) return false;
		if(s.equals("")) return false;
		s=deleteDH(s);
		//s=deleteDol(s);
		try {
			Double.parseDouble(s);
			flag=true;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			flag=false;
		}
		return flag;
	}

	/**����ַ����Ƿ���Ӣ����ĸ*/
	public static boolean strIsLetter(String s)
	{
		boolean flag=false;
		if(s!=null && s.length()>0){
			for(int i=0;i<s.length();i++)
			{
				char tmpC=s.charAt(i);
				if(Character.getNumericValue(tmpC)<10 || Character.getNumericValue(tmpC)>35){
					return false;
				}
				// System.out.println("char of number "+i+" is "+tmpC);
			}
			flag=true;
		}
		return flag;
	}
	
	/**����˳���*/
	public static int colIndex(String col)
	{
		if(col.length()>=2) 
			{
			return ((col.charAt(0)-'A')+1)*26+col.charAt(1)-'A';
			}
		else
		    return col.charAt(0)-'A';
	}
	
	/**������������*/
	public static String getDataType(String s)
	{
		String type=null;
		if(!strIsNum(s)) return type;
		s=deleteDH(s);
		try {
			Integer.parseInt(s);
			type="Integer";
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			type="Double";
			// System.out.println(s +"is not Integer,is Double");
		}
		return type;
	}
	
	public static String doChildOrgIds(String childOrgIds)
	{
		if(childOrgIds==null) return null;
		ArrayList al=new ArrayList();
		String lowerOrgIds = "";
		String[] ids=childOrgIds.split(",");
		String temp="";
		for(int i=0;i<ids.length;i++)
		{
			temp=ids[i];
			al.add("'"+temp+"'");
		}
		if(al.size()==0) return lowerOrgIds;
		for(int j=0;j<al.size();j++)
		{
			String orgId = (String)al.get(j);
        	lowerOrgIds = lowerOrgIds.equals("") ? orgId : lowerOrgIds + "," + orgId;
		}
		
		
		return lowerOrgIds;
	}
	
	/**���ַ���תΪ����*/
	public static String deleteDH(String s)
	{
		s=formatStr(s);
		s=deleteDol(s);
		return s;
	}
	
	/**ȥ���ַ����е�$*/
	public static String deleteDol(String s)
	{
		if(s==null) return s;
		if(s.equals("")) return s;
		if(s.indexOf("$")>-1){
			s=s.substring(s.indexOf("$")+1);
		}
		
		return s;
	}
	
	/**ȥ���ַ����еĶ���*/
	public static String formatStr(String s)
	{
//		s=deleteDH(s);
//		s=deleteDol(s);
//		return s;
		if(s==null) return s;
		if(s.equals("")) return s;
		
		return s.replaceAll(",","");
	}
	
	/**
	 * �ַ���ת��"ISO8859-1"to"GB2312"
	 */
	public static String changeCoding(String sourceCode){
		if(sourceCode==null || sourceCode.length()<1) return null;
		try{
		    return new String(sourceCode.getBytes("ISO8859-1"),"GB2312");
		}catch(Exception e){return sourceCode;}

		
	}
}
