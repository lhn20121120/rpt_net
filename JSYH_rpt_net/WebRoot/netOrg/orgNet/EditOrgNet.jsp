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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:useBean id="utilOrgTypeForm" scope="page" class="com.fitech.net.form.UtilOrgTypeForm" />
<jsp:useBean id="utilMRegionForm" scope="page" class="com.fitech.net.form.UtilMRegionForm" />

<html:html locale="true">
	<head>
	<html:base/>
    <title>�����޸�</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../../css/common.css" type="text/css" rel="stylesheet">
  </head> 
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
  <body>
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>��ǰλ�� >> 	�������� >> �����޸�</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	
		<html:form action="/updateOrgNet" method="post" onsubmit="return _submit(this)">
				<table border="0" width="50%" align="center" cellspacing="1" cellpadding="4" class="tbcolor">
					<TR class="tableHeader" >
						<TD width="15%" valign="bottom">
						�����޸�
						</TD>
					</TR>
					<logic:present name="orgNetForm" scope="request">
					<%
					OrgNetForm orgNetForm=(OrgNetForm)request.getAttribute("orgNetForm");
					String orgTypeId=String.valueOf(orgNetForm.getOrg_type_id().intValue());
					String preOrgId=orgNetForm.getPre_org_id().trim();
					String regionId=String.valueOf(orgNetForm.getRegion_id());
					List preOrgs=null;
					List mRegions=null;
					if(!orgNetForm.getPre_org_id().equals("-1"))
					{
						OrgType orgType=StrutsOrgTypeDelegate.findPreOrgTypeId1(orgNetForm.getOrg_type_id());
						if(orgType!=null)
						preOrgs=StrutsOrgNetDelegate.getAllOrgByTypeId(orgType.getOrgTypeId());
						mRegions=StrutsMRegionDelegate.selectRegions(orgNetForm.getOrg_type_id());
					}
					List typeList=StrutsOrgTypeDelegate.findAll();
					%>
					<TR align="center" valign="middle" bgcolor="#FFFFFF">
					  <TD>
						<table border="0" width="100%" align="center" cellspacing="1" cellpadding="4">
						  <TR>
						    <TD align="right">�������룺</TD>
						    <TD><html:text property="org_id" size="20" styleClass="input-text" maxlength="20"  readonly="true"/></TD>
				          </TR>
				          <TR>
						    <TD align="right">�������ƣ�</TD>
						    <TD><html:text property="org_name" size="30" styleClass="input-text" maxlength="30"/></TD>
				          </TR>
				          <TR>
				          	<TD align="right">�������ͣ�</TD>
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
				          	<TD align="right">�ϼ�������</TD>
							<TD>
								<html:select property="pre_org_id">
								<%
								// System.out.println(orgNetForm.getOrg_name());
								if(orgNetForm.getPre_org_name()==null){
									
								%>
									<html:option value="-1">--��ѡ���ϼ�����--</html:option>
								<%}
								if(orgNetForm.getPre_org_name()!=null){
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
					      <TR>
				          	<TD align="right">����������</TD>
							<TD>								
								<html:select property="region_id">
									<html:option value="-1">--��ѡ���������--</html:option>
									<html:optionsCollection name="utilMRegionForm" property="regions"/>
								</html:select>
							</TD>
				          </TR>
					      <TR>
					        <TD align="right" colspan="2">
				               <html:submit styleClass="input-button"  value="����"/>
				            </TD>
				            <TD align="center" colspan="2">
				               <input type="button" class="input-button" onclick="_back()" value="����"/>
				            </TD>
				          </TR>
				        </table>
				      </TD>
					</TR>
					</logic:present>
      				<logic:notPresent name="orgNetForm" scope="request">
						<tr align="left">
							<td bgcolor="#ffffff" colspan="9">
								���޻�����Ϣ
							</td>
						</tr>
					</logic:notPresent>
				</table>
		</html:form>	
	</body>
	<SCRIPT language="javascript">
		function _submit(form){
			if(form.org_id.value.Trim()==""){
				alert("������������룡");
				form.org_id.focus();
				return false;
			}
			if(form.org_name.value.Trim()==""){
				alert("������������ƣ�");
				form.org_name.focus();
				return false;
			}
			if(form.org_type_id.value=="-1"){
				alert("��ѡ��������ͣ�");
				form.org_type_id.focus();
				return false;
			}
			if(form.org_type_id.value=="-1"){
				alert("��ѡ��������ͣ�");
				form.org_type_id.focus();
				return false;
			}
			if(form.pre_org_id.value=="-1"){
				if(form.org_type_id.value != maxOTId){
					alert("��ѡ�������ϼ�������");
					form.pre_org_id.focus();
					return false;
				}
			}
			if(form.region_id.value=="-1"){
				alert("��ѡ�����������");
				form.region_id.focus();
				return false;
			}
		}
		function orgType_Change(optionMenu){
			removeOptions(optionMenu);
			var orgType=orgNetForm.org_type_id.value;
			var arr=map[orgType];
			optionMenu[0]=new Option("--��ѡ���ϼ�����--",-1);
			for(i=1;i<arr.length;i++){
				var obj = arr[i].split(",");
				optionMenu[i]=new Option(obj[0],obj[1]);
			}			
		}  
		function orgNet_Change(optionMenu){
			removeOptions(optionMenu);
			var orgNet=orgNetForm.pre_org_id.value;
			var arr=orgMap[orgNet];
			optionMenu[0]=new Option("--��ѡ���������--",-1);
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
		window.location="<%=request.getContextPath()%>/viewOrgNet.do";
		}
			    
	</SCRIPT>
</html:html>

