package com.fitech.gznx.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fitech.gznx.service.AFReportDealDelegate;
import com.fitech.gznx.service.SearchPbocDataDelegate;
import com.fitech.gznx.vo.SearchVo;

public class SearchPBOCDataAction2 extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SearchVo vo = new SearchVo();
		// 获得币种
		String mcurrId = request.getParameter("mcurrId");
		
		// 获得口径
		String repFreqId = request.getParameter("freqId");
		vo.setFreqId(repFreqId);
		// 获得期数
		String startTeam = request.getParameter("startTeam");
		String endTeam = request.getParameter("endTeam");
		vo.setStartTeam(startTeam);
		vo.setEndTeam(endTeam);
		// 获得年月
		String startYear = "";
		String startMonth = "";
		String endYear = "";
		String endMonth = "";
		String targets  =  request.getParameter("targets");
		String[] templateIdAndVersionIdAndCellName = null;
		if(targets != null){
			templateIdAndVersionIdAndCellName = targets.split(",");
		}
		else{
			targets = "";
			templateIdAndVersionIdAndCellName = request.getParameterValues("templateIdAndVersionIdAndCellName");
			
			for (int i = 0; i < templateIdAndVersionIdAndCellName.length; i++) {
				String string = templateIdAndVersionIdAndCellName[i];
				targets += string ;
						if(i!=templateIdAndVersionIdAndCellName.length-1){
							targets+=",";
						}
			}
			vo.setTargets(targets);
		}
		if (startTeam != null && !startTeam.equals("") && startTeam.indexOf("-") > 0) {
			startMonth = startTeam.substring(startTeam.indexOf("-") + 1,
					startTeam.lastIndexOf("-"));
			startYear = startTeam.substring(0, startTeam.indexOf("-"));
		}
		if (endTeam != null && !endTeam.equals("") && endTeam.indexOf("-") > 0) {
			endMonth = endTeam.substring(endTeam.indexOf("-") + 1,
					endTeam.lastIndexOf("-"));
			endYear = endTeam.substring(0, endTeam.indexOf("-"));
		}
	
		vo.setStartYear(startYear);
		vo.setStartMonth(startMonth);
		vo.setEndYear(endYear);
		vo.setEndMonth(endMonth);
		vo.setMcurrId(Integer.parseInt(mcurrId.trim()));
		
		// 获得机构
		String orgId = request.getParameter("orgIds");
		vo.setOrgIds(orgId);
		// 拆分机构
		String[] orgIds = null;
		
		String orgStr = "";
		if (orgId != null && !orgId.equals("")) {
			orgIds = orgId.split(",");

			for (int i = 0; i < orgIds.length; i++) {
				orgStr += "'" + orgIds[i] + "'";
				if (i != orgIds.length - 1)
					orgStr += ",";
			}
		}
		if (com.fitech.gznx.common.Config.RH_DQCX.equals("1")) {
			List<Object[]> objList = AFReportDealDelegate.searchPBOCData(
					orgStr, startTeam, mcurrId, repFreqId,
					templateIdAndVersionIdAndCellName);
			request.setAttribute("objList", objList);
			return mapping.findForward("dqcx");
		} else {
//			vo.setOrgIds(orgStr);
			//A1411_1410_E5
			vo.setTargets(targets);
			SearchPbocDataDelegate.getDataMap(vo, orgStr, startYear, startMonth, endYear, endMonth, mcurrId, repFreqId, templateIdAndVersionIdAndCellName);
			request.setAttribute("vo", vo);	
			String opration = request.getParameter("operation");
			if(opration!=null&&opration.endsWith("download")){
				return mapping.findForward("download");
			}else{
				return mapping.findForward("index");
			}
		}
	}
}
