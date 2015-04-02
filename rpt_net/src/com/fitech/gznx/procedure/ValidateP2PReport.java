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
	 * 需要替换为*0的字符串
	 */
	public static String needReplaceStr="/0.00,/(0.00+0.00*12.5)";
	
	/**
	 * jdbc技术 无特殊语法 不需要修改
	 * 卞以刚 2011-12-26
	 * 影响表：af_pbocreportdata || af_otherreportdata
	 * 
	 * 获取一个上报报表所有单元格的名称和上报值键值对
	 * 
	 * @param orclCon
	 *            Connection 数据库连接类
	 * @param reportIn
	 *            ReportIn 内网表单表
	 * @return Map key-单元格名称 value-填报值
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

			// 判断模板类型
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

				String cellName = plRS.getString("cell_name".toUpperCase());// 单元格名称
				String reportValue = plRS.getString("cell_data".toUpperCase());// 填报值

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
	 * 获取一个上报报表所有单元格的名称和上报值键值对(人行为指标键值对)
	 * 
	 * @param orclCon
	 *            Connection 数据库连接类
	 * @param reportIn
	 *            ReportIn 内网表单表
	 * @return Map key-单元格名称 value-填报值
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

			// 判断模板类型
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

				String cellName = plRS.getString("cell_name".toUpperCase());// 单元格名称
				String reportValue = plRS.getString("cell_data".toUpperCase());// 填报值

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
	 * 获取一个单元格的名称和指标键值对
	 * 
	 * @param orclCon
	 *            Connection 数据库连接类
	 * @param reportIn
	 *            ReportIn 内网表单表
	 * @return Map key-单元格名称 value-填报值
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

				String target = plRS.getString("target".toUpperCase());// 单元格名称
				String cellName = plRS.getString("cell_name".toUpperCase());// 填报值

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
     * 验证填报数据是否通过正则表达式验证
     * @author  gongming
     * @date    2007-09-27
     * @cellFormu   String              正则表达式
     * @cellMap     Map                 填报数据集合(key-单元格名称value单元格值)
     * @return  boolean 验证通过返回true  不通过返回false
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
        
        //判断小数点是否超过两位
        int point = cellValue.lastIndexOf(".");
        
        if(cellValue.length() - point > 3) {
        	
            Double temp = null;
            
            try {
                temp = Double.valueOf(cellValue); 
            } catch (NumberFormatException e) {
                return false;
            }
            
            cellValue = String.valueOf(temp.doubleValue() * 100);
            //判断是否是小数点后只有一位小数
            
            point = cellValue.lastIndexOf(".");
            if(cellValue.length() - point == 2)
                cellValue = cellValue + "0";
            //判断小数位数是否超长
            if(cellValue.length() - point > 3)
                cellValue = cellValue.substring(0,point + 3);
        }
       
        return Pattern.matches(reg, cellValue);
    }
    
    
    /**
     * jdbc技术 无特殊oracle语法 不需要修改
     * 卞以刚 2011-12-26
     * 获取含有上报值和单元格名称数组的数组
     * @param orclCon       Connection      数据库联接
     * @param cellNameLst   List            单元格名称集合
     * @param cellMap       Map             填报数据Map集合(key-单元格名称,value-填报值)
     * @param reportIn      ReportIn        内网表单表
     * @return  String[][]  
     * @throws Exception
     */
     public static String[][] getFormuValue(Connection orclCon,
			List cellNameLst, AfReport reportIn, Map cellMap, Integer templateType) throws Exception {

		if (cellNameLst == null || cellNameLst.isEmpty())
			return null;
		// 单元格表达式数组
		String formu[][] = null;

		Statement stmt = null;
		ResultSet orclRS = null;
		boolean flag = false;
		boolean yearBeginFlag = false; // 是否获取年初结转标志
		boolean templateYearBeginFlag = isExistsYearBeginConver(reportIn.getTemplateId(),reportIn.getVersionId(),9);//获取该报表是否具有年初结转标志
//		Integer dataRangeId = null;
//		dataRangeId = reportIn.getDataRangeId();
		
		try {
			StringBuffer orclSql = null;

			int size = cellNameLst.size();
			// 初始化数组
			formu = new String[size][];
			
			// 查询表达式中单元格的上报报值和单元格名称
			for (int i = 0; i < size; i++) {
				stmt = orclCon.createStatement();
				String cellName = cellNameLst.get(i).toString();
				
				// 判断是否属于表间校验
				if (cellName.indexOf("_") != -1) {
					//表间校验处理
					orclSql = new StringBuffer();
					String report[] = cellName.split("_");
					
					if (report.length >= 3) {
						
						// 判断是否是连续性校验,先获得需要比较报表的ID，然后根据报表ID获得对应单元格的数据
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
									//判断当前报表的执行期数是否在模板的生效日期内，如果不在，就将当前模板版本替换成上个版本
									if(!isValid(report[0],report[1], year+"-"+term+"-"+day)){
										String validVersion = getLastTemplateVersion(report[0],report[1]);
										if(!StringUtil.isEmpty(validVersion)){
											report[1] = validVersion ; 
										}
									}
								}
							}

							orclSql.append("select a.cell_data from ");
							
							//数据表判断
							if(templateType.toString().equals(com.fitech.gznx.common.Config.PBOC_REPORT))		
								orclSql.append("af_pbocreportdata");
							else
								orclSql.append("af_otherreportdata");
							
							orclSql.append(" a left join af_cellinfo b on a.cell_id = b.cell_id ");
							orclSql.append(" left join af_report c on a.rep_id = c.rep_id ");
							
							/**添加数据库判断*/
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
						//初始化查出的值
						String reportValue = "0.00";
						
						orclRS = stmt.executeQuery(orclSql.toString());
						
						if (orclRS.next() && orclRS.getString("cell_data".toUpperCase()) != null) {
							// 单元格对应的上报值
							reportValue = orclRS.getString("cell_data".toUpperCase());
						}
						orclRS.close();
						stmt.close();
						flag = false;
						formu[i] = new String[] { reportValue.trim(), cellName };
					}
				} else {
					// 默认上报值防止填报数据为空
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
      * 是否存在年初结转
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
      * jdbc技术 无特殊oracle语法 不需要修改
      * 卞以刚 2011-12-26
      * 验证一个填报报表的所有单元格表达式是否成立
      * @param orclCon       Connection      数据库连接类
      * @param formuMap      Map             单元格表达集合
      *                                      -value:去除"[","]"符号后
      *                                      符合数据库比较形式的字符串
      * @return Map          (key-单元格表达式,value是否成立的boolean值)
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
                    	//将分母为0 的数改为0；
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
                                // 判断是否有符合表达式的记录存在
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
     * 查询传入的期数时间是否在 模板的生效日期内
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
      *  根据当前模板的版本，找到上个版本
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
