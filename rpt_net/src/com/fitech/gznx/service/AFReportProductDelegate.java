package com.fitech.gznx.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.ReportIn;
import com.cbrc.smis.jdbc.FitechConnection;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.ReportExcelHandler;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.PageListInfo;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.dao.DaoModel;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfViewReport;
import com.fitech.gznx.po.QdCellinfo;
import com.fitech.net.hibernate.OrgNet;
import com.fitech.net.hibernate.ViewMReport;


public class AFReportProductDelegate extends DaoModel{

	/***
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����ViewMReport AfTemplate ReportIn
	 * @param reportInForm
	 * @param operator
	 * @param curPage
	 * @return
	 */
	public static PageListInfo selectYJHReportList(AFReportForm reportInForm,
			Operator operator,int curPage) {    	 
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	PageListInfo pageListInfo = null;
    	try{

    		if(reportInForm != null && reportInForm.getOrgId() != null){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			
    			String[] dates = reportInForm.getDate().split("-");

    			int year = Integer.parseInt(dates[0]);
    			int term = Integer.parseInt(dates[1]);
    			int day = Integer.parseInt(dates[2]);
    			
    			String rep_freq="";
//    			if(term == 12)
//    				rep_freq = "('��','��','����','��')";
//    			else if(term == 6)
//    				rep_freq = "('��','��','����')";
//    			else if(term == 3 || term == 9)
//    				rep_freq = "('��','��')";
//    			else rep_freq = "('��')";
    			if(term == 12)
    				rep_freq = "('��','��','����','��','��')";
    			else if(term == 6)
    				rep_freq = "('��','��','����','��')";
    			else if(term == 3 || term == 9)
    				rep_freq = "('��','��','��')";
    			else rep_freq = "('��','��')";
    	String strTerm = String.valueOf(term);
    	String orgId = reportInForm.getOrgId();
    	if(StringUtil.isEmpty(orgId)){
    		orgId = operator.getOrgId();
    	}
    	if(term<10) 
    		strTerm="0"+term;
//    	String hql=" from  ViewMReport a , AfTemplate t where a.comp_id.childRepId=t.id.templateId and a.comp_id.versionId=t.id.versionId and t.isReport!=3 and a.startDate<='"+year+"-"+strTerm+"-01' and a.endDate>='"+year+"-"+strTerm+"-01'" +
//				" and a.repFreqName in"+rep_freq;
    	String hql=" from  ViewMReport a , AfTemplate t where a.comp_id.childRepId=t.id.templateId and a.comp_id.versionId=t.id.versionId and t.isReport!=3 and a.startDate<='"+reportInForm.getDate()+"' and a.endDate>='"+reportInForm.getDate()+"'" +
		" and a.repFreqName in"+rep_freq;
    	 if(!orgId.equals(Config.DEFAULT_VALUE)){
    		 hql+=" and a.comp_id.orgId='"+orgId+"'";
    	 }
    	 
		/**���ϱ���Ȩ��
		 * ���������ݿ��ж�*/
		if(operator.isSuperManager() == false){
			 if(!orgId.equals(Config.DEFAULT_VALUE)){
				 if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					 hql += " and '" + orgId + "' || a.comp_id.childRepId in ("+ operator.getChildRepSearchPopedom() +")";
				 else if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					 hql += " and '" + orgId + "'+a.comp_id.childRepId in ("+ operator.getChildRepSearchPopedom() +")";
					 
			 }else{
				 if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					 hql += " and a.comp_id.orgId || a.comp_id.childRepId in ("+ operator.getChildRepSearchPopedom() +")";
				 else if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					 hql += " and a.comp_id.orgId + a.comp_id.childRepId in ("+ operator.getChildRepSearchPopedom() +")";
			 }
		}
		String repName = reportInForm.getRepName();
		if(repName!=null && !"".equals(repName.trim())){
			hql += " and a.reportName like '%"+repName+"%' ";
			
		}
		/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
		if (reportInForm.getRepFreqId() != null
				&& !String.valueOf(reportInForm.getRepFreqId()).equals(
						Config.DEFAULT_VALUE)) {
			hql += " and a.repFreqId="
					+ reportInForm.getRepFreqId();
		}
		
		if (reportInForm.getIsReport() != null
				&& !reportInForm.getIsReport().equals(
						Integer.valueOf(Config.DEFAULT_VALUE))) {
			hql +=" and t.isReport="+reportInForm.getIsReport();
		}
		
		String templateTypeid = reportInForm.getTemplateType();
		if(!StringUtil.isEmpty(templateTypeid)){
			hql += " and a.repTypeId in ("+templateTypeid+")";
		}
		/**order by ���� ע�͵� ���Ը� 2011-12-28 ������
		 * ���������ݿ��ж�*/
		if(Config.DB_SERVER_TYPE.equals("oracle"))
			hql +=" order by a.comp_id.childRepId";
		if(Config.DB_SERVER_TYPE.equals("sqlserver") || Config.DB_SERVER_TYPE.equals("db2"))
			hql+="";

		pageListInfo = findByPageWithSQL(session,hql,Config.PER_PAGE_ROWS,curPage);
	//	recordCount = (int) pageListInfo.getRowCount();
		if(pageListInfo==null)
			return pageListInfo;
		List list=pageListInfo.getList();
		if(list==null || list.size()==0)
			return pageListInfo;
			for(int i=0;i<list.size();i++){
				Object[] dd = (Object[])list.get(i);
				ViewMReport viewMReport=(ViewMReport) dd[0];
				Aditing aditing = new Aditing();
				aditing.setDataRgTypeName(viewMReport.getDataRgTypeName());
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getRepFreqId());
				aditing.setChildRepId(viewMReport.getComp_id().getChildRepId());
				aditing.setVersionId(viewMReport.getComp_id().getVersionId());
				aditing.setRepName(viewMReport.getReportName());
				if (aditing.getActuFreqID() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil
							.getFreqDateLast(reportInForm.getDate(),
									aditing.getActuFreqID());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}
				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				aditing.setDataRgId(viewMReport.getComp_id().getDataRangeId());
				OrgNet org=(OrgNet)session.get(OrgNet.class, viewMReport.getComp_id().getOrgId());
				aditing.setOrgName(org.getOrgName());
				aditing.setOrgId(org.getOrgId());
				String sql = "from ReportIn ri where ri.times =-2 and ri.MChildReport.comp_id.versionId='" + aditing.getVersionId() + "'  and ri.MCurr.curId="+aditing.getCurId()+
				 " and ri.MChildReport.comp_id.childRepId='" + aditing.getChildRepId() + "' and ri.year=" + year + 
				 " and ri.term=" + term + " and ri.orgId='" + org.getOrgId()  + "' and ri.MDataRgType.dataRgDesc='" + aditing.getDataRgTypeName()+"' order by ri.repInId desc";
	
				List reportInList = session.find(sql);
				
				if(reportInList != null && reportInList.size() > 0){
					aditing.setCheckFlag(new Short(new String("1")));					
				}else{
					aditing.setCheckFlag(new Short(new String("3")));					
				}
				resList.add(aditing);

    		}
			pageListInfo.setList(resList);
    		}	
    	}catch(HibernateException he){
    		if(conn != null) conn.endTransaction(true);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return pageListInfo;
    }

	/****
	 * jdbc���� ����oracle�﷨(upper) ���Ը� 2011-12-22
	 * rownum �����޸�Ϊtop
	 * @param reportInForm
	 * @param operator
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List selectYJHExportReportList(AFReportForm reportInForm,
			Operator operator, int offset, int limit ,int flag) {    	 
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
 
    	try{
    		if(reportInForm != null ){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			
    			String[] dates = reportInForm.getDate().split("-");

    			int year = Integer.parseInt(dates[0]);
    			int term = Integer.parseInt(dates[1]);
    			
    			String rep_freq="";
    			if(term == 12)
    				rep_freq = "('��','��','����','��')";
    			else if(term == 6)
    				rep_freq = "('��','��','����')";
    			else if(term == 3 || term == 9)
    				rep_freq = "('��','��')";
    			else rep_freq = "('��')";
    		StringBuffer sql= null;
    		/**���������ݿ��ж�*/
    		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
    			sql = new StringBuffer("select top "+(offset+limit)+"  a.org_id,d.org_name,a.CHILD_REP_ID,a.VERSION_ID,a.REPORT_NAME,a.CUR_NAME,a.REP_FREQ_NAME,a.DATA_RG_DESC,b.CHECK_FLAG,b.REP_IN_ID,a.CUR_ID,a.REP_FREQ_ID " +
					" from  VIEW_M_REPORT a left join org d on  a.org_id=d.org_id left join " +
					" (select * from report_in   where year="+year+" and term="+term+" and times=1) b " +
					" on  a. ORG_ID=b.org_id and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID 	and a.CUR_ID=b.CUR_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID ");
    		if(Config.DB_SERVER_TYPE.equals("oracle"))
    			sql = new StringBuffer("select t.*,rownum from ( select a.org_id,d.org_name,a.CHILD_REP_ID,a.VERSION_ID,a.REPORT_NAME,a.CUR_NAME,a.REP_FREQ_NAME,a.DATA_RG_DESC,b.CHECK_FLAG,b.REP_IN_ID,a.CUR_ID,a.REP_FREQ_ID " +
					" from  VIEW_M_REPORT a left join org d on  a.org_id=d.org_id left join " +
					" (select * from report_in   where year="+year+" and term="+term+" and times=1) b " +
					" on  a. ORG_ID=b.org_id and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID 	and a.CUR_ID=b.CUR_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID ");
    		if(Config.DB_SERVER_TYPE.equals("db2"))	{
				sql = new StringBuffer("select * from ( select t.*,row_number() over(order by t.ORG_ID,t.CHILD_REP_ID,t.VERSION_ID) as rownum from ( select a.org_id,d.org_name,a.CHILD_REP_ID,a.VERSION_ID,a.REPORT_NAME,a.CUR_NAME,a.REP_FREQ_NAME,a.DATA_RG_DESC,b.CHECK_FLAG,b.REP_IN_ID,a.CUR_ID,a.REP_FREQ_ID " +
						" from  VIEW_M_REPORT a left join org d on  a.org_id=d.org_id left join " +
						" (select * from report_in   where year="+year+" and term="+term+" and times=1) b " +
						" on  a. ORG_ID=b.org_id and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID 	and a.CUR_ID=b.CUR_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID ");
			}	
		/**��ѯ����״̬Ϊ���ͨ������*/
		StringBuffer where = new StringBuffer(" where a.START_DATE<='"+year+"-"+(term<10 ? "0" + term : term)+"-01' and a.END_DATE>='"+year+"-"+(term<10 ? "0" + term : term)+"-01' and a.REP_FREQ_NAME in "+rep_freq);

