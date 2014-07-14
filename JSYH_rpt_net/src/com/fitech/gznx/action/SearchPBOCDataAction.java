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

public class SearchPBOCDataAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//获得币种
		String mcurrId = request.getParameter("mcurrId");
		//获得口径
		String repFreqId = request.getParameter("freqId");
		//获得期数
		String team = request.getParameter("startTeam");
		String endTeam = request.getParameter("endTeam");
		//获得年月
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
		
		//获得机构
		String orgId = request.getParameter("orgIds");
		//拆分机构
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
			
		
		// add by 王明明
		String hql = "SELECT  A.ORG_NAME,AF.CELL_NAME,AF.ROW_NAME,AF.COL_NAME,I.CELL_DATA,A.REP_NAME,A.VERSION_ID,A.YEAR,A.TERM FROM (" +
					 "SELECT B.ORG_NAME,A.REP_ID,A.ORG_ID,A.TEMPLATE_ID,A.REP_NAME,A.VERSION_ID,A.YEAR,A.TERM FROM AF_REPORT A ,AF_ORG B WHERE  A.ORG_ID=B.ORG_ID " ;
					 if(orgStr!="")
						 hql += " AND A.ORG_ID IN ("+orgStr+") ";//机构
					 if (Config.DB_SERVER_TYPE.equals("oracle")) {
						 if(!startYear.equals("") && !startMonth.equals(""))//开始时间
							 hql += " AND TO_DATE(A.YEAR||'-'||A.TERM,'yyyy-MM')>=TO_DATE("+startYear+"||'-'||"+startMonth+",'yyyy-MM') ";
						 if(!endYear.equals("") && !endMonth.equals(""))//结束时间
							 hql += " AND TO_DATE(A.YEAR||'-'||A.TERM,'yyyy-MM')<=TO_DATE("+endYear+"||'-'||"+endMonth+",'yyyy-MM') ";
					}
					 if (Config.DB_SERVER_TYPE.equals("sqlserver")) {
						 if(!startYear.equals("") && !startMonth.equals(""))//开始时间
							 hql += " AND CONVERT(datatime,A.YEAR+'-'+A.TERM+'-1',120)>=CONVERT(datatime,"+startYear+"+'-'+"+startMonth+"+'-1',120) ";
						 if(!endYear.equals("") && !endMonth.equals(""))//结束时间
							 hql += " AND CONVERT(datatime,A.YEAR+'-'+A.TERM+'-1',120)>=CONVERT(datatime,"+endYear+"+'-'+"+endMonth+"+'-1',120) ";
					 }
					 
					 if(mcurrId!=null && !mcurrId.equals(""))
						 hql += " AND A.CUR_ID="+mcurrId+" ";//币种
					 if(repFreqId!=null && !repFreqId.equals(""))//口径
						 hql += " AND A.REP_FREQ_ID="+repFreqId;
		String whereSql="";
		String[] templateIdAndVersionIdAndCellName = request.getParameterValues("templateIdAndVersionIdAndCellName");
		if(templateIdAndVersionIdAndCellName!=null && templateIdAndVersionIdAndCellName.length>0){
			for(int i=0;i<templateIdAndVersionIdAndCellName.length;i++){
				String templateId = templateIdAndVersionIdAndCellName[i].split("_")[0];//模板ID
				String versionId = templateIdAndVersionIdAndCellName[i].split("_")[1];//版本ID
//				String cellName = templateIdAndVersionIdAndCellName[i].split("_")[2];//单元格名称
				
				whereSql+= "(A.TEMPLATE_ID='"+templateId+"' AND A.VERSION_ID='"+versionId+"')";//+"' and a.CELL_NAME='"+cellName+"')";
				if(i!=templateIdAndVersionIdAndCellName.length-1)
					whereSql += " OR ";
				
			}
		}
		//避免重复数据
		whereSql+=" AND A.TIMES = '1'";
		hql+=" AND "+"("+(whereSql==""?"1=1":whereSql)+")";
		hql+="  ) A ,  AF_PBOCREPORTDATA I  ";
		hql+=" , (SELECT AF.CELL_NAME,AF.ROW_NAME,AF.COL_NAME,AF.CELL_ID FROM AF_CELLINFO AF WHERE";
		whereSql="";
		if(templateIdAndVersionIdAndCellName!=null && templateIdAndVersionIdAndCellName.length>0){
			for(int i=0;i<templateIdAndVersionIdAndCellName.length;i++){
				String cellName = templateIdAndVersionIdAndCellName[i].split("_")[2];//单元格名称
				
				whereSql+= " (AF.CELL_NAME='"+cellName+"')";
				if(i!=templateIdAndVersionIdAndCellName.length-1)
					whereSql += " OR ";
				
			}
		}
		hql+=" ("+(whereSql==""?"1=1":whereSql)+")";
		hql+=" ) AF WHERE A.REP_ID=I.REP_ID AND I.CELL_ID=AF.CELL_ID";
		hql+= "  ORDER BY A.YEAR,A.TERM,AF.CELL_NAME";
		System.out.println("--"+hql);
		/***
		 * 遍历机构 根据机构查
		 * 1.根据给定的机构，期数 查询report_in r和af_cell_info a条件为：r.child_rep_id=a.template_id and r.version_id=a.version_id 
		 * and r.机构 in 机构信息 and r.期数=?
		 * 得到cell_id,rep_in_id
		 * 2.根据cell_id和rep_in_id检索report_in_info 得到单元格的值
		 * 需要a.row_name 对应的report_in_info的值 
		 * 机构 where 机构ID = ? 
		 * 
		 */
		List<Object[]> objList = AFReportDealDelegate.searchPBOCData(hql);
		request.setAttribute("objList", objList);
		return mapping.findForward("index");
	}
	
}
