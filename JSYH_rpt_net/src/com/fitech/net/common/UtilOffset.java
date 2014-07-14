package com.fitech.net.common;

import java.util.Locale;

import org.apache.struts.util.MessageResources;

import com.cbrc.smis.util.FitechException;

public class UtilOffset {
	FitechException log=new FitechException(UtilOffset.class);
	Locale LOCALE=Locale.CHINA;
	/**
	 * Excel模板对应PDF模板的起始行的偏移量
	 */
	private String REPORTFILE="com/fitech/net/common/OffsetValueResources";
	
	/**
	 * 从资源文件中，根据键获取其值
	 * 
	 * @param resourcesFile String 资源文件
	 * @param key 资源文件的键
	 * @return String 健的值
	 */
	private String getValue(String resourcesFile,String key){
		if(resourcesFile==null || key==null) return null;
		
		MessageResources resources=MessageResources.getMessageResources(resourcesFile);	
		
		String value=resources.getMessage(this.LOCALE,key);		
		
		return value==null?null:value.trim();
	}
	/**
	 * 根据偏移量生成新的公式
	 * @param oldFormula  原有的公式
	 * @return String     加上偏移量后的公式
	 */
		
		
	public String getValue1(String formuStrParam){
		String num="0690";
		String formuStr = new String(formuStrParam);
		String[] signs = {"+","-","*","/",">","<",","};
		int index = -1;

		StringBuffer buffer = new StringBuffer();
	
		do{
			index = -1;
			boolean bool = false;
			int[] signIndex = new int[7];
			int[] backIndex = new int[7];
			//找出+,-,*,/的索引
			signIndex[0] = formuStr.indexOf(signs[0]);
			signIndex[1] = formuStr.indexOf(signs[1]);
			signIndex[2] = formuStr.indexOf(signs[2]);
			signIndex[3] = formuStr.indexOf(signs[3]);
			signIndex[4] = formuStr.indexOf(signs[4]);
			signIndex[5] = formuStr.indexOf(signs[5]);
			signIndex[6] = formuStr.indexOf(signs[6]);
			for(int i=0;i<backIndex.length;i++){
				backIndex[i] = signIndex[i];
			}					
			//对索引进行排序
			for(int i=0;i<signIndex.length-1;i++){
				if(signIndex[i+1] == -1) continue;
				if(signIndex[0] > signIndex[i+1] || signIndex[0] == -1){
					int temp = signIndex[0];
					signIndex[0] = signIndex[i+1];
					signIndex[i+1] = temp;
				}
			}
			index = signIndex[0];
			String sign = "";
			for(int i=0;i<backIndex.length;i++){
				if(backIndex[i] == index && index != -1){
					sign = signs[i];
					break;
				}
			}
			String formu =null;		
			formu=(index == -1) ? formuStr : formuStr.substring(0,index);				


			if(formu.indexOf("IF(")>-1)
			{				
				formu = formu.substring(3,formu.length());					
				buffer.append("(");
			}
				if(formu.indexOf("(") > -1 ){						
						formu = formu.substring(1,formu.length());				
					buffer.append("(");
				}
				
			
				if(formu.indexOf(">")>-1)
				{					
					formu = formu.substring(1,formu.length());					
					buffer.append(">");
				}
				if(formu.indexOf("<")>-1)
				{
					formu = formu.substring(1,formu.length());					
					buffer.append("<");
				}
			/*	if(formu.indexOf(",(")>-1)
				{
					formu = formu.substring(1,formu.length());	

					buffer.append("?");
				}
				if(formu.lastIndexOf("),")>-1)
				{
					formu = formu.substring(0,formu.length()-1);					
	
					buffer.append(":");
				}*/
				if(formu.indexOf(")") > -1){
					formu = formu.substring(0,formu.length()-1);
					bool = true;
				}	
				if(formu.indexOf("_") <= -1){
					buffer.append(formu);
					if(bool == true) buffer.append(")");
					buffer.append(sign);
					if(index != -1){
						formuStr = formuStr.substring(index+1);
					}
					continue;
				}
		
		

			String[] str = null;
			str=formu.split("_");

		//	buffer.append(str[0]+"_");	
			char[] charArray = str[1].toCharArray();	// C39

			for(int i=charArray.length -1 ;i >= 0;i--){
				try{
					Integer.parseInt(String.valueOf(charArray[i]));		
				}catch(Exception ex){
					String backStr = new String(str[1]);// C39.	
					String rowStr = backStr.substring(i+1,charArray.length);// 39
				
					if(str[0].indexOf("(")==0)
					{			
						formu = formu.substring(1,formu.length());				
						buffer.append("(");							
						String x=formu.substring(formu.indexOf("(")+1,formu.length());
						str=x.split("_");	
					
												
					}	
					buffer.append(str[0]+"_");			
					String keyValue=getValue(REPORTFILE,str[0]+num);					
					int a=Integer.parseInt(rowStr);
					int b=Integer.parseInt(keyValue);		
					int newValue=a+b;				
					backStr = new String(str[1]);   
					String colStr = backStr.substring(0,i+1);  //C		
			
					buffer.append(colStr+newValue);
				} 
			}
			if(bool == true) buffer.append(")");
			buffer.append(sign);	
			if(index != -1){
				formuStr = formuStr.substring(index+1);
			}
		}while(index != -1);
		int y=0;
		String ss=null;
		String xxx=null;
		/* 如果有IF函数,把IF解析成三目运算符 */
		if(buffer.toString().indexOf(",")>-1)
		{
			String s=buffer.toString().replace(',','?');
			int [] strs=new int[10];
			for(int i=0;i<s.length();i++)
			{
				char c=s.charAt(i);
				if(c=='?')
				{
					y++;
					strs[y]=i;
				}
				
			}
			
			if(y!=0 && y%2==0)
			{
				ss=s.substring(strs[2]-1,s.length());			
				xxx=s.substring(0,strs[2]-1)+ss.replace('?',':');			
			}
			return xxx;
		}else
		{
			return buffer.toString();
		}
		


	}
	
	public String getValueByKey(String key){
		if(key==null) return null;
		
		MessageResources resources=MessageResources.getMessageResources(REPORTFILE);	
		
		String value=resources.getMessage(this.LOCALE,key);		
		
		return value==null?null:value.trim();
	}
	public static void main(String[] args){
		String id22="((G0400_C36+G0400_C37)-IF(((G1102_F66*0.02+G1102_H66*0.25*G1102_I66*0.5+G1102_J66)-G1102_C68),0))/G0100_F62";
		String str="(G0102_D8*2%+G0102_C40+G0102_C41+G0102_C42)/G0102_D8*50%";
		String newStr=new UtilOffset().getValue1(id22);
		// System.out.println("newStr="+newStr);
	}
	
}
