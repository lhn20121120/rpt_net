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
 * ����Excel
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
		/** �Ƿ����������б��� */
		String isAllRep = "";

		/** ������������������Ϣ */
		String childRepId = "";
		String version = "";
		String reptDate = "";
		/**�Ƿ�ֻ�������*/
		String isSepOrg = "";

		/* �����Ӧ�Ĳ��� */
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
				/* ����ȫ������ */
				if (isAllRep.equals("true"))
				{
					// System.out.println("****����ȫ������****");
					reportList = this.getReportInfo(version);
				}
				/* �������� */
				else if (isAllRep.equals("false") && childRepId != null && !childRepId.equals(""))
				{
					// System.out.println("*****���ɵ�������****");
					reportList.add(childRepId);
				}
				else
				{
					messages.add("����Excelʧ��!");
				}

				if (reportList != null && reportList.size() != 0)
				{
					/*�ɹ�����*/
					int successNum = 0;
					/*ʧ������*/
					int failedNum = 0;					

					for (int i = 0; i < reportList.size(); i++)
					{
						/*��ñ���Id*/
						String reportId = (String) reportList.get(i);
						List orgList = null;
						
						/*�ֻ������ɱ���*/
						if(isSepOrg.equals("true"))
						{
							/*���ݱ���Idȡ�øñ�����Ҫ���ٻ���ȥ�,�ɴ�ȡ����Ҫ������Щ�����ı���*/
							orgList = StrutsMRepRangeDelegate.findAll(reportId, version);
						}
						/*���ֻ������ɱ���*/
						else if(isSepOrg.equals("false"))
						{
							orgList = new ArrayList();
							MRepRangeForm mRepRangeForm=new MRepRangeForm();
							mRepRangeForm.setOrgId("0");
							orgList.add(mRepRangeForm);
						}
												
						if (orgList != null && orgList.size() != 0)
						{
							
							/*ѭ������ÿ�������ı���*/
							for (int j = 0; j < orgList.size(); j++)
							{
								MRepRangeForm orgInfo = (MRepRangeForm) orgList.get(j);
								/*��û���ID*/
								String orgId = orgInfo.getOrgId();

								/*����Excel*/
								DataFormulaToExcel dataFormulaToExcel = new DataFormulaToExcel(reportId,
										version, orgId, reptDate);
								try
								{
									/*������Excel*/
									boolean isSuccess = dataFormulaToExcel.createExcel();
									String logInfo;
									if (isSuccess == true)
									{
										logInfo = "����:" + reportId + " �汾��:" + version + " ����:" + reptDate
												+ "����: " + orgId + " ����Excel�ɹ�!";
										successNum++;
									}
									else
									{
										logInfo = "����:" + reportId + " �汾��:" + version + " ����:" + reptDate
												+ "����: " + orgId + " ����Excelʧ��!";
										failedNum++;
									}
									FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);
								}
								catch (Exception e)
								{
									e.printStackTrace();
									String logInfo = "����:" + reportId + " �汾��:" + version + " ����:" + reptDate
											+ "����: " + orgId + " ����Excelʧ��!";
									FitechLog.writeLog(Config.LOG_OPERATION, logInfo, request);
									failedNum++;
									continue;
								}
							}
						}
					}

					messages.add("����Excel���!���ɳɹ�" + successNum + "��,ʧ��" + failedNum + "��!������־�в鿴��ϸ��Ϣ!");
				}
			}
			else
			{
				messages.add("����Excelʧ��!");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			messages.add("����Excelʧ��!");
		}
		if (messages != null && messages.getSize() != 0)
		{
			request.setAttribute(Config.MESSAGES, messages);
		}
		return mapping.findForward("view");

	}

	/**
	 * ���һ���汾�µ����б���ģ����Ϣ(��ʱд��Action��,������Ҫ�����Ƶ������İ���)
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
