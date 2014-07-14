    //Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_4.0.1/xslt/JavaClass.xsl

package com.cbrc.smis.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.db2xml.Db2Xml;
import com.cbrc.smis.util.FitechMessages;

/** 
 * 手动生成仓库文件，并输出日志
 * @author 姚捷
 *
 * @struts.action path="/system_mgr/viewDb2XmlManual"
 * @struts.action-forward name="db2xml_manual" path="/system_mgr/db2xml_manual.jsp"
 */
public class Db2XmlManualAction extends Action {

    // --------------------------------------------------------- Instance Variables

    // --------------------------------------------------------- Methods

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
        HttpServletResponse response) {
        FitechMessages messages = new FitechMessages();
        MessageResources resources=getResources(request);
        
        Db2Xml db2xml = new Db2Xml();
        int result;
        try 
        {
            result = db2xml.DataBase2Xml();
            if(result==Config.DataToXML_SUCCESS)
                messages.add(resources.getMessage("db2xml.success"));
            else if(result == Config.NO_DataToXML)
                messages.add(resources.getMessage("db2xml.noData"));
            else if(result == Config.DataToXML_FAILED)
              messages.add(resources.getMessage("db2xml.fail"));
        }
        catch (Exception e) {
            messages.add(resources.getMessage("db2xml.fail"));
            e.printStackTrace();
        }
        
        request.setAttribute(Config.MESSAGES,messages);
        return new ActionForward("/system_mgr/viewDb2XmlLog.do");
    }

}

