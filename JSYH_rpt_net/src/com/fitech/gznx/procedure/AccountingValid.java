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

	/** �跽�������־ */
	private static String TAG_HAPPEN_DEB_BAL = "hd";
	/** �����������־ */
	private static String TAG_HAPPEN_CRE_BAL = "hc";
	/** �跽����־ */
	private static String TAG_CURR_DEB_BAL = "cd";
	/** ��������־*/
	private static String TAG_CURR_CRE_BAL = "cc";
	
	/** �跽������ */
	private static String HAPPEN_DEB_BAL = "HAPPEN_DEB_BAL";
	/** ���������� */
	private static String HAPPEN_CRE_BAL = "HAPPEN_CRE_BAL";
	/** �跽��� */
	private static String CURR_DEB_BAL = "CURR_DEB_BAL";
	/** ������� */
	private static String CURR_CRE_BAL = "CURR_CRE_BAL";
	
	/**
	 * �ó�ÿ����Ŀ�ľ���ֵ
	 * @param orclCon db����
	 * @param cellMap ����Ԫ��map
	 * @param cellNameLst ��ȡֵ��Ŀ
	 * @param dates ����
	 * @param orgId ������
	 * @param freqId Ƶ��
	 * @param curId ����
	 * @param templateType ģ������
	 * @return ��Ŀ���Ӧֵ������
	 * @throws Exception
	 */
	public static String[][] AccoutingBalanceVal(Connection orclCon, Map cellMap, List cellNameLst,
			String dates,String orgId,String freqId,String curId,
			Integer templateType) throws Exception{

		//��Ԫ��ֵ����
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
			// ��ʼ������
			cellValue = new String[size][];
			
			// ��ѯ���ʽ�е�Ԫ����ϱ���ֵ�͵�Ԫ������
			for (int i = 0; i < size; i++) {
				stmt = orclCon.createStatement();
				String cellName = cellNameLst.get(i).toString();
				
				// �ж��Ƿ����ڿ�ĿУ��
				if (cellName.indexOf(".") <= 1) {
					// Ĭ���ϱ�ֵ��ֹ�����Ϊ��
					String reportValue = "0.00";
					
					if (null != cellMap.get(cellName))
						reportValue = cellMap.get(cellName).toString();
					
					cellValue[i] = new String[] { reportValue.trim(), cellName };
					
				} else {
					
					//��ĿУ�鴦��
					String accting[] = cellName.split("\\.");
					
					//��ʼ�������ֵ
					String reportValue = "0.00";
					
					if (accting.length == 3) {
						
						reportValue = "0.00";
						
						//���Ŀ��Ӧֵ
						sql = AcctHql(accting[2],accting[0],orgId,dates,accting[1],freqId);
						
						orclRS = stmt.executeQuery(sql);
						
						// ָ���Ӧֵ
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
	 * �淶��sql
	 * @param balTag ȡ���ֶ�
	 * @param vsId ָ���
	 * @param orgId ������
	 * @param dates ����
	 * @param vccyId ����
	 * @param freqId Ƶ��
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
		
		//��������ȥaccouting��ȡ��Ӧ�ֶ�
		/**���������ݿ��ж� ���Ը� 2012-01-18*/
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
