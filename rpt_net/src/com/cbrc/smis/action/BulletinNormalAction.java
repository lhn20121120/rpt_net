package com.cbrc.smis.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cbrc.smis.adapter.StrutsBulletinDelegate;
import com.cbrc.smis.common.ApartPage;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.hibernate.Bulletin;

/**
*
* <p>标题: BulletinNormalAction</p>
*
* <p>描述:  公告查看Action </p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Company: </p>
*
* @author   龚明
* @date     2008-1-15
* @version 1.0
*/
public class BulletinNormalAction extends DispatchAction
{

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        //分页参数,设置分页对象
       String curPage = request.getParameter("curPage");
       ApartPage apartPage=new ApartPage();  
       if(StringUtils.isNotEmpty(curPage))
       {
           apartPage.setCurPage(Integer.parseInt(curPage));
       }       
       //获取分页公告
       List rsltLst = StrutsBulletinDelegate.findBulletin(apartPage);
       if(rsltLst != null && !rsltLst.isEmpty())
       {
           request.setAttribute("rsltLst",rsltLst);
       }
       request.setAttribute(Config.APART_PAGE_OBJECT,apartPage);
       return mapping.findForward("view_bulletin");
    }

    public ActionForward download(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        String bulletId = request.getParameter("bulletId");
        if(StringUtils.isNotEmpty(bulletId) && StringUtils.isNumeric(bulletId))
        {
            Integer pId = new Integer(bulletId);
            Bulletin bulletin = StrutsBulletinDelegate.findBulletinById(pId);
            if(bulletin != null)
            {
                String virtualName  = bulletin.getVirtualName();
                String fileName     = bulletin.getUploadFileName();
                if(virtualName != null)
                {
                    File file = new File(virtualName);
                    InputStream inStream = new FileInputStream(file);
                 
                   // response.setContentType("text/plain;charset=gb2312");
                   
                    fileName = new String(fileName.getBytes(),"ISO-8859-1");
                   
                    response.setContentType("application/octet-stream");
                    //response.setHeader("Content-length", String.valueOf(inStream.available()));
                    response.addHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
                    
                    
                    ServletOutputStream outStream = response.getOutputStream();
                   
                    byte b[] = new byte[inStream.available()];
                   
                    inStream.read(b);
                    inStream.close();
                    outStream.write(b);
                    outStream.flush();
                    outStream.close();
                    return null;
                }
            }
        }
        return mapping.findForward("view_bulletin");
    }
}
