package com.fitech.net.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.net.adapter.StrutsTargetDefineWarnDelegate;
import com.fitech.net.form.TargetDefineWarnForm;

public final class InsertTargetdefinePrestandardAction extends Action {
    private static FitechException log = new FitechException(
    		InsertTargetdefinePrestandardAction.class);
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        MessageResources resources = getResources(request);

        FitechMessages messages = new FitechMessages();
          boolean result=false;
        TargetDefineWarnForm targetDefineWarnForm = (TargetDefineWarnForm) form;
        try
        {
        	//纪录转换成list
        	List list =GetWarnList(targetDefineWarnForm);
        	
        	result=StrutsTargetDefineWarnDelegate.insert(list);
        	if(result==true)messages.add("比上期指标增加成功！");
        	else messages.add("增加失败！");
        }
        catch(Exception e)
        {
        	log.printStackTrace(e);
        	 messages.add("增加失败！");
        }
        if(messages!=null && messages.getSize()>0)
        {
        	request.setAttribute(Config.MESSAGES,messages);
        }
        if(targetDefineWarnForm!=null)
        {
        	request.setAttribute("ObjForm",targetDefineWarnForm);
        }
        if(result==false)
        {
        	return mapping.findForward("add");
        }
        else
        return mapping.findForward("view");
    }


    private List GetWarnList(TargetDefineWarnForm targetDefineWarnForm)
    {
    	List list=null;
    	if(targetDefineWarnForm==null)return null;
    	String standard=targetDefineWarnForm.getStandard();
    	Integer id=targetDefineWarnForm.getTargetDefineId();
    	if(standard==null || standard.equals("") || id==null) return null;
    	String arr[]=standard.split(Config.SPLIT_SYMBOL_ESP);
    	if(arr!=null && arr.length>0){
			list=new ArrayList();
			for(int i=0;i<arr.length;i++){
				String item[]=arr[i].split(Config.SPLIT_SYMBOL_COMMA);
				if(item.length<3) break;
				TargetDefineWarnForm temp=new TargetDefineWarnForm ();
				temp.setTargetDefineId(id);
				temp.setType("2");//比上期指标
				temp.setUpLimit(Float.valueOf(item[0].trim()));
				temp.setDownLimit(Float.valueOf(item[1].trim()));
				temp.setColor(item[2].trim());
				list.add(temp);
			}
    	}
    	return list;
    	
    }
}