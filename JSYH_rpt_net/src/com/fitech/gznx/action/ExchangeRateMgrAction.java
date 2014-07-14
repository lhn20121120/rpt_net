package com.fitech.gznx.action;

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
import com.fitech.gznx.service.StrutsExchangeRateDelegate;


/** 
 * ���ʴ���DispatchAction
 * @author 	gongming
 * @date	2007-07-27
 * 
 */

public class ExchangeRateMgrAction extends DispatchAction {

	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	/** 
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����VCurrency ExchangeRate
	 * ��Ӧ�û���URL��ҳ����������������
	 * ������ sysManage/exchangeRate/viewExchangeRate.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward viewExchangeRate(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,	HttpServletResponse response) {

        ExchangeRateForm dataForm = (ExchangeRateForm)form;
        dataForm.setRateDate("");
        Map paramMap = new HashMap();
		List exchangeRateLst = null;
		//		��ȡURL
		String url = request.getRequestURL() + "?method=viewExchangeRate";
		
		ExchangeRate rate = new ExchangeRate();
		String rateDate = null;
		String currCode = null;

		if (request.getParameter("sourceVcId") != null) {
			currCode = request.getParameter("sourceVcId");
			rate.setSourceVcId(currCode);//setCurrencyCode1(new VCurrency(null, currCode, null));
			dataForm.setSourceVcId(currCode);//setCurrencyCode1(currCode);
            url = url + "&sourceVcId=" + currCode;
            paramMap.put("sourceVcId",currCode);
		}
//		if(dataForm.getStartDate()!=null && !dataForm.getStartDate().equals("")){
//			url = url + "&startDate=" + dataForm.getStartDate();			
//            paramMap.put("startDate",dataForm.getStartDate());
//		}
//		if(dataForm.getEndDate()!=null && !dataForm.getEndDate().equals("")){
//			url = url + "&endDate=" + dataForm.getEndDate();			
//            paramMap.put("endDate",dataForm.getEndDate());
//		}
		if(dataForm.getRateDate()!=null && !dataForm.getRateDate().equals("")){
			url = url + "&rateDate=" + dataForm.getRateDate();			
	        paramMap.put("rateDate",dataForm.getRateDate());
		}
		
		//��ҳ����
		int curPage = 1;
		ApartPage apartPage = new ApartPage();
        apartPage.setTerm(url);
		if(request.getParameter("curPage") != null){
			curPage = Integer.parseInt(request.getParameter("curPage"));
		}
			url = url + "&curPage=";
       
		//���õ�ǰҳ
		apartPage.setCurPage(curPage);
        paramMap.put("curPage",new Integer(curPage));
        request.setAttribute("paramMap",paramMap);
        
		//���ּ���
        /**��ʹ��hibernate ���Ը� 2011-12-28
         * Ӱ�����ExchangeRate*/
		exchangeRateLst = StrutsExchangeRateDelegate.getExchangeRates(dataForm,apartPage);
		if (exchangeRateLst != null && !exchangeRateLst.isEmpty()) {
			request.setAttribute("exchangeRateLst", exchangeRateLst);
			request.setAttribute("url",url);
			request.setAttribute("apartPage", apartPage);
		}
		//���ּ���
		/**��ʹ��hibernate ���Ը� 2011-12-28
		 * Ӱ�����VCurrency*/
		List currencyLst = StrutsExchangeRateDelegate.getCurrencys();
		if(currencyLst != null && !currencyLst.isEmpty()){
			request.setAttribute("currencyLst",currencyLst);
		}
		request.setAttribute("rate", rate);
		return mapping.findForward("exchangeRateLst");
	}

	/** 
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����VCurrency ExchangeRate
	 * ��Ӧ�û������ѯ������ѯ���ʵ�����
	 * ������ sysManage/exchangeRate/viewExchangeRate.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward query(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,	HttpServletResponse response) {
		
		ExchangeRateForm dataForm = (ExchangeRateForm)form;
		String rateDate = dataForm.getRateDate();
		String currCode = dataForm.getSourceVcId();
	
		List exchangeRateLst = null;
		Map paramMap = new HashMap();
		//		��ȡURL
		String url = request.getRequestURL().toString();
		url = url + "?method=viewExchangeRate";
		
		//��ҳ����
		int curPage = 1;
		ApartPage apartPage = new ApartPage();
		
		ExchangeRate rate = new ExchangeRate();
		if(currCode != null && !"".equals(currCode)){
			//rate.setCurrencyCode1(new Currency(null,currCode,null));
			rate.setSourceVcId(currCode);
			url = url +"&sourceVcId=" + currCode;
            paramMap.put("sourceVcId",currCode);
		}

//		if(dataForm.getStartDate()!=null && !dataForm.getStartDate().equals("")){
//			url = url + "&startDate=" + dataForm.getStartDate();			
//            paramMap.put("startDate",dataForm.getStartDate());
//		}
//		if(dataForm.getEndDate()!=null && !dataForm.getEndDate().equals("")){
//			url = url + "&endDate=" + dataForm.getEndDate();			
//            paramMap.put("endDate",dataForm.getEndDate());
//		}
		if(dataForm.getRateDate()!=null && !dataForm.getRateDate().equals("")){
			url = url + "&rateDate=" + dataForm.getRateDate();			
	        paramMap.put("rateDate",dataForm.getRateDate());
		}
		
		apartPage.setTerm(url);
		url = url + "&curPage=";
		
		//���õ�ǰҳ
		apartPage.setCurPage(curPage);
        paramMap.put("curPage",new Integer(curPage));
		request.setAttribute("paramMap",paramMap);
		
		//���ּ���
		/**��ʹ��hibernate ���Ը� 2011-12-28
		 * Ӱ�����ExchangeRate*/
		exchangeRateLst = StrutsExchangeRateDelegate.getExchangeRates(dataForm,apartPage);
		
		if (exchangeRateLst != null && !exchangeRateLst.isEmpty()) {
			apartPage.setTerm(url.substring(0,url.lastIndexOf("&")));
			request.setAttribute("exchangeRateLst", exchangeRateLst);
			request.setAttribute("url",url);
			request.setAttribute("apartPage", apartPage);
		}
		
		//���ּ���
		/**��ʹ��hibernate ���Ը� 2011-12-28
		 * Ӱ�����VCurrency*/
		List currencyLst = StrutsExchangeRateDelegate.getCurrencys();
		if(currencyLst != null && !currencyLst.isEmpty()){
			request.setAttribute("currencyLst",currencyLst);
		}
		
		request.setAttribute("rate", rate);
		request.setAttribute("StartDate",dataForm.getStartDate());
		request.setAttribute("EndDate",dataForm.getEndDate());
		return mapping.findForward("exchangeRateLst");
	}
	
	/** 
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����VCurrency
	 * ��Ӧ�û����ӻ��ʵ�����
	 * ������ sysManage/exchangeRate/addExchangeRate.jsp
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
		
		ExchangeRateForm dataForm = (ExchangeRateForm)form;
		dataForm.setSourceVcId("USD");
		
        String curPage = null;
        String currCode = null;
        String rateDate = null;
        Map paramMap = new HashMap();
        
        if(request.getParameter("curPage") != null){
            curPage = request.getParameter("curPage");
            paramMap.put("curPage",curPage);
        }
        if(request.getParameter("currCode") != null){
            currCode = request.getParameter("currCode");
            paramMap.put("currCode",currCode);
        }

//        if(dataForm.getStartDate()!=null && !dataForm.getStartDate().equals("")){			
//            paramMap.put("startDate",dataForm.getStartDate());
//		}
//		if(dataForm.getEndDate()!=null && !dataForm.getEndDate().equals("")){			
//            paramMap.put("endDate",dataForm.getEndDate());
//		}
		
		if(dataForm.getRateDate()!=null && !dataForm.getRateDate().equals("")){			
            paramMap.put("rateDate",dataForm.getRateDate());
		}
		
        request.setAttribute("paramMap",paramMap);
		//���ּ���
        /**��ʹ��hibernate ���Ը� 2011-12-28
         * Ӱ�����VCurrency*/
		List currencyLst = StrutsExchangeRateDelegate.getCurrencys();
		
		if(currencyLst != null && !currencyLst.isEmpty()){
			request.setAttribute("currencyLst",currencyLst);
		}
		
		return mapping.findForward("exchangeRateMgr_add");
	}
	
	/** 
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����ExchangeRate VCurrency
	 * ��Ӧ�û�������ӻ��ʵ�����
	 * ������ sysManage/exchangeRate/viewExchangeRate.jsp
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
		//��form�л�ȡ����
		ExchangeRateForm dataForm = (ExchangeRateForm)form;
		String currCode1 = dataForm.getSourceVcId();
		String currCode2 = dataForm.getTargetVcId();
		String rateDate = dataForm.getRateDate();
		String extRate = dataForm.getExtRate();
		
        Map paramMap = new HashMap();
		//��ҳԤ����
		String url = request.getRequestURL()+"?method=viewExchangeRate";
		ApartPage apartPage = new ApartPage();
	
        paramMap.put("curPage",new Integer("1"));
        request.setAttribute("paramMap",paramMap);
      
		// ����������
        ExchangeRate rate = new ExchangeRate();
		if(null != currCode1 && !"".equals(currCode1)
				&& null != currCode2 && !"".equals(currCode2)
				&& null != rateDate && !"".equals(rateDate)
				&& null != extRate){
			
//			rate.setCurrencyCode1(new Currency(null,currCode1,null));
//			rate.setCurrencyCode2(new Currency(null,currCode2,null));
			rate.setSourceVcId(currCode1);
			rate.setTargetVcId(currCode2);
			rate.setRateDate(rateDate);
			rate.setExtRate(extRate);
			
			/**��ʹ��hibernate ���Ը� 2011-12-28
			 * Ӱ�����ExchangeRate*/
			if(StrutsExchangeRateDelegate.saveOrUpdateExchangeRate(rate,"new")){
				messages.add(FitechResource.getMessage(locale, resources, "add.success","sysInfo.exchangeRate"));
                paramMap.put("currCode",currCode1);
                url = url + "&currCode" + currCode1;
			}else{
				messages.add(FitechResource.getMessage(locale, resources, "add.failedcausesam","sysInfo.exchangeRate"));
			}
		}else{
			messages.add(FitechResource.getMessage(locale, resources, "add.failed","sysInfo.exchangeRate"));
		}
        apartPage.setTerm(url);
        url = url +"&curPage=";
		//���ʼ���
        /**��ʹ��hibernate ���Ը� 2011-12-28
         * Ӱ�����ExchangeRate*/
        List exchangeRateLst = StrutsExchangeRateDelegate.getExchangeRates(dataForm,apartPage);
        if(exchangeRateLst != null && !exchangeRateLst.isEmpty()){
            request.setAttribute("exchangeRateLst",exchangeRateLst);
            request.setAttribute("apartPage", apartPage);
            request.setAttribute("url", url);
        }
		
		//���ּ���
        /**��ʹ��hibernate ���Ը� 2011-12-28
         * Ӱ�����VCurrency*/
		List currencyLst = StrutsExchangeRateDelegate.getCurrencys();
		if(currencyLst != null && !currencyLst.isEmpty()){
			request.setAttribute("currencyLst",currencyLst);
		}
        dataForm.setRateDate("");
        request.setAttribute("messages",messages);
        request.setAttribute("paramMap",paramMap);
		return mapping.findForward("exchangeRateLst");
	}
	
	/** 
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����ExchangeRate VCurrency
	 * ��Ӧ�û��޸Ļ��ʵ�����
	 * ������ sysManage/exchangeRate/editExchangeRate.jsp
	 * @param mapping			ActionMapping
	 * @param form				ActionForm
	 * @param request			HttpServletRequest
	 * @param response			HttpServletResponse
	 * @return ActionForward	
	 */
	public ActionForward toUpdate(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request,	HttpServletResponse response) {
		
		ExchangeRateForm dataForm = (ExchangeRateForm)form;
		String erId = null;
        String curPage = null;
        String currCode = null;
        String rateDate = null;
        
        Map paramMap = new HashMap();
        if(request.getParameter("curPage") != null){
            curPage = request.getParameter("curPage");
            paramMap.put("curPage",curPage);
        }
        if(request.getParameter("currCode") != null){
            currCode = request.getParameter("currCode");
            paramMap.put("currCode",currCode);
        }

//        if(dataForm.getStartDate()!=null && !dataForm.getStartDate().equals("")){			
//            paramMap.put("startDate",dataForm.getStartDate());
//		}
//		if(dataForm.getEndDate()!=null && !dataForm.getEndDate().equals("")){			
//            paramMap.put("endDate",dataForm.getEndDate());
//		}
        
		if(dataForm.getRateDate()!=null && !dataForm.getRateDate().equals("")){			
            paramMap.put("rateDate",dataForm.getRateDate());
		}
		
        request.setAttribute("paramMap",paramMap);
        
		if(request.getParameter("erId") != null){
			erId = request.getParameter("erId");
			if(erId != null){
				/**��ʹ��hibernate ���Ը� 2011-12-28
				 * Ӱ�����ExchangeRate*/
				ExchangeRate exchangeRate = (ExchangeRate)StrutsExchangeRateDelegate.getExchangeRateById(new Long(erId));
				if(exchangeRate != null){
					dataForm.setSourceVcId(exchangeRate.getSourceVcId());
					dataForm.setTargetVcId(exchangeRate.getTargetVcId());
					request.setAttribute("exchangeRate", exchangeRate);
				}
			}
		}
		//���ּ���
		/**��ʹ��hibernate ���Ը� 2011-12-28
		 * Ӱ�����VCurrency*/
		List currencyLst = StrutsExchangeRateDelegate.getCurrencys();
		if(currencyLst != null && !currencyLst.isEmpty()){
			request.setAttribute("currencyLst",currencyLst);
		}
		return mapping.findForward("exchangeRateMgr_update");
	}
	
	/** 
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����ExchangeRate VCurrency
	 * ��Ӧ�û�����޸Ļ��ʵ�����
	 * ������ sysManage/exchangeRate/eidtExchangeRate.jsp
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
		
		//��form�л�ȡ����
		ExchangeRateForm dataForm = (ExchangeRateForm)form;
		
		Long erId = dataForm.getErId();
		String currCode1 = dataForm.getSourceVcId();
		String currCode2 = dataForm.getTargetVcId();
		String rateDate = dataForm.getRateDate();
		
		String extRate = dataForm.getExtRate();
		
		Map paramMap = new HashMap();
		
		//���»���
		if(null != currCode1 && !"".equals(currCode1)
				&& null != currCode2 && !"".equals(currCode2)
				&& null != rateDate && !"".equals(rateDate)
				&& null != extRate && null != erId){
			
			ExchangeRate rate = new ExchangeRate();
//			rate.setCurrencyCode1(new Currency(null,currCode1,null));
//			rate.setCurrencyCode2(new Currency(null,currCode2,null));
			rate.setSourceVcId(currCode1);
			rate.setTargetVcId(currCode2);
			rate.setRateDate(rateDate);
			rate.setExtRate(extRate);
			rate.setErId(erId);
			
//			rate.setStarExpiDate(dataForm.getStarExpiDate());
//			rate.setOverExpiDate(dataForm.getOverExpiDate());
			
            paramMap.put("currCode",currCode1);
            paramMap.put("rateDate",rateDate);
            
            /**��ʹ��hibernate ���Ը� 2011-12-28
             * Ӱ�����ExchangeRate*/
			if(StrutsExchangeRateDelegate.saveOrUpdateExchangeRate(rate,"update")){
				messages.add(FitechResource.getMessage(locale, resources, "update.success","sysInfo.exchangeRate"));
			}else{
				messages.add(FitechResource.getMessage(locale, resources, "update.failed","sysInfo.exchangeRate"));
			}
			
			/**��ʹ��hibernate ���Ը� 2011-12-28
			 * Ӱ�����ExchangeRate*/
			rate = StrutsExchangeRateDelegate.getExchangeRateById(erId);
			dataForm.setSourceVcId(rate.getSourceVcId());
			dataForm.setTargetVcId(rate.getTargetVcId());
			
			request.setAttribute("exchangeRate", rate);
			request.setAttribute("messages",messages);
		}		
		//���ּ���
		/**��ʹ��hibernate ���Ը� 2011-12-28
		 * Ӱ�����VCurrency*/
		List currencyLst = StrutsExchangeRateDelegate.getCurrencys();
		if(currencyLst != null && !currencyLst.isEmpty()){
			request.setAttribute("currencyLst",currencyLst);
		}	
        request.setAttribute("paramMap",paramMap);
		return mapping.findForward("exchangeRateMgr_update");
	}
	
	/** 
	 * ��ʹ��Hibernate ���Ը� 2011-12-28
	 * Ӱ�����ExchangeRate VCurrency
	 * ��Ӧ�û�ɾ�����ʵ����󵼺���/sysManage/exchangeRate/viewExchangeRate.jsp
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
		
		ExchangeRateForm dataForm = (ExchangeRateForm)form;
		
        Map paramMap = new HashMap();
		String url = request.getRequestURL() + "?method=viewExchangeRate";
		//��form�л�ȡ����
		String[] erId = request.getParameterValues("erId");
		ExchangeRate rate = new ExchangeRate();
		//��ȡ��ѯ��ԭ���ֲ���
		String currCode = request.getParameter("currCode");
		if(null != currCode && !"".equals(currCode)){
			currCode = request.getParameter("currCode");
			url = url + "&currCode=" + currCode;
			
//			rate.setCurrencyCode1(new Currency(null,currCode,null));
			rate.setSourceVcId(currCode);
            paramMap.put("currCode",currCode);
		}

		//��ҳ����
		int curPage = 1;
		ApartPage apartPage = new ApartPage();		
		if(request.getParameter("currPage") != null){
			curPage = Integer.parseInt(request.getParameter("curPage"));
			url = url + "&curPage=";
		}
		apartPage.setCurPage(curPage);
        paramMap.put("curPage",new Integer(curPage));
        request.setAttribute("paramMap",paramMap);
		//ɾ������
		
		if(erId != null && erId.length > 0){
			/**��ʹ��hibernate ���Ը� 2011-12-28
			 * Ӱ�����ExchangeRate*/
			if(StrutsExchangeRateDelegate.deleteExchangeRateById(erId)){
				messages.add(FitechResource.getMessage(locale, resources, "delete.success","sysInfo.exchangeRate"));			
			}else{
				messages.add(FitechResource.getMessage(locale, resources, "delete.failed","sysInfo.exchangeRate"));
			}
			request.setAttribute("messages",messages);
		}else{
			messages.add(FitechResource.getMessage(locale, resources, "delete.success","sysInfo.exchangeRate"));
		}
		//���ּ���
		/**��ʹ��hibernate ���Ը� 2011-12-28
		 * Ӱ�����VCurrency*/
		List currencyLst = StrutsExchangeRateDelegate.getCurrencys();
		if(currencyLst != null && !currencyLst.isEmpty()){
			request.setAttribute("currencyLst",currencyLst);
		}
		//���ʼ���
		/**��ʹ��hibernate ���Ը� 2011-12-28
		 * Ӱ�����ExchangeRate*/
		List exchangeRateLst = StrutsExchangeRateDelegate.getExchangeRates(dataForm,apartPage);
		if(exchangeRateLst != null && !exchangeRateLst.isEmpty()){
			request.setAttribute("exchangeRateLst",exchangeRateLst);
            request.setAttribute("apartPage", apartPage);
            request.setAttribute("url", url);
		}
		
		request.setAttribute("rate", rate);
		
		return mapping.findForward("exchangeRateLst");
	}
}