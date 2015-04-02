package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsOrgDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.OrgForm;
import com.cbrc.smis.util.FitechMessages;

public class ViewOrgAction extends Action {
	/**
	 * Performs action.
	 * 
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		
		FitechMessages messages = new FitechMessages();
		
		OrgForm orgForm=(OrgForm)form;
		RequestUtils.populate(orgForm,request);
		
		int recordCount =0; //��¼����
        int offset=0; //ƫ����
        int limit=0;  //ÿҳ��ʾ�ļ�¼����
        List list = null;
        
        ApartPage aPage=new ApartPage();
        String strCurPage=request.getParameter("curPage");
        if(strCurPage!=null){
            if(!strCurPage.equals(""))
              aPage.setCurPage(new Integer(strCurPage).intValue());
        }else{
            aPage.setCurPage(1);
        }
        //����ƫ����
        offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
        limit = Config.PER_PAGE_ROWS;   
        
        //ȡ�ü�¼����
        recordCount = StrutsOrgDelegate.getCount(orgForm);
        //��ʾ��ҳ��ļ�¼
        if(recordCount>0) list = StrutsOrgDelegate.findAll(orgForm,offset,limit);
        
        //�Ƴ�request��session��Χ�ڵ�����
        //FitechUtil.removeAttribute(mapping,request);
        //��ApartPage��������request��Χ��
        String term="";
        if(orgForm!=null){
        	if(orgForm.getOrgSrhName()!=null && !orgForm.getOrgSrhName().equals("")){
				term+=(term.equals("")?"":"&") + "orgSrhName=" + orgForm.getOrgSrhName();
				request.setAttribute("OrgSrhName",orgForm.getOrgSrhName());
			}
			if(orgForm.getOrgClsId()!=null && !orgForm.getOrgClsId().equals("")){
				term+=(term.equals("")?"":"&") + "orgClsId=" + orgForm.getOrgClsId();
				request.setAttribute("OrgClsId",orgForm.getOrgClsId());
			}
			request.setAttribute("CurPage",strCurPage);
        }
        aPage.setTerm(term);
        aPage.setCount(recordCount);
        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
        
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
		if(list!=null && list.size()!=0) request.setAttribute(Config.RECORDS,list);
		
		return mapping.findForward("view");
	}
}
