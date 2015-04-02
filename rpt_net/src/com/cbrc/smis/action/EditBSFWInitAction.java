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
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.org.adapter.StrutsMOrgClDelegate;
import com.cbrc.org.form.MOrgClForm;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;



/**
 *  数据范围查看
 * @author 唐磊
 *
 *
 */
public final class EditBSFWInitAction extends Action {
	 private static FitechException log = new FitechException(ViewMRepRangeAction.class);
   /**
    * Performs action.
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException if an input/output error occurs
    * @exception ServletException if a servlet exception occurs
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException 
   {
        FitechMessages messages = new FitechMessages();
        MessageResources resources = getResources(request);
        
        HttpSession session=request.getSession();
        // 是否有Request
        MOrgClForm mOrgClForm = new MOrgClForm();
        RequestUtils.populate(mOrgClForm,request);

        String childRepId =mOrgClForm.getChildRepId();
        String versionId = mOrgClForm.getVersionId();
        
       // List resList = new ArrayList();
        List _orgCls =null;
        try 
        {
          // String orgsStr=StrutsMRepRangeDelegate.getOrgsString(childRepId,versionId);
                
           //获取所有的机构类型
            _orgCls= StrutsMOrgClDelegate.findAll();
           
           if(_orgCls!=null && _orgCls.size()>0)
           {
              
               /**不存在session对象(第一次)*/
               if(session.getAttribute("SelectedOrgIds")==null)
               {
                   HashMap hMap = new HashMap();
                   /**数据库中的记录*/
                   List selectedOrgFromDB = StrutsMRepRangeDelegate.findAll(childRepId,versionId);
                   if(selectedOrgFromDB!=null && selectedOrgFromDB.size()>0)
                   {
                       for(int i=0;i<selectedOrgFromDB.size();i++)
                       {
                           MRepRangeForm item =(MRepRangeForm)selectedOrgFromDB.get(i);
                           hMap.put(item.getOrgId().trim(),item.getOrgClsId().trim());
                           //// System.out.println("Key=="+item.getOrgId()+" Value=="+item.getOrgClsId());
                       }
                   }
                   session.setAttribute("SelectedOrgIds",hMap);
               }
               HashMap hMap=(HashMap)session.getAttribute("SelectedOrgIds");
                                        
               for(int i=0;i<_orgCls.size();i++)
               {
                   MOrgClForm item=(MOrgClForm)_orgCls.get(i);
                   if(hMap.containsValue(item.getOrgClsId()))
                   {
                       item.setSelAll(new Integer(1));
                   }
               }                             
           }
        }
        catch(Exception e)
        {
            log.printStackTrace(e);
            messages.add(resources.getMessage("tbfw.get.orgs.failed"));
        }
        //移除request或session范围内的属性
        //FitechUtil.removeAttribute(mapping,request);

        if (messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        //如果StrutsMOrgClDelegate类中返回的reslist对象不为空并且对象的大小大于0，
        //则返回一个包含reslist集合的request对象
        if (_orgCls != null && _orgCls.size() > 0){
            request.setAttribute(Config.RECORDS,_orgCls);
        }
        String reportName=mOrgClForm.getReportName();
        request.setAttribute("ReportName", new String(reportName.getBytes("iso-8859-1"),"GBK"));
        request.setAttribute("ChildRepId",mOrgClForm.getChildRepId());
        request.setAttribute("VersionId", mOrgClForm.getVersionId());
        if(FitechUtil.getRequestParameter(request,"curPage")!=null)  
            request.setAttribute("curPage",FitechUtil.getRequestParameter(request,"curPage"));
            //返回到页面view     
        return mapping.findForward("view");
    }
    
}
/*
        FitechMessages messages = new FitechMessages();
        MessageResources resources = getResources(request);
    
        // 是否有Request
        MOrgClForm mOrgClForm = new MOrgClForm();
        RequestUtils.populate(mOrgClForm,request);
    
        List resList = null;
    
        try 
        {
            resList = StrutsMOrgClDelegate.findAll();
    
        } 
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(resources.getMessage("BSFW.select.failed"));
        }
        //移除request或session范围内的属性
        FitechUtil.removeAttribute(mapping,request);
    
        if (messages.getMessages() != null && messages.getMessages().size() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        //如果StrutsMOrgClDelegate类中返回的reslist对象不为空并且对象的大小大于0，
        //则返回一个包含reslist集合的request对象
        if (resList != null && resList.size() > 0){
            request.setAttribute(Config.RECORDS,resList);
        }
        
        request.setAttribute("ReportName", mOrgClForm.getReportName());
        request.setAttribute("ChildRepId", mOrgClForm.getChildRepId());
        request.setAttribute("VersionId", mOrgClForm.getVersionId());
        // System.out.println("[EditBSFWInitAction]:curPage:" + request.getParameter("curPage"));
        if(FitechUtil.getRequestParameter(request,"curPage")!=null)  
        	request.setAttribute("curPage",FitechUtil.getRequestParameter(request,"curPage"));
        // System.out.println("[EditBSFWInitAction]:curPage:" + FitechUtil.getRequestParameter(request,"curPage"));
        //返回到页面view     
        return mapping.findForward("view");
   }
}
*/