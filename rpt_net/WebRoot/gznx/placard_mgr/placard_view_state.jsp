<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html>
	<head>
		<title>����鿴״̬</title>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<script language="javascript" src="../../js/func.js"></script>
		<script language="javascript">
		String.prototype.trim= function()
 		{
   		 // ��������ʽ��ǰ��ո�
   		 // �ÿ��ַ��������

   		 return this.replace(/(^\s*)|(\s*$)/g, "");
  		}
		function query(flag){
			var flagStr;
			var mainTable = document.getElementById("main_table");
			if(flag==9999){
			
				for(var i=2;i<mainTable.rows.length ;i++){
					mainTable.rows[i].style.display="block";
				}
				return ;
			}
			
			if(flag == 1)
				flagStr='�Ѳ鿴';
				
			else if(flag == 0)
				flagStr='δ�鿴';
				
			for(var i=2;i<mainTable.rows.length ;i++){
				mainTable.rows[i].style.display="none";
				var temp = mainTable.rows[i].cells[2].innerText;
				if(temp.trim()==flagStr.trim()){
					mainTable.rows[i].style.display="block";
				}
			
			}
		
		}
	</script>
	
	</head>
	<body>
		<table width="95%" border="0" align="center">
			<tr>
				<td height="20">
					��ǰλ�� &gt;&gt; ��Ϣ���� &gt;&gt; ��Ϣ����鿴״̬
				<td>
			</tr>
			<tr>
				<td height="5">
				<td>
			</tr>
		</table>
	<table width="90%" border="0" align="center">
			<tr>
			<td>
				<fieldset id="fieldset">
						<table width="95%" border="0" align="center">
							<tr>
								<td height="5">
								<td>
							</tr>
							<tr>
								<td align="left">
									<input type=radio name="radio" onclick="query(9999)" CHECKED>
										ȫ ��
									<input type=radio name="radio" onclick="query(1)">
										�Ѳ鿴
									<input type=radio name="radio"  onclick="query(0)">
										δ�鿴&nbsp;&nbsp;
								</td>
								<td align="right">
									<input type="button" name="back" value="��  ��" class="input-button" onclick="window.location.assign('<%=request.getContextPath() %>/placard_mgr/viewPlacardAction.do')">
								</td>
							</tr>
						</table>
				</fieldset>
			</td>
		</tr>
	</table>
		<table id="main_table" width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab">
				<th align="center" colspan="4">
					����鿴���
				</th>
			</tr>
			<tr align="center" class="middle">
				<td align="center" width="30%">
					<STRONG>�û���</STRONG>
				</td>
				<td align="center" width="30%">
					<STRONG>��������</STRONG>
				</td>
				<td align="center" width="20%">
					<STRONG>�鿴״̬</STRONG>
				</td>
			</tr>
			<logic:present name="UserViewList" scope="request">
				<logic:iterate id="item" name="UserViewList" indexId="index">
					<tr bgcolor="#ffffff" style="display:block">
						<td align="center">
							<bean:write name="item" property="userName" />
						</td>
						<td align="center">
							<bean:write name="item" property="orgname" />
						</td>
						<td align="center">
							<logic:equal name="item" property="viewState" value="0">
								<font color='#FF0000'>δ�鿴</font>
							</logic:equal>
							<logic:equal name="item" property="viewState" value="1">
								<font color='#7FFF00'>�Ѳ鿴</font>
							</logic:equal>
						</td>
					</tr>
				</logic:iterate>
			</logic:present>
		</table>
	</body>
</html>
