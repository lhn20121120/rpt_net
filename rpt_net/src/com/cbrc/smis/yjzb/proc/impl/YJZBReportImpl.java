package com.cbrc.smis.yjzb.proc.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.cbrc.smis.yjzb.bean.AbnormityResult;
import com.cbrc.smis.yjzb.bean.Abnormity_actu_standard;
import com.cbrc.smis.proc.impl.Report;
import com.cbrc.smis.proc.impl.ReportImpl;
/**
 * ��ȡ�쳣��Ԫ��ֵ
 * @author jhb
 *
 */
public class YJZBReportImpl {	
	/**
	 * ��
	 */
	public static final int  month=1;
	/**
	 * ��
	 */
	public static final int  season=2;
	/**
	 * ����
	 */
	public static final int  half_year=3;
	/**
	 * ��
	 */
	public static final int  years=4;
	/**
	 * ������
	 */
	public static final int  times=1;
	/**
	 * Ĭ�ϱ���С��λ
	 */
	public static final int DEFAULTSCALE=4;
	/**
	 * Ĭ���������ֵ
	 */
	public static final String MAXNUMBER="��"; 
	/**
	 * ��ʽID
	 */
	public static final int EXP_B_ID=1;   //B�๫ʽID
	public static final int EXP_SAMETERM_ID=6;  //|A1-C0|/C0
   /**
    * ��ñ��ڱ����쳣��Ԫ���ֵ
    * 
    * @param  conn
    * @param  Integer repInId ʵ���ֱ���ID
    * @param  String childRepId �ֱ���ID
    * @param  String versionId  �汾ID
    * @param  String cellName   ��Ԫ������
    * @return String �쳣��Ԫ�����ֵ
    * @throws Exception
    */
	public static String getCellValue(Connection conn,int repInId,String childRepId,String versionId,String cellName)
	throws Exception{
		String value="0";
		if(conn==null || repInId<=0 || childRepId==null || versionId==null || cellName==null) return value;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try{
			
			String sql="select rii.report_value from report_in_info rii where rii.rep_in_id=?"+
			" and rii.cell_id=(select mc.cell_id from m_cell mc where mc.child_rep_id=? and " +
			" mc.version_id=? and mc.cell_name=?)";
			stmt=conn.prepareStatement(sql.toUpperCase());
			stmt.setInt(1,repInId);
			stmt.setString(2,childRepId);
			stmt.setString(3,versionId);
			stmt.setString(4,cellName);
		
			rs=stmt.executeQuery();
			if(rs!=null && rs.next()){
				value=rs.getString("report_value".toUpperCase());
				if(ReportImpl.isPercentCell(conn,childRepId,versionId,cellName)==true){
					value=format(Double.parseDouble(value)/100);
				}
			}
		}catch(Exception e)
		{
			value="0";
		  	throw new Exception(e.getMessage());
		}finally{
			if(stmt!=null)stmt.close();
			if(rs!=null)rs.close();
		}
		if(value.trim().equals(""))value="0";
		return value;
	}
	
	
	/**
	 * 
	 * ������ڱ����쳣��Ԫ���ֵ
	 * 
	 * @param conn 
	 * @param childRepId �ֱ���ID
	 * @param rep_freq_id Ƶ��
	 * @param versionId   �汾
	 * @param cellName    ��Ԫ������
	 * @param year        ��
	 * @param term        ����
	 * @param orgid       ����ID
	 * @param dataRangeId ���ݷ�Χ
	 * @return String �����쳣��Ԫ�����ֵ
	 * @throws Exception
	 */
	public static String getOldCellValue(Connection conn,String childRepId,Integer rep_freq_id,String versionId,String cellName,Integer year,Integer term , 
			             String orgid,Integer dataRangeId, Integer cur_Id) throws Exception{
		String value="0";
		
		if(conn==null ||  rep_freq_id==null || childRepId==null || versionId==null || 
		  cellName==null || year==null || term==null || orgid==null || dataRangeId==null || cur_Id==null) return value;
		
		PreparedStatement stmt=null;
		ResultSet rs=null;
		int Year=year.intValue();
		int Term=term.intValue();
		int repfreqid=rep_freq_id.intValue();
		
		try{
				if( repfreqid==month) //�±�
				{
					if(Term==1){
						Year=Year-1;
						Term=12;
					}
					else{
						Term=Term-1;
					}
					
				}
				if(repfreqid==season) //����
				{   
					if(Term==3){
						Year=Year-1;
						Term=12;
					}
					else
					{
						Term=Term-3;
					}	
				}
				if(repfreqid==half_year) //���걨
				{
					if(Term==6){
						Year=Year-1;
						Term=12;
					}
					else
					{
						Term=Term-6;
					}	
				}
				if(repfreqid==years) //�걨
				{
					Year=Year-1;
					Term=12;
				}
			String sql="select rii.report_value from report_in_info rii where rii.rep_in_id=(select rep.rep_in_id from report_in rep where ".toUpperCase()+
			 "  rep.child_rep_id=? and rep.version_id=? and rep.year=? and rep.term=? and rep.org_id=? and rep.data_range_id=? and rep.cur_id=?".toUpperCase() + 
			 "  and (rep.check_flag=".toUpperCase() +  Report.CHECK_FLAG_OK + ") and " +
			 " rep.is_max_times=".toUpperCase()+times+")"+
			 "  and rii.cell_id=(select mc.cell_id from m_cell mc where mc.child_rep_id=? and ".toUpperCase() +
			 "  mc.version_id=? and mc.cell_name=?)".toUpperCase();
			
			stmt=conn.prepareStatement(sql);
			stmt.setString(1,childRepId);
			stmt.setString(2,versionId);
			stmt.setInt(3,Year);
			stmt.setInt(4,Term);
			stmt.setString(5,orgid);
			stmt.setInt(6,dataRangeId.intValue());
			stmt.setInt(7,cur_Id.intValue());
			stmt.setString(8,childRepId);
			stmt.setString(9,versionId);
			stmt.setString(10,cellName);
			rs=stmt.executeQuery();
			if(rs!=null && rs.next()){
				value=rs.getString("report_value".toUpperCase());
				if(ReportImpl.isPercentCell(conn,childRepId,versionId,cellName)==true){
					value=format(Double.parseDouble(value)/100);
				}
			}			
		}catch(Exception e)
		{
			value="0";
		  	throw new Exception(e.getMessage());
		}finally{
			if(stmt!=null)stmt.close();
			if(rs!=null)rs.close();
		}
		if(value.trim().equals("")) value="0";
		return value;
	}
	
