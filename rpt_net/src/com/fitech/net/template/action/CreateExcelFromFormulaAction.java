package com.fitech.net.template.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.dao.DBConn;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.template.util.DataFormulaToExcel;

/**
 * 生成Excel
 * 
 * @author wh
 * 
 */
public final class CreateExcelFromFormulaAction extends Action
{
	private static FitechException log = new FitechException(CreateExcelFromFormulaAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		/** 是否是生成所有报表 */
		String isAllRep = "";

		/** 单个报表生成所需信息 */
		String childRepId = "";
		String version = "";
		String reptDate = "";
		/**是否分机构生成*/
		String isSepOrg = "";

		/* 获得相应的参数 */
		if (request.getParameter("isAllRep") != null)
		{
			isAllRep = request.getParameter("isAllRep");
		}
		if (request.getParameter("childRepId") != null)
		{
			childRepId = request.getParameter("childRepId").split(",")[0];
		}
		if (request.getParameter("version") != null)
		{
			version = request.getParameter("version");
		}
		if (request.getParameter("reptDate") != null)
		{
			reptDate = request.getParameter("reptDate");
		}
		if (request.getParameter("isSepOrg") != null)
		{
			isSepOrg = request.getParameter("isSepOrg");
		}

		try
		{
			List reportList = new ArrayList();
			if (isAllRep != null && !isAllRep.equals("") && version != null && !version.equals(""))
			{
				/* 生成全部报表 */
				if (isAllRep.equals("true"))
				{
					// System.out.println("****生成全部报表****");
					reportList = this.getReportInfo(version);
				}
				/* 单个报表 */
				else if (isAllRep.equals("false") && childRepId != null && !childRepId.equals(""))
				{
					// System.out.println("*****生成单个报表****");
					reportList.add(childRepId);
				}
				else
				{
					messages.add("生成Excel失败!");
				}

				if (reportList != null && reportList.size() != 0)
				{
					/*成功数量*/
					int successNum = 0;
					/*失败数量*/
					int failedNum = 0;					

					for (int i = 0; i < reportList.size(); i++)
					{
						/*获得报表Id*/
						String reportId = (String) reportList.get(i);
						List orgList = null;
						
						/*分机构生成报表*/
						if(isSepOrg.equals("true"))
						{
							/*根据报表Id取得该报表需要多少机构去填报,由此取得需要生成哪些机构的报表*/
							orgList = StrutsMRepRangeDelegate.findAll(reportId, version);
						}
						/*不分机构生成报表*/
						else if(isSepOrg.equals("false"))
						{
							orgList = new ArrayList();
							MRepRangeForm mRepRangeForm=new MRepRangeForm();
							mRepRangeForm.setOrgId("0");
							orgList.add(mRepRangeForm);
						}
												
						if (orgList != null && orgList.size() != 0)
						{
							
							/*循环生成每个机构的报表*/
							for (int j = 0; j < orgList.size(); j++)
							{
								MRepRangeForm orgInfo = (MRepRangeForm) orgList.get(j);
								/*获得机构ID*/
								String orgId = orgInfo.getOrgId();

								/*生成Excel*/
								DataFormulaToExcel dataFormulaToExcel = new DataFormulaToExcel(reportId,
										version, orgId, reptDate);
								try
								{
									/*创建该Excel*/
									boolean isSuccess = dataFormulaToExcel.createExcel();
									String logInfo;
									if (isSuccess == true)
									{
										logInfo = "报表:" + reportId + " 版本号:" + version + " 日期:" + reptDate
												+ "机构: " + orgId + " 生成Excel成功!";
										successNum++;
									}
									else
									{
										logInfo = "报表:" + reportId + " 版本号:" + version + " 日期:" + reptDate
												+ "机构: " + orgId + " 生成Excel失败!";
										failedNum++;
									}
									FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);
								}
								catch (Exception e)
								{
									e.printStackTrace();
									String logInfo = "报表:" + reportId + " 版本号:" + version + " 日期:" + reptDate
											+ "机构: " + orgId + " 生成Excel失败!";
									FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);
									failedNum++;
									continue;
								}
							}
						}
					}

					messages.add("生成Excel完成!生成成功" + successNum + "张,失败" + failedNum + "张!请在日志中查看详细信息!");
				}
			}
			else
			{
				messages.add("生成Excel失败!");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			messages.add("生成Excel失败!");
		}
		if (messages != null && messages.getSize() != 0)
		{
			request.setAttribute(Config.MESSAGES, messages);
		}
		return mapping.findForward("view");

	}

	/**
	 * 获得一个版本下的所有报表模板信息(暂时写在Action中,如有需要可以移到其他的包下)
	 * 
	 * @param version
	 * @return
	 */
	public List getReportInfo(String version)
	{
		List result = null;
		DBConn conn = null;
		try
		{
			String hql = "select distinct mcp.comp_id.childRepId from MChildReport mcp "
					+ "where mcp.comp_id.versionId=?";
			conn = new DBConn();
			Session session = conn.openSession();
			Query query = session.createQuery(hql);
			query.setString(0, version);

			List list = query.list();

			if (list != null && list.size() > 0)
			{
				Iterator it = list.iterator();
				result = new ArrayList();
				while (it.hasNext())
				{
					String childRepId = (String) it.next();
					result.add(childRepId);
				}
			}
		}
		catch (Exception e)
		{
			log.printStackTrace(e);
			result = null;
		}
		finally
		{
			if (conn != null)
				conn.closeSession();
		}

		return result;
	}
}
