package com.gather.servlets;

import java.util.Date;
import java.util.Timer;

import javax.servlet.http.HttpServlet;

import com.gather.common.Cleaner;
import com.gather.common.DateUtil;
import com.gather.common.Log;

public class Tasks extends HttpServlet {


   public void init(){
	   Timer timer=new Timer();
	   String time="23:59:00";
	   String date=DateUtil.toSimpleDateFormat(new Date(),DateUtil.NORMALDATE);
	   //// System.out.println("dateStr is: "+date+" "+time);
	   Date toDate=DateUtil.getDateByString(date+" "+time,DateUtil.DATA_TIME);
	   
	   Date currentTime=new Date();
	   //// System.out.println("currentTime is"+DateUtil.toSimpleDateFormat(currentTime,DateUtil.DATA_TIME));
	   long difference=toDate.getTime()-currentTime.getTime();
	   //// System.out.println("toDate.getTime() is: "+toDate.getTime()+" currentTime.getTime() is: "+currentTime.getTime());
	   //// System.out.println("difference is: "+difference+" and 时间差距是"+(difference/(60*60*1000))+"小时");
	   long oneDay=24*60*60*1000;
	   //long delayTime=1;
	   timer.schedule(new Cleaner(),difference,oneDay);
	   new Log(Tasks.class).info(":::task was start and delayTime is: "+difference);
   }

}
