package com.fitech.net.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.IDataRelationForm;
import com.fitech.net.hibernate.IDataRelation;

/**
 * 数据关联表DELEGATE
 * 
 * @author LQ
 */

public class StrutsIDataRelationDelegate
{
	private static FitechException log = new FitechException(StrutsIDataRelationDelegate.class);

	public static List find(String childRepId, String versionId)
	{
		List resList = null;

		if (childRepId == null || versionId == null)
			return resList;

		DBConn conn = null;
		try
		{
			String sql = "from IDataRelation idr where idr.idrId in(select mc.cellId from MCell mc where mc.MChildReport.comp_id.childRepId='"
					+ childRepId + "' and mc.MChildReport.comp_id.versionId='" + versionId + "') order by idr.idrId";
			conn = new DBConn();
			List list = conn.openSession().find(sql);
			if (list != null && list.size() > 0)
			{
				resList = new ArrayList();
				for (int i = 0; i < list.size(); i++)
				{
					IDataRelationForm form = new IDataRelationForm();
					form.setModify(false);
					TranslatorUtil.copyVoToPersistence((IDataRelation) list.get(i), form);
					resList.add(form);
				}
			}
		}
		catch (HibernateException he)
		{
			resList = null;
			log.printStackTrace(he);
		}
		catch (Exception e)
		{
			resList = null;
			log.printStackTrace(e);
		}
		finally
		{
			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}

	public static boolean save(HashMap hm){
		DBConn conn = null;
		Session session = null;
		boolean result = false;
		Iterator it = hm.entrySet().iterator();
		List updateList = new ArrayList();
		conn = new DBConn();
		session = conn.beginTransaction();
		try{
			while (it.hasNext()){
				Map.Entry map = (Map.Entry) it.next();
				IDataRelationForm iDataRelationForm = (IDataRelationForm) map.getValue();
				if (iDataRelationForm.isModify()){
					IDataRelation iDataRelation = new IDataRelation();
					iDataRelation = (IDataRelation) session.get(IDataRelation.class, iDataRelationForm.getIdrId());
					if (iDataRelation != null)					
						updateList.add(iDataRelationForm);					
					else{
						iDataRelation=new IDataRelation();
						TranslatorUtil.copyPersistenceToVo(iDataRelationForm, iDataRelation);
						session.save(iDataRelation);
					}
				}
			}
			if(updateList.size()>0){
				for(int i=0;i<updateList.size();i++){
					IDataRelationForm iDataRelationForm = (IDataRelationForm)updateList.get(i);
					IDataRelation idr = new IDataRelation();
					idr = (IDataRelation) session.get(IDataRelation.class, iDataRelationForm.getIdrId());
					TranslatorUtil.copyPersistenceToVo(iDataRelationForm, idr);
					session.update(idr);
				}
			}
			result=true;
		}
		catch (HibernateException he){
			result=false;
			log.printStackTrace(he);
		}
		catch (Exception e){
			result=false;
			log.printStackTrace(e);
		}
		finally{
			if (conn != null)
				conn.endTransaction(result);
		}
		return true;
	}
}
