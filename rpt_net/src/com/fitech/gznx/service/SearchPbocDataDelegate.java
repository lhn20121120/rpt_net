package com.fitech.gznx.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.fitech.gznx.po.AfCellinfo;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.vo.SearchVo;

public class SearchPbocDataDelegate {
	public static SearchVo getDataMap(SearchVo vo,String orgStr ,String startYear , String startMonth ,String endYear ,String endMonth, String mcurrId, String repFreqId, String[] templateIdAndVersionIdAndCellName ) {

		if (vo == null) {
			return vo;
		}
		if(null == repFreqId){
			repFreqId = "1";
		}
		Map<String, String> itemMap = new LinkedHashMap<String,String>();
		// 期数<2014-09，2014-09>
		Map<String ,String> dateMap = new  TreeMap<String ,String>();
		// 机构<0900,总行>
		Map<String ,String> orgMap = new TreeMap<String ,String>();
		// 鏁版嵁
		Map<String,String> dataMap = new HashMap<String,String>();

		String strYearMonth = "";

//			StringBuffer targetId = new StringBuffer("");
//			StringBuffer cellIndex = new StringBuffer("");
//			StringBuffer orgid = new StringBuffer("");
//			for (int i = 0; i < vo.getOrgIds().length; i++) {
//				orgid.append(orgid.toString().equals("") ? "'"
//						+ vo.getOrgIds()[i] + "'" : ",'" + vo.getOrgIds()[i]
//						+ "'");
//
//			}
			
			
			List<AfOrg> orgList = AFOrgDelegate.selectOrgByIds(vo.getOrgIds());
			for (int i = 0; i < orgList.size(); i++) {
				orgMap.put(orgList.get(i).getOrgId(), orgList.get(i).getOrgName());
			}
			
			String targets = vo.getTargets();
			String [] allTargets = targets.split(",");
			for (int i = 0; i <allTargets.length; i++) {
				String target  = allTargets[i];//A1411_1410_E5
				String cellCnName = getCellCnName(target);
				if(cellCnName!=""){
					itemMap.put(target, cellCnName);
				}
			}
			
			if (true) {
				String start ="";
				String end = "";
				if(null != vo.getStartYear() && null != vo.getStartMonth()){
					start = vo.getStartYear()+"-"+( vo.getStartMonth());
					end = vo.getEndYear()+"-"+(vo.getEndMonth());
				}else{
					String start1[] = vo.getStartDate().split("-");
					String end1[] = vo.getEndDate().split("-");
					start = start1[0]+"-"+(Integer.parseInt(start1[1])<10 ? start1[1]  : start1[1]);
					end = end1[0]+"-"+(Integer.parseInt(end1[1])<10 ? end1[1] : end1[1]);
				}
				vo.setStartDate(start);
				vo.setEndDate(end);
				dateMap.put(start, start);
				dateMap.put(end, end);


				int startYear1 = Integer.parseInt(start.split("-")[0]);
				int startMonth1 = Integer.parseInt(start.split("-")[1]);
				int endYear1 = Integer.parseInt(end.split("-")[0]);
				int endMonth1 = Integer.parseInt(end.split("-")[1]);

				List<String> dateList = new ArrayList<String>();
				dateList.add(startYear1 + "-" + startMonth1);
				dateList.add(endYear1 + "-" + endMonth1);

				if (startYear1 == endYear1) {
					for (int i = startMonth1 + 1; i < endMonth1; i++) {
						String yearmonth = startYear1 + "-"
								+ (i < 10 ? "0" + i : i);
						dateMap.put(yearmonth, yearmonth);
						dateList.add(startYear1 + "-" + i);
					}
				} else {
					for (int i = startMonth1 + 1; i <= 12; i++) {
						String yearmonth = startYear1 + "-"
								+ (i < 10 ? "0" + i : i);
						dateMap.put(yearmonth, yearmonth);
						dateList.add(startYear1 + "-" + i);
					}
					for (int i = startYear1 + 1; i < endYear1; i++) {
						for (int j = 1; j <= 12; j++) {
							String yearmonth = i + "-" + (j < 10 ? "0" + j : j);
							dateMap.put(yearmonth, yearmonth);
							dateList.add(i + "-" + j);
						}
					}
					for (int i = 1; i < endMonth1; i++) {
						String yearmonth = endYear1 + "-"
								+ (i < 10 ? "0" + i : i);
						dateMap.put(yearmonth, yearmonth);
						dateList.add(endYear1 + "-" + i);
					}
				}
				strYearMonth = "";
				for (String str : dateList) {
					strYearMonth += "'" + str + "',";
				}
				if (strYearMonth != null && !strYearMonth.equals("")) {
					strYearMonth = strYearMonth.substring(0,
							strYearMonth.length() - 1);
				}
			}
			dataMap  = SearchPbocDataDelegate.searchPBOCData(orgStr, startYear, startMonth, endYear, endMonth, mcurrId, repFreqId, templateIdAndVersionIdAndCellName);

			
			vo.setItemMap(itemMap);
			vo.setOrgMap(orgMap);
			vo.setDateMap(dateMap);
			vo.setDataMap(dataMap);
		return vo;
	}
	public static String round(String v, int scale) {
		if (v == null)
			return "";

		try {
			BigDecimal bdFinalVal = new BigDecimal("1.00");
			BigDecimal bdChangeVal = new BigDecimal(v);

			return bdChangeVal.divide(bdFinalVal, scale,
					BigDecimal.ROUND_HALF_UP).toString();
		} catch (Exception e) {
			return "";
		}
	}