	/**
	 * 
	 * ��ȡ����ͬ��ֵ
	 * 
	 * @param conn Connection ���ݿ����� 
	 * @param childRepId  String ������
	 * @param versionId   String �汾
	 * @param orgId       String ����ID
	 * @param repFreqId   Integer Ƶ�� 
	 * @param cellName    String ��Ԫ������
	 * @param year        Integer ��
	 * @param term        Integer ���� 
	 * @param dataRangeId Integer ���ݷ�Χ
	 * @return String �����쳣��Ԫ�����ֵ
	 * @throws Exception
	 */
	public static String getPreYearSameTermCellValue(Connection conn,
			String childRepId,
			String versionId,
			String orgId,
			Integer dataRangeId,
			Integer repFreqId,			
			String cellName,
			Integer year,
			Integer term,			
			Integer curId) throws Exception{
		String value="0";
		
		if(conn==null ||  repFreqId==null || childRepId==null || versionId==null || 
		  cellName==null || year==null || term==null || orgId==null || dataRangeId==null ||
		  curId==null) return value;
		
		PreparedStatement stmt=null;
		ResultSet rs=null;
				
		try{				
			String sql="select rii.report_value from report_in_info rii where rii.rep_in_id=(select rep.rep_in_id from report_in rep where ".toUpperCase()+
			 "  rep.child_rep_id=? and rep.version_id=? and rep.year=? and rep.term=? and rep.org_id=? and rep.data_range_id=? and rep.cur_id=?".toUpperCase() + 
			 "  and (rep.check_flag=".toUpperCase() +  Report.CHECK_FLAG_OK + ") and " +
			 " rep.is_max_times=".toUpperCase()+times+")"+
			 "  and rii.cell_id=(select mc.cell_id from m_cell mc where mc.child_rep_id=? and ".toUpperCase() +
			 "  mc.version_id=? and mc.cell_name=?)".toUpperCase();
			
			stmt=conn.prepareStatement(sql);
			stmt.setString(1,childRepId);
			stmt.setString(2,versionId);
			stmt.setInt(3,year.intValue()-1);
			stmt.setInt(4,term.intValue());
			stmt.setString(5,orgId);
			stmt.setInt(6,dataRangeId.intValue());
			stmt.setInt(7,curId.intValue());
			stmt.setString(8,childRepId);
			stmt.setString(9,versionId);
			stmt.setString(10,cellName);
			rs=stmt.executeQuery();
			if(rs!=null && rs.next()){
				value=rs.getString("report_value".toUpperCase());
				if(ReportImpl.isPercentCell(conn,childRepId,versionId,cellName)==true){
					value=format(Double.parseDouble(value)/100);
				}
			}			
		}catch(Exception e)
		{
			value="0";
		  	throw new Exception(e.getMessage());
		}finally{
			if(stmt!=null)stmt.close();
			if(rs!=null)rs.close();
		}
		if(value.trim().equals("")) value="0";
		return value;
	}
	
