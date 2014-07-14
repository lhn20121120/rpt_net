<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
        <head>	
	    <title>模板列表</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=gbk">
	    <meta http-equiv="Pragma" content="no-cache">
	    <meta http-equiv="Cache-Control" content="no-cache">
	    <meta http-equiv="Expires" content="0">
	    <LINK REL="StyleSheet" HREF="<bean:write name="css"/>" TYPE="text/css">
	</head>
	<body>	
           <table width="95%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
              <tr>
      	         <td bgcolor="#ffffff">
      		    <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
      		       <tr class="titletab">
				<th colspan="4" align="left" id="list">
					<strong>
						报表模板信息
					</strong>
				</th>
			</tr>
			<TR class="middle">
				<TD class="tableHeader" width="5%">
					<b>编号</b>
				</TD>
				<TD class="tableHeader" width="30%">
					<b>报表模板名称</b>
				</TD>
				<TD class="tableHeader" width="10%">
					<b>版本号</b>
				</TD>
				<TD class="tableHeader" width="20%">
					<b>取数模式</b>
				</TD>
												
			</TR>
			<tr bgcolor="#FFFFFF">
			   <td height="5" align="center">
			      <bean:write name="to" property="repID"/>
			   </td>	
			   <td height="5" align="left">
			         &nbsp;
			      <bean:write name="to" property="excelName"/>
			   </td>
			   <td height="5" align="center">
			      <bean:write name="to" property="versionID"/>
			   </td>	
			   <td height="5" align="center">
			      <bean:write name="to" property="mode"/>			      
			   </td>			   
			</tr>  
      		       <tr class="titletab">
				<th colspan="4" align="left" id="list">
					<strong>
						数据源信息
					</strong>
				</th>
			</tr>			
			<tr>
			   <td colspan="4">
			      <table width="100%">
				<TR class="middle">
					<TD class="tableHeader" width="30%">
						<b>对应Sheet名称</b>
					</TD>
					<TD class="tableHeader" width="30%">
						<b>对应数据源名称</b>
					</TD>
					<TD class="tableHeader" width="10%">
						<b>状态</b>
					</TD>
				</TR>
				<logic:iterate id="element" indexId="ind" name="dsoMap">
				<tr bgcolor="#FFFFFF">
				   <td height="5" align="left">
				         &nbsp;
				      <bean:write name="element" property="value.sheetName"/>
				   </td>
				   <td height="5" align="center">				      
				      <bean:write name="element" property="value.xlsName"/>
				   </td>
				   <td height="5" align="center">				      
				      <bean:write name="element" property="value.state"/>
				   </td>				   
				   	
				</tr>
				</logic:iterate>	
			      </table>
			   </td>
			</tr>
			<tr>
			   <td colspan="4" align="center">
			      <input type="button" name="butBuild" value="  数据生成  " 
			          onclick="window.location.href='templateView.do?guid=<bean:write name="to" property="guid"/>&excelName=<bean:write name="to" property="excelName"/>&repID=<bean:write name="to" property="repID"/>&versisonID=<bean:write name="to" property="versionID"/>';">
			      &nbsp;&nbsp;&nbsp;&nbsp;
			      <input type="button" name="butBack"  value="  返    回  " onclick="returnEnter()">
			   </td>
			</tr>
      		    </table>
      		 </td>
      	      </tr>    
           </table>
	</body>  
	<script language="javascript">
		function returnEnter(){
			window.location="<%=request.getContextPath()%>/viewTemplateNet.do"; 
		}
	</script>    
</html:html>