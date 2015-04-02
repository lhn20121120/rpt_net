package com.cbrc.smis.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsReportInDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.BatchAgainForm;
import com.cbrc.smis.util.FitechMessages;

/**
*
* <p>标题: DoBatchAgainAction</p>
*
* <p>描述: 批量重报入库Action </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2008-01-19
* @version 1.0
*/

public class DoBatchAgainAction extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException 
    {
       BatchAgainForm pform         = (BatchAgainForm) form;
       FitechMessages messages      = new FitechMessages();    
       MessageResources resources   = getResources(request);
       String cause                 = pform.getCause(); 
       //批量重报报表的Id
       String param                 = request.getParameter("repInIds");
       //含年份，期数，报表名称，当前页等参数的查询字符串
       String queryString           = request.getParameter("queryString");
      
       if(StringUtils.isNotEmpty(param))
       {
               String arry[] = param.split(Config.SPLIT_SYMBOL_COMMA);
               try
               {
                  boolean result=StrutsReportInDelegate.batchAgain(arry,cause);
                  if(result)
                       messages.add(resources.getMessage("batchAgain.success"));
                   else
                       messages.add(resources.getMessage("batchAgain.failure"));
               }
               catch (HibernateException e)
               {
                   messages.add(resources.getMessage("batchAgain.error"));
               }
       }
       else
       {
           messages.add(resources.getMessage("batchAgain.error"));    
       }
       request.setAttribute(Config.MESSAGES,messages);
       return new ActionForward("/report/repSearch.do?" + queryString);
    }
}
