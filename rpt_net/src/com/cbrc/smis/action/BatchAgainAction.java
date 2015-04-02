package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechMessages;

/**
*
* <p>标题: BatchAgainAction</p>
*
* <p>描述: 批量重报Action </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2008-01-18
* @version 1.0
*/

public class BatchAgainAction extends Action
{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException 
    {
       FitechMessages messages      = new FitechMessages();    
       MessageResources resources   = getResources(request);
       //批量重报报表的Id
       String param                 = request.getParameter("param");
       //含年份，期数，报表名称，当前页等参数的查询字符串
       String queryString           = request.getQueryString();
      
       if(param != null)
       {
           request.setAttribute("queryString",queryString);
           request.setAttribute("param",param);
           return mapping.findForward("batch_toInsert");           
       }
       else
       {
           messages.add(resources.getMessage("batchAgain.error"));
           request.setAttribute(Config.MESSAGES,messages);
       }
       return new ActionForward("/report/repSearch.do?" + queryString);
    }
    
   
}
