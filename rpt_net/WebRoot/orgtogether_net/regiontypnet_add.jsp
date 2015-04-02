<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:useBean id="utilForm" scope="page" class="com.fitech.net.common.UtilForm" />

<html:html locale="true">
	<head>
		<html:base/>
		<title>新增地区类型</title>
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
	<table border="0" width="80%" align="center">
		<tr>
			<td height="4"></td>
		</tr>
		<tr>
			<td>
				当前位置 >> 地区类型管理 >> 地区类型增加
			</td>
		</tr>
		<tr>
			<td height="4"></td>
		</tr>
	</table>
	
	<html:form action="/addRegionTypNet" method="post">
		<table cellSpacing="1" cellPadding="4" border="0" width="80%" class="tbcolor">
			<tr class="titletab">
		            <th align="center" colspan="10" height="20">
		            	新增地区类型
		            </th>
		      </tr>
		   
					
			 <tr>
			  
			  <td align="center" bgcolor="#ffffff" width="50%" >
			         地区类型Id:<html:select property="regionTypId">
					               <html:optionsCollection name="utilForm" property="regionTypId" label="label" value="value"/>
					           </html:select>
			  </td>
			         
			 		<!--<td align="center" bgcolor="#ffffff" width="50%">
			               	分类编号:<html:text property="regionTypId" styleClass="input-text"/>
			             </td> 	  			
			        --><td align="center" bgcolor="#ffffff" width="50%">
			            机构类型名称:<html:text property="regionTypNm"  styleClass="input-text"/>
			        </td>        			        
			    </tr>	
			    
			    <tr >
			            <td colspan="6" align="right" bgcolor="#ffffff">
			            	<div id=location>
			            	</div>
			            </td>
			            </tr>
			          <tr>
			            <td colspan="12" align="right" bgcolor="#ffffff">			            				
	           <html:submit value="保存" styleClass="input-button"/>&nbsp;
	           <html:button property="back" value="返回" styleClass="input-button" 
	           onclick="window.location.assign('../regionTypNet.do')"/>
				</td>
			</tr>
			    		    
			    
			  
		</table>
	</html:form>	
	</body>
</html:html>