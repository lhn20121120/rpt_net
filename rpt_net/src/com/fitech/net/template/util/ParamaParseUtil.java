package com.fitech.net.template.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.cbrc.smis.dao.FitechConnection;
/**
 * 用来处理SQL模板中的参数,给相应的参数赋值
 * @author Yao
 *
 */
public class ParamaParseUtil
{
	/**
	 * SQL参数开始标志
	 */
	private final String PARAM_START_SIGN = "(@";

	/**
	 * SQL参数结束标志
	 */
	private final String PARAM_END_SIGN = "@)";
	/**
	 * 几张TOP表的参数
	 */
	private final String G1301_PARAMA = "Top_G1301";
	private final String G1302_PARAMA = "Top_G1302";
	private final String G1303_PARAMA = "Top_G1303";
	private final String G1304_PARAMA = "Top_G1304";
	private final String G1401_PARAMA = "Top_G1401";
	private final String G1402_PARAMA = "Top_G1402";
	private final String G1403_PARAMA = "Top_G1403";
	private final String G2300_PARAMA = "Top_G2300";
	private final String G2400_PARAMA = "Top_G2400";
	
	/**
	 * 年初日期参数
	 */
	private final String YEAR_START_DATE = "年初日期";
	
	/**
	 * 上年末日期参数
	 */
	private final String LAST_YEAR_END_DATE= "上年末日期";
	
	/**
	 * 报告日参数
	 */
	private final String REPORT_DATE ="报告日";
	
	/**
	 * 机构ID参数
	 */
	private final String ORG_ID="机构ID";
	
	
	/* G1300各个子报表对应的五级分类名称 */
	public final String G1300_FiveLevelName[] =
	{ "'关注类'", "'次级类'", "'可疑类'", "'损失类'" };

	/* G1300 SQL模板 */
	public final String G1300_SQLMODE = "select MCR_ID from "
			+ "(select a.MCR_ID, sum(b.EER_AMT*(a.EC_NORMALBALANCE+a.EC_OVERDUEBALANCE)) as MaxValue, "
			+ "row_number() over(order by sum(b.EER_AMT*(a.EC_NORMALBALANCE+a.EC_OVERDUEBALANCE)) desc) as NUM"
			+ " from E_CREDIT a , E_EXCHANGERATE b, V_CURRENCY c, V_CLASSIFYTYPE d, V_BUSINESSTYPE e "
			+ "where e.VBT_ID=a.VBT_ID and d.VCLA_ID=a.VCLA_ID and b.VC_ID=c.VC_ID and a.VC_ID=c.VC_ID "
			+ "and b.VD_ID= (@报告日@) and d.VCLA_NAME=(@五级分类名称@) and a.EC_CDate=(@报告日@) and (e.VBT_ID not like '2%' and e.VBT_ID<>'1020015' and e.VBT_ID not in ('1020018','1090010','1090015')) "
			+ "and a.MCR_ID is not NULL and a.ORG_ID in((@机构ID@)) group by a.MCR_ID)  T1 where int(T1.NUM)=(@排名@)";

	/* G1401 SQL模板 */
	public final String G1401_SQLMODE = "select MCR_ID  from "
			+ "(select a.MCR_ID, sum(b.EER_AMT*(case when a.VBT_ID in ('1110070','1110080','1120003') then (a.EC_BUSINESSSUM-a.EC_NORMALBALANCE-a.EC_OVERDUEBALANCE) else "
			+ " (a.EC_NORMALBALANCE+a.EC_OVERDUEBALANCE) end)) as MaxValue, row_number() over(order by sum(b.EER_AMT*(case when a.VBT_ID in ('1110070','1110080','1120003')"
			+ " then (a.EC_BUSINESSSUM-a.EC_NORMALBALANCE-a.EC_OVERDUEBALANCE) else (a.EC_NORMALBALANCE+a.EC_OVERDUEBALANCE) end)) desc) as NUM "
			+ " from E_Credit a, E_ExchangeRate b, V_Currency c where b.VD_ID=(@报告日@) and b.VC_ID=c.VC_ID and a.VC_ID=c.VC_ID  and a.EC_CDate=(@报告日@) "
			+ " and a.VBT_ID<>'1020015' and (substr(a.VBT_ID,1,4) in('2010','2030','2040','2050','2080','2090') or a.VBT_ID like '1%') "
			+ " and a.MCR_ID is not null and a.ORG_ID in((@机构ID@)) group by a.MCR_ID)  T1 where int(T1.NUM)=(@排名@)";
	
