package com.fitech.gznx.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.service.IVorgRelService;
import com.cbrc.smis.service.impl.VorgRelServiceImpl;
import com.fitech.gznx.po.vOrgRel;

public class SearchVorgRelInitAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		int pageNo = 0;
		int pageSize = 0;
		
		ApartPage aPage=new ApartPage();
	    String strCurPage=request.getParameter("curPage");
	        
	    if(strCurPage!=null && !strCurPage.equals(""))
	    {
	       aPage.setCurPage(new Integer(strCurPage).intValue());
	    }
	    else
	       aPage.setCurPage(1);
	        //计算偏移量
	    pageNo=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
	    pageSize = Config.PER_PAGE_ROWS;   
		
	    IVorgRelService relService = new VorgRelServiceImpl();
	    List<vOrgRel> relList = new ArrayList<vOrgRel>();
	    int count = relService.selectCount();
	    if(count>0)
	    	relList = relService.findVorgRelAll(pageNo, pageSize);
		
	    //把ApartPage对象存放在request范围内
        aPage.setCount(count);
        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
        
	    request.setAttribute("relList", relList);
		return mapping.findForward("success");
	}
	
}
