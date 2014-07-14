<%@ page language="java" pageEncoding="GBK"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base />
	<title>�޸Ļ���</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/globalStyle.css" type="text/css" rel="stylesheet">
	<script src="../../js/Tree_for_xml.js"></script>
	<script language="javascript">
		//��ʾ��
			function createTree_org()
			{
				var fileName ="<logic:present name="FileName" scope="request"><bean:write name="FileName"/></logic:present>";
				tree=new dhtmlXTreeObject("treeObj_org","100%","100%",0);
				tree.setImagePath("../../image/treeImgs/");
				tree.enableCheckBoxes(0);
				tree.loadXML("../../tree_xml/org_tree/"+fileName);
			}
			function validate()
			{
				var orgId=document.getElementById("orgId");
				var orgName=document.getElementById("newOrgName");
				var orgType=document.getElementById("orgType");
				var orgLevel=document.getElementById("orgLevel");
				
				if(orgName==null||orgName.value==""){
					alert("������������ƣ�");
					orgName.focus();
					return false;
				}
				if(orgLevel.value=="empty"){
					alert('���������ֵ��������ӻ�����Σ�');
					return false;
				}
				if(orgType.value=="empty"){
					alert('���������ֵ��������ӻ������ͣ�');
					return false;
				}
				return true;	
			}
			//ֱ�Ӹ��»��ܹ�ϵ
			function updateCollect(){
				document.getElementById("type").value="update";
				document.getElementById("orgInfoForm").action="../../system_mgr/collect/beforeEdit.do?style=collect&&execType=${execType}&&orgId=${orgId}&&type=${type}";
				document.getElementById("orgInfoForm").submit();
			}
			//ֱ�Ӹ������ɹ�ϵ
			function updateProduct(){
				document.getElementById("type").value="update";
				document.getElementById("orgInfoForm").action="../../system_mgr/collect/beforeEdit.do?style=product&&execType=${execType}&&orgId=${orgId}&&type=${type}";
				document.getElementById("orgInfoForm").submit();
			}
			//�޸��ܷ�У���ϵ
			function updateRegion(){
				document.getElementById("type").value="update";
				document.getElementById("orgInfoForm").action="../../system_mgr/collect/collectValidateRelation.do?style=vali";
				document.getElementById("orgInfoForm").submit();
			}
			
	</script>