	/* G1402 SQL模板 */
	public final String G1402_SQLMODE = "select VCI_ID from "
			+ "(select VCI_ID, SUM(EER_AMT*ED_AMT) as MaxValue, row_number() over(order by SUM(EER_AMT*ED_AMT) desc) as NUM  "
			+ "from E_DLDT a, V_DLDTTYPE b,V_Currency c, E_ExchangeRate d "
			+ "where VD_DES in ('拆放同业','质押逆回购','买断逆回购','存放同业') "
			+ "and a.VC_ID=c.VC_ID and c.VC_ID = d.VC_ID and d.VD_ID=(@报告日@) "
			+ "and a.ED_CDate=(@报告日@) and a.VD_ID=b.VD_ID and a.VCI_ID is not NULL and a.ORG_ID in((@机构ID@))  group by VCI_ID) T1 "
			+ "where int(T1.NUM)=(@排名@)";
	
	/* G1403 SQL模板 */
	public final String G1403_SQLMODE = "select VCI_ID  from "
			+ "(select a.VCI_ID, sum(b.EER_AMT*(case when a.VBT_ID in ('1110070','1110080','1120003') then (a.EC_BUSINESSSUM-a.EC_NORMALBALANCE-a.EC_OVERDUEBALANCE) else "
			+ " (a.EC_NORMALBALANCE+a.EC_OVERDUEBALANCE) end)) as MaxValue, row_number() over(order by sum(b.EER_AMT*(case when a.VBT_ID in ('1110070','1110080','1120003')"
			+ " then (a.EC_BUSINESSSUM-a.EC_NORMALBALANCE-a.EC_OVERDUEBALANCE) else (a.EC_NORMALBALANCE+a.EC_OVERDUEBALANCE) end)) desc) as NUM "
			+ " from E_Credit a, E_ExchangeRate b, V_Currency c where b.VD_ID=(@报告日@) and b.VC_ID=c.VC_ID and a.VC_ID=c.VC_ID  and a.EC_CDate=(@报告日@) "
			+ " and (a.VBT_ID not like '2%' and a.VBT_ID<>'1020015' and a.VBT_ID not in ('1020018','1090010','1090015')) "
			+ " and a.VCI_ID is not null and a.ORG_ID in((@机构ID@)) group by a.VCI_ID)  T1 where int(T1.NUM)=(@排名@)";

	public final String G2300_SQLMODE = "select MCR_ID from (select E.MCR_ID, SUM(b.EER_AMT*a.EDEP_BALANCE), row_number() "
		+ " over(order by SUM(b.EER_AMT*a.EDEP_BALANCE) desc) as NUM from E_DEPOSIT a , E_EXCHANGERATE b, V_CURRENCY c, V_DTTYPE d, "
		+ " M_CUSTOMERINFO e  where e.MCI_ID=a.VCI_ID and d.VDT_ID=a.VDT_ID and  b.VC_ID=c.VC_ID and a.VC_ID=c.VC_ID and b.VD_ID=(@报告日@)"
		+ " and a.EDEP_CDate=(@报告日@) and a.VDT_ID like '2010%' and E.MCR_ID IS NOT NULL and a.ORG_ID in((@机构ID@))"
		+ " group by E.MCR_ID)  T1 where int(T1.NUM)=(@排名@)";

