package com.fitech.net.action;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsActuTargetResultDelegate;
import com.fitech.net.form.ActuTargetResultForm;

public final class ViewTargetResultAction extends Action {
	private FitechException log=new FitechException(ViewTargetResultAction.class);
	public ActionForward execute(
		      ActionMapping mapping,
		      ActionForm form,
		      HttpServletRequest request,
		      HttpServletResponse response
			)throws IOException, ServletException {
				
		FitechMessages messages = new FitechMessages();
		ActuTargetResultForm actuTargetResultForm = (ActuTargetResultForm)form;
		RequestUtils.populate(actuTargetResultForm, request);
		List resList = null;
		String orgId = null;
			
		try{
			// 获得当前机构ID
			HttpSession session = request.getSession();
			Operator operator =(Operator)session.getAttribute(Config.OPERATOR_SESSION_NAME);
			orgId = operator.getOrgId();
			actuTargetResultForm.setOrgId(orgId);
			
			Calendar calendar = Calendar.getInstance();		
			calendar.add(Calendar.MONTH,-1);
			if(actuTargetResultForm.getYear() == null || "".equals(actuTargetResultForm.getYear()))			   
				actuTargetResultForm.setYear(new Integer(calendar.get(Calendar.YEAR)));		   
			if(actuTargetResultForm.getMonth() == null || "".equals(actuTargetResultForm.getMonth()))			   
				actuTargetResultForm.setMonth(new Integer(calendar.get(Calendar.MONTH)+1));
			if(actuTargetResultForm.getOrgId() == null || "".equals(actuTargetResultForm.getOrgId()))
				actuTargetResultForm.setOrgId(orgId);
			
			resList=StrutsActuTargetResultDelegate.search(actuTargetResultForm);
		}catch(Exception e){
			log.printStackTrace(e);
		}

		if(messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES,messages);
			
		if(resList!=null && resList.size()>0){
			String yearAndMonth="";
			for(int i=0;i<resList.size();i++){		 			
				yearAndMonth+=((ActuTargetResultForm)resList.get(i)).getYearAndMonth()+"##";
			}
			
			resList=targetAdd(resList);
			request.setAttribute("yearAndMonth",yearAndMonth); 
			request.setAttribute(Config.RECORDS,resList); 
			request.setAttribute("orgId", orgId);
		}
		return mapping.findForward("add");
	}
	
	public List targetAdd(List list){   //单元格合并的标记方法
		List result=list;
		String tName="";
		String nName="";
		int tNumber=1;
		int nNumber=1;
		for(int i=list.size()-1;i>=0;i--){
			ActuTargetResultForm bean=(ActuTargetResultForm)list.get(i);
			
			if(!tName.equals(bean.getBusinessName())){
				if(i+1<list.size()){
					ActuTargetResultForm aBean=((ActuTargetResultForm)result.get(i+1));
					aBean.setChange(new Integer(tNumber));
					aBean.setNorChange(new Integer(nNumber));
					result.set(i+1,aBean);
				}
				tName=bean.getBusinessName();
				nName=bean.getNormalName();
				//以下2行是修改指标查看的格式问题增加的
				bean.setChange(new Integer(tNumber));
				bean.setNorChange(new Integer(nNumber));
				tNumber=1;
				nNumber=1;
			}
			else{
				tNumber++;
				if(!nName.equals(bean.getNormalName())){
					ActuTargetResultForm aBean=((ActuTargetResultForm)result.get(i+1));
					aBean.setNorChange(new Integer(nNumber));
					result.set(i+1,aBean);
					nName=bean.getNormalName();
					nNumber=1;
				}else
					nNumber++;
				if(i==0){
					bean.setChange(new Integer(tNumber));
					bean.setNorChange(new Integer(nNumber));
					result.set(0,bean);
				}
			}
			result.set(i,bean);
		}
		return result;
	}
}