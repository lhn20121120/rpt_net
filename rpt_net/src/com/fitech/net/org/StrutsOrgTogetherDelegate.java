package com.fitech.net.org;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.OrgActuTypeForm;
import com.cbrc.smis.hibernate.OrgActuType;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.config.Config;
import com.fitech.net.org.form.OrgnetForm;
import com.fitech.net.orgcls.form.OrgclsNetForm;
import com.fitech.net.orgcls.hibernate.MOrgClNet;
import com.fitech.net.orglayer.form.OrgLayerForm;
import com.fitech.net.orglayer.hibernate.OrgLayer;
import com.fitech.net.region.form.RegionForm;
import com.fitech.net.region.hibernate.Region;
import com.fitech.net.regiontyp.form.RegionTypNetForm;
import com.fitech.net.regiontyp.hibernate.RegionTypNet;



public class StrutsOrgTogetherDelegate {
	
	private static FitechException log=new FitechException(StrutsOrgTogetherDelegate.class);
	
	  public static int getRecordCount(OrgnetForm orgnetForm) throws Exception
	   {   
		   int recordCount = 0;
			List retVals = null;
			DBConn conn = null;

	      /* int result =0;
	       DBConn conn =null;
	       Session session =null;*/
	       try
	       {   // System.out.println("zhangxinke "+orgnetForm.getOrgId());
	    	   StringBuffer hql = new StringBuffer("select count(*) from Orgnet orgnet where 1=1");
	    	   /*StringBuffer hql = new StringBuffer("select count(*) from com.gather.hibernate.MOrg where 1=1");*/
			   StringBuffer where = new StringBuffer("");
	    	   String event=orgnetForm.getEvent();
	    	  
			  if (orgnetForm != null) {
					String orgId= orgnetForm.getOrgId();
                    if("add".equals(event)==true)
					{
                    	where.append("");
				}else{
					if (orgId != null && !orgId.equals(""))
						where.append(" and orgnet.orgId like'%" + orgId.trim()
								+ "%'");
				}
			  }
				hql.append(where.toString());

				conn = new DBConn();

				Session session = conn.openSession();
				Query query = session.createQuery(hql.toString());
				retVals = query.list();
				if (retVals != null && retVals.size() != 0) {
					recordCount = Integer.parseInt(retVals.get(0).toString());
				}
			} catch (HibernateException he) {
				he.printStackTrace();
				log.printStackTrace(he);
			} finally {
				if (conn != null)
					conn.closeSession();
			}
			return recordCount;
	   }
	  
	  public static int getRecordCount2() throws Exception
	   {
	       int result =0;
	       DBConn conn =null;
	       Session session =null;
	       try
	       {
	           conn = new DBConn();
	           session = conn.openSession();
	           
	           Query query = session.createQuery("select count(*) from Region");
	           List list = query.list();
	     
	           if(list!=null && list.size()!=0)
	               result = ((Integer)list.get(0)).intValue();
	           
	       }
	       catch(Exception e)
	       {
	           log.printStackTrace(e);
	           e.printStackTrace();
	           result = 0;
	       }
	       finally{
	           if(conn!=null)
	               conn.closeSession();
	       }
	      
	       return result;
	   }
	  
	  
	  /**
	     * 分页显示记录
	     * @author zhangxinke
	     * @param  offset int 偏移量
	     * @param  limit int 取最大记录数
	     * @return  List 记录集合
	     * 根据查询条件查询
	     * @exception   Exception  
	     */
	    public static List orgselect (OrgnetForm orgnetForm,int offset,int limit) throws Exception {
	       List retVals = null;
	       DBConn conn = null;	    
	       try{   
	    	   StringBuffer hql = new StringBuffer("from Orgnet orgnet where 1=1");
				StringBuffer where = new StringBuffer("");
				String event=orgnetForm.getEvent();
				
				 if (orgnetForm != null) {						
					 String orgId= orgnetForm.getOrgId();	                 
					 if("add".equals(event)==true){   	                 
						 where.append("");					
					 }else{					
						 if (orgId != null && !orgId.equals(""))						
							 where.append(" and orgnet.orgId like'%" + orgId.trim() + "%'");					
					 }				  
				 }
				
				hql.append(where.toString());
				conn = new DBConn();

				Session session = conn.openSession();
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(offset);
				query.setMaxResults(limit);
				List list = query.list();
				
				if (list != null && list.size() > 0) {
					retVals = new ArrayList();
		               for(Iterator it = list.iterator(); it.hasNext();)
		               {
//		            	   OrgnetForm orgnetFormTemp = new OrgnetForm();
//		            	   Orgnet orgPersistence = (Orgnet)it.next();
//		                   TranslatorUtil.copyPersistenceToVo(orgPersistence, orgnetFormTemp);
//		                   retVals.add(orgnetFormTemp);         
		               }       
		                    
		           }
			} catch (HibernateException he) {
				he.printStackTrace();
				log.printStackTrace(he);
			} finally {
				if (conn != null)
					conn.closeSession();
			}
			return retVals;
	    }
	    public static List orgselect2 (OrgnetForm orgnetForm,int offset,int limit) throws Exception {
		       List retVals = null;
		       DBConn conn = null;		      
		       try{   
		    	   StringBuffer hql = new StringBuffer("from Orgnet orgnet where 1=1");
					StringBuffer where = new StringBuffer("");
					/*String event=orgnetForm.getEvent();*/
					 if (orgnetForm != null) {							
						 String orgName= orgnetForm.getOrgName();													
						 if (orgName != null && !orgName.equals(""))						
							 where.append(" and orgnet.orgName like'%" + orgName.trim() + "%'");					  
					 }
					
					hql.append(where.toString());
					conn = new DBConn();

					Session session = conn.openSession();
					Query query = session.createQuery(hql.toString());
					query.setFirstResult(offset);
					query.setMaxResults(limit);
					List list = query.list();
					
					if (list != null && list.size() > 0) {
						retVals = new ArrayList();
			               for(Iterator it = list.iterator(); it.hasNext();)
			               {
//			            	   OrgnetForm orgnetFormTemp = new OrgnetForm();
//			            	   Orgnet orgPersistence = (Orgnet)it.next();
//			                   TranslatorUtil.copyPersistenceToVo101(orgPersistence, orgnetFormTemp);
//			                   retVals.add(orgnetFormTemp);         
			               }       
			                    
			           }
				} catch (HibernateException he) {
					log.printStackTrace(he);
				} finally {
					if (conn != null)
						conn.closeSession();
				}

				return retVals;
		    }
        
	    public static List orgselect22(OrgnetForm orgnetForm,int offset,int limit) throws Exception {
		       List retVals = null;
		       DBConn conn = null;
		      // Session session = null;
		       try
		       {   
		    	   StringBuffer hql = new StringBuffer("from Orgnet orgnet where 1=1");
					StringBuffer where = new StringBuffer("");
					/*String event=orgnetForm.getEvent();*/
					 if (orgnetForm != null) {
							String orgName= orgnetForm.getOrgName();
							/* // System.out.println("event "+event);*/
		                    /*if("add".equals(event)==true)*/
							/*{*/   /*// System.out.println("event "+event);*/
		             /*       	where.append("");
						}*//*else{*/
							if (orgName != null && !orgName.equals(""))
								where.append(" and orgnet.orgName like'%" + orgName.trim()
										+ "%'");
						/*}*/
					  }
					
					hql.append(where.toString());
					conn = new DBConn();

					Session session = conn.openSession();
					Query query = session.createQuery(hql.toString());
					query.setFirstResult(offset);
					query.setMaxResults(limit);
					List list = query.list();
					
					if (list != null && list.size() > 0) {
						retVals = new ArrayList();
			               for(Iterator it = list.iterator(); it.hasNext();)
			               {
//			            	   OrgnetForm orgnetFormTemp = new OrgnetForm();
//			            	   Orgnet orgPersistence = (Orgnet)it.next();
//			                   TranslatorUtil.copyPersistenceToVo1011(orgPersistence, orgnetFormTemp);
//			                   retVals.add(orgnetFormTemp);         
			               }       
			                    
			           }
				} catch (HibernateException he) {
					log.printStackTrace(he);
				} finally {
					if (conn != null)
						conn.closeSession();
				}

				return retVals;
		    }
	    
