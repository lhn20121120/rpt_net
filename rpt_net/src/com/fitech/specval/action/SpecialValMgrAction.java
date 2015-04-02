package com.fitech.specval.action;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.gznx.form.ExchangeRateForm;
import com.fitech.gznx.po.ExchangeRate;
import com.fitech.gznx.po.VCurrency;
import com.fitech.gznx.service.StrutsExchangeRateDelegate;
import com.fitech.specval.adapter.StrutsSpecValidateInfoDelegate;
import com.fitech.specval.form.SpecialValForm;
import com.fitech.specval.po.SpecValidateInfo;


/** 
 * 特殊校验处理DispatchAction
 * @author 	cb
 * @date	2007-07-27
 * 
 */

public class SpecialValMgrAction extends DispatchAction {

	/** 
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：SpecValidateInfo
	 * 响应用户从URL分页浏览汇率情况的请求
	 * 导航至 specval/viewSpecVal.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward viewSpecVal(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,	HttpServletResponse response) {

		SpecialValForm dataForm = (SpecialValForm)form;
        
        Map paramMap = new HashMap();
        
		List specValInfoLst = null;
		//获取URL
		String url = request.getRequestURL() + "?method=viewSpecVal";
		
		SpecValidateInfo specValInfo = new SpecValidateInfo();

		//获得查询条件
		if (request.getParameter("valName") != null) {
			String valName = request.getParameter("valName");
			specValInfo.setValName(valName);
            url = url + "&valName=" + valName;
            paramMap.put("valName",valName);
		}
		
		//分页设置
		int curPage = 1;
		ApartPage apartPage = new ApartPage();
        apartPage.setTerm(url);
		if(request.getParameter("curPage") != null){
			curPage = Integer.parseInt(request.getParameter("curPage"));
		}
			url = url + "&curPage=";
       
		//设置当前页
		apartPage.setCurPage(curPage);
        paramMap.put("curPage",new Integer(curPage));
        request.setAttribute("paramMap",paramMap);
        
		//校验信息集合
        /**已使用hibernate 卞以刚 2011-12-28
         * 影响对象：SpecValidateInfo*/
        specValInfoLst = StrutsSpecValidateInfoDelegate.getSpecValidateInfos(dataForm,apartPage);
		if (specValInfoLst != null && !specValInfoLst.isEmpty()) {
			request.setAttribute("specValInfoLst", specValInfoLst);
			request.setAttribute("url",url);
			request.setAttribute("apartPage", apartPage);
		}