		/**��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ��*/
		if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){				
//			if(Config.SYSTEM_SCHEMA_FLAG==1){
//				where.append(" and a.CHILD_REP_ID in ("
//						+ reportInForm.getTemplateId().trim() + ")");
//			}else{
//				
//			}
			where.append(" and upper(a.CHILD_REP_ID) like upper('%" 
					+ reportInForm.getTemplateId().trim() + "%')");
		}
		/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
		if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
			where.append(" and a.REPORT_NAME like '%" + reportInForm.getRepName().trim() + "%'");
		}

		/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
		if(reportInForm.getRepFreqId() != null && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
			where.append(" and a.REP_FREQ_ID=" + reportInForm.getRepFreqId() );
		}
		if(!StringUtil.isEmpty(reportInForm.getOrgId())  && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
			where.append(" and a.ORG_ID='" + reportInForm.getOrgId().trim() + "'");
		}
		/**��ӱ���״̬��δ���/���ͨ��/���δͨ������ѯ����*/
		//�������������޸ģ���������������������̣��˴˴��޸�Ϊ�ϱ�֮����ܲ鿴  �ϱ����CHECK_FLAG=0
		if(!Config.HZLHYH)
			where.append(" and b.CHECK_FLAG="+Config.YJ_EXP_REP_FLAG);
		else
			where.append(" and b.CHECK_FLAG="+Config.CHECK_FLAG_UNCHECK);
		if(Config.SYSTEM_SCHEMA_FLAG==1)
			where.append(toExpCbrcSql());
		if(flag == 0){
			if (operator.isSuperManager() == false){
				if (operator.getChildRepSearchPopedom() != null &&!operator.getChildRepSearchPopedom().equals(""))
				{
					if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
						where.append(" and a.ORG_ID || a.CHILD_REP_ID in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
					if(Config.DB_SERVER_TYPE.equals("sqlserver"))
						where.append(" and a.ORG_ID+a.CHILD_REP_ID in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
				}
			}
		}else{
			if (operator.isSuperManager() == false){
				if (operator.getChildRepReportPopedom() != null &&!operator.getChildRepReportPopedom().equals(""))
				{
					String  ss = operator.getChildRepReportPopedom();
					
					if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
						where.append(" and a.ORG_ID || a.CHILD_REP_ID in ("+operator.getChildRepReportPopedom()+")");
					if(Config.DB_SERVER_TYPE.equals("sqlserver"))
						where.append(" and a.ORG_ID+a.CHILD_REP_ID in ("+operator.getChildRepReportPopedom()+")");
				}
			}
		}
		/**���������ݿ��ж�*/
		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			sql.append(where.toString() + " order by a.ORG_ID,a.CHILD_REP_ID,a.VERSION_ID ");
		if(Config.DB_SERVER_TYPE.equals("oracle"))
			sql.append(where.toString() + " order by a.ORG_ID,a.CHILD_REP_ID,a.VERSION_ID ) t where rownum<="+(offset+limit));
		if(Config.DB_SERVER_TYPE.equals("db2")){
			sql.append(where.toString() + " order by a.ORG_ID,a.CHILD_REP_ID,a.VERSION_ID ) t  ) where rownum<="+(offset+limit));
		}
		//System.out.println("sql "+sql);
		Connection connection =session.connection();
		ResultSet rs=connection.createStatement().executeQuery(sql.toString());
		
		int i=1;
		while(rs.next()){
			if(i<=offset){
				i++;
				continue;
			}
				Aditing aditing  = new Aditing();

				//���ñ������
				aditing.setYear(year);
				//���ñ�������
				aditing.setTerm(term);
				aditing.setDay(1);
				aditing.setRepName(rs.getString("REPORT_NAME"));
				//���ñ���������
//				aditing.setReportDate(reportInRecord.getReportDate());
				//���ñ�����
				aditing.setChildRepId(rs.getString("CHILD_REP_ID"));
				//���ñ���汾��
				aditing.setVersionId(rs.getString("VERSION_ID"));
				//���ñ����������
				aditing.setCurrName(rs.getString("CUR_NAME"));
				
				aditing.setCurId(rs.getInt("CUR_ID"));
				aditing.setActuFreqID(rs.getInt("REP_FREQ_ID"));
				
					//���ñ��Ϳھ�
				aditing.setDataRgTypeName(rs.getString("DATA_RG_DESC"));
					//���ñ���Ƶ��
				aditing.setActuFreqName(rs.getString("REP_FREQ_NAME"));
				if(rs.getString("REP_IN_ID")!=null){
					aditing.setRepInId(new Integer(rs.getString("REP_IN_ID")));
				} else{
					aditing.setRepInId(0);
				}
				if(rs.getString("CHECK_FLAG") == null ){
					if(aditing.getRepInId() != null)
						aditing.setRepInId(0);
				}else
					aditing.set_checkFlag(Short.valueOf(rs.getString("CHECK_FLAG")));
				
				aditing.setOrgName(rs.getString("ORG_NAME"));
				aditing.setOrgId(rs.getString("ORG_ID"));

				resList.add(aditing);
				
    		}
		
    		}	
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    	
    		if(conn != null) conn.closeSession();
    	}
    	return resList;
    }

	public static List selectYJHReportList(AFReportForm reportInForm,
			Operator operator) {
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(reportInForm != null && reportInForm.getOrgId() != null){
    			conn = new DBConn();
    			session = conn.openSession();
    			
    			String[] dates = reportInForm.getDate().split("-");

    			int year = Integer.parseInt(dates[0]);
    			int term = Integer.parseInt(dates[1]);
    			
    			String rep_freq="";
    			if(term == 12)
    				rep_freq = "('��','��','����','��')";
    			else if(term == 6)
    				rep_freq = "('��','��','����')";
    			else if(term == 3 || term == 9)
    				rep_freq = "('��','��')";
    			else rep_freq = "('��')";
    
    	String strTerm = String.valueOf(term);
    	if(term<10) 
    		strTerm="0"+term;
    	String orgId = reportInForm.getOrgId();
    	if(StringUtil.isEmpty(orgId)){
    		orgId = operator.getOrgId();
    	}
    	    String hql="from ViewMReport a , AfTemplate t where a.comp_id.childRepId=t.id.templateId and a.comp_id.versionId=t.id.versionId and t.isReport!=3 and  a.startDate<='"+year+"-"+strTerm+"-01' and a.endDate>='"+year+"-"+strTerm+"-01'" +
				" and a.repFreqName in"+rep_freq;
    	 if(!orgId.equals(Config.DEFAULT_VALUE)){
       		 hql+=" and a.comp_id.orgId='"+orgId+"'";
       	 }
		/**���ϱ���Ȩ��
		 * ���������ݿ��ж�*/
		if(operator.isSuperManager() == false){
			if(!orgId.equals(Config.DEFAULT_VALUE)){
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and '" + orgId + "'||a.comp_id.childRepId in ("+ operator.getChildRepSearchPopedom() +")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and '" + orgId + "'+a.comp_id.childRepId in ("+ operator.getChildRepSearchPopedom() +")";
			}else{
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and a.comp_id.orgId||a.comp_id.childRepId in ("+ operator.getChildRepSearchPopedom() +")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and a.comp_id.orgId+a.comp_id.childRepId in ("+ operator.getChildRepSearchPopedom() +")";
		 }
		}
		String repName = reportInForm.getRepName();
		if(repName!=null && !"".equals(repName.trim())){
			hql += " and a.reportName like '%"+repName+"%' ";
			
		}
		/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
		if (reportInForm.getRepFreqId() != null
				&& !String.valueOf(reportInForm.getRepFreqId()).equals(
						Config.DEFAULT_VALUE)) {
			hql += " and a.repFreqId="
					+ reportInForm.getRepFreqId();
		}
		
		String templateTypeid = reportInForm.getTemplateType();
		if(!StringUtil.isEmpty(templateTypeid)){
			hql += " and a.repTypeId in ("+templateTypeid+")";
		}
		hql +=" order by t.priorityFlag,a.comp_id.childRepId";
		Query query = session.createQuery(hql);
		List list=query.list();
		
		for(int i=0;i<list.size();i++){
			Object[] dd = (Object[])list.get(i);
			ViewMReport viewMReport=(ViewMReport) dd[0];
			Aditing aditing = new Aditing();
			aditing.setDataRgTypeName(viewMReport.getDataRgTypeName());
			aditing.setActuFreqName(viewMReport.getRepFreqName());
			aditing.setActuFreqID(viewMReport.getRepFreqId());
			aditing.setChildRepId(viewMReport.getComp_id().getChildRepId());
			aditing.setVersionId(viewMReport.getComp_id().getVersionId());
			aditing.setRepName(viewMReport.getReportName());
			aditing.setYear(new Integer(year));
			aditing.setTerm(new Integer(term));
			aditing.setCurrName(viewMReport.getCurName());
			aditing.setCurId(viewMReport.getComp_id().getCurId());
			aditing.setDataRangeId(viewMReport.getComp_id().getDataRangeId());
			OrgNet org=(OrgNet)session.get(OrgNet.class, viewMReport.getComp_id().getOrgId());
			aditing.setOrgName(org.getOrgName());
			aditing.setOrgId(org.getOrgId());

			resList.add(aditing);
    		}
    		}	
    	}catch(HibernateException he){
    		he.printStackTrace();
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return resList;
    }

	public static boolean insertYJHReport(Map reportMap, Operator operator) {
		FitechMessages messages = new FitechMessages();
		ReportInForm reportInForm = null;
		ReportIn reportInTemp = null;
		String childRepId = (String) reportMap.get("templateid");
		String versionId = (String) reportMap.get("versionod");
		String dataRangeid = String.valueOf(reportMap.get("dataRgId")) ;
		String orgId = (String) reportMap.get("orgid");
		String time = (String) reportMap.get("times");
		String filePath = (String) reportMap.get("file");
		String qdFlg = String.valueOf(reportMap.get("QDFlg"));
		String year = null;
		String term = null;
		if(!StringUtil.isEmpty(time)){
			year = time.substring(0, 4);
			term = time.substring(4, 6);
		}
		String reportName =null;
		boolean result = false;
		try {
		// �����������
		reportName =new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId)
				.getReportName();
		/**��ñ���ı��֣�Ŀǰ��֧�ֶ���ֵ��������ɣ�*/
		String curId = String.valueOf(reportMap.get("curId"));
		if (curId == null || curId.equals(""))
			curId = "1";
		 MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId.equals("01")?"1":curId);		
				
		if(dataRangeid == null){
			messages.add(reportName+"���ɿھ�����");
			return false;
		}
		
		/**�ж����ɿھ��Ƿ���ȷ*/
		if(StrutsReportInDelegate.isExistDataRange(dataRangeid,childRepId,versionId) == false){
			messages.add(reportName+"���ɿھ�����");
			return false;
		}
	
		/**�жϱ����Ƿ��Ѿ����ɣ���������ɾ����Ӧ��Ϣ��*/
		reportInTemp = StrutsReportInDelegate.getReportIn(childRepId,versionId,orgId,new Integer(year)
				,new Integer(term),Integer.valueOf(dataRangeid),mCurr.getCurId(),new Integer(-2));
		if(reportInTemp != null ){
			return true;
		}

		Integer repInId = null;
		//Ԥ�Ȳ����¼�¼
		reportInForm = new ReportInForm();
		reportInForm.setChildRepId(childRepId);
		reportInForm.setVersionId(versionId);
		reportInForm.setDataRangeId(Integer.valueOf(dataRangeid));
		reportInForm.setCurId(new Integer(curId));
		reportInForm.setYear(new Integer(year));
		reportInForm.setTerm(new Integer(term));
		reportInForm.setOrgId(orgId);
		reportInForm.setTimes(new Integer(-2));
		reportInForm.setReportDate(new Date());
		reportInForm.setCheckFlag(Config.CHECK_FLAG_PRODUCT);
		reportInForm.setCurName(mCurr.getCurName());
		reportInForm.setRepName(reportName);
		// ��������
		StrutsReportInDelegate strutsReportInDelegate=new StrutsReportInDelegate();
		/***
		 * 20130117���ָ�ѡ���ɻ���REPORT_IN���в������ݣ����±����ѯ�����ظ�����ע�͸ù���
		 */
		//ReportIn reportIn = strutsReportInDelegate.insertNewyjhReport(reportInForm);
//		if (reportIn != null)
//			repInId = reportIn.getRepInId();
		// ���뱨������
		if(!StringUtil.isEmpty(qdFlg) && qdFlg.equals(com.fitech.gznx.common.Config.PROFLG_SENCEN_QD)){
//			result =  insertQDReportData(childRepId,versionId,
//					String.valueOf(repInId.intValue()),filePath,com.fitech.gznx.common.Config.CBRC_REPORT);
			result = true;
		} else{
			//ReportExcelHandler excelHandler = new ReportExcelHandler(repInId, filePath);
			/***
			 * 20130117���ָ�ѡ���ɻ���REPORT_IN���в������ݣ����±����ѯ�����ظ�����ע�͸ù���
			 */
			//result = excelHandler.copyExcelToDB();
			
			AFReportDealDelegate.deleteUploadFile(filePath);
			result = true;
		}
		
		/***
		 * 20130117���ָ�ѡ���ɻ���REPORT_IN���в������ݣ����±����ѯ�����ظ�����ע�͸ù���
		 */
//		if (result)
//			result = strutsReportInDelegate.updateReportInCheckFlag_e(repInId, Config.CHECK_FLAG_PRODUCT);
		
		
		}catch (Exception e){
			messages.add(reportName+"����ʧ�ܣ�");
			e.printStackTrace();
		}
		
		return result;
	}

	private static boolean removeReportIn(ReportIn reportInTemp) throws SQLException {
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
			stmt.addBatch("delete from report_in_info where rep_in_id=" + reportInTemp.getRepInId());
			stmt.addBatch("delete from report_in where rep_in_id=" + reportInTemp.getRepInId());
			stmt.addBatch("delete from qd_report_in_info where rep_in_id=" + reportInTemp.getRepInId());
			stmt.executeBatch();
			conn.commit();
			
		}catch(Exception e){
			conn.rollback();
			e.printStackTrace();			
			return false;
		}finally{				
			if(stmt!=null)
				stmt.close();
			if (conn != null){
				conn.close();
			}
		
		}
		
		return true;
	}

	public static boolean insertNXReport(Map reportMap, Operator operator, String reportFlg) {
		FitechMessages messages = new FitechMessages();
		AFReportForm reportInForm = null;
		ReportIn reportInTemp = null;
		String childRepId = (String) reportMap.get("templateid");
		String versionId = (String) reportMap.get("versionod");
		String dataRangeid = String.valueOf(reportMap.get("dataRgId")) ;
		String orgId = (String) reportMap.get("orgid");
		String time = (String) reportMap.get("times");
		String filePath = (String) reportMap.get("file");
		String repFreqId = String.valueOf(reportMap.get("freqID"));
		String qdFlg = String.valueOf(reportMap.get("QDFlg"));
		String year = null;
		String term = null;
		String day = null;
		if(!StringUtil.isEmpty(time)){
			year = time.substring(0, 4);
			term = time.substring(4, 6);
			day = time.substring(6, 8);
		}
		String reportName =null;
		boolean result = false;
		AfTemplate afTemplateInfo = null;
		try {
		// �����������
		afTemplateInfo =getAFTemplate(childRepId, versionId);
		reportName =afTemplateInfo.getTemplateName();
		int isreport = afTemplateInfo.getIsReport().intValue();
		/**��ñ���ı��֣�Ŀǰ��֧�ֶ���ֵ��������ɣ�*/
		String curId = String.valueOf(reportMap.get("curId"));
		if (curId == null || curId.equals(""))
			curId = "1";
		 MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);		

		Integer repInId = null;
		//Ԥ�Ȳ����¼�¼
		reportInForm = new AFReportForm();
		reportInForm.setTemplateId(childRepId);
		reportInForm.setVersionId(versionId);
		reportInForm.setDataRangeId(dataRangeid);
		reportInForm.setCurId(curId);
		reportInForm.setYear(year);
		reportInForm.setMonth(term);
		reportInForm.setRepFreqId(repFreqId);
		reportInForm.setDay(day);
		if(isreport != 2){
			reportInForm.setTimes("1");
			
			reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_PASS.toString());
		}else{
			reportInForm.setTimes("-2");
			reportInForm.setCheckFlag(com.fitech.net.config.Config.CHECK_FLAG_UNREPORT.toString());
		}
		
		reportInForm.setOrgId(orgId);
		
		reportInForm.setReportDate(new Date());
		
		//reportInForm.setc(mCurr.getCurName());
		
		reportInForm.setRepName(reportName);
		// ���뱨����Ϣ
		/***
		 * 20130117���ָ�ѡ���ɻ���REPORT_IN���в������ݣ����±����ѯ�����ظ�����ע�͸ù���
		 */
//		AfReport reportIn = insertNewReport(reportInForm,afTemplateInfo.getIsReport().intValue(),reportFlg);
//		reportInForm.setRepId(String.valueOf(reportIn.getRepId()));
		// �����౨��
		// ���뱨������
		/***
		 * 20130117���ָ�ѡ���ɻ���REPORT_IN���в������ݣ����±����ѯ�����ظ�����ע�͸ù���
		 */
//			if(!StringUtil.isEmpty(qdFlg) && qdFlg.equals(com.fitech.gznx.common.Config.PROFLG_SENCEN_QD)){
//
//				result = true;
//			}	else {
//				result =  insertReportData(reportInForm,filePath,reportFlg);
//			}			
			AFReportDealDelegate.deleteUploadFile(filePath);