	    public static List select2(int offset,int limit) throws Exception {
		       List result = null;
		       DBConn conn = null;
		       Session session = null;
		       try
		       {
		           conn = new DBConn();
		           session = conn.openSession();
		       
		           Query query = session.createQuery("from Region");
		           query.setFirstResult(offset);
		           query.setMaxResults(limit);
		           
		           List list = query.list();
		           if(list!=null && list.size()!=0)
		           {
		        	  
		        	   result = new ArrayList();
		               for(Iterator it = list.iterator(); it.hasNext();)
		               {
		            	   RegionForm orgnetFormTemp = new RegionForm();
		            	   Region orgPersistence = (Region)it.next();
		                   TranslatorUtil.copyPersistenceToVo2(orgPersistence, orgnetFormTemp);
		                   result.add(orgnetFormTemp);         
		               }       
		                    
		           }
		       }
		       catch(Exception e)
		       {
		           result = null;
		           log.printStackTrace(e);
		       }
		       finally{
		           if(conn!=null)
		              conn.closeSession();
		       }
		       return result;
		    }
	    
	    /**
	     * 删除机构信息
	     * @author 张新科
	     * @param   
	     * @return  boolean 删除是否成功
	     * @exception   
	     */  
	    public static boolean remove2(OrgnetForm orgnetForm) throws Exception {
	      boolean result = false;
	      
	      DBConn conn =null;
	      Session session =null;
	      if(orgnetForm!=null)
	      {
	          try
	          {
//	              conn = new DBConn();
//	              session = conn.beginTransaction(); 
//	              Orgnet orgnet = (Orgnet)session.load(Orgnet.class,orgnetForm.getOrgId());       
//	              session.delete(orgnet);
//	              session.flush();
//	              result = true;
	          }
	          catch(Exception e)
	          {
	              log.printStackTrace(e);
	              result = false;
	          }
	          finally{
	              if(conn!=null)
	                  conn.endTransaction(result);
	          }
	      }	      
	      return result;
	      
	    }    
	      
	    
	    public static int getRecordCount3() throws Exception
	    {
	        int result =0;
	        DBConn conn =null;
	        Session session =null;
	        try
	        {
	            conn = new DBConn();
	            session = conn.openSession();
	            
	            Query query = session.createQuery("select count(*) from MOrgClNet dept");
	            List list = query.list();
	      
	            if(list!=null && list.size()!=0)
	                result = ((Integer)list.get(0)).intValue();
	            
	        }
	        catch(Exception e)
	        {
	            log.printStackTrace(e);
	            result = 0;
	        }
	        finally{
	            if(conn!=null)
	                conn.closeSession();
	        }
	       
	        return result;
	    }
	    /**
	     * 分页显示记录
	     * @author zhangxinke
	     * @param  offset int 偏移量
	     * @param  limit int 取最大记录数
	     * @return  List 记录集合
	     * @exception   Exception  
	     */
	    public static List select3(int offset,int limit) throws Exception {
	       List result = null;
	       DBConn conn = null;
	       Session session = null;
	       try
	       {
	           conn = new DBConn();
	           session = conn.openSession();
	       
	           Query query = session.createQuery("from MOrgClNet");
	           query.setFirstResult(offset);
	           query.setMaxResults(limit);
	           
	           List list = query.list();
	           if(list!=null && list.size()!=0)
	           {
	        	  
	        	   result = new ArrayList();
	               for(Iterator it = list.iterator(); it.hasNext();)
	               {
	            	   OrgclsNetForm orgclsFormTemp = new OrgclsNetForm();
	                   MOrgClNet orgclsPersistence = (MOrgClNet)it.next();
	                   TranslatorUtil.copyPersistenceToVo3(orgclsPersistence, orgclsFormTemp);
	                   result.add(orgclsFormTemp);         
	               }       
	                    
	           }
	       }
	       catch(Exception e)
	       {
	           result = null;
	           log.printStackTrace(e);
	       }
	       finally{
	           if(conn!=null)
	              conn.closeSession();
	       }
	       return result;
	    }   
	    
	    /**
		 * 在org中是否有orgClId
		 * 
		 * @author zhangxinke
		 */
		public static int getOrgFromorgClsId(String orgClId) {
			int result = 0;
			DBConn conn = null;
			Session session = null;
			try {
				conn = new DBConn();
				session = conn.openSession();
				if(orgClId==null||orgClId.equals(""))
					return -1;
				
				String hql =  "select count(*) from Orgnet op where op.orgClsId='"+ orgClId.toString()+"'";
				
				//// System.out.println("========="+hql);
				Query query = session.createQuery(hql);
				
				//// System.out.println("query");
				List list = query.list();
				if (list != null && list.size() != 0) {
					result =((Integer) list.get(0)).intValue();
				}
			} catch (Exception e) {			
				log.printStackTrace(e);
				
				
			} finally {
				if (conn != null)
					conn.closeSession();
			}
			return result;
		}
		
		
		   /**
		    * 删除机构分类信息
		    * @author 张新科
		    * @param   
		    * @return  boolean 删除是否成功
		    * @exception   Exception   If the com.cbrc.auth.form.DepartmentForm object cannot be removed.
		    */  
		   public static boolean remove3(OrgclsNetForm orgclsNetForm) throws Exception {
		     boolean result = false;
		     
		     DBConn conn =null;
		     Session session =null;
		     if(orgclsNetForm!=null)
		     {
		         try
		         {
		             conn = new DBConn();
		             session = conn.beginTransaction(); 
		             
		             MOrgClNet mOrgClNet = (MOrgClNet)session.load(MOrgClNet.class,orgclsNetForm.getOrgClsId());
		             session.delete(mOrgClNet);
		             session.flush();
		             result = true;
		         }
		         catch(Exception e)
		         {
		             log.printStackTrace(e);
		             result = false;
		         }
		         finally{
		             if(conn!=null)
		                 conn.endTransaction(result);
		         }
		     }
		         
		     return result;
		   }      
		   
