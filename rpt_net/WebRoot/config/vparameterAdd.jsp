<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:useBean id="utilParameterForm" scope="page" class="com.fitech.net.form.UtilParameterForm" />

<html:html locale="true">
	<head>
	<html:base/>
    <title>参数表设定</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
  </head>  
  <body>
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>当前位置 >> 	参数设定 >> 参数表新增</td>
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
				
	<table border="0" width="90%" align="center" cellspacing="1" cellpadding="4" class="tbcolor">
		<TR class="tableHeader">
			<TD width="15%" valign="bottom">参数表设定</TD>
		</TR>
		<TR align="center" valign="middle" bgcolor="#FFFFFF">
			<TD>
				<html:form action="/config/InsertVParam" method="post" onsubmit="return _submit(form)">
					<table border="0" width="100%" align="center" cellspacing="1" cellpadding="4">
						<TR>
							<TD align="left">库表英文名：&nbsp;<html:text property="vpTableid" size="20" styleClass="input-text" maxlength="20"/></TD>
						    <TD align="left">库表中文名：<html:text property="vpTabledesc" size="20" styleClass="input-text" maxlength="20"/></TD>
						    <TD align="left">库表类型：
						      <html:select styleId="vttId" property="vttId">
						        <html:option value="1">事实表</html:option>
						        <html:option value="2">维度表</html:option>
						        <html:option value="3">指标表</html:option>
						      </html:select>
							</TD>
						</TR>
				        <TR>
				            <TD align="left">字段英文名：&nbsp;<html:text property="vpColumnid" size="20" styleClass="input-text" maxlength="20"/></TD>
							<TD align="left">字段中文名：<html:text property="vpColumndesc" size="20" styleClass="input-text" maxlength="20"/></TD>
							<TD align="left">字段类型：
							  <html:select styleId="vpColtype" property="vpColtype">
						        <html:option value="0">数据</html:option>
						        <html:option value="1">字符</html:option>
						      </html:select>
							</TD>
						</TR>
						<TR>
					        <TD align="left" colspan="1">&nbsp;&nbsp;上级字段：
						      <html:select styleId="pre_vpId" property="pre_vpId">
						        <html:option value="-1">----选择关联上级字段----</html:option>
						        <html:optionsCollection name="utilParameterForm" property="params"/>
						      </html:select>
					        </TD>
					        <TD align="left" colspan="2">
					        	&nbsp;&nbsp;备注信息：<html:text property="vpNote" size="60" styleClass="input-text" maxlength="50"/>
					        </TD>					  
						</TR>
						<TR>
					        <TD align="right" colspan="3">
				              <html:submit styleClass="input-button"  value="新  增"/>
				            </TD>
						</TR>
					</table>
				</html:form>	
			</TD>
		</TR>
	</table>
	<SCRIPT language="javascript">
		function _submit(form){
			if(form.vpTableid.value.Trim()==""){
				alert("请输入实际库表名！");
				form.vpTableid.focus();
				return false;
			}
			if(form.vpTabledesc.value.Trim()==""){
				alert("请输入库表描述名！");
				form.vpTabledesc.focus();
				return false;
			}
			if(form.vpColumnid.value=="-1"){
				alert("请输入实际字段名！");
				form.vpColumnid.focus();
				return false;
			}
			if(form.vpColumndesc.value=="-1"){
				alert("请输入字段描述名！");
				form.vpColumndesc.focus();
				return false;
			}
		}
		String.prototype.Trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g,"");
		}	
	</SCRIPT>
	</body>	
</html:html>

