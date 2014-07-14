<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
	<title>���淢��</title>
	<html:base/>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath()%>/css/common.css" rel="stylesheet" type="text/css">
	<script language="javascript" src="<%=request.getContextPath()%>/js/func.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/prototype-1.4.0.js"></script>
	<script src="<%=request.getContextPath()%>/js/Tree_for_xml.js"></script>

	<style rel="STYLESHEET" type="text/css">
			.defaultTreeTable{margin : 0;padding : 0;border : 0;}
			.containerTableStyle { overflow : auto;}
			.standartTreeRow{	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 	font-size : 14px; -moz-user-select: none; }
			.selectedTreeRow{ background-color : navy; color:white; font-family : Verdana, Geneva, Arial, Helvetica, sans-serif; 		font-size : 14x;  -moz-user-select: none;  }
			.standartTreeImage{ width:14x; height:1px;  overflow:hidden; border:0; padding:0; margin:0; }			
			.hiddenRow { width:1px;   overflow:hidden;  }
			.dragSpanDiv{ 	font-size : 12px; 	border: thin solid 1 1 1 1; }
	</style>
	<script language="javascript">
		var userTypeTreePath ="";
		var deptTreePath ="";
		function validate()
		{
			//����
			var title = document.getElementById('title');
			//����
			var  contents = document.getElementById('contents');
			if(title.value=="")
			{
				alert('���ⲻ��Ϊ�գ�');
				title.focus();
				return false;
			}
			if(contents.value=="")
			{
				alert('���ݲ���Ϊ�գ�');
				contents.focus();
				return false;
			}
			
			if(contents.value.length > 1000)
			{
				alert('�����������1000��֮�ڣ����ڵ�����Ϊ��'+contents.value.length+"����")
				contents.focus();
				return false;
			}
			var checkIdStr=tree2.getAllChecked();	
			if(checkIdStr.replace(/(^[\s]*)|([\s]*$)/g, "")==''){
			
				alert("������ѡ��һ�������û���");
				return false;
			}
			else
			{
				document.getElementById('userIdStr').value=checkIdStr;
				return true;
			}
			
		}

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
	<table width="90%" border="0" align="center">
			<tr>
				<td height="20">
					��ǰλ�� &gt;&gt; ��Ϣ���� &gt;&gt; ���淢��
				<td>
			</tr>
			<tr>
				<td height="5">
				<td>
			</tr>
		</table>
	<html:form styleId="addForm" action="/placard_mgr/addPlacardAction" method="post" enctype="multipart/form-data" onsubmit="return validate()">
		<input type="hidden" id="userIdStr" name="userIdStr">
		<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
			<tr class="titletab">
				<th align="center">
					���淢��
				</th>
			</tr>
			<tr>
				<td bgcolor="#ffffff" align="center">
					<table width="100%" border="0" align="left" cellpadding="2" cellspacing="1">
						<tr bgcolor="#ffffff">
							<td height="5">
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td width="20%" align="right">
								�� �� :
							</td>
							<td width="80%" align="left">
								<INPUT type="text" name="title" maxLength="35" class="input-text" id="title" size="102" />
							</td>

						</tr>
						<tr bgcolor="#ffffff">
							<td valign="top" align="right">
								�� �� :
							</td>
							<td colspan="1" bgcolor="#ffffff" align="left">
								<textarea name="contents" class="input-text" rows="10" cols="100" id="contents"></textarea>
								��1000�����ڣ�
							</td>
						</tr>
						<tr>
							<td align="right">
								����:
							</td>
							<td align="left">
								<html:file property="placardFile" size="70" styleClass="input-button" />
								(������)
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td align="right">
								�ɲ鿴�û�:
							</td>						
						</tr>
						<tr>
							<td align="right" valign="top">
							</td>
							<td width="80%" align="left">
							<div id="treeboxbox_tree2" style="width: 100%; height: 300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
								
							</td>
						</tr>
						<tr>
							<TD height="12"></TD>
						</tr>
						<tr>
							<td colspan="4" align="center" bgcolor="#ffffff">
								<html:submit property="submit" value="����" styleClass="input-button" />
								&nbsp;
								<INPUT type="button" name="back" value="����" class="input-button" onclick="window.location.assign('<%=request.getContextPath()%>/placard_mgr/viewPlacardAction.do')">
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
	<script>
		tree2=new dhtmlXTreeObject("treeboxbox_tree2","100%","100%",0);
		tree2.setImagePath("../../image/treeImgs/");
		tree2.enableCheckBoxes(1);
		tree2.enableThreeStateCheckboxes(true);
		tree2.loadXML("<%=request.getContextPath()%>/xml/<%=request.getAttribute("placrdadduser")%>");	
	</script>
</body>
</html:html>
