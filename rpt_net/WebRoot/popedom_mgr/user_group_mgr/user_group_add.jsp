<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html locale="true">
<head>
	<html:base/>
		<title>�û�����Ϣ���</title>
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
					alert("�������û�������ƣ�");
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
			 <td>��ǰλ�� >> Ȩ�޹��� >>�û�����Ϣ���</td>
		</tr>
		<tr>
			<td height="5"></td>
		</tr>
	</table>
    <br>
		 
	    	<table width="80%" border="0" align="center" cellpadding="4" cellspacing="1" class="tbcolor">
		      <tr class="titletab">
		            <th align="center">
		            	�û�����Ϣ���
		            </th>
		      </tr>
		      <tr>
		      	<td bgcolor="#ffffff">
		      		<table width="100%" border="0" align="center" cellpadding="4" cellspacing="1">
				   <html:form action="/popedom_mgr/insertMUserGrp" method="post" onsubmit="return validate()">
				 
			          
			          <tr>
			         	 <td align="center" bgcolor="#ffffff">
			               	�û�������:<html:text property="userGrpNm" styleClass="input-text" maxlength="40"/>
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
			            	<html:submit value="����" styleClass="input-button"/>&nbsp;
			            	<html:button property="back" value="����" styleClass="input-button" onclick="javascript:_goBack()"/>
			            </td>
			            </tr>
			         </table>
		      	</td>
		      </tr>
		      </table>
		      
	    </html:form>
		
	</body>
</html:html> 
