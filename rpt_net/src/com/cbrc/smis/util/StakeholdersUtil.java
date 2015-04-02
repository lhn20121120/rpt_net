package com.cbrc.smis.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.cbrc.smis.jdbc.FitechConnection;

/**
 * 取默认干系人
 * @author jianghu
 *
 */
public class StakeholdersUtil {
	private static int STAKEHOLDERS = 1;
	
	public static String[] getStakeholders(Integer repInId, String templateId, String versionId)
    {
    	FitechConnection connFactory = null;
		Statement state = null;
		ResultSet rs = null;
		Connection connection = null;
		String sql = null;
		String result[] = new String[]{"","",""};
		try {
			connFactory = new FitechConnection();
			connection = connFactory.getConn();
			state = connection.createStatement();
			
			sql = "SELECT * FROM  SYS_PARAMETER WHERE PAR_NAME = 'STAKEHOLDERS'";
			rs = state.executeQuery(sql);
			while (rs.next()) {
				STAKEHOLDERS = rs.getInt("PAR_VALUE");
			}
			rs.close();
			if(STAKEHOLDERS == 1)
				sql = "SELECT * FROM STAKEHOLDERS_MAPPING s , af_org a"
					+ " where s.orgid = a.org_id "
					+ " and s.tempalte_id is null and s.version_id is null"
			        + " and s.orgid = (select org_id from report_in r where r.rep_in_id = "+repInId+")";
			if(STAKEHOLDERS == 2)
			{
				sql += "SELECT * FROM STAKEHOLDERS_MAPPING s where s.tempalte_id = '" + templateId + "' and s.version_id = '" + versionId + "'"
						+ " and s.orgid = (select org_id from report_in r where r.rep_in_id = "+repInId+")";
			}
			System.out.println("[getStakeholders]:"+sql);
			rs = state.executeQuery(sql);
			if(rs.next())
			{
				result[0] = rs.getString("WRITER");
				result[1] = rs.getString("CHECKER");
				result[2] = rs.getString("PRINCIPAL");
			}					
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (state != null) {
					state.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("[Stakeholders: " + result[0] + "," + result[1] + "," + result[2] + "]");
		return result;
    }

}
