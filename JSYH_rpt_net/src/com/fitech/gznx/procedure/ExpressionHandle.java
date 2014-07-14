package com.fitech.gznx.procedure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import com.cbrc.smis.excel.NotValidateExpress;
import com.cbrc.smis.proc.impl.Expression;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.proc.impl.ReportImpl;
import com.cbrc.smis.proc.impl.ReportQDRegImpl;
import com.cbrc.smis.proc.impl.S3401Impl;
import com.cbrc.smis.proc.po.MCellFormu;
import com.cbrc.smis.proc.po.ReportIn;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.po.AfValidateformula;
import com.cbrc.smis.common.Config;



public class ExpressionHandle {
	
	/**
	 * 常量
	 */
	private static String STATIC="*double(1.00)";
	/**
	 * 系统错误信息
	 */
	private static String errMsg="";
	/**
	 * 校验未通过的原因
	 */
	private static String cause="";		
	/**
	 * 左边中括号
	 */
	public static String SPLIT_SYMBOL_LEFT_MID_KUOHU="[";    	
	/**
	 * 右边中括号
	 */
	public static String SPLIT_SYMBOL_RIGHT_MID_HUOHU="]";   	
	/**
	 * 等于号
	 */
	public static String SPLIT_SYMBOL_EQUAL="=";
	/**
	 * 反斜线
	 */
	public static String BACKLASH="/";
		
	private static Evaluator evaluator = new Evaluator();

	
    /**
     *  填报数据表校验 
     * @author  gongming
     * @date    2007-09-26
     * @param orclCon       Connection          数据库连接类
     * @param reportIn      ReportIn            内网表单表
     * @param cellFormuLst  List                单元校验表达式集合
     * @param cellMap       Map                 填报数据单元格名称，值键值对集合
     * @return   Map(key-单元格表达式类,value-是否通过校验的boolean值)
     * @throws Exception
     */
    public static Map valid(Connection orclCon,AfReport reportIn,List cellFormuLst,Map cellMap, Integer templateType) throws Exception{
       
        if(orclCon == null || reportIn == null || templateType == null) return null;
        
        Map validMap = null;
        
//        if(Report.REPORT_STYLE_DD == reportIn.getReportStyle().intValue()){
        if(Report.REPORT_STYLE_DD == reportIn.getTemplateStyle()){
            //点对点表达式的校验
            if(cellFormuLst != null && !cellFormuLst.isEmpty() && cellMap != null && !cellMap.isEmpty())
            	
                validMap = ddExpressionValid(orclCon,reportIn,cellFormuLst,cellMap,templateType);
                cellMap.clear();
        }
        return validMap;
    }

