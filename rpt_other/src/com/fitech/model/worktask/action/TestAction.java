package com.fitech.model.worktask.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.service.IWorkTaskMoniService;
import com.fitech.model.worktask.service.TestService;

public class TestAction extends WorkTaskBaseAction{
	
	private TestService testService;
	
	public TestService getTestService() {
		return testService;
	}
	public void setTestService(TestService testService) {
		this.testService = testService;
	}
	@Override
	public String initMethod() throws Exception {
		this.println("开始。。。");
		this.testService.saveWorkTaskType();
		IWorkTaskMoniService bean = (IWorkTaskMoniService)this.getBean("workTaskMoniService");
		bean.insertWorkTaskAll("month",true);
		return null;
	}
	public static void main(String[] args)throws Exception{
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.HOUR, 40);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(cal.getTime()));
	}
}