	/**
	 * ������ںͱ��ڵĽ��ֵ
	 * 
	 * @param Exp_ID ���㹫ʽID
	 * @param newvalue double ���ڵĵ�Ԫ��ֵ
	 * @param oldvalue double���ڵĵ�Ԫ��ֵ
	 * @return value ���ֵ
	 * @throws Exception
	 */
	public static String getResultValue(double newvalue,double oldvalue,Integer Exp_Id) throws Exception{
		double value=0;
		String values="0";
		double divisor=1.0d,tmpValue=0;

		int exp_id=Exp_Id.intValue();
		
		try{
			switch(exp_id){
				case 1:
					value=newvalue;
					values=format(value);
					break;
				case 2:
					tmpValue=newvalue-oldvalue;
					if(oldvalue==0){ 
						return tmpValue!=0?MAXNUMBER:values;
					}
					value=Math.abs(tmpValue)/oldvalue;
					values=round(value,divisor,DEFAULTSCALE);
					break;
				case 3:
					 value=newvalue-oldvalue;
					 values=format(value);
				 	 break;
				case 4:
					 tmpValue=newvalue-oldvalue;
					 if(oldvalue==0){ 
						return tmpValue!=0?MAXNUMBER:values;
					 }
					 value=tmpValue/oldvalue;
					 values=round(value,divisor,DEFAULTSCALE);
					 break;
				case 5:
					 tmpValue=newvalue-oldvalue;
					 value=Math.abs(tmpValue);
					 values=format(value);
					 break;
				case 6:
					 tmpValue=newvalue-oldvalue;
					 if(oldvalue==0){ 
						return tmpValue!=0?MAXNUMBER:values;
					 }
					 value=Math.abs(tmpValue)/oldvalue;
					 values=round(value,divisor,DEFAULTSCALE);
					 break;
			 }			 
		}catch(Exception e){
			values="0";
			throw new Exception(e.getMessage());
		}

		return values;
	}
	
