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
* <p>Description ����������ݿ����������</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   ����
* @date     2009-09-14
* @version 1.0
*/

public class StrutsCollectReportDelegate {

    private static FitechException log=new FitechException(StrutsCollectReportDelegate.class);
    
    public StrutsCollectReportDelegate() {
          }

   /**
    * ��ѯһ�����������е�ĳһ�ڵ������һ�λ������
    * @param orgId      String      ���� Id
    * @param year       String      ���
    * @param term       String      ����
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
     * �����������һ������
     * @param id        CollectReportPK     �ϱ����� Id
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
     * ����oracle�﷨(upper) ��Ҫ�޸� ���Ը� 2011-12-22
     * ����������� ��û���������ܵı�����Ϣ
     * 
     * @param reportInForm 
     * @param operator
     * @return   ������Ϣ���б�
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
 				rep_freq = "('��','��','����','��')";
 			else if(term == 6)
 				rep_freq = "('��','��','����')";
 			else if(term == 3 || term == 9)
 				rep_freq = "('��','��')";
 			else rep_freq = "('��')";
 			    			
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
 			if(reportInForm.getOrgId().equals("-999")){//2010-06-17 ȫ������
 				/**
 				 * ���ݵ�¼�û�orgId ƴ���������л�����orgId�ִ�
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
 			/**��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ��*/
 			if(reportInForm.getChildRepId() != null && !reportInForm.getChildRepId().equals("")){				
 				hql +=" and upper(mrr.comp_id.childRepId) like upper('%" 
 						+ reportInForm.getChildRepId().trim() + "%')";
 			}
 			/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
 			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
 				hql += " and mrr.MChildReport.reportName like '%" + reportInForm.getRepName().trim() + "%'";
 			}
 			/**���ģ�����ͣ�ȫ��/����/��֧����ѯ����*/
 			if(reportInForm.getFrOrFzType() != null && !reportInForm.getFrOrFzType().equals(Config.DEFAULT_VALUE)){
 				hql += " and mrr.MChildReport.frOrFzType='" + reportInForm.getFrOrFzType() + "'";
 			}			
 			/**���ϱ����Ƿ��Ѿ�����*/
 			hql +=" and  mrr.MChildReport.isPublic = 1" ;
 			
 			/**���ϱ�����Ȩ��
 			 * ���������ݿ��ж�*/
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
 				/**��ʹ��hibernate ���Ը� 2011-12-22**/
 				List currList = MCurrUtil.newInstance().isExist(mChildReport.getComp_id().getChildRepId());
 				
 				/**���Ƕ���ֵ����**/
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
         						/**��ʹ��hibernate ���Ը� 2011-12-22**/
         						aditing.setOrgName(StrutsOrgNetDelegate.getOrgName(aditing.getOrgId()));
         						//  �õ�times��ֵ,�����-1���ܹ���    							
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
     						aditing.setCurrName("�����");
     						aditing.setCurId(new Integer("1"));
     						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
     						aditing.setOrgId(mRepRange.getComp_id().getOrgId());
     						/**��ʹ��hibernate ���Ը� 2011-12-22**/
     						aditing.setOrgName(StrutsOrgNetDelegate.getOrgName(aditing.getOrgId()));
     						// �õ�times��ֵ,�����-1���ܹ���							
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
     * ,����������� ��û���������ܵı�����Ϣ
     * @param reportInForm 
     * @param operator
     * @return   ������Ϣ���б�
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
    				rep_freq = "('��','��','����','��')";
    			else if(term == 6)
    				rep_freq = "('��','��','����')";
    			else if(term == 3 || term == 9)
    				rep_freq = "('��','��')";
    			else rep_freq = "('��')";
    			    			
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
    			
    			/** ���ϱ����Ƿ��Ѿ�����  */    			
    			hql +=" and  mrr.MChildReport.isPublic = 1" ;
    			
    			/**���ϱ���Ȩ��*/
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
    			 * �ֿ������û��Ƿ��ǳ����û�
    			 * �����û����ü�Ȩ��
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
        				
        				/**���Ƕ���ֵ����**/
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
    	        						
    	        						//�õ�times��ֵ,�����-1���ܹ���            				
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
            						aditing.setCurrName("�����");
            						aditing.setCurId(new Integer("1"));
            						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
            						
            						//�õ�times��ֵ,�����-1���ܹ���        							
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
        				
        				/**���Ƕ���ֵ����**/
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
    	        						
    	        						//�õ�times��ֵ,�����-1���ܹ���            				
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
            						aditing.setCurrName("�����");
            						aditing.setCurId(new Integer("1"));
            						aditing.setDataRgId(mActuRep.getMDataRgType().getDataRangeId());
            						
            						//�õ�times��ֵ,�����-1���ܹ���        							
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
