package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.adapter.StrutsMCellFormuDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MCellFormuForm;
import com.cbrc.smis.other.Expression;
import com.cbrc.smis.util.FitechMessages;

/**
 * 表内表间关系修改前的初如化操作类
 * 
 * @author rds
 * @serialDate 2005-12-19 13:32
 */
public class EditBJGXInit extends Action
{
    /**
     * 已使用hibernate 卞以刚 2011-12-22 Performs action.
     * 
     * @param mapping Action mapping.
     * @param form Action form.
     * @param request HTTP request.
     * @param response HTTP response.
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        Locale locale = getLocale(request);
        MessageResources resources = getResources(request);

        FitechMessages messages = new FitechMessages();

        MCellFormuForm mCellFormuForm = new MCellFormuForm();
        RequestUtils.populate(mCellFormuForm, request);

        String curPage = null;

        if (request.getParameter("curPage") != null)
            curPage = (String) request.getParameter("curPage");

        /** 已使用hibernate 卞以刚 2011-12-22 **/
        List resList = StrutsMCellFormuDelegate.findAllSpe(mCellFormuForm.getChildRepId(),
                mCellFormuForm.getVersionId(), false);

        List resListJG = StrutsMCellFormuDelegate.findAllSpe(mCellFormuForm.getChildRepId(),
                mCellFormuForm.getVersionId(), true);
        // List expressions=getExpressions(resList);

        // 表内表间关系表达式列表
        // if(expressions!=null)
        // request.setAttribute("Expressions",expressions);
        if (resList != null)
        {
            request.setAttribute(Config.RECORDS, resList);

        }
        if (resListJG != null)
        {
            request.setAttribute(Config.RECORDSJG, resListJG);
        }

        /** 已使用hibernate 卞以刚 2011-12-22 **/
        com.cbrc.smis.form.MChildReportForm mChildReportForm = new StrutsMChildReportDelegate().getMChildReport(
                mCellFormuForm.getChildRepId(), mCellFormuForm.getVersionId());
        if (mChildReportForm != null)
        {
            mCellFormuForm.setReportStyle(mChildReportForm.getReportStyle());
            mCellFormuForm.setReportName(mChildReportForm.getReportName());
            // yql 添加
            mCellFormuForm.setChildRepId(mChildReportForm.getChildRepId());
        }

        request.setAttribute("ObjForm", mCellFormuForm);

        if (messages != null && messages.getSize() > 0)
            request.setAttribute(Config.MESSAGES, messages);
        request.setAttribute("CurPage", curPage);

        return mapping.findForward("view");
    }

    /**
     * 将MCellFormuForm信息列表转换成Expression对象信息列表
     * 
     * @param mCellFormuForms List
     * @return List
     */
    private List getExpressions(List mCellFormuForms)
    {
        List listExp = null;

        if (mCellFormuForms == null)
            return listExp;
        // System.out.println("mCellFormuForms.size:" + mCellFormuForms.size());
        Iterator it = mCellFormuForms.iterator();
        if (it != null)
            listExp = new ArrayList();
        MCellFormuForm form = null;
        while (it.hasNext())
        {
            form = (MCellFormuForm) it.next();
            Expression expression = new Expression();
            expression.setContent(form.getCellFormu());
            expression.setType(form.getFormuType());
            listExp.add(expression);
        }

        return listExp;
    }
}
