package com.cbrc.smis.action;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
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
import org.apache.struts.util.RequestUtils;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.cbrc.smis.adapter.StrutsAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsColAbnormityChangeDelegate;
import com.cbrc.smis.adapter.StrutsMChildReportDelegate;
import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AbnormityChangeForm;
import com.cbrc.smis.form.ColAbnormityChangeForm;
import com.cbrc.smis.form.MChildReportForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.cbrc.smis.util.FitechResource;
import com.cbrc.smis.util.FitechUtil;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;

/**
 * 异常变化设定修改初始化操作类
 * 
 * @author rds
 * @serialData 2005-12-23 22:14
 */
public class ViewPreYCBHAction extends Action {
	private FitechException log=new FitechException(AddYCBHAction.class);
	
	/**
	 * Performs action.
	 * @param mapping Action mapping.
	 * @param form Action form.
	 * @param request HTTP request.
	 * @param response HTTP response.
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet exception occurs
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		Locale locale = getLocale(request);
		MessageResources resources = getResources(request);		
		FitechMessages messages=new FitechMessages();
		HttpSession session=request.getSession();	
		
		AbnormityChangeForm abnormityChangeForm=new AbnormityChangeForm();
		RequestUtils.populate(abnormityChangeForm,request);
		if(abnormityChangeForm.getVersionId()==null){
			String version=request.getParameter("versionId");
			if(version!=null)
				abnormityChangeForm.setVersionId(version);
		}if(abnormityChangeForm.getReportName()==null){
			String reportname=request.getParameter("childRepId");
			if(reportname!=null)
				abnormityChangeForm.setReportName(reportname);
		}
		
		boolean flag=false;		
		try{
			/**从Session中消除机构列表信息**/
			if(session.getAttribute("SelectedOrgIds")!=null) session.setAttribute("SelectedOrgIds",null);
						
			if(abnormityChangeForm!=null && abnormityChangeForm.getChildRepId()!=null 
					&& abnormityChangeForm.getVersionId()!=null){							
				MChildReportForm mChildReportForm=new StrutsMChildReportDelegate().getMChildReport(
						abnormityChangeForm.getChildRepId(),
						abnormityChangeForm.getVersionId());
				if(mChildReportForm!=null){
					abnormityChangeForm.setReportName(mChildReportForm.getReportName());
					abnormityChangeForm.setReportStyle(mChildReportForm.getReportStyle());
				}
				
				if (session.getAttribute("SelectedOrgIds")==null){				
					HashMap hMap = new HashMap();                       
					//List selectedOrgFromDB = StrutsMRepRangeDelegate.findAll2(abnormityChangeForm.getChildRepId(),abnormityChangeForm.getVersionId());					
					List selectedOrgFromDB = StrutsAbnormityChangeDelegate.findAll2(abnormityChangeForm.getChildRepId(),abnormityChangeForm.getVersionId());
					if(selectedOrgFromDB!=null && selectedOrgFromDB.size()>0){                    
						for(int i=0;i<selectedOrgFromDB.size();i++){
							AbnormityChangeForm item =(AbnormityChangeForm)selectedOrgFromDB.get(i);
	                        hMap.put(item.getOrgId().trim(),"ok");	                        
	                    }                
					}                
					session.setAttribute("SelectedOrgIds",hMap);				
				}
				//添加一级子机构				
				Operator operator=(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);	 
				//根据机构ID,取得机构名称;		
				String orgName = StrutsOrgNetDelegate.getOrgName(operator.getOrgId());						
				List lowerOrgList=StrutsOrgNetDelegate.selectLowerOrgList(operator.getOrgId(),session);	
		
				if (lowerOrgList == null || lowerOrgList.size() == 0) {			
					request.setAttribute("isNull", "isNull");		
				}			
				request.setAttribute("lowerOrgList",lowerOrgList);
				
				String fileName = Config.WEBROOTPATH + "xml"+Config.FILESEPARATOR+"ycbh.xml"; // 创建一个XML文件	
				
				if(mChildReportForm.getReportStyle().compareTo(Config.REPORT_STYLE_DD)==0){ //点对点式
					//取报送范围					
					List _list=StrutsAbnormityChangeDelegate.findAllList(
							abnormityChangeForm.getChildRepId(),
							abnormityChangeForm.getVersionId(),
							operator.getOrgId());
										
					if(_list!=null && _list.size()>0){
						StringBuffer js=new StringBuffer("var arrAC=new Array();\nvar i=0;\n");
						AbnormityChangeForm acForm=null;					
						for(int j=0;j<_list.size();j++){	
							acForm=(AbnormityChangeForm)_list.get(j);					
							js.append("arrAC[i++]=new AbnormityChange(\"" + 
									acForm.getCellId() + "\",\"" +
									acForm.getCellName() + "\"," +
									acForm.getPrevFallStandard() + "," +
									acForm.getPrevRiseStandard() + "," + 
									acForm.getSameFallStandard() + "," +
									acForm.getSameRiseStandard() + ");\n");
						}
						if(!js.toString().equals("")) request.setAttribute("JS",js);
					}				
				}else{ //清单式
					List _list=StrutsColAbnormityChangeDelegate.findAllList(
							abnormityChangeForm.getChildRepId(),
							abnormityChangeForm.getVersionId(),
							operator.getOrgId());
					if(_list!=null && _list.size()>0){
						StringBuffer js=new StringBuffer("var arrAC=new Array();\nvar i=0;\n");
						ColAbnormityChangeForm acForm=null;
						for(int j=0;j<_list.size();j++){
							acForm=(ColAbnormityChangeForm)_list.get(j);
							js.append("arrAC[i++]=new AbnormityChange(\"" + 
									acForm.getColName() + "\",\"" +
									acForm.getColName() + "\"," +
									acForm.getPrevFallStandard() + "," +
									acForm.getPrevRiseStandard() + "," + 
									acForm.getSameFallStandard() + "," +
									acForm.getSameRiseStandard() + ");\n");
						}
						if(!js.toString().equals("")) request.setAttribute("JS",js);
					}
				}
				request.setAttribute("ObjForm",abnormityChangeForm);
				flag=true;
				
				//创建一个XML
				org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		//		document.setXMLEncoding("GBK");
				
				Element rootElement = document.addElement("tree");
				rootElement.addAttribute("id", "0");

				Element oneElement = rootElement.addElement("item");
				oneElement.addAttribute("text", orgName);
				oneElement.addAttribute("id", operator.getOrgId());
				oneElement.addAttribute("open", "1");
				oneElement.addAttribute("checked", "1");
				getNode(operator.getOrgId(), session, oneElement);
				try {
//					OutputFormat format = OutputFormat.createPrettyPrint();
//					format.setEncoding("GBK");
//					XMLWriter output = new XMLWriter(
//							new FileWriter(new File(fileName)), format);
//					output.write(document);
//					output.close();
					String xmlStr = document.asXML();
					File file = new File(fileName);
					OutputStream out = new DataOutputStream(new FileOutputStream (file));
					StringBuffer sb = new  StringBuffer();
					sb.append(xmlStr);			
					out.write(sb.toString().getBytes("UTF-8"));
					out.flush();
					out.close();			
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}else{ //获取报表ID和版本号失败
				messages.add(FitechResource.getMessage(locale,resources,"get.childRepIdAndVersionId.error"));
			}			
		}catch(Exception e){
			e.printStackTrace();
			log.printStackTrace(e);
			messages.add(FitechResource.getMessage(locale,resources,"ycbh.edit.init.failed"));
		}
		
