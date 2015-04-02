<%@ page contentType="text/html;charset=gb2312"%>
<%@ page
	import="com.cbrc.smis.common.Config,java.util.List,org.apache.struts.util.LabelValueBean"%>
<%@ page import="com.cbrc.smis.security.Operator"%>
<%@ page import="com.fitech.gznx.form.OnlineUserForm"%>

<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html:html locale="true">
<head>
	<html:base />
	<title>在线工作人员列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="../css/pageControl.css" />
	<script type="text/javascript" src="../../js/prototype-1.4.0.js"></script>

	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<link href="../../css/tree.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="../../js/tree/tree.js"></script>
	<script type="text/javascript" src="../../js/tree/defTreeFormat.js"></script>
	<link href="../../css/globalStyle.css" rel="stylesheet" type="text/css">

	<link href="../../css/common.css" rel="stylesheet" type="text/css">
	<script src="../../js/Tree_for_xml.js"></script>

	
	<%
		Operator operator = null;
		if (session.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME) != null)
			operator = (Operator) session
			.getAttribute(Config.OPERATOR_SESSION_ATTRIBUTE_NAME);
		else
			operator = new Operator();
		// String orgTreePath = operator.getOrgTreeByUserWithOrgPath();
	%>

</head>
<script language="javascript">
  
  	//树节点双击事件 --选择机构
		function treeOnClick(id,value)
		{
			
			document.getElementById('query_orgId').value = id;
			document.getElementById('orgName_query').value = value;
			document.getElementById("orgTree").style.height = "0";
			document.getElementById('orgTree').style.visibility="hidden"; 
		}

	
	//显示,关闭树型菜单
	function showTree(){
		if(document.getElementById('orgTree').style.visibility =='hidden'){
		    var textname = document.getElementById('orgName_query');
			document.getElementById("orgTree").style.top = getObjectTop(textname)+20;
			document.getElementById("orgTree").style.left = getObjectLeft(textname);
			
			document.getElementById("orgTree").style.height = "200";
			document.getElementById("orgTree").style.visibility = "visible";   // 显示树型菜单
		}

		else if(document.getElementById("orgTree").style.visibility == "visible"){
			document.getElementById("orgTree").style.height = "0";
			document.getElementById('orgTree').style.visibility="hidden";      //关闭树型菜单
		}
	}	
				
	
	//距离文本框的水平相对位置
	function getObjectLeft(e)   
	{   
		var l=e.offsetLeft;   
		while(e=e.offsetParent)   
			l += e.offsetLeft;   
		return   l;   
	}   
	//距离文本框的垂直相对位置
	function getObjectTop(e)   
	{   
		var t=e.offsetTop;   
		while(e=e.offsetParent)   
			t += e.offsetTop;   
		return   t;   
	}
	
	//用户查询
	function userQuery(){
		//用户名
		var userId = document.getElementById('operatorId_query');

		//机构名
		var org_name = document.getElementById('orgName_query');

		
		document.getElementById("query_operatorId").value = userId.value;

		document.getElementById("query_orgName").value = org_name.value;

		document.getElementById("form_user").submit();
	}
	
	function getLastQuery()
	{
		var query_userId ="<logic:present name='QueryForm' scope='request'><logic:notEmpty name='QueryForm' property='operatorId'><bean:write name='QueryForm' property='operatorId' /></logic:notEmpty></logic:present>";
		var query_orgId ="<logic:present name='QueryForm' scope='request'><logic:notEmpty name='QueryForm' property='orgId'><bean:write name='QueryForm' property='orgId' /></logic:notEmpty></logic:present>";
		var userTypeId = "<logic:present name='QueryForm' scope='request'><bean:write name='QueryForm' property='userTypeId'/></logic:present>";
		var query_orgName ="<logic:present name='QueryForm' scope='request'><logic:notEmpty name='QueryForm' property='orgName'><bean:write name='QueryForm' property='orgName' /></logic:notEmpty></logic:present>";
		var query_ipAdd ="<logic:present name='QueryForm' scope='request'><logic:notEmpty name='QueryForm' property='ipAdd'><bean:write name='QueryForm' property='ipAdd' /></logic:notEmpty></logic:present>";
	
		if(query_userId!=null && query_userId!='')
			document.getElementById("operatorId_query").value = query_userId;
		
		if(query_orgName!=null && query_orgName!='')
			document.getElementById("orgName_query").value = query_orgName;
		
		if(query_ipAdd!=null && query_ipAdd!='')
			document.getElementById("ipAdd_query").value = query_ipAdd;
				
		if(userTypeId!=null && userTypeId!='')
		{
			var userTypeList = document.getElementById('userTypeId_query');
				for(var i=0;i<userTypeList.length;i++)
		  		{
		  			if(userTypeList.options[i].value == userTypeId)
		  			{		
		  				userTypeList.selectedIndex = i;
		  				break;
		  			}
		  		}		
		 }
	}
	
	</script>