		   /**
			 * 在org中是否有orgClId
			 * 
			 * @author zhangxinke
			 */
			public static int getorgClsId(String orgClId) {
				int result = 0;
				DBConn conn = null;
				Session session = null;
				try {
					conn = new DBConn();
					session = conn.openSession();
					if(orgClId==null||orgClId.equals(""))
						return -1;
					
					String hql =  "select count(*) from MOrgClNet op where op.orgClsId='"+ orgClId.toString()+"'";
					
					//// System.out.println("========="+hql);
					Query query = session.createQuery(hql);
					
					//// System.out.println("query");
					List list = query.list();
					if (list != null && list.size() != 0) {
						result =((Integer) list.get(0)).intValue();
					}
				} catch (Exception e) {			
					log.printStackTrace(e);
					
					
				} finally {
					if (conn != null)
						conn.closeSession();
				}
				return result;
			}
			public static int getorgClsId11(String orgClId) {
				int result = 0;
				DBConn conn = null;
				Session session = null;
				try {
					conn = new DBConn();
					session = conn.openSession();
					if(orgClId==null||orgClId.equals(""))
						return -1;
					
					String hql =  "select count(*) from Orgnet op where op.orgId='"+ orgClId.toString()+"'";
					
					//// System.out.println("========="+hql);
					Query query = session.createQuery(hql);
					
					//// System.out.println("query");
					List list = query.list();
					if (list != null && list.size() != 0) {
						result =((Integer) list.get(0)).intValue();
					}
				} catch (Exception e) {			
					log.printStackTrace(e);
					
					
				} finally {
					if (conn != null)
						conn.closeSession();
				}
				return result;
			}
		
			public static boolean create (OrgclsNetForm orgclsNetForm) throws Exception {
			       
			       boolean result = false;
			      DBConn conn =null;
			      Session session =null;
			      if(orgclsNetForm!=null)
			      {
			          try
			          {
			              conn = new DBConn();
			              session = conn.beginTransaction(); 
			              
			              MOrgClNet mOrgClNet = new  MOrgClNet();
			              mOrgClNet.setOrgClsId(orgclsNetForm.getOrgClsId());
			              mOrgClNet.setOrgClsNm(orgclsNetForm.getOrgClsNm());
			              mOrgClNet.setOrgClsType(orgclsNetForm.getOrgClsType());
			    //          dept.setProductUser(StrutsProductUserDelegate.getCurrentSysUser());
//			              dept.setProductUser(StrutsProductUserDelegate.getCurrentSysUser());
			              session.save(mOrgClNet);
			             // conn.endTransaction(true);
			              // System.out.println(mOrgClNet);
			              session.flush();
			              // System.out.println(mOrgClNet);
			              result = true;
			          }
			          catch(Exception e)
			          {
			              log.printStackTrace(e);
			              result = false;
			          }
			          finally{
			              if(conn!=null)
			                  conn.endTransaction(result);
			          }
			      }
			      return result;
			   }
		   
			public static boolean create11 (OrgnetForm orgnetForm) throws Exception {
			       
			       boolean result = false;
			      DBConn conn =null;
			      Session session =null;
			      if(orgnetForm!=null)
			      {
			          try
			          {
			              conn = new DBConn();
			              session = conn.beginTransaction(); 
			              
//			              Orgnet orgNet = new  Orgnet();
//			              
//			              orgNet.setOrgId(orgnetForm.getOrgId());
//			              orgNet.setOrgClsId(orgnetForm.getOrgClsId());
//			             /* orgNet.setOrgClsName(orgnetForm.getOrgClsName());*/
//			              orgNet.setOat_Id(orgnetForm.getOat_Id());
//			              orgNet.setOrgType(orgnetForm.getOrgType());
//			              orgNet.setRegionId(orgnetForm.getRegionId());
//			              orgNet.setIsCorp(orgnetForm.getIsCorp());
//			              orgNet.setOrgCode(orgnetForm.getOrgCode());	              
//			              orgNet.setParent_Org_Id(orgnetForm.getParent_Org_Id());
//			              orgNet.setOrgName(orgnetForm.getOrgName());		          
//		                  session.save(orgNet);
//			              session.flush();
			              
			              result = true;
			          }
			          catch(Exception e)
			          {
			              log.printStackTrace(e);
			              result = false;
			          }
			          finally{
			              if(conn!=null)
			                  conn.endTransaction(result);
			          }
			      }
			      return result;
			   }
			/**			   		    
	   		 * @author zhangxinke			   		   
	   		 * @return  新增org表时同时也在Morg表中新增		   		 
	   		 *//*
			public static void create111 (OrgnetForm orgnetForm) throws Exception {
				 			      
				com.gather.dao.DBConn  conn = new com.gather.dao.DBConn();
				Session session =null;
				String orgid=orgnetForm.getOrgId();
			      if(orgnetForm!=null)
			      {  
			          try
			          {
			        	  session=conn.beginTransaction();			              			              			             
			              com.gather.hibernate.MOrg mOrgPersistence = new com.gather.hibernate.MOrg();
			              
			              判断gather库中的MOrg表是否有与orgid相同的字段
			              if(StrutsOrgTogetherDelegate.selectgatherorg(orgid)==false)
			              {				            	  
			              mOrgPersistence.setOrgId(orgnetForm.getOrgId());
			              mOrgPersistence.setOrgName(orgnetForm.getOrgName());
			              mOrgPersistence.setOrgType(Integer.getInteger(orgnetForm.getOrgType()));
			              mOrgPersistence.setIsCorp(orgnetForm.getIsCorp());				              
				          session.save(mOrgPersistence);
				          // System.out.println("mOrgPersistencexxxxxxxxxooo  "+mOrgPersistence);
			              }				          
			              session.flush();
			          }
			          catch(Exception e)
			          {
			              log.printStackTrace(e);
			              
			          }
			         
			      }
			     
			   }
			判断gather库中的MOrg表是否有与orgid相同的字段的方法
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
					log.printStackTrace(e);	
					
				} finally {
					if (conn != null)
						conn.closeSession();
				}
				return result;
			}
				*/
			
			 public static boolean update2 (OrgclsNetForm orgclsNetForm) throws Exception {
				    
			       boolean result = false;
			       DBConn conn =null;
			       Session session =null;
			       if(orgclsNetForm!=null)
			       {
			           try
			           {
			               conn = new DBConn();
			               session = conn.beginTransaction(); 			               
			               MOrgClNet mOrgClNet = (MOrgClNet)session.load(MOrgClNet.class,orgclsNetForm.getOrgClsId());
			               mOrgClNet.setOrgClsNm((orgclsNetForm.getOrgClsNm()));
			               mOrgClNet.setOrgClsType((orgclsNetForm.getOrgClsType()));			                          
			               session.update(mOrgClNet);
			               session.flush();
			               result = true;
			           }
			           catch(Exception e)
			           {
			               log.printStackTrace(e);
			               result = false;
			           }
			           finally{
			               if(conn!=null)
			                   conn.endTransaction(result);
			           }
			       }
			      return result;
			   }
			 public static boolean update22 ( OrgnetForm orgnetForm) throws Exception {
				    
			       boolean result = false;
			       DBConn conn =null;
			       Session session =null;
			       if(orgnetForm!=null)
			       {
			           try
			           {
			               conn = new DBConn();
			               session = conn.beginTransaction(); 
			               
//			               Orgnet orgnet = (Orgnet)session.load(Orgnet.class,orgnetForm.getOrgId());
//			               orgnet.setOrgId((orgnetForm.getOrgId()));
//			               orgnet.setOrgName((orgnetForm.getOrgName()));
//			               orgnet.setOrgClsId((orgnetForm.getOrgClsId()));
//			               /*orgnet.setOrgClsName((orgnetForm.getOrgClsName()));*/
//			               orgnet.setOrgType((orgnetForm.getOrgType()));
//			               orgnet.setIsCorp((orgnetForm.getIsCorp()));
//			               orgnet.setParent_Org_Id((orgnetForm.getParent_Org_Id()));
//			               orgnet.setRegionId((orgnetForm.getRegionId()));
//			               orgnet.setOat_Id(orgnetForm.getOat_Id());			        
//			               session.update(orgnet);
			              
			               session.flush();
			               result = true;
			           }
			           catch(Exception e)
			           {
			               log.printStackTrace(e);
			               result = false;
			           }
			           finally{
			               if(conn!=null)
			                   conn.endTransaction(result);
			           }
			       }
			      return result;
			   }
			
