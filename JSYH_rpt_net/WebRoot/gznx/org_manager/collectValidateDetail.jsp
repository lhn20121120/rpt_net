<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%	
	String style = "";
	if(request.getAttribute("style") != null)
		style = (String) request.getAttribute("style");
	
	String styleName = "总分校验";
	//if(style.equals("product"))
	//	styleName = "生成";
	//else
	//	styleName = "汇总";
%>

<html:html locale="true">
<head>
	<html:base />
	<title>修改<%=styleName %>关系</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/globalStyle.css" type="text/css" rel="stylesheet">
	<script src="../../js/Tree_for_xml.js"></script>
	<script language="javascript">
		//显示树
			function createTree_org()
			{
				var fileName ="<logic:present name="FileName" scope="request"><bean:write name="FileName"/></logic:present>";
				tree=new dhtmlXTreeObject("treeObj_org","100%","100%",0);
				tree.setImagePath("../../image/treeImgs/");
				tree.enableCheckBoxes(1);
				tree.loadXML("../../tree_xml/org_tree/"+fileName);
			}
			function validate()
			{
				var checkId=tree.getAllChecked();
				
				if(checkId==null||checkId==""){
						alert("至少应该选择一个机构！");
						return false;
				}
				document.getElementById("orgId").value = checkId;
				return true;		
			}
			
			function returnURL(){
				window.location.assign("../../system_mgr/OrgInfo/${execType}.do?orgId=${orgId}&&type=${type}");
			}
	</script>
</head>
<body style="background: white;">
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<table width="98%" border="0" align="center">
		<tr height="30">
			<td>
				当前位置 &gt;&gt; 系统管理 &gt;&gt; 机构管理 &gt;&gt;查看<%=styleName %>关系
			<td>
		</tr>
	</table>

	<html:form action="/system_mgr/collect/edit" method="post"
		onsubmit="return validate()">
		<table width="80%" border="0" cellpadding="4" cellspacing="1"
			class="tbcolor" align="center">
			<tr class="titletab">
				<th align="center">
					查看<%=styleName %>关系
				</th>
			</tr>
			<tr bgcolor="#ffffff">
				<td>
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td width="40%" align="right" height="40">
								机构名称：
							</td>
							<td align="left">
								<%--显示需要增加汇总关系的机构名称--%>
								<input type="text" id="viewOrgName" name="viewOrgName"
									value="<bean:write name="orgInfo" property="orgName"/>"
									disabled="disabled">
								<%--显示需要增加汇总关系的汇总ID--%>
								<input type="hidden" id="orgName" name="orgName"
									value="<bean:write name="orgInfo" property="orgName"/>">
								<input type="hidden" id="collectId" name="collectId"
									value="<bean:write name="orgInfo" property="orgId"/>">
							</td>
						</tr>
						<tr align="center">
							<td valign="top" align="right">
								查看<%=styleName %>机构：
							</td>
							<td align="left" width="100%">
								<input type="hidden" id="orgId" name="orgId">
								<div id="treeObj_org"
									style="width:80%; height:300;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto;"></div>
							<td>
						</tr>
						<tr>
							<td colspan="2" align="right">
								<div id=location>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="right">
								<input type="button" value="返  回" class="input-button"
									onclick="returnURL()" />
								&nbsp;&nbsp;
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</html:form>

	<script language="javascript">
		createTree_org();
	</script>
</body>
</html:html>
