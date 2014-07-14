package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfTemplateOuterRep;
import com.fitech.gznx.po.AfTemplateOuterRepId;
/**
 * �ϱ���Χ�趨������
 * @author Administrator
 *
 */
public class StrutsTemplateOrgOuterRelationDelegate {

	private static FitechException log = new FitechException(
			StrutsTemplateOrgOuterRelationDelegate.class);
    
    /**
     * Ӱ�����AfTemplateOrgRelation
     * �����ӱ���ID�Ͱ汾�Ż�ȡ�䱨�ͷ�Χ��Ϣ�б�
     * @param childRepId String �ӱ���ID
     * @param versionId String �汾�� 
     * @return List ����Ϣ�б�����null
     */
    public static List findAll(String templateId, String versionId){
    	List resList=new ArrayList();;
    	
    	if(templateId==null || versionId==null) return null;
    	
    	DBConn conn=null;
    	
    	try{
    		String hql="from AfTemplateOuterRep mrr where mrr.id.templateId='" + templateId + 
    			"' and mrr.id.versionId='" + versionId + "'";
    		conn=new DBConn();
    		Session session = conn.openSession();
    		Query query = session.createQuery(hql);
    		resList = query.list(); 
    	}catch(Exception e){
    		log.printStackTrace(e);
    	}finally{
    		if(conn!=null) conn.closeSession();
    	}
    	
    	return resList;
    }
    
    /**
     *�Ƴ�orgId�¼�����
     * @param childRepid
     * @return
     * @author Administrator xsf
     */
    public static boolean removeLowerOrg(String  childRepId,String versionId, String orgId)
    {
    	boolean result=false;
    	DBConn conn=null;
    	Session session=null;
    	
    	try{
    		conn=new DBConn();
    		session=conn.beginTransaction();
    		
    		String hql=" from AfTemplateOuterRep mrr where";
        	if(versionId!=null && !versionId.equals("")){
        		hql+=" mrr.id.versionId='"+versionId+"'";
        	}
        	if(childRepId!=null && !childRepId.equals("")){
        		hql+=" and mrr.id.templateId='"+childRepId+"'";
        	}        	
        	session.delete(hql.toString());
        	
        	session.flush();
    		result=true;
    		
    	}catch(HibernateException he){
    		log.printStackTrace(he);
    	}catch(Exception e){
    		log.printStackTrace(e);
    	}finally{
    		if (conn!=null)conn.endTransaction(result);
    	}
    	return result;
    }
    
    
    /**
     * ͨ��id�õ�Ψһ�Ķ���
     * @param childRepId
     * @param versionId
     * @return
     */
    public static AfTemplateOuterRep findById(String  childRepId,String versionId,String orgId)
    {
    	boolean result=false;
    	DBConn conn=null;
    	Session session=null;
    	List list = new ArrayList();
    	AfTemplateOuterRep o = null;
    	try{
    		conn=new DBConn();
    		session=conn.openSession();
    		String hql=" from AfTemplateOuterRep mrr where";
        	if(versionId!=null && !versionId.equals("")){
        		hql+=" mrr.id.versionId='"+versionId+"'";
        	}
        	if(childRepId!=null && !childRepId.equals("")){
        		hql+=" and mrr.id.templateId='"+childRepId+"'";
        	} 
        	if(childRepId!=null && !childRepId.equals("")){
        		hql+=" and mrr.id.orgId='"+orgId+"'";
        	}     
        	Query query  = session.createQuery(hql);
        	list = query.list();
        	o = (AfTemplateOuterRep)list.get(0);
    	}catch(HibernateException he){
    		log.printStackTrace(he);
    	}catch(Exception e){
    		log.printStackTrace(e);
    	}finally{
    		if (conn!=null)conn.endTransaction(result);
    	}
    	return o;
    }
    
    /**��һ��ģ�����ñ��ͷ�Χ*/
    public static void updateBSSD(String childRepId,String versionId,ArrayList orgIds)
    {
    	if(childRepId==null) return;
    	if(versionId==null) return;
    	if(orgIds==null) return;
    	if(childRepId.equals("")) return;
    	if(versionId.equals("")) return;
    	if(orgIds.size()==0) return;
    	DBConn conn=new DBConn();
    	Session session=conn.beginTransaction();
    	AfTemplateOuterRepId comp_id=new AfTemplateOuterRepId();
    	comp_id.setTemplateId(childRepId);
    	comp_id.setVersionId(versionId);
    	
    	if(orgIds != null && orgIds.size() > 0){
    		for(int i=0;i<orgIds.size();i++)
        	{
    			AfTemplateOuterRep mrr=new AfTemplateOuterRep();
        		comp_id.setOrgId((String)orgIds.get(i));
        		mrr.setId(comp_id);
        		//����Ƿ����ظ���¼
        		if(!recordIsExist(comp_id)){
    	    		try {
    					session.save(mrr);
    					session.flush();
    				} catch (HibernateException e) {
    					e.printStackTrace();
    				}
        		}
        	}
        	conn.endTransaction(true);
        	if(conn!=null)
        	conn.closeSession();
    	}
    }
   
