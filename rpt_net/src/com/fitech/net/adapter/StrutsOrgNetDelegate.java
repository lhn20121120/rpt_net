
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
 *@StrutsOrgNetDelegate  机构Delegate
 * 
 * @author jcm
 */
public class StrutsOrgNetDelegate {
	//Catch到本类的抛出的所有异常
	private static FitechException log=new FitechException(StrutsOrgNetDelegate.class);

	/**
	 * 新增地区
	 * @param orgNetForm OrgNetForm
     * @exception Exception，捕捉异常处理 
     * @return int result  0--操作失败  1--操作成功  2--要添加的机构代码已经存在
     */
	public static int create (OrgNetForm orgNetForm) throws Exception {	   
		int result = 0;				//置result标记	   
		boolean bool = false;

		OrgNet orgNetPersistence = new OrgNet();
	   	   	   
		if (orgNetForm == null || orgNetForm.getOrg_name().equals("")) 		
			return result;
	   	   
		//连接对象的初始化	   
		DBConn conn=null;	   
		//会话对象的初始化	   
		Session session=null;	   
		try{		
			TranslatorUtil.copyVoToPersistence(orgNetPersistence,orgNetForm);		   
			//实例化连接对象		   
			conn =new DBConn();		   
			//会话对象为连接对象的事务属性		   
			session=conn.beginTransaction();
		   		   
			String hql = "from OrgNet ont where ont.orgId='" + orgNetForm.getOrg_id() + "'";		   
			List list = session.find(hql);		   
		  
			if(list != null && list.size() > 0){			
				result = 2;        //要增加的机构代码已经存在			   
				bool = true;			   
				return result;		   
			}
				   
			//会话对象保存持久层对象		  
			session.save(orgNetPersistence);		   
			session.flush();		   
//			//创建管理员组		   
//			MUserGrp mUserGrp = new MUserGrp();
//			OrgNet orgNet = new OrgNet();
//			orgNet.setOrgId(orgNetPersistence.getOrgId());
//			mUserGrp.setSetOrg(orgNet);		   
//			mUserGrp.setUserGrpNm(orgNetPersistence.getOrgName()+"系统管理组");		   
//			StrutsMUserGrpDelegate.create(mUserGrp);
//		   		   
//			//创建管理员帐号		  
//			OperatorForm operatorForm = new OperatorForm();		   
//			operatorForm.setUserName(orgNetPersistence.getOrgId());			
//			operatorForm.setTitle(orgNetPersistence.getOrgName()+"系统管理员");			
//			operatorForm.setFirstName("系统管理员");		
//			operatorForm.setPassword("123456");			
//			operatorForm.setOrgId(orgNetPersistence.getOrgId());			
//			Long user_id = StrutsOperatorDelegate.create(operatorForm,new Integer(0));
//			
//			//创建组与用户关系
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
		   		   
//			//增加系统管理角色		   
//			Role role = new Role();		   
//			role.setRoleName(orgNetPersistence.getOrgName()+"系统管理");		   
//			role.setSetOrgId(orgNetPersistence.getOrgId());		   
//			StrutsRoleDelegate.create(role);
//		   		   
//			//创建用户与角色的关系		   
//			UserRoleForm roleForm = new UserRoleForm();		   
//			roleForm.setUserId(user_id);		   
//			roleForm.setRoleId(role.getRoleId());		   
//			roleForm.setSelectedRoleIds(role.getRoleId().toString());		   
//			StrutsUserRoleDelegate.insert(roleForm);
//		   		   
//			//创建该角色可以访问的菜单
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
			result=1;       //增加成功		   
			bool = true;
		}catch(HibernateException e){		
			//持久层的异常被捕捉		   
			result = 0;		   
			bool = false;		   
			log.printStackTrace(e);	   
		}finally{		
			//如果连接状态有,则断开,结束事务,返回		   
			if(conn!=null) conn.endTransaction(bool);	   
		}	   
		return result;
	}    
   
