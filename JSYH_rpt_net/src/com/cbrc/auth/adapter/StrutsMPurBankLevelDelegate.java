
package com.cbrc.auth.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.auth.form.MBankLevelForm;
import com.cbrc.auth.form.MPurBanklevelForm;
import com.cbrc.auth.hibernate.MBankLevel;
import com.cbrc.auth.hibernate.MPurBanklevel;
import com.cbrc.auth.hibernate.MPurBanklevelKey;
import com.cbrc.auth.hibernate.MUserGrp;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.util.FitechException;

public class StrutsMPurBankLevelDelegate {
    private static FitechException log = new FitechException(StrutsMPurBankLevelDelegate.class);
    
    /** 
     * ��ʹ��hibernate ���Ը� 2011-12-28
     * Ӱ�����MPurBanklevel
     * ͨ���û���idȡ�ø��û����Ѿ����ù����м����� 
     * @param userGrpId �û���ID
     * @param powType Ȩ������
     * @return �û������ù����м�����  
     */
    public static List getUserGrpBankLevelPopedom(Long userGrpId,Integer powType){
    	List result = null;
    	DBConn conn = null;
    	Session session = null;
    	try{ 
    		if(userGrpId != null && powType != null){
    			conn = new DBConn();
    			session = conn.openSession();    			
    			String hql = "select distinct mbl.comp_id.MBankLevel.bankLevelId,mbl.comp_id.MBankLevel.bankLevelName from MPurBanklevel mbl " +
    					"where mbl.comp_id.MUserGrp.userGrpId=" + userGrpId + " and mbl.comp_id.powType=" + powType;
    			
    			Query query = session.createQuery(hql);
    			List list = query.list();
    			if(list != null && list.size() > 0){
    				result = new ArrayList();
    				for(int i=0;i<list.size();i++){    					
    					Object[] object = (Object[])list.get(i);
    					MBankLevelForm mBankLevelForm = new MBankLevelForm();
    					mBankLevelForm.setBankLevelId(new Integer(object[0].toString()));
    					mBankLevelForm.setBankLevelName(object[1].toString());
    					result.add(mBankLevelForm);
    				}
    			}
    		}
    	}catch(Exception ex){
    		result = null;
    		log.printStackTrace(ex);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return result;
    }
    
    /**
     * ��ʹ��hibernate ���Ը� 2011-12-28
     * Ӱ�����MPurBanklevel
     * ͨ���û���ȡ�ø��û����Ѿ����ù����м����� 
     * @param powType Ȩ������
     * @param userGrpIds �û���IDs
     * @return �û������ù����м�����
     */
    public static List getUserGrpBankLevelPopedom(String userGrpIds,Integer powType){
    	List result = null;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpIds != null && !userGrpIds.equals("") && powType != null){
    			conn = new DBConn();
    			session = conn.openSession();    			
    			String hql = "select distinct mbl.comp_id.MBankLevel.bankLevelId,mbl.comp_id.MBankLevel.bankLevelName from MPurBanklevel mbl " +
    					"where mbl.comp_id.MUserGrp.userGrpId in (" + userGrpIds + ") and mbl.comp_id.powType=" + powType;    			    		
    			
    			Query query = session.createQuery(hql);
    			List list = query.list();
    			if(list != null && list.size() > 0){
    				result = new ArrayList();
    				for(int i=0;i<list.size();i++){
    					Object[] object = (Object[])list.get(i);
    					MBankLevelForm mBankLevelForm = new MBankLevelForm();
    					mBankLevelForm.setBankLevelId(new Integer(object[0].toString()));
    					mBankLevelForm.setBankLevelName(object[1].toString());
    					result.add(mBankLevelForm);
    				}
    			}
    		}
    	}catch(Exception ex){
    		result = null;
    		log.printStackTrace(ex);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return result;
    }
    