			 public static boolean update222 ( OrgnetForm orgnetForm) throws Exception {
				    
			       boolean result = false;
			       DBConn conn =null;
			       Session session =null;
			       if(orgnetForm!=null)
			       {
			           try
			           {
			               conn = new DBConn();
			               session = conn.beginTransaction(); 
			               
//			               Orgnet orgnet = (Orgnet)session.load(Orgnet.class,orgnetForm.getOrgId());
//			               orgnet.setOrgId((orgnetForm.getOrgId()));			               			              
//			               orgnet.setDepartmentId(orgnetForm.getDepartmentId());
//			               orgnet.setDeptName(orgnetForm.getDeptName());
//			               session.update(orgnet);			              
//			               session.flush();
			               result = true;
			           }
			           catch(Exception e)
			           {
			               log.printStackTrace(e);
			               result = false;
			           }
			           finally{
			               if(conn!=null)
			                   conn.endTransaction(result);
			           }
			       }
			      return result;
			   }
			 public static boolean update223( OrgnetForm orgnetForm) throws Exception {
				    
			       boolean result = false;
			       DBConn conn =null;
			       Session session =null;
			       if(orgnetForm!=null)
			       {
			           try
			           {
			               conn = new DBConn();
			               session = conn.beginTransaction(); 
			               
//			               Orgnet orgnet = (Orgnet)session.load(Orgnet.class,orgnetForm.getOrgId());
//			               orgnet.setOrgId((orgnetForm.getOrgId()));			               			              
//			               orgnet.setOrglayerId(orgnetForm.getOrglayerId());
//			               orgnet.setOrglayer(orgnetForm.getOrglayer());
//			               session.update(orgnet);			              
//			               session.flush();
			               result = true;
			           }
			           catch(Exception e)
			           {
			               log.printStackTrace(e);
			               result = false;
			           }
			           finally{
			               if(conn!=null)
			                   conn.endTransaction(result);
			           }
			       }
			      return result;
			   }
			 
			 public static int getRecordCount4() throws Exception
			   {
			       int result =0;
			       DBConn conn =null;
			       Session session =null;
			       try
			       {
			           conn = new DBConn();
			           session = conn.openSession();
			           /*Query query = session.createQuery("select count(*) from com.fitech.net.orgcls.hibernate.MOrgClNet");*/
			           /*Query query = session.createQuery("select count(*) from RegionTypNet");*/
			          /* Query query = session.createQuery("select count(*) from com.gather.hibernate.MOrg");*/
			           Query query = session.createQuery("select count(*) from com.cbrc.org.hibernate.MOrgCl");
			           List list = query.list();
			           
			     
			           if(list!=null && list.size()!=0)
			               result = ((Integer)list.get(0)).intValue();
			           
			       }
			       catch(Exception e)
			       {
			           log.printStackTrace(e);
			           result = 0;
			       }
			       finally{
			           if(conn!=null)
			               conn.closeSession();
			       }
			      
			       return result;
			   }
			 
			 public static int getRecordCount44() throws Exception
			   {
			       int result =0;
			       DBConn conn =null;
			       Session session =null;
			       try
			       {
			           conn = new DBConn();
			           session = conn.openSession();			          
			           Query query = session.createQuery("select count(*) from OrgLayer");
			           List list = query.list();
			           
			     
			           if(list!=null && list.size()!=0)
			               result = ((Integer)list.get(0)).intValue();
			           
			       }
			       catch(Exception e)
			       {
			           log.printStackTrace(e);
			           result = 0;
			       }
			       finally{
			           if(conn!=null)
			               conn.closeSession();
			       }
			      
			       return result;
			   } 
			 
			 public static List select4(int offset,int limit) throws Exception {
			       List result = null;
			       DBConn conn = null;
			       Session session = null;
			       try
			       {
			           conn = new DBConn();
			           session = conn.openSession();
			       
			           Query query = session.createQuery("from RegionTypNet");
			           query.setFirstResult(offset);
			           query.setMaxResults(limit);
			           
			           List list = query.list();
			           if(list!=null && list.size()!=0)
			           {
			        	  
			        	   result = new ArrayList();
			               for(Iterator it = list.iterator(); it.hasNext();)
			               {
			            	   RegionTypNetForm orgnetFormTemp = new RegionTypNetForm();
			            	   RegionTypNet orgPersistence = (RegionTypNet)it.next();
			                   TranslatorUtil.copyPersistenceToVo4(orgPersistence, orgnetFormTemp);
			                   result.add(orgnetFormTemp);         
			               }       
			                    
			           }
			       }
			       catch(Exception e)
			       {
			           result = null;
			           log.printStackTrace(e);
			       }
			       finally{
			           if(conn!=null)
			              conn.closeSession();
			       }
			       return result;
			    }
			 
			 public static List select44(int offset,int limit) throws Exception {
			       List result = null;
			       DBConn conn = null;
			       Session session = null;
			       try
			       {
			           conn = new DBConn();
			           session = conn.openSession();
			       
			           Query query = session.createQuery("from OrgLayer");
			           query.setFirstResult(offset);
			           query.setMaxResults(limit);
			           
			           List list = query.list();
			           if(list!=null && list.size()!=0)
			           {
			        	  
			        	   result = new ArrayList();
			               for(Iterator it = list.iterator(); it.hasNext();)
			               {
			            	   OrgLayerForm orgnetFormTemp = new OrgLayerForm();
			            	   OrgLayer orgPersistence = (OrgLayer)it.next();
			                   TranslatorUtil.copyPersistenceToVo44(orgPersistence, orgnetFormTemp);
			                   result.add(orgnetFormTemp);         
			               }       
			                    
			           }
			       }
			       catch(Exception e)
			       {
			           result = null;
			           log.printStackTrace(e);
			       }
			       finally{
			           if(conn!=null)
			              conn.closeSession();
			       }
			       return result;
			    }
			 
