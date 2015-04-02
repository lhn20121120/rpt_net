<%@ page language="java" pageEncoding="GB2312"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
	<html:base/>
    <title>指标类型添加</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <link href="../css/common.css" type="text/css" rel="stylesheet">
     <%
        //----------------------------------------------------
        // 2008-06-19 gongming
        // 验证用户输入是否合法。
     %>
    <script type="text/javascript" language="javascript">
      function inputValid()
      {
        var txt = document.forms[0]["businessName"].value;
        var pattern = /(\s*)/g;
        txt = txt.replace(pattern,"");
        if( 0 == txt.length)
        {
          alert('指标类型名称未输入');
          return false;
        }
        return true;
      }  
      
      function trimTxt()
      {   
        var txt_name = document.forms[0]["businessName"];
        var txt_desc = document.forms[0]["businessNote"];
        var pattern = /(\s*)/g;
        var value_name = txt_name.value.replace(pattern,"");
        txt_name.value = value_name;
        var value_desc = txt_desc.value.replace(pattern,"");
        txt_desc.value= value_desc;
      }
    </script>
  </head>  
  <body onload="trimTxt()">
	<table border="0" width="90%" align="center">
		<tr><td height="8"></td></tr>
		<tr>
			 <td>当前位置 >> 	指标分析 >> 指标类型添加</td>
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
		<html:form action="/target/businessAdd.do" method="post" onsubmit="return inputValid()">
				<table border="0" width="50%" align="center" cellspacing="1" cellpadding="4" class="tbcolor">
					<TR class="tableHeader" >
						<TD width="15%" valign="bottom">
						指标类型设定
						</TD>
					</TR>
					
					<TR align="center" valign="middle" bgcolor="#FFFFFF">
					  <TD>
						<table border="0" width="100%" align="center" cellspacing="1" cellpadding="4">
						  <TR>
						    <TD align="center">指标类型名称：<html:text property="businessName" size="20" styleClass="input-text" maxlength="9"/>
						    </TD>
				          </TR>
<%--				          <TR>--%>
<%--						    <TD align="center">指标类型备注：<html:text property="businessNote" size="20" styleClass="input-text" maxlength="9"/>--%>
<%--						    </TD>--%>
<%--				          </TR>--%>
					           <TR>
					             <TD align="center">
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

