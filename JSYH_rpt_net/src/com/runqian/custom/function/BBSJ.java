package com.runqian.custom.function;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.runqian.base4.resources.EngineMessage;
import com.runqian.base4.resources.MessageManager;
import com.runqian.base4.util.ReportError;
import com.runqian.report4.model.expression.Expression;
import com.runqian.report4.model.expression.Function;
import com.runqian.report4.usermodel.Context;

public class BBSJ extends Function {
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
		Object freqId = ctx.getParamValue("Freq");
		Integer point = (Integer)ctx.getParamValue("Point");
		point = 2;
		String cellName = param1.toString();
		Connection con = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer("");
	//	sql.append(" select round(sum(case when number_varchar(report_value)=1 then to_number(report_value) else 0.00 end )/10000," + point + ") as rs from (");
		sql.append(" select round(sum(case when number_varchar(report_value)=1 then to_number(report_value) else 0.00 end )," + point + ") as rs from (");
		sql.append(" select ri.org_id,af.child_rep_id,af.version_id,af.cell_name,ri.year,ri.term,ri.data_range_id,rii.report_value ");
		sql.append(" from kerong.report_in ri join kerong.m_cell af on ri.child_rep_id=af.child_rep_id and ri.version_id=af.version_id");
		sql.append(" join kerong.report_in_info rii on af.cell_id=rii.cell_id and ri.rep_in_id=rii.rep_in_id");
		sql.append(" where ri.org_id='" + orgId + "' and to_char(last_day(to_date(to_char(ri.year) || trim(to_char(ri.term,'00')),'yyyymm')),'yyyymmdd')='" + repDate + "'");
		sql.append(" and ri.data_range_id=1 and ri.times=1");
		sql.append(" union");
		sql.append(" select ri.org_id,af.child_rep_id,af.version_id,af.cell_name,ri.year,ri.term,ri.data_range_id,rii.report_value ");
		sql.append(" from cunzhen.report_in ri join cunzhen.m_cell af on ri.child_rep_id=af.child_rep_id and ri.version_id=af.version_id");
		sql.append(" join cunzhen.report_in_info rii on af.cell_id=rii.cell_id and ri.rep_in_id=rii.rep_in_id");
		sql.append(" where to_char(last_day(to_date(to_char(ri.year) || trim(to_char(ri.term,'00')),'yyyymm')),'yyyymmdd')='20120630'");
		sql.append(" and ri.data_range_id=1 and ri.times=1) where child_rep_id=upper('" + template + "') and cell_name=upper('" + cellName + "')");
		sql.append(" group by child_rep_id,cell_name,year,term,data_range_id");
		System.out.println("sql:" + sql.toString());
		Double result = 0.00;
		try{
			con = ctx.getConnectionFactory("rsmis").getConnection();
			rs =  con.createStatement().executeQuery(sql.toString());
			if(rs.next())
				result = rs.getDouble("rs");
			System.out.println("result=" + result);
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(con!=null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return result;
	}
}
