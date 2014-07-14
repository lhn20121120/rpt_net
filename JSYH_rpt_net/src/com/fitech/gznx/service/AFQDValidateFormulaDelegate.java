package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;

import com.fitech.gznx.form.QDValidateForm;
import com.fitech.gznx.po.AfQdValidateformula;

public class AFQDValidateFormulaDelegate {
	/**
	 * 取得对应报表的校验公式
	 * @param templateId
	 * @param versionId
	 * @return
	 */
	public static List getValidateListById(String templateId,String versionId){

		List retVals = null;
		DBConn conn = null;

		try {
			conn = new DBConn();
			Session session = conn.openSession();
			String hql =" from AfQdValidateformula a where a.templateId='"+templateId+"' and a.versionId='"+versionId+"'";
			
			Query query = session.createQuery(hql);
			List<AfQdValidateformula> list = query.list();
			retVals = new ArrayList();
			for(AfQdValidateformula vForm:list){
				QDValidateForm form = traslateForm(vForm);
				retVals.add(form);
			}

		} catch (Exception he) {
			he.printStackTrace();
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return retVals;
	}
	/**
	 * 增加校验公式
	 * @param vForm
	 * @return 插入是否成功
	 */
	public  static boolean insertValidateFormula(QDValidateForm vForm){
		boolean result = true;		
		DBConn conn = null;
		Session session = null;
		try {			
			conn = new DBConn();
			session = conn.beginTransaction();
			AfQdValidateformula temp = traslateForm(vForm);
			session.save(temp);
			result=true;
		} catch (Exception e) {
			result = false;		
		} finally {
			if (conn != null) conn.endTransaction(result);
		}		
		return result;
		
	}
	/**
	 * 修改校验公式
	 * @param vForm
	 * @return 插入是否成功
	 */
	public static boolean updateValidateFormula(QDValidateForm vForm){
		boolean result = true;		
		DBConn conn = null;
		Session session = null;
		try {			
			conn = new DBConn();
			session = conn.beginTransaction();
			AfQdValidateformula temp = traslateForm(vForm);
			session.update(temp);
			result=true;
		} catch (Exception e) {
			result = false;		
		} finally {
			if (conn != null) conn.endTransaction(result);
		}		
		return result;
		
	}
	/**
	 * 修改校验公式
	 * @param vForm
	 * @return 插入是否成功
	 */
	public static QDValidateForm getValidateFormula(String formulaId){

		DBConn conn = null;
		Session session = null;
		QDValidateForm vForm = null;
		try {			
			conn = new DBConn();
			session = conn.beginTransaction();

			AfQdValidateformula temp = (AfQdValidateformula) session.load(AfQdValidateformula.class, 
										Integer.valueOf(formulaId));
			if(temp != null){
				vForm = traslateForm(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) 
				conn.closeSession();
		}		
		return vForm;
		
	}
	/**
	 * 删除校验公式
	 * @param vForm
	 * @return 插入是否成功
	 */
	public static boolean deleteValidateFormula(String formulaIds){
		boolean result = true;		
		DBConn conn = null;	    
		Session session = null;
		Connection connection=null;
		Statement stmt = null;
		StringBuffer sql = null;
		try {
			conn = new DBConn();	            
			session = conn.beginTransaction();
			connection = session.connection();
			stmt = connection.createStatement();
			sql = new StringBuffer();
			sql.append("delete from af_qd_validateformula where formula_id in (")
					.append(formulaIds).append(")");
			stmt.execute(sql.toString().toUpperCase());			
			result=true;
		} catch (Exception e) {
			result = false;		
		} finally {			
			try {
				if(result == true) {
					connection.commit();
				}else{
					connection.rollback();
				}
				connection.setAutoCommit(result);
				if(stmt != null) 
					stmt.close();

				if(connection != null)
					connection.close();
				if(session != null) 
					session.close();
				if(conn != null) 
					conn.closeSession();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (HibernateException e) {
					e.printStackTrace();
				}
			
		}		
		return result;
		
	}
	
	private static QDValidateForm traslateForm(AfQdValidateformula vForm){
		QDValidateForm form = new QDValidateForm();
		if(vForm.getFormulaId()!=null)
		form.setFormulaId(String.valueOf(vForm.getFormulaId().intValue()));
		form.setFormuType(String.valueOf(vForm.getFormuType().intValue()));
		form.setTemplateId(vForm.getTemplateId());
		form.setVersionId(vForm.getVersionId());
		form.setColNum(vForm.getColNum());
		form.setColName(vForm.getColName());
		form.setFormuDes(vForm.getFormuDes());
		form.setFormuName(vForm.getFormuName());
		form.setFormuValue(vForm.getFormuValue());
		form.setValidateMessage(vForm.getValidateMessage());
		return form;
	}
	private static AfQdValidateformula traslateForm(QDValidateForm vForm){
		AfQdValidateformula form = new AfQdValidateformula();
		if(vForm.getFormulaId()!=null)
		form.setFormulaId(Integer.valueOf(vForm.getFormulaId()));
		form.setFormuType(Integer.valueOf(vForm.getFormuType()));
		form.setTemplateId(vForm.getTemplateId());
		form.setVersionId(vForm.getVersionId());
		form.setColNum(vForm.getColNum());
		form.setColName(vForm.getColName());
		form.setFormuDes(vForm.getFormuDes());
		form.setFormuName(vForm.getFormuName());
		form.setFormuValue(vForm.getFormuValue());
		form.setValidateMessage(vForm.getValidateMessage());
		return form;
	}

}
