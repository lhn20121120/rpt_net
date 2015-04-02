package com.fitech.gznx.procedure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.hibernate.Session;

import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.proc.impl.Expression;
import com.fitech.gznx.common.Config;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfTemplate;
import com.fitech.gznx.po.AfTemplateValiSche;
import com.fitech.gznx.po.AfValidateformula;
import com.fitech.gznx.service.AfTemplateValiScheDelegate;

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
			 AfTemplateValiSche valiSche = AfTemplateValiScheDelegate.findAfTemplateValiSche(
             		reportIn.getTemplateId(),reportIn.getVersionId());
			
			String orclSql = "";

			// �ж�ģ������
			if (templateType.toString().equals(Config.PBOC_REPORT)){
				if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle") || com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("db2"))
					orclSql += "select b.col_num||'.'||b.cell_pid as cell_name,a.cell_data from af_pbocreportdata";
				if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver"))
					orclSql += "select b.col_num+'.'+b.cell_pid as cell_name,a.cell_data from af_pbocreportdata";
			}
//			else if (templateType.toString().equals(Config.OTHER_REPORT))
			else if(templateType.toString().equals(Config.OTHER_REPORT)){
				if(valiSche!=null && new Integer(2).equals(valiSche.getValidateSchemeId())){
					if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle") || com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("db2"))
						orclSql += "select b.col_num||'.'||b.cell_pid as cell_name,a.cell_data from af_otherreportdata";
					if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("sqlserver"))
						orclSql += "select b.col_num+'.'+b.cell_pid as cell_name,a.cell_data from af_otherreportdata";
				}else{
					orclSql += "select b.cell_name,a.cell_data from af_otherreportdata";
				}
			}

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
		boolean yearBeginFlag = false; // �Ƿ��ȡ�����ת��־
		boolean templateYearBeginFlag = isExistsYearBeginConver(reportIn.getTemplateId(),reportIn.getVersionId(),9);//��ȡ�ñ����Ƿ���������ת��־
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
									if(reportIn.getRepFreqId().intValue()!=9){
										if(repfreqId.equals("2"))
											monthj = monthj * 3;
										if(repfreqId.equals("3"))
											monthj = monthj * 6;
										if(repfreqId.equals("4"))
											monthj = monthj * 12;
									}
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
								if(templateType==2 && report.length>4 && reportIn.getRepFreqId().intValue()!=9 && templateYearBeginFlag){
									int currentTerm = reportIn.getTerm().intValue();
									if((repfreqId.equals("1") && currentTerm==1) ||
									   (repfreqId.equals("2") && currentTerm==3) || 
									   (repfreqId.equals("3") && currentTerm==6) || 
									   (repfreqId.equals("4") && currentTerm==12)){
										year = "" + (Integer.parseInt(fenDate[0])+1);
										term = "01";
										day = "01";
										yearBeginFlag = true;
									}
								}
								if(report.length>4){
									//�жϵ�ǰ�����ִ�������Ƿ���ģ�����Ч�����ڣ�������ڣ��ͽ���ǰģ��汾�滻���ϸ��汾
									if(!isValid(report[0],report[1], year+"-"+term+"-"+day)){
										String validVersion = getLastTemplateVersion(report[0],report[1]);
										if(!StringUtil.isEmpty(validVersion)){
											report[1] = validVersion ; 
										}
									}
								}
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
								if(com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("oracle") || com.cbrc.smis.common.Config.DB_SERVER_TYPE.equals("db2"))
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
								if(!yearBeginFlag)
									orclSql.append(" d.rep_freq_id =").append(repfreqId).append(" and ");
								else
									orclSql.append(" d.rep_freq_id =9 and ");
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
								if(!yearBeginFlag)
									orclSql.append(" c.rep_freq_id =").append(repfreqId).append(" and ");
								else
									orclSql.append(" c.rep_freq_id =9 and ");
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
      * �Ƿ���������ת
      * @param templateId
      * @param versionId
      * @param repFreqId
      * @return
      */
    public static boolean isExistsYearBeginConver(String templateId,String versionId,Integer repFreqId){
    	boolean result = false;
		DBConn conn=null;	   
		Session session=null;
		try{
			conn =new DBConn();
			session = conn.openSession();
			String hsql = "select count(*) from AfTemplateFreqRelation a where a.id.templateId='" + templateId + "' and a.id.versionId='" + versionId + "' and a.id.repFreqId=" + repFreqId; 
			Integer o = (Integer)session.find(hsql).get(0);
			if(o.intValue()>0)
				result = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(session!=null)
				conn.closeSession();
		}
    	return result;
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
     * ��ѯ���������ʱ���Ƿ��� ģ�����Ч������
     * @param templateId
     * @param versionId
     * @param term
     * @return
     */
     public static boolean isValid(String templateId,String versionId,String term){
    	 boolean result = false;
 		DBConn conn=null;	   
 		Session session=null;
 		if(StringUtil.isEmpty(templateId) || StringUtil.isEmpty(versionId)
 				|| StringUtil.isEmpty(term))
 			return result;
 		try{
 			conn =new DBConn();
 			session = conn.openSession();
 			String hql = "from AfTemplate t where t.id.templateId='"+templateId+"' and t.id.versionId='"+versionId+"'";
 			AfTemplate t = (AfTemplate)session.createQuery(hql).uniqueResult();
 			if(t!=null && !StringUtil.isEmpty(t.getStartDate())
 					&& !StringUtil.isEmpty(t.getEndDate())){
 				Date startDate = DateUtil.DATE_FORMAT.parse(t.getStartDate());
 				Date endDate = DateUtil.DATE_FORMAT.parse(t.getEndDate());
 				Date curDate = DateUtil.DATE_FORMAT.parse(term);
 				long startTime = startDate.getTime();
 				long endTime = endDate.getTime();
 				long curTime = curDate.getTime();
 				if(curTime >= startTime && curTime <= endTime){
 					result = true ; 
 				}
 			}
 		}catch(Exception e){
 			e.printStackTrace();
 		}finally{
 			if(session!=null)
 				conn.closeSession();
 		}
 		return result;
     }
     
     /**
      *  ���ݵ�ǰģ��İ汾���ҵ��ϸ��汾
      * @param templateId
      * @param versionId
      * @return
      */
      public static String getLastTemplateVersion(String templateId,String versionId){
     	String version = null;
  		DBConn conn=null;	   
  		Session session=null;
  		if(StringUtil.isEmpty(templateId) || StringUtil.isEmpty(versionId))
  			return version;
  		try{
  			conn =new DBConn();
  			session = conn.openSession();
  			String hql = "select max(t.id.versionId) from AfTemplate t where t.id.templateId ='"+templateId+"' and t.id.versionId !='"+versionId+"'";
  			version = (String)session.createQuery(hql).uniqueResult();
  		}catch(Exception e){
  			e.printStackTrace();
  		}finally{
  			if(session!=null)
  				conn.closeSession();
  		}
  		return version;
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
