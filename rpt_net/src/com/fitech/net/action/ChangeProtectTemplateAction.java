package com.fitech.net.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsMCellDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellForm;
import com.cbrc.smis.util.FitechException;
import com.fitech.net.adapter.StrutsIDataRelationDelegate;
import com.fitech.net.common.CommMethod;
import com.fitech.net.form.IDataRelationForm;

/**
 * 数据关系设定
 * 
 * @author LQ
 */

public class ChangeProtectTemplateAction extends Action
{
	private static FitechException log = new FitechException(ChangeProtectTemplateAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException
	{		
		IDataRelationForm iDataRelationForm = (IDataRelationForm)form;
		if (iDataRelationForm.getMethod() != null && iDataRelationForm.getMethod().equals("saveDateToSession"))		
			return saveDateToSession(mapping,form,request,response);
		
		if (iDataRelationForm.getMethod() != null && iDataRelationForm.getMethod().equals("changeCell"))		
			return changeCell(mapping,form,request,response);
		
		if (iDataRelationForm.getMethod() != null && iDataRelationForm.getMethod().equals("showAllData"))		
			return showAllData(mapping,form,request,response);
		
		return null;
	}

	private ActionForward saveDateToSession(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		HashMap hm = null;
		IDataRelationForm iDataRelationForm = (IDataRelationForm)form;
		
		if (request.getSession().getAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET) != null)		
			hm = (HashMap)request.getSession().getAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET);		
		else{
			putDataToSession(request, iDataRelationForm.getChildRepId(), iDataRelationForm.getVersionId());
			hm = (HashMap) request.getSession().getAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET);
		}
		
		Integer idrId = iDataRelationForm.getIdrId();
		IDataRelationForm idrForm = new IDataRelationForm();
		idrForm.setIdrId(iDataRelationForm.getIdrId());
		idrForm.setIdrRelative(iDataRelationForm.getIdrRelative());
		idrForm.setIdrFormula(iDataRelationForm.getIdrFormula());
		idrForm.setIdrDefaultvalue(iDataRelationForm.getIdrDefaultvalue());
		idrForm.setIdrInitvalue(iDataRelationForm.getIdrInitvalue());
		idrForm.setModify(true);
		hm.put(String.valueOf(idrId), idrForm);
		
		CommMethod.buidPageInfo(request);
		request.getSession().setAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET, hm);
		request.setAttribute("idrId", idrForm.getIdrId());
		request.setAttribute("idrRelative", idrForm.getIdrRelative());
		request.setAttribute("idrFormula", idrForm.getIdrFormula());
		request.setAttribute("idrDefaultvalue", idrForm.getIdrDefaultvalue());
		request.setAttribute("idrInitvalue", idrForm.getIdrInitvalue());
		
		return mapping.findForward("success");
	}

	private ActionForward changeCell(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		IDataRelationForm iDataRelationForm = (IDataRelationForm)form;
		
		if (request.getSession().getAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET) != null){
			HashMap hm = (HashMap)request.getSession().getAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET);
			Integer idrId = iDataRelationForm.getIdrId();
			if (hm.containsKey(String.valueOf(idrId))){
				IDataRelationForm idrForm = (IDataRelationForm) hm.get(String.valueOf(idrId));
				request.setAttribute("idrId", idrForm.getIdrId());
				request.setAttribute("idrRelative", idrForm.getIdrRelative());
				request.setAttribute("idrFormula", idrForm.getIdrFormula());
				request.setAttribute("idrDefaultvalue", idrForm.getIdrDefaultvalue());
				request.setAttribute("idrInitvalue", idrForm.getIdrInitvalue());
			}else			
				request.setAttribute("idrId", idrId);			
		}
		
		CommMethod.buidPageInfo(request);
		return mapping.findForward("success");
	}

	/**
	 * 从数据关联表读出已经设置的单元格，放到SESSION中
	 */
	public void putDataToSession(HttpServletRequest request, String childRepId, String versionId){
		
		List list = StrutsIDataRelationDelegate.find(childRepId, versionId);
		HashMap hm = null;
		
		if (list != null && list.size() > 0){
			hm = new HashMap();
			IDataRelationForm form = null;
			for (int i = 0; i < list.size(); i++){
				form = (IDataRelationForm) list.get(i);
				hm.put(String.valueOf(form.getIdrId()), list.get(i));
			}
			if (form != null){
				request.setAttribute("idrId", form.getIdrId());
				request.setAttribute("idrRelative", form.getIdrRelative());
				request.setAttribute("idrFormula", form.getIdrFormula());
				request.setAttribute("idrDefaultvalue", form.getIdrDefaultvalue());
				request.setAttribute("idrInitvalue", form.getIdrInitvalue());
			}
		}
		// 将组织在HASHMAP里面的数据放到SESSION里
		if (hm != null)
			request.getSession().setAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET, hm);
	}

	private ActionForward showAllData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		IDataRelationForm iDataRelationForm = (IDataRelationForm)form;
		HashMap hm = null;
		List list = null;
				
		if (request.getSession().getAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET) == null)
			this.putDataToSession(request, iDataRelationForm.getChildRepId(), iDataRelationForm.getVersionId());
		
		hm = (HashMap) request.getSession().getAttribute(com.fitech.net.config.Config.DATA_RELATION_IS_SET);
		Iterator it = hm.entrySet().iterator();
		HashMap cells = this.getCell(iDataRelationForm.getChildRepId(), iDataRelationForm.getVersionId());
		if (it.hasNext()) list = new ArrayList();
		
		while (it.hasNext()){
			IDataRelationForm idrForm = new IDataRelationForm();
			Map.Entry map = (Map.Entry) it.next();
			idrForm = (IDataRelationForm) map.getValue();
			idrForm.setCellName(cells.get(idrForm.getIdrId()).toString());
			
			if (idrForm.getIdrRelative() != null){
				if (idrForm.getIdrRelative().equals(Config.RELATIIONYWXTSC))				
					idrForm.setIdrRelativeName("业务系统生成");
				
				if (idrForm.getIdrRelative().equals(Config.RELATIONSGWH))				
					idrForm.setIdrRelativeName("手工维护");	
				
				if (idrForm.getIdrRelative().equals(Config.RELATONJSX))				
					idrForm.setIdrRelativeName("计算项");				
			}
			list.add(idrForm);
		}
		if (list != null){
			for (int i = 0; i < list.size(); i++){
				IDataRelationForm idrForm = (IDataRelationForm) list.get(i);
				if (cells.containsKey(idrForm.getIdrId()))
					cells.remove(idrForm.getIdrId());
			}
		}
		it = cells.entrySet().iterator();
		String notSetCell = "";
		if (it.hasNext()){
			Map.Entry map = (Map.Entry) it.next();
			notSetCell += map.getValue();
		}
		
		while (it.hasNext()){
			Map.Entry map = (Map.Entry) it.next();
			notSetCell += ",";
			notSetCell += map.getValue();
		}
		
		request.setAttribute("notSetCell", notSetCell);
		request.setAttribute("Records", list);
		request.setAttribute("childRepId", iDataRelationForm.getChildRepId());
		request.setAttribute("versionId", iDataRelationForm.getVersionId());
		return mapping.findForward("showAllData");
	}

	private HashMap getCell(String childRepId, String versionId){
		HashMap hm = null;
		try{
			List list = null;
			list = StrutsMCellDelegate.getCells(childRepId, versionId);
			if (list != null && list.size() > 0){
				hm = new HashMap();
				MCellForm mCellForm = null;
				for (int i = 0; i < list.size(); i++){
					mCellForm = (MCellForm) list.get(i);
					hm.put(mCellForm.getCellId(), mCellForm.getCellName());
				}
			}
		}
		catch (Exception ex){
			log.printStackTrace(ex);
		}
		return hm;
	}
}