			 public static boolean create5 (RegionTypNetForm orgclsNetForm) throws Exception {
			       
			       boolean result = false;
			      DBConn conn =null;
			      Session session =null;
			      if(orgclsNetForm!=null)
			      {
			          try
			          {
			              conn = new DBConn();
			              session = conn.beginTransaction(); 
			              
			              RegionTypNet mOrgClNet = new  RegionTypNet();
			              mOrgClNet.setRegionTypId(orgclsNetForm.getRegionTypId());
			              mOrgClNet.setRegionTypNm(orgclsNetForm.getRegionTypNm());
			    //          dept.setProductUser(StrutsProductUserDelegate.getCurrentSysUser());
//			              dept.setProductUser(StrutsProductUserDelegate.getCurrentSysUser());
			              session.save(mOrgClNet);
			             // conn.endTransaction(true);
			              // System.out.println(mOrgClNet);
			              session.flush();
			              // System.out.println(mOrgClNet);
			              result = true;
			          }
			          catch(Exception e)
			          {
			              log.printStackTrace(e);
			              result = false;
			          }
			          finally{
			              if(conn!=null)
			                  conn.endTransaction(result);
			          }
			      }
			      return result;
			   }
			 public static boolean create55(OrgLayerForm orgclsNetForm) throws Exception {
			       
			       boolean result = false;
			      DBConn conn =null;
			      Session session =null;
			      if(orgclsNetForm!=null)
			      {
			          try
			          {
			              conn = new DBConn();
			              session = conn.beginTransaction(); 
			              
			              OrgLayer mOrgClNet = new  OrgLayer();
			              mOrgClNet.setOrgLayerId(orgclsNetForm.getOrgLayerId());
			              mOrgClNet.setOrgLayer(orgclsNetForm.getOrgLayer());			         
			              session.save(mOrgClNet);			            
			              session.flush();			             
			              result = true;
			          }
			          catch(Exception e)
			          {
			              log.printStackTrace(e);
			              result = false;
			          }
			          finally{
			              if(conn!=null)
			                  conn.endTransaction(result);
			          }
			      }
			      return result;
			   }
				public static int getRegionTypId(RegionTypNetForm regionTypNetForm ) {
					int result = 0;
					DBConn conn = null;
					Session session = null;
					try {
						String orgClId = regionTypNetForm.getRegionTypId();
						// System.out.println("regionTypNetForm.getRegionTypId()  "+regionTypNetForm.getRegionTypId());
						// System.out.println("regionTypNetForm.getRegionTypNm()  "+regionTypNetForm.getRegionTypNm());
						conn = new DBConn();
						session = conn.openSession();
						if(orgClId==null||orgClId.equals(""))
							return -1;
						
						String hql =  "select count(*) from RegionTypNet op where op.regionTypId='"+ orgClId.toString()+"'";
						
						//// System.out.println("========="+hql);
						Query query = session.createQuery(hql);
						// System.out.println("hql  "+hql);
						//// System.out.println("query");
						List list = query.list();
						if (list != null && list.size() != 0) {
							result =((Integer) list.get(0)).intValue();
						}
					} catch (Exception e) {			
						log.printStackTrace(e);
						
						
					} finally {
						if (conn != null)
							conn.closeSession();
					}
					return result;
				}
				
				public static int getRegionTypId1(OrgLayerForm regionTypNetForm ) {
					int result = 0;
					DBConn conn = null;
					Session session = null;
					try {
						String orgClId = regionTypNetForm.getOrgLayerId();
						
						conn = new DBConn();
						session = conn.openSession();
						if(orgClId==null||orgClId.equals(""))
							return -1;
						
						String hql =  "select count(*) from OrgLayer op where op.orgLayerId='"+ orgClId.toString()+"'";
						
						//// System.out.println("========="+hql);
						Query query = session.createQuery(hql);
						// System.out.println("hql  "+hql);
						//// System.out.println("query");
						List list = query.list();
						if (list != null && list.size() != 0) {
							result =((Integer) list.get(0)).intValue();
						}
					} catch (Exception e) {			
						log.printStackTrace(e);
						
						
					} finally {
						if (conn != null)
							conn.closeSession();
					}
					return result;
				}
			 
				public static boolean update5 (RegionTypNetForm regionTypNetForm) throws Exception {
				    
				       boolean result = false;
				       DBConn conn =null;
				       Session session =null;
				       if(regionTypNetForm!=null)
				       {
				           try
				           {
				               conn = new DBConn();
				               session = conn.beginTransaction(); 
				               
				               RegionTypNet regionTypNet = (RegionTypNet)session.load(RegionTypNet.class,regionTypNetForm.getRegionTypId());
				               regionTypNet.setRegionTypId((regionTypNetForm.getRegionTypId()));
				               regionTypNet.setRegionTypNm((regionTypNetForm.getRegionTypNm()));
				                          
				               session.update(regionTypNet);
				               session.flush();
				               result = true;
				           }
				           catch(Exception e)
				           {
				               log.printStackTrace(e);
				               result = false;
				           }
				           finally{
				               if(conn!=null)
				                   conn.endTransaction(result);
				           }
				       }
				      return result;
				   }

				public static boolean update55 (OrgLayerForm regionTypNetForm) throws Exception {
				    
				       boolean result = false;
				       DBConn conn =null;
				       Session session =null;
				       if(regionTypNetForm!=null)
				       {
				           try
				           {
				               conn = new DBConn();
				               session = conn.beginTransaction(); 
				               
				               OrgLayer regionTypNet = (OrgLayer)session.load(OrgLayer.class,regionTypNetForm.getOrgLayerId());
				               regionTypNet.setOrgLayerId((regionTypNetForm.getOrgLayerId()));
				               regionTypNet.setOrgLayer((regionTypNetForm.getOrgLayer()));
				                          
				               session.update(regionTypNet);
				               session.flush();
				               result = true;
				           }
				           catch(Exception e)
				           {
				               log.printStackTrace(e);
				               result = false;
				           }
				           finally{
				               if(conn!=null)
				                   conn.endTransaction(result);
				           }
				       }
				      return result;
				   }
				public static int getMOrgFromorgClsId2(String regionTypId) {
					int result = 0;
					DBConn conn = null;
					Session session = null;
					try {
						conn = new DBConn();
						session = conn.openSession();
						if(regionTypId==null||regionTypId.equals(""))
							return -1;
						
						String hql =  "select count(*) from Region op where op.regionTypId='"+ regionTypId.toString()+"'";
						
						//// System.out.println("========="+hql);
						Query query = session.createQuery(hql);
						
						//// System.out.println("query");
						List list = query.list();
						if (list != null && list.size() != 0) {
							result =((Integer) list.get(0)).intValue();
						}
					} catch (Exception e) {			
						log.printStackTrace(e);
						
						
					} finally {
						if (conn != null)
							conn.closeSession();
					}
					return result;
				}
				
				public static int getMOrgFromorgClsId22(String regionTypId) {
					int result = 0;
					DBConn conn = null;
					Session session = null;
					try {
						conn = new DBConn();
						session = conn.openSession();
						if(regionTypId==null||regionTypId.equals(""))
							return -1;
						
						String hql =  "select count(*) from Orgnet op where op.orgLayerId='"+ regionTypId.toString()+"'";
						
						//// System.out.println("========="+hql);
						Query query = session.createQuery(hql);
						
						//// System.out.println("query");
						List list = query.list();
						if (list != null && list.size() != 0) {
							result =((Integer) list.get(0)).intValue();
						}
					} catch (Exception e) {			
						log.printStackTrace(e);
						
						
					} finally {
						if (conn != null)
							conn.closeSession();
					}
					return result;
				}
				
				public static int getOrgFromorgClsId22(String regionId) {
					int result = 0;
					DBConn conn = null;
					Session session = null;
					try {
						conn = new DBConn();
						session = conn.openSession();
						if(regionId==null||regionId.equals(""))
							return -1;
						
						String hql =  "select count(*) from Orgnet op where op.regionId='"+ regionId.toString()+"'";
						
						//// System.out.println("========="+hql);
						Query query = session.createQuery(hql);
						
						//// System.out.println("query");
						List list = query.list();
						if (list != null && list.size() != 0) {
							result =((Integer) list.get(0)).intValue();
						}
					} catch (Exception e) {			
						log.printStackTrace(e);
						
						
					} finally {
						if (conn != null)
							conn.closeSession();
					}
					return result;
				}
				  public static boolean remove6(RegionTypNetForm regionTypNetForm) throws Exception {
					     boolean result = false;
					     
					     DBConn conn =null;
					     Session session =null;
					     if(regionTypNetForm!=null)
					     {
					         try
					         {
					             conn = new DBConn();
					             session = conn.beginTransaction(); 
					             
					             Region regionTypNet = (Region)session.load(Region.class,regionTypNetForm.getRegionTypId());
					             session.delete(regionTypNet);
					             session.flush();
					             result = true;
		
					         
					         
					         
					         }
					         catch(Exception e)
					         {
					             log.printStackTrace(e);
					             result = false;
					         }
					         finally{
					             if(conn!=null)
					                 conn.endTransaction(result);
					         }
					     }
					         
					     return result;
					   } 
				  
