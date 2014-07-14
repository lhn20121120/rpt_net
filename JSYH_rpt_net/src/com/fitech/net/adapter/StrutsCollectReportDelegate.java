package com.fitech.net.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MCurrForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MActuRep;
import com.cbrc.smis.hibernate.MChildReport;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.hibernate.MRepRange;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.net.collect.bean.CollectReport;
import com.fitech.net.collect.bean.CollectReportPK;
import com.fitech.net.common.MCurrUtil;

/**
*
* <p>Title: StrutsCollectReportDelegate </p>
*
* <p>Description 汇总情况数据库操作代理类</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2009-09-14
* @version 1.0
*/

public class StrutsCollectReportDelegate {

    private static FitechException log=new FitechException(StrutsCollectReportDelegate.class);
    
    public StrutsCollectReportDelegate() {
          }

   /**
    * 查询一个机构的所有的某一期的最近的一次汇总情况
    * @param orgId      String      机构 Id
    * @param year       String      年份
    * @param term       String      期数
    * @return   List
    */
    public static List findCollectReports(String orgId,String year,String term){
        List list = null;
        DBConn conn=null;
        Session session=null;
        
        if(orgId == null || year== null || term == null) return null;
        
        String hql = "from CollectReport t where t.id.orgId='"+ orgId+"'";
                hql +=" and t.id.year = "+ year;
                hql +=" and t.id.term = " + term;
        try{
            conn = new DBConn();
            session = conn.openSession();
            list = session.createQuery(hql).list();
        }catch(Exception he){
           log.printStackTrace(he);
        }finally{              
            if (conn!=null) conn.closeSession();         
        }
        return list;
    }
 
    /**
     * 根据主键查找汇总情况
     * @param id        CollectReportPK     上报汇总 Id
     * @return
     */
    public static CollectReport findById(CollectReportPK id)
    {
        CollectReport report= null;
       
        DBConn conn=null;
        Session session=null;
        
        if(id == null) return null;
        if(id.getOrgId() == null) return null;
        if(id.getTerm() == null || id.getYear() == null) return null;
        if(id.getVersionId() == null || id.getChildRepId() == null) return null;
        
        try{
            
           /* String hql = "from CollectReport t where t.id.orgId = '"+id.getOrgId() +"'";
                    hql += " and t.id.term=" + id.getTerm();
                    hql += " and t.id.year=" + id.getYear();
                    hql += " and t.id.versionId='"+id.getVersionId() + "'";
                    hql += " and t.id.childRepId='"+id.getChildRepId() + "'";
            conn = new DBConn();
            session = conn.openSession();
            report = (CollectReport)session.createQuery(hql).uniqueResult();*/
            report = (CollectReport)session.get(CollectReport.class,id);
        }catch(Exception he){
           log.printStackTrace(he);
        }finally{              
            if (conn!=null) conn.closeSession();         
        }
        return report;
    }
    