//		}

		
		}catch (Exception e){
			messages.add(reportName+"����ʧ�ܣ�");
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * �����嵥ʽ��������
	 * @param reportInForm
	 * @param filePath
	 * @param reportFlg 
	 * @return
	 */
	private static boolean insertQDReportData(String templateId,String versionId,String repId,
			String filePath, String reportFlg) {
		
		DBConn conn = null;
		Connection connection=null;
		StringBuffer sql = null;
		FileInputStream inStream = null;
		Statement stmt = null;
		try
		{
			// ȡ���嵥ʽ��Ԫ��Ϣ
			QdCellinfo qdcell = AFQDCellInfoDelegate.getCellInfo(templateId, versionId);
			// ��ʼ��
			int startCol = convertColStringToNum(qdcell.getStartCol());
			// ������
			int endCol = convertColStringToNum(qdcell.getEndCol());
			// ��ʼ��
			int startRow = qdcell.getStartRow().intValue();
			// ������־
			int flgCol = convertColStringToNum(qdcell.getFlagCol());
			String endFlg = qdcell.getEndFlag();
			// ��ǰ��
			int row = startRow-1;
			
			File inputWorkbook  = new File(filePath);
			if(!inputWorkbook.exists()){
				return false;
			} 
			inStream = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(inStream);        
			HSSFSheet sheet = workbook.getSheetAt(0);
			conn = new DBConn();
			Session session = conn.openSession();
			connection=session.connection();
	    	connection.setAutoCommit(false);	    	
	    	stmt = connection.createStatement();
	    	boolean result = true;
	    	// ɾ����Ч����
			sql = new StringBuffer();
			sql.append("delete from qd_report_in_info where rep_in_id=" + repId);
			stmt.addBatch(sql.toString().toUpperCase());
	    	while(row<com.fitech.gznx.common.Config.MAX_ROW && result){
	    		for(int col=startCol-1;col<endCol;col++){
					HSSFCell excelcell = null;					
					try{
						if(sheet.getRow(row)==null){
							result = false;
							break;
						}
						excelcell = sheet.getRow(row).getCell((short) flgCol);
						String endflgvalue = getCellasString(excelcell);
						// �������ݲ�����������
						if(!StringUtil.isEmpty(endflgvalue) && endflgvalue.trim().equals(endFlg.trim())){
							result = false;
							break;
						}
						
						excelcell = sheet.getRow(row).getCell((short) col);
						String cellValue = getCellasString(excelcell);
						// �������ݲ�����������
						if(StringUtil.isEmpty(cellValue)){
							continue;
						}
						// ��Ԫ������
						String cellname = convertNumToCol(col)+(row+1);
						// ��������
						StringBuffer celldatasql = new StringBuffer();
						// 1104����
						if(reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
							celldatasql.append("insert into qd_report_in_info (rep_in_id,row_id,col_id,cell_name,report_value) values (").append(repId).
							append(" ,").append(row+1).append(",").append(col+1).append(",'").append(cellname).
							append("','").append(cellValue).append("')");
						}					
						
						stmt.addBatch(celldatasql.toString());
						
					}catch (Exception e){
						result = false;
						e.printStackTrace();						
					}
	    		}
	    		row++;
	    	}
			
			stmt.executeBatch();
			connection.commit();
			
		}catch (Exception e)
		{
			e.printStackTrace();
			return false; 
		}
		finally
		{
			if(inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null)
				conn.closeSession();
		}	
		return true;
	}
	/**
	 * �����嵥ʽ��������
	 * @param reportInForm
	 * @param filePath
	 * @param reportFlg 
	 * @return
	 */
	private static boolean insertNXQDReportData(String templateId,String versionId,String repId,
			String filePath) {
		
		DBConn conn = null;
		Connection connection=null;
		StringBuffer sql = null;
		FileInputStream inStream = null;
		Statement stmt = null;
		try
		{
			// ȡ���嵥ʽ��Ԫ��Ϣ
			QdCellinfo qdcell = AFQDCellInfoDelegate.getCellInfo(templateId, versionId);
			// ��ʼ��
			int startCol = convertColStringToNum(qdcell.getStartCol());
			// ������
			int endCol = convertColStringToNum(qdcell.getEndCol());
			// ��ʼ��
			int startRow = qdcell.getStartRow().intValue();
			// ������־
			int flgCol = convertColStringToNum(qdcell.getFlagCol());
			String endFlg = qdcell.getEndFlag();
			// ��ǰ��
			int row = startRow-1;
			
			File inputWorkbook  = new File(filePath);
			if(!inputWorkbook.exists()){
				return false;
			} 
			inStream = new FileInputStream(filePath);
			HSSFWorkbook workbook = new HSSFWorkbook(inStream);        
			HSSFSheet sheet = workbook.getSheetAt(0);
			conn = new DBConn();
			Session session = conn.openSession();
			connection=session.connection();
	    	connection.setAutoCommit(false);	    	
	    	stmt = connection.createStatement();
	    	boolean result = true;
	    	String tablename = "AF_QD_"+templateId.toUpperCase().trim();
	    	// ɾ����Ч����
			sql = new StringBuffer();
			sql.append("delete from "+tablename+" where rep_id=" + repId);
			stmt.addBatch(sql.toString().toUpperCase());
			// ȡ��sql���
			StringBuffer insertsql = new StringBuffer();
			insertsql.append("INSERT INTO ").append(tablename).append("(REP_ID,ROW_ID");
			for(int i=(short)startCol+1;i<=endCol+1;i++){
				insertsql.append(",COL").append(i);
			}
			insertsql.append(") VALUES (");
	    	while(row<com.fitech.gznx.common.Config.MAX_ROW && result){
	    		if(sheet.getRow(row) == null ){
					break;
				}
	    		HSSFCell excelcell = null;	
	    		excelcell = sheet.getRow(row).getCell((short) flgCol);
				String endflgvalue = getCellasString(excelcell);
				// �������ݲ�����������
				if(!StringUtil.isEmpty(endflgvalue) && endflgvalue.trim().equals(endFlg.trim())){
					result = false;
					break;
				}
				sql = new StringBuffer();
				sql.append(insertsql).append(repId).append(",").append(row+1);
				boolean insetflg = false;
	    		for(int col=startCol;col<=endCol;col++){
					excelcell = sheet.getRow(row).getCell((short) col);
					String cellValue = getCellasString(excelcell);
					// �������ݲ�����������
					if(StringUtil.isEmpty(cellValue)){
						sql.append(",null");
						continue;
					}
					sql.append(",'").append(cellValue.trim()).append("'");	
					insetflg = true;
	    		}
	    		sql.append(")");
	    		if(insetflg)
	    			stmt.addBatch(sql.toString());
	    		row++;
	    	}
			
			stmt.executeBatch();
			connection.commit();
			
		}catch (Exception e)
		{
			e.printStackTrace();
			return false; 
		}
		finally
		{
			if(inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null)
				conn.closeSession();
		}	
		return true;
	}
	/**
     * ���к�ת��Ϊ����
     * @param ref
     * @return
     */
    public static String convertNumToCol(int num) {
    	StringBuffer col= new StringBuffer() ;
    	if(num<26){
    		int acs = num + 'A';
    		col.append((char) acs);
    	}else{
    		int acs = num/26-1 + 'A';
    		int acs1 = num%26 + 'A';
    		col.append((char) acs).append((char) acs1);
    	}
    	return col.toString();
    }

	private static boolean insertReportData(AFReportForm reportInForm,String filePath,
			String reportFlg) {
		String templateId = reportInForm.getTemplateId();
		String versionId = reportInForm.getVersionId();
		DBConn conn = null;
		Connection connection=null;
		StringBuffer sql = null;
		FileInputStream inStream = null;
		Statement stmt = null;
		try
		{
		 File inputWorkbook  = new File(filePath);
           if(!inputWorkbook.exists()){
               return false;
           } 
           inStream = new FileInputStream(filePath);
           HSSFWorkbook workbook = new HSSFWorkbook(inStream);        
           HSSFSheet sheet = workbook.getSheetAt(0);
			conn = new DBConn();
			Session session = conn.openSession();
			connection=session.connection();
	    	connection.setAutoCommit(false);
	    	
	    	stmt = connection.createStatement();
			sql = new StringBuffer();
			sql.append("from AfCellinfo c where c.templateId='").append(templateId).append("' and c.versionId='").append(versionId).append("'");
			List sqlList = session.find(sql.toString());
			if (sqlList != null && sqlList.size() > 0) {
				for(int i=0;i<sqlList.size();i++){
					AfCellinfo cellInfo = (AfCellinfo) sqlList.get(i);
					HSSFCell excelcell = null;					
					try{
						int cellid = cellInfo.getCellId().intValue();
						int row =new Integer(cellInfo.getRowNum())-1;
						short col = (short) convertColStringToNum(cellInfo.getColNum());
						HSSFRow sheetrow = sheet.getRow(row);
						if(sheetrow == null ){
							continue;
						}
						excelcell = sheetrow.getCell(col);
						String cellValue = getCellasString(excelcell);
						// �������ݲ�����������
						if(StringUtil.isEmpty(cellValue)){
							continue;
						}
						// ��������
						StringBuffer celldatasql = new StringBuffer();
						if(reportFlg.equals("2")){
							celldatasql.append("insert into af_pbocreportdata (rep_id,cell_id,cell_data) values (").append(reportInForm.getRepId()).
							append(" ,").append(cellid).append(",'").append(cellValue).append("')");
						}else{
							celldatasql.append("insert into af_otherreportdata (rep_id,cell_id,cell_data) values (").append(reportInForm.getRepId()).
							append(" ,").append(cellid).append(",'").append(cellValue).append("')");
						}				
						stmt.addBatch(celldatasql.toString());
						
					}catch (Exception e){
						e.printStackTrace();
					}
				}
				stmt.executeBatch();
				connection.commit();
				
			}
		}catch (Exception e)
		{
			e.printStackTrace();
			return false; 
		}
		finally
		{
			if(inStream != null){
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null)
				conn.closeSession();
		}	
		return true;
	}

	/**
     * ���к�ת��Ϊ����
     * 
     * @param ref
     * @return
     */
    private static int convertColStringToNum(String ref) {
	int retval = 0;
	int pos = 0;
	for (int k = ref.length() - 1; k > -1; k--) {
	    char thechar = ref.charAt(k);
	    if (pos == 0)
		retval += Character.getNumericValue(thechar) - 9;
	    else
		retval += (Character.getNumericValue(thechar) - 9) * (pos * 26);
	    pos++;
	}
	return retval - 1;
    }
	
	private static AfReport insertNewReport(AFReportForm reportInForm, int isreport, String reportFlg) {
		if (reportInForm == null)
			return null;
		AfReport reportIn = null;
		boolean result = false;

		DBConn conn = null;
		try
		{
			conn = new DBConn();
			Session session = conn.beginTransaction();
			

		
			//��ѯ���ݿ����Ƿ��Ѿ���������ͬ��¼
			reportIn = getReportIn(reportInForm);
			//������ݿ����Ѿ������˸�����¼����ֱ�ӷ��ظļ�¼����Ϣ�������µĲ��������������������
			if(reportIn!=null)
			{
				// ��ѯ�౨��
				removeAFcell(reportIn.getRepId(),isreport,reportFlg);
				
			}
			//����������µ���Ϣ
			reportIn = new AfReport();
			copyVoToPersistence(reportIn, reportInForm);	
			session.save(reportIn);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		finally
		{
			if (conn != null)
				conn.endTransaction(result);
		}
		return reportIn;
	}



	private static void removeAFcell(Long repId, int isreport, String reportFlg) throws SQLException {
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
			if(isreport!=3){
				if(reportFlg.equals("2")){
					stmt.addBatch("delete from af_pbocreportdata where rep_id=" + repId);
				}else{
					stmt.addBatch("delete from af_otherreportdata where rep_id=" + repId);
				}				
			}
			stmt.addBatch("delete from af_report where rep_id=" + repId);
			stmt.executeBatch();
			conn.commit();
			
		}catch(Exception e){
			conn.rollback();
			e.printStackTrace();			
			
		}finally{				
			if(stmt!=null)
				stmt.close();
			if (conn != null){
				conn.close();
			}
		
		}
		
	}


	private static void copyVoToPersistence(AfReport reportIn,
			AFReportForm reportInForm) {
		reportIn.setTemplateId(reportInForm.getTemplateId());
		reportIn.setVersionId(reportInForm.getVersionId());
		reportIn.setCurId(new Long(reportInForm.getCurId()));
		reportIn.setYear(new Long(reportInForm.getYear()));
		reportIn.setTerm(new Long(reportInForm.getMonth()));
		reportIn.setDay(new Long(reportInForm.getDay()));
		reportIn.setOrgId(reportInForm.getOrgId());
		reportIn.setCheckFlag(Long.valueOf(reportInForm.getCheckFlag()));
		reportIn.setTimes(new Long(reportInForm.getTimes()));
		reportIn.setRepFreqId(new Long(reportInForm.getRepFreqId()));
		reportIn.setWriter(reportInForm.getWriter());
	}

	private static AfReport getReportIn(AFReportForm reportInForm) {
		AfReport report =null;
		DBConn conn = null;
		StringBuffer sql = null;
		try
		{
			conn = new DBConn();
			Session session = conn.openSession();
			sql = new StringBuffer();
			sql.append("from AfReport ri where ri.templateId='").append(reportInForm.getTemplateId())
			.append("' and ri.versionId='").append(reportInForm.getVersionId())
			.append("' and ri.orgId='").append(reportInForm.getOrgId())
			.append("' and ri.curId=").append(reportInForm.getCurId()).append(" and ri.year=")
			.append(reportInForm.getYear()).append(" and ri.term=").append(reportInForm.getMonth())
			.append(" and ri.day=").append(reportInForm.getDay()).append(" and ri.times=").append(reportInForm.getTimes());
			List sqlList = session.find(sql.toString());
			if (sqlList != null && sqlList.size() > 0) {
				report = (AfReport) sqlList.get(0);
				
			}
		}catch (Exception e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			if (conn != null)
				conn.closeSession();
		}
		return report;
	}
	
	/****
	 * ��ʹ��hibernate ���Ը� 2011-12-21
	 * Ӱ�����AfTemplate
	 * @param templateId
	 * @param versionId
	 * @return
	 */
	public static AfTemplate getAFTemplate(String templateId, String versionId) {
		AfTemplate afTemplate = null;

		if (StringUtil.isEmpty(templateId) || StringUtil.isEmpty(versionId))
			return null;

		DBConn conn = null;

		try {
			String hql = "from AfTemplate mcr where mcr.id.templateId='"
					+ templateId + "' and " + "mcr.id.versionId='"
					+ versionId + "'";

			conn = new DBConn();
			List list = conn.openSession().find(hql);
			if (list != null && list.size() > 0) {
				afTemplate = (AfTemplate) list.get(0);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			if (conn != null)
				conn.closeSession();
		}

		return afTemplate;
	}
	
	   /**
     * ��õ�Ԫ���ֵ
     * 
     * @author gongming
     * @date 2008-01-13
     * @param cell
     * @return
     */
    @SuppressWarnings("deprecation")
	private static String getCellasString(HSSFCell cell) {
	if (cell == null)
	    return "";

	String contents = null;
	if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA
		|| cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
	    try {
		contents = String.valueOf(cell.getNumericCellValue());
		if (contents.equals("#DIV/0!") || contents.equals("NaN"))
		    return "";
		if ("0.0".equals(contents))
		    contents = "0.00";
	    } catch (Exception e) {
		contents = "0.00";
	    }

	    boolean isPresentNumber = false;
	    /* ǧλ�ָ�����ݴ��� */
	    if (contents.indexOf(",") > -1)
		contents = contents.replaceAll(",", "");
	    /* �ٷ������ݴ��� */
	    if (contents.indexOf("%") > -1
		    && contents.indexOf("%") == contents.length() - 1) {
		isPresentNumber = true;
		contents = parsePresentToDouble(contents);
	    }
	    /* ��ѧ���������ݴ��� */
	    if (contents.indexOf('E') > -1 || contents.indexOf('e') > -1) {
		try {
		    // ��������ָ�ʽ�����ʽ��ΪС�������λ
		    if (!isPresentNumber)
			contents = new java.text.DecimalFormat("##0.000000")
				.format(Double.parseDouble(contents))
				.toString();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    contents = new java.text.DecimalFormat("##0.000000").format(
		    Double.parseDouble(contents)).toString();
	} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
		 contents = cell.getStringCellValue();
		 int length = contents.length()-1;
		 if(contents.charAt(length) == '%'){
			 String ss = contents.substring(0, length);
			 if(StringUtil.isFloat(ss)){
				 float cont = Float.parseFloat(ss);
				  BigDecimal b1 = new BigDecimal(Float.toString(cont));
			      BigDecimal b2 = new BigDecimal(new Integer(100).toString()); 
				 cont = b1.divide(b2).floatValue();
				 contents = String.valueOf(cont);
			 }
		 }
	}else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
	    contents = "";

	return contents;
    }
    
    /**
     * ���ٷ���ת��λС��
     * 
     * @param presentNumber
     * @return
     */
    private static String parsePresentToDouble(String presentNumber) {
	String contents = presentNumber
		.substring(0, presentNumber.indexOf("%"));
	String sign = "";
	if (contents.substring(0, 1).equals("+")
		|| contents.substring(0, 1).equals("-")) {
	    sign = String.valueOf(contents.charAt(0));
	    contents = contents.substring(1);
	}
	int dotIndex = contents.indexOf(".");

	if (dotIndex == -1)
	    contents = "0." + contents;
	else if (dotIndex == 0)
	    contents = "0.00" + contents.substring(1);
	else if (dotIndex == 1)
	    contents = "0.0" + contents.substring(0, 1) + contents.substring(2);
	else if (dotIndex == 2)
	    contents = "0." + contents.substring(0, 2) + contents.substring(3);
	else {
	    StringBuffer contentsStr = new StringBuffer(contents);
	    contentsStr.insert(dotIndex - 2, ".");
	    contentsStr.deleteCharAt(dotIndex + 1);
	    contents = contentsStr.toString();
	}

	return new java.text.DecimalFormat("##0.000000").format(
		Double.parseDouble(sign + contents)).toString();
    }
    
    /**
     * ��ʹ��hibernate ���Ը� 2011-12-21
     * Ӱ�����AfViewReport AfOrg AfReport
     * @param reportInForm
     * @param operator
     * @param curPage
     * @param reportFlg
     * @return
     */
	public static PageListInfo selectNOTYJHReportList(AFReportForm reportInForm,
			Operator operator,int curPage,String reportFlg) {    	 
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	PageListInfo pageListInfo = null;
    	try{
    		
    		if(reportInForm != null && reportInForm.getOrgId() != null){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			// �����±����������
    			String[] dates = reportInForm.getDate().split("-");

    			int term = Integer.parseInt(dates[1]);
    			
    			String rep_freq="";
    			if (term == 12)
					// rep_freq = "('��','��','����','��')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_YEAR;
				else if (term == 6)
					// rep_freq = "('��','��','����')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
				else if (term == 3 || term == 9)
					// rep_freq = "('��','��')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON;
				else if(term == 1)
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN;
				else
					// rep_freq = "('��')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							.toString();
    
    	String orgId = reportInForm.getOrgId();
    	if(StringUtil.isEmpty(orgId)){
    		orgId = operator.getOrgId();
    	}
//    	String templatename = StrutsCodeLibDelegate.getCodeLib(com.fitech.gznx.common.Config.CUSTOM_SEARCH,"1").getCodeName();
	    String hql="from AfViewReport a where ('"
					+ reportInForm.getDate()
					+ "' between a.startDate and a.endDate"
					+ " and a.comp_id.repFreqId in ("
					+ com.fitech.gznx.common.Config.FREQ_DAY + Config.SPLIT_SYMBOL_COMMA + com.fitech.gznx.common.Config.FREQ_TENDAY + Config.SPLIT_SYMBOL_COMMA
					 + rep_freq

					+ "))  and a.templateType='"+reportFlg+"'";
				//	+ " and a.templateName!='"+templatename+"'";
	    if(!orgId.equals(Config.DEFAULT_VALUE)){
	    	hql +=" and a.comp_id.orgId='"+orgId+"'";
	    }
		/**���ϱ���Ȩ��
		 * ���������ݿ��ж�*/
		if(operator.isSuperManager() == false){
			if(!orgId.equals(Config.DEFAULT_VALUE)){
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and '" + orgId + "'||a.comp_id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and '" + orgId + "'+a.comp_id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
			}else{
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and a.comp_id.orgId||a.comp_id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and a.comp_id.orgId+a.comp_id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
			}
		}
		String repName = reportInForm.getRepName();
		if(repName!=null && !"".equals(repName.trim())){
			hql += " and a.templateName like '%"+repName+"%' ";
			
		}
		/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
		if (reportInForm.getRepFreqId() != null
				&& !String.valueOf(reportInForm.getRepFreqId()).equals(
						Config.DEFAULT_VALUE)) {
			hql += " and a.comp_id.repFreqId="
					+ reportInForm.getRepFreqId();
		}
		String templateTypeid = reportInForm.getTemplateType();
		if(!StringUtil.isEmpty(templateTypeid)){
			hql += " and a.bak1 in ("+templateTypeid+") ";
		}
		if(reportInForm.getIsReport()!=null && reportInForm.getIsReport()>0){
			hql +=" and a.isReport="+reportInForm.getIsReport();
		}
		/**order by ���;ۺϺ�����ͻ �Ѿ�ע�� ���Ը� 2011-12-29
		 * ���������ݿ��ж�*/
		if(Config.DB_SERVER_TYPE.equals("oracle"))
			hql +=" order by a.priorityFlag,a.comp_id.templateId";
		if(Config.DB_SERVER_TYPE.equals("sqlserver") || Config.DB_SERVER_TYPE.equals("db2"))
			hql+="";

		pageListInfo = findByPageWithSQL(session,hql,Config.PER_PAGE_ROWS,curPage);

		List list=pageListInfo.getList();
			for(int i=0;i<list.size();i++){
				
				AfViewReport viewMReport=(AfViewReport)list.get(i);
				Aditing aditing = new Aditing();
				
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getComp_id().getRepFreqId());
				aditing.setChildRepId(viewMReport.getComp_id().getTemplateId());
				aditing.setVersionId(viewMReport.getComp_id().getVersionId());
				aditing.setRepName(viewMReport.getTemplateName());
				if (aditing.getActuFreqID() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil
							.getFreqDateLast(reportInForm.getDate(),
									aditing.getActuFreqID());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}
				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				aditing.setCurrName(viewMReport.getCurName());
				//aditing.setDataRgId(viewMReport.getComp_id().getDataRangeId());
				AfOrg org=(AfOrg)session.get(AfOrg.class, viewMReport.getComp_id().getOrgId());
				aditing.setOrgName(org.getOrgName());
				aditing.setOrgId(org.getOrgId());
				aditing.setDataRgTypeName(StrutsMDataRgTypeDelegate.getdatename(1));
				String sql = "from AfReport ri where ((ri.times =-2 and ri.checkFlag=3) or (ri.times =1 and ri.checkFlag=1)) and ri.versionId='" + aditing.getVersionId() + "'  and ri.curId="+aditing.getCurId()+
				 " and ri.templateId='" + aditing.getChildRepId() + "' and ri.year=" + aditing.getYear() + 
				 " and ri.term=" + aditing.getTerm() + " and ri.day=" + aditing.getDay()+ " and ri.orgId='" + org.getOrgId()  + "'  order by ri.repId desc";
	
				List reportInList = session.find(sql);
				if(reportInList != null && reportInList.size() > 0){
					aditing.setCheckFlag(new Short(new String("1")));					
				}else{
					aditing.setCheckFlag(new Short(new String("3")));				
				}
				resList.add(aditing);

    		}
			pageListInfo.setList(resList);
    		}	
    	}catch(HibernateException he){
    		he.printStackTrace();
    		if(conn != null) conn.endTransaction(true);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return pageListInfo;
    }
	/***
	 * jdbc���� oracle�﷨(rownum) ���޸� ���Ը� 2011-12-22
	 * rownum �޸�Ϊtop�÷�
	 * @param reportInForm
	 * @param operator
	 * @param offset
	 * @param limit
	 * @param reportFlg
	 * @return
	 */
	public static List selectNOTYJHExportReportList(AFReportForm reportInForm,
			Operator operator, int offset, int limit,String reportFlg ,int flag ) {    	 
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	try{
    		if(reportInForm != null ){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			// �����±����������
    			String[] dates = DateUtil.getFreqDateLast(reportInForm.getDate(),1).split("-");

    			int yueyear = Integer.parseInt(dates[0]);
    			int yueterm = Integer.parseInt(dates[1]);
    			int yueday = Integer.parseInt(dates[2]);
    			String[] yestoday = reportInForm.getDate().split("-");
    			int term = Integer.parseInt(yestoday[1]);
    			int year = Integer.parseInt(yestoday[0]);
    			int day = Integer.parseInt(yestoday[2]);
    			
    			// ����Ѯ�����
    			String[] xundate = DateUtil.getFreqDateLast(reportInForm.getDate(),5).split("-");
    			int xunterm = Integer.parseInt(xundate[1]);
    			int xunyear = Integer.parseInt(xundate[0]);
    			int xunday = Integer.parseInt(xundate[2]);
    		
    			String rep_freq="";
    			if (yueterm == 12)
					// rep_freq = "('��','��','����','��')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_YEAR;
				else if (yueterm == 6)
					// rep_freq = "('��','��','����')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
				else if (yueterm == 3 || yueterm == 9)
					// rep_freq = "('��','��')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON;
				else if(term == 1)
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN;
				else
					// rep_freq = "('��')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							.toString();
    

    			StringBuffer sql=null;
    			/**���������ݿ��ж�*/
    			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
    				sql= new StringBuffer("select top "+(offset+limit)+" a.org_id,d.org_name,a.template_id," +
						"a.version_id,a.template_name,a.rep_freq_id,a.cur_id , a.cur_name,a.rep_freq_name,b.check_flag,b.rep_id,a.supplement_flag from " +
						" VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id left join (select * from af_report   where times=1) b " +
								"    on a.org_id=b.org_id and a.template_id=b.template_id and a.version_id=b.version_id and a.cur_id=b.cur_id and a.rep_freq_id=b.rep_freq_id ");
    			if(Config.DB_SERVER_TYPE.equals("oracle"))
    				sql= new StringBuffer("select t.*,rownum from (select  a.org_id,d.org_name,a.template_id," +
						"a.version_id,a.template_name,a.rep_freq_id,a.cur_id , a.cur_name,a.rep_freq_name,b.check_flag,b.rep_id,a.supplement_flag from " +
						" VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id left join (select * from af_report   where times=1) b " +
								"    on a.org_id=b.org_id and a.template_id=b.template_id and a.version_id=b.version_id and a.cur_id=b.cur_id and a.rep_freq_id=b.rep_freq_id ");
    			if(Config.DB_SERVER_TYPE.equals("db2"))
    				sql= new StringBuffer("select * from ( select t.*,row_number() over(order by t.ORG_ID,t.supplement_flag,t.template_id,t.VERSION_ID) as rownum from (select  a.org_id,d.org_name,a.template_id," +
						"a.version_id,a.template_name,a.rep_freq_id,a.cur_id , a.cur_name,a.rep_freq_name,b.check_flag,b.rep_id,a.supplement_flag from " +
						" VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id left join (select * from af_report   where times=1) b " +
								"    on a.org_id=b.org_id and a.template_id=b.template_id and a.version_id=b.version_id and a.cur_id=b.cur_id and a.rep_freq_id=b.rep_freq_id ");				
				/**��ѯ����״̬Ϊ���ͨ������*/
				StringBuffer where = new StringBuffer();
				where.append(" where (((('"	+ reportInForm.getDate() + "' between a.start_date and a.end_date"
								+ " and a.rep_freq_id=6 and b.year="+year+" and b.term="+term+" and b.day="+ day +") or ('"
								+ reportInForm.getDate()
								+ "' between a.start_date and a.end_date"
								+ " and a.rep_freq_id=5 and b.year="+xunyear+" and b.term="+xunterm+" and b.day="+ xunday +") or ('"
								+ reportInForm.getDate()
								+ "' between a.start_date and a.end_date"
								+ " and a.rep_freq_id in (" + rep_freq
								// �������
								+ ") and  ((b.year="+yueyear+" and b.term="+yueterm+" and b.day="+yueday+" and a.rep_freq_id!=9) or (b.year="+yueyear+" and b.term=1 and b.day=1 and a.rep_freq_id=9)))) "
								//�������������޸ģ���������������������̣��˴˴��޸�Ϊ�ϱ�֮����ܲ鿴  �ϱ����CHECK_FLAG=0
								+" and b.check_flag="+Config.RH_EXP_REP_FLAG
								//+" and b.check_flag=1 "
								+" and a.is_report!=3) or ('"
								+ reportInForm.getDate()
								+ "' between a.start_date and a.end_date"
								+ " and (a.rep_freq_id in (" + rep_freq +",6)");
								// �������
								//+  "or (a.rep_freq_id=5 and CONVERT(int,DAY(getdate()))="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				// �������  ���������ݿ��ж�
				if(Config.DB_SERVER_TYPE.equals("oracle"))
					where.append(" or (a.rep_freq_id=5 and to_char(sysdate,'dd')="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				if(Config.DB_SERVER_TYPE.equals("db2")){
					where.append(" or (a.rep_freq_id=5 and day(current date)="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				}
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					where.append(" or (a.rep_freq_id=5 and CONVERT(int,DAY(getdate()))="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
				if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
					where.append(" and a.template_name like '%" + reportInForm.getRepName().trim() + "%'");
				}
				
				/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
				if(!StringUtil.isEmpty(reportInForm.getRepFreqId()) && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
					where.append(" and a.rep_freq_id=" + reportInForm.getRepFreqId() );
				}
				if(!StringUtil.isEmpty(reportInForm.getOrgId()) && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
					where.append(" and trim(a.ORG_ID)='" + reportInForm.getOrgId().trim() + "'");
				}
				/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
				if (reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")) {
//					if(Config.SYSTEM_SCHEMA_FLAG==1){
//						where.append(" and a.TEMPLATE_ID in ("
//								+ reportInForm.getTemplateId().trim() + ")");
//					}else{
//						
//					}
					where.append(" and upper(a.TEMPLATE_ID) like upper('%"
							+ reportInForm.getTemplateId().trim() + "%')");
				}
		
				/** ���������� */
				if (!StringUtil.isEmpty(reportInForm.getBak1()) && !reportInForm.getBak1().trim().equals(
								Config.DEFAULT_VALUE)) {
					where.append(" and a.BAK1 in (" + reportInForm.getBak1()+")");
				}
				/** �������� */
				if (reportInForm.getIsReport() != null
						&& !reportInForm.getIsReport().equals(
								Integer.valueOf(Config.DEFAULT_VALUE))) {
					where.append(" and a.IS_REPORT=" + reportInForm.getIsReport());
				}
				
				/**��ӱ���鿴Ȩ�ޣ������û�������ӣ�
				 * ���������ݿ��ж�*/
				if(flag ==0){
					if (operator.isSuperManager() == false){
						if (operator.getChildRepSearchPopedom() != null &&!operator.getChildRepSearchPopedom().equals(""))
						{
							if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
								where.append(" and a.ORG_ID||a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
							if(Config.DB_SERVER_TYPE.equals("sqlserver"))
								where.append(" and a.ORG_ID+a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
						}
					}
				}else{
					if (operator.isSuperManager() == false){
						if (operator.getChildRepReportPopedom() != null &&!operator.getChildRepReportPopedom().equals(""))
						{
							if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
								where.append(" and a.ORG_ID||a.template_id in ("+operator.getChildRepReportPopedom()+")");
							if(Config.DB_SERVER_TYPE.equals("sqlserver"))
								where.append(" and a.ORG_ID+a.template_id in ("+operator.getChildRepReportPopedom()+")");
						}
					}
				}
				/**��ӱ������β�ѯ */
				if (!StringUtil.isEmpty(reportInForm.getSupplementFlag()) && !reportInForm.getSupplementFlag().equals(Config.DEFAULT_VALUE)) {
					if(reportInForm.getSupplementFlag().equals("2")){
						where.append(" and (a.supplement_flag is null or a.supplement_flag=" + reportInForm.getSupplementFlag()+")");
					} else {
						where.append(" and a.supplement_flag=" + reportInForm.getSupplementFlag());
					}
				}
				if(Config.SYSTEM_SCHEMA_FLAG==1){
					where.append(toExpPbocSql());
				}
				if(Config.DB_SERVER_TYPE.equals("oracle"))
					sql.append(where.toString() + " order by a.ORG_ID,a.supplement_flag,a.template_id,a.VERSION_ID ) t where rownum<="+(offset+limit));
				if(Config.DB_SERVER_TYPE.equals("db2"))
					sql.append(where.toString() + " order by a.ORG_ID,a.supplement_flag,a.template_id,a.VERSION_ID ) t ) where rownum<="+(offset+limit));
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					sql.append(where.toString() + " order by a.ORG_ID,a.supplement_flag,a.template_id,a.VERSION_ID");
				//System.out.println("sql:"+sql);
				Connection connection =session.connection();
				ResultSet rs=connection.createStatement().executeQuery(sql.toString());
				
				int i=1;
				while(rs.next()){
					if(i<=offset){
						i++;
						continue;
					}
					Aditing aditing  = new Aditing();


					aditing.setRepName(rs.getString("TEMPLATE_NAME"));
					aditing.setActuFreqID(rs.getInt("REP_FREQ_ID"));
					aditing.setCurId(rs.getInt("CUR_ID"));
					//���ñ���������
//						aditing.setReportDate(reportInRecord.getReportDate());
					//���ñ�����
					aditing.setChildRepId(rs.getString("TEMPLATE_ID"));
					//���ñ���汾��
					aditing.setVersionId(rs.getString("VERSION_ID"));
					//���ñ����������
					aditing.setCurrName(rs.getString("CUR_NAME"));
					int batchId = rs.getInt("SUPPLEMENT_FLAG");
					if(batchId != 0){
						aditing.setBatchId(String.valueOf(batchId));
					}else{
						aditing.setBatchId("2");
					}
					
					if (aditing.getActuFreqID() != null) {
						// yyyy-mm-dd ��������ȷ�������ھ������������
						String trueDate = DateUtil
								.getFreqDateLast(reportInForm.getDate(),
										aditing.getActuFreqID());
						aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
						aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
						aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
					}
					
						//���ñ��Ϳھ�
				//	aditing.setDataRgTypeName(rs.getString("DATA_RG_DESC"));
					
						//���ñ���Ƶ��
					aditing.setActuFreqName(rs.getString("REP_FREQ_NAME"));
					if(rs.getString("REP_ID") != null){
					aditing.setRepInId(rs.getInt("REP_ID"));
					} else {
						aditing.setRepInId(0);
					}
					if(rs.getString("CHECK_FLAG") == null ){
						if(aditing.getRepInId() != null)
							aditing.setRepInId(0);
					}else
						aditing.set_checkFlag(Short.valueOf(rs.getString("CHECK_FLAG")));
					
					aditing.setOrgName(rs.getString("ORG_NAME"));
					aditing.setOrgId(rs.getString("ORG_ID"));

					resList.add(aditing);						
		    	}
    		}	
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    	
    		if(conn != null) conn.closeSession();
    	}
    	return resList;
    }
	
	/***
	 * jdbc���� ����oracle�﷨ ���Ը� 2011-12-22
	 * @param reportInForm
	 * @param operator
	 * @param reportFlg
	 * @return
	 */
	public static int selectNOTYJHExportReportCount(AFReportForm reportInForm,
			Operator operator,String reportFlg ,int flag ){
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;
		
		try{
			
			
			// �����±����������
			String[] dates = DateUtil.getFreqDateLast(reportInForm.getDate(),1).split("-");

			int yueyear = Integer.parseInt(dates[0]);
			int yueterm = Integer.parseInt(dates[1]);
			int yueday = Integer.parseInt(dates[2]);
			// �����ձ����
			String[] yestoday = reportInForm.getDate().split("-");
			int term = Integer.parseInt(yestoday[1]);
			int year = Integer.parseInt(yestoday[0]);
			int day = Integer.parseInt(yestoday[2]);
			// ����Ѯ�����
			String[] xundate = DateUtil.getFreqDateLast(reportInForm.getDate(),5).split("-");
			int xunterm = Integer.parseInt(xundate[1]);
			int xunyear = Integer.parseInt(xundate[0]);
			int xunday = Integer.parseInt(xundate[2]);
    		
			String rep_freq="";
			if (yueterm == 12)
				// rep_freq = "('��','��','����','��')";
				rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_SEASON
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_YEAR;
			else if (yueterm == 6)
				// rep_freq = "('��','��','����')";
				rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_SEASON
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
			else if (yueterm == 3 || yueterm == 9)
				// rep_freq = "('��','��')";
				rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
						+ Config.SPLIT_SYMBOL_COMMA
						+ com.fitech.gznx.common.Config.FREQ_SEASON;
			else if(term == 1)
				rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
				+ Config.SPLIT_SYMBOL_COMMA
				+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN;
			else
				// rep_freq = "('��')";
				rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
						.toString();
			StringBuffer sql = new StringBuffer("select count(*) from  VIEW_AF_REPORT a left join " +
					" (select * from af_report   where times=1) b " +
					"on a.org_id=b.org_id and a.template_id=b.template_id and a.version_id=b.version_id and a.cur_id=b.cur_id and a.rep_freq_id=b.rep_freq_id ");
			
							
			/**��ѯ����״̬Ϊ���ͨ������*/
			StringBuffer where = new StringBuffer();
			where.append(" where (((('"	+ reportInForm.getDate() + "' between a.start_date and a.end_date"
							+ " and a.rep_freq_id=6 and b.year="+year+" and b.term="+term+" and b.day="+ day +") or ('"
							+ reportInForm.getDate()
							+ "' between a.start_date and a.end_date"
							+ " and a.rep_freq_id =5 and b.year="+xunyear+" and b.term="+xunterm+" and b.day="+  xunday +") or ('"
							+ reportInForm.getDate()
							+ "' between a.start_date and a.end_date"
							+ " and a.rep_freq_id in (" + rep_freq
							+ ") and ((b.year="+yueyear+" and b.term="+yueterm+" and b.day="+yueday+" and a.rep_freq_id!=9) or (b.year="+yueyear+" and b.term=1 and b.day=1 and a.rep_freq_id=9)))) "
							//�������������޸ģ���������������������̣��˴˴��޸�Ϊ�ϱ�֮����ܲ鿴  �ϱ����CHECK_FLAG=0
							+" and b.check_flag="+Config.RH_EXP_REP_FLAG
							//+" and b.check_flag=1 "
							+" and a.is_report!=3) or ('"
							+ reportInForm.getDate()
							+ "' between a.start_date and a.end_date"
							+ " and (a.rep_freq_id in (" + rep_freq +",6)");
							//+ " or (a.rep_freq_id=5 and CONVERT(int,DAY(getdate()))="+ xunday +"))  and a.is_report=3)) and a.template_type='"+reportFlg+"' ");
			/**���������ݿ��ж�*/
			if(Config.DB_SERVER_TYPE.equals("oracle"))
				where.append(" or (a.rep_freq_id=5 and to_char(sysdate,'dd')="+ xunday +"))  and a.is_report=3)) and a.template_type='"+reportFlg+"'");
			if(Config.DB_SERVER_TYPE.equals("db2"))
				where.append(" or (a.rep_freq_id=5 and day(current date)="+ xunday +"))  and a.is_report=3)) and a.template_type='"+reportFlg+"'");
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
				where.append(" or (a.rep_freq_id=5 and CONVERT(int,DAY(getdate()))="+ xunday +"))  and a.is_report=3)) and a.template_type='"+reportFlg+"'");

			if(Config.SYSTEM_SCHEMA_FLAG==1){
				where.append(toExpPbocSql());
			}
			
			/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
				where.append(" and a.template_name like '%" + reportInForm.getRepName().trim() + "%'");
			}

			/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
			if(!StringUtil.isEmpty(reportInForm.getRepFreqId()) && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
				where.append(" and a.REP_FREQ_ID=" + reportInForm.getRepFreqId() );
			}
			if(!StringUtil.isEmpty(reportInForm.getOrgId()) && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
				where.append(" and trim(a.ORG_ID)='" + reportInForm.getOrgId().trim() + "'");
			}
			/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
			if (reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")) {
//				if(Config.SYSTEM_SCHEMA_FLAG==1){
//					where.append(" and a.TEMPLATE_ID in ("
//							+ reportInForm.getTemplateId().trim() + ")");
//				}else{
//					
//				}
				where.append(" and upper(a.TEMPLATE_ID) like upper('%"
						+ reportInForm.getTemplateId().trim() + "%')");
			}
	
			/** ���������� */
			if (!StringUtil.isEmpty(reportInForm.getBak1()) && !reportInForm.getBak1().trim().equals(
							Config.DEFAULT_VALUE)) {
				where.append(" and a.BAK1 in (" + reportInForm.getBak1()+")");
			}
			/** �������� */
			if (reportInForm.getIsReport() != null
					&& !reportInForm.getIsReport().equals(
							Integer.valueOf(Config.DEFAULT_VALUE))) {
				where.append(" and a.IS_REPORT=" + reportInForm.getIsReport());
			}
			
			/**��ӱ���鿴Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж�*/
//			if (operator.isSuperManager() == false){
//				if (operator.getChildRepSearchPopedom() == null || operator.getChildRepSearchPopedom().equals(""))
//					return count;
//				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
//					where.append(" and a.ORG_ID||a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
//				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
//					where.append(" and a.ORG_ID+a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
//			}
			if(flag==0){
				if (operator.isSuperManager() == false){
					if (operator.getChildRepSearchPopedom() != null &&!operator.getChildRepSearchPopedom().equals(""))
					{
						if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
							where.append(" and a.ORG_ID||a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
						if(Config.DB_SERVER_TYPE.equals("sqlserver"))
							where.append(" and a.ORG_ID+a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
					}
				}
			}else{
				if (operator.isSuperManager() == false){
					if (operator.getChildRepReportPopedom() != null &&!operator.getChildRepReportPopedom().equals(""))
					{
						if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
							where.append(" and a.ORG_ID||a.template_id in ("+operator.getChildRepReportPopedom()+")");
						if(Config.DB_SERVER_TYPE.equals("sqlserver"))
							where.append(" and a.ORG_ID+a.template_id in ("+operator.getChildRepReportPopedom()+")");
					}
				}
			}
			/**��ӱ������β�ѯ */
			if (!StringUtil.isEmpty(reportInForm.getSupplementFlag()) && !reportInForm.getSupplementFlag().equals(Config.DEFAULT_VALUE)) {
				if(reportInForm.getSupplementFlag().equals("2")){
					where.append(" and (a.supplement_flag is null or a.supplement_flag=" + reportInForm.getSupplementFlag()+")");
				} else {
					where.append(" and a.supplement_flag=" + reportInForm.getSupplementFlag());
				}
			}
			sql.append(where.toString());
			System.out.println("sql ="+sql);
			conn = new DBConn();
			session = conn.openSession();
			Connection connection =session.connection();
			ResultSet rs=connection.createStatement().executeQuery(sql.toString());
			
			if(rs.next()){
				count=rs.getInt(1);
			}
		} catch (Exception e) {
			count = 0;
			e.printStackTrace();
		} finally {
			//�ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}
	
	public static List selectNOTYJHReportList(AFReportForm reportInForm,
			Operator operator,String reportFlg) {    	 
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;

    	try{
    		if(reportInForm != null && reportInForm.getOrgId() != null){
    			conn = new DBConn();
    			session = conn.openSession();
    			// �����±����������
    			String[] dates = reportInForm.getDate().split("-");
    			
    			int term = Integer.parseInt(dates[1]);
    			String rep_freq="";
    			if (term == 12)					
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_YEAR;
				else if (term == 6)					
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
				else if (term == 3 || term == 9)					
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON;
				else if(term == 1)
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN;
				else					
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							.toString();
    
    			String orgId = reportInForm.getOrgId();
    	    	if(StringUtil.isEmpty(orgId)){
    	    		orgId = operator.getOrgId();
    	    	}
    	   // 	String templatename = StrutsCodeLibDelegate.getCodeLib(com.fitech.gznx.common.Config.CUSTOM_SEARCH,"1").getCodeName();
    	    String hql="from AfViewReport a where ('"
						+ reportInForm.getDate()
						+ "' between a.startDate and a.endDate"
						+ " and a.comp_id.repFreqId in ("
						+ com.fitech.gznx.common.Config.FREQ_DAY+ Config.SPLIT_SYMBOL_COMMA + com.fitech.gznx.common.Config.FREQ_TENDAY + Config.SPLIT_SYMBOL_COMMA
						 + rep_freq
						// �������
						+ ")) and a.isReport!=3 and a.templateType='"+reportFlg+"' ";
				//		+ " and a.templateName!='"+templatename+"'";
    	    if(!orgId.equals(Config.DEFAULT_VALUE)){
    	    	hql +=" and a.comp_id.orgId='"+orgId+"'";
    	    }
		/**���ϱ���Ȩ��*/
		if(operator.isSuperManager() == false){
			if(!orgId.equals(Config.DEFAULT_VALUE)){
				/**���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and '" + orgId + "'||a.comp_id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and '" + orgId + "'+a.comp_id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
			}else{
				/***���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and a.comp_id.orgId||a.comp_id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and a.comp_id.orgId+a.comp_id.templateId in ("+ operator.getChildRepSearchPopedom() +")";
			}
		}
		String repName = reportInForm.getRepName();
		if(repName!=null && !"".equals(repName.trim())){
			hql += " and a.templateName like '%"+repName+"%' ";
			
		}
		/** ��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ���� */
		if (reportInForm.getRepFreqId() != null
				&& !String.valueOf(reportInForm.getRepFreqId()).equals(
						Config.DEFAULT_VALUE)) {
			hql += " and a.comp_id.repFreqId="
					+ reportInForm.getRepFreqId();
		}
		String templateTypeid = reportInForm.getTemplateType();
		if(!StringUtil.isEmpty(templateTypeid)){
			hql += " and a.bak1 in ("+templateTypeid+") ";
		}
		hql +=" order by a.priorityFlag,a.comp_id.templateId";

		Query query = session.createQuery(hql);
		List list=query.list();
			for(int i=0;i<list.size();i++){
				
				AfViewReport viewMReport=(AfViewReport)list.get(i);
				Aditing aditing = new Aditing();
				
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getComp_id().getRepFreqId());
				aditing.setChildRepId(viewMReport.getComp_id().getTemplateId());
				aditing.setVersionId(viewMReport.getComp_id().getVersionId());
				aditing.setRepName(viewMReport.getTemplateName());
				if (aditing.getActuFreqID() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil.getFreqDateLast(reportInForm.getDate(),
									aditing.getActuFreqID());
					aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
				}
				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				//aditing.setDataRgId(viewMReport.getComp_id().getDataRangeId());
				AfOrg org=(AfOrg)session.get(AfOrg.class, viewMReport.getComp_id().getOrgId());
				if(org == null){
					continue;
				}
				aditing.setOrgName(org.getOrgName());
				aditing.setOrgId(org.getOrgId());
				aditing.setDataRgTypeName(StrutsMDataRgTypeDelegate.getdatename(1));
			

				resList.add(aditing);
	    		
    		}

    		}	
    	}catch(HibernateException he){
    		he.printStackTrace();
    		
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return resList;
    }
	
	/***
	 * jdbc���� ����oracle�﷨(upper) ���Ը� 2011-12-22
	 * @param reportInForm
	 * @param operator
	 * @return
	 */
	public static int selectYJHExportReportCount(AFReportForm reportInForm,
			Operator operator ,int flag ) {
		int count = 0;
		DBConn conn = null;
		Session session = null;

		if (reportInForm == null || operator == null)
			return count;
		
		try{
			String[] dates = reportInForm.getDate().split("-");

			int year = Integer.parseInt(dates[0]);
			int term = Integer.parseInt(dates[1]);

			String rep_freq = "";
			if (term == 12)
				rep_freq = "('��','��','����','��')";
			else if (term == 6)
				rep_freq = "('��','��','����')";
			else if (term == 3 || term == 9)
				rep_freq = "('��','��')";
			else
				rep_freq = "('��')";
			StringBuffer sql = new StringBuffer("select count(*) from  VIEW_M_REPORT a left join  " +
					" (select * from report_in   where year="+year+" and term="+term+" and times=1) b " +
					" on a. ORG_ID=b.org_id and a.CHILD_REP_ID=b.CHILD_REP_ID and a.VERSION_ID=b.VERSION_ID 	and a.CUR_ID=b.CUR_ID and a.DATA_RANGE_ID=b.DATA_RANGE_ID ");
			
							
			/**��ѯ����״̬Ϊ���ͨ������*/
			StringBuffer where = new StringBuffer(" where a.START_DATE<='"+year+"-"+term+"-01' and a.END_DATE>='"+year+"-"+term+"-01' and a.REP_FREQ_NAME in "+rep_freq);

			/**��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ��*/
			if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")){				
//				if(Config.SYSTEM_SCHEMA_FLAG==1){
//					where.append(" and a.CHILD_REP_ID in ("
//							+ reportInForm.getTemplateId().trim() + ")");
//				}else{
//					
//				}
				where.append(" and upper(a.CHILD_REP_ID) like upper('%" 
						+ reportInForm.getTemplateId().trim() + "%')");
			}
			
			/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
			if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
				where.append(" and a.REPORT_NAME like '%" + reportInForm.getRepName().trim() + "%'");
			}

			/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
			if(reportInForm.getRepFreqId() != null && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
				where.append(" and a.REP_FREQ_ID=" + reportInForm.getRepFreqId() );
			}
			if(!StringUtil.isEmpty(reportInForm.getOrgId()) && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
				where.append(" and a.ORG_ID='" + reportInForm.getOrgId().trim() + "'");
			}
			/**��ӱ���״̬��δ���/���ͨ��/���δͨ������ѯ����*/
			//�������������޸ģ���������������������̣��˴˴��޸�Ϊ�ϱ�֮����ܲ鿴  �ϱ����CHECK_FLAG=0
			if(!Config.HZLHYH)
				where.append(" and b.CHECK_FLAG="+Config.YJ_EXP_REP_FLAG);
			else
				where.append(" and b.CHECK_FLAG="+Config.CHECK_FLAG_UNCHECK);
			if(Config.SYSTEM_SCHEMA_FLAG==1){
				where.append(toExpCbrcSql());
			}
			/**��ӱ���鿴Ȩ�ޣ������û�������ӣ�
			 * ���������ݿ��ж�*/
			if(flag == 0 ){
				if (operator.isSuperManager() == false){
					if (operator.getChildRepSearchPopedom() != null &&!operator.getChildRepSearchPopedom().equals(""))
					{
						if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
							where.append(" and a.ORG_ID || a.CHILD_REP_ID in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
						if(Config.DB_SERVER_TYPE.equals("sqlserver"))
							where.append(" and a.ORG_ID+a.CHILD_REP_ID in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
					}
				}
			}else{
				if (operator.isSuperManager() == false){
					if (operator.getChildRepReportPopedom() != null &&!operator.getChildRepReportPopedom().equals(""))
					{
						if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
							where.append(" and a.ORG_ID || a.CHILD_REP_ID in ("+operator.getChildRepReportPopedom()+")");
						if(Config.DB_SERVER_TYPE.equals("sqlserver"))
							where.append(" and a.ORG_ID+a.CHILD_REP_ID in ("+operator.getChildRepReportPopedom()+")");
					}
				}
			}
			sql.append(where.toString());
			System.out.println("sql:"+sql);
			conn = new DBConn();
			session = conn.openSession();
			Connection connection =session.connection();
			ResultSet rs=connection.createStatement().executeQuery(sql.toString());
			
			if(rs.next()){
				count=rs.getInt(1);
			}
		} catch (Exception e) {
			count = 0;
			e.printStackTrace();
		} finally {
			//�ر����ݿ�����
			if (conn != null)
				conn.closeSession();
		}
		return count;
	}
	public static String toExpCbrcSql(){
		StringBuffer sql = new StringBuffer("");
		sql.append(" and a.CHILD_REP_ID in(");
		sql.append(" select w3.template_id from work_task_moni w1 ");
		sql.append(" join");
		sql.append(" (");
		sql.append(" select m1.* from work_task_node_moni m1 join work_task_node_info m2 ");
		sql.append(" on m1.node_id=m2.node_id where m1.node_id ");
		sql.append(" not in(select pre_node_id from work_task_node_info)");
		sql.append(" )w2 ");
		sql.append(" on w1.task_moni_id=w2.task_moni_id");
		sql.append(" join");
		sql.append("  work_task_node_object_moni w3 on w2.task_moni_id=w3.task_moni_id and w2.node_id=w3.node_id and w2.org_id=w3.org_id ");
		sql.append(" where w2.node_flag=3 and w2.final_exec_flag=1 and w3.node_io_flag=1 and b.org_id=w3.org_id and a.CHILD_REP_ID=w3.template_id");
		sql.append(" and b.year=w1.year and b.term=w1.term");
		sql.append(" )");
		return sql.toString();
	}
	public static String toExpPbocSql(){
		StringBuffer sql = new StringBuffer("");
		sql.append(" and a.template_id in(");
		sql.append(" select w3.template_id from work_task_moni w1 ");
		sql.append(" join");
		sql.append(" (");
		sql.append(" select m1.* from work_task_node_moni m1 join work_task_node_info m2 ");
		sql.append(" on m1.node_id=m2.node_id where m1.node_id ");
		sql.append(" not in(select pre_node_id from work_task_node_info)");
		sql.append(" )w2 ");
		sql.append(" on w1.task_moni_id=w2.task_moni_id");
		sql.append(" join");
		sql.append("  work_task_node_object_moni w3 on w2.task_moni_id=w3.task_moni_id and w2.node_id=w3.node_id and w2.org_id=w3.org_id ");
		sql.append(" where w2.node_flag=3 and w2.final_exec_flag=1 and w3.node_io_flag=1 and b.org_id=w3.org_id and a.template_id=w3.template_id");
		sql.append(" and b.year=w1.year and b.term=w1.term");
		sql.append(" )");
		return sql.toString();
	}
	public static String toExpPbocTextSql(){
		StringBuffer sql = new StringBuffer("");
		sql.append(" and a.template_id in(");
		sql.append(" select w3.template_id from work_task_moni w1 ");
		sql.append(" join");
		sql.append(" (");
		sql.append(" select m1.* from work_task_node_moni m1 join work_task_node_info m2 ");
		sql.append(" on m1.node_id=m2.node_id where m1.node_id ");
		sql.append(" not in(select pre_node_id from work_task_node_info)");
		sql.append(" )w2 ");
		sql.append(" on w1.task_moni_id=w2.task_moni_id");
		sql.append(" join");
		sql.append("  work_task_node_object_moni w3 on w2.task_moni_id=w3.task_moni_id and w2.node_id=w3.node_id and w2.org_id=w3.org_id ");
		sql.append(" where w2.node_flag=3 and w2.final_exec_flag=1 and w3.node_io_flag=1 and a.org_id=w3.org_id and a.template_id=w3.template_id");
		sql.append(" and a.year=w1.year and a.term=w1.term");
		sql.append(" )");
		return sql.toString();
	}
	/**
	 * jdbc���� ������oracle�﷨ ���ܲ���Ҫ�޸� ���Ը� 2011-12-27
	 * Ӱ���af_report af_template af_org
	 * �������и�ʽ�����ı���
	 * @param reportInForm
	 * @param operator
	 * @param reportFlg
	 * @return
	 */
	public static List selectRhExportReportList(AFReportForm reportInForm,
			Operator operator, String reportFlg) {
		
		// TODO Auto-generated method stub
		List resList = new ArrayList();
		DBConn conn = null;
		Session session = null;
		
		try {	
			if (reportInForm != null && reportInForm.getOrgId() != null) {
				
				conn = new DBConn();
				session = conn.beginTransaction();
				String[] dates = reportInForm.getDate().split("-");
				
				StringBuffer sql = new StringBuffer("select d.*,mrf.rep_freq_name " +
						"from (select c.*,ao.org_name " +
						"from (select a.rep_id, a.rep_name, a.org_id, a.rep_freq_id, " +
						"a.template_id, a.version_id, b.supplement_flag " +
						"from af_report a " +
						"inner join (select * from af_template bb " +
						"where bb.template_type = 2 " +
						"and bb.is_report = 2 " +
						"and bb.using_flag = 1 ");
				
				if(reportInForm.getSupplementFlag()!= null && 
						!reportInForm.getSupplementFlag().equals("") && 
						!reportInForm.getSupplementFlag().equals(Config.DEFAULT_VALUE) &&
						reportInForm.getRepFreqId().equals("1")){
					sql.append("and bb.supplement_flag='"+reportInForm.getSupplementFlag().trim()+"'");
				}
				
				sql.append(") b on b.template_id=a.template_id and b.version_id=a.version_id " +
							"where a.times='1' and a.rep_name is not null " );

				// �����ѯ����
				StringBuffer where = new StringBuffer();
				
				//Ƶ�Ȳ�ѯ
				if(reportInForm.getRepFreqId() != null && !reportInForm.getRepFreqId().equals("")){				
					where.append(" and a.rep_freq_id='"+reportInForm.getRepFreqId().trim()+"'");					
				}
				
				//������ѯ
				if(reportInForm.getOrgId()!= null && 
						!reportInForm.getOrgId().equals("") && 
						!reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
					
					where.append(" and a.org_id='"+reportInForm.getOrgId().trim()+"'");
					
				}else{
					
					where.append(" and a.org_id in (select t.org_id from af_org t " +
							"where t.org_type='-99' or t.pre_org_id=0)");
				}
				
				//���ڲ�ѯ
				if(reportInForm.getDate()!= null && !reportInForm.getDate().equals("")){
					where.append(" and a.year='"+dates[0].trim()+"' and a.term='"+dates[1]+"'");
				}
				/**order by ���� ��ۺϺ�����ͻ �Ѿ�ע�� ���Ը� 2011-12-29
				 * ���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
				{
					sql.append(where.toString() + " and a.check_flag='1' order by a.org_id, a.rep_id) c left join af_org ao on " +
							"ao.org_id=c.org_id )d left join m_rep_freq mrf on mrf.rep_freq_id=d.rep_freq_id "+
							"order by d.template_id,d.org_id");
				}
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
				{
					sql.append(where.toString() + " and a.check_flag='1') c left join af_org ao on " +
					"ao.org_id=c.org_id )d left join m_rep_freq mrf on mrf.rep_freq_id=d.rep_freq_id ");
				}
				System.out.println(sql.toString());
				Connection connection = session.connection();
				ResultSet rs = connection.createStatement().executeQuery(sql.toString());
				
				while (rs.next()) {
					Aditing aditing = new Aditing();
					//����id
					aditing.setRepInId(rs.getInt("rep_id"));
					//��������
					aditing.setRepName(rs.getString("rep_name"));
					// ����ID
					aditing.setOrgId(rs.getString("org_id"));
					//����name
					aditing.setOrgName(rs.getString("org_name"));
					//Ƶ��id
					aditing.setActuFreqID(rs.getInt("rep_freq_id"));
					//Ƶ��name
					aditing.setActuFreqName(rs.getString("rep_freq_name"));
					aditing.setVersionId(rs.getString("version_id"));
					aditing.setTemplateId(rs.getString("template_id"));
					// ���ñ�������
					int batchId = rs.getInt("supplement_flag");
					if (batchId != 0) {
						aditing.setBatchId(String.valueOf(batchId));
					} else {
					//����Ϊ�յĶ���Ϊ�ڶ���
						aditing.setBatchId("2");
					}
					resList.add(aditing);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}
	
	/***
	 * jdbc���� ��Ҫ�޸� ���Ը� 2011-12-22
	 * �˴��޸�oracle to_char����Ϊsqlserver�÷� ������
	 * Ӱ���VIEW_AF_REPORT af_report VIEW_ORG_REP
	 * ���Ը� 2011-12-27
	 */
	public static List searchReportRecord(AFReportForm reportInForm,
			Operator operator, String reportFlg) {    	 
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	PageListInfo pageListInfo = null;
    	try{
    		if(reportInForm != null && reportInForm.getOrgId() != null){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			// �����±����������
    			String[] dates = DateUtil.getFreqDateLast(reportInForm.getDate(),1).split("-");

    			int yueyear = Integer.parseInt(dates[0]);
    			int yueterm = Integer.parseInt(dates[1]);
    			int yueday = Integer.parseInt(dates[2]);
    			String[] yestoday = reportInForm.getDate().split("-");
    			int term = Integer.parseInt(yestoday[1]);
    			int year = Integer.parseInt(yestoday[0]);
    			int day = Integer.parseInt(yestoday[2]);
    			
    			// ����Ѯ�����
    			String[] xundate = DateUtil.getFreqDateLast(reportInForm.getDate(),5).split("-");
    			int xunterm = Integer.parseInt(xundate[1]);
    			int xunyear = Integer.parseInt(xundate[0]);
    			int xunday = Integer.parseInt(xundate[2]);
    		
    			String rep_freq="";
    			if (yueterm == 12)
					// rep_freq = "('��','��','����','��')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_YEAR;
				else if (yueterm == 6)
					// rep_freq = "('��','��','����')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
				else if (yueterm == 3 || yueterm == 9)
					// rep_freq = "('��','��')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							+ Config.SPLIT_SYMBOL_COMMA
							+ com.fitech.gznx.common.Config.FREQ_SEASON;
				else if(term == 1)
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN;
				else
					// rep_freq = "('��')";
					rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
							.toString();
    

				
				StringBuffer sql = new StringBuffer(" select  a.org_id,d.org_name,a.template_id," +
						"a.version_id,a.template_name,a.rep_freq_id,a.cur_id , a.cur_name,a.rep_freq_name,b.check_flag,b.rep_id,a.supplement_flag, a.report_style from " +
						" VIEW_AF_REPORT a left join af_org d on a.org_id=d.org_id left join (select * from af_report   where times=1) b " +
								"    on a.org_id=b.org_id and a.template_id=b.template_id and a.version_id=b.version_id and a.cur_id=b.cur_id and a.rep_freq_id=b.rep_freq_id ");
						
				/**��ѯ����״̬Ϊ���ͨ������*/
				StringBuffer where = new StringBuffer();
				/**�˴��޸�oracle to_char����Ϊsqlserver�÷�
				 * ���Ը� 2011-12-27*/
				where.append(" where (((('"	+ reportInForm.getDate() + "' between a.start_date and a.end_date"
								+ " and a.rep_freq_id=6 and b.year="+year+" and b.term="+term+" and b.day="+ day +") or ('"
								+ reportInForm.getDate()
								+ "' between a.start_date and a.end_date"
								+ " and a.rep_freq_id=5 and b.year="+xunyear+" and b.term="+xunterm+" and b.day="+ xunday +") or ('"
								+ reportInForm.getDate()
								+ "' between a.start_date and a.end_date"
								+ " and a.rep_freq_id in (" + rep_freq
								// �������
								+ ") and  ((b.year="+yueyear+" and b.term="+yueterm+" and b.day="+yueday+" and a.rep_freq_id!=9) or (b.year="+yueyear+" and b.term=1 and b.day=1 and a.rep_freq_id=9)))) and b.check_flag=1  and a.is_report!=3) or ('"
								+ reportInForm.getDate()
								+ "' between a.start_date and a.end_date"
								+ " and (a.rep_freq_id in (" + rep_freq);
				// �������
				/**�˴��޸�oracle to_char����Ϊsqlserver�÷�
				 * ���Ը� 2011-12-27 ���������ݿ��ж�*/
				if(Config.DB_SERVER_TYPE.equals("oracle"))
						where.append(",6) or (a.rep_freq_id=5 and to_char(sysdate,'dd')="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				if(Config.DB_SERVER_TYPE.equals("db2")){
					where.append(",6) or (a.rep_freq_id=5 and day(current date)="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				}				
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
						where.append(",6) or (a.rep_freq_id=5 and day(getdate())="+ xunday +")) and a.is_report=3))and  a.template_type='"+reportFlg+"' ");
				/**��ӱ������Ʋ�ѯ������ģ����ѯ��*/
				if(reportInForm.getRepName() != null && !reportInForm.getRepName().equals("")){
					where.append(" and a.template_name like '%" + reportInForm.getRepName().trim() + "%'");
				}
				
				/**��ӱ���Ƶ�ȣ���/��/����/�꣩��ѯ����*/
				if(reportInForm.getRepFreqId() != null && !String.valueOf(reportInForm.getRepFreqId()).equals(Config.DEFAULT_VALUE)){
					where.append(" and a.rep_freq_id=" + reportInForm.getRepFreqId() );
				}
				if(!StringUtil.isEmpty(reportInForm.getOrgId()) && !reportInForm.getOrgId().equals(Config.DEFAULT_VALUE)){
					where.append(" and a.ORG_ID='" + reportInForm.getOrgId().trim() + "'");
				}
				/** ��ӱ����Ų�ѯ���������Դ�Сдģ����ѯ�� */
				if (reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().equals("")) {
					where.append(" and upper(a.TEMPLATE_ID) like upper('%"
							+ reportInForm.getTemplateId().trim() + "%')");
				}
		
				/** ���������� */
				if (!StringUtil.isEmpty(reportInForm.getBak1()) && !reportInForm.getBak1().trim().equals(
								Config.DEFAULT_VALUE)) {
					where.append(" and a.BAK1 in (" + reportInForm.getBak1()+")");
				}
				/** �������� */
				if (reportInForm.getIsReport() != null
						&& !reportInForm.getIsReport().equals(
								Integer.valueOf(Config.DEFAULT_VALUE))) {
					where.append(" and a.IS_REPORT=" + reportInForm.getIsReport());
				}
				
				/**��ӱ���鿴Ȩ�ޣ������û�������ӣ�
				 * ���������ݿ��ж�*/
				if (operator.isSuperManager() == false){
					if (operator.getChildRepSearchPopedom() != null &&!operator.getChildRepSearchPopedom().equals(""))
					{	
						if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
							where.append(" and a.ORG_ID||a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
						if(Config.DB_SERVER_TYPE.equals("sqlserver"))
							where.append(" and a.ORG_ID+a.template_id in (select ORG_REP_ID  from VIEW_ORG_REP where POW_TYPE=2 and USER_ID="+operator.getOperatorId()+")");
					}
				}

				
				sql.append(where.toString() + " order by a.ORG_ID,a.supplement_flag,a.template_id,a.VERSION_ID ");
				Connection connection =session.connection();
				ResultSet rs=connection.createStatement().executeQuery(sql.toString());
				

				while(rs.next()){
					
					Aditing aditing  = new Aditing();


					aditing.setRepName(rs.getString("TEMPLATE_NAME"));
					aditing.setActuFreqID(rs.getInt("REP_FREQ_ID"));
					aditing.setCurId(rs.getInt("CUR_ID"));
					//���ñ���������
//						aditing.setReportDate(reportInRecord.getReportDate());
					//���ñ�����
					aditing.setChildRepId(rs.getString("TEMPLATE_ID"));
					//���ñ���汾��
					aditing.setVersionId(rs.getString("VERSION_ID"));
					//���ñ����������
					aditing.setCurrName(rs.getString("CUR_NAME"));
					int batchId = rs.getInt("SUPPLEMENT_FLAG");
					if(batchId != 0){
						aditing.setBatchId(String.valueOf(batchId));
					}else{
						aditing.setBatchId("2");
					}
					
					if (aditing.getActuFreqID() != null) {
						// yyyy-mm-dd ��������ȷ�������ھ������������
						String trueDate = DateUtil
								.getFreqDateLast(reportInForm.getDate(),
										aditing.getActuFreqID());
						aditing.setYear(Integer.valueOf(trueDate.substring(0, 4)));
						aditing.setTerm(Integer.valueOf(trueDate.substring(5, 7)));
						aditing.setDay(Integer.valueOf(trueDate.substring(8, 10)));
					}
					
						//���ñ��Ϳھ�
				//	aditing.setDataRgTypeName(rs.getString("DATA_RG_DESC"));
					
						//���ñ���Ƶ��
					aditing.setActuFreqName(rs.getString("REP_FREQ_NAME"));
					if(rs.getString("REP_ID") != null){
					aditing.setRepInId(rs.getInt("REP_ID"));
					} else {
						aditing.setRepInId(0);
					}
					if(rs.getString("CHECK_FLAG") == null ){
						if(aditing.getRepInId() != null)
							aditing.setRepInId(0);
					}else
						aditing.set_checkFlag(Short.valueOf(rs.getString("CHECK_FLAG")));
					
					aditing.setOrgName(rs.getString("ORG_NAME"));
					aditing.setOrgId(rs.getString("ORG_ID"));
					
					if(rs.getString("REPORT_STYLE") != null){
						aditing.setReportStyle(rs.getInt("REPORT_STYLE"));
					}

					resList.add(aditing);						
		    	}
    		}	
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    	
    		if(conn != null) conn.closeSession();
    	}
    	return resList;
    }

	
	/***
	 * ʹ��hibernate ���Ը� 2011-12-22
	 * Ӱ�����AfViewReport AfReport
	 * @param afReportForm
	 * @param operator
	 * @param curPage
	 * @return
	 */
	public static PageListInfo selectDownloadReportList(
			AFReportForm afReportForm, Operator operator, int curPage) {    	 
    	List resList = new ArrayList();
    	DBConn conn = null;
    	Session session = null;
    	PageListInfo pageListInfo = null;
    	try{    		
    		if(afReportForm != null ){
    			conn = new DBConn();
    			session = conn.beginTransaction();
    			String hql = "from AfViewReport a where a.templateType='"
					+ afReportForm.getTemplateType()+"'"
					+ getValidDateSql(afReportForm);
//					+ " and a.comp_id.orgId='" + operator.getOrgId() + "'";

			/** ���ϱ���Ȩ�� 
			 * ���������ݿ��ж�*/
			if (operator.isSuperManager() == false) {
				if(Config.DB_SERVER_TYPE.equals("oracle") || Config.DB_SERVER_TYPE.equals("db2"))
					hql += " and a.comp_id.orgId||a.comp_id.templateId in ("
						+ operator.getChildRepReportPopedom() + ")";
				if(Config.DB_SERVER_TYPE.equals("sqlserver"))
					hql += " and a.comp_id.orgId+a.comp_id.templateId in ("
						+ operator.getChildRepReportPopedom() + ")";
			}
			/** ����������� */
			if (afReportForm.getTemplateId() != null
					&& !afReportForm.getTemplateId().equals("")) {
				hql += " and a.comp_id.templateId like '%" + afReportForm.getTemplateId()+"%'";
			}

			if (afReportForm.getRepName() != null && !"".equals(afReportForm.getRepName().trim())) {
				hql += " and a.templateName like '%" + afReportForm.getRepName() + "%' ";
			}
			/** ���������� */
			if (afReportForm.getBak1() != null
					&& !afReportForm.getBak1().trim().equals(
							Config.DEFAULT_VALUE)
					&& !afReportForm.getBak1().equals("")) {
				hql += " and a.bak1 in (" + afReportForm.getBak1()+")";
			}
			/** ����������� */
			if (afReportForm.getOrgId() != null
					&& !afReportForm.getOrgId().trim().equals(Config.DEFAULT_VALUE)
					&& !afReportForm.getOrgId().trim().equals("")) {
				hql += " and a.comp_id.orgId in ('" + afReportForm.getOrgId()+"')";
			}
			/** ����Ƶ�� */
			if (afReportForm.getRepFreqId() != null
					&& !afReportForm.getRepFreqId().trim().equals(Config.DEFAULT_VALUE)
					&& !afReportForm.getRepFreqId().equals("")) {
				hql += " and a.comp_id.repFreqId =" + afReportForm.getRepFreqId();
			}
			hql += " and a.isReport in (";
					//+ com.fitech.gznx.common.Config.TEMPLATE_REPORT
					//+ ","
					/**order by ���;ۺϺ�����ͻ �Ѿ�ע�� ���Ը� 2011-12-28
					 * ���������ݿ��ж�*/
			if(Config.DB_SERVER_TYPE.equals("oracle"))
				hql+= com.fitech.gznx.common.Config.TEMPLATE_ANALYSIS+") order by a.comp_id.templateId, a.comp_id.repFreqId";
			if(Config.DB_SERVER_TYPE.equals("sqlserver")  || Config.DB_SERVER_TYPE.equals("db2"))
				hql+= com.fitech.gznx.common.Config.TEMPLATE_ANALYSIS+")";

			pageListInfo = findByPageWithSQL(session,hql,Config.PER_PAGE_ROWS,curPage);
	
			List list=pageListInfo.getList();
			for(int i=0;i<list.size();i++){
				AfViewReport viewMReport = (AfViewReport) list.get(i);

				Aditing aditing = new Aditing();
				// aditing.setDataRgTypeName(viewMReport.getDataRgTypeName());
				aditing.setActuFreqName(viewMReport.getRepFreqName());
				aditing.setActuFreqID(viewMReport.getComp_id()
						.getRepFreqId());
				aditing.setTemplateId(viewMReport.getComp_id()
						.getTemplateId());
				aditing.setVersionId(viewMReport.getComp_id()
						.getVersionId());
				aditing.setRepName(viewMReport.getTemplateName());

				if (viewMReport.getComp_id().getRepFreqId() != null) {
					// yyyy-mm-dd ��������ȷ�������ھ������������
					String trueDate = DateUtil.getFreqDateLast(afReportForm
							.getDate(), viewMReport.getComp_id()
							.getRepFreqId());
					aditing.setYear(Integer.valueOf(trueDate
							.substring(0, 4)));
					aditing.setTerm(Integer.valueOf(trueDate
							.substring(5, 7)));
					aditing.setDay(Integer.valueOf(trueDate
							.substring(8, 10)));
				}

				aditing.setCurrName(viewMReport.getCurName());
				aditing.setCurId(viewMReport.getComp_id().getCurId());
				// aditing.setDataRgId(viewMReport.getComp_id().getDataRangeId());

				AfOrg org = (AfOrg) session.get(AfOrg.class, viewMReport
						.getComp_id().getOrgId());
				aditing.setOrgName(org.getOrgName());
				aditing.setOrgId(org.getOrgId());
				
				//  ���������õ�times��ֵ,�����-1���ܹ���
				// 	����Ƿ��Ѿ��ϱ�
				String timesSql="from AfReport ri where  "
				 	+" ri.templateId='"+ aditing.getTemplateId()+"'"
					+" and ri.versionId='"+aditing.getVersionId()+"'"
				//	+" and ri.MDataRgType.dataRangeId="+aditing.getDataRangeId()
					+" and ri.year="+aditing.getYear()
					+" and ri.term="+aditing.getTerm()
					+" and ri.day="+aditing.getDay()
					+" and ri.repFreqId="+aditing.getActuFreqID()
					+" and ri.curId="+aditing.getCurId()
					+" and ri.orgId='"+aditing.getOrgId()+"'"
					+" and ri.times=1"
					+" and ri.checkFlag=" + Config.CHECK_FLAG_PASS;
				List rlist = session.find(timesSql);
				
				if(rlist != null && rlist.size() > 0){
					AfReport reportIn = (AfReport)rlist.get(0);        							
					aditing.setRepInId(reportIn.getRepId().intValue());
					aditing.setIsCollected(new Integer(1));
					
				}else{
					aditing.setIsCollected(new Integer(0));
				}
					
				resList.add(aditing);
    		}
			pageListInfo.setList(resList);
    		}	
    	}catch(HibernateException he){
    		he.printStackTrace();
    		if(conn != null) conn.endTransaction(true);
    	}finally{
    		if(conn != null) conn.closeSession();
    	}
    	return pageListInfo;
    }
	/**
	 * ���ݴ������ڻ����Чʱ���ڵı���sql
	 * @param afReportForm
	 * @return sql
	 */
	private static String getValidDateSql(AFReportForm afReportForm){
		
		if(afReportForm.getDate() == null) return null;
		
		String datesql = "";
		// �����±����������
//		String[] dates = DateUtil.getLastMonth(afReportForm.getDate()).split("-");
		String[] dates = afReportForm.getDate().split("-");

		//int year = Integer.parseInt(dates[0]);
		int term = Integer.parseInt(dates[1]);
		//int day = Integer.parseInt(dates[2]);

		String rep_freq = "";
		if (term == 12)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEAR;
		else if (term == 6)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_HALFYEAR;
		else if (term == 3 || term == 9)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_SEASON;
		else if (term == 1)
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH
					+ Config.SPLIT_SYMBOL_COMMA
					+ com.fitech.gznx.common.Config.FREQ_YEARBEGAIN;
		else
			rep_freq = com.fitech.gznx.common.Config.FREQ_MONTH.toString();
		
//		// �ձ���ѯ
//		 datesql = " and (('"
//			+ afReportForm.getDate()
//			+ "' between a.startDate and a.endDate"
//			+ " and a.comp_id.repFreqId in ("
//			+ com.fitech.gznx.common.Config.FREQ_DAY
//			// Ѯ����ѯ
//			+ ")) or ('"
//			+ DateUtil.getLastTenDay(afReportForm.getDate())
//			+ "' between a.startDate and a.endDate"
//			+ " and a.comp_id.repFreqId in ("
//			+ com.fitech.gznx.common.Config.FREQ_TENDAY
//			// �±�������
//			+ ")) or ('"
//			+ DateUtil.getLastMonth(afReportForm.getDate())
//			+ "' between a.startDate and a.endDate"
//			+ " and a.comp_id.repFreqId in (" + rep_freq
//			// �������
//			+ ")))";
		 datesql = " and ('"
				+ afReportForm.getDate()
				+ "' between a.startDate and a.endDate"
				+ " and a.comp_id.repFreqId in ("
				+ com.fitech.gznx.common.Config.FREQ_DAY + Config.SPLIT_SYMBOL_COMMA
				+ com.fitech.gznx.common.Config.FREQ_TENDAY + Config.SPLIT_SYMBOL_COMMA
				+ rep_freq
				+ "))";
		
		
		return datesql;
	}
	
	/**
	 * �������и�ʽ�����ı���
	 * @param reportInForm
	 * @param operator
	 * @param reportFlg
	 * @param newFlg
	 * @return
	 */
	public static List selectRhExportReportList(AFReportForm reportInForm,
			Operator operator, String reportFlg, int newFlg) {
		
		// TODO Auto-generated method stub
		List resList = new ArrayList();
		DBConn conn = null;
		Session session = null;
		
		
		try {
			if (reportInForm != null && reportInForm.getOrgId() != null) {
				String rep_freq="";
				if(reportInForm.getRepFreqId()!=null&&reportInForm.getRepFreqId().trim().indexOf("9")>-1){
					if(reportInForm.getRepFreqId().equals("91"))
						rep_freq="1";
					if(reportInForm.getRepFreqId().equals("92"))
						rep_freq="2";
					if(reportInForm.getRepFreqId().equals("93"))
						rep_freq="4";
				}
				conn = new DBConn();
				session = conn.beginTransaction();
				String[] dates = reportInForm.getDate().split("-");
				
				StringBuffer sql = new StringBuffer("select report_pboc.* from(select d.*,mrf.rep_freq_name " +
						"from (select c.*,ao.org_name " +
						"from (select a.rep_id, a.rep_name, a.org_id, a.rep_freq_id, " +
						"a.template_id, a.version_id, b.supplement_flag " +
						"from af_report a " +
						"inner join (select * from af_template bb " +
						"where bb.template_type = '2' " +
						"and bb.is_report = 2 " +
						"and bb.using_flag = 1 ");
				
				//2012-07-26�޸� �ſ������ε�����
				if(reportInForm.getSupplementFlag()!= null && 
						!reportInForm.getSupplementFlag().equals("") && 
						!reportInForm.getSupplementFlag().equals(Config.DEFAULT_VALUE) //&&
						){//reportInForm.getRepFreqId().equals("1")){
					sql.append("and bb.supplement_flag="+reportInForm.getSupplementFlag().trim()+"");
				}
//				}else if(reportInForm.getSupplementFlag()!= null && 
//						!reportInForm.getSupplementFlag().equals("") && 
//						!reportInForm.getSupplementFlag().equals(Config.DEFAULT_VALUE) &&
//						reportInForm.getRepFreqId().indexOf("9")>-1){
//					sql.append("and bb.supplement_flag='"+reportInForm.getSupplementFlag().trim()+"'");
//				}
				
				sql.append(") b on b.template_id=a.template_id and b.version_id=a.version_id " +
							"where a.times=1 and a.rep_name is not null " );
				
				// �����ѯ����
				StringBuffer where = new StringBuffer();
				
				//�����Ų�ѯ
				if(reportInForm.getTemplateId() != null && !reportInForm.getTemplateId().trim().equals("")){	
					if(reportInForm.getTemplateId().indexOf(",")>0){
						String tems[] = reportInForm.getTemplateId().split(",");
						String templatesId = "";
						for (int i = 0; i < tems.length; i++) {
							templatesId+="'"+tems[i]+"',";
						}
						templatesId= templatesId.substring(0, templatesId.length()-1);
						where.append(" and a.template_id in("+templatesId+")");
					}else{
					
						where.append(" and a.template_id like '%"+reportInForm.getTemplateId().trim()+"%'");
					}
				}
				//�������Ʋ�ѯ
				if(reportInForm.getRepName() != null && !reportInForm.getRepName().trim().equals("")){				
					where.append(" and a.rep_name like '%"+reportInForm.getRepName().trim()+"%'");					
				}
				//Ƶ�Ȳ�ѯ
				if(reportInForm.getRepFreqId() != null && !reportInForm.getRepFreqId().equals("")){				
					where.append(" and a.rep_freq_id="+reportInForm.getRepFreqId().trim().substring(0,1)+"");					
				}
				if(Config.SYSTEM_SCHEMA_FLAG==1){
					where.append(toExpPbocTextSql());
				}
				//������ѯ
				String s = reportInForm.getOrgId();
				if(reportInForm.getOrgId()!= null && 
						!reportInForm.getOrgId().equals("") && 
						!reportInForm.getOrgId().equals("'"+Config.DEFAULT_VALUE+"'")&&
						!reportInForm.getOrgId().equals("'null'")){
					if(reportInForm.getOrgId().indexOf(",")>0){
						String orgs[] = reportInForm.getOrgId().split(",");
						String getOrgId = "";
						for (int i = 0; i < orgs.length; i++) {
							getOrgId+="'"+orgs[i]+"',";
						}
						getOrgId= getOrgId.substring(1, getOrgId.length()-2);
						where.append(" and a.org_id in ("+getOrgId+")");
					}else{
						where.append(" and a.org_id in ("+reportInForm.getOrgId()+")");
					}
				}
				else
					where.append(" and a.org_id in (select ator.org_id from af_template_org_relation ator where ator.template_id=a.template_id and ator.version_id=a.version_id) ");
				
				where.append(" and a.org_id in (select at.org_id from AF_TEMPLATE_OUTER_REP at where at.template_id=a.template_id and at.version_id=a.version_id) ");

				//���ڲ�ѯ
				if(reportInForm.getDate()!= null && !reportInForm.getDate().equals("")){
					where.append(" and a.year="+dates[0].trim()+" and a.term="+dates[1]+" and a.day="+dates[2]);
				}
				
				sql.append(where.toString() + " and a.check_flag=1 order by a.org_id, a.rep_id) c " +
						"left join af_org ao on ao.org_id=c.org_id ) d " +
						"left join m_rep_freq mrf on mrf.rep_freq_id=d.rep_freq_id " +
						"order by d.template_id,d.org_id)report_pboc ");
				if(rep_freq!=null && !rep_freq.equals("")){
					sql.append(" inner join (select * from af_template_freq_relation afr where afr.rep_freq_id="+rep_freq+")freq_rl on report_pboc.template_id=freq_rl.template_id and report_pboc.version_id=freq_rl.version_id");
				}
				System.out.println("�б�sql="+sql.toString());
				Connection connection = session.connection();
				ResultSet rs = connection.createStatement().executeQuery(sql.toString());
				
				while (rs.next()) {
					Aditing aditing = new Aditing();
					//����id
					aditing.setRepInId(rs.getInt("rep_id"));
					//��������
					aditing.setRepName(rs.getString("rep_name"));
					// ����ID
					aditing.setOrgId(rs.getString("org_id"));
					//����name
					aditing.setOrgName(rs.getString("org_name"));
					//Ƶ��id
					aditing.setActuFreqID(rs.getInt("rep_freq_id"));
					//Ƶ��name
					aditing.setActuFreqName(rs.getString("rep_freq_name"));
					aditing.setVersionId(rs.getString("version_id"));
					aditing.setTemplateId(rs.getString("template_id"));
					// ���ñ�������
					int batchId = rs.getInt("supplement_flag");
					if (batchId >= 0) {
						aditing.setBatchId(String.valueOf(batchId));
					} else {
					//����Ϊ�յĶ���Ϊ�ڶ���
						aditing.setBatchId("2");
					}
					resList.add(aditing);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (conn != null)
				conn.closeSession();
		}
		return resList;
	}
	
}
