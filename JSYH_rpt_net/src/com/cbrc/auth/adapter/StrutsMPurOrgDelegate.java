
package com.cbrc.auth.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.auth.form.MPurOrgForm;
import com.cbrc.auth.hibernate.MPurOrg;
import com.cbrc.auth.hibernate.MPurOrgKey;
import com.cbrc.auth.hibernate.MUserGrp;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfOrg;
import com.fitech.net.form.OrgNetForm;
import com.fitech.net.hibernate.OrgNet;

public class StrutsMPurOrgDelegate {
    private static FitechException log = new FitechException(StrutsMPurOrgDelegate.class);
    
    /**
     * 根据用户组ID取得该用户组已经设置过的实际机构
     * @param userGrpId 用户组ID
     * @param powType 权限类型
     * @return
     */
    public static List getUserGrpOrgPopedom(Long userGrpId,Integer powType){
    	List result = null;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpId != null){
    			conn = new DBConn();
    			session = conn.openSession();
    			String hql = "select distinct on.orgId,on.orgName from OrgNet on where on.orgId in (select distinct mpo.comp_id.org.orgId from MPurOrg mpo " +
    					"where mpo.comp_id.MUserGrp.userGrpId=" + userGrpId + " and mpo.comp_id.powType=" + powType + ")";
    			
    			Query query = session.createQuery(hql);
    			List list = query.list();
    			if(list != null && list.size() > 0){
    				result = new ArrayList();
    				for(int i=0;i<list.size();i++){
    					Object[] object = (Object[])list.get(i);
        				OrgNetForm form = new OrgNetForm();
        				form.setOrg_id(object[0].toString());
        				form.setOrg_name(object[1].toString());
        				result.add(form);
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
    
    public static List getMPurOrgPopedom(String userGrpIds,Integer powType){
        List result = null;
        DBConn conn = null;
        Session session = null;
        try{
            if(userGrpIds!=null && !userGrpIds.equals("") && powType != null){
                conn = new DBConn();
                session = conn.openSession();                               
                              
                String hql = "select distinct mpo.comp_id.childRepId,mpo.comp_id.org.orgId from MPurOrg mpo where mpo.comp_id.MUserGrp.userGrpId in(" 
                	+ userGrpIds + ") and mpo.comp_id.powType=" + powType;
                Query query = session.createQuery(hql);
                
                List list = query.list();
                if(list!=null && list.size()!=0){
                    result = new ArrayList();
                    for(Iterator it = list.iterator(); it.hasNext();){
                    	Object[] item = (Object[]) it.next();
                    	MPurOrgForm form = new MPurOrgForm();
                    	form.setChildRepId(item[0] != null ? item[0].toString() : "");
    					form.setOrgId(item[1] != null ? item[1].toString() : "");    					
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
     * 查看该用户组是否设置过实际机构权限
     * @param userGrpId  用户组
     * @param powType
     * @return
     */
    public static int getUserGrpOrgPopedomCount(Long userGrpId,Integer powType){
    	int result = 0;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpId != null){
    			conn = new DBConn();
    			session = conn.openSession();
    			String hql = "select count(*) from MPurOrg mpo where mpo.comp_id.MUserGrp.userGrpId=" + userGrpId + " and mpo.comp_id.powType=" + powType;
    			
    			Query query = session.createQuery(hql);
    			List list = query.list();
    			if(list != null && list.size() > 0)
    				result = ((Integer)list.get(0)).intValue();
    		}
    	}catch(Exception ex){
    		result = 0 ;
    		log.printStackTrace(ex);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return result;
    }
    
    /**
     * 删除该用户组已设定的实际机构权限
     * @param userGrpId 用户组ID
     * @param powType 权限类型
     * @return boolean
     */
    public static boolean deleteUserGrpOrgPopedom(Long userGrpId,Integer powType){
    	boolean result = false;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpId != null && powType != null){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			
    			String hql = "from MPurOrg mpo where mpo.comp_id.MUserGrp.userGrpId=" + userGrpId + " and mpo.comp_id.powType=" + powType;
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
     * 已使用hibernate 卞以刚 2011-12-28
     * 影响对象：MPurOrg
     * 删除该用户组已设定的实际机构权限
     * @param userGrpId 用户组ID
     * @return boolean
     */
    public static boolean deleteUserGrpOrgPopedom(Long userGrpId){
    	boolean result = false;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpId != null){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			
    			String hql = "from MPurOrg mpo where mpo.comp_id.MUserGrp.userGrpId=" + userGrpId;
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
     * 设置该用户组的实际机构权限
     * @param userGrpId Long 用户组ID
     * @param selectOrgIds String 选择的实际机构id字串（之间用“”隔开）
     * @param selectRepIds String 选择的报表id字串（之间用“,”隔开）
     * @param powType Integer 权限类型
     * @return boolean
     */
    public static boolean insertUserGrpPopedom(Long userGrpId,String selectOrgIds,String selectRepIds,Integer powType){
    	boolean result = false;
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(userGrpId != null && powType != null
    				&& selectOrgIds != null && !selectOrgIds.equals("")
    				&& selectRepIds != null && !selectRepIds.equals("")){
    			
    			String orgIds[] = selectOrgIds.split(Config.SPLIT_SYMBOL_COMMA);
    			String childRepIds[] = selectRepIds.split(Config.SPLIT_SYMBOL_COMMA);
    			//查询该机构有无该报表的报送权限
    			Map orgtem = getOrgHaveReport(orgIds);
    			//获得需设定的报送范围
    			//Map orgtem = getOrgNeedReport(orgIds,childRepIds);
    			
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			for(int i=0;i<orgIds.length;i++){
    				Map orgtemap = (Map) orgtem.get(orgIds[i]);
    				for(int j=0;j<childRepIds.length;j++){
    					if(!orgtemap.containsKey(childRepIds[j])){
    						continue;
    					}
    					MPurOrg mPurOrg = new MPurOrg(); 
    					
    					MPurOrgKey key = new MPurOrgKey();
    					key.setChildRepId(childRepIds[j]);
    					key.setPowType(powType);
//    					OrgNet org = new OrgNet();
    					AfOrg org = new AfOrg();
    					org.setOrgId(orgIds[i]);
    					MUserGrp userGrp = new MUserGrp();
    					userGrp.setUserGrpId(userGrpId);
    					key.setOrg(org);
    					key.setMUserGrp(userGrp);
    					
    					mPurOrg.setComp_id(key);
    					session.save(mPurOrg);
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
     * 查询该机构已经设定的报送范围
     * @author
     * @param 机构ID
     * @return
     */
	private static Map getOrgHaveReport(String orgIds[]){
		if(orgIds == null || orgIds.length==0) return null;
	    
		Map orgap =new HashMap();   
		DBConn conn = null;	    
		Session session = null;
	    
		try{	    
			conn = new DBConn();	        
			session = conn.openSession();            
	        for(String orgId :orgIds){
	        	Map temMap =new HashMap();   
	        	String sql = "select distinct mrr.comp_id.childRepId from MRepRange mrr where mrr.comp_id.orgId='" + orgId + "' order by mrr.comp_id.childRepId";
				Query query = session.createQuery(sql);	        
				List list = query.list();	        
				if (list != null && list.size() > 0){							
		            for(Iterator it = list.iterator();it.hasNext();){	            	
		            	String childRepId = (String) it.next();
									
						if(!StringUtil.isEmpty(childRepId) && !temMap.containsKey(childRepId)){
							temMap.put(childRepId, childRepId);
						}
		            }	            
				}
				sql = "select distinct mrr.id.templateId " +
						"from AfTemplateOrgRelation mrr where mrr.id.orgId='" + orgId + "' order by mrr.id.templateId";
				query = session.createQuery(sql);	        
				list = query.list();	        
				if (list != null && list.size() > 0){
		            for(Iterator it = list.iterator();it.hasNext();){	            	
		            	String childRepId = (String) it.next();
				
						if(!StringUtil.isEmpty(childRepId) && !temMap.containsKey(childRepId)){
							temMap.put(childRepId, childRepId);
						}
					}
				}
				orgap.put(orgId, temMap);
	        }
			
		}catch(Exception e){	    
			orgap = null;	        
			log.printStackTrace(e);	        
		}finally{ 
			if(conn!=null)	        
				conn.closeSession();	    
		}	       
		return orgap;	    
	}
	
	/**
     * 获得该机构需设定的报送范围
     * @author
     * @param 机构ID
     * @return
     */
	private static Map getOrgNeedReport(String orgIds[],String childRepIds[]){
		
		if(orgIds == null || orgIds.length==0) return null;
		if(childRepIds == null || childRepIds.length==0) return null;
	    
		
		Map orgap =new HashMap();   

        for(String orgId :orgIds){
        	
        	Map temMap =new HashMap();   
  
            for(String childRepId : childRepIds){	            	
		
				if(!StringUtil.isEmpty(childRepId) && !temMap.containsKey(childRepId)){
					temMap.put(childRepId, childRepId);
				}
				
				orgap.put(orgId, temMap);
            }
			
		}
		return orgap;	    
	}
}
