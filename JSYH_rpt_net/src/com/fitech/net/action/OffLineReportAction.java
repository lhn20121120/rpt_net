package com.fitech.net.action;

import java.io.IOException;

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
import com.fitech.gznx.service.AFOrgDelegate;

/**
 * 在线填报
 * 
 * @author Yao（修改）
 * 
 */
public class OffLineReportAction extends Action
{

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
		Aditing aditing = new Aditing();
		ReportInForm reportInForm = (ReportInForm) form;
		RequestUtils.populate(reportInForm, request);
		try
		{

			String childRepId = request.getParameter("childRepId");
			String versionId = request.getParameter("versionId");
			String year = request.getParameter("year");
			String term = request.getParameter("term");
			String curId = request.getParameter("curId");
			String orgId = request.getParameter("orgId");
			String flag = request.getParameter("flag");
			String dataRangeId = request.getParameter("dataRangeId");
			String checkFlag = request.getParameter("checkFlag");
			String actuFreqID = request.getParameter("actuFreqID");
			String curPage = request.getParameter("curPage") != null ? request.getParameter("curPage") : "1";
			
			String backQry = request.getParameter("backQry");
			String[] splitQry =null;
			if(backQry!=null && !backQry.equals(""))
				splitQry = backQry.split("_");
			
			/* 获取子报表详细信息 */ 
			/**已使用hibernate 卞以刚 2011-12-21**/
			MChildReportForm mcr =new StrutsMChildReportDelegate().getMChildReport(childRepId, versionId);
			reportInForm.setRepName(mcr.getReportName());
			if (curId == null || curId.equals(""))
				curId = "1";
			/* 获取币种信息 */
			/**已使用hibernate 卞以刚 2011-12-21**/
			MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);
			String CurrName = mCurr.getCurName();
			reportInForm.setCurName(CurrName);

			// 得到报送口径
			/**已使用hibernate 卞以刚 2011-12-21**/
			MDataRgType mrt = new StrutsMDataRgTypeDelegate().selectOneByName(dataRangeId);
			if (mrt != null)
				aditing.setDataRgTypeName(mrt.getDataRgDesc());
			// 得到报送频度
			MRepFreq mrf = new StrutsMDataRgTypeDelegate().getActuRepFlag(new Integer(actuFreqID));

			aditing.setRepName(mcr.getReportName());
			aditing.setChildRepId(childRepId);
			aditing.setVersionId(versionId);
			aditing.setYear(new Integer(year));
			aditing.setTerm(new Integer(term));
			aditing.setCurId(new Integer(curId));
			aditing.setOrgId(orgId);
			/**已使用hibernate 卞以刚 2011-12-21**/
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
			
		    String	requestParam=(String)request.getAttribute("RequestParam") != null ? (String)request.getAttribute("RequestParam") : rp ;
			request.setAttribute("curPage", curPage);
			request.setAttribute("RequestParam", requestParam);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		

		request.setAttribute("aditing", aditing);
		return mapping.findForward("viewOffLine");

	}
}