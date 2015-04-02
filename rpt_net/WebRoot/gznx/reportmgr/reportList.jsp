<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%
	/** ����ѡ�б�־ **/
	String reportFlg = "0";	
	if (session.getAttribute(Config.REPORT_SESSION_FLG) != null){
		reportFlg = (String) session.getAttribute(Config.REPORT_SESSION_FLG);
	}
	
%>
<jsp:useBean id="FormBean" scope="page" class="com.fitech.gznx.util.FormUtil" />
<jsp:setProperty property="reportFlg" name="FormBean" value="<%=reportFlg%>"/>

<html:html locale="true">
	<head >
	<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="<%=request.getContextPath()%>/css/common.css" type="text/css" rel="stylesheet">
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/tree.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/tree/defTreeFormat.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
		<script language="javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.2.js"></script>
		<script language="javascript">
		var SPLIT_SYMBOL_COMMA="<%=Config.SPLIT_SYMBOL_COMMA%>";
		
			function addreport(){
				window.location="<%=request.getContextPath()%>/gznx/reportmgr/addNXTemplate.jsp";
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
			
			
			function treeOnClick(id,value)
			{
				document.getElementById('templateType').value = id;
				document.getElementById('templateTypeName').value = value;
				document.getElementById("orgTree").style.height = "0";
				document.getElementById('orgTree').style.visibility="hidden"; 
			}
			

		//��ʾ,�ر����Ͳ˵�
		function showTree(){
		if(document.getElementById('orgTree').style.visibility =='hidden'){
		    var textname = document.getElementById('selectedTypeName');
			document.getElementById("orgTree").style.top = getObjectTop(textname)+20;
			document.getElementById("orgTree").style.left = getObjectLeft(textname);
			
			document.getElementById("orgTree").style.height = "200";
			document.getElementById("orgTree").style.visibility = "visible";   // ��ʾ���Ͳ˵�
		}

		else if(document.getElementById("orgTree").style.visibility == "visible"){
			document.getElementById("orgTree").style.height = "0";
			document.getElementById('orgTree').style.visibility="hidden";      //�ر����Ͳ˵�
		}
	}
	
		function closeTree(objTxt,objTree){	  
		   var obj = event.srcElement;
		   if(obj!=objTxt && obj!=objTree){
		     
		     objTree.style.height = "0";
		     objTree.style.visibility="hidden";      //�ر����Ͳ˵�
		   }
		}
		
		//�����ı����ˮƽ���λ��
		function getObjectLeft(e)   
		{   
			var l=e.offsetLeft;   
			while(e=e.offsetParent)   
				l += e.offsetLeft;   
			return   l;   
		}   
		//�����ı���Ĵ�ֱ���λ��
		function getObjectTop(e)   
		{   
			var t=e.offsetTop;   
			while(e=e.offsetParent)   
				t += e.offsetTop;   
			return   t;   
		}
		
		// add by ������
		function changeToLavender(obj){
			obj.bgColor="lavender";
		}
		function changeToWhite(obj){
			
			obj.bgColor="#FFFFFF";
		}
		
		function institution(){
			window.location="<%=request.getContextPath()%>/institution/uploadFormula.jsp";
		}
		function addSqlPac()
		{
			window.location="<%=request.getContextPath()%>/fitetl.do?method=bakList";
		}

		</script>
	</head>
	<body style="TEXT-ALIGN: center">
			
			<html:form action="/afreportDefine" method="POST">
			<input type="hidden" name="templateType" value="<bean:write  name="form"  property="templateType"/>"/>
			<div align="center">
				<table border="0" width="98%">
					<tr>
						<td height="10">
						</td>
					</tr>
					<tr>
						 <td>
						 	��ǰλ�� &gt;&gt; ������� &gt;&gt; ���������
						 </td>
					</tr>
					<tr>
						<td height="10"> 
						</td>
					</tr>
				</table>
				<table border="0" width="98%">	
					<TR>
						<TD width="20%" align="right">
							�������ƣ�
						</TD>
						<TD width="20%" align="left">
							<html:text property="templateName" size="20"  styleClass="input-text"/>
						</TD>
						<td width="10%" align="left">
							<html:submit value="��  ѯ" styleClass="input-button"/>
						</td>
						<td width="50%" align="center">
							<input type="button" name="expAllTemplate"  value="��������ģ��" onclick="javascript:expreportAll()" class="input-button">
							<input type="button" name="expTemplate"  value="����ģ��" onclick="javascript:expreport()" class="input-button">
							<input type="button" name="addTemplateType" value="���ӱ�����Ϣ" onclick="javascript:addreport()" class="input-button">
							<%--
							<logic:equal value="admin" name="Operator" property="userName" scope="session">
								<input type="button" style="visibility:visble" value="����ȫ��У����Ϣ" onclick="javascript:window.location='utilAction.do';" class="input-button" />
							</logic:equal>
							--%>
							<input type="button" name="addTemplateType" value="����У�鹫ʽ" onclick="javascript:institution()" class="input-button">
						
							<input type="button" name="addTemplateType" value="У�鹫ʽ��ԭ" onclick="javascript:addSqlPac()" class="input-button">
						
						</td>
					</TR>
				</table>
			</div>
				<TABLE cellSpacing="0" width="95%" border="0" align="center" cellpadding="4">
				<br>
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
									<td class="tableHeader" width="8%">
										<b><input id="checkAll" type="checkbox" /></b>
									</td>
									<TD class="tableHeader" width="8%">
										<b>������</b>
									</td>
									<TD class="tableHeader" width="37%">
										<b>��������</b>
									</TD>
									<TD class="tableHeader" width="7%">
										<b>�汾��</b>
									</TD>
			
									<TD class="tableHeader" width="8%">
										<b>��ʼʱ��</b>
									</td>
									<TD class="tableHeader" width="8%">
										<b>����ʱ��</b>
									</td>
									<TD class="tableHeader" width="5%">
										<b>�Ƿ񷢲�</b>
									</td>
									<TD class="tableHeader" width="12%">
										<b>��ϸ����</b>
									</TD>									
								</TR>
									<logic:present name="Records" scope="request">
											<logic:iterate id="item" name="Records">
												<TR bgcolor="#FFFFFF" onmouseover="changeToLavender(this)" onmouseout="changeToWhite(this)">
													<td align="center"><input type="checkbox" name="chk" value=<bean:write name="item" property="templateId"/>_<bean:write name="item" property="versionId"/>_<bean:write name="item" property="templateName"/> /></td>
													<td align="center"><bean:write name="item" property="templateId"/></td>
													<TD align="center">
														<logic:notEmpty name="item" property="templateName">
															<bean:write name="item" property="templateName"/>
														</logic:notEmpty>
														<logic:empty name="item" property="templateName">
															��
														</logic:empty>
													</TD>
													
													<td align="center"><bean:write name="item" property="versionId"/></td>
													
													<td align="center"><bean:write name="item" property="startDate"/></td>
													<td align="center"><bean:write name="item" property="endDate"/></td>
													<td align="center">
														<logic:equal name="item" property="usingFlag" value="<%=String.valueOf(Config.IS_PUBLIC)%>">
															<a href="javascript:updateunState('<bean:write name='item' property='templateId'/>','<bean:write name='item' property='versionId'/>')"  alt="δ����"><font color="blue">�ѷ���</font></a>
														</logic:equal>
														<logic:notEqual name="item" property="usingFlag" value="<%=String.valueOf(Config.IS_PUBLIC)%>">															
															<a href="javascript:updateState('<bean:write name='item' property='templateId'/>','<bean:write name='item' property='versionId'/>')"  alt="����">δ����</a>
														</logic:notEqual>
													</td>
													<TD align="center">
														<a href="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId=<bean:write name="item" property="templateId"/>&templateName=<bean:write name="item" property="templateName"/>&versionId=<bean:write name="item" property="versionId"/>&bak2=1">�鿴</a>
														<a href="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId=<bean:write name="item" property="templateId"/>&templateName=<bean:write name="item" property="templateName"/>&versionId=<bean:write name="item" property="versionId"/>&bak2=2">�޸�</a>
														<a href="javascript:deletetemplate('<bean:write name='item' property='templateId'/>','<bean:write name='item' property='versionId'/>','<bean:write name='item' property='reportStyle'/>')" >ɾ��</a>
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
												<jsp:param name="url" value="../../afreportDefine.do" />
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
			if(confirm("�Ƿ�ģ����Ϊ����?")){
				window.location="<%=request.getContextPath()%>/afreportDefine.do?child="+child+"&version="+version;
			}
		}
		function updateunState(child,version){
			if(confirm("�Ƿ�ģ����Ϊδ����?")){
				window.location="<%=request.getContextPath()%>/afreportDefine.do?fabuflg=1&child="+child+"&version="+version;
			}
		}
		function deletetemplate(child,version,reportStyle){
			var isDelTemplateGroup = false;
			if(confirm("�Ƿ�ɾ����ģ�壿")){
				if(confirm("�Ƿ�ɾ��ģ��Ȩ��?")){
					isDelTemplateGroup = true;
				}
				window.location="<%=request.getContextPath()%>/viewAFTemplateDetail.do?templateId="+child+"&versionId="+version+"&bak2=3"+"&reportStyle="+reportStyle+"&isDelTemplateGroup="+isDelTemplateGroup;
			}
		}
	</script>
</html:html>
