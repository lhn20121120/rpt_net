<%@ page language="java" import="java.util.*" pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>fileUpload</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="${pageContext.request.contextPath }/css/common.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.4.2.js"></script>
<script language="javascript" src="${pageContext.request.contextPath }/js/progressBar.js"></script>
<script type="text/javascript">
	var  progressBar=new ProgressBar("���ڸ������ݣ����Ժ�........");
	$(function(){
		$("#uploadForm").submit(function(){
			if($("input[name='formFile']").val()==""){
				$("#ft").html("�ϴ��ļ�����Ϊ��!");
				//$("#ft").focus();
				return false;
			}		
			var type  = $("input[name='formFile']").val();
			if(type.substr(type.length-3)!="zip"){
				$("#ft").html("�ϴ��ļ�����zip��ʽ!");
				return false;
			}
			progressBar.show();
		});
		
		
	});
</script>
</head>

<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table border="0" width="98%" align="center" id="listOrder">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>��ǰλ�� &gt;&gt; ������� &gt;&gt; У�鹫ʽ�ϴ�</td>

		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	<form action="${pageContext.request.contextPath}/institution/uploadFile.do?method=execUpload" id="uploadForm" method="post" enctype="multipart/form-data">
		<table cellpadding="4" cellspacing="1" border="0" width="70%" align="center" class="tbcolor">
			<tr class="titletab" height="30px">
				<th align="center" colspan="3">
					�ϴ�����У�鹫ʽ�ļ�
				</th>
			</tr>
			<tr bgcolor="#ffffff" height="40px">
				<td align="right" width="20%">��ѡ���ϴ��ļ�:</td>
				<td width="60%" align="center"><input type="file" name="formFile" class="input-text" size="60"/></td>
				<td width="20%"><font id="ft" color="red">(ֻ֧���ϴ�zip��ʽ���ƶȰ�)</font></td>
			</tr>
			<tr bgcolor="#ffffff" height="30px">
				<td colspan="3" align="center"><input border="0" type="submit" value="��   ��"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="��  ��" onclick="location.href='${pageContext.request.contextPath}/afreportDefine.do'" >
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
