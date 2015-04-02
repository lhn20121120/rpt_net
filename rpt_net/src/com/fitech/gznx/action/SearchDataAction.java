package com.fitech.gznx.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.Config;
import com.fitech.gznx.service.AFReportDealDelegate;

public class SearchDataAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//��ñ���
		String mcurrId = request.getParameter("mcurrId");
		//��ÿھ�
		String repFreqId = request.getParameter("freqId");
		//�������
		String team = request.getParameter("startTeam");
		String endTeam = request.getParameter("endTeam");
		//�������
		String startYear="";
		String startMonth="";
		String endYear="";
		String endMonth="";
		if(team!=null && !team.equals("") && team.indexOf("-")>0){
			startMonth = team.substring(team.indexOf("-")+1,team.lastIndexOf("-"));
			startYear = team.substring(0,team.indexOf("-"));
		}
		if(endTeam!=null && !endTeam.equals("") && endTeam.indexOf("-")>0){
			endMonth = endTeam.substring(endTeam.indexOf("-")+1,endTeam.lastIndexOf("-"));
			endYear = endTeam.substring(0,endTeam.indexOf("-"));
		}
		
		//��û���
		String orgId = request.getParameter("orgIds");
		//��ֻ���
		String[] orgIds = null;
		String orgStr = "";
		if(orgId!=null && !orgId.equals("")){
			orgIds = orgId.split(",");
			
			for(int i=0;i<orgIds.length;i++){
				orgStr += "'"+orgIds[i]+"'";
				if(i!=orgIds.length-1)
					orgStr +=",";
			}
		}
			
		
		
		//��ѯhql ����,��Ԫ������,��Ԫ���ֵ,��������,�汾,��,�� 	
//		String hql = "select r.orgId,a.cellName,i.reportValue,r.repName,r.MChildReport.comp_id.versionId,r.year,r.term from ReportIn r " +
//				"left join AfCellinfo a on r.MChildReport.comp_id.childRepId=a.templateId and r.MChildReport.comp_id.versionId=a.versionId" +
//		" left join ReportInInfo i on i.comp_id.cellId=a.cellId and i.comp_id.repInId=r.repInId  where 1=1 ";
		String hql = "select o.ORG_NAME,a.CELL_NAME,c.ROW_NAME,c.COL_NAME,i.REPORT_VALUE,r.REP_NAME,r.VERSION_ID,r.YEAR,r.TERM from REPORT_IN r " +
					 "left join M_CELL a on r.CHILD_REP_ID=a.CHILD_REP_ID and r.VERSION_ID=a.VERSION_ID " +
					 "left join REPORT_IN_INFO i on i.CELL_ID=a.CELL_ID and i.REP_IN_ID=r.REP_IN_ID  " +
					 "left join AF_ORG o on o.ORG_ID=r.ORG_ID " +
					 "left join AF_CELLINFO c on r.CHILD_REP_ID=c.TEMPLATE_ID and r.VERSION_ID=c.VERSION_ID and a.CELL_NAME=c.CELL_NAME where 1=1 ";
		
		
		if(orgStr!="")
			hql += " and r.ORG_ID in ("+orgStr+") ";//����
		if(!startYear.equals("") && !startMonth.equals("")){//��ʼʱ��
			if(Config.DB_SERVER_TYPE.equals("db2")){
				hql += " and date(rtrim(char(CAST(r.year as int))) || '-'||rtrim(char(CAST(r.term as int)))||'-01')>='"+(startYear+"-"+startMonth+"-01")+"' ";
			}
			if(Config.DB_SERVER_TYPE.equals("oracle")){
				hql += " and to_date(r.year||'-'||r.term||'-'||'01','yyyy-MM-dd')>=to_date('"+(startYear+"-"+startMonth+"-01")+"','yyyy-MM-dd')";
			}
			//hql += " and r.year>="+startYear+" and r.term>="+startMonth+"  ";
		}
		if(!endYear.equals("") && !endMonth.equals("")){//����ʱ��
			if(Config.DB_SERVER_TYPE.equals("db2")){
				hql += " and date(rtrim(char(CAST(r.year as int))) || '-'||rtrim(char(CAST(r.term as int)))||'-01')<='"+(endYear+"-"+endMonth+"-01")+"' ";
			}
			if(Config.DB_SERVER_TYPE.equals("oracle")){
				hql += " and to_date(r.year||'-'||r.term||'-'||'01','yyyy-MM-dd')<=to_date('"+(endYear+"-"+endMonth+"-01")+"','yyyy-MM-dd')";
			}
			//hql += " and r.year<="+endYear+" and r.term<="+endMonth+" ";
		}
		if(mcurrId!=null && !mcurrId.equals(""))
			hql += " and r.CUR_ID="+mcurrId+" ";//����
		if(repFreqId!=null && !repFreqId.equals(""))//�ھ�
			hql += " and r.DATA_RANGE_ID="+repFreqId+" ";
		
		String whereSql = "";
		//��ñ���ID �汾��Ϣ�͵�Ԫ������
		String[] templateIdAndVersionIdAndCellName = request.getParameterValues("templateIdAndVersionIdAndCellName");
		if(templateIdAndVersionIdAndCellName!=null && templateIdAndVersionIdAndCellName.length>0){
			for(int i=0;i<templateIdAndVersionIdAndCellName.length;i++){
				String templateId = templateIdAndVersionIdAndCellName[i].split("_")[0];//ģ��ID
				String versionId = templateIdAndVersionIdAndCellName[i].split("_")[1];//�汾ID
				String cellName = templateIdAndVersionIdAndCellName[i].split("_")[2];//��Ԫ������
				
				whereSql += "(r.CHILD_REP_ID='"+templateId+"' and r.VERSION_ID='"+versionId+"' and a.CELL_NAME='"+cellName+"')";
				if(i!=templateIdAndVersionIdAndCellName.length-1)
					whereSql += " or ";
				
			}
		}
		//�����ظ�����
		whereSql+=" and r.times = 1";
		String sql = hql+"and "+"("+(whereSql==""?"1=1":whereSql)+")";
		sql += " ORDER BY r.year ,r.term, a.CELL_NAME";
		System.out.println("--"+sql);
		/***
		 * �������� ���ݻ�����
		 * 1.���ݸ����Ļ��������� ��ѯreport_in r��af_cell_info a����Ϊ��r.child_rep_id=a.template_id and r.version_id=a.version_id 
		 * and r.���� in ������Ϣ and r.����=?
		 * �õ�cell_id,rep_in_id
		 * 2.����cell_id��rep_in_id����report_in_info �õ���Ԫ���ֵ
		 * ��Ҫa.row_name ��Ӧ��report_in_info��ֵ 
		 * ���� where ����ID = ? 
		 * 
		 */
		List<Object[]> objList = AFReportDealDelegate.searchData(sql);
		request.setAttribute("objList", objList);
		return mapping.findForward("index");
	}
	
}