    /**����Ƿ����ظ���¼*/
    public static boolean recordIsExist(AfTemplateOuterRepId comp_id)
    {
    	boolean flag=true;
    	if(comp_id==null) return flag;
    	DBConn conn=new DBConn();
    	Session session=conn.openSession();
    	StringBuffer hql=new StringBuffer();
    	hql.append("from AfTemplateOuterRep mrr where mrr.id.orgId='");
    	hql.append(comp_id.getOrgId());
    	hql.append("' and mrr.id.templateId='");
    	hql.append(comp_id.getTemplateId());
    	hql.append("' and mrr.id.versionId='");
    	hql.append(comp_id.getVersionId());
    	hql.append("'");
    	
    	List list=null;
    	try {
			list=session.find(hql.toString());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn!=null) conn.closeSession();
		}
		if(list==null){
			flag=false;
		}
		if(list.size()==0){
			flag=false;
		}
    	return flag;
    }

//	public static List getPreOrgRepRange(String orgId, String reportFlg) {
//		List retVals = null;
//		DBConn conn = null;
//		try {
//			String hql = "select  t.id.templateId,t.id.versionId,t.templateName" 
//						 + " from AfTemplate t where  t.templateType=" +reportFlg+
//						 		//" and mrr.id.orgId=(select on.preOrgId from OrgNet on where on.orgId='" + orgId 
//						  " order by t.id.templateId,t.id.versionId";			
//			conn = new DBConn();
//			Session session = conn.openSession();
//			Query query = session.createQuery(hql);
//			List list = query.list();
//			if (list != null && list.size() > 0) {							
//				retVals = new ArrayList();
//				for(Iterator iter = list.iterator();iter.hasNext();){
//					Object[] object = (Object[]) iter.next();					
//					retVals.add(new LabelValueBean("[" + (String) object[0] + "_" + (String) object[1] + "]" + (String) object[2],(String) object[0] + "-" + (String) object[1]+"-2"));	
//				}
//			}
//		} catch (HibernateException he) {
//			retVals = null;
//			log.printStackTrace(he);
//		} catch (Exception e) {
//			retVals = null;
//			log.printStackTrace(e);
//		} finally {
//			if (conn != null)
//				conn.closeSession();
//		}
//
//		return retVals;
//	}
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * �鿴�û����Ƿ����ù����ܷ�Χ
	 * @param orgId
	 * @return
	 */
	public static int getRepRangeCountByOrg(String orgId){
		int result = 0;	    
		DBConn conn = null;	    
		Session session = null;
	    
		try{	    
			if(orgId != null){	        
				conn = new DBConn();	            
				session = conn.openSession();

				Query query = session.createQuery("select count(*) from AfTemplateOuterRep mrr where mrr.id.orgId='"+orgId+"'");	            
				List list = query.list();
				if(list!=null && list.size()!=0){	            
					result = ((Integer)list.get(0)).intValue();	               
				}	            
			}	        
		}catch(Exception e){	        
			result = 0;	        
			log.printStackTrace(e);	        
		}finally{	       
			if(conn!=null)	        
				conn.closeSession();	    
		}
		return result;
	}
	
	/***ɾ���û��������б�����ܹ�ϵ
	 * ���� 2013-3-21
	 * @param orgId
	 * @return
	 */
	public static boolean deleteCollRalationgByOrgId(String orgId) {
	
		boolean result = false;	    
		DBConn conn = null;	    
		Session session = null;
	    
		try{	    
			if(orgId != null){
	        
				conn = new DBConn();	            
				session = conn.beginTransaction();

				String hql = "from AfTemplateOuterRep mrr where mrr.id.orgId='"+orgId+"'";	            
				session.delete(hql);	            
				result = true; 	            
			}
		}catch(Exception e){	    
			result = false;	        
			log.printStackTrace(e);	        
		}finally{	       
			if(conn!=null)	        
				conn.endTransaction(result);	       
		}	    
		return result;
	}
	
	/**
	 * 
	 * @param reportFlg
	 * @return
	 * Administrator
	 * 2013-3-21 ����01:52:25
	 * List
	 */
	public static List getPreOrgRepRange(String reportFlg) {
		List retVals = null;
		DBConn conn = null;
		try {
			String hql = "select  t.id.templateId,t.id.versionId,t.templateName" 
						 + " from AfTemplate t where  t.templateType=" +reportFlg+
						 		//" and mrr.id.orgId=(select on.preOrgId from OrgNet on where on.orgId='" + orgId 
						  " order by t.id.templateId,t.id.versionId";			
			conn = new DBConn();
			Session session = conn.openSession();
			Query query = session.createQuery(hql);
			List list = query.list();
			if (list != null && list.size() > 0) {							
				retVals = new ArrayList();
				for(Iterator iter = list.iterator();iter.hasNext();){
					Object[] object = (Object[]) iter.next();					
					retVals.add(new LabelValueBean("[" + (String) object[0] + "_" + (String) object[1] + "]" + (String) object[2],(String) object[0] + "-" + (String) object[1]+"-1"));	
				}
			}
		} catch (HibernateException he) {
			retVals = null;
			log.printStackTrace(he);
		} catch (Exception e) {
			retVals = null;
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return retVals;
	}
	
}
