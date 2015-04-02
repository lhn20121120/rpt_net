<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<html:html locale="true">
	<head>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="../../js/func.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.2.js"></script>
		<script language="javascript">
		var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
			function addreport(){
				window.location="<%=request.getContextPath()%>/template/add/addFZTemplate.jsp";
			}
			
			function addFormulaPac(){
				window.location="<%=request.getContextPath()%>/institutioncbrc/upLoadZipPac.jsp";
			}
			
			function addSqlPac()
			{
				window.location="<%=request.getContextPath()%>/fitetlcbrc.do?method=list";
			}
			
			function addTempPac()
			{
				window.location="<%=request.getContextPath()%>/addtemp/addTemp.jsp";
			}
			
			//�������б���
			function expreportAll(){
				
				window.location="<%=request.getContextPath()%>/gznx/reportmgr/expReportTemplate.do?type=all&repNames=all";
			}
			
			//�������� add by wmm
			function expreport(){
				if(hasOneSelected()){
					alert("��ѡ��Ҫ���ص��ļ�");
					return;
				}else{
					var repNames="";
					var objChks=document.getElementsByName("chk");
					for ( var i = 0; i < objChks.length; i++) {
						if(objChks[i].checked){
							repNames+=(repNames==""?"":SPLIT_SYMBOL_COMMA)+objChks[i].value;
						}
					}
					window.location="<%=request.getContextPath()%>/gznx/reportmgr/expReportTemplate.do?type=checkbox&repNames="+repNames;	
				}
			
			}
			
			//�Ƿ���ѡ�е��ļ� add by wmm
			function hasOneSelected(){
				var flag=true;
				var objChks=document.getElementsByName("chk");
				for ( var i = 0; i < objChks.length; i++) {
					if(objChks[i].checked){
						flag= false;
						break;
					}
				}
				return flag;
			}
		
			
			//ѡ�����и�ѡ�� add by wmm
			$(function(){
				$("#checkAll").click(function(){
					
					$("input[name='chk']").attr("checked",this.checked);
					
					var subBox=$("input[name='chk']");
					
					subBox.click(function(){
						
						$("#checkAll").attr("checked",subBox.length==$("input[name='chk']:checked").length ? true: false);
					});
				});
			});
			// add by ������
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
		
		function del(childRepId,versionId){
			if(childRepId=="" || versionId=="")
			  	return;
			var isDelTemplateGroup = false;
			if(confirm("�Ƿ�ɾ��ģ��?")){
				var res = confirm("�Ƿ�ɾ��ģ��Ȩ��?");
				if(res){
					isDelTemplateGroup = true;
				}
				location.href='<%=request.getContextPath()%>/template/viewTemplateDetail.do?childRepId='+childRepId+'&versionId='+versionId+'&type=del&isDelTemplateGroup='+isDelTemplateGroup;
			}
		}
		</script>
	</head>
	<body style="TEXT-ALIGN: center">
			
			<html:form action="/template/viewTemplate" method="POST">
			<div align="center">
				<table border="0"width="95%">
					<tr>
						<td height="8">
						</td>
					</tr>
					<tr>
						 <td>
						 	��ǰλ�� >> ������� >> ���������
						 </td>
					</tr>
					<tr>
						<td height="10"> 
						</td>
					</tr>
					 <br>
					<TR>
						<td>
							<fieldset id="fieldset">
							<table cellspacing="0" cellpadding="0" border="0" width="100%" align="center">
								<tr>
										<td height="5"></td>
									</tr>
								<tr>
								<tr>
									<TD width="25%" align="right">
										��������/��ţ�<html:text property="reportName" size="15"  styleClass="input-text"/>
									</TD>
									<TD width="10%" align="center">
										<html:submit value="��  ѯ" styleClass="input-button"/>
									</TD>
									<td width="10%" align="left">
										
									</td>
									<td width="55%" align="center">
										<input type="button" name="expAllTemplate"  value="��������ģ��" onclick="javascript:expreportAll()" class="input-button">
										<input type="button" name="expTemplate"  value="����ģ��" onclick="javascript:expreport()" class="input-button">
										<input type="button" name="addTemplateType" value="���ӱ�����Ϣ" onclick="javascript:addreport()" class="input-button">
										<input type="button" name="addTemplateType" value="У�鹫ʽ����" onclick="javascript:addFormulaPac()" class="input-button">
										<input type="button" name="addTemplateType" value="����sql" onclick="javascript:addSqlPac()" class="input-button">
										<input type="button" name="addTemplateZip" value="����ģ��" onclick="javascript:addTempPac()" class="input-button">
									</td>
								</tr>
							</table>
							</fieldset>
						</td>
						
					</TR>
				</table>
		</div>
				<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
				<tr align="right">
				   <br>
				  
				  </tr>
					<!-- <TR><TD><FONT color="#FF0000">��ɫ��ʾΪ�Զ���ģ��</FONT></TD></TR>-->
					<TR>
						<TD>
							<TABLE cellSpacing="1" cellPadding="4" width="100%" border="0"  class="tbcolor">
								<tr class="titletab">
									<th colspan="8" align="center" id="list">
										<strong>
											�����б�
										</strong>
									</th>
								</tr>
								<TR class="middle">
									<td class="tableHeader" width="5%">
										<b><input id="checkAll" type="checkbox" /></b>
									</td>
									<TD class="tableHeader" width="8%">
										<b>������</b>
									</td>
									<TD class="tableHeader" width="35%">
										<b>��������</b>
									</TD>
									
									<TD class="tableHeader" width="8%">
										<b>�汾��</b>
									</td>
									<TD class="tableHeader" width="18%">
										<b>��������</b>
									</TD>
									<TD class="tableHeader" width="8%">
										<b>���ҵ�λ</b>
									</td>
									<TD class="tableHeader" width="8%">
										<b>�Ƿ񷢲�</b>
									</td>
									<TD class="tableHeader" width="10%">
										<b>��ϸ��Ϣ</b>
									</TD>									
								</TR>
									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<TR bgcolor="#FFFFFF"  onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
												<td align="center"><input type="checkbox" name="chk" value=<bean:write name="item" property="childRepId"/>_<bean:write name="item" property="versionId"/>_<bean:write name="item" property="reportName"/> /></td>
													<td align="center"><bean:write name="item" property="childRepId"/></td>
													<TD align="center">
														<logic:notEmpty name="item" property="reportName">
														<!-- 
															<a href="<%=request.getContextPath()%>/servlets/selfReadExcelServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" >
														-->
															<!-- <a href="<%=request.getContextPath()%>/servlets/ReadReportControlServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" >
															<bean:write name="item" property="reportName"/>
															</a>-->
															<logic:equal name="item" property="templateType" value="excel">
																<a href="<%=request.getContextPath()%>/servlets/selfReadExcelServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>" >
																	<FONT color="#FF00FF"><bean:write name="item" property="reportName"/></FONT>
																</a>
															</logic:equal>
															<logic:equal name="item" property="templateType" value="">
																<a href="<%=request.getContextPath()%>/servlets/selfReadExcelServlet?childRepId=<bean:write name='item' property='childRepId'/>&versionId=<bean:write name='item' property='versionId'/>">
																	<bean:write name="item" property="reportName"/>
																</a>
															</logic:equal>
														</logic:notEmpty>
														<logic:empty name="item" property="reportName">
															��
														</logic:empty>
													</TD>
													
													<td align="center"><bean:write name="item" property="versionId"/></td>
													<TD align="center">
														<logic:notEmpty name="item" property="repTypeName">
															<bean:write name="item" property="repTypeName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="repTypeName">
															��
														</logic:empty>
													</TD>
													<td align="center"><bean:write name="item" property="curUnitName"/></td>
													<td align="center">
														<logic:equal name="item" property="isPublic" value="<%=String.valueOf(Config.IS_PUBLIC)%>">
															<a href="javascript:updateState('<bean:write name='item' property='childRepId'/>','<bean:write name='item' property='versionId'/>')" alt="���·���"><font color="blue">�ѷ���</font></a>
														</logic:equal>
														<logic:notEqual name="item" property="isPublic" value="<%=String.valueOf(Config.IS_PUBLIC)%>">
															<a href="javascript:yupdateState('<bean:write name='item' property='childRepId'/>','<bean:write name='item' property='versionId'/>')" alt="���·���"><font color="blue">δ����</font></a>
														</logic:notEqual>
													</td>
													<TD align="center">
														<a href="../viewTemplateDetail.do?childRepId=<bean:write name="item" property="childRepId"/>&reportName=<bean:write name="item" property="reportName"/>&versionId=<bean:write name="item" property="versionId"/>">�鿴</a>
														<a href="../viewTemplateDetail.do?childRepId=<bean:write name="item" property="childRepId"/>&reportName=<bean:write name="item" property="reportName"/>&versionId=<bean:write name="item" property="versionId"/>&bak2=2">�޸�</a>
														<a onclick="del('<bean:write name="item" property="childRepId"/>','<bean:write name="item" property="versionId"/>')" href="javascript:;">ɾ��</a>
													</TD>
												</TR>
											</logic:iterate>
									</logic:present>
									<logic:notPresent name="Records" scope="request">
										<tr bgcolor="#FFFFFF">
											<td colspan="8">
												���޼�¼
											</td>
										</tr>
									</logic:notPresent>
								</TABLE>
									<table cellSpacing="1" cellPadding="4" width="100%" border="0">
										<tr>
											<TD colspan="7" bgcolor="#FFFFFF">
											<jsp:include page="../../apartpage.jsp" flush="true">
												<jsp:param name="url" value="../viewTemplate.do" />
											</jsp:include>
											</TD>
										</tr>
									</table>
						</TD>
					</TR>
				</TABLE>	
	   
			</html:form>	
			
	</body>
	<script language="javascript">
		function updateState(child,version){
			if(confirm("�Ƿ�ģ����Ϊδ����?")){
				window.location="../template/viewTemplate.do?child="+child+"&version="+version+"&usingFlag=0";
			}
		
		}
		function yupdateState(child,version){
			if(confirm("�Ƿ�ģ����Ϊ�ѷ���?")){
				window.location="../template/viewTemplate.do?child="+child+"&version="+version+"&usingFlag=1";
			}
		
		}
	</script>
</html:html>
