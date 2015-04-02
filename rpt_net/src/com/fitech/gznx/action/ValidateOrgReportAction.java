package com.fitech.gznx.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.po.AfReport;
import com.fitech.gznx.service.AFReportDelegate;
import com.fitech.gznx.service.AFValiRelationDelegate;
import com.fitech.gznx.service.OrgValiInfoDelegate;
import com.fitech.net.config.Config;

public class ValidateOrgReportAction extends Action {
	private FitechException log = new FitechException(
			ValidateNXOnLineReportAction.class);
	
	/***
	 * 有oracle语法(nextval) 需要修改 卞以刚 2011-12-26
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);

		boolean resultHZ=true;
		List<Map<String,String>> resultList= new ArrayList<Map<String,String>>();
		Integer repInId = null;
		String failedReportInIds = "";
		

		HttpSession session = request.getSession();

		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
					.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		// 取得模板类型
		Integer templateType = null;
		if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
			templateType = Integer.valueOf(session.getAttribute(
					com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());

		String[]  repIds=((String)request.getParameter("repInIds")).split(",");
		System.out.println((String)request.getParameter("repInIds"));
        Connection orclCon = null;
        for(int i=0;i<repIds.length;i++){
             try {
                String repId=repIds[i];
                repInId = new Integer(repId);
                System.out.println("=====****报表ID值****====="+repId);
	            orclCon = (new com.cbrc.smis.proc.jdbc.FitechConnection()).getConnect();
	            AfReport reportIn = AFReportDelegate.getReportIn(repInId.longValue());
	            List<Map<String,String>> cellMap = AFValiRelationDelegate.parseCell(orclCon, reportIn, templateType);
	            //如果填报的单元格集合没有数据
                if(cellMap == null || cellMap.isEmpty()) {
                	PrintWriter out = response.getWriter();

        			response.setContentType("text/xml");
        			response.setHeader("Cache-control", "no-cache");
        			String result = "noData";
        			out.println("<response><result>" + result + "</result></response>");
        			out.close();
        			return null;
                } 
                System.out.println(reportIn); 
                String orgRepIds=repId;
                String orgSonRepIds="";
	            Map org_rep =AFValiRelationDelegate.getSonReq(orclCon, reportIn, templateType);
	            Iterator it=org_rep.entrySet().iterator();
	            while(it.hasNext()){
	            	Entry entry=(Entry)it.next();
	            	orgRepIds+=","+entry.getValue();
	            	orgSonRepIds+=","+entry.getValue();
	            }
	            /**2、获取该报表的所有子机构报表 **/ 
	            if(org_rep.size()>0){
	            	resultList=resultHZ(orclCon,cellMap,org_rep,reportIn,orgRepIds);
	            }
	            if(resultList.size()>0){
	            	resultHZ=resultHZ&&false;
	            }
	            OrgValiInfoDelegate.add(resultList, reportIn,repId);
				// System.out.println(repInId + " 校验结束==" + new Date()); 
				
			} catch (Exception e) {
				e.printStackTrace(); 
			}finally {
                try {
                    if(null != orclCon)
                        orclCon.close();
                } catch (SQLException e) {
                    //e.printStackTrace();
                }
            }
		 }

			PrintWriter out = response.getWriter();

			response.setContentType("text/xml");
			response.setHeader("Cache-control", "no-cache");
			boolean result = true;
			if (resultHZ){
				AfReport reportIn;
				try {
					reportIn = AFReportDelegate.getReportIn(repInId.longValue());
					FitechLog.writeLog(com.cbrc.smis.common.Config.LOG_OPERATION,operator.getUserName(),
							"报表[编号:" + reportIn.getTemplateId() 
	                        + ",版本号:" + reportIn.getVersionId() 
	                        + "]校验通过!");
				} catch (Exception e) {
					e.printStackTrace();
				}
				result = true;
			}
			else
				result = false;
			out.println("<response><result>" + result + "</result></response>");
			out.close();
 

		return null;
	}
	/**
	 * 2012-09-14 孙大为
	 * @param 校验子结构
	 * @param report_in
	 * @return
	 * @throws Exception 
	 */
	
	public List<Map<String,String>> resultHZ(Connection orclCon ,List<Map<String,String>> cellMap,Map org_rep,AfReport reportIn,String repids) throws Exception{
		boolean result=false;
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>() ;
		List<ArrayList<Map<String,String>>> dataList=new ArrayList<ArrayList<Map<String,String>>>();  
		AfReport afReport=reportIn;
		Iterator it=org_rep.entrySet().iterator();
		while(it.hasNext()){
			java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
			afReport.setRepId((Long)entry.getValue());
			//获取同一张报表每个机构的值
			ArrayList<Map<String,String>> org_map=(ArrayList<Map<String, String>>) AFValiRelationDelegate.parseCell(orclCon, afReport, 2);
			//System.out.println(org_map.size());
			dataList.add(org_map); 
		}
		for(Map<String,String> cellmap:cellMap){
			float data=new Float(0); 
			//求此单元格子机构相加的值
			for (int i=0;i<dataList.size();i++){
				for(int j=0;j<dataList.get(i).size();j++){
					if(cellmap.get("cell_name").equals(dataList.get(i).get(j).get("cell_name"))){
						data=data+Float.parseFloat((String)dataList.get(i).get(j).get("cell_data"));	
						//System.out.println("flag");
					}
				}
			}
			//System.out.println("单元格：'"+cellmap.get("cell_name")+"'，分支汇总值='"+data+"',总行值='"+cellmap.get("cell_data")+"'");
			if(data!=Float.parseFloat(cellmap.get("cell_data"))){
				resultList.addAll(AFValiRelationDelegate.getSonCell(orclCon, reportIn, 2, repids, cellmap.get("cell_name")));
			} 
		}
		System.out.println(resultList.size()); 
		return resultList;
	}
	
	
}