	private static boolean stringIsEmety(String str) {

		if (str == null || str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	private static String getLastMonthDate(String date) {

		int year = Integer.parseInt(date.split("-")[0]);
		int month = Integer.parseInt(date.split("-")[1]);

		String result = "";

		if (month != 1) {
			result = year + "-"
					+ ((month - 1) < 10 ? "0" + (month - 1) : (month - 1))
					+ "-01";
		} else {
			result = (year - 1) + "-12-31";
		}

		return result;
	}

	private static String getLastSeasonDate(String date) {

		int year = Integer.parseInt(date.split("-")[0]);
		int month = Integer.parseInt(date.split("-")[1]);

		String result = "";

		if (month == 6 || month == 9 || month == 12) {
			result = year + "-"
					+ ((month - 3) < 10 ? "0" + (month - 3) : (month - 3))
					+ "-01";
		} else if (month == 3) {
			result = (year - 1) + "-12-31";
		}

		return result;
	}

	private String getLastHalfYearDate(String date) {

		int year = Integer.parseInt(date.split("-")[0]);
		int month = Integer.parseInt(date.split("-")[1]);

		String result = "";

		if (month == 12) {
			result = year + "-"
					+ ((month - 6) < 10 ? "0" + (month - 6) : (month - 6))
					+ "-30";
		} else if (month == 6) {
			result = (year - 1) + "-12-31";
		}

		return result;
	}

	private String getLastYearDate(String date) {

		int year = Integer.parseInt(date.split("-")[0]);
		int month = Integer.parseInt(date.split("-")[1]);

		String result = "";
		result = (year - 1) + "-" + (month < 10 ? "0" + month : month) + "-01";

		return result;
	}

	private String getLastYearLastDayDate(String date) {

		int year = Integer.parseInt(date.split("-")[0]);
		int month = Integer.parseInt(date.split("-")[1]);

		String result = "";

		result = (year - 1) + "-12-31";

		return result;
	}
	
	
	public static Map<String, String> searchPBOCData(String orgStr ,String startYear , String startMonth ,String endYear ,String endMonth, String mcurrId, String repFreqId, String[] templateIdAndVersionIdAndCellName ){
		String hql = "select A.org_id||'_'||A.template_id||'_'||A.version_id||'_'|| AF.CELL_NAME||'_'||a.riqi as dataKey, I.CELL_DATA as dataValue FROM ("
				+ "SELECT B.ORG_NAME, A.REP_ID,A.ORG_ID,A.TEMPLATE_ID,A.REP_NAME, A.VERSION_ID,A.YEAR||'-'||A.TERM  as riqi FROM AF_REPORT A ,AF_ORG B WHERE  A.ORG_ID=B.ORG_ID ";
		if (orgStr != "")
			hql += " AND A.ORG_ID IN (" + orgStr + ") ";// 机构
		if (Config.DB_SERVER_TYPE.equals("oracle")) {
			if (!startYear.equals("") && !startMonth.equals(""))// 开始时间
				hql += " AND TO_DATE(A.YEAR||'-'||A.TERM,'yyyy-MM')>=TO_DATE("
						+ startYear
						+ "||'-'||"
						+ startMonth
						+ ",'yyyy-MM') ";
			if (!endYear.equals("") && !endMonth.equals(""))// 结束时间
				hql += " AND TO_DATE(A.YEAR||'-'||A.TERM,'yyyy-MM')<=TO_DATE("
						+ endYear + "||'-'||" + endMonth + ",'yyyy-MM') ";
		}
		if (Config.DB_SERVER_TYPE.equals("sqlserver")) {
			if (!startYear.equals("") && !startMonth.equals(""))// 开始时间
				hql += " AND CONVERT(datatime,A.YEAR+'-'+A.TERM+'-1',120)>=CONVERT(datatime,"
						+ startYear + "+'-'+" + startMonth + "+'-1',120) ";
			if (!endYear.equals("") && !endMonth.equals(""))// 结束时间
				hql += " AND CONVERT(datatime,A.YEAR+'-'+A.TERM+'-1',120)>=CONVERT(datatime,"
						+ endYear + "+'-'+" + endMonth + "+'-1',120) ";
		}
		
		if(Config.DB_SERVER_TYPE.equals("db2")){
			if (!startYear.equals("") && !startMonth.equals(""))// 开始时间
				hql += " AND DATE(A.YEAR||'-'||A.TERM||'-01')>=DATE("
						+ startYear
						+ "||'-'||"
						+ startMonth
						+ "||'-01') ";
			if (!endYear.equals("") && !endMonth.equals(""))// 结束时间
				hql += " AND DATE(A.YEAR||'-'||A.TERM||'-01')<=DATE("
						+ endYear + "||'-'||" + endMonth + "||'-01') ";
		}
		if (mcurrId != null && !mcurrId.equals(""))
			hql += " AND A.CUR_ID=" + mcurrId + " ";// 币种
		if (repFreqId != null && !repFreqId.equals(""))// 口径
			hql += " AND A.REP_FREQ_ID=" + repFreqId;
		String whereSql = "";
		// String[] templateIdAndVersionIdAndCellName =
		// request.getParameterValues("templateIdAndVersionIdAndCellName");
		if (templateIdAndVersionIdAndCellName != null
				&& templateIdAndVersionIdAndCellName.length > 0) {
			for (int i = 0; i < templateIdAndVersionIdAndCellName.length; i++) {
				String templateId = templateIdAndVersionIdAndCellName[i]
						.split("_")[0];// 模板ID
				String versionId = templateIdAndVersionIdAndCellName[i]
						.split("_")[1];// 版本ID
				// String cellName =
				// templateIdAndVersionIdAndCellName[i].split("_")[2];//单元格名称

				whereSql += "(A.TEMPLATE_ID='" + templateId
						+ "' AND A.VERSION_ID='" + versionId + "')";// +"' and a.CELL_NAME='"+cellName+"')";
				if (i != templateIdAndVersionIdAndCellName.length - 1)
					whereSql += " OR ";

			}
		}
		// 避免重复数据
		whereSql += " AND A.TIMES = '1'";
		hql += " AND " + "(" + (whereSql == "" ? "1=1" : whereSql) + ")";
		hql += "  ) A ,  AF_PBOCREPORTDATA I  ";
		hql += " , (SELECT AF.CELL_NAME,AF.ROW_NAME,AF.COL_NAME,AF.CELL_ID FROM AF_CELLINFO AF WHERE";
		whereSql = "";
		if (templateIdAndVersionIdAndCellName != null
				&& templateIdAndVersionIdAndCellName.length > 0) {
			for (int i = 0; i < templateIdAndVersionIdAndCellName.length; i++) {
				String cellName = templateIdAndVersionIdAndCellName[i]
						.split("_")[2];// 单元格名称

				whereSql += " (AF.CELL_NAME='" + cellName + "')";
				if (i != templateIdAndVersionIdAndCellName.length - 1)
					whereSql += " OR ";

			}
		}
		hql += " (" + (whereSql == "" ? "1=1" : whereSql) + ")";
		hql += " ) AF WHERE A.REP_ID=I.REP_ID AND I.CELL_ID=AF.CELL_ID";
		hql += " ORDER BY AF.CELL_NAME ,to_date(A.riqi ,'yyyy-MM')  ,A.ORG_NAME desc";
		System.out.println("--" + hql);
		
		List<Object[]> objList = null;
		DBConn conn=null;
		Session session=null;
		Statement stmt = null;
		Connection connection=null;
		Map<String,String> dataMap = null;
		ResultSet rs = null;
		try {
			conn = new DBConn();
			session = conn.openSession();
			connection = session.connection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(hql);
			objList = new ArrayList<Object[]>();
			dataMap = new HashMap<String,String>();
			while (rs.next()) {
				Object[] obj = new Object[2];
				String key = (String) rs.getObject("dataKey");
				String value = (String) rs.getObject("dataValue");
				dataMap.put(key, value);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rs = null;
			}
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}if(conn != null){
				conn.closeSession();
			}
		}
		return dataMap;
	}
	
	
	public static String getCellCnName(String target){
		DBConn conn = null;
		Session session = null;
		List resultList = null;
		String []targets  = target.split("_");
		String cellCnName = "";
		try {
			conn = new DBConn();
			session = conn.openSession();

//	SELECT t.row_name||t.col_name FROM af_cellinfo t where t.template_id ='A1411' and t.version_id = '1410' and t. cell_name='E5';
			String hql = "from AfCellinfo t where t.templateId='"+targets[0]+"' and t.versionId='"+targets[1]+"' and t.cellName = '"+targets[2]+"'";
			
			List list = session.createQuery(hql).list();
			
			if(list!=null){
				AfCellinfo cellinfo = (AfCellinfo) list.get(0);
				cellCnName = cellinfo.getRowName()+cellinfo.getColName();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (session != null)
				conn.closeSession();
		}
		return cellCnName;
	}
}
