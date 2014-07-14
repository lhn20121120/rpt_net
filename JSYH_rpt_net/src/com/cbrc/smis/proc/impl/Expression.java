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
 * ���ʽ���������
 * 
 * @author rds
 * @serialData 2005-12-20 19:51
 */
public class Expression {
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
	
	private static Map<String, String> forMap = new HashMap<String, String>();
	
	static{
		forMap.put(">", "$");
		forMap.put(">=", "$!");
		forMap.put("=", "!");
		forMap.put("<", "&");
		forMap.put(">=", "&!");
	}
	/**
	 * ���У��
	 * 
	 * @param conn Connection ���ݿ�����
	 * @param reportIn ReportIn ʵ�����ݱ������
	 * @param cellFormu String ���ʽ
	 * @return boolean У��ͨ��������true;���򣬷���false
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
		
		if(reportIn.getReportStyle().intValue()==Report.REPORT_STYLE_DD){     //��Ե���ʽ��У��   
			resVal=ddExpressionValidate(conn,reportIn,cellFormu,pointNumber);
		}else{   //�嵥ʽ���ʽ��У��
			resVal=dqExpressionValidate(conn,reportIn,cellFormu,pointNumber);
		}
			
		return resVal;
	}
	
	/**
	 * ����У��
	 * 
	 * @param conn Connection ���ݿ�����
	 * @param reportIn ReportIn ʵ�����ݱ������
	 * @param cellFormu String ���ʽ
	 * @return boolean У��ͨ��������true;���򣬷���false
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
			//��Ե���ʽ��У��
			resVal=ddExpressionValidate(conn,reportIn,cellFormu,PointNumber);
		}else{   
			//�嵥ʽ���ʽ��У��
			resVal=dqExpressionValidate(conn,reportIn,cellFormu,PointNumber);
		}
		
		return resVal;
	}
	
	/**
     *  ����ݱ��У�� --��ʹ��
     * @author  gongming
     * @date    2007-09-25
     * @param orclCon       Connection          ���ݿ�������
     * @param reportIn      ReportIn            ��������
     * @param mCellFormu    MCellForm           ��ԪУ����ʽ
     * @param cellMap       Map                 ����ݵ�Ԫ�����ƣ�ֵ��ֵ�Լ���
     * @return   ����ͨ������true��ͨ������false
     * @throws Exception
	 */
    public static boolean bnValid(Connection orclCon,ReportIn reportIn,MCellFormu mCellFormu,Map cellMap) throws Exception{
        boolean isValid = false;
        if(orclCon == null || reportIn == null 
                || mCellFormu == null || cellMap == null || cellMap.isEmpty())
            return false;
        
        if(Report.REPORT_STYLE_DD == reportIn.getReportStyle().intValue()){  
            //��Ե���ʽ��У��
            if(null != mCellFormu.getCellFormu() && null != mCellFormu.getPointNumber()){
               // isValid =ddExpressionValid(orclCon,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber(),cellMap);
            }
        }else{  
            //�嵥ʽ���ʽ��У��
            if(null != mCellFormu.getCellFormu())
                isValid =dqExpressionValidate(orclCon,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber());
           
        }        
        return isValid;
    }
    
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
    public static Map valid(Connection orclCon,ReportIn reportIn,List cellFormuLst,Map cellMap) throws Exception{
       
        if(orclCon == null || reportIn == null) return null;
        Map validMap = null;
        if(Report.REPORT_STYLE_DD == reportIn.getReportStyle().intValue()){  
            //��Ե���ʽ��У��
            if(cellFormuLst != null && !cellFormuLst.isEmpty()
                    && cellMap != null && !cellMap.isEmpty())
                validMap =ddExpressionValid(orclCon,reportIn,cellFormuLst,cellMap);
                cellMap.clear();
        }else{  
            //�嵥ʽ���ʽ��У��
//            if(null != mCellFormu.getCellFormu())
//                isValid =dqExpressionValidate(orclCon,reportIn,mCellFormu.getCellFormu(),mCellFormu.getPointNumber());
           
        }        
        return validMap;
    }
    
