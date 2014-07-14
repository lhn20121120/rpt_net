<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
	<head>
	<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" type="text/css" rel="stylesheet">
			<script language="javascript" src="../script/globalScript.js" type="text/javascript"></script>
	</head>
	<body>
	
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td align="center"><h2><bean:write name="repName"/></h2></td>
		</tr>
	</table>
		<table width="95%" border="0" cellpadding="4" cellspacing="1"  class="tbcolor">
				<tr class="tableHeader">
					<td align="left" colspan="4"><b>������У���ϵ</b></td>
				</tr>	
				<tr bgcolor="#FFFFFF">
					<td align="center" width="10%">У���ϵ</td>
					<td align="center" width="75%">У�鹫ʽ</td>
					<td align="center" width="10%">У����</td>
				</tr>
		<logic:present name="p2p" scope="request">
			<logic:iterate name="p2p" id="item">
				<tr bgcolor="#FFFFFF">
					<td align="center">
							<bean:write name="item" property="validateTypeName"/>
					</td>
					<td align="center">
							<bean:write name="item" property="cellFormu"/> 
					</td>
					<td align="center">
						<logic:equal name="item" property="result" value="0">��</logic:equal>
						<logic:equal name="item" property="result" value="1">��</logic:equal>
					</td>
				</tr>
				<tr height="50">
					<td bgcolor="#FFFFFF" align="center">
						У������ϸ
					</td>
					<logic:present name="p2p" scope="request">
						<td align="center" colspan="2"  bgcolor="#FFFFFF">
							<textArea name="cause" cols="20" rows="8" style="WIDTH:100%"<bean:write name="item" property="cause"/>"/>
						</td>
					</logic:present>
					<logic:notPresent name="p2p" scope="request">
						<td align="center" colspan="2"  bgcolor="#FFFFFF">
							-
						</td>
					</logic:notPresent>
				</tr>
			</logic:iterate>
		</logic:present>
				
				<logic:notPresent name="p2p" scope="request">
					<tr align="left">
						<td bgcolor="#ffffff" colspan="6" align="center">
						��ƥ���¼
						</td>
					</tr>
				</logic:notPresent>
		</table>
		<table>
			<tr align="center">
				<td height="10">
				</td>
			</tr>
		</table>
	
		 <table cellSpacing="1" cellPadding="4" width="95%" border="0" class="tbcolor">
			<tr class="tableHeader">
				<td align="left" colspan="4"><b>�����쳣�仯���</b>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
			<td align="center" width="25%">������������׼</td>
			<td align="center" width="25%">�������½���׼</td>
			<td align="center" width="25%">������ͬ��������׼</td>
			<td align="center" width="25%">������ͬ���½���׼</td>
		</tr>	
	<logic:present name="abnormity" scope="request">
			<logic:iterate name="abnormity" id="item">
				<tr bgcolor="#FFFFFF">
					<td align="center">
							<bean:write name="item" property="prevRiseStandard"/>
					</td>
					<td align="center">
							<bean:write name="item" property="prevFallStandard"/>
					</td>
					<td align="center">
							<bean:write name="item" property="sameRiseStandard"/> 
					</td>
					<td align="center">
						<bean:write name="item" property="sameFallStandard"/> 
					</td>
				</tr>	
		</logic:iterate>
	</logic:present>
				
			<logic:notPresent name="abnormity" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="6" align="center">
						��ƥ���¼
					</td>
				</tr>
			</logic:notPresent>
		</table>
		
		<table>
			<tr align="center">
				<td height="10">
				</td>
			</tr>
		</table>

		<table table cellSpacing="1" cellPadding="4" width="95%" border="0" class="tbcolor">
			<tr class="tableHeader">
				<td align="left"><b>�ٱ�������</b>
				</td>
			</tr>
		<logic:present name="day" scope="request">
			<tr bgcolor="#ffffff">
				<td align="center">���� <bean:write name="day"/> ��</td>
			</tr>
		</logic:present>
		<logic:notPresent name="day" scope="request">
			<tr align="center">
				<td bgcolor="#ffffff" colspan="6" align="center">
					��ƥ���¼
				</td>
			</tr>
		</logic:notPresent>
		
		</table>
		<table>
			<tr align="center">
				<td height="10">
				</td>
			</tr>
		</table>
		<input class="input-button" id="OK2" onclick="window.history.back()" type="submit" value=" �� �� " name="OK2">
	</body>
</html:html>