    /**
     * 无特殊oracle语法 不需要修改 
     * 卞以刚 2011-12-26
     * 验证一个报表所有单元格表达式的填报数据是否正确
     * @author  gongming
     * @date    2007-09-26
     * @param   conn            Connection      数据库连接类
     * @param   reportIn        ReportIn        内网表单表
     * @param   cellFormuLst    List            单元格表达集合
     * @param   cellMap         Map             填报数据单元格名称，值键值对集合
     * @return  Map(key-单元格表达式类，value-验证是否正确boolean值)
     * @throws Exception
     */
    private static Map ddExpressionValid(Connection orclCon,AfReport reportIn,
    		List cellFormuLst,Map cellMap,Integer templateType) throws Exception{
    	
        if(cellMap == null || cellMap.isEmpty()) return null;
        
        Map validMap = null;
        
        if (cellFormuLst != null && !cellFormuLst.isEmpty()) {
        	
            Iterator itr = cellFormuLst.iterator();
            validMap = new HashMap();
            List notRegLst = new ArrayList();
            
            String dates = reportIn.getYear()+"-"+reportIn.getTerm()+"-"+reportIn.getDay();

            //正则表达验证结果
            boolean valid = false;           
            while (itr.hasNext()) {
                AfValidateformula mCellFormu = (AfValidateformula) itr.next();
                String cellFormu = mCellFormu.getFormulaValue();
                cellFormu = cellFormu.trim();
                
                //如果是正则表达式校验
                if (cellFormu.toLowerCase().indexOf("reg") >= 0) {
                	
                    valid = ValidateP2PReport.isRegexRight(cellFormu,cellMap);
                    validMap.put(mCellFormu, new Boolean(valid));
                    
                } else { 
                	if(cellFormu.indexOf(Config.SPLIT_SYMBOL_CURRMONTH)>-1){
                		cellFormu = cellFormu.replaceAll(Config.SPLIT_SYMBOL_CURRMONTH, String.valueOf(reportIn.getTerm()));
                	}
                    // 获取单元格表达式内的单元格名称的集合
                    List cellNameLst = Expression.getCellNames(cellFormu);
                    
                    // 获取含有上报值和单元格名称数组的数组
                    String[][] formuValue = null;
                    
                    if(cellFormu.toLowerCase().indexOf("acct") >= 0){
                    	/**无特殊oracle语法**/
                    	formuValue = AccountingValid.AccoutingBalanceVal(orclCon, cellMap, cellNameLst, 
                    			dates, reportIn.getOrgId(), reportIn.getRepFreqId().toString(), 
                    			reportIn.getCurId().toString(), templateType);
                    	
                    	//取出acct后半部分的校验公式
                    	cellFormu = cellFormu.split("acct")[1].trim();
                    	
                    }else{
                    	/****
                    	 * jdbc技术 无特殊oracle语法 不需要修改
                    	 * 卞以刚 2011-12-26
                    	 */
                    	formuValue = ValidateP2PReport.getFormuValue(orclCon,
                            cellNameLst, reportIn, cellMap, templateType);
                    }
                    
                    
                    int length = formuValue.length;
                    
                    for (int i = 0; i < length; i++) {
                        String value = formuValue[i][0];
                        
                        if (value.indexOf("-") == 0)
                            value = "(" + value + ")";
                        
                        cellFormu = cellFormu.replaceAll("\\Q["+formuValue[i][1]+"]\\E", value);
                    }
                    // 如果公式中存在if语句
                    if(cellFormu.indexOf(Config.SPLIT_SYMBOL_IF)>-1){
                    	int nstartdex = cellFormu.indexOf(Config.SPLIT_SYMBOL_IF);
                    	int nenddex = cellFormu.indexOf(Config.SPLIT_SYMBOL_RIGHT_SMALL_HUOHU);
                    	String sIfFromla = cellFormu.substring(nstartdex,nenddex+1);

                    	String[] formulaif = sIfFromla.substring(3, nenddex-nstartdex).split(Config.SPLIT_SYMBOL_COMMA);
                    	if(formulaif != null && formulaif.length>2){                    		
                    		String formulavalue = "0.00";
                    		try {
                    			String panduanStr = formulaif[0];
                    			if(panduanStr.indexOf(Config.SPLIT_SYMBOL_EQUAL)>-1 && panduanStr.indexOf("==")==-1
                    					&& panduanStr.indexOf(">=")==-1 && panduanStr.indexOf("<=")==-1){
                    				panduanStr = panduanStr.replace(Config.SPLIT_SYMBOL_EQUAL, "==");
                    			}
                    			if(evaluator.evaluate(panduanStr).equals("1.0")){
                    				formulavalue = formulaif[1];
                    			} else {
                    				formulavalue = formulaif[2];
                    			}
                    		} catch (EvaluationException e) {
                    			e.printStackTrace();
                    		}
                    		
                    		cellFormu = cellFormu.replace(sIfFromla, formulavalue);
                    	}
                    }
                    
                    mCellFormu.setFormulaValue(cellFormu);
                    notRegLst.add(mCellFormu);
                }
            }
            /***
             * jdbc技术 无特殊oracle语法 不需要修改
             * 卞以刚 2011-12-26
             */
            Map formuMap = ValidateP2PReport.hasResult(orclCon,splitFormu(orclCon,notRegLst));
            if(formuMap != null) 
                validMap.putAll(formuMap);   
        }
        return validMap;
    }
    
