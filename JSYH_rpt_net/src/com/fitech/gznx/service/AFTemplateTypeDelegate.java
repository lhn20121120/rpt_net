package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.TreeContentBuilder;
import com.fitech.gznx.common.TreeNode;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfTemplatetype;


public class AFTemplateTypeDelegate {
	// 创建报表类别树
	public static String createTemplateTypeTree(String treeName,String reportFlg,String orgPodedom) {
		 //连接对象和会话对象初始化
		DBConn conn=null;
		Session session=null;   
		//	 查询条件HQL的生成
		String curr = null;

		try{
			//conn对象的实例化		  
			 conn=new DBConn();
			//打开连接开始会话
			session=conn.beginTransaction();
			String hql="";
			
			/***
			 * 20121122修改 te.bak1
			 */
			if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver"))	
			{
				hql= "from AfTemplatetype ri where ri.id.typeId in ( select t.pretypeId from AfTemplatetype t where  t.id.templateType="+reportFlg+" " +
					 " and convert(varchar,t.id.typeId) in (select te.bak1  from AfTemplate te where te.id.templateId in ("+orgPodedom+" " +
					 "group by viewOrgRep.childRepId ) and te.templateType="+reportFlg+"" +
					" group by te.bak1) group by t.pretypeId ) and ri.id.templateType="+reportFlg+" order by ri.id.typeId ";
			}
			else
			{
				hql= "from AfTemplatetype ri where ri.id.typeId in ( select t.pretypeId from AfTemplatetype t where  t.id.templateType="+reportFlg+" " +
				 " and t.id.typeId in (select te.bak1 from AfTemplate te where te.id.templateId in ("+orgPodedom+" " +
				 "group by viewOrgRep.childRepId ) and te.templateType="+reportFlg+"" +
				" group by te.bak1) group by t.pretypeId ) and ri.id.templateType="+reportFlg+" order by ri.id.typeId ";
			}
			List<AfTemplatetype> prelist = session.createQuery(hql).list();
			if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver"))
			{
				hql = "from AfTemplatetype t where  t.id.templateType="+reportFlg+" " +
					  " and convert(varchar,t.id.typeId) in (select te.bak1 from AfTemplate te where te.id.templateId in ("+orgPodedom+" " +
					 "group by viewOrgRep.childRepId ) and te.templateType="+reportFlg+"" +
					 " group by te.bak1) order by t.id.typeId ";
			}
			else
			{
				hql = "from AfTemplatetype t where  t.id.templateType="+reportFlg+" " +
				" and t.id.typeId in (select te.bak1 from AfTemplate te where te.id.templateId in ("+orgPodedom+" " +
						"group by viewOrgRep.childRepId ) and te.templateType="+reportFlg+"" +
						" group by te.bak1) order by t.id.typeId ";
			}
			List<AfTemplatetype> list = session.createQuery(hql).list();
			
			
			List root = new ArrayList();
			
			for(int i=0;i<list.size();i++){
				AfTemplatetype templateType = list.get(i);
				if(templateType.getPretypeId() == null){
					prelist.add(templateType);
					list.remove(i);
					i--;
				}
			}
			
			for(AfTemplatetype templateType:prelist){
				List<TreeNode> plist = findOrgByParentId(list, String.valueOf(templateType.getId().getTypeId()));
				String typeId = String.valueOf(templateType.getId().getTypeId());
				for(TreeNode node:plist){
					typeId = typeId+","+node.getKey();
				}
				root.add(new TreeNode(typeId, 
						templateType.getTypeName(),plist ));
			}

			for(AfTemplatetype templateType:list){
				root.add(new TreeNode(String.valueOf(templateType.getId().getTypeId()), 
						templateType.getTypeName(), null));
			}
			String treeContent = new TreeContentBuilder(treeName, root, null,false).buildTreeContent();
			
			return treeContent;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn!=null) conn.closeSession();
		}
		
	}
	
	
	/**
	 * 根据orgId取得下级所有机构
	 * 
	 * @param items
	 *            取得该下级机构,就从list里remove掉这个机构
	 * @param orgId
	 * @return List 2008-6-4
	 */
	private static List<TreeNode> findOrgByParentId(List items, String orgId) {
		List<TreeNode> types = new ArrayList<TreeNode>();
		for (int i = 0; i < items.size(); i++) {
			AfTemplatetype tempInfo = (AfTemplatetype) items.get(i);
			if (String.valueOf(tempInfo.getPretypeId()).trim().equals(orgId.trim())) {
				types.add(new TreeNode(String.valueOf(tempInfo.getId().getTypeId()), 
						tempInfo.getTypeName(), null));
				items.remove(i);
				i--;
			}
		}
		return types;
	}
	// 创建报表类别树
	public static String getTemplateTypeName(String templateType,String reportFlg) {
		 //连接对象和会话对象初始化
		DBConn conn=null;
		Session session=null;   
		//	 查询条件HQL的生成
		String curr = null;
		String typeId = "";
		if(templateType.indexOf(",")>-1){
			typeId = getpreTemplateId(templateType,reportFlg);
		} else {
			typeId = templateType;
		}
		String hql = "from AfTemplatetype t where t.id.typeId="+typeId+" and t.id.templateType="+reportFlg;
		try{
			//conn对象的实例化		  
			 conn=new DBConn();
			//打开连接开始会话
			session=conn.beginTransaction();
			List list = session.createQuery(hql).list();
			if(list != null && list.size()>0){
				AfTemplatetype aftemplateType = (AfTemplatetype) list.get(0);
				if(aftemplateType != null)
				return aftemplateType.getTypeName();
			}			
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn!=null) conn.closeSession();
		}
		
	}
	private static String getpreTemplateId(String templateType, String reportFlg) {
		 //连接对象和会话对象初始化
		DBConn conn=null;
		Session session=null;   
		//	 查询条件HQL的生成

//		if(templateType.indexOf(":")>-1){
//			reportFlg = templateType.split(":")[1];
//			templateType = templateType.split(":")[0];
//		}
		String hql = "select distinct t.pretypeId from AfTemplatetype t where t.id.typeId in ("+templateType+") and t.id.templateType="+reportFlg;
		try{
			//conn对象的实例化		  
			 conn=new DBConn();
			//打开连接开始会话
			session=conn.beginTransaction();
			List list = session.createQuery(hql).list();
			if(list != null && list.size()>0){
				Long aftemplateType = (Long) list.get(0);
				if(aftemplateType != null)
				return String.valueOf(aftemplateType);
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn!=null) conn.closeSession();
		}
		
	}
	public static List getTemplateTypes(String reportFlg) {
		List result = new ArrayList();
		// 连接对象和会话对象初始化
		DBConn conn = null;
		Session session = null;

		// 查询条件HQL的生成
		try {
			
			// conn对象的实例化
			conn = new DBConn();
			// 打开连接开始会话
			session = conn.beginTransaction();
			String hql = "select t.id.typeId,t.typeName from AfTemplatetype t where t.pretypeId is null and t.id.templateType="+reportFlg;
			
			List list = session.createQuery(hql).list();
			result.add(new LabelValueBean("",""));
			for(int i=0;i<list.size();i++){
				Object[] os = (Object[])list.get(i);
				if(os != null && os.length>0){
				String templateTypeName = (String) os[1];
				String sql = "select t.id.typeId,t.typeName from AfTemplatetype t where t.pretypeId="+os[0]
				+" and t.id.templateType="+reportFlg;
				List userlist = session.createQuery(sql).list();
				if(userlist != null && userlist.size()>0){
				for(int j=0;j<userlist.size();j++){
					Object[] type = (Object[])userlist.get(j);
					result.add(new LabelValueBean((templateTypeName+"-"+String.valueOf(type[1])),String.valueOf(type[0])));
				}
				}else{
					result.add(new LabelValueBean(templateTypeName,String.valueOf(os[0])));
				}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 如果连接存在，则断开，结束会话，返回
			if (conn != null)
				conn.closeSession();
		}
		return result;
		
	}
	public static String createTemplateTypeTree(String treeName) {
		 //连接对象和会话对象初始化
		DBConn conn=null;
		Session session=null;   
		//	 查询条件HQL的生成
		String curr = null;
		try{
			List<TreeNode> types = new ArrayList<TreeNode>();
			for(int reportFlg=1;reportFlg<4;reportFlg++){
			//conn对象的实例化		  
			 conn=new DBConn();
			//打开连接开始会话
			session=conn.beginTransaction();
			String hql = "from AfTemplatetype t where t.pretypeId is null and t.id.templateType="+reportFlg+" order by t.id.typeId ";
			List list = session.createQuery(hql).list();
			List<TreeNode> root = new ArrayList<TreeNode>();
			String templateTypeIds ="";
			for(int i=0;i<list.size();i++){
				AfTemplatetype templateType = (AfTemplatetype)list.get(i);
				String sql = "from AfTemplatetype t where t.pretypeId="+templateType.getId().getTypeId()
								+" and t.id.templateType="+reportFlg;
				List userlist = session.createQuery(sql).list();
				List content = new ArrayList();
				String strTypeIds ="";
				for(int j=0;j<userlist.size();j++){
					AfTemplatetype preTemplateType = (AfTemplatetype)userlist.get(j);
					content.add(new TreeNode(String.valueOf(preTemplateType.getId().getTypeId()), preTemplateType.getTypeName()));
					if(strTypeIds.equals("")){
						strTypeIds = String.valueOf(preTemplateType.getId().getTypeId());
					} else {
						strTypeIds = strTypeIds + "," + String.valueOf(preTemplateType.getId().getTypeId());
					}					
				}
				String pretypeId="";
				if(templateType != null){
					 pretypeId = strTypeIds.equals("")?String.valueOf(templateType.getId().getTypeId()):strTypeIds;
					root.add(new TreeNode(pretypeId, templateType.getTypeName(), content));
				}
				if(templateTypeIds.equals("")){
					templateTypeIds = pretypeId;
				}else{
					templateTypeIds = templateTypeIds+","+pretypeId;
				}
			}
			String typeName = "";
			if(reportFlg == 1){
				typeName = Config.CBRC_REPORT_NAME;
			} else if(reportFlg == 2){
				typeName = Config.PBOC_REPORT_NAME;				
			} else if(reportFlg == 3){
				typeName = Config.OTHER_REPORT_NAME;				
			}
			types.add(new TreeNode(templateTypeIds+":"+reportFlg, typeName, root));
			}
			String treeContent = new TreeContentBuilder(treeName, types, null,false).buildTreeContent();
			return treeContent;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn!=null) conn.closeSession();
		}
		
	}


	public static String createTemplateTypeTree(String treeName, String reportFlg) {
		 //连接对象和会话对象初始化
		DBConn conn=null;
		Session session=null;   
		//	 查询条件HQL的生成
		String curr = null;
		String hql = "from AfTemplatetype t where t.pretypeId is null and t.id.templateType="+reportFlg+" order by t.id.typeId ";
		try{
			//conn对象的实例化		  
			 conn=new DBConn();
			//打开连接开始会话
			session=conn.beginTransaction();
			List list = session.createQuery(hql).list();
			List<TreeNode> root = new ArrayList<TreeNode>();
			for(int i=0;i<list.size();i++){
				AfTemplatetype templateType = (AfTemplatetype)list.get(i);
				String sql = "from AfTemplatetype t where t.pretypeId="+templateType.getId().getTypeId()
								+" and t.id.templateType="+reportFlg;
				List userlist = session.createQuery(sql).list();
				List content = new ArrayList();
				String strTypeIds ="";
				for(int j=0;j<userlist.size();j++){
					AfTemplatetype preTemplateType = (AfTemplatetype)userlist.get(j);
					content.add(new TreeNode(String.valueOf(preTemplateType.getId().getTypeId()), preTemplateType.getTypeName()));
					if(strTypeIds.equals("")){
						strTypeIds = String.valueOf(preTemplateType.getId().getTypeId());
					} else {
						strTypeIds = strTypeIds + "," + String.valueOf(preTemplateType.getId().getTypeId());
					}					
				}
				
				if(templateType != null){
					String pretypeId = strTypeIds.equals("")?String.valueOf(templateType.getId().getTypeId()):strTypeIds;
					root.add(new TreeNode(pretypeId, templateType.getTypeName(), content));
				}
			}
			String treeContent = new TreeContentBuilder(treeName, root, null,false).buildTreeContent();
			
			return treeContent;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			//如果连接存在，则断开，结束会话，返回
			if(conn!=null) conn.closeSession();
		}
	}
}
