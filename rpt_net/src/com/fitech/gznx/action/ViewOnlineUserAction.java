package com.fitech.gznx.action;

import java.io.IOException;
import java.util.ArrayList;
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

import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechUtil;
import com.cbrc.smis.common.Config;
import com.fitech.gznx.form.OnlineUserForm;
import com.fitech.gznx.service.OnlineUserUtil;
import com.fitech.gznx.service.XmlTreeUtil;


public class ViewOnlineUserAction extends Action {

	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
	
		MessageResources resources = getResources(request);
		
		Locale locale = request.getLocale();

		List list = new ArrayList();
		List querylist = new ArrayList();
		OnlineUserForm onlineUserForm = new OnlineUserForm();
		RequestUtils.populate(onlineUserForm, request);
		/** 查询条件 */
		String userId_query="";
//		String ipAdd_query="";
		if(onlineUserForm.getOperatorId()!=null){
			userId_query = onlineUserForm.getOperatorId().trim();
		}else{
			userId_query = onlineUserForm.getOperatorId();
		}
	
//		if(onlineUserForm.getOperatorId()!=null){
//		ipAdd_query = onlineUserForm.getIpAdd().trim();
//		}else{
//			ipAdd_query = onlineUserForm.getIpAdd();
//		}
		String orgId_query = onlineUserForm.getOrgId();
		
		// 取得机构树信息
	    try {
	    	onlineUserForm.setTreeOrgContent(XmlTreeUtil.createOrgXml(request,"TREE1_NODES",null,false,true,true)) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/** Operator中在线用户信息 */
		String userId ="";

		String ipAdd = "";
		String orgId = "";
		try
		{

			list = OnlineUserUtil.getOnlineUserList();
			if ((userId_query != null && !userId_query.equals("")) 
				
					|| (orgId_query != null && !orgId_query.equals("")))
			{
				if (list != null && list.size() != 0)
				{
					for (int i = 0; i < list.size(); i++)
					{
						Operator operator = (Operator) list.get(i);
						if (operator != null && !operator.equals(""))
						{
							userId = String.valueOf(operator.getOperatorId());

							ipAdd = operator.getIpAdd();
							orgId = operator.getOrgId();

							if (userId_query != null && !userId_query.equals("")
									&& userId.indexOf(userId_query) < 0)
								continue;

//							if (ipAdd_query != null && !ipAdd_query.equals("")
//									&& ipAdd.indexOf(ipAdd_query) < 0)
//								continue;
							if (orgId_query != null && !orgId_query.equals("") && !orgId.equals(orgId_query))
								continue;

							if (!operator.getOperatorId().equals("")									
									&& !operator.getIpAdd().equals("") && !operator.equals(""))
							{
								querylist.add(operator);
							}
						}
					}
				}
			}
			if (onlineUserForm != null)
				request.setAttribute("QueryForm", onlineUserForm);
		}
		catch (Exception e)
		{
			
		}
		// 移除request或session范围内的属性
		FitechUtil.removeAttribute(mapping, request);
		// 把ApartPage对象存放在request范围内


		if ((userId_query != null && !userId_query.equals("")) 
				
				|| (orgId_query != null && !orgId_query.equals("")))
		{
			if (list != null && list.size() != 0)
				request.setAttribute(Config.RECORDS, querylist);
		}
		else
		{
			if (list != null && list.size() != 0)
				request.setAttribute(Config.RECORDS, list);
		}
		
		return mapping.findForward("onlineList");
	}

}
