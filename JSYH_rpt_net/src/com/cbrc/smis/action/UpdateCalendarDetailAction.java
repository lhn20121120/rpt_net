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

import com.cbrc.smis.adapter.StrutsCalendarDetailDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.CalendarDetailForm;
import com.cbrc.smis.other.InnerToGather;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
import com.gather.adapter.StrutsJieKou;

/**
 * 更新工作日历
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/system_mgr/updateCalendarDetail"
 *    name="CalendarDetailForm"
 *    scope="request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="calendar_mgr"
 *    path="/system_mgr/calendar_mgr.jsp"
 *    redirect="false"
 *

 */
public final class UpdateCalendarDetailAction extends Action {
	private static FitechException log = new FitechException(UpdateCalendarDetailAction.class);
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {

	   List recList=null;
	   Locale locale = this.getLocale(request);
	   MessageResources resources=getResources(request);
	   FitechMessages messages = new FitechMessages();
	   //将查询条件
	   CalendarDetailForm calendarDetailForm = new CalendarDetailForm();
	   RequestUtils.populate(calendarDetailForm,request);
	   
	   try 
	   {
		   if(calendarDetailForm!=null)
		   {
			   int recordCount = StrutsCalendarDetailDelegate.getRecordCount(calendarDetailForm);
			   if(recordCount>0)//如果该年月设置过工作日，则删除
			   {
				   boolean result = StrutsCalendarDetailDelegate.remove(calendarDetailForm);
				  
				   
				   if(result==false)//删除失败
				   {
                       //// System.out.println("**********Delete Fail*******");
					   messages.add(FitechResource.getMessage(locale,resources,"save.fail","calendar.info"));
                       request.setAttribute(Config.MESSAGES,messages);
                       return new ActionForward(this.getViewPath(calendarDetailForm));
                   }
			   }
			   //插入数据库
			   boolean result = StrutsCalendarDetailDelegate.create(calendarDetailForm);
			   
			   if(result!=true)
			   {
                   //// System.out.println("**********Create Fail*******");
				   messages.add(FitechResource.getMessage(locale,resources,"save.fail","calendar.info"));
			   }
               else
            	   //从工作明细表中查处所有符合记录的集合
    			   recList=StrutsCalendarDetailDelegate.select(calendarDetailForm);
			   		
			   		//如果recList不为空或尺寸大于0，则调用外网的方法Create开始操作
    			   	if(recList!=null && recList.size()>0){
    			   		//如果查找到的记录符合条件开始转换reclist为gather中的list对象
    			   		List insertResult=InnerToGather.insertCalendarDetail(recList);
    			   			//调用StrutsJiekou中的方法插入数据到外网数据库
    			   		boolean revResult=StrutsJieKou.create(insertResult);

    			   		if(revResult==true){
    			   			messages.add(FitechResource.getMessage(locale,resources,"save.success","calendar.info")); 
    			   		}else{
    			   			messages.add(FitechResource.getMessage(locale,resources,"save.fail","calendar.info"));
    			   		}
    			   	}
                }
		   else //无查询条件
           {
               //// System.out.println("**********Form Null*******");
			   messages.add(FitechResource.getMessage(locale,resources,"save.fail","calendar.info"));	
           }
	   }
	   catch (Exception e) 
	   {
	       log.printStackTrace(e);
	       messages.add(FitechResource.getMessage(locale,resources,"save.fail","calendar.info"));	
	   }
	   //移除request或session范围内的属性
	   FitechUtil.removeAttribute(mapping,request);

	   if(messages.getMessages() != null && messages.getMessages().size() > 0)
		   request.setAttribute(Config.MESSAGES,messages);
	   
	   return new ActionForward(this.getViewPath(calendarDetailForm));
   }
   /**
    * 获得viewAction的path (加过查询条件后的)
    * @param calendarDetailForm 包含查询条件
    * @return 
    */
   private String getViewPath(CalendarDetailForm calendarDetailForm)
   {
	   StringBuffer path=new StringBuffer("/system_mgr/viewCalendarDetail.do");
	   	//取得查询条件
	   Integer calYear = calendarDetailForm.getCalYear();
	   Integer calMonth = calendarDetailForm.getCalMonth();
	   //Integer calId = calendarDetailForm.getCalId();
	   if(calYear!=null && calMonth!=null)
		   path.append("?calYear="+calYear.intValue()+"&calMonth="+calMonth.intValue());//+"&calId="+calId.intValue());   
	  // // System.out.println("PATH======="+path);
       return path.toString();

   }
}