				  public static boolean remove66(OrgLayerForm regionTypNetForm) throws Exception {
					     boolean result = false;
					     
					     DBConn conn =null;
					     Session session =null;
					     if(regionTypNetForm!=null)
					     {
					         try
					         {
					             conn = new DBConn();
					             session = conn.beginTransaction(); 
					             
					            OrgLayer regionTypNet = (OrgLayer)session.load(OrgLayer.class,regionTypNetForm.getOrgLayerId());
					             session.delete(regionTypNet);
					             session.flush();
					             result = true;
		
					         
					         
					         
					         }
					         catch(Exception e)
					         {
					             log.printStackTrace(e);
					             result = false;
					         }
					         finally{
					             if(conn!=null)
					                 conn.endTransaction(result);
					         }
					     }
					         
					     return result;
					   } 
				  public static boolean remove66(RegionForm regionForm) throws Exception {
					     boolean result = false;
					     
					     DBConn conn =null;
					     Session session =null;
					     if(regionForm!=null)
					     {
					         try
					         {
					             conn = new DBConn();
					             session = conn.beginTransaction(); 
					             
					             Region regionTypNet = (Region)session.load(Region.class,regionForm.getRegionId());
					             session.delete(regionTypNet);
					             session.flush();
					             result = true;
		
					         
					         
					         
					         }
					         catch(Exception e)
					         {
					             log.printStackTrace(e);
					             result = false;
					         }
					         finally{
					             if(conn!=null)
					                 conn.endTransaction(result);
					         }
					     }
					         
					     return result;
					   } 
				  
				  public static int getRegionTypId2(String orgClId) {
						int result = 0;
						DBConn conn = null;
						Session session = null;
						try {
							conn = new DBConn();
							session = conn.openSession();
							if(orgClId==null||orgClId.equals(""))
								return -1;
							
							String hql =  "select count(*) from Region op where op.regionId='"+ orgClId.toString()+"'";
							
							//// System.out.println("========="+hql);
							Query query = session.createQuery(hql);
							
							//// System.out.println("query");
							List list = query.list();
							if (list != null && list.size() != 0) {
								result =((Integer) list.get(0)).intValue();
							}
						} catch (Exception e) {			
							log.printStackTrace(e);
							
							
						} finally {
							if (conn != null)
								conn.closeSession();
						}
						return result;
					}
				
				  public static boolean create6(RegionForm regionForm) throws Exception {
				       
				       boolean result = false;
				      DBConn conn =null;
				      Session session =null;
				      if(regionForm!=null)
				      {
				          try
				          {
				              conn = new DBConn();
				              session = conn.beginTransaction(); 
				              
				              Region regionNet = new  Region();
				              regionNet.setRegionId(regionForm.getRegionId());
				              regionNet.setRegionName(regionForm.getRegionName());
				              regionNet.setRegionTypId(regionForm.getRegionTypId());
				    //          dept.setProductUser(StrutsProductUserDelegate.getCurrentSysUser());
//				              dept.setProductUser(StrutsProductUserDelegate.getCurrentSysUser());
				              session.save(regionNet);
				             // conn.endTransaction(true);
				              // System.out.println(regionNet);
				              session.flush();
				              // System.out.println(regionNet);
				              result = true;
				          }
				          catch(Exception e)
				          {
				              log.printStackTrace(e);
				              result = false;
				          }
				          finally{
				              if(conn!=null)
				                  conn.endTransaction(result);
				          }
				      }
				      return result;
				   }
				  
				  public static boolean update7(RegionForm regionForm) throws Exception {
					    
				       boolean result = false;
				       DBConn conn =null;
				       Session session =null;
				       if(regionForm!=null)
				       {
				           try
				           {
				               conn = new DBConn();
				               session = conn.beginTransaction(); 
				               
				               Region regionNet = (Region)session.load(Region.class,regionForm.getRegionId());
				               regionNet.setRegionId((regionForm.getRegionId()));
				               regionNet.setRegionName((regionForm.getRegionName()));
				               regionNet.setRegionTypId((regionForm.getRegionTypId()));           
				               session.update(regionNet);
				               session.flush();
				               result = true;
				           }
				           catch(Exception e)
				           {
				               log.printStackTrace(e);
				               result = false;
				           }
				           finally{
				               if(conn!=null)
				                   conn.endTransaction(result);
				           }
				       }
				      return result;
				   }
				  
				  /**
				    * 取得所有机构分类类型记录
				    *
				    * @exception   Exception  
				    */
				   public static List findAllorgclsNetForm () throws Exception {
				      List retVals = null;
				      
				      DBConn conn=null;
				      
				      try{
				    	  conn=new DBConn();
				    	  List list=conn.openSession().find("from MOrgClNet");

				    	  if(list!=null && list.size()>0){
				    		  retVals=new ArrayList();
				    		  for(int i=0;i<list.size();i++){
				    			  MOrgClNet mOrgClNet = (MOrgClNet)list.get(i);
				    			  OrgclsNetForm mOrgClNetForm = new OrgclsNetForm();
				    			  TranslatorUtil.copyPersistenceToVo10(mOrgClNet,mOrgClNetForm);
				    			  retVals.add(mOrgClNetForm);
				    		  }
				    	  }
				      }catch(HibernateException he){
				    	  log.printStackTrace(he);
				      }finally{
				    	  if(conn!=null) conn.closeSession();
				      }
				      return retVals;
				   }
				   
