<%@ page language="java"   pageEncoding="GB2312"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
String orgId=request.getParameter("orgId");
				java.util.List list=com.fitech.net.adapter.StrutsOrgNetDelegate.selectLowerOrgList(orgId);
				request.setAttribute("Records",list);
				
				%>
		<table width="98%" border="0"  cellspacing="1" class="tbcolor">
			<tr>
			<logic:present name="Records" scope="request">
				<logic:iterate id="iterm" name="Records" >
					<tr bgcolor="#FFFFFF">
					<td align="center">
						<bean:write name="iterm" property="orgId" />
						</td>
						<td align="center">
						<bean:write name="iterm" property="orgName" />
						</td>
						<td align="center">
						<bean:write name="iterm" property="orgType.orgTypeName" />
						</td>
						<td align="center">
						<input type="button"   class="input-button" value="修改"  onclick="_edit('<bean:write name="iterm" property="orgId" />')">
						<input type="button"   class="input-button" value="删除" onclick="_delete('<bean:write name="iterm" property="orgId" />')">
						<input type="button"   class="input-button" value="报送范围" onclick="_range('<bean:write name="iterm" property="orgId" />')">
						</td>
						
					</tr>
				</logic:iterate>
			</logic:present>

			<logic:notPresent name="Records" scope="request">
				<tr bgcolor="#FFFFFF">
					<td colspan="10">
						暂无机构信息
					</td>
				</tr>
			</logic:notPresent>

		</table>

