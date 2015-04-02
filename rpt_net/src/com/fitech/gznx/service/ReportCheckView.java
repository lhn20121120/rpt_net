package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.TranslatorUtil;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.hibernate.MCellFormu;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.ValidateType;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.form.AFGatherformulaFrom;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.form.ReportCheckForm;
import com.fitech.gznx.po.AfGatherformula;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateId;
import com.fitech.gznx.po.AfValidateformula;

public class ReportCheckView {
	


	private static FitechException log = new FitechException(ReportCheckView.class); 

	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：AfValidateformula ValidateType
	 * 取得所有报表（不分版本号）
	 * 
	 * @author 姚捷
	 * @return List 所有报表
	 * @exception Exception
	 */
	public static List  getList(String templateId,String versionId){  
		
		List resList = new ArrayList();
		if(templateId==null)return null;
		if(versionId==null)return null;
		DBConn conn=null;
			try{
				String hql=" select afg.validateTypeId,afg.formulaValue,afg.formulaName,afg.formulaId, val.validateTypeName,afg.cellId from AfValidateformula afg,ValidateType val where afg.templateId='"
					+ templateId + "' and " + "afg.versionId='"
					+ versionId + "' and val.validateTypeId=afg.validateTypeId";	
				conn=new DBConn();
				List<Object[]> list=conn.openSession().find(hql);
				for(Object[] reportcheck:list){
					ReportCheckForm form = new ReportCheckForm();
					form.setVersionId(versionId);
					form.setTemplateId(templateId);
					form.setValidateTypeId(String.valueOf(reportcheck[0]));
					form.setValidateTypeName((String)reportcheck[4]);
					form.setFormulaValue((String)reportcheck[1]);
					form.setFormulaName((String)reportcheck[2]);
					form.setFormulaId((Long)reportcheck[3]);
					form.setCellId((Long)reportcheck[5]);
				 
					resList.add(form);
				}
	    	}catch(Exception he){
	    		he.printStackTrace();
	    	}finally{
	    		if(conn != null) conn.closeSession();
	    	}
			return resList;
		}
	/***
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：ValidateType
	 * @param id
	 * @return
	 */
	public static String getValidateTypeName(String id)  {

		List list=null;
		List retVals = new ArrayList();
		DBConn conn=null;
		try {
				String hql=" from ValidateType val where val.validateTypeId='"
					+ id + "'";	
				conn=new DBConn();
				list=conn.openSession().find(hql);
									ValidateType validate = (ValidateType)list.get(0);
					return validate.getValidateTypeName();
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return null;
	}



	private static AfTemplate getTemplate(String templateId, String versionId) {
		// TODO Auto-generated method stub
		boolean result = false;
	    DBConn conn =null;
	    Session session =null;
	    AfTemplate template = new AfTemplate();
	    if(templateId!=null && versionId!=null)
	    {
	        try
	        {
	            conn = new DBConn();
	            session = conn.beginTransaction(); 
				AfTemplateId id = new AfTemplateId();
				id.setTemplateId(templateId);
				id.setVersionId(versionId);
//					AfTemplate template = new AfTemplate();
//					template.setId(id);
				template = (AfTemplate)session.load(AfTemplate.class, id);
	        }catch(Exception e)
	        {
	            log.printStackTrace(e);
	            result = false;
	        }
	        finally{
	        	try {
					session.close();
				} catch (HibernateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            if(conn!=null)
	                conn.endTransaction(result);
	        }
	       
	    }
		
		return template;
	}
	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 取得所有报表（不分版本号）
	 * 
	 * @author 姚捷
	 * @return List 所有报表
	 * @exception Exception
	 */
	public static ReportCheckForm  getCheck(String templateId,String versionId,Long formulId){  
		ReportCheckForm checkForm=null;

		if(templateId==null)return null;
		if(versionId==null)return null;
		DBConn conn=null;
			try{
				String hql=" from AfValidateformula afg where afg.templateId='"
					+ templateId + "' and " + "afg.versionId='"
					+ versionId + "' and afg.formulaId="+ formulId;	
				conn=new DBConn();
				List<AfValidateformula> list=conn.openSession().find(hql);
				for(AfValidateformula reportcheck:list){
					checkForm = new ReportCheckForm();
					checkForm.setVersionId(versionId);
					checkForm.setTemplateId(templateId);
					checkForm.setValidateTypeId(String.valueOf(reportcheck.getValidateTypeId()));
					/**已使用hibernate 卞以刚 2011-12-28
					 * 影响对象：ValidateType**/
					checkForm.setValidateTypeName(getValidateTypeName(String.valueOf(reportcheck.getValidateTypeId())));
					checkForm.setFormulaValue(reportcheck.getFormulaValue());
					checkForm.setFormulaName(reportcheck.getFormulaName());
					checkForm.setFormulaId(reportcheck.getFormulaId());
					checkForm.setCellId(reportcheck.getCellId());
					break;
				}
	    	}catch(Exception he){
	    		if(conn != null) conn.endTransaction(true);
	    	}finally{
	    		if(conn != null) conn.closeSession();
	    	}
			return checkForm;
		}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 取得所有报表（不分版本号）
	 * 
	 * @author 姚捷
	 * @return List 所有报表
	 * @exception Exception
	 */
	public static ReportCheckForm  getYJHCheck(String templateId,String versionId,Long formulId){  
		ReportCheckForm checkForm=null;

		if(templateId==null)return null;
		if(versionId==null)return null;
		DBConn conn=null;
			try{
				String hql="select m.formuType,m.cellFormu,m.cellFormuView from MCellFormu m,MCellToFormu t where t.childRepId='"
					+ templateId + "' and " + "t.versionId='"
					+ versionId + "' and t.cellFormuId=m.cellFormuId and m.cellFormuId="+ formulId;	
				conn=new DBConn();
				List<Object[]> list=conn.openSession().find(hql);
				for(Object[] reportcheck:list){
					checkForm = new ReportCheckForm();
					checkForm.setVersionId(versionId);
					checkForm.setTemplateId(templateId);
					checkForm.setValidateTypeId(String.valueOf(reportcheck[0]));
					//checkForm.setValidateTypeName(getValidateTypeName(String.valueOf(reportcheck.getValidateTypeId())));
					checkForm.setFormulaValue((String)reportcheck[1]);
					checkForm.setFormulaName((String)reportcheck[2]);
					checkForm.setFormulaId(formulId);

					break;
				}
	    	}catch(Exception he){
	    		he.printStackTrace();
	    	}finally{
	    		if(conn != null) conn.closeSession();
	    	}
			return checkForm;
		}
	/***
	 * 已使用Hibernate 卞以刚 2011-12-28
	 * 影响对象：MCellFormu MCellToFormu ValidateType
	 * @param templateId 
	 * @param versionId
	 * @return
	 */
	public static List getBNJList(String templateId, String versionId) {
		// TODO Auto-generated method stub
		  
		List list=null;
		List resList = new ArrayList();
		if(templateId==null)return null;
		if(versionId==null)return null;
		System.out.println(templateId+versionId);
		DBConn conn=null;
			try{
			String hql="from MCellFormu mcf where mcf.cellFormuId in (" + 
				"select mctf.cellFormuId from MCellToFormu mctf where mctf.childRepId='" + templateId + 
				"' and mctf.versionId='" + versionId + "')" + 
				" order by mcf.formuType,mcf.cellFormu";
			
			conn=new DBConn();
			list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				resList=new ArrayList();
				MCellFormu mCellFormu=null;
				for(int i=0;i<list.size();i++){
					mCellFormu=(MCellFormu)list.get(i);
					ReportCheckForm form = new ReportCheckForm();
					form.setVersionId(versionId);
					form.setTemplateId(templateId);
					form.setValidateTypeId(String.valueOf(mCellFormu.getFormuType()));
					/**已使用hibernate 卞以刚 2011-12-22
					 * 影响对象：ValidateType**/
					form.setValidateTypeName(getValidateTypeName(String.valueOf(mCellFormu.getFormuType())));
					form.setFormulaValue(mCellFormu.getCellFormu());
					form.setFormulaName(mCellFormu.getCellFormuView());
					form.setFormulaId(new Long(mCellFormu.getCellFormuId()));
					resList.add(form);
				}
			}
			
		}catch(Exception he){
	    		he.printStackTrace();
	    	}finally{
	    		if(conn != null) conn.closeSession();
	    	}
			return resList;
		}
}