	/**
	 * 
	 * 将脱去"[","]"符号的单元格表达式，重新组合成符合数据库比较形式的字符串
	 *   以Map集合形式返回
	 * @author   gongming
	 * @date     2007-09-26
	 * @param mCellFormuLst      List      单元格表达式集合
	 * @return   Map         (key-单元格表达式，value-拆分成左右运算式和比较符的数组)
	 * @throws Exception
	 */
	public static Map splitFormu(Connection orclCon,List mCellFormuLst)throws Exception{
		Map mCellFormuMap = null;
		// 比较符号数组
		String[] compareSign = { ">=", "<=", ">", "<", "=" };
		int length = compareSign.length;
        
		if (mCellFormuLst != null || !mCellFormuLst.isEmpty()) {
            mCellFormuMap = new HashMap();
            Iterator itr = mCellFormuLst.iterator();
            
            while (itr.hasNext()) {                
            	//单元格表达式类
                AfValidateformula cellFormu = (AfValidateformula)itr.next();
                //单元格计算公式
                String formu = cellFormu.getFormulaValue();
                for (int i = 0; i < length; i++) {
                    //比较符号
                    String sign = compareSign[i];              
                    String[] temp = formu.split(sign);
                    String tempFormu = null;
                    // 如果拆分成两个元素的数组
                    if (temp != null) {
	                    int _length = temp.length;
	                    /**
	                     * 小数位数问题：当0.61=16500000000.12/28340000000.14  小数位数若设为4就校验不通过
	                     * A1>=A2+A3
	                     * A2+A3<=A1<=A4+A5
	                     * 
	                     * 所以根据小数位为标准来定小数位数
	                     */
	                    int deci=4;
	                    
	                    for(int j = 0 ; j<_length ; j++){
	                    	if(temp[j].indexOf("/")>=0){
	                    		temp[j] = isZERO(orclCon,temp[j]);
	                    	}
	                    }
	                    
	                    // a(>=,>,<=,<,<)形式
	                    if(_length == 2){ 
	                    	if(temp[0]!=null){
	                    		deci=temp[0].length()-temp[0].indexOf(".")-1;
	                    		if(deci<0||deci>6){
	                    			deci=4;
	                    		}
	                    	}
	                    	tempFormu = "round((" +temp[0] +"),"+deci+")";
	                    	tempFormu += sign + "round((" + temp[1] +"),"+deci+")";
	                    }
	                   
	                    // a(>=,>,<=,<)b(>=,>,<=,<)c 形式     
	                    if(_length == 3){
	                    	if(temp[0]!=null){
	                    		deci=temp[0].length()-temp[0].indexOf(".")-1;
	                           	if(deci<0||deci>6){
	                           		deci=4;
	                           	}
	                    	}
	                    	tempFormu = "round(( " +temp[1] +"),"+deci+") between ";
	                        
	                    	if(">=".equals(sign)||">".equals(sign)){                               
	                    		tempFormu += "round((" + temp[2] + "),"+deci+") and ";
	                    		tempFormu += "round((" + temp[0] + "),"+deci+")";
	                    	}else{                       
	                    		tempFormu += "round((" + temp[0] + "),"+deci+") and ";
	                    		tempFormu += "round((" + temp[2] + "),"+deci+")";
	                    	}
	                    }
	                    
	                    // a(>=,>,<=,<)b(>=,>,<=,<)c(>=,>,<=,<)d(>=,>,<=,<)... 形式 
	                    if(_length > 3){
	                    	if(temp[0]!=null){
	                    		deci=temp[0].length()-temp[0].indexOf(".")-1;
	                    		if(deci<0||deci>6){
	                    			deci=4;
	                           	}
	                    	}
		                    	
	                    	tempFormu = "";
	                    	for(int j=0;j<_length-1;j++){
	                    		if(j != 0) tempFormu += " and ";
	                    		
	                    		tempFormu += "round((" + temp[j] + "),"+deci+")";
	                    		tempFormu += sign + "round((" + temp[j+1] + "),"+deci+")";
	                    	}	                    	
	                    }
		                    
	                    /**将解释后的表达式放入MAP*/
	                    if(_length >=2){
	                    	cellFormu.setSource(temp[0]);
	                    	cellFormu.setTarget(temp[1]);
	                    	mCellFormuMap.put(cellFormu,tempFormu);
	                    	break;
	                    }
                    }
                }
            }
        }
        return mCellFormuMap;
	}

	/**
	 * 判断是否是零
	 * 
	 * @param orclCon
	 * @param temp
	 * @return
	 * @throws SQLException
	 */
	private static  String isZERO(Connection orclCon,String temp) throws SQLException{
		Statement plStmt = null;
		ResultSet plRS = null;
		String formu = temp;
		try {
			//表达式是否成立的统计查询语句
			String orclSql = "select count(1) as count1 from REPORT_IN where " + formu.toUpperCase() +" = " + temp;
			try {
				plStmt = orclCon.createStatement();
				plRS = plStmt.executeQuery(orclSql);
				
				if (plRS.next()) {
					// 判断是否有符合表达式的记录存在
					int rsCount = Integer.parseInt(plRS.getString("count1"));
					if (rsCount > 0) {
						return temp;
					}
				}
				plRS.close();
				plStmt.close();
			} catch (SQLException e) {
				if(e.getErrorCode() == -801){
					formu = "0";
				}
			}                        
		} catch ( Exception e) {            
			e.printStackTrace();            
		} finally {
			if (plRS != null) plRS.close();
			if (plStmt != null) plStmt.close();
		}
		return formu;
	}
	
    /**
     * 获取单元格计算公式中的单元格名称的集合
     * @author  gongming
     * @date    2007-09-25
     * @param   cellFormu     String      单元格计算公式
     * @return  List
     */
	public static List getCellNames(String cellFormu){	    
        List cellNameLst = null;
        try{
            //"["索引
            int posS=cellFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU);
            //"]"索引
            int posE=cellFormu.indexOf(SPLIT_SYMBOL_RIGHT_MID_HUOHU);
            cellNameLst = new ArrayList();
            while(posS>=0 && posE>=0){
                String cellName = cellFormu.substring(posS+1,posE);
                if(cellName != null)
                    cellNameLst.add(cellName);
                cellFormu = cellFormu.substring(posE + 1);
                posS=cellFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU);
                posE=cellFormu.indexOf(SPLIT_SYMBOL_RIGHT_MID_HUOHU);
            }
        }catch(Exception e){
            e.printStackTrace();
        }    
        return cellNameLst;
    }
}
