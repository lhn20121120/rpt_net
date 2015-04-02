package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsMCellFormuDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.AddcheckForm;
import com.fitech.gznx.form.CollectRelationForm;
import com.fitech.gznx.form.ReportCheckForm;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.AfCollectRelation;
import com.fitech.gznx.po.AfCollectRelationId;
import com.fitech.gznx.po.AfValidateformula;

public class DeleteCheck {
	private static FitechException log=new FitechException(DeleteCheck.class);
	//校验关系表达式是否存在
	public static boolean ValidateFlag(String templateId,String versionId,String formulaName,String formulaValue ){
		boolean result=false;
		if(StringUtil.isEmpty(templateId) ||StringUtil.isEmpty(versionId) ||StringUtil.isEmpty(formulaName)||StringUtil.isEmpty(formulaValue)) {
			result =true;
			
		}
		DBConn conn=null;
		try{
			String hql=" from AfValidateformula afv where afv.templateId='"
				+ templateId + "' and " + "afv.versionId='"
				+ versionId + "'  and " + "afv.formulaValue='"
					+ formulaValue + "'";
			
			conn=new DBConn();
			List list=conn.openSession().find(hql);
			if(list!=null && list.size()>0){
				result=true;
			}
			}catch(Exception he){
	    		if(conn != null) conn.endTransaction(true);
	    	}finally{
	    		if(conn != null) conn.closeSession();
	    	}
			return result;
	}
	//进行删除操作
	public static boolean deletecheck(String templateId,String versionId,String formulaId) {

		boolean result = true;
		DBConn conn = null;
		Session session = null;

		if (templateId == null&&versionId == null) {
			return false;
		}
		try {
			conn = new DBConn();
			session = conn.beginTransaction();

			AfValidateformula af = null;

			af = (AfValidateformula) session.load(AfValidateformula.class, Long.valueOf(formulaId));
			
			session.delete(af);
			session.flush();
			result = true;
		} catch (Exception e) {
			log.printStackTrace(e);
			result = false;
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}

}
