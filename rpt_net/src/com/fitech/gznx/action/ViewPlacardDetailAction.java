package com.fitech.gznx.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.common.StringUtil;
import com.fitech.gznx.form.PlacardForm;
import com.fitech.gznx.service.StrutsPlacardDelegate;
import com.fitech.gznx.service.StrutsPlacardUserViewDelegate;
import com.fitech.gznx.service.XmlTreeUtil;



public class ViewPlacardDetailAction extends Action {

	public static String _flag = "";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{

		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		Locale locale = request.getLocale();

		PlacardForm placardForm = new PlacardForm();
		RequestUtils.populate(placardForm, request);

				HttpSession session = request.getSession();
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session.getAttribute(Config.OPERATOR_SESSION_NAME);
		else
			operator = new Operator();
		PlacardForm record = null;
		
		String flag = request.getParameter("flag");
		
		_flag = flag;
		Map hmap = new HashMap();
		
		
		try
		{
			record = StrutsPlacardDelegate.getPlacardInfo(placardForm.getPlacardId());
			if(record!=null)
			{
				List userList = record.getUserIdList();
				for(int i=0;i<userList.size();i++){
					String userid = (String) userList.get(i);
					hmap.put(userid, userid);
				}
				String operatorId = String.valueOf(operator.getOperatorId());
				// 清理打印文件夹
	    		String deleteFilePath = Config.WEBROOTPATH +"xml"+ File.separator + operatorId;
	       		File outfile = new File(deleteFilePath);
	       		if(outfile.exists())
	    			StringUtil.deleteUploadFile(deleteFilePath);    		
	    		outfile.mkdir();
	    		String filePath = deleteFilePath + File.separator + "placardupdateuser.xml";
				StrutsPlacardDelegate.createUserTree(filePath,hmap);
				
				request.setAttribute("placrdupdateuser", operatorId+ "/placardupdateuser.xml");
				request.setAttribute(Config.RECORDS, record);
				if(record.getUserIdList()!=null)
					request.setAttribute("UserIDList",record.getUserIdList());
				if(record.getUserViewList()!=null)
					request.setAttribute("UserViewList",record.getUserViewList());
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (messages.getMessages() != null && messages.getMessages().size() > 0)
			request.setAttribute(Config.MESSAGES, messages);	
		
		//查看详细信息
		if(flag.equals("1"))
		{
			return mapping.findForward("detail");
		}
		//更新公告
		else if(flag.equals("2"))
		{
			return mapping.findForward("update");
		}
		//用户查看情况
		else if(flag.equals("3"))
		{
			return mapping.findForward("viewState");
		}
		//用户查看公告
		else if(flag.equals("4"))
		{
			if(record!=null)
				StrutsPlacardUserViewDelegate.changeViewState(record.getPlacardId(),String.valueOf(operator.getOperatorId()));
			return mapping.findForward("userView");
		}
		return mapping.findForward("view");
	}

}