    /**
     * 疑是oracle语法(upper) 需要修改 卞以刚 2011-12-22
     * 根据年和期数 查该机构所需汇总的报表信息
     * 
     * @param reportInForm 
     * @param operator
     * @return   报表信息的列表
     */
     public static List selectAllCollectReports(ReportInForm reportInForm,Operator operator){
     	List rList = null;
     	List resList = null;
     	DBConn conn = null;
     	Session session = null;
     	
     	if(reportInForm == null || operator == null) 
     		return resList;
     	
     	try{
     		conn = new DBConn();
 			session = conn.beginTransaction();
 			
 			int term = reportInForm.getTerm().intValue();
 			int year = reportInForm.getYear().intValue();
 			
 			Calendar calendar = Calendar.getInstance();
 			calendar.set(year,term-1,1);
 			
 			String rep_freq="";
 			if(term == 12)
 				rep_freq = "('月','季','半年','年')";
 			else if(term == 6)
 				rep_freq = "('月','季','半年')";
 			else if(term == 3 || term == 9)
 				rep_freq = "('月','季')";
 			else rep_freq = "('月')";
 			    			
 			String hql = "from MRepFreq mrf where mrf.repFreqName in " + rep_freq;
 			List rep_freqList = session.createQuery(hql).list();
 			
 			if(rep_freqList == null && rep_freqList.size() ==0)
 				return null;
 			
 			rep_freq = "";
 			for(int i=0;i<rep_freqList.size();i++){
 				MRepFreq mrepFreq = (MRepFreq)rep_freqList.get(i);
 				rep_freq += mrepFreq.getRepFreqId() + ",";
 			}
 			String orgId = reportInForm.getOrgId();
 			
 			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
 			String validate_date = format.format(calendar.getTime());
 			if(reportInForm.getOrgId().equals("-999")){//2010-06-17 全部机构
 				/**
 				 * 根据登录用户orgId 拼凑下属所有机构的orgId字串
 				 * */
// 				String perOrgId=operator.getOrgId();
// 				List childOrg=AFOrgDelegate.getChildListByOrgId(perOrgId);
// 				StringBuffer orgIdSB=new StringBuffer("'").append(perOrgId).append("'");
// 				if (childOrg!=null && childOrg.size()>0){
// 					for(int i=0;i<childOrg.size();i++){
// 						orgIdSB.append(",'").append((String)childOrg.get(i)).append("'");
// 					}
// 				}
 				hql = "from MRepRange mrr where mrr.comp_id.orgId in (" +operator.getSubOrgIds()+",'"+operator.getOrgId()+"') and '"+
					validate_date +"' between mrr.MChildReport.startDate and mrr.MChildReport.endDate";
 			}else{
 				hql = "from MRepRange mrr where mrr.comp_id.orgId='"+ orgId +"' and '" +
 					validate_date +"' between mrr.MChildReport.startDate and mrr.MChildReport.endDate";
 			}
 			String subOrg="";
 			if(Config.BANK_NAME.equals("CZBANK")){
 				subOrg=operator.getChildOrgIds().replaceAll("'", "").split(",")[0];
 				hql = "from MRepRange mrr where mrr.comp_id.orgId='"+ subOrg +"' and '" +
 				validate_date +"' between mrr.MChildReport.startDate and mrr.MChildReport.endDate";
 			}
 			/**添加报表编号查询条件（忽略大小写模糊查询）*/
 			if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){				
 				hql +=" and upper(mrr.comp_id.childRepId) like upper('%" 
 						+ reportInForm.getChildRepId().trim() + "%')";
 			}
 			/**添加报表名称查询条件（模糊查询）*/
 			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
 				hql += " and mrr.MChildReport.reportName like '%" + reportInForm.getRepName().trim() + "%'";
 			}
 			/**添加模板类型（全部/法人/分支）查询条件*/
 			if(reportInForm.getFrOrFzType() != null && !reportInForm.getFrOrFzType().equals(Config.DEFAULT_VALUE)){
 				hql += " and mrr.MChildReport.frOrFzType='" + reportInForm.getFrOrFzType() + "'";
 			}			
 			/**加上报表是否已经发布*/
 			hql +=" and  mrr.MChildReport.isPublic = 1" ;
 			
 			/**加上报表报送权限
 			 * 已增加数据库判断*/
   			if(operator.isSuperManager() == false){
 				if(operator.getChildRepReportPopedom() == null || operator.getChildRepReportPopedom().equals(""))
 					return resList;
 				if(Config.DB_SERVER_TYPE.equals("oracle"))
 					hql += " and '" + orgId + "'||mrr.MChildReport.comp_id.childRepId in ("+ operator.getChildRepReportPopedom() +")";
 				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
 					hql += " and '" + orgId + "'+mrr.MChildReport.comp_id.childRepId in ("+ operator.getChildRepReportPopedom() +")";
 				if(Config.BANK_NAME.equals("CZBANK")){
 					if(Config.DB_SERVER_TYPE.equals("oralce"))
 						hql += " and '" + subOrg + "'||mrr.MChildReport.comp_id.childRepId in ("+ operator.getChildRepReportPopedom() +")";
 					if(Config.DB_SERVER_TYPE.equals("sqlserver"))
 						hql += " and '" + subOrg + "'+mrr.MChildReport.comp_id.childRepId in ("+ operator.getChildRepReportPopedom() +")";
 				}
   			}			
 			hql += " order by mrr.MChildReport.comp_id.childRepId";
 			
