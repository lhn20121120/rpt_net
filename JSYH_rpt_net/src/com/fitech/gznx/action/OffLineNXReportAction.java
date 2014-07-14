package com.fitech.gznx.action;

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
import com.fitech.gznx.form.AFReportForm;
import com.fitech.gznx.form.AFTemplateForm;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;

/**
 * ��ʹ��hibernate ���Ը� 2011-12-28
 * Ӱ�����AfTemplate MCurr MRepFreq AfOrg
 * ����� 
 * 
 * @author Yao���޸ģ�
 * 
 */
public class OffLineNXReportAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		FitechMessages messages = new FitechMessages();
		MessageResources resources = getResources(request);
		Aditing aditing = new Aditing();
		AFReportForm afReportForm = (AFReportForm) form;
		RequestUtils.populate(afReportForm, request);
		try {

			String childRepId = request.getParameter("childRepId");
			String versionId = request.getParameter("versionId");
			String year = request.getParameter("year");
			String term = request.getParameter("term");
			String day = request.getParameter("day");
			String curId = request.getParameter("curId");
			String orgId = request.getParameter("orgId");
			String flag = request.getParameter("flag");
			// String dataRangeId = request.getParameter("dataRangeId");
			String checkFlag = request.getParameter("checkFlag");
			String actuFreqID = request.getParameter("actuFreqID");
			String curPage = request.getParameter("curPage") != null ? request
					.getParameter("curPage") : "1";
			String backQry = request.getParameter("backQry");
					
			/* ��ȡ�ӱ�����ϸ��Ϣ */
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ�����AfTemplate**/
			AFTemplateForm mcr = AFTemplateDelegate.getTemplateInfo(childRepId, versionId);

			if (curId == null || curId.equals(""))
				curId = "1";
			/* ��ȡ������Ϣ */
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ�����MCurr*/
			MCurr mCurr = StrutsMCurrDelegate.getISMCurr(curId);

			// �õ����Ϳھ�
			// MDataRgType mrt = new
			// StrutsMDataRgTypeDelegate().selectOneByName(dataRangeId);
			// if (mrt != null)
			// aditing.setDataRgTypeName(mrt.getDataRgDesc());
			
			// �õ�����Ƶ��
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ�����MRepFreq**/
			MRepFreq mrf = new StrutsMDataRgTypeDelegate().getActuRepFlag(new Integer(actuFreqID));

			aditing.setRepName(mcr.getTemplateName());
			aditing.setChildRepId(childRepId);
			aditing.setVersionId(versionId);
			aditing.setYear(new Integer(year));
			aditing.setTerm(new Integer(term));
			aditing.setDay(new Integer(day));
			aditing.setCurId(new Integer(curId));
			aditing.setOrgId(orgId);
			/**��ʹ��hibernate ���Ը� 2011-12-22
			 * Ӱ�����AfOrg**/
			aditing.setOrgName(AFOrgDelegate.getOrgInfo(orgId).getOrgName());
			aditing.setCurrName(mCurr.getCurName());
			// aditing.setDataRangeId(new Integer(dataRangeId));
			aditing.setActuFreqName(mrf.getRepFreqName());
			aditing.setActuFreqID(new Integer(actuFreqID));
			if (checkFlag != null)
				aditing.setCheckFlag(Short.valueOf(afReportForm.getCheckFlag()));

			request.setAttribute("flag", flag);
			
			String rp = "&curPage=" + curPage;
			if(backQry!=null){
				String[] splitQry = backQry.split("_");
	
				rp = "date=" + splitQry[0]
				             + "&repName="+ splitQry[1]
				             + "&orgId="+ splitQry[2]
				             + "&repFreqId="+ splitQry[3]
				             + "&curPage=" + curPage;
				if(splitQry.length>=5)             
					rp += "&bak1=" + splitQry[4];
				if(splitQry.length>=6)   
					rp += "&templateId=" + splitQry[5];	
			}
			
			String requestParam = (String) request.getAttribute("RequestParam") != null ? (String) request
					.getAttribute("RequestParam")
					: rp;

			request.setAttribute("curPage", curPage);
			request.setAttribute("RequestParam", requestParam);
			request.setAttribute("backQry", backQry);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

		request.setAttribute("aditing", aditing);
		return mapping.findForward("viewOffLine");

	}
}