		String curPage=FitechUtil.getRequestParameter(request,"curPage");
		if(curPage!=null) request.setAttribute("curPage",curPage);			
		if(messages!=null && messages.getSize()>0) 
			request.setAttribute(Config.MESSAGES,messages);
						
		if(flag==true)
			return mapping.getInputForward();
		else
			return mapping.findForward("view");
		
	}
	private boolean flag = true;

	private void addChild(Element e, OrgNet orgNet, HttpSession session) {
		if (orgNet != null) {
			String id = orgNet.getOrgId();
			Element element = e;
			if (flag) {
				element = e.addElement("item");
				element.addAttribute("text", orgNet.getOrgName());
				element.addAttribute("id", orgNet.getOrgId());
				if (orgNet.getPreOrgId().equals("true")) {
					element.addAttribute("checked", "1");
					element.addAttribute("open", "1");
				}
			}
			List childList = StrutsOrgNetDelegate.selectLowerOrgList(id,
					session);

			if (childList != null && !childList.equals("")) {
				for (int i = 0; i < childList.size(); i++) {
					OrgNet o = (OrgNet) childList.get(i);
					flag = false;
					Element child = element.addElement("item");
					child.addAttribute("text", o.getOrgName());
					child.addAttribute("id", o.getOrgId());
					if (o.getPreOrgId().equals("true")) {
						child.addAttribute("checked", "1");
						child.addAttribute("open", "1");
					}
					addChild(child, o, session);
				}
				flag = true;
			}
		}
	}

	private void getNode(String orgId, HttpSession session, Element rootElement) {

		List lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId,
				session);
		if (lowerOrgList != null && !lowerOrgList.equals("")) {
			for (int i = 0; i < lowerOrgList.size(); i++) {
				OrgNet o = (OrgNet) lowerOrgList.get(i);
				try {
					addChild(rootElement, o, session);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
