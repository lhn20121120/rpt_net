package com.fitech.gznx.action;

import java.io.IOException;
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

import com.cbrc.smis.common.Config;
import com.cbrc.smis.form.AfTemplateCollRuleForm;
import com.cbrc.smis.security.Operator;
import com.cbrc.smis.util.FitechException;
import com.cbrc.smis.util.FitechMessages;
import com.fitech.gznx.form.OrgInfoForm;
import com.fitech.gznx.po.AfOrg;
import com.fitech.gznx.po.PreOrg;
import com.fitech.gznx.service.AFOrgDelegate;
import com.fitech.gznx.service.AFTemplateCollRuleDelegate;
import com.fitech.gznx.service.AFTemplateDelegate;
import com.fitech.gznx.treexml.BaseOrgTreeIteratorHZRule;
import com.fitech.net.adapter.StrutsOrgNetDelegate;
import com.fitech.net.hibernate.OrgNet;


public class AFHZRuleAction extends Action {
	private static FitechException log = new FitechException(AFHZRuleAction.class);
	private boolean flag = true;

    /** 
     * ��ʹ��Hibernate ���Ը� 2011-12-27
     * Ӱ�����AfTemplate
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
        
		Locale locale = getLocale(request);
		HttpSession session = request.getSession();
		
		Operator operator = null;
		if(session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME)!=null)
			operator = (Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		String curOrgId=operator.getOrgId();
		/** ����ѡ�б�־ **/
		String reportFlg = "0";
		if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
			reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
		}
        MessageResources resources = getResources(request);
        FitechMessages messages = new FitechMessages();
        
        //ȡ��request��Χ�ڵ�����������������afRuleForm��
        
        AfTemplateCollRuleForm afRuleForm = (AfTemplateCollRuleForm) form;
        RequestUtils.populate(afRuleForm, request);
        String orgId=request.getParameter("orgId");
        String templateId=request.getParameter("templateId");
        String versionId=request.getParameter("versionId");
        String reportName=request.getParameter("reportName");
        String opType=request.getParameter("optype");//����ֵ�ж������������޸�
       
        afRuleForm.setReportName(reportName);
        afRuleForm.setColl_schema(AFTemplateCollRuleDelegate.getCollSchemaName(orgId, templateId, versionId));// HZTJH-ͬ���� CUSTOM_ORG-�Զ������
        afRuleForm.setHz_style("0");// 0 -������� 1-����
        afRuleForm.setOrg_id(orgId);
        afRuleForm.setTemplate_id(templateId);
        afRuleForm.setVersion_id(versionId);
