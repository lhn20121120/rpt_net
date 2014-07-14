<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.cbrc.smis.common.Config"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<logic:notEmpty name="traceList">
<logic:iterate id="afDataTrace" name="traceList">
		<logic:equal value="1" name="afDataTrace" property="status">
			<tr style="background-color: #c8c8c8">
		</logic:equal>
		<logic:notEqual value="1" name="afDataTrace" property="status">
			<tr onmouseover="this.style.backgroundColor='#EEEEFF'" onmouseout="this.style.backgroundColor='white'">
		</logic:notEqual>
			<td><bean:write name="afDataTrace" property="username"/></td>
			<td><bean:write name="afDataTrace" property="dateTime"/></td>
			<td><bean:write name="afDataTrace" property="originalData"/></td>
			<td><bean:write name="afDataTrace" property="changeData"/></td>
			<td><bean:write name="afDataTrace" property="finalData"/></td>
			<!-- 
			<logic:notEqual value="1" name="afDataTrace" property="status">
				<td colspan="<%=Config.ISHAVEDELETE?"1":"2" %>">
					<bean:write name="afDataTrace" property="descTrace"/>
				</td>
			</logic:notEqual>
			<logic:equal value="1" name="afDataTrace" property="status">
				<td colspan="2">
					<bean:write name="afDataTrace" property="descTrace"/>
				</td>
			</logic:equal>
			 -->
			<td>
				<bean:write name="afDataTrace" property="descTrace"/>
			</td>
			<logic:notEqual value="1" name="afDataTrace" property="status">
				<%if(Config.ISHAVEDELETE){ %>
					<td><img src="<%=request.getContextPath()%>/image/check_error.gif" alt="删除" onclick="this.parentNode.parentNode.style.backgroundColor='#c8c8c8';removeTrace(<bean:write name="afDataTrace" property="traceId"/>,<bean:write name="afDataTrace" property="changeData"/>,1)" style="cursor: hand" /></td>
				<% }%>
			</logic:notEqual>
			<logic:equal value="1" name="afDataTrace" property="status">
				<%if(Config.ISHAVEDELETE){ %>
					<td><img src='<%=request.getContextPath() %>/image/check_right.gif' alt="恢复" onclick="this.parentNode.parentNode.style.backgroundColor='#c8c8c8';removeTrace(<bean:write name="afDataTrace" property="traceId"/>,<bean:write name="afDataTrace" property="changeData"/>,0)" style="cursor: hand" /></td>
				<% }%>
			</logic:equal>
		</tr>
</logic:iterate>
</logic:notEmpty>
