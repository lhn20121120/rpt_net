package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.cbrc.smis.form.CalendarDetailForm;
import com.cbrc.smis.other.CalendarCell;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
/**
 * 查询工作日历
 *
 * @author 姚捷
 *
 * @struts.action
 *    path="/system_mgr/viewCalendarDetail"
 *
 * @struts.action-forward
 *    name="calendar_mgr"
 *    path="/system_mgr/calendar_mgr.jsp"
 *    redirect="false"
 *

 */
public final class ViewCalendarDetailAction extends Action {
	private static FitechException log = new FitechException(ViewCalendarDetailAction.class);  
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
	   
	   Locale locale = this.getLocale(request);
	   MessageResources resources=getResources(request);
	   FitechMessages messages = new FitechMessages();
	   //将查询条件
	   CalendarDetailForm calendarDetailForm = new CalendarDetailForm();
	   RequestUtils.populate(calendarDetailForm,request);
	   //// System.out.println("Year In viewAction ="+calendarDetailForm.getCalYear());
       //// System.out.println("Month In viewAction ="+calendarDetailForm.getCalMonth());
       
	   ArrayList list = new ArrayList();
	   
	   try 
	   {
		   //初始化全都为空，35个单元格，5行7列,相当于一个日历
		   for(int i=0;i<35;i++)
			   list.add("");				   
		
		   //设置日期
		   Calendar calendar = Calendar.getInstance();
		   int date = 1;
		   calendar.set(Calendar.YEAR,calendarDetailForm.getCalYear().intValue());
		   calendar.set(Calendar.MONTH,calendarDetailForm.getCalMonth().intValue()-1);
		   calendar.set(Calendar.DATE,date);
		   
		   //取得该月1号是星期几，计算出偏移量
		   int offset = calendar.get(Calendar.DAY_OF_WEEK)-1;
		   
		   //取得该月的总天数
		   int day_of_month = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
		   
		   //计算日期写的位置
		   int endPos = 35;
		   if( (offset + day_of_month) < 35)
			   endPos = offset + day_of_month;
		   //把当月的日历写入list
		   for(int i=offset; i<endPos; i++)
		   {
			   CalendarCell cell = getCalendar(date,calendarDetailForm);
			   list.add(i,cell);
			   date++;
		   }
		   
		   //处理那些30号和31号超出日历长度的月份,把这些日期,放到list的前面
		   if((offset + day_of_month - 35) > 0)
		   {
			   for(int i=0;i<(offset + day_of_month - 35);i++)
			   {
				   CalendarCell cell = getCalendar(date,calendarDetailForm);
				   list.set(i,cell);
				   date++; 
			   }   
		   }
		 List result = StrutsCalendarDetailDelegate.select(calendarDetailForm);
		 //同步数据库的值
		 if(result!=null && result.size()!=0)
		  {
			 for(int i=0;i<list.size();i++)
			 {
				 if(!list.get(i).equals(""))
				 {
					 CalendarCell cell = (CalendarCell)list.get(i);
					 for(int j=0;j<result.size();j++)
					 {
						 int resultDate = ((CalendarDetailForm)result.get(j)).getCalDay().intValue();
						 if(cell.getDay() == resultDate)//数据库中该日期是否是工作日
						 {
							//日历类别
							int calTypeId = ((CalendarDetailForm)result.get(j)).getCalTypeId().intValue();
							if(calTypeId == 0)//非工作日
							 cell.setSetting(false);
							else //工作日
							 cell.setSetting(true);
							break;
						 }
					 }				 
				 }	 
			 }
		  }
	   } 
	   catch (Exception e) {
		   log.printStackTrace(e);
		   messages.add(FitechResource.getMessage(locale,resources,"select.fail","calendar.info"));	
	   }
	   //清除request或session范围内的属性
	   FitechUtil.removeAttribute(mapping,request);
	   //
	   request.setAttribute("Calendar",list);
	   request.setAttribute("year",calendarDetailForm.getCalYear());
	   request.setAttribute("month",calendarDetailForm.getCalMonth());
	   //request.setAttribute("selectCalId",calendarDetailForm.getCalId());
	   return mapping.findForward("calendar_mgr");
   }
   /**
    * 获得日期单元格
    * @param date 日期
    * @param calendarDetailForm 包含年月的值
    * @return 设置属性的单元格对象
    */
   private CalendarCell getCalendar(int date,CalendarDetailForm calendarDetailForm)
   {
	   	//日历的一个单元格
	   	CalendarCell cell = new CalendarCell();
	   	//单元格所显示的日期
	   	cell.setDay(date);
	   
	   	Calendar cal = Calendar.getInstance();
		cal.set(calendarDetailForm.getCalYear().intValue(),calendarDetailForm.getCalMonth().intValue()-1,date);
//      根据一般的时间设定工作日和非工作日（星期六，日是非工作日）
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
	    
        if(day_of_week ==1 || day_of_week==7)
	    	cell.setSetting(false);
	    else
	    	cell.setSetting(true);
	    return cell;
   }
}
