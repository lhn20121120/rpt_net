package com.cbrc.smis.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import com.cbrc.org.form.MOrgClForm;
import com.cbrc.org.util.MOrgUtil;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.adapter.StrutsMRepRangeDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AbnormityChangeForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.form.MRepRangeForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
/**
 * 新增异常变化的初始化操作类
 * 
 * @author rds
 * @serialData 2005-12-10 23:50
 */
public class AddYCBHInitAction extends Action {
	private FitechException log=new FitechException(AddYCBHInitAction.class);
	
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws IOException, ServletException {

		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);
		
		HttpSession session=request.getSession();
		
		
		
		FitechMessages messages = new FitechMessages();
		
		boolean flag=false;
		// System.out.println("sssssssssss");
		try{
			AbnormityChangeForm abnormityChangeForm=null;
			
			if(request.getAttribute("ObjForm")!=null){
				abnormityChangeForm=(AbnormityChangeForm)request.getAttribute("ObjForm");
				
				
				if(abnormityChangeForm!=null && abnormityChangeForm.getChildRepId()!=null
						&& abnormityChangeForm.getVersionId()!=null){
									MChildReportForm mChildReportForm=new StrutsMChildReportDelegate().getMChildReport(
							abnormityChangeForm.getChildRepId(),
							abnormityChangeForm.getVersionId());
									// System.out.println("sssssgggggggg");
					if(mChildReportForm!=null){
						abnormityChangeForm.setReportName(mChildReportForm.getReportName());
						abnormityChangeForm.setReportStyle(mChildReportForm.getReportStyle());
						// System.out.println(mChildReportForm.getReportName()+"==============");
					}
				}
				
				String orgsStr=StrutsMRepRangeDelegate.getOrgsString(abnormityChangeForm.getChildRepId(),
						abnormityChangeForm.getVersionId());
				
				//获取所有的机构类型
				List _orgCls=MOrgUtil.getOrgCls(StrutsMRepRangeDelegate.getOrgClsString(abnormityChangeForm.getChildRepId(),
						abnormityChangeForm.getVersionId()));
				
				//List _orgCls=MOrgUtil.getOrgCls();  //列出所有的机构类别
				
				/**根据报送范围内的机构获得所对应的机构类别**/
				/*List _orgCls=MOrgUtil.getOrgClsByOrgs(orgsStr);
				String _strOrgCls="";
				for(int j=0;j<_orgCls.size();j++){
					if(_orgCls!=null) _strOrgCls+=(_strOrgCls.equals("")?"":",") + "'" + 
						((String)_orgCls.get(j)).trim() + "'";
				}
				_orgCls=MOrgUtil.getOrgCls(_strOrgCls);*/
			
				
				if (session.getAttribute("SelectedOrgIds")==null)
				{
				HashMap hMap = new HashMap();
        
                List selectedOrgFromDB = StrutsMRepRangeDelegate.findAll(abnormityChangeForm.getChildRepId(),abnormityChangeForm.getVersionId());
                if(selectedOrgFromDB!=null && selectedOrgFromDB.size()>0)
                {
                    for(int i=0;i<selectedOrgFromDB.size();i++)
                    {
                        MRepRangeForm item =(MRepRangeForm)selectedOrgFromDB.get(i);
                        hMap.put(item.getOrgId().trim(),"TOP");
                        //// System.out.println("Key=="+item.getOrgId()+" Value=="+item.getOrgClsId());
                    }
                }
                session.setAttribute("SelectedOrgIds",hMap);
				}
				
				
				//子机构
				Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
				 List lowerOrgList=StrutsOrgNetDelegate.selectLowerOrgList(operator.getOrgId(),session);		   
					request.setAttribute("lowerOrgList",lowerOrgList);	
				
					
					
				if(_orgCls!=null && _orgCls.size()>0){
					List orgCls=new ArrayList();
					if(session.getAttribute("SelectedOrgIds")!=null){
						HashMap hMap=(HashMap)session.getAttribute("SelectedOrgIds");
						Iterator it=hMap.keySet().iterator();
						/*while(it.hasNext()){
							Object _next=it.next();
							// System.out.println(((String)_next) + "\t" + hMap.get(_next));
						}*/
						for(int i=0;i<_orgCls.size();i++){
							MOrgClForm mOrgClForm=(MOrgClForm)_orgCls.get(i);
							/*// System.out.println("mOrgClForm.getOrgClsId():" + mOrgClForm.getOrgClsId());*/
							if(hMap.containsValue(mOrgClForm.getOrgClsId())==true){
								mOrgClForm.setSelAll(new Integer(1));
							}
							orgCls.add(mOrgClForm);
						}
					}else{
						for(int i=0;i<_orgCls.size();i++){
							MOrgClForm mOrgClForm=(MOrgClForm)_orgCls.get(i);
							orgCls.add(mOrgClForm);
						}
					}
					request.setAttribute("OrgCls",orgCls);
				}
				request.setAttribute("ObjForm",abnormityChangeForm);
				flag=true;
			}else{ //获取报表ID和版本号失败
				messages.add(FitechResource.getMessage(locale,resources,"get.childRepIdAndVersionId.error"));
			}
		}catch(Exception e){
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale,resources,"errors.system"));
			return mapping.findForward(Config.FORWARD_SYS_ERROR_PAGE);
		}
		
		if(messages!=null && messages.getSize()>0) request.setAttribute(Config.MESSAGES,messages);
		
		if(flag==true){	 //初始化成功,转入异常变化设定页面
			return mapping.getInputForward();
		}else{  //初始化失败，转入系统错误页面
			return mapping.getInputForward();
		}
	}
}