package com.fitech.gznx.action;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.fitech.gznx.service.AfCollectDataValidateDelegate;
import com.fitech.net.config.Config;

/**
 * 总分校验 2013-5-15
 * 
 * @author Administrator
 * 
 */
public class CollectValidateAction extends Action {

	private FitechException log = new FitechException(CollectValidateAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// FitechMessages messages = new FitechMessages();
		// MessageResources resources = getResources(request);
		AFReportForm reportInForm = (AFReportForm) form;
		RequestUtils.populate(reportInForm, request);

		HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

		// 取得模板类型
		Integer templateType = null;
		if (session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG) != null)
			templateType = Integer.valueOf(session.getAttribute(com.cbrc.smis.common.Config.REPORT_SESSION_FLG).toString());

		boolean resultHZ = true;
		Integer repInId = null;

		/** 初始化相关参数 */
		String repInIds = (String) request.getParameter("repInIds");
		String[] repIds = null;
		if (repInIds != null && repInIds.trim().length() > 0) {// 此时为总分校验选择
			repIds = repInIds.split(",");
		} else {// 此时为总分校验全部
			reportInForm.setTemplateType("2");//只查询人行报表记录
			List<Integer> listRepInId = AFReportDelegate.selectOfManualAll(reportInForm, operator, null);// 查询所有未审核通过的报表记录
			if (listRepInId != null && listRepInId.size() > 0) {
				repIds = new String[listRepInId.size()];
				for (int i = 0; i < listRepInId.size(); i++) {
					repIds[i] = ((Integer) listRepInId.get(i)).toString();
				}
			}
		}

		com.cbrc.smis.proc.jdbc.FitechConnection fitechConn = null;
		try {
			// 初始化数据库连接
			fitechConn = new com.cbrc.smis.proc.jdbc.FitechConnection();
			Connection orclCon = fitechConn.getConnect();

			/** 开始进行迭代校验操作 */
			for (int i = 0; i < repIds.length; i++) {
				boolean singleFlag = true;
				List<Map<String, String>> resultList = null;
				try {
					String repId = repIds[i];
					repInId = new Integer(repId);
					// System.out.println("=====****报表ID值****====="+repId);

					AfReport reportIn = AfCollectDataValidateDelegate.getReportIn(repInId.longValue()); // 需修改
					List<Map<String, String>> cellMap = AfCollectDataValidateDelegate.parseCell(orclCon, reportIn, templateType);
					// 如果填报的单元格集合没有数据
					if (cellMap == null || cellMap.isEmpty()) {
						PrintWriter out = response.getWriter();

						response.setContentType("text/xml");
						response.setHeader("Cache-control", "no-cache");
						String result = "noData";
						out.println("<response><result>" + result + "</result></response>");
						out.close();
						return null;
					}
					// System.out.println(reportIn);
					String orgRepIds = repId;
					// String orgSonRepIds = "";
					Map org_rep = AfCollectDataValidateDelegate.getSonReq(orclCon, reportIn, templateType);
					Iterator it = org_rep.entrySet().iterator();
					while (it.hasNext()) {
						Entry entry = (Entry) it.next();
						orgRepIds += "," + entry.getValue();
						// orgSonRepIds += "," + entry.getValue();
					}
					/** 2、获取该报表的所有子机构报表 * */
					if (org_rep.size() > 0) {
						resultList = resultHZ(orclCon, cellMap, org_rep, reportIn, orgRepIds);
					}
					if (resultList != null && resultList.size() > 0) {
						singleFlag = false;
					}
					AfCollectDataValidateDelegate.add(resultList, reportIn, repId);
					// System.out.println(repInId + " 校验结束==" + new Date());

					// 更新总分校验标志位
					AfCollectDataValidateDelegate.updateReport(orclCon, repId, singleFlag);

					// 更新日志操作
					StringBuffer log = new StringBuffer();
					log.append("报表[机构号:").append(reportIn.getOrgId()).append(",编号:").append(reportIn.getTemplateId()).append(",版本号:").append(reportIn.getVersionId());
					if (singleFlag) {
						FitechLog.writeLog(com.cbrc.smis.common.Config.LOG_OPERATION, operator.getOperatorName(),log.append("]总分校验通过!").toString());
					} else {
						FitechLog.writeLog(com.cbrc.smis.common.Config.LOG_OPERATION, operator.getOperatorName(), log.append("]总分校验不通过!").toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 设定所有记录总分校验标志位
				resultHZ = resultHZ && singleFlag;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fitechConn.close();
		}

		/** 返回校验结果 */
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Cache-control", "no-cache");
		boolean result = resultHZ;
		out.println("<response><result>" + result + "</result></response>");
		out.close();

		return null;
	}

	/**
	 * 2012-09-14 孙大为
	 * 
	 * @param 校验子结构
	 * @param report_in
	 * @return
	 * @throws Exception
	 */

	public List<Map<String, String>> resultHZ(Connection orclCon, List<Map<String, String>> cellMap, Map org_rep, AfReport reportIn, String repids) throws Exception {
		// boolean result = false;
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<ArrayList<Map<String, String>>> dataList = new ArrayList<ArrayList<Map<String, String>>>();
		AfReport afReport = reportIn;
		Iterator it = org_rep.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			afReport.setRepId((Long) entry.getValue());
			// 获取同一张报表每个机构的值
			ArrayList<Map<String, String>> org_map = (ArrayList<Map<String, String>>) AfCollectDataValidateDelegate.parseCell(orclCon, afReport, 2);
			// System.out.println(org_map.size());
			dataList.add(org_map);
		}
		for (Map<String, String> cellmap : cellMap) {
			// float data=new Float(0);
			Double data = new Double(0);
			// 求此单元格子机构相加的值
			for (int i = 0; i < dataList.size(); i++) {
				for (int j = 0; j < dataList.get(i).size(); j++) {
					if (cellmap.get("cell_name").equals(dataList.get(i).get(j).get("cell_name"))) {
						//System.out.println(Double.parseDouble((String) dataList.get(i).get(j).get("cell_data")));
						data = data + Double.parseDouble((String) dataList.get(i).get(j).get("cell_data"));
					}
				}
			}
			// System.out.println("单元格：'"+cellmap.get("cell_name")+"'，分支汇总值='"+data+"',总行值='"+cellmap.get("cell_data")+"'");
			if (Math.abs(data-Double.parseDouble(cellmap.get("cell_data")))>0.1) {//如果相差大于0.1,则校验不通过
				resultList.addAll(AfCollectDataValidateDelegate.getSonCell(orclCon, reportIn, 2, repids, cellmap.get("cell_name")));
			}
		}
		// System.out.println(resultList.size());
		return resultList;
	}
}
