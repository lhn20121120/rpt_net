package com.cbrc.auth.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.RequestUtils;

import com.cbrc.auth.form.OperatorForm;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.service.OnlineUserUtil;
/**
 * 查询在线用户列表
 * @author Administrator
 *
 */
public class ViewOnlineOperatorAction extends DispatchAction {

	private static FitechException log = new FitechException(ViewOperatorAction.class);
	
	public ActionForward searchOnlineUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	     FitechMessages messages = new FitechMessages();
	     OperatorForm operatorForm=(OperatorForm)form;
	     RequestUtils.populate(operatorForm,request);
	     int recordCount =0; //记录总数
//	     int limit = Config.PER_PAGE_ROWS;
         List list= OnlineUserUtil.getOnlineUserList();
         
         recordCount = OnlineUserUtil.getOnlineUserCount();
         ApartPage aPage=new ApartPage();
         String strCurPage=request.getParameter("curPage");
       
         if(strCurPage!=null && !strCurPage.equals("") && !strCurPage.equals("null"))
         {
             aPage.setCurPage(new Integer(strCurPage).intValue());
         }
         else
             aPage.setCurPage(1);
         //计算偏移量
         list = getUserListPage(list, (aPage.getCurPage()-1)*10);
         aPage.setCount(recordCount);
         request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
         request.setAttribute("curPage",aPage.getCurPage()+"");
         request.setAttribute(Config.RECORDS,list!=null && !list.isEmpty()?list:null);
         
		return mapping.findForward("success");
	}
	
	public ActionForward onLineUserExit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String sessionId = request.getParameter("sessionId");
		FitechMessages messages = new FitechMessages();
		String userName = OnlineUserUtil.getIOnlineUserName(sessionId);
		HttpSession session = (HttpSession)request.getSession().getServletContext().getAttribute(userName);
		if(session!=null){
			if(request.getSession().getServletContext().getAttribute(userName)!=null){
				session.invalidate();
				session = null;
				request.getSession().getServletContext().removeAttribute(userName);
				OnlineUserUtil.removeOnlineUser(sessionId);
				messages.add("成功注销用户"+userName+"!");
				request.setAttribute(Config.MESSAGES, messages);
			}
		}
		return searchOnlineUser(mapping, form, request, response);
	}
	
	public List getUserListPage(List list,int offset){
		List ls= new ArrayList();
		if(list!=null){
			for(int i=offset;i<offset+10;i++){
				if(i<list.size()){
					ls.add(list.get(i));
				}
			}
		}
		return ls;
	}
}
