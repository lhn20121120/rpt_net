 package com.gather.adapter;
	import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.fitech.net.form.OrgNetForm;
import com.gather.common.Log;
import com.gather.common.StringUtil;
import com.gather.dao.DBConn;
import com.gather.hibernate.MOrg;
import com.gather.struts.forms.MOrgForm;
;

	/** ������
	 * This is a delegate class to handle interaction with the backend persistence layer of hibernate. 
	 * It has a set of methods to handle persistence for MRepRange data (i.e. 
	 * com.gather.struts.forms.MRepRangeForm objects).
	 * 
	 * @author <strong>Generated by Middlegen.</strong>
	 */
	public class StrutsMOrg {
		
		//������
	    public static List getmorg(String[] orgIds){
		      List retVals = new ArrayList();
		      
		      DBConn conn=new DBConn();
		      Session session=conn.openSession();
		      String sql="from com.gather.hibernate.MOrg as obj1 where obj1.orgId in(" + StringUtil.getStrForSqlIN(orgIds) + ")";
		      try{
		          retVals.addAll(session.find(sql));
		          session.close();
		         }catch(Exception e){
		        	 new Log(StrutsMOrg.class).info(":::class:StrutsMOrg --  method: getmorg �쳣��"+e.getMessage());
		        	 e.printStackTrace();
		         }finally{
		        	 try{
		        		 session.close();
		        	 }catch(Exception e){
		        		 e.printStackTrace();
		        	 }
		         }
		
		      List org_vos = new ArrayList();
		      
		      for (Iterator it = retVals.iterator(); it.hasNext();) {
		    	  MOrgForm morgFormTemp = new MOrgForm();
		    	  MOrg morg = (MOrg)it.next();
		    	 // // System.out.println("mRepRangePersistence : "+mRepRangePersistence.getComp_id().getChildRepId());
		    	  try{
		         TranslatorUtil.copyPersistenceToVo(morg, morgFormTemp);
		    	  }catch(Exception e){
		    		  new Log(StrutsMOrg.class).info(":::class:StrutsMOrg --  method: getmorg �쳣��"+e.getMessage());
		    		  e.printStackTrace();
		    	  }
		         org_vos.add(morgFormTemp);
		      }
		      return org_vos;
	    
}
	    
	    /**
	     * @author linfeng
	     * @function �õ����л�����Ϣ
	     * @return List orgs
	     */
	    
	    public static List getAll(){
	    	List list=new ArrayList();
	    	DBConn conn=new DBConn();
	    	Session session=conn.openSession();
	    	String hsql="from com.gather.hibernate.MOrg as obj";
	    	List resultList=new ArrayList();
	    	try{
	    		list.addAll(session.find(hsql));
	    		if(list!=null && list.size()>0){
	    			for(int i=0;i<list.size();i++){
	    				MOrg orgs=(MOrg)list.get(i);
	    				MOrgForm myForm=new MOrgForm();
	    				TranslatorUtil.copyPersistenceToVo(orgs, myForm);
	    				resultList.add(myForm);
	    			}
	    		}
	    	}catch(Exception e){
	    		new Log(StrutsMOrg.class).info(":::class:StrutsMOrg --  method:getAll() �쳣��"+e.getMessage());
	    		e.printStackTrace();
	    	}finally{
	    		try{
	    			if(session!=null) session.close();
	    		}catch(Exception e){e.printStackTrace();}
	    	}
	    	return resultList;
	    }
	    
	    
	    public static boolean create111 (OrgNetForm orgnetForm) throws Exception {
	    	boolean resultFlag = false;
			DBConn  conn = new DBConn();
			Session session =null;
			String orgid=orgnetForm.getOrg_id();
			
		    if(orgnetForm != null){  
		          try {
		        	  session=conn.beginTransaction();			              			              			             
		              MOrg mOrgPersistence = new MOrg();
		              
		              /*�ж�gather���е�MOrg���Ƿ�����orgid��ͬ���ֶ�*/
		              if(StrutsMOrg.selectgatherorg(orgid)==false)
		              {				            	  
		              mOrgPersistence.setOrgId(orgnetForm.getOrg_id());
		              mOrgPersistence.setOrgName(orgnetForm.getOrg_name());
		              /*mOrgPersistence.setOrgType(Integer.getInteger(orgnetForm.getOrgType()));*/
		              //mOrgPersistence.setIsCorp(orgnetForm());				              
			          session.save(mOrgPersistence);	
			          resultFlag = true;
		              }else if(StrutsMOrg.selectgatherorg(orgid)==true){
		            	  resultFlag = false;  
		              }				          
		               
		          } catch(Exception e) {
		              
		              e.printStackTrace();
		          }finally{
		        	  conn.endTransaction(resultFlag);		        	  
		          }
		         
		      }
		     return    resultFlag; 
		   }
    public static boolean selectgatherorg(String orgid){
		boolean result=false;
		
		com.gather.dao.DBConn  conn = new com.gather.dao.DBConn();
		Session session =null;
		try {					
			session = conn.openSession();			
			String hql =  "select count(*) from com.gather.hibernate.MOrg op where op.orgId='"+ orgid.toString()+"'";
			
			//// System.out.println("========="+hql);
			Query query = session.createQuery(hql);
			// System.out.println("hql  "+hql);
			//// System.out.println("query");
			List list = query.list();
			if (list != null && list.size() != 0) {
				return result==true;
			}else if(list==null&&list.size()==0){
				return result==false;
			}
		} catch (Exception e) {			
				
			
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return result;
	}	 
	 
	 public static boolean update202( OrgNetForm orgnetForm){
			boolean res=false;
			
			if(orgnetForm==null) return res;
			if(orgnetForm.getOrg_id()==null) return res;
				
			DBConn conn=null;
			Session session=null;
			
			try{
				conn=new DBConn();
				session=conn.beginTransaction();
				if(orgnetForm!=null){
					 MOrg mOrgPersistence= (MOrg)session.load(MOrg.class,orgnetForm.getOrg_id());
	        	      mOrgPersistence.setOrgId(orgnetForm.getOrg_id());
	        	      mOrgPersistence.setOrgName(orgnetForm.getOrg_name());
	        	     /* mOrgPersistence.setOrgType(Integer.getInteger(orgnetForm.getOrgType()));*/
	        	      //mOrgPersistence.setIsCorp(orgnetForm.getIsCorp());
		              session.update(mOrgPersistence);
					res=true;
				}
			}catch(Exception e){
				res=false;
				e.printStackTrace();
			}finally{
				if(conn!=null) conn.endTransaction(res);
			}
			
			return res;
		}
	 
	 /**			   		    
	    * @author ���¿�			   		   
	    * @return  ɾ��org��ʱͬʱҲ��gather�е�Morg����ɾ��		   		 
	    */ 
	 public static boolean remove202(OrgNetForm orgnetForm) throws Exception {
	      boolean resultFlag = false;
	      
	        DBConn  conn = new DBConn();
			Session session =null;
	      if(orgnetForm!=null)
	      {
	          try
	          {	             
	              session = conn.beginTransaction(); 
	              MOrg mOrgPersistence =(MOrg)session.load(MOrg.class,orgnetForm.getOrg_id());		             
	              session.delete(mOrgPersistence);
	              session.flush();
	              resultFlag = true;
	          }catch (Exception e) {					   			
		   			e.printStackTrace();
		   			resultFlag=false;
		   		} finally {
		   			if (conn != null)
		   				
		   		 conn.endTransaction(resultFlag);
		   		 
		   		}	         
	      }
	      return resultFlag;
	    }    
	    
}