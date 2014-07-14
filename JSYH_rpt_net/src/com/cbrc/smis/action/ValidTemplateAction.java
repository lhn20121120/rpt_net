package com.cbrc.smis.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;

import com.cbrc.smis.adapter.StrutsMActuRepDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
/**
*
* <p>标题: ValidTemplateAction</p>
*
* <p>描述: 验证模板在发布前是否设定频度和范围Action </p>
*
* <p>Copyright: Copyright (c) 2008</p>
*
* <p>Company: </p>
*
* @author   gongming
* @date     2008-07-02
* @version 1.0
*/
public class ValidTemplateAction extends Action
{

    private static Logger log = Logger.getLogger(ValidTemplateAction.class);    
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        if(log.isInfoEnabled())
            log.info("valid template frep begin ........");
        String sChildRepId = request.getParameter("childRepId");
        String sVersionId  = request.getParameter("versionId");
        StringBuffer xml   = new StringBuffer();       
        xml.append("<result>");
        
        if(log.isInfoEnabled())
            log.info("getQueryString: " + request.getQueryString());
        if(StringUtils.isNotEmpty(sChildRepId) && StringUtils.isNotEmpty(sVersionId))
        {
            if(log.isInfoEnabled())
                log.info("query frep ...........");
            List freps = StrutsMActuRepDelegate.getMActuRep(sChildRepId,sVersionId); //频度集合
            
            if(log.isInfoEnabled())
                log.info("query range ...........");            
            List ranges = StrutsMRepRangeDelegate.findAll(sChildRepId,sVersionId); //范围集合
            if(null != freps && !freps.isEmpty())
            {
                if(log.isInfoEnabled())
                    log.info("has setted freq ........");
                xml.append("<frep>valid</frep>");                   //频度已设定
            }
            else 
            {
                if(log.isInfoEnabled())
                    log.info("haven't set freq ........");
                xml.append("<frep>inValid</frep>");
            }
            
            if(null != ranges && !ranges.isEmpty())
            {
                if(log.isInfoEnabled())
                    log.info("has setted range ........");
                xml.append("<range>valid</range>");                 // 范围已设定
            }
            else
            {
                if(log.isInfoEnabled())
                    log.info("haven't setted range ........");
                xml.append("<range>inValid</range>");
            }
            xml.append("<param>get</param>");
        }
        else
        {
            xml.append("<frep>lost</frep>");
            xml.append("<range>lost</range>");
            xml.append("<param>lost</param>");
        }     
        xml.append("</result>");                            //异步回写验证信息
        PrintWriter pw = response.getWriter();
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        pw.write(xml.toString());
        pw.close();
        return null;
    }
}