</head>
<body style="background: white;">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table width="98%" border="0" align="center">
		<tr height="30">
			<td>
				��ǰλ�� &gt;&gt; ϵͳ���� &gt;&gt; �������� &gt;&gt;�޸Ļ���
			<td>
		</tr>
	</table>
	<br />
	<html:form action="/system_mgr/OrgInfo/edit.do"
		onsubmit="return validate();">
		<table width="90%" border="0" align="center" cellpadding="4"
			cellspacing="1" class="tbcolor">
			<tr class="titletab">
				<th align="center" colspan="2">
					�����趨
				</th>
			</tr>
			<tr class="middle">
				<TD width="50%">
					<div align="center">
						<b>������Ϣ</b>
					</div>
					<br />
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="1" class="tbcolor">
						<tr>
							<td width="100%">
								<div id="treeObj_org"
									style="width:100%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
							<td>
						</tr>
					</table>
				</TD>

				<TD valign="top">
					<div align="center">
						<b>�޸Ļ���</b>
					</div>
					<br />
					<table height="300" width="100%" border="0" align="center"
						cellpadding="0" cellspacing="1" bgcolor="#f5f5f5">
						<tr height="25">
							<td align="right" width="30%">
								�������룺
							</td>
							<td>
							<input type="text" id="newOrgId" name="newOrgId" disabled="disabled"
									value="<bean:write name="orgInfoForm" property="orgId"/>">
								<html:hidden property="orgId" />
							<!--  	
							<logic:equal name="orgInfoForm" property="isCollect" value="1">
								
							</logic:equal>
							<logic:notEqual name="orgInfoForm" property="isCollect" value="1">
								<input type="text" id="newOrgId" name="newOrgId"
									value="<bean:write name="orgInfoForm" property="orgId"/>">
								<html:hidden property="orgId" />
							</logic:notEqual>
							-->
							</td>
						</tr>
						<tr height="25">
							<td align="right" width="30%">
								�ⱨ���룺
							</td>
							<td>
								<input type="text" id="orgOuterId" name="orgOuterId" 
									value="<bean:write name="orgInfoForm" property="orgOuterId"/>">
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								�������ƣ�
							</td>
							<td>
									<%--��ʵ����--%>
									<logic:notEqual name="orgInfoForm" property="isCollect" value="1">
										<input type="text" name="newOrgName" id="newOrgName" size="20"
											maxlength="20" 
											value="<bean:write name="orgInfoForm" property="orgName"/>">
									</logic:notEqual>
									<%--�������--%>
									<logic:equal name="orgInfoForm" property="isCollect" value="1">
										<input type="text" name="newOrgName" id="newOrgName" size="20"
											maxlength="20"
											value="<bean:write name="orgInfoForm" property="orgName"/>">
										<strong><font color="#FF0000">*</font> </strong>
									</logic:equal>
								
								<html:hidden property="orgName" />
							</td>
						</tr>
						<!-- 
						<tr height="25">
							<td align="right">
								������Σ�
							</td>
							<td>
								<%--������ʵ����--%>
								<logic:equal name="isCollect" value="0" scope="request">
									<html:select property="orgLevel">
										<logic:present name="lstOrgLevel">
											<logic:notEmpty name="lstOrgLevel">
												<html:optionsCollection name="lstOrgLevel" label="codeValue"
													value="id.codeId" />
											</logic:notEmpty>
											<logic:empty name="lstOrgLevel">
												<html:option value="0">���޻������</html:option>
											</logic:empty>
										</logic:present>
									</html:select>
								</logic:equal>
								<%--�������������������ι̶�Ϊ"����"--%>
								<logic:equal name="isCollect" value="1" scope="request">
									<html:select property="orgLevel">
										<html:option value="6">����</html:option>
									</html:select>
								</logic:equal>
							</td>
						</tr>
						 -->
						<tr height="25">
							<td align="right">
								�������ͣ�
							</td>
							<td>
								<logic:present name="orgType">
									<logic:notEmpty name="orgType">
										<input type="text" name="orgType" maxlength="20"
											size="20"
											value="<bean:write name="orgType" property="orgTypeName"/>"
											disabled="disabled">
									</logic:notEmpty>
								</logic:present>
								<logic:notPresent name="orgType">
									<logic:equal name="orgInfoForm" property="orgType" value="-99">
									<input type="text" name="orgType" maxlength="20"
										size="20" value="�����������" disabled="disabled" />
									</logic:equal>
									<logic:equal name="orgInfoForm" property="orgType" value="-99">
									<input type="text" name="orgType" maxlength="20"
										size="20" value="�����������" disabled="disabled" />
									</logic:equal>
								</logic:notPresent>
								<input type="hidden" name="orgType"
									value="<bean:write name="orgInfoForm" property="orgType" />" />
							</td>
						</tr>
						<%--������ʵ����
						<logic:equal name="isCollect" value="0" scope="request">--%>
						<tr height="25">
							<td align="right">
								����������
							</td>
							<td>
								<html:select property="orgRegion">
									<logic:present name="lstMRegion">
										<logic:notEmpty name="lstMRegion">
											<html:optionsCollection name="lstMRegion" label="region_name"
												value="region_id" />
										</logic:notEmpty>
										<logic:empty name="lstMRegion">
											<html:option value="0">���޻�������</html:option>
										</logic:empty>
									</logic:present>
								</html:select>
							</td>
						</tr>
						<tr height="25">
							<td align="right">
								�ϼ�������
							</td>
							<td>
								<logic:present name="parentOrgInfo">
									<logic:notEmpty name="parentOrgInfo">
										<input type="text" name="parentOrgName" maxlength="20"
											size="20"
											value="<bean:write name="parentOrgInfo" property="orgName"/>"
											disabled="disabled">
									</logic:notEmpty>
								</logic:present>
								<logic:notPresent name="parentOrgInfo">
									<input type="text" name="parentOrgName" maxlength="20"
										size="20" value="��" disabled="disabled" />
								</logic:notPresent>
								<input type="hidden" name="parentOrgId"
									value="<bean:write name="orgInfoForm" property="parentOrgId" />" />
							</td>
						</tr>
						<tr height="25">
							<td align="right" colspan="2">
								<table>
								<tr><td>
								
										<input type="button" name="addOrg" value="�޸��ܷ�У���ϵ"
											Class="input-button" onclick="updateRegion();" />
											&nbsp;&nbsp;&nbsp;
									</td></tr>
								<tr><td>
								
										<input type="button" name="addOrg" value="�޸����ɻ�����ϵ"
											Class="input-button" onclick="updateProduct();" />
											&nbsp;&nbsp;
									</td></tr>
								<tr><td>
										<input type="button" name="addOrg" value="�޸Ļ��ܻ�����ϵ"
											Class="input-button" onclick="updateCollect();" />
											&nbsp;&nbsp;
									</td></tr>
								<tr>
									<td>
									<html:submit value="��  ��" styleClass="input-button"></html:submit>
									&nbsp;&nbsp;
									<input type="button" name="addOrg" value="��  ��"
									Class="input-button"
									onclick="window.location.assign('../../system_mgr/OrgInfo/view.do');" />
									</td>
								</tr>
								<input type="hidden" id="type" name="type" value="">
								</table>
							</td>
						</tr>
						<tr height="25">
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
					</table>
				</TD>
			</tr>
		</table>
	</html:form>
	<script language="javascript">
		createTree_org();
	</script>
</body>
</html:html>
