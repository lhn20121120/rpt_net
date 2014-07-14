package com.cbrc.smis.proc.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.cbrc.smis.excel.NotValidateExpress;
import com.cbrc.smis.proc.po.MCellFormu;
import com.cbrc.smis.proc.po.ReportIn;
import com.fitech.gznx.procedure.AccountingValid;
import com.fitech.gznx.procedure.ValidateP2PReport;
/**
 * 表达式处理操作类
 * 
 * @author rds
 * @serialData 2005-12-20 19:51
 */
public class Expression {
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
	
	private static Map<String, String> forMap = new HashMap<String, String>();
	
	static{
		forMap.put(">", "$");
		forMap.put(">=", "$!");
		forMap.put("=", "!");
		forMap.put("<", "&");
		forMap.put(">=", "&!");
	}
	/**
	 * 表间校验
	 * 
	 * @param conn Connection 数据库连接
	 * @param reportIn ReportIn 实际数据报表对象
	 * @param cellFormu String 表达式
	 * @return boolean 校验通过，返回true;否则，返回false
	 * @exception Exception
	 */
	public static boolean bjValidate(Connection conn,ReportIn reportIn,String cellFormu,Integer pointNumber)
		throws Exception{
		boolean resVal=false;
		
		if(conn==null || reportIn==null || reportIn.getRepInId()==null || reportIn.getChildRepId()==null ||
				reportIn.getVersionId()==null || reportIn.getDataRangeId()==null || reportIn.getOrgId()==null ||
				reportIn.getYear()==null || reportIn.getTerm()==null || reportIn.getReportStyle()==null 
				|| cellFormu==null) 
			return resVal;
		
		errMsg="";
		cause="";
		
		if(reportIn.getReportStyle().intValue()==Report.REPORT_STYLE_DD){     //点对点表达式的校验   
			resVal=ddExpressionValidate(conn,reportIn,cellFormu,pointNumber);
		}else{   //清单式表达式的校验
			resVal=dqExpressionValidate(conn,reportIn,cellFormu,pointNumber);
		}
			
		return resVal;
	}
	
	/**
	 * 表内校验
	 * 
	 * @param conn Connection 数据库连接
	 * @param reportIn ReportIn 实际数据报表对象
	 * @param cellFormu String 表达式
	 * @return boolean 校验通过，返回true;否则，返回false
	 * @exception Exception
	 */
	public static boolean bnValidate(Connection conn,ReportIn reportIn,String cellFormu,Integer PointNumber) throws Exception{
		boolean resVal=false;
		
		if(conn==null || reportIn==null || reportIn.getRepInId()==null || reportIn.getChildRepId()==null ||
				reportIn.getVersionId()==null || reportIn.getDataRangeId()==null || reportIn.getOrgId()==null ||
				reportIn.getYear()==null || reportIn.getTerm()==null || reportIn.getReportStyle()==null 
				|| cellFormu==null) 
			return resVal;
		
		errMsg="";
		cause="";
		
		if(reportIn.getReportStyle().intValue()==Report.REPORT_STYLE_DD){  
			//点对点表达式的校验
			resVal=ddExpressionValidate(conn,reportIn,cellFormu,PointNumber);
		}else{   
			//清单式表达式的校验
			resVal=dqExpressionValidate(conn,reportIn,cellFormu,PointNumber);
		}
		
		return resVal;
	}
	
	/**
     *  填报数据表间校验 --不使用
     * @author  gongming
     * @date    2007-09-25
     * @param orclCon       Connection          数据库连接类
     * @param reportIn      ReportIn            内网表单表
     * @param mCellFormu    MCellForm           单元校验表达式
     * @param cellMap       Map                 填报数据单元格名称，值键值对集合
     * @return   检验通过返回true不通过返回false
     * @throws Exception
	 */
    public static boolean bnValid(Connection orclCon,ReportIn reportIn,MCellFormu mCellFormu,Map cellMap) throws Exception{
        boolean isValid = false;
        if(orclCon == null || reportIn == null 
                || mCellFormu == null || cellMap == null || cellMap.isEmpty())
            return false;
        
        if(Report.REPORT_STYLE_DD == reportIn.getReportStyle().intValue()){  
            //点对点表达式的校验
            if(null != mCellFormu.getCellFormu() && null != mCellFormu.getPointNumber()){
               // isValid =ddExpressionValid(orclCon,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber(),cellMap);
            }
        }else{  
            //清单式表达式的校验
            if(null != mCellFormu.getCellFormu())
                isValid =dqExpressionValidate(orclCon,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber());
           
        }        
        return isValid;
    }
    
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
    public static Map valid(Connection orclCon,ReportIn reportIn,List cellFormuLst,Map cellMap) throws Exception{
       
        if(orclCon == null || reportIn == null) return null;
        Map validMap = null;
        if(Report.REPORT_STYLE_DD == reportIn.getReportStyle().intValue()){  
            //点对点表达式的校验
            if(cellFormuLst != null && !cellFormuLst.isEmpty()
                    && cellMap != null && !cellMap.isEmpty())
                validMap =ddExpressionValid(orclCon,reportIn,cellFormuLst,cellMap);
                cellMap.clear();
        }else{  
            //清单式表达式的校验
//            if(null != mCellFormu.getCellFormu())
//                isValid =dqExpressionValidate(orclCon,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber());
           
        }        
        return validMap;
    }
    
