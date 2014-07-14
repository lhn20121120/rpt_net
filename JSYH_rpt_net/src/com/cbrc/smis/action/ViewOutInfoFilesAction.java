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
		int recordCount =0; //��¼����
		int offset=0; //ƫ����
		int limit=0;  //ÿҳ��ʾ�ļ�¼����

		
		ApartPage aPage=new ApartPage();
	   	String strCurPage=request.getParameter("curPage");
		if(strCurPage!=null){
		    if(!strCurPage.equals(""))
		      aPage.setCurPage(new Integer(strCurPage).intValue());
		}
		else
			aPage.setCurPage(1);
		//����ƫ����
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
//		��ApartPage��������request��Χ��
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
		return mapping.findForward("view");

	}
	


}
