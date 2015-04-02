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
 * @author �ܷ���
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UpInfoFilesAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws HibernateException, ParseException {
        
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        InfoFilesForm infoFilesForm = (InfoFilesForm) form;
        
        /**ȡ�ñ�����session�еĲ���Ա��Ϣ*/
		HttpSession session=request.getSession();
        Operator operator = new Operator();
        if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
            operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);

     
        /**�ļ���Ϣ*/    
        FormFile file = infoFilesForm.getInfoFile();
		Date recordTime=FitechUtil.getNowTime();
		if (file == null||file.getFileSize()==0) 
        {
			messages.add(resources.getMessage("info_files_up.fail"));
			request.setAttribute(Config.MESSAGES,messages);
			return new ActionForward("/viewUpInfoFiles.do");
			
		}
		
		//���ݿ����
		try
        {
            /**�ϴ���id*/
            infoFilesForm.setUserId(operator.getOperatorId());
			/**ʵ���ļ���*/
            infoFilesForm.setInfoFileName(file.getFileName());
			/**�������ļ���*/
            infoFilesForm.setInfoFileLocation(this.saveAsFileName(file.getFileName(),recordTime));
			/**�ļ�����*/
            infoFilesForm.setInfoFileType(file.getContentType());
			/**�ļ���ʽ,�ϴ�or����*/
            infoFilesForm.setInfoFileStyle(Config.INFO_FILES_STYLE_UP);
			/**�ϴ�ʱ��*/
            infoFilesForm.setRecordTime(recordTime);
            /**�ļ���С*/
            infoFilesForm.setInfoFileSize(new Integer(file.getFileSize()));
            
            InputStream inStream = file.getInputStream();
			            
			if (StrutsInfoFilesDelegate.newInfoFIle2(infoFilesForm,inStream))
            {
				messages.add(resources.getMessage("info_files_up.success"));
			}
            else
                messages.add(resources.getMessage("info_files_up.database_fail"));
			
			inStream.close();
			file.destroy();
			request.setAttribute(Config.MESSAGES,messages);
			return new ActionForward("/viewUpInfoFiles.do");
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
			messages.add(resources.getMessage("info_files_up.database_fail"));
			request.setAttribute(Config.MESSAGES,messages);
			return new ActionForward("/viewUpInfoFiles.do");
		}

	}
	/**
	 * �ļ�������Ŀ¼�µ�����(��ʱ������)
     * @param fullname ԭ�ļ�ȫ��
     * @param recordTime �ϴ�ʱ��
     * @return String ������ļ���
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
     * �ļ����·��
     * @param fullname �ļ�ȫ��
     * @param recordTime  �ϴ�ʱ��
     * @return    
     * @throws ParseException
     */
	private String getSavePath(String fullname,Date recordTime) throws ParseException{
		//ServletContext context = super.servlet.getServletContext();
		String baseDir = Config.WEBROOTPATH + Config.FILESEPARATOR
				+ Config.INFO_FILES_UPPATH;
		String fileName = this.saveAsFileName(fullname,recordTime);
		return baseDir+Config.FILESEPARATOR+fileName;
	}
    
    /**
     * �ļ�����·��
     * @param fullname �ļ�ȫ��
     * @param recordTime  �ϴ�ʱ��
     * @return    
     * @throws ParseException
     */
    private String getBakPath(String fullname,Date recordTime) throws ParseException{
        //ServletContext context = super.servlet.getServletContext();
        String baseDir = Config.BAK_INFO_FILES_UPPATH;
        String fileName = this.saveAsFileName(fullname,recordTime);
        return baseDir+Config.FILESEPARATOR+fileName;
    }

}
