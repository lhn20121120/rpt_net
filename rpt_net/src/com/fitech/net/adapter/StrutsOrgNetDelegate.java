
package com.fitech.net.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.AbnormityChange;
import com.cbrc.smis.hibernate.ColAbnormityChange;
import com.cbrc.smis.hibernate.MRepRange;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.po.AfOrg;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.MRegion;
import com.fitech.net.hibernate.OrgNet;
import com.fitech.net.hibernate.OrgType;
/**
 *@StrutsOrgNetDelegate  ����Delegate
 * 
 * @author jcm
 */
public class StrutsOrgNetDelegate {
	//Catch��������׳��������쳣
	private static FitechException log=new FitechException(StrutsOrgNetDelegate.class);

	/**
	 * ��������
	 * @param orgNetForm OrgNetForm
     * @exception Exception����׽�쳣���� 
     * @return int result  0--����ʧ��  1--�����ɹ�  2--Ҫ��ӵĻ��������Ѿ�����
     */
	public static int create (OrgNetForm orgNetForm) throws Exception {	   
		int result = 0;				//��result���	   
		boolean bool = false;

		OrgNet orgNetPersistence = new OrgNet();
	   	   	   
		if (orgNetForm == null || orgNetForm.getOrg_name().equals("")) 		
			return result;
	   	   
		//���Ӷ���ĳ�ʼ��	   
		DBConn conn=null;	   
		//�Ự����ĳ�ʼ��	   
		Session session=null;	   
		try{		
			TranslatorUtil.copyVoToPersistence(orgNetPersistence,orgNetForm);		   
			//ʵ�������Ӷ���		   
			conn =new DBConn();		   
			//�Ự����Ϊ���Ӷ������������		   
			session=conn.beginTransaction();
		   		   
			String hql = "from OrgNet ont where ont.orgId='" + orgNetForm.getOrg_id() + "'";		   
			List list = session.find(hql);		   
		  
			if(list != null && list.size() > 0){			
				result = 2;        //Ҫ���ӵĻ��������Ѿ�����			   
				bool = true;			   
				return result;		   
			}
				   
			//�Ự���󱣴�־ò����		  
			session.save(orgNetPersistence);		   
			session.flush();		   
//			//��������Ա��		   
//			MUserGrp mUserGrp = new MUserGrp();
//			OrgNet orgNet = new OrgNet();
//			orgNet.setOrgId(orgNetPersistence.getOrgId());
//			mUserGrp.setSetOrg(orgNet);		   
//			mUserGrp.setUserGrpNm(orgNetPersistence.getOrgName()+"ϵͳ������");		   
//			StrutsMUserGrpDelegate.create(mUserGrp);
//		   		   
//			//��������Ա�ʺ�		  
//			OperatorForm operatorForm = new OperatorForm();		   
//			operatorForm.setUserName(orgNetPersistence.getOrgId());			
//			operatorForm.setTitle(orgNetPersistence.getOrgName()+"ϵͳ����Ա");			
//			operatorForm.setFirstName("ϵͳ����Ա");		
//			operatorForm.setPassword("123456");			
//			operatorForm.setOrgId(orgNetPersistence.getOrgId());			
//			Long user_id = StrutsOperatorDelegate.create(operatorForm,new Integer(0));
//			
//			//���������û���ϵ
//			MUserToGrpForm mUserToGrpForm = new MUserToGrpForm();			  
//			mUserToGrpForm.setUserId(user_id);			
//			mUserToGrpForm.setUserGrpId(mUserGrp.getUserGrpId());			
//			mUserToGrpForm.setSelectedUserGrpIds(mUserGrp.getUserGrpId().toString());			
//			StrutsMUserToGrpDelegate.insert(mUserToGrpForm);   
			
//			String lowerOrgIds = "";selectLowerOrgIds(orgNetPersistence.getOrgId());		   
//			String childRepIds = "";		   
//			List reportList = StrutsMChildReportDelegate.getAllReports();		   
//			if(reportList != null && reportList.size() > 0){			
//				for(int i=0;i<reportList.size();i++){				
//					MChildReportForm form = (MChildReportForm)reportList.get(i);				   
//					childRepIds = childRepIds.equals("") ? form.getChildRepId() : childRepIds +","+form.getChildRepId();			   
//				}		   
//			}		   
//			StrutsMPurViewDelegate.insertUserGrpPopedom(mUserGrp.getUserGrpId(),lowerOrgIds,childRepIds);
		   		   
//			//����ϵͳ�����ɫ		   
//			Role role = new Role();		   
//			role.setRoleName(orgNetPersistence.getOrgName()+"ϵͳ����");		   
//			role.setSetOrgId(orgNetPersistence.getOrgId());		   
//			StrutsRoleDelegate.create(role);
//		   		   
//			//�����û����ɫ�Ĺ�ϵ		   
//			UserRoleForm roleForm = new UserRoleForm();		   
//			roleForm.setUserId(user_id);		   
//			roleForm.setRoleId(role.getRoleId());		   
//			roleForm.setSelectedRoleIds(role.getRoleId().toString());		   
//			StrutsUserRoleDelegate.insert(roleForm);
//		   		   
//			//�����ý�ɫ���Է��ʵĲ˵�
//			List menuList = StrutsToolSettingDelegate.findAll();
//			if(menuList != null && menuList.size() > 0){
//				String menuStrings = "";
//				for(int i=0;i<menuList.size();i++){
//					ToolSettingForm form = (ToolSettingForm)menuList.get(i);				   
//					menuStrings = menuStrings.equals("") ? form.getMenuId().toString() : menuStrings + "," + form.getMenuId();			   
//				}
//				RoleToolForm form = new RoleToolForm();
//				form.setRoleId(role.getRoleId());
//				form.setSelectedMenuIds(menuStrings);
//				StrutsRoleToolDelegate.insert(form);
//			}
			result=1;       //���ӳɹ�		   
			bool = true;
		}catch(HibernateException e){		
			//�־ò���쳣����׽		   
			result = 0;		   
			bool = false;		   
			log.printStackTrace(e);	   
		}finally{		
			//�������״̬��,��Ͽ�,��������,����		   
			if(conn!=null) conn.endTransaction(bool);	   
		}	   
		return result;
	}    
   
	/**
	 * ��������
	 * @param orgNetForm OrgNetForm
     * @exception Exception����׽�쳣���� 
     * @return int result  0--����ʧ��  1--�����ɹ�  2--Ҫ��ӵĻ��������Ѿ�����
     */
	public static int create (OrgNetForm orgNetForm,String orgId) throws Exception {
		int result = 0;				//��result���	   
		boolean bool = false;	   
		OrgNet orgNetPersistence = new OrgNet();

		if (orgNetForm == null || orgNetForm.getOrg_name().equals("")) 		
			return result;	   
	   
		//���Ӷ���ĳ�ʼ��	   
		DBConn conn=null;	   
		//�Ự����ĳ�ʼ��	   
		Session session=null;
	   
		try{		
			//ʵ�������Ӷ���		   
			conn =new DBConn();		   
			//�Ự����Ϊ���Ӷ������������		   
			session=conn.beginTransaction();		   
		   
			String hql = "from OrgNet ont where ont.orgId='" + orgNetForm.getOrg_id() + "'";		   
			List list = session.find(hql);

			if(list != null && list.size() > 0){			
				result = 2;        //Ҫ���ӵĻ��������Ѿ�����			  
				bool = true;			   
				return result;		   
			}
			
			//�Ự���󱣴�־ò����		   
			TranslatorUtil.copyVoToPersistence(orgNetPersistence,orgNetForm);		   
			session.save(orgNetPersistence);		   
			session.flush();
			
			result=1;       //���ӳɹ�		   
			bool = true;		   	   
		}catch(HibernateException e){		
			//�־ò���쳣����׽		   
			result = 0;		   
			bool = false;		   
			log.printStackTrace(e);	   
		}finally{		
			//�������״̬��,��Ͽ�,��������,����		   
			if(conn!=null) conn.endTransaction(bool);	   
		}	   
		return result;    
	}

