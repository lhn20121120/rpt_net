package com.fitech.gznx.procedure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.cbrc.smis.proc.impl.Expression;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfValidateformula;

public class ValidateP2PReport {

	private static String termValidFlag="TERM";
	/**
	 * ��Ҫ�滻Ϊ*0���ַ���
	 */
	public static String needReplaceStr="/0.00,/(0.00+0.00*12.5)";
	
	/**
	 * jdbc���� �������﷨ ����Ҫ�޸�
	 * ���Ը� 2011-12-26
	 * Ӱ���af_pbocreportdata || af_otherreportdata
	 * 
	 * ��ȡһ���ϱ��������е�Ԫ������ƺ��ϱ�ֵ��ֵ��
	 * 
	 * @param orclCon
	 *            Connection ���ݿ�������
	 * @param reportIn
	 *            ReportIn ��������
	 * @return Map key-��Ԫ������ value-�ֵ
	 * @throws Exception
	 */
	public static Map parseCell(Connection orclCon, AfReport reportIn,
			Integer templateType) throws Exception {

		if (orclCon == null || reportIn == null || templateType == null)
			return null;

		Map cellMap = null;
		PreparedStatement pStmt = null;
		ResultSet plRS = null;

		try {
			String orclSql = "";

			// �ж�ģ������
			if (templateType.toString().equals(Config.PBOC_REPORT)){
				if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle"))
					orclSql += "select b.col_num||'.'||b.cell_pid as cell_name,a.cell_data from af_pbocreportdata";
				if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver"))
					orclSql += "select b.col_num+'.'+b.cell_pid as cell_name,a.cell_data from af_pbocreportdata";
			}
//			else if (templateType.toString().equals(Config.OTHER_REPORT))
			else
				orclSql += "select b.cell_name,a.cell_data from af_otherreportdata";

			orclSql += " a left join af_cellinfo b on a.cell_id = b.cell_id where "
					+ " a.rep_id = ?";

			pStmt = orclCon.prepareStatement(orclSql.toUpperCase());
			pStmt.setLong(1, reportIn.getRepId());
			plRS = pStmt.executeQuery();

			cellMap = new HashMap();

			while (plRS.next()) {

				String cellName = plRS.getString("cell_name".toUpperCase());// ��Ԫ������
				String reportValue = plRS.getString("cell_data".toUpperCase());// �ֵ

				if (cellName != null && reportValue != null)
					cellMap.put(cellName, reportValue);

			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}
		return cellMap;
	}

	/**
	 * ��ȡһ���ϱ��������е�Ԫ������ƺ��ϱ�ֵ��ֵ��(����Ϊָ���ֵ��)
	 * 
	 * @param orclCon
	 *            Connection ���ݿ�������
	 * @param reportIn
	 *            ReportIn ��������
	 * @return Map key-��Ԫ������ value-�ֵ
	 * @throws Exception
	 */
	public static Map parseCellNew(Connection orclCon, AfReport reportIn,
			Integer templateType) throws Exception {

		if (orclCon == null || reportIn == null || templateType == null)
			return null;

		Map cellMap = null;
		PreparedStatement pStmt = null;
		ResultSet plRS = null;

		try {
			String orclSql = "";//"select b.cell_name,a.cell_data from ";

			// �ж�ģ������
			if (templateType.toString().equals(Config.PBOC_REPORT))
				orclSql += "select b.col_num||'.'||b.cell_pid as cell_name,a.cell_data from af_pbocreportdata";
//			else if (templateType.toString().equals(Config.OTHER_REPORT))
			else
				orclSql += "select b.cell_name,a.cell_data from af_otherreportdata";

			orclSql += " a left join af_cellinfo b on a.cell_id = b.cell_id where "
					+ " a.rep_id = ?";

			pStmt = orclCon.prepareStatement(orclSql.toUpperCase());
			pStmt.setLong(1, reportIn.getRepId());
			plRS = pStmt.executeQuery();

			cellMap = new HashMap();

			while (plRS.next()) {

				String cellName = plRS.getString("cell_name".toUpperCase());// ��Ԫ������
				String reportValue = plRS.getString("cell_data".toUpperCase());// �ֵ

				if (cellName != null && reportValue != null)
					cellMap.put(cellName, reportValue);

			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}
		return cellMap;
	}
	
	/**
	 * ��ȡһ����Ԫ������ƺ�ָ���ֵ��
	 * 
	 * @param orclCon
	 *            Connection ���ݿ�������
	 * @param reportIn
	 *            ReportIn ��������
	 * @return Map key-��Ԫ������ value-�ֵ
	 * @throws Exception
	 */
	public static Map parsePidCell(Connection orclCon, AfReport reportIn,
			Integer templateType) throws Exception {

		if (orclCon == null || reportIn == null || templateType == null)
			return null;

		Map cellMap = null;
		PreparedStatement pStmt = null;
		ResultSet plRS = null;

		//select ac.cell_pid||'.'||ac.col_num as target,ac.cell_name from af_cellinfo ac where ac.template_id='A1401' 
		try {
			String orclSql = "select b.col_num||'.'||b.cell_pid as target,b.cell_name from af_report a";

			orclSql += "left join af_cellinfo b on "
					+ "a.template_id = b.template_id and a.template_id = b.template_id "
					+ "where a.rep_id = ?";

			pStmt = orclCon.prepareStatement(orclSql.toUpperCase());
			pStmt.setLong(1, reportIn.getRepId());
			plRS = pStmt.executeQuery();

			cellMap = new HashMap();

			while (plRS.next()) {

				String target = plRS.getString("target".toUpperCase());// ��Ԫ������
				String cellName = plRS.getString("cell_name".toUpperCase());// �ֵ

				if (cellName != null && target != null)
					cellMap.put(target, cellName);

			}
		} catch (SQLException e) {
			throw new Exception(e.getMessage());
		} finally {
			if (plRS != null)
				plRS.close();
			if (pStmt != null)
				pStmt.close();
		}
		return cellMap;
	}

	
    /**
     * ��֤������Ƿ�ͨ��������ʽ��֤
     * @author  gongming
     * @date    2007-09-27
     * @cellFormu   String              ������ʽ
     * @cellMap     Map                 ����ݼ���(key-��Ԫ������value��Ԫ��ֵ)
     * @return  boolean ��֤ͨ������true  ��ͨ������false
     */
    public static boolean isRegexRight(String cellFormu,Map cellMap) throws Exception{
    	
        int colLeft = cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
        int colRight = cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
        int regLeft = cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU,colRight + 1);
        int regRight = cellFormu.lastIndexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
        
        String cellName = cellFormu.substring(colLeft + 1, colRight);
        String reg = cellFormu.substring(regLeft + 1, regRight);
        
        if (null == cellMap.get(cellName))
            return false;
        
        String cellValue = (cellMap.get(cellName)).toString();
        
        //�ж�С�����Ƿ񳬹���λ
        int point = cellValue.lastIndexOf(".");
        
        if(cellValue.length() - point > 3) {
        	
            Double temp = null;
            
            try {
                temp = Double.valueOf(cellValue); 
            } catch (NumberFormatException e) {
                return false;
            }
            
            cellValue = String.valueOf(temp.doubleValue() * 100);
            //�ж��Ƿ���С�����ֻ��һλС��
            
            point = cellValue.lastIndexOf(".");
            if(cellValue.length() - point == 2)
                cellValue = cellValue + "0";
            //�ж�С��λ���Ƿ񳬳�
            if(cellValue.length() - point > 3)
                cellValue = cellValue.substring(0,point + 3);
        }
       
        return Pattern.matches(reg, cellValue);
    }
    
    
    /**
     * jdbc���� ������oracle�﷨ ����Ҫ�޸�
     * ���Ը� 2011-12-26
     * ��ȡ�����ϱ�ֵ�͵�Ԫ���������������
     * @param orclCon       Connection      ���ݿ�����
     * @param cellNameLst   List            ��Ԫ�����Ƽ���
     * @param cellMap       Map             �����Map����(key-��Ԫ������,value-�ֵ)
     * @param reportIn      ReportIn        ��������
     * @return  String[][]  
     * @throws Exception
     */
     public static String[][] getFormuValue(Connection orclCon,
			List cellNameLst, AfReport reportIn, Map cellMap, Integer templateType) throws Exception {

		if (cellNameLst == null || cellNameLst.isEmpty())
			return null;
		// ��Ԫ����ʽ����
		String formu[][] = null;

		Statement stmt = null;
		ResultSet orclRS = null;
		boolean flag = false;
//		Integer dataRangeId = null;
//		dataRangeId = reportIn.getDataRangeId();
		
		try {
			StringBuffer orclSql = null;

			int size = cellNameLst.size();
			// ��ʼ������
			formu = new String[size][];
			
			// ��ѯ���ʽ�е�Ԫ����ϱ���ֵ�͵�Ԫ������
			for (int i = 0; i < size; i++) {
				stmt = orclCon.createStatement();
				String cellName = cellNameLst.get(i).toString();
				
				// �ж��Ƿ����ڱ��У��
				if (cellName.indexOf("_") != -1) {
					//���У�鴦��
					orclSql = new StringBuffer();
					String report[] = cellName.split("_");
					
					if (report.length >= 3) {
						
						// �ж��Ƿ���������У��,�Ȼ����Ҫ�Ƚϱ����ID��Ȼ����ݱ���ID��ö�Ӧ��Ԫ�������
						if (!report[0].equals("")) {
							String year = String.valueOf(reportIn.getYear());
							String term = String.valueOf(reportIn.getTerm());
							String day = String.valueOf(reportIn.getDay());
							String repfreqId = "";
							if(report.length>3){
								repfreqId = report[3];
								String date = DateUtil.getDateStr(Integer.valueOf(year),Integer.valueOf(term),Integer.valueOf(day));
								
								if(report.length>4){
									int monthj = -Integer.valueOf(report[4]);
									Calendar calendar = Calendar.getInstance();
									calendar.set(Calendar.YEAR, Integer.valueOf(year));
									calendar.set(Calendar.MONTH, DateUtil.MONTH_ARRAY[Integer.valueOf(term)]);
									calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
									calendar.add(Calendar.MONTH, monthj);

									 SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
										"yyyy-MM-dd");
									 date = DATE_FORMAT.format(calendar.getTime());
								}
								String trueDate = DateUtil.getFreqDateLast(date,Integer.valueOf(repfreqId));
								String[] fenDate = trueDate.split("-");
								year = fenDate[0];
								term = fenDate[1];
								day = fenDate[2];
							}

							orclSql.append("select a.cell_data from ");
							
							//���ݱ��ж�
							if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT))		
								orclSql.append("af_pbocreportdata");
							else
								orclSql.append("af_otherreportdata");
							
							orclSql.append(" a left join af_cellinfo b on a.cell_id = b.cell_id ");
							orclSql.append(" left join af_report c on a.rep_id = c.rep_id ");
							
							/**������ݿ��ж�*/
							if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT)){
								if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle"))
									orclSql.append(" where b.col_num||'.'||b.cell_pid='").append(report[2]).append("' and ");
								if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver"))
									orclSql.append(" where b.col_num+'.'+b.cell_pid='").append(report[2]).append("' and ");
							}
							else
								orclSql.append(" where b.cell_name='").append(report[2]).append("' and ");
							
							orclSql.append(" c.times = (select max(d.times) from af_report d where ");
							orclSql.append(" d.template_id = '").append(report[0]).append("' and ");
							orclSql.append(" d.version_id = '").append(report[1]).append("' and ");
							orclSql.append(" d.org_id ='").append(reportIn.getOrgId()).append("' and ");
							if(!StringUtil.isEmpty(repfreqId)){
								orclSql.append(" d.rep_freq_id =").append(repfreqId).append(" and ");
							}
//							orclSql.append(" d.cur_id ='").append(reportIn.getCurId()).append("' and ");
							orclSql.append(" d.year =").append(year).append(" and ");
							orclSql.append(" d.term =").append(term).append(" and ");
							orclSql.append(" d.day =").append(day);
//							orclSql.append(" d.data_range_id =").append(dataRangeId).append(")");
							orclSql.append(" ) and c.template_id = '").append(report[0]).append("' and ");
							orclSql.append(" c.version_id = '").append(report[1]).append("' and ");
							orclSql.append(" c.org_id ='").append(reportIn.getOrgId()).append("' and ");
							orclSql.append(" c.year =").append(year).append(" and ");
							orclSql.append(" c.term =").append(term).append(" and ");
							orclSql.append(" c.day =").append(day).append(" and ");
							if(!StringUtil.isEmpty(repfreqId)){
								orclSql.append(" c.rep_freq_id =").append(repfreqId).append(" and ");
							}
//							orclSql.append(" c.data_range_id =").append(dataRangeId).append(" and ");
							orclSql.append(" c.CUR_ID =").append(reportIn.getCurId());

						}
						//��ʼ�������ֵ
						String reportValue = "0.00";
						
						orclRS = stmt.executeQuery(orclSql.toString());
						
						if (orclRS.next() && orclRS.getString("cell_data".toUpperCase()) != null) {
							// ��Ԫ���Ӧ���ϱ�ֵ
							reportValue = orclRS.getString("cell_data".toUpperCase());
						}
						orclRS.close();
						stmt.close();
						flag = false;
						formu[i] = new String[] { reportValue.trim(), cellName };
					}
				} else {
					// Ĭ���ϱ�ֵ��ֹ�����Ϊ��
					String reportValue = "0.00";
					if (null != cellMap.get(cellName))
						reportValue = cellMap.get(cellName).toString();
					// formu[i] = new String[]{(cellMap.get(cellName)).toString(),cellName};
					formu[i] = new String[] { reportValue.trim(), cellName };
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
		return formu;
	}
    
     /**
      * jdbc���� ������oracle�﷨ ����Ҫ�޸�
      * ���Ը� 2011-12-26
      * ��֤һ�����������е�Ԫ����ʽ�Ƿ����
      * @param orclCon       Connection      ���ݿ�������
      * @param formuMap      Map             ��Ԫ���Ｏ��
      *                                      -value:ȥ��"[","]"���ź�
      *                                      �������ݿ�Ƚ���ʽ���ַ���
      * @return Map          (key-��Ԫ����ʽ,value�Ƿ������booleanֵ)
      * @throws Exception
      */
     public static Map hasResult(Connection orclCon,Map formuMap) throws Exception{
       
        Map resultMap = null;
        Statement plStmt = null;
        ResultSet plRS = null;
        AfValidateformula cellFormu = null;
        try {
            
            if (formuMap != null && !formuMap.isEmpty()) {
            	
                resultMap = new HashMap();
                Iterator itr = formuMap.keySet().iterator();
                
                while (itr.hasNext()) {
                	
                    cellFormu = (AfValidateformula) itr.next();
                    
                    if (formuMap.get(cellFormu) != null) {
                    	
//                    	if (cellFormu.getFormulaId().equals((long)21))
//                    		System.out.println((formuMap.get(cellFormu)).toString());
                    		
                    	String formu = (formuMap.get(cellFormu)).toString();
                    	//����ĸΪ0 ������Ϊ0��
                    	if(needReplaceStr!=null){
                    		
	                    	String[] needReplaceStrs=needReplaceStr.split(",");
	                    	
	                    	for(int i=0;i<needReplaceStrs.length;i++){
	                    		
	                    		while(formu.indexOf(needReplaceStrs[i])>-1){
	                    			
	                        		formu = formu.substring(0,formu.indexOf(needReplaceStrs[i])) + "*0.00 " 
	                        					+ formu.substring(formu.indexOf(needReplaceStrs[i])
	                        					+ needReplaceStrs[i].length()); 
	                        	}
	                    	}
                    	}
                        	
                        String orclSql = "select count(1) as count1 from CALCULATE_TABLE where "+ formu;                        
                        
                        try {
                        	
                            plStmt = orclCon.createStatement();
                            plRS = plStmt.executeQuery(orclSql);
                            
                            if (plRS.next()) {
                                // �ж��Ƿ��з��ϱ��ʽ�ļ�¼����
                                int rsCount = Integer.parseInt(plRS.getString("count1"));
                                
                                if (rsCount > 0) {
                                    resultMap.put(cellFormu, new Boolean(true));
                                } else {
                                    resultMap.put(cellFormu, new Boolean(false));
                                }
                            }
                            plRS.close();
                            
                            plStmt.close();
                            plRS = null;
                            plStmt= null;
                            
                        } catch (SQLException e) {
                            resultMap.put(cellFormu,new Boolean(false));
                            continue;
                        }
                    }
                }
                formuMap.clear();
            }
        } catch ( Exception e) {            
        	e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            if (plRS != null)
                plRS.close();
            if (plStmt != null)
                plStmt.close();
        }
        return resultMap;
    }
    
	/**
	 * 
	 * @param conn
	 * @param repInId
	 * @param childRepId
	 * @param versionId
	 * @param cellName
	 * @return
	 * @throws Exception
	 */
//	public static String getCellValueString(Connection conn, int repInId,
//			String childRepId, String versionId, String cellName) throws Exception{
//		String value = null;
//
//		if (conn == null || repInId <= 0 || childRepId == null
//				|| versionId == null || cellName == null)
//			return value;
//
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//			String sql = "select rii.report_value from report_in_info rii where rii.rep_in_id=?".toUpperCase()
//					+ " and rii.cell_id=(select mc.cell_id from m_cell mc where mc.child_rep_id=? and ".toUpperCase()
//					+ " mc.version_id=? and mc.cell_name=?)".toUpperCase();
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, repInId);
//			pstmt.setString(2, childRepId);
//			pstmt.setString(3, versionId);
//			pstmt.setString(4, cellName);
//			rs = pstmt.executeQuery();
//			if (rs != null && rs.next()) {
//				value = rs.getString("report_value".toUpperCase());
//				if(value==null)
//					value="";
//			}
//		} catch (SQLException sqle) {
//			throw new Exception(sqle.getMessage());
//		} catch (Exception e) {
//			throw new Exception(e.getMessage());
//		} finally {
//			if (rs != null)
//				rs.close();
//			if (pstmt != null)
//				pstmt.close();
//		}
//		return value;
//	}
    
}