	/* G2400 SQL模板 */
	public final String G2400_SQLMODE = "select temp.mmi from(select row_number() over(order by sum(ed.ed_amt*eex.eer_amt) desc) as num,mc.MCI_ID mmi,mc.MCI_NAME,sum(ed.ed_amt*eex.eer_amt) " +
			"from M_CUSTOMERINFO mc,e_dldt ed,v_customertype vc,v_currency vcy,e_exchangerate eex " +
			"where mc.mci_id=ed.vci_id and vc.vct_id=mc.mct_id and vc.vct_name in('境内银行','境内银行','境内非银行金融机构','境外非银行金融机构') " +
			"and ed.vd_id='BR' and ed.vc_id=vcy.vc_id and ed.vc_id=eex.vc_id and eex.vd_id=ed.ed_cdate and ed.ed_cdate=(@报告日@) and ed.ORG_ID in((@机构ID@)) " +
			"group by mc.mci_id,mc.mci_name) temp where int(num)=(@排名@)";
	
	
	
	/**
	 * 将SQL表达式中的所有参数替换成具体的值
	 * 
	 * @param sqlFormual
	 * @param paramMap
	 * @return
	 */
	public StringBuffer replaceParamToValue(String sqlFormual, Map paramMap) throws Exception
	{
		if (sqlFormual == null || sqlFormual.equals(""))
			return null;
		StringBuffer resultStr = new StringBuffer("");

		/* 获取第一个参数的开始标志位置 */
		int index_of_startSign = sqlFormual.indexOf(this.PARAM_START_SIGN);
		/* 解析到的位置 */
		int parseIndex = 0;

		while (index_of_startSign >= 0)
		{
			/* 获得对应的结束标志位置 */
			int index_of_endSign = sqlFormual.indexOf(this.PARAM_END_SIGN, index_of_startSign);

			if (index_of_endSign == -1)
				throw new StringIndexOutOfBoundsException("参数: " + sqlFormual + " 错误!缺少结束标志!");

			/* 取出一对"<@ @>"中的参数名称 */
			String paramName = sqlFormual.substring(index_of_startSign + 2, index_of_endSign);
			String paramValue = null;
			
			/*如果参数Map中存在这个参数,则替换这个参数*/
			if (paramMap.containsKey(paramName))
			{
				//// System.out.println("************");
				paramValue = (String) paramMap.get(paramName);
			}
			/*特殊参数处理*/
			else
			{
				paramValue = this.parseOtherParamToValue(paramName,paramMap);
			}

			/* 将解析好的部分追加到解析结果中 */
			resultStr.append(sqlFormual.substring(parseIndex, index_of_startSign));
			resultStr.append(paramValue);
			parseIndex = index_of_endSign + 2;

			/* 取得下一个SQL语句开始标志 */
			index_of_startSign = sqlFormual.indexOf(this.PARAM_START_SIGN, index_of_endSign);
		}
		resultStr.append(sqlFormual.substring(parseIndex));

		return resultStr;
		
	}
	
	
	/**
	 * 处理特殊参数
	 * 
	 * @param paramaName
	 * @param paramMap
	 * @return
	 */
	private String parseOtherParamToValue(String paramaName,Map paramMap) throws Exception
	{
		if (paramaName == null || paramaName.equals("")||paramMap==null)
			return null;
		
		/*检查是否有TOP的参数*/
		if(paramaName.indexOf(this.G1301_PARAMA)!=-1 ||paramaName.indexOf(this.G1302_PARAMA)!=-1
				||paramaName.indexOf(this.G1303_PARAMA)!=-1 ||paramaName.indexOf(this.G1304_PARAMA)!=-1
				||paramaName.indexOf(this.G1401_PARAMA)!=-1 ||paramaName.indexOf(this.G1402_PARAMA)!=-1
				||paramaName.indexOf(this.G1403_PARAMA)!=-1
				||paramaName.indexOf(this.G2300_PARAMA)!=-1 ||paramaName.indexOf(this.G2400_PARAMA)!=-1)
		{
			String[] paramSplit = paramaName.split("_");
			if(paramSplit!=null)
			{
				try
				{
					/*报表ID*/
					String repId = paramSplit[1];
					/*排名*/
					int topNum = Integer.parseInt(paramSplit[2]);
						
					String result = this.getTopRecordId(repId,topNum,paramMap);
					
					if(result==null || result.equals(""))
						result = "'0'";
					else
						result = "'"+result+"'";
					
					return result;
				}
				catch (NumberFormatException e)
				{
					throw new IllegalArgumentException("参数: " + paramaName + "错误!");
				}
			}
			else
			{
				throw new IllegalArgumentException("参数: " + paramaName + "错误!");
			}
		}
		/*检查是否有去年末日期的参数*/
		else if(paramaName.equals(this.LAST_YEAR_END_DATE))
		{
			if(paramMap.get(this.REPORT_DATE)==null)
				throw new IllegalArgumentException("缺少(@报告日@)参数: ");
			String result = this.getLastYearEndDate((String)paramMap.get(this.REPORT_DATE));
			if(result==null || result.equals(""))
				result = "0";
			return result;
		}
		/*检查是否有年初日期的参数*/
		else if(paramaName.equals(this.YEAR_START_DATE))
		{
			if(paramMap.get(this.REPORT_DATE)==null)
				throw new IllegalArgumentException("缺少(@报告日@)参数: ");
			String result = this.getYearStartDate((String)paramMap.get(this.REPORT_DATE));
			if(result==null || result.equals(""))
				result = "0";
			return result;
		}
		else
		{
			throw new IllegalArgumentException("不能解析参数: " + paramaName + " ,该SQL语句将不能正常运行!");
		}
	}
	
