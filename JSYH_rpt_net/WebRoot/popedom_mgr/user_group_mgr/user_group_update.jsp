<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.cbrc.smis.util.FitechUtil" %> 

<%
  request.setCharacterEncoding("GB2312");
  response.setCharacterEncoding("GB2312");
 %>

<html:html locale="true">
<head>
	<html:base/>
		<title>用户组信息修改</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
		<link href="../../css/common.css" rel="stylesheet" type="text/css">
		<script language="javascript">
			function validate()
			{
				var txtUserGrpName = document.getElementById('userGrpNm');
				if(txtUserGrpName.value.Trim()=="")
				{
					alert("请输入新用户组的名称！");
					txtUserGrpName.focus();
					return false;
				}
				else
					return true;
			}
			function _goBack(){
				window.location="<%=request.getContextPath()%>/popedom_mgr/viewMUserGrp.do";
			}
			String.prototype.Trim=function(){
				return this.replace(/(^\s*)|(\s*$)/g,"");
			}	
		</script>
	</head>
	<body>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	 <table cellspacing="0" cellpadding="0" border="0" width="98%">
		<tr>
			<td height="5"></td>
		</tr>
		<tr>
			 <td>当前位置 >> 权限管理 >>用户组信息修改</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
	<br>
 
		
	    	<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		      <tr class="titletab">
		            <th align="center">
		            	用户组信息修改
		            </th>
		      </tr>
		      <tr>
		      	<td bgcolor="#ffffff">
		      	    <html:form action="/popedom_mgr/updateMUserGrp" method="post" onsubmit=" return validate()">
			      	  <table width="100%" border="0" align="center">
					    
			          <tr>
			         	 <td align="center" bgcolor="#ffffff">
			         	 
			         	 	<%
			         	 		//String roleName = FitechUtil.getParameter(request,"roleName");
								String userGrpNm  = request.getParameter("userGrpNm");
								
											if (com.cbrc.smis.common.Config.WEB_SERVER_TYPE == 1) {
											userGrpNm = new String(userGrpNm.getBytes("iso-8859-1"), "gb2312");
										}
																								
			         	 	%>

			               	原用户组名称:&nbsp;<input type ="text" class="input-text" name="oldUserGrpName" value="<%=userGrpNm%>" size="20" readonly>
			             </td> 
			             
			          </tr>
				
			          <tr>
				         	 <td align="center" bgcolor="#ffffff">
				         	 	<bean:parameter id="UserGrpId" name="userGrpId"/>
				         	 	<input type="hidden" name="userGrpId" value="<bean:write name="UserGrpId"/>">
				               	新用户组名称:&nbsp;<html:text styleId="userGrpNm" property="userGrpNm" styleClass="input-text" size="20"  maxlength="40"/>
				             </td>          
				          </tr>
				          <tr >
				            <td colspan="4" align="right" bgcolor="#ffffff">
				            	<div id=location>
				            	</div>
				            </td>
				            </tr>
				          <tr>
				            <td colspan="4" align="right" bgcolor="#ffffff">
				            	<html:submit value="保存" styleClass="input-button"/>&nbsp;
				            	<html:button property="back" value="返回" styleClass="input-button" onclick="javascript:_goBack()"/>
				            </td>
				            </tr>
				          </table>
				    </html:form>
		      	</td>
		      </tr>
		    </table>
		    
		
	</body>
</html:html> 
