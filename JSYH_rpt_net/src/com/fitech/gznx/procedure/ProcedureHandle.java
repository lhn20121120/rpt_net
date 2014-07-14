package com.fitech.gznx.procedure;

import java.sql.SQLException;

import com.fitech.gznx.procedure.NXReportValid;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.util.FitechException;

public class ProcedureHandle {
	
	private static FitechException log=new FitechException(ProcedureHandle.class);

	/**
	 * 有oracle语法(nextval) 需要修改 
	 * 卞以刚 2011-12-26
	 * 执行表（间/内?）校验
	 * 
	 * @param repInId
	 *            Integer 实际数据报表ID
	 * @return boolean 执行成功，返回true;否则，返回false
	 */
	public static boolean runBNJY(Integer repInId,String userName,Integer templateType){

		boolean result=false;
		// System.out.println("repInId:" + repInId);
		
		if(repInId==null) return result;
		try{
			/***
			 * 有oracle语法(nextval) 需要修改 
			 * 卞以刚 2011-12-26
			 */
            return NXReportValid.processValid(repInId,Report.VALIDATE_TYPE_BN,userName,templateType);
            
		}catch(SQLException sqle){
			result=false;
			log.printStackTrace(sqle);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}
		return result;
	}
	
	/**
	 * 有oracle语法(nextval) 需要修改
	 * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
	 * 已增加数据库判断
	 * 卞以刚 2011-12-26
	 * 执行表间校验
	 * 
	 * @param repInId
	 *            Integer 实际数据报表ID
	 * @return boolean 执行成功，返回true;否则，返回false
	 */
	public static boolean runBJJY(Integer repInId,String userName,Integer templateType){

		boolean result=false;
		
		if(repInId==null) return result;
		
		try{
			/***
			 * 有oracle语法(nextval) 需要修改
			 * 已修改 添加sqlserver数据库sql语句 卞以刚 待测试 2011-12-26
			 * 已增加数据库判断
			 * 卞以刚 2011-12-26
			 */
            return NXReportValid.processValid(repInId,Report.VALIDATE_TYPE_BJ,userName,templateType);
            
		}catch(SQLException sqle){
			result=false;
			log.printStackTrace(sqle);
		}catch(Exception e){
			result=false;
			log.printStackTrace(e);
		}
		return result;
	}
	
}
