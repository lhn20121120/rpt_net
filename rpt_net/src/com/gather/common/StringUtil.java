package com.gather.common;


public class StringUtil {
	/**
	 * @author linfeng
	 * @function 把字符串数组转化成sql in 语句中的字符串表达式
	 * @param sql
	 * @return String
	 */
	public static String getStrForSqlIN(String[] sql){
		if(sql==null || sql.length<1) return null;
		StringBuffer temp=new StringBuffer();
		for(int i=0;i<sql.length;i++){
			if(sql[i]==null) continue;
			temp.append("'"+sql[i].trim()+"',");
		}
		if(temp.length()>1){
			String resultStr=temp.toString();
			return resultStr.substring(0,resultStr.length()-1);	
		}else{
			return null;
		}
	}
	/**
	 * 新增加的SQL语言转换函数(转换Integer型)
	 * @param sql
	 * @return
	 */
	public static String getStrForSqlIN(Integer[] sql){
		if(sql==null || sql.length<1) return null;
		//// System.out.println("getStrForSqlIN() : "+sql.length+"--"+sql[0]);
		StringBuffer temp=new StringBuffer();
		for(int i=0;i<sql.length;i++){
			if(sql[i]==null) continue;
			if(i==sql.length){
				temp.append(sql[i]);
			}else{
				temp.append(sql[i]+",");
			}
			
		}
		if(temp.length()>1){
			String resultStr=temp.toString();
			//// System.out.println(resultStr.substring(0,resultStr.length()-1));
			return resultStr.substring(0,resultStr.length()-1);	
		}else{
			return null;
		}
	}
	
	/**
	 * @author linfeng
	 * @function 把字符串转化成int型数字
	 * @param sql
	 * @return String
	 */
	public static int str2Int(String str){
		if(str==null) return -1;
		try{
		    return new Integer(str).intValue();	
		}catch(Exception e){return -1;}		
	}
	
	/**
	 * @author linfeng
	 * @function 取文件的后缀名
	 * @param fileName String
	 * @return suffix String
	 */
	public static String getSuffix(String fileName){
		int pIndex=fileName.lastIndexOf('.');
		return fileName.substring(pIndex+1,fileName.length());
	}

	
	private static boolean judgeFileFormat(String fileName){
		// System.out.println("index is: "+fileName.lastIndexOf('.')+"length is: "+fileName.length());
		// System.out.println(fileName.substring(fileName.length()-3,fileName.length()));
		return fileName.substring(fileName.length()-4,4).equals("zip");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] temp=new String[3];
		temp[0]="1111.zip.zip";
		temp[1]="2222";
		temp[2]="3333";
		String[] t=new String[1];
        // System.out.println(judgeFileFormat("my.zip"));  
        // System.out.println("my.zip".substring(3,6));
        // System.out.println(getSuffix("my.zip"));
	}


}