	/**
	 * 最大10(20)家
	 * 
	 * @param reportId
	 *            报表ID
	 * @param topNum
	 *            排名第几位
	 * @param parama
	 *            参数Map
	 * @return
	 * @throws Exception
	 */
	private String getTopRecordId(String reportId, int topNum, Map paramaMap) throws Exception
	{
		String sql = null;
		if(paramaMap.get(this.REPORT_DATE)==null)
			throw new IllegalArgumentException("缺少(@报告日@)参数: ");
		if(paramaMap.get(this.ORG_ID)==null)
			throw new IllegalArgumentException("缺少(@机构ID@)参数: ");
		
		/*报告日*/
		String reportDate = (String)paramaMap.get(this.REPORT_DATE);
		/*机构*/
		String orgId =(String)paramaMap.get(this.ORG_ID);
		
		if (reportId.equals("G1301"))
		{
			sql = G1300_SQLMODE.replaceAll("\\(@报告日@\\)",reportDate)
					.replaceAll("\\(@排名@\\)", String.valueOf(topNum))
					.replaceAll("\\(@五级分类名称@\\)", G1300_FiveLevelName[0])
					.replaceAll("\\(@机构ID@\\)", orgId);
					//.replaceAll("\\(@机构ID@\\)",reportDate);

		}
		else if (reportId.equals("G1302"))
		{
			sql = G1300_SQLMODE.replaceAll("\\(@报告日@\\)", reportDate)
					.replaceAll("\\(@排名@\\)", String.valueOf(topNum))
					.replaceAll("\\(@五级分类名称@\\)", G1300_FiveLevelName[1])
					.replaceAll("\\(@机构ID@\\)", orgId);

		}
		else if (reportId.equals("G1303"))
		{
			sql = G1300_SQLMODE.replaceAll("\\(@报告日@\\)", reportDate)
				.replaceAll("\\(@排名@\\)", String.valueOf(topNum))
				.replaceAll("\\(@五级分类名称@\\)", G1300_FiveLevelName[2])
				.replaceAll("\\(@机构ID@\\)", orgId);
		}
		else if (reportId.equals("G1304"))
		{
			sql = G1300_SQLMODE.replaceAll("\\(@报告日@\\)", reportDate)
				.replaceAll("\\(@排名@\\)", String.valueOf(topNum))
				.replaceAll("\\(@五级分类名称@\\)", G1300_FiveLevelName[3])
				.replaceAll("\\(@机构ID@\\)", orgId);
		}
		else if (reportId.equals("G1401"))
		{
			sql = G1401_SQLMODE.replaceAll("\\(@报告日@\\)", reportDate)
				.replaceAll("\\(@排名@\\)", String.valueOf(topNum))
				.replaceAll("\\(@机构ID@\\)", orgId);
		}
		else if (reportId.equals("G1402"))
		{
			sql = G1402_SQLMODE.replaceAll("\\(@报告日@\\)", reportDate)
			.replaceAll("\\(@排名@\\)", String.valueOf(topNum))
			.replaceAll("\\(@机构ID@\\)", orgId);
			// System.out.println("sql==="+sql);
		}
		else if (reportId.equals("G1403"))
		{
			sql = G1403_SQLMODE.replaceAll("\\(@报告日@\\)", reportDate)
			.replaceAll("\\(@排名@\\)", String.valueOf(topNum))
			.replaceAll("\\(@机构ID@\\)", orgId);
		}
		else if (reportId.equals("G2300"))
		{
			sql = G2300_SQLMODE.replaceAll("\\(@报告日@\\)", reportDate)
			.replaceAll("\\(@排名@\\)", String.valueOf(topNum))
			.replaceAll("\\(@机构ID@\\)", orgId);
		}
		else if (reportId.equals("G2400"))
		{
			sql = G2400_SQLMODE.replaceAll("\\(@报告日@\\)", reportDate)
			.replaceAll("\\(@排名@\\)", String.valueOf(topNum))
			.replaceAll("\\(@机构ID@\\)", orgId);
		}

		// System.out.println("SQL==" + sql);
		return executeSQL(sql);

	}

