package com.cbrc.smis.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class BatchAgainForm extends ActionForm
{
    private String cause; 
    

    public String getCause()
    {
        return cause;
    }

    public void setCause(String cause)
    {
        this.cause = cause;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        // TODO Auto-generated method stub
        super.reset(arg0, arg1);
    }

    public ActionErrors validate(ActionMapping arg0, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if(cause == null || "".equals(cause.trim()))
        {
            errors.add("batchAgain.cause",new ActionMessage("batchAgain.cause.empty"));
            request.setAttribute("param",request.getParameter("param"));
            request.setAttribute("queryString",request.getParameter("queryString"));
        }
        
        return errors;
    }

}