    /**
     * ȡ�ö���û������õı���Ȩ��(�����м���ʵ�ʻ�������ı���Ȩ��) 
     * @param powType Ȩ������
     * @param userGrpIds String �û���ids
     * @return �û������ù��ı���Ȩ����Ϣ
     */
    public static List getUserGrpRepPopedom(String userGrpIds,Integer powType){
        List result = null;
        DBConn conn = null;
        Session session = null;
        try{
            if(userGrpIds!=null && !userGrpIds.equals("") && powType != null){
                conn = new DBConn();
                session = conn.openSession();
                
                String hql = "select distinct mcr.comp_id.childRepId,mcr.reportName from MChildReport mcr where mcr.comp_id.childRepId in" +
   			 				 "(select distinct mpbl.comp_id.childRepId from MPurBanklevel mpbl where mpbl.comp_id.MUserGrp.userGrpId in(" + userGrpIds 
   			 				 + ") and mpbl.comp_id.powType=" + powType + ") or mcr.comp_id.childRepId in (select distinct mpo.comp_id.childRepId " 
   			 				 + "from MPurOrg mpo where mpo.comp_id.MUserGrp.userGrpId in(" + userGrpIds + ") and mpo.comp_id.powType= "+ powType 
   			 				 + ") order by mcr.comp_id.childRepId";
                
                Query query = session.createQuery(hql);
                
                List list = query.list();
                if(list!=null && list.size()!=0){
                    result = new ArrayList();
                    for(Iterator it = list.iterator(); it.hasNext();){
                    	Object[] item = (Object[]) it.next();
    					MChildReportForm form = new MChildReportForm();
    					form.setChildRepId(item[0] != null ? (String) item[0] : "");					
    					form.setReportName(item[1] != null ? (String) item[1] : "");
                        result.add(form);           
                    }       
                }
            }
        }catch(Exception e){
            result = null;
            log.printStackTrace(e);
        }finally{
            if(conn!=null)
               conn.closeSession();
        }
        return result;
    }
    
    /**
     * ȡ�ö���û������õı���Ȩ��(�����м���ʵ�ʻ�������ı���Ȩ��) 
     * @param powType Ȩ������
     * @param userGrpIds String �û���ids
     * @return �û������ù��ı���Ȩ����Ϣ
     */
    public static List getUserGrpRepPopedomFromAF(String userGrpIds,Integer powType){
        List result = null;
        DBConn conn = null;
        Session session = null;
        try{
            if(userGrpIds!=null && !userGrpIds.equals("") && powType != null){
                conn = new DBConn();
                session = conn.openSession();
                
                String hql = "select distinct mcr.id.templateId,mcr.templateName from AfTemplate mcr where mcr.id.templateId in" +
   			 				 "(select distinct mpbl.comp_id.childRepId from MPurBanklevel mpbl where mpbl.comp_id.MUserGrp.userGrpId in(" + userGrpIds 
   			 				 + ") and mpbl.comp_id.powType=" + powType + ") or mcr.id.templateId in (select distinct mpo.comp_id.childRepId " 
   			 				 + "from MPurOrg mpo where mpo.comp_id.MUserGrp.userGrpId in(" + userGrpIds + ") and mpo.comp_id.powType= "+ powType 
   			 				 + ") order by mcr.id.templateId";
                
                Query query = session.createQuery(hql);
                
                List list = query.list();
                if(list!=null && list.size()!=0){
                    result = new ArrayList();
                    for(Iterator it = list.iterator(); it.hasNext();){
                    	Object[] item = (Object[]) it.next();
    					MChildReportForm form = new MChildReportForm();
    					form.setChildRepId(item[0] != null ? (String) item[0] : "");					
    					form.setReportName(item[1] != null ? (String) item[1] : "");
                        result.add(form);           
                    }       
                }
            }
        }catch(Exception e){
            result = null;
            log.printStackTrace(e);
        }finally{
            if(conn!=null)
               conn.closeSession();
        }
        return result;
    }
    
