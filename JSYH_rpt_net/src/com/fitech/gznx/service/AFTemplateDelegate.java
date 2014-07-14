package com.fitech.gznx.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.struts.util.LabelValueBean;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.form.HZFormulaForm;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateCurrRelation;
import com.fitech.gznx.po.AfTemplateId;
import com.fitech.gznx.po.PreOrg;
import com.fitech.gznx.util.DateUtil;
import com.fitech.gznx.util.TranslatorUtil;

public class AFTemplateDelegate extends DaoModel{


	private static FitechException log = new FitechException(AFTemplateDelegate.class); 

	/**
	 * ȡ�����б������ְ汾�ţ�
	 * 
	 * @author Ҧ��
	 * @return List ���б���
	 * @exception Exception
	 */
	public static List getAllReports() throws Exception {

		List retVals = null;
		DBConn conn = null;

		try {
			StringBuffer hql = new StringBuffer(
					"select distinct mcr.id.templateId,mcr.templateName from AfTemplate mcr order by mcr.id.templateId");
			conn = new DBConn();

			Session session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				retVals = new ArrayList();
				while (it.hasNext()) {
					Object[] item = (Object[]) it.next();
					MChildReportForm form = new MChildReportForm();
					form.setChildRepId(item[0] != null ? (String) item[0] : "");					
					form.setReportName(item[1] != null ? (String) item[1] : "");
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
	 * ��ʹ��Hibernate ���Ը� 2011-12-28
	 * Ӱ�����AfTemplate
	 * ȡ�����б������ְ汾�ţ�
	 * 
	 * @author Ҧ��
	 * @return List ���б���
	 * @exception Exception
	 */
	public static List getAllReportsByType(String templatetype) throws Exception {

		List retVals = null;
		DBConn conn = null;

		try {
			StringBuffer hql = new StringBuffer(
					"select distinct mcr.id.templateId,mcr.templateName from AfTemplate mcr where" +
						" mcr.templateType='"+templatetype+"' and mcr.usingFlag=1 order by mcr.id.templateId");
			conn = new DBConn();

			Session session = conn.openSession();
			Query query = session.createQuery(hql.toString());
			List list = query.list();

			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				retVals = new ArrayList();
				while (it.hasNext()) {
					Object[] item = (Object[]) it.next();
					String templateId = item[0] != null ? (String) item[0] : "";
					String templateName = item[1] != null ? (String) item[1] : "";

					retVals.add(new LabelValueBean(templateName,templateId));
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
	
	/** ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfTemplate*/
	public static boolean updateUsingFlag(String templateId, String versionId, int usingFlg)  throws Exception {    
	    boolean result = false;
	    DBConn conn =null;
	    Session session =null;
	    if(templateId!=null && versionId!=null)
	    {
	        try
	        {
	            conn = new DBConn();
	            session = conn.beginTransaction(); 
	            /** ��ʹ��hibernate ���Ը� 2011-12-21
	             * Ӱ�����AfTemplate**/
	            AfTemplate mcr = getTemplate(templateId,versionId);
	            mcr.setUsingFlag(usingFlg);
	            session.update(mcr);

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
	    return   result;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfTemplate
	 * ���ݱ����汾�Ż��ģ��
	 * @param templateId
	 * @param versionId
	 * @return AfTemplate
	 */
	public static AfTemplate getTemplate(String templateId, String versionId) {
		// TODO Auto-generated method stub
		boolean result = false;
	    DBConn conn =null;
	    Session session =null;
	    AfTemplate template = new AfTemplate();
	    
	    if(templateId!=null && versionId!=null) {
	        try {
	            conn = new DBConn();
	            session = conn.beginTransaction(); 
				AfTemplateId id = new AfTemplateId();
				id.setTemplateId(templateId);
				id.setVersionId(versionId);

				template = (AfTemplate)session.load(AfTemplate.class, id);
				
	        } catch(Exception e) {
	        	
	            log.printStackTrace(e);
	            result = false;
	            
	        }
	        finally{

	            if(conn!=null)
	                conn.endTransaction(result);
	        }
	       
	    }
		
		return template;
	}
	
	/****
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * @param templateId
	 * @param versionId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean updateAFTemplateUsing(String templateId, String versionId,String startDate,String endDate){
		boolean result = true;		
		DBConn conn = null;
		Session session = null;
		try {
			AfTemplate temp = getTemplate(templateId, versionId);
			temp.setStartDate(startDate);
			temp.setEndDate(endDate);
			temp.setUsingFlag(1);
			conn = new DBConn();
			session = conn.beginTransaction();
			session.update(temp);
			result=true;
		} catch (Exception e) {
		result = false;
		log.printStackTrace(e);
		} finally {
			if (conn != null) conn.endTransaction(result);
		}
		
		return result;
	}
	
	/****
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * Ӱ�����AfTemplate
	 * @param templateForm
	 * @param reportFlg
	 * @param curPage
	 * @return
	 */
	public static PageListInfo getTemplates(AFTemplateForm templateForm,
			String reportFlg, int curPage) {
		List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	PageListInfo pageListInfo=null;
    	try{    		
			conn = new DBConn();
			session = conn.beginTransaction();
    		
			 String hql=" from AfTemplate t where t.templateType='"+reportFlg+"'";
			 if (templateForm != null) {
					String reportName = templateForm.getTemplateName();
					if (reportName != null && !reportName.equals("")){
						hql+=" and t.templateName like'%" + reportName.trim() + "%'";
					}
					String templateType = templateForm.getTemplateType();
					if (templateType != null && !templateType.equals("")){
						hql+=" and t.bak1 in (" + templateType.trim() + ")";
					}
				}
			 /**order by ���� ��ע�͵� ���Ը� 2011-12-28*/
			 if(Config.DB_SERVER_TYPE.equals("oracle"))
				 hql+=" order by t.id.templateId";
			 if(Config.DB_SERVER_TYPE.equals("sqlserver") || Config.DB_SERVER_TYPE.equals("db2"))
				 hql+="";
			 /**��ʹ��hibernate ���Ը� 2011-12-21**/
			pageListInfo = findByPageWithSQL(session,hql,Config.PER_PAGE_ROWS,curPage);
			//recordCount = (int) pageListInfo.getRowCount();
			List list=pageListInfo.getList();
			for(int i=0;i<list.size();i++){
				AfTemplate template = (AfTemplate)list.get(i);
				AFTemplateForm form = new AFTemplateForm();
				form.setTemplateId(template.getId().getTemplateId());
				form.setVersionId(template.getId().getVersionId());
				form.setEndDate(template.getEndDate());
				form.setIsCollect(String.valueOf(template.getIsCollect()));
				form.setIsReport(String.valueOf(template.getIsReport()));
				form.setUsingFlag(String.valueOf(template.getUsingFlag()));
				form.setStartDate(template.getStartDate());
				form.setTemplateName(template.getTemplateName());
	//			form.setTemplateType(getTemplateType(template.getBak1(),reportFlg));
				
				resList.add(form);
			}
			pageListInfo.setList(resList);
    	}catch(Exception he){
    		if(conn != null) conn.endTransaction(true);
    	}finally{
    		if(conn != null) 
    			conn.closeSession();
    	}
		return pageListInfo;
	}
	/****
	 * ��ʹ��hibernate wmm 2013-4-25
	 * Ӱ�����AfTemplateCollRep,AfTemplateCollRule
	 * @param orgInfoForm
	 * @param reportFlg
	 * @param curPage
	 * @return
	 */
	public static List getHZItems(OrgInfoForm orgInfoForm,
			String reportFlg, int curPage,String templateId ,String versionId) {
		List resList = new ArrayList();
		DBConn conn = null;
		Session session = null;
		PageListInfo pageListInfo=null;
		Connection connection=null;
		Statement stm=null;
		StringBuffer sb=new StringBuffer();
		try{    		
			 conn = new DBConn();
			 session=conn.openSession();
			 connection=session.connection();
			 stm=connection.createStatement();
			 
			 sb.append("SELECT A.ORG_ID,A.TEMPLATE_ID,A.VERSION_ID,A.TEMPLATE_NAME,A.ORG_NAME ,B.COLL_SCHEMA,B.COLL_FORMULA FROM ( ");
			 sb.append("SELECT A.*,B.ORG_NAME  FROM (  ");
			 sb.append("SELECT A.*,B.TEMPLATE_NAME  FROM AF_TEMPLATE_COLL_REP A ,(SELECT MAX(TEMPLATE_NAME)  TEMPLATE_NAME,TEMPLATE_ID FROM AF_TEMPLATE GROUP BY TEMPLATE_ID) B ");
			 sb.append("WHERE A.TEMPLATE_ID=B.TEMPLATE_ID ) A,AF_ORG B WHERE A.ORG_ID=B.ORG_ID AND A.TEMPLATE_ID='"+templateId+"' and a.version_id = '"+versionId+"'");
			 if (orgInfoForm != null) {
					String orgName = orgInfoForm.getOrgName();
					if (orgName != null && !orgName.equals("")){
						orgName.replaceAll(" ", "");
						sb.append(" AND B.ORG_NAME LIKE'%" + orgName.trim() + "%'");
					}
					
				
				}
			 sb.append(" )");
			 sb.append("A LEFT JOIN AF_TEMPLATE_COLL_RULE B ON A.TEMPLATE_ID=B.TEMPLATE_ID AND A.ORG_ID=B.ORG_ID AND A.VERSION_ID=B.VERSION_ID  ORDER BY A.ORG_ID");
			 ResultSet result=stm.executeQuery(sb.toString());
			 while (result.next()) {
				 HZFormulaForm form = new HZFormulaForm();
				 form.setOrgId(result.getString(1));
				 form.setTemplateId(result.getString(2));
				 form.setVersionId(result.getString(3));
				 form.setReportName(result.getString(4));
				 form.setOrgName(result.getString(5));
				 form.setCollSchema(result.getString(6));
				 form.setCollFormula(result.getString(7));
				 resList.add(form);
				 
			}
			/**order by ���� ��ע�͵� wmm 2011-12-28*/
			
			/**��ʹ��hibernate wmm 2011-12-21**/
//			pageListInfo = findHZItemsByPageWithSQL(session,sb.toString(),Config.PER_PAGE_ROWS,curPage);
//			//recordCount = (int) pageListInfo.getRowCount();
//			List list=pageListInfo.getList();
//			for(int i=0;i<list.size();i++){
//				Object[] template = (Object[])list.get(i);
//				AfOrg afOrg = (AfOrg)template[1];
//				HZFormulaForm form = new HZFormulaForm();
//				
//				form.setOrgName(afOrg.getOrgName());
//				form.setOrgId(afOrg.getOrgId());
//				//			form.setTemplateType(getTemplateType(template.getBak1(),reportFlg));
//				
//				resList.add(form);
//			}
			//pageListInfo.setList(resList);
		}catch(Exception he){
			log.printStackTrace(he);
		}finally{
			if(conn != null) 
				conn.closeSession();
			try{
			if(session!=null){
				session.close();
			}
			if(connection!=null){
				connection.close();
			}
			if(stm!=null){
				stm.close();
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resList;
	}
	/**
	 * 
	 * @param orgId  add by ������
	 * @return
	 */
	
	public static List<PreOrg> getHZRulePreOrgIds(String orgId) {
		List<PreOrg> resList = new ArrayList<PreOrg>();
		DBConn conn = null;
		Session session = null;
		Connection connection=null;
		Statement stm=null;
		StringBuffer sb=new StringBuffer();
		try{    		
			conn = new DBConn();
			session=conn.openSession();
			connection=session.connection();
			stm=connection.createStatement();
			sb.append(" SELECT ORG_ID,ORG_NAME FROM (");
			sb.append(" SELECT ORG_ID, ORG_NAME FROM AF_ORG ORDER BY ORG_ID)  ORG_REAL");
			sb.append(" UNION ");
			sb.append(" SELECT ORG_ID,ORG_NAME FROM (");
			sb.append(" select B.COLLECT_ID ORG_ID,C.ORG_NAME from af_collect_relation B,AF_ORG C WHERE B.COLLECT_ID=C.ORG_ID ORDER BY B.COLLECT_ID)  ORG_VIRTUAL ");
//			sb.append(" SELECT ORG_ID, ORG_NAME FROM AF_ORG WHERE ORG_ID =(select PRE_ORG_ID from af_org WHERE ORG_ID='"+orgId+"')  ");
//			sb.append(" UNION ");
//			sb.append(" select B.COLLECT_ID ORG_ID,C.ORG_NAME from af_collect_relation B,AF_ORG C WHERE B.COLLECT_ID=C.ORG_ID AND B.ORG_ID='"+orgId+"' ");
			ResultSet result=stm.executeQuery(sb.toString());
			while (result.next()) {
				PreOrg form = new PreOrg();
				form.setOrgId(result.getString(1));
				form.setOrgName(result.getString(2));
				resList.add(form);
			}
			
		}catch(Exception he){
			log.printStackTrace(he);
		}finally{
			if(conn != null) 
				conn.closeSession();
			try{
				if(session!=null){
					session.close();
				}
				if(connection!=null){
					connection.close();
				}
				if(stm!=null){
					stm.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resList;
	}
	/**
	 * 
	 * @param orgId  add by ������
	 * @return
	 */
	
	public static List<PreOrg> getHZRuleVirtualPreOrgIds(String orgId) {
		List<PreOrg> resList = new ArrayList<PreOrg>();
		DBConn conn = null;
		Session session = null;
		Connection connection=null;
		Statement stm=null;
		StringBuffer sb=new StringBuffer();
		try{    		
			conn = new DBConn();
			session=conn.openSession();
			connection=session.connection();
			stm=connection.createStatement();
//			sb.append(" SELECT ORG_ID,ORG_NAME FROM (");
//			sb.append(" SELECT ORG_ID, ORG_NAME FROM AF_ORG ORDER BY ORG_ID)  ORG_REAL");
//			sb.append(" UNION ");
//			sb.append(" SELECT ORG_ID,ORG_NAME FROM (");
//			sb.append(" select B.COLLECT_ID ORG_ID,C.ORG_NAME from af_collect_relation B,AF_ORG C WHERE B.COLLECT_ID=C.ORG_ID ORDER BY B.COLLECT_ID)  ORG_VIRTUAL ");
//			sb.append(" SELECT ORG_ID, ORG_NAME FROM AF_ORG WHERE ORG_ID =(select PRE_ORG_ID from af_org WHERE ORG_ID='"+orgId+"')  ");
//			sb.append(" UNION ");
			sb.append(" select B.COLLECT_ID ORG_ID,C.ORG_NAME from af_collect_relation B,AF_ORG C WHERE B.COLLECT_ID=C.ORG_ID AND B.ORG_ID='"+orgId+"' ");
			ResultSet result=stm.executeQuery(sb.toString());
			while (result.next()) {
				PreOrg form = new PreOrg();
				form.setOrgId(result.getString(1));
				form.setOrgName(result.getString(2));
				resList.add(form);
			}
			
		}catch(Exception he){
			log.printStackTrace(he);
		}finally{
			if(conn != null) 
				conn.closeSession();
			try{
				if(session!=null){
					session.close();
				}
				if(connection!=null){
					connection.close();
				}
				if(stm!=null){
					stm.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resList;
	}
	
	
	/**
	 * @author wangmm 2013-04-25
	 * @param session
	 * @param queryStr
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	 public static PageListInfo findHZItemsByPageWithSQL(Session session,String queryStr,  int pageSize , int curPage){
	        PageListInfo pageListInfo = new PageListInfo(pageSize,curPage);
	        try {
	        	
	            Query  query;
	            
	            query =  session.createQuery("select count(*) from "+queryStr);
	            Integer i = (Integer)query.list().get(0);
	            pageListInfo.setRowCount(i);
	            int firstPage = (pageListInfo.getCurPage()-1)*pageSize;
	            query =  session.createQuery(queryStr);
	            query.setFirstResult(firstPage);
	            query.setMaxResults(pageSize);
	            pageListInfo.setList(query.list());

	            //wgm 09-3-20 ���setCurPageRowCount����Ϊ��ҳ��ò�����ȷ�ĵ�ǰҲ����������.
	            //��ʾ��ʼ����15������ڶ�ҲʱҲ��15����ȷ��Ӧ����30����������ǽ��������
	            //��ǰ�����ǵ�8ҳ������ѡ�е�����һҳ����Ӧ����120�Ŷԣ�������15��
	            //---begin
	            int count = curPage * pageSize;
	            //��¼����û��pageSize(Ŀǰ��15��)��,��ʾʵ������
	            if(pageListInfo.getRowCount()<pageSize || curPage == pageListInfo.getPageCount())
	            	count = (int)pageListInfo.getRowCount();
	            pageListInfo.setCurPageRowCount(count);
	            //---end
	            
	            return pageListInfo;
	        }catch (RuntimeException re) {
	            
	            throw re;
	        }catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return pageListInfo;
	    }
	
	/****
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfTemplate
	 * @param templateForm
	 * @param reportFlg
	 * @param curPage
	 * @param operator
	 * @return
	 */
	public static PageListInfo getCustomTemplates(AFTemplateForm templateForm,
			String reportFlg, int curPage,Operator operator) {
		List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	PageListInfo pageListInfo=null;
    	try{    		
			conn = new DBConn();
			session = conn.beginTransaction();
			String orgId = operator.getOrgId();
			
			 String hql=" from AfTemplate t where t.templateType='"+reportFlg+"' and t.isReport!="+com.fitech.gznx.common.Config.TEMPLATE_VIEW
			 +" and t.usingFlag=1 and (t.reportStyle!=2 or t.reportStyle is null)";
			 /**��ӱ���鿴Ȩ�ޣ������û�������ӣ�
			  * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false){
				if (operator.getChildRepSearchPopedom() != null &&!operator.getChildRepSearchPopedom().equals("")){
					if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
//						hql+=" and '" + orgId + "'||t.id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
						//2013-12-24:LuYueFei�޸�Ϊ���ϴ��룬������������ѯ�����趨1104ʵ�������Ȩ��
						hql+=" and t.id.templateId in (select distinct viewOrgRep.childRepId from ViewOrgRep viewOrgRep where viewOrgRep.powType="
			        		+Config.POWERTYPESEARCH+" and viewOrgRep.userId="+operator.getOperatorId()+")";
					if(Config.DB_SERVER_TYPE.equals("sqlserver"))
						hql+=" and '" + orgId + "'+t.id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
				}
			}
			 if (templateForm != null) {
					String reportName = templateForm.getTemplateName();
					if (reportName != null && !reportName.equals("")){
						hql+=" and t.templateName like'%" + reportName.trim() + "%'";
					}
					String templateType = templateForm.getTemplateType();
					if (templateType != null && !templateType.equals("")){
						hql+=" and t.bak1 in (" + templateType.trim() + ")";
					}
				}
			 /**order by ������ ע�͵� ���Ը� 2011-12-29
			  * ���������ݿ��ж�*/
			 if(Config.DB_SERVER_TYPE.equals("oracle"))
				 hql+=" order by t.id.templateId";
			 if(Config.DB_SERVER_TYPE.equals("sqlserver") || Config.DB_SERVER_TYPE.equals("db2"))
				 hql+="";
			 /**��ʹ��hibernate ���Ը� 2011-12-21**/
			pageListInfo = findByPageWithSQL(session,hql,Config.PER_PAGE_ROWS,curPage);
			//recordCount = (int) pageListInfo.getRowCount();
			List<AfTemplate> list=pageListInfo.getList();
			for(AfTemplate template:list){

				AFTemplateForm form = new AFTemplateForm();
				form.setTemplateId(template.getId().getTemplateId());
				form.setVersionId(template.getId().getVersionId());
				form.setEndDate(template.getEndDate());
				form.setIsCollect(String.valueOf(template.getIsCollect()));
				form.setIsReport(String.valueOf(template.getIsReport()));
				form.setUsingFlag(String.valueOf(template.getUsingFlag()));
				form.setStartDate(template.getStartDate());
				form.setTemplateName(template.getTemplateName());
			//	form.setTemplateType(getTemplateType(template.getBak1(),reportFlg));
				
				resList.add(form);
			}
			pageListInfo.setList(resList);
    	}catch(Exception he){
    		he.printStackTrace();
    		if(conn != null) conn.endTransaction(true);
    	}finally{
    		if(conn != null) 
    			conn.closeSession();
    	}
		return pageListInfo;
	}
	
//	private static String getTemplateType(String bak1, String reportFlg) {
//		return AFTemplateTypeDelegate.getTemplateTypeName(bak1, reportFlg);	
//	}

	/**
	 * ���ݱ����źͱ���İ汾���жϴ˱����Ƿ����
	 * 
	 * @author rds
	 * @serialData 2005-12-07
	 * 
	 * @param childRepId
	 *            String ������
	 * @param versionId
	 *            String ����汾��
	 * @return void ������ڣ�����true;���򣬷���false
	 */
	public static boolean isTemplateExists(String childRepId,
			String versionId) {
		boolean isExists = false;

		if (childRepId == null || versionId == null)
			return isExists;

		DBConn conn = null;
		Session session = null;

		try {
			String hql = "select count(*) from AfTemplate mcr where mcr.id.templateId='"
					+ childRepId
					+ "'"
					+ " and mcr.id.versionId='"
					+ versionId + "'";

			conn = new DBConn();
			session = conn.openSession();

			List list = session.find(hql);
			if (list != null && list.size() > 0) {
				int count = list.get(0) == null ? 0 : ((Integer) list.get(0))
						.intValue();
				if (count > 0)
					isExists = true;
			}

		} catch (HibernateException he) {
			log.printStackTrace(he);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return isExists;
	}
	
	/***
	 * jdbc���� ������oracle�﷨ ����Ҫ�޸� ���Ը� 2011-12-27
	 * Ӱ���af_template_curr_relation af_template_freq_relation af_template_org_relation
	 * 		 af_gatherformula af_validateformula af_cellinfo qd_cellinfo af_template
	 * @param templateId
	 * @param versionId
	 * @return
	 */
	public static boolean deleteTemplate(String templateId, String versionId) {
		boolean result = false;
		Connection conn = null;
		FitechConnection connFactory = null;
		Statement stmt = null;
		try
		{
			connFactory = new FitechConnection();
		
			conn = connFactory.getConn();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			
			// ɾ��ԭ������
			String tablename = "AF_QD_"+templateId.toUpperCase().trim();
			
			stmt.addBatch("delete from af_template_curr_relation where template_id='"+templateId+"' and version_id='"+versionId+"'" );
			stmt.addBatch("delete from af_template_freq_relation where template_id='"+templateId+"' and version_id='"+versionId+"'" );
			stmt.addBatch("delete from af_template_org_relation where template_id='"+templateId+"' and version_id='"+versionId+"'" );
			stmt.addBatch("delete from af_gatherformula where template_id='"+templateId+"' and version_id='"+versionId+"'" );
			stmt.addBatch("delete from af_validateformula where template_id='"+templateId+"' and version_id='"+versionId+"'" );
			stmt.addBatch("delete from af_cellinfo where template_id='"+templateId+"' and version_id='"+versionId+"'" );
			stmt.addBatch("delete from qd_cellinfo where template_id='"+templateId+"' and version_id='"+versionId+"'" );
			stmt.addBatch("delete from af_template where template_id='"+templateId+"' and version_id='"+versionId+"'" );
			stmt.addBatch("delete from af_report where template_id='"+templateId+"' and version_id='"+versionId+"'");
			stmt.addBatch("delete from AF_TEMPLATE_COLL_REP where template_id='"+templateId+"' and version_id='"+versionId+"'");
			stmt.addBatch("delete from AF_TEMPLATE_OUTER_REP where template_id='"+templateId+"' and version_id='"+versionId+"'");
			
			//ɾ�������ı�
			stmt.addBatch("drop table "+tablename);
			
			stmt.executeBatch();
			conn.commit();
			
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();			
			return false;
		}finally{
				try {
				if(stmt!=null)
					stmt.close();
				if (conn != null){
					conn.close();
				}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return true;
	}

	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * Ӱ�����AfTemplate
	 * ��ȡ�ӱ�����ϸ��Ϣ
	 * 
	 * @author rds
	 * @serialData 2005-12-19
	 * 
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @return MChildReportForm
	 */
	public static AFTemplateForm getTemplateInfo(String templateId, String versionId) {
		
		AFTemplateForm afTemplateForm = null;

		if (templateId == null || versionId == null)
			return null;

		DBConn conn = null;

		try {
			String hql = "from AfTemplate mcr where mcr.id.templateId='"
					+ templateId + "' and " + "mcr.id.versionId='" + versionId + "'";

			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0) {
				AfTemplate afTemplate = (AfTemplate) list.get(0);
				afTemplateForm = new AFTemplateForm();
				TranslatorUtil.copyPersistenceToVo(afTemplate,
						afTemplateForm);
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return afTemplateForm;
	}
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfTemplate
	 * new�����ӱ���id�Ͱ汾�ţ��жϸñ�����ʲô���͵ı�����Ե�ʽ or �嵥ʽ��
	 * 
	 * @author Ҧ��
	 * @param childReportId
	 *            String �Ա���id
	 * @param versionId
	 *            String �汾��
	 * @return 1 Config.REPORT_STYLE_DD ��Ե�ʽ 2 Config.REPORT_STYLE_QD �嵥ʽ
	 */
	public static int getReportStyle(String templateId, String versionId) {
		int reportStyle = 0;

		List retVals = null;
		DBConn conn = null;

		if (templateId != null && !templateId.equals("")
				&& versionId != null && !versionId.equals("")) {
			try {
				
				StringBuffer hql = new StringBuffer("select mcr.reportStyle from AfTemplate mcr where 1=1");
				
				hql.append(" and mcr.id.templateId = '" + templateId + "'");
				hql.append(" and mcr.id.versionId ='" + versionId + "'");

				conn = new DBConn();

				Session session = conn.openSession();
				Query query = session.createQuery(hql.toString());

				retVals = query.list();

				if (retVals != null && retVals.size() != 0) {
					reportStyle = ((Long) retVals.get(0)).intValue();
				}
			} catch (HibernateException he) {
				log.printStackTrace(he);
			} finally {
				if (conn != null)
					conn.closeSession();
			}
		}

		return reportStyle;
	}
	
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ���ݱ������ơ�������ñ������
	 * 
	 * @param title ��������
	 * @param subTitle �����ӱ���
	 * @param year �������
	 * @param term �����·�
	 * @return MChildReport �ӱ������
	 */
    public static AfTemplate findByTitle(String title,String subTitle,String date){
    	AfTemplate mcr = null;
        DBConn conn = null;
        Session session = null;
        
        try {
        	title = title +"-"+ subTitle;
        	String hql="";
        	/***���������ݿ��ж�*/
        	if(Config.DB_SERVER_TYPE.equals("sqlserver"))
        		hql= "from AfTemplate mcr where mcr.templateName='"+ title +"' " +
            			 "and convert(datetime,'" + date +" ',120) between convert(datetime,mcr.startDate,120) " +
            			 "and convert(datetime,mcr.endDate,120)";
        	if(Config.DB_SERVER_TYPE.equals("oracle"))
        		hql= "from AfTemplate mcr where mcr.templateName='"+ title +"' " +
            			 "and to_date('" + date +" ','yyyy-MM-dd') between to_date(mcr.startDate,'yyyy-MM-dd') " +
            			 "and to_date(mcr.endDate,'yyyy-MM-dd')";
        	if(Config.DB_SERVER_TYPE.equals("db2"))
        		hql= "from AfTemplate mcr where mcr.templateName='"+ title +"' " +
            			 "and date('" + date +" ') between date(mcr.startDate) " +
            			 "and date(mcr.endDate)";
            conn = new DBConn();
            session = conn.openSession();
            
            List list = session.createQuery(hql).list();
            if (list != null && list.size()==1) {
                 mcr= (AfTemplate) list.get(0);
            }
        } catch (HibernateException he) {
        	mcr = null;
            log.printStackTrace(he);            
        } catch(Exception e){
        	mcr = null;
            log.printStackTrace(e);
        } finally {
            if (conn != null)
                conn.closeSession();
        }
        return mcr;
    }
    
	/**
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * ��ȡ�ӱ�����ϸ��Ϣ
	 * 
	 * @author rds
	 * @serialData 2005-12-19
	 * 
	 * @param childRepId
	 *            String �ӱ���ID
	 * @param versionId
	 *            String �汾��
	 * @return MChildReportForm
	 */
	public static String getTemplateVersionId(String templateId) {
		
		

		if (StringUtil.isEmpty(templateId))
			return null;

		DBConn conn = null;

		try {
			String hql = "select mcr.id.versionId from AfTemplate mcr where mcr.id.templateId='"
					+ templateId + "' and mcr.usingFlag=1 order by mcr.id.versionId desc ";

			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0) {
				return (String) list.get(0);				
			}
		} catch (HibernateException he) {
			log.printStackTrace(he);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return null;
	}
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * @param templateId
	 * @param versionId
	 * @return
	 */
	public static Map getCurrById(String templateId,String versionId){
		if(StringUtil.isEmpty(templateId) || StringUtil.isEmpty(versionId)){
			return null;
		}
		Map mapkey = new HashMap();
		DBConn conn = null;
        Session session = null;
        
        try { 
        	
            String hql = "from AfTemplateCurrRelation t where t.id.templateId='"+templateId+"' and t.id.versionId='"+versionId+"'";            
            conn = new DBConn();
            session = conn.beginTransaction();
            
            List list = session.createQuery(hql).list();
            if (list != null && list.size()>0) {
            	for(int i=0;i<list.size();i++){
            		AfTemplateCurrRelation afCurr = (AfTemplateCurrRelation) list.get(i);
            		mapkey.put(String.valueOf(afCurr.getId().getCurId()), String.valueOf(afCurr.getId().getCurId()));
            	}
            	return mapkey;
            }
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if (conn != null)
                conn.closeSession();
        }
		
		return null;
	}
	
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfTemplate
	 * @param templateForm
	 */
	public static void resetVersion(AFTemplateForm templateForm) {
		DBConn conn = null;
        Session session = null;
        boolean result = false;
        try { 
        	
            String hql = "from AfTemplate t where t.id.templateId='"+templateForm.getTemplateId()+"' " +
            		"and t.id.versionId!='"+templateForm.getVersionId() +"' order by t.id.versionId desc";            
            conn = new DBConn();
            session = conn.beginTransaction();
            
            List list = session.createQuery(hql).list();
            if (list != null && list.size()>0) {
            	AfTemplate afCurr = (AfTemplate) list.get(0);
            	String startDate = templateForm.getStartDate();
            	String endDate = DateUtil.getLastMonth(startDate);
            	afCurr.setEndDate(endDate);
            	session.update(afCurr);
            	session.flush();
            }
            result = true;
        } catch(Exception e){
            log.printStackTrace(e);
        } finally {
            if(conn!=null)
                conn.endTransaction(result);
        }
	}
	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfTemplate
	 * @param templateName
	 * @param templateType
	 * @param reportFlg
	 * @return
	 */
	public static List selectAllTemplate(String templateName,String templateType,String reportFlg) {
		DBConn conn = null;
        Session session = null;

        try { 
	
        	String hql=" from AfTemplate t where t.templateType='"+reportFlg+"'";
			if (templateName != null && !templateName.equals("")){
				hql+=" and t.templateName like'%" + templateName.trim() + "%'";
			}
			
			if (templateType != null && !templateType.equals("")){
				hql+=" and t.bak1 in (" + templateType.trim() + ")";
			}

			hql+=" order by t.id.templateId";
			conn = new DBConn();
            session = conn.beginTransaction();
            
           return session.createQuery(hql).list();
	 
        } catch(Exception e){
            log.printStackTrace(e);
            return null;
        } finally {
            if (conn != null)
                conn.closeSession();
        }
	}
	
	public static List<AfTemplate> findGatherReport(AfTemplate af){
		DBConn conn = null;
        Session session = null;
        
        try { 
        	
        	String hql=" from AfTemplate t where 1=1 " ;
        	if(af.getTemplateType()!=null && !af.getTemplateType().equals(""))
        		hql+=" and t.templateType='"+af.getTemplateType()+"'";
        	if(af.getIsReport()!=null && !af.getIsReport().equals("")){
        		hql += " and t.isReport='"+af.getIsReport()+"'";
        	}
			hql+=" order by t.id.templateId";
			conn = new DBConn();
            session = conn.beginTransaction();
            
           return session.createQuery(hql).list();
	 
        } catch(Exception e){
            log.printStackTrace(e);
            return null;
        } finally {
            if (conn != null)
                conn.closeSession();
        }
	}
}
