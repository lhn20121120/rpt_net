package com.cbrc.smis.action;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.adapter.StrutsInfoFilesDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.InfoFilesForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
/**
 * 
 * @author 曹发根
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewOutInfoFilesAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws HibernateException, ParseException {
		
		MessageResources resources = getResources(request);
		FitechMessages messages = new FitechMessages();
		InfoFilesForm infoFilesForm = (InfoFilesForm) form;


        /**取得保存在session中的操作员信息*/
        HttpSession session=request.getSession();
        Operator operator = new Operator();
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		       
        FormFile file = infoFilesForm.getInfoFile();
		Date recordTime=FitechUtil.getNowTime();

		if (file == null||file.getFileSize()==0) {
			messages.add(resources.getMessage("info_files_out.fail"));
			request.setAttribute(Config.MESSAGES,messages);
			return mapping.findForward("view");
		}
		
		//数据库插入
		try{
            infoFilesForm.setUserId(operator.getOperatorId());
			infoFilesForm.setInfoFileName(file.getFileName());
			infoFilesForm.setInfoFileLocation(saveAsFileName(file.getFileName(),recordTime));//Config.INFO_FILES_OUTPATH);
			infoFilesForm.setInfoFileType(file.getContentType());
			infoFilesForm.setInfoFileStyle(Config.INFO_FILES_STYLE_OUT);
			infoFilesForm.setRecordTime(recordTime);
			infoFilesForm.setInfoFileSize(new Integer(file.getFileSize()));
			
			InputStream inStream = file.getInputStream();
			
			if (StrutsInfoFilesDelegate.newInfoFIle2(infoFilesForm,inStream)){
				messages.add(resources.getMessage("info_files_out.success"));
			}
            else
                messages.add(resources.getMessage("info_files_out.database_fail"));
			
			inStream.close();
			file.destroy();
			request.setAttribute(Config.MESSAGES,messages);
			return mapping.findForward("view");
		}catch (Exception e){
			e.printStackTrace();
			messages.add(resources.getMessage("info_files_out.database_fail"));
			request.setAttribute(Config.MESSAGES,messages);
			return mapping.findForward("view");
		}

	}
	
	/*
	private String getName(String fullname,Date recordTime) throws ParseException{
		ServletContext context = super.servlet.getServletContext();
		String baseDir = context.getRealPath("/") + Config.FILESEPARATOR
				+ Config.INFO_FILES_OUTPATH;
		int i=fullname.lastIndexOf(".");
		String type=fullname.substring(i);
		return baseDir+"/"+FitechUtil.Formatter(recordTime)+type;
	}*/
    /**
     * 文件保存在目录下的名称(以时间命名)
     * @param fullname 原文件全名
     * @param recordTime 上传时间
     * @return String 另存后的文件名
     */

    private String saveAsFileName(String fullname,Date recordTime) throws ParseException 
    {
        int i=fullname.lastIndexOf(".");
        String fileType ="";
        if(i>0)
            fileType =fullname.substring(i);
        return FitechUtil.Formatter(recordTime)+fileType;
        
    }
    /**
     * 文件存放路径
     * @param fullname 文件全名
     * @param recordTime  上传时间
     * @return    
     * @throws ParseException
     */
    private String getSavePath(String fullname,Date recordTime) throws ParseException{
        //ServletContext context = super.servlet.getServletContext();
        String baseDir = Config.WEBROOTPATH + Config.FILESEPARATOR
                + Config.INFO_FILES_OUTPATH;
        String fileName = this.saveAsFileName(fullname,recordTime);
        return baseDir+Config.FILESEPARATOR+fileName;
    }
    
    /**
     * 文件备份路径
     * @param fullname 文件全名
     * @param recordTime  上传时间
     * @return    
     * @throws ParseException
     */
    private String getBakPath(String fullname,Date recordTime) throws ParseException{
        //ServletContext context = super.servlet.getServletContext();
        String baseDir = Config.BAK_INFO_FILES_OUTPATH;
        String fileName = this.saveAsFileName(fullname,recordTime);
        return baseDir+Config.FILESEPARATOR+fileName;
    }


}
