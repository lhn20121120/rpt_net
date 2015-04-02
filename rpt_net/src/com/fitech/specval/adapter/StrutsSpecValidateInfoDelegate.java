package com.fitech.specval.adapter;

import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.ExchangeRate;
import com.fitech.specval.form.SpecialValForm;
import com.fitech.specval.po.SpecValidateInfo;

public class StrutsSpecValidateInfoDelegate {
	
	private static FitechException log = new FitechException(
			StrutsSpecValidateInfoDelegate.class);
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：SpecValidateInfo
	 * 分页浏览查询校验
	 * @param rate				ExchangeRate		汇率
	 * @param apartPage			ApartPage			分页对象
	 * @return		List
	 */
	public static List getSpecValidateInfos(SpecialValForm dataForm ,ApartPage apartPage){
		
		List specValLst = null;
		DBConn dbConn = null;
		Session session = null;
		
		try {
			dbConn = new DBConn();
			session = dbConn.openSession();
			StringBuffer sbfHql = new StringBuffer("from SpecValidateInfo t ");
			StringBuffer sbWhere=new StringBuffer("");
			
			if(dataForm != null){
				if(dataForm.getValName() != null && !dataForm.getValName().trim().equals("")){
					sbWhere.append(sbWhere.toString().equals("")?"":" and ")
						.append(" t.valName='").append(dataForm.getValName()).append("'");
				}

				if(!sbWhere.toString().equals(""))
					sbfHql.append(" where ").append(sbWhere.toString());
			}
			
			Query query = session.createQuery("select count(t.speValId) " + sbfHql.toString());
			
			specValLst = query.list();
			
			int count=0;
			if(specValLst!=null && specValLst.size()==1){
				count=((Integer)specValLst.get(0)).intValue();
			}
			
			int offset=0;
			if(apartPage!=null){
				offset = (apartPage.getCurPage() - 1) * Config.PER_PAGE_ROWS;
				apartPage.setCount(count);
			}
			
			sbfHql.append(" order by t.speValId desc");
			
			query = session.createQuery(sbfHql.toString());
			query.setFirstResult(offset);
			query.setMaxResults(Config.PER_PAGE_ROWS);
			
			specValLst = query.list();
			
		} catch (Exception e) {
			specValLst = null;
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)	dbConn.closeSession();
		}
		return specValLst;
	}
	
	/**
	 * 姓增或修改一个校验
	 * @param exchangeRate		ExchangeRate		汇率
	 * @return	新增或修改成功返回	true	失败返回 false
	 */
	public static boolean saveOrUpdateSpecVal(SpecValidateInfo specValInfo,String todo){
		boolean insert = false;
		DBConn dbConn = null;
		Session session = null;
		try {
			if(specValInfo != null){
				dbConn = new DBConn();
				session = dbConn.beginTransaction();
				
				if(todo.trim().equals("new")){
						session.save(specValInfo);
						session.flush();
						insert = true;
				}else{
					session.update(specValInfo);
					session.flush();
					insert = true;
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.endTransaction(insert);
		}
		return insert;
	}

	/**
	 * 通过汇率的主键查找汇率
	 * @param pId		Long	汇率Id
	 * @return	ExchangeRate
	 */
	public static SpecValidateInfo getSpecValById(Long pId){
		
		SpecValidateInfo specValInfo = null;
		
		DBConn dbConn = null;
		Session session = null;
		
		try {
			if(pId != null){
				dbConn = new DBConn();
				session = dbConn.openSession();
				String hql = "from SpecValidateInfo e where e.speValId=:speValId";
				Query query = session.createQuery(hql);
				query.setLong("speValId",pId.longValue());
				List list = query.list();
				if(list != null) specValInfo = (SpecValidateInfo)list.get(0);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.closeSession();
		}
		return specValInfo;
	}
	
	/**
	 * 批量删除校验规则
	 * @param pId		Long[]			 校验Id 数组
	 * @return	成功返回	true 	失败返回	false
	 */
	public static boolean deleteSpecValById(String[] pId){
		
		boolean delete = false;
		
		DBConn dbConn = null;
		Session session = null;
		
		try {
			if (pId != null) {
				dbConn = new DBConn();
				session = dbConn.beginTransaction();
				
				int length = pId.length;
				
				for (int i = 0; i < length; i++) {
					//获取要删除的汇率
					SpecValidateInfo specValInfo = (SpecValidateInfo) session.get(SpecValidateInfo.class, new Long(pId[i]));
					if (specValInfo != null) {
						session.delete(specValInfo);
					} else {
						delete = false;
						break;
					}
				}
				
				if (!delete) {
					session.flush();
					delete = true;
				}
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (dbConn != null)
				dbConn.endTransaction(delete);
		}
		return delete;
	}
}
