<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
<title>地区信息管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<link href="../css/common.css" rel="stylesheet" type="text/css">		
</head>

<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	<br>
	<br>
	<table border="0" width="75%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		 <tr class="titletab">
		            <th align="left">
		            	子机构显示
		            </th>
		      </tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	 <table width="75%" border="0" cellpadding="6" cellspacing="1" class="tbcolor">
      
        <tr align="center" class="middle">
	          <td align="center" width="15%" >
	          	子机构层次
	          </td>
	          <td align="center" >
	          	子机构名称
	          </td>	          
         </tr>		  
		<logic:present name="Records" scope="request">
				<logic:iterate id="item" name="Records" indexId="index">
					 <tr align="center" >
			        <td bgcolor="#ffffff">
						<%=((Integer)index).intValue() + 1%>
						<FONT color="#0000ff">级子机构</FONT>
					</td>
					<logic:iterate id="zhangxinke" name="item" property="tempList">
			                <td bgcolor="#ffffff" align="left">
			                <bean:write name="zhangxinke" property="ortId" />				                
				            <bean:write name="zhangxinke" property="orgName"/>;   				                
			                </td>		
			        </logic:iterate>	                 			                			                
			         </tr>
				</logic:iterate>
		  </logic:present>
		  <logic:notPresent name="Records" scope="request">
				<tr align="left">
					<td bgcolor="#ffffff" colspan="6">
						暂无机构分类信息
					</td>
				</tr>
			</logic:notPresent>
						
    </table>
    <table width="75%" border="0">
		<tr>
		    <td colspan="4" align="right" bgcolor="#ffffff">

		       <input type="button" value="返回"  styleClass="input-button" onclick="history.go(-1);">

            </td>
             
        </tr>
	</table>
	</body>
</html:html>
