<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
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
		function createTree(){
			tree=new dhtmlXTreeObject("treeObj","100%","100%",0);
			tree.setImagePath("../../image/treeImgs/");
			tree.enableCheckBoxes(0);
			tree.enableThreeStateCheckboxes(true);
			tree.setOnDblClickHandler(treeOnDBClick);
			tree.loadXML("../../xml/orgInfo.xml");
		}
		
		//树节点双击事件 取得机构ID
		function treeOnDBClick(id){
			var OrgID=id;	
			if(OrgID!="")
			{
			var url="<%=request.getContextPath()%>/popedom_mgr/showUserInfo.do?orgId="+OrgID;
			 window.parent.showUserInfo.location.assign(url);				
			}	
		}
		function flushTreeView(){
			window.top.location="<%=request.getContextPath()%>/orgNetInfoTree.do";
		}
		
	</script>
</head>

<body>	
	<table cellSpacing="1" cellPadding="4" width="96%" border="0" align="center" bgcolor="#ffffff" class="tbcolor">
		<TR class="tbcolor1">
			<th align="center" height="30" colspan="2">
				<span style="FONT-SIZE: 11pt"> 机构信息</span>
			</th>
		</TR>
		<tr>
			<td colspan="2" align="left" bgcolor="#EEEEEE">
				机构信息树:
			</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td valign="top" colspan="2">
				<div id="treeObj" style="width:100%; height:200;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
			</td>
		</tr>
	</table>
	<script language="javascript">	
		createTree();
	</script>
	<TABLE>
		<TR><TD><input type="button" name="flushButton" value="刷新机构树" styleClass="input-button" onclick="flushTreeView()"></TD></TR>
	</TABLE>
</body>
</html:html>
