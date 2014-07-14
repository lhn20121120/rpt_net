<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html>
	<head>
		<html:base />
		<title>公告管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../css/common.css" type="text/css" rel="stylesheet">
		<script type="text/javascript">

	function _view(id)
	{
		window.open('<%=request.getContextPath()%>/bulletin/BulletinView.jsp?bullId='+id,'newwindow','height=420,width=510,top=70,left=42,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no,status=no'); //弹出窗口
	}

</script>
	</head>
	<body>



		<table border="0" width="98%" align="center">
			<tr>
				<td height="4"></td>
			</tr>
			<tr>
				<td>
					当前位置 &gt;&gt; 公告查看
				</td>


			</tr>
			<tr>
				<td height="4"></td>
			</tr>
		</table>


		<br />


		<table width="98%" border="0" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab" id="tbcolor">
				<th height="24" align="center" id="list" colspan="10">
					公告列表
				</th>
			</tr>
			<tr>
			<tr class="middle">
				<td align="center" width="40%">
					<strong>公告标题</strong>
				</td>
				<td align="center" width="15%">
					<strong>日期</strong>
				</td>
				<td align="center" width="30%">
					<strong>附件</strong>
				</td>
				<td align="center" width="10%">
					<strong>操作</strong>
				</td>

			</tr>
			<logic:present name="rsltLst" scope="request">
			<logic:iterate id="bulletin" name="rsltLst"scope="request">
				<tr bgcolor="#FFFFFF">
					<td align="center">
						
						<bean:write name="bulletin" property="bullTitle"/>
					</td>
					<td align="center">
						<bean:write name="bulletin" property="addTime"/>
					</td>
					<td align="center">
						<logic:notEmpty name="bulletin" property="uploadFileName">
							<bean:write name="bulletin" property="uploadFileName"/>
							
							<html:link action="/bulletin/bulletinNormal.do?method=download" paramName="bulletin" paramProperty="bullId" paramId="bulletId">下载</html:link>
						</logic:notEmpty>
						<logic:empty name="bulletin" property="uploadFileName">
							无附件
						</logic:empty>
						
					</td>
					<td align="center">
						<input type="button" class="input-button" value="查&nbsp;&nbsp;&nbsp看" onclick="_view('<bean:write name='bulletin' property='bullId'/>')">
					</td>
				</tr>
			</logic:iterate>
			</logic:present>
			

		</table>
	<table  width="98%" >
	<tr>
		<td>
			<jsp:include page="/apartpage.jsp" flush="false">
					<jsp:param name="url" value="../bulletin/bulletinNormal.do?method=view" />
				</jsp:include>
		</td>				
	</tr>
	</table>

	</body>
	</html>