	/**
	 * 新增机构
	 * @param orgNetForm OrgNetForm
     * @exception Exception，捕捉异常处理 
     * @return int result  0--操作失败  1--操作成功  2--要添加的机构代码已经存在
     */
	public static int create (OrgNetForm orgNetForm,String orgId) throws Exception {
		int result = 0;				//置result标记	   
		boolean bool = false;	   
		OrgNet orgNetPersistence = new OrgNet();

		if (orgNetForm == null || orgNetForm.getOrg_name().equals("")) 		
			return result;	   
	   
		//连接对象的初始化	   
		DBConn conn=null;	   
		//会话对象的初始化	   
		Session session=null;
	   
		try{		
			//实例化连接对象		   
			conn =new DBConn();		   
			//会话对象为连接对象的事务属性		   
			session=conn.beginTransaction();		   
		   
			String hql = "from OrgNet ont where ont.orgId='" + orgNetForm.getOrg_id() + "'";		   
			List list = session.find(hql);

			if(list != null && list.size() > 0){			
				result = 2;        //要增加的机构代码已经存在			  
				bool = true;			   
				return result;		   
			}
			
			//会话对象保存持久层对象		   
			TranslatorUtil.copyVoToPersistence(orgNetPersistence,orgNetForm);		   
			session.save(orgNetPersistence);		   
			session.flush();
			
			result=1;       //增加成功		   
			bool = true;		   	   
		}catch(HibernateException e){		
			//持久层的异常被捕捉		   
			result = 0;		   
			bool = false;		   
			log.printStackTrace(e);	   
		}finally{		
			//如果连接状态有,则断开,结束事务,返回		   
			if(conn!=null) conn.endTransaction(bool);	   
		}	   
		return result;    
	}

