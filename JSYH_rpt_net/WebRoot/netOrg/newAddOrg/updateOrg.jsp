<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.fitech.net.adapter.StrutsMRegionDelegate" %>
<%@ page import="com.fitech.net.adapter.StrutsOrgNetDelegate" %>
<%@ page import="com.fitech.net.form.OrgNetForm"%>
<%@ page import="com.fitech.net.form.MRegionForm"%>
<%@ page import="com.fitech.net.form.OrgTypeForm"%>
<%@ page import="com.fitech.net.adapter.StrutsOrgTypeDelegate"%>
<%@ page import="com.fitech.net.adapter.StrutsMRegionDelegate"%>
<%@ page import="com.fitech.net.hibernate.OrgType"%>
<%@ page import="com.fitech.net.hibernate.OrgNet"%>
<%@ page import="java.util.*"%>
<%@ page import="com.cbrc.smis.security.Operator" %>
<%@ page import="com.cbrc.smis.common.Config" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%
	Operator operator = session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null ? 
				(Operator)session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) : new Operator();

%>
<jsp:useBean id="utilOrgTypeForm" scope="page" class="com.fitech.net.form.UtilOrgTypeForm" />
<jsp:setProperty property="orgId" name="utilOrgTypeForm" value="<%=operator.getOrgId()%>"/>
<jsp:useBean id="utilMRegionForm" scope="page" class="com.fitech.net.form.UtilMRegionForm" />


  <%
	java.util.List orgTypeList = com.fitech.net.adapter.StrutsOrgTypeDelegate.findAll();
	java.util.List orgNetList = com.fitech.net.adapter.StrutsOrgNetDelegate.findAll();
	java.util.List regionList = com.fitech.net.adapter.StrutsMRegionDelegate.findAll();
	com.fitech.net.hibernate.OrgType maxOrgType = com.fitech.net.adapter.StrutsOrgTypeDelegate.findMaxOrgTyp();
	String maxOrgTypeID = maxOrgType != null && maxOrgType.getOrgTypeId() != null ? maxOrgType.getOrgTypeId().toString() : "";
	java.util.List maxOrgNetList = com.fitech.net.adapter.StrutsMRegionDelegate.selectRegions(maxOrgType.getOrgTypeId()) != null ? 
			com.fitech.net.adapter.StrutsMRegionDelegate.selectRegions(maxOrgType.getOrgTypeId()) : new java.util.ArrayList();	
	%>
	<script>
	 var map = new Array();
	 var maxOTId = "<%=maxOrgTypeID%>";
	 var size = "<%=maxOrgNetList.size()%>";
	 var value;
	 var orgMap = new Array();
	 var orgRegionValue;
	 var maxOrgNetArray = new Array();
	 
	</script>
	<%
	for(int i=0;i<maxOrgNetList.size();i++){
		com.fitech.net.form.MRegionForm maxRegionForm = (com.fitech.net.form.MRegionForm)maxOrgNetList.get(i);
		String max_regionId = maxRegionForm.getRegion_id().toString();
		String max_regionName = maxRegionForm.getRegion_name();
		String pre_region_name = maxRegionForm.getPre_region_name() != null ? maxRegionForm.getPre_region_name().toString() : "";
		pre_region_name = !pre_region_name.trim().equals("") ? "(" + pre_region_name + ")" : "";
		i++;
	%><script>maxOrgNetArray["<%=i%>"]="<%=max_regionName%><%=pre_region_name%>,<%=max_regionId%>";	</script><% 	
	}
	
	for(java.util.Iterator iter=orgTypeList.iterator();iter.hasNext();){
		com.fitech.net.form.OrgTypeForm orgTypeForm = (com.fitech.net.form.OrgTypeForm)iter.next();
		String orgTypeId = orgTypeForm.getOrg_type_id().toString();
		int j = 0;
	%>
	<script>value=new Array();</script>
	<%
		for(int i=0;i<orgNetList.size();i++){
			com.fitech.net.form.OrgNetForm orgNetForm_orgType = (com.fitech.net.form.OrgNetForm)orgNetList.get(i);
			String orgId_orgType = orgNetForm_orgType.getOrg_id();
			String orgName_orgType = orgNetForm_orgType.getOrg_name();
			String orgTypeId_orgNet = orgNetForm_orgType.getOrg_type_id() != null ? orgNetForm_orgType.getOrg_type_id().toString() : "";
			String preOrgTypeId = "";
			com.fitech.net.hibernate.OrgType orgType = com.fitech.net.adapter.StrutsOrgTypeDelegate.findPreOrgTypeId(orgNetForm_orgType.getOrg_type_id());
			if(orgType != null && orgType.getOrgTypeId() != null) preOrgTypeId = orgType.getOrgTypeId().toString();
			if(preOrgTypeId.equals(orgTypeId)){
				j++;
	%><script>value["<%=j%>"]="<%=orgName_orgType%>,<%=orgId_orgType%>";</script><% 
			}	
		}
	%><script>map["<%=orgTypeId%>"]=value;</script><%   
	}
	
	for(java.util.Iterator iter2=orgNetList.iterator();iter2.hasNext();){
		com.fitech.net.form.OrgNetForm orgNetForm = (com.fitech.net.form.OrgNetForm)iter2.next();
		String orgId = orgNetForm.getOrg_id();
		String regionId = orgNetForm.getRegion_id() != null ? orgNetForm.getRegion_id().toString() : "";
		int k = 0;
	%>
	<script>orgRegionValue=new Array();</script>
	<%
		for(int i=0;i<regionList.size();i++){
			com.fitech.net.form.MRegionForm mRegionForm = (com.fitech.net.form.MRegionForm)regionList.get(i);
			String regionName_orgNet = mRegionForm.getRegion_name();
			String regionId_orgNet = mRegionForm.getRegion_id().toString();
			String preRegionId = "";
			String preRegionName = "";
			if(mRegionForm.getPre_region_id() != null) preRegionId = mRegionForm.getPre_region_id().toString();
			com.fitech.net.hibernate.MRegion preMRegion = com.fitech.net.adapter.StrutsMRegionDelegate.selectOne(mRegionForm.getPre_region_id());
			if(preMRegion != null && preMRegion.getRegionName() != null) preRegionName = preMRegion.getRegionName();
			String _preRegionName = !preRegionName.trim().equals("") ? "(" + preRegionName.trim() + ")" : "";
			if(regionId.equals(preRegionId)){
				k++;
	%><script>orgRegionValue["<%=k%>"]="<%=regionName_orgNet%><%=_preRegionName%>,<%=regionId_orgNet%>";</script><% 
			}	
		}
	%><script>orgMap["<%=orgId%>"]=orgRegionValue;</script><%   
	}
	%> 
	
		<html:form action="/updateOrgNet" method="post" onsubmit="return _submit(this)">
		<table cellSpacing="1" cellPadding="4" width="100%" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
			<TR class="tbcolor1">
				<th align="center" height="30" colspan="2">
					<span style="FONT-SIZE: 11pt"> 机构修改</span>
				</th>
			</TR>
			
					<logic:present name="orgNetForm" scope="request">
					<%
					
					OrgNetForm orgNetFormbean=(OrgNetForm)request.getAttribute("orgNetForm");
					String orgTypeId=String.valueOf(orgNetFormbean.getOrg_type_id().intValue());
					String preOrgId=orgNetFormbean.getPre_org_id().trim();
					String regionId=String.valueOf(orgNetFormbean.getRegion_id());
					List preOrgs=null;
					List mRegions=null;
					if(!orgNetFormbean.getPre_org_id().equals("-1"))
					{
						OrgType orgType=StrutsOrgTypeDelegate.findPreOrgTypeId1(orgNetFormbean.getOrg_type_id());
						if(orgType!=null)
						preOrgs=StrutsOrgNetDelegate.getAllOrgByTypeId(orgType.getOrgTypeId());
						mRegions=StrutsMRegionDelegate.selectRegions(orgNetFormbean.getOrg_type_id());
					}
					List typeList=StrutsOrgTypeDelegate.findSubOrgTypes(operator.getOrgId());
					%>
					
				<TR align="center" valign="middle" bgcolor="#FFFFFF">
				<TD>
				 <div  style="width:100%; height:350;background-color:#f5f5f5;border :1px solid Silver; overflow:auto;">
		
					<table border="0" width="100%" align="center" cellspacing="1"  cellpadding="7">
						  <TR>
						    <TD align="right">机构编码：</TD>
						    <TD><html:text property="org_id" size="20" styleClass="input-text" maxlength="20"  readonly="true"/></TD>
				          </TR>
				          <TR>
						    <TD align="right">机构名称：</TD>
						    <TD><html:text property="org_name" size="30" styleClass="input-text" maxlength="30"/></TD>
				          </TR>
				          <TR>
				          	<TD align="right">机构类型：</TD>
							<TD>
								<html:select property="org_type_id" onchange="orgType_Change(pre_org_id)">
								<%
									if(orgTypeId!=null && !orgTypeId.equals("")){
								
								%>
									<html:option value="<%=orgTypeId%>"><bean:write name="orgNetForm" property="org_type_name"/></html:option>
								<%
									}
									if(typeList!=null && typeList.size()>0){
										OrgTypeForm orgTypeForm=null;
										String typeId="";
										String typeName="";
										for(int k=0;k<typeList.size();k++)
										{
											orgTypeForm=(OrgTypeForm)typeList.get(k);
											typeId=String.valueOf(orgTypeForm.getOrg_type_id().intValue());
											typeName=orgTypeForm.getOrg_type_name();
											if(!typeId.equals(orgTypeId)){
								%>
											<html:option value="<%=typeId%>"><%=typeName%></html:option>
								<%
											}
										}
									}
								
								
								%>
								</html:select>
							</TD>
				          </TR>
				          <TR>
				          	<TD align="right">上级机构：</TD>
							<TD>
								<html:select property="pre_org_id">
								<%
								// System.out.println(orgNetFormbean.getOrg_name());
								if(orgNetFormbean.getPre_org_name()==null){
									
								%>
									<html:option value="-1">--请选择上级机构--</html:option>
								<%}
								if(orgNetFormbean.getPre_org_name()!=null){
								%>
									<html:option value="<%=preOrgId%>"><bean:write name="orgNetForm" property="pre_org_name"/></html:option>
								<%
									if(preOrgs!=null && preOrgs.size()>0){
										OrgNet org=null;
										String orgId="";
										String orgName="";
										for(int i=0;i<preOrgs.size();i++)
										{
											org=(OrgNet)preOrgs.get(i);
											orgId=org.getOrgId();
											orgName=org.getOrgName();
											if(!orgId.equals(preOrgId)){
								%>
											<html:option value="<%=orgId%>"><%=orgName%></html:option>
								<%
											}
										}
									}
								}
								
								%>
								</html:select>
							</TD>
				          </TR>
					      <TR>
				          	<TD align="right">机构地区：</TD>
							<TD>								
								<html:select property="region_id">
									<html:option value="-1">--请选择机构地区--</html:option>
									<html:optionsCollection name="utilMRegionForm" property="regions"/>
								</html:select>
							</TD>
				          </TR>
					      <TR>
					        <TD align="center" colspan="2">
				               <html:submit styleClass="input-button"  value="保存"/>&nbsp;
				            
				               <input type="button" class="input-button" onclick="_back()" value="返回"/>
				            </TD>
				          </TR>
				   </table>
				   </div>
				</TD>
			</TR>
		</logic:present>
		
				</table>
		</html:form>	
	<SCRIPT language="javascript">
		function _submit(form){
			if(form.org_id.value.Trim()==""){
				alert("请输入机构编码！");
				form.org_id.focus();
				return false;
			}
			if(form.org_name.value.Trim()==""){
				alert("请输入机构名称！");
				form.org_name.focus();
				return false;
			}
			if(form.org_type_id.value=="-1"){
				alert("请选择机构类型！");
				form.org_type_id.focus();
				return false;
			}
			if(form.org_type_id.value=="-1"){
				alert("请选择机构类型！");
				form.org_type_id.focus();
				return false;
			}
			if(form.pre_org_id.value=="-1"){
				if(form.org_type_id.value != maxOTId){
					alert("请选择所属上级机构！");
					form.pre_org_id.focus();
					return false;
				}
			}
			if(form.region_id.value=="-1"){
				alert("请选择机构地区！");
				form.region_id.focus();
				return false;
			}
		}
		function orgType_Change(optionMenu){
			removeOptions(optionMenu);
			var orgType=orgNetForm.org_type_id.value;
			var arr=map[orgType];
			optionMenu[0]=new Option("--请选择上级机构--",-1);
			for(i=1;i<arr.length;i++){
				var obj = arr[i].split(",");
				optionMenu[i]=new Option(obj[0],obj[1]);
			}			
		}  
		function orgNet_Change(optionMenu){
			removeOptions(optionMenu);
			var orgNet=orgNetForm.pre_org_id.value;
			var arr=orgMap[orgNet];
			optionMenu[0]=new Option("--请选择机构地区--",-1);
			for(i=1;i<arr.length;i++){
				var obj = arr[i].split(",");
				optionMenu[i]=new Option(obj[0],obj[1]);
			}
		}  
		function removeOptions(optionMenu){
			for(i=0;i<optionMenu.options.length;i++){
				//optionMenu.options[i]=null;
				optionMenu.options.remove(optionMenu.options.length-1);
			}
			optionMenu.options.length=0;
		}
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}
		
		function _back()
		{
		window.location="<%=request.getContextPath()%>/viewOrgNet.do?back=_back";
		}
			    
	</SCRIPT>


