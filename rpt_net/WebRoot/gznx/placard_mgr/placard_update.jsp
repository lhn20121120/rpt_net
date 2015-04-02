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
		<title>公告修改</title>
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
				//标题
				var title = document.getElementById('title');
				//内容
				var  contents = document.getElementById('contents');
				if(title.value=="")
				{
					alert('标题不能为空！');
					title.focus();
					return false;
				}
				if(contents.value=="")
				{
					alert('内容不能为空！');
					contents.focus();
					return false;
				}
				
				if(contents.value.length > 1000)
				{
					alert('内容请控制在1000字之内，现在的字数为：'+txtmsgBody.value.length+"个。")
					contents.focus();
					return false;
				}
				var checkIdStr=tree2.getAllChecked();	
				if(checkIdStr.replace(/(^[\s]*)|([\s]*$)/g, "")==''){
				
					alert("请至少选择一个接收用户！");
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
					当前位置 &gt;&gt; 信息公告 &gt;&gt; 公告修改
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
					公告修改
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
										标 题 :
									</td>
									<td width="80%" align="left">
										<input type="text" name="title" class="input-text" size="102" maxlength="20" value="<bean:write name='Records' property='title'/>">
									</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td valign="top" align="right">
										内 容 :
									</td>
									<td colspan="1" bgcolor="#ffffff" align="left">
										<textarea name="contents" class="input-text" rows="10" cols="100"><bean:write name='Records' property='contents' /></textarea>
									</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td align="right">
										附件:
									</td>
									<td align="left">
										<logic:notEmpty name="Records" property="fileId">
											<a href="<%=request.getContextPath()%>/DownloadBlobAction.do?fileId=<bean:write name='Records' property='fileId'/>"><bean:write name="Records" property="fileName" />(<bean:write name="Records" property="fileSizeStr" />)</a>
										</logic:notEmpty>
										<logic:empty name="Records" property="fileId">无</logic:empty>
									</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td align="right">
										重新载入:
									</td>
									<td align="left">
										<html:file property="placardFile" size="70" styleClass="input-button" />
										(不必须)
									</td>
								</tr>
								<tr bgcolor="#ffffff">
									<td align="right">
										可查看用户:
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
									<INPUT type="submit" name="back" value="保存" class="input-button">
									<INPUT type="button" name="back" value="返回" class="input-button" onclick="window.location.assign('../placard_mgr/viewPlacardAction.do')">
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
