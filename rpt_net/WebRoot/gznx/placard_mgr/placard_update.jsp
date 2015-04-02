<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.cbrc.smis.common.Config"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html>
	<head>
		<title>�����޸�</title>
		<html:base/>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="<%=request.getContextPath() %>/css/common.css" rel="stylesheet" type="text/css">

	<script language="javascript" src="<%=request.getContextPath() %>/js/func.js"></script>
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
					alert('�����������1000��֮�ڣ����ڵ�����Ϊ��'+txtmsgBody.value.length+"����")
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
		<table width="95%" border="0" align="center">
			<tr>
				<td height="20">
					��ǰλ�� &gt;&gt; ��Ϣ���� &gt;&gt; �����޸�
				<td>
			</tr>
			<tr>
				<td height="5">
				<td>
			</tr>
		</table>

		<table width="90%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">

			<tr class="titletab">
				<th align="center" colspan="6">
					�����޸�
				</th>
			</tr>
			<tr>
				<td bgcolor="#ffffff" align="center">
					<html:form styleId="form1" action="/placard_mgr/updatePlacardAction"  enctype="multipart/form-data" method="Post" onsubmit="return validate()"> 
						
						<table width="100%" border="0" align="left" cellpadding="2" cellspacing="1">
							<logic:present name="Records" scope="request">
							<input type="hidden" name="placardId" value="<bean:write name='Records' property='placardId' />">
							<input type="hidden" id="userIdStr" name="userIdStr">
								<tr bgcolor="#ffffff">
									<td width="20%" align="right">
										�� �� :
									</td>
									<td width="80%" align="left">
										<input type="text" name="title" class="input-text" size="102" maxlength="20" value="<bean:write name='Records' property='title'/>">
									</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td valign="top" align="right">
										�� �� :
									</td>
									<td colspan="1" bgcolor="#ffffff" align="left">
										<textarea name="contents" class="input-text" rows="10" cols="100"><bean:write name='Records' property='contents' /></textarea>
									</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td align="right">
										����:
									</td>
									<td align="left">
										<logic:notEmpty name="Records" property="fileId">
											<a href="<%=request.getContextPath()%>/DownloadBlobAction.do?fileId=<bean:write name='Records' property='fileId'/>"><bean:write name="Records" property="fileName" />(<bean:write name="Records" property="fileSizeStr" />)</a>
										</logic:notEmpty>
										<logic:empty name="Records" property="fileId">��</logic:empty>
									</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td align="right">
										��������:
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
										<td colspan="1" bgcolor="#ffffff" align="left">
								
							</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td align="right" valign="top">
									</td>
									<td width="80%" align="left">
					
									<div id="treeboxbox_tree2" style="width: 100%; height: 300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
								
									</td>
								</tr>
							</logic:present>

							<tr>
								<td colspan="4" align="center" bgcolor="#ffffff">
									<INPUT type="submit" name="back" value="����" class="input-button">
									<INPUT type="button" name="back" value="����" class="input-button" onclick="window.location.assign('../placard_mgr/viewPlacardAction.do')">
								</td>
							</tr>
						</table>
					</html:form>
				</td>
			</tr>
		</table>
	<script>
		tree2=new dhtmlXTreeObject("treeboxbox_tree2","100%","100%",0);
		tree2.setImagePath("../../image/treeImgs/");
		tree2.enableCheckBoxes(1);
		tree2.enableThreeStateCheckboxes(true);
		tree2.loadXML("<%=request.getContextPath()%>/xml/<%=request.getAttribute("placrdupdateuser")%>");	
	</script>
		
	</body>
</html>
