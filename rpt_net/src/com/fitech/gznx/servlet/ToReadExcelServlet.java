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
	 * ��ʹ��hibernate ���Ը� 2011-12-22
	 * Ӱ�����AfTemplate AfGatherformula AfCellinfo ValidateType MCellFormu 
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
		//��ģ��Id�Ͱ汾id��Ϊ�գ���ʹ��"�鲢��ϵ"+��ǰ��������
		if(file==null || file.equals("")){
			Calendar c=Calendar.getInstance();
			file=type+"_"+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE);
		}
		String filePath=file+".xls";
		HttpSession session = request.getSession();
		
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
		//��ֹ���ݻ���
		response.reset();
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// ������Ӧ�����͸�ʽΪ���ӱ���ʽ
		response.setContentType("application/vnd.ms-excel;charset=gb2312;name=\"" + filePath + "\""); 
		response.addHeader("Content-Disposition","attachment; filename=\""+ FitechUtil.toUtf8String(filePath) + "\"");
		response.setHeader("Accept-ranges", "bytes");
		// �����������Servlet������С�
		OutputStream os = response.getOutputStream();
		//����һ��������������ݿ��ж�ȡ���ݵ�Dao������
		CheckExcel cex= new CheckExcel();
		if(loopflg != null && loopflg.equals("1")){
			String templateType = request.getParameter("templateType") != null ? request.getParameter("templateType") : "";
			String templateName = request.getParameter("templateName") != null ? request.getParameter("templateName") : "";
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ�����AfTemplate AfGatherformula AfCellinfo ValidateType**/
			cex.createAllExcelSheeet(templateType, templateName, reportFlg,forflg);
		} else {
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ�����AfTemplate AfGatherformula AfCellinfo MCellFormu ValidateType**/
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