	/**
	 * ��Ե�ʽ����ı��ʽ��У��
	 * 
	 * @param conn Connection ���ݿ�����
	 * @param reportIn ReportIn ʵ�����ݱ������
	 * @param cellFormu String ���ʽ
	 * @return boolean У��ͨ��������true;���򣬷���false
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
				if(cellName.indexOf("_")>0){  //���У��
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
				}else{  //����У��
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
    private static Map ddExpressionValid(Connection orclCon,ReportIn reportIn,List cellFormuLst,Map cellMap) throws Exception{
        if(cellMap == null || cellMap.isEmpty()) return null;
        Map validMap = null;
        if (cellFormuLst != null && !cellFormuLst.isEmpty()) {
            Iterator itr = cellFormuLst.iterator();
            validMap = new HashMap();
            List notRegLst = new ArrayList();
            
            String dates = reportIn.getYear() + "-" + reportIn.getTerm() + "-"
            	+ com.fitech.gznx.common.DateUtil.getMonthLastDay(reportIn.getYear().intValue(), reportIn.getTerm().intValue());
            
            //��������֤���
            boolean valid = false;           
            while (itr.hasNext()) {
                MCellFormu mCellFormu = (MCellFormu) itr.next();
                String cellFormu = mCellFormu.getCellFormu();
                cellFormu = cellFormu.trim();
                
                /**�������������У��Ĺ�ʽ����*/
                NotValidateExpress notValidate = NotValidateExpress.newInstance();
//                String expresses = null;
//                try{
//                	expresses = notValidate.getNotValidateExpresses(reportIn.getOrgId()+"_"+reportIn.getChildRepId()+"_"+reportIn.getDataRangeId());
//                }catch(Exception ex){
//                	expresses = null;
//                }
                //2013-12-12:LuYueFei--����G3302��Ҫ��������Ĺ�ʽע���������֣��޸�Ϊ���������У�鹫ʽ�Ĵ���ʽ
                String key1=new StringBuffer().append(reportIn.getOrgId()).append("_").append(reportIn.getChildRepId()).append("_").append(reportIn.getDataRangeId()).toString();
                String key2=new StringBuffer().append(reportIn.getChildRepId()).append("_").append(reportIn.getDataRangeId()).append("_").append(reportIn.getCurId()).toString();
                String expresses = notValidate.getNotValidateExpressesFromMap(key1);//�õ�������ID+����ID����Ӧ��ֵ
                expresses=(expresses==null)?notValidate.getNotValidateExpressesFromMap(key2):expresses;//�õ�(����ID+����ID)��Ӧ��ֵ
                if(expresses != null && expresses.indexOf(cellFormu) > -1) continue;
                
