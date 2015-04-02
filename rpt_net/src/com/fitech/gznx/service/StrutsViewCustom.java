package com.fitech.gznx.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.security.Operator;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.common.TreeContentBuilder;
import com.fitech.gznx.common.TreeNode;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.form.CustomViewForm;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;


public class StrutsViewCustom  extends DaoModel{


	/**
     * 查找数据仓库报表信息列表
     * @return
     * @throws Exception
     */
	public static List findReportCur(String templateId,String versionId) throws Exception{

	       List resuList=null;
	       DBConn conn = null;
	       Session session = null;
	 		try
	 		{	 			
 				conn = new DBConn();
    			session = conn.beginTransaction();
	 			
	 			String sql = "select t.id.curId ,m.curName  from AfTemplateCurrRelation t,MCurr m where " +
	 					"t.id.curId=m.curId and t.id.templateId='"+templateId+
	 			"' and t.id.versionId="+versionId +" order by t.id.curId";
	 			resuList = session.createQuery(sql).list();	 					
			}catch(Exception e){
				resuList = null;			
				e.printStackTrace();		
			}finally{
				if (conn != null)
					conn.closeSession();
			}
	 		return resuList;
		
	}

	public static List findReportFreq(String templateId, String versionId,
			String reportFlg) {

	       List resuList=null;
	       DBConn conn = null;
	       Session session = null;
	 		try
	 		{	 			
				conn = new DBConn();
				session = conn.beginTransaction();
				String sql= "";
	 			if(!reportFlg.equals("1")){
	 				sql = "select t.id.repFreqId ,m.repFreqName  from AfTemplateFreqRelation t,MRepFreq m where " +
 					"t.id.repFreqId=m.repFreqId and t.id.templateId='"+templateId+
 					"' and t.id.versionId="+versionId+" order by t.id.repFreqId";
	 			}else {
	 				sql = "select t.comp_id.repFreqId ,m.repFreqName  from MActuRep t,MRepFreq m where " +
 					"t.comp_id.repFreqId=m.repFreqId and t.comp_id.childRepId='"+templateId+
 					"' and t.comp_id.versionId="+versionId+" order by t.comp_id.repFreqId";
	 			}
	 			
	 			resuList = session.createQuery(sql).list();	 					
			}catch(Exception e){
				resuList = null;	
				e.printStackTrace();
			}finally{
				if (conn != null)
					conn.closeSession();
			}
	 		return resuList;
		
	}
	
