<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.fitech.gznx.form.CodeLibForm"%>
<jsp:useBean id="utilFormBean" scope="page" class="com.cbrc.smis.form.UtilForm" />
<%
	String typeName = (String)request.getAttribute("typeName");
%>
<html:html locale="true">
<head>
	<html:base />
	<title>系统字典管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/globalStyle.css" rel="stylesheet" type="text/css">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<link href="../../css/calendar-blue.css" type="text/css"
		rel="stylesheet">
	<script type="text/javascript" src="../../calendar/calendar.js"></script>
	<script type="text/javascript" src="../../calendar/calendar-cn.js"></script>
	<script language="javascript" src="../../calendar/calendar-func.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
	<script language="javascript">
			function updateCodeLib(codeTypeId,codeId,effectiveDate,isModi)
			{
				if(isModi==1){
					document.getElementById("codeTypeId_upd").value=codeTypeId;
					document.getElementById("codeId_upd").value=codeId;
					document.getElementById("effectiveDate_upd").value=effectiveDate;
					document.getElementById("form2").submit();
				}else{
					alert("不可以修改!")
				}
			}
			function deleteCodeLib(codeTypeId,codeId,effectiveDate,isModi)
			{
				if(isModi==1){
					if(confirm("你确定要删除该参数信息吗?"))
					{
						document.getElementById("codeTypeId_del").value=codeTypeId;
						document.getElementById("codeId_del").value=codeId;
						document.getElementById("effectiveDate_del").value=effectiveDate;
						document.getElementById("form3").submit();
					}
				}else{
					alert("不可以删除!")
				}
			}
			window.onload=function(){
				var options=document.getElementsByName("codeTypeId")[0].options;
				var codeTypeId="${QueryForm.codeTypeId}";
				if(codeTypeId=="")
					return;
				for(i=0;i<options.length;i++){
					if(options[i].value==codeTypeId)
						options[i].selected="selected";
				}		
			}
			
			function getCodeTypeValue(){
			//	var hiddenCodeTypeValue = document.getElementsByName("codeTypeValue")[0];
			//	var codeTypeValue =  document.getElementByName("codeTypeId").value;
			//	alert(codeTypeValue);
			//	hiddenCodeTypeVlue.value = codeTypeValue;
				var codeTypeId = document.getElementById("codeTypeId");
				document.getElementById("typeName").value=codeTypeId.options[codeTypeId.selectedIndex].text;
			//	alert(codeTypeId.options[codeTypeId.selectedIndex].value);
				return true;
			}
			function initmethod(){
			    var typeName ='<%=typeName%>';
				var codeTypeId = document.getElementById("codeTypeId");
				var ops = codeTypeId.options;
				for(i=0;i<ops.length;i++){
					if(ops[i].text==typeName){
						ops[i].selected=true;
						break;
					}
				}
			}
	</script>
</head>

<body onload="initmethod()">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table width="90%" border="0" align="center" cellpadding="4"
		cellspacing="0">
		<tr>
			<td height="30" colspan="2">
				当前位置 >> 系统管理 >> 字典管理
			<td>
		</tr>
		<tr>
			<td>
				<html:form action="/system_mgr/viewCodeLib.do" method="POST" onsubmit="return getCodeTypeValue()">
					<div>
						<table>
							<tr>							
								<td align="right" width="45%">
									字典类型：
									<html:select property="codeTypeId" styleId="codeTypeId">									
									<html:optionsCollection name="utilFormBean" property="typeName"/>
									</html:select>
									<html:hidden property="typeName" styleId="typeName"/>						
								</td>									
								<td width="25%" align="left">
									<html:submit value="查  询" styleClass="input-button" />
									<html:hidden property="codeTypeValue" />
								</td>
							</tr>
						</table>
					</div>
				</html:form>
			</td>
			<td>
				<table width="20%" align="right">
					<tr>
						<td align="right">
							<input type="submit" class="input-button" value="增加参数"
								onclick="window.location.assign('../gznx/codeLib_mgr/addCodeLib.jsp')">
						</td>
						<td width="10"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table width="90%" border="0" cellpadding="4" cellspacing="1"
		class="tbcolor" align="center">
		<tr class="titletab">
			<th height="25" align="center" colspan="10">
				系统字典信息
			</th>
		</tr>
		<tr align="center" class="middle">
			<td align="center" width="5%">
				序号
			</td>
			<td align="center" width="10%">
				字典类型ID
			</td>
			<td align="center" width="20%">
				字典类型值
			</td>
			<td align="center" width="8%">
				字典ID
			</td>
			<td align="center" width="20%">
				字典值
			</td>

			<td align="center" width="10%">
				可否修改
			</td>
			<td align="center" width="8%">
				修改
			</td>
			<td align="center" width="8%">
				删除
			</td>
		</tr>
		<logic:present name="Records" scope="request">
			<logic:iterate id="item" name="Records" indexId="index">
				<tr align="center">
					<td bgcolor="#ffffff">
						<%=((Integer) index).intValue() + 1%>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="codeTypeId">
							<bean:write name="item" property="codeTypeId" />
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="codeTypeValue">
							<bean:write name="item" property="codeTypeValue" />
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="codeId">
							<bean:write name="item" property="codeId" />
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="codeValue">
							<bean:write name="item" property="codeValue" />
						</logic:notEmpty>
					</td>

					<td bgcolor="#ffffff">
						<logic:equal name="item" property="isModi" value="0">
							不可以
						</logic:equal>
						<logic:notEqual name="item" property="isModi" value="0">
							可以
						</logic:notEqual>
					</td>
					<td bgcolor="#ffffff">
						<a
							href="javascript:updateCodeLib('<bean:write name="item" property="codeTypeId"/>','<bean:write name="item" property="codeId"/>','<bean:write name="item" property="effectiveDate"/>','<bean:write name="item" property="isModi" format="#"/>')">修改</a>
					</td>
					<td bgcolor="#ffffff">
						<a
							href="javascript:deleteCodeLib('<bean:write name="item" property="codeTypeId"/>','<bean:write name="item" property="codeId"/>','<bean:write name="item" property="effectiveDate"/>','<bean:write name="item" property="isModi" format="#"/>')">删除</a>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="10">
					暂无参数信息
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<table cellSpacing="1" cellPadding="4" width="100%" border="0">
		<tr>
			<TD colspan="7" bgcolor="#FFFFFF">
				<jsp:include page="../../apartpage.jsp" flush="true">
					<jsp:param name="url" value="../../system_mgr/viewCodeLib.do" />
				</jsp:include>
			</TD>
		</tr>
	</table>
	<html:form styleId="form2" action="/system_mgr/codeLibUpdate"
		method="post">
		<input type="hidden" id="codeTypeId_upd" name="codeTypeId">
		<input type="hidden" id="codeId_upd" name="codeId">
		<input type="hidden" id="effectiveDate_upd" name="effectiveDate">
	</html:form>
	<html:form styleId="form3" action="/system_mgr/deleteCodeLib"
		method="post">
		<input type="hidden" id="codeTypeId_del" name="codeTypeId">
		<input type="hidden" id="codeId_del" name="codeId">
		<input type="hidden" id="effectiveDate_del" name="effectiveDate">
	</html:form>
</body>
</html:html>
