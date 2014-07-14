package com.fitech.gznx.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.form.OrgNetForm;

public class ReportStatisticsCollection {
	
	private static FitechException log = new FitechException(
			ReportStatisticsCollection.class);
	
	
	/***
	 * oracle�﷨(rownum) ��Ҫ�޸� ���Ը� 2011-12-27
	 * ��oracle��to_date���� �޸�Ϊsqlserver ��convert����
	 * ��oracle nvl�﷨�޸�Ϊsqlserver isnull�﷨
	 * oracle rownumα���޸�Ϊsqlserver ROW_NUMBER()����
	 * @param orgNetForm
	 * @param templateType
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static List<OrgNetForm> reportStat(OrgNetForm orgNetForm,Integer templateType,int offset,int limit){
		
		List<OrgNetForm> list = null;
		
		if(orgNetForm == null ||orgNetForm.getStartDate()==null 
				|| orgNetForm.getStartDate().trim().equals("")
				|| templateType == null)
			return list;
		
		//�����������
		String[] terms = getTerms(orgNetForm);
		
		//���û������ֱ�ӷ���
		if(terms == null || terms.length <= 0 ) return list;
		
		StringBuffer unionSql = new StringBuffer();
		
		//��ȡ��ѯsql
		for( int i = 0;i<terms.length;i++ ){
			
			if (i != 0)  unionSql.append(" union ");
			
			/**jdbc���� oracle�﷨ ������Ҫ�޸� ���Ը� 2011-12-22
			 * ��oracle��to_date���� �޸�Ϊsqlserver ��convert����
			 * ��oracle nvl�﷨�޸�Ϊsqlserver isnull�﷨
			 * ���Ը� 2011-12-27 ������*/
			unionSql.append(sql(terms[i],templateType));
			
		}
		
		StringBuffer sql  = new StringBuffer();
		sql.append("select dates,org_id,org_name,org_level,needed,reported,checked,agained,lated from (");
		/**���˴���oracle rownumα���޸�Ϊsqlserver ROW_NUMBER()����
		 * ���������ݿ��ж�*/
		if(Config.DB_SERVER_TYPE.equals("oracle"))
			sql.append("select rownum as rown,dates,org_id,org_name,org_level,needed,reported,checked,agained,lated from (");
		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			sql.append("select ROW_NUMBER() over(order by org_id,org_level,dates ) as rown,dates,org_id,org_name,org_level,needed,reported,checked,agained,lated from (");
		sql.append("select dates,org_id,org_name,org_level,needed,reported,checked,agained,lated from (")
		.append(unionSql)
		.append(") fin ");
		
		if (orgNetForm.getOrg_name()!=null && !orgNetForm.getOrg_name().trim().equals("")){
			sql.append(" where fin.org_name like '%").append(orgNetForm.getOrg_name()).append("%'");
		}
		
		/**order by ���� ע�͵� ʹ����ROW_NUMBER()������
		 * ���������ݿ��ж�*/
		if(Config.DB_SERVER_TYPE.equals("oracle"))
			sql.append("order by org_id,org_level,dates");
		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			sql.append(" ");
		sql.append(") fin1) fin2 where rown>").append(offset).append(" and rown<=").append(limit);
		
