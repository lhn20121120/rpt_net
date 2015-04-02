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
	 * ����
	 */
	private static String STATIC="*double(1.00)";
	/**
	 * ϵͳ������Ϣ
	 */
	private static String errMsg="";
	/**
	 * У��δͨ����ԭ��
	 */
	private static String cause="";		
	/**
	 * ���������
	 */
	public static String SPLIT_SYMBOL_LEFT_MID_KUOHU="[";    	
	/**
	 * �ұ�������
	 */
	public static String SPLIT_SYMBOL_RIGHT_MID_HUOHU="]";   	
	/**
	 * ���ں�
	 */
	public static String SPLIT_SYMBOL_EQUAL="=";
	/**
	 * ��б��
	 */
	public static String BACKLASH="/";
		
	private static Evaluator evaluator = new Evaluator();

	
    /**
     *  ����ݱ�У�� 
     * @author  gongming
     * @date    2007-09-26
     * @param orclCon       Connection          ���ݿ�������
     * @param reportIn      ReportIn            ��������
     * @param cellFormuLst  List                ��ԪУ����ʽ����
     * @param cellMap       Map                 ����ݵ�Ԫ�����ƣ�ֵ��ֵ�Լ���
     * @return   Map(key-��Ԫ����ʽ��,value-�Ƿ�ͨ��У���booleanֵ)
     * @throws Exception
     */
    public static Map valid(Connection orclCon,AfReport reportIn,List cellFormuLst,Map cellMap, Integer templateType) throws Exception{
       
        if(orclCon == null || reportIn == null || templateType == null) return null;
        
        Map validMap = null;
        
//        if(Report.REPORT_STYLE_DD == reportIn.getReportStyle().intValue()){
        if(Report.REPORT_STYLE_DD == reportIn.getTemplateStyle()){
            //��Ե���ʽ��У��
            if(cellFormuLst != null && !cellFormuLst.isEmpty() && cellMap != null && !cellMap.isEmpty())
            	
                validMap = ddExpressionValid(orclCon,reportIn,cellFormuLst,cellMap,templateType);
                cellMap.clear();
        }
        return validMap;
    }

    /**
     * ������oracle�﷨ ����Ҫ�޸� 
     * ���Ը� 2011-12-26
     * ��֤һ���������е�Ԫ����ʽ��������Ƿ���ȷ
     * @author  gongming
     * @date    2007-09-26
     * @param   conn            Connection      ���ݿ�������
     * @param   reportIn        ReportIn        ��������
     * @param   cellFormuLst    List            ��Ԫ���Ｏ��
     * @param   cellMap         Map             ����ݵ�Ԫ�����ƣ�ֵ��ֵ�Լ���
     * @return  Map(key-��Ԫ����ʽ�࣬value-��֤�Ƿ���ȷbooleanֵ)
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

            //��������֤���
            boolean valid = false;           
            while (itr.hasNext()) {
                AfValidateformula mCellFormu = (AfValidateformula) itr.next();
                String cellFormu = mCellFormu.getFormulaValue();
                cellFormu = cellFormu.trim();
                
                //�����������ʽУ��
                if (cellFormu.toLowerCase().indexOf("reg") >= 0) {
                	
                    valid = ValidateP2PReport.isRegexRight(cellFormu,cellMap);
                    validMap.put(mCellFormu, new Boolean(valid));
                    
                } else { 
                	if(cellFormu.indexOf(Config.SPLIT_SYMBOL_CURRMONTH)>-1){
                		cellFormu = cellFormu.replaceAll(Config.SPLIT_SYMBOL_CURRMONTH, String.valueOf(reportIn.getTerm()));
                	}
                    // ��ȡ��Ԫ����ʽ�ڵĵ�Ԫ�����Ƶļ���
                    List cellNameLst = Expression.getCellNames(cellFormu);
                    
                    // ��ȡ�����ϱ�ֵ�͵�Ԫ���������������
                    String[][] formuValue = null;
                    
                    if(cellFormu.toLowerCase().indexOf("acct") >= 0){
                    	/**������oracle�﷨**/
                    	formuValue = AccountingValid.AccoutingBalanceVal(orclCon, cellMap, cellNameLst, 
                    			dates, reportIn.getOrgId(), reportIn.getRepFreqId().toString(), 
                    			reportIn.getCurId().toString(), templateType);
                    	
                    	//ȡ��acct��벿�ֵ�У�鹫ʽ
                    	cellFormu = cellFormu.split("acct")[1].trim();
                    	
                    }else{
                    	/****
                    	 * jdbc���� ������oracle�﷨ ����Ҫ�޸�
                    	 * ���Ը� 2011-12-26
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
                    // �����ʽ�д���if���
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
             * jdbc���� ������oracle�﷨ ����Ҫ�޸�
             * ���Ը� 2011-12-26
             */
            Map formuMap = ValidateP2PReport.hasResult(orclCon,splitFormu(orclCon,notRegLst));
            if(formuMap != null) 
                validMap.putAll(formuMap);   
        }
        return validMap;
    }
    
	/**
	 * 
	 * ����ȥ"[","]"���ŵĵ�Ԫ����ʽ��������ϳɷ������ݿ�Ƚ���ʽ���ַ���
	 *   ��Map������ʽ����
	 * @author   gongming
	 * @date     2007-09-26
	 * @param mCellFormuLst      List      ��Ԫ����ʽ����
	 * @return   Map         (key-��Ԫ����ʽ��value-��ֳ���������ʽ�ͱȽϷ�������)
	 * @throws Exception
	 */
	public static Map splitFormu(Connection orclCon,List mCellFormuLst)throws Exception{
		Map mCellFormuMap = null;
		// �ȽϷ�������
		String[] compareSign = { ">=", "<=", ">", "<", "=" };
		int length = compareSign.length;
        
		if (mCellFormuLst != null || !mCellFormuLst.isEmpty()) {
            mCellFormuMap = new HashMap();
            Iterator itr = mCellFormuLst.iterator();
            
            while (itr.hasNext()) {                
            	//��Ԫ����ʽ��
                AfValidateformula cellFormu = (AfValidateformula)itr.next();
                //��Ԫ����㹫ʽ
                String formu = cellFormu.getFormulaValue();
                for (int i = 0; i < length; i++) {
                    //�ȽϷ���
                    String sign = compareSign[i];              
                    String[] temp = formu.split(sign);
                    String tempFormu = null;
                    // �����ֳ�����Ԫ�ص�����
                    if (temp != null) {
	                    int _length = temp.length;
	                    /**
	                     * С��λ�����⣺��0.61=16500000000.12/28340000000.14  С��λ������Ϊ4��У�鲻ͨ��
	                     * A1>=A2+A3
	                     * A2+A3<=A1<=A4+A5
	                     * 
	                     * ���Ը���С��λΪ��׼����С��λ��
	                     */
	                    int deci=4;
	                    
	                    for(int j = 0 ; j<_length ; j++){
	                    	if(temp[j].indexOf("/")>=0){
	                    		temp[j] = isZERO(orclCon,temp[j]);
	                    	}
	                    }
	                    
	                    // a(>=,>,<=,<,<)��ʽ
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
	                   
	                    // a(>=,>,<=,<)b(>=,>,<=,<)c ��ʽ     
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
	                    
	                    // a(>=,>,<=,<)b(>=,>,<=,<)c(>=,>,<=,<)d(>=,>,<=,<)... ��ʽ 
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
		                    
	                    /**�����ͺ�ı��ʽ����MAP*/
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
	 * �ж��Ƿ�����
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
			//���ʽ�Ƿ������ͳ�Ʋ�ѯ���
			String orclSql = "select count(1) as count1 from REPORT_IN where " + formu.toUpperCase() +" = " + temp;
			try {
				plStmt = orclCon.createStatement();
				plRS = plStmt.executeQuery(orclSql);
				
				if (plRS.next()) {
					// �ж��Ƿ��з��ϱ��ʽ�ļ�¼����
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
     * ��ȡ��Ԫ����㹫ʽ�еĵ�Ԫ�����Ƶļ���
     * @author  gongming
     * @date    2007-09-25
     * @param   cellFormu     String      ��Ԫ����㹫ʽ
     * @return  List
     */
	public static List getCellNames(String cellFormu){	    
        List cellNameLst = null;
        try{
            //"["����
            int posS=cellFormu.indexOf(SPLIT_SYMBOL_LEFT_MID_KUOHU);
            //"]"����
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
