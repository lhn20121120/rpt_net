package com.fitech.net.collect.test;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.MMainRep;

public class Test5 {
	
	public static void main(String[] args)
	{
		String s="+65664531";
		
		// System.out.println(dataIsNum(s));
	}

	public static boolean dataIsNum(String s)
	{
		
		// System.out.println("in Test5***************");
		boolean flag=false;
		if(s!=null && s.length()>0){
			for(int i=0;i<s.length();i++)
			{
				char tmpC=s.charAt(i);
				if(Character.getNumericValue(tmpC)<0 || Character.getNumericValue(tmpC)>9){
					return false;
				}
				// System.out.println("char of number "+i+" is "+tmpC);
			}
			flag=true;
		}
		return flag;
		
	}
	
	
	public static void deleteMMainRep()
	{
		DBConn conn=new DBConn();
		Session session=conn.beginTransaction();
		List mMainRepList=null;
		MMainRep mMainRep=null;
//		MCurUnit mCurUnit=new MCurUnit();
//		mCurUnit.setCurUnit(new Integer(2));
//		mCurUnit.setCurUnitName("万元");
//		MMainRep mMainRep=new MMainRep();
//		mMainRep.setRepCnName("报表");
//		mMainRep.setRepEnName("report");
//		mMainRep.setMCurUnit(mCurUnit);
		try {
			mMainRepList=session.find("from MMainRep");
			mMainRep=(MMainRep)mMainRepList.get(mMainRepList.size()-1);
			session.delete(mMainRep);
			session.flush();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			conn.endTransaction(false);
			e.printStackTrace();
		}
		finally{
			if(conn!=null) conn.endTransaction(true);
		}
		
		
		// System.out.println("OK");
	}
}