		return mapping.findForward("specValLst");
	}

	/** 
	 * 已使用hibernate 卞以刚 2011-12-28
	 * 影响对象：SpecValidateInfo
	 * 响应用户输入查询条件查询汇率的请求
	 * 导航至 sysManage/SpecValidateInfo/viewSpecValidateInfo.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward query(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,	HttpServletResponse response) {
		
		SpecialValForm dataForm = (SpecialValForm)form;
	
		List specValInfoLst = null;
		Map paramMap = new HashMap();
		
		//获取URL
		String url = request.getRequestURL().toString();
		url = url + "?method=viewSpecVal";
		
		//分页设置
		int curPage = 1;
		ApartPage apartPage = new ApartPage();
		
		SpecValidateInfo specValInfo = new SpecValidateInfo();


		if(dataForm.getValName()!=null && !dataForm.getValName().equals("")){
			url = url + "&valName=" + dataForm.getValName();			
	        paramMap.put("valName", dataForm.getValName());
		}
		
		apartPage.setTerm(url);
		url = url + "&curPage=";
		
		//设置当前页
		apartPage.setCurPage(curPage);
        paramMap.put("curPage",new Integer(curPage));
		request.setAttribute("paramMap",paramMap);
		
		//币种集合
		/**已使用hibernate 卞以刚 2011-12-28
		 * 影响对象：SpecValidateInfo*/
		specValInfoLst = StrutsSpecValidateInfoDelegate.getSpecValidateInfos(dataForm,apartPage);
		
		if (specValInfoLst != null && !specValInfoLst.isEmpty()) {
			apartPage.setTerm(url.substring(0,url.lastIndexOf("&")));
			request.setAttribute("specValInfoLst", specValInfoLst);
			request.setAttribute("url",url);
			request.setAttribute("apartPage", apartPage);
		}
		
		return mapping.findForward("specValLst");
	}
	
	/** 
	 * 
	 * 响应用户增加汇率的请求
	 * 导航至 sysManage/SpecValidateInfo/addSpecValidateInfo.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward toAdd(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,	
			HttpServletResponse response) {
		
		SpecialValForm dataForm = (SpecialValForm)form;
		
        String curPage = null;
        String currCode = null;
        String rateDate = null;
        Map paramMap = new HashMap();
        
        if(request.getParameter("curPage") != null){
            curPage = request.getParameter("curPage");
            paramMap.put("curPage",curPage);
        }
		
		if(dataForm.getValName()!=null && !dataForm.getValName().equals("")){			
            paramMap.put("valName",dataForm.getValName());
		}
		
        request.setAttribute("paramMap",paramMap);
		
		return mapping.findForward("specValMgr_add");
	}
	
	/** 
	 * 
	 * 响应用户完成增加汇率的请求
	 * 导航至 sysManage/SpecValidateInfo/viewSpecValidateInfo.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward add(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,	HttpServletResponse response) {	
		
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		
		SpecialValForm dataForm = (SpecialValForm)form;
		
        Map paramMap = new HashMap();
		//分页预设置
		String url = request.getRequestURL()+"?method=viewSpecVal";
		ApartPage apartPage = new ApartPage();
		
        paramMap.put("curPage",new Integer("1"));
        request.setAttribute("paramMap",paramMap);
      
		// 构建校验类
        SpecValidateInfo specValInfo = new SpecValidateInfo();
		if(null != dataForm.getValName() && !"".equals(dataForm.getValName())
				&& null != dataForm.getValFormula() && !"".equals(dataForm.getValFormula())
				&& null != dataForm.getValDes() && !"".equals(dataForm.getValDes())){
			
			specValInfo.setValName(dataForm.getValName());
			specValInfo.setValFormula(dataForm.getValFormula());
			specValInfo.setValDes(dataForm.getValDes());
			
			if(StrutsSpecValidateInfoDelegate.saveOrUpdateSpecVal(specValInfo,"new")){
				messages.add("新增成功！");
                //paramMap.put("currCode",currCode1);
                //url = url + "&currCode" + currCode1;
			}else{
				messages.add("新增失败！");
			}
		}else{
			messages.add("缺少条件，3个都为必填！");
		}
		
        apartPage.setTerm(url);
        url = url +"&curPage=";
		
        //校验集合
		List specValInfoLst = StrutsSpecValidateInfoDelegate.getSpecValidateInfos(dataForm,apartPage);
        
		if(specValInfoLst != null && !specValInfoLst.isEmpty()){
            request.setAttribute("specValInfoLst",specValInfoLst);
            request.setAttribute("apartPage", apartPage);
            request.setAttribute("url", url);
        }
		
        request.setAttribute("messages",messages);
        request.setAttribute("paramMap",paramMap);
		return mapping.findForward("specValLst");
	}
	
	/** 
	 * 
	 * 响应用户修改汇率的请求
	 * 导航至 sysManage/SpecValidateInfo/editSpecValidateInfo.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward toUpdate(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,	HttpServletResponse response) {
		
		SpecialValForm dataForm = (SpecialValForm)form;

        String curPage = null;
        
        Map paramMap = new HashMap();
        if(request.getParameter("curPage") != null){
            curPage = request.getParameter("curPage");
            paramMap.put("curPage",curPage);
        }
        
		if(dataForm.getValName()!=null && !dataForm.getValName().equals("")){			
            paramMap.put("valName",dataForm.getValName());
		}
		
        request.setAttribute("paramMap",paramMap);
        
		if(request.getParameter("speValId") != null){
			
			String erId = request.getParameter("speValId");
			
			if(erId != null){
				SpecValidateInfo specValidateInfo = (SpecValidateInfo)
					StrutsSpecValidateInfoDelegate.getSpecValById(Long.valueOf(erId));
				
				if(specValidateInfo != null){
					
					dataForm.setValName(specValidateInfo.getValName());
					dataForm.setValFormula(specValidateInfo.getValFormula());
					dataForm.setValDes(specValidateInfo.getValDes());
					
					request.setAttribute("specValidateInfo", specValidateInfo);
				}
			}
		}
		
		return mapping.findForward("specValMgr_update");
	}
	
	/** 
	 * 
	 * 响应用户完成修改汇率的请求
	 * 导航至 sysManage/SpecValidateInfo/eidtSpecValidateInfo.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,	HttpServletResponse response) {	
		
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		
		//从form中获取数据
		SpecialValForm dataForm = (SpecialValForm)form;
						
		Map paramMap = new HashMap();
		
		//更新汇率
		if(null != dataForm.getSpeValId() && !"".equals(dataForm.getSpeValId())
				&& null != dataForm.getValName() && !"".equals(dataForm.getValName())
				&& null != dataForm.getValFormula() && !"".equals(dataForm.getValFormula())
				&& null != dataForm.getValDes() && !"".equals(dataForm.getValDes())){
			
			SpecValidateInfo specValidateInfo = new SpecValidateInfo();

			specValidateInfo.setSpeValId(dataForm.getSpeValId());
			specValidateInfo.setValName(dataForm.getValName());
			specValidateInfo.setValFormula(dataForm.getValFormula());
			specValidateInfo.setValDes(dataForm.getValDes());
			
			if(StrutsSpecValidateInfoDelegate.saveOrUpdateSpecVal(specValidateInfo,"update")){
				messages.add("更新成功");
			}else{
				messages.add("更新失败");
			}
			
			specValidateInfo = StrutsSpecValidateInfoDelegate.getSpecValById(dataForm.getSpeValId());
			
			request.setAttribute("specValidateInfo", specValidateInfo);
			request.setAttribute("messages",messages);
		}

        request.setAttribute("paramMap",paramMap);
        
		return mapping.findForward("specValMgr_update");
	}
	
	/** 
	 * 响应用户删除汇率的请求导航至/sysManage/SpecValidateInfo/viewSpecValidateInfo.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();
		
		SpecialValForm dataForm = (SpecialValForm)form;
		
        Map paramMap = new HashMap();
		String url = request.getRequestURL() + "?method=viewSpecVal";
		
		//从form中获取数据
		String[] speValId = request.getParameterValues("speValId");
		
		SpecValidateInfo specValInfo = new SpecValidateInfo();
		
		//获取查询的参数
		String valName = request.getParameter("valName");
		if(null != valName && !"".equals(valName)){
			valName = request.getParameter("valName");
			url = url + "&valName=" + valName;
			
			specValInfo.setValName(valName);
            paramMap.put("valName",valName);
		}

		//分页设置
		int curPage = 1;
		ApartPage apartPage = new ApartPage();		
		if(request.getParameter("currPage") != null){
			curPage = Integer.parseInt(request.getParameter("curPage"));
			url = url + "&curPage=";
		}
		apartPage.setCurPage(curPage);
        paramMap.put("curPage",new Integer(curPage));
        request.setAttribute("paramMap",paramMap);
        
		//删除汇率
		if(speValId != null && speValId.length > 0){
			if(StrutsSpecValidateInfoDelegate.deleteSpecValById(speValId)){
				messages.add("删除成功！");			
			}else{
				messages.add("删除失败！");
			}
			request.setAttribute("messages",messages);
		}else{
			messages.add("缺少删除条件！");
		}

		//汇率集合
		List specValInfoLst = StrutsSpecValidateInfoDelegate.getSpecValidateInfos(dataForm,apartPage);
		
		if(specValInfoLst != null && !specValInfoLst.isEmpty()){
			request.setAttribute("specValInfoLst",specValInfoLst);
            request.setAttribute("apartPage", apartPage);
            request.setAttribute("url", url);
		}
				
		return mapping.findForward("specValLst");
	}
}