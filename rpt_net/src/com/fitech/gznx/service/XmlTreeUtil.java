package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import net.sf.hibernate.Session;

import com.fitech.gznx.common.TreeNode;
import com.fitech.gznx.common.TreeContentBuilder;
import com.fitech.gznx.po.AfCodelib;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.net.hibernate.OrgNet;

import com.cbrc.smis.security.Operator;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.MCurr;



public class XmlTreeUtil {
	
	/**
	 * ���ݵ�ǰ����Ȩ�����ɻ���ģ��xml
	 * 
	 * @param 1-request,2-�����ƣ�3-ѡ�е�checkList��4-�Ƿ���ѡ�е�checkList 5-�Ƿ���Ҫ��ǰ�û�Ȩ���ڵ���
	 *        true -��Ȩ��, �Ƿ���ʾ������� true-��ʾ
	 * @return list 2008-6-4
	 */
	public static String createOrgXml(HttpServletRequest request, String treeName,
			Map keyMap, boolean multiSel, boolean flag, boolean isDisplay)
			throws Exception {
		Operator operatorBase = null;
		HttpSession sess = request.getSession();
		if (sess.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operatorBase = (Operator) sess
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		else
			operatorBase = new Operator();
		DBConn conn = null;
		Session session = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			List item = new ArrayList();
			List itemVitual = new ArrayList();
			StringBuffer filter = new StringBuffer();
			StringBuffer hzsql = new StringBuffer();
			
			if (flag == true) {
				
				String orgIdString = operatorBase.getSubOrgIds();
				
				
				// ������л�����Ϣ
				if (operatorBase.isSuperManager()) {
					filter.append("from AfOrg instance order by instance.orgId asc");						
				} else {
				// ����Ȩ�޻�����л�����Ϣ
					if (!orgIdString.equals("") && orgIdString != null) {
						filter.append("from AfOrg instance where instance.orgId in(").append(orgIdString)
							.append(Config.SPLIT_SYMBOL_COMMA).append(
									"'").append(
									operatorBase.getOrgId()).append("')");
					
					} else {
						filter.append("from AfOrg instance where instance.orgId in('").append(
								operatorBase.getOrgId()).append("')");
						
					}
				}
			} else {
				filter.append("from AfOrg instance order by instance.orgId asc");				
			}

			item = session.createQuery(filter.toString()).list();
			List<TreeNode> root = new ArrayList<TreeNode>();
			if (isDisplay == true) {
				for (int i = 0; i < item.size(); i++) {
					AfOrg orgInfo = (AfOrg) item.get(i);
					if(orgInfo.getOrgType()!=null && orgInfo.getOrgType().equals("1")){
						// ������������Ϣ
						hzsql.append("from AfOrg t where t.orgId in(select instance.id.orgId " +
								"from AfCollectRelation instance where instance.id.collectId='").
								append(orgInfo.getOrgId()).append("')");
						itemVitual = session.createQuery(hzsql.toString()).list();
						
						List<TreeNode> result = new ArrayList<TreeNode>();
						for(int j=0;j<itemVitual.size();j++){
							AfOrg afOrg = (AfOrg) itemVitual.get(j);
							result.add(new TreeNode(afOrg.getOrgId(),
									afOrg.getOrgName(),null));
						}
						
						//root.add(new TreeNode(orgInfo.getOrgId(),
						//		orgInfo.getOrgName(),result));
					}
				}
			}
			// ���������ʾ��List
			if (operatorBase.isSuperManager()) {
				List<TreeNode> trueRoot = productTree(item, "0");
				for(int i=0;i<trueRoot.size();i++){
					root.add(trueRoot.get(i));
				}
				
			} else {
				for (int j = 0; j < item.size(); j++) {
					AfOrg orgInfo = (AfOrg) item.get(j);
					// ������ϼ�,����Id.
					AfOrg orgInfo_new = IsExisParOrgId(orgInfo.getPreOrgId(), item);

					// �����ϼ��������ϼ��ڵ�Id������
					if (orgInfo_new != null) {
						List result = productTree(item, orgInfo_new.getOrgId());
						// /root = this.productPTree(item,orgInfo_new);
						root.add(new TreeNode(orgInfo_new.getOrgId(),
								orgInfo_new.getOrgName(), result));
						 orgIf = null;
						// ����ýڵ��и��ڵ�,��ŵ��ø��ڵ���.
					} else {
						List result = productTree(item, orgInfo.getOrgId());
						root.add(new TreeNode(orgInfo.getOrgId(), orgInfo
								.getOrgName(), result));
						if (root == null && root.size() < 0) {
							root.add(new TreeNode(orgInfo.getOrgId(), orgInfo
									.getOrgName(), null));
							item.remove(j);
							j--;
						}
					}
				}
			}
			
			
			String treeContent = new TreeContentBuilder(treeName, root, keyMap,
					multiSel).buildTreeContent();
			
			return treeContent;
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return null;
	}

	/**
	 * �ݹ�
	 * 
	 * @param items
	 *            ԴList
	 * @param orgId
	 *            orgId
	 * @return 2008-6-4
	 */
	private static List<TreeNode> productTree(List items, String orgId) {
		List<TreeNode> result = new ArrayList<TreeNode>();
		// ����¼�����list
		List temps = findOrgByParentOrgId(items, orgId);
		if (temps != null && temps.size() != 0) {
			for (int i = 0; i < temps.size(); i++) {
				AfOrg tempOrgInfo = (AfOrg) temps.get(i);
				result.add(new TreeNode(tempOrgInfo.getOrgId(), tempOrgInfo
						.getOrgName(), productTree(items, tempOrgInfo
						.getOrgId())));
			}
			return result;
		} else {
			return result;
		}
	}
	
	/**
	 * �ݹ�
	 * 
	 * @param items
	 *            ԴList
	 * @param orgId
	 *            orgId
	 * @return 2008-6-4
	 */
	private static List<TreeNode> producthzTree(List temps, String orgId) {
		List<TreeNode> result = new ArrayList<TreeNode>();

		if (temps != null && temps.size() != 0) {
			for (int i = 0; i < temps.size(); i++) {
				AfOrg tempOrgInfo = (AfOrg) temps.get(i);
				result.add(new TreeNode(tempOrgInfo.getOrgId(), tempOrgInfo
						.getOrgName(), producthzTree(temps, tempOrgInfo
						.getOrgId())));
			}
			return result;
		} else {
			return result;
		}
	}
	/**
	 * ����orgIdȡ���¼����л���
	 * 
	 * @param items
	 *            ȡ�ø��¼�����,�ʹ�list��remove���������
	 * @param orgId
	 * @return List 2008-6-4
	 */
	private static List findOrgByParentOrgId(List items, String orgId) {
		List result = new ArrayList();
		for (int i = 0; i < items.size(); i++) {
			AfOrg orgInfo = (AfOrg) items.get(i);
			if (orgInfo.getPreOrgId().trim().equals(orgId.trim())) {
				result.add(orgInfo);
				items.remove(i);
				i--;
			}
		}
		return result;
	}
	
	private static AfOrg orgIf = null;
	// ������һ���������ϼ�,����Id,���û����һ��,�򷵻�null,�������һ��,�Ҳ������ϼ��Ļ�,���ص�һ��.
	private static AfOrg IsExisParOrgId(String orgId, List item) {
		for (int i = 0; i < item.size(); i++) {
			AfOrg orgInfo = (AfOrg) item.get(i);
			if (orgInfo.getOrgId().trim().equals(orgId.trim())) {
				orgIf = orgInfo;
				orgIf = IsExisParOrgId(orgIf.getPreOrgId(), item);
				break;
			}
		}
		return orgIf;

	}

	public static String createCurrTree(String treeName,
			Map keyMap, boolean multiSel) {
		 //���Ӷ���ͻỰ�����ʼ��
		DBConn conn=null;				
		Session session=null;   
		//	 ��ѯ����HQL������
		String curr = null;
		String hql = "from MCurr a order by a.curId ";
		try{
			//conn�����ʵ����		  
			 conn=new DBConn();
			//�����ӿ�ʼ�Ự
			session=conn.openSession();
			List list = session.createQuery(hql).list();
			List<TreeNode> root = new ArrayList<TreeNode>();
			for(int i=0;i<list.size();i++){
				MCurr mcurr = (MCurr) list.get(i);
				if(mcurr != null){
					root.add(new TreeNode(String.valueOf(mcurr.getCurId()), mcurr.getCurName(), null));
				}
			}
			String treeContent = new TreeContentBuilder(treeName, root, keyMap,
					multiSel).buildTreeContent();
			
			return treeContent;
		}catch(Exception e){
		 e.printStackTrace();
		 return null;
		}finally{
		//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn!=null) conn.closeSession();
		}
		
	}

	public static String createUserTree(String treeName,
			Map keyMap, boolean multiSel) {
		 //���Ӷ���ͻỰ�����ʼ��
		DBConn conn=null;				
		Session session=null;   
		//	 ��ѯ����HQL������
		String curr = null;
		String hql = "from OrgNet a order by a.orgId  ";
		try{
			//conn�����ʵ����		  
			 conn=new DBConn();
			//�����ӿ�ʼ�Ự
			session=conn.openSession();
			List list = session.createQuery(hql).list();
			List<TreeNode> root = new ArrayList<TreeNode>();
			for(int i=0;i<list.size();i++){
				OrgNet userGrp = (OrgNet)list.get(i);
				String sql = "from Operator a where a.org.orgId = "+userGrp.getOrgId();
				List userlist = session.createQuery(sql).list();
				List content = new ArrayList();
				for(int j=0;j<userlist.size();j++){
					com.cbrc.auth.hibernate.Operator operator = (com.cbrc.auth.hibernate.Operator) userlist.get(j);
					content.add(new TreeNode(String.valueOf(operator.getUserId()), operator.getUserName(), null));
				}
				if(userGrp != null){
					root.add(new TreeNode(String.valueOf(userGrp.getOrgId()), userGrp.getOrgName(), content));
				}
			}
			String treeContent = new TreeContentBuilder(treeName, root, keyMap,
					multiSel).buildTreeContent();
			
			return treeContent;
		}catch(Exception e){
		 e.printStackTrace();
		 return null;
		}finally{
		//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn!=null) conn.closeSession();
		}
		
	}
	
	public static List selectUserTree() {
		 //���Ӷ���ͻỰ�����ʼ��
		DBConn conn=null;				
		Session session=null;   

		String hql = "from OrgNet a order by a.orgId  ";
		try{
			//conn�����ʵ����		  
			 conn=new DBConn();
			//�����ӿ�ʼ�Ự
			session=conn.openSession();
			return session.createQuery(hql).list();
			
		}catch(Exception e){
		 e.printStackTrace();
		 return null;
		}finally{
		//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn!=null) conn.closeSession();
		}		
	}
	
	public static List selectUserTreebyID(String orgId) {
		 //���Ӷ���ͻỰ�����ʼ��
		DBConn conn=null;				
		Session session=null;   

		String sql = "from Operator a where a.org.orgId = "+orgId;
		try{
			//conn�����ʵ����		  
			 conn=new DBConn();
			//�����ӿ�ʼ�Ự
			session=conn.openSession();
			return session.createQuery(sql).list();
			
		}catch(Exception e){
		 e.printStackTrace();
		 return null;
		}finally{
		//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn!=null) conn.closeSession();
		}		
	}
	
	/**
	 * ���ɱ�����
	 * @param treeName
	 * @param keyMap
	 * @param multiSel
	 * @return
	 */
	public static String createTemplateTree(String treeName,
			Map keyMap, boolean multiSel) {
		
		//���Ӷ���ͻỰ�����ʼ��
		DBConn conn = null;
		Session session = null;
		
		//	 ��ѯ����HQL������
		String curr = null;
		
		try{
			//conn�����ʵ����
			conn = new DBConn();
			//�����ӿ�ʼ�Ự
			session = conn.openSession();
			
			String[][] repType = {{"140","����ᱨ��"},{"141","���б���"},{"142","��������"}};

			List<TreeNode> root = new ArrayList<TreeNode>();
			
			for(int i=0;i<repType.length;i++){
				
				String sql = "from AfCodelib a where a.id.codeType =" + repType[i][0] + " order by a.id.codeId";
				
				List codelib = session.createQuery(sql).list();
				
				List content = new ArrayList();
				
				for(int j=0;j<codelib.size();j++){
					
					AfCodelib cd = (AfCodelib) codelib.get(j);
					
					String tpsql = "select distinct(a.id.templateId),a.templateName from AfTemplate a where a.bak1=" + cd.getId().getCodeId() 
									+ " and a.usingFlag=1 order by a.id.templateId";
					
					List templateList = session.createQuery(tpsql).list();
					
					List contentx = new ArrayList();
					
					for(int k=0;k<templateList.size();i++) {
						
						AfTemplate template = (AfTemplate) templateList.get(i);
						
						contentx.add(new TreeNode(String.valueOf(template.getId().getTemplateId()),template.getTemplateName(),null));
					}
					
					content.add(new TreeNode(String.valueOf(cd.getId().getCodeId()), cd.getCodeName(), contentx));
				}
				
				root.add(new TreeNode(repType[i][0], repType[i][1], content));

			}
			
			String treeContent = new TreeContentBuilder(treeName, root, keyMap,
					multiSel).buildTreeContent();
			
			return treeContent;
		} catch(Exception e) {
			
		 e.printStackTrace();
		 return null;
		 
		} finally {
			//������Ӵ��ڣ���Ͽ��������Ự������
			if(conn!=null) conn.closeSession();
		}
		
	}
	
	public static void main(String a[]){
		//String[][] repType = {{"140","����ᱨ��"},{"141","���б���"},{"142","��������"}};
		//System.out.println(repType[1][1]);	

	}
	
}
