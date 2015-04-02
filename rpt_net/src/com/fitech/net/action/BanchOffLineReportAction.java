package com.fitech.net.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMCurrDelegate;
import com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.ReportInForm;
import com.cbrc.smis.hibernate.MCurr;
import com.cbrc.smis.hibernate.MDataRgType;
import com.cbrc.smis.hibernate.MRepFreq;
import com.cbrc.smis.other.Aditing;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.DateUtil;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.service.AFOrgDelegate;

/**
 * �����
 * 
 * @author Yao���޸ģ�
 * 
 */
public class BanchOffLineReportAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
		
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);

		String date = request.getParameter("term");
		if(date!=null&&!date.equals("")){
			String year = DateUtil.getYear(date);
			String month = DateUtil.getMonth(date);
			String day = DateUtil.getDay(date);
			request.setAttribute("date", date);
			request.setAttribute("year", year	);
			request.setAttribute("month", month	);
			request.setAttribute("day", day	);
		}
		String orgId = request.getParameter("orgId");
		if(orgId!=null&&!orgId.equals("")){
			request.setAttribute("orgId", orgId	);
			request.setAttribute("orgName", AFOrgDelegate.getOrgInfo(orgId).getOrgName()	);
			
		}
		String url = request.getParameter("url");
		if(url!=null&&!orgId.equals("")){
			System.out.println(url);
			request.setAttribute("url", url	);
		}
			/*String repInIds = request.getParameter("repInIds");
			System.out.println(repInIds);
			//List fileList = null;
			resList = new ArrayList();
			if (repInIds != null && !repInIds.equals("")) {
				String[] repNameArr = repInIds.split(",");
				if (repNameArr != null && repNameArr.length > 0) {

					*//** ��ѡ���� *//*
					for (int i = 0; i < repNameArr.length; i++) {
						String[] repInfo = repNameArr[i].split("_");
						Aditing aditing = new Aditing();
						String childRepId = repInfo[0];
						String versionId = repInfo[1];
						String dataRangeId= repInfo[2]; 
						String actuFreqID = repInfo[3];
						String curId = repInfo[4];
						String aorgId = repInfo[5];
						 ��ȡ�ӱ�����ϸ��Ϣ  
						*//**��ʹ��hibernate ���Ը� 2011-12-21**//*
						MChildReportForm mcr =new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId);
						//reportInForm.setRepName(mcr.getReportName());
						if (curId == null || curId.equals(""))
							curId = "1";
						 ��ȡ������Ϣ 
						*//**��ʹ��hibernate ���Ը� 2011-12-21**//*
						MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
						String CurrName = mCurr.getCurName();
						//reportInForm.setCurName(CurrName);

						// �õ����Ϳھ�
						*//**��ʹ��hibernate ���Ը� 2011-12-21**//*
						MDataRgType mrt = new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeId);
						if (mrt != null)
							aditing.setDataRgTypeName(mrt.getDataRgDesc());
						// �õ�����Ƶ��
						MRepFreq mrf = new StrutsMDataRgTypeDelegate().getActuRepFlag(new Integer(actuFreqID));

						aditing.setRepName(mcr.getReportName());
						aditing.setChildRepId(childRepId);
						aditing.setVersionId(versionId);
						aditing.setYear(new Integer(year));
						aditing.setTerm(new Integer(month));
						
						aditing.setCurId(new Integer(curId));
						aditing.setOrgId(aorgId);
						*//**��ʹ��hibernate ���Ը� 2011-12-21**//*
						aditing.setOrgName(AFOrgDelegate.getOrgInfo(aorgId).getOrgName());
						aditing.setCurrName(CurrName);
						aditing.setDataRangeId(new Integer(dataRangeId));
						aditing.setActuFreqName(mrf.getRepFreqName());
						aditing.setActuFreqID(new Integer(actuFreqID));
						aditing.setCheckFlag((short)-1);
						//resList.add(aditing);
					}
				}
			}
			if(resList!=null && resList.size()>0){
				request.setAttribute(Config.RECORDS,resList);   
			}*/
			/*String childRepId = request.getParameter("childRepId");
			String versionId = request.getParameter("versionId");
			String year = request.getParameter("year");
			String curId = request.getParameter("curId");
			String flag = request.getParameter("flag");
			String dataRangeId = request.getParameter("dataRangeId");
			String checkFlag = request.getParameter("checkFlag");
			String actuFreqID = request.getParameter("actuFreqID");
			String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "1";
			
			String backQry = request.getParameter("backQry");
			String[] splitQry =null;
			if(backQry!=null && !backQry.equals(""))
				splitQry = backQry.split("_");
			
			 ��ȡ�ӱ�����ϸ��Ϣ  
			*//**��ʹ��hibernate ���Ը� 2011-12-21**//*
			MChildReportForm mcr =new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId);
			reportInForm.setRepName(mcr.getReportName());
			if (curId == null || curId.equals(""))
				curId = "1";
			 ��ȡ������Ϣ 
			*//**��ʹ��hibernate ���Ը� 2011-12-21**//*
			MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
			String CurrName = mCurr.getCurName();
			reportInForm.setCurName(CurrName);

			// �õ����Ϳھ�
			*//**��ʹ��hibernate ���Ը� 2011-12-21**//*
			MDataRgType mrt = new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeId);
			if (mrt != null)
				aditing.setDataRgTypeName(mrt.getDataRgDesc());
			// �õ�����Ƶ��
			MRepFreq mrf = new StrutsMDataRgTypeDelegate().getActuRepFlag(new Integer(actuFreqID));

			aditing.setRepName(mcr.getReportName());
			aditing.setChildRepId(childRepId);
			aditing.setVersionId(versionId);
			aditing.setYear(new Integer(year));
			aditing.setTerm(new Integer(term));
			aditing.setCurId(new Integer(curId));
			aditing.setOrgId(orgId);
			*//**��ʹ��hibernate ���Ը� 2011-12-21**//*
			aditing.setOrgName(AFOrgDelegate.getOrgInfo(orgId).getOrgName());
			aditing.setCurrName(CurrName);
			aditing.setDataRangeId(new Integer(dataRangeId));
			aditing.setActuFreqName(mrf.getRepFreqName());
			aditing.setActuFreqID(new Integer(actuFreqID));
			if (checkFlag != null)
				aditing.setCheckFlag(reportInForm.getCheckFlag());

			request.setAttribute("flag", flag);
			
			
			//String rp = "year=" + year + "&term=" + term + "&curPage=" + curPage;
			String rp = "";
			if(splitQry!=null)
				rp = "date=" + splitQry[0] + "&repName=" + splitQry[1] +"&orgId="+ splitQry[2] + "&curPage=" + curPage;
			
		}
		*/
		String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "1";
		String	requestParam="orgId="+ orgId +"&term="+date +"&workTaskTerm="+date+"&workTaskOrgId="+orgId;
		request.setAttribute("curPage", curPage);
		request.setAttribute("RequestParam", requestParam);

		//request.setAttribute("aditing", aditing);
		return mapping.findForward("viewOffLine");

	}
}