	/**
	 * 点对点式报表的表达式的校验
	 * 
	 * @param conn Connection 数据库连接
	 * @param reportIn ReportIn 实际数据报表对象
	 * @param cellFormu String 表达式
	 * @return boolean 校验通过，返回true;否则，返回false
	 * @exception Exception
	 */
	private static boolean ddExpressionValidate(Connection conn,ReportIn reportIn,String cellFormu,Integer pointNumber)
		throws Exception{
		boolean result=false;

		if(conn==null || reportIn==null || reportIn.getRepInId()==null || reportIn.getChildRepId()==null ||
				reportIn.getVersionId()==null || reportIn.getDataRangeId()==null || reportIn.getOrgId()==null ||
				reportIn.getYear()==null || reportIn.getTerm()==null || reportIn.getReportStyle()==null 
				|| cellFormu==null) 
			return false;
		
		int posS=0,posE=0;
		
		cellFormu=cellFormu.trim();
		if(cellFormu.toLowerCase().indexOf("reg")>=0)
			return ReportDDImpl.isRegexRight(conn,reportIn.getRepInId().intValue(),cellFormu,reportIn.getChildRepId(),reportIn.getVersionId());
		
		posS=cellFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU);
		posE=cellFormu.indexOf(SPLIT_SYMBOL_RIGHT_MID_HUOHU);
		
		String cellName=null;
		String cellValue="0";
		int dataRangeId=0;
		while(posS>=0 && posE>=0){
			cellName=cellFormu.substring(posS,posE+1);
		
			if(cellName!=null && cellName.length()>2){
				if(cellName.indexOf("_")>0){  //表间校验
					String arr[]=cellName.substring(1,cellName.length()-1).split("_");
					if(arr.length>=3){
						if(arr.length>3){
							try{
								if(arr.length==4 && !arr[3].startsWith("(")) dataRangeId=Integer.parseInt(arr[3]);
								if(arr.length==5) dataRangeId=Integer.parseInt(arr[4]);
							}catch(Exception e){
								dataRangeId=0;
							}
						}else{
							dataRangeId=reportIn.getDataRangeId().intValue();
						}
						cellValue=ReportImpl.getCellValue(conn,
								reportIn.getRepInId().intValue(),
								arr[0],
								arr[1],
								dataRangeId,
								reportIn.getOrgId(),
								reportIn.getCurId().intValue(),
								reportIn.getYear().intValue(),
								reportIn.getTerm().intValue(),
								arr[2],
								Report.FORMU_TYPE_BJ);
					}
				}else{  //表内校验
					cellValue=ReportImpl.getCellValue(conn,reportIn.getRepInId().intValue(),
							reportIn.getChildRepId(),
							reportIn.getVersionId(),
							cellName.substring(1,cellName.length()-1));
				}
				
			    cellValue=format(cellValue);
				
				cellFormu=cellFormu.substring(0,posS) + 
					(cellValue.indexOf("-")==0?"(" + String.valueOf(cellValue) + ")":String.valueOf(cellValue)) + 
					cellFormu.substring(posE+1);
				
				posS=cellFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
				posE=cellFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
			}
		}
		result=isEquals(conn,cellFormu,pointNumber);
		
		return result;
	}
	
    /**
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
    private static Map ddExpressionValid(Connection orclCon,ReportIn reportIn,List cellFormuLst,Map cellMap) throws Exception{
        if(cellMap == null || cellMap.isEmpty()) return null;
        Map validMap = null;
        if (cellFormuLst != null && !cellFormuLst.isEmpty()) {
            Iterator itr = cellFormuLst.iterator();
            validMap = new HashMap();
            List notRegLst = new ArrayList();
            
            String dates = reportIn.getYear() + "-" + reportIn.getTerm() + "-"
            	+ com.fitech.gznx.common.DateUtil.getMonthLastDay(reportIn.getYear().intValue(), reportIn.getTerm().intValue());
            
            //正则表达验证结果
            boolean valid = false;           
            while (itr.hasNext()) {
                MCellFormu mCellFormu = (MCellFormu) itr.next();
                String cellFormu = mCellFormu.getCellFormu();
                cellFormu = cellFormu.trim();
                
                /**将个别机构无需校验的公式过滤*/
                NotValidateExpress notValidate = NotValidateExpress.newInstance();
