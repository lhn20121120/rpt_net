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
    	<tr class="titletab">
        	<td>
      			<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">	
      		    	<html:form action="/templateDataBuildList" method="Post" styleId="form1">			      
      		       		<tr>
      		          		<td align="right">模板名称：<html:text property="reportName" styleClass="input-text" size="28"/></td>
      		          		<td>版本号：<html:text property="versionId" styleClass="input-text" size="10"/></td>
      		          		<td><html:submit styleClass="input-button" value=" 查 询 "/></td>
      		       		</tr>
      		       </html:form>
      		    </table>
            </td>
        </tr>
              <tr>
      	         <td bgcolor="#ffffff">
      		    <table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">				      
      		       <tr class="titletab">
				<th colspan="5" align="center" id="list">
					<strong>
						报表模板列表
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
				<TD class="tableHeader" width="15%">
					<b>操作</b>
				</TD>									
			</TR>
			<logic:iterate id="omList" indexId="ind" name="omList">
			<tr bgcolor="#FFFFFF">
			   <td height="5" align="center">
			      <bean:write name="omList" property="repID"/>
			   </td>	
			   <td height="5" align="left">
			         &nbsp;
			      <bean:write name="omList" property="excelName"/>
			   </td>
			   <td height="5" align="center">
			      <bean:write name="omList" property="versionID"/>
			   </td>	
			   <td height="5" align="center">
			      <bean:write name="omList" property="mode"/>
			   </td>	
			   <td height="5" align="center">
			      <a href="templateDataBuildPre.do?repID=<bean:write name="omList" property="repID"/>&versisonID=<bean:write name="omList" property="versionID"/>">
			         数据生成
			      </a>

			   </td>
			</tr>
			</logic:iterate>	
      		    </table>
      		 </td>
      	      </tr>    
           </table>
	</body>    
</html:html>