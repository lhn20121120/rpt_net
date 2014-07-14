package com.fitech.net.obtain.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;



public class StructsSearchFormulaDelegate {
	private static FitechException log = new FitechException(StrutsMChildReportDelegate.class);

	/**
	 * @param args
	 */
	public static List getFormulaList(String ChildRepId,String version,String Reportname,int offset, int limit )
	{
		List retVals = null;
		DBConn conn = null;

		try {
			StringBuffer hql = new StringBuffer(
					"select distinct mcr.childReportId,mcr.versionId,"
							+ "mchild.reportName,mcr.dataSourceEname,mcr.dataSourceCname,mcr.rowColumn,mcr.splitChar,mcr.formula,mcr.flag,mcr.des,mcr.id,mcr.orgId "
							+ "from obtaintext mcr,com.cbrc.smis.hibernate.MChildReport mchild where mcr.childReportId=mchild.comp_id.childRepId and mcr.versionId=mchild.comp_id.versionId "
							+"   and  1=1 ");
			if (version!=null && !version.trim().equals( ""))
				hql.append("   and  mcr.versionId='"+version.trim()+"'");
			if( ChildRepId!=null && !ChildRepId.trim().equals("" ))
			hql.append("  and mcr.childReportId like '%"+ChildRepId.trim()+"%' ");
			if (Reportname!=null && !Reportname.trim().equals( ""))
				hql.append( "  and mchild.reportName like '%"+Reportname.trim()+"%'");
			// System.out.println(hql.toString() );
//			StringBuffer hql = new StringBuffer("select distinct mcr.childReportId,mcr.versionId," +
//					"mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId "
//					+"from obtaintext mcr");
			conn = new DBConn();
			
			Session session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			List list = query.list();
			//	// System.out.println(list.size()+"++++++++++++++++++++++++++++++++++++++++++");
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				retVals = new ArrayList();
				while (it.hasNext()) {
					Object[] item = (Object[]) it.next();
					obtaintext form = new obtaintext();
					form.setChildReportId(item[0] != null ? (String) item[0] : "");
					form.setVersionId(item[1] != null ? (String) item[1] : "");
					form.setReportname(item[2] != null ? (String) item[2] : "");
					form.setDataSourceEname(item[3] != null ? (String) item[3]
									: "");
					form.setDataSourceCname(item[4] != null ? (String) item[4]
									: null);
					form.setRowColumn(item[5] != null ? (String) item[5]
									: "");
					form.setSplitChar(item[6] != null ? (String) item[6] : "");
					form.setFormula(item[7] != null ? (String) item[7] : "");
					form.setFlag(item[8] != null ? (String) item[8] : "");
					form.setDes(item[9] != null ? (String) item[9] : "");
					form.setId(item[10] != null ? (String) item[10] : "");
					form.setOrgId(item[11] != null ? (String) item[11] : "");
					retVals.add(form);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
			he.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return retVals;
	
	}
	public static String getChildPepId(String version,String Reportname)
	{
		String  recordCount =null;
		List retVals = null;
		DBConn conn = null;
		
		try
		{
		StringBuffer hql = new StringBuffer(
				"select mchild.comp_id.childRepId  "
						+ "from obtaintext mcr,com.cbrc.smis.hibernate.MChildReport mchild where mcr.childReportId=mchild.comp_id.childRepId and mcr.versionId=mchild.comp_id.versionId "
						+"   and  1=1 ");
		if (version!=null && !version.trim().equals( ""))
			hql.append("   and  mcr.versionId='"+version.trim()+"'");
		
		if (Reportname!=null && !Reportname.trim().equals( ""))
			hql.append( "  and mchild.reportName='"+Reportname.trim()+"'");
		
		conn = new DBConn();

		Session session = conn.openSession();
		Query query = session.createQuery(hql.toString());
		retVals = query.list();
		if (retVals != null && retVals.size() != 0) {
			recordCount= retVals.get(0).toString();
		}
		
	} catch (HibernateException he) {
		log.printStackTrace(he);
		he.printStackTrace();
	} finally {
		if (conn != null)
			conn.closeSession();
	}

	return recordCount;
	}
	public static int getFormulaCount(String ChildRepId,String version,String Reportname)
	{
		int recordCount = 0;
		List retVals = null;
		DBConn conn = null;
		
		try
		{
		StringBuffer hql = new StringBuffer(
				"select count(*)  "
						+ "from obtaintext mcr,com.cbrc.smis.hibernate.MChildReport mchild where mcr.childReportId=mchild.comp_id.childRepId and mcr.versionId=mchild.comp_id.versionId "
						+"   and  1=1 ");
		if (version!=null && !version.trim().equals( ""))
			hql.append("   and  mcr.versionId='"+version.trim()+"'");
		if( ChildRepId!=null && !ChildRepId.trim().equals("" ))
		hql.append("  and mcr.childReportId like '%"+ChildRepId.trim()+"%' ");
		if (Reportname!=null && !Reportname.trim().equals( ""))
			hql.append( "  and mchild.reportName like'%"+Reportname.trim()+"%'");
		
		conn = new DBConn();

		Session session = conn.openSession();
		Query query = session.createQuery(hql.toString());
		retVals = query.list();
		if (retVals != null && retVals.size() != 0) {
			recordCount = Integer.parseInt(retVals.get(0).toString());
		}
		
	} catch (HibernateException he) {
		log.printStackTrace(he);
		he.printStackTrace();
	} finally {
		if (conn != null)
			conn.closeSession();
	}

	return recordCount;
	}
	
	/**
	 * 判断输入的报表是否存在
	 * @param id
	 * @return
	 */
	public static int getReportCount(String ChildRepId,String version,String Reportname)
	{
		int recordCount = 0;
		List retVals = null;
		DBConn conn = null;
		
		try
		{
		StringBuffer hql = new StringBuffer(
				"select count(*)  "
						+ "from com.cbrc.smis.hibernate.MChildReport mchild where "//mcr.childReportId=mchild.comp_id.childRepId and mcr.versionId=mchild.comp_id.versionId "
						+"     1=1 ");
		if (version!=null && !version.trim().equals( ""))
			hql.append("   and  mchild.comp_id.versionId='"+version.trim()+"'");
		if( ChildRepId!=null && !ChildRepId.trim().equals("" ))
		hql.append("  and mchild.comp_id.childRepId='"+ChildRepId.trim()+"' ");
		
		conn = new DBConn();

		Session session = conn.openSession();
		Query query = session.createQuery(hql.toString());
		retVals = query.list();
		if (retVals != null && retVals.size() != 0) {
			recordCount = Integer.parseInt(retVals.get(0).toString());
		}
		
	} catch (HibernateException he) {
		he.printStackTrace();
		log.printStackTrace(he);
	} finally {
		if (conn != null)
			conn.closeSession();
	}

	return recordCount;
	}
	public static List GetFormulaList(String id)
	{
		List retVals = null;
		DBConn conn = null;

		try {
			StringBuffer hql = new StringBuffer(
					"select distinct mcr.childReportId,mcr.versionId,"
							+ "mchild.reportName,mcr.dataSourceEname,mcr.dataSourceCname,mcr.rowColumn,mcr.splitChar,mcr.formula,mcr.flag,mcr.des,mcr.id,mcr.orgId "
							+ "from obtaintext mcr,com.cbrc.smis.hibernate.MChildReport mchild where mcr.childReportId=mchild.comp_id.childRepId and mcr.versionId=mchild.comp_id.versionId "
							+"   and  1=1 ");
			if (id!=null && !id.trim().equals( ""))
				hql.append("   and  mcr.id='"+id.trim()+"'");
			
//			StringBuffer hql = new StringBuffer("select distinct mcr.childReportId,mcr.versionId," +
//					"mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId,mcr.versionId "
//					+"from obtaintext mcr");
			conn = new DBConn();
			
			Session session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());
			
			List list = query.list();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				retVals = new ArrayList();
				while (it.hasNext()) {
					Object[] item = (Object[]) it.next();
					obtaintext form = new obtaintext();
					form.setChildReportId(item[0] != null ? (String) item[0] : "");
					form.setVersionId(item[1] != null ? (String) item[1] : "");
					form.setReportname(item[2] != null ? (String) item[2] : "");
					form.setDataSourceEname(item[3] != null ? (String) item[3]
									: "");
					form.setDataSourceCname(item[4] != null ? (String) item[4]
									: null);
					form.setRowColumn(item[5] != null ? (String) item[5]
									: "");
					form.setSplitChar(item[6] != null ? (String) item[6] : "");
					form.setFormula(item[7] != null ? (String) item[7] : "");
					form.setFlag(item[8] != null ? (String) item[8] : "");
					form.setDes(item[9] != null ? (String) item[9] : "");
					form.setId(item[10] != null ? (String) item[10] : "");
					form.setOrgId((String)item[11]);
					retVals.add(form);
				}
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
			he.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return retVals;
	}
	public static List getFormulaListById(String id,AddFormulaForm form)
	{
		
		List retVals = null;
		DBConn conn = null;

		try {
			StringBuffer hql = new StringBuffer("select distinct ob.id,ob.formula,ob.splitChar,mchild.reportName,ob.rowColumn,ob.childReportId,ob.versionId,ob.dataSourceEname,ob.orgId,ob.flag from obtaintext ob ,com.cbrc.smis.hibernate.MChildReport mchild where ob.childReportId=mchild.comp_id.childRepId and ob.versionId=mchild.comp_id.versionId and ob.id='"+id+"'");
			conn = new DBConn();
			
			Session session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());
			List list = query.list();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				retVals = new ArrayList();
				while (it.hasNext()) {
					Object[] item = (Object[]) it.next();
					form.setId((String)item[0]);
					form.setFormula((String)item[1]);
					form.setSplitChar((String)item[2]);
					form.setReportname((String)item[3]);
					form.setRowColumn((String)item[4]);
					form.setChildReportId((String)item[5]);
					form.setVersionId((String)item[6]);
					form.setDataSourceEname((String)item[7]);
					form.setOrgId((String)item[8]);
					form.setFlag((String)item[9]);
					retVals.add(form);
					
				}
			}
			
		} catch (HibernateException he) {
			log.printStackTrace(he);
			he.printStackTrace();			
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return retVals;
	}
	
	
	public static boolean update (AddFormulaForm addFormulaForm)
	{
		obtaintext FormulaData=new obtaintext();
		boolean result=false;
		//		连接对象的初始化
		   DBConn conn=null;
		   //会话对象的初始化
		   Session session=null;
		   try
		   {
			   
			   
			   copyVoToPersistence(FormulaData,addFormulaForm);
			   //实例化连接对象
			   conn =new DBConn();
			   //会话对象为连接对象的事务属性
			   session=conn.beginTransaction();
			  
			   //会话对象保存持久层对象
			   session.update(FormulaData);
			   session.flush();
			   
			  
			   result=true;
		   }
		   catch(HibernateException e)
		   {
			   e.printStackTrace() ;
		   }
		   finally{
			   //如果连接状态有,则断开,结束事务,返回
			   if(conn!=null) conn.endTransaction(result);
		   }
		   return result;
		
	}
	private static void copyVoToPersistence(obtaintext  FormulaData,AddFormulaForm addFormulaForm)
	{		
		FormulaData.setChildReportId(addFormulaForm.getChildReportId());
		FormulaData.setDataSourceCname(addFormulaForm.getDataSourceCname());
		FormulaData.setDataSourceEname(addFormulaForm.getDataSourceEname());
		FormulaData.setDes(addFormulaForm.getDes());
		FormulaData.setFlag(addFormulaForm.getFlag());
		FormulaData.setFormula(addFormulaForm.getFormula());
		FormulaData.setReportname(addFormulaForm.getReportname());
		FormulaData.setRowColumn(addFormulaForm.getRowColumn());
		FormulaData.setSplitChar(addFormulaForm.getSplitChar());
		FormulaData.setVersionId(addFormulaForm.getVersionId());
		FormulaData.setId(addFormulaForm.getId());
		FormulaData.setOrgId(addFormulaForm.getOrgId());
	}
	public static boolean UpdataFlag(String flag,obtaintext o)
	{		
		boolean result=false;
		//		连接对象的初始化
		   DBConn conn=null;
		   //会话对象的初始化
		   Session session=null;
		   try
		   {
			   //实例化连接对象
			   conn =new DBConn();
			   //会话对象为连接对象的事务属性
			   session=conn.beginTransaction();
			  o.setFlag(flag);
			   //会话对象保存持久层对象
			   session.update(o);
			   session.flush();
			   
			  
			   result=true;
		   }
		   catch(HibernateException e)
		   {
			   e.printStackTrace() ;
		   }
		   finally{
			   //如果连接状态有,则断开,结束事务,返回
			   if(conn!=null) conn.endTransaction(result);
		   }
		   return result;
	}
}