    /**
     * ȡ�ö���û������õı���Ȩ��(�����м���ʵ�ʻ�������ı���Ȩ��) 
     * @param powType Ȩ������
     * @param userGrpIds String �û���ids
     * @return �û������ù��ı���Ȩ����Ϣ
     */
    public static List getUserGrpRepPopedomFromAFBytype(String userGrpIds,Integer powType,String templatetype){
        List result = null;
        DBConn conn = null;
        Session session = null;
        try{
            if(userGrpIds!=null && !userGrpIds.equals("") && powType != null){
                conn = new DBConn();
                session = conn.openSession();
                
                String hql = "select distinct mcr.id.templateId,mcr.templateName from AfTemplate mcr where( mcr.id.templateId in" +
   			 				 "(select distinct mpbl.comp_id.childRepId from MPurBanklevel mpbl where mpbl.comp_id.MUserGrp.userGrpId in(" + userGrpIds 
   			 				 + ") and mpbl.comp_id.powType=" + powType + ") or mcr.id.templateId in (select distinct mpo.comp_id.childRepId " 
   			 				 + "from MPurOrg mpo where mpo.comp_id.MUserGrp.userGrpId in(" + userGrpIds + ") and mpo.comp_id.powType= "+ powType 
   			 				 + ")) and mcr.templateType="+templatetype+"  and mcr.usingFlag=1 order by mcr.id.templateId";
                
                Query query = session.createQuery(hql);
                
                List list = query.list();
                if(list!=null && list.size()!=0){
                    result = new ArrayList();
                    for(Iterator it = list.iterator(); it.hasNext();){
                    	Object[] item = (Object[]) it.next();
                    	String templateId = item[0] != null ? (String) item[0] : "";
    					String templateName = item[1] != null ? (String) item[1] : "";
                        result.add(new LabelValueBean(templateName,templateId));           
                    }
                }
            }
        }catch(Exception e){
            result = null;
            log.printStackTrace(e);
        }finally{
            if(conn!=null)
               conn.closeSession();
        }
        return result;
    }
    
    /**
     * ͨ���û����ִ�ȡ�ø��û������õ��м������뱨��Ȩ��
     * @param userGrpIds �û���id�ִ�
     * @param powType  Ȩ������
     * @return �û��鴮���ù����м��뱨��Ȩ��
     */
    public static List getMPurBankLevelPopedom(String userGrpIds,Integer powType){
        List result = null;
        DBConn conn = null;
        Session session = null;
        try{
            if(userGrpIds!=null && !userGrpIds.equals("") && powType != null){
                conn = new DBConn();
                session = conn.openSession();
                
                String hql = "select distinct mpbl.comp_id.childRepId,mpbl.comp_id.MBankLevel.bankLevelId,mpbl.comp_id.MBankLevel.bankLevelName from MPurBanklevel " +
                		"mpbl where mpbl.comp_id.MUserGrp.userGrpId in (" + userGrpIds + ") and mpbl.comp_id.powType=" + powType;
                                
                Query query = session.createQuery(hql);
                
                List list = query.list();
                if(list!=null && list.size()!=0){
                    result = new ArrayList();
                    for(Iterator it = list.iterator(); it.hasNext();){
                    	Object[] item = (Object[]) it.next();
                    	MPurBanklevelForm form = new MPurBanklevelForm();
                    	form.setChildRepId(item[0] != null ? item[0].toString() : "");
    					form.setBankLevelId(item[1] != null ? item[1].toString() : "");  
    					form.setBankLevelName(item[2] != null ? item[2].toString() : "");
                        result.add(form);           
                    }       
                }
            }
        }catch(Exception e){
            result = null;
            log.printStackTrace(e);
        }finally{
            if(conn!=null)
               conn.closeSession();
        }
        return result;
    }
    
