package com.cbrc.smis.service.impl;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.entity.AFForceRep;
import com.cbrc.smis.form.AFForceRepForm;
import com.cbrc.smis.service.IAFForceRepService;

public class AFForceRepServiceImpl implements IAFForceRepService {
	private DBConn dbConn = null;
	private Session session = null;
	
	@Override
	public void addAFForceRep(AFForceRepForm repForm) throws Exception {
		if(repForm==null){
			throw new Exception("无选中机构");
		}
		Integer repInId = repForm.getRepInId();
		if(repInId==null || repInId.equals(""))
			throw new Exception("无选中机构");
		open();
		dbConn.beginTransaction();
		
		AFForceRep rep = new AFForceRep();
		rep.setRepInId(Integer.valueOf(repInId));//设置报表ID
		rep.setForceType(Config.CELL_CHECK_BETWEEN);//公式类型为表间校验
		session.save(rep);

		dbConn.endTransaction(true);
		
			

	}
	
	@Override
	public void deleteAFForceRep(Integer repInId) throws Exception {
		AFForceRep rep = new AFForceRep();
		rep.setRepInId(repInId);
		open();
		dbConn.beginTransaction();
		try {
			session.delete(rep);
			dbConn.endTransaction(true);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public AFForceRep findAFForceRepByRepInId(Integer repInId) throws Exception {
		if(repInId==null)
			throw new Exception();
		open();
		AFForceRep rep = null;
		try {
			String hql = "from AFForceRep a where a.repInId="+repInId;
			Query query = session.createQuery(hql);
			rep = (AFForceRep)query.uniqueResult();
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new Exception("该类可能不存在");
		}
		
		return rep;
	}

	
	
	public void open(){
		dbConn = new DBConn();
		session = dbConn.openSession();
	}
	
	public void close(){
		try {
			if(session.isOpen())
				session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		dbConn = null;
	}
}
