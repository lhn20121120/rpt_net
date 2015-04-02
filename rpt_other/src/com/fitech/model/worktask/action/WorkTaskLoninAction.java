package com.fitech.model.worktask.action;

import com.fitech.model.worktask.service.IWorkTaskLoginService;

public class WorkTaskLoninAction extends WorkTaskBaseAction{

	@Override
	public String initMethod() throws Exception {
		// TODO Auto-generated method stub
		IWorkTaskLoginService service = (IWorkTaskLoginService)this.getBean("testWebService");
		System.out.println(service.getUerName("ssss"));
		return null;
	}

}
