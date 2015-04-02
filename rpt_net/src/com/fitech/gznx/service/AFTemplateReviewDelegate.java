package com.fitech.gznx.service;

import java.util.List;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.entity.AfTemplateReview;

public class AFTemplateReviewDelegate {
	public static List<AfTemplateReview> findAFTemplateReview(AfTemplateReview review) throws Exception{
		if(review==null)
			return null;
		
		//拼接查询SQL
		String hql = "from AfTemplateReview t where 1=1 ";
		//模板ID
		if(review.getId()!=null && review.getId().getTemplateId()!=null && !review.getId().getTemplateId().equals(""))
			hql += " and t.id.templateId='"+review.getId().getTemplateId()+"'";
		//版本号
		if(review.getId()!=null && review.getId().getVersionId()!=null && !review.getId().getVersionId().equals(""))
			hql += " and t.id.versionId='"+review.getId().getVersionId()+"'";
		if(review.getId()!=null && review.getId().getTerm()!=null && !review.getId().getTerm().equals(""))
			hql += " and t.id.term='"+review.getId().getTerm()+"'";
		//审阅日期
		if(review.getReviewDate()!=null && !review.getReviewDate().equals(""))
			hql += " and t.reviewDate='"+review.getReviewDate()+"'";
		//审阅状态
		if(review.getReviewStatus()!=null && !review.getReviewStatus().equals(""))
			hql += " and t.reviewStatus='"+review.getReviewStatus()+"'";
		
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			//查询
			List<AfTemplateReview> reviewList = session.createQuery(hql).list();
			
			return reviewList;//返回集合
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}finally{
			if(conn != null) conn.closeSession();
		}
	}
	
	public static int findCount() throws Exception{
		String hql = "from AfTemplateReview";
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			return session.createQuery(hql).list().size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}finally{
			if(conn != null) conn.closeSession();
		}
	}
	
	public static void insertAfTemplateReview(AfTemplateReview review) throws Exception{
		DBConn conn = null;
		Session session = null;
		
		try {
			conn = new DBConn();
			
			session = conn.beginTransaction();
			session.save(review);
			
		} catch (Exception e) {
			throw e;
		}finally{
			if(conn != null) conn.endTransaction(true);
		}
	}
}
