package com.cbrc.smis.servlet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.cbrc.org.adapter.StrutsMOrgDelegate;
import com.cbrc.org.form.MOrgForm;
import com.cbrc.smis.adapter.StrutsOrgDelegate;
import com.cbrc.smis.form.OrgForm;
import com.cbrc.smis.util.FitechException;
import com.gather.adapter.StrutsJieKou;
/**
 * 同步外网数据表操作类
 * 
 * @author rds
 * @date 2006-02-04
 */
public class SyncronizedServlet extends HttpServlet {
	private FitechException log=new FitechException(SyncronizedServlet.class);
	
	/**
	 * Init方法
	 * 
	 * @param config ServletConfig
	 * @return void
	 */
	public void init(ServletConfig config) throws ServletException{
		Timer timer=new Timer();
		SMISTimerTask timerTask=new SMISTimerTask();
		// System.out.println("************Start*************");
		Calendar calendar = Calendar.getInstance();
		
		if(config.getInitParameter("day")!=null){
			String _day=config.getInitParameter("day");
			
			try{
				int day=Integer.parseInt(_day);
				
				calendar.set(Calendar.DAY_OF_MONTH,day);
				calendar.set(Calendar.HOUR_OF_DAY, 13);
				calendar.set(Calendar.MINUTE, 53);
				calendar.set(Calendar.SECOND, 0);
			}catch(NumberFormatException nfe){
				log.printStackTrace(nfe);
			}
		}
		
		timer.schedule(timerTask,calendar.getTime(),24*60*60*1000);
		// System.out.println("************End*************");
	}
		
	/**
	 * 消毁
	 * 
	 * @return void
	 */
	public void destroy() {

	}
}

class SMISTimerTask extends TimerTask{
	private FitechException log=new FitechException(SyncronizedServlet.class);
	
	/**
	 * 执行定时器
	 */
	public void run(){
		syncronizedMOrgServlet();
	}
	
	/**
	 * 将机构的数据同步到外网的数据表中
	 * 
	 * @return void
	 */
	private void syncronizedMOrgServlet(){
		int offset=0;     //读取机构类数据的起始位置
		int limit=10;     //每次读取机构的数量
		int time=1;       //读取次数
		
		boolean flag=true;
		List listRes=null;
		
		try{
			MOrgForm mOrgForm=null;

			List mOrgList=null;
			List orgList=null;
			
			while(flag){
				offset=(time-1)*limit;
				listRes=StrutsMOrgDelegate.select(null,offset,limit);
				if(listRes!=null && listRes.size()>0){
					mOrgList=new ArrayList();
					orgList=new ArrayList();
					for(int i=0;i<listRes.size();i++){
						mOrgForm=(MOrgForm)listRes.get(i);
						com.gather.struts.forms.MOrgForm _mOrgForm=new com.gather.struts.forms.MOrgForm();
						_mOrgForm.setOrgId(mOrgForm.getOrgId());
						_mOrgForm.setOrgName(mOrgForm.getOrgName());
						/*_mOrgForm.setOrgType(mOrgForm.getOrgType());
						_mOrgForm.setIsCorp(mOrgForm.getIsCorp());
						_mOrgForm.setIsInUsing(String.valueOf(1));*/
						_mOrgForm.setOrgType(null);
						_mOrgForm.setIsCorp(null);
						_mOrgForm.setIsInUsing(null);
						mOrgList.add(_mOrgForm);
						
						OrgForm orgForm=new OrgForm();
						orgForm.setOrgId(mOrgForm.getOrgId());
						orgForm.setOrgName(mOrgForm.getOrgName());
						orgForm.setOrgType(mOrgForm.getOrgType());
						orgForm.setIsCorp(mOrgForm.getIsCorp());
						orgForm.setOrgClsId(mOrgForm.getOrgClsId());
						orgForm.setOrgClsName(mOrgForm.getOrgClsName());
						orgList.add(orgForm);
					}
					if(mOrgList!=null && mOrgList.size()>0){
						if(StrutsJieKou.createPatchMOrg(mOrgList)==false){
							log.println("将机构数据同步外网失败[" + time + "]!");
						}else{
							log.println("将机构数据同步外网成功[" + time + "]!");
						}
					}
					
					if(orgList!=null && orgList.size()>0){
						if(StrutsOrgDelegate.createPatchMOrg(orgList)==false){
							log.println("同步机构数据失败[" + time + "]!");
						}else{
							log.println("同步机构数据成功[" + time + "]!");
						}
					}
				}else{
					flag=false;
				}
				
				time++;
			}
		}catch(Exception e){
			log.printStackTrace(e);
		}
	}	
}