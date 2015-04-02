package com.fitech.model.worktask.interceptor;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fitech.gznx.service.webservice.OperatorService;
import com.fitech.gznx.service.webservice.impl.BaseWebService;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.common.WorkTaskUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class AdminLoginInterceptor extends MethodFilterInterceptor {

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		ActionContext ctx = invocation.getInvocationContext();
		Map session = ctx.getSession();
		com.fitech.model.worktask.security.Operator operator = (com.fitech.model.worktask.security.Operator) session.get(WorkTaskConfig.OPERATOR_SESSION_NAME);
		if(operator==null)
			return "logout";
		OperatorService service = (OperatorService) WorkTaskUtil.getWebServices(OperatorService.class,WorkTaskConfig.loginWebServiceURL);
		String result = service.existsUser(operator.getUserName());
		if (result.equals(BaseWebService.FAIL_FLAG)){
			return "logout";
		}
		return invocation.invoke();
	}

}