	public static String getResultValue(String strNewVal,String strOldVal,Integer Exp_Id) throws Exception{
		String values="0";
		if(strNewVal==null || strNewVal.equals("")) return values;
		
		double value=0;
		double divisor=1.0d,tmpValue=0;

		int exp_id=Exp_Id.intValue();
		
		try{
			double newValue=Double.parseDouble(strNewVal);
			double oldValue=strOldVal!=null && !strOldVal.equals("")?Double.parseDouble(strOldVal):0;
			switch(exp_id){
				case 1:
					value=newValue;
					values=format(value);
					break;
				case 2:
					tmpValue=newValue-oldValue;
					if(oldValue==0){ 
						return tmpValue!=0?MAXNUMBER:values;
					}
					value=Math.abs(tmpValue)/oldValue;
					values=round(value,divisor,DEFAULTSCALE);
					break;
				case 3:
					 value=newValue-oldValue;
					 values=format(value);
				 	 break;
				case 4:
					 tmpValue=newValue-oldValue;
					 if(oldValue==0){ 
						return tmpValue!=0?MAXNUMBER:values;
					 }
					 value=tmpValue/oldValue;
					 values=round(value,divisor,DEFAULTSCALE);
					 break;
				case 5:
					 tmpValue=newValue-oldValue;
					 value=Math.abs(tmpValue);
					 values=format(value);
					 break;
				case 6:
					 tmpValue=newValue-oldValue;
					 if(oldValue==0){ 
						return tmpValue!=0?MAXNUMBER:values;
					 }
					 value=Math.abs(tmpValue)/oldValue;
					 values=round(value,divisor,DEFAULTSCALE);
					 break;
			 }			 
		}catch(Exception e){
			values="0";
			throw new Exception(e.getMessage());
		}

		return values;
	}
	
	/**
	 * ���뵥Ԫ������
	 * 
	 * @param  conn
	 * @param  abnormityresult Ԥ��ָ��������
	 * @return flag          ״̬��־
	 * @throws Exception
	 */
	public static boolean setValue(Connection conn,AbnormityResult abnormityresult) throws Exception{
		boolean flag=true;

		int list=0;
		if(conn==null || abnormityresult==null)return false;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		int count=0;
		
		try{
			String selectsql="select count(*) as total from abnormity_result where rep_in_id=? and  as_id=?".toUpperCase();
			stmt=conn.prepareStatement(selectsql);
			stmt.setInt(1,abnormityresult.getRepInId().intValue());
			stmt.setInt(2,abnormityresult.getAs_id().intValue());
			rs=stmt.executeQuery();
			if(rs!=null && rs.next()) count=rs.getInt("total".toUpperCase());
			
			if(count==0){
				String sql="insert into abnormity_result(current_value,previou_value,result_value,rep_in_id,as_id,exp_id,flag,face_value)values(" +
						"?,?,?,?,?,?,?,?)";
				
				stmt=conn.prepareStatement(sql.toUpperCase());

				stmt.setString(1, abnormityresult.getCurrent_value());
				stmt.setString(2, abnormityresult.getPreviou_value());
				stmt.setString(3, abnormityresult.getResult_value());

				stmt.setString(1, String.valueOf(abnormityresult.getCurrent_value()));
				stmt.setString(2, String.valueOf(abnormityresult.getPreviou_value()));
				stmt.setString(3, String.valueOf(abnormityresult.getResult_value()));

				stmt.setInt(4,abnormityresult.getRepInId().intValue());
				stmt.setInt(5,abnormityresult.getAs_id().intValue());
				stmt.setInt(6,abnormityresult.getExp_id().intValue());
				stmt.setInt(7,abnormityresult.getFlag().intValue());
				stmt.setString(8,abnormityresult.getFace_value());
			
				list=stmt.executeUpdate();
				
			}else
			{
				String sql="update abnormity_result set current_value=?,previou_value=?,result_value=?,exp_id=?,flag=?,face_value=? where"
					+" rep_in_id=? and as_id=?";
				
				stmt=conn.prepareStatement(sql.toUpperCase());				

				stmt.setString(1, abnormityresult.getCurrent_value());
				stmt.setString(2, abnormityresult.getPreviou_value());
				stmt.setString(3, abnormityresult.getResult_value());

				stmt.setString(1, String.valueOf(abnormityresult.getCurrent_value()));
				stmt.setString(2, String.valueOf(abnormityresult.getPreviou_value()));
				stmt.setString(3, String.valueOf(abnormityresult.getResult_value()));

				stmt.setInt(4,abnormityresult.getExp_id().intValue());
				stmt.setInt(5,abnormityresult.getFlag().intValue());
				stmt.setString(6,abnormityresult.getFace_value());
				stmt.setInt(7,abnormityresult.getRepInId().intValue());
				stmt.setInt(8,abnormityresult.getAs_id().intValue());
				list=stmt.executeUpdate();
			}
			
		}catch(Exception e){
			flag=false;
			throw new Exception(e.getMessage());
		}finally{
			if(list!=0){
				conn.commit();
			}else {
				conn.rollback();
			}
			if(stmt!=null) stmt.close();
			if(rs!=null) rs.close();
			
		}
		return flag;
	}
	
