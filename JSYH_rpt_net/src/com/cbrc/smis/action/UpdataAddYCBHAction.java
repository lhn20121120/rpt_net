package com.cbrc.smis.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cbrc.smis.form.UpdateBSFWForm;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;
/**
 * 报送范围修改后的更新操作类
 * 
 * @author 王东伟
 *
 */
public class UpdataAddYCBHAction extends Action {
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
		HashMap hMap=null;
		if(session.getAttribute("SelectedOrgIds")==null)
        {
             hMap = new HashMap();
           
        }	
		else
		{
			hMap=(HashMap)session.getAttribute("SelectedOrgIds");
			session.removeAttribute("SelectedOrgIds");
		}
		
		//删除子机构
		// System.out.println("orgId"+bsfwForm.getOrgId());
		if(bsfwForm.getOrgId()!=null && !bsfwForm.getOrgId().equals(""))
		{
		List lowerOrgList=StrutsOrgNetDelegate.selectLowerOrgList(bsfwForm.getOrgId());
		for(int i=0;i<lowerOrgList.size();i++)
		{
			hMap.remove(((OrgNet)lowerOrgList.get(i)).getOrgId());
		}
		}
		//加上选中的项
		if(orgIds!=null)
		 for(int i=0;i<orgIds.length;i++)
         {
             hMap.put(orgIds[i].trim(),"ok");
                          
         }
     session.setAttribute("SelectedOrgIds",hMap);
		
     
     
		
//		String childRepId=(String)request.getAttribute("childRepId");
		String childRepId=bsfwForm.getChildRepId();
		// System.out.println(childRepId);
//		String versionId=(String)request.getAttribute("versionId");
		String versionId=bsfwForm.getVersionId();
		// System.out.println(versionId);
	//	StrutsMRepRangeDelegate.updateBSFW(childRepId,versionId,bsfwForm.getOrgIds());
		// System.out.println(bsfwForm.getReportName());
		String path = "";
        
       // 操作成功，返回频度设定页
            path = mapping.findForward("view").getPath();
        

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
       if(bsfwForm.getGotoOrg()!=null )
    	   url+=(url.equals("")?"":"&")+"orgId="
    	   		+bsfwForm.getGotoOrg();
       
        // System.out.println(path + (url.equals("") ? "" : "?" + url));
        return new ActionForward(path + (url.equals("") ? "" : "?" + url));
	}
	
	
}
