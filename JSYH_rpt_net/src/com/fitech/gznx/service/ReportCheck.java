package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.po.AfCodelib;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateId;

public class ReportCheck extends DaoModel{


	private static FitechException log = new FitechException(ReportCheck.class); 

	/**
	 * 取得所有报表（不分版本号）
	 * 
	 * @author 姚捷
	 * @return List 所有报表
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


	private static AfTemplate getTemplate(String templateId, String versionId) {
		// TODO Auto-generated method stub
		boolean result = false;
	    DBConn conn =null;
	    Session session =null;
	    AfTemplate template = new AfTemplate();
	    if(templateId!=null && versionId!=null)
	    {
	        try
	        {
	            conn = new DBConn();
	            session = conn.beginTransaction(); 
				AfTemplateId id = new AfTemplateId();
				id.setTemplateId(templateId);
				id.setVersionId(versionId);
//					AfTemplate template = new AfTemplate();
//					template.setId(id);
				template = (AfTemplate)session.load(AfTemplate.class, id);
	        }catch(Exception e)
	        {
	            log.printStackTrace(e);
	            result = false;
	        }
	        finally{
	        	try {
					session.close();
				} catch (HibernateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            if(conn!=null)
	                conn.endTransaction(result);
	        }
	       
	    }
		
		return template;
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：AfTemplate
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
						hql+=" and t.templateName like '%" + reportName.trim() + "%'";
					}
					String templateType = templateForm.getTemplateType();
					if (templateType != null && !templateType.equals("")){
						hql+=" and t.bak1 in (" + templateType + ")";
					}
				}
			 /**order by和聚合函数出错 已经注释 卞以刚 2011-12-28
			  * 已增加数据库判断*/
			 if(Config.DB_SERVER_TYPE.equals("oracle"))
				 hql+=" order by t.id.templateId";
			 if(Config.DB_SERVER_TYPE.equals("sqlserver"))
				 hql+="";
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
			//	form.setTemplateType(getTemplateType(template.getBak1(),reportFlg));
				
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
//	private static String getTemplateType(String bak1, String reportFlg) {
//		return AFTemplateTypeDelegate.getTemplateTypeName(bak1, reportFlg);	
////		String codeTypeId = null;
////		if(reportFlg.equals("1")){
////			codeTypeId = "140";
////		}
////		if(reportFlg.equals("2")){
////			codeTypeId = Config.RHTEMPLATE_TYPE;
////		} else if(reportFlg.equals("3")){
////			codeTypeId = Config.QTTEMPLATE_TYPE;
////		}
////		AfCodelib codelib = StrutsCodeLibDelegate.getCodeLib(codeTypeId, bak1);
////		if(codelib != null){
////			return codelib.getCodeName();	
////		} else {
////			return null;
////		}
//		
//	}

}
