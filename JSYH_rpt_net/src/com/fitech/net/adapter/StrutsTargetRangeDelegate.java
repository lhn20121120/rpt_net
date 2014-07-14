package com.fitech.net.adapter;

import java.util.List;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.hibernate.TargetRange;
public class StrutsTargetRangeDelegate {
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsTargetRangeDelegate.class);
	
	
	public static boolean Exit(String orgId,Integer TargetDefineId)
	{
		boolean result=false;
		if(orgId==null || TargetDefineId==null)return false;
		
		DBConn conn=null;
		try
		{
			String sql="from TargetRange t where t.orgId='"+orgId+"'"
			            +"  and t.targetDefineId="+TargetDefineId;
			conn=new DBConn();
		    List list=conn.beginTransaction().find(sql);
		    if(list!=null && list.size()>0)
		    	result=true;
		}
		catch(Exception e)
		{
			log.printStackTrace(e);
			result=false;
		}
		finally
		{
			if(conn!=null)conn.closeSession();
		}
		
		return result;
	}
	public static boolean delete(String orgId,Integer TargetDefineId)
	{
		boolean result=false;
		
		if(orgId==null || TargetDefineId==null)return result;
		
		DBConn conn=null;
		Session session=null;
		try
		{
			String sql="from TargetRange t where t.orgId='"+orgId+"'"
            +"  and t.targetDefineId="+TargetDefineId;
            conn=new DBConn();
            session=conn.beginTransaction();
           session.delete(sql);
           session.flush();
            result=true;
			
			
			
		}
		catch(Exception e)
		{
			log.printStackTrace(e);
			result=false;
		}
		finally
		{
			if(conn!=null)conn.endTransaction(result);
		}
		return result;
	}
	public static boolean add(String orgId,Integer TargetDefineId)
	{
		boolean result=false;
		DBConn conn=null;
		try
		{
			TargetRange t=new TargetRange();
			t.setOrgId(orgId);
			t.setTargetDefineId(TargetDefineId);
			conn=new DBConn();
			conn.beginTransaction().save(t);
			result=true;
		}
		catch(Exception e)
		{
			log.printStackTrace(e);
			result=false;
		}
		finally
		{
			if(conn!=null)conn.endTransaction(result);
			
		}
		
		return result;
	}
}