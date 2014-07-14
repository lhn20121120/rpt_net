package com.cbrc.smis.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.adapter.StrutsInfoFilesDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;

public class ViewOutInfoFilesAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		int recordCount =0; //记录总数
		int offset=0; //偏移量
		int limit=0;  //每页显示的记录条数

		
		ApartPage aPage=new ApartPage();
	   	String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
		    if(!strCurPage.equals(""))
		      aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		//计算偏移量
		offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
		limit = Config.PER_PAGE_ROWS;	
	
		try {
			recordCount = StrutsInfoFilesDelegate.getTotalOutFile();
			List list=StrutsInfoFilesDelegate.viewoutfile(offset,limit);
			if(list!=null && list.size()!=0)
				  request.setAttribute(Config.RECORDS,list);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		把ApartPage对象存放在request范围内
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
		return mapping.findForward("view");

	}
	


}
