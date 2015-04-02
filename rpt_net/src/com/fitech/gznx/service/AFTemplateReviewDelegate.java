package com.fitech.gznx.service;

import java.util.List;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.entity.AfTemplateReview;

public class AFTemplateReviewDelegate {
	public static List<AfTemplateReview> findAFTemplateReview(AfTemplateReview review) throws Exception{
		if(review==null)
			return null;
		
		//ƴ�Ӳ�ѯSQL
		String hql = "from AfTemplateReview t where 1=1 ";
		//ģ��ID
		if(review.getId()!=null && review.getId().getTemplateId()!=null && !review.getId().getTemplateId().equals(""))
			hql += " and t.id.templateId='"+review.getId().getTemplateId()+"'";
		//�汾��
		if(review.getId()!=null && review.getId().getVersionId()!=null && !review.getId().getVersionId().equals(""))
			hql += " and t.id.versionId='"+review.getId().getVersionId()+"'";
		if(review.getId()!=null && review.getId().getTerm()!=null && !review.getId().getTerm().equals(""))
			hql += " and t.id.term='"+review.getId().getTerm()+"'";
		//��������
		if(review.getReviewDate()!=null && !review.getReviewDate().equals(""))
			hql += " and t.reviewDate='"+review.getReviewDate()+"'";
		//����״̬
		if(review.getReviewStatus()!=null && !review.getReviewStatus().equals(""))
			hql += " and t.reviewStatus='"+review.getReviewStatus()+"'";
		
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			//��ѯ
			List<AfTemplateReview> reviewList = session.createQuery(hql).list();
			
			return reviewList;//���ؼ���
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