//                String expresses = null;
//                try{
//                	expresses = notValidate.getNotValidateExpresses(reportIn.getOrgId()+"_"+reportIn.getChildRepId()+"_"+reportIn.getDataRangeId());
//                }catch(Exception ex){
//                	expresses = null;
//                }
                //2013-12-12:LuYueFei--由于G3302需要过滤特殊的公式注释上述部分，修改为下面的无需校验公式的处理方式
                String key1=new StringBuffer().append(reportIn.getOrgId()).append("_").append(reportIn.getChildRepId()).append("_").append(reportIn.getDataRangeId()).toString();
                String key2=new StringBuffer().append(reportIn.getChildRepId()).append("_").append(reportIn.getDataRangeId()).append("_").append(reportIn.getCurId()).toString();
                String expresses = notValidate.getNotValidateExpressesFromMap(key1);//得到（机构ID+报表ID）对应的值
                expresses=(expresses==null)?notValidate.getNotValidateExpressesFromMap(key2):expresses;//得到(报表ID+币种ID)对应的值
                if(expresses != null && expresses.indexOf(cellFormu) > -1) continue;
                
                //如果是正则表达式校验
                if (cellFormu.toLowerCase().indexOf("reg") >= 0) {
//                	if (cellFormu.indexOf("F57")!=-1){
//                		System.out.println(99);
//                	}
                    valid = ReportDDImpl.isRegexRight(mCellFormu,cellFormu,cellMap);
                    validMap.put(mCellFormu, new Boolean(valid));
                } else {            
                	
                    // 获取单元格表达式内的单元格名称的集合
                    List cellNameLst = Expression.getCellNames(cellFormu);
                    // 获取含有上报值和单元格名称数组的数组

                    // 获取含有上报值和单元格名称数组的数组
                    String[][] formuValue = null;
                    
                    if(cellFormu.toLowerCase().indexOf("acct") >= 0){
                    	
                    	formuValue = AccountingValid.AccoutingBalanceVal(orclCon, cellMap, cellNameLst, 
                    			dates, reportIn.getOrgId(), reportIn.getRepFreqId().toString(), 
                    			reportIn.getCurId().toString(), null);
                    	
                    	//取出acct后半部分的校验公式
                    	cellFormu = cellFormu.split("acct")[1].trim();
                    	
                    }else{
                    	/**
                    	 * 获取含有上报值和单元格名称数组
                    	 */
                        formuValue = ReportDDImpl.getFormuValue(orclCon,
                                cellNameLst, reportIn, cellMap);
                    }
                    
                    
                    int length = formuValue.length;
                    for (int i = 0; i < length; i++) {                            
                        String value = formuValue[i][0];
                        if (value.indexOf("-") == 0)
                            value = "(" + value + ")";
                        cellFormu = cellFormu.replaceAll("\\Q["+formuValue[i][1]+"]\\E", value);
                    }
                    /***
                     * 校验公式，将单元格替换为上报值
                     * 0.00-2<=0.00+0.00<=0.00+2
                     */
                    mCellFormu.setCellFormu(cellFormu);
                    notRegLst.add(mCellFormu);
                }
            }
            Map formuMap = ReportDDImpl.hasResult(orclCon,splitFormu(orclCon,notRegLst));
            if(formuMap != null) 
                validMap.putAll(formuMap);   
        }
        return validMap;
    }

	/**
	 * 清单式报表的表达式的校验
	 * 
	 * @param conn Connection 数据库连接
	 * @param reportIn ReportIn 实际数据报表对象
	 * @param colFormu String 列公式
	 * @return boolean 校验正确，返回true;否则，返回false
	 * @exception Exception
	 */
	private static boolean dqExpressionValidate(Connection conn,ReportIn reportIn,String colFormu,Integer pointNumber) throws Exception{
		boolean resVal=true;

		if(conn==null || reportIn==null || reportIn.getRepInId()==null ||
				reportIn.getChildRepId()==null || reportIn.getVersionId()==null
				|| colFormu==null)
			return false;
		
		/**清单式报表的实际数据表名**/
		String table=ReportImpl.findTableName(conn,reportIn.getChildRepId(),reportIn.getVersionId());
		if(table==null) {
			errMsg="获得清单式报表[编号:" + reportIn.getChildRepId() + 
				";版本号:" + reportIn.getVersionId() +
				"]的实际数据表失败，操作终止!";
			return false;
		}
		if(colFormu.toLowerCase().indexOf("reg")>=0){
			return ReportQDRegImpl.isRegexRight(conn,reportIn,colFormu,table);
		}
		
		if(colFormu.indexOf("∑")>=0){
			return ReportQDRegImpl.isSumRight(conn,reportIn,colFormu,table,pointNumber);
		}
		
		String[] arr=null;
		String operator=null;
		colFormu=colFormu.trim();
		
		if(arr==null || arr.length<=1){arr=colFormu.split(">=");operator=">=";}
		if(arr==null || arr.length<=1){arr=colFormu.split("<=");operator="<=";}
		if(arr==null || arr.length<=1){arr=colFormu.split(">");operator=">";}
		if(arr==null || arr.length<=1){arr=colFormu.split("<");operator="<";}
		if(arr==null || arr.length<=1){arr=colFormu.split("=");operator="=";}
		
		if(arr==null || arr.length<=0) {errMsg="无法解析关系表达式，操作终止!";return false;}
		
		String sql="";
		if(colFormu.indexOf("_")>=0){ //表达式的表间校验
			int posS=0,posE=0;			
						
			posS=colFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU);
			posE=colFormu.indexOf(SPLIT_SYMBOL_RIGHT_MID_HUOHU);
			
			String cellName=null;
			String cellValue="0";
			int dataRangeId=0;
			while(posS>=0 && posE>=0){
				cellName=colFormu.substring(posS+1,posE-1);
				
				if(cellName!=null && cellName.length()>2){
					if(cellName.indexOf("_")>0){
						String _arr[]=cellName.substring(1,cellName.length()-1).split("_");
						if(arr.length>=3){
							if(arr.length>3){
								try{
									if(arr.length==4 && !arr[3].startsWith("(")) dataRangeId=Integer.parseInt(arr[3]);
									if(arr.length==5) dataRangeId=Integer.parseInt(arr[4]);
								}catch(Exception e){
									dataRangeId=0;
								}
							}else{
								dataRangeId=reportIn.getDataRangeId().intValue();
							}
							cellValue=ReportImpl.getCellValue(conn,
									reportIn.getRepInId().intValue(),
									_arr[0],
									_arr[1],
									dataRangeId,
									reportIn.getOrgId(),
									reportIn.getCurId().intValue(),
									reportIn.getYear().intValue(),
									reportIn.getTerm().intValue(),
									_arr[2],
									Report.FORMU_TYPE_BJ);
						}
					}else if(cellName.substring(1,3).equals("T.")){
						cellValue=ReportImpl.getTotalCellValue(conn,table,reportIn.getRepInId().intValue(),
								cellName.substring(3,cellName.length()-1));
					}
					cellValue=format(cellValue);
					colFormu=colFormu.substring(0,posS) + 
						(cellValue.indexOf("-")==0?"(" + String.valueOf(cellValue)+ ")":String.valueOf(cellValue)) +  
						colFormu.substring(posE+1);

					posS=colFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
					posE=colFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);
				}
			}
			resVal=isEquals(conn,colFormu,pointNumber);
			
		}else{ //表达式的表内校验
			//判断公式中是否有"capAmount"单元格，如果有，把公式中的"[capAmount]"
			if(colFormu.indexOf("capAmount")>=0){
				String capAmountVal=ReportImpl.getCapAmountValue(conn,reportIn.getRepInId());
				if(capAmountVal.equals("")){
					return false;
				}
				colFormu=colFormu.replaceAll("[capAmount]",capAmountVal);
			}
			
			if(colFormu.indexOf("T.")>=0){	//校验报表的合计行
				int posS=0,posE=0;			
				
				posS=colFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU);
				posE=colFormu.indexOf(SPLIT_SYMBOL_RIGHT_MID_HUOHU);
				
				String cellName=null;
				String cellValue="0";
				while(posS>=0 && posE>=0){
					cellName=colFormu.substring(posS+1,posE);
					
					if(cellName!=null && cellName.length()>2){
						if(cellName.substring(0,2).equals("T.")){
							cellValue=ReportImpl.getTotalCellValue(conn,table,reportIn.getRepInId().intValue(),
									cellName.substring(2));
						}else{  //如果需获取的单元格不是合计行的单元格，返回false
							return false;
						}
						
						cellValue=format(cellValue);
						colFormu=colFormu.substring(0,posS) + 
							(cellValue.indexOf("-")==0?"(" + String.valueOf(cellValue) + ")":String.valueOf(cellValue)) +  
							colFormu.substring(posE+1);
						
						posS=colFormu.indexOf(Expression.SPLIT_SYMBOL_LEFT_MID_KUOHU);
						posE=colFormu.indexOf(Expression.SPLIT_SYMBOL_RIGHT_MID_HUOHU);

					}
				}
				resVal=isEquals(conn,colFormu,pointNumber);
			}else{
				if(reportIn.getChildRepId().equals("S3401")){
					return S3401Impl.validate(colFormu,arr,reportIn,table,conn,operator);
				}
			
				for(int i=0;i<arr.length-1;i++){
					if(getCellName(arr[i]).equals("")){throw new Exception("arr[i] is empty!");}
					sql+=(sql.equals("")?"":",") + 
						"(" + formatFormu(arr[i],reportIn.getChildRepId()) + 
						")-(" + 
						formatFormu(arr[i+1],reportIn.getChildRepId()) +
						")";
				}
				
				sql=sql.trim();
				if(sql.equals("")) {errMsg="将表内校验关系表达式生成的SQL为空，操作终止!";return false;}
				
				sql="select " + sql + " from " + table + " where rep_in_id=" + reportIn.getRepInId().intValue()+ " and replace(col1,' ','')<>'合计'";
						
				List resList=ReportImpl.getCalculatorResults(conn,sql,arr.length-1);
				
				if(resList==null){errMsg="获得表内校验关系表达式运算值失败，操作终止!";return false;}
				
				double res[]=null;
				errMsg="";
				for(int i=0;i<resList.size();i++){
					res=(double[])resList.get(i);					
					for(int j=0;j<res.length;j++){
						if(operator.equals(">=")){
							if(res[j]<0){resVal=false;errMsg+="表达式[" + colFormu + "],第" + i + "行校验不通过!";cause=errMsg;break;}
						}else if(operator.equals("<=")){
							if(res[j]>0){resVal=false;errMsg+="表达式[" + colFormu + "],第" + i + "行校验不通过!";cause=errMsg;break;}
						}else if(operator.equals(">")){
							if(res[j]<=0){resVal=false;errMsg+="表达式[" + colFormu + "],第" + i + "行校验不通过!";cause=errMsg;break;}
						}else if(operator.equals("<")){
							if(res[j]>=0){resVal=false;errMsg+="表达式[" + colFormu + "],第" + i + "行校验不通过!";cause=errMsg;break;}
						}else{
							if(res[j]!=0){resVal=false;errMsg+="表达式[" + colFormu + "],第" + i + "行校验不通过!";cause=errMsg;break;}
						}
					}
				}
			}			
		}
		
		return resVal;
	}
	
	/**
	 * 判断表达式的运算是否正确
	 * 
	 * @param conn Connection 连接
	 * @param expression String 已用单元格值替换后的表达式
	 * @boolean 运算正确，返回true；否则，返回false
	 */
	public static boolean isEquals(Connection conn,String expression,Integer pointNumber) throws Exception{
		boolean result=false;
		
		if(expression==null) return false;
		if(expression.equals("")) return false;
		expression=expression.replaceAll(BACKLASH,STATIC + BACKLASH);
		
		String[] arr=null;
		String operator=null;
		
		if(arr==null || arr.length<=1){arr=expression.split(">=");operator=">=";}
		if(arr==null || arr.length<=1){arr=expression.split("<=");operator="<=";}
		if(arr==null || arr.length<=1){arr=expression.split(">");operator=">";}
		if(arr==null || arr.length<=1){arr=expression.split("<");operator="<";}
		if(arr==null || arr.length<=1){arr=expression.split("=");operator="=";}
	
		if(arr==null || arr.length==0) return false;

		String sql="";
		int i=0;
		for(i=0;i<arr.length;i++){
			if(arr[i].indexOf(",")>=0) return false;
			sql+=(sql.equals("")?"":",") + 
				(arr[i].indexOf("/")>0?String.valueOf(ReportImpl.getExpressionResult(conn,arr[i])):arr[i]);
		}
		
		if(sql.equals("")) return false;
		
		String[] res=ReportImpl.getCalculatorResult(conn,sql,arr.length);
		if(res==null) return false;
			
		if(res.length>0){
			Double _value=Expression.round(res[0],pointNumber.intValue());
			if(operator.equals(">=")){
				for(i=1;i<res.length;i++){
					if(_value.compareTo(Expression.round(res[i],pointNumber.intValue()))>=0){
						result=true;
						_value=Expression.round(res[i],pointNumber.intValue());
					}else{
						result=false;
						break;
					}
				}
			}else if(operator.equals("<=")){
				for(i=1;i<res.length;i++){
					if(_value.compareTo(Expression.round(res[i],pointNumber.intValue()))<=0){
						result=true;
						_value=Expression.round(res[i],pointNumber.intValue());
					}else{
						result=false;
						break;
					}
				}
			}else if(operator.equals(">")){
				for(i=1;i<res.length;i++){
					if(_value.compareTo(Expression.round(res[i],pointNumber.intValue()))>0){
						result=true;
						_value=Expression.round(res[i],pointNumber.intValue());
					}else{
						result=false;
						break;
					}
				}
			}else if(operator.equals("<")){
				for(i=1;i<res.length;i++){
					if(_value.compareTo(Expression.round(res[i],pointNumber.intValue()))<0){
						result=true;
						_value=Expression.round(res[i],pointNumber.intValue());
					}else{
						result=false;
						break;
					}
				}
			}else{
				for(i=1;i<res.length;i++){
					if(_value.compareTo(Expression.round(res[i],pointNumber.intValue()))==0){
						result=true;
						_value=Expression.round(res[i],pointNumber.intValue());
					}else{
						result=false;
						break;
					}
				}
				
			}
		}
		return result;
	}
	
	/**
	 * 获取系统错误信息
	 */
	public static String getErrMsg(){
		return errMsg;
	}
	
	/**
	 * 获取校验未通过的原因
	 */
	public static String getCause(){
		return cause;
	}
	
	/** 
	 * 获得单元格的名称
	 * 
	 * @param cellFormu String 表达式中的单元格名称,如:[A10]
	 * @return String 格式化过的单元格名称,如:A10
	 */
	public static String getCellName(String cellFormu){
		String cellName="";
		
		if(cellFormu==null) return cellName;		
		try{
			int posS=cellFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU);
			int posE=cellFormu.indexOf(SPLIT_SYMBOL_RIGHT_MID_HUOHU);
	
			while(posS>=0 && posE>=0){
				//cellName+= cellFormu.substring(posS+1,posE);
				cellFormu=cellFormu.substring(0,posS) + 
					cellFormu.substring(posS+1,posE) + 
					cellFormu.substring(posE+1);
				posS=cellFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU,posS+1);
				posE=cellFormu.indexOf(SPLIT_SYMBOL_RIGHT_MID_HUOHU,posE+1);
			}
			cellName=cellFormu;
		}catch(Exception e){
			cellName="";
		}
		
		return cellName;
	}
	
	/** 
	 * 格式单元格表达式
	 * 
	 * @param cellFormu String 表达式中的单元格名称,如:[A10]-[A11]
	 * @return String 格式化过的单元格名称,如:Decimal(A10,10,2)-Decimal(A11,10,2)
	 */
	public static String formatFormu(String cellFormu,String childRepId){
		String G51="G5100";

		String cellName="",_cellFormu="";
		
		if(cellFormu==null) return cellName;
		
		try{
			int posS=cellFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU);
			int posE=cellFormu.indexOf(SPLIT_SYMBOL_RIGHT_MID_HUOHU);
			
			while(posS>=0 && posE>=0){
				//cellName+= cellFormu.substring(posS+1,posE);
				if(childRepId!=null && childRepId.toUpperCase().equals(G51)){
					cellName=getColName(cellFormu.substring(posS+1,posE));
				}else{
					cellName=cellFormu.substring(posS+1,posE);
				}
				if(cellName.equals("")) return "";
				cellFormu=cellFormu.substring(0,posS) + 
					"case when " + cellName + " is null then 0 else double(" +  cellName + ") end" + 
					cellFormu.substring(posE+1);
				posS=cellFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU,posS+1);
				posE=cellFormu.indexOf(SPLIT_SYMBOL_RIGHT_MID_HUOHU,posE+1);
			}
			_cellFormu=cellFormu;
		}catch(Exception e){
			_cellFormu="";
		}
		
		return _cellFormu;
	}
	
   /**  
	* 提供小数位四舍五入处理。
	*   
	* @param v 需要四舍五入的数字  
	* @param scale 小数点后保留几位  
	* @return 四舍五入后的结果  
	*/  
	public static Double round(String v,int scale){  
		if(v==null) return new Double(0.0);
		if(v.equals("")) return new Double(0.0);
		
		try{
			if(v.substring(0,1).equals("-")){
				int pos=v.indexOf(".");

				if(pos>=0 && v.substring(pos+1).length()>scale){
					if(v.substring(pos+1+scale,pos+1+scale+1).equals("5")){
						if(new Double("0." + v.substring(pos+1+scale)).doubleValue()==0.5){
							v=v.substring(0,pos+1+scale) + "0";
						}
					}
				}
			}
			
			BigDecimal bdFinalVal=new BigDecimal("1.00");
			BigDecimal bdChangeVal=new BigDecimal(v);
			
			return new Double(bdChangeVal.divide(bdFinalVal,scale,BigDecimal.ROUND_HALF_UP).doubleValue());
		}catch(Exception e){
			return new Double(0.00);
		}
	} 
	
	/**  
	 * 获得商的精确值
	 *   
	 * @param dividend double 被除数
	 * @param divisor double 除数
	 * @param scale int 保留的小数位
	 * @return float
	 */
	public static float round(double dividend,double divisor,int scale){  
	   try{			
			BigDecimal bdDividend=new BigDecimal(dividend);
			BigDecimal bdVivisor=new BigDecimal(divisor);
			
			return bdDividend.divide(bdVivisor,scale,BigDecimal.ROUND_HALF_UP).floatValue();
		}catch(Exception e){
			return 0;
		}
	} 
	
	/**
	 * XML数据元素名对应的数据表的列名
	 */
	private static String[][] ELEMENTTOCOL={
		{"COL2","Col2"},
		{"COL3","Col3"},
		{"COL4","Col4"},
		{"COL5","Col5"},
		{"COL6","Col6"},
		{"COL7","Col7"},
		{"COL8","Col8"},
		{"COL10-1","Col9"},
		{"COL11-1","Col10"},
		{"COL12-1","Col11"},
		{"COL13-1","Col12"},
		{"COL14-1","Col13"},
		{"COL15-1","Col14"},
		{"COL16-1","Col15"},
		{"COL17-1","Col16"},
		{"COL18-1","Col17"},
		{"COL10-2","Col18"},
		{"COL11-2","Col19"},
		{"COL12-2","Col20"},
		{"COL13-2","Col21"},
		{"COL14-2","Col22"},
		{"COL15-2","Col23"},
		{"COL16-2","Col24"},
		{"COL17-2","Col25"},
		{"COL18-2","Col26"},
		{"COL10-3","Col27"},
		{"COL11-3","Col28"},
		{"COL12-3","Col29"},
		{"COL13-3","Col30"},
		{"COL14-3","Col31"},
		{"COL15-3","Col32"},
		{"COL16-3","Col33"},
		{"COL17-3","Col34"},
		{"COL18-3","Col35"},
		{"COL10-4","Col36"},
		{"COL11-4","Col37"},
		{"COL12-4","Col38"},
		{"COL13-4","Col39"},
		{"COL14-4","Col40"},
		{"COL15-4","Col41"},
		{"COL16-4","Col42"},
		{"COL17-4","Col43"},
		{"COL18-4","Col44"}
	};
	
	/**
	 * 根据XML元素的名称获取其对应的数据表的列名
	 *
	 * @author rds
	 * @date 2006-01-11
	 * 
	 * @param key String 键
	 * @return String 值
	 */
	public static String getColName(String key){
		String colName="";
		if(key==null) return colName;
		
		try{
			for(int i=0;i<ELEMENTTOCOL.length;i++){
				if(ELEMENTTOCOL[i][0].toUpperCase().equals(key.toUpperCase())){
					colName=ELEMENTTOCOL[i][1];
					break;
				}
			}
		}catch(Exception e){
			colName="";
		}
		
		return colName;
	}
	
	/**
	 * 格式化<br>
	 * 将值中多余的"0"去除
	 * 
	 * @param cellValue String 值
	 * @return String 
	 */
	public static String format(String cellValue){
		String res="0";
		
		if(cellValue==null) return res;
		
		try{
			Double dblObj=new Double(cellValue);
			
			DecimalFormat dF=new DecimalFormat("#.####################");
			
			dF.setDecimalSeparatorAlwaysShown(false);
			res=dF.format(dblObj);
		}catch(Exception e){
			res="0";
		}
		
		return res + STATIC;
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

	/**
     * 验证单元格表达式内的值的比较结果是否成立
     * @author  gongming
     * @date    2007-09-25
     * @param   orclCon       Connection      数据库连接类
     * @param   formu         String          单元格表达式
     * @return  表达式成立返回true否则返回false
     * @throws Exception
     */
	public static boolean validExpression(Connection orclCon,String formu)throws Exception{
		if(orclCon == null || formu == null) return false;

		boolean passValid = false;
		String[] formuArray = null;
		
		//比较符号数组
		String[] compareSign = {">=","<=",">","<","="};
		int length = compareSign.length;
       
		for(int i = 0; i < length ; i ++){
			String sign = compareSign[i];
			formuArray = formu.split(sign);
          
			//如果拆分成两个元素数组
			if(formuArray != null && formuArray.length == 2)
				return ReportDDImpl.hasResult(orclCon,formuArray,sign);
		}         
		return passValid;
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
                MCellFormu cellFormu = (MCellFormu)itr.next();
                //单元格计算公式
                String formu = cellFormu.getCellFormu();
                /***
                 *  IF公式（表间）	if([C24]>0,[G0102_1010_F20]>0,1==1)
				 *	IF公式（表内）	[C10]+2>=if([C7]-[C6]>0,[C7]-[C6],0)>=[C10]-2
				 *	Min公式	[C13]+2>=min([C10],[C11])>=[C13]-2
				 *	Max公式	[C7]+2>=max([C8],[C9])>=[C7]-2
				 *	&&公式	0<if([C16]+[C17]=0&&[C18]+[C19]+[C20]=0,0,1)
				 *	||公式	暂无，格式参考&&公式
				 *
				 *	if([C7]-[C6]>0&&[C5]-[C4]<0&&([C3]-[C2]>0||[C1]-[C2]<0)||min([C10],C[11])||max([C8],[C9]),[C7]-[C6],0)=if([C7]-[C6]>0&&[C5]-[C4]<0&&([C3]-[C2]>0||[C1]-[C2]<0)||min([C10],C[11])||max([C8],[C9]),[C7]-[C6],0)>=[C10]-2
                 */
                try {
                	formu = convertFormula(formu.toLowerCase());
				} catch (Exception e) {	
					e.printStackTrace();
					throw new Exception("转换校验公式失败");
				}
                
                for (int i = 0; i < length; i++) {
                    //比较符号
                    String sign = compareSign[i];   
                    /***
                     * --------------------------------------------
                     * 由于包含if、min、max校验公式，公式内具备操作符
                     * 故在此处对校验公式进一步替换
                     * --------------------------------------------
                     */
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
	                    Set<Entry<String, String>> entry = forMap.entrySet();
	    				for(Entry<String, String> e:entry){
	    					if(tempFormu!=null && tempFormu.indexOf(e.getValue())>0)
	    						tempFormu = tempFormu.replace(e.getValue(), e.getKey());
	    				}    
	                    /**将解释后的表达式放入MAP*/
	                    if(_length >=2){
	                    	
	                    	for(Entry<String, String> e:entry){
	                    		if(temp[0]!=null && temp[0].indexOf(e.getValue())>0)
	                    			temp[0] = temp[0].replace(e.getValue(), e.getKey());
	                    		if(temp[1]!=null && temp[1].indexOf(e.getValue())>0)
	                    			temp[1] = temp[1].replace(e.getValue(), e.getKey());
	                    	}
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
	
	/***
	 * ------------------------------------------------------------------------
	 * 转换含有逻辑判断的校验公式，将逻辑判断形式转换为TSQL支持的语法格式
	 * 逻辑判断字符有：if,min,max
	 * if表达式：if(公式判断,值1,值2)---若公式判断为真，取值1，否则取值2，
	 * 		该表达式转换为TSQL语法为：case when 公式判断 then 值1 else 值2 end
	 * min表达式：min(值1,值2)----------取值1，值2两者中较小的值，
	 * 		该表达式转换为TSQL语法为：case when 值1<值2 then 值1 else 值2 end
	 * max表达式：max(值1,值2)----------取值1，值2两者中较大的值，
	 * 		该表达式转换为TSQL语法为：case when 值1>值2 then 值1 else 值2 end	
	 * @param oriS 校验公式字符串，用来传递转换前的校验公式及递归时的校验公式
	 * @author 卞以刚 2012/12/25
	 * ------------------------------------------------------------------------
	 */
	public static String convertFormula(String oriS) throws Exception{
		return convertFormula(oriS,null,null);
	}
	/***
	 * ------------------------------------------------------------------------
	 * @param oriS 校验公式字符串，用来传递转换前的校验公式及递归时的校验公式
	 * @param index 逻辑判断字符串，传递if min max 逻辑判断的字符串
	 * @param gatherS 保存每个逻辑判断字符之前的字符串，
	 * 				     转换后将转换的字符串与此字符串拼接成新字符串，递归解析
	 * 	假设校验公式如：[C7]+2>=max([C8],[C9])>=[C7]-2,
	 *  解析获得逻辑判断字符“max”在该校验公式中位置
	 *  gatherS用于保存“[C7]+2>=”，将max解析为case when后，与gatherS拼接为新字符串
	 *  [C7]+2>= case when [C8]>[C9] then [C8] else [C9] >=[C7]-2
	 * @return 返回解析过后的字符串
	 * @author 卞以刚 2012/12/25
	 * ------------------------------------------------------------------------
	 */
	public static String convertFormula(String oriS,String index,String gatherS) throws Exception{
		
		if(oriS==null || oriS.equals(""))
			return oriS;
		if(gatherS==null)
			gatherS = "";
		if(index==null)
			index = "";
		
		//判断是否含有逻辑判断字符“if”
		if(oriS.indexOf("if")>=0){
			//获取逻辑判断字符“if”之前的字符串（不包含if）
			String beforeIf = oriS.substring(0,oriS.indexOf("if"));
			//获取逻辑判断字符“if”后的字符串（不包含if）
			String overIf = oriS.substring(oriS.indexOf("if")+2);
			//分割字符串之后，if字符会丢失，影响后续解析，故此处手动添加“if”字符
			gatherS += beforeIf+"if";

			return convertFormula(overIf,"if",gatherS);//递归解析
		}
		if(oriS.indexOf("min")>=0){
			//获取逻辑判断字符“min”之前的字符串（不包含min）
			String beforeMin = oriS.substring(0,oriS.indexOf("min"));
			//获取逻辑判断字符“min”后的字符串（不包含min）
			String overMin = oriS.substring(oriS.indexOf("min")+3);	
			//分割字符串之后，min字符会丢失，影响后续解析，故此处手动添加“min”字符
			gatherS += beforeMin+"min";

			return convertFormula(overMin,"min",gatherS);//递归解析
		}
		if(oriS.indexOf("max")>=0){
			String beforeMax = oriS.substring(0,oriS.indexOf("max"));
			String overMax = oriS.substring(oriS.indexOf("max")+3);
			gatherS += beforeMax+"max";

			return convertFormula(overMax,"max",gatherS);//递归解析
		}
		try {
			if(index.equals("if")){
				/***
				 * if(公式,值1,值2):公式为true取值1,否则取值2
				 * 假设表达式如：if(2-3>0,2,3) 结果为3
				 */
				//截取“(”字符至第一个逗号之间的字符,如：“2-3>0”此为公式项
				String formulaS = oriS.substring(oriS.indexOf("(")+1,oriS.indexOf(","));
				/***
				 * 判断该公式项是否具有括号表达式，
				 * 若在算术表达式中包含“()”表达式将会影响后续判断，故在此处单独拎出表示算术表达式的“()”
				 * 将此表达式替换为指定字符，转换完成后再替换回来，以下均做如此处理
				 * 为保证能正确获取“(”，然后再获取“)”，此处将字符做分割处理（不做分割处理会出现“)”在前，“(”在后的情况，也能被替换）
				 */
				formulaS = convertMathFormu(formulaS);
				Set<Entry<String, String>> entry = forMap.entrySet();
				for(Entry<String, String> e:entry){
					if(formulaS.indexOf(e.getKey())>0)
						formulaS = formulaS.replace(e.getKey(), e.getValue());
				}
				/**
				 * 获取逗号之后剩下的字符串,因在截取公式项字符时去除了首字符“(”字符，
				 * 故在截取剩余字符时，取公式项字符长度+1
				 * 为去除公式项字符与值1之间的“,”字符，故在上述基础上再加1
				 * 故此处去公式项长度+2
				 * 值如：“2,3)”
				 */
				String valueS1 = oriS.substring(formulaS.length()+2);//去除开头(字符及末尾,字符
				//截取 值1字符串，如：2
				String value_1 = valueS1.substring(0,valueS1.indexOf(","));
				/**
				 * 判断该公式项是否具有括号表达式
				 */
				value_1 = convertMathFormu(value_1);
					
				//获取值1之后的字符串，为去除首字符“,”字符，故此处+1，如：3)
				String valueS2 = valueS1.substring(value_1.length()+1);//去除,
				/**
				 * 判断该公式项是否具有括号表达式
				 */
				valueS2 = convertMathFormu(valueS2);
				
				//获取值2字符串，并保留末尾)字符串，如：3
				String value_2 = valueS2.substring(0,valueS2.indexOf(")"));
				/**
				 * 判断该公式项是否具有括号表达式
				 */
				value_2 = convertMathFormu(value_2);
				//获取)后的字符串
				String valueS3 = valueS2.substring(value_2.length()+1);
				
				formulaS = " case when "+formulaS;//公式项转换为case when 2-3>0 TSQL支持的语法
				
				if(formulaS.indexOf("&&")>=0 || formulaS.indexOf("||")>=0){//含有&&符号，或者含有 ||符号
					if(formulaS.indexOf("&&")>=0){//判断是否含有&&符号
						formulaS = formulaS.replace("&&", " and ");	//替换&&为and
					} 
					if(formulaS.indexOf("||")>=0){//判断是否含有||符号
						formulaS = formulaS.replace("||", " or ");//替换||为or
					}
				}
				value_1 = " then " + value_1;//值1的转换  then 2
				
				value_2 = " else " + value_2 + " end ";//值2的转换 else 3 end
				
				/***
				 * 拼接转换后的字符
				 * case when 公式项 then 值1 else 值2 end;
				 * case when 2-3>0 then 2 else 3 end;
				 */
				String formu = formulaS+value_1+value_2+valueS3;
				
				/**
				 * 获取转换后的完整字符串，递归继续转换
				 * 递归转换时，截取if min max字符会造成此3个字符丢失，后续无法继续转换，故递归转换时在此字符位置添加
				 * if min max字符，转换后，再截取该出3个特殊字符，完成字符串拼接
				 */
				oriS = gatherS.substring(0,gatherS.lastIndexOf(index))+formu;
				
				return convertFormula(oriS, "", "");//递归转换
				
			}
			if(index.equals("min") || index.equals("max")){
				/**
				 * min(值1,值2):去值1，值2两值中较小的值
				 * 假设表达式为：min(2,3) 结果为2
				 */
				//截取值1,字符:2
				String value_1 = oriS.substring(oriS.indexOf("(")+1,oriS.indexOf(","));
				/**
				 * 判断该公式项是否具有括号表达式
				 */
				value_1 = convertMathFormu(value_1);
				//获取逗号之后剩下的字符串,并去除“,”字符：3)
				String valueS1 = oriS.substring(value_1.length()+2);//去除,
				/**
				 * 判断该公式项是否具有括号表达式
				 */
				valueS1 = convertMathFormu(valueS1);
				//截取值2,字符:3
				String value_2 = valueS1.substring(0,valueS1.indexOf(")"));
				/**
				 * 判断该公式项是否具有括号表达式
				 */
				value_2 = convertMathFormu(value_2);
				//获取“)”之后剩下的字符串
				String valueS2 = valueS1.substring(value_2.length()+1);
				
				String caseWhen = " case when "+value_1;//转换为TSQL支持语法  case when 2
				//value_2 = value_2.replace(")", "");//转换)为空字符
				
				String symBol = "";
				/***
				 * min(值1,值2)转换为:
				 * case when 值1>值2 then 值2 else 值1 end;
				 * case when 2>3 then 3 else 2 end;
				 */
				if(index.equals("min")){
					symBol = forMap.get(">");
				}
				/**
				 * max(值1,值2):去值1，值2两值中较大的值
				 * 假设表达式为：max(2,3) 结果为3
				 * case when 值1<值2 then 值2 else 值1 end;
				 * case when 2<3 then 3 else 2 end;
				 */
				if(index.equals("max")){
					symBol = forMap.get("<");
				}
				
				String formu = caseWhen+symBol+value_2+" then "+value_2+" else "+value_1+" end"+valueS2;
				
				/**
				 * 获取转换后的完整字符串，递归继续转换
				 * 递归转换时，截取if min max字符会造成此3个字符丢失，后续无法继续转换，故递归转换时在此字符位置添加
				 * if min max字符，转换后，再截取该出3个特殊字符，完成字符串拼接
				 */
				oriS = gatherS.substring(0,gatherS.lastIndexOf(index))+formu;
				
				return convertFormula(oriS, "", "");//递归转换
			}else{
				return oriS.replace("@", ")").replace("#", "(");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/***
	 * -----------------------------------------------------------------------------------------
	 * 判断该公式项是否具有括号表达式，
	 * 若在算术表达式中包含“()”表达式将会影响后续判断，故在此处单独拎出表示算术表达式的“()”
	 * 将此表达式替换为指定字符，转换完成后再替换回来，一下均做如此处理
	 * 为保证能正确获取“(”，然后再获取“)”
	 * 此处将字符做分割处理（不错分割处理会出现“)”在前，“(”在后的情况，也能被替换）
	 * @param valueString  传递的字符串
	 * @return 返回处理完成后的字符串
	 * @author 卞以刚 2012/12/25
	 * -----------------------------------------------------------------------------------------
	 */
	public static String convertMathFormu(String valueString){
		if(valueString==null || valueString.equals(""))
			return valueString;
		//循环替换
		while(valueString.indexOf("(")>=0 && valueString.indexOf(")")>0){
			//做字符分割处理，以第一个“(”字符做分割
			String beforeLeft = valueString.substring(0,valueString.indexOf("("));
			//“(”字符余下的字符串
			String  afterLeft = valueString.substring(valueString.indexOf("("));
			//将“(”替换为“@”
			afterLeft = afterLeft.replaceFirst("\\)", "@");
			//将“(”替换为“#”
			valueString = (beforeLeft+afterLeft).replaceFirst("\\(", "#");
		}
		return valueString;
	}
	
	
}