    /**
     * ͨ���û���idȡ�ø��û����Ѿ����ù��ı���Ȩ����Ϣ(�����м���ʵ�ʻ�������ı���Ȩ��) 
     * @param powType Ȩ������
     * @param userGrpId Long �û���id
     * @return ���û������ù��ı���Ȩ����Ϣ
     */
    public static List getUserGrpRepPopedom(Long userGrpId,Integer powType){
        List result = null;
        DBConn conn = null;
        Session session = null;
        try{
            if(userGrpId!=null && powType != null){
                conn = new DBConn();
                session = conn.openSession();  
                
                String hql = "select distinct mcr.comp_id.childRepId,mcr.reportName from MChildReport mcr where mcr.comp_id.childRepId in" +
	 				 "(select distinct mpbl.comp_id.childRepId from MPurBanklevel mpbl where mpbl.comp_id.MUserGrp.userGrpId=" + userGrpId 
	 				 + " and mpbl.comp_id.powType=" + powType + ") or mcr.comp_id.childRepId in (select distinct mpo.comp_id.childRepId " 
	 				 + "from MPurOrg mpo where mpo.comp_id.MUserGrp.userGrpId=" + userGrpId + " and mpo.comp_id.powType= "+ powType 
	 				 + ") order by mcr.comp_id.childRepId";
   			   
                Query query = session.createQuery(hql);
                List list = query.list();			   
                if(list!=null && list.size()!=0){			    
                	result = new ArrayList();			       
                	for(Iterator it = list.iterator(); it.hasNext();){			       	
                		Object[] item = (Object[]) it.next();						
                		MChildReportForm form = new MChildReportForm();
						form.setChildRepId(item[0] != null ? (String) item[0] : "");					
						form.setReportName(item[1] != null ? (String) item[1] : "");			           
						result.add(form);           			       
                	} 			   
                }                
            }
        }catch(Exception e){
            result = null;
            log.printStackTrace(e);
        }finally{
            if(conn!=null)
               conn.closeSession();
        }
        return result;
    }    
    
    /**
     * ��ʹ��hibernate ���Ը� 2011-12-28
     * Ӱ�����AfTemplate MPurBanklevel MPurOrg
     * ͨ���û���idȡ�ø��û����Ѿ����ù��ı���Ȩ����Ϣ(�����м���ʵ�ʻ�������ı���Ȩ��) 
     * @param powType Ȩ������
     * @param userGrpId Long �û���id
     * @return ���û������ù��ı���Ȩ����Ϣ
     */
    public static List getUserGrpRepPopedomFromAF(Long userGrpId,Integer powType){
        List result = null;
        DBConn conn = null;
        Session session = null;
        try{
            if(userGrpId!=null && powType != null){
                conn = new DBConn();
                session = conn.openSession();  
                
                String hql = "select distinct mcr.id.templateId,mcr.templateName from AfTemplate mcr where mcr.id.templateId in" +
	 				 "(select distinct mpbl.comp_id.childRepId from MPurBanklevel mpbl where mpbl.comp_id.MUserGrp.userGrpId=" + userGrpId 
	 				 + " and mpbl.comp_id.powType=" + powType + ") or mcr.id.templateId in (select distinct mpo.comp_id.childRepId " 
	 				 + "from MPurOrg mpo where mpo.comp_id.MUserGrp.userGrpId=" + userGrpId + " and mpo.comp_id.powType= "+ powType 
	 				 + ") order by mcr.id.templateId";
   			   
                Query query = session.createQuery(hql);
                List list = query.list();			   
                if(list!=null && list.size()!=0){			    
                	result = new ArrayList();			       
                	for(Iterator it = list.iterator(); it.hasNext();){			       	
                		Object[] item = (Object[]) it.next();						
                		MChildReportForm form = new MChildReportForm();
						form.setChildRepId(item[0] != null ? (String) item[0] : "");					
						form.setReportName(item[1] != null ? (String) item[1] : "");			           
						result.add(form);           			       
                	} 			   
                }                
            }
        }catch(Exception e){
            result = null;
            log.printStackTrace(e);
        }finally{
            if(conn!=null)
               conn.closeSession();
        }
        return result;
    }
    