//        if (!"HZTJH".equals(afRuleForm.getColl_schema())) {
//�޸�Ĭ���ϼ�����        	
        	String[] str=null;
			try {
				str = com.cbrc.smis.proc.util.FitechUtil.parseFomual(AFTemplateCollRuleDelegate.getCollFormulaName(orgId, templateId, versionId));
				
				if (str!=null) {
					afRuleForm.setPreOrgId(str[0]);
				}else{
					List<PreOrg> preOrgList=AFTemplateDelegate.getHZRuleVirtualPreOrgIds(orgId);
					if (preOrgList!=null&&preOrgList.size()>0) {
						afRuleForm.setPreOrgId(preOrgList.get(0).getOrgId());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
//		}
        AfOrg afOrg=AFOrgDelegate.getOrgInfo(orgId);
        String orgIds=AFTemplateCollRuleDelegate.getSameLevelOrgIds(afOrg);
        afRuleForm.setColl_formula(orgIds);
//		request.setAttribute("form", afRuleForm);
		request.setAttribute("childRepId", templateId);
		request.setAttribute("versionId", versionId);
		request.setAttribute("orgId", orgId);
		request.setAttribute("curOrgId", curOrgId);
		request.setAttribute("optype",opType);
		request.setAttribute("preOrgList", AFTemplateDelegate.getHZRulePreOrgIds(orgId));
		
		/*// ���ݻ���ID,ȡ�û�������;
		String org_name = StrutsOrgNetDelegate.getAfOrgName(orgId);
		String xmlorgname = FitechUtil.getObjectName()+"bsfw.xml";
		String fileName = Config.WEBROOTPATH + "xml" + File.separator + xmlorgname; // ����һ��XML�ļ�
		List lowerOrgList = AFTemplateCollRuleDelegate.getSameLevelOrgList(afOrg);
		*//***
		 * 20121224�����޸ģ����ӻ���Ҳ����ʾ
		 *//*
		if (orgIds != null && !orgIds.equals("")) {
			String OrgIds[] = orgIds.split(",");
			HashMap hMap = null;
			if (session.getAttribute("SelectedOrgIds") == null) {
				hMap = new HashMap();

			} else {
				hMap = (HashMap) session.getAttribute("SelectedOrgIds");
				session.removeAttribute("SelectedOrgIds");
			}

			// ɾ���ӻ���
			if (orgId != null && !orgId.equals("")) {
				for (int i = 0; i < lowerOrgList.size(); i++) {
					hMap.remove(((AfOrg) lowerOrgList.get(i)).getOrgId());
				}
			}
			// ����ѡ�е���
			if (orgIds != null)
				for (int i = 0; i < OrgIds.length; i++) {
					if (OrgIds[i].trim() != null
							&& !OrgIds[i].trim().equals(""))
						hMap.put(OrgIds[i].trim(), "ok");

				}
			session.setAttribute("SelectedOrgIds", hMap);
		}
		boolean flag1 = false;
		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		document.setXMLEncoding("GB2312");

		Element rootElement = document.addElement("tree");
		rootElement.addAttribute("id", "0");
		for (Iterator iterator = lowerOrgList.iterator(); iterator.hasNext();) {
			AfOrg obj = (AfOrg) iterator.next();
			
			Element oneElement = rootElement.addElement("item");
			oneElement.addAttribute("text", obj.getOrgName());
			oneElement.addAttribute("id", obj.getOrgId());
			oneElement.addAttribute("open", "1");
			if(isContainsOrgId(orgId,templateId,obj.getOrgId())){
				oneElement.addAttribute("checked", "1");
			}
		}
		
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GB2312");
			XMLWriter output = new XMLWriter();
			output = new XMLWriter(new FileOutputStream(fileName), format);
			output.write(document);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
*/		
		makeOrgTree(orgId,templateId,operator.getUserName());
		request.setAttribute("FileName", com.fitech.gznx.common.Config.BASE_ORG_TREEXML_NAME);

		if (session.getAttribute(Config.OPERATOR_SESSION_NAME) != null)
			operator = (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_NAME);
		else
			operator = new Operator();
		// ˢ�»�����,ʹҳ����ʾ����һ��
		/**��ʹ��hibernate  ���Ը� 2011-12-21**/
		operator.reFreshOrgTreeHZRule();
//		request.setAttribute("xmlorgname", xmlorgname);
		return mapping.findForward("index");
	}
	/*
	 * �鿴���ܹ����af_template_coll_rule���Ƿ��������id
	 */
	  private boolean isContainsOrgId(String orgId,String templateId,String subOrgId) {
		// TODO Auto-generated method stub
		 boolean flag=false;
		 flag=AFTemplateCollRuleDelegate.isContainsOrgId(orgId,templateId,subOrgId);
		return false;
	}
	/**
     * ȡ�ò�ѯ����url  
     * @param logInForm
     * @return
     */
    public String getTerm(OrgInfoForm orgInfoForm)
    {
        String term="";
        
        String orgName = orgInfoForm.getOrgName();
        if(orgName!=null)
        {
            term += (term.indexOf("?")>=0 ? "&" : "?");
            term += "orgName="+orgName.toString();   
        }
        
        if(term.indexOf("?")>=0)
            term = term.substring(term.indexOf("?")+1);
            
        // System.out.println("term"+term);
        return term;
        
    }
    
    
	private void addChild(Element e, OrgNet orgNet, HttpSession session) {
		if (orgNet != null) {
			String id = orgNet.getOrgId();
			Element element = e;
			if (flag) {
				element = e.addElement("item");
				element.addAttribute("text", orgNet.getOrgName());
				element.addAttribute("id", orgNet.getOrgId());
				element.addAttribute("open", "1");
				if (orgNet.getPreOrgId().equals("true")) {
					element.addAttribute("checked", "1");
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
					child.addAttribute("open", "1");
					if (o.getPreOrgId().equals("true")) {
						child.addAttribute("checked", "1");
					}
					addChild(child, o, session);
				}
				flag = true;
			}
		}
	}
    
	private void getNode(String orgId, HttpSession session, Element rootElement, String reportFlg) {
		List lowerOrgList = null;
		if(!reportFlg.equals(com.fitech.gznx.common.Config.CBRC_REPORT)){
			lowerOrgList = AFOrgDelegate.selectLowerOrgList(orgId,
					session);	
		}else{
			lowerOrgList = StrutsOrgNetDelegate.selectLowerOrgList(orgId,
					session);
		}
		
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
	
	
	
	public static boolean makeOrgTree(String orgId,String templateId,String userName) {

		boolean result = false;
		/** ����XML */
		BaseOrgTreeIteratorHZRule orgTree = new BaseOrgTreeIteratorHZRule();
		orgTree.setOrgId(orgId);
		orgTree.setTemplateId(templateId);
		orgTree.setUserName(userName);
		/**��ʹ��hibernate ���Ը� 2011-12-22**/
		if (orgTree.createTreeForTagXml()) {
			result = true;
		}
		return result;
	}
	
	
	

}