	/**
	 * �õ���ͬ������ҵ��������ݵ�repInId,�����ڿ��ʽ��У��
	 * 
	 * @param Connection conn
	 * @param Abnormity_actu_standard aas
	 * @param String childRepId
	 * @return int repInId
	 */
	public static int getRepInId(Connection conn,Abnormity_actu_standard aas,String childRepId)throws Exception{
		int _repInId=-1;
		if(conn==null || aas==null || childRepId==null) return _repInId;

		Statement stmt=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		ResultSet rs2=null;
		String sql="select version_id,org_id,data_range_id,year,term,cur_id from report_in in where in.rep_in_id=" ;
		sql=sql.toUpperCase()+ aas.getRepInId();

		String sql2="select REP_IN_ID from REPORT_IN in where in.CHILD_REP_ID=? and " +
				    " VERSION_ID=? and ORG_ID=? and DATA_RANGE_ID=? and YEAR=?"+
				    " and TERM=? and CUR_ID=? and TIMES=1 and (CHECK_FLAG=? or CHECK_FLAG=?)";
		try{
			 stmt=conn.createStatement();
			 rs=stmt.executeQuery(sql);
			 pstmt=conn.prepareStatement(sql2.toUpperCase());
		     
		     if(rs!=null && rs.next()){
		    	 /*// System.out.println("select rep_in_id from report_in in where in.child_rep_id='" + childRepId + 
		    		"' and " +
				    " version_id='" + rs.getString("version_id") + "' and org_id='" + rs.getString("org_id") + 
				    "' and data_range_id=" + rs.getInt("data_range_id") + 
				    " and year=" + rs.getInt("year") + 
				    " and term=" + rs.getInt("term") + 
				    " and cur_id=" + rs.getInt("cur_id") + 
				    " and is_max_times=1 and (check_flag=" + Report.CHECK_FLAG_OK + " or check_flag=" + Report.CHECK_FLAG_UN + ")");*/
		    	 
		    		 pstmt.setString(1,childRepId);
		    		 pstmt.setString(2,rs.getString("VERSION_ID"));
		    		 pstmt.setString(3,rs.getString("ORG_ID"));
		    		 pstmt.setInt(4,rs.getInt("DATA_RANGE_ID"));
		    		 pstmt.setInt(5,rs.getInt("YEAR"));
		    		 pstmt.setInt(6,rs.getInt("TERM"));
		    		 pstmt.setInt(7,rs.getInt("CUR_ID"));
		    		 pstmt.setInt(8,Report.CHECK_FLAG_OK);
		    		 pstmt.setInt(9,Report.CHECK_FLAG_UN);
		    	 
		    	     rs2=pstmt.executeQuery();
		    	     if(rs2!=null && rs2.next()){
		    		     _repInId=rs2.getInt("REP_IN_ID");
		    	      }
		     }
		}catch(Exception e){
			_repInId=-1;
			throw new Exception(e.getMessage());
		}finally{
			if(rs!=null) rs.close();
			if(stmt!=null) stmt.close();
			if(rs2!=null) rs2.close();
			if(pstmt!=null) pstmt.close();
		}
		
		return _repInId;
	}

	
  /**
   * ��õ�Ԫ����쳣ָ��
   * 
   * @param conn  ���ݿ�����
   * @param abnormity_actu_standard �쳣ָ�����
   * @param orgid     ����ID
   * @return standard Ԥ��ָ��
   * @throws Exception
   */
	public static String getstandard(Connection conn,Abnormity_actu_standard abnormity_actu_standard,String orgid) throws Exception{
		String standard=null;
		int as_id;
		as_id=abnormity_actu_standard.getAs_id().intValue();
		if(conn==null || abnormity_actu_standard==null || orgid==null)return null;
		
		 PreparedStatement pstmt=null;
		 ResultSet rs=null;
		 try{			 
			String sql="select as_standard from abnormity_actu_standard where as_id=" +
					"(select as_id from ab_rep_org_relation where as_id=? and orgid=?)";

		    pstmt=conn.prepareStatement(sql.toUpperCase());
		    pstmt.setInt(1,as_id);
		    pstmt.setString(2,orgid);
		    rs=pstmt.executeQuery();
			if(rs!=null && rs.next()){
				standard=rs.getString("as_standard".toUpperCase());
			}
		 }catch(Exception e){
			 standard=null;
			 new Exception(e.getMessage());
		 }finally{
			 if(pstmt!=null)pstmt.close();
			 if(rs!=null)rs.close();
		 }
		 
		 return standard;
	}
	
