package com.fitech.model.worktask.action;

import java.io.IOException;
import java.util.Map;

import com.fitech.framework.core.web.action.DefaultBaseAction;
import com.fitech.model.worktask.common.WorkTaskConfig;
import com.fitech.model.worktask.security.Operator;
import com.fitech.model.worktask.service.IOpreatorService;

public class LoginSynchronizeAction extends DefaultBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7618263214339645903L;

	
	private IOpreatorService opreatorService;
	
	private Operator op;
	
	public IOpreatorService getOpreatorService() {
		return opreatorService;
	}

	public void setOpreatorService(IOpreatorService opreatorService) {
		this.opreatorService = opreatorService;
	}

	public Operator getOp() {
		return op;
	}

	public void setOp(Operator op) {
		this.op = op;
	}

	public void loginSynchronzize(){
		try {
			if(op!=null && op.getOperatorId()!=null
					&& op.getRoleIds()!=null){
				Map roles = opreatorService.findRoles(op.getRoleIds());
				op.setRoles(roles);
				Operator operator = opreatorService.findOneOperator(op.getOperatorId());
				if(operator!=null){
					op.setTelephone(operator.getTelephone());
					op.setSuperManager(operator.isSuperManager());
				}
				this.getRequest().getSession().setAttribute(WorkTaskConfig.OPERATOR_SESSION_NAME, op);
				this.getResponse().getWriter().print(2);
				WorkTaskConfig.moveMapDate(op.getUserName());
			}else
				this.getResponse().getWriter().print(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
