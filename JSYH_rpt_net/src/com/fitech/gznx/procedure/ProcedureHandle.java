package com.fitech.gznx.procedure;

import java.sql.SQLException;

import com.fitech.gznx.procedure.NXReportValid;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.util.FitechException;

public class ProcedureHandle {
	
	private static FitechException log=new FitechException(ProcedureHandle.class);

	/**
	 * ��oracle�﷨(nextval) ��Ҫ�޸� 
	 * ���Ը� 2011-12-26
	 * ִ�б���/��?��У��
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
	 * @return boolean ִ�гɹ�������true;���򣬷���false
	 */
	public static boolean runBNJY(Integer repInId,String userName,Integer templateType){

		boolean result=false;
		// System.out.println("repInId:" + repInId);
		
		if(repInId==null) return result;
		try{
			/***
			 * ��oracle�﷨(nextval) ��Ҫ�޸� 
			 * ���Ը� 2011-12-26
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
	 * ��oracle�﷨(nextval) ��Ҫ�޸�
	 * ���޸� ���sqlserver���ݿ�sql��� ���Ը� ������ 2011-12-26
	 * ���������ݿ��ж�
	 * ���Ը� 2011-12-26
	 * ִ�б��У��
	 * 
	 * @param repInId
	 *            Integer ʵ�����ݱ���ID
	 * @return boolean ִ�гɹ�������true;���򣬷���false
	 */
	public static boolean runBJJY(Integer repInId,String userName,Integer templateType){

		boolean result=false;
		
		if(repInId==null) return result;
		
		try{
			/***
			 * ��oracle�﷨(nextval) ��Ҫ�޸�
			 * ���޸� ���sqlserver���ݿ�sql��� ���Ը� ������ 2011-12-26
			 * ���������ݿ��ж�
			 * ���Ը� 2011-12-26
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
