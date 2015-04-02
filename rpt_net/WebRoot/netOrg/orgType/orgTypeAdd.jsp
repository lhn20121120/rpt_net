<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:useBean id="utilOrgLayerForm" scope="page" class="com.fitech.net.form.UtilOrgLayerForm" />

<html:html locale="true">
	<head>
	<html:base/>
    <title>机构类别设定</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../../css/common.css" type="text/css" rel="stylesheet">
  </head>  
  <SCRIPT language="javascript">
		function _submit(form){
			if(form.org_type_name.value.Trim()==""){
				alert("请输入机构类别名称！");
				form.org_type_name.focus();
				return false;
			}
			if(form.org_layer_id.value=="-1"){
				alert("请选择机构级别！");
				form.org_layer_id.focus();
				return false;
			}
		}
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}	    
	</SCRIPT>
  <body>
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>当前位置 >> 	机构管理 >> 机构类型新增</td>
		</tr>
		<tr><td height="10"></td></tr>
	</table>
	<logic:present name="Message" scope="request">
		<logic:greaterThan name="Message" property="size" value="0">
			<script language="javascript">
				alert("<bean:write name='Message' property='alertMsg'/>");
			</script>
		</logic:greaterThan>
	</logic:present>
	
		<html:form action="/insertOrgType" method="post" onsubmit="return _submit(this)">
				<table border="0" width="50%" align="center" cellspacing="1" cellpadding="4" class="tbcolor">
					<TR class="tableHeader" >
						<TD width="15%" valign="bottom">
						机构类别设定
						</TD>
					</TR>
					
					<TR align="center" valign="middle" bgcolor="#FFFFFF">
					  <TD>
						<table border="0" width="100%" align="center" cellspacing="1" cellpadding="4">
						  <TR>
						    <TD align="right">机构类别名称：</TD>
						    <TD><html:text property="org_type_name" size="20" styleClass="input-text" maxlength="20"/></TD>
				          </TR>
				          <TR>
				          	<TD align="right">机构级别：</TD>
							<TD>
								<html:select property="org_layer_id">
									<html:option value="-1">----请选择机构级别----</html:option>
									<html:optionsCollection name="utilOrgLayerForm" property="orgLayers"/>
								</html:select>
							</TD>
				          </TR>
					      <TR>
					        <TD align="center" colspan="2">
				               <html:submit styleClass="input-button"  value="新增"/>
				            </TD>
				          </TR>
				        </table>
				      </TD>
					</TR>
				</table>
		</html:form>	
	</body>
</html:html>

