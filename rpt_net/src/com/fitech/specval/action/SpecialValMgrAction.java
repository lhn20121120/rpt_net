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
 * ����У�鴦��DispatchAction
 * @author 	cb
 * @date	2007-07-27
 * 
 */

public class SpecialValMgrAction extends DispatchAction {

	/** 
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����SpecValidateInfo
	 * ��Ӧ�û���URL��ҳ����������������
	 * ������ specval/viewSpecVal.jsp
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
		//��ȡURL
		String url = request.getRequestURL() + "?method=viewSpecVal";
		
		SpecValidateInfo specValInfo = new SpecValidateInfo();

		//��ò�ѯ����
		if (request.getParameter("valName") != null) {
			String valName = request.getParameter("valName");
			specValInfo.setValName(valName);
            url = url + "&valName=" + valName;
            paramMap.put("valName",valName);
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
        
		//У����Ϣ����
        /**��ʹ��hibernate ���Ը� 2011-12-28
         * Ӱ�����SpecValidateInfo*/
        specValInfoLst = StrutsSpecValidateInfoDelegate.getSpecValidateInfos(dataForm,apartPage);
		if (specValInfoLst != null && !specValInfoLst.isEmpty()) {
			request.setAttribute("specValInfoLst", specValInfoLst);
			request.setAttribute("url",url);
			request.setAttribute("apartPage", apartPage);
		}

		return mapping.findForward("specValLst");
	}

	/** 
	 * ��ʹ��hibernate ���Ը� 2011-12-28
	 * Ӱ�����SpecValidateInfo
	 * ��Ӧ�û������ѯ������ѯ���ʵ�����
	 * ������ sysManage/SpecValidateInfo/viewSpecValidateInfo.jsp
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
		
		//��ȡURL
		String url = request.getRequestURL().toString();
		url = url + "?method=viewSpecVal";
		
		//��ҳ����
		int curPage = 1;
		ApartPage apartPage = new ApartPage();
		
		SpecValidateInfo specValInfo = new SpecValidateInfo();


		if(dataForm.getValName()!=null && !dataForm.getValName().equals("")){
			url = url + "&valName=" + dataForm.getValName();			
	        paramMap.put("valName", dataForm.getValName());
		}
		
		apartPage.setTerm(url);
		url = url + "&curPage=";
		
		//���õ�ǰҳ
		apartPage.setCurPage(curPage);
        paramMap.put("curPage",new Integer(curPage));
		request.setAttribute("paramMap",paramMap);
		
		//���ּ���
		/**��ʹ��hibernate ���Ը� 2011-12-28
		 * Ӱ�����SpecValidateInfo*/
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
	 * ��Ӧ�û����ӻ��ʵ�����
	 * ������ sysManage/SpecValidateInfo/addSpecValidateInfo.jsp
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
	 * ��Ӧ�û�������ӻ��ʵ�����
	 * ������ sysManage/SpecValidateInfo/viewSpecValidateInfo.jsp
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
		//��ҳԤ����
		String url = request.getRequestURL()+"?method=viewSpecVal";
		ApartPage apartPage = new ApartPage();
		
        paramMap.put("curPage",new Integer("1"));
        request.setAttribute("paramMap",paramMap);
      
		// ����У����
        SpecValidateInfo specValInfo = new SpecValidateInfo();
		if(null != dataForm.getValName() && !"".equals(dataForm.getValName())
				&& null != dataForm.getValFormula() && !"".equals(dataForm.getValFormula())
				&& null != dataForm.getValDes() && !"".equals(dataForm.getValDes())){
			
			specValInfo.setValName(dataForm.getValName());
			specValInfo.setValFormula(dataForm.getValFormula());
			specValInfo.setValDes(dataForm.getValDes());
			
			if(StrutsSpecValidateInfoDelegate.saveOrUpdateSpecVal(specValInfo,"new")){
				messages.add("�����ɹ���");
                //paramMap.put("currCode",currCode1);
                //url = url + "&currCode" + currCode1;
			}else{
				messages.add("����ʧ�ܣ�");
			}
		}else{
			messages.add("ȱ��������3����Ϊ���");
		}
		
        apartPage.setTerm(url);
        url = url +"&curPage=";
		
        //У�鼯��
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
	 * ��Ӧ�û��޸Ļ��ʵ�����
	 * ������ sysManage/SpecValidateInfo/editSpecValidateInfo.jsp
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
	 * ��Ӧ�û�����޸Ļ��ʵ�����
	 * ������ sysManage/SpecValidateInfo/eidtSpecValidateInfo.jsp
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
		SpecialValForm dataForm = (SpecialValForm)form;
						
		Map paramMap = new HashMap();
		
		//���»���
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
				messages.add("���³ɹ�");
			}else{
				messages.add("����ʧ��");
			}
			
			specValidateInfo = StrutsSpecValidateInfoDelegate.getSpecValById(dataForm.getSpeValId());
			
			request.setAttribute("specValidateInfo", specValidateInfo);
			request.setAttribute("messages",messages);
		}

        request.setAttribute("paramMap",paramMap);
        
		return mapping.findForward("specValMgr_update");
	}
	
	/** 
	 * ��Ӧ�û�ɾ�����ʵ����󵼺���/sysManage/SpecValidateInfo/viewSpecValidateInfo.jsp
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
		
		//��form�л�ȡ����
		String[] speValId = request.getParameterValues("speValId");
		
		SpecValidateInfo specValInfo = new SpecValidateInfo();
		
		//��ȡ��ѯ�Ĳ���
		String valName = request.getParameter("valName");
		if(null != valName && !"".equals(valName)){
			valName = request.getParameter("valName");
			url = url + "&valName=" + valName;
			
			specValInfo.setValName(valName);
            paramMap.put("valName",valName);
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
		if(speValId != null && speValId.length > 0){
			if(StrutsSpecValidateInfoDelegate.deleteSpecValById(speValId)){
				messages.add("ɾ���ɹ���");			
			}else{
				messages.add("ɾ��ʧ�ܣ�");
			}
			request.setAttribute("messages",messages);
		}else{
			messages.add("ȱ��ɾ��������");
		}

		//���ʼ���
		List specValInfoLst = StrutsSpecValidateInfoDelegate.getSpecValidateInfos(dataForm,apartPage);
		
		if(specValInfoLst != null && !specValInfoLst.isEmpty()){
			request.setAttribute("specValInfoLst",specValInfoLst);
            request.setAttribute("apartPage", apartPage);
            request.setAttribute("url", url);
		}
				
		return mapping.findForward("specValLst");
	}
}