	/**
	 * ȡ�ð�������ѯ���ļ�¼����
	 * @return int ��ѯ���ļ�¼����	
	 * @param   orgNetForm   ������ѯ��������Ϣ������ID���������ƣ�
	 */
	public static int getRecordCount(OrgNetForm orgNetForm){
	   
		int count=0;

		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn=null;		   
		Session session=null;

		//��ѯ����HQL������		
		StringBuffer hql = new StringBuffer("select count(*) from OrgNet ont");								
		StringBuffer where = new StringBuffer("");

		if (orgNetForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if (orgNetForm.getRegion_name() != null && !orgNetForm.getRegion_name().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");		
		}

		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString());	    	
			//conn�����ʵ����		  	    	
			conn=new DBConn();	    	
			//�����ӿ�ʼ�Ự	    	
			session=conn.openSession();	    	
			List list=session.find(hql.toString());	    	
			if(list!=null && list.size()==1){	    	
				count=((Integer)list.get(0)).intValue();	    	
			}	      
		}catch(HibernateException he){	    
			log.printStackTrace(he);	      
		}catch(Exception e){	    
			log.printStackTrace(e);	      
		}finally{	    
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn!=null) conn.closeSession();	      
		}	    
		return count;   
	}


	/**
	 * ȡ�ð�������ѯ���ļ�¼����
	 * @return int ��ѯ���ļ�¼����	
	 * @param   orgNetForm   ������ѯ��������Ϣ������ID���������ƣ�
	 */
	public static int getRecordCount(OrgNetForm orgNetForm,Operator operator){	   
		int count=0;
	
		//���Ӷ���ͻỰ�����ʼ��		  
		DBConn conn=null;
		Session session=null;

		//��ѯ����HQL������		
		StringBuffer hql = new StringBuffer("select count(*) from OrgNet ont");								
		StringBuffer where = new StringBuffer("");

		if (orgNetForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if (orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");		  
		}
		where.append((where.toString().equals("") ? "" : " and ") + "ont.setOrgId = '" + operator.getOrgId() + "'");
		
		try {		
			hql.append((where.toString().equals("")?"":" where ") + where.toString());			
			//conn�����ʵ����			
			conn=new DBConn();			
			//�����ӿ�ʼ�Ự			
			session=conn.openSession();			
			List list=session.find(hql.toString());			
			if(list!=null && list.size()==1){			
				count=((Integer)list.get(0)).intValue();			  
			}
		}catch(HibernateException he){		
			log.printStackTrace(he);		   
		}catch(Exception e){			
			log.printStackTrace(e);		   
		}finally{			
			//������Ӵ��ڣ���Ͽ��������Ự������			
			if(conn!=null) conn.closeSession();		  
		}		   
		return count;
	}
	
	/**
	 * ������ѯ
	 * @param orgNetForm OrgNetForm ��ѯ������
	 * @param offset
	 * @param limit
	 * @return List ������ҵ���¼������List��¼�������򣬷���null
	 */
	public static List select(OrgNetForm orgNetForm,int offset,int limit){
	   	   
		//List���ϵĶ��� 	   
		List refVals = null;		

		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;					   
		Session session = null;

		//��ѯ����HQL������	   
		StringBuffer hql = new StringBuffer("from OrgNet ont");								   
		StringBuffer where = new StringBuffer("");
		
		if (orgNetForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if (orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");		
		}
		
		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString());	    	
			//conn�����ʵ����		  	    	
			conn = new DBConn();	    	
			//�����ӿ�ʼ�Ự	    	
			session = conn.openSession();			
			Query query = session.createQuery(hql.toString());			
			query.setFirstResult(offset).setMaxResults(limit);			
			List list = query.list();

			if (list != null){	    	
				refVals = new ArrayList();	    		
				//ѭ����ȡ���ݿ����������¼			      
				for(Iterator it = list.iterator(); it.hasNext(); ) {			    
					OrgNetForm orgNetFormTemp = new OrgNetForm();	    	
					OrgNet orgNetPersistence = (OrgNet)it.next();			    	
					TranslatorUtil.copyPersistenceToVo(orgNetPersistence, orgNetFormTemp);			    	
					refVals.add(orgNetFormTemp);			     
				}	    	   
			}	      
		}catch(HibernateException he){	    
			refVals = null;	    	
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	    	
			refVals = null;  	    	  
			e.printStackTrace();	    	
			log.printStackTrace(e);	      
		}finally{	    	  
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	      
		}	    
		return refVals;   
	}
	
	/**
	 * ������ѯ
	 * @param orgNetForm OrgNetForm ��ѯ������
	 * @param offset
	 * @param limit
	 * @param operator
	 * @return List ������ҵ���¼������List��¼�������򣬷���null
	 */   
	public static List select(OrgNetForm orgNetForm,int offset,int limit,Operator operator){
		//List���ϵĶ��� 	   
		List refVals = null;
		
		//���Ӷ���ͻỰ�����ʼ��	   
		DBConn conn = null;	   
		Session session = null;
	   	   
		//��ѯ����HQL������	   
		StringBuffer hql = new StringBuffer("from OrgNet ont");								   
		StringBuffer where = new StringBuffer("");		
		
		if (orgNetForm != null) {		
			// �����������ж�,�������Ʋ���Ϊ��			
			if (orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");		
		}				
		where.append((where.toString().equals("") ? "" : " and ") + "ont.setOrgId = '" + operator.getOrgId() + "'");

		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString());	    	
			//conn�����ʵ����		    	
			conn = new DBConn();	    	
			//�����ӿ�ʼ�Ự	    	
			session = conn.openSession();			
			Query query = session.createQuery(hql.toString());			
			query.setFirstResult(offset).setMaxResults(limit);			
			List list = query.list();

			if (list != null){	    	
				refVals = new ArrayList();	    		
				//ѭ����ȡ���ݿ����������¼			    
				for(Iterator it = list.iterator(); it.hasNext(); ) {			    
					OrgNetForm orgNetFormTemp = new OrgNetForm();			    	
					OrgNet orgNetPersistence = (OrgNet)it.next();			        
					TranslatorUtil.copyPersistenceToVo(orgNetPersistence, orgNetFormTemp);
			        refVals.add(orgNetFormTemp);			     
				}	    	  
			}	      
		}catch(HibernateException he){	    
			refVals = null;	    	
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	    
			refVals = null;  	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	      
		}finally{	    	
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return refVals;   
	}
   	/**
	 *<p>����:��ѯ�ӻ�������</p>
	 *<p>����:</p>
	 *<p>���ڣ�2008-1-3</p>
	 *<p>���ߣ��ܷ���</p>
	 */ 
	public static int selectSubOrgCount(OrgNetForm orgNetForm,Operator operator){
		int count=0;  
		DBConn conn = null;	   
		Session session = null;
		
		if(orgNetForm == null) return count;
		try {
			StringBuffer hql = new StringBuffer("select count(*) from OrgNet ont where ont.orgId in (" 
					+ operator.getChildRepSearchPopedom().replace("orgRepId", "orgId") +")");
			
			StringBuffer where = new StringBuffer();
			
			//����������Ʋ�ѯ����		
			if (orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals(""))			
				where.append("and ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");
			
			hql.append(where.toString());
			
			conn = new DBConn();	    	
			session = conn.openSession();			
			Query query = session.createQuery(hql.toString());				
			List list = query.list();

			if(list!=null && list.size()==1){			
				count=((Integer)list.get(0)).intValue();			  
			}     
		}catch(HibernateException he){	    
				
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	 	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	      
		}finally{	    	
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return count;   
	}
   	/**
	 *<p>����:��ѯ�ӻ�������</p>
	 *<p>����:</p>
	 *<p>���ڣ�2008-1-3</p>
	 *<p>���ߣ��ܷ���</p>
	 */ 
	public static int selectOrgNameCount(OrgNetForm orgNetForm){
		int count=0;  
		DBConn conn = null;	   
		Session session = null;
		
		if(orgNetForm == null) return count;
		try {  
			String hql = "select count(*) from OrgNet ont where ont.orgName like '%" + orgNetForm.getOrg_name() + "%'";	
			
			conn = new DBConn();	    	
			session = conn.openSession();			
			Query query = session.createQuery(hql);				
			List list = query.list();

			if(list!=null && list.size()==1){			
				count=((Integer)list.get(0)).intValue();			  
			}     
		}catch(HibernateException he){	    
				
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	 	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	      
		}finally{	    	
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return count;   
	}
	/**
	 *<p>����:��ѯ�ӻ���</p>
	 *<p>����:</p>
	 *<p>���ڣ�2008-1-3</p>
	 *<p>���ߣ��ܷ���</p>
	 */ 
	public static List selectSubOrg(OrgNetForm orgNetForm,int offset,int limit){
		if(orgNetForm == null) return null;
		
		List refVals = null;
		DBConn conn = null;	   
		Session session = null;
		
		try {
			//��ѯ����HQL������
			StringBuffer hql = new StringBuffer("from OrgNet ont where ont.preOrgId='" 
					+ orgNetForm.getPre_org_id() + "'");								   
			StringBuffer where = new StringBuffer();		
			
			//����������Ʋ�ѯ����		
			if (orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals(""))			
				where.append("and ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");
			where.append(" order by ont.orgName");
			hql.append(where.toString());
				
			conn = new DBConn();	    	
			session = conn.openSession();			
			Query query = session.createQuery(hql.toString());			
			query.setFirstResult(offset).setMaxResults(limit);			
			List list = query.list();

			if (list != null){	    	
				refVals = new ArrayList();	    		
				if(list != null && list.size() > 0){
					for(int i=0;i<list.size();i++){
						OrgNet orgNet = (OrgNet)list.get(i);
						refVals.add(orgNet);
					}
				}    	  
			}	      
		}catch(HibernateException he){	    
			refVals = null;	    	
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	    
			refVals = null;  	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	      
		}finally{	    	
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return refVals;   
	}
	/**
	 *<p>����:��ѯ�ӻ���</p>
	 *<p>����:</p>
	 *<p>���ڣ�2008-1-3</p>
	 *<p>���ߣ��ܷ���</p>
	 */ 
	public static List selectSubOrgByName(OrgNetForm orgNetForm,int offset,int limit){
		if(orgNetForm == null) return null;
		
		List refVals = null;
		DBConn conn = null;	   
		Session session = null;
		
		try {
			//��ѯ����HQL������
			String  hql = "from OrgNet ont where  ont.orgName like '%" + orgNetForm.getOrg_name() + "%' order by ont.orgName";

			conn = new DBConn();	    	
			session = conn.openSession();			
			Query query = session.createQuery(hql);			
			query.setFirstResult(offset).setMaxResults(limit);			
			List list = query.list();

			if (list != null){	    	
				refVals = new ArrayList();	    		
				if(list != null && list.size() > 0){
					for(int i=0;i<list.size();i++){
						OrgNet orgNet = (OrgNet)list.get(i);
						refVals.add(orgNet);
					}
				}    	  
			}	      
		}catch(HibernateException he){	    
			refVals = null;	    	
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	    
			refVals = null;  	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	      
		}finally{	    	
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return refVals;   
	}
	/**
	 * ����¼������б���Ϣ
	 * 
	 * @param orgNetForm
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List selectSubOrgList(OrgNetForm orgNetForm,Operator operator,int offset,int limit){
		if(orgNetForm == null) return null;
		
		List refVals = null;
		DBConn conn = null;	   
		Session session = null;
		
		try {
			//��ѯ����HQL������
			StringBuffer hql = new StringBuffer("from OrgNet ont where ont.orgId in ("
					+ operator.getChildRepSearchPopedom().replace("orgRepId", "orgId") +")");
			StringBuffer where = new StringBuffer();		
			
			//����������Ʋ�ѯ����		
			if (orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals(""))			
				where.append("and ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");
			where.append(" order by ont.orgId,ont.orgName");
			hql.append(where.toString());
				
			conn = new DBConn();	    	
			session = conn.openSession();			
			Query query = session.createQuery(hql.toString());			
			query.setFirstResult(offset).setMaxResults(limit);			
			List list = query.list();

			if (list != null){	    	
				refVals = new ArrayList();
				OrgNetForm orgNetFormTemp = null;
				if(list != null && list.size() > 0){
					for(int i=0;i<list.size();i++){
						OrgNet orgNetPersistence = (OrgNet)list.get(i);
						orgNetFormTemp = new OrgNetForm();
						TranslatorUtil.copyPersistenceToVo(orgNetPersistence, orgNetFormTemp);
						
						refVals.add(orgNetFormTemp);
					}
				}    	  
			}	      
		}catch(HibernateException he){	    
			refVals = null;	    	
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	    
			refVals = null;  	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	      
		}finally{	    	
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return refVals;   
	}
	
	/**
	 * ����OrgNetForm����
	 *
	 * @param   orgNetForm   OrgNetForm ������ݵĶ���
	 * @exception   Exception  ���OrgNetForm����ʧ��,��׽�׳��쳣
	 */
	public static boolean update (OrgNetForm orgNetForm) throws Exception {	
		boolean result = false;		
		DBConn conn = null;
		Session session = null;

		OrgNet orgNetPersistence = new OrgNet();

		if (orgNetForm == null) {
			return result;
		}
		try {
			if (orgNetForm.getOrg_name() == null
					|| orgNetForm.getOrg_name().equals("")) {
				return result;
			}
			conn = new DBConn();
			session = conn.beginTransaction();

			TranslatorUtil.copyVoToPersistence(orgNetPersistence,orgNetForm);
			session.update(orgNetPersistence);

			result = true;
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.endTransaction(result);
		}
		return result;
	}
     
	/**
	 * �༭����
	 * 
	 * @param orgNetForm  The OrgNetForm  �������ݵĴ���
	 * @exception Exception ��� OrgNetForm ����ʧ���׳��쳣.
	 */
	public static boolean  edit (OrgNetForm orgNetForm) throws Exception {	  
		boolean result = false;
		
		DBConn conn = null;	   
		Session session = null;

		OrgNet orgNetPersistence = new OrgNet ();	   
		orgNetForm = new OrgNetForm();

		if (orgNetForm == null ) return  result;	   	   
		try{		
			if (orgNetForm.getOrg_name() == null || orgNetForm.getOrg_name().equals(""))			
				return result;
		   
			conn = new DBConn();		   
			session = conn.beginTransaction();

			orgNetPersistence = (OrgNet)session.load(OrgNet.class, orgNetForm.getOrg_name());		   
			TranslatorUtil.copyVoToPersistence(orgNetPersistence, orgNetForm);
		}catch(HibernateException he){		
			log.printStackTrace(he);	  
		}finally {		
			if(conn!=null) conn.endTransaction(result);	   
		}	   
		return result;   
	}

	/**
	 * ɾ������
	 *
	 * @param   orgNetForm   OrgNetForm ��ѯ���Ķ���
	 * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
	 */  
	public static boolean remove (OrgNetForm orgNetForm) throws Exception {
		//�ñ�־result
		boolean result=false;
	   
		//���ӺͻỰ����ĳ�ʼ��	   
		DBConn conn=null;	   
		Session session=null;

		//orgNetForm�Ƿ�Ϊ��,����result	   
		if (orgNetForm == null || orgNetForm.getOrg_id() == null) 		
			return result;

		try{	    
			//	���Ӷ���ͻỰ�����ʼ��		   
			conn=new DBConn();		   
			session=conn.beginTransaction();

			OrgNet orgNet=(OrgNet)session.load(OrgNet.class,orgNetForm.getOrg_id());		   
			//�Ự����ɾ���־ò����		   
			session.delete(orgNet);		   
			session.flush();

			//ɾ���ɹ�����Ϊtrue		   
			result=true;		  
		}catch(HibernateException he){		 
			//��׽������쳣,�׳�		   
			log.printStackTrace(he);	   
		}finally{		
			//�����������Ͽ����ӣ������Ự������		   
			if (conn!=null) conn.endTransaction(result);	   
		}	   
		return result;   
	}

	/**
	 * ��ѯһ����¼
	 * @param orgNetForm OrgNetFormʵ��������
	 * @return OrgNetForm ����һ����¼
	 */
	public static OrgNetForm selectOne(OrgNetForm orgNetForm){
	
		DBConn conn = null;	   
		Session session = null;
		
		if(orgNetForm != null){		
			try{			
				conn = new DBConn();			   
				session = conn.openSession();			   
				OrgNet orgNetPersistence = (OrgNet)session.load(OrgNet.class,orgNetForm.getOrg_id());			   
				TranslatorUtil.copyPersistenceToVo(orgNetPersistence,orgNetForm);		   
			}catch(HibernateException he){			
				log.printStackTrace(he);		   
			}catch(Exception e){			
				log.printStackTrace(e);		   
			}finally{			
				//������Ӵ��ڣ���Ͽ��������Ự������			   
				if(conn != null) conn.closeSession();		   
			}	   
		}	   
		return orgNetForm;   
	}

	/**
	 * ��ѯһ����¼
	 * @param orgNetForm OrgNetFormʵ��������
     * @return OrgNetForm ����һ����¼
     */
	public static OrgNetForm selectOne(String orgId,boolean bool){
		OrgNetForm orgNetForm = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();					   
			session = conn.openSession();

			String hql = "from OrgNet ont where ont.orgId = '" + orgId.trim() + "'"; 
			List list = session.find(hql.toString());
			if(list != null && list.size() > 0){
				OrgNet orgNet = (OrgNet)list.get(0);
				orgNetForm=new OrgNetForm();
				TranslatorUtil.copyPersistenceToVo(orgNet,orgNetForm);
			}			
		}catch(HibernateException he){
			orgNetForm = null;
			log.printStackTrace(he);
		}catch(Exception e){
			orgNetForm = null ;
			log.printStackTrace(e);			
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetForm;
	}

	/**
	 * ��ѯ���м�¼
	 * @return List ��ѯ���ļ�¼����	
	 */
	public static List findAll () throws Exception {
				
		List refVals = null;
		DBConn conn = null;
		Session session = null;
		StringBuffer hql = new StringBuffer("from OrgNet");
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			List list = session.find(hql.toString());
			
			if(list != null){
				refVals = new ArrayList();
		//		OrgNet orgNetPersistence = null;
				for(Iterator iter = list.iterator();iter.hasNext();){
					OrgNetForm orgNetFormTemp = new OrgNetForm();
					OrgNet orgNetPersistence = (OrgNet)iter.next();
					TranslatorUtil.copyPersistenceToVo(orgNetPersistence,orgNetFormTemp);					
					refVals.add(orgNetFormTemp);
				}
			}
		}catch(HibernateException he){
			refVals = null;
			log.printStackTrace(he);
		}catch(Exception e){
			refVals = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return refVals;
	}
	
	/**
	 * ��ʹ��hibernate	���Ը� 2011-12-21
	 * Ӱ�����OrgNet
	 * ���ݻ���id��ѯ������Ϣ
	 * @param orgId ����id
	 * @return OrgNet
	 */
	public static OrgNet selectOne(String orgId){
		OrgNet orgNetResult = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.beginTransaction();
			
			String hql = "from OrgNet ont where ont.orgId = '" + orgId.trim() + "'"; 			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				orgNetResult = (OrgNet)list.get(0);
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			orgNetResult = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) 
				conn.closeSession();
		}
		return orgNetResult;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-27
	 * Ӱ�����OrgNet
	 * ���ݻ���id����ѯ�û����������ӻ���
	 * @param orgId
	 * @return
	 */
	public static String selectAllLowerOrgIds(String orgId){
		String allLowerOrgIds="";
		String lowerOrgIds = "'"+orgId.trim()+"'";
		DBConn conn = null;
		Session session = null;
		try{
			conn = new DBConn();
			session = conn.openSession();
			while(true){
				String temp=lowerOrgIds;
				lowerOrgIds="";
				String hql = "from OrgNet ont where ont.preOrgId in (" + temp + ")"; 
				
				List list = session.find(hql.toString());
							
				if(list != null && list.size() > 0){					
					for(int i=0;i<list.size();i++){
						OrgNet orgNet = (OrgNet)list.get(i);
						lowerOrgIds = lowerOrgIds.equals("") ? "'" + orgNet.getOrgId() + "'" : lowerOrgIds + ",'" + orgNet.getOrgId() + "'";
					}
				}
				if(lowerOrgIds.equals("")) break;
				else allLowerOrgIds = allLowerOrgIds.equals("") ? lowerOrgIds : allLowerOrgIds + "," + lowerOrgIds;
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			lowerOrgIds = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return allLowerOrgIds;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-11-21
	 * Ӱ�����OrgNet
	 * ���ݻ���id��ѯ�û�����һ���ӻ���
	 * @param orgId ����id
	 * @return String �����¼�����id�Ӵ�
	 */
	public static String selectLowerOrgIds(String orgId){
		
		String lowerOrgIds = "";
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from OrgNet ont where ont.preOrgId = '" + orgId.trim() + "'"; 
			
			List list = session.find(hql.toString());
						
			if(list != null && list.size() > 0){
				
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					lowerOrgIds = lowerOrgIds.equals("") ? "'" + orgNet.getOrgId() + "'" : lowerOrgIds + ",'" + orgNet.getOrgId() + "'";
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			lowerOrgIds = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return lowerOrgIds;
	}
	
	/**
	 * ȡ�õ�ǰ�������¼�����
	 * @param orgId
	 * @return
	 */
	public static String selectSubOrgIds(String orgId){
		
		String lowerOrgIds = "";
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from OrgNet ont where ont.preOrgId = '" + orgId.trim() + "'"; 
			
			List list = session.find(hql.toString());
						
			if(list != null && list.size() > 0){
				
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					lowerOrgIds = lowerOrgIds.equals("") ? "'"+orgNet.getOrgId()+"'"  : lowerOrgIds + "," + "'"+orgNet.getOrgId()+"'";
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			lowerOrgIds = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return lowerOrgIds;
	}
	
	/**
	 * �������쳣�仯���¼�����
	 * @param orgId
	 * @param childRepId
	 * @param versionId
	 * @return
	 */
	public static List selectLowerOrgListInAbnormityChange(String orgId,String childRepId,String versionId){
		
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();
						
			orgNetList = new ArrayList();
			
			String hql = "from OrgNet ont where ont.preOrgId = '" + orgId.trim() + 
			"'  and ont.orgId in ("+" select MR.comp_id.orgId from AbnormityChange MR where  MR.comp_id.childRepId='"+childRepId+"' and MR.comp_id.versionId='"+versionId+"'"+")"; 
			
			List list = session1.find(hql.toString());
			
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					orgNetList.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	public static List selectLowerOrgListInColAbnormityChange(String orgId,String childRepId,String versionId){
		
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();
						
			orgNetList = new ArrayList();
			
			String hql = "from OrgNet ont where ont.preOrgId = '" + orgId.trim() + 
			"'  and ont.orgId in ("+" select MR.comp_id.orgId from ColAbnormityChange MR where  MR.comp_id.childRepId='"+childRepId+"' and MR.comp_id.versionId='"+versionId+"'"+")"; 
			
			List list = session1.find(hql.toString());
			
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					orgNetList.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	/**
	 * ���������Χ�ڵ��¼�����
	 * @param orgId
	 * @return
	 */
	public static List selectLowerOrgListInMRepRange(String orgId,String childRepId,String versionId){
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();
						
			orgNetList = new ArrayList();
			
			String hql = "from OrgNet ont where ont.preOrgId = '" + orgId.trim() + 
			"'  and ont.orgId in ("+" select MR.comp_id.orgId from MRepRange MR where  MR.comp_id.childRepId='"+childRepId+"' and MR.comp_id.versionId='"+versionId+"'"+")"; 
			
			List list = session1.find(hql.toString());
			
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					orgNetList.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * ���ݻ���ID��ѯ�����ӻ���
	 * @param orgId
	 * @return
	 */
	public static List selectLowerOrgList(String orgId){
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();
						
			orgNetList = new ArrayList();
			
			String hql = "from OrgNet ont where ont.preOrgId = '" + orgId.trim() + "'"; 
			System.out.println(hql);
			List list = session1.find(hql.toString());
			
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					orgNetList.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * ��ȡ��֧�����б�
	 * 
	 * @param preOrgId �ϼ�����ID
	 * @return List
	 */
	public static List selectSubOrgList(String preOrgId){
		if(preOrgId == null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();
			
			orgNetList = new ArrayList();
			
			String hql = "from OrgNet ont where ont.preOrgId = '" + preOrgId.trim() + "'"; 
			
			List list = session1.find(hql.toString());
			
			if(list != null && list.size() > 0){
				OrgNetForm orgNetFormTemp = null;
				OrgNet orgNetPersistence = null;
				
				for(int i=0;i<list.size();i++){
					orgNetPersistence = (OrgNet)list.get(i);
					orgNetFormTemp = new OrgNetForm();
					TranslatorUtil.copyPersistenceToVo(orgNetPersistence,orgNetFormTemp);					
					
					orgNetList.add(orgNetFormTemp);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * ���ݻ���ID��ѯ�����ӻ����ͱ�����
	 * @param orgId
	 * @return
	 */
	public static List selectLowerOrgCurrOrgList(String orgId){
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();
						
			orgNetList = new ArrayList();
			
			String hql = "from OrgNet ont where ont.preOrgId = '" + orgId.trim() + "'  or ont.orgId='"+ orgId.trim() + "'" ; 
			
			List list = session1.find(hql.toString());
			
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					orgNetList.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 *   �������list��� û�и������Ľڵ�ȥ��
	 * @param 
	 * @param list����String
	 * @return
	 */
	public static ArrayList selectAllLowerOrgList(ArrayList list,String orgId){
		
		ArrayList newList=new ArrayList();
		List temp=selectLowerOrgList(orgId);
		String tempOrg=null;
		if(temp!=null)
		for(int i=0;i<temp.size();i++){
			tempOrg=((OrgNet)temp.get(i)).getOrgId();
			if(list.contains(tempOrg))
				newList.add(tempOrg);
		}
			
		int Cpoint=0;
		while(Cpoint<newList.size()){
			temp=selectLowerOrgList((String)newList.get(Cpoint));
			if(temp!=null)
			for(int i=0;i<temp.size();i++){
				tempOrg=((OrgNet)temp.get(i)).getOrgId();
				if(list.contains(tempOrg))
					newList.add(tempOrg);
			}
			Cpoint++;
		}
		
		return newList;
	}
	
	/**
	 *   �������list��� û�и������Ľڵ�ȥ��
	 * @param 
	 * @param list��ŵ���OrgNet
	 * @return
	 */		
	public static List selectAllLowerOrgListBBB(List list,String orgId){					
		List newList=new ArrayList();		
		List temp=selectLowerOrgList(orgId);
		
		OrgNet tempOrg=null;		
		if(temp!=null){		
			for(int i=0;i<temp.size();i++){			
				tempOrg=(OrgNet)temp.get(i);
				if(list.contains(tempOrg))
					newList.add(tempOrg);
			}
		}
		
		int Cpoint=0;
		
		while(Cpoint<newList.size()){		
			temp=selectLowerOrgList(((OrgNet)newList.get(Cpoint)).getOrgId());			
			if(temp!=null)			
				for(int i=0;i<temp.size();i++){
					tempOrg=(OrgNet)temp.get(i);
					if(list.contains(tempOrg)){						
						newList.add(tempOrg);
					}
				}				
			Cpoint++;			
		}					
		return newList;		
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * Ӱ����� OrgNet
	 * ��ѯ�ӻ�����Ϣ
	 * @param orgId
	 * @param session
	 * @return
	 */
	public static List selectLowerOrgList(String orgId,HttpSession session){
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();						
			orgNetList = new ArrayList();
			
			String hql = "from OrgNet ont where ont.preOrgId = '" + orgId.trim() + "'"; 
			
			List list = session1.find(hql.toString());
			HashMap Map=null;
			if(session.getAttribute("SelectedOrgIds")!=null){			  
				Map=(HashMap)session.getAttribute("SelectedOrgIds");			  
			}
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					
					if(Map!=null){						
						if(Map.containsKey(orgNet.getOrgId())){
							orgNet.setPreOrgId("true");//������pre org��־�Ƿ�ѡ��							
						}
					}
					orgNetList.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	public static List selectLowerAfOrgList(String orgId,HttpSession session){
		if(orgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();						
			orgNetList = new ArrayList();
			
			String hql = "from AfOrg ont where ont.preOrgId = '" + orgId.trim() + "'"; 
			
			List list = session1.find(hql.toString());
			HashMap Map=null;
			if(session.getAttribute("SelectedOrgIds")!=null){			  
				Map=(HashMap)session.getAttribute("SelectedOrgIds");			  
			}
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					AfOrg orgNet = (AfOrg)list.get(i);
					
					if(Map!=null){						
						if(Map.containsKey(orgNet.getOrgId())){
							orgNet.setPreOrgId("true");//������pre org��־�Ƿ�ѡ��							
						}
					}
					orgNetList.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * ��ѯ�ӻ�������
	 * @param orgId
	 * @return
	 */
	public static int selectLowerOrgs(String orgId){
		
		int count = 0;		
		DBConn conn = null;
		Session session = null;		
		try{
			if(orgId==null) return count;
			
			conn = new DBConn();
			session = conn.openSession();						
			
			String hql = "select count(*) from OrgNet ont where ont.preOrgId = '" + orgId.trim() + "'"; 			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				count = ((Integer)list.get(0)).intValue();
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return count;
	}
	
	/**
	 * ��õ�ǰ��������һ������id
	 * @param orgId
	 * @return
	 */
	public static String getUpOrgId(String orgId){
		OrgNet org = selectOne(orgId);
		if(org==null) return "";
		return org.getPreOrgId();
	}
	
	/**
	 * ��ѯ�������һ������
	 * @return
	 */
	public static String getMostLowerOrgIds(){
		String mostLowerOrgIds = "";
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from OrgNet ont where ont.orgId not in (select org.preOrgId from OrgNet org)"; 
						
			List list = session.find(hql.toString());
						
			if(list != null && list.size() > 0){
				
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					mostLowerOrgIds = mostLowerOrgIds.equals("") ? orgNet.getOrgId() : mostLowerOrgIds + "," + orgNet.getOrgId();
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			mostLowerOrgIds = "" ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return mostLowerOrgIds;
	}
	
	/**
	 * ��ѯ�����¼�����
	 * @return
	 */
	public static List getNotLowerOrgs(){
		List notLowerOrgs = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from OrgNet ont where ont.orgId in (select org.preOrgId from OrgNet org)"; 						
			List list = session.find(hql.toString());
						
			if(list != null && list.size() > 0){
				notLowerOrgs = new ArrayList();
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);
					notLowerOrgs.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			notLowerOrgs = null;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return notLowerOrgs;
	}
	
	/**
	 * �жϻ����Ƿ����  
	 * @param  ����ID
	 * @xsf
	 */
	public static boolean OrgExist(String orgId){
		boolean result =false;
		DBConn conn = null;
		Session session = null;
		try{
			
			conn=new DBConn();
			session=conn.beginTransaction();
			String sql="from OrgNet org where org.orgId='"+orgId.trim()+"'";
					   
			List list=session.find(sql.toString());			   
			if(list!=null && list.size()>0) result=true;			
		}catch(Exception e){
			e.printStackTrace();			
		}
		finally{
			if(conn != null) conn.closeSession();
		}
		return result;
	}
	
	/**
	 * �ж��Ƿ�����߼�����
	 * @param orgId
	 * @return
	 */
	public static boolean isMaxOrgNet(String orgId){

		boolean bool = false;
		OrgNet orgNet = StrutsOrgNetDelegate.selectOne(orgId);
		
		if(orgNet != null){
			if(StrutsOrgNetDelegate.OrgExist(orgNet.getPreOrgId()) != true)
				bool = true;
		}
		return bool;
	}


	/**
	 * ���ݻ������ͣ���ѯ��Ӧ�Ļ�����Ϣ
	 * @param orgTypeId
	 * @return
	 */
	public static List getAllOrgByTypeId(Integer orgTypeId)
	{
		List list=null;
		DBConn conn=new DBConn();
		Session session = conn.openSession();
		String hql = "from OrgNet org where org.orgType.orgTypeId=" + orgTypeId;
		try {
			list=session.find(hql);
		} catch (HibernateException e) {			
			e.printStackTrace();
		}finally{
			if(conn != null) conn.closeSession();
		}			
		return list;
	}
	
	/**
	 * ���ݵ�������ѯ��Ӧ�Ļ�����Ϣ
	 * @param regionId
	 * @return
	 */
	public static List getOrgByRegionId(Integer regionId)
	{
		List list = null;
		DBConn conn = new DBConn();
		Session session = conn.openSession();
		String hql="from OrgNet org where org.region.regionId=" + regionId;
		try {
			list=session.find(hql);
		} catch (HibernateException e) {			
			e.printStackTrace();
		}finally{
			if(conn != null) conn.closeSession();
		}		
		return list;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * Ӱ�����OrgNet 
	 * ���ݻ���ID,ȡ�û�������;
	 * @param orgId
	 * return ��������
	 */
	public static String getOrgName(String orgId){		
		String strOrgId="";
		DBConn conn = null;
		Session session = null;		
		try{ 
			conn = new DBConn();
			
			session = conn.openSession();			
			String hql = "select ont.orgName from OrgNet ont where ont.orgId = '" + orgId.trim() + "'"; 			
			List list = session.find(hql.toString());						
			if(list != null && list.size()==1){			
				strOrgId= list.get(0).toString();
	
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			strOrgId = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return strOrgId;
	}
	
	public static String getAfOrgName(String orgId){		
		String strOrgId="";
		DBConn conn = null;
		Session session = null;		
		try{ 
			conn = new DBConn();
			
			session = conn.openSession();			
			String hql = "select ont.orgName from AfOrg ont where ont.orgId = '" + orgId.trim() + "'"; 			
			List list = session.find(hql.toString());						
			if(list != null && list.size()==1){			
				strOrgId= list.get(0).toString();
				
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			strOrgId = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return strOrgId;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * ������ѯ
	 * 
	 * @param orgNetForm OrgNetForm ��ѯ������
	 * @return List ������ҵ���¼������List��¼�������򣬷���null
	 */	   
	public static List selectChildOrg(String orgId){		   
		if(orgId==null) return null;	
		
		//List���ϵĶ��� 		
		List refVals  = new ArrayList();  
		
		//���Ӷ���ͻỰ�����ʼ��		
		DBConn conn = null;						
		Session session = null;	   
		
		//��ѯ����HQL������		  
		String hql = "from OrgNet ont where ont.preOrgId ='" + orgId.trim()+"'"; 				  
	    
		try {    	 	    
			//conn�����ʵ����		  	    	
			conn = new DBConn();	    	
			//�����ӿ�ʼ�Ự	    	
			session = conn.openSession();		 
			
			List list =session.find(hql);			
			if (list != null && list.size()>0){    		    		 			
				for(int i=0;i<list.size();i++) {			    
					OrgNet orgNet=(OrgNet)list.get(i);    				    	
					OrgNetForm orgForm=new OrgNetForm();			    	
					TranslatorUtil.copyPersistenceToVo(orgNet,orgForm);			        
					refVals.add(orgForm);			      
				}	    	   
			}			      
		}catch(HibernateException he){	    	
			refVals = null;	    	
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	    
			refVals = null;  	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	      
		}finally{	    
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	      
		}	    
		return refVals;	   
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * ������Ϣ��ѯ
	 * 
	 * @param orgNetForm OrgNetForm ��ѯ������
	 * @return  OrgNetForm ������ҵ���¼������OrgNetForm�����򣬷���null
	 */	   
	public static OrgNetForm selectOrgInfo(String orgId){

		if(orgId==null) return null;		
		//���Ӷ���ͻỰ�����ʼ��
		
		DBConn conn = null;						
		Session session = null;
		
		OrgNetForm orgNetFormTemp = new OrgNetForm();		
		//	 ��ѯ����HQL������		
		String hql = new String("from OrgNet ont where ont.orgId = '" + orgId + "'");
		try {
			//conn�����ʵ����		    	
			conn = new DBConn();	    	
			//�����ӿ�ʼ�Ự	    	
			session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());			
			List list = query.list();
	    	
			if (list != null){	    		  
				//ѭ����ȡ���ݿ����������¼
				for(Iterator it = list.iterator(); it.hasNext(); ) {
					OrgNet orgNetPersistence = (OrgNet)it.next();			        
					TranslatorUtil.copyPersistenceToVo(orgNetPersistence, orgNetFormTemp);			      				
				}	    	   
			}	      
		}catch(HibernateException he){	    
			orgNetFormTemp = null;	    	
			he.printStackTrace();
			log.printStackTrace(he);	      
		}catch(Exception e){	    
			orgNetFormTemp = null;  	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	    
		}finally{	    
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	      
		}	    
		return orgNetFormTemp;	   
	}
	
	/**
	 * ɾ�����������Ϣ����
	 *
	 * @param   orgNetForm   OrgNetForm ��ѯ���Ķ���
	 * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
	 */  
	public static boolean removeOrgXGInfo(OrgNetForm orgNetForm) throws Exception {

		if (orgNetForm == null || orgNetForm.getOrg_id() == null)   return false;		
		boolean result=false;
		
		//���ӺͻỰ����ĳ�ʼ��		
		DBConn conn=null;		
		Session session=null;
		
		try{		
			//���Ӷ���ͻỰ�����ʼ��			
			conn=new DBConn();			
			session=conn.beginTransaction();
			
			//ɾ�����ͷ�Χ			
			boolean delBSFW = deleteBSFW(orgNetForm.getOrg_id(),session);			   			
			//ɾ���쳣�仯			
			//	boolean delYCBH = deleteYCBH(orgNetForm.getOrg_id(),session);	
			boolean delYCBH=true;
			//ɾ������
	        boolean  delOrg=removeOrg(orgNetForm,session);
		      
	        if(delBSFW && delYCBH && delOrg){		    
	        	result=true;		      
	        }		   
		}catch(HibernateException he){		
			//��׽������쳣,�׳�			
			log.printStackTrace(he);			
			result=false;		   
		}finally{		
			//�����������Ͽ����ӣ������Ự������			
			if (conn!=null) conn.endTransaction(result);		
		}
		return result; 
	}

	/**
	 * ɾ���쳣�仯
	 * @param orgId
	 * @param session
	 * @return
	 * @throws Exception
	 */ 
	public static boolean deleteYCBH (String  orgId,Session session) throws Exception {
	   
		boolean result=false;	   
		if (orgId != null) {		
			try{		
				Query query = session.createQuery("from AbnormityChange abc where abc.comp_id.orgId='"+orgId+"'");        
				List list = query.list();	        
				if(list!=null && list.size()!=0){
	            
					for(Iterator it = list.iterator(); it.hasNext();){	             	
						AbnormityChange abnormityChange = (AbnormityChange)it.next();			            
						/**ɾ���쳣�仯*/	
						session.delete(abnormityChange);			            
					}	        
				}
	         
				Query queryQD = session.createQuery("from ColAbnormityChange abc where abc.comp_id.orgId='"+orgId+"'");	         
				List listQD = queryQD.list();	         
				if(listQD!=null && listQD.size()!=0){	            
					for(Iterator it = listQD.iterator(); it.hasNext();){	             	
						ColAbnormityChange colAbnormityChange = (ColAbnormityChange)it.next();			            
						/**ɾ���쳣�仯*/			        
						session.delete(colAbnormityChange);
					}	         
				}
				session.flush();
				result=true;  
			}catch(Exception e){        	 
				result=false;          
				log.printStackTrace(e);      
			}
		} 
		return result;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * ɾ�����ͷ�Χ
	 * @param orgId
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static boolean deleteBSFW (String  orgId,Session session) throws Exception {
		   
		boolean result=false;		
		if(orgId!=null){
			try{	     	
				Query query = session.createQuery("from MRepRange mrr where mrr.comp_id.orgId='"+orgId+"'");	        
				List list = query.list();
	         
				if(list!=null && list.size()!=0){	            
					for(Iterator it = list.iterator(); it.hasNext();){						
						MRepRange repRange = (MRepRange)it.next();
			            /**ɾ�����ͷ�Χ*/			       			             
						session.delete(repRange);			            
						session.flush();	             
					}	         
				}		    
				result=true;	  
			}catch(Exception e){   	     	
				result=false;	          
				log.printStackTrace(e);	      
			}	  
		}	 
		return result;	
	}
	
	/**
	 * ɾ������
	 * @param   orgNetForm   OrgNetForm ��ѯ���Ķ���
	 * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
	 */  
	public static boolean removeOrg (OrgNetForm orgNetForm,Session session) throws Exception {	    
		//�ñ�־result		
		boolean result=false;	 		
		
		if (orgNetForm == null || orgNetForm.getOrg_id() == null) return result;
		
		try{
			OrgNet orgNet=(OrgNet)session.load(OrgNet.class,orgNetForm.getOrg_id());
			
			//�Ự����ɾ���־ò����
			session.delete(orgNet);			
			session.flush();			
			
			result=true;			
		}catch(HibernateException he){		
			//��׽������쳣,�׳�			
			log.printStackTrace(he);		   
		}		
		return result;
	}
	/**
	 * ɾ��������Ӧ�ı���
	 * @param   orgNetForm   OrgNetForm ��ѯ���Ķ���
	 * @return   boolean  ���ɾ���ɹ��򷵻�true,����false
	 */  
	public static boolean removeOrgRep (OrgNetForm orgNetForm,Session session) throws Exception {	    
		//�ñ�־result		
		boolean result=false;	 		
		
		if (orgNetForm == null || orgNetForm.getOrg_id() == null) return result;
		
		try{
			OrgNet orgNet=(OrgNet)session.load(OrgNet.class,orgNetForm.getOrg_id());
			//int updatedEntities = session.createQuery( "" );
			//�Ự����ɾ���־ò����
			session.delete(orgNet);			
			session.flush();			
			
			result=true;			
		}catch(HibernateException he){		
			//��׽������쳣,�׳�			
			log.printStackTrace(he);		   
		}		
		return result;
	}
	/**
	 * ���ݵ�ǰ��������ѯ�ӻ���
	 * @param orgId
	 * @return List ���û�м�¼������null
	 */
	public static List selectAllOrg(String orgId){

		List refVals = null;
		//���Ӷ���ͻỰ�����ʼ��		
		DBConn conn = null;						
		Session session = null;
				
		if(orgId == null) return null;
		//��ѯ����HQL������		
		String hql = "from OrgNet ont where  ont.preOrgId = '" + orgId.trim() + "' order by ont.orgName";						    
		try {	    			  	    
			conn = new DBConn();	    			
			session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());			
			List list = query.list();

			if (list != null && list.size() > 0){	    	
				refVals = new ArrayList();	    		
				//ѭ����ȡ���ݿ����������¼			    
				for(Iterator it = list.iterator(); it.hasNext(); ) {			    
					OrgNetForm orgNetFormTemp = new OrgNetForm();			    	
					OrgNet orgNetPersistence = (OrgNet)it.next();			        
					orgNetFormTemp.setOrg_id(orgNetPersistence.getOrgId());												       
					refVals.add(orgNetFormTemp);					
				}	    	   
			}	      
		}catch(HibernateException he){	    
			refVals = null;	    	
			he.printStackTrace();	    	
			log.printStackTrace(he);	      
		}catch(Exception e){	    
			refVals = null;  	    	
			e.printStackTrace();	    	
			log.printStackTrace(e);	      
		}finally{	    
			//������Ӵ��ڣ���Ͽ��������Ự������	    	
			if(conn != null) conn.closeSession();	    
		}
		return refVals;	   
	}
	
	/**
	 * ��߻���
	 * @param ����ID ��ѯ������
	 * @return  ����ID
	 */	   
	public static OrgNetForm getMaxOrg(){	
		OrgNetForm orgNetFormTemp=null;
		
		try{		
			List list=StrutsOrgNetDelegate.findAll();	        
			if(list!=null){	        
				for(Iterator iter = list.iterator();iter.hasNext();){	  				
					orgNetFormTemp = (OrgNetForm)iter.next();	  			  	
					OrgNet orgNet = StrutsOrgNetDelegate.selectOne(orgNetFormTemp.getOrg_id());	  			  	
					if(orgNet != null){	  			  	
						if(StrutsOrgNetDelegate.OrgExist(orgNet.getPreOrgId()) != true){	  			  		
							return  orgNetFormTemp;
						}	  			  		
					}	    			
				}	        
			}		  
		}catch(Exception e){		
			e.printStackTrace();		   
		}	         
		return   orgNetFormTemp;	   
	}
	
	/**
	 * ���ݻ���IDȡ����Ӧ������Ϣ
	 * @param orgIds
	 * @return List �����б�
	 */
	public static List selectOrgByIds(String orgIds){
		List result = null;
		DBConn conn = null;
		Session session = null;
		
		try{
			conn = new DBConn();
			session = conn.openSession();
			
			String hql = "from OrgNet ont where ont.orgId in(" + orgIds + ") order by ont.orgName"; 			
			List list = session.find(hql.toString());
			
			if(list != null && list.size() > 0){
				result = new ArrayList();
				for(int i=0;i<list.size();i++){
					OrgNet orgNet = (OrgNet)list.get(i);					
					result.add(orgNet);
				}
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			result = null ;
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return result;
	}
	
	/**
	 * ��������и��ݻ�������Orgname����OrgId�ļ���
	 * 
	 * @param orgName
	 * 
	 * @return list������id�ļ���
	 * 
	 */
	public static List selectOrgNames (String orgName){
		
		List refVals=null;		
		//���Ӷ���ͻỰ�����ʼ��		
		DBConn conn=null;		
		Session session=null;
		
		if (orgName!=null && !orgName.toString().equals("")){	           
			StringBuffer hql = new StringBuffer("from OrgNet mo where 1=1"); 	        
			StringBuffer where = new StringBuffer("");	        
			where.append(" and mo.orgName like '%" +orgName+"%'");
	        
			try{	           
				hql.append(where.toString());
				//conn�����ʵ���� 	            
				conn=new com.cbrc.smis.dao.DBConn();	            
				//�����ӿ�ʼ�Ự	            
				session=conn.openSession();	            
				Query query=session.createQuery(hql.toString());

				List list=query.list();
				if (list!=null){	            
					refVals = new ArrayList();	                
					//ѭ����ȡ���ݿ����������¼	                
					for (Iterator it = list.iterator(); it.hasNext(); ) {	               
						OrgNetForm mOrgFormTemp = new OrgNetForm();	                    
						OrgNet mOrgPersistence = (OrgNet)it.next();	                    
						TranslatorUtil.copyPersistenceToVo(mOrgPersistence, mOrgFormTemp);	                    
						refVals.add(mOrgFormTemp);	                 
					}	              
				}	          
			}catch(HibernateException he){	        
				refVals=null;	            
				log.printStackTrace(he);	          
			}catch(Exception e){	          
				refVals=null; 	            
				log.printStackTrace(e);	          
			}finally{	          
				//������Ӵ��ڣ���Ͽ��������Ự������	            
				if(conn!=null) conn.closeSession();	         
			}		   
		}	 		
		return refVals;	    
	}
	   
	/**
	 * ����StrutsMOrgDelegate�е�selectOrgNames�����ҵ��Ļ���������ת��Ϊ�ַ���	 
	 * @param orgName
	 * @return
	 */
	public static String selectOrgIdToString (String orgName){	
		List list=null;
	   	list=new ArrayList();
	   	String strOrgName="";
	   	
	   	list = StrutsOrgNetDelegate.selectOrgNames(orgName);

	   	for(int i=0;i<list.size();i++){
	   		strOrgName = strOrgName.equals("") ? Config.SPLIT_SYMBOL_SIGNLE_QUOTES + ((OrgNetForm)list.get(i)).getOrg_id() + Config.SPLIT_SYMBOL_SIGNLE_QUOTES
	   							: strOrgName + Config.SPLIT_SYMBOL_COMMA + Config.SPLIT_SYMBOL_SIGNLE_QUOTES + ((OrgNetForm)list.get(i)).getOrg_id() + Config.SPLIT_SYMBOL_SIGNLE_QUOTES;	   		
	   	}	    
	   	return strOrgName;	   
	}
	/**
	 * ����preOrgId���������м��������
	 * @param preOrgId
	 * @return
	 */
	public static List RhOrgId(String preOrgId){
		if(preOrgId==null) return null;
		DBConn conn = null;
		Session session1 = null;
		List orgNetList = null;
		
		try{
			conn = new DBConn();
			session1 = conn.openSession();						
			orgNetList = new ArrayList();
			String hql = "select * from af_org ao where ao.pre_org_id= '" + preOrgId+ "'"; 			
			Connection connection = session1.connection();
			ResultSet rs = connection.createStatement().executeQuery(hql);
			while(rs.next()){				
					OrgNet orgNet = new OrgNet();
					OrgType orgtype = new OrgType();
					MRegion mregion = new MRegion();
					mregion.setRegionId(rs.getInt("region_id"));
					orgNet.setOrgId(rs.getString("org_id"));
					orgNet.setOrgName(rs.getString("org_name"));
					orgNet.setOrgType(orgtype);
					orgNet.setPreOrgId(rs.getString("pre_org_id"));
					orgNet.setRegion(mregion);
					orgNet.setSetOrgId(rs.getString("set_org_id"));
					orgNetList.add(orgNet);				
			}
		}catch(HibernateException he){
			log.printStackTrace(he);
		}catch(Exception e){
			log.printStackTrace(e);
		}finally{
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
}
