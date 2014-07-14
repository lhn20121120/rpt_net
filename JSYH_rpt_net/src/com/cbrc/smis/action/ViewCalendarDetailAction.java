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
 * ��ѯ��������
 *
 * @author Ҧ��
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
	   //����ѯ����
	   CalendarDetailForm calendarDetailForm = new CalendarDetailForm();
	   RequestUtils.populate(calendarDetailForm,request);
	   //// System.out.println("Year In viewAction ="+calendarDetailForm.getCalYear());
       //// System.out.println("Month In viewAction ="+calendarDetailForm.getCalMonth());
       
	   ArrayList list = new ArrayList();
	   
	   try 
	   {
		   //��ʼ��ȫ��Ϊ�գ�35����Ԫ��5��7��,�൱��һ������
		   for(int i=0;i<35;i++)
			   list.add("");				   
		
		   //��������
		   Calendar calendar = Calendar.getInstance();
		   int date = 1;
		   calendar.set(Calendar.YEAR,calendarDetailForm.getCalYear().intValue());
		   calendar.set(Calendar.MONTH,calendarDetailForm.getCalMonth().intValue()-1);
		   calendar.set(Calendar.DATE,date);
		   
		   //ȡ�ø���1�������ڼ��������ƫ����
		   int offset = calendar.get(Calendar.DAY_OF_WEEK)-1;
		   
		   //ȡ�ø��µ�������
		   int day_of_month = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
		   
		   //��������д��λ��
		   int endPos = 35;
		   if( (offset + day_of_month) < 35)
			   endPos = offset + day_of_month;
		   //�ѵ��µ�����д��list
		   for(int i=offset; i<endPos; i++)
		   {
			   CalendarCell cell = getCalendar(date,calendarDetailForm);
			   list.add(i,cell);
			   date++;
		   }
		   
		   //������Щ30�ź�31�ų����������ȵ��·�,����Щ����,�ŵ�list��ǰ��
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
		 //ͬ�����ݿ��ֵ
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
						 if(cell.getDay() == resultDate)//���ݿ��и������Ƿ��ǹ�����
						 {
							//�������
							int calTypeId = ((CalendarDetailForm)result.get(j)).getCalTypeId().intValue();
							if(calTypeId == 0)//�ǹ�����
							 cell.setSetting(false);
							else //������
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
	   //���request��session��Χ�ڵ�����
	   FitechUtil.removeAttribute(mapping,request);
	   //
	   request.setAttribute("Calendar",list);
	   request.setAttribute("year",calendarDetailForm.getCalYear());
	   request.setAttribute("month",calendarDetailForm.getCalMonth());
	   //request.setAttribute("selectCalId",calendarDetailForm.getCalId());
	   return mapping.findForward("calendar_mgr");
   }
   /**
    * ������ڵ�Ԫ��
    * @param date ����
    * @param calendarDetailForm �������µ�ֵ
    * @return �������Եĵ�Ԫ�����
    */
   private CalendarCell getCalendar(int date,CalendarDetailForm calendarDetailForm)
   {
	   	//������һ����Ԫ��
	   	CalendarCell cell = new CalendarCell();
	   	//��Ԫ������ʾ������
	   	cell.setDay(date);
	   
	   	Calendar cal = Calendar.getInstance();
		cal.set(calendarDetailForm.getCalYear().intValue(),calendarDetailForm.getCalMonth().intValue()-1,date);
//      ����һ���ʱ���趨�����պͷǹ����գ������������Ƿǹ����գ�
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
	    
        if(day_of_week ==1 || day_of_week==7)
	    	cell.setSetting(false);
	    else
	    	cell.setSetting(true);
	    return cell;
   }
}
