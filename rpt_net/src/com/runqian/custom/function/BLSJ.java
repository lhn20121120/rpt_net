package com.runqian.custom.function;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.runqian.base4.resources.EngineMessage;
import com.runqian.base4.resources.MessageManager;
import com.runqian.base4.util.ReportError;
import com.runqian.report4.model.expression.Expression;
import com.runqian.report4.model.expression.Function;
import com.runqian.report4.usermodel.Context;

public class BLSJ extends Function {
	public Object calculate(Context ctx, boolean isInput) // 标准接口，ctx为运算环境，isInput为是否填报
	{
		// 得到获得的参数数据
		// 判断自定义函数接收到得参数个数，如果个数为0，则抛出异常信息
		if (this.paramList.size() < 1) {
			MessageManager mm = EngineMessage.get();
			throw new ReportError("paramater:" + mm.getMessage("function.missingParam"));
		}

		/*
		 * 取得第一个参数，默认为表达式，需要把该表达式算出来，结果才是函数的参数值
		 */
		// 取得第一个参数的表达式param1
		Expression param0 = (Expression) this.paramList.get(0);
		Expression param1 = (Expression) this.paramList.get(1);
		String template = param0.toString();
		String orgId = (String)ctx.getParamValue("OrgID");
		String repDate = (String)ctx.getParamValue("ReptDate");
		String freqId = (String)ctx.getParamValue("Freq");
		String cellName = param1.toString();
		String col = cellName.replaceFirst("[0-9]", "");
		String row = cellName.replaceFirst("[A-Z]", "");
		String col_str = "COL" + this.getCnNameColId(col);
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer("");
		if(row.equals(""))
			sql.append("select  sum(a." + col_str + ") as rs");
		else
			sql.append("select  max(a." + col_str + ") as rs");
		sql.append(" from af_qd_" + template + " a");
		sql.append(" left join (select af.*,t.template_name from AF_REPORT af join af_template t on af.template_id=t.template_id and af.version_id=t.version_id) b");
		sql.append(" on a.REP_ID = b.REP_ID");
		sql.append(" where b.ORG_ID ='" + orgId + "' and");
		sql.append(" to_char(last_day(to_date(to_char(b.year) || trim(to_char(b.term,'00')),'yyyymm')),'yyyymmdd')='" + repDate + "' and");
		sql.append(" b.rep_freq_id=" + freqId + " and b.template_id=upper('" + template + "')");
		if(!row.equals(""))
			sql.append(" and a.row_id='" + row + "'");
		System.out.println("sql:" + sql.toString());
		Double result = 0.00;
		try{
			rs =  ctx.getConnectionFactory("rsmis").getConnection().createStatement().executeQuery(sql.toString());
			if(rs.next())
				result = rs.getDouble("rs");
			System.out.println("result=" + result);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return result;
	}
	public String getCnNameColId(String colName){
		String [] strs = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}; 
		String[] colNames = colName.split("\\+");
		StringBuffer sb = new StringBuffer("");
		for(int m=0;m<colNames.length;m++){
			int temp = 1;
			for(int i=0;i<strs.length;i++){
				if(colNames[m].equals(strs[i])){
					temp = i;
					break;
				}
			}
			sb.append("+" + String.valueOf(temp));
		}
		String result = sb.length()>1 ? sb.substring(1) : colName;
		result = "" + (Integer.parseInt(result) + 1);
		return result;
	}
}