    /**
     * ���ø��û�����м�Ȩ��
     * @param userGrpId �û���ID
     * @param selectBankLevelIds String ѡ����м�id�ִ���֮���á�,���Ÿ�����
     * @param selectRepIds String ѡ��ı���id�ִ���֮���á�,��������
     * @param powType Ȩ������
     * @return
     */
    public static boolean insertUserGrpPopedom(Long userGrpId,String selectBankLevelIds,String selectRepIds,Integer powType){
    	boolean result = false;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpId != null && powType != null
    				&& selectBankLevelIds != null && !selectBankLevelIds.equals("")
    				&& selectRepIds != null && !selectRepIds.equals("")){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			
    			String bankLevelIds[] = selectBankLevelIds.split(Config.SPLIT_SYMBOL_COMMA);
    			String childRepIds[] = selectRepIds.split(Config.SPLIT_SYMBOL_COMMA);
    			
    			for(int i=0;i<bankLevelIds.length;i++){
    				for(int j=0;j<childRepIds.length;j++){
    					MPurBanklevel mPurBankLevel = new MPurBanklevel(); 
    					
    					MPurBanklevelKey key = new MPurBanklevelKey();
    					key.setChildRepId(childRepIds[j]);
    					key.setPowType(powType);
    					MBankLevel bankLevel = new MBankLevel();
    					bankLevel.setBankLevelId(new Integer(bankLevelIds[i]));
    					MUserGrp userGrp = new MUserGrp();
    					userGrp.setUserGrpId(userGrpId);
    					key.setMBankLevel(bankLevel);
    					key.setMUserGrp(userGrp);
    					
    					mPurBankLevel.setComp_id(key);
    					session.save(mPurBankLevel);
    				}
    			}
    			session.flush();
    			result = true;
    		}
    	}catch(Exception e){
    		result = false;
    		log.printStackTrace(e);
    	}finally{
    		if(conn != null)
    			conn.endTransaction(result);
    	}
    	return result;
    }
    
    /**
     * �鿴���û����Ƿ����ù��м�Ȩ��
     * @param userGrpId �û���ID
     * @param powType  Ȩ������
     * @return �м�Ȩ�޶�������
     */
    public static int getUserGrpBankLevelPopedomCount(Long userGrpId,Integer powType){
    	int result = 0;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpId != null && powType != null){
    			conn = new DBConn();
    			session = conn.openSession();
    			String hql = "select count(*) from MPurBanklevel mpbl where mpbl.comp_id.MUserGrp.userGrpId=" + userGrpId + " and mpbl.comp_id.powType=" + powType;
    			
    			Query query = session.createQuery(hql);
    			List list = query.list();
    			if(list != null && list.size() !=0) result = ((Integer)list.get(0)).intValue();
    			
    		}
    	}catch(Exception ex){
    		result = 0;
    		log.printStackTrace(ex);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return result;
    }
        
    /**
     * ɾ�����û�����ǰ���ù����м�Ȩ��
     * @param userGrpId �û���ID
     * @param powType Ȩ������
     * @return
     */
    public static boolean deleteUserGrpPopedom(Long userGrpId,Integer powType){
    	boolean result = false;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpId != null && powType != null){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			
    			String hql = " from MPurBanklevel mpbl where mpbl.comp_id.MUserGrp.userGrpId=" + userGrpId + " and mpbl.comp_id.powType=" + powType;
    			session.delete(hql);
    			session.flush();
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
     * ��ʹ��Hibernate ���Ը� 2011-12-28
     * Ӱ�����MPurBanklevel
     * ɾ�����û�����ǰ���ù����м�Ȩ��
     * @param userGrpId �û���ID
     * @return
     */
    public static boolean deleteUserGrpPopedom(Long userGrpId){
    	boolean result = false;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpId != null){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			
    			String hql = " from MPurBanklevel mpbl where mpbl.comp_id.MUserGrp.userGrpId=" + userGrpId;
    			session.delete(hql);
    			session.flush();
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
    
//    public static String getOrgPopedomSQL(String userGrpIds){
//    	String lowerOrgIds = "";
//    	DBConn conn = null;
//        Session session = null;
//        try
//        {
//            if(userGrpIds!=null){
//                conn = new DBConn();
//                session = conn.beginTransaction();
//            
//                String hql = "select distinct pv.comp_id.orgId from MPurView pv where pv.comp_id.userGrpId in("+userGrpIds+")";
//                Query query = session.createQuery(hql);
//                List list = query.list();
//                if(list!=null && list.size()!=0){
//                    for(int i=0;i<list.size();i++){
//                    	String orgId = (String)list.get(i);
//                    	lowerOrgIds = lowerOrgIds.equals("") ? orgId : lowerOrgIds + "," + orgId;
//                    }
//                }
//                
//            }
//        }
//        catch(Exception e){
//        	lowerOrgIds = "";
//            log.printStackTrace(e);
//        }
//        finally{
//            if(conn!=null)
//               conn.closeSession();
//        }
//        return lowerOrgIds;
//    }  
}
