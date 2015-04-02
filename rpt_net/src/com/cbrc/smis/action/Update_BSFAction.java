package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.UpdateBSFWForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
/**
 * 报送范围修改后的更新操作类
 * 
 * @author 王东伟
 *
 */
public class Update_BSFAction extends Action {
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {
		
		 Locale locale=getLocale(request);
		   MessageResources resources=this.getResources(request);
		   FitechMessages messages=new FitechMessages();
		   
		UpdateBSFWForm bsfwForm=(UpdateBSFWForm)form;
		if(bsfwForm!=null){
			if(bsfwForm.getOrgIds()!=null){
				// System.out.println("getOrgIds.length is "+bsfwForm.getOrgIds().length);
			}
		}else{
			return mapping.findForward("updateOK");
		}
		if(bsfwForm.getOrgIds()!=null && bsfwForm.getOrgIds().length>0){
			for(int i=0;i<bsfwForm.getOrgIds().length;i++)
			{
				// System.out.println((bsfwForm.getOrgIds())[i]);
			}
		}
		String orgIds[]=bsfwForm.getOrgIds();
		HttpSession session=request.getSession();
		
		
		ArrayList list=new ArrayList();
		if(session.getAttribute("SelectedOrgIds")!=null)
		{
			HashMap hMap=(HashMap)session.getAttribute("SelectedOrgIds");
			if(hMap!=null){
				Iterator it=hMap.keySet().iterator();
				while(it.hasNext()){
					
                    list.add((String)it.next());
					//orgList.addAll();
				}
			}
			//保存后删除
			session.removeAttribute("SelectedOrgIds");
		}
		//增加当前选择的
		String orglist[]=bsfwForm.getOrgIds();
		for (int i=0;i<orglist.length;i++)
		{
			if(!list.contains(orglist[i]))
			{
				list.add(orglist[i]);
			}
			
		}
		StrutsOrgNetDelegate.selectAllLowerOrgList(list,((Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId());
		// System.out.println(list.size()+"+++++++");
//		String childRepId=(String)request.getAttribute("childRepId");
		String childRepId=bsfwForm.getChildRepId();
		// System.out.println(childRepId);
//		String versionId=(String)request.getAttribute("versionId");
		String versionId=bsfwForm.getVersionId();
		// System.out.println(versionId);
		//删除操作。。删除org下级机构
		StrutsMRepRangeDelegate.removeLowerOrg(childRepId,versionId,((Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)).getOrgId());
		StrutsMRepRangeDelegate.updateBSFW(childRepId,versionId,list);
		
		if(bsfwForm.getChildRepId()!=null && bsfwForm.getVersionId()!=null)
			bsfwForm.setReportName(StrutsMChildReportDelegate.getname(bsfwForm.getChildRepId(),versionId));
		
		
		String path = "";
        request.setAttribute("ObjForm",bsfwForm);
       // 操作成功，返回频度设定页
        messages.add("保存成功！");
        if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
        
            path = mapping.findForward("setBSPL").getPath();
        

        String url = "";
        if (childRepId != null)
            url += (url.equals("") ? "" : "&") + "childRepId="
                    + childRepId;
        if (versionId != null)
            url += (url.equals("") ? "" : "&") + "versionId="
                    + versionId;
        if (bsfwForm.getReportName() != null)
            url += (url.equals("") ? "" : "&") + "reportName="
                    + bsfwForm.getReportName();
       if (bsfwForm.getReportStyle() != null)
            url += (url.equals("") ? "" : "&") + "reportStyle="
                    + bsfwForm.getReportStyle();
        // System.out.println(path + (url.equals("") ? "" : "?" + url));
        return new ActionForward(path + (url.equals("") ? "" : "?" + url));
	}
	
	
}