	/**
	 * ����쳣��Ԫ���ֵ�Ƿ��������ķ�Χ֮��<br>
	 * 1:��ʾ������׼;0:��ʾ�������ķ�Χ֮��;-1:ϵͳ����
	 * 
	 * @param  value     String ���ֵ
	 * @param  colFormu  String �쳣��׼
	 * @return flag      int ״̬
	 * @throws Exception
	 */
	 public static int operation(String resVal, String colFormu) throws Exception {
		if((resVal==null || resVal.equals("")) || (colFormu==null || colFormu.equals("")))return -1;
		
		if(resVal.trim().equals(MAXNUMBER)) return 1;
		
		int flag = 0; //״̬   //�ж��ǲ��ǳ�����׼
		
		double value=Double.parseDouble(resVal);
		colFormu=colFormu.trim();
		
		String[] arr = null; //��һ�ζԹ�ʽ����������
		String title = null; //��һ�ν������֮��õ��Ľ��ֵ
		String operator1 = null; //%���ŵı�����ʽ����%����100/û��%����1/
		String arr1[] = null; //�ڶ��ζԹ�ʽ����������
		String operator = null; //�������
	
		String value1 = null; //���ֵ����%ʱ
		double values = 0; //���ձȽϵĽ��ֵ
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split(",");
			operator = ",";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split("<>");
			operator = "!=";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split(">=");
			operator = ">=";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split("<=");
			operator = "<=";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split(">");
			operator = ">";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split("<");
			operator = "<";
		}
		if (arr == null || arr.length <= 1) {
			arr = colFormu.split("=");
			operator = "=";
		}
		//������ʽ������ʽ
		if (operator.equals(",")) {
			for (int i = 0; i < arr.length; i++) {
				flag = GetResult.getCompare(value, arr[i]);
				if (flag == 1 || flag==-1) {
					break;
				}
			}
		}
		//�������ʽ������ʽ
		else if (operator.equals(">=")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}
			if (operator.equals(">=")) {
				if (operator1.equals("/100")) {
					if (value >= values / 100) {
						flag = 1;

					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value >= values / 1) {
						flag = 1;

					} else {
						flag = 0;
					}
				}
			}
		} else if (operator.equals("<=")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}
			if (operator.equals("<=")) {
				if (operator1.equals("/100")) {
					if (value <= values / 100) {
						flag = 1;
					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value <= values / 1) {
						flag = 1;
					} else {
						flag = 0;
					}
				}
			}
		} else if (operator.equals("!=")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}

			if (operator.equals("!=")) {
				if (operator1.equals("/100")) {
					if (value != values / 100) {
						flag = 1;
					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value != values / 1) {
						flag = 1;
					} else {
						flag = 0;
					}
				}
			}
		} else if (operator.equals("=")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}
			if (operator.equals("=")) {
				if (operator1.equals("/100")) {
					if (value == values / 100) {
						flag = 1;

					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value == values / 1) {
						flag = 1;

					} else {
						flag = 0;
					}
				}
			}

		} else if (operator.equals(">")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}
			if (operator.equals(">")) {
				if (operator1.equals("/100")) {
					if (value > values / 100) {
						flag = 1;
					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value > values / 1) {
						flag = 1;
					} else {
						flag = 0;
					}
				}
			}
		} else if (operator.equals("<")) {
			for (int i = 0; i < arr.length; i++) {

				if (!arr[i].equals(""))
					title = arr[i];
			}
			if (title != null) {
				if (arr1 == null || arr1.length <= 1) {
					int index = title.indexOf("%");
					if (index > 0) {
						arr1 = title.split("%");
						operator1 = "/100";
					} else {
						value1 = title.trim();
						operator1 = "/1";
					}
				}
			}
			if (value1 != null) {
				values = Double.parseDouble(value1);
			} else {
				for (int i = 0; i < arr1.length; i++) {
					if (arr1[i] != "") {
						values = Double.parseDouble(arr1[i]);
					}
				}
			}
			if (operator.equals("<")) {
				if (operator1.equals("/100")) {
					if (value < values / 100) {
						flag = 1;

					} else {
						flag = 0;
					}
				} else if (operator1.equals("/1")) {
					if (value < values / 1) {
						flag = 1;

					} else {
						flag = 0;
					}
				}
			}
		}
		return flag;
	}
	 
	 /**
	  * ת�����ֵΪչʾ����ʽ
	  * 
	  * @param value  double ��Ԫ����ֵ
	  * @param formu  String ��Ӧ��Ԥ��ָ��
	  * @return face_value string չʾ��ҳ���ֵ
	  * @throws Exception
	  */	 
	public static String getFace_value(String  resVal,String formu){
			String sValue="";
			
			if((resVal==null || resVal.equals("")) || (formu==null || formu.equals(""))) return sValue;
			if(resVal.trim().equalsIgnoreCase(MAXNUMBER)) return MAXNUMBER;
				
			formu=formu.trim();
			
			int index;
			try{
				StringBuffer svalues=null;				
				double value=Double.parseDouble(resVal);
				index=formu.indexOf("%");
				if(index>0){
					value=value*100;
					sValue=format(value);
					svalues=new StringBuffer(sValue);
					svalues.append("%");
					sValue=svalues.toString();		
				}else{
					sValue=format(value);	
				}
			}catch(Exception e){
				sValue="";
			}
			
			return sValue;
	}
	
	/**
	 * ��ʽ����ֵ
	 * 
	 * @param value double ��ֵ
	 * @return String
	 */
	public static String format(double value){
		try{
			java.text.DecimalFormat dFormat=new java.text.DecimalFormat("0.##########");
			return dFormat.format(value);
		}catch(Exception e){
			return "";
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
	public static String round(double dividend,double divisor,int scale){  
	   try{			
			BigDecimal bdDividend=new BigDecimal(dividend);
			BigDecimal bdVivisor=new BigDecimal(divisor);
			
			return bdDividend.divide(bdVivisor,scale,BigDecimal.ROUND_HALF_UP).toString();
		}catch(Exception e){
			return "0";
		}
	} 
}