	/***
	 * 已使用Hibernate 卞以刚 2011-12-22
	 * 影响对象：AfTemplateOrgRelation ||MRepRange ,AfOrg
	 * @param templateId
	 * @param versionId
	 * @param reportFlg
	 * @param operatorId
	 * @return
	 */
	public static Map findReportOrg(String templateId, String versionId,
			String reportFlg,Long operatorId) {
			Map orgmap = new HashMap();
	       DBConn conn = null;
	       Session session = null;
	       /**已使用Hibernate 卞以刚 2011-12-22**/
	       Map viewOrgIds = findViewOrgIds(templateId,operatorId);
	 		try
	 		{
				conn = new DBConn();
				session = conn.beginTransaction();
				String sql= "";
	 			if(!reportFlg.equals("1")){
	 				sql = "select t.id.orgId ,m.orgName  from AfTemplateOrgRelation t,AfOrg m where " +
					"t.id.orgId=m.orgId and t.id.templateId='"+templateId+
					"' and t.id.versionId="+versionId;
	 			}else {
	 				sql = "select t.comp_id.orgId ,m.orgName  from MRepRange t,AfOrg m where " +
					"t.comp_id.orgId=m.orgId and t.comp_id.childRepId='"+templateId+
					"' and t.comp_id.versionId="+versionId;
	 			}
	 			
	 			List<Object[]> orgList = session.createQuery(sql).list();	
	 			
	 			if(orgList != null && orgList.size()>0){
		 			for(Object[] reportForm:orgList){		 				
		 				if(reportForm != null && viewOrgIds.containsKey(String.valueOf(reportForm[0])) && !orgmap.containsKey(String.valueOf(reportForm[0]))){		 					
		 					orgmap.put(String.valueOf(reportForm[0]), String.valueOf(reportForm[1]));
		 				}
		 			}
	 			}
	 			
	 			return orgmap;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}finally{
				if (conn != null)
					conn.closeSession();
			}
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：OrgNet AfTemplateOrgRelation ||MRepRange ,AfOrg || OrgNet
	 * @param session
	 * @param fileName
	 * @param templateId
	 * @param versionId
	 */
	public static void createOrgTree(HttpSession session,String fileName,String templateId,String versionId){
		Operator operator = (Operator) session
		.getAttribute(com.cbrc.smis.common.Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		// 取得机构Id
		String orgId = operator.getOrgId();
		// 根据机构ID,取得机构名称;
		/**已使用hibernate 卞以刚 2011-12-22
		 * 影响对象：OrgNet **/
		String orgName = StrutsOrgNetDelegate.getOrgName(orgId);
		/** 报表选中标志 **/
		String reportFlg = "0";
		if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG);
		}
		
		/**已使用hibernate 卞以刚 2011-12-22
		 * 影响对象：AfTemplateOrgRelation ||MRepRange ,AfOrg**/
		Map orgMap = findReportOrg(templateId,versionId,reportFlg,operator.getOperatorId());
		
		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		document.setXMLEncoding("GB2312");

		Element rootElement = document.addElement("tree");
		rootElement.addAttribute("id", "0");

		Element oneElement = rootElement.addElement("item");
		oneElement.addAttribute("text", orgName);
		oneElement.addAttribute("id", orgId);
		oneElement.addAttribute("open", "1");
		oneElement.addAttribute("checked", "1");
		/**已使用hibernate 卞以刚 2011-12-27
		 * 已影响对象：AfOrg || OrgNet*/
		getNode(orgId, session,orgMap, oneElement,reportFlg);
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GB2312");
			XMLWriter output = new XMLWriter();
			output = new XMLWriter(new FileOutputStream(fileName), format);
			output.write(document);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private static boolean flag = true;
	private static void addChild(Element e, OrgNet orgNet, HttpSession session,Map orgMap) {
		if (orgNet != null) {
			String id = orgNet.getOrgId();
			Element element = e;
			if (flag) {
				element = e.addElement("item");
				element.addAttribute("text", orgNet.getOrgName());
				element.addAttribute("id", orgNet.getOrgId());
				if (orgNet.getPreOrgId().equals("true")) {
					element.addAttribute("checked", "1");
					element.addAttribute("open", "1");
				}

			}
			List childList = StrutsOrgNetDelegate.selectLowerOrgList(id,
					session);

			if (childList != null && !childList.equals("")) {
				for (int i = 0; i < childList.size(); i++) {
					OrgNet o = (OrgNet) childList.get(i);
					if(!orgMap.containsKey(o.getOrgId())){
						continue;
					}
					flag = false;
					Element child = element.addElement("item");
					child.addAttribute("text", o.getOrgName());
					child.addAttribute("id", o.getOrgId());
					if (o.getPreOrgId().equals("true")) {
						child.addAttribute("checked", "1");
						child.addAttribute("open", "1");
					}
					addChild(child, o, session,orgMap);
				}
				flag = true;
			}
		}
	}
	
	/**
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：AfOrg || OrgNet
	 * @param orgId
	 * @param session
	 * @param orgMap
	 * @param rootElement
	 * @param reportFlg
	 */
	private static void getNode(String orgId, HttpSession session,Map orgMap, Element rootElement, String reportFlg) {
		List lowerOrgList = null;
		if(reportFlg.equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
			/**已使用Hibernate 卞以刚 2011-12-22
			 * 影响对象：AfOrg**/
			lowerOrgList = AFOrgDelegate.selectLowerOrgList(orgId,
					session);	
		}else{
			/**已使用Hibernate 卞以刚 2011-12-22
			 * 影响对象： OrgNet**/
			lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId,
					session);
		}
		
		if (lowerOrgList != null && !lowerOrgList.equals("")) {
			for (int i = 0; i < lowerOrgList.size(); i++) {
				OrgNet o = (OrgNet) lowerOrgList.get(i);
				try {
					if(orgMap.containsKey(o.getOrgId()))
					addChild(rootElement, o, session,orgMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	/***
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：ViewOrgRep
	 * @param templateId
	 * @param operatorId
	 * @return
	 */
	public static Map findViewOrgIds(String templateId, Long operatorId) {
			Map map = new HashMap();
	       DBConn conn = null;
	       Session session = null;
	 		try
	 		{
				conn = new DBConn();
				session = conn.beginTransaction();
				String sql= "select t.orgId from ViewOrgRep t where t.powType="
					+com.cbrc.smis.common.Config.POWERTYPESEARCH+" and t.userId="+operatorId+"and t.childRepId='"+templateId+"'";
				List<String> resuList = session.createQuery(sql).list();
				for(String orgId:resuList){
					
					map.put(orgId, orgId);					
				}
			}catch(Exception e){
				map = null;	
				e.printStackTrace();
			}finally{
				if (conn != null)
					conn.closeSession();
			}
	 		return map;
		
	}
	public static List findReportCell(String templateId, String versionId,
			String reportFlg) {

	       List resuList=null;
	       DBConn conn = null;
	       Session session = null;
	 		try
	 		{
				conn = new DBConn();
				session = conn.beginTransaction();
				String sql= "";
	 			if(!reportFlg.equals("1")){
	 				sql = "select t.cellId ,t.rowName,t.colName,t.cellPid  from AfCellinfo t where " +
					" t.templateId='"+templateId+
					"' and t.versionId="+versionId+" and (t.colName not like '上期%' or t.colName is null )order by t.cellId";
	 			}else {
	 				sql = "select m.cellId ,t.rowName,t.colName,t.cell_pid  from AfCellinfo t,MCell m where " +
					"t.templateId=m.MChildReport.comp_id.childRepId and t.versionId=m.MChildReport.comp_id.versionId" +
					" and t.rowNum=m.rowId and t.colNum=m.colId and  t.templateId='"+templateId+
					"' and t.versionId="+versionId+" and (t.colName not like '上期%' or t.colName is null ) order by t.cellId";
	 			}
	 			
	 			resuList = session.createQuery(sql).list();	
			}catch(Exception e){
				resuList = null;	
				e.printStackTrace();
			}finally{
				if (conn != null)
					conn.closeSession();
			}
	 		return resuList;
		
	}
	/*
	 * 取得灵活查询结果
	 */
	public static List getCustomViewList(CustomViewForm customViewForm){
		List list = new ArrayList();
		DBConn conn = null;
	    Session session = null;

	    if(customViewForm != null){
		try{
			String templateId = customViewForm.getTemplateId();
			String versionId = customViewForm.getVersionId();
			String cellIds = customViewForm.getMeaStr();
			String startDate = customViewForm.getStartDate();
			String endDate = customViewForm.getEndDate();
			String orgId = null;
			String curId = customViewForm.getCurId();
			String repFreqId = customViewForm.getRepFreqId();
			String reportFlg = customViewForm.getReportFlg();
			if(customViewForm.getUserList() == null){
				return null;
			}
			for(String orgids:customViewForm.getUserList()){
				orgId = orgId==null?orgids:(orgId+","+orgids);
			}
			
			conn = new DBConn();
			session = conn.beginTransaction();
			StringBuffer sql = new StringBuffer();
			if(reportFlg.equals(Config.CBRC_REPORT)){
				String datarangeId = getdatarangeId(templateId,templateId,repFreqId);
				if(StringUtil.isEmpty(datarangeId)){
					datarangeId = "1";
				}
				sql.append("select mc.cellId,c.rowName,c.colName,o.orgName,m.curName,r.repFreqName,d.reportValue,t.year,t.term from ReportIn t,AfCellinfo c,MCell mc,AfOrg o,MCurr m,MRepFreq r,ReportInInfo d where t.MChildReport.comp_id.childRepId='").append(templateId)
				.append("' and t.MChildReport.comp_id.versionId=").append(versionId).append(" and t.MChildReport.comp_id.childRepId=c.templateId and t.MChildReport.comp_id.versionId=c.versionId and ")
				.append(" c.templateId=mc.MChildReport.comp_id.childRepId and c.versionId=mc.MChildReport.comp_id.versionId and c.rowNum=mc.rowId and c.colNum=mc.colId and ")
				.append(" mc.cellId in (").append(cellIds).append(")").append(" and t.orgId in (").append(orgId)
				.append(") and t.MCurr.curId=").append(curId).append(" and t.MDataRgType.dataRangeId=").append(datarangeId)
				.append(" and d.comp_id.repInId=t.repInId and d.comp_id.cellId=mc.cellId and CONCAT(CONCAT(CONCAT(t.year,'-'),CONCAT(t.term,'-')),'01') between '")
				.append(startDate).append("' and '").append(endDate).append("' and o.orgId=t.orgId and m.curId=t.MCurr.curId ")
				.append(" and r.repFreqId=").append(repFreqId);
				
			} else if(reportFlg.equals(Config.PBOC_REPORT)){
				sql.append("select c.cellId,c.rowName,c.colName,o.orgName,m.curName,r.repFreqName,d.cellData,t.year,t.term,t.day from AfReport t,AfCellinfo c,AfOrg o,MCurr m,MRepFreq r,AfPbocreportdata d where t.templateId='").append(templateId)
				.append("' and t.versionId=").append(versionId).append(" and t.templateId=c.templateId and t.versionId=c.versionId and ")
				.append(" c.cellId in (").append(cellIds).append(")").append(" and t.orgId in (").append(orgId)
				.append(") and t.curId=").append(curId).append(" and t.repFreqId=").append(repFreqId)
				.append(" and d.id.repId=t.repId and d.id.cellId=c.cellId and CONCAT(CONCAT(CONCAT(t.year,'-'),CONCAT(t.term,'-')),t.day) between '")
				.append(startDate).append("' and '").append(endDate).append("' and o.orgId=t.orgId and m.curId=t.curId ")
				.append(" and r.repFreqId=t.repFreqId ");
			} else if(reportFlg.equals(Config.OTHER_REPORT)){
				sql.append("select c.cellId,c.rowName,c.colName,o.orgName,m.curName,r.repFreqName,d.cellData,t.year,t.term,t.day from AfReport t,AfCellinfo c,AfOrg o,MCurr m,MRepFreq r,AfOtherreportdata d where t.templateId='").append(templateId)
				.append("' and t.versionId=").append(versionId).append(" and t.templateId=c.templateId and t.versionId=c.versionId and ")
				.append(" c.cellId in (").append(cellIds).append(")").append(" and t.orgId in (").append(orgId)
				.append(") and t.curId=").append(curId).append(" and t.repFreqId=").append(repFreqId)
				.append(" and d.id.repId=t.repId and d.id.cellId=c.cellId and CONCAT(CONCAT(CONCAT(t.year,'-'),CONCAT(t.term,'-')),t.day) between '")
				.append(startDate).append("' and '").append(endDate).append("' and o.orgId=t.orgId and m.curId=t.curId ")
				.append(" and r.repFreqId=t.repFreqId");
			}
			List<Object[]> customList = session.createQuery(sql.toString()).list();	
			if(customList != null && customList.size()>0){
				for(Object[] custom:customList){
					Map map = new HashMap();
					map.put("cellId", custom[0]);
					String lname="";
					if(custom[1] != null){
						lname=(String)custom[1]+"-"+custom[2];
					}else{
						lname=(String)custom[2];
					}
					map.put("cellName", lname);
					map.put("orgName", custom[3]);
					map.put("curName", custom[4]);
					map.put("repFreqName", custom[5]);
					
					map.put("cellData", custom[6]);
					String celldate = "";
					if(reportFlg.equals(Config.CBRC_REPORT)){
						celldate = String.valueOf(custom[7])+"-"+String.valueOf(custom[8]);
					}else{
					celldate = String.valueOf(custom[7])+"-"+String.valueOf(custom[8])+"-"+String.valueOf(custom[9]);
					}
					map.put("celldate", celldate);
	
					list.add(map);
				}
			}
		}catch (Exception e){
			list = null;	
			e.printStackTrace();
		}finally{
			if (conn != null)
				conn.closeSession();
		}
	    }
		return list;
	}
	
	/***
	 * 已使用hibernate  卞以刚 2011-12-21
	 * 影响对象：MActuRep
	 * @param childrepid
	 * @param versionid
	 * @param repFreqId
	 * @return
	 */
	public static String getdatarangeId(String childrepid, String versionid,String repFreqId) {
		String datarangeId = "";
		if (childrepid == null || repFreqId == null || versionid == null){
			return datarangeId;
		}
			
		DBConn conn = null;
		try {
			String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"
					+ childrepid + "'" + " and mcr.comp_id.repFreqId="
					+ repFreqId + " and mcr.comp_id.versionId='" + versionid 
					+ "' ";
			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0) {
				MActuRep mar = (MActuRep) list.get(0);
				datarangeId = String.valueOf(mar.getComp_id().getDataRangeId());
			}
		} catch (Exception e) {
			e.printStackTrace();			
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return datarangeId;
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-21
	 * @param childrepid
	 * @param versionid
	 * @param dataRangeId
	 * @return
	 */
	public static String getRepFreqId(String childrepid, String versionid,String dataRangeId) {
		String repFreqId = "";
		if (childrepid == null || dataRangeId == null || versionid == null){
			return repFreqId;
		}
			
		DBConn conn = null;
		try {
			String hql = "from MActuRep mcr where mcr.comp_id.childRepId='"
					+ childrepid + "'" + " and mcr.comp_id.dataRangeId="
					+ dataRangeId + " and mcr.comp_id.versionId='" + versionid 
					+ "' ";
			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0) {
				MActuRep mar = (MActuRep) list.get(0);
				repFreqId = String.valueOf(mar.getComp_id().getRepFreqId());
			}
		} catch (Exception e) {
			e.printStackTrace();			
		} finally {
			if (conn != null)
				conn.closeSession();
		}
		return repFreqId;
	}

}
