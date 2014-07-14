//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.smis.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsLogInDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;

/** 
 * �鿴���ɲֿ��ļ�����־
 * @author Ҧ��
 * 
 * XDoclet definition:
 * @struts.action path="/system_mgr/viewDb2XmlLog"
 * @struts.action-forward name="db2xml_manual" path="/system_mgr/db2xml_manual.jsp"
 */
public class ViewDb2XmlLogAction extends Action {
    private static FitechException log = new FitechException(ViewLogInAction.class); 
    /** 
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)  throws IOException, ServletException
     {
        
       MessageResources resources=getResources(request);
       FitechMessages messages = null;
       if(request.getAttribute("Message")!=null)
           messages = (FitechMessages)request.getAttribute("Message"); 
       else
           messages = new FitechMessages();
       
        int recordCount =0; //��¼����
        int offset=0; //ƫ����
        int limit=0;  //ÿҳ��ʾ�ļ�¼����
        List list = null;
        
        ApartPage aPage=new ApartPage();
        String strCurPage=request.getParameter("curPage");
        if(strCurPage!=null){
            if(!strCurPage.equals(""))
              aPage.setCurPage(new Integer(strCurPage).intValue());
        }
        else
            aPage.setCurPage(1);
        //����ƫ����
        offset=(aPage.getCurPage()-1)*Config.PER_PAGE_ROWS; 
        limit = Config.PER_PAGE_ROWS;   
        
        try 

        {
            //ȡ�ü�¼����
            recordCount = StrutsLogInDelegate.getDb2XmlLogCount();
            //��ʾ��ҳ��ļ�¼
            if(recordCount > 0)
                list = StrutsLogInDelegate.getDb2XmlLog(offset,limit);
           
        }
        catch (Exception e) 
        {
            log.printStackTrace(e);
            messages.add(resources.getMessage("db2xml.log.fail"));      
        }
        //�Ƴ�request��session��Χ�ڵ�����
        FitechUtil.removeAttribute(mapping,request);
 
        aPage.setCount(recordCount);
        request.setAttribute(Config.APART_PAGE_OBJECT,aPage);
        
        if(messages.getMessages() != null && messages.getMessages().size() > 0)
          request.setAttribute(Config.MESSAGES,messages);
        if(list!=null && list.size()!=0)
          request.setAttribute(Config.RECORDS,list);
        return mapping.findForward("db2xml_manual");
    }
   
 }
