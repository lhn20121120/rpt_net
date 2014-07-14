package com.fitech.gznx.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ReportInForm;
import com.fitech.gznx.action.ExportPbocAFReportAction;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.RhReportForm;
import com.fitech.gznx.po.AfPboccell;
import com.fitech.gznx.po.AfPboccellId;
import com.fitech.gznx.po.AfPbocreportdata;

public class AfPboccellDelegate {

	public static List<RhReportForm> getPbocCell(RhReportForm from,String deleteIndex){
		
		DBConn conn =null;
		List<RhReportForm> pList = new ArrayList();
		String templateId  = from.getTemplateId();
        String versionId = from.getVersionId();
	    if(from != null)
	    {
	        try
	        {
	            conn = new DBConn();
	            
	            String hql=" from AfPboccell t where t.id.templateId='"+templateId+"' and " +
	            		"t.id.versionId="+versionId;
	            List list = conn.openSession().find(hql);
	            if (list != null && list.size() >= 1) {
	            	for(int i=0;i<list.size();i++){
	            		RhReportForm reportfrom = new RhReportForm();
	            		AfPboccell cell = (AfPboccell) list.get(i);
	            		reportfrom.setTemplateId(templateId);
	            		reportfrom.setVersionId(versionId);
	            		reportfrom.setColId(cell.getId().getColId());
	            		reportfrom.setCurId(cell.getCurId());
	            		reportfrom.setDataType(cell.getDataType());
	            		reportfrom.setDanweiId(cell.getDanweiId());
	            		reportfrom.setPsuziType(cell.getPsuziType());
	            		reportfrom.setColName(StrutsAFCellDelegate.getColNamebyId(templateId,versionId,reportfrom.getColId()));
	            		reportfrom.setCurName(StrutsCodeLibDelegate.getCodeLibbybak(Config.CURID_TYPE,cell.getCurId()).getCodeName());
	            		reportfrom.setDanweiName(StrutsCodeLibDelegate.getCodeLib(Config.DANWEIID_TYPE,cell.getDanweiId()).getCodeName());
	            		reportfrom.setPsuziTypeName(StrutsCodeLibDelegate.getCodeLib(Config.PSUZITYPE_TYPE,cell.getPsuziType()).getCodeName());
	            		reportfrom.setDataTypeName(StrutsCodeLibDelegate.getCodeLib(Config.DATATYPE_TYPE,cell.getDataType()).getCodeName());
	            		reportfrom.setFlag("0");
	            		
	            		pList.add(reportfrom);
	            	}
	            }
	            
	        } catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally{
	        	if(conn != null) 
	    			conn.closeSession();
	        }
	    }
	    try{
	    if(!StringUtil.isEmpty(deleteIndex) && Integer.parseInt(deleteIndex)>=0){
	    	pList.get(Integer.parseInt(deleteIndex)).setFlag("1");
	    } else if(!StringUtil.isEmpty(deleteIndex) && Integer.parseInt(deleteIndex)==-1){
	    	RhReportForm reportfrom = new RhReportForm();
	    	reportfrom.setTemplateId(templateId);
    		reportfrom.setVersionId(versionId);
    		reportfrom.setFlag("1");
    		pList.add(reportfrom);
	    }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		
		return pList;
	}

	public static boolean save(RhReportForm reportForm) {
		  boolean result = false;
	      DBConn conn =null;
	      Session session =null;
	      if(reportForm!=null)
	      {
	          try
	          {
	              conn = new DBConn();
	              session = conn.beginTransaction(); 
	              AfPboccellId id = new AfPboccellId();
	              id.setColId(reportForm.getColId());
	              id.setTemplateId(reportForm.getTemplateId());
	              id.setVersionId(reportForm.getVersionId());
	              AfPboccell pcell = null;
	              try{
	            	  pcell =(AfPboccell) session.load(AfPboccell.class,id);
	              }catch (Exception e){
	            	  pcell=null;
	              }
	              
	              if(pcell != null){
	            	  pcell.setCurId(reportForm.getCurId());
	            	  pcell.setDanweiId(reportForm.getDanweiId());
	            	  pcell.setDataType(reportForm.getDataType());
	            	  pcell.setPsuziType(reportForm.getPsuziType());
	            	  session.update(pcell);
	              }else{
	            	  AfPboccell cell = new AfPboccell();
		              cell.setId(id);
		              cell.setCurId(reportForm.getCurId());
		              cell.setDanweiId(reportForm.getDanweiId());
		              cell.setDataType(reportForm.getDataType());
		              cell.setPsuziType(reportForm.getPsuziType());
	            	  session.save(cell);
	              }
	              
	              session.flush();
	              result = true;
	          }
	          catch(Exception e)
	          {
	        	  e.printStackTrace();
	              result = false;
	          }
	          finally{
	              if(conn!=null)
	                  conn.endTransaction(result);
	          }
	      }
		return result;
	}

	public static boolean deleteCell(RhReportForm reportForm) {
		  boolean result = false;
	      DBConn conn =null;
	      Session session =null;
	      if(reportForm!=null)
	      {
	          try
	          {
	              conn = new DBConn();
	              session = conn.beginTransaction(); 
	              AfPboccellId id = new AfPboccellId();
	              id.setColId(reportForm.getColId());
	              id.setTemplateId(reportForm.getTemplateId());
	              id.setVersionId(reportForm.getVersionId());
	              
	              AfPboccell cell = new AfPboccell();
	              cell.setId(id);
	              
	              session.delete(cell);
	              session.flush();
	              result = true;
	          }
	          catch(Exception e)
	          {
	        	  e.printStackTrace();
	              result = false;
	          }
	          finally{
	              if(conn!=null)
	                  conn.endTransaction(result);
	          }
	      }
		return result;
	}
	
	/**已使用Hibernate 卞以刚 2011-12-27
	 * 影响对象：AfPboccell**/
	public static  List getAFPbocCell(String templateId,  String versionId) {
		List pList = null;
        DBConn conn =null;
	    if(!StringUtil.isEmpty(templateId) && !StringUtil.isEmpty(versionId) )
	    {
	        try
	        {
	            conn = new DBConn();
	            
	            String hql=" from AfPboccell t where t.id.templateId='"+templateId+"' and " +
	            		"t.id.versionId="+versionId;
	            pList = conn.openSession().find(hql);
	        } catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally{
	        	if(conn != null) 
	    			conn.closeSession();
	        }
	    }
	  
		
		return pList;
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：AfPbocreportdata
	 * @param reportInForm
	 * @param colId
	 * @return
	 */
	public static  boolean isAFPbocCell(ReportInForm reportInForm,String colId) {
		boolean result = false;
        DBConn conn =null;
        Session session = null;
	    if(reportInForm != null && !StringUtil.isEmpty(colId) )
	    {
	        try
	        {
	            conn = new DBConn();
	            session = conn.openSession();
	            String hql=" from AfPbocreportdata t where t.id.repId="+reportInForm.getRepInId()+" and " +
	            		"t.id.cellId in (select c.cellId from AfCellinfo c where c.colNum='"+colId+"'" +
	            				" and c.templateId='"+reportInForm.getChildRepId()+"'" +
	            						" and c.versionId='"+reportInForm.getVersionId()+"')";
	           List<AfPbocreportdata> pList = session.createQuery(hql).list();
	           
	           int zero = 0;
	           int allowMath = 0;
	           
	          
				
	           if(pList != null && pList.size()>0){
	        	   
	        	   for(AfPbocreportdata ap:pList){
	        		   /***
						 * 20120116修改，该处数据精度已配置文件
						 * application.properties中double.precision的值为准------------------开始
						 */
	        		   String cellData = String.format("%1$."+Config.DOUBLEPERCISION+"f", Double.valueOf(ap.getCellData()));
	        		   
	        		   if(cellData.equals(ExportPbocAFReportAction.ZEROS)){//此处判断是否为0
	        			   zero++;
	        		   }else{
	        			   allowMath++; 
	        		   }
	        		   /***
						 * 20120116修改，该处数据精度已配置文件
						 * application.properties中double.precision的值为准------------------结束
						 */
	        	   }
	        	   if(allowMath>0)
	        		   result = true;
	        	   else
	        		   result = false;
	           }
	        } catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally{
	        	if(conn != null) 
	    			conn.closeSession();
	        }
	    }
	  
		
		return result;
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：AfCellinfo
	 * @param reportInForm
	 * @param colId
	 * @return
	 */
	public static  List getAFPbocCellList(ReportInForm reportInForm,String colId) {
		List pList = null;
        DBConn conn =null;
	    if(reportInForm != null && !StringUtil.isEmpty(colId) )
	    {
	        try
	        {
	            conn = new DBConn();
	            
	            String hql="  from AfCellinfo c where c.colNum='"+colId+"'" +
	            				" and c.templateId='"+reportInForm.getChildRepId()+"'" +
	            						" and c.versionId='"+reportInForm.getVersionId()+"'" +
	            						" order by c.cellId";
	            pList = conn.openSession().find(hql);
	           
	        } catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally{
	        	if(conn != null) 
	    			conn.closeSession();
	        }
	    }
	  
		
		return pList;
	}
	
	/***
	 * 已使用hibernate 卞以刚 2011-12-27
	 * 影响对象：AfPbocreportdata 
	 * @param reportInForm
	 * @param celllId
	 * @return
	 */
	public static  String getAFPbocCellData(ReportInForm reportInForm,Long celllId) {
		List pList = null;
		String cellData = "";
        DBConn conn =null;
	    if(reportInForm != null && celllId != null )
	    {
	        try
	        {
	            conn = new DBConn();
	            
	            String hql="from AfPbocreportdata t where t.id.repId="+reportInForm.getRepInId()+" and " +
	            		"t.id.cellId="+celllId;
	            pList = conn.openSession().find(hql);
	            if(pList != null && pList.size()>0){
	            	AfPbocreportdata data = (AfPbocreportdata) pList.get(0);
	            	cellData = data.getCellData();
	            }
	           
	        } catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally{
	        	if(conn != null) 
	    			conn.closeSession();
	        }
	    }		
		return cellData;
	}
	
	/**
	 * 根据人行报表ID查询此报表所有单元格的数据，查询结果以键值对存放
	 */
	public static Map getAFPbocCellDataMap(ReportInForm reportInForm) {
		Map result=new Hashtable();
        DBConn conn =null;
	    if(reportInForm != null)
	    {	
			AfPbocreportdata data=null;
	        try
	        {
	            conn = new DBConn();
	            
	            String hql="from AfPbocreportdata t where t.id.repId="+reportInForm.getRepInId();
	            List pList = conn.openSession().find(hql);//查询单张人行报表的数据
	            if(pList != null && pList.size()>0){
	            	for (int i = 0; i < pList.size(); i++) {
	            		data = (AfPbocreportdata) pList.get(i);
	            		result.put(data.getId().getCellId(), data.getCellData());//单元格数据以键值对的形式放入Hashtable中
					}
	            	
	            }
	           
	        } catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally{
	        	if(conn != null) 
	    			conn.closeSession();
	        }
	    }		
		return result;
	}
}
