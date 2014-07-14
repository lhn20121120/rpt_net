package com.fitech.gznx.procedure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.cbrc.smis.common.Config;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfReport;

public class AccountingValid {

	/** 借方发生额标志 */
	private static String TAG_HAPPEN_DEB_BAL = "hd";
	/** 贷方发生额标志 */
	private static String TAG_HAPPEN_CRE_BAL = "hc";
	/** 借方余额标志 */
	private static String TAG_CURR_DEB_BAL = "cd";
	/** 贷方余额标志*/
	private static String TAG_CURR_CRE_BAL = "cc";
	
	/** 借方发生额 */
	private static String HAPPEN_DEB_BAL = "HAPPEN_DEB_BAL";
	/** 贷方发生额 */
	private static String HAPPEN_CRE_BAL = "HAPPEN_CRE_BAL";
	/** 借方余额 */
	private static String CURR_DEB_BAL = "CURR_DEB_BAL";
	/** 贷方余额 */
	private static String CURR_CRE_BAL = "CURR_CRE_BAL";
	
	/**
	 * 得出每个栏目的具体值
	 * @param orclCon db连接
	 * @param cellMap 本表单元格map
	 * @param cellNameLst 需取值栏目
	 * @param dates 期数
	 * @param orgId 机构号
	 * @param freqId 频度
	 * @param curId 币种
	 * @param templateType 模板类型
	 * @return 栏目与对应值的数组
	 * @throws Exception
	 */
	public static String[][] AccoutingBalanceVal(Connection orclCon, Map cellMap, List cellNameLst,
			String dates,String orgId,String freqId,String curId,
			Integer templateType) throws Exception{

		//单元格值数组
		String[][] cellValue = null;

		if (cellMap == null || cellNameLst == null || dates == null 
				|| orgId == null || freqId == null )
			return cellValue;
		
		Statement stmt = null;
		ResultSet orclRS = null;
		boolean flag = false;
		
		try {
			String sql = null;

			int size = cellNameLst.size();
			// 初始化数组
			cellValue = new String[size][];
			
			// 查询表达式中单元格的上报报值和单元格名称
			for (int i = 0; i < size; i++) {
				stmt = orclCon.createStatement();
				String cellName = cellNameLst.get(i).toString();
				
				// 判断是否属于科目校验
				if (cellName.indexOf(".") <= 1) {
					// 默认上报值防止填报数据为空
					String reportValue = "0.00";
					
					if (null != cellMap.get(cellName))
						reportValue = cellMap.get(cellName).toString();
					
					cellValue[i] = new String[] { reportValue.trim(), cellName };
					
				} else {
					
					//科目校验处理
					String accting[] = cellName.split("\\.");
					
					//初始化查出的值
					String reportValue = "0.00";
					
					if (accting.length == 3) {
						
						reportValue = "0.00";
						
						//求科目对应值
						sql = AcctHql(accting[2],accting[0],orgId,dates,accting[1],freqId);
						
						orclRS = stmt.executeQuery(sql);
						
						// 指标对应值
						if (orclRS.next() && orclRS.getString("val".toUpperCase()) != null) {
							reportValue = orclRS.getString("val".toUpperCase());
						}
						
						orclRS.close();
						stmt.close();
						
						flag = false;
						
						cellValue[i] = new String[] { reportValue.trim(), cellName };
						
					}else{
						cellValue[i] = new String[] { "0.00", cellName };
					}
					
					
				}
			}
		} catch (SQLException e) {
			flag = true;
			throw new Exception(e.getMessage());
		} finally {
			if (flag) {
				if (orclRS != null)
					orclRS.close();
				if (stmt != null)
					stmt.close();
			}
		}
		
		return cellValue;

	}
	
	/**
	 * 规范出sql
	 * @param balTag 取数字段
	 * @param vsId 指标号
	 * @param orgId 机构号
	 * @param dates 期数
	 * @param vccyId 币种
	 * @param freqId 频度
	 * @return
	 */
	private static String AcctHql(String balTag,String vsId,String orgId,
			String dates, String vccyId,String freqId){
		
		if(balTag==null||vsId==null||orgId==null||
				dates==null||vccyId==null||freqId==null)
			return null;
			
		if(balTag.toLowerCase().equals(TAG_CURR_CRE_BAL)){
			balTag = CURR_CRE_BAL;
		}else if(balTag.toLowerCase().equals(TAG_CURR_DEB_BAL)){
			balTag = CURR_DEB_BAL;
		}else if(balTag.toLowerCase().equals(TAG_HAPPEN_CRE_BAL)){
			balTag = HAPPEN_CRE_BAL;
		}else if(balTag.toLowerCase().equals(TAG_HAPPEN_DEB_BAL)){
			balTag = HAPPEN_DEB_BAL;
		}else{
			return null;
		}
		
		//根据条件去accouting表取相应字段
		/**已增加数据库判断 卞以刚 2012-01-18*/
		String hql="";
		if(Config.DB_SERVER_TYPE.equals("oracle"))
		{
			 hql = "select sum("+ balTag +") as val from e_accounting e where " 
				+ "trim(e.vs_id)='" + vsId + "' and " 
				+ "e.accout_type=" +
						"(select cfa.accout_type from c_freq_acounttype cfa where cfa.freq_id=" + freqId + ") and "
				+ "date_id=to_date('"+ dates +"','yyyy-MM-dd') and "
				+ "trim(org_id) in " +
						"(select voc.org_id from view_org_collect voc where voc.pre_org_id='" + orgId + "') and "
				+ "v_ccy_id='"+ vccyId +"'";
		}
		if(Config.DB_SERVER_TYPE.equals("sqlserver"))
		{
			 hql = "select sum("+ balTag +") as val from e_accounting e where " 
				+ "rtrim(ltrim(e.vs_id))='" + vsId + "' and " 
				+ "e.accout_type=" +
						"(select cfa.accout_type from c_freq_acounttype cfa where cfa.freq_id=" + freqId + ") and "
				+ "date_id=convert(datetime,'"+ dates +"',120) and "
				+ "rtrim(ltrim(org_id)) in " +
						"(select voc.org_id from view_org_collect voc where voc.pre_org_id='" + orgId + "') and "
				+ "v_ccy_id='"+ vccyId +"'";
		}
		return hql;
		
	}

}
