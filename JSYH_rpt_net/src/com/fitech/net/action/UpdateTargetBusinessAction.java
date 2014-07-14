package com.fitech.net.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.cbrc.smis.common.Config;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsTargetDelegate;
import com.fitech.net.form.TargetBusinessForm;
import com.fitech.net.hibernate.MBusiness;
/**
 * ����ָ�����������Action����
 *
 * @author masclnj

 */
public final class UpdateTargetBusinessAction extends Action {
	private static FitechException log = new FitechException(UpdateTargetBusinessAction.class);
    
    /**
     * ��־��¼��
     */
    private static Logger logger = Logger.getLogger(UpdateTargetBusinessAction.class);
   /**
    * @param mapping Action mapping.
    * @param form Action form.
    * @param request HTTP request.
    * @param response HTTP response.
    * @exception IOException�Ƿ�������/������쳣
    * @exception ServletException�Ƿ���servlet���쳣ռ��
    */
   public ActionForward execute(
      ActionMapping mapping,
      ActionForm form,
      HttpServletRequest request,
      HttpServletResponse response
   )
      throws IOException, ServletException {
	   Locale locale=getLocale(request);
	   MessageResources resources=this.getResources(request);
	   FitechMessages messages=new FitechMessages();
	   
	   HttpSession session = request.getSession();
	   
	   TargetBusinessForm targetBusiness = (TargetBusinessForm)form;
		
		String curPage="";
		if(request.getParameter("curPage")!=null) curPage=(String)request.getParameter("curPage");
			
		boolean updateResult = false;
		
		try {
            //----------------------------------------------------------------------
            // 2008-06-19 gongming �ж������ݿ����Ƿ�����ͬ���Ƶ�ָ�����ʹ���
            // ��ͬ���������
            if(logger.isInfoEnabled())
                logger.info(" begin juge BusinessName isEquals with db.......");
            boolean equals = false;
            String tarName = targetBusiness.getBusinessName().trim();
            String tarId   = targetBusiness.getBusinessId().toString();
            if(logger.isInfoEnabled())
                logger.info(" get parametar tarName = [" + tarName +"] tarId = [" + tarId + "]");
            if(StringUtils.isNotEmpty(tarName) && StringUtils.isNotEmpty(tarId))
            {
                if(logger.isInfoEnabled())
                    logger.info("query all targetBusiness.........");
                List tarLst = StrutsTargetDelegate.findAll();
                if(tarLst != null && !tarLst.isEmpty())
                {
                    for (Iterator iter = tarLst.iterator(); iter.hasNext();)
                    {
                        MBusiness elmt = (MBusiness) iter.next();
                        if(tarName.equals(elmt.getBusinessName().trim())
                                && !tarId.equals(elmt.getBusinessId().toString()))
                        {
                            if(logger.isInfoEnabled())
                                logger.info("find equals bussinessName .......");
                            equals = true;
                            break;
                        }                    
                    }
                    if(!equals)
                    {                   
                        if (targetBusiness != null) {
                            if(logger.isInfoEnabled())
                                logger.info("begin update targetBusiness");
                            updateResult = StrutsTargetDelegate.update(targetBusiness);                        
                            if (updateResult == true){  //���³ɹ�
                                if(logger.isInfoEnabled())
                                    logger.info("update targetBusiness success.......");
                                messages.add("���³ɹ�");
                            }else{ 
                                if(logger.isInfoEnabled())
                                    logger.info("update targetBusiness faile........");
                                messages.add("����ʧ��");
                            }
                        }
                    }
                    else
                    {
                        messages.add("������ͬ����ָ�����");
                    }
                    tarLst.clear();
                }
                else
                {
                    messages.add("�������쳣���������Ա��ϵ������");
                }
            }
            else
            {
                messages.add("ָ�����Ͳ���δ��ȡ");
            }
            
            //-------------------------------------------------------------------------------
//            if (targetBusiness != null) {
//                updateResult = com.fitech.net.adapter.StrutsTargetDelegate.update(targetBusiness);
//                if (updateResult == true){  //���³ɹ�
//                    messages.add("���³ɹ�");
//                }else{                      //����ʧ��
//                    messages.add("����ʧ��");
//                }
//            }
			
		} catch (Exception e) {
			log.printStackTrace(e);
		}

//		FitechUtil.removeAttribute(mapping,request);
       
		//�ж�������ʾ��Ϣ�����н���洢��Request�����У���������
		if(messages!=null && messages.getSize()>0)
			request.setAttribute(Config.MESSAGES,messages);
		
		String path="";
		if(updateResult==true){	//�ɹ������ر����б�ҳ
			form = null;
			path = mapping.findForward("update").getPath() +
				"?curPage=" + curPage + 
				"&businessName=";
		}else{					//ʧ�ܣ������ύҳ
			//path = mapping.getInputForward().getPath();
            
            return mapping.getInputForward();
		}
		
		path=path==null?mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE).getPath():path;
		
		return new ActionForward(path);
	}
  }