	/**
	 * 获得该日期的年初日期
	 * 
	 * @param dataId
	 *            日期Id
	 */
	private String getYearStartDate(String dateId) throws Exception
	{
		String resultDateId = null;
		/* 根据传过来的日期ID,查出它的日期名称 */
		String sql = "select VD_NAME from V_DATE where VD_ID=" + dateId;
		String date = executeSQL(sql);

		if (date != null && !date.equals(""))
		{
			/* 年初日期 = 该日期年份的1月1号 */
			String resultDate = date.substring(0, 4) + "0101";
			resultDateId = executeSQL("select VD_ID from V_DATE where VD_NAME='" + resultDate + "'"); 
		}
		return resultDateId;
	}

	/**
	 * 获得该日期的上年末日期
	 * 
	 * @param dataId
	 *            日期Id
	 */
	private String getLastYearEndDate(String dateId) throws Exception
	{
		String resultDateId = null;
		/* 根据传过来的日期ID,查出它的日期名称 */
		String sql = "select VD_NAME from V_DATE where VD_ID=" + dateId;
		String date = executeSQL(sql);

		if (date != null && !date.equals(""))
		{
			/* 上年末日期 = 该日期上年的12月31号 */
			String resultDate = (Integer.parseInt(date.substring(0, 4)) - 1) + "1231";
			resultDateId = executeSQL("select VD_ID from V_DATE where VD_NAME='" + resultDate + "'");
		}
		return resultDateId;
	}


	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	private  String executeSQL(String sql) throws Exception
	{
		String result = null;
		Connection conn;

		// 获取数据库连接
		conn = (new FitechConnection()).getConnect();
		if (conn == null)
		{
			return "Connection Wrong!";
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try
		{
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next())
			{
				result = rs.getString(1);
			}
		}
		catch (Exception e)
		{
			result = null;
			e.printStackTrace();
		}
		finally
		{
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			close(conn);
		}

		return result;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 *            Connection
	 * @return void
	 * @exception Exception
	 */
	private  void close(Connection conn) throws Exception
	{
		if (conn != null)
		{
			try
			{
				conn.close();
			}
			catch (SQLException sqle)
			{
				throw new Exception(sqle.getMessage());
			}
		}
	}
	
	public static void main(String[] args)
	{
		ParamaParseUtil util = new ParamaParseUtil();
		String sql = "<<select A.a from A where A.a=(@Top_G2300_5@)>>";
		Map paramaMap = new HashMap();
		paramaMap.put("报告日","732492");
	
		try
		{
			// System.out.println("*********"+util.replaceParamToValue(sql,paramaMap));
		}
		catch (Exception e)
		{
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
}