 			Query query = session.createQuery(hql);
 			rList = query.list(); 
 			resList = new ArrayList();
 			for(Iterator iter=rList.iterator();iter.hasNext();){
 				MRepRange mRepRange = (MRepRange)iter.next();
 				MChildReport mChildReport = mRepRange.getMChildReport();
 				Set mActuRepSet = mChildReport.getMActuReps();
 				Map map = new HashMap();
 				/**已使用hibernate 卞以刚 2011-12-22**/
 				List currList = MCurrUtil.newInstance().isExist(mChildReport.getComp_id().getChildRepId());
 				
 				/**考虑多币种的情况**/
 				if(currList != null && currList.size() > 0){
 					for(int i=0;i<currList.size();i++){
 						MCurrForm mCurrForm = (MCurrForm)currList.get(i);
 						String currName = mCurrForm.getCurName().split("_")[1];
 						
 						for(Iterator iter2=mActuRepSet.iterator();iter2.hasNext();){
         					MActuRep mActuRep = (MActuRep)iter2.next();
         					Aditing aditing = new Aditing();
         					Calendar cal = Calendar.getInstance();
         	    			cal.set(year,term-1,1);
         	    			
         					int days = 0;
         					try{
         						days = mActuRep.getNormalTime().intValue();
         						if(mActuRep.getDelayTime() != null)
         							days += mActuRep.getDelayTime().intValue();
         					}catch(Exception ex){
         						days = 0;
         					}
         					if(StrutsMRepRangeDelegate.contains(mActuRep.getMRepFreq().getRepFreqId().intValue(),rep_freq)
         							&& StrutsMRepRangeDelegate.beforeEnd(cal,days,mChildReport.getEndDate())){
         						if(map.containsKey(mActuRep.getMDataRgType().getDataRangeId()+currName))
         							continue;
         						map.put(mActuRep.getMDataRgType().getDataRangeId()+currName,mActuRep.getMDataRgType().getDataRangeId());
         						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
         						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
         						aditing.setActuFreqID(mActuRep.getMRepFreq().getRepFreqId());
         						aditing.setChildRepId(mActuRep.getComp_id().getChildRepId());
         						aditing.setVersionId(mActuRep.getComp_id().getVersionId());
         						String repName = mActuRep.getMChildReport().getMMainRep().getRepCnName().equals(mActuRep.getMChildReport().getReportName()) ?
         										 mActuRep.getMChildReport().getMMainRep().getRepCnName() : mActuRep.getMChildReport().getMMainRep().getRepCnName() 
         										 + "-" + mActuRep.getMChildReport().getReportName();
         						aditing.setRepName(repName);
         						aditing.setYear(new Integer(year));
         						aditing.setTerm(new Integer(term));
         						aditing.setCurrName(currName);
         						aditing.setCurId(mCurrForm.getCurId());
         						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
         						aditing.setOrgId(mRepRange.getComp_id().getOrgId());
         						/**已使用hibernate 卞以刚 2011-12-22**/
         						aditing.setOrgName(StrutsOrgNetDelegate.getOrgName(aditing.getOrgId()));
         						//  得到times的值,如果是-1汇总过的    							
     							String timesSql="from ReportIn ri where  "
     							 	+" ri.MChildReport.comp_id.childRepId='"+ aditing.getChildRepId()+"'"
     								+" and ri.MChildReport.comp_id.versionId='"+aditing.getVersionId()+"'"
     							//	+" and ri.MDataRgType.dataRangeId="+aditing.getDataRangeId()
     								+" and ri.year="+year
     								+" and ri.term="+term
     								+" and ri.MCurr.curId="+aditing.getCurId()
     								+" and ri.orgId='"+aditing.getOrgId()+"'"
     								+" and ri.times=-1";
     							List list = session.find(timesSql);
     							
     						
     							if(list != null && list.size() > 0){
         							ReportIn reportIn = (ReportIn)list.get(0);        							
         							aditing.setRepInId(reportIn.getRepInId());
         							aditing.setIsCollected(new Integer(1));
         							
         						}else{
         							aditing.setIsCollected(new Integer(0));
         						}
         						resList.add(aditing);
         					}	
         				}
 					}
 				}else{
 					for(Iterator iter2=mActuRepSet.iterator();iter2.hasNext();){
     					MActuRep mActuRep = (MActuRep)iter2.next();
     					Aditing aditing = new Aditing();
     					Calendar cal = Calendar.getInstance();
     	    			cal.set(year,term-1,1);
     	    			
     					int days = 0;
     					try{
     						days = mActuRep.getNormalTime().intValue();
     						if(mActuRep.getDelayTime() != null)
     							days += mActuRep.getDelayTime().intValue();
     					}catch(Exception ex){
     						days = 0;
     					}
     					if(StrutsMRepRangeDelegate.contains(mActuRep.getMRepFreq().getRepFreqId().intValue(),rep_freq)
     							&& StrutsMRepRangeDelegate.beforeEnd(cal,days,mChildReport.getEndDate())){
     						if(map.containsKey(mActuRep.getMDataRgType().getDataRangeId()))
     							continue;
     						map.put(mActuRep.getMDataRgType().getDataRangeId(),mActuRep.getMDataRgType().getDataRangeId());
     						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
     						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
     						aditing.setActuFreqID(mActuRep.getMRepFreq().getRepFreqId());
     						aditing.setChildRepId(mActuRep.getComp_id().getChildRepId());
     						aditing.setVersionId(mActuRep.getComp_id().getVersionId());
     						String repName = mActuRep.getMChildReport().getMMainRep().getRepCnName().equals(mActuRep.getMChildReport().getReportName()) ?
     										 mActuRep.getMChildReport().getMMainRep().getRepCnName() : mActuRep.getMChildReport().getMMainRep().getRepCnName() 
     										 + "-" + mActuRep.getMChildReport().getReportName();
     						aditing.setRepName(repName);
     						aditing.setYear(new Integer(year));
     						aditing.setTerm(new Integer(term));
     						aditing.setCurrName("人民币");
     						aditing.setCurId(new Integer("1"));
     						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
     						aditing.setOrgId(mRepRange.getComp_id().getOrgId());
     						/**已使用hibernate 卞以刚 2011-12-22**/
     						aditing.setOrgName(StrutsOrgNetDelegate.getOrgName(aditing.getOrgId()));
     						// 得到times的值,如果是-1汇总过的							
 							String timesSql="from ReportIn ri where  "
 							 	+" ri.MChildReport.comp_id.childRepId='"+ aditing.getChildRepId()+"'"
 								+" and ri.MChildReport.comp_id.versionId='"+aditing.getVersionId()+"'"
 								+" and ri.MDataRgType.dataRangeId="+aditing.getDataRgId()
 								+" and ri.year="+year
 								+" and ri.term="+term
 								+" and ri.MCurr.curId="+aditing.getCurId()
 								+" and ri.orgId='"+aditing.getOrgId()+"'"
 								+" and ri.times=-1";
 							List list = session.find(timesSql);
 							if(list != null && list.size() > 0){
     							ReportIn reportIn = (ReportIn)list.get(0);        							
     							aditing.setRepInId(reportIn.getRepInId());
     							aditing.setIsCollected(new Integer(1));
     						}else{
     							aditing.setIsCollected(new Integer(0));
     						}
     						resList.add(aditing);
     					}	
     				}
 				}
 			}
     	}catch(HibernateException he){
     		log.printStackTrace(he);
     		if(conn != null) conn.endTransaction(true);
     	}catch(Exception e){
     		e.printStackTrace();
     		log.printStackTrace(e);
     	}
     	finally{
     		if(conn != null) conn.closeSession();
     	}
     	return resList;
    }
        
    /**
     * ,根据年和期数 查该机构所需汇总的报表信息
     * @param reportInForm 
     * @param operator
     * @return   报表信息的列表
     */
    public static List selectAllCollectReport(ReportInForm reportInForm,Operator operator){
    	List rList = null;
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	String childRepReportPopedom = null;
    	
    	try{
    		if(reportInForm != null && reportInForm.getOrgId() != null){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			
    			int term = reportInForm.getTerm().intValue();
    			int year = reportInForm.getYear().intValue();
    			
    			Calendar calendar = Calendar.getInstance();
    			calendar.set(year,term-1,1);
    			
    			String rep_freq="";
    			if(term == 12)
    				rep_freq = "('月','季','半年','年')";
    			else if(term == 6)
    				rep_freq = "('月','季','半年')";
    			else if(term == 3 || term == 9)
    				rep_freq = "('月','季')";
    			else rep_freq = "('月')";
    			    			
    			String hql = "from MRepFreq mrf where mrf.repFreqName in " + rep_freq;
    			List rep_freqList = session.createQuery(hql).list();
    			
    			if(rep_freqList == null && rep_freqList.size() ==0)
    				return null;
    			
    			rep_freq = "";
    			for(int i=0;i<rep_freqList.size();i++){
    				MRepFreq mrepFreq = (MRepFreq)rep_freqList.get(i);
    				rep_freq += mrepFreq.getRepFreqId() + ",";
    			}
    			String orgId = reportInForm.getOrgId();
    			
    			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    			String validate_date = format.format(calendar.getTime());
    			
    			hql = "from MRepRange mrr where mrr.comp_id.orgId='"+ orgId +"' and '" +
						validate_date +"' between mrr.MChildReport.startDate and mrr.MChildReport.endDate";
    			String subOrg="";
    			if(Config.BANK_NAME.equals("CZBANk")){
    				subOrg=operator.getChildOrgIds().replaceAll("'", "").split(",")[0];
    				hql = "from MRepRange mrr where mrr.comp_id.orgId='"+ subOrg +"' and '" +
					validate_date +"' between mrr.MChildReport.startDate and mrr.MChildReport.endDate";
			
    			}
    			
    			/** 加上报表是否已经发布  */    			
    			hql +=" and  mrr.MChildReport.isPublic = 1" ;
    			
    			/**加上报送权限*/
    			if(operator == null) return resList;
    			childRepReportPopedom = operator.getChildRepReportPopedom();
    			if(operator.isSuperManager() == false){
    				if(childRepReportPopedom == null || childRepReportPopedom.equals(""))
    					return resList;
//    				hql += " and '" + orgId + "'||mrr.MChildReport.comp_id.childRepId in ("+ operator.getChildRepSearchPopedom() +")";
    			}
    			hql += " order by mrr.MChildReport.comp_id.childRepId";
    			
    			Query query = session.createQuery(hql);
    			rList = query.list(); 
    			
    			/**
    			 * 分开考虑用户是否是超级用户
    			 * 超级用户不用加权限
    			 */
    			if(operator.isSuperManager() == false){
    				String childRepId = null;
    				for(Iterator iter=rList.iterator();iter.hasNext();){
        				MRepRange mRepRange = (MRepRange)iter.next();
        				MChildReport mChildReport = mRepRange.getMChildReport();
        				Set mActuRepSet = mChildReport.getMActuReps();        				
        				Map map = new HashMap();
        				
        				childRepId = mChildReport.getComp_id().getChildRepId();
						if(childRepReportPopedom.indexOf(orgId+childRepId) <= -1)
							continue;
						
        				List currList = MCurrUtil.newInstance().isExist(childRepId);
        				
        				/**考虑多币种的情况**/
        				if(currList != null && currList.size() > 0){
        					for(int i=0;i<currList.size();i++){
        						MCurrForm mCurrForm = (MCurrForm)currList.get(i);
        						String currName = mCurrForm.getCurName().split("_")[1];
        						
        						for(Iterator iter2=mActuRepSet.iterator();iter2.hasNext();){
    	        					MActuRep mActuRep = (MActuRep)iter2.next();
    	        					Aditing aditing = new Aditing();
    	        					Calendar cal = Calendar.getInstance();
    	        	    			cal.set(year,term-1,1);
    	        	    			
    	        					int days = 0;
    	        					try{
    	        						days = mActuRep.getNormalTime().intValue();
    	        						if(mActuRep.getDelayTime() != null)
    	        							days += mActuRep.getDelayTime().intValue();
    	        					}catch(Exception ex){
    	        						days = 0;
    	        					}
    	        					if(StrutsMRepRangeDelegate.contains(mActuRep.getMRepFreq().getRepFreqId().intValue(),rep_freq)
    	        							&& StrutsMRepRangeDelegate.beforeEnd(cal,days,mChildReport.getEndDate())){
    	        						if(map.containsKey(mActuRep.getMDataRgType().getDataRangeId()+currName))
    	        							continue;
    	        						map.put(mActuRep.getMDataRgType().getDataRangeId()+currName,mActuRep.getMDataRgType().getDataRangeId());
    	        						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
    	        						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
    	        						aditing.setActuFreqID(mActuRep.getMRepFreq().getRepFreqId());
    	        						aditing.setChildRepId(childRepId);
    	        						aditing.setVersionId(mActuRep.getComp_id().getVersionId());
    	        						String repName = mActuRep.getMChildReport().getMMainRep().getRepCnName().equals(mActuRep.getMChildReport().getReportName()) ?
    	        										 mActuRep.getMChildReport().getMMainRep().getRepCnName() : mActuRep.getMChildReport().getMMainRep().getRepCnName() 
    	        										 + "-" + mActuRep.getMChildReport().getReportName();
    	        						aditing.setRepName(repName);
    	        						aditing.setYear(new Integer(year));
    	        						aditing.setTerm(new Integer(term));
    	        						aditing.setCurrName(currName);
    	        						aditing.setCurId(mCurrForm.getCurId());
    	        						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
    	        						
    	        						//得到times的值,如果是-1汇总过的            				
            							String timesSql="select * from ReportIn ri where  "
            							 	+" ri.MChildReport.comp_id.childRepId='"+ aditing.getChildRepId()+"'"
            								+" and ri.MChildReport.comp_id.versionId='"+aditing.getVersionId()+"'"
            								+" and ri.MDataRgType.dataRangeId="+aditing.getDataRangeId()
            								+" and ri.year="+year
            								+" and ri.term="+term
            								+" and ri.MCurr.curId="+aditing.getCurId()
            								+" and ri.orgId='"+orgId+"'"
            								+" and ri.times=-1";
            							List list = session.find(timesSql);
            							if(list != null && list.size() > 0){
                							ReportIn reportIn = (ReportIn)list.get(0);        							
                							aditing.setRepInId(reportIn.getRepInId());
                							aditing.setIsCollected(new Integer(1));
                							
                						}else{
                							aditing.setIsCollected(new Integer(0));
                						}
    	        						resList.add(aditing);
    	        					}	
    	        				}
        					}
        				}else{
        					for(Iterator iter2=mActuRepSet.iterator();iter2.hasNext();){
            					MActuRep mActuRep = (MActuRep)iter2.next();
            					Aditing aditing = new Aditing();
            					Calendar cal = Calendar.getInstance();
            	    			cal.set(year,term-1,1);
            	    			
            					int days = 0;
            					try{
            						days = mActuRep.getNormalTime().intValue();
            						if(mActuRep.getDelayTime() != null)
            							days += mActuRep.getDelayTime().intValue();
            					}catch(Exception ex){
            						days = 0;
            					}
            					if(StrutsMRepRangeDelegate.contains(mActuRep.getMRepFreq().getRepFreqId().intValue(),rep_freq)
            							&& StrutsMRepRangeDelegate.beforeEnd(cal,days,mChildReport.getEndDate())){
            						if(map.containsKey(mActuRep.getMDataRgType().getDataRangeId()))
            							continue;
            						map.put(mActuRep.getMDataRgType().getDataRangeId(),mActuRep.getMDataRgType().getDataRangeId());
            						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
            						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
            						aditing.setActuFreqID(mActuRep.getMRepFreq().getRepFreqId());
            						aditing.setChildRepId(mActuRep.getComp_id().getChildRepId());
            						aditing.setVersionId(mActuRep.getComp_id().getVersionId());
            						String repName = mActuRep.getMChildReport().getMMainRep().getRepCnName().equals(mActuRep.getMChildReport().getReportName()) ?
            										 mActuRep.getMChildReport().getMMainRep().getRepCnName() : mActuRep.getMChildReport().getMMainRep().getRepCnName() 
            										 + "-" + mActuRep.getMChildReport().getReportName();
            						aditing.setRepName(repName);
            						aditing.setYear(new Integer(year));
            						aditing.setTerm(new Integer(term));
            						aditing.setCurrName("人民币");
            						aditing.setCurId(new Integer("1"));
            						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
            						
            						//得到times的值,如果是-1汇总过的        							
        							String timesSql="from ReportIn ri where  "
        							 	+" ri.MChildReport.comp_id.childRepId='"+ aditing.getChildRepId()+"'"
        								+" and ri.MChildReport.comp_id.versionId='"+aditing.getVersionId()+"'"
        								+" and ri.MDataRgType.dataRangeId="+aditing.getDataRgId()
        								+" and ri.year="+year
        								+" and ri.term="+term
        								+" and ri.MCurr.curId="+aditing.getCurId()
        								+" and ri.orgId='"+orgId+"'"
        								+" and ri.times=-1";
        							List list = session.find(timesSql);
        							if(list != null && list.size() > 0){
            							ReportIn reportIn = (ReportIn)list.get(0);        							
            							aditing.setRepInId(reportIn.getRepInId());
            							aditing.setIsCollected(new Integer(1));
            							
            						}else{
            							aditing.setIsCollected(new Integer(0));
            						}
            						resList.add(aditing);
            					}	
            				}
        				}
        			}
    			}else{
    				for(Iterator iter=rList.iterator();iter.hasNext();){
        				MRepRange mRepRange = (MRepRange)iter.next();
        				MChildReport mChildReport = mRepRange.getMChildReport();
        				Set mActuRepSet = mChildReport.getMActuReps();
        				Map map = new HashMap();
        				    				
        				List currList = MCurrUtil.newInstance().isExist(mChildReport.getComp_id().getChildRepId());
        				
        				/**考虑多币种的情况**/
        				if(currList != null && currList.size() > 0){
        					for(int i=0;i<currList.size();i++){
        						MCurrForm mCurrForm = (MCurrForm)currList.get(i);
        						String currName = mCurrForm.getCurName().split("_")[1];
        						
        						for(Iterator iter2=mActuRepSet.iterator();iter2.hasNext();){
    	        					MActuRep mActuRep = (MActuRep)iter2.next();
    	        					Aditing aditing = new Aditing();
    	        					Calendar cal = Calendar.getInstance();
    	        	    			cal.set(year,term-1,1);
    	        	    			
    	        					int days = 0;
    	        					try{
    	        						days = mActuRep.getNormalTime().intValue();
    	        						if(mActuRep.getDelayTime() != null)
    	        							days += mActuRep.getDelayTime().intValue();
    	        					}catch(Exception ex){
    	        						days = 0;
    	        					}
    	        					if(StrutsMRepRangeDelegate.contains(mActuRep.getMRepFreq().getRepFreqId().intValue(),rep_freq)
    	        							&& StrutsMRepRangeDelegate.beforeEnd(cal,days,mChildReport.getEndDate())){
    	        						if(map.containsKey(mActuRep.getMDataRgType().getDataRangeId()+currName))
    	        							continue;
    	        						map.put(mActuRep.getMDataRgType().getDataRangeId()+currName,mActuRep.getMDataRgType().getDataRangeId());
    	        						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
    	        						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
    	        						aditing.setActuFreqID(mActuRep.getMRepFreq().getRepFreqId());
    	        						aditing.setChildRepId(mActuRep.getComp_id().getChildRepId());
    	        						aditing.setVersionId(mActuRep.getComp_id().getVersionId());
    	        						String repName = mActuRep.getMChildReport().getMMainRep().getRepCnName().equals(mActuRep.getMChildReport().getReportName()) ?
    	        										 mActuRep.getMChildReport().getMMainRep().getRepCnName() : mActuRep.getMChildReport().getMMainRep().getRepCnName() 
    	        										 + "-" + mActuRep.getMChildReport().getReportName();
    	        						aditing.setRepName(repName);
    	        						aditing.setYear(new Integer(year));
    	        						aditing.setTerm(new Integer(term));
    	        						aditing.setCurrName(currName);
    	        						aditing.setCurId(mCurrForm.getCurId());
    	        						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
    	        						
    	        						//得到times的值,如果是-1汇总过的            				
            							String timesSql="select * from ReportIn ri where  "
            							 	+" ri.MChildReport.comp_id.childRepId='"+ aditing.getChildRepId()+"'"
            								+" and ri.MChildReport.comp_id.versionId='"+aditing.getVersionId()+"'"
            								+" and ri.MDataRgType.dataRangeId="+aditing.getDataRangeId()
            								+" and ri.year="+year
            								+" and ri.term="+term
            								+" and ri.MCurr.curId="+aditing.getCurId()
            								+" and ri.orgId='"+orgId+"'"
            								+" and ri.times=-1";
            							List list = session.find(timesSql);
            							if(list != null && list.size() > 0){
                							ReportIn reportIn = (ReportIn)list.get(0);        							
                							aditing.setRepInId(reportIn.getRepInId());
                							aditing.setIsCollected(new Integer(1));
                							
                						}else{
                							aditing.setIsCollected(new Integer(0));
                						}
    	        						resList.add(aditing);
    	        					}	
    	        				}
        					}
        				}else{
        					for(Iterator iter2=mActuRepSet.iterator();iter2.hasNext();){
            					MActuRep mActuRep = (MActuRep)iter2.next();
            					Aditing aditing = new Aditing();
            					Calendar cal = Calendar.getInstance();
            	    			cal.set(year,term-1,1);
            	    			
            					int days = 0;
            					try{
            						days = mActuRep.getNormalTime().intValue();
            						if(mActuRep.getDelayTime() != null)
            							days += mActuRep.getDelayTime().intValue();
            					}catch(Exception ex){
            						days = 0;
            					}
            					if(StrutsMRepRangeDelegate.contains(mActuRep.getMRepFreq().getRepFreqId().intValue(),rep_freq)
            							&& StrutsMRepRangeDelegate.beforeEnd(cal,days,mChildReport.getEndDate())){
            						if(map.containsKey(mActuRep.getMDataRgType().getDataRangeId()))
            							continue;
            						map.put(mActuRep.getMDataRgType().getDataRangeId(),mActuRep.getMDataRgType().getDataRangeId());
            						aditing.setDataRgTypeName(mActuRep.getMDataRgType().getDataRgDesc());
            						aditing.setActuFreqName(mActuRep.getMRepFreq().getRepFreqName());
            						aditing.setActuFreqID(mActuRep.getMRepFreq().getRepFreqId());
            						aditing.setChildRepId(mActuRep.getComp_id().getChildRepId());
            						aditing.setVersionId(mActuRep.getComp_id().getVersionId());
            						String repName = mActuRep.getMChildReport().getMMainRep().getRepCnName().equals(mActuRep.getMChildReport().getReportName()) ?
            										 mActuRep.getMChildReport().getMMainRep().getRepCnName() : mActuRep.getMChildReport().getMMainRep().getRepCnName() 
            										 + "-" + mActuRep.getMChildReport().getReportName();
            						aditing.setRepName(repName);
            						aditing.setYear(new Integer(year));
            						aditing.setTerm(new Integer(term));
            						aditing.setCurrName("人民币");
            						aditing.setCurId(new Integer("1"));
            						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
            						
            						//得到times的值,如果是-1汇总过的        							
        							String timesSql="from ReportIn ri where  "
        							 	+" ri.MChildReport.comp_id.childRepId='"+ aditing.getChildRepId()+"'"
        								+" and ri.MChildReport.comp_id.versionId='"+aditing.getVersionId()+"'"
        								+" and ri.MDataRgType.dataRangeId="+aditing.getDataRgId()
        								+" and ri.year="+year
        								+" and ri.term="+term
        								+" and ri.MCurr.curId="+aditing.getCurId()
        								+" and ri.orgId='"+orgId+"'"
        								+" and ri.times=-1";
        							List list = session.find(timesSql);
        							if(list != null && list.size() > 0){
            							ReportIn reportIn = (ReportIn)list.get(0);        							
            							aditing.setRepInId(reportIn.getRepInId());
            							aditing.setIsCollected(new Integer(1));
            							
            						}else{
            							aditing.setIsCollected(new Integer(0));
            						}
            						resList.add(aditing);
            					}	
            				}
        				}
        			}
    			}    			
    		}
    	}catch(HibernateException he){
    		log.printStackTrace(he);
    		if(conn != null) conn.endTransaction(true);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return resList;
    }
}
