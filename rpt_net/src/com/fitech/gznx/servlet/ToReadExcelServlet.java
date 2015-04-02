package com.fitech.gznx.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;

import com.fitech.gznx.util.CheckExcel;

public class ToReadExcelServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/***
	 * 已使用hibernate 卞以刚 2011-12-22
	 * 影响对象：AfTemplate AfGatherformula AfCellinfo ValidateType MCellFormu 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String templateId = request.getParameter("templateId") != null ? request.getParameter("templateId") : "";
		String versionId = request.getParameter("versionId") != null ? request.getParameter("versionId") : "";
		String forflg = request.getParameter("forflg") != null ? request.getParameter("forflg") : "";
		String loopflg = request.getParameter("loopflg") != null ? request.getParameter("loopflg") : "";
		String type=request.getParameter("type")!=null ? request.getParameter("type") : "";
		String file=templateId+versionId;
		//若模板Id和版本id都为空，则使用"归并关系"+当前日期命名
		if(file==null || file.equals("")){
			Calendar c=Calendar.getInstance();
			file=type+"_"+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE);
		}
		String filePath=file+".xls";
		HttpSession session = request.getSession();
		
		/** 报表选中标志 **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		//禁止数据缓存
		response.reset();
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// 设置响应的类型格式为电子表格格式
		response.setContentType("application/vnd.ms-excel;charset=gb2312;name=\"" + filePath + "\""); 
		response.addHeader("Content-Disposition","attachment; filename=\""+ FitechUtil.toUtf8String(filePath) + "\"");
		response.setHeader("Accept-ranges", "bytes");
		// 将数据输出到Servlet输出流中。
		OutputStream os = response.getOutputStream();
		//这是一个用来处理从数据库中读取数据的Dao操作类
		CheckExcel cex= new CheckExcel();
		if(loopflg != null && loopflg.equals("1")){
			String templateType = request.getParameter("templateType") != null ? request.getParameter("templateType") : "";
			String templateName = request.getParameter("templateName") != null ? request.getParameter("templateName") : "";
			/**已使用hibernate 卞以刚 2011-12-22
			 * 影响对象：AfTemplate AfGatherformula AfCellinfo ValidateType**/
			cex.createAllExcelSheeet(templateType, templateName, reportFlg,forflg);
		} else {
			/**已使用hibernate 卞以刚 2011-12-22
			 * 影响对象：AfTemplate AfGatherformula AfCellinfo MCellFormu ValidateType**/
			cex.createExcelSheeet(templateId, versionId, forflg,reportFlg);
		}		
		cex.exportExcel(os);
  
		os.flush();
		os.close();

	}


	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

}