<body>
	
	<table width="100%" border="0" align="center" cellpadding="4"
		cellspacing="0">
		<tr>
			<td height="30" colspan="2">
				当前位置 >> 系统管理 >> 在线工作人员列表
			<td>
		</tr>
	</table>
	<fieldset id="fieldset">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td height="6"></td>
			</tr>
			<tr height="32">
				<td align="right">
					用户名：
				</td>
				<td align="left">
					<input type="text" id="operatorId_query" name="operatorId"
						size="20" maxlength="12" class="input-text">
				</td>
				<td align="right">
					所属机构：
				</td>
				<td align="left">					
					<input type="text" id="orgName_query" readonly name="orgName"
						style="width:200px;cursor:hand" onclick="return showTree()"
						Class="input-text" />
					<div id="orgTree"  
					style="left:316px;top:70px;width:200px; height:0;background-color:#f5f5f5;border :1px solid Silver;; overflow:auto; VISIBILITY: hidden; position:absolute; z-index:2;">					
					
					<script type="text/javascript">
						<bean:write  name="QueryForm"  property="treeOrgContent" filter="false"/>
					    var tree= new ListTree("tree", TREE1_NODES,DEF_TREE_FORMAT,"","treeOnClick('#KEY#','#CAPTION#');");
				      	tree.init();
				 	</script>
   					
					</div>
				</td>
				<td align="right">
					<input type="submit" class="input-button" value=" 查  询 "
						onclick="return userQuery();">
					&nbsp;&nbsp;
					<INPUT type="button" name="onlineUser" value=" 刷  新 "
						class="input-button"
						onclick="location.assign('../system_mgr/viewOnlineUser.do')">
					&nbsp;&nbsp;
				</td>
			</tr>
			
		</table>
	</fieldset>
	<p></p>
	<table width="100%" border="0" cellpadding="4" cellspacing="1"
		class="tbcolor" align="center">
		<tr class="titletab">
			<th height="25" align="center" colspan="10">
				在线工作人员列表
			</th>
		</tr>
		<tr align="center" class="middle">
			<td align="center" width="5%">
				序号
			</td>
			<td align="center" width="15%">
				用户名
			</td>
			<td align="center" width="15%">
				姓名
			</td>
			<td align="center" width="25%">
				所属机构
			</td>
			<td align="center" width="15%">
				部门
			</td>
			<td align="center" width="10%">
				IP地址
			</td>
		</tr>
		<logic:present name="Records" scope="request">
			<logic:iterate id="item" name="Records" indexId="index">
				<tr align="center">
					<td bgcolor="#ffffff">
						<%=((Integer) index).intValue() + 1%>
					</td>
					<%
								String startDate = (new SimpleDateFormat("yyyy-MM-dd"))
								.format(new Date());
					%>
					<td bgcolor="#ffffff">						
							<logic:notEmpty name="item" property="userName">
								<bean:write name="item" property="userName" />
							</logic:notEmpty> 
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="operatorName">
							<bean:write name="item" property="operatorName" />
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="orgName">
							<bean:write name="item" property="orgName" />
						</logic:notEmpty>
					</td>
					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="deptName">
							<bean:write name="item" property="deptName" />
						</logic:notEmpty>
					</td>

					<td bgcolor="#ffffff">
						<logic:notEmpty name="item" property="ipAdd">
							<bean:write name="item" property="ipAdd" />
						</logic:notEmpty>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="Records" scope="request">
			<tr align="left">
				<td bgcolor="#ffffff" colspan="10">
					暂无用户信息
				</td>
			</tr>
		</logic:notPresent>
	</table>
	<table align="center" width="90%" border="0">
		<tr height="5">
			<td></td>
		</tr>
		<tr>
			
		</tr>
	</table>
	<html:form styleId="form_user" action="/system_mgr/viewOnlineUser"
		method="post">
		<input type="hidden" id="query_operatorId" name="operatorId">

		<input type="hidden" id="query_orgId" name="orgId"
			value="<bean:write name="QueryForm" property="orgId"/>">
		<input type="hidden" id="query_orgName" name="orgName"
			value="<bean:write name="QueryForm" property="orgName"/>">
	</html:form>
	
</body>
</html:html>
