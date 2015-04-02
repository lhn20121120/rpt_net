package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.form.AFGatherformulaFrom;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.po.AfGatherformula;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateId;
import com.fitech.gznx.po.AfValidateformula;

public class AFReportmergerView{
	


	private static FitechException log = new FitechException(AFReportmergerView.class); 

	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：AfGatherformula AfCellinfo 
	 * 取得所有报表（不分版本号）
	 * 
	 * @author 姚捷
	 * @return List 所有报表
	 * @exception Exception
	 */
	public static List  getList(String templateId,String versionId){  
			List<Object[]> list=null;
			List resList = new ArrayList();
			if(templateId==null)return null;
			if(versionId==null)return null;
			DBConn conn=null;
			Session session = null;
			try{
				String hql="select t.formulaId,t.formulaValue,t.formulaName,c.cellName,c.rowName,c.colName,c.cellPid " +
				"from AfGatherformula t,AfCellinfo c where t.templateId='"
				+ templateId + "' and " + "t.versionId='"
				+ versionId + "' and t.cellId=c.cellId" +
						" order by c.colNum,c.cellId";	
				conn = new DBConn();
				session = conn.beginTransaction();
				list = session.createQuery(hql).list();
				for(Object[] merger:list){					
					AFGatherformulaFrom form = new AFGatherformulaFrom();
					form.setVersionId(versionId);
					form.setTemplateId(templateId);
					form.setFormulaValue((String)merger[1]);
				
					if(merger[6] != null && !StringUtil.isEmpty((String)merger[6])){
						form.setFormulaName((String)merger[6]+":"+(String)merger[4]+"_"+(String)merger[5]);
		            } else {
		            	form.setFormulaName((String)merger[4]+"_"+(String)merger[5]);
		            }
					form.setCellName((String)merger[3]);
					resList.add(form);
				}
	    	}catch(Exception he){
	    		if(conn != null) conn.endTransaction(true);
	    	}finally{
	    		if(conn != null) conn.closeSession();
	    	}
			return resList;
		}
	
	public static List  getValidateList(String templateId,String versionId){  

		List resList = new ArrayList();
		if(templateId==null)return null;
		if(versionId==null)return null;
		DBConn conn=null;
		try{
			String hql=" from AfValidateformula afg where afg.templateId='"
				+ templateId + "' and " + "afg.versionId='"
				+ versionId + "'";	
			conn=new DBConn();
			resList=conn.openSession().find(hql);
			
    	}catch(Exception he){
    		if(conn != null) conn.endTransaction(true);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
		return resList;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：AfGatherformula AfCellinfo
	 * 取得所有报表（不分版本号）
	 * 
	 * @author 姚捷
	 * @return List 所有报表
	 * @exception Exception
	 */
	public static List  getGatherFormula(String templateId,String versionId){  
			if(templateId==null)return null;
			if(versionId==null)return null;
			DBConn conn=null;
			Session session = null;
			try{
				String hql="select t.formulaId,t.formulaValue,t.formulaName,c.cellName,c.rowName,c.colName,c.cellPid " +
						"from AfGatherformula t,AfCellinfo c where t.templateId='"
					+ templateId + "' and " + "t.versionId='"
					+ versionId + "' and t.cellId=c.cellId" +
							" order by c.colNum,c.cellId";	
				conn = new DBConn();
				session = conn.beginTransaction();
				return session.createQuery(hql).list();
	    	}catch(Exception he){
	    		if(conn != null) conn.endTransaction(true);
	    		return null;
	    	}finally{
	    		if (conn != null)
					conn.closeSession();
	    	}
		}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：AfValidateformula ValidateType
	 * @param templateId
	 * @param versionId
	 * @return
	 */
	public static List  getValidateFormula(String templateId,String versionId){  

		if(templateId==null)return null;
		if(versionId==null)return null;
		DBConn conn=null;
		Session session = null;
		try{
			String hql="select t.formulaId,t.formulaValue,t.formulaName,v.validateTypeName  from AfValidateformula t,ValidateType v where t.templateId='"
				+ templateId + "' and " + "t.versionId='"
				+ versionId + "' and t.validateTypeId=v.validateTypeId order by t.formulaValue";	
			conn = new DBConn();
			session = conn.beginTransaction();
			return session.createQuery(hql).list();			
    	}catch(Exception he){
    		if(conn != null) conn.endTransaction(true);
    		return null;
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
	}

	public static List getBNJValidateList(String templateId, String versionId) {
		// TODO Auto-generated method stub
		List resList = new ArrayList();
		if(templateId==null)return null;
		if(versionId==null)return null;
		DBConn conn=null;
		try{
			String hql="from MCellFormu mcf where mcf.cellFormuId in (" + 
			"select mctf.cellFormuId from MCellToFormu mctf where mctf.childRepId='" + templateId + 
			"' and mctf.versionId='" + versionId + "')" + 
			" order by mcf.formuType,mcf.cellFormu";
			conn=new DBConn();
			resList=conn.openSession().find(hql);
			
    	}catch(Exception he){
    		if(conn != null) conn.endTransaction(true);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
		return resList;
	}
	/***
	 * 使用hibernate 卞以刚 2011-12-22
	 * 影响对象：MCellFormu ValidateType
	 * @param templateId
	 * @param versionId
	 * @return
	 */
	public static List getAllBNJValidateList(String templateId, String versionId) {
		// TODO Auto-generated method stub
		List resList = new ArrayList();
		if(templateId==null)return null;
		if(versionId==null)return null;
		DBConn conn=null;
		try{
			String hql="select mcf.cellFormuId,mcf.cellFormu,mcf.cellFormuView,v.validateTypeName from MCellFormu mcf,ValidateType v where mcf.formuType=v.validateTypeId and mcf.cellFormuId in (" + 
			"select mctf.cellFormuId from MCellToFormu mctf where mctf.childRepId='" + templateId + 
			"' and mctf.versionId='" + versionId + "')" + 
			" order by mcf.formuType,mcf.cellFormu";
			conn=new DBConn();
			resList=conn.openSession().find(hql);
			
    	}catch(Exception he){
    		if(conn != null) conn.endTransaction(true);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
		return resList;
	}
}