                //�����������ʽУ��
                if (cellFormu.toLowerCase().indexOf("reg") >= 0) {
//                	if (cellFormu.indexOf("F57")!=-1){
//                		System.out.println(99);
//                	}
                    valid = ReportDDImpl.isRegexRight(mCellFormu,cellFormu,cellMap);
                    validMap.put(mCellFormu, new Boolean(valid));
                } else {            
                	
                    // ��ȡ��Ԫ����ʽ�ڵĵ�Ԫ�����Ƶļ���
                    List cellNameLst = Expression.getCellNames(cellFormu);
                    // ��ȡ�����ϱ�ֵ�͵�Ԫ���������������

                    // ��ȡ�����ϱ�ֵ�͵�Ԫ���������������
                    String[][] formuValue = null;
                    
                    if(cellFormu.toLowerCase().indexOf("acct") >= 0){
                    	
                    	formuValue = AccountingValid.AccoutingBalanceVal(orclCon, cellMap, cellNameLst, 
                    			dates, reportIn.getOrgId(), reportIn.getRepFreqId().toString(), 
                    			reportIn.getCurId().toString(), null);
                    	
                    	//ȡ��acct��벿�ֵ�У�鹫ʽ
                    	cellFormu = cellFormu.split("acct")[1].trim();
                    	
                    }else{
                    	/**
                    	 * ��ȡ�����ϱ�ֵ�͵�Ԫ����������
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
                     * У�鹫ʽ������Ԫ���滻Ϊ�ϱ�ֵ
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
	 * �嵥ʽ����ı��ʽ��У��
	 * 
	 * @param conn Connection ���ݿ�����
	 * @param reportIn ReportIn ʵ�����ݱ������
	 * @param colFormu String �й�ʽ
	 * @return boolean У����ȷ������true;���򣬷���false
	 * @exception Exception
	 */
	private static boolean dqExpressionValidate(Connection conn,ReportIn reportIn,String colFormu,Integer pointNumber) throws Exception{
		boolean resVal=true;

		if(conn==null || reportIn==null || reportIn.getRepInId()==null ||
				reportIn.getChildRepId()==null || reportIn.getVersionId()==null
				|| colFormu==null)
			return false;
		
		/**�嵥ʽ�����ʵ�����ݱ���**/
		String table=ReportImpl.findTableName(conn,reportIn.getChildRepId(),reportIn.getVersionId());
		if(table==null) {
			errMsg="����嵥ʽ����[���:" + reportIn.getChildRepId() + 
				";�汾��:" + reportIn.getVersionId() +
				"]��ʵ�����ݱ�ʧ�ܣ�������ֹ!";
			return false;
		}
		if(colFormu.toLowerCase().indexOf("reg")>=0){
			return ReportQDRegImpl.isRegexRight(conn,reportIn,colFormu,table);
		}
		
		if(colFormu.indexOf("��")>=0){
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
		
		if(arr==null || arr.length<=0) {errMsg="�޷�������ϵ���ʽ��������ֹ!";return false;}
		
		String sql="";
		if(colFormu.indexOf("_")>=0){ //���ʽ�ı��У��
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
			
		}else{ //���ʽ�ı���У��
			//�жϹ�ʽ���Ƿ���"capAmount"��Ԫ������У��ѹ�ʽ�е�"[capAmount]"
			if(colFormu.indexOf("capAmount")>=0){
				String capAmountVal=ReportImpl.getCapAmountValue(conn,reportIn.getRepInId());
				if(capAmountVal.equals("")){
					return false;
				}
				colFormu=colFormu.replaceAll("[capAmount]",capAmountVal);
			}
			
			if(colFormu.indexOf("T.")>=0){	//У�鱨��ĺϼ���
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
						}else{  //������ȡ�ĵ�Ԫ���Ǻϼ��еĵ�Ԫ�񣬷���false
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
				if(sql.equals("")) {errMsg="������У���ϵ���ʽ���ɵ�SQLΪ�գ�������ֹ!";return false;}
				
				sql="select " + sql + " from " + table + " where rep_in_id=" + reportIn.getRepInId().intValue()+ " and replace(col1,' ','')<>'�ϼ�'";
						
				List resList=ReportImpl.getCalculatorResults(conn,sql,arr.length-1);
				
				if(resList==null){errMsg="��ñ���У���ϵ���ʽ����ֵʧ�ܣ�������ֹ!";return false;}
				
				double res[]=null;
				errMsg="";
				for(int i=0;i<resList.size();i++){
					res=(double[])resList.get(i);					
					for(int j=0;j<res.length;j++){
						if(operator.equals(">=")){
							if(res[j]<0){resVal=false;errMsg+="���ʽ[" + colFormu + "],��" + i + "��У�鲻ͨ��!";cause=errMsg;break;}
						}else if(operator.equals("<=")){
							if(res[j]>0){resVal=false;errMsg+="���ʽ[" + colFormu + "],��" + i + "��У�鲻ͨ��!";cause=errMsg;break;}
						}else if(operator.equals(">")){
							if(res[j]<=0){resVal=false;errMsg+="���ʽ[" + colFormu + "],��" + i + "��У�鲻ͨ��!";cause=errMsg;break;}
						}else if(operator.equals("<")){
							if(res[j]>=0){resVal=false;errMsg+="���ʽ[" + colFormu + "],��" + i + "��У�鲻ͨ��!";cause=errMsg;break;}
						}else{
							if(res[j]!=0){resVal=false;errMsg+="���ʽ[" + colFormu + "],��" + i + "��У�鲻ͨ��!";cause=errMsg;break;}
						}
					}
				}
			}			
		}
		
		return resVal;
	}
	
	/**
	 * �жϱ��ʽ�������Ƿ���ȷ
	 * 
	 * @param conn Connection ����
	 * @param expression String ���õ�Ԫ��ֵ�滻��ı��ʽ
	 * @boolean ������ȷ������true�����򣬷���false
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
	 * ��ȡϵͳ������Ϣ
	 */
	public static String getErrMsg(){
		return errMsg;
	}
	
	/**
	 * ��ȡУ��δͨ����ԭ��
	 */
	public static String getCause(){
		return cause;
	}
	
	/** 
	 * ��õ�Ԫ�������
	 * 
	 * @param cellFormu String ���ʽ�еĵ�Ԫ������,��:[A10]
	 * @return String ��ʽ�����ĵ�Ԫ������,��:A10
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
	 * ��ʽ��Ԫ����ʽ
	 * 
	 * @param cellFormu String ���ʽ�еĵ�Ԫ������,��:[A10]-[A11]
	 * @return String ��ʽ�����ĵ�Ԫ������,��:Decimal(A10,10,2)-Decimal(A11,10,2)
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
	* �ṩС��λ�������봦��
	*   
	* @param v ��Ҫ�������������  
	* @param scale С���������λ  
	* @return ���������Ľ��  
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
	 * ����̵ľ�ȷֵ
	 *   
	 * @param dividend double ������
	 * @param divisor double ����
	 * @param scale int ������С��λ
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
	 * XML����Ԫ������Ӧ�����ݱ������
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
	 * ����XMLԪ�ص����ƻ�ȡ���Ӧ�����ݱ������
	 *
	 * @author rds
	 * @date 2006-01-11
	 * 
	 * @param key String ��
	 * @return String ֵ
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
	 * ��ʽ��<br>
	 * ��ֵ�ж����"0"ȥ��
	 * 
	 * @param cellValue String ֵ
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

	/**
     * ��֤��Ԫ����ʽ�ڵ�ֵ�ıȽϽ���Ƿ����
     * @author  gongming
     * @date    2007-09-25
     * @param   orclCon       Connection      ���ݿ�������
     * @param   formu         String          ��Ԫ����ʽ
     * @return  ���ʽ��������true���򷵻�false
     * @throws Exception
     */
	public static boolean validExpression(Connection orclCon,String formu)throws Exception{
		if(orclCon == null || formu == null) return false;

		boolean passValid = false;
		String[] formuArray = null;
		
		//�ȽϷ�������
		String[] compareSign = {">=","<=",">","<","="};
		int length = compareSign.length;
       
		for(int i = 0; i < length ; i ++){
			String sign = compareSign[i];
			formuArray = formu.split(sign);
          
			//�����ֳ�����Ԫ������
			if(formuArray != null && formuArray.length == 2)
				return ReportDDImpl.hasResult(orclCon,formuArray,sign);
		}         
		return passValid;
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
                MCellFormu cellFormu = (MCellFormu)itr.next();
                //��Ԫ����㹫ʽ
                String formu = cellFormu.getCellFormu();
                /***
                 *  IF��ʽ����䣩	if([C24]>0,[G0102_1010_F20]>0,1==1)
				 *	IF��ʽ�����ڣ�	[C10]+2>=if([C7]-[C6]>0,[C7]-[C6],0)>=[C10]-2
				 *	Min��ʽ	[C13]+2>=min([C10],[C11])>=[C13]-2
				 *	Max��ʽ	[C7]+2>=max([C8],[C9])>=[C7]-2
				 *	&&��ʽ	0<if([C16]+[C17]=0&&[C18]+[C19]+[C20]=0,0,1)
				 *	||��ʽ	���ޣ���ʽ�ο�&&��ʽ
				 *
				 *	if([C7]-[C6]>0&&[C5]-[C4]<0&&([C3]-[C2]>0||[C1]-[C2]<0)||min([C10],C[11])||max([C8],[C9]),[C7]-[C6],0)=if([C7]-[C6]>0&&[C5]-[C4]<0&&([C3]-[C2]>0||[C1]-[C2]<0)||min([C10],C[11])||max([C8],[C9]),[C7]-[C6],0)>=[C10]-2
                 */
                try {
                	formu = convertFormula(formu.toLowerCase());
				} catch (Exception e) {	
					e.printStackTrace();
					throw new Exception("ת��У�鹫ʽʧ��");
				}
                
                for (int i = 0; i < length; i++) {
                    //�ȽϷ���
                    String sign = compareSign[i];   
                    /***
                     * --------------------------------------------
                     * ���ڰ���if��min��maxУ�鹫ʽ����ʽ�ھ߱�������
                     * ���ڴ˴���У�鹫ʽ��һ���滻
                     * --------------------------------------------
                     */
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
	                    Set<Entry<String, String>> entry = forMap.entrySet();
	    				for(Entry<String, String> e:entry){
	    					if(tempFormu!=null && tempFormu.indexOf(e.getValue())>0)
	    						tempFormu = tempFormu.replace(e.getValue(), e.getKey());
	    				}    
	                    /**�����ͺ�ı��ʽ����MAP*/
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
	
	/***
	 * ------------------------------------------------------------------------
	 * ת�������߼��жϵ�У�鹫ʽ�����߼��ж���ʽת��ΪTSQL֧�ֵ��﷨��ʽ
	 * �߼��ж��ַ��У�if,min,max
	 * if���ʽ��if(��ʽ�ж�,ֵ1,ֵ2)---����ʽ�ж�Ϊ�棬ȡֵ1������ȡֵ2��
	 * 		�ñ��ʽת��ΪTSQL�﷨Ϊ��case when ��ʽ�ж� then ֵ1 else ֵ2 end
	 * min���ʽ��min(ֵ1,ֵ2)----------ȡֵ1��ֵ2�����н�С��ֵ��
	 * 		�ñ��ʽת��ΪTSQL�﷨Ϊ��case when ֵ1<ֵ2 then ֵ1 else ֵ2 end
	 * max���ʽ��max(ֵ1,ֵ2)----------ȡֵ1��ֵ2�����нϴ��ֵ��
	 * 		�ñ��ʽת��ΪTSQL�﷨Ϊ��case when ֵ1>ֵ2 then ֵ1 else ֵ2 end	
	 * @param oriS У�鹫ʽ�ַ�������������ת��ǰ��У�鹫ʽ���ݹ�ʱ��У�鹫ʽ
	 * @author ���Ը� 2012/12/25
	 * ------------------------------------------------------------------------
	 */
	public static String convertFormula(String oriS) throws Exception{
		return convertFormula(oriS,null,null);
	}
	/***
	 * ------------------------------------------------------------------------
	 * @param oriS У�鹫ʽ�ַ�������������ת��ǰ��У�鹫ʽ���ݹ�ʱ��У�鹫ʽ
	 * @param index �߼��ж��ַ���������if min max �߼��жϵ��ַ���
	 * @param gatherS ����ÿ���߼��ж��ַ�֮ǰ���ַ�����
	 * 				     ת����ת�����ַ�������ַ���ƴ�ӳ����ַ������ݹ����
	 * 	����У�鹫ʽ�磺[C7]+2>=max([C8],[C9])>=[C7]-2,
	 *  ��������߼��ж��ַ���max���ڸ�У�鹫ʽ��λ��
	 *  gatherS���ڱ��桰[C7]+2>=������max����Ϊcase when����gatherSƴ��Ϊ���ַ���
	 *  [C7]+2>= case when [C8]>[C9] then [C8] else [C9] >=[C7]-2
	 * @return ���ؽ���������ַ���
	 * @author ���Ը� 2012/12/25
	 * ------------------------------------------------------------------------
	 */
	public static String convertFormula(String oriS,String index,String gatherS) throws Exception{
		
		if(oriS==null || oriS.equals(""))
			return oriS;
		if(gatherS==null)
			gatherS = "";
		if(index==null)
			index = "";
		
		//�ж��Ƿ����߼��ж��ַ���if��
		if(oriS.indexOf("if")>=0){
			//��ȡ�߼��ж��ַ���if��֮ǰ���ַ�����������if��
			String beforeIf = oriS.substring(0,oriS.indexOf("if"));
			//��ȡ�߼��ж��ַ���if������ַ�����������if��
			String overIf = oriS.substring(oriS.indexOf("if")+2);
			//�ָ��ַ���֮��if�ַ��ᶪʧ��Ӱ������������ʴ˴��ֶ���ӡ�if���ַ�
			gatherS += beforeIf+"if";

			return convertFormula(overIf,"if",gatherS);//�ݹ����
		}
		if(oriS.indexOf("min")>=0){
			//��ȡ�߼��ж��ַ���min��֮ǰ���ַ�����������min��
			String beforeMin = oriS.substring(0,oriS.indexOf("min"));
			//��ȡ�߼��ж��ַ���min������ַ�����������min��
			String overMin = oriS.substring(oriS.indexOf("min")+3);	
			//�ָ��ַ���֮��min�ַ��ᶪʧ��Ӱ������������ʴ˴��ֶ���ӡ�min���ַ�
			gatherS += beforeMin+"min";

			return convertFormula(overMin,"min",gatherS);//�ݹ����
		}
		if(oriS.indexOf("max")>=0){
			String beforeMax = oriS.substring(0,oriS.indexOf("max"));
			String overMax = oriS.substring(oriS.indexOf("max")+3);
			gatherS += beforeMax+"max";

			return convertFormula(overMax,"max",gatherS);//�ݹ����
		}
		try {
			if(index.equals("if")){
				/***
				 * if(��ʽ,ֵ1,ֵ2):��ʽΪtrueȡֵ1,����ȡֵ2
				 * ������ʽ�磺if(2-3>0,2,3) ���Ϊ3
				 */
				//��ȡ��(���ַ�����һ������֮����ַ�,�磺��2-3>0����Ϊ��ʽ��
				String formulaS = oriS.substring(oriS.indexOf("(")+1,oriS.indexOf(","));
				/***
				 * �жϸù�ʽ���Ƿ�������ű��ʽ��
				 * �����������ʽ�а�����()�����ʽ����Ӱ������жϣ����ڴ˴����������ʾ�������ʽ�ġ�()��
				 * ���˱��ʽ�滻Ϊָ���ַ���ת����ɺ����滻���������¾�����˴���
				 * Ϊ��֤����ȷ��ȡ��(����Ȼ���ٻ�ȡ��)�����˴����ַ����ָ�������ָ�����֡�)����ǰ����(���ں�������Ҳ�ܱ��滻��
				 */
				formulaS = convertMathFormu(formulaS);
				Set<Entry<String, String>> entry = forMap.entrySet();
				for(Entry<String, String> e:entry){
					if(formulaS.indexOf(e.getKey())>0)
						formulaS = formulaS.replace(e.getKey(), e.getValue());
				}
				/**
				 * ��ȡ����֮��ʣ�µ��ַ���,���ڽ�ȡ��ʽ���ַ�ʱȥ�������ַ���(���ַ���
				 * ���ڽ�ȡʣ���ַ�ʱ��ȡ��ʽ���ַ�����+1
				 * Ϊȥ����ʽ���ַ���ֵ1֮��ġ�,���ַ������������������ټ�1
				 * �ʴ˴�ȥ��ʽ���+2
				 * ֵ�磺��2,3)��
				 */
				String valueS1 = oriS.substring(formulaS.length()+2);//ȥ����ͷ(�ַ���ĩβ,�ַ�
				//��ȡ ֵ1�ַ������磺2
				String value_1 = valueS1.substring(0,valueS1.indexOf(","));
				/**
				 * �жϸù�ʽ���Ƿ�������ű��ʽ
				 */
				value_1 = convertMathFormu(value_1);
					
				//��ȡֵ1֮����ַ�����Ϊȥ�����ַ���,���ַ����ʴ˴�+1���磺3)
				String valueS2 = valueS1.substring(value_1.length()+1);//ȥ��,
				/**
				 * �жϸù�ʽ���Ƿ�������ű��ʽ
				 */
				valueS2 = convertMathFormu(valueS2);
				
				//��ȡֵ2�ַ�����������ĩβ)�ַ������磺3
				String value_2 = valueS2.substring(0,valueS2.indexOf(")"));
				/**
				 * �жϸù�ʽ���Ƿ�������ű��ʽ
				 */
				value_2 = convertMathFormu(value_2);
				//��ȡ)����ַ���
				String valueS3 = valueS2.substring(value_2.length()+1);
				
				formulaS = " case when "+formulaS;//��ʽ��ת��Ϊcase when 2-3>0 TSQL֧�ֵ��﷨
				
				if(formulaS.indexOf("&&")>=0 || formulaS.indexOf("||")>=0){//����&&���ţ����ߺ��� ||����
					if(formulaS.indexOf("&&")>=0){//�ж��Ƿ���&&����
						formulaS = formulaS.replace("&&", " and ");	//�滻&&Ϊand
					} 
					if(formulaS.indexOf("||")>=0){//�ж��Ƿ���||����
						formulaS = formulaS.replace("||", " or ");//�滻||Ϊor
					}
				}
				value_1 = " then " + value_1;//ֵ1��ת��  then 2
				
				value_2 = " else " + value_2 + " end ";//ֵ2��ת�� else 3 end
				
				/***
				 * ƴ��ת������ַ�
				 * case when ��ʽ�� then ֵ1 else ֵ2 end;
				 * case when 2-3>0 then 2 else 3 end;
				 */
				String formu = formulaS+value_1+value_2+valueS3;
				
				/**
				 * ��ȡת����������ַ������ݹ����ת��
				 * �ݹ�ת��ʱ����ȡif min max�ַ�����ɴ�3���ַ���ʧ�������޷�����ת�����ʵݹ�ת��ʱ�ڴ��ַ�λ�����
				 * if min max�ַ���ת�����ٽ�ȡ�ó�3�������ַ�������ַ���ƴ��
				 */
				oriS = gatherS.substring(0,gatherS.lastIndexOf(index))+formu;
				
				return convertFormula(oriS, "", "");//�ݹ�ת��
				
			}
			if(index.equals("min") || index.equals("max")){
				/**
				 * min(ֵ1,ֵ2):ȥֵ1��ֵ2��ֵ�н�С��ֵ
				 * ������ʽΪ��min(2,3) ���Ϊ2
				 */
				//��ȡֵ1,�ַ�:2
				String value_1 = oriS.substring(oriS.indexOf("(")+1,oriS.indexOf(","));
				/**
				 * �жϸù�ʽ���Ƿ�������ű��ʽ
				 */
				value_1 = convertMathFormu(value_1);
				//��ȡ����֮��ʣ�µ��ַ���,��ȥ����,���ַ���3)
				String valueS1 = oriS.substring(value_1.length()+2);//ȥ��,
				/**
				 * �жϸù�ʽ���Ƿ�������ű��ʽ
				 */
				valueS1 = convertMathFormu(valueS1);
				//��ȡֵ2,�ַ�:3
				String value_2 = valueS1.substring(0,valueS1.indexOf(")"));
				/**
				 * �жϸù�ʽ���Ƿ�������ű��ʽ
				 */
				value_2 = convertMathFormu(value_2);
				//��ȡ��)��֮��ʣ�µ��ַ���
				String valueS2 = valueS1.substring(value_2.length()+1);
				
				String caseWhen = " case when "+value_1;//ת��ΪTSQL֧���﷨  case when 2
				//value_2 = value_2.replace(")", "");//ת��)Ϊ���ַ�
				
				String symBol = "";
				/***
				 * min(ֵ1,ֵ2)ת��Ϊ:
				 * case when ֵ1>ֵ2 then ֵ2 else ֵ1 end;
				 * case when 2>3 then 3 else 2 end;
				 */
				if(index.equals("min")){
					symBol = forMap.get(">");
				}
				/**
				 * max(ֵ1,ֵ2):ȥֵ1��ֵ2��ֵ�нϴ��ֵ
				 * ������ʽΪ��max(2,3) ���Ϊ3
				 * case when ֵ1<ֵ2 then ֵ2 else ֵ1 end;
				 * case when 2<3 then 3 else 2 end;
				 */
				if(index.equals("max")){
					symBol = forMap.get("<");
				}
				
				String formu = caseWhen+symBol+value_2+" then "+value_2+" else "+value_1+" end"+valueS2;
				
				/**
				 * ��ȡת����������ַ������ݹ����ת��
				 * �ݹ�ת��ʱ����ȡif min max�ַ�����ɴ�3���ַ���ʧ�������޷�����ת�����ʵݹ�ת��ʱ�ڴ��ַ�λ�����
				 * if min max�ַ���ת�����ٽ�ȡ�ó�3�������ַ�������ַ���ƴ��
				 */
				oriS = gatherS.substring(0,gatherS.lastIndexOf(index))+formu;
				
				return convertFormula(oriS, "", "");//�ݹ�ת��
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
	 * �жϸù�ʽ���Ƿ�������ű��ʽ��
	 * �����������ʽ�а�����()�����ʽ����Ӱ������жϣ����ڴ˴����������ʾ�������ʽ�ġ�()��
	 * ���˱��ʽ�滻Ϊָ���ַ���ת����ɺ����滻������һ�¾�����˴���
	 * Ϊ��֤����ȷ��ȡ��(����Ȼ���ٻ�ȡ��)��
	 * �˴����ַ����ָ������ָ�����֡�)����ǰ����(���ں�������Ҳ�ܱ��滻��
	 * @param valueString  ���ݵ��ַ���
	 * @return ���ش�����ɺ���ַ���
	 * @author ���Ը� 2012/12/25
	 * -----------------------------------------------------------------------------------------
	 */
	public static String convertMathFormu(String valueString){
		if(valueString==null || valueString.equals(""))
			return valueString;
		//ѭ���滻
		while(valueString.indexOf("(")>=0 && valueString.indexOf(")")>0){
			//���ַ��ָ���Ե�һ����(���ַ����ָ�
			String beforeLeft = valueString.substring(0,valueString.indexOf("("));
			//��(���ַ����µ��ַ���
			String  afterLeft = valueString.substring(valueString.indexOf("("));
			//����(���滻Ϊ��@��
			afterLeft = afterLeft.replaceFirst("\\)", "@");
			//����(���滻Ϊ��#��
			valueString = (beforeLeft+afterLeft).replaceFirst("\\(", "#");
		}
		return valueString;
	}
	
	
}