	/**
	 * 取得按条件查询到的记录条数
	 * @return int 查询到的记录条数	
	 * @param   orgNetForm   包含查询的条件信息（机构ID，机构名称）
	 */
	public static int getRecordCount(OrgNetForm orgNetForm){
	   
		int count=0;

		//连接对象和会话对象初始化	   
		DBConn conn=null;		   
		Session session=null;

		//查询条件HQL的生成		
		StringBuffer hql = new StringBuffer("select count(*) from OrgNet ont");								
		StringBuffer where = new StringBuffer("");

		if (orgNetForm != null) {		
			// 查找条件的判断,查找名称不可为空			
			if (orgNetForm.getRegion_name() != null && !orgNetForm.getRegion_name().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");		
		}

		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString());	    	
			//conn对象的实例化		  	    	
			conn=new DBConn();	    	
			//打开连接开始会话	    	
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn!=null) conn.closeSession();	      
		}	    
		return count;   
	}


	/**
	 * 取得按条件查询到的记录条数
	 * @return int 查询到的记录条数	
	 * @param   orgNetForm   包含查询的条件信息（机构ID，机构名称）
	 */
	public static int getRecordCount(OrgNetForm orgNetForm,Operator operator){	   
		int count=0;
	
		//连接对象和会话对象初始化		  
		DBConn conn=null;
		Session session=null;

		//查询条件HQL的生成		
		StringBuffer hql = new StringBuffer("select count(*) from OrgNet ont");								
		StringBuffer where = new StringBuffer("");

		if (orgNetForm != null) {		
			// 查找条件的判断,查找名称不可为空			
			if (orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");		  
		}
		where.append((where.toString().equals("") ? "" : " and ") + "ont.setOrgId = '" + operator.getOrgId() + "'");
		
		try {		
			hql.append((where.toString().equals("")?"":" where ") + where.toString());			
			//conn对象的实例化			
			conn=new DBConn();			
			//打开连接开始会话			
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
			//如果连接存在，则断开，结束会话，返回			
			if(conn!=null) conn.closeSession();		  
		}		   
		return count;
	}
	
	/**
	 * 机构查询
	 * @param orgNetForm OrgNetForm 查询表单对象
	 * @param offset
	 * @param limit
	 * @return List 如果查找到记录，返回List记录集；否则，返回null
	 */
	public static List select(OrgNetForm orgNetForm,int offset,int limit){
	   	   
		//List集合的定义 	   
		List refVals = null;		

		//连接对象和会话对象初始化	   
		DBConn conn = null;					   
		Session session = null;

		//查询条件HQL的生成	   
		StringBuffer hql = new StringBuffer("from OrgNet ont");								   
		StringBuffer where = new StringBuffer("");
		
		if (orgNetForm != null) {		
			// 查找条件的判断,查找名称不可为空			
			if (orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");		
		}
		
		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString());	    	
			//conn对象的实例化		  	    	
			conn = new DBConn();	    	
			//打开连接开始会话	    	
			session = conn.openSession();			
			Query query = session.createQuery(hql.toString());			
			query.setFirstResult(offset).setMaxResults(limit);			
			List list = query.list();

			if (list != null){	    	
				refVals = new ArrayList();	    		
				//循环读取数据库符合条件记录			      
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	      
		}	    
		return refVals;   
	}
	
	/**
	 * 机构查询
	 * @param orgNetForm OrgNetForm 查询表单对象
	 * @param offset
	 * @param limit
	 * @param operator
	 * @return List 如果查找到记录，返回List记录集；否则，返回null
	 */   
	public static List select(OrgNetForm orgNetForm,int offset,int limit,Operator operator){
		//List集合的定义 	   
		List refVals = null;
		
		//连接对象和会话对象初始化	   
		DBConn conn = null;	   
		Session session = null;
	   	   
		//查询条件HQL的生成	   
		StringBuffer hql = new StringBuffer("from OrgNet ont");								   
		StringBuffer where = new StringBuffer("");		
		
		if (orgNetForm != null) {		
			// 查找条件的判断,查找名称不可为空			
			if (orgNetForm.getOrg_name() != null && !orgNetForm.getOrg_name().equals(""))			
				where.append((where.toString().equals("")?"":" and ") + "ont.orgName like '%" + orgNetForm.getOrg_name() + "%'");		
		}				
		where.append((where.toString().equals("") ? "" : " and ") + "ont.setOrgId = '" + operator.getOrgId() + "'");

		try {	    
			hql.append((where.toString().equals("")?"":" where ") + where.toString());	    	
			//conn对象的实例化		    	
			conn = new DBConn();	    	
			//打开连接开始会话	    	
			session = conn.openSession();			
			Query query = session.createQuery(hql.toString());			
			query.setFirstResult(offset).setMaxResults(limit);			
			List list = query.list();

			if (list != null){	    	
				refVals = new ArrayList();	    		
				//循环读取数据库符合条件记录			    
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return refVals;   
	}
   	/**
	 *<p>描述:查询子机构数量</p>
	 *<p>参数:</p>
	 *<p>日期：2008-1-3</p>
	 *<p>作者：曹发根</p>
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
			
			//加入机构名称查询条件		
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return count;   
	}
   	/**
	 *<p>描述:查询子机构数量</p>
	 *<p>参数:</p>
	 *<p>日期：2008-1-3</p>
	 *<p>作者：曹发根</p>
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return count;   
	}
	/**
	 *<p>描述:查询子机构</p>
	 *<p>参数:</p>
	 *<p>日期：2008-1-3</p>
	 *<p>作者：曹发根</p>
	 */ 
	public static List selectSubOrg(OrgNetForm orgNetForm,int offset,int limit){
		if(orgNetForm == null) return null;
		
		List refVals = null;
		DBConn conn = null;	   
		Session session = null;
		
		try {
			//查询条件HQL的生成
			StringBuffer hql = new StringBuffer("from OrgNet ont where ont.preOrgId='" 
					+ orgNetForm.getPre_org_id() + "'");								   
			StringBuffer where = new StringBuffer();		
			
			//加入机构名称查询条件		
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return refVals;   
	}
	/**
	 *<p>描述:查询子机构</p>
	 *<p>参数:</p>
	 *<p>日期：2008-1-3</p>
	 *<p>作者：曹发根</p>
	 */ 
	public static List selectSubOrgByName(OrgNetForm orgNetForm,int offset,int limit){
		if(orgNetForm == null) return null;
		
		List refVals = null;
		DBConn conn = null;	   
		Session session = null;
		
		try {
			//查询条件HQL的生成
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return refVals;   
	}
	/**
	 * 获得下级机构列表信息
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
			//查询条件HQL的生成
			StringBuffer hql = new StringBuffer("from OrgNet ont where ont.orgId in ("
					+ operator.getChildRepSearchPopedom().replace("orgRepId", "orgId") +")");
			StringBuffer where = new StringBuffer();		
			
			//加入机构名称查询条件		
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	     
		}	      
		return refVals;   
	}
	
	/**
	 * 更新OrgNetForm对象
	 *
	 * @param   orgNetForm   OrgNetForm 存放数据的对象
	 * @exception   Exception  如果OrgNetForm更新失败,则捕捉抛出异常
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
	 * 编辑操作
	 * 
	 * @param orgNetForm  The OrgNetForm  保持数据的传递
	 * @exception Exception 如果 OrgNetForm 对象丢失则抛出异常.
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
	 * 删除操作
	 *
	 * @param   orgNetForm   OrgNetForm 查询表单的对象
	 * @return   boolean  如果删除成功则返回true,否则false
	 */  
	public static boolean remove (OrgNetForm orgNetForm) throws Exception {
		//置标志result
		boolean result=false;
	   
		//连接和会话对象的初始化	   
		DBConn conn=null;	   
		Session session=null;

		//orgNetForm是否为空,返回result	   
		if (orgNetForm == null || orgNetForm.getOrg_id() == null) 		
			return result;

		try{	    
			//	连接对象和会话对象初始化		   
			conn=new DBConn();		   
			session=conn.beginTransaction();

			OrgNet orgNet=(OrgNet)session.load(OrgNet.class,orgNetForm.getOrg_id());		   
			//会话对象删除持久层对象		   
			session.delete(orgNet);		   
			session.flush();

			//删除成功，置为true		   
			result=true;		  
		}catch(HibernateException he){		 
			//捕捉本类的异常,抛出		   
			log.printStackTrace(he);	   
		}finally{		
			//如果由连接则断开连接，结束会话，返回		   
			if (conn!=null) conn.endTransaction(result);	   
		}	   
		return result;   
	}

	/**
	 * 查询一条记录
	 * @param orgNetForm OrgNetForm实例化对象
	 * @return OrgNetForm 包含一条记录
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
				//如果连接存在，则断开，结束会话，返回			   
				if(conn != null) conn.closeSession();		   
			}	   
		}	   
		return orgNetForm;   
	}

	/**
	 * 查询一条记录
	 * @param orgNetForm OrgNetForm实例化对象
     * @return OrgNetForm 包含一条记录
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetForm;
	}

	/**
	 * 查询所有记录
	 * @return List 查询到的记录条数	
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return refVals;
	}
	
	/**
	 * 已使用hibernate	卞以刚 2011-12-21
	 * 影响对象：OrgNet
	 * 根据机构id查询机构信息
	 * @param orgId 机构id
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) 
				conn.closeSession();
		}
		return orgNetResult;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：OrgNet
	 * 根据机构id，查询该机构下所有子机构
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return allLowerOrgIds;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-11-21
	 * 影响对象：OrgNet
	 * 根据机构id查询该机构的一级子机构
	 * @param orgId 机构id
	 * @return String 返回下级机构id子串
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return lowerOrgIds;
	}
	
	/**
	 * 取得当前机构的下级机构
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return lowerOrgIds;
	}
	
	/**
	 * 查找有异常变化的下级机构
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
			//如果连接存在，则断开，结束会话，返回
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	/**
	 * 查找在填报范围内的下级机构
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * 根据机构ID查询所有子机构
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * 获取分支机构列表
	 * 
	 * @param preOrgId 上级机构ID
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * 根据机构ID查询所有子机构和本机构
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 *   把输入的list里的 没有父机构的节点去掉
	 * @param 
	 * @param list里是String
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
	 *   把输入的list里的 没有父机构的节点去掉
	 * @param 
	 * @param list里放的是OrgNet
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
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象： OrgNet
	 * 查询子机构信息
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
							orgNet.setPreOrgId("true");//姑且用pre org标志是否选中							
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
			//如果连接存在，则断开，结束会话，返回
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
							orgNet.setPreOrgId("true");//姑且用pre org标志是否选中							
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
	
	/**
	 * 查询子机构数量
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return count;
	}
	
	/**
	 * 获得当前机构的上一级机构id
	 * @param orgId
	 * @return
	 */
	public static String getUpOrgId(String orgId){
		OrgNet org = selectOne(orgId);
		if(org==null) return "";
		return org.getPreOrgId();
	}
	
	/**
	 * 查询所有最低一级机构
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return mostLowerOrgIds;
	}
	
	/**
	 * 查询非最下级机构
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return notLowerOrgs;
	}
	
	/**
	 * 判断机构是否存在  
	 * @param  机构ID
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
	 * 判断是否是最高级机构
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
	 * 根据机构类型，查询对应的机构信息
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
	 * 根据地区，查询相应的机构信息
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
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：OrgNet 
	 * 根据机构ID,取得机构名称;
	 * @param orgId
	 * return 机构名称
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
			//如果连接存在，则断开，结束会话，返回
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return strOrgId;
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 机构查询
	 * 
	 * @param orgNetForm OrgNetForm 查询表单对象
	 * @return List 如果查找到记录，返回List记录集；否则，返回null
	 */	   
	public static List selectChildOrg(String orgId){		   
		if(orgId==null) return null;	
		
		//List集合的定义 		
		List refVals  = new ArrayList();  
		
		//连接对象和会话对象初始化		
		DBConn conn = null;						
		Session session = null;	   
		
		//查询条件HQL的生成		  
		String hql = "from OrgNet ont where ont.preOrgId ='" + orgId.trim()+"'"; 				  
	    
		try {    	 	    
			//conn对象的实例化		  	    	
			conn = new DBConn();	    	
			//打开连接开始会话	    	
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	      
		}	    
		return refVals;	   
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 机构信息查询
	 * 
	 * @param orgNetForm OrgNetForm 查询表单对象
	 * @return  OrgNetForm 如果查找到记录，返回OrgNetForm；否则，返回null
	 */	   
	public static OrgNetForm selectOrgInfo(String orgId){

		if(orgId==null) return null;		
		//连接对象和会话对象初始化
		
		DBConn conn = null;						
		Session session = null;
		
		OrgNetForm orgNetFormTemp = new OrgNetForm();		
		//	 查询条件HQL的生成		
		String hql = new String("from OrgNet ont where ont.orgId = '" + orgId + "'");
		try {
			//conn对象的实例化		    	
			conn = new DBConn();	    	
			//打开连接开始会话	    	
			session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());			
			List list = query.list();
	    	
			if (list != null){	    		  
				//循环读取数据库符合条件记录
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	      
		}	    
		return orgNetFormTemp;	   
	}
	
	/**
	 * 删除机构相关信息操作
	 *
	 * @param   orgNetForm   OrgNetForm 查询表单的对象
	 * @return   boolean  如果删除成功则返回true,否则false
	 */  
	public static boolean removeOrgXGInfo(OrgNetForm orgNetForm) throws Exception {

		if (orgNetForm == null || orgNetForm.getOrg_id() == null)   return false;		
		boolean result=false;
		
		//连接和会话对象的初始化		
		DBConn conn=null;		
		Session session=null;
		
		try{		
			//连接对象和会话对象初始化			
			conn=new DBConn();			
			session=conn.beginTransaction();
			
			//删除报送范围			
			boolean delBSFW = deleteBSFW(orgNetForm.getOrg_id(),session);			   			
			//删除异常变化			
			//	boolean delYCBH = deleteYCBH(orgNetForm.getOrg_id(),session);	
			boolean delYCBH=true;
			//删除机构
	        boolean  delOrg=removeOrg(orgNetForm,session);
		      
	        if(delBSFW && delYCBH && delOrg){		    
	        	result=true;		      
	        }		   
		}catch(HibernateException he){		
			//捕捉本类的异常,抛出			
			log.printStackTrace(he);			
			result=false;		   
		}finally{		
			//如果由连接则断开连接，结束会话，返回			
			if (conn!=null) conn.endTransaction(result);		
		}
		return result; 
	}

	/**
	 * 删除异常变化
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
						/**删除异常变化*/	
						session.delete(abnormityChange);			            
					}	        
				}
	         
				Query queryQD = session.createQuery("from ColAbnormityChange abc where abc.comp_id.orgId='"+orgId+"'");	         
				List listQD = queryQD.list();	         
				if(listQD!=null && listQD.size()!=0){	            
					for(Iterator it = listQD.iterator(); it.hasNext();){	             	
						ColAbnormityChange colAbnormityChange = (ColAbnormityChange)it.next();			            
						/**删除异常变化*/			        
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
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 删除报送范围
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
			            /**删除报送范围*/			       			             
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
	 * 删除操作
	 * @param   orgNetForm   OrgNetForm 查询表单的对象
	 * @return   boolean  如果删除成功则返回true,否则false
	 */  
	public static boolean removeOrg (OrgNetForm orgNetForm,Session session) throws Exception {	    
		//置标志result		
		boolean result=false;	 		
		
		if (orgNetForm == null || orgNetForm.getOrg_id() == null) return result;
		
		try{
			OrgNet orgNet=(OrgNet)session.load(OrgNet.class,orgNetForm.getOrg_id());
			
			//会话对象删除持久层对象
			session.delete(orgNet);			
			session.flush();			
			
			result=true;			
		}catch(HibernateException he){		
			//捕捉本类的异常,抛出			
			log.printStackTrace(he);		   
		}		
		return result;
	}
	/**
	 * 删除机构对应的报表
	 * @param   orgNetForm   OrgNetForm 查询表单的对象
	 * @return   boolean  如果删除成功则返回true,否则false
	 */  
	public static boolean removeOrgRep (OrgNetForm orgNetForm,Session session) throws Exception {	    
		//置标志result		
		boolean result=false;	 		
		
		if (orgNetForm == null || orgNetForm.getOrg_id() == null) return result;
		
		try{
			OrgNet orgNet=(OrgNet)session.load(OrgNet.class,orgNetForm.getOrg_id());
			//int updatedEntities = session.createQuery( "" );
			//会话对象删除持久层对象
			session.delete(orgNet);			
			session.flush();			
			
			result=true;			
		}catch(HibernateException he){		
			//捕捉本类的异常,抛出			
			log.printStackTrace(he);		   
		}		
		return result;
	}
	/**
	 * 根据当前机构，查询子机构
	 * @param orgId
	 * @return List 如果没有纪录，返回null
	 */
	public static List selectAllOrg(String orgId){

		List refVals = null;
		//连接对象和会话对象初始化		
		DBConn conn = null;						
		Session session = null;
				
		if(orgId == null) return null;
		//查询条件HQL的生成		
		String hql = "from OrgNet ont where  ont.preOrgId = '" + orgId.trim() + "' order by ont.orgName";						    
		try {	    			  	    
			conn = new DBConn();	    			
			session = conn.openSession();
			
			Query query = session.createQuery(hql.toString());			
			List list = query.list();

			if (list != null && list.size() > 0){	    	
				refVals = new ArrayList();	    		
				//循环读取数据库符合条件记录			    
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
			//如果连接存在，则断开，结束会话，返回	    	
			if(conn != null) conn.closeSession();	    
		}
		return refVals;	   
	}
	
	/**
	 * 最高机构
	 * @param 机构ID 查询表单对象
	 * @return  机构ID
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
	 * 根据机构ID取得相应机构信息
	 * @param orgIds
	 * @return List 机构列表
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return result;
	}
	
	/**
	 * 报表审核中根据机构名称Orgname查找OrgId的集合
	 * 
	 * @param orgName
	 * 
	 * @return list　机构id的集合
	 * 
	 */
	public static List selectOrgNames (String orgName){
		
		List refVals=null;		
		//连接对象和会话对象初始化		
		DBConn conn=null;		
		Session session=null;
		
		if (orgName!=null && !orgName.toString().equals("")){	           
			StringBuffer hql = new StringBuffer("from OrgNet mo where 1=1"); 	        
			StringBuffer where = new StringBuffer("");	        
			where.append(" and mo.orgName like '%" +orgName+"%'");
	        
			try{	           
				hql.append(where.toString());
				//conn对象的实例化 	            
				conn=new com.cbrc.smis.dao.DBConn();	            
				//打开连接开始会话	            
				session=conn.openSession();	            
				Query query=session.createQuery(hql.toString());

				List list=query.list();
				if (list!=null){	            
					refVals = new ArrayList();	                
					//循环读取数据库符合条件记录	                
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
				//如果连接存在，则断开，结束会话，返回	            
				if(conn!=null) conn.closeSession();	         
			}		   
		}	 		
		return refVals;	    
	}
	   
	/**
	 * 根据StrutsMOrgDelegate中的selectOrgNames（）找到的机构名集合转换为字符串	 
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
	 * 根据preOrgId，查找人行及虚拟机构
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
			//如果连接存在，则断开，结束会话，返回
			if(conn != null) conn.closeSession();
		}
		return orgNetList;
	}
}
