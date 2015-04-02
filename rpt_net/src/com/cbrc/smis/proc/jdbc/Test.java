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
	 * Main方法
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
	* 提供小数位四舍五入处理。  
	* @param v 需要四舍五入的数字  
	* @param scale 小数点后保留几位  
	* @return 四舍五入后的结果  
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
