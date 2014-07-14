package com.cbrc.smis.proc.jdbc;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.cbrc.smis.proc.ReportZDJS;
import com.cbrc.smis.proc.ReportYCBH;
import com.cbrc.smis.proc.ReportBNValidate;
import com.cbrc.smis.proc.ReportBJValidate;
import com.cbrc.smis.proc.UpdateMChildReport;
import com.cbrc.smis.yjzb.proc.ReportYJZBValidate;

public class Test {
	/**
	 * Main����
	 */
	public static void main(String[] args){
		int repInId=12589;
		try{
			//new ReportYCBH().calculateYCBH(repInId);
			//new ReportBJValidate().validate(repInId);
			//com.cbrc.smis.yjzb.proc.ReportYJZBValidate.Validate(repInId);
			// System.out.println("begin");
			new ReportBNValidate().validate(repInId);
			// System.out.println("end");
			//new UpdateMChildReport().updateEndDate("G0100","0513","2006-04-01");
		}catch(Exception e){
			e.printStackTrace();
		}
		/*float i=5487950.0000f;
		// System.out.println(":::float: "+Test.round(i,0));*/
	}
	
   /**  
	* �ṩС��λ�������봦��  
	* @param v ��Ҫ�������������  
	* @param scale С���������λ  
	* @return ���������Ľ��  
	*/  
	public static float round(float v,int scale){  
		//String temp=scale==0?"#########0":"#########0.";  
		String temp="#########0.";
		for (int i=0;i<scale ;i++ )  
		{  
			temp+="0";  
		}
		// System.out.println("temp is:"+temp);
		return Float.valueOf(new java.text.DecimalFormat(temp).format(v)).floatValue();  
	} 
	
}
