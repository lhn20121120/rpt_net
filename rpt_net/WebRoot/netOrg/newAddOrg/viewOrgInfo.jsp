<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html lang="true">
<head>
	<html:base />

	<title>机构信息查询</title>

	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" type="text/css" rel="stylesheet">
	<script src="../../js/Org_Tree_for_xml.js"></script>

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
		
		//显示树
		function createTree()
		{
			tree=new dhtmlXTreeObject("treeObj","100%","100%",0);
			tree.setImagePath("../../image/treeImgs/");
			tree.enableCheckBoxes(0);
			tree.enableThreeStateCheckboxes(true);
			tree.loadXML("../../xml/orgInfo.xml");
		}
		
		function relodeView(){
			window.location="<%=request.getContextPath()%>/viewOrgNet.do?relode=relode";
		
		}
	</script>
</head>

<body>


	<html:form action="/template/saveDataRelationFormualSetting" method="post" styleId="frmSel">
		<table width="100%" cellSpacing="1" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
			<tr>
				<td>
					<table cellSpacing="1" cellPadding="4" width="100%" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
						<TR class="tbcolor1">
							<th align="center" height="30" colspan="2">
								<span style="FONT-SIZE: 11pt"> 机构信息</span>
							</th>
						</TR>

						<tr bgcolor="#ffffff">
							<td valign="top" colspan="2">
								<div id="treeObj" style="width:100%; height:350;background-color:#f5f5f5;border :1px solid Silver; overflow:auto;"></div>
							</td>
						</tr>


					</table>
				</td>
			</tr>
			<TR>
				<td align="center">
					<a href="javascript:relodeView()">刷新</a>
				</td>
			</TR>
		</table>
		<script language="javascript">	
					createTree();
				</script>
	</html:form>


</body>

</html:html>