				  /* *//**
				    * 从orgnet中取得所有orgClsId条数
				    *
				    * @exception   Exception  
				    *//*
				   public static List findAllorgclsIdFormOrg () throws Exception {
				      List retVals = null;				      
				      DBConn conn=null;				      
				      try{
				    	 
				    	  List list=conn.openSession().find("from Orgnet");
				    	  if(list!=null && list.size()>0){
				    		  retVals=new ArrayList();
				    		  for(int i=0;i<list.size();i++){
				    			  Orgnet mOrgClNet = (Orgnet)list.get(i);
				    			  OrgnetForm mOrgClNetForm = new OrgnetForm();
				    			 TranslatorUtil.copyPersistenceToVo100(mOrgClNet,mOrgClNetForm);
				    			 retVals.add(mOrgClNetForm);
				    		  }
				    	  }
				      }catch(HibernateException he){
				    	  log.printStackTrace(he);
				      }finally{
				    	  if(conn!=null) conn.closeSession();
				      }
				      return retVals;				      
				   }
				   */
				   /**获得oatId
				    * @exception   Exception  
				    */
				   public static List findOatId() throws Exception {
					      List retVals = null;					      
					      DBConn conn=null;					      
					      try{
					    	  conn=new DBConn();
					    	  List list=conn.openSession().find("from OrgActuType");

					    	  if(list!=null && list.size()>0){
					    		  retVals=new ArrayList();
					    		  for(int i=0;i<list.size();i++){
					    			  OrgActuType orgActuType = (OrgActuType)list.get(i);
					    			  OrgActuTypeForm orgActuTypeForm = new OrgActuTypeForm();
					    			  TranslatorUtil.copyPersistenceToVo111(orgActuType,orgActuTypeForm);
					    			  retVals.add(orgActuTypeForm);
					    		  }
					    	  }
					      }catch(HibernateException he){
					    	  log.printStackTrace(he);
					      }finally{
					    	  if(conn!=null) conn.closeSession();
					      }
					      return retVals;
					   }
				   public static List findAllCopy () throws Exception {
					      List retVals = null;
					      
					      DBConn conn=null;
					      
					      try{
					    	  conn=new DBConn();
					    	  List list=conn.openSession().find("from MOrgClNet");

					    	  if(list!=null && list.size()>0){
					    		  retVals=new ArrayList();
					    		  for(int i=0;i<list.size();i++){
					    			  MOrgClNet mOrgClNet = (MOrgClNet)list.get(i);
					    			  OrgclsNetForm orgclsNetForm = new OrgclsNetForm();
					    			  TranslatorUtil.copyPersistenceToVoOrgcls(mOrgClNet,orgclsNetForm);
					    			  retVals.add(orgclsNetForm);
					    		  }
					    	  }
					      }catch(HibernateException he){
					    	  log.printStackTrace(he);
					      }finally{
					    	  if(conn!=null) conn.closeSession();
					      }
					      return retVals;
					   }

				   
				   /**获得地区类型
				    * @exception   Exception  
				    */
				   public static List findAllfindAllregiontyp() throws Exception {
					      List retVals = null;					      
					      DBConn conn=null;					      
					      try{
					    	  conn=new DBConn();
					    	  List list=conn.openSession().find("from Region");

					    	  if(list!=null && list.size()>0){
					    		  retVals=new ArrayList();
					    		  for(int i=0;i<list.size();i++){
					    			  Region regionTypNet = (Region)list.get(i);
					    			  RegionForm regionTypNetForm = new RegionForm();
					    			  TranslatorUtil.copyPersistenceToVo11(regionTypNet,regionTypNetForm);
					    			  retVals.add(regionTypNetForm);
					    		  }
					    	  }
					      }catch(HibernateException he){
					    	  log.printStackTrace(he);
					      }finally{
					    	  if(conn!=null) conn.closeSession();
					      }
					      return retVals;
					   }
				   /**获得地区类型
				    * @exception   Exception  
				    */
				   public static List findAllfindAllregion() throws Exception {
					      List retVals = null;					      
					      DBConn conn=null;					      
					      try{
					    	  conn=new DBConn();
					    	  List list=conn.openSession().find("from Region");

					    	  if(list!=null && list.size()>0){
					    		  retVals=new ArrayList();
					    		  for(int i=0;i<list.size();i++){
					    			  Region regionTypNet = (Region)list.get(i);
					    			  RegionForm regionTypNetForm = new RegionForm();
					    			  TranslatorUtil.copyPersistenceToVo11(regionTypNet,regionTypNetForm);
					    			  retVals.add(regionTypNetForm);
					    		  }
					    	  }
					      }catch(HibernateException he){
					    	  log.printStackTrace(he);
					      }finally{
					    	  if(conn!=null) conn.closeSession();
					      }
					      return retVals;
					   } 
				   
				   public static List findAllfindAllDeptName() throws Exception {
					      List retVals = null;					      
					      DBConn conn=null;					      
					      try{
					    	  conn=new DBConn();
					    	  List list=conn.openSession().find("from Orgnet org where 1=1");

					    	  if(list!=null && list.size()>0){
					    		  retVals=new ArrayList();
					    		  for(int i=0;i<list.size();i++){
//					    			  Orgnet regionTypNet = (Orgnet)list.get(i);
//					    			  OrgnetForm regionTypNetForm = new OrgnetForm();
//					    			  TranslatorUtil.copyPersistenceToVo111(regionTypNet,regionTypNetForm);
//					    			  retVals.add(regionTypNetForm);
					    		  }
					    	  }
					      }catch(HibernateException he){
					    	  log.printStackTrace(he);
					      }finally{
					    	  if(conn!=null) conn.closeSession();
					      }
					      return retVals;
					   }
				   /**
					 * 根据查询条件,取出该附和该条件的模板数量
					 * 
					 * @author 姚捷
					 * @param mChildReportForm
					 *            MChildReportForm 包含查询条件
					 * @return List 包含查询到的所有记录
					 * @exception Exception
					 *                If the com.cbrc.smis.form.MChildReportForm objects cannot
					 *                be retrieved.
					 */
					public static int getTemplateCount2(String orgId)
							throws Exception {
						int recordCount = 0;
						List retVals = null;
						DBConn conn = null;

						try {
//							StringBuffer hql = new StringBuffer(
//								"select count(*) from MChildReport mcr where mcr.templateType='excel'");
							StringBuffer hql = new StringBuffer(
							"select count(*) from ReportIn ri where ri.orgId='"+orgId+
							"' and ri.checkFlag="+Config.CHECK_FLAG_UNREPORT);
							// System.out.println(hql);
							/*StringBuffer where = new StringBuffer("");

							if (mChildReportForm != null) {
								String reportName = mChildReportForm.getReportName();

								if (reportName != null && !reportName.equals(""))
									where.append(" and mcr.reportName like'%" + reportName.trim()
											+ "%'");
							}
							hql.append(where.toString());*/

							conn = new DBConn();

							Session session = conn.openSession();
							Query query = session.createQuery(hql.toString());
							retVals = query.list();
							if (retVals != null && retVals.size() != 0) {
								recordCount = Integer.parseInt(retVals.get(0).toString());
							}
						} catch (HibernateException he) {
							log.printStackTrace(he);
						} finally {
							if (conn != null)
								conn.closeSession();
						}

						return recordCount;


					}

				   
				   /**
					 * 根据查询条件,取出该附和该条件的模板数量
					 * 
					 * @author
					 */
					public static int getTemplateCount(MChildReportForm mChildReportForm)
							throws Exception {
						int recordCount = 0;
						List retVals = null;
						DBConn conn = null;

						try {
							StringBuffer hql = new StringBuffer(
								"select count(*) from MChildReport mcr where mcr.templateType='excel'");
							// System.out.println("hql  "+hql);
							/*StringBuffer where = new StringBuffer("");

							if (mChildReportForm != null) {
								String reportName = mChildReportForm.getReportName();

								if (reportName != null && !reportName.equals(""))
									where.append(" and mcr.reportName like'%" + reportName.trim()
											+ "%'");
							}
							hql.append(where.toString());*/

							conn = new DBConn();

							Session session = conn.openSession();
							Query query = session.createQuery(hql.toString());
							retVals = query.list();
							if (retVals != null && retVals.size() != 0) {
								recordCount = Integer.parseInt(retVals.get(0).toString());
							}
						} catch (HibernateException he) {
							log.printStackTrace(he);
						} finally {
							if (conn != null)
								conn.closeSession();
						}

						return recordCount;

					}
					
