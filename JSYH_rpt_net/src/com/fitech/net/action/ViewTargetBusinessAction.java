package com.fitech.net.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsTargetDelegate;
import com.fitech.net.form.TargetBusinessForm;

/**
 * 指标业务类型查看
 * @author masclnj
 *
 */
public final class ViewTargetBusinessAction extends Action {
	private static FitechException log = new FitechException(ViewTargetBusinessAction.class); 

   /**
    *@list 数据集合List 保存浏览的数据
    * @exception IOException  IO异常
    * @exception ServletException  ServletException异常
    */
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	  FitechMessages messages = new FitechMessages();
      MessageResources resources = getResources(request);
	   
      // 是否有Request
      TargetBusinessForm targetBusiness = (TargetBusinessForm)form;
      //// System.out.println("DataRgDesc==============="+mCurrForm.getDataRgDesc());
      RequestUtils.populate(targetBusiness, request);
  	
		
		int recordCount =0; //记录总数
		int offset=0; //偏移量
		int limit=0;  //每页显示的记录条数

      //List对象的初始化
      List resList=null;
      //
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
		
		try 
		{
	   		//取得记录总数
	   		recordCount = StrutsTargetDelegate.getRecordCount(targetBusiness);
	   		//显示分页后的记录
	   		if(recordCount > 0)
		   	    resList = StrutsTargetDelegate.select(targetBusiness,offset,limit);  
		}
		catch (Exception e) 
		{
			log.printStackTrace(e);
			messages.add(resources.getMessage("log.select.fail"));		
		}
		//移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping,request);
		//把ApartPage对象存放在request范围内		
	 	aPage.setTerm(this.getTerm(targetBusiness));
	 	aPage.setCount(recordCount);
	 	request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
		
	 if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   	  request.setAttribute(Config.MESSAGES,messages);
      //如果StrutsMCurrDelegate类中返回的reslist对象不为空并且对象的大小大于0，
      //则返回一个包含reslist集合的request对象
      if(resList!=null && resList.size()>0) request.setAttribute(Config.RECORDS,resList);
    //返回到页面view     
      return mapping.findForward("view_business");
   }
   
   public String getTerm(TargetBusinessForm targetBusiness)
   {
	   String term="";
	   
	   Integer businesslId = targetBusiness.getBusinessId();
	   String businessName = targetBusiness.getBusinessName();
	   String businessNote = targetBusiness.getBusinessNote();
	   
	   if(businesslId!=null)
	   {
		   term += (term.indexOf("")>=0 ? "" : "&");
		   term += "curId="+businesslId.toString();   
	   }
	   if(businessName!=null && !businessName.equals(""))
	   {
		   term += (term.indexOf("")>=0 ? "" : "&");
		   term += "curName="+businessName;   
	   }
	   if(businessNote!=null && !businessNote.equals(""))
	   {
		   term += (term.indexOf("")>=0 ? "" : "&");
		   term += "curName="+businessNote;   
	   }
	   return term;
   }
   }