//		select dates,org_id,org_name,org_level,needed,reported,checked,agained,lated from (
//				select rownum as rown,dates,org_id,org_name,org_level,needed,reported,checked,agained,lated from (
//						select dates,org_id,org_name,org_level,needed,reported,checked,agained,lated from (
//								...
//						) fin where fin.org_name like '%..%' order by org_id,org_level,dates 
//				)
//		) where rown>.. And rown<=..
//		select ....from (select ROW_NUMBER() over(order by (select 0)) AS rown,..by...from(...))where rown>..and rown<=...
		//,org_level,dates
		DBConn dbConn = null;
		Connection conn = null;
		ResultSet rs = null;

		try{
			dbConn = new DBConn();
			conn = dbConn.openSession().connection();
			rs = conn.createStatement().executeQuery(sql.toString());
			
			OrgNetForm orgStat = null;
			list = new ArrayList<OrgNetForm>();
			
			while(rs.next()){
				
				orgStat = new OrgNetForm();
				orgStat.setDate(rs.getString("dates"));
				orgStat.setOrg_id(rs.getString("org_id"));
				orgStat.setOrg_name(rs.getString("org_name"));
				orgStat.setYbReportNum(rs.getInt("needed"));//Ӧ��
				orgStat.setBsReportNum(rs.getInt("reported"));
				orgStat.setSqReportNum(rs.getInt("checked"));
				orgStat.setCbReportNum(rs.getInt("agained"));
				orgStat.setZbReportNum(rs.getInt("lated"));
				orgStat.setLbReportNum(orgStat.getYbReportNum()-orgStat.getBsReportNum());
				list.add(orgStat);
				
			}


		}catch(Exception e){
			
			log.printStackTrace(e);

		}finally{
			if(dbConn!=null)
				dbConn.closeSession();
		}
		
		return list;
	}
	/**
	 * jdbc���� oracle�﷨ ������Ҫ�޸� ���Ը� 2011-12-22
	 * ��oracle��to_date���� �޸�Ϊsqlserver ��convert����
	 * ��oracle nvl�﷨�޸�Ϊsqlserver isnull�﷨
	 * ���Ը� 2011-12-27 ������
	 * **/
	public static int  reportStatCount(OrgNetForm orgNetForm,Integer templateType){
		int count = 0;
		
		if(orgNetForm == null ||orgNetForm.getStartDate()==null 
				|| orgNetForm.getStartDate().trim().equals("")
				|| templateType == null)
			return count;
		
		//�����������
		String[] terms = getTerms(orgNetForm);
		
		//���û������ֱ�ӷ���
		if(terms == null || terms.length <= 0 ) return count;
		
		StringBuffer unionSql = new StringBuffer();
		
		//��ȡ��ѯsql
		for( int i = 0;i<terms.length;i++ ){
			
			if (i != 0)  unionSql.append(" union ");
			/**��oracle��to_date���� �޸�Ϊsqlserver ��convert����
			 * ��oracle nvl�﷨�޸�Ϊsqlserver isnull�﷨
			 * ���Ը� 2011-12-27 ������*/
			unionSql.append(sql(terms[i],templateType));
			
		}
		
		StringBuffer sql  = new StringBuffer();
		
		sql.append("select count(0) as count from (")
		.append(unionSql)
		.append(") fin ");
		
		if (orgNetForm.getOrg_name()!=null && !orgNetForm.getOrg_name().trim().equals("")){
			sql.append(" where fin.org_name like '%").append(orgNetForm.getOrg_name()).append("%'");
		}
		/***
		 * sql�ַ���ƴװ��������oracle�﷨ ������Ҫ�޸� ���Ը� 2011-12-22
		 * sql=
		 * select count(0) as count from 
		 * 		(select '2011-11' as dates,nr.org_id,g.org_name,
		 * 							 g.org_type_id as org_level,
		 * 				nvl(nr.need_rep,0) as needed,nvl(rd.reported,0) as reported,
		 * 				nvl(cd.checked,0) as checked,nvl(ad.agained,0) as agained,
		 * 				nvl(lr.lated,0) as lated  from  
		 * 				(select count(0) as need_rep,vm.org_id from 
		 * 					 view_m_report vm  where 
		 * 				vm.rep_freq_id in (1)  and ('2011-11-15' between vm.start_date and vm.end_date ) group by org_id) nr  
		 * 		left join  
		 * 			select count(0) as reported,ri.org_id from report_in ri  
		 * 			where year=2011 and term=11 and ri.check_flag in (0,1) group by org_id) rd  
		 * on nr.org_id = rd.org_id  
		 * left join  
		 * 		(select count(0) as checked,ri.org_id from report_in ri  
		 * 			where year=2011 and term=11 and ri.check_flag in (1) group by org_id) cd 
		 * on nr.org_id=cd.org_id 
		 * left join  
		 * 		(select count(0) as agained,g.org_id from report_in ri,org g 
		 * 			where ri.org_id=g.org_id and  year=2011 and term=11 and ri.forse_report_again_flag=1
		 * 			group by g.org_id) ad  
		 * on nr.org_id=ad.org_id  
		 * left join  
		 * 		(select count(0) as lated,nrr.org_id from  
		 * 			(select vm.child_rep_id,vm.version_id,vm.org_id,vm.normal_time from view_m_report vm  
		 * 							where vm.rep_freq_id in (1)  and ('2011-11-15' between vm.start_date and vm.end_date )) nrr
		 * 				left join 
		 * 							(select ri.child_rep_id,ri.version_id,ri.org_id,ri.report_date from report_in ri  
		 * 							where year=2011 and term=11 ) rir 
		 * 				on  nrr.child_rep_id=rir.child_rep_id and nrr.version_id=rir.version_id and nrr.org_id=rir.org_id  
		 * 				where (to_date('2011-12-1','yyyy-MM-dd')+nrr.normal_time)<=rir.report_date  group by nrr.org_id) lr  
		 * on nr.org_id=lr.org_id  
		 * left join org g on nr.org_id=g.org_id ) fin
		 */
		
		DBConn dbConn = null;
		Connection conn = null;
		ResultSet rs = null;

		try{
			dbConn = new DBConn();
			conn = dbConn.openSession().connection();
			rs = conn.createStatement().executeQuery(sql.toString());
			
			if (rs.next()){
				count=rs.getInt(1);
			}


		}catch(Exception e){
			
			log.printStackTrace(e);

		}finally{
			if(dbConn!=null)
				dbConn.closeSession();
		}
		
		return count;
		
	}
	
	/***
	 * ��oracle��to_date���� �޸�Ϊsqlserver ��convert����
	 * ��oracle nvl�﷨�޸�Ϊsqlserver isnull�﷨
	 * ���Ը� 2011-12-27 ������
	 * @param term
	 * @param templateType
	 * @return
	 */
	private static String sql(String term,Integer templateType){
		
		String sql = "";
		
		if(term == null || templateType == null) 
			return sql;
		
		//��ø���Ƶ��
		String freqs = freqs(Integer.valueOf(term.substring(5, 7)));
		
		//1104��������ͬ���ֱ���
		if(templateType.toString().equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			
			sql = "select '"+ term +"' as dates,nr.org_id,g.org_name,g.org_type_id as org_level," +
						"isnull(nr.need_rep,0) as needed," +
						"isnull(rd.reported,0) as reported," +
						"isnull(cd.checked,0) as checked," +
						"isnull(ad.agained,0) as agained," +
						"isnull(lr.lated,0) as lated " +
						" from " +
			
			//--Ӧ��ͳ��
			" (select count(0) as need_rep,vm.org_id from view_m_report vm " +
			" where vm.rep_freq_id in ("+ freqs +") " +
			" and ('"+ term +"-15' between vm.start_date and vm.end_date ) group by org_id) nr " +
			
			//--�ϱ�ͳ��
			" left join " +
			" (select count(0) as reported,ri.org_id from report_in ri " +
			" where year="+ term.substring(0, 4)+" and term="+ term.substring(5, 7)+
			" and ri.check_flag in (0,1) group by org_id) rd " +
			" on nr.org_id = rd.org_id " +
			
			//--���ͳ��
			" left join " +
			" (select count(0) as checked,ri.org_id from report_in ri " +
			" where year="+ term.substring(0, 4)+" and term="+ term.substring(5, 7)+
			" and ri.check_flag in (1) group by org_id) cd " +
			" on nr.org_id=cd.org_id" +
			
			//--�ر�ͳ��
			" left join " +
			" (select count(0) as agained,g.org_id from report_in ri,org g " +
			" where ri.org_id=g.org_id and " +
			" year="+ term.substring(0, 4)+" and term="+ term.substring(5, 7)+
			" and ri.forse_report_again_flag=1 group by g.org_id) ad " +
			" on nr.org_id=ad.org_id " +
			
			//--�ٱ�ͳ��
			" left join " +
			" (select count(0) as lated,nrr.org_id from " +
			" (select vm.child_rep_id,vm.version_id,vm.org_id,vm.normal_time from view_m_report vm " +
			" where vm.rep_freq_id in ("+ freqs + ") " +
			" and ('"+ term +"-15' between vm.start_date and vm.end_date )) nrr " +
			" left join " +
			"(select ri.child_rep_id,ri.version_id,ri.org_id,ri.report_date from report_in ri " +
			" where year="+ term.substring(0, 4)+" and term="+ term.substring(5, 7)+
			" ) rir on " +
			" nrr.child_rep_id=rir.child_rep_id and nrr.version_id=rir.version_id and nrr.org_id=rir.org_id " ;
			/**�δ���oracle��to_date���� �޸�Ϊsqlserver ��convert����
			 * ���������ݿ��ж�*/
			if(Config.DB_SERVER_TYPE.equals("oracle"))
			{
				sql+=" where (to_date('"+com.fitech.gznx.common.DateUtil.getNextMonthDate(term+"-1") +"','yyyy-MM-dd')" +
						"+nrr.normal_time)<=rir.report_date " ;
			}
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			{
				sql+=" where (convert(datetime,'"+com.fitech.gznx.common.DateUtil.getNextMonthDate(term+"-1") +"',120)" +
				"+nrr.normal_time)<=rir.report_date " ;
			}
			sql+=" group by nrr.org_id) lr " +
			" on nr.org_id=lr.org_id " +
			
			//--����������
			" left join org g on nr.org_id=g.org_id ";
			//--where nr.org_id='0099'

		
		}else{
			/**�δ���oracle nvl�﷨�޸�Ϊsqlserver isnull�﷨
			 * ���������ݿ��ж�*/
			sql = "select '"+ term + "' as dates,nr.org_id,g.org_name,g.org_level," ;
			if(Config.DB_SERVER_TYPE.equals("oracle"))
			{
				sql+="nvl(nr.need_rep,0) as needed,nvl(rd.reported,0) as reported," +
				"nvl(cd.checked,0) as checked,nvl(ad.agained,0) as agained," +
				"nvl(lr.lated,0) as lated " ;
			}
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			{
				sql+="isnull(nr.need_rep,0) as needed,isnull(rd.reported,0) as reported," +
				"isnull(cd.checked,0) as checked,isnull(ad.agained,0) as agained," +
				"isnull(lr.lated,0) as lated " ;
			}
			sql+=" from " +
			//--Ӧ��ͳ��
			" (select count(0) as need_rep,vm.org_id from view_af_report vm " +
			" where vm.REP_FREQ_ID in ("+ freqs +") " +
			" and ('"+ term +"-15' between vm.START_DATE and vm.END_DATE )" +
			"and vm.template_type="+ templateType +" group by org_id) nr " +
			" left join " +
			//--�ϱ�ͳ��
			" (select count(0) as reported,ri.org_id from af_report ri where " +
			" year="+ term.substring(0, 4) +" and term=" +term.substring(5, 7) +
			" and ri.check_flag in (0,1) " ;
			if (Config.DB_SERVER_TYPE.equals("sqlserver")) {
				sql+=" and ri.template_id+'-'+ri.version_id in ";
				sql+=" (select ri.template_id+'-'+ri.version_id from af_template at " ;
			}
			if (Config.DB_SERVER_TYPE.equals("oracle")) {
				sql+=" and ri.template_id||'-'||ri.version_id in ";
				sql+=" (select ri.template_id||'-'||ri.version_id from af_template at " ;
			}
			
//			" and ri.template_id+'-'+ri.version_id in " +
			
			sql+=" where at.template_type=" + templateType+ ")"+ 
			" group by org_id) rd " +
			" on nr.org_id=rd.org_id " +
			" left join " +
			//--���ͳ��
			" (select count(0) as checked,ri.org_id from af_report ri where " +
			" year="+ term.substring(0, 4) +" and term=" +term.substring(5, 7) +
			" and ri.check_flag in (1) " ;
			if (Config.DB_SERVER_TYPE.equals("sqlserver")) {
				sql+=" and ri.template_id+'-'+ri.version_id in " ;
				sql+=" (select ri.template_id+'-'+ri.version_id from af_template at " ;
			}
			if (Config.DB_SERVER_TYPE.equals("oracle")) {
				sql+=" and ri.template_id||'-'||ri.version_id in " ;
				sql+=" (select ri.template_id||'-'||ri.version_id from af_template at " ;
			}
		
			sql+=" where at.template_type=" + templateType+ ")"+ 
			" group by org_id) cd " +
			" on nr.org_id=cd.org_id " +
			" left join " +
			//--�ر�ͳ��
			" (select count(0) as agained,g.org_id from af_report ri,org g where ri.org_id=g.org_id and " +
			" year="+ term.substring(0, 4) +" and term=" +term.substring(5, 7) +
			" and ri.forse_report_again_flag=1 " ;
			if (Config.DB_SERVER_TYPE.equals("sqlserver")) {
				sql+=" and ri.template_id+'-'+ri.version_id in " ;
				sql+=" (select ri.template_id+'-'+ri.version_id from af_template at " ;
			}
			if (Config.DB_SERVER_TYPE.equals("oracle")) {
				sql+=" and ri.template_id||'-'||ri.version_id in " ;
				sql+=" (select ri.template_id||'-'||ri.version_id from af_template at " ;
			}
			
			sql+=" where at.template_type=" + templateType+ ")"+
			" group by g.org_id) ad " +
			" on nr.org_id=ad.org_id " +
			" left join " +
			//--�ٱ�ͳ��
			" (select count(0) as lated,nrr.org_id " +
			" from " +
			" (select vm.template_id,vm.version_id,vm.org_id,vm.normal_time from view_af_report vm " +
			" where vm.REP_FREQ_ID in ("+ freqs +") and vm.template_type=" + templateType +
			" and ('"+ term +"-15' between vm.start_date and vm.end_date )) nrr " +
			" left join " +
			"(select ri.template_id,ri.version_id,ri.org_id,ri.report_date from af_report ri where " +
			" year="+ term.substring(0, 4) +" and term=" +term.substring(5, 7) ;
			if (Config.DB_SERVER_TYPE.equals("sqlserver")) {
				sql+=" and ri.template_id+'-'+ri.version_id in " ;
				sql+=" (select ri.template_id+'-'+ri.version_id from af_template at " ;
			}
			if (Config.DB_SERVER_TYPE.equals("oracle")) {
				sql+=" and ri.template_id||'-'||ri.version_id in " ;
				sql+=" (select ri.template_id||'-'||ri.version_id from af_template at " ;
			}
			
			sql+=" where at.template_type=" + templateType+ ")" + 
			") rir " +
			" on " +
			" nrr.template_id=rir.template_id and nrr.version_id=rir.version_id and nrr.org_id=rir.org_id " ;
			/**�δ���oracle��to_date���� �޸�Ϊsqlserver ��convert����*/
			if(Config.DB_SERVER_TYPE.equals("oracle"))
			{
				sql+=" where (to_date('"+com.fitech.gznx.common.DateUtil.getNextMonthDate(term+"-1") +"','yyyy-MM-dd')" +
					"+nrr.normal_time)<=rir.report_date " ;
			}
			if(Config.DB_SERVER_TYPE.equals("sqlserver"))
			{
				sql+=" where (convert(datetime,'"+com.fitech.gznx.common.DateUtil.getNextMonthDate(term+"-1") +"',120)" +
				"+nrr.normal_time)<=rir.report_date " ;
			}
			sql+=" group by nrr.ORG_ID) lr " +
			" on nr.org_id=lr.org_id " +
			" left join " +
			//--����������
			" af_org g " +
			" on nr.org_id=g.org_id ";
			//--where nr.org_id='0099'
		}
		
		
		return sql;
	}
	
	/**
	 * �����·ݲ��Ƶ��
	 * @param term �·�
	 * @return
	 */
	private static String freqs(int term){
		
		String rep_freq = "";
		
		if (term<1 || term>12) return rep_freq;
		
		if (term == 12)
			rep_freq = "1,2,3,4";
		else if (term == 6)
			rep_freq = "1,2,3";
		else if (term == 3 || term == 9)
			rep_freq = "1,2";
		else
			rep_freq = "1";
		
		return rep_freq;
	}
	
	
	/**
	 * �����������ֹ���ڻ�õ�ʱ��
	 * @param orgNetForm
	 * @return
	 */
	private static String[] getTerms(OrgNetForm orgNetForm){
		
		String[] terms = null;
		
		if(orgNetForm == null ||orgNetForm.getStartDate()==null 
				|| orgNetForm.getStartDate().trim().equals(""))
			return terms;
		
		String startDate = orgNetForm.getStartDate();
		
		if(orgNetForm.getStartDate()==null 
				|| orgNetForm.getStartDate().trim().equals(""))
			orgNetForm.setEndDate(orgNetForm.getStartDate());
		
		String endDate = orgNetForm.getEndDate();
		
		try {
			terms = com.fitech.gznx.common.DateUtil.getTerms(startDate+"-15",endDate+"-15");		
		}catch(Exception e){
			log.printStackTrace(e);
			return terms;
		}
		
		return terms;
	}
	

	
	public static void main (String a[])throws Exception{

	}

}