					/**
					 * 根据条件查询模板
					 * 
					 * @author 
					 * @exception Exception
					 *                If the com.cbrc.smis.form.MChildReportForm objects cannot
					 *                be retrieved.
					 */
					public static List getTemplates2(int offset, int limit,String orgId) throws Exception {

						List retVals = null;
						DBConn conn = null;
//						String s="from ReportIn ri where ri.orgId='"+orgId+"' and ri.checkFlag=3";

						try {
//							StringBuffer hql = new StringBuffer(
//									"select mcr.comp_id.childRepId,mcr.comp_id.versionId,"
//											+ "mcr.reportName,mcr.MMainRep.MRepType.repTypeName,mcr.templateType "
//											+ "from MChildReport mcr where mcr.templateType='excel'");
							StringBuffer hql = new StringBuffer(
									"from ReportIn ri where ri.orgId='"+orgId+"' and ri.checkFlag="+Config.CHECK_FLAG_PASS);
							/*StringBuffer where = new StringBuffer("");

							if (mChildReportForm != null) {
								String reportName = mChildReportForm.getReportName();
								if (reportName != null && !reportName.equals(""))
									where.append(" and mcr.reportName like'%" + reportName.trim()
											+ "%'");
							}
							hql.append(where.toString());*/
//							// System.out.println("hql   "+hql);
							conn = new DBConn();

							Session session = conn.openSession();
							Query query = session.createQuery(hql.toString());
							query.setFirstResult(offset);
							query.setMaxResults(limit);
							retVals=query.list();
//							List list = query.list();
//                            // System.out.println("list  "+list);
//							if (list != null && list.size() > 0) {
//								Iterator it = list.iterator();
//								retVals = new ArrayList();
//								while (it.hasNext()) {
//									Object[] item = (Object[]) it.next();
//									MChildReportForm form = new MChildReportForm();
//									form.setChildRepId(item[0] != null ? (String) item[0] : "");
//									form.setVersionId(item[1] != null ? (String) item[1] : "");
//									form.setReportName(item[2] != null ? (String) item[2] : "");
//									form
//											.setRepTypeName(item[3] != null ? (String) item[3]
//													: "");
//									/*form
//											.setIsPublic(item[4] != null ? (Integer) item[4]
//													: null);
//									form
//											.setCurUnitName(item[5] != null ? (String) item[5]
//													: "");*/
//									form
//									.setTemplateType(item[4] != null ? (String) item[4]
//											: "");
//									retVals.add(form);
//								}
//							}
						} catch (HibernateException he) {
							log.printStackTrace(he);
						} finally {
							if (conn != null)
								conn.closeSession();
						}

						return retVals;
					}

					/**
					 * 根据条件查询模板
					 * 
					 * @author 
					 */
					public static List getTemplates(MChildReportForm mChildReportForm,
							int offset, int limit) throws Exception {

						List retVals = null;
						DBConn conn = null;

						try {
							StringBuffer hql = new StringBuffer(
									"select mcr.comp_id.childRepId,mcr.comp_id.versionId,"
											+ "mcr.reportName,mcr.MMainRep.MRepType.repTypeName,mcr.templateType "
											+ "from MChildReport mcr where mcr.templateType='excel'");
							/*StringBuffer where = new StringBuffer("");

							if (mChildReportForm != null) {
								String reportName = mChildReportForm.getReportName();
								if (reportName != null && !reportName.equals(""))
									where.append(" and mcr.reportName like'%" + reportName.trim()
											+ "%'");
							}
							hql.append(where.toString());*/
							// System.out.println("hql   "+hql);
							conn = new DBConn();

							Session session = conn.openSession();
							Query query = session.createQuery(hql.toString());
							query.setFirstResult(offset);
							query.setMaxResults(limit);
							List list = query.list();
                            // System.out.println("list  "+list);
							if (list != null && list.size() > 0) {
								Iterator it = list.iterator();
								retVals = new ArrayList();
								while (it.hasNext()) {
									Object[] item = (Object[]) it.next();
									MChildReportForm form = new MChildReportForm();
									form.setChildRepId(item[0] != null ? (String) item[0] : "");
									form.setVersionId(item[1] != null ? (String) item[1] : "");
									form.setReportName(item[2] != null ? (String) item[2] : "");
									form
											.setRepTypeName(item[3] != null ? (String) item[3]
													: "");
									/*form
											.setIsPublic(item[4] != null ? (Integer) item[4]
													: null);
									form
											.setCurUnitName(item[5] != null ? (String) item[5]
													: "");*/
									form
									.setTemplateType(item[4] != null ? (String) item[4]
											: "");
									retVals.add(form);
								}
							}
						} catch (HibernateException he) {
							log.printStackTrace(he);
						} finally {
							if (conn != null)
								conn.closeSession();
						}

						return retVals;
					}
					/**
					 * 在org中是否有orgClId
					 * 
					 * @author zhangxinke
					 */
					public static int getOrgIdfrom(String orgClId) {
						int result = 0;
						DBConn conn = null;
						Session session = null;
						try {
							conn = new DBConn();
							session = conn.openSession();
							if(orgClId==null||orgClId.equals(""))
								return -1;
							
							String hql =  "select count(*) from MOrgClNet op where op.orgClsId='"+ orgClId.toString()+"'";
							
							//// System.out.println("========="+hql);
							Query query = session.createQuery(hql);
							
							//// System.out.println("query");
							List list = query.list();
							if (list != null && list.size() != 0) {
								result =((Integer) list.get(0)).intValue();
							}
						} catch (Exception e) {			
							log.printStackTrace(e);
							
							
						} finally {
							if (conn != null)
								conn.closeSession();
						}
						return result;
					}
					
			/*		public static int getRecordCount1(OrgnetForm orgnetForm) throws Exception
					   {   
						   int recordCount = 0;
							List retVals = null;
							DBConn conn = null;					     
					       try
					       {   // System.out.println("zhangxinke "+orgnetForm.getOrgId());
					    	   StringBuffer hql = new StringBuffer("select count(*) from Orgnet orgnet where 1=1");
					    	   StringBuffer hql = new StringBuffer("select count(*) from com.gather.hibernate.MOrg where 1=1");
							   StringBuffer where = new StringBuffer("");
					    	   String event=orgnetForm.getEvent();
					    	  
							  if (orgnetForm != null) {
									String orgId= orgnetForm.getOrgId();
				                    if("add".equals(event)==true)
									{
				                    	where.append("");
								}else{
									if (orgId != null && !orgId.equals(""))
										where.append(" and orgnet.orgId like'%" + orgId.trim()
												+ "%'");
								}
							  }
								hql.append(where.toString());

								conn = new DBConn();

								Session session = conn.openSession();
								Query query = session.createQuery(hql.toString());
								retVals = query.list();
								if (retVals != null && retVals.size() != 0) {
									recordCount = Integer.parseInt(retVals.get(0).toString());
								}
							} catch (HibernateException he) {
								log.printStackTrace(he);
							} finally {
								if (conn != null)
									conn.closeSession();
							}
							// System.out.println("recordCount "+recordCount);
							return recordCount;
					   }*/
					public static int getRecordCount1(OrgnetForm orgnetForm) 
					throws Exception {
				int recordCount = 0;
				List retVals = null;
				DBConn conn = null;

				try {
					StringBuffer hql = new StringBuffer(
						"select count(*) from Orgnet orgnet where 1=1");
					StringBuffer where = new StringBuffer("");

					if (orgnetForm != null) {
						String orgName= orgnetForm.getOrgName();

						if (orgName != null && !orgName.equals(""))
							where.append(" and orgnet.orgName like'%" + orgName.trim()
									+ "%'");
					}
					hql.append(where.toString());

					conn = new DBConn();

					Session session = conn.openSession();
					Query query = session.createQuery(hql.toString());
					retVals = query.list();
					if (retVals != null && retVals.size() != 0) {
						recordCount = Integer.parseInt(retVals.get(0).toString());
					}
				} catch (HibernateException he) {
					log.printStackTrace(he);
				} finally {
					if (conn != null)
						conn.closeSession();
				}

				return recordCount;

			}   
  
             }
