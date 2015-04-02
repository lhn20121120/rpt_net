package com.cbrc.smis.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.MDataRgTypeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechLog;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
/**
 * ���ݷ�Χ�Ĳ���һ����¼��Action����
 *
 * @author ����
 *
 * @struts.action
 *    path="/struts/insertMDataRgType"
 *    name="MDataRgTypeForm"
 *    scope="request"
 *    validate="false"
 *
 * @struts.action-forward
 *    name="view"
 *    path="/struts/viewMDataRgType.do"
 *    redirect="false"
 *

 */
public final class InsertMDataRgTypeAction extends Action {
	private static FitechException log = new FitechException(InsertMDataRgTypeAction.class);
   /**
    *�������ݵ����ݿ�
    * @exception IOException  �Ƿ���IO�쳣��������׽���׳�
    * @exception ServletException �Ƿ���Servlet�쳣��������׽���׳�
    */
   
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request
			,HttpServletResponse response) throws IOException, ServletException{
		boolean insertResult=false;
		Locale locale=getLocale(request);
	   
		MessageResources resources=getResources(request);
		FitechMessages messages=new FitechMessages();
		
		MDataRgTypeForm mDataRgTypeForm = new MDataRgTypeForm();
		RequestUtils.populate(mDataRgTypeForm, request);
		try{
			HttpSession session = request.getSession();
    		Operator operator = null; 
    		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
    			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
    		
			if (mDataRgTypeForm!=null){
				insertResult = com.cbrc.smis.adapter.StrutsMDataRgTypeDelegate.create( mDataRgTypeForm);
				if (insertResult==true){
					/** д����־ */
                	String msg = FitechResource.getMessage(locale,resources,
                			"save.success","dataRange.msg",
							mDataRgTypeForm.getDataRgDesc());
                	FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
                	
					messages.add(FitechResource.getMessage(locale,resources,"save.success", "dataRange.info"));
				}else{
					/** д����־ */
                	String msg = FitechResource.getMessage(locale,resources,
                			"save.failed","dataRange.msg",
							mDataRgTypeForm.getDataRgDesc());
                	FitechLog.writeLog(Config.LOG_OPERATION,operator.getUserName(),msg);
                	
					messages.add(FitechResource.getMessage(locale,resources,"save.failed", "dataRange.info"));
				}
			}
		}catch (Exception e){
			insertResult=false;
			messages.add(FitechResource.getMessage(locale,resources,"save.failed", "dataRange.info"));
			log.printStackTrace( e);
		}
		if(messages!=null && messages.getSize()>0) 
			request.setAttribute(Config.MESSAGES,messages);
         
		//����ʧ��
		if(insertResult==false){
			return mapping.findForward("insert"); 
		}
		String path="/config/ViewDataRangeType.do?dataRgDesc=";
		return new ActionForward(path);
	}
}
